package model;

import java.util.Set;
import java.util.Map;
import java.util.HashSet;
import java.util.Deque;
import java.util.ArrayDeque;

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

    /**  
     * Valida si la cacdena ingresada es aceptada por el AFND.
     * Se realiza un recorrido por los estados posibles a medida que se procesan los símbolos de la cadena,
     * considerando las transiciones epsilon.
     * @param cadena La cadena a validar.
     * @return true si la cadena es aceptada, false en caso contrario.
     */
    @Override 
    public boolean validarCadena(String cadena) {
        
        Set<Estado> estadosActuales = new HashSet<>();
        estadosActuales.add(estadoInicial);

        for (int i = 0; i < cadena.length(); i++) {
            char simbolo = cadena.charAt(i);

           Set<Estado> siguienteNivel = new HashSet<>();       

            for (Estado estado : estadosActuales) {
                if (transiciones.containsKey(estado) && transiciones.get(estado).containsKey(simbolo)) {
                    siguienteNivel.addAll(transiciones.get(estado).get(simbolo));
                }
            }

            if (siguienteNivel.isEmpty()) {
                return false; // No hay transiciones posibles para el símbolo actual
            }
            
            estadosActuales = siguienteNivel;
        }

        /*
        * Después de procesar toda la cadena, verificamos si alguno de los estados actuales es un estado de aceptación.
        * Si al menos uno de los estados actuales es un estado de aceptación, la cadena es aceptada.
        */
        for (Estado estado : estadosActuales) {
            if (estadosAceptacion.contains(estado)) {
                return true; // Al menos un estado actual es un estado de aceptación
            }
        }
        return false;
    }

    /*
    * Calcula la clausura epsilon de un conjunto de estados.
    * La clausura epsilon de un conjunto de estados es el conjunto de todos los estados que se pueden alcanzar desde los estados del conjunto original mediante transiciones epsilon.
    * @param conjunto El conjunto de estados del cual se calculará la clausura epsilon.
    * @return Un conjunto que contiene todos los estados alcanzables desde el conjunto original mediante transiciones epsilon.
    */
    private Set<Estado> clausuraEpsilon(Set<Estado> conjunto) {
        Set<Estado> clausura = new HashSet<>(conjunto);
        if (transicionesEpsilon == null) {
            return clausura;
        }
        Deque<Estado> pendientes = new ArrayDeque<>(conjunto);
        while (!pendientes.isEmpty()) {
            Estado actual = pendientes.pop();
            Set<Estado> alcanzables = transicionesEpsilon.get(actual);
            if (alcanzables != null) {
                for (Estado estado : alcanzables) {
                    if (!clausura.contains(estado)) {
                        clausura.add(estado);
                        pendientes.push(estado);
                    }
                }
            }
        }
        return clausura;
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