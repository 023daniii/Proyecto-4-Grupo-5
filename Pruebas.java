import java.nio.file.*;
import java.util.*;

/** Pruebas sencillas: lanza AssertionError si algun resultado no es el esperado. */
public class Pruebas {
    private static int aprobadas;

    public static void main(String[] args) throws Exception {
        PlanEstudios valido = PlanEstudios.cargarCSV(Path.of("datos/plan_valido.csv"));
        comprobar(valido.codigos().size() == 6, "Carga CSV de seis materias");
        comprobar(valido.prerrequisitos("ALG301").equals(Set.of("MAT201", "PRO201")),
                "Carga de varios prerrequisitos");
        comprobar(ordenValido(valido, valido.kahn()), "Kahn con CSV valido");
        comprobar(ordenValido(valido, valido.dfs()), "DFS con CSV valido");

        PlanEstudios ciclico = PlanEstudios.cargarCSV(Path.of("datos/plan_con_ciclo.csv"));
        comprobar(!ciclico.esValido() && ciclico.kahn() == null, "Kahn detecta ciclo del CSV");
        comprobar(ciclico.dfs() == null, "DFS detecta ciclo del CSV");
        comprobar(cicloValido(ciclico, ciclico.encontrarCiclo()), "Muestra un ciclo concreto");

        PlanEstudios independiente = PlanEstudios.cargarCSV(Path.of("datos/plan_independiente.csv"));
        comprobar(ordenValido(independiente, independiente.kahn())
                && ordenValido(independiente, independiente.dfs()), "Materias independientes");
        comprobar(new HashSet<>(valido.disponibles(Set.of())).equals(Set.of("MAT101", "PRO101")),
                "Consulta sin materias aprobadas");
        comprobar(new HashSet<>(valido.disponibles(Set.of("MAT101", "PRO101")))
                .equals(Set.of("MAT201", "PRO201")), "Consulta con materias aprobadas");
        comprobar(!valido.disponibles(Set.of("MAT201")).contains("ALG301")
                && valido.disponibles(Set.of("MAT201", "PRO201")).contains("ALG301"),
                "Exige todos los prerrequisitos");
        comprobar(valido.disponibles(valido.codigos()).isEmpty(), "Excluye materias ya aprobadas");
        try {
            valido.disponibles(Set.of("NO_EXISTE"));
            throw new AssertionError("Se acepto una materia desconocida");
        } catch (IllegalArgumentException esperado) {
            comprobar(true, "Rechaza codigos desconocidos en la consulta");
        }
        try {
            ciclico.disponibles(Set.of());
            throw new AssertionError("Se permitio consultar un plan con ciclo");
        } catch (IllegalArgumentException esperado) {
            comprobar(true, "Rechaza consultas en planes con ciclos");
        }

        // Plan armado en codigo, ademas de los planes cargados desde CSV.
        PlanEstudios manual = new PlanEstudios();
        manual.agregarMateria("A", "Primera");
        manual.agregarMateria("B", "Segunda");
        manual.agregarPrerrequisito("B", "A");
        comprobar(ordenValido(manual, manual.kahn()) && ordenValido(manual, manual.dfs()),
                "Plan valido armado en codigo");
        manual.agregarPrerrequisito("A", "B");
        comprobar(manual.kahn() == null && manual.dfs() == null
                && cicloValido(manual, manual.encontrarCiclo()), "Ciclo armado en codigo");

        PlanEstudios vacio = new PlanEstudios();
        comprobar(vacio.kahn().isEmpty() && vacio.dfs().isEmpty(), "Plan vacio");
        PlanEstudios propio = new PlanEstudios();
        propio.agregarMateria("A", "Primera");
        propio.agregarPrerrequisito("A", "A");
        comprobar(propio.kahn() == null && propio.dfs() == null
                && propio.encontrarCiclo().equals(List.of("A", "A")), "Materia con dependencia de si misma");

        csvInvalido("codigo,nombre,prerrequisitos\nA,Uno,Z\n", "Referencia inexistente");
        csvInvalido("codigo,nombre,prerrequisitos\nA,Uno,\nA,Dos,\n", "Codigo duplicado");
        csvInvalido("codigo,nombre,prerrequisitos\nA,Uno\n", "Cantidad de columnas incorrecta");
        csvInvalido("otro,encabezado\n", "Encabezado incorrecto");
        csvInvalido("codigo,nombre,prerrequisitos\nA,Uno,A;\n", "Prerrequisito vacio");
        System.out.println("Todas las pruebas pasaron. Total: " + aprobadas);
    }

    private static void comprobar(boolean condicion, String nombre) {
        if (!condicion) throw new AssertionError("FALLO: " + nombre);
        aprobadas++;
        System.out.println("OK: " + nombre);
    }

    // No exige un unico orden: verifica todas las relaciones del grafo.
    private static boolean ordenValido(PlanEstudios plan, List<String> orden) {
        if (orden == null || orden.size() != plan.codigos().size()
                || !new HashSet<>(orden).equals(plan.codigos())) return false;
        for (String materia : plan.codigos()) {
            for (String previo : plan.prerrequisitos(materia)) {
                if (orden.indexOf(previo) >= orden.indexOf(materia)) return false;
            }
        }
        return true;
    }

    private static boolean cicloValido(PlanEstudios plan, List<String> ciclo) {
        if (ciclo.size() < 2 || !ciclo.get(0).equals(ciclo.get(ciclo.size() - 1))) return false;
        for (int i = 1; i < ciclo.size(); i++) {
            if (!plan.prerrequisitos(ciclo.get(i)).contains(ciclo.get(i - 1))) return false;
        }
        return true;
    }

    private static void csvInvalido(String contenido, String nombre) throws Exception {
        Path temporal = Files.createTempFile("plan-invalido-", ".csv");
        try {
            Files.writeString(temporal, contenido);
            try {
                PlanEstudios.cargarCSV(temporal);
                throw new AssertionError("Se acepto un CSV invalido: " + nombre);
            } catch (IllegalArgumentException esperado) {
                comprobar(true, nombre);
            }
        } finally {
            Files.delete(temporal);
        }
    }
}
