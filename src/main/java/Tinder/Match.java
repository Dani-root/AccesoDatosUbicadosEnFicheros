package Tinder;

import java.util.ArrayList;

/**
 * Representa un emparejamiento (Match) entre dos usuarios basado en sus aficiones comunes.
 * <p>
 * Implementa la interfaz {@link Comparable} para permitir la ordenación de los
 * matches según el número total de aficiones en común (de menor a mayor).
 * </p>
 *
 * @author Dani
 * @version 1.0
 */
public class Match implements Comparable<Match> {

    /** Identificador combinado del par de usuarios (formato: "U1 U2"). */
    private String idConjunto;

    /** Lista de cadenas que representa las aficiones compartidas entre ambos usuarios. */
    private ArrayList<String> aficionesComunes = new ArrayList<>();

    /**
     * Constructor para instanciar un nuevo Match a partir de dos identificadores numéricos
     * y la lista de aficiones en común.
     *
     * @param usr1 Numérico del ID del primer usuario.
     * @param usr2 Numérico del ID del segundo usuario.
     * @param aficionesComunes Lista de aficiones que coinciden entre ambos usuarios.
     */
    public Match(int usr1, int usr2, ArrayList<String> aficionesComunes) {
        // Formatea el identificador conjunto con el prefijo 'U'
        setIdConjunto("U" + usr1 + " U" + usr2);
        setAficionesComunes(aficionesComunes);
    }

    /**
     * Devuelve una representación en formato texto del match,
     * compuesta por el identificador de los usuarios y la lista de aficiones comunes.
     *
     * @return Cadena formateada para guardar en archivo o mostrar por pantalla.
     */
    @Override
    public String toString() {
        return getIdConjunto() + " " + aficionesToString();
    }

    /**
     * Compara este objeto Match con otro según la cantidad de aficiones comunes.
     * Permite ordenar colecciones de objetos Match de menor a mayor afinitud.
     *
     * @param match2 El objeto Match con el que comparar.
     * @return Un valor negativo, cero o positivo si este match tiene menos,
     *         igual o más aficiones comunes que el objeto comparado.
     */
    @Override
    public int compareTo(Match match2) {
        // Compara los tamaños de las listas de aficiones en común
        return Integer.compare(getAficionesComunes().size(), match2.getAficionesComunes().size());
    }

    /**
     * Obtiene el identificador conjunto de la pareja de usuarios.
     *
     * @return Identificador en formato "U1 U2".
     */
    public String getIdConjunto() {
        return idConjunto;
    }

    /**
     * Establece el identificador conjunto de los usuarios.
     *
     * @param idConjunto Cadena formateada del identificador conjunto.
     */
    public void setIdConjunto(String idConjunto) {
        this.idConjunto = idConjunto;
    }

    /**
     * Obtiene la lista de aficiones compartidas.
     *
     * @return ArrayList con las aficiones coincidentes.
     */
    public ArrayList<String> getAficionesComunes() {
        return aficionesComunes;
    }

    /**
     * Establece la lista de aficiones compartidas.
     *
     * @param aficionesComunes ArrayList con las aficiones coincidentes.
     */
    public void setAficionesComunes(ArrayList<String> aficionesComunes) {
        this.aficionesComunes = aficionesComunes;
    }

    /**
     * Convierte la lista de aficiones comunes en un único String separado por espacios.
     * <p>
     * Utiliza {@link StringBuilder} para optimizar el rendimiento al concatenar las cadenas.
     * </p>

     * @return Cadena con todas las aficiones compartidas separadas por espacios.
     */
    public String aficionesToString() {
        StringBuilder sb = new StringBuilder();
        for (String aficionComun : aficionesComunes) {
            sb.append(aficionComun).append(" ");
        }
        return sb.toString();
    }
}