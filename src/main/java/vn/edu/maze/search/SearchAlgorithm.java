package vn.edu.maze.search;

import vn.edu.maze.model.Maze;
import java.util.function.BooleanSupplier;

public interface SearchAlgorithm {
    SearchResult search(Maze maze, BooleanSupplier cancelled);
}
