# 🏥 Sistema CLI Java + MySQL para hospitalizaciones La Esperanza

> CLI en Java para gestionar hospitalizaciones (pacientes, expedientes, prescripciones, altas y búsquedas) con MySQL.

## Stack

Real, según `src/` y `sql/` (sin Maven/Gradle):

- Java 17+ (compilación manual con `javac`, entrada `hosp.MenuMain`)
- MySQL 8 vía JDBC (`jdbc:mysql://localhost:3306/hospitalizaciones`, usuario `root`)
- CLI con `Scanner` y colores ANSI (`hosp.colores.colores`)
- SQL plano: `sql/Hospitaciones.sql` (DDL) y `sql/QUERYS.sql` (consultas de reportes)

## Estructura

Árbol real (resumido):

```text
src/hosp/
  MenuMain.java (main -> Menu.Mostrar())
  Menu.java (menú: nuevo paciente, expediente, prescripción, alta, búsquedas)
  Escribir/MenuEscribir.java, MetodosPacienteInsert.java, MetodoGenerarExpediente.java
  Escribir/ExpedientesInsert.java, insertar1.java, insert2.java
  Escribir/ConsultarListaDeHabitaciones.java, ConsultarServicios.java, DesplegarServicios.java
  Escribir/Expediente_Especialidad.java, Expediente_Servi.java, ServicioElegido.java
  Escribir/MostrarMedicamentos.java, test.java, nota.txt
  Escribir2/Tratamiento.java
  Leer/Cons1.java ... Cons10.java (consultas)
  Menuconsultas/MenuBuscarPrincipal.java, MenuBuscaPaciente.java, MenuBuscaMedico.java
  Actualizar/update.java
  colores/colores.java
sql/
  Hospitaciones.sql (CREATE DATABASE Hospitalizaciones + tablas: contactos, servicios, pacientes, especialidades, habitaciones, medicos, ...)
  QUERYS.sql (reportes: ingreso de paciente, historiales, etc.)
lib/
  aqui_va_mysqlconnector (archivo vacío: aquí va el .jar, no incluido)
```

## Cómo correr

1. Crear la base de datos (nombre real del archivo, con esa ortografía):

```bash
mysql -u root -p < sql/Hospitaciones.sql
```

2. Descargar el conector MySQL (ZIP *Platform Independent* desde Oracle) y copiar el `mysql-connector-j-*.jar` dentro de `lib/`.

3. Verificar la conexión en el código (`String url = "jdbc:mysql://localhost:3306/hospitalizaciones"`, usuario `root`, password `""`) y ajustarla a tu entorno.

4. Compilar y ejecutar (ejemplo Windows):

```bash
javac -d bin src/hosp/*.java src/hosp/*/*.java
java -cp "bin;lib/mysql-connector-j-*.jar" hosp.MenuMain
```

En Linux/macOS usa `:` en vez de `;` en el `-cp`.

## Estado/Notas

- Avance académico funcional por menú CLI; falta el `.jar` del conector (la carpeta `lib/` solo trae el marcador vacío).
- Nombres reales corregidos: el SQL se llama `Hospitaciones.sql` (no `hospitalizaciones.sql`) y crea la DB `Hospitalizaciones`.
- Hecho en conjunto con `lladux` según README anterior.
