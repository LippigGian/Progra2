package setModule;

import listModule.MyArrayList;
import listModule.MyList;

/** Set implementado sobre el arreglo dinamico del TDA List propio. */
public class MyArraySet<T> implements MySet<T> {
    private final MyList<T> elements = new MyArrayList<>();

    @Override
    public void add(T element) {
        if (contains(element)) {
            throw new IllegalArgumentException("El elemento ya pertenece al conjunto.");
        }
        elements.add(element);
    }

    @Override
    public void remove(T element) {
        if (!contains(element)) {
            throw new IllegalArgumentException("El elemento no pertenece al conjunto.");
        }
        elements.remove(element);
    }

    @Override
    public boolean contains(T element) {
        if (element == null) {
            throw new IllegalArgumentException("El conjunto no admite null.");
        }
        return elements.contains(element);
    }

    @Override
    public int size() { return elements.size(); }

    @Override
    public boolean isEmpty() { return elements.isEmpty(); }

    @Override
    public MySet<T> union(MySet<T> other) {
        checkOther(other);
        MySet<T> result = new MyArraySet<>();
        MyList<T> incoming = other.toList();
        for (int i = 0; i < size(); i++) result.add(elements.get(i));
        for (int i = 0; i < incoming.size(); i++) {
            T element = incoming.get(i);
            if (!result.contains(element)) result.add(element);
        }
        return result;
    }

    @Override
    public MySet<T> intersection(MySet<T> other) {
        return filter(other, true);
    }

    @Override
    public MySet<T> difference(MySet<T> other) {
        return filter(other, false);
    }

    private MySet<T> filter(MySet<T> other, boolean shared) {
        checkOther(other);
        MySet<T> result = new MyArraySet<>();
        for (int i = 0; i < size(); i++) {
            T element = elements.get(i);
            if (other.contains(element) == shared) result.add(element);
        }
        return result;
    }

    private void checkOther(MySet<T> other) {
        if (other == null) throw new IllegalArgumentException("El conjunto no puede ser null.");
    }

    @Override
    public MyList<T> toList() {
        MyList<T> copy = new MyArrayList<>();
        for (int i = 0; i < size(); i++) copy.add(elements.get(i));
        return copy;
    }

    @Override
    public String toString() { return elements.toString(); }
}
