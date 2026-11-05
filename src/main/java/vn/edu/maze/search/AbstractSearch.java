package vn.edu.maze.search;

import vn.edu.maze.model.*;
import java.util.*;
import java.util.concurrent.CancellationException;
import java.util.function.BooleanSupplier;

public abstract class AbstractSearch implements SearchAlgorithm {
    protected void checkCancelled(BooleanSupplier cancelled) {
        if (cancelled.getAsBoolean() || Thread.currentThread().isInterrupted()) throw new CancellationException();
    }
    protected SearchResult finish(Maze maze, Map<Cell, Cell> parent, List<Cell> expanded, boolean found, long began) {
        List<Cell> path = new ArrayList<>();
        if (found) {
            for (Cell c = maze.goal(); c != null; c = parent.get(c)) path.add(c);
            Collections.reverse(path);
        }
        return new SearchResult(path, expanded, System.nanoTime() - began);
    }
}
