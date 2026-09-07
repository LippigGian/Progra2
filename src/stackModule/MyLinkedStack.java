package stackModule;


public class MyLinkedStack<E> implements MyStack<E> {

    private Node<E> last;
    private int size = 0;

    private static class Node<E> {
        private final E element;
        private final Node<E> next;

        private Node(E element, Node<E> next) {
            this.element = element;
            this.next = next;
        }
    }

    @Override
    public void push(E element) {
        if (element == null) {
            throw new IllegalArgumentException("La pila no admite elementos nulos.");
        }
         // No hace falta verificar el size, porque los nodos no tiene una capacidad fija
        // El nuevo nodo apunta al que antes era el ultimo.
        last = new Node<>(element, last);
        size++;
    }

    @Override
    public E pop() {
        if (isEmpty()) {
            throw new IllegalStateException("Stack is empty");
        }
       
        E result = last.element;
        last = last.next;
        size--;
        return result;
    }

    @Override
    public E peek() {
        if (isEmpty()) {
            throw new IllegalStateException("Stack is empty");
        }
        return last.element;
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
    public void clear() {
        last = null;
        size = 0;
    }
}
