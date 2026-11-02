package grupo5.algoritmos;

import grupo5.modelo.PlanEstudios;
import java.util.*;

/** DFS con estados: sin visitar, en recorrido y terminado. */
public final class OrdenTopologicoDFS {
    public ResultadoOrdenamiento ordenar(PlanEstudios plan) {
        Map<String, Integer> estado = new HashMap<>();
        List<String> camino = new ArrayList<>(), orden = new ArrayList<>(), ciclo = new ArrayList<>();
        for (String codigo : new TreeSet<>(plan.materias().keySet())) {
            if (estado.getOrDefault(codigo, 0) == 0 && visitar(codigo, plan, estado, camino, orden, ciclo))
                return new ResultadoOrdenamiento(false, List.of(), ciclo);
        }
        Collections.reverse(orden);
        return new ResultadoOrdenamiento(true, orden, List.of());
    }

    private boolean visitar(String codigo, PlanEstudios plan, Map<String, Integer> estado,
                            List<String> camino, List<String> orden, List<String> ciclo) {
        estado.put(codigo, 1);
        camino.add(codigo);
        for (String siguiente : new TreeSet<>(plan.dependientes().get(codigo))) {
            int marca = estado.getOrDefault(siguiente, 0);
            if (marca == 1) {
                ciclo.addAll(camino.subList(camino.indexOf(siguiente), camino.size()));
                ciclo.add(siguiente);
                return true;
            }
            if (marca == 0 && visitar(siguiente, plan, estado, camino, orden, ciclo)) return true;
        }
        camino.removeLast();
        estado.put(codigo, 2);
        orden.add(codigo);
        return false;
    }
}
