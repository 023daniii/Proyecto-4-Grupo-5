package grupo5;

import java.util.Objects;

/** Ejecuta las pruebas con Java, sin bibliotecas externas ni opcion -ea. */
public final class Pruebas {
    private static int aprobadas;
    private static int fallidas;
    @FunctionalInterface
    interface Caso { void ejecutar() throws Exception; }

    public static void main(String[] args) throws Exception {
        var carga = new CargadorCSVTest();
        try {
            ejecutar("CargadorCSVTest.cargaPlanValido", carga::cargaPlanValido);
            ejecutar("CargadorCSVTest.rechazaReferenciaInexistente", carga::rechazaReferenciaInexistente);
            ejecutar("CargadorCSVTest.rechazaCodigoDuplicado", carga::rechazaCodigoDuplicado);
            ejecutar("CargadorCSVTest.rechazaFormatoInvalido", carga::rechazaFormatoInvalido);
            ejecutar("CargadorCSVTest.rechazaPrerrequisitoVacio", carga::rechazaPrerrequisitoVacio);
            ejecutar("CargadorCSVTest.rechazaEncabezadoInvalido", carga::rechazaEncabezadoInvalido);
            ejecutar("ConsultaMateriasTest.ofreceMateriasSinPrerrequisitos", new ConsultaMateriasTest()::ofreceMateriasSinPrerrequisitos);
            ejecutar("ConsultaMateriasTest.exigeTodosLosPrerrequisitos", new ConsultaMateriasTest()::exigeTodosLosPrerrequisitos);
            ejecutar("ConsultaMateriasTest.excluyeMateriasAprobadas", new ConsultaMateriasTest()::excluyeMateriasAprobadas);
            ejecutar("ConsultaMateriasTest.rechazaCodigoDesconocido", new ConsultaMateriasTest()::rechazaCodigoDesconocido);
            ejecutar("ConsultaMateriasTest.rechazaPlanConCiclo", new ConsultaMateriasTest()::rechazaPlanConCiclo);
            ejecutar("OrdenTopologicoDFSTest.ordenaPlanConRamificaciones", new OrdenTopologicoDFSTest()::ordenaPlanConRamificaciones);
            ejecutar("OrdenTopologicoDFSTest.incluyeMateriasIndependientes", new OrdenTopologicoDFSTest()::incluyeMateriasIndependientes);
            ejecutar("OrdenTopologicoDFSTest.detectaCicloDeTresMaterias", new OrdenTopologicoDFSTest()::detectaCicloDeTresMaterias);
            ejecutar("OrdenTopologicoKahnTest.ordenaPlanConRamificaciones", new OrdenTopologicoKahnTest()::ordenaPlanConRamificaciones);
            ejecutar("OrdenTopologicoKahnTest.incluyeMateriasIndependientes", new OrdenTopologicoKahnTest()::incluyeMateriasIndependientes);
            ejecutar("OrdenTopologicoKahnTest.detectaCicloDeTresMaterias", new OrdenTopologicoKahnTest()::detectaCicloDeTresMaterias);
        } finally {
            carga.limpiar();
        }
        System.out.println("Pruebas aprobadas: " + aprobadas + "; fallidas: " + fallidas);
        if (fallidas > 0) throw new AssertionError("Hay pruebas fallidas");
    }
    private static void ejecutar(String nombre, Caso caso) {
        try {
            caso.ejecutar();
            aprobadas++;
            System.out.println("OK: " + nombre);
        } catch (Exception | AssertionError error) {
            fallidas++;
            System.err.println("FALLO: " + nombre + " - " + error);
        }
    }
    static void assertTrue(boolean condicion) {
        if (!condicion) throw new AssertionError("Se esperaba verdadero");
    }
    static void assertFalse(boolean condicion) {
        if (condicion) throw new AssertionError("Se esperaba falso");
    }
    static void assertEquals(Object esperado, Object actual) {
        if (!Objects.equals(esperado, actual))
            throw new AssertionError("Esperado: " + esperado + "; obtenido: " + actual);
    }
    static void assertThrows(Class<? extends Exception> tipo, Caso caso) {
        try {
            caso.ejecutar();
        } catch (Exception error) {
            if (tipo.isInstance(error)) return;
            throw new AssertionError("Excepcion inesperada", error);
        }
        throw new AssertionError("Se esperaba " + tipo.getSimpleName());
    }
}
