package vn.edu.maze.io;

import vn.edu.maze.model.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;

/** MAZE 1; kích thước; tọa độ S; tọa độ G; rồi rows dòng chỉ gồm . và #. */
public final class MazeFile {
    private MazeFile() { }
    public static Maze read(Path path) throws IOException {
        if (Files.size(path) > 65536) throw new IOException("Tệp mê cung quá lớn (tối đa 64 KiB).");
        List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
        try {
            if (lines.size() < 4 || !lines.get(0).equals("MAZE 1")) throw new IllegalArgumentException("Sai tiêu đề MAZE 1.");
            int[] size = pair(lines.get(1)); Maze maze = new Maze(size[0], size[1]);
            int[] s = pair(lines.get(2)), g = pair(lines.get(3));
            maze.setStart(new Cell(s[0], s[1])); maze.setGoal(new Cell(g[0], g[1]));
            if (lines.size() != maze.rows()+4) throw new IllegalArgumentException("Sai số dòng.");
            for (int r=0; r<maze.rows(); r++) {
                String row = lines.get(r+4);
                if (row.length()!=maze.cols()) throw new IllegalArgumentException("Sai số cột.");
                for (int c=0; c<maze.cols(); c++) {
                    char value = row.charAt(c);
                    if (value!='.' && value!='#') throw new IllegalArgumentException("Chỉ chấp nhận . và #.");
                    if (value=='#') maze.setWall(new Cell(r,c), true);
                }
            }
            return maze;
        } catch (IllegalArgumentException ex) { throw new IOException("Tệp không hợp lệ: " + ex.getMessage(), ex); }
    }
    private static int[] pair(String line) {
        String[] parts = line.trim().split("\\s+");
        if (parts.length!=2) throw new IllegalArgumentException("Cần đúng hai số nguyên.");
        return new int[]{Integer.parseInt(parts[0]), Integer.parseInt(parts[1])};
    }
    public static void write(Maze maze, Path path) throws IOException {
        List<String> lines = new ArrayList<>();
        lines.add("MAZE 1"); lines.add(maze.rows()+" "+maze.cols());
        lines.add(maze.start().row()+" "+maze.start().col()); lines.add(maze.goal().row()+" "+maze.goal().col());
        for (int r=0; r<maze.rows(); r++) {
            StringBuilder row = new StringBuilder();
            for (int c=0; c<maze.cols(); c++) row.append(maze.isWall(new Cell(r,c)) ? '#' : '.');
            lines.add(row.toString());
        }
        Files.write(path, lines, StandardCharsets.UTF_8);
    }
}
