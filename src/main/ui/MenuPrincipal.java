package ui;

import exceptions.AutomataException;
import io.AutomataEscritura;
import io.AutomataLectura;
import model.AFD;
import model.AFND;
import model.Automata;
import model.Estado;
import service.Conversor;
import service.Minimizador;
import service.Validador;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Clase que representa el menú principal de la aplicación.
 */
public class MenuPrincipal {

    /*
     * Longitud máxima de las cadenas que se prueban al verificar equivalencia.
     */
    private static final int LONGITUD_MAXIMA_VERIFICACION = 6;

    private ConsolaUI consola;
    private AutomataLectura lectura;
    private AutomataEscritura escritura;
    private Conversor conversor;
    private Minimizador minimizador;
    private Validador validador;

    /*
     * Estado de la sesión.
     * original: el autómata tal cual se cargó del archivo (AFD o AFND).
     * afd: el AFD para minimizar (el mismo original si ya era AFD, o el resultado de convertir).
     * afdMinimo: el AFD mínimo, una vez minimizado.
     */
    private Automata original;
    private AFD afd;
    private AFD afdMinimo;

    public MenuPrincipal() {
        this.consola = new ConsolaUI();
        this.lectura = new AutomataLectura();
        this.escritura = new AutomataEscritura();
        this.conversor = new Conversor();
        this.minimizador = new Minimizador();
        this.validador = new Validador();
    }

    public void iniciar() {
        int opcion = 0;
        do {
            consola.mostrarMensaje("\n========================================");
            consola.mostrarMensaje("      SIMULADOR DE AUTÓMATAS FINITOS    ");
            consola.mostrarMensaje("========================================");
            consola.mostrarMensaje("1. Cargar autómata desde archivo");
            consola.mostrarMensaje("2. Convertir AFND a AFD (subconjuntos)");
            consola.mostrarMensaje("3. Minimizar AFD");
            consola.mostrarMensaje("4. Validar una cadena");
            consola.mostrarMensaje("5. Verificar equivalencia y ver tabla comparativa");
            consola.mostrarMensaje("6. Guardar AFD mínimo en archivo");
            consola.mostrarMensaje("7. Ejecutar proceso completo");
            consola.mostrarMensaje("8. Salir");

            opcion = consola.leerEntero("Seleccione una opción: ");

            try {
                switch (opcion) {
                    case 1 -> accionCargar();
                    case 2 -> accionConvertir();
                    case 3 -> accionMinimizar();
                    case 4 -> accionValidarCadena();
                    case 5 -> accionVerificar();
                    case 6 -> accionGuardar();
                    case 7 -> accionProcesoCompleto();
                    case 8 -> consola.mostrarMensaje("\n¡Saliendo del programa. Hasta luego!");
                    case -1 -> opcion = 8; // se terminó la entrada (Ctrl+D / EOF)
                    default -> consola.mostrarMensaje("\nOpción inválida. Intente nuevamente.");
                }
            } catch (AutomataException e) {
                consola.mostrarError(e.getMessage());
            }

        } while (opcion != 8);

        consola.cerrar();
    }

    private void accionCargar() {
        String ruta = consola.leerCadena("Ruta del archivo de entrada: ");
        if (ruta == null || ruta.isEmpty()) {
            consola.mostrarError("No se ingresó ninguna ruta.");
            return;
        }
        cargar(ruta);
    }

    private void cargar(String ruta) {
        Automata leido = lectura.leerAutomata(ruta);

        original = leido;
        afd = (leido instanceof AFD d) ? d : null;
        afdMinimo = null;

        consola.mostrarMensaje("Autómata cargado:");
        consola.mostrarAutomata(leido);
        if (afd != null) {
            consola.mostrarMensaje("Ya es un AFD: no hace falta convertirlo, se puede minimizar directamente.");
        }
    }

    private void accionConvertir() {
        if (!hayOriginal()) {
            return;
        }
        if (original instanceof AFD) {
            consola.mostrarMensaje("El autómata cargado ya es un AFD, no hace falta convertirlo.");
            return;
        }

        afd = conversor.convertirAFNDaAFD((AFND) original);
        afdMinimo = null;

        consola.mostrarMensaje("AFD resultante de la conversión:");
        consola.mostrarAutomata(afd);
        consola.mostrarMensaje(validador.compararCantidadEstados(original, afd, "AFND", "AFD"));
    }

    private void accionMinimizar() {
        if (!hayOriginal()) {
            return;
        }
        if (afd == null) {
            consola.mostrarError("Primero convertí el AFND a AFD (opción 2).");
            return;
        }

        afdMinimo = minimizador.minimizar(afd);

        consola.mostrarMensaje("AFD mínimo:");
        consola.mostrarAutomata(afdMinimo);
        consola.mostrarMensaje(validador.compararCantidadEstados(afd, afdMinimo, "AFD", "AFD mínimo"));
    }

    private void accionValidarCadena() {
        if (!hayOriginal()) {
            return;
        }

        String cadena = consola.leerCadena("Cadena a validar (Enter = cadena vacía): ");
        if (cadena == null) {
            cadena = "";
        }

        consola.mostrarMensaje("Resultado para \"" + cadena + "\":");
        consola.mostrarMensaje("  Original (" + tipo(original) + "): " + resultado(original, cadena));
        if (afd != null && afd != original) {
            consola.mostrarMensaje("  AFD: " + resultado(afd, cadena));
        }
        if (afdMinimo != null) {
            consola.mostrarMensaje("  AFD mínimo: " + resultado(afdMinimo, cadena));
        }
    }

    private void accionVerificar() {
        if (!hayOriginal()) {
            return;
        }
        if (afdMinimo == null) {
            consola.mostrarError("Primero minimizá el AFD (opción 3).");
            return;
        }

        Set<Character> alfabeto = original.getAlfabeto();
        boolean huboConversion = afd != original;

        consola.mostrarMensaje("Verificación de equivalencia (todas las cadenas de hasta "
                + LONGITUD_MAXIMA_VERIFICACION + " símbolos):");
        if (huboConversion) {
            compararEquivalencia("Original", original, "AFD", afd, alfabeto);
        }
        compararEquivalencia("AFD", afd, "AFD mínimo", afdMinimo, alfabeto);
        if (huboConversion) {
            compararEquivalencia("Original", original, "AFD mínimo", afdMinimo, alfabeto);
        }

        consola.mostrarMensaje("");
        consola.mostrarMensaje("Tabla comparativa:");
        consola.mostrarMensaje(String.format("  %-18s %8s %14s", "Autómata", "Estados", "Transiciones"));
        consola.mostrarMensaje(filaTabla("Original (" + tipo(original) + ")", original));
        if (huboConversion) {
            consola.mostrarMensaje(filaTabla("AFD", afd));
        }
        consola.mostrarMensaje(filaTabla("AFD mínimo", afdMinimo));
        if (original instanceof AFND) {
            consola.mostrarMensaje("  (En el AFND se cuentan también las transiciones epsilon)");
        }
    }

    private void accionGuardar() {
        if (!hayOriginal()) {
            return;
        }
        if (afdMinimo == null) {
            consola.mostrarError("Primero minimizá el AFD (opción 3).");
            return;
        }

        String ruta = consola.leerCadena("Ruta del archivo de salida: ");
        if (ruta == null || ruta.isEmpty()) {
            consola.mostrarError("No se ingresó ninguna ruta.");
            return;
        }
        escritura.escribirAutomata(afdMinimo, ruta);
        consola.mostrarMensaje("AFD mínimo guardado en: " + ruta);
    }

    private void accionProcesoCompleto() {
        String entrada = consola.leerCadena("Ruta del archivo de entrada: ");
        String salida = consola.leerCadena("Ruta del archivo de salida (AFD mínimo): ");
        if (entrada == null || entrada.isEmpty() || salida == null || salida.isEmpty()) {
            consola.mostrarError("Hay que ingresar las dos rutas.");
            return;
        }

        cargar(entrada);
        if (original instanceof AFND) {
            accionConvertir();
        }
        accionMinimizar();
        accionVerificar();

        escritura.escribirAutomata(afdMinimo, salida);
        consola.mostrarMensaje("AFD mínimo guardado en: " + salida);
    }

    /*
     * Compara dos autómatas con todas las cadenas hasta la longitud máxima y muestra el resultado.
     */
    private void compararEquivalencia(String nombreA, Automata a, String nombreB, Automata b, Set<Character> alfabeto) {
        List<String> contraejemplos = validador.obtenerContraejemplosExhaustivo(a, b, alfabeto, LONGITUD_MAXIMA_VERIFICACION);

        if (contraejemplos.isEmpty()) {
            consola.mostrarMensaje("  " + nombreA + " y " + nombreB + ": equivalentes");
        } else {
            int cantidad = Math.min(5, contraejemplos.size());
            consola.mostrarMensaje("  " + nombreA + " y " + nombreB + ": NO equivalentes. Cadenas donde difieren: "
                    + contraejemplos.subList(0, cantidad));
        }
    }

    private String filaTabla(String nombre, Automata automata) {
        return String.format("  %-18s %8d %14d", nombre, automata.getEstados().size(), contarTransiciones(automata));
    }

    /*
     * Cuenta las transiciones de un autómata. En un AFND cuenta cada destino
     * por separado, y suma las transiciones epsilon.
     */
    private int contarTransiciones(Automata automata) {
        if (automata instanceof AFD d) {
            return validador.contarTransicionesAFD(d);
        }

        AFND afnd = (AFND) automata;
        int total = 0;
        for (Map<Character, Set<Estado>> porSimbolo : afnd.getTransiciones().values()) {
            for (Set<Estado> destinos : porSimbolo.values()) {
                total += destinos.size();
            }
        }
        if (afnd.getTransicionesEpsilon() != null) {
            for (Set<Estado> destinos : afnd.getTransicionesEpsilon().values()) {
                total += destinos.size();
            }
        }
        return total;
    }

    private String tipo(Automata automata) {
        return (automata instanceof AFND) ? "AFND" : "AFD";
    }

    private String resultado(Automata automata, String cadena) {
        return validador.validarCadena(automata, cadena) ? "ACEPTA" : "RECHAZA";
    }

    private boolean hayOriginal() {
        if (original == null) {
            consola.mostrarError("Primero cargá un autómata (opción 1).");
            return false;
        }
        return true;
    }
}