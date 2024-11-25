import java.util.*;

class LRUCache<K, V> {
    private class Node {
        K key;
        V value;
        Node prev, next;
        Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    private final int capacity;
    private final Map<K, Node> map;
    private final Node head; // Dummy head
    private final Node tail; // Dummy tail

    public LRUCache(int capacity) {
        this.capacity = capacity;
        this.map = new HashMap<>();
        this.head = new Node(null, null); // Dummy head
        this.tail = new Node(null, null); // Dummy tail
        head.next = tail;
        tail.prev = head;
    }

    // GET method
    public V get(K key) {
        if (!map.containsKey(key)) {
            return null; // Key not found
        }
        Node node = map.get(key);
        moveToHead(node); // Mark as recently used
        return node.value;
    }

    // PUT method
    public void put(K key, V value) {
        if (map.containsKey(key)) {
            Node node = map.get(key);
            node.value = value;
            moveToHead(node); // Mark as recently used
        } else {
            if (map.size() >= capacity) {
                removeLRUNode(); // Remove least recently used
            }
            Node newNode = new Node(key, value);
            addToHead(newNode);
            map.put(key, newNode);
        }
    }

    // Move a node to the head
    private void moveToHead(Node node) {
        removeNode(node); // Remove node from its current position
        addToHead(node); // Add node to the head
    }

    // Add a node to the head
    private void addToHead(Node node) {
        node.next = head.next;
        node.prev = head;
        head.next.prev = node;
        head.next = node;
    }

    // Remove a node
    private void removeNode(Node node) {
        node.prev.next = node.next;
        node.next.prev = node.prev;
    }

    // Remove the least recently used (tail's previous node)
    private void removeLRUNode() {
        Node lru = tail.prev;
        removeNode(lru);
        map.remove(lru.key);
    }
}

public class Main {
    public static void main(String[] args) {
        LRUCache<Integer, String> cache = new LRUCache<>(3);
        cache.put(1, "A");
        cache.put(2, "B");
        cache.put(3, "C");
        System.out.println(cache.get(1)); // Access key 1, output: A
        cache.put(4, "D"); // Key 2 will be evicted (LRU)
        System.out.println(cache.get(2)); // Output: null
    }
}
