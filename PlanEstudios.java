import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;

/** Planificador de consola: CSV, grafo, Kahn, DFS y consultas. */
public class PlanEstudios {
    // Cada codigo tiene un nombre, sus prerrequisitos y sus materias sucesoras.
    private final Map<String, String> materias = new TreeMap<>();
    private final Map<String, Set<String>> prereqs = new TreeMap<>();
    private final Map<String, Set<String>> sucesores = new TreeMap<>();

    public void agregarMateria(String codigo, String nombre) {
        if (codigo == null || codigo.isBlank() || nombre == null || nombre.isBlank())
            throw new IllegalArgumentException("Codigo y nombre obligatorios");
        if (materias.containsKey(codigo))
            throw new IllegalArgumentException("Codigo duplicado: " + codigo);
        materias.put(codigo, nombre);
        prereqs.put(codigo, new TreeSet<>());
        sucesores.put(codigo, new TreeSet<>());
    }

    public void agregarPrerrequisito(String materia, String previo) {
        if (!materias.containsKey(materia) || !materias.containsKey(previo))
            throw new IllegalArgumentException("Materia o prerrequisito inexistente: " + materia + ", " + previo);
        prereqs.get(materia).add(previo);
        sucesores.get(previo).add(materia);
    }

    public Set<String> codigos() {
        return Collections.unmodifiableSet(materias.keySet());
    }

    public Set<String> prerrequisitos(String codigo) {
        return Collections.unmodifiableSet(prereqs.get(codigo));
    }

    /** Formato simple: tres columnas, sin comillas; varios prerrequisitos usan ;. */
    public static PlanEstudios cargarCSV(Path archivo) throws IOException {
        List<String> lineas = Files.readAllLines(archivo, StandardCharsets.UTF_8);
        if (lineas.isEmpty() || !lineas.get(0).replace("\uFEFF", "").trim()
                .equals("codigo,nombre,prerrequisitos"))
            throw new IllegalArgumentException("Encabezado CSV invalido");
        PlanEstudios plan = new PlanEstudios();
        Map<String, String> pendientes = new TreeMap<>();
        for (int i = 1; i < lineas.size(); i++) {
            String linea = lineas.get(i);
            if (linea.isBlank()) continue;
            String[] campos = linea.split(",", -1);
            if (campos.length != 3 || linea.contains("\""))
                throw new IllegalArgumentException("Formato invalido en linea " + (i + 1));
            String codigo = campos[0].trim();
            plan.agregarMateria(codigo, campos[1].trim());
            pendientes.put(codigo, campos[2].trim());
        }
        // Segunda pasada: permite referencias a materias declaradas mas adelante.
        for (String codigo : pendientes.keySet()) {
            String lista = pendientes.get(codigo);
            if (lista.isEmpty()) continue;
            for (String previo : lista.split(";", -1)) {
                if (previo.isBlank()) throw new IllegalArgumentException("Prerrequisito vacio en " + codigo);
                plan.agregarPrerrequisito(codigo, previo.trim());
            }
        }
        return plan;
    }

    /** Kahn: quita materias sin prerrequisitos pendientes. Null indica un ciclo. */
    public List<String> kahn() {
        Map<String, Integer> grados = new HashMap<>();
        Queue<String> cola = new ArrayDeque<>();
        for (String codigo : materias.keySet()) {
            int grado = prereqs.get(codigo).size();
            grados.put(codigo, grado);
            if (grado == 0) cola.add(codigo);
        }
        List<String> orden = new ArrayList<>();
        while (!cola.isEmpty()) {
            String codigo = cola.remove();
            orden.add(codigo);
            for (String siguiente : sucesores.get(codigo)) {
                int grado = grados.get(siguiente) - 1;
                grados.put(siguiente, grado);
                if (grado == 0) cola.add(siguiente);
            }
        }
        return orden.size() == materias.size() ? orden : null;
    }

    /** DFS: devuelve un orden completo o null cuando encuentra un ciclo. */
    public List<String> dfs() {
        List<String> orden = new ArrayList<>();
        if (!recorrerDFS(orden).isEmpty()) return null;
        Collections.reverse(orden);
        return orden;
    }

    public boolean esValido() {
        return kahn() != null;
    }

    /** Devuelve el ciclo cerrado (primer codigo repetido al final), o una lista vacia. */
    public List<String> encontrarCiclo() {
        return recorrerDFS(new ArrayList<>());
    }

    private List<String> recorrerDFS(List<String> orden) {
        Map<String, Integer> estado = new HashMap<>();
        List<String> camino = new ArrayList<>();
        List<String> ciclo = new ArrayList<>();
        for (String codigo : materias.keySet()) {
            if (estado.getOrDefault(codigo, 0) == 0 && visitar(codigo, estado, camino, orden, ciclo)) break;
        }
        return ciclo;
    }

    // Estado 0: sin visitar; 1: en el camino actual; 2: terminado.
    private boolean visitar(String codigo, Map<String, Integer> estado,
                            List<String> camino, List<String> orden, List<String> ciclo) {
        estado.put(codigo, 1);
        camino.add(codigo);
        for (String siguiente : sucesores.get(codigo)) {
            int marca = estado.getOrDefault(siguiente, 0);
            if (marca == 1) {
                ciclo.addAll(camino.subList(camino.indexOf(siguiente), camino.size()));
                ciclo.add(siguiente);
                return true;
            }
            if (marca == 0 && visitar(siguiente, estado, camino, orden, ciclo)) return true;
        }
        camino.remove(camino.size() - 1);
        estado.put(codigo, 2);
        orden.add(codigo);
        return false;
    }

    public List<String> disponibles(Set<String> aprobadas) {
        for (String codigo : aprobadas) {
            if (!materias.containsKey(codigo))
                throw new IllegalArgumentException("Materia aprobada desconocida: " + codigo);
        }
        if (!esValido()) throw new IllegalArgumentException("Plan invalido: contiene un ciclo");
        List<String> resultado = new ArrayList<>();
        for (String codigo : materias.keySet()) {
            if (!aprobadas.contains(codigo) && aprobadas.containsAll(prereqs.get(codigo)))
                resultado.add(codigo);
        }
        return resultado;
    }

    public static void main(String[] args) {
        Scanner entrada = new Scanner(System.in, StandardCharsets.UTF_8);
        PlanEstudios plan = null;
        while (true) {
            limpiarTerminal();
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
            limpiarTerminal();
            if (opcion.equals("0")) {
                System.out.println("Hasta pronto.");
                return;
            }
            try {
                if (opcion.equals("1")) {
                    System.out.print("Ruta del CSV: ");
                    if (!entrada.hasNextLine()) return;
                    plan = cargarCSV(Path.of(entrada.nextLine().trim()));
                    System.out.println("Materias cargadas: " + plan.materias.size());
                } else if (Set.of("2", "3", "4", "5").contains(opcion)) {
                    if (plan == null) {
                        throw new IllegalArgumentException("Primero carga un CSV.");
                    }
                    if (opcion.equals("5")) {
                        System.out.print("Codigos aprobados separados por ; (vacio si ninguno): ");
                        if (!entrada.hasNextLine()) return;
                        String texto = entrada.nextLine().trim();
                        Set<String> aprobadas = new HashSet<>();
                        if (!texto.isEmpty()) for (String codigo : texto.split(";", -1)) aprobadas.add(codigo.trim());
                        var materias = plan.disponibles(aprobadas);
                        if (materias.isEmpty()) System.out.println("No hay materias disponibles.");
                        for (String codigo : materias) System.out.println(codigo + " - " + plan.materias.get(codigo));
                    } else {
                        List<String> orden = opcion.equals("4") ? plan.dfs() : plan.kahn();
                        if (orden == null) System.out.println("Plan invalido: dependencia circular "
                                + String.join(", ", plan.encontrarCiclo()));
                        else if (opcion.equals("2")) System.out.println("Plan valido: es un DAG.");
                        else System.out.println("Orden valido: " + String.join(", ", orden));
                    }
                } else System.out.println("Opcion invalida.");
            } catch (IOException | IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
            }
            System.out.print("Pulsa Enter para volver al menu...");
            if (!entrada.hasNextLine()) return;
            entrada.nextLine();
        }
    }

    /** Usa cls en Windows y secuencias de control en terminales compatibles. */
    private static void limpiarTerminal() {
        if (System.getProperty("os.name").startsWith("Windows")) {
            try {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } catch (IOException e) {
                // La operacion del menu sigue disponible si no se puede limpiar.
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        } else {
            String terminal = System.getenv("TERM");
            if (terminal != null && !terminal.equals("dumb")) {
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        }
    }

}
