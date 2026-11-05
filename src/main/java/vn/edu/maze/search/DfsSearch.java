package vn.edu.maze.search;

import vn.edu.maze.model.*;
import java.util.*;
import java.util.function.BooleanSupplier;

public final class DfsSearch extends AbstractSearch {
    @Override public SearchResult search(Maze maze, BooleanSupplier cancelled) {
        long began = System.nanoTime();
        Deque<Cell> stack = new ArrayDeque<>();
        Set<Cell> visited = new HashSet<>();
        Map<Cell, Cell> parent = new HashMap<>();
        List<Cell> expanded = new ArrayList<>();
        stack.push(maze.start()); visited.add(maze.start());
        while (!stack.isEmpty()) {
            checkCancelled(cancelled);
            Cell c = stack.pop(); expanded.add(c);
            if (c.equals(maze.goal())) return finish(maze, parent, expanded, true, began);
            List<Cell> neighbors = maze.neighbors(c);
            // Đẩy ngược để ưu tiên lấy lên, phải, xuống, trái.
            for (int i = neighbors.size()-1; i >= 0; i--) {
                Cell n = neighbors.get(i);
                if (visited.add(n)) { parent.put(n, c); stack.push(n); }
            }
        }
        return finish(maze, parent, expanded, false, began);
    }
}
