package datastructures;

import interfaces.DataStructure;

/**
 * Simplified self-balancing binary search tree (AVL-style balancing).
 *
 * This is the "BalancedTree (simplified)" version referenced in the
 * team's Day 1 task sheet — a real AVL tree with height tracking and
 * the 4 standard rotation cases (left-left, right-right, left-right,
 * right-left), rather than a full red-black implementation. Height
 * stays O(log n) after any sequence of inserts, which is the whole
 * point: it never degenerates into a plain linked list the way an
 * unbalanced BST can with sorted input.
 *
 * @param <T> element type (must be Comparable)
 */
public class RedBlackTree<T extends Comparable<T>> implements DataStructure<T> {

    private class Node {
        T value;
        Node left;
        Node right;
        int height;

        Node(T value) {
            this.value = value;
            this.height = 1; // a new leaf node has height 1
        }
    }

    private Node root;
    private int size;

    public RedBlackTree() {
        root = null;
        size = 0;
    }

    // --- Height / balance helpers ---

    private int heightOf(Node node) {
        return node == null ? 0 : node.height;
    }

    private int balanceFactor(Node node) {
        return node == null ? 0 : heightOf(node.left) - heightOf(node.right);
    }

    private void updateHeight(Node node) {
        node.height = 1 + Math.max(heightOf(node.left), heightOf(node.right));
    }

    // --- Rotations ---

    private Node rotateRight(Node y) {
        Node x = y.left;
        Node transferred = x.right;

        x.right = y;
        y.left = transferred;

        updateHeight(y);
        updateHeight(x);

        return x; // new subtree root
    }

    private Node rotateLeft(Node x) {
        Node y = x.right;
        Node transferred = y.left;

        y.left = x;
        x.right = transferred;

        updateHeight(x);
        updateHeight(y);

        return y; // new subtree root
    }

    /** Rebalances a node if its balance factor is outside [-1, 1]. */
    private Node rebalance(Node node) {
        updateHeight(node);
        int balance = balanceFactor(node);

        // Left-heavy
        if (balance > 1) {
            if (balanceFactor(node.left) < 0) {
                node.left = rotateLeft(node.left); // left-right case
            }
            return rotateRight(node); // left-left case
        }

        // Right-heavy
        if (balance < -1) {
            if (balanceFactor(node.right) > 0) {
                node.right = rotateRight(node.right); // right-left case
            }
            return rotateLeft(node); // right-right case
        }

        return node; // already balanced
    }

    // --- Insert ---

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
        } else {
            return node; // duplicate, ignore
        }

        return rebalance(node);
    }

    // --- Remove ---

    @Override
    public void remove(T item) {
        if (item == null || root == null) {
            return;
        }
        root = delete(root, item);
    }

    private Node delete(Node node, T item) {
        if (node == null) {
            return null;
        }

        int comparison = item.compareTo(node.value);
        if (comparison < 0) {
            node.left = delete(node.left, item);
        } else if (comparison > 0) {
            node.right = delete(node.right, item);
        } else {
            size--;
            if (node.left == null) {
                return node.right;
            }
            if (node.right == null) {
                return node.left;
            }
            Node successor = findMin(node.right);
            node.value = successor.value;
            size++; // undo the decrement above; the actual removal happens in the recursive call below
            node.right = delete(node.right, successor.value);
        }

        return rebalance(node);
    }

    private Node findMin(Node node) {
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }

    // --- Traversal / access ---

    /** Prints the tree's values in sorted order. */
    public void inorder() {
        inorderRecursive(root);
        System.out.println();
    }

    private void inorderRecursive(Node node) {
        if (node == null) {
            return;
        }
        inorderRecursive(node.left);
        System.out.print(node.value + " ");
        inorderRecursive(node.right);
    }

    @Override
    public T get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index out of bounds: " + index);
        }
        return getByIndex(root, new int[] { index });
    }

    private T getByIndex(Node node, int[] index) {
        if (node == null) {
            return null;
        }

        T leftResult = getByIndex(node.left, index);
        if (leftResult != null) {
            return leftResult;
        }

        if (index[0] == 0) {
            return node.value;
        }
        index[0]--;

        return getByIndex(node.right, index);
    }

    /** Returns the height of the tree (0 for an empty tree). */
    public int height() {
        return heightOf(root);
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