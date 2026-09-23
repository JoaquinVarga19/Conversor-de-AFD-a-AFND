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
        Map<Estado, Map<Character, Set<Estado>>> transicionesAFD = new HashMap<>();

        // --- Estado inicial ---
        Set inicialAFNDset = new HashSet<>();
        inicialAFNDset.add(afnd.getEstadoInicial());

        Estado estadoInicialAFD = new Estado("q0");
        mapeoConjuntos.put(inicialAFNDset, estadoInicialAFD);
        colaPendientes.add(inicialAFNDset);
        estadosAFD.add(estadoInicialAFD);


        
        return null; // Implementación pendiente
    }
}
