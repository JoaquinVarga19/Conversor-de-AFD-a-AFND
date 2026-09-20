package model;

import java.util.Set;
import java.util.Map;

/*
* Clase que representa un Autómata Finito No Determinista (AFND).
*/
public class AFND extends Automata {

    /**
     * Mapa de transiciones del AFND. Cada estado puede tener múltiples transiciones para un mismo símbolo, por lo que se utiliza un Set de Estados como valor.
     */
    private Map<Estado, Map<Character, Set<Estado>>> transiciones;

    /*
    * Mapa de transiciones epsilon del AFND. Cada estado puede tener múltiples transiciones epsilon, por lo que se utiliza un Set de Estados como valor.
    */
    private Map<Estado, Set<Estado>> transicionesEpsilon;

    /**
     * Constructor por defecto
     */
    public AFND() {
        super();
    }

    public AFND(Set<Estado> estados, Set<Character> alfabeto, Estado estadoInicial, Set<Estado> estadosAceptacion,
            Map<Estado, Map<Character, Set<Estado>>> transiciones) {
        super(estados, alfabeto, estadoInicial, estadosAceptacion);
        this.transiciones = transiciones;
    }

    @Override 
    public boolean validarCadena(String cadena) {
        return false; // Implementación pendiente
    }

    /**
     * Getters y setters
     */
    public Map<Estado, Map<Character, Set<Estado>>> getTransiciones() {
        return transiciones;
    }

    public void setTransiciones(Map<Estado, Map<Character, Set<Estado>>> transiciones) {
        this.transiciones = transiciones;
    }

    public Map<Estado, Set<Estado>> getTransicionesEpsilon() {
        return transicionesEpsilon;
    }

    public void setTransicionesEpsilon(Map<Estado, Set<Estado>> transicionesEpsilon) {
        this.transicionesEpsilon = transicionesEpsilon;
    }    
}
