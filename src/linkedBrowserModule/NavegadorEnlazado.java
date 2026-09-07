package linkedBrowserModule;

import stackModule.MyLinkedStack;
import stackModule.MyStack;

/** Historial de navegacion independiente de la consola. */
public class NavegadorEnlazado {

    private final MyStack<String> atras = new MyLinkedStack<>();
    private final MyStack<String> adelante = new MyLinkedStack<>();
    private String paginaActual;

    public String getPaginaActual() {
        return paginaActual;
    }

    public boolean puedeRetroceder() {
        return !atras.isEmpty();
    }

    public boolean puedeAvanzar() {
        return !adelante.isEmpty();
    }

    public void visitar(String pagina) {
        if (pagina == null || pagina.isBlank()) {
            throw new IllegalArgumentException("El nombre de la pagina no puede estar vacio.");
        }
        if (paginaActual != null) {
            atras.push(paginaActual);
        }
        paginaActual = pagina.trim();
        // Una visita nueva reemplaza solo el camino hacia adelante.
        adelante.clear();
    }

    public boolean retroceder() {
        if (!puedeRetroceder()) {
            return false;
        }
        adelante.push(paginaActual);
        paginaActual = atras.pop();
        return true;
    }

    public boolean avanzar() {
        if (!puedeAvanzar()) {
            return false;
        }
        atras.push(paginaActual);
        paginaActual = adelante.pop();
        return true;
    }
}
