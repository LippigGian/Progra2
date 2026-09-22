package queueModule;

/** Cola FIFO: el primero en entrar es el primero en salir. No admite null. */
public interface MyQueue<E> {
    /** Agrega al final. Lanza IllegalArgumentException si element es null. */
    public void enqueue(E element);
    /** Retira el primero. Lanza IllegalStateException si esta vacia. */
    public E dequeue();
    /** Consulta el primero sin retirarlo. Lanza IllegalStateException si esta vacia. */
    public E peek();
    public boolean isEmpty();
    public int size();
    public void clear();
}
