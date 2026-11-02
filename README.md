# Planificador académico — Grupo 5

Proyecto académico de consola para cargar un plan de estudios desde CSV, modelarlo como grafo dirigido, comprobar si es un DAG, obtener órdenes topológicos mediante Kahn y DFS, explicar ciclos y consultar materias disponibles según las aprobadas.

## Uso

Requiere JDK 21 y Maven 3.9.x. Desde la raíz del repositorio:

```sh
mvn clean test
mvn compile exec:java
```

En el menú, selecciona `1` e introduce `datos/plan_valido.csv`. Usa `2` para comprobar si es un DAG, `3` o `4` para ver un orden de cursado y `5` para consultar materias disponibles. En la consulta escribe códigos separados por `;`, por ejemplo `MAT101;PRO101`, o deja la entrada vacía si no has aprobado ninguna. `0` sale del programa.

También puedes cargar `datos/plan_con_ciclo.csv` para ver una dependencia circular y `datos/plan_independiente.csv` para probar materias sin prerrequisitos. Un plan con ciclo no permite consultas de materias disponibles.

## Formato CSV

```csv
codigo,nombre,prerrequisitos
MAT101,Matemática básica,
PRO101,Programación inicial,
ALG201,Algoritmos,MAT101;PRO101
```

Archivos UTF-8 con tres columnas separadas por comas; varios prerrequisitos se separan con `;`. Este formato sencillo no admite campos entre comillas ni comas internas. Se validan encabezado, campos obligatorios, códigos duplicados y referencias inexistentes. Un archivo con solo el encabezado representa un plan vacío. Las líneas vacías se ignoran.

Por ahora se mantiene la orientación **prerrequisito → materia dependiente**, que produce directamente el orden de cursado. Kahn utiliza grados de entrada y DFS estados de visita. Si Kahn detecta un plan inválido, usa DFS para identificar un ciclo concreto. El orden puede variar entre ambos algoritmos y seguir siendo válido.

## Estructura

- `Main.java`: entrada y menú de consola.
- `modelo/`: materias y relaciones del plan.
- `io/`: carga CSV.
- `algoritmos/`: Kahn, DFS y resultado del ordenamiento.
- `servicio/`: consulta de materias disponibles.
- `src/test/java/`: pruebas de carga, órdenes, ciclos y consultas.
- `datos/`: ejemplos para ejecutar y probar.

El informe se prepara aparte. No se requiere base de datos ni interfaz web.
