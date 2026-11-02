package grupo5.modelo;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/** Materias por codigo y relaciones por codigo. Arista: del prerrequisito al dependiente.
 * El cargador CSV construye ambas relaciones de forma coherente.
 */
public record PlanEstudios(Map<String, Materia> materias,
                           Map<String, Set<String>> prerrequisitos,
                           Map<String, Set<String>> dependientes) {
    public PlanEstudios {
        materias = Map.copyOf(materias);
        prerrequisitos = copiarRelaciones(prerrequisitos);
        dependientes = copiarRelaciones(dependientes);
    }

    private static Map<String, Set<String>> copiarRelaciones(Map<String, Set<String>> relaciones) {
        return relaciones.entrySet().stream().collect(
                Collectors.toUnmodifiableMap(Map.Entry::getKey, e -> Set.copyOf(e.getValue())));
    }
}
