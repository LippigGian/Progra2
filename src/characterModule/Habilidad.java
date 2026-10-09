package characterModule;

import java.util.Locale;

/** Nombre inmutable: permite usar equals sin alterar la identidad en el Set. */
public final class Habilidad {
    private final String nombre;

    public Habilidad(String nombre) {
        this.nombre = normalizar(nombre);
    }

    static String normalizar(String nombre) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre no puede estar vacio.");
        }
        return nombre.strip().replaceAll("\\s+", " ");
    }

    static String clave(String nombre) { return nombre.toLowerCase(Locale.ROOT); }

    @Override
    public boolean equals(Object other) {
        return other instanceof Habilidad
                && clave(nombre).equals(clave(((Habilidad) other).nombre));
    }

    @Override
    public int hashCode() { return clave(nombre).hashCode(); }

    @Override
    public String toString() { return nombre; }
}
