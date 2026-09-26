package service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import model.AFD;
import model.Estado;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Queue;
import java.util.LinkedList;
import java.util.ArrayList;

/**
 * Clase encargada de minimizar un Autómata Finito Determinista (AFD).
 */
public class Minimizador {

    /**
     * Minimiza un Autómata Finito Determinista (AFD).
     * @param afdOriginal El AFD a minimizar.
     * @return El AFD minimizado.
     */
    public AFD minimizar(AFD afdOriginal) {

        /*
        * Obtiene los componentes del AFD original.
        */

        Set<Character> alfabeto = afdOriginal.getAlfabeto();
        /*
        * Obtiene el estado inicial del AFD original.
        */
        Estado estadoInicial = afdOriginal.getEstadoInicial();
        
        /*
        * Obtiene los estados de aceptación del AFD original.
        */
        Set<Estado> estadosAceptacion = afdOriginal.getEstadosAceptacion();
        
        /*
        * Obtiene las transiciones del AFD original.
        */
        Map<Estado, Map<Character, Estado>> transiciones = afdOriginal.getTransiciones();

        /*
        * Obtiene el conjunto de estados alcanzables desde el estado inicial del AFD.
        */
        Set<Estado> estadosAlcanzables = obtenerEstadosAlcanzables(afdOriginal);

        /*
        * Obtiene los estados de aceptación que son alcanzables.
        */
        Set<Estado> aceptacionAlcanzables = new HashSet<>(estadosAceptacion);
        aceptacionAlcanzables.retainAll(estadosAlcanzables);

        /*
        * Obtiene los estados de no aceptación que son alcanzables.
        */
        Set<Estado> noAceptacionAlcanzables = new HashSet<>(estadosAlcanzables);
        noAceptacionAlcanzables.removeAll(aceptacionAlcanzables);

        List<Set<Estado>> particiones = new ArrayList<>();
        if (!aceptacionAlcanzables.isEmpty()) {
            particiones.add(aceptacionAlcanzables);
        }
        if (!noAceptacionAlcanzables.isEmpty()) {
            particiones.add(noAceptacionAlcanzables);
        }

        boolean cambios;
        do {
            cambios = false;
            List<Set<Estado>> nuevasParticiones = new ArrayList<>();

            for (Set<Estado> grupo : particiones) {
                Map<List<Integer>, Set<Estado>> subgrupos = new HashMap<>();

                for (Estado estado : grupo) {
                    List<Integer> firma = obtenerFirmaTransiciones(estado, alfabeto, transiciones, particiones);
                    subgrupos.computeIfAbsent(firma, k -> new HashSet<>()).add(estado);
                }

                if (subgrupos.size() > 1) {
                    cambios = true;
                }
                nuevasParticiones.addAll(subgrupos.values());
            }
            particiones = nuevasParticiones;
        } while (cambios);

        return construirAFDMinimizado(particiones, alfabeto, estadoInicial, transiciones, estadosAceptacion);
    }

    /*
    * Obtiene el conjunto de estados alcanzables desde el estado inicial del AFD.
    * @param afd El AFD del cual se obtendrán los estados alcanzables.
    * @return Un conjunto de estados alcanzables.
    */
    private Set<Estado> obtenerEstadosAlcanzables(AFD afd) {

        /*
         * Inicializa el conjunto de estados alcanzables y la cola de estados por visitar.
         */
        Set<Estado> alcanzables = new HashSet<>();
        Queue<Estado> cola = new LinkedList<>();
        
        cola.add(afd.getEstadoInicial());
        alcanzables.add(afd.getEstadoInicial());

        /*
         * Procesa los estados en la cola, agregando a los alcanzables aquellos que no han sido visitados.
         */
        Map<Estado, Map<Character, Estado>> transiciones = afd.getTransiciones();

        /*
        * Mientras que la cola no este vacia, osea este disponible, se procesan los estados.
        */
        while (!cola.isEmpty()) {
            Estado actual = cola.poll();
            Map<Character, Estado> transEstado = transiciones.get(actual);

            if (transEstado != null) {
                for (Estado destino : transEstado.values()) {
                    if (alcanzables.add(destino)) {
                        cola.add(destino);
                    }
                }
            }
        }
        return alcanzables;
    }

    /*
    * Obtiene la firma de transiciones de un estado dado, que representa a qué partición pertenece cada estado alcanzable desde él para cada símbolo del alfabeto.
    * @param estado El estado del cual se obtendrá la firma.
    * @param alfabeto El conjunto de símbolos del alfabeto.
    * @param transiciones El mapa de transiciones del AFD.
    * @param particiones La lista de particiones actuales de estados.
    * @return Una lista que representa la firma de transiciones del estado.
    */
    private List<Integer> obtenerFirmaTransiciones(Estado estado, Set<Character> alfabeto, Map<Estado, Map<Character, Estado>> transiciones, List<Set<Estado>> particiones) {

        /*
        * Inicializa la lista de firma y obtiene las transiciones del estado actual.
        */
        List<Integer> firma = new ArrayList<>();
        Map<Character, Estado> transEstado = transiciones.get(estado);

        /*
         * Recorre cada símbolo del alfabeto y determina a qué partición pertenece el estado destino.
         */
        for (Character simbolo : alfabeto) {
            Estado destino = (transEstado != null) ? transEstado.get(simbolo) : null;
            int indiceParticion = -1;

            /*
            * Si hay destino, se busca a qué partición pertenece. Si no hay destino, se asigna -1.
            */
            if (destino != null) {
                for (int i = 0; i < particiones.size(); i++) {
                    if (particiones.get(i).contains(destino)) {
                        indiceParticion = i;
                        break;
                    }
                }
            }
            firma.add(indiceParticion);
        }
        return firma;
    }

    /*
    * Construye un nuevo AFD minimizado a partir de las particiones de estados.
    * @param particiones La lista de particiones de estados.
    * @param alfabeto El conjunto de símbolos del alfabeto.
    * @param estadoInicialOriginal El estado inicial del AFD original.
    * @param transicionesOriginales El mapa de transiciones del AFD original.
    * @param aceptacionOriginales El conjunto de estados de aceptación del AFD original.
    * @return Un nuevo AFD minimizado.
    */
    private AFD construirAFDMinimizado(List<Set<Estado>> particiones, Set<Character> alfabeto,  Estado estadoInicialOriginal, Map<Estado, Map<Character, Estado>> transicionesOriginales, Set<Estado> aceptacionOriginales) {
        
        /*
        Crea un mapeo de estado del AFD original al nuevo estado representativo en el AFD minimizado.
        */
        Map<Estado, Estado> mapeoEstadoNuevo = new HashMap<>();      
        
        /*
        Crea los nuevos estados del AFD minimizado.
        */
        Set<Estado> nuevosEstados = new HashSet<>();
       
        /*
        Crea los nuevos estados de aceptación del AFD minimizado.
        */
        Set<Estado> nuevosAceptacion = new HashSet<>();
        
        /*
        Crea el nuevo estado inicial del AFD minimizado.
        */
        Estado nuevoEstadoInicial = null;

        /*
        Crea los nuevos estados de transición del AFD minimizado.
        */
        int id = 0;
        
        /*
        Itera sobre cada grupo de estados en las particiones.
        */
        for (Set<Estado> grupo : particiones) {
            Estado representante = new Estado("S" + id++);
            nuevosEstados.add(representante);

            /*
            Asigna el estado representante a cada estado del grupo.
            */
            for (Estado e : grupo) {
                mapeoEstadoNuevo.put(e, representante);
                if (e.equals(estadoInicialOriginal)) {
                    nuevoEstadoInicial = representante;
                }
                if (aceptacionOriginales.contains(e)) {
                    nuevosAceptacion.add(representante);
                }
            }
        }

        /*
        Crea las nuevas transiciones del AFD minimizado.
        */
        Map<Estado, Map<Character, Estado> > nuevasTransiciones = new HashMap<>();

        /*
        Itera sobre cada grupo de estados en las particiones.
        */
        for (Set<Estado> grupo : particiones) {
            Estado representanteOrigen = mapeoEstadoNuevo.get(grupo.iterator().next());
            Estado miembroCualquiera = grupo.iterator().next();
            Map<Character, Estado> transOriginal = transicionesOriginales.get(miembroCualquiera);
            
            /*
            Procesa las transiciones originales y crea las nuevas transiciones mapeadas.
            */
            if (transOriginal != null) {
                Map<Character, Estado> transNueva = new HashMap<>();
                for (Map.Entry<Character, Estado> entry : transOriginal.entrySet()) {
                  Estado destinoMapeado = mapeoEstadoNuevo.get(entry.getValue());
                  transNueva.put(entry.getKey(), destinoMapeado);
                }
                nuevasTransiciones.put(representanteOrigen, transNueva);
            }
        }
        return new AFD(nuevosEstados, alfabeto, nuevoEstadoInicial, nuevosAceptacion, nuevasTransiciones);
    }
}