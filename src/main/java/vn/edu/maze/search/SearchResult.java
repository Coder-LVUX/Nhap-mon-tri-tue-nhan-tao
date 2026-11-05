package vn.edu.maze.search;

import vn.edu.maze.model.Cell;
import java.util.List;

public record SearchResult(List<Cell> path, List<Cell> expanded, long elapsedNanos) {
    public SearchResult { path = List.copyOf(path); expanded = List.copyOf(expanded); }
    public boolean found() { return !path.isEmpty(); }
    public int steps() { return found() ? path.size() - 1 : -1; }
}
