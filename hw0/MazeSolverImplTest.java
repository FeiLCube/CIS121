import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class MazeSolverImplTest {

    private int[][] smallWriteupMaze;
    private int[][] bigWriteupMaze;
    private int[][] customMaze;

    @Before
    public void setupTestMazes() {
        smallWriteupMaze = new int[][]{
                {0, 0, 0, 0},
                {1, 1, 0, 0},
                {0, 0, 0, 1},
                {0, 0, 1, 0}
        };

        bigWriteupMaze = new int[][]{
                {0, 0, 0, 1, 0, 0, 0, 0, 0, 0},
                {0, 1, 1, 1, 1, 1, 0, 1, 1, 0},
                {0, 0, 0, 1, 0, 1, 0, 1, 1, 0},
                {1, 1, 0, 1, 1, 0, 1, 0, 1, 0},
                {0, 1, 0, 1, 0, 0, 0, 0, 1, 0},
                {0, 1, 0, 0, 1, 0, 0, 0, 1, 0},
                {0, 1, 1, 0, 0, 1, 1, 0, 1, 0},
                {0, 0, 1, 1, 0, 0, 0, 0, 0, 0},
                {1, 0, 0, 0, 0, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1}
        };
        
        customMaze = new int[][] {
            {0, 0, 0, 0, 1},
            {1, 1, 0, 0, 0},
            {0, 0, 0, 1, 0},
            {1, 1, 0, 0, 0}
        };
    }

    @Test
    public void testReturnsSmallMazeSolutionPathInWriteup() {
        int[][] solutionPath = {
                {1, 1, 1, 0},
                {0, 0, 1, 0},
                {1, 1, 1, 0},
                {1, 1, 0, 0}
        };
        assertArrayEquals(solutionPath, MazeSolverImpl.solveMaze(smallWriteupMaze,
                new Coordinate(0, 0), new Coordinate(0, 2)));
    }

    @Test
    public void testReturnsBigMazeSolutionPathInWriteup() {
        int[][] bigWriteupSolution = new int[][]{
                {1, 1, 1, 0, 1, 1, 1, 1, 1, 1},
                {1, 0, 0, 0, 0, 0, 0, 0, 0, 1},
                {1, 1, 1, 0, 0, 0, 0, 0, 0, 1},
                {0, 0, 1, 0, 0, 0, 0, 0, 0, 1},
                {0, 0, 1, 0, 0, 0, 0, 0, 0, 1},
                {0, 0, 1, 1, 0, 0, 0, 0, 0, 1},
                {0, 0, 0, 1, 1, 0, 0, 0, 0, 1},
                {0, 0, 0, 0, 1, 1, 1, 1, 1, 1},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0}
        };
        int[][] returnedPath = MazeSolverImpl.solveMaze(bigWriteupMaze, new Coordinate(2, 0),
                new Coordinate(4, 0));
        assertArrayEquals(bigWriteupSolution, returnedPath);
    }

    /*
      Note: the above tests are the ones included in the writeup and NOT exhaustive. The autograder
      uses other test cases not listed above. Please thoroughly read all stub files, including
      CoordinateTest.java!

      For help with creating test cases, please see this link:
      https://www.seas.upenn.edu/~cis121/current/testing_guide.html
     */
    
    @Test
    public void testCustomMaze() {
        int[][] expectedSoln = new int[][] {
            {1, 1, 1, 0, 0},
            {0, 0, 1, 0, 1},
            {0, 0, 1, 0, 1},
            {0, 0, 1, 1, 1}
        };
        
        int[][] returnedPath = MazeSolverImpl.solveMaze(
            customMaze, 
            new Coordinate(0, 0), 
            new Coordinate(4, 1));
        assertArrayEquals(expectedSoln, returnedPath);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullArrayInput() {
        int[][] anyMazeSoln = new int[][]{
            {1, 1, 1, 0},
            {0, 1, 0, 0},
            {0, 0, 0, 1},
            {0, 1, 1, 1}
        };
        int[][] returnedPath = MazeSolverImpl.solveMaze(
            null, 
            new Coordinate(2, 0),
            new Coordinate(3, 0));
        assertArrayEquals(anyMazeSoln, returnedPath);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidMazeDimensions() {
        int[][] invalidMaze = new int[][] {
            {0}
        };
        int[][] anyMazeSoln = new int[][] {
            {0}
        };
        int[][] returnedPath = MazeSolverImpl.solveMaze(
            invalidMaze, 
            new Coordinate(0, 0), 
            new Coordinate(0, 0));
        assertArrayEquals(anyMazeSoln, returnedPath);
        
        invalidMaze = new int[1][0];
        returnedPath = MazeSolverImpl.solveMaze(
            invalidMaze, 
            new Coordinate(0, 0), 
            new Coordinate(0, 0));
        assertArrayEquals(anyMazeSoln, returnedPath);
        
        invalidMaze = new int[0][1];
        returnedPath = MazeSolverImpl.solveMaze(
            invalidMaze, 
            new Coordinate(0, 0), 
            new Coordinate(0, 0));
        assertArrayEquals(anyMazeSoln, returnedPath);
        
        invalidMaze = new int[0][0];
        returnedPath = MazeSolverImpl.solveMaze(
            invalidMaze, 
            new Coordinate(0, 0), 
            new Coordinate(0, 0));
        assertArrayEquals(new int[0][0], returnedPath);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testStartOutofBounds() {
        int[][] anyMazeSoln = new int[][]{
            {1, 1, 1, 0},
            {0, 1, 0, 0},
            {0, 0, 0, 1},
            {0, 1, 1, 1}
        };
        int[][] returnedPath = MazeSolverImpl.solveMaze(
            smallWriteupMaze, 
            new Coordinate(5, 5), 
            new Coordinate(0, 2));
        assertArrayEquals(anyMazeSoln, returnedPath);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGoalOutofBounds() {
        int[][] anyMazeSoln = new int[][]{
            {1, 1, 1, 0},
            {0, 1, 0, 0},
            {0, 0, 0, 1},
            {0, 1, 1, 1}
        };
        int[][] returnedPath = MazeSolverImpl.solveMaze(
            smallWriteupMaze, 
            new Coordinate(2, 0), 
            new Coordinate(5, 8));
        assertArrayEquals(anyMazeSoln, returnedPath);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testStartIsBlocked() {
        int[][] anyMazeSoln = new int[][]{
            {1, 1, 1, 0},
            {0, 1, 0, 0},
            {0, 0, 0, 1},
            {0, 1, 1, 1}
        };
        int[][] returnedPath = MazeSolverImpl.solveMaze(
            smallWriteupMaze, 
            new Coordinate(0, 1), 
            new Coordinate(0, 2));
        assertArrayEquals(anyMazeSoln, returnedPath);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGoalIsBlocked() {
        int[][] anyMazeSoln = new int[][]{
            {1, 1, 1, 0},
            {0, 1, 0, 0},
            {0, 0, 0, 1},
            {0, 1, 1, 1}
        };
        int[][] returnedPath = MazeSolverImpl.solveMaze(
            smallWriteupMaze, 
            new Coordinate(0, 0), 
            new Coordinate(1, 1));
        assertArrayEquals(anyMazeSoln, returnedPath);
    }
    
    @Test
    public void sameStartAndGoal() {
        int[][] testMaze = new int[][] {
            {0, 0, 1, 0},
            {1, 0, 0, 1}
        };
        Coordinate start = new Coordinate(0, 0);
        Coordinate goal = new Coordinate(0, 0);
        int[][] returnedPath = MazeSolverImpl.solveMaze(testMaze, start, goal);
        int[][] mazeSoln = new int[][] {
            {1, 0, 0, 0},
            {0, 0, 0, 0}
        };
        assertArrayEquals(mazeSoln, returnedPath);
    }
    
    @Test
    public void testAlreadyVisited() {
        Coordinate c1 = new Coordinate(0, 0);
        Coordinate c2 = new Coordinate(1, 0);
        Coordinate c3 = new Coordinate(3, 1);
        Coordinate[] testArr = new Coordinate[0];
        assertFalse(MazeSolverImpl.alreadyVisited(c1, testArr));
        assertFalse(MazeSolverImpl.alreadyVisited(c2, testArr));
        assertFalse(MazeSolverImpl.alreadyVisited(c3, testArr));
        testArr = new Coordinate[] {
            new Coordinate(1, 3), new Coordinate(2, 2)
        };
        assertFalse(MazeSolverImpl.alreadyVisited(c1, testArr));
        assertFalse(MazeSolverImpl.alreadyVisited(c2, testArr));
        assertFalse(MazeSolverImpl.alreadyVisited(c3, testArr));
        testArr = new Coordinate[] {
            new Coordinate(0, 0), new Coordinate(2, 2), new Coordinate(3, 1)
        };
        assertTrue(MazeSolverImpl.alreadyVisited(c1, testArr));
        assertFalse(MazeSolverImpl.alreadyVisited(c2, testArr));
        assertTrue(MazeSolverImpl.alreadyVisited(c3, testArr));
    }
    
    @Test
    public void testAddtoPath() {
        Coordinate c = new Coordinate(0, 0);
        Coordinate[] testArr = new Coordinate[0];
        testArr = MazeSolverImpl.addToPath(c, testArr);
        Coordinate[] expected = new Coordinate[] {
            c
        };
        assertArrayEquals(expected, testArr);
        
        c = new Coordinate(3, 0);
        testArr = MazeSolverImpl.addToPath(c, testArr);
        expected = new Coordinate[] {
            new Coordinate(0, 0), c
        };
        assertArrayEquals(expected, testArr);
    }
    
    @Test
    public void testRemoveFromPath() {
        Coordinate[] testArr = new Coordinate[] {
            new Coordinate(0, 1), new Coordinate(2, 2), new Coordinate(3, 2)
        };
        testArr = MazeSolverImpl.removeFromPath(testArr);
        Coordinate[] expected = new Coordinate[] {
            new Coordinate(0, 1), new Coordinate(2, 2)
        };
        assertArrayEquals(expected, testArr);
        
        testArr = MazeSolverImpl.removeFromPath(testArr);
        expected = new Coordinate[] { 
            new Coordinate(0, 1) 
        };
        assertArrayEquals(expected, testArr);
    }
    
    @Test
    public void testNoSolution() {
        int[][] expected = new int[0][0];
        int[][] result = MazeSolverImpl.solveMaze(
            smallWriteupMaze, 
            new Coordinate(0, 0), 
            new Coordinate(3, 3));
        assertArrayEquals(expected, result);
    }
    
    @Test
    public void testBlockedStart() {
        int[][] expected = new int[0][0];
        int[][] result = MazeSolverImpl.solveMaze(
            smallWriteupMaze, 
            new Coordinate(3, 3), 
            new Coordinate(0, 0));
        assertArrayEquals(expected, result);
    }
}
