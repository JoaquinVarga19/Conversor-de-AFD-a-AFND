package io;

import model.AFD;
import model.AFND;
import model.Automata;
import model.Estado;

import exceptions.FormatoArchivoException;
 
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;
import java.util.Map;


/*
Clase encargada de escribir un autómata (AFD o AFND) en un archivo de texto, usando el mismo formato
que la clase AutomataLectura para leerlo.
*/
public class AutomataEscritura {

    /*
    * Escribe un autómata en un archivo de texto.
    * @param automata El autómata a escribir.
    * @param rutaArchivo La ruta del archivo donde se escribirá el autómata.
    * @throws FormatoArchivoException Si ocurre un error al escribir el archivo.
    */
    public void escribirAutomata(Automata automata, String rutaArchivo) {
        StringBuilder sb = new StringBuilder();

        String tipo = (automata instanceof AFD) ? "AFD" : "AFND";

        sb.append("TIPO: ").append(tipo).append("\n");
        sb.append("ALFABETO: ").append(unirSimbolos(automata.getAlfabeto())).append("\n");
        sb.append("ESTADOS: ").append(unirEstados(automata.getEstados())).append("\n");
        sb.append("INICIAL: ").append(automata.getEstadoInicial().getId()).append("\n");
        sb.append("FINALES: ").append(unirEstados(automata.getEstadosAceptacion())).append("\n");
        sb.append("TRANSICIONES:\n");
        
        if (automata instanceof AFD afd) {
            escribirTransicionesAFD(sb, afd);
        } else if (automata instanceof AFND afnd) {
            escribirTransicionesAFND(sb, afnd);
        }

        try {
            Files.writeString(Path.of(rutaArchivo), sb.toString());
        } catch (IOException e) {
            throw new FormatoArchivoException("No se pudo escribir el archivo: " + rutaArchivo + " (" + e.getMessage() + ")");
        }
    }

    /*
    * Escribe las transiciones de un AFD en el StringBuilder.
    * @param sb El StringBuilder donde se escribirán las transiciones.
    * @param afd El AFD del cual se escribirán las transiciones.
    */
    private void escribirTransicionesAFD(StringBuilder sb, AFD afd) {
        for (Map.Entry<Estado, Map<Character, Estado>> entradaEstado : afd.getTransiciones().entrySet()) {
            Estado origen = entradaEstado.getKey();
            for (Map.Entry<Character, Estado> entradaSimbolo : entradaEstado.getValue().entrySet()) {
                sb.append(origen.getId()).append(",")
                .append(entradaSimbolo.getKey())
                .append(",")
                .append(entradaSimbolo.getValue().getId()).append("\n");
            }
        }
    }

    /*
    * Escribe las transiciones de un AFND en el StringBuilder.
    * @param sb El StringBuilder donde se escribirán las transiciones.
    * @param afnd El AFND del cual se escribirán las transiciones.
    */
    private void escribirTransicionesAFND(StringBuilder sb, AFND afnd) {
        for (Map.Entry<Estado, Map<Character, Set<Estado>>> entradaEstado : afnd.getTransiciones().entrySet()) {
            Estado origen = entradaEstado.getKey();

            for (Map.Entry<Character, Set<Estado>> entradaSimbolo : entradaEstado.getValue().entrySet()) {
                sb.append(origen.getId()).append(",").append(entradaSimbolo.getKey());
                for (Estado destino : entradaSimbolo.getValue()) {
                    sb.append(",").append(destino.getId());
                }
                sb.append("\n");
            }
        }
        
        if (afnd.getTransicionesEpsilon() != null && !afnd.getTransicionesEpsilon().isEmpty()) {
            sb.append("EPSILON:\n");
            for (Map.Entry<Estado, Set<Estado>> entrada : afnd.getTransicionesEpsilon().entrySet()) {
                sb.append(entrada.getKey().getId());            
                for (Estado destino : entrada.getValue()) {
                    sb.append(",").append(destino.getId());
                }
                sb.append("\n");
            }
        }
    }

    /*
    * Une los símbolos de un conjunto en una sola cadena separada por comas.
    * @param simbolos El conjunto de símbolos a unir.
    * @return Una cadena con los símbolos separados por comas.
    */
    private String unirSimbolos(Set<Character> simbolos) {
        StringBuilder sb = new StringBuilder();
        boolean primero = true;
        for (char c : simbolos) {
            if (!primero) {
                sb.append(",");
            }
            sb.append(c);
            primero = false;
        }
        return sb.toString();
    }

    /*
    * Une los estados de un conjunto en una sola cadena separada por comas.
    * @param estados El conjunto de estados a unir.
    * @return Una cadena con los IDs de los estados separados por comas.
    */
    private String unirEstados(Set<Estado> estados) {
        StringBuilder sb = new StringBuilder();
        boolean primero = true;
        for (Estado estado : estados) {
            if (!primero) {
                sb.append(",");
            }
            sb.append(estado.getId());
            primero = false;
        }
        return sb.toString();
    }
}
