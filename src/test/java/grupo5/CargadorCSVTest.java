package grupo5;
import grupo5.io.CargadorCSV;
import java.nio.file.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import static org.junit.jupiter.api.Assertions.*;

class CargadorCSVTest {
    @TempDir Path temporal;
    @Test void cargaPlanValido() throws Exception {
        var plan = new CargadorCSV().cargar(Path.of("datos/plan_valido.csv"));
        assertEquals(6, plan.materias().size());
        assertEquals(java.util.Set.of("MAT201", "PRO201"), plan.prerrequisitos().get("ALG301"));
        assertTrue(plan.dependientes().get("MAT201").contains("ALG301"));
    }
    @Test void rechazaReferenciaInexistente() throws Exception { invalido("A,Nombre,Z\n"); }
    @Test void rechazaCodigoDuplicado() throws Exception { invalido("A,Uno,\nA,Dos,\n"); }
    @Test void rechazaFormatoInvalido() throws Exception { invalido("A,Nombre\n"); }
    @Test void rechazaPrerrequisitoVacio() throws Exception { invalido("A,Nombre,A;\n"); }
    @Test void rechazaEncabezadoInvalido() throws Exception {
        Path archivo = Files.writeString(temporal.resolve("mal.csv"), "otro,encabezado\n");
        assertThrows(IllegalArgumentException.class, () -> new CargadorCSV().cargar(archivo));
    }
    private void invalido(String filas) throws Exception {
        Path archivo = Files.writeString(temporal.resolve("mal.csv"), "codigo,nombre,prerrequisitos\n" + filas);
        assertThrows(IllegalArgumentException.class, () -> new CargadorCSV().cargar(archivo));
    }
}
