# Wypożyczalnia Narzędzi - "Toolsy"


1.  **Aplikacja jest dostępna pod adresem:**

    - **Strona główna:** [http://localhost:8080](http://localhost:8080)
    - **pgAdmin (zarządzanie bazą):** [http://localhost:5050](http://localhost:5050)

2.  **Dane logowania:**
    - **Admin:** `admin@admin.pl` / `admin1`
    - **User:** `michal@wp.pl` / `michal`

## Opis Projektu

"Toolsy" to projekt aplikacji internetowej typu wypożyczalnia narzędzi. Aplikacja umożliwia przeglądanie dostępnych narzędzi, rejestrację i logowanie użytkowników, a także zarządzanie katalogiem narzędzi i kategoriami przez administratora. Zalogowani użytkownicy mogą wypożyczać narzędzia oraz zarządzać swoimi wypożyczeniami (w tym dokonywać zwrotów).

Celem projektu było stworzenie prostej, ale funkcjonalnej aplikacji webowej bez użycia frameworków PHP po stronie backendu, z wykorzystaniem czystego PHP, HTML, CSS i JavaScript, oraz bazy danych PostgreSQL. Aplikacja jest skonteneryzowana przy użyciu Docker.

## Autor

- Michał Chwastek

## Technologie i Narzędzia

- **Backend:** PHP
- **Frontend:** HTML, CSS, JavaScript
- **Baza Danych:** PostgreSQL
- **Serwer WWW:** Nginx
- **Konteneryzacja:** Docker, Docker Compose
- **Zarządzanie Bazą Danych (narzędzie):** pgAdmin 4
- **Projektowanie UI (wstępne):** Figma
- **System Kontroli Wersji:** Git, GitHub

## Funkcjonalności

### Główne Moduły:

1.  **Autentykacja Użytkowników:**
    - Rejestracja nowych użytkowników.
    - Logowanie istniejących użytkowników.
    - Wylogowywanie.
    - Zarządzanie sesją użytkownika.
2.  **Role Użytkowników:**
    - **User (Użytkownik):** Może przeglądać narzędzia, wypożyczać je, przeglądać swoje wypożyczenia i zwracać narzędzia.
    - **Admin (Administrator):** Posiada uprawnienia użytkownika oraz dodatkowo może zarządzać katalogiem kategorii i narzędzi.
3.  **Katalog Narzędzi (Publiczny):**
    - Wyświetlanie listy wszystkich dostępnych narzędzi wraz z podstawowymi informacjami (nazwa, kategoria, cena, dostępność, zdjęcie).
    - Wyświetlanie szczegółowych informacji o wybranym narzędziu.
4.  **System Wypożyczeń:**
    - Możliwość wypożyczenia dostępnego narzędzia przez zalogowanego użytkownika (wybór daty zwrotu).
    - Automatyczna zmiana statusu dostępności narzędzia po wypożyczeniu.
    - Zapisywanie informacji o wypożyczeniu w bazie danych.
    - Panel "Moje Wypożyczenia" dla zalogowanego użytkownika z listą jego wypożyczeń (aktywnych i historycznych).
    - Możliwość "zwrotu" narzędzia przez użytkownika.
    - Automatyczna aktualizacja statusu wypożyczenia, daty zwrotu, obliczenie kosztu i zmiana dostępności narzędzia.
5.  **Panel Administratora:**
    - Zarządzanie kategoriami narzędzi (dodawanie, wyświetlanie, edycja, usuwanie).
    - Zarządzanie narzędziami (dodawanie, wyświetlanie, edycja, usuwanie, przypisywanie do kategorii, ustawianie ceny, dostępności, URL zdjęcia).
    - Dostęp do panelu admina ograniczony tylko dla użytkowników z rolą "admin".

### Architektura i Rozwiązania Techniczne:

- Prosty routing oparty o parametr `action` w URL.
- Struktura kodu backendu częściowo oparta o wzorzec Model-Widok-Kontroler (MVC) w uproszczonej formie (Modele dla operacji bazodanowych, Kontrolery dla logiki biznesowej, Widoki dla prezentacji HTML).
- Połączenie z bazą danych przy użyciu PDO.
- Stosowanie Prepared Statements w zapytaniach SQL w celu ochrony przed SQL Injection.
- Haszowanie haseł użytkowników (funkcje `password_hash()` i `password_verify()`).
- Obsługa sesji PHP.
- Transakcje bazodanowe przy operacjach modyfikujących wiele tabel (np. proces wypożyczenia/zwrotu).


## Opis Funkcjonalności Aplikacji:

### 2.1    **Autentykacja Użytkowników**

Umożliwia nowym użytkownikom założenie konta oraz logowanie dla już zarejestrowanych. Proces obejmuje:

**Rejestracja:** Formularz zbierający imię, nazwisko, email oraz hasło (z potwierdzeniem). Hasła są bezpiecznie hashowane przed zapisem do bazy. Zaimplementowano dynamiczne sprawdzanie dostępności adresu email po stronie klienta (JavaScript/Fetch API) oraz walidację po stronie serwera.

![image](https://github.com/user-attachments/assets/0323c702-b996-48dc-b22c-5b7be9bf2937)


**Logowanie:** Formularz email i hasła. Aplikacja weryfikuje dane z bazą i w przypadku sukcesu tworzy sesję użytkownika.

![image](https://github.com/user-attachments/assets/2490165e-198b-4f03-a7f4-a8163d1b58e6)


**Wylogowywanie:** Bezpieczne zakończenie sesji użytkownika.

![image](https://github.com/user-attachments/assets/ca066e65-e4c3-42cf-87e2-87ad2ed61e0b)


### 2.2    **Role Użytkowników**

System rozróżnia dwie role:

●	**Użytkownik (user):** Może przeglądać narzędzia, wypożyczać je, przeglądać swoje aktywne i historyczne wypożyczenia oraz dokonywać zwrotów.

●	**Administrator (admin):** Posiada wszystkie uprawnienia zwykłego użytkownika oraz dostęp do panelu administracyjnego, gdzie może zarządzać kategoriami narzędzi i samymi narzędziami.


### 2.3    **Publiczny katalog narzędzi**

Dostępny dla wszystkich odwiedzających, umożliwia:

●	**Przeglądanie listy narzędzi:** Narzędzia wyświetlane są w formie kart, prezentując zdjęcie (lub placeholder), nazwę, kategorię, cenę za dobę oraz aktualną dostępność. Zaimplementowano filtrowanie narzędzi po kategoriach. 

![image](https://github.com/user-attachments/assets/5263a2c2-ff5e-49f2-9058-8cbc3ebd7aea)


●	**Szczegóły narzędzia:** Po kliknięciu na narzędzie użytkownik widzi jego pełny opis, większe zdjęcie oraz, jeśli jest zalogowany i narzędzie jest dostępne, opcję wypożyczenia. 

![image](https://github.com/user-attachments/assets/b6b2112f-5bfb-46a4-ade2-c53ec82dda63)


### 2.4 System wypożyczeń

Dostępny dla zalogowanych użytkowników:

●	**Proces wypożyczenia:** Na stronie szczegółów narzędzia użytkownik wybiera planowaną datę zwrotu. Po potwierdzeniu, narzędzie zmienia status na "niedostępne", a informacja o wypożyczeniu (kto, co, od kiedy, do kiedy) jest zapisywana w bazie danych. Operacje te są objęte transakcją bazodanową. 

![image](https://github.com/user-attachments/assets/7133b79b-5484-4045-9adb-afd86663f531)


●	**Moje Wypożyczenia:** Zalogowany użytkownik ma dostęp do panelu, gdzie widzi listę swoich wypożyczeń (aktywnych i zakończonych) wraz ze szczegółami (nazwa narzędzia, daty, status, obliczony koszt po zwrocie). 

![image](https://github.com/user-attachments/assets/381ffe01-6dad-44c6-9efe-c0cd0811e76c)


●	**Zwrot narzędzia:** Użytkownik może "zwrócić" aktywne wypożyczenie. System zapisuje datę rzeczywistego zwrotu, oblicza całkowity koszt wypożyczenia i zmienia status narzędzia na "dostępne". Operacje te również są objęte transakcją.

![image](https://github.com/user-attachments/assets/a41b7691-b4ef-4a06-ab93-854332204181)


### 2.5 Panel Administratora

Dostępny tylko dla użytkowników z rolą "admin", umożliwia zarządzanie:

●	**Kategoriami Narzędzi:** Dodawanie nowych kategorii, przeglądanie listy, edycja istniejących oraz ich usuwanie. 

![image](https://github.com/user-attachments/assets/6179c7bd-c372-4040-9c37-dafe23ce82ee)


●	**Narzędziami:** Dodawanie nowych narzędzi (z przypisaniem do kategorii, ustawieniem ceny, dostępności, opisu, URL zdjęcia), przeglądanie listy, edycja istniejących oraz ich usuwanie.

![image](https://github.com/user-attachments/assets/542aeefb-56dc-4615-8ea2-90d62d16e1ee)


## 3. Architektura i użyte technologie

### 3.1 Struktura aplikacji

Aplikacja została zbudowana w oparciu o własny, prosty system routingu (parametr action w URL). Logika backendu została częściowo zorganizowana z użyciem uproszczonego wzorca MVC:

●	Modele (src/Models/): Klasy (User.php, Category.php, Tool.php, Rental.php) odpowiedzialne za interakcję z bazą danych (operacje CRUD, pobieranie danych). Wykorzystują PDO i prepared statements.

●	Widoki (views/): Pliki PHP zawierające kod HTML do prezentacji danych użytkownikowi.

●	Kontrolery (src/Controllers/): Klasy (AuthController.php, CategoryController.php, ToolController.php, RentalController.php) zarządzające logiką aplikacji, przetwarzające żądania, komunikujące się z modelami i wybierające odpowiednie widoki.

●	Konfiguracja (config/): Plik database.php z danymi połączenia do bazy.

●	Punkt wejściowy (public/index.php): Obsługuje wszystkie przychodzące żądania, inicjalizuje sesję, definiuje stałe, ładuje kontrolery i kieruje do odpowiednich akcji.

●	Zasoby publiczne (public/): Folder css/style.css dla stylów.



### 3.2 Baza Danych PostgreSQL

●	**Schemat:** Zaprojektowano relacyjną bazę danych składającą się z 5 tabel: Uzytkownicy, KategorieNarzędzi, Narzędzia, Wypozyczenia, LogiDostepnosciNarzędzi.


**Diagram ERD:**
![image](https://github.com/user-attachments/assets/fdaf3d0e-0dba-4a69-a793-3ba469b2741f)


**Zaawansowane funkcje:**

○	Widok SQL: Stworzono widok AktywneWypozyczeniaInfo do prezentacji aktywnych wypożyczeń.

○	Funkcja SQL: Stworzono funkcję ObliczDniWypozyczenia do kalkulacji długości wypożyczenia.

○	Wyzwalacz SQL: Zaimplementowano wyzwalacz Trigger_PoZmianieDostepnosciNarzędzia wraz z funkcją LogujZmianeDostepnosciNarzędzia do automatycznego logowania zmian statusu dostępności narzędzi w tabeli LogiDostepnosciNarzędzi.

○	Transakcje: Użyto transakcji bazodanowych w procesach wypożyczania i zwrotu narzędzi dla zapewnienia spójności danych. 

![image](https://github.com/user-attachments/assets/618999c7-7ded-4f04-8439-4af6f36dc440)

### 3.3 Konteneryzacja Docker

Aplikacja jest w pełni skonteneryzowana przy użyciu Docker i Docker Compose. Plik docker-compose.yml definiuje następujące serwisy:

●	php: Kontener z PHP-FPM (wersja 8.2.11-fpm-alpine3.18) i wymaganymi rozszerzeniami (np. pdo_pgsql).

●	nginx: Serwer WWW Nginx skonfigurowany do współpracy z PHP-FPM.

●	db: Serwer bazy danych PostgreSQL (wersja 15-alpine).

●	pgadmin: Narzędzie pgAdmin 4 do zarządzania bazą danych. Wolumeny Docker są używane do persystencji danych PostgreSQL i pgAdmin oraz do mapowania kodu aplikacji do kontenerów, co umożliwia dewelopment na żywo.

