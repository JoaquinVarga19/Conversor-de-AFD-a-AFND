package service;

import model.AFD;
import model.AFND;
import model.Estado;
import java.util.Set;
import java.util.Map;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Queue;
import java.util.LinkedList;

/*
Convertir un AFND a un AFD por el metodo de subconjuntos.
*/

public class Conversor {

    /*
    Contador para nombrar los nuevos estados del AFD de manera única.
    */
    private int contadorEstados;


    /*
    Convierte un AFND a un AFD utilizando el método de subconjuntos.
    @param afnd El autómata finito no determinista a convertir.
    @return El AFD resultante.
    */
    public AFD convertirAFNDaAFD(AFND afnd) {
       // Lógica principal del algoritmo de subconjuntos
        
        // 1. Crear la estructura para los nuevos estados del AFD
        // 2. Calcular el estado inicial del AFD
        // 3. Usar una cola o lista pendiente para explorar los nuevos subconjuntos de estados
        // 4. Construir la nueva tabla de transiciones deterministas
        // 5. Determinar cuáles son los estados de aceptación del AFD

        Set<Character> alfabeto = afnd.getAlfabeto();

        Map<Set<Estado>, Estado> mapeoConjuntos = new HashMap<>();
        Queue<Set<Estado>> colaPendientes = new LinkedList<>();

        Set<Estado> estadosAFD = new HashSet<>();
        Set<Estado> estadosAceptacionAFD = new HashSet<>();
        Map<Estado, Map<Character, Estado>> transicionesAFD = new HashMap<>();

        contadorEstados = 0;

        // Estado inicial del AFD, clausura epsilon del estado inicial del AFND
        Set<Estado> conjuntoInicial = epsilonClausura(afnd, Set.of(afnd.getEstadoInicial()));

        Estado estadoInicialAFD = obtenerOCrearEstado(conjuntoInicial, mapeoConjuntos, afnd);
        estadosAFD.add(estadoInicialAFD);
        colaPendientes.add(conjuntoInicial);

        if (contieneAceptacion(conjuntoInicial, afnd)) {
            estadosAceptacionAFD.add(estadoInicialAFD);
        }

        while (!colaPendientes.isEmpty()) {
            Set<Estado> conjuntoActual = colaPendientes.poll();
            Estado estadoActualAFD = mapeoConjuntos.get(conjuntoActual);
            
            for (char simbolo : alfabeto) {
                Set<Estado> destino = mover(afnd, conjuntoActual, simbolo);
                destino = epsilonClausura(afnd, destino);

                if (destino.isEmpty()) {
                    continue; // No hay transición para este símbolo
                }

                boolean esNuevoEstado = !mapeoConjuntos.containsKey(destino);
                Estado estadoDestinoAFD = obtenerOCrearEstado(destino, mapeoConjuntos, afnd);

                if (esNuevoEstado) {
                    estadosAFD.add(estadoDestinoAFD);
                    colaPendientes.add(destino);

                    if (contieneAceptacion(destino, afnd)) {
                        estadosAceptacionAFD.add(estadoDestinoAFD);
                    }
                }

                 transicionesAFD
                    .computeIfAbsent(estadoActualAFD, k -> new HashMap<>())
                    .put(simbolo, estadoDestinoAFD);
            }
        }

        return new AFD(estadosAFD, alfabeto, estadoInicialAFD, estadosAceptacionAFD, transicionesAFD); 
    }


    /*
     * Devuelve el estado del AFD asociado a un subconjunto de estados del AFND,
     * creándolo si todavía no existe.
     */
    private Estado obtenerOCrearEstado(Set<Estado> conjunto, Map<Set<Estado>, Estado> mapeoConjuntos, AFND afnd) {
        if (mapeoConjuntos.containsKey(conjunto)) {
            return mapeoConjuntos.get(conjunto);
        }
        Estado nuevoEstado = new Estado("q" + contadorEstados++, contieneAceptacion(conjunto, afnd));
        mapeoConjuntos.put(conjunto, nuevoEstado);
        return nuevoEstado;
    }

    /*
     * Calcula el conjunto de estados alcanzables desde 'origen' consumiendo 'simbolo'.
     */
    private Set<Estado> mover(AFND afnd, Set<Estado> origen, char simbolo) {
        Set<Estado> resultado = new HashSet<>();
        Map<Estado, Map<Character, Set<Estado>>> transiciones = afnd.getTransiciones();

        for (Estado estado : origen) {
            Map<Character, Set<Estado>> transicionesEstado = transiciones.get(estado);
            if (transicionesEstado != null && transicionesEstado.containsKey(simbolo)) {
                resultado.addAll(transicionesEstado.get(simbolo));
            }
        }
        return resultado;
    }

    
    /**
     * Calcula la clausura-epsilon de un conjunto de estados: todos los estados
     * alcanzables sin consumir ningún símbolo (transiciones epsilon).
     * Si el AFND no tiene transiciones epsilon definidas, devuelve el mismo conjunto.
     */
    private Set<Estado> epsilonClausura(AFND afnd, Set<Estado> conjunto) {
        Map<Estado, Set<Estado>> transicionesEpsilon = afnd.getTransicionesEpsilon();

        Set<Estado> clausura = new HashSet<>(conjunto);

        if (transicionesEpsilon == null) {
            return clausura; 
        }

        Queue<Estado> pendientes = new LinkedList<>(conjunto);

        while (!pendientes.isEmpty()) {
            Estado actual = pendientes.poll();
            Set<Estado> alcanzables = transicionesEpsilon.get(actual);

            if (alcanzables == null) {
                continue;
            }

            for (Estado destino : alcanzables) {
                if (clausura.add(destino)) {
                    pendientes.add(destino);
                }
            }
        }
        return clausura;
    }
    
    /**
     * Indica si un subconjunto de estados del AFND contiene al menos un estado de aceptación.
     */
    private boolean contieneAceptacion(Set<Estado> conjunto, AFND afnd) {
        for (Estado estado : conjunto) {
            if (afnd.getEstadosAceptacion().contains(estado)) {
                return true;
            }
        }
        return false;
    }
}