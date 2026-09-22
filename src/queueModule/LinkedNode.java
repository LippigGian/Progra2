package queueModule;

/** Nodo doblemente enlazado, como en el ejemplo del profesor. */
public class LinkedNode<E> {
    public E value;
    public LinkedNode<E> next;
    public LinkedNode<E> prev;

    public LinkedNode(E value) {
        this.value = value;
        this.next = null;
        this.prev = null;
    }
}
