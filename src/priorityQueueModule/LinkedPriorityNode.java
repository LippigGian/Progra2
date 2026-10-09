package priorityQueueModule;

public class LinkedPriorityNode<E> {
    public LinkedPriorityNode<E> prev = null;
    public LinkedPriorityNode<E> next = null;
    public E value;
    public int priority;


    public LinkedPriorityNode(E value, int priority) {
        this.value = value;
        this.priority = priority;
    }
}
