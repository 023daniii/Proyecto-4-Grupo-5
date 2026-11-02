package grupo5.io;

import grupo5.modelo.*;
import java.io.IOException;
import java.nio.file.*;
import java.util.*;

/** Lee el formato simple codigo,nombre,prerrequisitos (separados por ;). */
public final class CargadorCSV {
    public PlanEstudios cargar(Path archivo) throws IOException {
        var lineas = Files.readAllLines(archivo);
        if (lineas.isEmpty() || !lineas.getFirst().replace("\uFEFF", "").trim()
                .equals("codigo,nombre,prerrequisitos")) {
            throw new IllegalArgumentException("Encabezado CSV inválido");
        }
        Map<String, Materia> materias = new TreeMap<>();
        Map<String, Set<String>> requisitos = new TreeMap<>();
        Map<String, Set<String>> dependientes = new TreeMap<>();
        for (int i = 1; i < lineas.size(); i++) {
            String linea = lineas.get(i);
            if (linea.isBlank()) continue;
            String[] campos = linea.split(",", -1);
            if (campos.length != 3 || campos[0].isBlank() || campos[1].isBlank()
                    || linea.contains("\"")) {
                throw new IllegalArgumentException("Formato inválido en línea " + (i + 1));
            }
            String codigo = campos[0].trim();
            if (materias.putIfAbsent(codigo, new Materia(codigo, campos[1].trim())) != null)
                throw new IllegalArgumentException("Código duplicado: " + codigo);
            Set<String> previas = new TreeSet<>();
            if (!campos[2].isBlank()) {
                for (String previa : campos[2].split(";", -1)) {
                    if (previa.isBlank()) throw new IllegalArgumentException("Prerrequisito vacío en " + codigo);
                    previas.add(previa.trim());
                }
            }
            requisitos.put(codigo, previas);
            dependientes.put(codigo, new TreeSet<>());
        }
        for (String codigo : materias.keySet()) {
            for (String previa : requisitos.get(codigo)) {
                if (!materias.containsKey(previa))
                    throw new IllegalArgumentException("Prerrequisito inexistente: " + previa + " de " + codigo);
                dependientes.get(previa).add(codigo);
            }
        }
        return new PlanEstudios(materias, requisitos, dependientes);
    }
}
