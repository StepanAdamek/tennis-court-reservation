# Tennis Court Reservation System

![CI](https://github.com/StepanAdamek/tennis-court-reservation/actions/workflows/ci.yml/badge.svg)

## Přehled projektu

Tato aplikace je REST API vytvořené pomocí Spring Boot pro správu rezervací tenisových kurtů.

Projekt byl zaměřen na:

* vývoj řízený testy (TDD),
* práci s Git workflow,
* automatizované CI/CD pipeline,
* Docker kontejnerizaci,
* Kubernetes deployment,
* základní DevOps procesy.

---

# Technologie

## Backend

* Java 21
* Spring Boot
* Spring Web
* Spring Data JPA
* Hibernate

## Databáze

* PostgreSQL

## Testování

* JUnit 5
* Mockito
* Spring Boot Test

## Build nástroje

* Maven

## DevOps

* Docker
* Docker Compose
* Kubernetes (Docker Desktop)
* GitHub Actions (CI/CD)

## Monitoring

* Spring Boot Actuator

---

# Doména aplikace

Aplikace umožňuje:

* zobrazit dostupné kurty,
* vytvářet rezervace,
* zabránit kolizím rezervací,
* spravovat uživatele a kurty.

---

# REST API Endpoints

## Users

* GET `/api/users`
* POST `/api/users`
* DELETE `/api/users/{id}`

## Courts

* GET `/api/courts`
* POST `/api/courts`
* DELETE `/api/courts/{id}`

## Reservations

* GET `/api/reservations`
* POST `/api/reservations`
* PUT `/api/reservations/{id}/cancel`

---

# Hlavní entity

* User
* Court
* Reservation

---

# Business pravidla

* rezervace se nesmí překrývat na stejném kurtu,
* rezervace musí být v budoucnosti,
* konec rezervace musí být po začátku rezervace,
* uživatel může mít maximálně 3 aktivní budoucí rezervace,
* rezervaci lze zrušit pouze více než 24 hodin před začátkem,
* večerní rezervace mají vyšší cenu,
* uživatel i kurt musí existovat.

---

# Architektura

Aplikace používá vrstvenou architekturu.

## Diagram komponent

```text
[Client]
    ↓
[Controller]
    ↓
[Service]
    ↓
[Repository]
    ↓
[Database]
```

## Popis vrstev

### Controller

REST API endpointy.

### Service

Obsahuje business logiku aplikace.

### Repository

Databázová vrstva pomocí Spring Data JPA.

### Model / DTO

Datové entity a přenosové objekty.

---

# Tok dat

```text
Request
   ↓
Controller
   ↓
Service
   ↓
Repository
   ↓
Database
   ↓
Response
```

---

# Struktura projektu

```text
src/
├── main/
│   ├── java/cz/school/tenniscourtreservation/
│   │   ├── controller/
│   │   ├── service/
│   │   ├── repository/
│   │   ├── model/
│   │   ├── dto/
│   │   ├── exception/
│   │   └── config/
│   │
│   └── resources/
│       └── application.properties
│
├── test/
│   ├── controller/
│   ├── service/
│   └── integration/
│
k8s/
├── staging/
└── production/

.github/workflows/

Dockerfile
docker-compose.yml
pom.xml
```

---

# Testovací strategie

## Controller testy

Controller testy ověřují REST API vrstvu aplikace.

Testují například:

* HTTP status kódy,
* JSON response,
* serializaci/deserializaci dat,
* správné volání service vrstvy.

Použité technologie:

* MockMvc
* Mockito
* JUnit 5

---

## Service testy

Service testy ověřují business logiku aplikace izolovaně bez databáze.

Testují například:

* validaci času rezervace,
* kontrolu překryvu rezervací,
* limit maximálně 3 aktivních rezervací,
* storno rezervace méně než 24 hodin před začátkem,
* výpočet ceny rezervace,
* vytváření a mazání uživatelů a kurtů,
* výjimky a chybové scénáře.

Použit je Mockito pro mockování repository závislostí.

---

## Integrační testy

Integrační testy ověřují propojení:

* Controller → Service → Repository → PostgreSQL

Testy používají:

* Spring Boot Test
* MockMvc
* PostgreSQL databázi

Integrační testy ověřují:

* fungování REST API,
* ukládání dat do databáze,
* mazání dat z databáze,
* propojení všech vrstev aplikace,
* reálné business scénáře aplikace.

---

## Mocking

Mockito je použito pro:

* izolaci Service vrstvy,
* mockování repository/service závislostí,
* ověřování volání metod pomocí verify().

Použité test doubles:

* mock
* stub

---

## TDD přístup

Vývoj probíhal pomocí cyklu:

1. RED – vytvoření neprocházejícího testu
2. GREEN – minimální implementace
3. REFACTOR – úprava a zlepšení kódu

Refaktoring je viditelný v historii Git commitů i ve struktuře aplikace.

---

# Code Coverage

Měřeno pomocí JaCoCo.

## Coverage výsledky

* Line coverage: 77 %
* Branch coverage: 72 %

## Strategie pokrytí

Nejvyšší pokrytí má Service vrstva:

* Service layer: 91 % line coverage
* Controller layer: 100 % line coverage

Testy pokrývají:

* business pravidla rezervací,
* REST API endpointy,
* validační scénáře,
* výjimky a chybové stavy,
* práci s databází,
* integrační scénáře.

Nižší coverage zůstává hlavně v:

* konfiguraci aplikace,
* exception handlerech,
* bootstrap třídách,
* jednoduchých DTO objektech.

Tyto části neobsahují komplexní business logiku.

---

## Zdůvodnění coverage

Cílem projektu nebylo dosáhnout 100% pokrytí, ale efektivně testovat kritickou business logiku aplikace.

Největší důraz byl kladen na:

* Service vrstvu,
* validační pravidla,
* kolize rezervací,
* storno rezervací,
* výpočet ceny,
* integraci Controller → Service → Database.

Coverage přes 70 % je v tomto projektu považována za dostatečnou, protože kritická business logika je pokryta výrazně vyšším procentem.

---

# Databáze

* PostgreSQL
* Spring Data JPA (Hibernate)

---

# CI/CD Pipeline

Použit GitHub Actions.

## CI Pipeline obsahuje

* Maven build,
* unit testy,
* integrační testy,
* JaCoCo coverage,
* build Docker image,
* upload artefaktů,
* deployment do staging prostředí.

---

## CI/CD workflow

1. Push do GitHub repozitáře
2. GitHub Actions spustí CI pipeline
3. Maven build + testy + coverage
4. Docker image je vytvořen a pushnut do GitHub Container Registry
5. Staging deployment workflow nasadí aplikaci do Kubernetes
6. Kubernetes provede rollout nové verze aplikace

---

# Docker

Aplikace je kontejnerizována pomocí Dockeru.

## Vlastnosti Docker image

* reprodukovatelný build,
* spuštění na portu 8080,
* konfigurace pomocí environment variables,
* healthcheck endpoint:
  `/actuator/health`

---

# Docker Compose

Lokální spuštění aplikace a databáze.

```yaml
version: '3.8'

services:
  db:
    image: postgres:16
    environment:
      POSTGRES_DB: tennis_reservation_db
      POSTGRES_USER: postgres
      POSTGRES_PASSWORD: postgres
    ports:
      - "5432:5432"

  app:
    build: .
    ports:
      - "8080:8080"
    depends_on:
      - db
```

---

# Kubernetes Deployment

Nasazení probíhá do lokálního Kubernetes clusteru (Docker Desktop).

## Použité Kubernetes resource

* Deployment
* Service
* ConfigMap
* Secret

## Resource limity

Aplikace používá:

* CPU requests/limits
* Memory requests/limits

---

# Prostředí

## Staging

Slouží pro testování a ověřování aplikace.

* namespace: `tennis-staging`

## Production

Simulované produkční prostředí.

* namespace: `tennis-production`

## Rozdíly konfigurace

Staging používá testovací konfiguraci a image tagy určené pro ověřování.

Production používá oddělený namespace a samostatnou konfiguraci.

---

# Deployment

## Lokální spuštění

```bash
./mvnw spring-boot:run
```

## Testy

```bash
./mvnw clean verify
```

## Docker Compose

```bash
docker compose up --build
```

## Kubernetes deployment

### Staging

```bash
kubectl apply -f k8s/staging/
```

### Production

```bash
kubectl apply -f k8s/production/
```

## Přístup k aplikaci

```bash
kubectl port-forward service/tennis-app-service 8080:8080 -n tennis-staging
```

Aplikace:

```text
http://localhost:8080/actuator/health
```

---

# Observabilita

Použit Spring Boot Actuator.

## Monitoring

* `/actuator/health`
* readiness probes
* liveness probes

## Logging

Logy jsou dostupné pomocí:

```bash
kubectl logs <pod-name> -n tennis-staging
```

---

# Bezpečnost

Citlivé údaje nejsou uloženy v repozitáři.

Použité mechanismy:

* Kubernetes Secrets
* GitHub Secrets

---

# Git Workflow

Projekt používá Git workflow:

* feature branches,
* průběžné commity,
* merge do hlavní větve,
* CI pipeline při push.

---

# Spuštění projektu

## Klonování projektu

```bash
git clone https://github.com/StepanAdamek/tennis-court-reservation.git
cd tennis-court-reservation
```

## Maven

```bash
./mvnw clean verify
```

## Docker

```bash
docker compose up --build
```

---

# Repository

GitHub repository:

https://github.com/StepanAdamek/tennis-court-reservation
