package Tinder;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Representa un usuario dentro del sistema Tinder.
 * <p>
 * Cada usuario cuenta con un identificador numérico único y una lista
 * de aficiones cargadas a partir del formato de texto del fichero de datos.
 * </p>

 * @author Dani
 * @version 1.0
 */
public class Usuario {

    /** Identificador numérico del usuario (extraído sin el prefijo 'U'). */
    private int idUsuario;

    /** Lista de aficiones o gustos asociados al usuario. */
    private ArrayList<String> aficiones = new ArrayList<>();

    /**
     * Constructor que procesa una línea de texto del fichero de datos para
     * instanciar un nuevo usuario.
     * <p>
     * Espera una cadena estructurada con el formato {@code "U1 AFICION1 AFICION2 ..."},
     * donde el primer elemento es el ID con prefijo 'U' y los siguientes son las aficiones.
     * </p>
     *
     * @param datos Cadena de texto completa extraída del archivo.
     */
    public Usuario(String datos) {
        // Divide la cadena de texto por espacios en blanco
        String[] datosUsuario = datos.split(" ");

        // El primer elemento (posición 0) corresponde al ID del usuario (ej: "U1")
        setIdUsuario(datosUsuario[0]);

        // Los elementos restantes (a partir del índice 1) son las aficiones
        aficiones.addAll(Arrays.asList(datosUsuario).subList(1, datosUsuario.length));
    }

    /**
     * Establece el identificador numérico del usuario a partir de su representación en cadena.
     * <p>
     * Elimina el primer carácter (la 'U') mediante {@link String#substring(int)}
     * y convierte el resto a un valor entero de tipo {@code int}.
     * </p>

     * @param idUsuario Cadena con el ID del usuario en formato de texto (ej: "U12").
     */
    public void setIdUsuario(String idUsuario) {
        this.idUsuario = Integer.parseInt(idUsuario.substring(1));
    }

    /**
     * Obtiene el identificador numérico del usuario.
     *
     * @return Número de ID del usuario.
     */
    public int getIdUsuario() {
        return idUsuario;
    }

    /**
     * Establece la lista completa de aficiones del usuario.
     *
     * @param aficiones ArrayList de cadenas con las nuevas aficiones.
     */
    public void setAficiones(ArrayList<String> aficiones) {
        this.aficiones = aficiones;
    }

    /**
     * Obtiene la lista de aficiones registradas del usuario.
     *
     * @return ArrayList de cadenas con las aficiones.
     */
    public ArrayList<String> getAficiones() {
        return aficiones;
    }
}