import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.HashMap;
import java.util.NoSuchElementException;

import org.junit.Before;
import org.junit.Test;

public class BinaryMinHeapImplTest<Key, V> {
    
    BinaryMinHeapImpl minHeap;
    BinaryMinHeap.Entry<Key, V> e1;
    BinaryMinHeap.Entry<Key, V> e2;
    BinaryMinHeap.Entry<Key, V> e3;
    BinaryMinHeap.Entry<Key, V> e4;
    BinaryMinHeap.Entry<Key, V> e5;
    BinaryMinHeap.Entry<Key, V> e6;
    BinaryMinHeap.Entry<Key, V> e7;
    ArrayList<BinaryMinHeap.Entry> array;
    HashMap<V, Integer> map;
    
    @Before
    public void createHeap() {
        minHeap = new BinaryMinHeapImpl();
        array = new ArrayList<>();
        map = new HashMap<>();
        e1 = new BinaryMinHeap.Entry(1, "CIS");
        e2 = new BinaryMinHeap.Entry(2, "121");
        e3 = new BinaryMinHeap.Entry(3, "ROCKS");
        e4 = new BinaryMinHeap.Entry(4, "IN");
        e5 = new BinaryMinHeap.Entry(5, "EVERY");
        e6 = new BinaryMinHeap.Entry(6, "WAY");
        e7 = new BinaryMinHeap.Entry(7, "IMAGINABLE");
    }
    
    @Test
    public void testSize() {
        assertEquals(0, minHeap.size());
        minHeap.add((Comparable) e1.key, e1.value);
        assertEquals(1, minHeap.size());
        minHeap.add((Comparable) e2.key, e2.value);
        assertEquals(2, minHeap.size());
    }
    
    @Test
    public void testIsEmpty() {
        assertTrue(minHeap.isEmpty());
        minHeap.add((Comparable) e1.key, e1.value);
        assertFalse(minHeap.isEmpty());
    }
    
    @Test
    public void testContainsValue() {
        minHeap.add((Comparable) e1.key, e1.value);
        assertTrue(minHeap.containsValue("CIS"));
        assertFalse(minHeap.containsValue("121"));
        minHeap.add((Comparable) e2.key, e2.value);
        assertTrue(minHeap.containsValue("121"));
    }
    
    @Test
    public void testSwimOnRightChild() {
        array.add(0, e7);
        array.add(1, e6);
        array.add(2, e5);
        array.add(3, e4);
        array.add(4, e3);
        array.add(5, e2);
        array.add(6, e1);
        minHeap.swim(array, 7);
        assertEquals(e1.key, array.get(0).key);
        assertEquals(e5.key, array.get(6).key);
        assertEquals(e7.key, array.get(2).key);
    }
    
    @Test
    public void testSwimOnLeftChild() {
        array.add(0, e7);
        array.add(1, e6);
        array.add(2, e5);
        array.add(3, e4);
        array.add(4, e3);
        array.add(5, e2);
        array.add(6, e1);
        minHeap.swim(array, 4);
        assertEquals(e4.key, array.get(0).key);
        assertEquals(e6.key, array.get(3).key);
        assertEquals(e7.key, array.get(1).key);
    }
    
    @Test
    public void testSwimOnSizeOneArray() {
        array.add(0, e1);
        minHeap.swim(array, 1);
        assertEquals(e1.key, array.get(0).key);
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void testAddExistingValue() {
        BinaryMinHeap.Entry<Key, V> e = new BinaryMinHeap.Entry(20, "CIS");
        minHeap.add((Comparable) e1.key, e1.value);
        minHeap.add((Comparable) e.key, e.value);
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void testAddNullKey() {
        BinaryMinHeap.Entry<Key, V> e = new BinaryMinHeap.Entry(null, "s");
        minHeap.add((Comparable) e.key, e.value);
    }
    
    @Test
    public void testAdd() {
        minHeap.add((Comparable) e1.key, e1.value);
        minHeap.add((Comparable) e3.key, e3.value);
        minHeap.add((Comparable) e7.key, e7.value);
        minHeap.add((Comparable) e5.key, e5.value);
        minHeap.add((Comparable) e4.key, e4.value);
        minHeap.add((Comparable) e6.key, e6.value);
        minHeap.add((Comparable) e2.key, e2.value);
        array.add(0, e1);
        array.add(1, e3);
        array.add(2, e2);
        array.add(3, e5);
        array.add(4, e4);
        array.add(5, e7);
        array.add(6, e6);
        ArrayList<BinaryMinHeap.Entry> resultArrList = minHeap.getArrayList();
        assertEquals(array.get(0).key, resultArrList.get(0).key);
        assertEquals(array.get(1).key, resultArrList.get(1).key);
        assertEquals(array.get(2).key, resultArrList.get(2).key);
        assertEquals(array.get(3).key, resultArrList.get(3).key);
        assertEquals(array.get(4).key, resultArrList.get(4).key);
        assertEquals(array.get(5).key, resultArrList.get(5).key);
        assertEquals(array.get(6).key, resultArrList.get(6).key);
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void testDecreaseKeyNullKey() {
        minHeap.add((Comparable) e1.key, e1.value);
        minHeap.decreaseKey(e1.value, null); 
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void testDecreaseKeyHigherKey() {
        minHeap.add((Comparable) e1.key, e1.value);
        minHeap.decreaseKey(e1.value, (Comparable) e3.key); 
    }
    
    @Test (expected = NoSuchElementException.class)
    public void testDecreaseKeyValueDoesNotExist() {
        minHeap.add((Comparable) e3.key, e3.value);
        minHeap.decreaseKey(e1.value, (Comparable) e2.key); 
    }
    
    @Test
    public void testDecreaseKeyOnSizeOneHeap() {
        minHeap.add((Comparable) e3.key, e3.value);
        minHeap.decreaseKey(e3.value, (Comparable) e2.key); 
        ArrayList<BinaryMinHeap.Entry> resultArrList = minHeap.getArrayList();
        assertEquals(e2.key, resultArrList.get(0).key);
    }
    
    @Test
    public void testDecreaseKeyOnLeafNode() {
        minHeap.add((Comparable) e1.key, e1.value);
        minHeap.add((Comparable) e3.key, e3.value);
        minHeap.add((Comparable) e4.key, e4.value);
        minHeap.add((Comparable) e7.key, e7.value);
        minHeap.decreaseKey(e7.value, (Comparable) e2.key);
        ArrayList<BinaryMinHeap.Entry> resultArrList = minHeap.getArrayList();
        assertEquals(e2.key, (Comparable) resultArrList.get(1).key);
    }
    
    @Test
    public void testDecreaseKeyOnNonLeafNode() {
        minHeap.add((Comparable) e1.key, e1.value);
        minHeap.add((Comparable) e3.key, e3.value);
        minHeap.add((Comparable) e4.key, e4.value);
        minHeap.add((Comparable) e7.key, e7.value);
        minHeap.add((Comparable) e5.key, e5.value);
        minHeap.decreaseKey(e3.value, 0);
        ArrayList<BinaryMinHeap.Entry> resultArrList = minHeap.getArrayList();
        assertEquals(0, (Comparable) resultArrList.get(0).key);
    }
    
    @Test
    public void testDecreaseKeySameKey() {
        minHeap.add((Comparable) e1.key, e1.value);
        minHeap.add((Comparable) e3.key, e3.value);
        minHeap.add((Comparable) e4.key, e4.value);
        minHeap.add((Comparable) e7.key, e7.value);
        minHeap.add((Comparable) e5.key, e5.value);
        minHeap.decreaseKey(e3.value, (Comparable) e3.key);
        ArrayList<BinaryMinHeap.Entry> resultArrList = minHeap.getArrayList();
        assertEquals(3, (Comparable) resultArrList.get(1).key);
    }
    
    @Test
    public void testMinHeapifyNoChildren() {
        array.add(e5);
        array.add(e4);
        array.add(e1);
        array.add(e3);
        array.add(e7);
        array.add(e6);
        array.add(e2);
        minHeap.minHeapify(3, array);
        assertEquals(3, (Comparable) array.get(3).key);
        minHeap.minHeapify(4, array);
        assertEquals(7, (Comparable) array.get(4).key);
    }
    
    @Test
    public void testMinHeapifyNoRightChildren() {
        array.add(e5);
        array.add(e4);
        array.add(e1);
        array.add(e3);
        array.add(e7);
        array.add(e6);
        minHeap.minHeapify(5, array);
        assertEquals(6, (Comparable) array.get(5).key);
    }
    
    @Test
    public void testMinHeapifyNoRightChildrenAndLeftChildIsSmaller() {
        array.add(e4);
        array.add(e2);
        array.add(e5);
        array.add(e3);
        minHeap.minHeapify(0, array);
        assertEquals(4, (Comparable) array.get(3).key);
        assertEquals(3, (Comparable) array.get(1).key);
    }
    
    @Test
    public void testMinHeapify() {
        array.add(e5);
        array.add(e4);
        array.add(e1);
        array.add(e3);
        array.add(e7);
        array.add(e6);
        array.add(e2);
        minHeap.minHeapify(0, array);
        assertEquals(2, (Comparable) array.get(2).key);
        assertEquals(5, (Comparable) array.get(6).key);
        assertEquals(1, (Comparable) array.get(0).key);
        minHeap.minHeapify(1, array);
        assertEquals(3, (Comparable) array.get(1).key);
        assertEquals(4, (Comparable) array.get(3).key);
    }
    
    @Test (expected = NoSuchElementException.class)
    public void testExtractMinEmptyHeap() {
        minHeap.extractMin();
    }
    
    @Test
    public void testExtractMin() {
        minHeap.add((Comparable) e3.key, e3.value);
        minHeap.add((Comparable) e5.key, e5.value);
        minHeap.add((Comparable) e7.key, e7.value);
        minHeap.add((Comparable) e1.key, e1.value);
        BinaryMinHeap.Entry e = minHeap.extractMin();
        assertEquals(1, (Comparable) e.key);
    }
    
    @Test (expected = NoSuchElementException.class)
    public void testPeekEmptyHeap() {
        minHeap.peek();
    }
    
    @Test
    public void testPeek() {
        minHeap.add((Comparable) e3.key, e3.value);
        minHeap.add((Comparable) e5.key, e5.value);
        minHeap.add((Comparable) e7.key, e7.value);
        minHeap.add((Comparable) e1.key, e1.value);
        BinaryMinHeap.Entry e = minHeap.peek();
        assertEquals(1, (Comparable) e.key);
    }
    
    @Test
    public void testValues() {
        Set<V> expectedSet = new HashSet<V>();
        minHeap.add((Comparable) e1.key, e1.value);
        minHeap.add((Comparable) e2.key, e2.value);
        minHeap.add((Comparable) e3.key, e3.value);
        minHeap.add((Comparable) e4.key, e4.value);
        minHeap.add((Comparable) e5.key, e5.value);
        minHeap.add((Comparable) e6.key, e6.value);
        minHeap.add((Comparable) e7.key, e7.value);
        expectedSet.add(e1.value);
        expectedSet.add(e2.value);
        expectedSet.add(e3.value);
        expectedSet.add(e4.value);
        expectedSet.add(e5.value);
        expectedSet.add(e6.value);
        expectedSet.add(e7.value);
        Set<V> result = minHeap.values();
        assertEquals(result.size(), expectedSet.size());
        assertEquals(expectedSet, result);
    }
}
