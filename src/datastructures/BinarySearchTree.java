package datastructures;

import interfaces.DataStructure;

/**
 * Custom binary search tree for ordered campus data.
 *
 * @param <T> element type
 */
public class BinarySearchTree<T extends Comparable<T>> implements DataStructure<T> {

    private class Node {
        T value;
        Node left;
        Node right;

        Node(T value) {
            this.value = value;
        }
    }

    private Node root;
    private int size;

    public BinarySearchTree() {
        root = null;
        size = 0;
    }

    @Override
    public void add(T item) {
        if (item == null) {
            throw new IllegalArgumentException("Null elements not allowed");
        }
        root = insert(root, item);
    }

    private Node insert(Node node, T item) {
        if (node == null) {
            size++;
            return new Node(item);
        }

        int comparison = item.compareTo(node.value);

        if (comparison < 0) {
            node.left = insert(node.left, item);
        } else if (comparison > 0) {
            node.right = insert(node.right, item);
        }

        return node;
    }

    @Override
    public void remove(T item) {
        if (item == null || root == null) {
            return;
        }

        root = delete(root, item, true);
    }

    private Node delete(Node node, T item, boolean decrementSize) {
        if (node == null) {
            return null;
        }

        int comparison = item.compareTo(node.value);

        if (comparison < 0) {
            node.left = delete(node.left, item, decrementSize);
        } else if (comparison > 0) {
            node.right = delete(node.right, item, decrementSize);
        } else {
            if (decrementSize) {
                size--;
            }

            if (node.left == null) {
                return node.right;
            }

            if (node.right == null) {
                return node.left;
            }

            Node successor = findMin(node.right);
            node.value = successor.value;
            node.right = delete(node.right, successor.value, false);
        }

        return node;
    }

    private Node findMin(Node node) {
        while (node.left != null) {
            node = node.left;
        }

        return node;
    }

    // --- Height ---

    /**
     * Returns the height of the tree.
     * An empty tree has height 0.
     * A tree containing only the root has height 1.
     */
    public int height() {
        return height(root);
    }

    private int height(Node node) {
        if (node == null) {
            return 0;
        }

        return 1 + Math.max(
                height(node.left),
                height(node.right)
        );
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public T get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException(
                    "Index out of bounds: " + index
            );
        }

        return getByIndex(root, new int[] { index }).value;
    }

    private Node getByIndex(Node node, int[] index) {
        if (node == null) {
            return null;
        }

        Node leftResult = getByIndex(node.left, index);

        if (leftResult != null) {
            return leftResult;
        }

        if (index[0] == 0) {
            return node;
        }

        index[0]--;

        return getByIndex(node.right, index);
    }

    @Override
    public void set(int index, T item) {
        if (item == null) {
            throw new IllegalArgumentException(
                    "Null elements not allowed"
            );
        }

        T existing = get(index);
        remove(existing);
        add(item);
    }
}