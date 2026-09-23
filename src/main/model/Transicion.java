package model;

import java.util.Objects;

/**
 * Clase que representa una transición en un autómata finito.
 */
public class Transicion {

    /**
     * Estado de origen de la transición.
     */
    private Estado origen;
    
    /**
     * Símbolo que activa la transición.
     */
    private char simbolo;

    /*
     * Estado de destino de la transición.
     */
    private Estado destino;

    /**
     * Constructor de la clase Transicion.
     * @param origen Estado de origen.
     * @param simbolo Símbolo que activa la transición.
     * @param destino Estado de destino.
     */
    public Transicion(Estado origen, char simbolo, Estado destino) {
        this.origen = origen;
        this.simbolo = simbolo;
        this.destino = destino;
    }

    /*
    * Getters y Setters
    */
    public Estado getOrigen() {
        return origen;
    }

    public void setOrigen(Estado origen) {
        this.origen = origen;
    }

    public char getSimbolo() {
        return simbolo;
    }

    public void setSimbolo(char simbolo) {
        this.simbolo = simbolo;
    }

    public Estado getDestino() {
        return destino;
    }

    public void setDestino(Estado destino) {
        this.destino = destino;
    }

    /*
    * Compara dos transiciones por su estado de origen, símbolo y estado de destino.
    * @param obj El objeto a comparar.
    * @return true si las transiciones son iguales, false en caso contrario.
    */
    @Override
    public int hashCode() {
        return Objects.hash(origen, simbolo, destino);
    }

    /*
    * Compara dos transiciones por su estado de origen, símbolo y estado de destino.
    * @param obj El objeto a comparar.
    * @return true si las transiciones son iguales, false en caso contrario.
    */
    @Override
    public boolean equals(Object obj) {
    if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Transicion that = (Transicion) obj;
        return simbolo == that.simbolo && 
               Objects.equals(origen, that.origen) && 
               Objects.equals(destino, that.destino);
    }

    /*
    * Devuelve una representación en cadena de la transición.
    * @return Una cadena que representa la transición.
    */
    @Override
    public String toString() {
        return "(" + origen + ", " + simbolo + ") -> " + destino;
    }
}