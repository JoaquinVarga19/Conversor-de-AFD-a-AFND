package application;

import io.AutomataEscritura;
import io.AutomataLectura;
import model.AFND;
import model.AFD;
import service.Conversor;

public class Prueba {
    public static void main(String[] args) {
        try {
            // 1. Instanciar el lector de archivos
            AutomataLectura lector = new AutomataLectura();
            
            // 2. Leer el AFND desde el archivo afnd.txt en la raíz del proyecto
            // (Nota: asegúrate de usar el método correspondiente que creaste en AutomataLectura para AFND)
            AFND afnd = (AFND) lector.leerAutomata("afnd.txt"); // O el nombre del método que tengas en tu lector
            
            System.out.println("¡AFND leído exitosamente desde el archivo!");
            System.out.println("Estado inicial: " + afnd.getEstadoInicial().getId());
            
            // 3. Probar la conversión a AFD usando el conversor
            Conversor conversor = new Conversor();
            AFD afdResultante = conversor.convertirAFNDaAFD(afnd);
            
            System.out.println("¡Conversión a AFD por subconjuntos realizada con éxito!");

            // Guardar el resultado en un archivo de texto
            AutomataEscritura escritor = new AutomataEscritura();
            escritor.escribirAutomata(afdResultante, "afd_resultado.txt");

            System.out.println("¡AFD resultante guardado exitosamente en el archivo 'afd_resultado.txt'!");
            
        } catch (Exception e) {
            System.out.println("Ocurrió un error al procesar el autómata: " + e.getMessage());
            e.printStackTrace();
        }
    }
}