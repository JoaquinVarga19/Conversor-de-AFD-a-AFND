package model;

import java.util.Set;
import java.util.Map;

/**
 * Clase que representa un Autómata Finito Determinista (AFD).
 */
public class AFD extends Automata {

    /*
     * Mapa de transiciones del AFD.
     */
    private Map<Estado, Map<Character, Estado>> transiciones;

    /*
    * Constructor por defecto.
    */
    public AFD() {
        super();
    }

    /**
     * Constructor con parámetros
     * @param estados
     * @param alfabeto
     * @param estadoInicial
     * @param estadosAceptacion
     * @param transiciones
     */
    public AFD(Set<Estado> estados, Set<Character> alfabeto, Estado estadoInicial, Set<Estado> estadosAceptacion, Map<Estado, Map<Character, Estado>> transiciones) {
        super(estados, alfabeto, estadoInicial, estadosAceptacion);
        this.transiciones = transiciones;
    }
    
    @Override 
    public boolean validarCadena(String cadena) {
        return false; // Implementación pendiente
    }

    /*
    * Getters y Setters
    */
    public Map<Estado, Map<Character, Estado>> getTransiciones() {
        return transiciones;
    }

    public void setTransiciones(Map<Estado, Map<Character, Estado>> transiciones) {
        this.transiciones = transiciones;
    }
}