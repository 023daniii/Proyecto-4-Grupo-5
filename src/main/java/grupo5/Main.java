package grupo5;

import grupo5.algoritmos.*;
import grupo5.io.CargadorCSV;
import grupo5.modelo.PlanEstudios;
import grupo5.servicio.ConsultaMaterias;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.*;

/** Entrada y menu de la aplicacion de consola. */
public final class Main {
    private Main() { }
    public static void main(String[] args) {
        Scanner entrada = new Scanner(System.in, StandardCharsets.UTF_8);
        PlanEstudios plan = null;
        while (true) {
            System.out.println("""
                    Planificador academico - Grupo 5
                    1. Cargar CSV
                    2. Comprobar DAG y detectar ciclos
                    3. Orden completo mediante Kahn
                    4. Orden completo mediante DFS
                    5. Consultar materias disponibles
                    0. Salir""");
            System.out.print("Opcion: ");
            if (!entrada.hasNextLine()) return;
            String opcion = entrada.nextLine().trim();
            if (opcion.equals("0")) {
                System.out.println("Hasta pronto.");
                return;
            }
            try {
                if (opcion.equals("1")) {
                    System.out.print("Ruta del CSV: ");
                    if (!entrada.hasNextLine()) return;
                    plan = new CargadorCSV().cargar(Path.of(entrada.nextLine().trim()));
                    System.out.println("Materias cargadas: " + plan.materias().size());
                } else if (Set.of("2", "3", "4", "5").contains(opcion)) {
                    if (plan == null) {
                        System.out.println("Primero carga un CSV.");
                        continue;
                    }
                    if (opcion.equals("5")) {
                        System.out.print("Codigos aprobados separados por ; (vacio si ninguno): ");
                        if (!entrada.hasNextLine()) return;
                        String texto = entrada.nextLine().trim();
                        Set<String> aprobadas = new HashSet<>();
                        if (!texto.isEmpty()) for (String codigo : texto.split(";", -1)) aprobadas.add(codigo.trim());
                        var materias = new ConsultaMaterias().disponibles(plan, aprobadas);
                        if (materias.isEmpty()) System.out.println("No hay materias disponibles.");
                        materias.forEach(m -> System.out.println(m.codigo() + " - " + m.nombre()));
                    } else {
                        var resultado = opcion.equals("4") ? new OrdenTopologicoDFS().ordenar(plan)
                                : new OrdenTopologicoKahn().ordenar(plan);
                        if (!resultado.valido()) System.out.println("Plan invalido: dependencia circular "
                                + String.join(", ", resultado.ciclo()));
                        else if (opcion.equals("2")) System.out.println("Plan valido: es un DAG.");
                        else System.out.println("Orden valido: " + String.join(", ", resultado.orden()));
                    }
                } else System.out.println("Opcion invalida.");
            } catch (IOException | IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
}
