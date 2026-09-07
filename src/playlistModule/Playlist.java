package playlistModule;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * TDA Playlist: guarda las canciones y controla el estado de reproducción.
 * No lee ni muestra datos por consola; esa tarea pertenece a PlaylistExercise.
 */
public class Playlist {

    private final List<Cancion> canciones;
    private boolean reproduciendo;
    private int indiceActual;

    public Playlist() {
        canciones = new ArrayList<>();
        reproduciendo = false;
        // -1 significa que todavía no hay ninguna canción seleccionada.
        indiceActual = -1;
    }

    /** Devuelve una vista que puede recorrerse, pero no modificarse desde afuera. */
    public List<Cancion> getCanciones() {
        return Collections.unmodifiableList(canciones);
    }

    public boolean estaVacia() {
        return canciones.isEmpty();
    }

    public boolean estaReproduciendo() {
        return reproduciendo;
    }

    public int getIndiceActual() {
        return indiceActual;
    }

    public Cancion getCancionActual() {
        if (indiceActual == -1 || canciones.isEmpty()) {
            return null;
        }
        return canciones.get(indiceActual);
    }

    public void agregarCancion(String nombre) {
        Cancion nuevaCancion = new Cancion(nombre);

        for (Cancion cancion : canciones) {
            if (cancion.getNombre().equalsIgnoreCase(nuevaCancion.getNombre())) {
                throw new IllegalArgumentException(
                        "La canción ya existe en la playlist.");
            }
        }

        canciones.add(nuevaCancion);
    }

    public boolean eliminarCancion(String nombre) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException(
                    "El nombre de la canción no puede ser nulo o vacío.");
        }

        for (int i = 0; i < canciones.size(); i++) {
            if (canciones.get(i).getNombre().equalsIgnoreCase(nombre.trim())) {
                canciones.remove(i);
                actualizarIndiceDespuesDeEliminar(i);
                return true;
            }
        }
        return false;
    }

    /** La posición visible comienza en 1, aunque los índices de Java comienzan en 0. */
    public boolean eliminarCancionPorPosicion(int posicion) {
        if (posicion < 1 || posicion > canciones.size()) {
            return false;
        }

        int indiceEliminado = posicion - 1;
        canciones.remove(indiceEliminado);
        actualizarIndiceDespuesDeEliminar(indiceEliminado);
        return true;
    }

    private void actualizarIndiceDespuesDeEliminar(int indiceEliminado) {
        if (canciones.isEmpty()) {
            indiceActual = -1;
            reproduciendo = false;
        } else if (indiceEliminado < indiceActual) {
            indiceActual--;
        } else if (indiceEliminado == indiceActual) {
            reproduciendo = false;
            if (indiceActual >= canciones.size()) {
                indiceActual = canciones.size() - 1;
            }
        }
    }

    public boolean reproducir() {
        if (canciones.isEmpty()) {
            return false;
        }
        if (indiceActual == -1) {
            indiceActual = 0;
        }
        reproduciendo = true;
        return true;
    }

    public boolean detener() {
        if (!reproduciendo) {
            return false;
        }
        reproduciendo = false;
        return true;
    }

    public boolean siguienteCancion() {
        if (canciones.isEmpty()) {
            return false;
        }
        if (indiceActual == -1) {
            indiceActual = 0;
            reproduciendo = true;
            return true;
        }
        if (indiceActual < canciones.size() - 1) {
            indiceActual++;
            reproduciendo = true;
            return true;
        }

        // Al llegar al final no existe una siguiente canción.
        reproduciendo = false;
        return false;
    }

    public boolean anteriorCancion() {
        if (canciones.isEmpty()) {
            return false;
        }
        if (indiceActual == -1) {
            indiceActual = 0;
            reproduciendo = true;
            return true;
        }
        if (indiceActual > 0) {
            indiceActual--;
            reproduciendo = true;
            return true;
        }

        // Ya se encuentra en la primera canción.
        return false;
    }
}
