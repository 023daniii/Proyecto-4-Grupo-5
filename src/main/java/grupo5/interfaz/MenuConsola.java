package grupo5.interfaz;

import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/** Presenta las funciones previstas y permite salir sin ejecutar lógica pendiente. */
public final class MenuConsola {
    public void iniciar() {
        Scanner entrada = new Scanner(System.in, StandardCharsets.UTF_8);
        while (true) {
            System.out.println("""
                    Planificador académico — Grupo 5
                    1. Cargar CSV — Pendiente de implementación
                    2. Comprobar DAG y explicar ciclos — Pendiente de implementación
                    3. Orden completo mediante Kahn — Pendiente de implementación
                    4. Orden completo mediante DFS — Pendiente de implementación
                    5. Consultar materias disponibles — Pendiente de implementación
                    0. Salir""");
            System.out.print("Opción: ");
            if (!entrada.hasNextLine()) {
                System.out.println("Fin de entrada. Hasta pronto.");
                return;
            }
            switch (entrada.nextLine().trim()) {
                case "0" -> {
                    System.out.println("Hasta pronto.");
                    return;
                }
                case "1", "2", "3", "4", "5" -> System.out.println("Pendiente de implementación");
                default -> System.out.println("Opción inválida. Intenta de nuevo.");
            }
        }
    }
}
