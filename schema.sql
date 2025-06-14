
DROP TABLE IF EXISTS Wypozyczenia CASCADE;
DROP TABLE IF EXISTS Narzedzia CASCADE;
DROP TABLE IF EXISTS KategorieNarzędzi CASCADE;
DROP TABLE IF EXISTS Uzytkownicy CASCADE;
DROP TABLE IF EXISTS LogiDostepnosciNarzędzi CASCADE;

CREATE TABLE Uzytkownicy (
    id_uzytkownika SERIAL PRIMARY KEY,
    imie VARCHAR(100) NOT NULL,
    nazwisko VARCHAR(100) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    haslo_hash VARCHAR(255) NOT NULL,
    rola VARCHAR(50) NOT NULL DEFAULT 'user',
    data_rejestracji TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE KategorieNarzędzi (
    id_kategorii SERIAL PRIMARY KEY,
    nazwa_kategorii VARCHAR(100) NOT NULL UNIQUE,
    opis_kategorii TEXT
);

CREATE TABLE Narzedzia (
    id_narzedzia SERIAL PRIMARY KEY,
    nazwa_narzedzia VARCHAR(255) NOT NULL,
    opis_narzedzia TEXT,
    id_kategorii INTEGER NOT NULL REFERENCES KategorieNarzędzi(id_kategorii),
    cena_za_dobe DECIMAL(10, 2) NOT NULL,
    dostepnosc BOOLEAN NOT NULL DEFAULT TRUE,
    zdjecie_url VARCHAR(500)
);


CREATE TABLE Wypozyczenia (
    id_wypozyczenia SERIAL PRIMARY KEY,
    id_uzytkownika INTEGER NOT NULL REFERENCES Uzytkownicy(id_uzytkownika),
    id_narzedzia INTEGER NOT NULL REFERENCES Narzedzia(id_narzedzia),
    data_wypozyczenia TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    data_planowanego_zwrotu DATE NOT NULL,
    data_rzeczywistego_zwrotu TIMESTAMP WITH TIME ZONE,
    status_wypozyczenia VARCHAR(50) NOT NULL DEFAULT 'aktywne',
    calkowity_koszt DECIMAL(10, 2)
);


CREATE TABLE LogiDostepnosciNarzędzi (
    id_logu SERIAL PRIMARY KEY,
    id_narzedzia INTEGER NOT NULL REFERENCES Narzedzia(id_narzedzia),
    poprzednia_dostepnosc BOOLEAN,
    nowa_dostepnosc BOOLEAN NOT NULL,
    czas_zmiany TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    kto_zmienil VARCHAR(255)
);


DROP VIEW IF EXISTS AktywneWypozyczeniaInfo;


CREATE VIEW AktywneWypozyczeniaInfo AS
SELECT 
    w.id_wypozyczenia,
    n.nazwa_narzedzia,
    n.id_narzedzia,
    u.imie AS uzytkownik_imie,
    u.nazwisko AS uzytkownik_nazwisko,
    u.email AS uzytkownik_email,
    w.data_wypozyczenia,
    w.data_planowanego_zwrotu,
    w.status_wypozyczenia
FROM 
    Wypozyczenia w
JOIN 
    Narzedzia n ON w.id_narzedzia = n.id_narzedzia
JOIN 
    Uzytkownicy u ON w.id_uzytkownika = u.id_uzytkownika
WHERE 
    w.status_wypozyczenia = 'aktywne'
ORDER BY
    w.data_planowanego_zwrotu ASC;

DROP FUNCTION IF EXISTS ObliczDniWypozyczenia(timestamp with time zone, timestamp with time zone);
DROP FUNCTION IF EXISTS ObliczDniWypozyczenia(timestamp with time zone);


CREATE OR REPLACE FUNCTION ObliczDniWypozyczenia(
    data_start timestamp with time zone,
    data_koniec timestamp with time zone
)
RETURNS integer AS $$
DECLARE
    dni_wypozyczenia integer;
BEGIN
    IF data_koniec IS NULL OR data_koniec < data_start THEN
        RETURN 0; 
    END IF;
    SELECT CEIL(EXTRACT(EPOCH FROM (data_koniec - data_start)) / 86400.0) INTO dni_wypozyczenia;
    IF dni_wypozyczenia = 0 AND (data_koniec >= data_start) THEN
      IF EXTRACT(EPOCH FROM (data_koniec - data_start)) > 0 THEN
        RETURN 1;
      ELSE
        RETURN 1; 
      END IF;
    END IF;
    IF dni_wypozyczenia < 1 AND (data_koniec >= data_start) THEN
        RETURN 1;
    END IF;
    RETURN dni_wypozyczenia;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION ObliczDniWypozyczenia(
    data_start timestamp with time zone
)
RETURNS integer AS $$
BEGIN
    RETURN ObliczDniWypozyczenia(data_start, CURRENT_TIMESTAMP);
END;
$$ LANGUAGE plpgsql;



DROP FUNCTION IF EXISTS LogujZmianeDostepnosciNarzędzia CASCADE;


CREATE OR REPLACE FUNCTION LogujZmianeDostepnosciNarzędzia()
RETURNS TRIGGER AS $$
DECLARE
    kto_zmienil_info TEXT;
BEGIN

    BEGIN
        kto_zmienil_info := current_setting('app.user_id', true);
        IF kto_zmienil_info IS NULL THEN
            kto_zmienil_info := CURRENT_USER;
        END IF;
    EXCEPTION WHEN OTHERS THEN
        kto_zmienil_info := 'Nieznany (błąd pobierania app.user_id) lub ' || CURRENT_USER;
    END;



    IF TG_OP = 'UPDATE' THEN
        IF OLD.dostepnosc IS DISTINCT FROM NEW.dostepnosc THEN
            INSERT INTO LogiDostepnosciNarzędzi (id_narzedzia, poprzednia_dostepnosc, nowa_dostepnosc, kto_zmienil)
            VALUES (NEW.id_narzedzia, OLD.dostepnosc, NEW.dostepnosc, kto_zmienil_info);
        END IF;
    ELSIF TG_OP = 'INSERT' THEN

        INSERT INTO LogiDostepnosciNarzędzi (id_narzedzia, poprzednia_dostepnosc, nowa_dostepnosc, kto_zmienil)
        VALUES (NEW.id_narzedzia, NULL, NEW.dostepnosc, kto_zmienil_info);
    END IF;
    
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;


DROP TRIGGER IF EXISTS Trigger_PoZmianieDostepnosciNarzędzia ON Narzedzia;


CREATE TRIGGER Trigger_PoZmianieDostepnosciNarzędzia
AFTER INSERT OR UPDATE OF dostepnosc ON Narzedzia
FOR EACH ROW
EXECUTE FUNCTION LogujZmianeDostepnosciNarzędzia();
