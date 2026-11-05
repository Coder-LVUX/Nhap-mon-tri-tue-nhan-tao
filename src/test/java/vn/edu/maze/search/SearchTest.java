package vn.edu.maze.search;

import org.junit.jupiter.api.Test;
import vn.edu.maze.model.*;
import java.util.*;
import java.util.concurrent.CancellationException;
import static org.junit.jupiter.api.Assertions.*;

class SearchTest {
    private List<SearchAlgorithm> algorithms() { return List.of(new BfsSearch(),new DfsSearch(),new AStarSearch()); }
    private void valid(Maze maze, SearchResult result) {
        assertTrue(result.found());
        assertEquals(maze.start(),result.path().get(0));
        assertEquals(maze.goal(),result.path().get(result.path().size()-1));
        assertEquals(result.path().size()-1,result.steps());
        assertEquals(result.expanded().size(),new HashSet<>(result.expanded()).size());
        assertEquals(result.path().size(),new HashSet<>(result.path()).size());
        Cell previous=null;
        for(Cell c:result.path()) {
            assertTrue(maze.contains(c)); assertFalse(maze.isWall(c));
            if(previous!=null) assertEquals(1,Math.abs(c.row()-previous.row())+Math.abs(c.col()-previous.col()));
            previous=c;
        }
        assertTrue(result.elapsedNanos()>=0);
    }
    @Test void validPathAvoidsWalls() {
        Maze maze=new Maze(8,8);
        for(int r=0;r<7;r++) maze.setWall(new Cell(r,3),true);
        for(SearchAlgorithm a:algorithms()) valid(maze,a.search(maze,()->false));
        assertEquals(14,new BfsSearch().search(maze,()->false).steps());
    }
    @Test void unreachable() {
        Maze maze=new Maze(5,5);
        for(int c=0;c<5;c++) maze.setWall(new Cell(2,c),true);
        for(SearchAlgorithm a:algorithms()) {
            SearchResult result=a.search(maze,()->false);
            assertFalse(result.found()); assertEquals(-1,result.steps()); assertEquals(10,result.expanded().size());
        }
    }
    @Test void sameStartAndGoal() {
        Maze maze=new Maze(5,5); maze.setGoal(maze.start());
        for(SearchAlgorithm a:algorithms()) {
            SearchResult result=a.search(maze,()->false); valid(maze,result);
            assertEquals(0,result.steps()); assertEquals(1,result.expanded().size());
        }
    }
    @Test void cyclicOpenGrid() {
        Maze maze=new Maze(10,10);
        for(SearchAlgorithm a:algorithms()) valid(maze,a.search(maze,()->false));
        assertEquals(18,new AStarSearch().search(maze,()->false).steps());
    }
    @Test void repeatedCallsHaveNoOldState() {
        for(SearchAlgorithm a:algorithms()) {
            Maze maze=new Maze(5,5); SearchResult first=a.search(maze,()->false);
            for(int c=0;c<5;c++) maze.setWall(new Cell(2,c),true);
            assertFalse(a.search(maze,()->false).found());
            maze.clearWalls(); SearchResult again=a.search(maze,()->false);
            assertEquals(first.path(),again.path()); assertEquals(first.expanded(),again.expanded());
        }
    }
    @Test void compare150SeededRandomMazes() {
        Random random=new Random(20261103L);
        for(int sample=0;sample<150;sample++) {
            Maze maze=new Maze(5+random.nextInt(26),5+random.nextInt(26));
            for(int r=0;r<maze.rows();r++) for(int c=0;c<maze.cols();c++) {
                Cell cell=new Cell(r,c);
                if(!cell.equals(maze.start())&&!cell.equals(maze.goal())&&random.nextDouble()<0.29) maze.setWall(cell,true);
            }
            SearchResult bfs=new BfsSearch().search(maze,()->false), astar=new AStarSearch().search(maze,()->false);
            assertEquals(bfs.found(),astar.found(),"Mẫu "+sample);
            assertEquals(bfs.steps(),astar.steps(),"Mẫu "+sample);
            SearchResult dfs=new DfsSearch().search(maze,()->false);
            assertEquals(bfs.found(),dfs.found());
            if(bfs.found()) { valid(maze,bfs); valid(maze,astar); valid(maze,dfs); }
        }
    }
    @Test void cancellationAndReuse() {
        for(SearchAlgorithm a:algorithms()) {
            assertThrows(CancellationException.class,()->a.search(new Maze(100,100),()->true));
            valid(new Maze(5,5),a.search(new Maze(5,5),()->false));
        }
    }
    @Test void maximumGrid() {
        Maze maze=new Maze(100,100);
        for(SearchAlgorithm a:algorithms()) valid(maze,a.search(maze,()->false));
        assertEquals(198,new BfsSearch().search(maze,()->false).steps());
    }
}
