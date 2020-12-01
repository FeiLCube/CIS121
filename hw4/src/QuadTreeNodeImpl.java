// CIS 121, QuadTree

public class QuadTreeNodeImpl implements QuadTreeNode {

    int x, y, height;
    Integer rgb;
    QuadTreeNodeImpl tr, tl, br, bl;

    QuadTreeNodeImpl() {
        this.x = 0;
        this.y = 0;
    }

    private QuadTreeNodeImpl(int parentHeight) {
        this.height = parentHeight - 1;
    }

    /**
     * ! Do not delete this method ! Please implement your logic inside this method
     * without modifying the signature of this method, or else your code won't
     * compile.
     * <p/>
     * As always, if you want to create another method, make sure it is not public.
     *
     * @param image image to put into the tree
     * @return the newly build QuadTreeNode instance which stores the compressed
     *         image
     * @throws IllegalArgumentException if image is null
     * @throws IllegalArgumentException if image is empty
     * @throws IllegalArgumentException if image.length is not a power of 2
     * @throws IllegalArgumentException if image, the 2d-array, is not a perfect
     *                                  square
     */
    public static QuadTreeNodeImpl buildFromIntArray(int[][] image) {
        // Exception handling
        if (image == null) {
            throw new IllegalArgumentException("Image is null.");
        } else if (!squareArray(image)) {
            throw new IllegalArgumentException("2D array is not a square.");
        } else if (image.length == 0) {
            throw new IllegalArgumentException("Image is empty.");
        } else if (!isPowerOfTwo(image.length) && image.length != 1) {
            throw new IllegalArgumentException("Image dimension is not a power of 2.");
        }

        QuadTreeNodeImpl root = new QuadTreeNodeImpl();

        // Handles 1x1 image array edge case
        if (image.length == 1) {
            root.rgb = Integer.valueOf(image[0][0]);
            root.height = 0;
            return root;
        }

        int originalHeight = 0;
        // Find height of root node
        while (Math.pow(2, originalHeight) < image.length) {
            originalHeight++;
        }
        root.height = originalHeight;

        root.tr = buildTree(root, image, QuadTreeNode.QuadName.TOP_RIGHT);
        root.tl = buildTree(root, image, QuadTreeNode.QuadName.TOP_LEFT);
        root.br = buildTree(root, image, QuadTreeNode.QuadName.BOTTOM_RIGHT);
        root.bl = buildTree(root, image, QuadTreeNode.QuadName.BOTTOM_LEFT);

        root.merge();
        return root;
    }

    // Static helper function to recurse and build the quad-tree.
    static QuadTreeNodeImpl buildTree(QuadTreeNodeImpl parentNode, 
            int[][] image, QuadTreeNode.QuadName quadrant) {

        QuadTreeNodeImpl root = new QuadTreeNodeImpl();

        root.height = parentNode.height - 1;

        // Set coordinates of root node
        switch (quadrant) {
            case TOP_LEFT:
                root.x = parentNode.x;
                root.y = parentNode.y;
                break;
            case TOP_RIGHT:
                root.x = parentNode.x + (int) (Math.pow(2, root.height));
                root.y = parentNode.y;
                break;
            case BOTTOM_LEFT:
                root.x = parentNode.x;
                root.y = parentNode.y + (int) (Math.pow(2, root.height));
                break;
            case BOTTOM_RIGHT:
                root.x = parentNode.x + (int) (Math.pow(2, root.height));
                root.y = parentNode.y + (int) (Math.pow(2, root.height));
                break;
            default:
                break;
        }

        // Base Case
        if (Math.pow(2, root.height) == 1) {
            root.rgb = Integer.valueOf(image[root.y][root.x]);
            return root;
        }

        // Recursive calls
        root.tr = buildTree(root, image, QuadTreeNode.QuadName.TOP_RIGHT);
        root.tl = buildTree(root, image, QuadTreeNode.QuadName.TOP_LEFT);
        root.br = buildTree(root, image, QuadTreeNode.QuadName.BOTTOM_RIGHT);
        root.bl = buildTree(root, image, QuadTreeNode.QuadName.BOTTOM_LEFT);

        // Merge if possible
        root.merge();

        return root;
    }

    // Helper function that will merge the root's four nodes if possible
    void merge() {
        if (this.tr.rgb == null 
                || this.tl.rgb == null 
                || this.br.rgb == null 
                || this.bl.rgb == null) {
            return;
        }

        if (this.tr.rgb.intValue() == this.tl.rgb.intValue() 
                && this.tr.rgb.intValue() == this.br.rgb.intValue()
                && this.tr.rgb.intValue() == this.bl.rgb.intValue()) {
            this.rgb = this.tr.rgb;
            this.tr = null;
            this.tl = null;
            this.br = null;
            this.bl = null;
        }
    }

    // Helper function to check if array is square or not
    static boolean squareArray(int[][] arr) {
        for (int i = 0; i < arr.length; i++) {
            if (arr[i].length != arr.length) {
                return false;
            }
        }
        return true;
    }

    // Helper function to check if an integer is a power of 2
    static boolean isPowerOfTwo(int x) {
        int i = 1;
        while (Math.pow(2, i) <= x) {
            if (x % Math.pow(2, i) == 0) {
                return true;
            }
            i++;
        }
        return false;
    }

    @Override
    public int getColor(int x, int y) {

        int dimension = this.getDimension();

        // Handle invalid coordinates
        if (x > dimension - 1 || y > dimension - 1 || x < 0 || y < 0) {
            throw new IllegalArgumentException("Invalid coodinates.");
        }

        if (this.isLeaf()) {
            return this.rgb.intValue();
        }

        if (x < dimension / 2 && y < dimension / 2) {
            return this.tl.getColor(x, y);
        } else if (x >= dimension / 2 && y < dimension / 2) {
            return this.tr.getColor(x - dimension / 2, y);
        } else if (x < dimension / 2 && y >= dimension / 2) {
            return this.bl.getColor(x, y - dimension / 2);
        } else {
            return this.br.getColor(x - dimension / 2, y - dimension / 2);
        }
    }

    @Override
    public void setColor(int x, int y, int c) {
        int dimension = this.getDimension();

        // Handle invalid coordinates
        if (x > dimension - 1 || y > dimension - 1 || x < 0 || y < 0) {
            throw new IllegalArgumentException("Invalid coodinates.");
        }

        boolean representsOnePixel = dimension == 1;

        // Handle cases where we are on a leaf node, and decide whether it can stay a
        // leaf node or
        // has to be broken up into four more quadrants.
        if (this.isLeaf()) {
            if (this.rgb.intValue() == c) {
                return;
            } else if (representsOnePixel) {
                this.rgb = Integer.valueOf(c);
                return;
            } else {
                this.createChildren();
            }
        }

        // Traverse to the correct quadrant
        if (x < dimension / 2 && y < dimension / 2) {
            this.tl.setColor(x, y, c);
        } else if (x >= dimension / 2 && y < dimension / 2) {
            this.tr.setColor(x - dimension / 2, y, c);
        } else if (x < dimension / 2 && y >= dimension / 2) {
            this.bl.setColor(x, y - dimension / 2, c);
        } else {
            this.br.setColor(x - dimension / 2, y - dimension / 2, c);
        }

        this.merge();
    }

    // Helper function to split the current leaf into four quadrants.
    void createChildren() {
        this.tr = new QuadTreeNodeImpl(this.height);
        this.tl = new QuadTreeNodeImpl(this.height);
        this.br = new QuadTreeNodeImpl(this.height);
        this.bl = new QuadTreeNodeImpl(this.height);
        this.tr.rgb = this.rgb;
        this.tl.rgb = this.rgb;
        this.br.rgb = this.rgb;
        this.bl.rgb = this.rgb;
        this.tl.x = this.x;
        this.tl.y = this.y;
        this.tr.x = this.x + this.getDimension() / 2;
        this.tr.y = this.y;
        this.bl.x = this.x;
        this.bl.y = this.y + this.getDimension() / 2;
        this.br.x = this.x + this.getDimension() / 2;
        this.br.y = this.y + this.getDimension() / 2;
        this.rgb = null;
    }

    @Override
    public QuadTreeNode getQuadrant(QuadName quadrant) {
        switch (quadrant) {
            case TOP_LEFT:
                return tl;
            case TOP_RIGHT:
                return tr;
            case BOTTOM_LEFT:
                return bl;
            case BOTTOM_RIGHT:
                return br;
            default:
                break;
        }

        return null;
    }

    @Override
    public int getDimension() {
        return (int) (Math.pow(2, this.height));
    }

    @Override
    public int getSize() {
        if (this.isLeaf()) {
            return 1;
        }
        return 1 + this.tr.getSize() + this.tl.getSize() + this.bl.getSize() + this.br.getSize();
    }

    @Override
    public boolean isLeaf() {
        return (this.tr == null && this.tl == null && this.br == null && this.bl == null);
    }

    @Override
    public int[][] decompress() {
        int[][] image = new int[this.getDimension()][this.getDimension()];

        this.buildArray(image);

        return image;
    }

    // Helper method to help recurse through the quad-tree and populate the image
    // array
    int[][] buildArray(int[][] image) {
        if (this.isLeaf()) {
            if (this.height == 0) {
                image[this.y][this.x] = this.rgb.intValue();
                return image;
            } else {
                for (int row = this.y; row < this.y + this.getDimension(); row++) {
                    for (int col = this.x; col < this.x + getDimension(); col++) {
                        image[row][col] = this.rgb.intValue();
                    }
                }
                return image;
            }
        } else {
            image = this.tr.buildArray(image);
            image = this.tl.buildArray(image);
            image = this.br.buildArray(image);
            image = this.bl.buildArray(image);
            return image;
        }
    }

    @Override
    public double getCompressionRatio() {
        return ((double) this.getSize()) / Math.pow(this.getDimension(), 2);
    }
}
