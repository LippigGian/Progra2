package playlistModule;

import java.util.Scanner;

import application.Exercise;

/** Interfaz de consola del TP 03 que utiliza el TDA Playlist. */
public class PlaylistExercise extends Exercise {

    private final Playlist playlist;

    public PlaylistExercise(Scanner scanner) {
        super(scanner);
        playlist = new Playlist();

        // Canciones temporales para facilitar las pruebas del trabajo.
        playlist.agregarCancion("Imagine");
        playlist.agregarCancion("Bohemian Rhapsody");
        playlist.agregarCancion("Billie Jean");
        playlist.agregarCancion("Hotel California");
        playlist.agregarCancion("De Música Ligera");
    }

    @Override
    protected void exerciseLogic() {
        mostrarPlaylist();
        mostrarMenu();

        String opcion = scanner.nextLine().trim();

        try {
            switch (opcion) {
                case "1":
                    agregarCancion();
                    break;
                case "2":
                    eliminarPorNombre();
                    break;
                case "3":
                    eliminarPorNumero();
                    break;
                case "4":
                    reproducir();
                    break;
                case "5":
                    detener();
                    break;
                case "6":
                    siguiente();
                    break;
                case "7":
                    anterior();
                    break;
                case "0":
                    running = false;
                    break;
                default:
                    System.out.println("Opción inválida.");
            }
        } catch (IllegalArgumentException e) {
            System.out.println("No se pudo realizar la operación: " + e.getMessage());
        }
    }

    private void mostrarMenu() {
        System.out.println("\n===== TP 03: PLAYLIST MUSICAL =====");
        System.out.println("1 - Agregar canción");
        System.out.println("2 - Remover canción por nombre");
        System.out.println("3 - Remover canción por número");
        System.out.println("4 - Reproducir");
        System.out.println("5 - Detener reproducción");
        System.out.println("6 - Siguiente canción");
        System.out.println("7 - Canción anterior");
        System.out.println("0 - Volver al menú principal");
        System.out.print("Seleccione una opción: ");
    }

    private void agregarCancion() {
        System.out.print("Nombre de la canción: ");
        String nombre = scanner.nextLine();
        playlist.agregarCancion(nombre);
        System.out.println("Canción agregada: " + nombre.trim());
    }

    private void eliminarPorNombre() {
        if (playlist.estaVacia()) {
            System.out.println("La playlist está vacía.");
            return;
        }

        System.out.print("Nombre de la canción a remover: ");
        String nombre = scanner.nextLine();

        if (playlist.eliminarCancion(nombre)) {
            System.out.println("Canción eliminada: " + nombre.trim());
        } else {
            System.out.println("No se encontró una canción con ese nombre.");
        }
    }

    private void eliminarPorNumero() {
        if (playlist.estaVacia()) {
            System.out.println("La playlist está vacía.");
            return;
        }

        System.out.print("Número de la canción a remover: ");
        String entrada = scanner.nextLine().trim();

        try {
            int posicion = Integer.parseInt(entrada);
            if (playlist.eliminarCancionPorPosicion(posicion)) {
                System.out.println("Canción eliminada correctamente.");
            } else {
                System.out.println("Ese número no corresponde a ninguna canción.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Debe ingresar un número entero.");
        }
    }

    private void reproducir() {
        if (playlist.reproducir()) {
            System.out.println("Reproduciendo: " + playlist.getCancionActual());
        } else {
            System.out.println("No se puede reproducir una playlist vacía.");
        }
    }

    private void detener() {
        if (playlist.detener()) {
            System.out.println("Reproducción detenida.");
        } else {
            System.out.println("No hay ninguna canción reproduciéndose.");
        }
    }

    private void siguiente() {
        if (playlist.siguienteCancion()) {
            System.out.println("Reproduciendo: " + playlist.getCancionActual());
        } else if (playlist.estaVacia()) {
            System.out.println("La playlist está vacía.");
        } else {
            System.out.println("Fin de la playlist.");
        }
    }

    private void anterior() {
        if (playlist.anteriorCancion()) {
            System.out.println("Reproduciendo: " + playlist.getCancionActual());
        } else if (playlist.estaVacia()) {
            System.out.println("La playlist está vacía.");
        } else {
            System.out.println("Ya se encuentra en la primera canción.");
        }
    }

    private void mostrarPlaylist() {
        System.out.println("\n---------- PLAYLIST ----------");

        if (playlist.estaVacia()) {
            System.out.println("(playlist vacía)");
        } else {
            for (int i = 0; i < playlist.getCanciones().size(); i++) {
                String indicador = "  ";
                if (i == playlist.getIndiceActual()) {
                    indicador = playlist.estaReproduciendo() ? "> " : "||";
                }
                System.out.println(indicador + " " + (i + 1) + ". "
                        + playlist.getCanciones().get(i));
            }
        }

        System.out.println("Estado: " + obtenerEstado());
        System.out.println("------------------------------");
    }

    private String obtenerEstado() {
        if (playlist.getCancionActual() == null) {
            return "sin canción seleccionada";
        }
        return (playlist.estaReproduciendo() ? "reproduciendo " : "detenida en ")
                + playlist.getCancionActual().getNombre();
    }
}
