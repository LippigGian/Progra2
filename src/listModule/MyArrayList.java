package listModule;

/**
 * Implementación del TDA List sobre un arreglo dinámico.
 *
 * Invariantes de la clase:
 *  - Las posiciones 0 .. size-1 del arreglo contienen elementos válidos.
 *  - Las posiciones size .. elements.length-1 valen null.
 *  - size <= elements.length en todo momento.
 */
public class MyArrayList<T> implements MyList<T> {

    private static final int INITIAL_CAPACITY = 4;

    private T[] elements;
    private int size;

    @SuppressWarnings("unchecked")
    public MyArrayList() {
        this.elements = (T[]) new Object[INITIAL_CAPACITY];
        this.size = 0;
    }

    @Override
    public void add(T element) {
        checkElement(element);
        ensureCapacity();
        elements[size] = element;
        size++;
    }

    @Override
    public void add(int index, T element) {
        checkElement(element);
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException(
                    "Índice " + index + " inválido para insertar. Tamaño actual: " + size);
        }
        ensureCapacity();

        // Corre una posición a la derecha, desde el final hacia el índice.
        for (int i = size; i > index; i--) {
            elements[i] = elements[i - 1];
        }

        elements[index] = element;
        size++;
    }

    @Override
    public T get(int index) {
        checkIndex(index);
        return elements[index];
    }

    @Override
    public T set(int index, T element) {
        checkElement(element);
        checkIndex(index);
        T previous = elements[index];
        elements[index] = element;
        return previous;
    }

    @Override
    public T remove(int index) {
        checkIndex(index);
        T removed = elements[index];

        // Corre una posición a la izquierda, tapando el hueco.
        for (int i = index; i < size - 1; i++) {
            elements[i] = elements[i + 1];
        }

        elements[size - 1] = null;   // libera la referencia sobrante
        size--;
        return removed;
    }

    @Override
    public boolean remove(T element) {
        int index = indexOf(element);
        if (index == -1) {
            return false;
        }
        remove(index);
        return true;
    }

    @Override
    public int indexOf(T element) {
        checkElement(element);
        for (int i = 0; i < size; i++) {
            if (elements[i].equals(element)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public boolean contains(T element) {
        return indexOf(element) != -1;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public void clear() {
        for (int i = 0; i < size; i++) {
            elements[i] = null;
        }
        size = 0;
    }

    /**
     * Capacidad actual del arreglo interno.
     * NO forma parte de la interfaz: es un detalle de esta implementación.
     * Existe solo para poder mostrar en clase cómo crece la estructura.
     */
    public int capacity() {
        return elements.length;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            builder.append(elements[i]);
            if (i < size - 1) {
                builder.append(", ");
            }
        }
        return builder.append("]").toString();
    }

    // ---------- Métodos privados de apoyo ----------

    private void checkIndex(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException(
                    "Índice " + index + " fuera de rango. Tamaño actual: " + size);
        }
    }

    private void checkElement(T element) {
        if (element == null) {
            throw new IllegalArgumentException("La lista no admite elementos nulos.");
        }
    }

    /** Si el arreglo está lleno, lo reemplaza por otro del doble de tamaño. */
    @SuppressWarnings("unchecked")
    private void ensureCapacity() {
        if (size < elements.length) {
            return;
        }

        T[] bigger = (T[]) new Object[elements.length * 2];
        for (int i = 0; i < size; i++) {
            bigger[i] = elements[i];
        }
        elements = bigger;
    }
}
