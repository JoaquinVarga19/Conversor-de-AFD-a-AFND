package ui;

import java.util.Scanner;

/**
 * Clase que proporciona una interfaz de usuario en consola.
 */
public class ConsolaUI {

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
     * Método para leer un número entero desde la consola.
     * @param mensaje El mensaje a mostrar antes de leer la entrada.
     * @return El número entero leído.
     */
    public int leerEntero(String mensaje) {
        System.out.print(mensaje);
        while (!scanner.hasNextInt()) {
            System.out.println("Entrada inválida. Por favor, ingrese un número entero.");
            scanner.next(); // Limpiar entrada incorrecta
        }
        int valor = scanner.nextInt();
        scanner.nextLine(); // Limpiar el buffer
        return valor;
    }

    /**
     * Método para leer una cadena de texto desde la consola.
     * @param mensaje El mensaje a mostrar antes de leer la entrada.
     * @return La cadena de texto leída.
     */
    public String leerCadena(String mensaje) {
        System.out.print(mensaje);
        return scanner.nextLine();
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
