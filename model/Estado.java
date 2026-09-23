package model;

import java.util.Objects;

/*
* Clase que representa un estado en un autómata finito.
*/
public class Estado {

    /*
    * Identificador del automata, ya sea q0, q1, q2, etc.
    */
    private String id;

    /*
    * Indica si el estado es de aceptación.
    */
    private boolean esAceptacion;

    /**
     * Constructor de la clase Estado.
     * @param id Identificador del estado.
     * @param esAceptacion Indica si el estado es de aceptación.
     */
    public Estado(String id) {
        this.id = id;
        this.esAceptacion = false;
    }

    /*
    * Constructor de la clase Estado, pasado al esAceptacion como parámetro.
    * @param id Identificador del estado.
    * @param esAceptacion Indica si el estado es de aceptación.
    */
    public Estado(String id, boolean esAceptacion) {
        this.id = id;
        this.esAceptacion = esAceptacion;
    }

    /*
    Getters y Setters
    */
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isEsAceptacion() {
        return esAceptacion;
    }

    public void setEsAceptacion(boolean esAceptacion) {
        this.esAceptacion = esAceptacion;
    }

    /*
    * Compara dos estados por su identificador.
    * @param obj El objeto a comparar.
    * @return true si los identificadores son iguales, false en caso contrario.
    */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Estado estado = (Estado) obj;
        return Objects.equals(id, estado.id);
    }

    /*
    * Calcula el código hash del estado.
    * @return El código hash del estado.
    */
    @Override
    public int hashCode() {
       return Objects.hash(id);
    }

    /*
    * Devuelve una representación en cadena del estado.
    * @return Una cadena que representa el estado.
    */
    @Override
    public String toString() {
        return "Estado [id=" + id + "]";
    }
}