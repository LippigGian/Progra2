package priorityQueueModule;
import java.util.NoSuchElementException;


public class SimpleLinkedPriorityQueue<E> implements SimplePriorityQueue<E> {
    private LinkedPriorityNode<E> first = null;
    private LinkedPriorityNode<E> last = null;
    private int size =0;


    @Override
    public void enqueue(E element, int priority) {
        LinkedPriorityNode<E> addedNode = new LinkedPriorityNode<>(element, priority);

        //Si esta vacio, el primero y el ultimo son el mismo
        if(isEmpty()) {
            first = addedNode;
            last = addedNode;
        }
        //Si es mas prioritario o igual que el primero salteamos el bucle.
        else if(priority <= first.priority){
            first.prev = addedNode;
            addedNode.next = first;
            first = addedNode;
        }
        //Si no esta vacia, conectamos al final de la nodo
        else {
            LinkedPriorityNode<E> current = last;
            //Iteramos mirando al nodo anterior
            //Seguimos avanzando si el nuevo es mas prioritario que current

            while(current.prev != null && priority < current.priority)
            {
                current = current.prev;
            }
            //Asignamos primero las referencias al nuevo nodo.
            addedNode.prev = current;
            addedNode.next = current.next;
            //Solo tenemos referencia a current
            //Primero modificamos a current.next, despues current
            //Sino, se corta la cadena
            if(current.next != null){
                current.next.prev = addedNode;
            }
            current.next = addedNode;
        }
        size++;

  }

    @Override
    public E dequeue() {
        if (isEmpty()) {
            throw new NoSuchElementException("Queue is empty");
        }
        E result = first.value;
        first = first.next;

        //Si queda un nodo aparte del que sacamos ese nodo no tiene mas previo
        if (first != null) {
            first.prev = null;
        } 
        
        //Sino significa que el ultimo tampoco existe mas
        else {
            last = null;
        }
        size--;
        return result;
    }

    @Override
    public E peek() {
        if (isEmpty()) {
            throw new NoSuchElementException("Queue is empty");
        }
        return first.value;
    }

    @Override
    public int getHighestPriority() {
        if (isEmpty()) {
            throw new NoSuchElementException("Queue is empty");
        }
        return first.priority;
    }

    @Override
    public void clear() {
        first = null;
        last = null;
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
