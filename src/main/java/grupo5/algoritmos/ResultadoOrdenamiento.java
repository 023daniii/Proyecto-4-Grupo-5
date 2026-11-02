package grupo5.algoritmos;

import java.util.List;

/** Resultado previsto: validez, codigos ordenados y codigos de un ciclo, si existe.
 * En un plan valido el ciclo estara vacio; en uno invalido el orden podra ser parcial.
 */
public record ResultadoOrdenamiento(boolean valido, List<String> orden, List<String> ciclo) {
    public ResultadoOrdenamiento {
        orden = List.copyOf(orden);
        ciclo = List.copyOf(ciclo);
    }
}
