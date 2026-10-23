package treeModule;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Queue;

public class BST<E extends Comparable<E>> {
    private int count = 0;
    private TreeNode<E> root = null;

    public void insert(E value) {
        if (value == null) {
            throw new IllegalArgumentException("El arbol no admite valores null.");
        }
        root = insertRecursive(root, value);
    }

    private TreeNode<E> insertRecursive(TreeNode<E> current, E value) {
        // Caso base al recibir un espacio vacio.
        if (current == null) {
            count++;
            return new TreeNode<>(value);
        }

        // 1: Derecha
        // -1: Izquierda
        // 0: Igual - si es igual se guardara a la derecha.
        // Comparar los valores.
        int comparison = value.compareTo(current.value);
        if (comparison < 0) {
            current.left = insertRecursive(current.left, value);
        } else {
            current.right = insertRecursive(current.right, value);
        }

        return current;
    }

    public void remove(E value) {
        if (value == null) {
            throw new IllegalArgumentException("El arbol no admite valores null.");
        }
        root = recursiveRemove(root, value);
    }

    private TreeNode<E> recursiveRemove(TreeNode<E> current, E value) {
        // Termino de recorrer el arbol y no se encontro el valor.
        // Caso base.
        if (current == null) {
            throw new NoSuchElementException("El valor no existe en el arbol: " + value);
        }

        // -1: Izquierda
        // 1: Derecha
        // 0: Igual
        int comparison = value.compareTo(current.value);

        // Busca el valor en el subarbol que corresponde.
        if (comparison < 0) {
            current.left = recursiveRemove(current.left, value);
            return current;
        } else if (comparison > 0) {
            current.right = recursiveRemove(current.right, value);
            return current;
        }

        // Se encontro el valor. Tendremos 3 casos: sin hijo, con 1 hijo o con 2 hijos.
        count--;

        // Caso 1 y 2: sin hijo izquierdo, o solo hijo derecho.
        if (current.left == null) {
            return current.right;
        }

        // Caso 2: solo hijo izquierdo.
        if (current.right == null) {
            return current.left;
        }

        // Caso 3: dos hijos. Se reemplaza por el menor del subarbol derecho.
        TreeNode<E> successor = getMinChild(current.right);
        current.value = successor.value;
        current.right = removeMin(current.right);
        return current;
    }

    private TreeNode<E> getMinChild(TreeNode<E> current) {
        // Busca el nodo mas a la izquierda del subarbol derecho.
        while (current.left != null) {
            current = current.left;
        }
        return current;
    }

    /** Quita el minimo del subarbol sin modificar count. */
    private TreeNode<E> removeMin(TreeNode<E> current) {
        if (current.left == null) {
            return current.right;
        }
        current.left = removeMin(current.left);
        return current;
    }

    public boolean contains(E value) {
        if (value == null) {
            throw new IllegalArgumentException("El arbol no admite valores null.");
        }

        TreeNode<E> current = root;
        while (current != null) {
            int comparison = value.compareTo(current.value);
            if (comparison == 0) {
                return true;
            }
            current = comparison < 0 ? current.left : current.right;
        }
        return false;
    }

    public boolean isEmpty() {
        return root == null;
    }

    public void clear() {
        root = null;
        count = 0;
    }

    public int size() {
        return count;
    }

    // Pre-order: raiz -> izquierdo -> derecho.
    public List<E> preOrder() {
        List<E> result = new ArrayList<>();
        preOrderRecursive(root, result);
        return result;
    }

    private void preOrderRecursive(TreeNode<E> current, List<E> result) {
        if (current == null) {
            return;
        }
        result.add(current.value);
        preOrderRecursive(current.left, result);
        preOrderRecursive(current.right, result);
    }

    // In-order: izquierdo -> raiz -> derecho. Devuelve los elementos ordenados.
    public List<E> inOrder() {
        List<E> result = new ArrayList<>();
        inOrderRecursive(root, result);
        return result;
    }

    private void inOrderRecursive(TreeNode<E> current, List<E> result) {
        if (current == null) {
            return;
        }
        inOrderRecursive(current.left, result);
        result.add(current.value);
        inOrderRecursive(current.right, result);
    }

    // Post-order: izquierdo -> derecho -> raiz.
    public List<E> postOrder() {
        List<E> result = new ArrayList<>();
        postOrderRecursive(root, result);
        return result;
    }

    private void postOrderRecursive(TreeNode<E> current, List<E> result) {
        if (current == null) {
            return;
        }
        postOrderRecursive(current.left, result);
        postOrderRecursive(current.right, result);
        result.add(current.value);
    }

    // Level-order (BFS): nivel por nivel, de izquierda a derecha.
    public List<E> levelOrder() {
        List<E> result = new ArrayList<>();
        if (root == null) {
            return result;
        }

        Queue<TreeNode<E>> queue = new ArrayDeque<>();
        queue.add(root);

        while (!queue.isEmpty()) {
            TreeNode<E> current = queue.remove();
            result.add(current.value);
            if (current.left != null) {
                queue.add(current.left);
            }
            if (current.right != null) {
                queue.add(current.right);
            }
        }
        return result;
    }

    // Busquedas o recorridos en nodos binarios:
    // Depth First Search (DFS) - Recorrido en profundidad.
    // Pre-Order: primero nodo padre, luego izquierda y luego derecha.
    // In-Order: primero izquierda, luego nodo padre y luego derecha.
    // En un BST, In-Order deja los valores ordenados de menor a mayor.
    // Post-Order: primero izquierda, luego derecha y luego nodo padre.
    // Breadth First Search (BFS) - Recorrido en anchura o level order.
    // Se recorre el arbol nivel por nivel, de izquierda a derecha.
}
