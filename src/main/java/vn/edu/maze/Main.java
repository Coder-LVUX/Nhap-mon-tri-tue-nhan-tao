package vn.edu.maze;

import javax.swing.*;
import vn.edu.maze.model.Maze;
import vn.edu.maze.ui.MainFrame;
import vn.edu.maze.controller.MazeController;

public final class Main {
    private Main() { }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(()->{
            try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); }
            catch(Exception ignored) { /* Dùng giao diện Swing mặc định. */ }
            Maze maze=new Maze(20,25);
            MainFrame view=new MainFrame(maze);
            new MazeController(view,maze);
            view.setVisible(true);
        });
    }
}
