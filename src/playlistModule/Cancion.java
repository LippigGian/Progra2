package playlistModule;

/** Representa una canción identificada por un nombre válido. */
public class Cancion {

    private final String nombre;

    public Cancion(String nombre) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException(
                    "El nombre de la canción no puede ser nulo o vacío.");
        }
        this.nombre = nombre.trim();
    }

    public String getNombre() {
        return nombre;
    }

    @Override
    public String toString() {
        return nombre;
    }
}
