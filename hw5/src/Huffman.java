import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Implements construction, encoding, and decoding logic of the Huffman coding algorithm. Characters
 * not in the given seed or alphabet should not be compressible, and attempts to use those
 * characters should result in the throwing of an {@link IllegalArgumentException} if used in {@link
 * #compress(String)}.
 */
public class Huffman {
    
    // This is the inner function to represent the Huffman Tree.
    class HuffmanTreeNode {
        int height, freq;
        String character;
        HuffmanTreeNode right;
        HuffmanTreeNode left;
        
        HuffmanTreeNode() {
            character = "";
        }
        
        HuffmanTreeNode(String str, int freq) {
            left = null; 
            right = null;
            height = 0;
            this.character = str;
            this.freq = freq;
        }
        
        HuffmanTreeNode(HuffmanTreeNode l, HuffmanTreeNode r) {
            left = l; 
            right = r;
            height = Math.max(l.height, r.height) + 1;
            StringBuilder str = new StringBuilder();
            str.append(l.character);
            str.append(r.character);
            character = str.toString();
            freq = l.freq + right.freq;
        }
        
        void mergeNodes() {
            while (minHeapOfNodes.size() > 2) {
                BinaryMinHeapImpl.Entry<Integer, HuffmanTreeNode> leftEntry = minHeapOfNodes.extractMin();
                BinaryMinHeapImpl.Entry<Integer, HuffmanTreeNode> rightEntry = minHeapOfNodes.extractMin();
                HuffmanTreeNode leftNode = leftEntry.value;
                HuffmanTreeNode rightNode = rightEntry.value;
                HuffmanTreeNode rootNode = new HuffmanTreeNode(leftNode, rightNode);
                minHeapOfNodes.add(rootNode.freq, rootNode);
            }
            
            BinaryMinHeapImpl.Entry<Integer, HuffmanTreeNode> leftEntry = minHeapOfNodes.extractMin();
            BinaryMinHeapImpl.Entry<Integer, HuffmanTreeNode> rightEntry = minHeapOfNodes.extractMin();
            HuffmanTreeNode leftNode = leftEntry.value;
            HuffmanTreeNode rightNode = rightEntry.value;
            
            huffTree = new HuffmanTreeNode(leftNode, rightNode);
        }
    }

    // Fields for Huffman class
    BinaryMinHeapImpl minHeap = new BinaryMinHeapImpl();
    private BinaryMinHeapImpl minHeapOfNodes = new BinaryMinHeapImpl();
    Map<String, String> encodingMap = new HashMap<>();
    HuffmanTreeNode huffTree = new HuffmanTreeNode();
    private double totalFreq = 0;
    private double aggRawInputLength = 0;
    private double aggCompressedLength = 0;

    /**
     * Constructs a {@code Huffman} instance from a seed string, from which to deduce the alphabet
     * and corresponding frequencies.
     * <p/>
     * Do NOT modify this constructor header.
     *
     * @param seed the String from which to build the encoding
     * @throws IllegalArgumentException seed is null, seed is empty, or resulting alphabet only has
     *                                  1 character
     */
    public Huffman(String seed) {
        if (seed == null) {
            throw new IllegalArgumentException("Seed is null.");
        }
        
        if (seed.isEmpty()) {
            throw new IllegalArgumentException("Seed is empty.");
        }
        
        Map<Character, Integer> freqMapper = new HashMap<>();
        
        for (int i = 0; i < seed.length(); i++) {
            Character c = seed.charAt(i);
            if (!freqMapper.containsKey(c)) {
                freqMapper.put(c, 1);
            } else {
                int freq = freqMapper.get(c);
                freq++;
                freqMapper.put(c, freq);
            }
        }
        
        if (freqMapper.size() < 2) {
            throw new IllegalArgumentException("Seed only has one character.");
        }
        
        totalFreq = seed.length();
        
        this.buildHuffTree(freqMapper);
    }

    /**
     * Constructs a {@code Huffman} instance from a frequency map of the input alphabet.
     * <p/>
     * Do NOT modify this constructor header.
     *
     * @param alphabet a frequency map for characters in the alphabet
     * @throws IllegalArgumentException if the alphabet is null, empty, has fewer than 2 characters,
     *                                  or has any non-positive frequencies
     */
    public Huffman(Map<Character, Integer> alphabet) {
        if (alphabet == null) {
            throw new IllegalArgumentException("Alphabet is null.");
        } else if (alphabet.size() < 2) {
            throw new IllegalArgumentException("Alphabet has less than 2 characters.");
        }
        
        Iterable<Integer> iter = (Iterable<Integer>) alphabet.values();
        for (Integer i : iter) {
            if (i <= 0) {
                throw new IllegalArgumentException("Alphabet has a non-positive frequency.");
            }
        }
        
        this.buildHuffTree(alphabet);
    }
    
    // Helper algorithm to build the huffman tree
    void buildHuffTree(Map<Character, Integer> alphabet) {
        
        Iterable<Character> iterChar = (Iterable<Character>) alphabet.keySet();
    
        for (Character key : iterChar) {
            String value = Character.toString(key);
            int freq = alphabet.get(key);
            minHeap.add(freq, value);
        }
        
        while (minHeap.size() > 1) {
            // Extract two entries from Heap
            BinaryMinHeap.Entry<Integer, String> leftEntry = minHeap.extractMin();
            BinaryMinHeap.Entry<Integer, String> rightEntry = minHeap.extractMin();
            HuffmanTreeNode leftNode = new HuffmanTreeNode(leftEntry.value, leftEntry.key);
            HuffmanTreeNode rightNode = new HuffmanTreeNode(rightEntry.value, rightEntry.key);
            
            // Merge the two entries into one, and add it back into the heap
            HuffmanTreeNode rootNode = new HuffmanTreeNode(leftNode, rightNode);
            minHeap.add(rootNode.freq, rootNode.character);
            
            // Add the left and right nodes into the minHeapOfNodes (only if they are leaves
            // to be used later on for creating the tree
            if (leftNode.character.length() == 1) {
                minHeapOfNodes.add(leftNode.freq, leftNode);
            }
            
            if (rightNode.character.length() == 1) {
                minHeapOfNodes.add(rightNode.freq, rightNode);
            }
        }
        
        BinaryMinHeap.Entry<Integer, String> lastEntry = minHeap.extractMin();
        
        // Create the huffman tree.
        huffTree.mergeNodes();
        
        // Create the encoding map to use for Compress later. Should take big-theta(lgn) time.
        createEncodingMap(huffTree, "");
    }
    
    // Helper function to create the encoding map
    void createEncodingMap(HuffmanTreeNode n, String encoding) {
        if (n.height == 0) {
            encodingMap.put(n.character, encoding.toString());
            return;
        }
        
        StringBuilder strLeft = new StringBuilder();
        strLeft.append(encoding);
        strLeft.append("0");
        StringBuilder strRight = new StringBuilder();
        strRight.append(encoding);
        strRight.append("1");
        
        createEncodingMap(n.left, strLeft.toString());
        createEncodingMap(n.right, strRight.toString());
    }

    /**
     * Compresses the input string.
     *
     * @param input the string to compress, can be the empty string
     * @return a string of ones and zeroes, representing the binary encoding of the inputted String.
     * @throws IllegalArgumentException if the input is null or if the input contains characters
     *                                  that are not compressible
     */
    public String compress(String input) {
        if (input == null) {
            throw new IllegalArgumentException("Input is null.");
        }
        
        if (input.contentEquals("")) {
            return "";
        }
        
        StringBuilder str = new StringBuilder();
        
        for (int i = 0; i < input.length(); i++) {
            Character c = input.charAt(i);
            String s = c.toString();
            if (!encodingMap.containsKey(s)) {
                throw new IllegalArgumentException("Input contains characters that are not compressible.");
            }
            String appendThis = encodingMap.get(s);
            str.append(appendThis);
        }

        aggRawInputLength += input.length() * 16;
        aggCompressedLength += str.length();
        
        return str.toString();
    }

    /**
     * Decompresses the input string.
     *
     * @param input the String of binary digits to decompress, given that it was generated by a
     *              matching instance of the same compression strategy
     * @return the decoded version of the compressed input string
     * @throws IllegalArgumentException if the input is null, or if the input contains characters
     *                                  that are NOT 0 or 1, or input contains a sequence of bits
     *                                  that is not decodable
     */
    public String decompress(String input) {
        if (input == null) {
            throw new IllegalArgumentException("Input is null.");
        }
        
        StringBuilder str = new StringBuilder();
        HuffmanTreeNode currNode = huffTree;
        
        for (int i = 0; i < input.length(); i++) {
            Character c = input.charAt(i);
            if (c == '0') {
                currNode = currNode.left;
            } else if (c == '1') {
                currNode = currNode.right;
            } else {
                throw new IllegalArgumentException("Input contains characters other than 0 or 1.");
            }
            
            if (currNode.height == 0) {
                str.append(currNode.character);
                currNode = huffTree;
            }
        }
        
        if (currNode.height != 0 && currNode.height != huffTree.height) {
            throw new IllegalArgumentException("Input contains a sequence of bits not decodable");
        }
        
        return str.toString();
    }

    /**
     * Computes the compression ratio so far. This is the length of all output strings from {@link
     * #compress(String)} divided by the length of all input strings to {@link #compress(String)}.
     * Assume that each char in the input string is a 16 bit int.
     *
     * @return the ratio of the total output length to the total input length in bits
     * @throws IllegalStateException if no calls have been made to {@link #compress(String)} before
     *                               calling this method
     */
    public double compressionRatio() {
        if (aggRawInputLength == 0) {
            throw new IllegalStateException("No seeds have been compressed yet!");
        }
        
        return aggCompressedLength/aggRawInputLength;
    }

    /**
     * Computes the expected encoding length of an arbitrary character in the alphabet based on the
     * objective function of the compression.
     * <p>
     * The expected encoding length is simply the sum of the length of the encoding of each
     * character multiplied by the probability that character occurs.
     *
     * @return the expected encoding length of an arbitrary character in the alphabet
     */
    public double expectedEncodingLength() {
        double abl = 0;
        
        Iterable<String> characters = (Iterable<String>) encodingMap.keySet();
        
        for (String s : characters) {
            String encoding = encodingMap.get(s);
            double encodingLength = encoding.length();
            HuffmanTreeNode curr = huffTree;
            for (int i = 0; i < encodingLength; i++) {
                //System.out.print(encoding.charAt(i));
                if (encoding.charAt(i) == '0') {
                    curr = curr.left;
                } else {
                    curr = curr.right;
                }
            }
            double freq = curr.freq;
            abl += (freq/totalFreq) * encodingLength;
        }
        
        return abl;
    }
}
