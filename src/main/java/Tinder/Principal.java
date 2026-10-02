package Tinder;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Clase principal que gestiona la aplicación de emparejamiento.
 * <p>
 * Permite seleccionar un fichero de datos de usuarios, añadir nuevos usuarios
 * con sus respectivas aficiones verificando que el ID no se repita, listar los
 * usuarios registrados y calcular las concordancias (matches) entre ellos según
 * un número mínimo de aficiones comunes.
 * </p>
 *
 * @author Dani
 * @version 1.0
 */
public class Principal {

    /**
     * Punto de entrada principal de la aplicación.
     * Contiene el bucle de interacción por consola con el menú de opciones.
     *
     * @param args Argumentos de la línea de comandos (no utilizados).
     */
    static void main(String[] args) {
        BufferedReader br;
        File[] datos = new File("datos").listFiles();

        String nombreFichero;
        boolean ficheroExistente;
        int opcion;

        // ==========================================
        // SELECCIÓN Y VALIDACIÓN DEL FICHERO DE DATOS
        // ==========================================
        do {
            // Mostrar los ficheros de texto disponibles en el directorio "datos"
            assert datos != null;
            for (File nombre : datos) {
                System.out.println("Ficheros disponibles:\n" + "- " + nombre);
            }
            nombreFichero = IO.readln("Introduce el nombre del fichero: ");

            // Intentar abrir el fichero para comprobar si existe en el sistema
            try {
                br = new BufferedReader(new FileReader("datos/" + nombreFichero));
                System.out.println("Fichero encontrado\n");
                ficheroExistente = true;
            } catch (Exception e) {
                System.out.println("Fichero no encontrado, introduzca el nombre del fichero.");
                ficheroExistente = false;
            }
        } while (!ficheroExistente);

        // ==========================================
        // BUCLE DEL MENÚ PRINCIPAL
        // ==========================================
        do {
            opcion = Integer.parseInt(IO.readln("""
            ========== MENÚ PRINCIPAL ==========
            1. Añadir usuario
            2. Mostrar usuarios introducidos
            3. Generar fichero de concordancias
            4. Salir
            ==============================
            Seleccione una opción:\s
            """));

            switch (opcion) {

                /*
                 * CASO 1: AÑADIR NUEVO USUARIO
                 * Lee el fichero para obtener el ID máximo y sugerir el siguiente disponible.
                 * Valida que el ID manual introducido no esté ya registrado.
                 */
                case 1 -> {
                    long numID = -1;
                    String aficiones;

                    try {
                        long maxID = 0;
                        boolean repetido;

                        // 1. Lectura del fichero para calcular el ID más alto existente
                        try (BufferedReader reader = new BufferedReader(new FileReader("datos/" + nombreFichero))) {
                            String linea;
                            while ((linea = reader.readLine()) != null) {
                                linea = linea.trim();
                                if (linea.startsWith("U")) {
                                    String[] partes = linea.split("\\s+");
                                    // Elimina la letra 'U' inicial para parsear el número de ID
                                    long idActual = Long.parseLong(partes[0].substring(1));
                                    if (idActual > maxID) {
                                        maxID = idActual;
                                    }
                                }
                            }
                        }

                        // El ID sugerido será siempre una unidad mayor al máximo registrado
                        long idSugerido = maxID + 1;

                        // 2. Bucle para solicitar el ID y comprobar duplicados
                        do {
                            repetido = false;

                            System.out.println("Sugerencia: El próximo ID recomendado es el " + idSugerido);
                            try {
                                numID = Long.parseLong(IO.readln("Introduce el numero de ID: "));
                            } catch (NumberFormatException e) {
                                System.out.println("ID no valido. Debe ser un numero.");
                                repetido = true;
                                continue;
                            }

                            // 3. Verificación de existencia del ID seleccionado en el archivo
                            try (BufferedReader reader = new BufferedReader(new FileReader("datos/" + nombreFichero))) {
                                String linea;
                                while ((linea = reader.readLine()) != null) {
                                    linea = linea.trim();
                                    if (linea.startsWith("U")) {
                                        String[] partes = linea.split("\\s+");
                                        long idEnFichero = Long.parseLong(partes[0].substring(1));

                                        if (idEnFichero == numID) {
                                            System.out.println("El ID " + numID + " ya existe en el fichero. Intenta con otro.");
                                            repetido = true;
                                            break;
                                        }
                                    }
                                }
                            }

                        } while (repetido);

                    } catch (Exception e) {
                        throw new RuntimeException("Error al procesar el fichero: " + e.getMessage(), e);
                    }

                    // Lectura y validación de las aficiones (cadena no vacía en mayúsculas)
                    do {
                        aficiones = IO.readln("Introduce las aficiones del U" + numID + " separadas por espacios: ").toUpperCase();
                        if (aficiones.isBlank()) {
                            System.out.println("No has introducido aficiones");
                        }
                    } while (aficiones.isBlank());

                    // Escritura en el fichero en modo 'append' (añadir al final)
                    try (FileWriter fileWriter = new FileWriter("datos/" + nombreFichero, true)) {
                        fileWriter.write("U" + numID + " " + aficiones + "\n");
                        System.out.println("Usuario U" + numID + " añadido correctamente.\n");
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }

                /*
                 * CASO 2: MOSTRAR USUARIOS
                 * Lee línea por línea el fichero activo y lo imprime por pantalla.
                 */
                case 2 -> {
                    try {
                        br = new BufferedReader(new FileReader("datos/" + nombreFichero));
                        String linea;
                        while ((linea = br.readLine()) != null) {
                            System.out.println(linea);
                        }
                    } catch (Exception e) {
                        throw new RuntimeException("Error al leer el fichero de usuarios.", e);
                    }
                }

                /*
                 * CASO 3: GENERAR CONCORDANCIAS (MATCHES)
                 * Compara las aficiones de todos los usuarios 2 a 2 y guarda las coincidencias
                 * superiores o iguales al umbral indicado en "concordancias.txt".
                 */
                case 3 -> {
                    ArrayList<Usuario> usuarios = new ArrayList<>();
                    ArrayList<Match> matches = new ArrayList<>();
                    int concordancias;

                    // Lectura del umbral mínimo de coincidencias
                    do {
                        try {
                            concordancias = Integer.parseInt(IO.readln("Introduce el numero minimo de concordancias (min 1): "));
                        } catch (Exception e) {
                            System.out.println("Numero no valido introducido");
                            concordancias = 0;
                        }
                    } while (concordancias == 0);

                    // Carga de todos los usuarios del fichero en memoria
                    try {
                        br = new BufferedReader(new FileReader("datos/" + nombreFichero));
                        String linea;
                        while ((linea = br.readLine()) != null) {
                            usuarios.add(new Usuario(linea));
                        }
                    } catch (Exception e) {
                        throw new RuntimeException("Error al instanciar usuarios desde el fichero.", e);
                    }

                    // Comparación combinatoria de todos los pares de usuarios únicos (i, j)
                    for (int i = 0; i < usuarios.size(); i++) {
                        for (int j = i + 1; j < usuarios.size(); j++) {
                            Usuario usuario1 = usuarios.get(i);
                            Usuario usuario2 = usuarios.get(j);
                            ArrayList<String> aficionesComunes = new ArrayList<>();

                            // Intersección de aficiones entre usuario1 y usuario2
                            for (int k = 0; k < usuario1.getAficiones().size(); k++) {
                                for (int l = 0; l < usuario2.getAficiones().size(); l++) {
                                    String aficion1 = usuario1.getAficiones().get(k);
                                    String aficion2 = usuario2.getAficiones().get(l);

                                    if (aficion1.equals(aficion2)) {
                                        aficionesComunes.add(aficion1);
                                    }
                                }
                            }

                            // Si alcanzan el mínimo de aficiones comunes, se crea el Match
                            if (aficionesComunes.size() >= concordancias) {
                                matches.add(new Match(usuario1.getIdUsuario(), usuario2.getIdUsuario(), aficionesComunes));
                            }
                        }
                    }

                    // Ordenación de matches según el criterio definido en la clase Match
                    Collections.sort(matches);

                    // Generación del archivo de resultados o aviso en consola
                    if (matches.isEmpty()) {
                        System.out.println("No ha habido Matches");
                    } else {
                        try (FileWriter fileWriter = new FileWriter("datos/concordancias.txt")) {
                            for (Match match : matches) {
                                fileWriter.write(match.toString() + '\n');
                            }
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }

                        System.out.println("Hay " + matches.size() + " Matches.\n");
                    }
                }

                /*
                 * CASO 4: SALIDA DEL PROGRAMA
                 */
                case 4 -> System.out.println("Chao chao");
            }
        } while (opcion != 4);
    }
}