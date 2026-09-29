# AGENTS.md — Java-Hospital

CLI Java + MySQL (hospitalizaciones La Esperanza) + migración Spring Boot + Angular.
Sin CI/lint. Tests: solo `HospitalApplicationTests.contextLoads` (backend).

## CLI original (`src/`, `sql/`, `lib/`)
- Entrada: `src/hosp/MenuMain.java` -> `hosp.Menu.Mostrar()` (`src/hosp/Menu.java`).
- `src/hosp/Escribir/` inserts + wizard paciente/expediente, `Escribir2/Tratamiento.java` prescripciones, `Leer/Cons1-10.java` reportes, `Menuconsultas/` submenús buscar, `Actualizar/update.java` altas, `colores/colores.java` ANSI.
- `sql/setup_hospitalizaciones.sql` es el DDL limpio (crea `hospitalizaciones` minúscula utf8mb4). `Hospitaciones.sql` es el original roto (sin `USE`/`;`, `SELECTs` intercalados, typo `ESP010`, `'4 semanas'` en INT, columnas cortas) — no usar directo. `QUERYS.sql` solo referencia.
- `lib/` trae `aqui_va_mysqlconnector` (marcador) + `mysql-connector-j-9.4.0.jar` gitignorado.
- Compilar/correr CLI: `~/.local/opt/jdk-21*/bin/javac -encoding UTF-8 -d bin $(find src -name "*.java")` + `java -cp "bin:lib/mysql-connector-j-*.jar" hosp.MenuMain` (terminal real; el pipe falla por múltiples `Scanner(System.in)`).

## Backend Spring Boot 3.2 + Java 21 (`backend/`)
- Requiere JDK 21 (`~/.local/opt/jdk-21*`) y Maven portable (`~/.local/opt/apache-maven-*/bin/mvn`).
- `mvn compile`, correr: `mvn spring-boot:run` (puerto 8080, datasource `root/""` en `application.properties`, `ddl-auto=validate`).
- 15 entidades en `domain/` (tabla `ViaAdministraciones` con esa mayúscula; `ExpedServi` sin PK en DB → `@IdClass`); 15 repos; `dto/` con validaciones que replican al CLI (CP dígitos, dosis ≤35, síntomas ≤280).
- `service/HospitalService`: `crearPaciente` (numero MAX+1), `crearExpediente` (decrementa cama 1..3, folio IDENTITY, servicio N→`ESPxx`), `crearTratamiento`, `darAlta` (**corrige 2 bugs del CLI**: filtraba `folio=1` fijo y nunca liberaba la cama).
- `service/ReporteService`: Cons1-10 vía `JdbcTemplate` con `LEFT JOIN` (el CLI perdía expedientes sin tratamiento por `INNER JOIN`); `web/ReporteController` en `/api/reportes/*`; CRUD en `/api/pacientes|expedientes|tratamientos`, catálogos en `/api/*`, CORS solo `localhost:4200`.

## Frontend Angular 19 standalone (`frontend/`)
- Node/npm portables en `~/.local/opt/node-v22*/bin` (el sistema solo trae `node` sin npm).
- `npm install`, dev: `npx ng serve` (puerto 4200), build: `npx ng build`. API base hardcodeada `http://localhost:8080/api` en `core/api.service.ts`.
- Rutas: `/pacientes` (Menu:1), `/expedientes` (Menu:2+4), `/tratamientos` (Menu:3), `/reportes` (Menu:5, 10 reportes con parámetros).
