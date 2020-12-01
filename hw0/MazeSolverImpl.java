public class MazeSolverImpl {
    
    private static int[][] solvedMaze;
    private static Coordinate[] path = new Coordinate[0];
    private static Coordinate[] visitedCoords = new Coordinate[0];

    /**
     * You should write your code within this method. A good rule of thumb, especially with
     * recursive problems like this, is to write your input exception handling within this
     * method and then use helper methods to carry out the actual recursion.
     * <p>
     * How you set up the recursive methods is up to you, but note that since this is a *static*
     * method, all helper methods that it calls must *also* be static. Make them package-private
     * (i.e. without private or public modifiers) so you can test them individually.
     *
     * @param maze See the writeup for more details about the format of the input maze.
     * @param sourceCoord The source (starting) coordinate
     * @param goalCoord The goal (ending) coordinate
     * @return a matrix of the same dimension as the input maze containing the solution
     * path marked with 1's, or null if no path exists. See the writeup for more details.
     * @throws IllegalArgumentException in the following instances:
     * 1. If the maze is null
     * 2. If m * n <= 1 where m and n are the dimensions of the maze
     * 3. If either the source coordinate OR the goal coordinate are out of the matrix bounds.
     * 4. If your source or goal coordinate are on a blocked tile.
     */
    public static int[][] solveMaze(int[][] maze, Coordinate sourceCoord, Coordinate goalCoord) {
        // Check if maze is null or if dimensions are valid
        if (isInvalidMaze(maze)) {
            throw new IllegalArgumentException(
                "Maze is either null or the dimensions are invalid!");
        } else if (isInvalidCoordinate(maze, sourceCoord)) {
            throw new IllegalArgumentException("Starting coordinate is out of bounds!");
        } else if (isInvalidCoordinate(maze, goalCoord)) {
            throw new IllegalArgumentException("Goal coordinate is out of bounds!");
        }
        
        solvedMaze = new int[maze.length][maze[0].length];
        path = new Coordinate[0];
        visitedCoords = new Coordinate[0];
        
        return solve(maze, sourceCoord, goalCoord);
    }
    
    // The recursive function used to solve the maze
    static int[][] solve(int[][] maze, Coordinate startCoord, Coordinate goalCoord) {
        
        int x = startCoord.getX();
        int y = startCoord.getY();

        // Base Cases
        if (startCoord.equals(goalCoord)) {
            solvedMaze[y][x] = 1;
            
            for (int i = 0; i < path.length; i++) {
                Coordinate c = path[i];
                solvedMaze[c.getY()][c.getX()] = 1;
            }

            return solvedMaze;
        }
      
        
        // Update the coordinates we have visited
        if (!alreadyVisited(startCoord, visitedCoords)) {
            Coordinate[] tempArr = new Coordinate[visitedCoords.length + 1];
            
            for (int i = 0; i < visitedCoords.length; i++) {
                tempArr[i] = visitedCoords[i];
            }
            
            tempArr[visitedCoords.length] = startCoord;
            visitedCoords = tempArr;
        }
        
        Coordinate onePointDown = new Coordinate(x, y + 1);
        Coordinate onePointUp = new Coordinate(x, y - 1);
        Coordinate onePointLeft = new Coordinate(x - 1, y);
        Coordinate onePointRight = new Coordinate(x + 1, y);
        
        if (!isInvalidCoordinate(maze, onePointDown) && 
            !alreadyVisited(onePointDown, visitedCoords)) {
            path = addToPath(startCoord, path);
            return solve(maze, onePointDown, goalCoord);
        } else if (!isInvalidCoordinate(maze, onePointUp) && 
            !alreadyVisited(onePointUp, visitedCoords)) {
            path = addToPath(startCoord, path);
            return solve(maze, onePointUp, goalCoord);
        } else if (!isInvalidCoordinate(maze, onePointLeft) && 
            !alreadyVisited(onePointLeft, visitedCoords)) {
            path = addToPath(startCoord, path);
            return solve(maze, onePointLeft, goalCoord);
        } else if (!isInvalidCoordinate(maze, onePointRight) && 
            !alreadyVisited(onePointRight, visitedCoords)) {
            path = addToPath(startCoord, path);
            return solve(maze, onePointRight, goalCoord);
        } else {
            path = removeFromPath(path);
            if (path.length == 0) {
                return new int[0][0];
            }
            Coordinate prevCoord = path[path.length - 1];
            return solve(maze, prevCoord, goalCoord);
        }
    }
    
    // Helper function to add a coordinate to our path
    static Coordinate[] addToPath(Coordinate coord, Coordinate[] arr) {
        Coordinate[] tempArr = new Coordinate[arr.length + 1];
        
        for (int i = 0; i < arr.length; i++) {
            tempArr[i] = arr[i];
        }
        
        tempArr[arr.length] = coord;
        arr = tempArr;
        
        return arr;
    }
    
    // Helper function to remove a coordinate from our path
    static Coordinate[] removeFromPath(Coordinate[] arr) {
        if (arr.length == 0) {
            return arr;
        }
        
        Coordinate[] tempArr = new Coordinate[arr.length - 1];
        
        for (int i = 0; i < tempArr.length; i++) {
            tempArr[i] = arr[i];
        }
        
        arr = tempArr;
        
        return arr;
    }
    
    // Helper function to check if the coordinate has already been visited
    static boolean alreadyVisited(Coordinate coord, Coordinate[] arr) {
        
        for (int i = 0; i < arr.length; i ++) {
            if (arr[i].equals(coord)) {
                return true;
            }
        }
        
        return false;
    }
    
    // Helper function to check for invalid maze
    static boolean isInvalidMaze(int[][] maze) {
        
        if (maze == null ||
                maze.length == 0 ||
                maze[0].length == 0 ||
                (maze.length == 1 && maze[0].length == 1)) {
            return true;
        } else {
            return false;
        }
    }
    
    // Helper function to check for valid coordinate
    static boolean isInvalidCoordinate(int[][] maze, Coordinate coord) {
        
        int coordX = coord.getX();
        int coordY = coord.getY();
        int mazeHeight = maze.length;
        int mazeWidth = maze[0].length;
        
        if (coordX < 0 ||
            coordY < 0 || 
            coordX > mazeWidth - 1 || 
            coordY > mazeHeight - 1 ||
            maze[coordY][coordX] == 1) {
            return true;
        } else {
            return false;
        }
    }
}
