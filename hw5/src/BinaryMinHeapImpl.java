import java.util.Set;
import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.ArrayList;

/**
 * @param <V>   {@inheritDoc}
 * @param <Key> {@inheritDoc}
 */
public class BinaryMinHeapImpl<Key extends Comparable<Key>, V> implements BinaryMinHeap<Key, V> {
    /**
     * {@inheritDoc}
     */
    private ArrayList<Entry> array;
    private HashMap<V, Integer> map;
    
    BinaryMinHeapImpl() {
        array = new ArrayList<>();
        map = new HashMap<>();
    }
    
    // Function to help with testing
    ArrayList<Entry> getArrayList() {
        return this.array;
    }
    
    @Override
    public int size() {
        return array.size();
    }

    @Override
    public boolean isEmpty() {
        return array.isEmpty();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean containsValue(V value) {
        return map.containsKey(value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void add(Key key, V value) {
        if (key == null) {
            throw new IllegalArgumentException("Key can not be null.");
        }
        
        if (this.containsValue(value)) {
            throw new IllegalArgumentException("Value already exists.");
        }
        
        Entry<Key, V> newEntry = new Entry<Key, V>(key, value);
        array.add(newEntry);
        map.put(value, this.size() - 1);
        this.swim(this.array, this.size());
    }
    
    // Helper function to sort out an entry within the heap
    void swim(ArrayList<Entry> arr, int index) {
        if (index == 1) {
            return;
        }
        
        int parentIndex = index / 2;
        Entry e = arr.get(index - 1);
        
        Entry entryToSwap = arr.get(parentIndex - 1);
        
        Comparable<Key> currKey = (Comparable<Key>) e.key;
        Key parentKey = (Key) entryToSwap.key;
        if (currKey.compareTo(parentKey) < 0) {
            arr.set(index - 1, entryToSwap);
            arr.set(parentIndex - 1, e);
            swim(arr, parentIndex);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void decreaseKey(V value, Key newKey) {
        if (newKey == null) {
            throw new IllegalArgumentException("New key is null.");
        }
        
        if (!this.containsValue(value)) {
            throw new NoSuchElementException("Heap does not contain this value");
        }
        
        int index = map.get(value);
        Key oldKey = (Key) array.get(index).key;
        
        if (((Comparable) newKey).compareTo(oldKey) == 0) {
            return;
        }
        
        if (((Comparable) newKey).compareTo(oldKey) > 0) {
            throw new IllegalArgumentException("New key is higher than old key.");
        }
        
        Entry newEntry = new Entry(newKey, value);
        array.set(index, newEntry);
        swim(this.array, index + 1);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Entry<Key, V> peek() {
        if (this.isEmpty()) {
            throw new NoSuchElementException("No elements in the heap.");
        }
        
        return array.get(0);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Entry<Key, V> extractMin() {
        if (this.isEmpty()) {
            throw new NoSuchElementException("No elements in the heap.");
        }
        
        Entry removedEntry = array.get(0);
        Entry lastEntry = array.get(this.size() - 1);
        array.set(0, lastEntry);
        array.remove(this.size() - 1);
        this.minHeapify(0, this.array);
        return removedEntry;
    }
    
    // Helper function to min-heapify the specified element of the heap, modeled
    // after max-heapify from class.
    void minHeapify(int i, ArrayList<Entry> arr) {
        int indexLeftChild = 2 * (i + 1);
        int indexRightChild = 2 * (i + 1) + 1;
        
        if (indexLeftChild > arr.size()) {
            return;
        }
        
        int minIndex;
        
        Entry currEntry = arr.get(i);
        Entry leftEntry = arr.get(indexLeftChild - 1);
        
        Key currKey = (Key) currEntry.key;
        Key leftKey = (Key) leftEntry.key;
        
        if (leftKey.compareTo(currKey) < 0) {
            minIndex = indexLeftChild;
        } else {
            minIndex = i + 1;
        }
        
        // Checks if there is a right child for the current node, and if not, then just
        // swap the current node and the left child if the left child is smaller.
        if (indexRightChild > arr.size()) {
            if (minIndex == indexLeftChild) {
                arr.set(i, leftEntry);
                arr.set(indexLeftChild - 1, currEntry);
            }
            return;
        }
        
        Entry minEntry = arr.get(minIndex - 1);
        Key minKey = (Key) minEntry.key;
        Entry rightEntry = arr.get(indexRightChild - 1);
        Key rightKey = (Key) rightEntry.key;
        
        if (rightKey.compareTo(minKey) < 0) {
            minIndex = indexRightChild;
        }
        
        if (minIndex != i + 1) {
            minEntry = arr.get(minIndex - 1);
            minKey = (Key) minEntry.key;
            arr.set(i, minEntry);
            arr.set(minIndex - 1, currEntry);
            this.minHeapify(minIndex - 1, arr);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<V> values() {
        return map.keySet();
    }
}