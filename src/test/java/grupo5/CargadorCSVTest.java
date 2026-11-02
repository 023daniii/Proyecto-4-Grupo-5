package grupo5;
import grupo5.io.CargadorCSV;
import java.nio.file.*;
import static grupo5.Pruebas.*;

class CargadorCSVTest {
    Path temporal;
    CargadorCSVTest() throws java.io.IOException {
        temporal = Files.createTempDirectory("pruebas-planificador-");
    }
    void limpiar() throws java.io.IOException {
        try (var archivos = Files.list(temporal)) {
            for (Path archivo : archivos.toList()) Files.delete(archivo);
        }
        Files.delete(temporal);
    }
    void cargaPlanValido() throws Exception {
        var plan = new CargadorCSV().cargar(Path.of("datos/plan_valido.csv"));
        assertEquals(6, plan.materias().size());
        assertEquals(java.util.Set.of("MAT201", "PRO201"), plan.prerrequisitos().get("ALG301"));
        assertTrue(plan.dependientes().get("MAT201").contains("ALG301"));
    }
    void rechazaReferenciaInexistente() throws Exception { invalido("A,Nombre,Z\n"); }
    void rechazaCodigoDuplicado() throws Exception { invalido("A,Uno,\nA,Dos,\n"); }
    void rechazaFormatoInvalido() throws Exception { invalido("A,Nombre\n"); }
    void rechazaPrerrequisitoVacio() throws Exception { invalido("A,Nombre,A;\n"); }
    void rechazaEncabezadoInvalido() throws Exception {
        Path archivo = Files.writeString(temporal.resolve("mal.csv"), "otro,encabezado\n");
        assertThrows(IllegalArgumentException.class, () -> new CargadorCSV().cargar(archivo));
    }
    private void invalido(String filas) throws Exception {
        Path archivo = Files.writeString(temporal.resolve("mal.csv"), "codigo,nombre,prerrequisitos\n" + filas);
        assertThrows(IllegalArgumentException.class, () -> new CargadorCSV().cargar(archivo));
    }
}
