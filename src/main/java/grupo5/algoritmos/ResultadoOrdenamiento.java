package grupo5.algoritmos;

import java.util.List;

/** Resultado previsto: validez, códigos ordenados y códigos de un ciclo, si existe.
 * En un plan válido el ciclo estará vacío; en uno inválido el orden podrá ser parcial.
 */
public record ResultadoOrdenamiento(boolean valido, List<String> orden, List<String> ciclo) {
    public ResultadoOrdenamiento {
        orden = List.copyOf(orden);
        ciclo = List.copyOf(ciclo);
    }
}
