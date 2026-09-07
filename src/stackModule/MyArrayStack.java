package stackModule;

public class MyArrayStack<E> implements MyStack<E> {

    private E[] elements;
    private int size = 0;
    private static final int DEFAULT_SIZE = 4;

    @SuppressWarnings("unchecked")
    public MyArrayStack() {
        elements = (E[]) new Object[DEFAULT_SIZE];
    }

    @Override
    public void push(E element) {
        if (element == null) {
            throw new IllegalArgumentException("La pila no admite elementos nulos.");
        }
        validateSize(size + 1);
        elements[size] = element;
        size++;
    }

    @Override
    public E pop() {
        if (isEmpty()) {
            throw new IllegalStateException("Stack is empty");
        }
        E result = elements[size - 1];
        size--;
        elements[size] = null; // Libera la referencia del elemento retirado.
        return result;
    }

    @Override
    public E peek() {
        if (isEmpty()) {
            throw new IllegalStateException("Stack is empty");
        }
        /**Devuelve el ultimo elemento, por eso es size-1 **/
        return elements[size - 1];
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void clear() {
        // Mantiene una capacidad positiva incluso si ya estaba vacia.
        elements = (E[]) new Object[DEFAULT_SIZE];
        size = 0;
    }

    private void validateSize(int newSize) {
        if (newSize > elements.length) {
            resize();
        }
    }

    @SuppressWarnings("unchecked")
    private void resize() {
        // Creamos un nuevo array del doble de largo que el actual.
        E[] nextArray = (E[]) new Object[elements.length * 2];
        // Copiamos todo lo que esta en el array al nuevo.
        for (int i = 0; i < elements.length; i++) {
            nextArray[i] = elements[i];
        }
        elements = nextArray;
    }
}
