# Planificador academico - Grupo 5

Proyecto academico de consola para cargar un plan de estudios desde CSV, modelarlo como grafo dirigido, comprobar si es un DAG, obtener ordenes topologicos mediante Kahn y DFS, explicar ciclos y consultar materias disponibles segun las aprobadas.

## Uso

Solo requiere JDK 21. Comprueba `java -version` y `javac -version`.
Desde la raiz del repositorio, en CMD de Windows o una terminal de Linux:

```sh
javac --release 21 -encoding UTF-8 -d out @fuentes.txt
java -cp out grupo5.Main
```

Para compilar y ejecutar las 17 pruebas con Java:

```sh
javac --release 21 -encoding UTF-8 -cp out -d out @pruebas.txt
java -cp out grupo5.Pruebas
```

Primero compila la aplicacion. El ejecutor muestra cada caso, cuenta aprobadas y
fallidas y termina con error si falla una comprobacion. No requiere activar `-ea`.
En PowerShell escribe los argumentos como `"@fuentes.txt"` y `"@pruebas.txt"`.

`fuentes.txt` y `pruebas.txt` enumeran los archivos que compila javac. Si agregas
una clase, incluye su ruta en la lista correspondiente. `out/` contiene archivos
generados: puedes borrarla y volver a compilar. La antigua carpeta `target/`
tambien se puede borrar; ya no se utiliza. No se necesita Maven ni JUnit.

En el menu, selecciona `1` e introduce `datos/plan_valido.csv`. Usa `2` para comprobar si es un DAG, `3` o `4` para ver un orden de cursado y `5` para consultar materias disponibles. En la consulta escribe codigos separados por `;`, por ejemplo `MAT101;PRO101`, o deja la entrada vacia si no has aprobado ninguna. `0` sale del programa.

Tambien puedes cargar `datos/plan_con_ciclo.csv` para ver una dependencia circular y `datos/plan_independiente.csv` para probar materias sin prerrequisitos. Un plan con ciclo no permite consultas de materias disponibles.

## Formato CSV

```csv
codigo,nombre,prerrequisitos
MAT101,Matematica basica,
PRO101,Programacion inicial,
ALG201,Algoritmos,MAT101;PRO101
```

Archivos UTF-8 con tres columnas separadas por comas; varios prerrequisitos se separan con `;`. Este formato sencillo no admite campos entre comillas ni comas internas. Se validan encabezado, campos obligatorios, codigos duplicados y referencias inexistentes. Un archivo con solo el encabezado representa un plan vacio. Las lineas vacias se ignoran.

Por ahora se mantiene la orientacion **del prerrequisito a la materia dependiente**, que produce directamente el orden de cursado. Kahn utiliza grados de entrada y DFS estados de visita. Si Kahn detecta un plan invalido, usa DFS para identificar un ciclo concreto. El orden puede variar entre ambos algoritmos y seguir siendo valido.

## Estructura

- `Main.java`: entrada y menu de consola.
- `modelo/`: materias y relaciones del plan.
- `io/`: carga CSV.
- `algoritmos/`: Kahn, DFS y resultado del ordenamiento.
- `servicio/`: consulta de materias disponibles.
- `src/test/java/`: pruebas de carga, ordenes, ciclos y consultas.
- `datos/`: ejemplos para ejecutar y probar.

El informe se prepara aparte. No se requiere base de datos ni interfaz web.

La consola se limpia al elegir una opcion y al volver al menu. Despues de cada resultado, pulsa Enter para continuar. En Windows se usa `cls`; en otras terminales compatibles se usan secuencias de control.
