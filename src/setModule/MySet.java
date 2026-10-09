package setModule;

import listModule.MyList;

/**
 * Conjunto sin duplicados, con igualdad definida por equals.
 * null (elemento u operando), agregar duplicados y quitar ausentes son
 * datos/comandos invalidos y lanzan IllegalArgumentException.
 * Las operaciones algebraicas y toList devuelven contenedores nuevos.
 */
public interface MySet<T> {
    void add(T element);
    void remove(T element);
    boolean contains(T element);
    int size();
    boolean isEmpty();
    MySet<T> union(MySet<T> other);
    MySet<T> intersection(MySet<T> other);
    MySet<T> difference(MySet<T> other);
    MyList<T> toList();
}
