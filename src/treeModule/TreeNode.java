package treeModule;

/** Nodo de un arbol binario de busqueda. */
class TreeNode<E> {
    E value;
    TreeNode<E> left;
    TreeNode<E> right;

    TreeNode(E value) {
        this.value = value;
    }
}
