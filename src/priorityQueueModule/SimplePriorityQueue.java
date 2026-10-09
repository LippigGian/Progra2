package priorityQueueModule;

public interface SimplePriorityQueue<E> {
    public void enqueue(E element, int priority);
    public E dequeue();
    public E peek();
    public int getHighestPriority();
    public void clear();
    public int size();
    public boolean isEmpty();

}