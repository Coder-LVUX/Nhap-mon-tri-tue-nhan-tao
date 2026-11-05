package vn.edu.maze.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MazeTest {
    @Test void validatesBoundsAndEndpoints() {
        assertThrows(IllegalArgumentException.class,()->new Maze(4,5));
        assertThrows(IllegalArgumentException.class,()->new Maze(5,101));
        Maze maze=new Maze(5,5);
        assertThrows(IllegalArgumentException.class,()->maze.setWall(maze.start(),true));
        assertThrows(IllegalArgumentException.class,()->maze.setWall(maze.goal(),true));
        assertThrows(IllegalArgumentException.class,()->maze.setStart(new Cell(-1,0)));
        assertThrows(IllegalArgumentException.class,()->maze.setGoal(null));
        maze.setWall(new Cell(1,1),true);
        assertThrows(IllegalArgumentException.class,()->maze.setGoal(new Cell(1,1)));
    }
    @Test void copyIsIndependent() {
        Maze maze=new Maze(5,5); Maze copy=maze.copy();
        copy.setWall(new Cell(1,1),true); copy.setStart(new Cell(1,0));
        assertFalse(maze.isWall(new Cell(1,1))); assertEquals(new Cell(0,0),maze.start());
    }
}
