package grupo5.servicio;

import grupo5.modelo.*;
import grupo5.algoritmos.OrdenTopologicoKahn;
import java.util.*;

/** Ofrece materias no aprobadas cuyos prerrequisitos están todos aprobados. */
public final class ConsultaMaterias {
    public List<Materia> disponibles(PlanEstudios plan, Set<String> aprobadas) {
        for (String codigo : aprobadas) {
            if (!plan.materias().containsKey(codigo))
                throw new IllegalArgumentException("Materia aprobada desconocida: " + codigo);
        }
        if (!new OrdenTopologicoKahn().ordenar(plan).valido())
            throw new IllegalArgumentException("Plan inválido: contiene un ciclo");
        return plan.materias().values().stream()
                .filter(m -> !aprobadas.contains(m.codigo()))
                .filter(m -> aprobadas.containsAll(plan.prerrequisitos().get(m.codigo())))
                .sorted(Comparator.comparing(Materia::codigo)).toList();
    }
}
