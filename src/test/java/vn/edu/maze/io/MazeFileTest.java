package vn.edu.maze.io;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import vn.edu.maze.model.*;
import java.nio.file.*;
import java.io.IOException;
import static org.junit.jupiter.api.Assertions.*;

class MazeFileTest {
    @TempDir Path directory;
    @Test void roundTrip() throws IOException {
        Maze maze=new Maze(7,9); maze.setStart(new Cell(2,1)); maze.setGoal(new Cell(5,7));
        maze.setWall(new Cell(0,0),true); maze.setWall(new Cell(3,4),true);
        Path file=directory.resolve("maze.txt"); MazeFile.write(maze,file); Maze read=MazeFile.read(file);
        assertEquals(maze.rows(),read.rows()); assertEquals(maze.cols(),read.cols());
        assertEquals(maze.start(),read.start()); assertEquals(maze.goal(),read.goal());
        for(int r=0;r<maze.rows();r++) for(int c=0;c<maze.cols();c++)
            assertEquals(maze.isWall(new Cell(r,c)),read.isWall(new Cell(r,c)));
    }
    @Test void rejectBadFormat() throws IOException {
        String good="MAZE 1\n5 5\n0 0\n4 4\n.....\n.....\n.....\n.....\n.....\n";
        String[] bad={"",good.replace("MAZE 1","MAZE 2"),good.replace("5 5","4 5"),
                good.replace("0 0","-1 0"),good.replace("0 0","5 0"),good.replace("5 5","x 5"),
                good.replaceFirst("\\.\\.\\.\\.\\.","...."),good.replaceFirst("\\.","X"),
                good.replaceFirst("\\.","#"),good+"extra\n",good.substring(0,good.lastIndexOf(".....")),
                good.replace("0 0","0 0 0")};
        Path file=directory.resolve("invalid.txt");
        for(String text:bad) { Files.writeString(file,text); assertThrows(IOException.class,()->MazeFile.read(file),text); }
        Files.writeString(file,"x".repeat(65537)); assertThrows(IOException.class,()->MazeFile.read(file));
    }
    @Test void missingFile() { assertThrows(IOException.class,()->MazeFile.read(directory.resolve("missing.txt"))); }
}
