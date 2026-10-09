package characterModule;

import setModule.MyArraySet;
import setModule.MySet;

public final class Personaje {
    private final String nombre;
    private final MySet<Habilidad> habilidades = new MyArraySet<>();

    public Personaje(String nombre) { this.nombre = Habilidad.normalizar(nombre); }

    public boolean tiene(Habilidad habilidad) { return habilidades.contains(habilidad); }
    public void asignar(Habilidad habilidad) { habilidades.add(habilidad); }
    public void quitar(Habilidad habilidad) { habilidades.remove(habilidad); }

    /** Copia defensiva: el consumidor no puede modificar el conjunto interno. */
    public MySet<Habilidad> getHabilidades() {
        return habilidades.union(new MyArraySet<>());
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof Personaje && Habilidad.clave(nombre)
                .equals(Habilidad.clave(((Personaje) other).nombre));
    }

    @Override
    public int hashCode() { return Habilidad.clave(nombre).hashCode(); }

    @Override
    public String toString() { return nombre; }
}
