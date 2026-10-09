package complaintModule;

/** Reclamo inmutable que espera ser atendido por orden de urgencia. */
public final class Reclamo {
    private final String titulo;
    private final String descripcion;
    private final Urgencia urgencia;

    public Reclamo(String titulo, String descripcion, Urgencia urgencia) {
        if (titulo == null || titulo.isBlank()) {
            throw new IllegalArgumentException("El titulo no puede estar vacio.");
        }
        if (descripcion == null || descripcion.isBlank()) {
            throw new IllegalArgumentException("La descripcion no puede estar vacia.");
        }
        if (urgencia == null) {
            throw new IllegalArgumentException("La urgencia no puede ser null.");
        }
        this.titulo = titulo.strip();
        this.descripcion = descripcion.strip();
        this.urgencia = urgencia;
    }

    public Urgencia getUrgencia() {
        return urgencia;
    }

    @Override
    public String toString() {
        return "Titulo: " + titulo + "\nDescripcion: " + descripcion
                + "\nUrgencia: " + urgencia;
    }
}
