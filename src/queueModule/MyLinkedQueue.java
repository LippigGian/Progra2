package queueModule;

/** Implementacion propia de Queue mediante nodos enlazados. */
public class MyLinkedQueue<E> implements MyQueue<E> {
    private LinkedNode<E> first;
    private LinkedNode<E> last;
    private int size = 0;

    @Override
    public void enqueue(E element) {
        if (element == null) {
            throw new IllegalArgumentException("La cola no admite elementos nulos.");
        }
        LinkedNode<E> newNode = new LinkedNode<>(element);
        if (isEmpty()) {
            first = newNode;
        } else {
            last.next = newNode;
            newNode.prev = last;
        }
        last = newNode;
        size++;
    }

    @Override
    public E dequeue() {
        if (isEmpty()) {
            throw new IllegalStateException("La cola esta vacia.");
        }
        E result = first.value;
        first = first.next;
        size--;
        if (first != null) {
            first.prev = null;
        } else {
            last = null;
        }
        return result;
    }

    @Override
    public E peek() {
        if (isEmpty()) {
            throw new IllegalStateException("La cola esta vacia.");
        }
        return first.value;
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
        first = null;
        last = null;
        size = 0;
    }
}
