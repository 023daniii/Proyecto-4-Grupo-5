# Planificador académico — Grupo 5

Proyecto académico de consola para cargar un plan de estudios desde CSV, modelarlo como grafo dirigido, comprobar si es un DAG, obtener órdenes topológicos mediante Kahn y DFS, explicar ciclos y consultar materias disponibles según las aprobadas.

## Estado actual

Estructura inicial. El menú arranca y permite salir con `0`; las funciones principales están **pendientes de implementación**. Los métodos pendientes lanzan `UnsupportedOperationException`. Las pruebas previstas están deshabilitadas con motivo explícito; no representan validaciones aprobadas.

## Herramientas

- JDK 21 (verificar con `java -version`).
- Maven 3.9.x (verificar con `mvn -version`; debe usar Java 21).
- VS Code opcional, con Extension Pack for Java recomendado en `.vscode/extensions.json`.

Desde la raíz del repositorio:

```sh
mvn clean compile
mvn clean test
mvn compile exec:java
```

Para comprobar el arranque y la salida automáticamente:

```sh
printf '0\n' | mvn -q compile exec:java
```

## Estructura

- `pom.xml`: compilación Java 21, JUnit 5 y ejecución de `grupo5.Main`.
- `src/main/java/grupo5`: entrada y paquetes `modelo`, `io`, `algoritmos`, `servicio` e `interfaz`.
- `src/test/java/grupo5`: casos previstos de carga, Kahn, DFS y consultas.
- `src/test/resources/datos`: futuros datos específicos de pruebas.
- `datos`: tres planes ficticios de ejemplo.
- `docs/informe.md`: esquema del informe pendiente de completar.

## CSV y grafo

Codificación UTF-8; encabezado `codigo,nombre,prerrequisitos`. Comas separan columnas y punto y coma separa varios códigos de prerrequisitos. La celda vacía indica ausencia de prerrequisitos. Los ejemplos usan campos sin comas internas.

```csv
codigo,nombre,prerrequisitos
MAT101,Matemática básica,
PRO101,Programación inicial,
ALG201,Algoritmos,MAT101;PRO101
```

Cada materia declara sus prerrequisitos en su fila. La arista se orienta **prerrequisito → materia dependiente**, lo que permite obtener directamente el orden de cursado. `PlanEstudios` mantiene materias por código, prerrequisitos y dependientes; la futura carga deberá asegurar que ambas relaciones sean coherentes.

`plan_valido.csv` contiene seis materias, ramificaciones y dos prerrequisitos para ALG301. `plan_con_ciclo.csv` contiene A101 → B101 → C101 → A101, con todas las referencias existentes. `plan_independiente.csv` contiene tres materias sin dependencias.

## Próxima etapa

Implementar y probar la carga CSV y sus validaciones; después Kahn, DFS, explicación de ciclos y consultas. Activar cada prueba cuando tenga aserciones reales y completar el informe con resultados observados.
