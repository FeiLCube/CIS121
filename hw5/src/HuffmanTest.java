import java.util.Map;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;

public class HuffmanTest {
    
    String seed1;
    String seed2;
    Map<Character, Integer> mapping1 = new HashMap<>();
    Map<Character, Integer> mapping2 = new HashMap<>();
    
    @Before
    public void setUpSeeds() {
        seed1 = "banana";
        seed2 = "furnace";
        mapping1.put('b', 1);
        mapping1.put('n', 2);
        mapping1.put('a', 3);
        mapping2.put('f', 1);
        mapping2.put('u', 1);
        mapping2.put('r', 1);
        mapping2.put('n', 1);
        mapping2.put('a', 1);
        mapping2.put('c', 1);
        mapping2.put('e', 1);
    }
    
    @Test
    public void testCreateEncodingMapSeed1() {
        Huffman actual = new Huffman(seed1);
        assertEquals("0", actual.encodingMap.get("a"));
        assertEquals("10", actual.encodingMap.get("b"));
        assertEquals("11", actual.encodingMap.get("n"));
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void testConstructorNullSeed() {
        String seed = null;
        Huffman test = new Huffman(seed);
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void testConstructorEmptySeed() {
        String seed = "";
        Huffman test = new Huffman(seed);
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void testConstructorOneCharSeed() {
        String seed = "aaaaaaaaa";
        Huffman test = new Huffman(seed);
    }
    
    @Test
    public void testConstructorSeed1() {
        Huffman actual = new Huffman(seed1);
        BinaryMinHeapImpl testHeap = new BinaryMinHeapImpl();
        Huffman.HuffmanTreeNode nodeForB = actual.new HuffmanTreeNode("b", 1);
        Huffman.HuffmanTreeNode nodeForN = actual.new HuffmanTreeNode("n", 2);
        Huffman.HuffmanTreeNode nodeForA = actual.new HuffmanTreeNode("a", 3);
        Huffman.HuffmanTreeNode nodeForBN = actual.new HuffmanTreeNode(nodeForB, nodeForN);
        Huffman.HuffmanTreeNode nodeForABN = actual.new HuffmanTreeNode(nodeForA, nodeForBN);
        assertEquals(nodeForABN.freq, actual.huffTree.freq);
        assertEquals(nodeForABN.right.freq, actual.huffTree.right.freq);
        assertEquals(nodeForABN.right.right.freq, actual.huffTree.right.right.freq);
        assertEquals(nodeForABN.right.left.freq, actual.huffTree.right.left.freq);
        assertEquals(nodeForABN.left.freq, actual.huffTree.left.freq);
        assertEquals(nodeForABN.height, actual.huffTree.height);
    }
    
    @Test
    public void testConstructorSeed2() {
        Huffman actual = new Huffman(seed2);
        assertEquals(3, actual.huffTree.height);
        assertEquals(7, actual.huffTree.freq);
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void testSecondConstructorNullMap() {
        Map<Character, Integer> testMap = null;
        Huffman actual = new Huffman(testMap);
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void testSecondConstructorMapOfSizeOne() {
        Map<Character, Integer> testMap = new HashMap<>();
        testMap.put('a', 2);
        Huffman actual = new Huffman(testMap);
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void testSecondConstructorNegativeFrequencyMap() {
        Map<Character, Integer> testMap = new HashMap<>();
        testMap.put('a', 2);
        testMap.put('b', 5);
        testMap.put('c', -1);
        Huffman actual = new Huffman(testMap);
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void testSecondConstructorZeroFrequencyMap() {
        Map<Character, Integer> testMap = new HashMap<>();
        testMap.put('a', 2);
        testMap.put('b', 5);
        testMap.put('c', 0);
        Huffman actual = new Huffman(testMap);
    }
    
    @Test
    public void testSecondConstructor() {
        Map<Character, Integer> testMap = new HashMap<>();
        testMap.put('a', 2);
        testMap.put('b', 5);
        testMap.put('c', 5);
        Huffman actual = new Huffman(testMap); 
        assertEquals(12, actual.huffTree.freq);
        assertEquals(2, actual.huffTree.height);
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void testCompressNull() {
        Huffman actual = new Huffman(seed1);
        actual.compress(null);
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void testCompressImcompressible() {
        Huffman actual = new Huffman(seed1);
        actual.compress(seed2);
    }
    
    @Test
    public void testCompressEmptyInput() {
        Huffman actual = new Huffman(seed1);
        String seed = "";
        assertEquals("", actual.compress(seed));
    }
    
    @Test
    public void testCompress() {
        Huffman actual = new Huffman(seed1);
        String compressed = actual.compress("abanababa");
        assertEquals(13, compressed.length());
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void testDecompressNullInput() {
        Huffman actual = new Huffman(seed1);
        String decompressed = actual.decompress(null);
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void testDecompressContainsCharsOtherThanZeroOrOne() {
        Huffman actual = new Huffman(seed1);
        String decompressed = actual.decompress("001010102");
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void testDecompressContainsUndecodableSequence() {
        Huffman actual = new Huffman(seed1);
        String decompressed = actual.decompress("001010111");
    }
    
    @Test
    public void testDecompress() {
        Huffman actual = new Huffman(seed1);
        String compressed = actual.compress("banbanaban");
        String decompressed = actual.decompress(compressed);
        assertEquals("banbanaban", decompressed);
    }
    
    @Test (expected = IllegalStateException.class)
    public void testCompressionRatioNoneCompressed() {
        Huffman actual = new Huffman(seed1);
        double ratio = actual.compressionRatio();
    }
    
    @Test
    public void testCompressionRatio() {
        Huffman actual = new Huffman(seed1);
        actual.compress("banbanabb");
        double ratio = actual.compressionRatio();
        assertEquals(15.0/144, ratio, 0.0001);
        actual.compress("banana");
        ratio = actual.compressionRatio();
        assertEquals(24.0/240, ratio, 0.0001);
    }
    
    @Test
    public void testExpectedEncodingLengthSeed1() {
        Huffman actual = new Huffman(seed1);
        assertEquals(3.0/2, actual.expectedEncodingLength(), 0.0001);
    }
    
    @Test
    public void testExpectedEncodingLengthSeed2() {
        Huffman actual = new Huffman(seed2);
        assertEquals(20.0/7, actual.expectedEncodingLength(), 0.0001);
    }
    
    @Test
    public void testHuffmanSimple() {
        String seed = "abbccc";
        Huffman simpleHuff = new Huffman(seed);
        String compressed = simpleHuff.compress("abc");
        assertEquals(5, compressed.length());
        assertEquals("abc", simpleHuff.decompress(compressed));
        assertEquals(5.0 / 48, simpleHuff.compressionRatio(), .00001);
        assertEquals(1.5, simpleHuff.expectedEncodingLength(), .00001);
    }

 /* @author Dana Yi (danayi), 20fa */
}
