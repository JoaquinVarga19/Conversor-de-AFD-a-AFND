package ui;

import service.Conversor;
import service.Minimizador;

/**
 * Clase que representa el menú principal de la aplicación.
 */
public class MenuPrincipal {

    private ConsolaUI consola;
    private Conversor conversor;
    private Minimizador minimizador;

    public MenuPrincipal() {
        this.consola = new ConsolaUI();
        this.conversor = new Conversor();
        this.minimizador = new Minimizador();
    }

    public void iniciar() {
        int opcion = 0;
        do {
            consola.mostrarMensaje("\n========================================");
            consola.mostrarMensaje("      SIMULADOR DE AUTÓMATAS FINITOS    ");
            consola.mostrarMensaje("========================================");
            consola.mostrarMensaje("1. Cargar y Validar Cadena (AFD / AFND)");
            consola.mostrarMensaje("2. Convertir AFND a AFD (Subconjuntos)");
            consola.mostrarMensaje("3. Minimizar AFD");
            consola.mostrarMensaje("4. Salir");

            opcion = consola.leerEntero("Seleccione una opción: ");

            switch (opcion) {
                case 1:
                    consola.mostrarMensaje("\n[Funcionalidad de validación en desarrollo o lista con tus archivos IO].");
                    break;
                case 2:
                    consola.mostrarMensaje("\n[Aquí puedes integrar la lectura del AFND y llamar a conversor.convertirAFNDaAFD(afnd)].");
                    break;
                case 3:
                    consola.mostrarMensaje("\n[Aquí puedes integrar la lectura del AFD y llamar a minimizador.minimizar(afd)].");
                    break;
                case 4:
                    consola.mostrarMensaje("\n¡Saliendo del programa. Hasta luego!");
                    break;
                default:
                    consola.mostrarMensaje("\nOpción inválida. Intente nuevamente.");
            }

        } while (opcion != 4);
        
        consola.cerrar();
    }
}