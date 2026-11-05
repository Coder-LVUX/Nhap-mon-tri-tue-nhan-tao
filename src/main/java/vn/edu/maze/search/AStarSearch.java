package vn.edu.maze.search;

import vn.edu.maze.model.*;
import java.util.*;
import java.util.function.BooleanSupplier;

public final class AStarSearch extends AbstractSearch {
    private record Entry(Cell cell, int g, int f, long order) { }
    private int heuristic(Cell c, Cell goal) { return Math.abs(c.row()-goal.row()) + Math.abs(c.col()-goal.col()); }
    @Override public SearchResult search(Maze maze, BooleanSupplier cancelled) {
        long began = System.nanoTime();
        PriorityQueue<Entry> open = new PriorityQueue<>(Comparator.comparingInt(Entry::f).thenComparingLong(Entry::order));
        Map<Cell, Integer> gScore = new HashMap<>();
        Map<Cell, Cell> parent = new HashMap<>();
        Set<Cell> closed = new HashSet<>();
        List<Cell> expanded = new ArrayList<>();
        long order = 0;
        gScore.put(maze.start(), 0);
        open.add(new Entry(maze.start(), 0, heuristic(maze.start(), maze.goal()), order++));
        while (!open.isEmpty()) {
            checkCancelled(cancelled);
            Entry entry = open.remove(); Cell c = entry.cell();
            if (entry.g() != gScore.getOrDefault(c, Integer.MAX_VALUE) || !closed.add(c)) continue;
            expanded.add(c);
            if (c.equals(maze.goal())) return finish(maze, parent, expanded, true, began);
            for (Cell n : maze.neighbors(c)) {
                int next = entry.g()+1;
                if (next < gScore.getOrDefault(n, Integer.MAX_VALUE)) {
                    gScore.put(n, next); parent.put(n, c);
                    open.add(new Entry(n, next, next+heuristic(n, maze.goal()), order++));
                }
            }
        }
        return finish(maze, parent, expanded, false, began);
    }
}
