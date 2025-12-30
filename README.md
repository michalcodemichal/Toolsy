# Toolsy - Wypożyczalnia Narzędzi

Aplikacja webowa do zarządzania wypożyczalnią narzędzi z pełnym systemem autoryzacji, zarządzania użytkownikami i narzędziami.

## Technologie

### Backend
- **Java 17** - język programowania
- **Spring Boot 3.2.0** - framework aplikacyjny
- **Spring Security** - bezpieczeństwo i autoryzacja
- **JWT** - tokeny autoryzacyjne
- **Spring Data JPA** - warstwa dostępu do danych
- **H2 Database** - baza danych (możliwość przejścia na PostgreSQL)
- **Maven** - zarządzanie zależnościami

### Frontend
- **React 18** - biblioteka UI
- **React Router** - routing
- **Axios** - komunikacja z API
- **Vite** - narzędzie buildowe
- **React Toastify** - powiadomienia

## Funkcjonalności

- ✅ Rejestracja i logowanie użytkowników
- ✅ System ról (USER, ADMIN)
- ✅ Przeglądanie katalogu narzędzi
- ✅ Wyszukiwanie i filtrowanie narzędzi
- ✅ Wypożyczanie narzędzi
- ✅ Zarządzanie wypożyczeniami
- ✅ Panel administracyjny
- ✅ Zarządzanie użytkownikami
- ✅ Zarządzanie narzędziami (CRUD)
- ✅ Responsywny design

## Wymagania

- Java 17 lub nowsza
- Node.js 18 lub nowsza
- Maven 3.6+

## Instalacja i uruchomienie

### Backend

1. Przejdź do katalogu backend:
```bash
cd backend
```

2. Zainstaluj zależności i uruchom aplikację:
```bash
mvn clean install
mvn spring-boot:run
```

Backend będzie dostępny pod adresem: `http://localhost:8080`

Dokumentacja API (Swagger) dostępna pod adresem: `http://localhost:8080/swagger-ui.html`

### Frontend

1. Przejdź do katalogu frontend:
```bash
cd frontend
```

2. Zainstaluj zależności:
```bash
npm install
```

3. Uruchom aplikację w trybie deweloperskim:
```bash
npm run dev
```

Frontend będzie dostępny pod adresem: `http://localhost:3000`

## Dane testowe

Aplikacja automatycznie inicjalizuje dane testowe przy pierwszym uruchomieniu:

- **Admin**: 
  - Username: `admin`
  - Password: `admin123`

- **Użytkownicy**: 
  - Username: `user1` do `user10`
  - Password: `user123`

- **Narzędzia**: 30 narzędzi w różnych kategoriach
- **Wypożyczenia**: 10 przykładowych wypożyczeń

## API Endpoints

### Autoryzacja
- `POST /api/auth/login` - Logowanie
- `POST /api/auth/register` - Rejestracja

### Narzędzia
- `GET /api/tools` - Lista wszystkich narzędzi
- `GET /api/tools/available` - Lista dostępnych narzędzi
- `GET /api/tools/{id}` - Szczegóły narzędzia
- `GET /api/tools/search?q={query}` - Wyszukiwanie narzędzi
- `GET /api/tools/category/{category}` - Narzędzia po kategorii
- `POST /api/tools` - Utworzenie narzędzia (ADMIN)
- `PUT /api/tools/{id}` - Aktualizacja narzędzia (ADMIN)
- `DELETE /api/tools/{id}` - Usunięcie narzędzia (ADMIN)

### Wypożyczenia
- `POST /api/rentals` - Utworzenie wypożyczenia
- `GET /api/rentals/my` - Moje wypożyczenia
- `GET /api/rentals/{id}` - Szczegóły wypożyczenia
- `GET /api/rentals/all` - Wszystkie wypożyczenia (ADMIN)
- `PUT /api/rentals/{id}/approve` - Zatwierdzenie wypożyczenia (ADMIN)
- `PUT /api/rentals/{id}/complete` - Zakończenie wypożyczenia (ADMIN)
- `PUT /api/rentals/{id}/cancel` - Anulowanie wypożyczenia

### Użytkownicy
- `GET /api/users/me` - Aktualny użytkownik
- `GET /api/users` - Lista użytkowników (ADMIN)
- `GET /api/users/{id}` - Szczegóły użytkownika (ADMIN)
- `PUT /api/users/{id}/activate` - Aktywacja użytkownika (ADMIN)
- `PUT /api/users/{id}/deactivate` - Dezaktywacja użytkownika (ADMIN)

## Struktura projektu

```
Toolsy/
├── backend/
│   ├── src/
│   │   └── main/
│   │       ├── java/com/toolsy/
│   │       │   ├── config/          # Konfiguracja
│   │       │   ├── controller/      # Kontrolery REST
│   │       │   ├── dto/             # Data Transfer Objects
│   │       │   ├── exception/       # Obsługa wyjątków
│   │       │   ├── model/           # Encje JPA
│   │       │   ├── repository/      # Repozytoria
│   │       │   ├── security/        # Konfiguracja bezpieczeństwa
│   │       │   └── service/         # Logika biznesowa
│   │       └── resources/
│   │           └── application.properties
│   └── pom.xml
├── frontend/
│   ├── src/
│   │   ├── components/              # Komponenty UI
│   │   ├── context/                 # Context API
│   │   ├── pages/                   # Strony aplikacji
│   │   ├── services/                # Serwisy API
│   │   └── App.jsx
│   ├── package.json
│   └── vite.config.js
└── README.md
```

## Architektura

Aplikacja wykorzystuje architekturę warstwową:

- **Warstwa prezentacji**: React komponenty i strony
- **Warstwa API**: REST kontrolery w Spring Boot
- **Warstwa biznesowa**: Serwisy z logiką biznesową
- **Warstwa danych**: Repozytoria JPA i encje

## Bezpieczeństwo

- Uwierzytelnianie oparte na JWT
- Autoryzacja oparta na rolach (USER, ADMIN)
- Hasła hashowane przy użyciu BCrypt
- CORS skonfigurowany dla frontendu
- Walidacja danych wejściowych

## Baza danych

Aplikacja używa H2 Database (in-memory) z automatyczną inicjalizacją danych. W pliku `application.properties` można zmienić konfigurację na PostgreSQL dla produkcji.

### Diagram ERD

```
┌─────────────┐         ┌─────────────┐         ┌─────────────┐
│    User     │         │    Tool     │         │   Rental    │
├─────────────┤         ├─────────────┤         ├─────────────┤
│ id (PK)     │         │ id (PK)     │         │ id (PK)     │
│ username    │         │ name        │         │ user_id(FK) │
│ email       │         │ description │         │ tool_id(FK) │
│ password    │         │ category    │         │ startDate   │
│ firstName   │         │ dailyPrice  │         │ endDate     │
│ lastName    │         │ quantity    │         │ totalPrice  │
│ phoneNumber │         │ imageUrl    │         │ status      │
│ role        │         │ status      │         │ notes       │
│ active      │         │ createdAt   │         │ createdAt   │
│ createdAt   │         │ updatedAt   │         │ updatedAt   │
│ updatedAt   │         │             │         │ returnedAt  │
└─────────────┘         └─────────────┘         └─────────────┘
      │                        │                        │
      │                        │                        │
      └────────────────────────┼────────────────────────┘
                               │
                        (One-to-Many)
```

Relacje:
- User (1) ──< Rental (Many)
- Tool (1) ──< Rental (Many)

## Licencja

Projekt edukacyjny.
