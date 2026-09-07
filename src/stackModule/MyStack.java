package stackModule;

/**
 * TDA Stack: estructura LIFO (el ultimo en entrar es el primero en salir).
 * No admite null. pop y peek lanzan IllegalStateException si esta vacia.
 * Stack reutilizable para cualquier otro ejercicio en el futuro.
 */
public interface MyStack<E> {
    public void push(E element);

    /** elimina y devuelve el elemento superior de una pila,. **/
    public E pop();

    /** Te permite ver ese elemento superior sin eliminarlo.. **/
    public E peek();

    public boolean isEmpty();

    public int size();

    public void clear();
}
