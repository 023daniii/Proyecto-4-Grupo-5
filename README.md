# Planificador académico — Grupo 5

Proyecto académico de consola para cargar un plan de estudios desde CSV, modelarlo como grafo dirigido, comprobar si es un DAG, obtener órdenes topológicos mediante Kahn y DFS, explicar ciclos y consultar materias disponibles según las aprobadas.

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
