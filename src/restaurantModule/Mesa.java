package restaurantModule;

import queueModule.MyLinkedQueue;
import queueModule.MyQueue;

/** Una mesa y su cola de espera independiente. */
public class Mesa {
    private final int capacidad;
    private final MyQueue<Grupo> espera = new MyLinkedQueue<>();
    private Grupo ocupante;

    public Mesa(int capacidad) {
        if (capacidad != 2 && capacidad != 4) {
            throw new IllegalArgumentException("La mesa debe ser de 2 o 4 personas.");
        }
        this.capacidad = capacidad;
    }

    public int getCapacidad() {
        return capacidad;
    }

    public Grupo getOcupante() {
        return ocupante;
    }

    public boolean estaLibre() {
        return ocupante == null;
    }

    public int cantidadEnEspera() {
        return espera.size();
    }

    public Grupo siguienteGrupo() {
        return espera.isEmpty() ? null : espera.peek();
    }

    public void anotar(Grupo grupo) {
        if (grupo == null || (grupo.getPersonas() <= 2 ? 2 : 4) != capacidad) {
            throw new IllegalArgumentException("El grupo no corresponde a esta mesa.");
        }
        espera.enqueue(grupo);
    }

    public Grupo hacerPasar() {
        if (!estaLibre() || espera.isEmpty()) {
            return null;
        }
        ocupante = espera.dequeue();
        return ocupante;
    }

    public boolean liberar() {
        if (estaLibre()) {
            return false;
        }
        ocupante = null;
        return true;
    }
}
