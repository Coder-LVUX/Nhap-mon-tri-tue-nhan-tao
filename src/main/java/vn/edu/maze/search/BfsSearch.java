package vn.edu.maze.search;

import vn.edu.maze.model.*;
import java.util.*;
import java.util.function.BooleanSupplier;

public final class BfsSearch extends AbstractSearch {
    @Override public SearchResult search(Maze maze, BooleanSupplier cancelled) {
        long began = System.nanoTime();
        Queue<Cell> queue = new ArrayDeque<>();
        Set<Cell> visited = new HashSet<>();
        Map<Cell, Cell> parent = new HashMap<>();
        List<Cell> expanded = new ArrayList<>();
        queue.add(maze.start()); visited.add(maze.start());
        while (!queue.isEmpty()) {
            checkCancelled(cancelled);
            Cell c = queue.remove(); expanded.add(c);
            if (c.equals(maze.goal())) return finish(maze, parent, expanded, true, began);
            for (Cell n : maze.neighbors(c)) if (visited.add(n)) {
                parent.put(n, c); queue.add(n);
            }
        }
        return finish(maze, parent, expanded, false, began);
    }
}
