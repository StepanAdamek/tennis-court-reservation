# Rezervační systém tenisových kurtů

## Přehled projektu

Tato aplikace je REST API vytvořené v **Spring Bootu** pro správu rezervací tenisových kurtů.

Cílem projektu bylo demonstrovat:

* vývoj řízený testy (TDD),
* práci s Gitem,
* CI/CD pipeline,
* kontejnerizaci a nasazení do Kubernetes,
* základní DevOps přístupy.

---

## Technologie (Tech Stack)

### Backend
- Java 21
- Spring Boot
- Spring Web
- Spring Data JPA
- Hibernate

### Databáze
- PostgreSQL

### Testování
- JUnit 5
- Mockito
- Spring Boot Test

### Build nástroje
- Maven

### DevOps
- Docker
- Docker Compose
- Kubernetes (Docker Desktop)
- GitHub Actions (CI/CD)

### Monitoring
- Spring Boot Actuator

---

## Doména aplikace

Aplikace umožňuje:

* zobrazit dostupné kurty,
* vytvářet rezervace,
* zabránit kolizím rezervací,
* spravovat uživatele a kurty.

### Hlavní entity

* **User (uživatel)**
* **Court (kurt)**
* **Reservation (rezervace)**

### Business pravidla

* rezervace se nesmí překrývat na stejném kurtu,
* rezervace musí být v budoucnosti,
* čas rezervace musí být validní (od–do),
* uživatel i kurt musí existovat,
* nelze vytvořit duplicitní rezervaci.

---

## Architektura

Aplikace je navržena ve vrstvené architektuře:

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

### Popis vrstev

* **Controller** – REST API
* **Service** – business logika
* **Repository** – přístup k databázi (Spring Data JPA)
* **Model / DTO** – datové struktury

### Tok dat

Request → Controller → Service → Repository → Database → Response

---

## Struktura projektu

```
src/
├── main/
│ ├── java/cz/school/tenniscourtreservation/
│ │ ├── controller/ # REST controllery
│ │ ├── service/ # business logika
│ │ ├── repository/ # přístup k databázi
│ │ ├── model/ # entity
│ │ ├── dto/ # přenosové objekty
│ │ ├── exception/ # výjimky a error handling
│ │ └── config/ # konfigurace aplikace
│ └── resources/
│ └── application.properties
│
├── test/
│ ├── controller/ # testy controllerů
│ ├── service/ # unit testy
│ └── integration/ # integrační testy
│
k8s/
├── staging/ # staging prostředí
└── production/ # production prostředí
```

.github/workflows/ # CI/CD pipeline

Dockerfile
docker-compose.yml
pom.xml

---

## Testovací strategie

### Typy testů

* **Unit testy**

  * testují business logiku (Service vrstva)
* **Integrační testy**

  * Controller → Service → DB
* **Mocking**

  * použití Mockito pro izolaci komponent

### TDD přístup

Vývoj probíhal podle cyklu:

1. RED – napsání neprocházejícího testu
2. GREEN – implementace minimálního řešení
3. REFACTOR – úprava a zlepšení kódu

---

## Code coverage

Měřeno pomocí **JaCoCo**.

* **Line coverage:** ~68 %
* **Branch coverage:** ~64 %

### Strategie pokrytí

* vysoké pokrytí ve **Service vrstvě (~85 %)**,
* nižší pokrytí v:

  * konfiguraci,
  * exception handlerech,
  * hlavní třídě aplikace.

Tyto části nejsou intenzivně testovány, protože neobsahují komplexní logiku.

---

## Databáze

* PostgreSQL
* Spring Data JPA (Hibernate)

---

## CI pipeline

Použit **GitHub Actions**.

Pipeline obsahuje:

* build (Maven),
* unit + integrační testy,
* code coverage (JaCoCo),
* build Docker image,
* upload artefaktů (test reporty, coverage).

---

## Docker

Aplikace je kontejnerizována pomocí Dockeru.

### Vlastnosti:

* reprodukovatelný build,
* běží na portu `8080`,
* konfigurace přes environment variables,
* healthcheck: `/actuator/health`.

---

## Docker Compose (lokální vývoj)

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

## Kubernetes nasazení

Nasazení probíhá do lokálního clusteru (Docker Desktop).

### Použité resource:

* Deployment
* Service
* ConfigMap
* Secret
* resource limits/requests

---

## Prostředí

### Staging

* slouží pro testování
* namespace: `tennis-staging`

### Production

* simulované produkční prostředí
* namespace: `tennis-production`

---

## Deployment

### Kubernetes (staging)

```bash
kubectl apply -f k8s/staging/
kubectl port-forward service/tennis-app-service 8080:8080 -n tennis-staging
```

Aplikace dostupná na:

```
http://localhost:8080/actuator/health
```

---

## Bezpečnost

* žádná citlivá data nejsou v repozitáři,
* používá se:

  * **Kubernetes Secrets** (např. DB heslo),
  * **GitHub Secrets** (CI/CD).

---

## Git workflow

* průběžné commity během vývoje,
* použití feature branch,
* merge do hlavní větve (fast-forward).

---

## Spuštění projektu

### Lokálně (Maven)

```bash
./mvnw spring-boot:run
```

### Testy

```bash
./mvnw test
```

### Docker

```bash
docker build -t tennis-app .
docker run -p 8080:8080 tennis-app
```

---

## Monitoring

Použit Spring Boot Actuator:

```
/actuator/health
```
