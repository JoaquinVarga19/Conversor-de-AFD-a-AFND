package io;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import exceptions.FormatoArchivoException;
import exceptions.TransicionInvalidaException;
import model.Automata;
import model.Estado;

/*
* Clase encargada de leer un automata (AFD o AFND) desde un archivo de texto con el formato:
* TIPO: AFD | AFND
* ALFABETO: simbolo1,simbolo2,...
* ESTADOS: id1,id2,...
* INICIAL: id
* FINALES: id1,id2,...
* TRANSICIONES:
* origen,simbolo,destino              (AFD: exactamente un destino)
* origen,simbolo,destino1,destino2    (AFND: uno o más destinos)
* EPSILON:                            (opcional, solo AFND)
* origen,destino1,destino2,...
*/
public class AutomataLectura {

    /*
     * Lee un archivo y crea un autómata.
     * @param rutaArchivo La ruta del archivo a leer.
     * @return El autómata creado a partir del archivo.
     */
    public Automata leerAutomata(String rutaArchivo) {
        // Implementación de la lectura del archivo y creación del autómata
        return null; // Retornar el autómata leído
    }

    /*
    * Parsea un string que representa un conjunto de símbolos del alfabeto.
    * @param valor El string a parsear.
    * @return Un conjunto de símbolos.
    */
    private Set<Character> parsearSimbolos(String valor) {
        Set<Character> simbolos = new LinkedHashSet<>();
        for (String s : dividir(valor)) {
            if (s.length() != 1) {
                throw new FormatoArchivoException("Símbolo de alfabeto inválido (debe ser un solo carácter): '" + s + "'");
            }
            simbolos.add(s.charAt(0));
        }
        return simbolos;
    }

    /*
    * Divide un string en partes separadas por comas y elimina espacios en blanco.
    * @param valor El string a dividir.
    * @return Una lista de partes limpias.
    */
    private List<String> dividir(String valor) {
        List<String> partes = new ArrayList<>();
        for (String p : valor.split(",")) {
            String limpio = p.trim();
            if (!limpio.isEmpty()) {
                partes.add(limpio);
            }
        }
        return partes;
    }

    /*
    * Obtiene un estado a partir de su ID.
    * @param estadosPorId El mapa de estados por ID.
    * @param id El ID del estado a obtener.
    * @return El estado correspondiente al ID.
    * @throws FormatoArchivoException Si el estado no está declarado en ESTADOS.
    */
    private Estado obtenerEstado(Map<String, Estado> estadosPorId, String id) {
        Estado estado = estadosPorId.get(id);
        if (estado == null) {
            throw new FormatoArchivoException("Estado '" + id + "' no está declarado en ESTADOS.");
        }
        return estado;
    }

    /*
    * Parsea las transiciones de un AFD.
    * @param lineas Las líneas del archivo que contienen las transiciones.
    * @param estadosPorId El mapa de estados por ID.
    * @param alfabeto El conjunto de símbolos válidos.
    * @return El mapa de transiciones del AFD.
    */
    private Map<Estado, Map<Character, Estado>> parsearTransicionesAFD(List<String> lineas, Map<String, Estado> estadosPorId, Set<Character> alfabeto) {

        Map<Estado, Map<Character, Estado>> transiciones = new HashMap<>();

        for (String linea : lineas) {
            String partes[] = linea.split(",");
            if (partes.length != 3) {
                throw new TransicionInvalidaException("Transición AFD mal formada (se esperan exactamente 3 campos: origen,simbolo,destino): " + linea);
            }
            Estado origen = obtenerEstado(estadosPorId, partes[0].trim());
            char simbolo = parsearSimbolo(partes[1].trim(), alfabeto);
            Estado destino = obtenerEstado(estadosPorId, partes[2].trim());

            transiciones.computeIfAbsent(origen, k -> new HashMap<>()).put(simbolo, destino);
        }
        return transiciones;
    }

    /*
    * Parsea las transiciones de un AFND.
    * @param lineas Las líneas del archivo que contienen las transiciones.
    * @param estadosPorId El mapa de estados por ID.
    * @param alfabeto El conjunto de símbolos válidos.
    * @return El mapa de transiciones del AFND. 
    */
    private Map<Estado, Map<Character, Set<Estado>>> parsearTransicionesAFND(List<String> lineas, Map<String, Estado> estadosPorId, Set<Character> alfabeto) {

        Map<Estado, Map<Character, Set<Estado>>> transiciones = new HashMap<>();
        
        for (String linea : lineas) {
            String partes[] = linea.split(",");
            if (partes.length < 3) {
                throw new TransicionInvalidaException("Transición AFND mal formada (se esperan al menos 3 campos: origen,simbolo,destino...): " + linea);
            }
            Estado origen = obtenerEstado(estadosPorId, partes[0].trim());
            char simbolo = parsearSimbolo(partes[1].trim(), alfabeto);

            Set<Estado> destinos = new HashSet<>();
            for (int i = 2; i < partes.length; i++) {
                destinos.add(obtenerEstado(estadosPorId, partes[i].trim())); 
            }

            transiciones.computeIfAbsent(origen, k -> new HashMap<>())
                        .computeIfAbsent(simbolo, k -> new HashSet<>())
                        .addAll(destinos);
        }
        return transiciones;
    }

    /*
     * Parsea las transiciones de epsilon.
     * @param lineas Las líneas del archivo que contienen las transiciones de epsilon.
     * @param estadosPorId El mapa de estados por ID.
     * @return El mapa de transiciones de epsilon.
     */
    private Map<Estado, Set<Estado>> parsearEpsilon(List<String> lineas, Map<String, Estado> estadosPorId) {

        Map<Estado, Set<Estado>> transicionesEpsilon = new java.util.HashMap<>();

        for (String linea : lineas) {
            String[] partes = linea.split(",");
            if (partes.length < 2) {
                throw new TransicionInvalidaException("Transición epsilon mal formada (se esperan al menos 2 campos: origen,destino...): " + linea);
            }
            Estado origen = obtenerEstado(estadosPorId, partes[0].trim());
            Set<Estado> destinos = new HashSet<>();
            for (int i = 1; i < partes.length; i++) {
                destinos.add(obtenerEstado(estadosPorId, partes[i].trim()));
            }
            transicionesEpsilon.computeIfAbsent(origen, k -> new HashSet<>()).addAll(destinos);
        }
        return transicionesEpsilon;
    }

    /*
     * Parsea un string que representa un símbolo del alfabeto.
     * @param texto El string a parsear.
     * @param alfabeto El conjunto de símbolos válidos.
     * @return El carácter resultante.
     * @throws TransicionInvalidaException Si el string no representa un símbolo válido.
     */
    private char parsearSimbolo(String texto, Set<Character> alfabeto) {
        if (texto.length() != 1) {
            throw new TransicionInvalidaException("Símbolo de transición inválido: '" + texto + "'");
        }
        char simbolo = texto.charAt(0);
        if (!alfabeto.contains(simbolo)) {
            throw new TransicionInvalidaException("El símbolo '" + simbolo + "' no pertenece al alfabeto declarado.");
        }   
        return simbolo;
    }
}