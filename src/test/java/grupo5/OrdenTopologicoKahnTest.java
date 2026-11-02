package grupo5;
import grupo5.algoritmos.*;
import grupo5.io.CargadorCSV;
import java.nio.file.Path;
import java.util.*;
import static grupo5.Pruebas.*;

class OrdenTopologicoKahnTest {
    void ordenaPlanConRamificaciones() throws Exception { verificar("datos/plan_valido.csv"); }
    void incluyeMateriasIndependientes() throws Exception { verificar("datos/plan_independiente.csv"); }
    private void verificar(String archivo) throws Exception {
        var plan = new CargadorCSV().cargar(Path.of(archivo));
        var resultado = new OrdenTopologicoKahn().ordenar(plan);
        assertTrue(resultado.valido());
        assertTrue(resultado.ciclo().isEmpty());
        assertEquals(plan.materias().size(), resultado.orden().size());
        assertEquals(plan.materias().keySet(), new HashSet<>(resultado.orden()));
        plan.prerrequisitos().forEach((materia, previas) -> previas.forEach(previa ->
                assertTrue(resultado.orden().indexOf(previa) < resultado.orden().indexOf(materia))));
    }
    void detectaCicloDeTresMaterias() throws Exception {
        var plan = new CargadorCSV().cargar(Path.of("datos/plan_con_ciclo.csv"));
        var resultado = new OrdenTopologicoKahn().ordenar(plan);
        assertFalse(resultado.valido());
        var ciclo = resultado.ciclo();
        assertEquals(4, ciclo.size());
        assertEquals(ciclo.getFirst(), ciclo.getLast());
        for (int i = 1; i < ciclo.size(); i++)
            assertTrue(plan.dependientes().get(ciclo.get(i - 1)).contains(ciclo.get(i)));
    }
}
