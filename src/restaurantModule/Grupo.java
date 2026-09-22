package restaurantModule;

/** Datos del grupo que se registra, sin cambios posteriores. */
public class Grupo {
    private final String nombre;
    private final int personas;

    public Grupo(String nombre, int personas) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre no puede estar vacio.");
        }
        if (personas < 1 || personas > 4) {
            throw new IllegalArgumentException("Solo se admiten grupos de 1 a 4 personas.");
        }
        this.nombre = nombre.trim();
        this.personas = personas;
    }

    public String getNombre() {
        return nombre;
    }

    public int getPersonas() {
        return personas;
    }

    @Override
    public String toString() {
        return nombre + " (" + personas + " personas)";
    }
}
