package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final double DEFAULT_LOAD_FACTOR = 0.75;
    private static final int GROW_FACTOR = 2;
    private Node<K, V>[] table;
    private int size;
    private int capacity;
    private int threshold;

    public MyHashMap() {
        capacity = DEFAULT_CAPACITY;
        table = (Node<K, V>[]) new Node[capacity];
        threshold = (int) (capacity * DEFAULT_LOAD_FACTOR);
        size = 0;
    }

    @Override
    public void put(K key, V value) {
        if (size > threshold) {
            resize();
        }
        int index = getIndex(key);
        Node<K, V> currentNode = table[index];
        if (currentNode == null) {
            table[index] = new Node<>(hash(key), key, value, null);
            size++;
        } else {
            while (currentNode != null) {
                if ((currentNode.key == null && key == null)
                        || (currentNode.key != null && currentNode.key.equals(key))) {
                    currentNode.value = value;
                    return;
                } else {
                    if (currentNode.next != null) {
                        currentNode = currentNode.next;
                    } else {
                        currentNode.next = new Node<>(hash(key), key, value, null);
                        size++;
                        return;
                    }
                }
            }
        }
    }

    @Override
    public V getValue(K key) {
        int index = getIndex(key);
        Node<K, V> currentNode = table[index];
        while (currentNode != null) {
            if (key == null && currentNode.key == null) {
                return currentNode.value;
            } else {
                if (key != null && key.equals(currentNode.key)) {
                    return currentNode.value;
                }
            }
            currentNode = currentNode.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int hash(K key) {
        return (key == null) ? 0 : key.hashCode();
    }

    private int getIndex(K key) {
        int hash = hash(key);
        return hash & (capacity - 1);
    }

    private Node<K, V>[] resize() {
        capacity *= GROW_FACTOR;
        Node<K, V>[] newTable = (Node<K, V>[]) new Node[capacity];
        for (Node<K, V> node : table) {
            Node<K, V> currentNode = node;
            while (currentNode != null) {
                Node<K, V> nextNode = currentNode.next;
                int index = getIndex(currentNode.key);
                currentNode.next = newTable[index];
                newTable[index] = currentNode;
                currentNode = nextNode;
            }
        }
        table = newTable;
        threshold = (int) (capacity * DEFAULT_LOAD_FACTOR);
        return table;
    }

    private static class Node<K, V> {
        private final int hash;
        private final K key;
        private V value;
        private Node<K, V> next;

        Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
