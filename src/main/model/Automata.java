package model;

import java.util.Set;

/**
 * Clase abstracta que representa un autómata finito.
 */
public abstract class Automata {

    /*
    * Atributos de la clase Automata
    */
    protected Set<Estado> estados;
    protected Set<Character> alfabeto;
    protected Estado estadoInicial;
    protected Set<Estado> estadosAceptacion;
    
    /*
     * Constructor por defecto
     */
    public Automata() {
    }

    /*
     * Constructor con parámetros
     */
    public Automata(Set<Estado> estados, Set<Character> alfabeto, Estado estadoInicial, Set<Estado> estadosAceptacion) {
        this.estados = estados;
        this.alfabeto = alfabeto;
        this.estadoInicial = estadoInicial;
        this.estadosAceptacion = estadosAceptacion;
    }

    /**
     * Valida si una cadena pertenece al lenguaje del autómata.
     * @param cadena La cadena a validar.
     * @return true si la cadena es válida, false en caso contrario.
     */
    public abstract boolean validarCadena(String cadena);

    /*
        * Getters y Setters
    */
    public Set<Estado> getEstados() {
        return estados;
    }

    public void setEstados(Set<Estado> estados) {
        this.estados = estados;
    }

    public Set<Character> getAlfabeto() {
        return alfabeto;
    }

    public void setAlfabeto(Set<Character> alfabeto) {
        this.alfabeto = alfabeto;
    }

    public Estado getEstadoInicial() {
        return estadoInicial;
    }

    public void setEstadoInicial(Estado estadoInicial) {
        this.estadoInicial = estadoInicial;
    }

    public Set<Estado> getEstadosAceptacion() {
        return estadosAceptacion;
    }

    public void setEstadosAceptacion(Set<Estado> estadosAceptacion) {
        this.estadosAceptacion = estadosAceptacion;
    }
}