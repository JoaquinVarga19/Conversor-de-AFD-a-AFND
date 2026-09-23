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
    

    /*
    * Valida si una cadena pertenece al lenguaje del AFD.
    * Se declara el estado actual y se recorre cada simbolo de la cadena, verificando si existe una transición definida para el estado actual y el símbolo actual.
    * Si no existe una transición, la cadena no es aceptada. Si se procesan todos los símbolos y el estado final es un estado de aceptación, la cadena es aceptada.
    * param cadena La cadena a validar.
    * @Return true si la cadena es aceptada, false en caso contrario.
    */
    @Override 
    public boolean validarCadena(String cadena) {

        //estado actual
        Estado estadoActual = estadoInicial;

        // Recorremos cada símbolo de la cadena
        for (char simbolo : cadena.toCharArray()) {
            if (transiciones.get(estadoActual) == null || !transiciones.get(estadoActual).containsKey(simbolo)) {
                return false; // No hay transición definida para este símbolo desde el estado actual
            }

            estadoActual = transiciones.get(estadoActual).get(simbolo); // Avanzamos al siguiente estado
        }

        return estadosAceptacion.contains(estadoActual); // 
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