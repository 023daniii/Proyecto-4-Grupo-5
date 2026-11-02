package grupo5;
import grupo5.io.CargadorCSV;
import grupo5.servicio.ConsultaMaterias;
import grupo5.modelo.*;
import java.nio.file.Path;
import java.util.*;
import static grupo5.Pruebas.*;

class ConsultaMateriasTest {
    private PlanEstudios plan() throws Exception { return new CargadorCSV().cargar(Path.of("datos/plan_valido.csv")); }
    private Set<String> disponibles(Set<String> aprobadas) throws Exception {
        return new HashSet<>(new ConsultaMaterias().disponibles(plan(), aprobadas).stream().map(Materia::codigo).toList());
    }
    void ofreceMateriasSinPrerrequisitos() throws Exception {
        assertEquals(Set.of("MAT101", "PRO101"), disponibles(Set.of()));
    }
    void exigeTodosLosPrerrequisitos() throws Exception {
        assertFalse(disponibles(Set.of("MAT101", "PRO101", "MAT201")).contains("ALG301"));
        assertTrue(disponibles(Set.of("MAT101", "PRO101", "MAT201", "PRO201")).contains("ALG301"));
    }
    void excluyeMateriasAprobadas() throws Exception {
        assertFalse(disponibles(Set.of("MAT101")).contains("MAT101"));
        assertTrue(disponibles(plan().materias().keySet()).isEmpty());
    }
    void rechazaCodigoDesconocido() throws Exception {
        var plan = plan();
        assertThrows(IllegalArgumentException.class, () -> new ConsultaMaterias().disponibles(plan, Set.of("NO")));
    }
    void rechazaPlanConCiclo() throws Exception {
        var plan = new CargadorCSV().cargar(Path.of("datos/plan_con_ciclo.csv"));
        assertThrows(IllegalArgumentException.class, () -> new ConsultaMaterias().disponibles(plan, Set.of()));
    }
}
