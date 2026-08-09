package datastructures;

import interfaces.DataStructure;

/**
 * Custom B-tree placeholder suitable for large datasets and disk-backed
 * operations.
 * 
 * @param <T> element type
 */
public class BTree<T extends Comparable<T>> implements DataStructure<T> {

    private static final int MIN_DEGREE = 2;
    private static final int MAX_KEYS = 2 * MIN_DEGREE - 1;
    private static final int MAX_CHILDREN = 2 * MIN_DEGREE;

    private class Node {
        int keyCount;
        T[] keys;
        Node[] children;
        boolean leaf;

        @SuppressWarnings("unchecked")
        Node(boolean leaf) {
            this.leaf = leaf;
            this.keyCount = 0;
            this.keys = (T[]) new Comparable[MAX_KEYS];
            this.children = new BTree.Node[MAX_CHILDREN];
        }
    }

    private Node root;
    private int size;

    public BTree() {
        root = new Node(true);
        size = 0;
    }

    @Override
    public void add(T item) {
        if (item == null) {
            throw new IllegalArgumentException("Null elements not allowed");
        }
        if (root.keyCount == MAX_KEYS) {
            Node newRoot = new Node(false);
            newRoot.children[0] = root;
            splitChild(newRoot, 0);
            root = newRoot;
        }
        insertNonFull(root, item);
    }

    private void splitChild(Node parent, int index) {
        Node fullChild = parent.children[index];
        Node newChild = new Node(fullChild.leaf);
        newChild.keyCount = MIN_DEGREE - 1;

        for (int j = 0; j < MIN_DEGREE - 1; j++) {
            newChild.keys[j] = fullChild.keys[j + MIN_DEGREE];
            fullChild.keys[j + MIN_DEGREE] = null;
        }

        if (!fullChild.leaf) {
            for (int j = 0; j < MIN_DEGREE; j++) {
                newChild.children[j] = fullChild.children[j + MIN_DEGREE];
                fullChild.children[j + MIN_DEGREE] = null;
            }
        }

        fullChild.keyCount = MIN_DEGREE - 1;

        for (int j = parent.keyCount; j >= index + 1; j--) {
            parent.children[j + 1] = parent.children[j];
        }
        parent.children[index + 1] = newChild;

        for (int j = parent.keyCount - 1; j >= index; j--) {
            parent.keys[j + 1] = parent.keys[j];
        }
        parent.keys[index] = fullChild.keys[MIN_DEGREE - 1];
        fullChild.keys[MIN_DEGREE - 1] = null;
        parent.keyCount++;
    }

    private void insertNonFull(Node node, T item) {
        int i = node.keyCount - 1;
        if (node.leaf) {
            while (i >= 0 && item.compareTo(node.keys[i]) < 0) {
                node.keys[i + 1] = node.keys[i];
                i--;
            }
            if (i >= 0 && item.compareTo(node.keys[i]) == 0) {
                return;
            }
            node.keys[i + 1] = item;
            node.keyCount++;
            size++;
        } else {
            while (i >= 0 && item.compareTo(node.keys[i]) < 0) {
                i--;
            }
            i++;
            if (node.children[i].keyCount == MAX_KEYS) {
                splitChild(node, i);
                if (item.compareTo(node.keys[i]) > 0) {
                    i++;
                }
            }
            insertNonFull(node.children[i], item);
        }
    }

    @Override
    public void remove(T item) {
        if (item == null || root == null) {
            return;
        }

        remove(root, item);
        if (root.keyCount == 0 && !root.leaf) {
            root = root.children[0];
        }
    }

    private void remove(Node node, T item) {
        int idx = findKeyIndex(node, item);

        if (idx < node.keyCount && node.keys[idx].compareTo(item) == 0) {
            if (node.leaf) {
                removeFromLeaf(node, idx);
            } else {
                removeFromNonLeaf(node, idx);
            }
        } else {
            if (node.leaf) {
                return;
            }
            boolean lastChild = idx == node.keyCount;
            if (node.children[idx].keyCount < MIN_DEGREE) {
                fill(node, idx);
            }
            if (lastChild && idx > node.keyCount) {
                remove(node.children[idx - 1], item);
            } else {
                remove(node.children[idx], item);
            }
        }
    }

    private int findKeyIndex(Node node, T item) {
        int idx = 0;
        while (idx < node.keyCount && node.keys[idx].compareTo(item) < 0) {
            idx++;
        }
        return idx;
    }

    private void removeFromLeaf(Node node, int index) {
        for (int i = index + 1; i < node.keyCount; i++) {
            node.keys[i - 1] = node.keys[i];
        }
        node.keyCount--;
        size--;
    }

    private void removeFromNonLeaf(Node node, int index) {
        T value = node.keys[index];
        if (node.children[index].keyCount >= MIN_DEGREE) {
            T predecessor = getPredecessor(node, index);
            node.keys[index] = predecessor;
            remove(node.children[index], predecessor);
        } else if (node.children[index + 1].keyCount >= MIN_DEGREE) {
            T successor = getSuccessor(node, index);
            node.keys[index] = successor;
            remove(node.children[index + 1], successor);
        } else {
            merge(node, index);
            remove(node.children[index], value);
        }
    }

    private T getPredecessor(Node node, int index) {
        Node current = node.children[index];
        while (!current.leaf) {
            current = current.children[current.keyCount];
        }
        return current.keys[current.keyCount - 1];
    }

    private T getSuccessor(Node node, int index) {
        Node current = node.children[index + 1];
        while (!current.leaf) {
            current = current.children[0];
        }
        return current.keys[0];
    }

    private void fill(Node node, int index) {
        if (index != 0 && node.children[index - 1].keyCount >= MIN_DEGREE) {
            borrowFromPrev(node, index);
        } else if (index != node.keyCount && node.children[index + 1].keyCount >= MIN_DEGREE) {
            borrowFromNext(node, index);
        } else {
            if (index != node.keyCount) {
                merge(node, index);
            } else {
                merge(node, index - 1);
            }
        }
    }

    private void borrowFromPrev(Node node, int index) {
        Node child = node.children[index];
        Node sibling = node.children[index - 1];

        for (int i = child.keyCount - 1; i >= 0; i--) {
            child.keys[i + 1] = child.keys[i];
        }

        if (!child.leaf) {
            for (int i = child.keyCount; i >= 0; i--) {
                child.children[i + 1] = child.children[i];
            }
        }

        child.keys[0] = node.keys[index - 1];

        if (!child.leaf) {
            child.children[0] = sibling.children[sibling.keyCount];
        }

        node.keys[index - 1] = sibling.keys[sibling.keyCount - 1];
        child.keyCount++;
        sibling.keyCount--;
    }

    private void borrowFromNext(Node node, int index) {
        Node child = node.children[index];
        Node sibling = node.children[index + 1];

        child.keys[child.keyCount] = node.keys[index];

        if (!child.leaf) {
            child.children[child.keyCount + 1] = sibling.children[0];
        }

        node.keys[index] = sibling.keys[0];

        for (int i = 1; i < sibling.keyCount; i++) {
            sibling.keys[i - 1] = sibling.keys[i];
        }

        if (!sibling.leaf) {
            for (int i = 1; i <= sibling.keyCount; i++) {
                sibling.children[i - 1] = sibling.children[i];
            }
        }

        child.keyCount++;
        sibling.keyCount--;
    }

    private void merge(Node node, int index) {
        Node child = node.children[index];
        Node sibling = node.children[index + 1];

        child.keys[MIN_DEGREE - 1] = node.keys[index];

        for (int i = 0; i < sibling.keyCount; i++) {
            child.keys[i + MIN_DEGREE] = sibling.keys[i];
        }

        if (!child.leaf) {
            for (int i = 0; i <= sibling.keyCount; i++) {
                child.children[i + MIN_DEGREE] = sibling.children[i];
            }
        }

        for (int i = index + 1; i < node.keyCount; i++) {
            node.keys[i - 1] = node.keys[i];
        }
        for (int i = index + 2; i <= node.keyCount; i++) {
            node.children[i - 1] = node.children[i];
        }

        child.keyCount += sibling.keyCount + 1;
        node.keyCount--;
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
            throw new IndexOutOfBoundsException("Index out of bounds: " + index);
        }
        return traverse(root, new int[] { index });
    }

    private T traverse(Node node, int[] index) {
        if (node == null) {
            return null;
        }

        for (int i = 0; i < node.keyCount; i++) {
            if (!node.leaf) {
                T value = traverse(node.children[i], index);
                if (value != null) {
                    return value;
                }
            }
            if (index[0] == 0) {
                return node.keys[i];
            }
            index[0]--;
        }

        if (!node.leaf) {
            return traverse(node.children[node.keyCount], index);
        }

        return null;
    }

    @Override
    public void set(int index, T item) {
        if (item == null) {
            throw new IllegalArgumentException("Null elements not allowed");
        }
        T existing = get(index);
        remove(existing);
        add(item);
    }
}
