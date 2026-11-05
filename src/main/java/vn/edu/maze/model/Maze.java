package vn.edu.maze.model;

import java.util.ArrayList;
import java.util.List;

/** Lưới được đóng gói; bản sao độc lập được chuyển cho tác vụ tìm kiếm. */
public final class Maze {
    private final boolean[][] walls;
    private Cell start;
    private Cell goal;

    public Maze(int rows, int cols) {
        if (rows < 5 || rows > 100 || cols < 5 || cols > 100)
            throw new IllegalArgumentException("Kích thước phải từ 5 đến 100.");
        walls = new boolean[rows][cols];
        start = new Cell(0, 0);
        goal = new Cell(rows - 1, cols - 1);
    }
    public int rows() { return walls.length; }
    public int cols() { return walls[0].length; }
    public Cell start() { return start; }
    public Cell goal() { return goal; }
    public boolean contains(Cell c) {
        return c != null && c.row() >= 0 && c.row() < rows() && c.col() >= 0 && c.col() < cols();
    }
    private void check(Cell c) {
        if (!contains(c)) throw new IllegalArgumentException("Tọa độ ngoài lưới.");
    }
    public boolean isWall(Cell c) { check(c); return walls[c.row()][c.col()]; }
    public void setWall(Cell c, boolean value) {
        check(c);
        if (value && (c.equals(start) || c.equals(goal)))
            throw new IllegalArgumentException("Không thể đặt tường lên S hoặc G.");
        walls[c.row()][c.col()] = value;
    }
    public void setStart(Cell c) { check(c); if (isWall(c)) throw new IllegalArgumentException("S phải ở ô trống."); start = c; }
    public void setGoal(Cell c) { check(c); if (isWall(c)) throw new IllegalArgumentException("G phải ở ô trống."); goal = c; }
    public List<Cell> neighbors(Cell c) {
        check(c);
        List<Cell> result = new ArrayList<>(4);
        for (int[] d : new int[][]{{-1,0},{0,1},{1,0},{0,-1}}) {
            Cell n = new Cell(c.row()+d[0], c.col()+d[1]);
            if (contains(n) && !isWall(n)) result.add(n);
        }
        return result;
    }
    public Maze copy() {
        Maze copy = new Maze(rows(), cols());
        copy.start = start; copy.goal = goal;
        for (int r = 0; r < rows(); r++) System.arraycopy(walls[r], 0, copy.walls[r], 0, cols());
        return copy;
    }
    public void clearWalls() { for (boolean[] row : walls) java.util.Arrays.fill(row, false); }
}
