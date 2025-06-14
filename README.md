# Wypożyczalnia Narzędzi - "Toolsy"


1.  **Aplikacja jest dostępna pod adresem:**

    - **Strona główna:** [http://localhost:8080](http://localhost:8080)
    - **pgAdmin (zarządzanie bazą):** [http://localhost:5050](http://localhost:5050)

2.  **Dane logowania (opcjonalnie):**
    Możesz podać przykładowe dane do logowania, aby ułatwić testowanie.
    - **Admin:** `admin@example.com` / `admin123`
    - **User:** `user@example.com` / `user123`

## Opis Projektu

"Toolsy" to projekt studencki aplikacji internetowej typu wypożyczalnia narzędzi. Aplikacja umożliwia przeglądanie dostępnych narzędzi, rejestrację i logowanie użytkowników, a także zarządzanie katalogiem narzędzi i kategoriami przez administratora. Zalogowani użytkownicy mogą wypożyczać narzędzia oraz zarządzać swoimi wypożyczeniami (w tym dokonywać zwrotów).

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


![image](https://github.com/user-attachments/assets/e6a2ab5a-d90c-417d-8f52-31f02b753cdc)

