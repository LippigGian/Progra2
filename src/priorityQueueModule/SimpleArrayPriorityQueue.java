package priorityQueueModule;
import java.util.NoSuchElementException;

public class SimpleArrayPriorityQueue<E> implements SimplePriorityQueue<E> {
    
    private E[] elements;
    private int[] priorities;
    private int size = 0;
    private static final int DEFAULT_SIZE = 4;


    @SuppressWarnings("unchecked")
    public SimpleArrayPriorityQueue() {
        //nO SE SI VA ESTO ACA
        elements = (E[]) new Object[DEFAULT_SIZE];
        priorities = new int[DEFAULT_SIZE];
    }

    @Override
    public void enqueue(E element, int priority) {
        validateSize(size + 1);
        int insertIndex = size;

        for(int i = size; i> 0 && priority < priorities[i - 1]; i--) {
            //Corremos lo que esta el indice anterior hacia la derecha.
            
           
            elements[i] = elements[i - 1];
            priorities[i] = priorities[i - 1];
            //Vamos a insertar en el lugar que dejamos "vacio"
            insertIndex = i - 1;
        }
        elements[insertIndex] = element;
        priorities[insertIndex] = priority;
        size++;
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

        int[] nextPriorities = new int[priorities.length * 2];
        for (int i = 0; i < priorities.length; i++) {
            nextPriorities[i] = priorities[i];
        }
        priorities = nextPriorities;
    }

    @Override
    public E dequeue() {
        if (isEmpty()) {
            throw new NoSuchElementException("Queue is empty");
        }
        E result = elements[0];

        //Corremos todos los elementos a la izquierda para tapar el hueco.
        for (int i = 0; i < size - 1; i++) {
            elements[i] = elements[i + 1];
            priorities[i] = priorities[i + 1];
        }
        size--;
        elements[size] = null;
        priorities[size] = 0;
        return result;
    }

    @Override
    public E peek() {
        if (isEmpty()) {
            throw new NoSuchElementException("Queue is empty");
        }
        return elements[0];
    }

    @Override
    public int getHighestPriority() {
        if (isEmpty()) {
            throw new NoSuchElementException("Queue is empty");
        }
        return priorities[0];
    }

    @Override
    @SuppressWarnings("unchecked")
    public void clear() {
        elements = (E[]) new Object[DEFAULT_SIZE];
        priorities = new int[DEFAULT_SIZE];
        size = 0;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

}
