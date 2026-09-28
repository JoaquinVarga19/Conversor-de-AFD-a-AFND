package ui;

import model.AFD;
import model.AFND;
import model.Automata;
import model.Estado;

import java.util.Comparator;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * Clase que proporciona una interfaz de usuario en consola.
 */
public class ConsolaUI {

    /*
     * Ordena los estados por largo del id y después alfabéticamente,
     * así q2 queda antes que q10.
     */
    private static final Comparator<Estado> ORDEN_ESTADOS =
            Comparator.comparingInt((Estado e) -> e.getId().length()).thenComparing(Estado::getId);

    /*
     * Atributo que representa el scanner para leer la entrada del usuario.
     */
    private Scanner scanner;

    /*
     * Constructor de la clase.
     * Inicializa el scanner.
     */
    public ConsolaUI() {
        scanner = new Scanner(System.in);
    }

    /*
     * Método para mostrar un mensaje en la consola.
     * @param mensaje El mensaje a mostrar.
     */
    public void mostrarMensaje(String mensaje) {
        System.out.println(mensaje);
    }

    /*
     * Método para mostrar un mensaje de error en la consola.
     * @param mensaje El mensaje de error a mostrar.
     */
    public void mostrarError(String mensaje) {
        System.out.println("[ERROR] " + mensaje);
    }

    /*
     * Método para leer un número entero desde la consola.
     * @param mensaje El mensaje a mostrar antes de leer la entrada.
     * @return El número entero leído, o -1 si ya no hay más entrada disponible.
     */
    public int leerEntero(String mensaje) {
        System.out.print(mensaje);
        while (true) {
            if (!scanner.hasNextLine()) {
                return -1;
            }
            String linea = scanner.nextLine().trim();
            try {
                return Integer.parseInt(linea);
            } catch (NumberFormatException e) {
                System.out.print("Entrada inválida. Por favor, ingrese un número entero: ");
            }
        }
    }

    /**
     * Método para leer una cadena de texto desde la consola.
     * @param mensaje El mensaje a mostrar antes de leer la entrada.
     * @return La cadena de texto leída, o null si ya no hay más entrada disponible.
     */
    public String leerCadena(String mensaje) {
        System.out.print(mensaje);
        if (!scanner.hasNextLine()) {
            return null;
        }
        return scanner.nextLine().trim();
    }

    /*
     * Muestra un autómata completo: tipo, alfabeto, estados, inicial, finales y transiciones.
     * @param automata El autómata a mostrar (AFD o AFND).
     */
    public void mostrarAutomata(Automata automata) {
        boolean esAFND = automata instanceof AFND;

        System.out.println("  Tipo: " + (esAFND ? "AFND" : "AFD"));
        System.out.println("  Alfabeto: " + unirSimbolos(automata.getAlfabeto()));
        System.out.println("  Estados: " + unirEstados(automata.getEstados()));
        System.out.println("  Estado inicial: " + automata.getEstadoInicial().getId());
        System.out.println("  Estados finales: " + unirEstados(automata.getEstadosAceptacion()));
        System.out.println("  Transiciones:");

        if (automata instanceof AFD afd) {
            mostrarTransicionesAFD(afd);
            System.out.println("  (Las transiciones no listadas llevan a un estado de rechazo implícito)");
        } else if (automata instanceof AFND afnd) {
            mostrarTransicionesAFND(afnd);
        }
    }

    private void mostrarTransicionesAFD(AFD afd) {
        Map<Estado, Map<Character, Estado>> transiciones = afd.getTransiciones();

        for (Estado origen : transiciones.keySet().stream().sorted(ORDEN_ESTADOS).collect(Collectors.toList())) {
            Map<Character, Estado> porSimbolo = transiciones.get(origen);
            for (Character simbolo : new TreeSet<>(porSimbolo.keySet())) {
                System.out.println("    " + origen.getId() + " --" + simbolo + "--> " + porSimbolo.get(simbolo).getId());
            }
        }
    }

    private void mostrarTransicionesAFND(AFND afnd) {
        Map<Estado, Map<Character, Set<Estado>>> transiciones = afnd.getTransiciones();

        for (Estado origen : transiciones.keySet().stream().sorted(ORDEN_ESTADOS).collect(Collectors.toList())) {
            Map<Character, Set<Estado>> porSimbolo = transiciones.get(origen);
            for (Character simbolo : new TreeSet<>(porSimbolo.keySet())) {
                System.out.println("    " + origen.getId() + " --" + simbolo + "--> " + unirEstados(porSimbolo.get(simbolo)));
            }
        }

        Map<Estado, Set<Estado>> epsilon = afnd.getTransicionesEpsilon();
        if (epsilon != null && !epsilon.isEmpty()) {
            System.out.println("  Transiciones epsilon:");
            for (Estado origen : epsilon.keySet().stream().sorted(ORDEN_ESTADOS).collect(Collectors.toList())) {
                System.out.println("    " + origen.getId() + " --eps--> " + unirEstados(epsilon.get(origen)));
            }
        }
    }

    private String unirSimbolos(Set<Character> simbolos) {
        return new TreeSet<>(simbolos).stream()
                .map(String::valueOf)
                .collect(Collectors.joining(", ", "{", "}"));
    }

    private String unirEstados(Set<Estado> estados) {
        return estados.stream()
                .sorted(ORDEN_ESTADOS)
                .map(Estado::getId)
                .collect(Collectors.joining(", ", "{", "}"));
    }

    /*
     * Método para cerrar el scanner.
     * Cierra el scanner para liberar recursos.
     */
    public void cerrar() {
        if (scanner != null) {
            scanner.close();
        }
    }
}