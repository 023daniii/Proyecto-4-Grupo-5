package grupo5.algoritmos;

import grupo5.modelo.PlanEstudios;
import java.util.*;

/** Extrae materias sin prerrequisitos pendientes; DFS identifica el ciclo si falla. */
public final class OrdenTopologicoKahn {
    public ResultadoOrdenamiento ordenar(PlanEstudios plan) {
        Map<String, Integer> grados = new HashMap<>();
        Queue<String> disponibles = new PriorityQueue<>();
        for (String codigo : plan.materias().keySet()) {
            int grado = plan.prerrequisitos().get(codigo).size();
            grados.put(codigo, grado);
            if (grado == 0) disponibles.add(codigo);
        }
        List<String> orden = new ArrayList<>();
        while (!disponibles.isEmpty()) {
            String codigo = disponibles.remove();
            orden.add(codigo);
            for (String siguiente : plan.dependientes().get(codigo)) {
                if (grados.merge(siguiente, -1, Integer::sum) == 0) disponibles.add(siguiente);
            }
        }
        boolean valido = orden.size() == plan.materias().size();
        return new ResultadoOrdenamiento(valido, orden,
                valido ? List.of() : new OrdenTopologicoDFS().ordenar(plan).ciclo());
    }
}
