package listModule;

/**
 * Especificación del TDA List.
 *
 * Define QUÉ operaciones existen y qué hace cada una, sin decir nada
 * sobre CÓMO se guardan los datos. Cualquier clase que implemente esta
 * interfaz debe cumplir este contrato.
 *
 * Contrato de errores: los métodos que reciben un índice lanzan
 * IndexOutOfBoundsException si el índice no es válido, y los que reciben
 * un elemento lanzan IllegalArgumentException si el elemento es null.
 * Es responsabilidad de quien usa la lista evitar esas situaciones.
 */
public interface MyList<T> {

    /** Agrega el elemento al final de la lista. */
    void add(T element);

    /** Inserta el elemento en la posición indicada, corriendo el resto. */
    void add(int index, T element);

    /** Devuelve el elemento de la posición indicada. */
    T get(int index);

    /** Reemplaza el elemento de la posición indicada y devuelve el anterior. */
    T set(int index, T element);

    /** Elimina el elemento de la posición indicada y lo devuelve. */
    T remove(int index);

    /** Elimina la primera aparición del elemento. Devuelve si lo encontró. */
    boolean remove(T element);

    /** Posición de la primera aparición del elemento, o -1 si no está. */
    int indexOf(T element);

    /** Indica si el elemento está en la lista. */
    boolean contains(T element);

    /** Cantidad de elementos almacenados. */
    int size();

    /** Indica si la lista no tiene elementos. */
    boolean isEmpty();

    /** Vacía la lista. */
    void clear();
}
