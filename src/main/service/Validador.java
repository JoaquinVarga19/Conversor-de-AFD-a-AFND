package service;

import model.AFD;
import model.Estado;
import java.util.Set;
import model.Automata;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

/**
 * Servicio encargado de validar cadenas contra un autómata y de verificar
 * la equivalencia entre dos autómatas (por ejemplo: AFND original vs AFD,
 * o AFD vs AFD minimizado).
 */
public class Validador {

    /*
    Valida una cadena contra un autómata dado. Retorna true si la cadena es aceptada por el autómata, false en caso contrario.
    @param automata El autómata contra el cual se validará la cadena.
    @param cadena La cadena que se desea validar.
    @return true si la cadena es aceptada por el autómata, false en caso contrario.
    */
    public boolean validarCadena(Automata automata, String cadena) {
        return automata.validarCadena(cadena);
    }

    /*
    Verifica si dos autómatas son equivalentes.
    @param automata1 El primer autómata.
    @param automata2 El segundo autómata.
    @param cadenasDePrueba La lista de cadenas de prueba.
    @return true si los autómatas son equivalentes, false en caso contrario.
    */
    public boolean sonEquivalentes(Automata automata1, Automata automata2, List<String> cadenasDePrueba) {
        return obtenerContraEjemplos(automata1, automata2, cadenasDePrueba).isEmpty(); 
    }

    /*
    * Devuelve las cadenas en las que dos autómatas difieren (una la acepta
    * y la otra no). Una lista vacía significa que son equivalentes
    * respecto a las cadenas probadas.
    * @param automata1 El primer autómata.
    * @param automata2 El segundo autómata.
    * @param cadenasDePrueba La lista de cadenas de prueba.
    * @return Una lista de cadenas que son contraejemplos de la equivalencia entre los dos autómatas.
    * Si la lista está vacía, significa que los autómatas son equivalentes respecto a las cadenas probadas.
    */
    public List<String> obtenerContraEjemplos(Automata automata1, Automata automata2, List<String> cadenasDePrueba) {
        List<String> contraEjemplos = new ArrayList<>();

        for (String cadena : cadenasDePrueba) {
            if (automata1.validarCadena(cadena) != automata2.validarCadena(cadena)) {
                contraEjemplos.add(cadena);
            }
        }
        return contraEjemplos;
    }

    /*
    * Verifica equivalencia de forma exhaustiva: genera todas las cadenas
    * posibles del alfabeto hasta una longitud máxima y compara los dos
    * autómatas contra todas ellas. Útil para autómatas chicos/medianos,
    * donde probar "todas las cadenas hasta longitud N" da mucha confianza
    * sin tener que armar los casos a mano.
    */
    public boolean sonEquivalentesExhaustivo(Automata automata1, Automata automata2, Set<Character> alfabeto, int longitudMaxima) {
        return sonEquivalentes(automata1, automata2, generarCadenas(alfabeto, longitudMaxima)); 
    }

    /*
    * Igual que sonEquivalentesExhaustivo, pero devolviendo los contraejemplos
    * encontrados (para poder mostrarlos en el informe si algo falla).
    */
    public List<String> obtenerContraejemplosExhaustivo(Automata a1, Automata a2, Set<Character> alfabeto, int longitudMaxima) {
        return obtenerContraEjemplos(a1, a2, generarCadenas(alfabeto, longitudMaxima));
    }

    /*
    * Genera todas las cadenas posibles sobre el alfabeto dado, desde
    * longitud 0 (cadena vacía) hasta longitudMaxima inclusive.
    */
    private List<String> generarCadenas(Set<Character> alfabeto, int longitudMaxima) {
        return null;
    }

    /*
    * Compara la cantidad de estados entre dos autómatas (por ejemplo,
    * AFD antes y después de minimizar).
    */
    public String compararCantidadEstados(Automata antes, Automata despues, String nombreAntes, String nombreDespues) {

        int estadoAntes = antes.getEstados().size();
        int estadoDespues = despues.getEstados().size();

        StringBuilder reporte = new StringBuilder();
        reporte.append(nombreAntes).append(": ").append(estadoAntes).append(" estados\n");
        reporte.append(nombreDespues).append(": ").append(estadoDespues).append(" estados\n");

        if (estadoAntes < estadoDespues) {
            reporte.append("Se redujo la cantidad de estados en ")
                .append(estadoAntes - estadoDespues).append("\n");
        } else if (estadoDespues == estadoAntes) {
            reporte.append("La cantidad de estados se mantuvo (ya era mínimo).\n");
        } else {
            reporte.append("ADVERTENCIA: la cantidad de estados aumentó. Revisar el algoritmo.\n");
        }

        return reporte.toString();
    }

    /*
    * Cuenta la cantidad de transiciones definidas en un AFD.
    */
    public int contarTransicionesAFD(AFD afd) {
        
        int total = 0;
        
        for (Map<Character, Estado> transicionesPorEstado : afd.getTransiciones().values()) {
            total += transicionesPorEstado.size();
        }
        return total;
    }
}
