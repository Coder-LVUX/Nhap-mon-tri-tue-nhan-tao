package vn.edu.maze.ui;

import vn.edu.maze.model.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

public final class MazePanel extends JPanel {
    private Maze maze;
    private final Set<Cell> expanded = new HashSet<>(), path = new HashSet<>();
    private Consumer<Cell> editor = c -> { };
    public MazePanel(Maze maze) {
        this.maze = maze; setPreferredSize(new Dimension(700, 600)); setBackground(Color.WHITE);
        MouseAdapter mouse = new MouseAdapter() {
            @Override public void mousePressed(MouseEvent e) { edit(e); }
            @Override public void mouseDragged(MouseEvent e) { edit(e); }
        };
        addMouseListener(mouse); addMouseMotionListener(mouse);
    }
    public void setEditor(Consumer<Cell> editor) { this.editor = editor; }
    private int cellSize() { return Math.max(1, Math.min(getWidth()/maze.cols(), getHeight()/maze.rows())); }
    private void edit(MouseEvent e) {
        int size = cellSize();
        int x = e.getX()-(getWidth()-size*maze.cols())/2, y=e.getY()-(getHeight()-size*maze.rows())/2;
        if (x<0 || y<0) return;
        Cell c = new Cell(y/size, x/size);
        if (maze.contains(c)) editor.accept(c);
    }
    public void setMaze(Maze maze) { this.maze=maze; clearResult(); }
    public void clearResult() { expanded.clear(); path.clear(); repaint(); }
    public void showExpanded(Cell c) { expanded.add(c); }
    public void showPath(Cell c) { path.add(c); }
    @Override protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        Graphics2D g=(Graphics2D)graphics.create();
        int size=cellSize(), ox=(getWidth()-size*maze.cols())/2, oy=(getHeight()-size*maze.rows())/2;
        for(int r=0;r<maze.rows();r++) for(int c=0;c<maze.cols();c++) {
            Cell cell=new Cell(r,c);
            Color color=maze.isWall(cell)?new Color(45,55,65):Color.WHITE;
            if(expanded.contains(cell)) color=new Color(170,215,250);
            if(path.contains(cell)) color=new Color(255,210,80);
            if(cell.equals(maze.start())) color=new Color(90,195,130);
            if(cell.equals(maze.goal())) color=new Color(245,125,125);
            int x=ox+c*size,y=oy+r*size;
            g.setColor(color); g.fillRect(x,y,size,size);
            g.setColor(new Color(210,215,220)); g.drawRect(x,y,size,size);
            if(size>=14 && (cell.equals(maze.start())||cell.equals(maze.goal()))) {
                String label=cell.equals(maze.start())?(cell.equals(maze.goal())?"S/G":"S"):"G";
                g.setFont(new Font(Font.SANS_SERIF,Font.BOLD,Math.max(9,Math.min(18,size-3))));
                g.setColor(Color.BLACK); g.drawString(label,x+2,y+size-3);
            }
        }
        g.dispose();
    }
}
