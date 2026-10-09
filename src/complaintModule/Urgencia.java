package complaintModule;

/** Menor numero significa mayor prioridad en SimplePriorityQueue. */
public enum Urgencia {
    CRITICO("Critico", 1),
    ALTO("Alto", 2),
    MEDIO("Medio", 3),
    BAJO("Bajo", 4);

    private final String nombre;
    private final int prioridad;

    Urgencia(String nombre, int prioridad) {
        this.nombre = nombre;
        this.prioridad = prioridad;
    }

    public int getPrioridad() {
        return prioridad;
    }

    @Override
    public String toString() {
        return nombre;
    }
}
