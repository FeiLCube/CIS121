import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * Implementation exercise for a {@link Map} using hashing. The final result will be an
 * implementation with characteristics similar to the OpenJDK implementation for Java 7.
 *
 * @author scheiber
 * @author cquanze
 */
public class HashMap<K, V> extends BaseAbstractMap<K, V> {

    // The default initial capacity - MUST be a power of two.
    private static final int DEFAULT_INITIAL_CAPACITY = 16;

    // The maximum capacity, used if a higher value is implicitly specified by
    // either of the constructors with arguments. Here, we define capacity to be
    // the number of buckets in the hash table.
    private static final int MAXIMUM_CAPACITY = 1 << 30;

    // The load factor used when not specified in constructor.
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;

    // The load factor for the hash table.
    private final float loadFactor;

    // The table, resized as necessary. Length MUST always be a power of two.
    // The Entry class is really a primitive linked list; each Entry object has
    // a pointer to the next item of the linked list, or null if that Entry
    // corresponds to the "end" of the linked list. This approach uses slightly
    // less memory, and is in fact the technique that the pre-Java 8 JDK uses.
    // The size of the table is the number of buckets.
    private Entry<K, V>[] table;

    // The number of key-value mappings contained in this map. Note that this is
    // different from the table capacity.
    private int size;

    // The next size value at which to resize (capacity * load factor).
    private int threshold;

    /**
     * Constructs an empty HashMap with the specified initial capacity and load factor.
     *
     * @param initialCapacity the initial capacity
     * @param loadFactor      the load factor
     * @throws IllegalArgumentException if the initial capacity is non-positive, or the load factor
     *                                  is non-positive or NaN
     */
    @SuppressWarnings("unchecked")
    public HashMap(int initialCapacity, float loadFactor) {
        if (initialCapacity <= 0) {
            throw new IllegalArgumentException(
                "Illegal initial capacity: " + initialCapacity);
        }
        if (initialCapacity > MAXIMUM_CAPACITY) {
            initialCapacity = MAXIMUM_CAPACITY;
        }
        if (loadFactor <= 0 || Float.isNaN(loadFactor)) {
            throw new IllegalArgumentException(
                "Illegal load factor: " + loadFactor);
        }

        // Find a power of 2 >= initialCapacity
        int capacity = 1;
        while (capacity < initialCapacity) {
            capacity <<= 1;
        }

        this.loadFactor = loadFactor;
        this.threshold = (int) (capacity * loadFactor);
        this.table = new Entry[capacity];
    }

    /**
     * Constructs an empty HashMap with the specified initial capacity and the default load factor
     * (0.75).
     *
     * @param initialCapacity the initial capacity.
     * @throws IllegalArgumentException if the initial capacity is non-positive.
     */
    public HashMap(int initialCapacity) {
        this(initialCapacity, DEFAULT_LOAD_FACTOR);
    }

    /**
     * Constructs an empty HashMap with the default initial capacity (16) and the default load
     * factor (0.75).
     */
    public HashMap() {
        this(DEFAULT_INITIAL_CAPACITY, DEFAULT_LOAD_FACTOR);
    }

    /**
     * Applies a supplemental hash function to a given hashCode, which defends against poor quality
     * hash functions. This is critical because HashMap uses power-of-two length hash tables that
     * otherwise encounter collisions for hashCodes that do not differ in lower bits. Note: Null
     * keys always map to hash 0, thus index 0.
     * <p>
     * Additionally, this method truncates the hash to be a valid bucket based on the length
     * parameter, which represents the number of hash table buckets.
     */
    private static int hash(int h, int length) {
        // This function ensures that hashCodes that differ only by
        // constant multiples at each bit position have a bounded
        // number of collisions (approximately 8 at default load factor).
        h ^= (h >>> 20) ^ (h >>> 12);
        h ^= (h >>> 7) ^ (h >>> 4);
        return h & (length - 1);
    }

    /* NOTE: You shouldn't need to modify anything above this line. */

    @Override
    public int size() {
        return size;
    }

    @Override
    public V get(Object key) {
        // 1. Null keys should hash to index 0. Otherwise, hash the key via hash() (see above)
        // 2. Search the corresponding bucket for the specific key.
        // 3. Return the key's value, or null if it does not exist.
        Entry<K, V> currEntry;
        
        if (key == null) {
            currEntry = table[0];
        } else {
            int hashCode = key.hashCode();
            int bucket = hash(hashCode, table.length);
            currEntry = table[bucket];
        }
        
        while (currEntry != null) {
            if (key == currEntry.key) {
                return currEntry.value;
            } else {
                currEntry = currEntry.next;
            }
        }
        
        return null;
    }

    @Override
    public boolean containsKey(Object key) {
        // 1. Null keys should hash to index 0. Otherwise, hash the key via hash()
        // 2. Search the bucket for the specific key and return whether it exists.
        Entry<K, V> currEntry;
        
        if (key == null) {
            currEntry = table[0];
        } else {
            int hashCode = key.hashCode();
            int bucket = hash(hashCode, table.length);
            currEntry = table[bucket];
        }
        
        boolean foundKey = false;
        
        while (currEntry != null) {
            if (key == currEntry.key) {
                foundKey = true;
                break;
            } else {
                currEntry = currEntry.next;
            }
        }
        
        return foundKey;
    }

    @Override
    public V put(K key, V value) {
        // 1. Hash the key.
        // 2. Walk the appropriate bucket.
        // 3. If you find a matching key, replace its value with value.
        //    Otherwise, make a new entry and insert it at the front of the bucket.
        // 4. Don't forget to resize the array and update the size variable as
        //    necessary. Remember, you resize if this call to put causes the new size
        //    to be greater than or equal to the threshold. 

        // NOTE: you should return the previous value associated with key, or null if there
        // was no mapping for key. (A null return can also indicate that the map previously
        // associated null with key).
        int bucket = 0;
        
        if (key != null) {
            int hashCode = key.hashCode();
            bucket = hash(hashCode, table.length);
        }
        
        Entry<K, V> currEntry = table[bucket];
        V oldValue = null;
        
        if (this.containsKey(key)) {
            while (currEntry != null) {
                if (currEntry.key == key) {
                    oldValue = currEntry.value;
                    currEntry.value = value;
                    return oldValue;
                } else {
                    currEntry = currEntry.next;
                }
            }
        } else {
            Entry<K, V> newEntry = new Entry<>(key, value, currEntry);
            table[bucket] = newEntry;
            size++;
            if (size >= threshold && threshold != Integer.MAX_VALUE) {
                resize(table.length * 2);
            }
        }
        
        return oldValue;
    }

    /**
     * Rehashes the contents of this map into a new array with a larger capacity. This method
     * should be called automatically when the number of keys in this map reaches its threshold.
     * <p>
     * If current capacity is MAXIMUM_CAPACITY, this method should not resize the map, but instead
     * set threshold to Integer.MAX_VALUE. This has the effect of preventing future calls.
     *
     * @param newCapacity the new capacity, MUST be a power of two; must be greater than current
     *                    capacity unless current capacity is MAXIMUM_CAPACITY (in which case
     *                    value is irrelevant).
     *                    However, there is no need to invoke an exception if an invalid capacity
     *                    is passed in; since
     *                    this is a helper method only used by your implementation internally,
     *                    you can guarantee that
     *                    invalid capacities are not passed in.
     */
    void resize(int newCapacity) {
        // 1. Save the old hash table.
        // 2. Instantiate a new hash table (unless, of course, the current
        //    capacity is MAXIMUM_CAPACITY).
        // 3. Re-hash the old table into it. That is, re-hash all the keys as if you were
        //    reinserting them, in order from index 0 to the end, from the head of the linked
        //    list to its end.
        // 4. Set the new table threshold.

        // NOTE: You do not need to worry about resizing down.
        Entry<K, V>[] newTable;
        
        if (newCapacity >= MAXIMUM_CAPACITY) {
            threshold = Integer.MAX_VALUE;
            newTable = new Entry[MAXIMUM_CAPACITY];
        } else {
            newTable = new Entry[newCapacity];
            threshold = (int) (loadFactor * newCapacity);
        }
        
        for (int bucket = 0; bucket < table.length; bucket++) {
            Entry<K, V> currEntry = table[bucket];
            while (currEntry != null) {
                int hashCode = currEntry.key.hashCode();
                int newBucket = hash(hashCode, newTable.length);
                Entry<K, V> entry = newTable[newBucket];
                Entry<K, V> newEntry = new Entry<>(currEntry.key, currEntry.value, entry);
                newTable[newBucket] = newEntry;
                currEntry = currEntry.next;
            }
        }
        
        table = newTable;
    }
    
    // Getter function to help with testing.
    Entry<K, V>[] getTable() {
        return table;
    }

    @Override
    public V remove(Object key) {
        // Note that you should not resize down.
        int bucket = 0;
        
        if (key != null) {
            int hashCode = key.hashCode();
            bucket = hash(hashCode, table.length);
        }
        
        Entry<K, V> currEntry = table[bucket];
        Entry<K, V> prevEntry = currEntry;
        V oldValue = null;
        
        while (currEntry != null) {
            
            if (currEntry.key == key) {
                oldValue = currEntry.value;
                prevEntry.next = currEntry.next;
                
                if (table[bucket].equals(currEntry)) {
                    table[bucket] = null;
                }
                
                size--;
                break;
            }
            prevEntry = currEntry;
            currEntry = currEntry.next;
        }
        
        return oldValue;
    }

    @Override
    public boolean containsValue(Object value) {
        // Perform a naive search over each entry of each bucket of the hash table
        // and return true if you have found a matching value.
        for (int i = 0; i < table.length; i++) {
            Entry<K, V> currEntry = table[i];
            while (currEntry != null) {
                if (currEntry.value == value) {
                    return true;
                }
                currEntry = currEntry.next;
            }
        }
        
        return false;
    }

    @Override
    public void clear() {
        // Clear each bucket of the hash table. In order for clear() to be done in O(1),
        // you can definitely use a table of smaller size, as long as it's a power of two.
        // DEFAULT_INITIAL_CAPACITY is a good size to use.
        Entry<K, V>[] clearedTable = new Entry[DEFAULT_INITIAL_CAPACITY];
        table = clearedTable;
        size = 0;
        threshold = (int) (loadFactor * DEFAULT_INITIAL_CAPACITY);
    }

    @Override
    protected Iterator<Map.Entry<K, V>> entryIterator() {
        // Implement an iterator that walks through every single entry in the
        // hash map. Your iterator should NOT support the remove operation.
        // Your iterator MUST be lazy.
        return new HashMapIterator();
    }
    
    class HashMapIterator implements Iterator<java.util.Map.Entry<K, V>> {
        private int bucket = -1;
        private Entry<K, V> currEntry = null;

        public HashMapIterator() {
            for (int i = 0; i < table.length; i++) {
                if (table[i] != null) {
                    currEntry = table[i];
                    bucket = i;
                    break;
                }
            }
        }

        @Override
        public boolean hasNext() {
            if (bucket == -1) {
                return false;
            }
            
            if (currEntry != null) {
                return true;
            } else {
                for (int i = bucket + 1; i < table.length; i++) {
                    if (table[i] != null) {
                        return true;
                    }
                }
            }
            
            return false;
        }
        
        // Helper function to make the iterator iterate to the next entry.
        private void getNext() {
            if (currEntry != null) {
                currEntry = currEntry.next;
            } else {
                for (int i = bucket + 1; i < table.length; i++) {
                    if (table[i] != null) {
                        currEntry = table[i];
                        bucket = i;
                        break;
                    }
                }
            }
        }

        @Override
        public Entry<K, V> next() {
            if (hasNext()) {
                if (currEntry == null) {
                    getNext();
                }
                Entry<K, V> entry = currEntry;
                getNext();
                return entry;
            } else {
                throw new NoSuchElementException();
            }
        }
    }

    /* NOTE: Please do not modify anything below this line. If you'd like to delete the toString
             method for code coverage reasons, go ahead! 
     */

    static class Entry<K, V> implements Map.Entry<K, V> {
        private final K key;
        private V value;
        Entry<K, V> next;

        /**
         * Creates new entry.
         */
        Entry(K k, V v, Entry<K, V> n) {
            value = v;
            next = n;
            key = k;
        }

        @Override
        public K getKey() {
            return key;
        }

        @Override
        public V getValue() {
            return value;
        }

        @Override
        public V setValue(V value) {
            V oldValue = this.value;
            this.value = value;
            return oldValue;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            Entry<?, ?> entry = (Entry<?, ?>) o;
            return Objects.equals(key, entry.key)
                && Objects.equals(value, entry.value);
        }

        @Override
        public int hashCode() {
            return Objects.hash(key, value);
        }
    }
}
