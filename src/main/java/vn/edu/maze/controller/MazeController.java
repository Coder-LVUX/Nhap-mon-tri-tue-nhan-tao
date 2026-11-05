package vn.edu.maze.controller;

import vn.edu.maze.model.*;
import vn.edu.maze.search.*;
import vn.edu.maze.io.MazeFile;
import vn.edu.maze.ui.MainFrame;
import javax.swing.*;
import java.nio.file.Path;
import java.util.concurrent.*;

/** Toàn bộ trạng thái giao diện chỉ được thay đổi trên EDT. */
public final class MazeController {
    private Maze maze;
    private final MainFrame view;
    private SwingWorker<SearchResult,Void> worker;
    private Timer animation;
    private long generation;
    private boolean busy;
    public MazeController(MainFrame view, Maze maze) {
        this.view=view; this.maze=maze;
        view.board.setEditor(this::edit);
        view.run.addActionListener(e->run()); view.cancel.addActionListener(e->cancel());
        view.reset.addActionListener(e->{ invalidate(); resetResult(); });
        view.clear.addActionListener(e->{ invalidate(); maze.clearWalls(); resetResult(); });
        view.create.addActionListener(e->create());
        view.open.addActionListener(e->open()); view.save.addActionListener(e->save());
    }
    private void setBusy(boolean value) {
        busy=value; view.run.setEnabled(!value); view.cancel.setEnabled(value);
        view.editMode.setEnabled(!value); view.algorithm.setEnabled(!value);
    }
    private void invalidate() {
        generation++;
        if(worker!=null) { worker.cancel(true); worker=null; }
        if(animation!=null) { animation.stop(); animation=null; }
        setBusy(false);
    }
    private void resetResult() {
        view.board.clearResult(); view.metrics.setText("Số bước: — | Ô mở rộng: — | Thời gian thuật toán: —");
        view.status.setText("Sẵn sàng.");
    }
    private void cancel() { invalidate(); view.status.setText("Đã hủy. Có thể chạy lại hoặc đặt lại kết quả."); }
    private void edit(Cell cell) {
        if(busy) return;
        try {
            switch(view.editMode.getSelectedIndex()) {
                case 0 -> maze.setWall(cell,true);
                case 1 -> maze.setWall(cell,false);
                case 2 -> { if(cell.equals(maze.goal())) throw new IllegalArgumentException("Chọn S khác G trên giao diện."); maze.setStart(cell); }
                case 3 -> { if(cell.equals(maze.start())) throw new IllegalArgumentException("Chọn G khác S trên giao diện."); maze.setGoal(cell); }
                default -> throw new IllegalStateException();
            }
            resetResult();
        } catch(IllegalArgumentException ex) { view.status.setText(ex.getMessage()); }
    }
    private void create() {
        try {
            view.rows.commitEdit(); view.cols.commitEdit();
            Maze next=new Maze((Integer)view.rows.getValue(),(Integer)view.cols.getValue());
            replace(next);
        } catch(Exception ex) { error("Kích thước không hợp lệ: nhập số nguyên từ 5 đến 100."); }
    }
    private void replace(Maze next) {
        invalidate(); maze=next; view.board.setMaze(maze);
        view.rows.setValue(maze.rows()); view.cols.setValue(maze.cols()); resetResult();
    }
    private void run() {
        invalidate(); resetResult(); setBusy(true);
        long token=generation;
        Maze snapshot=maze.copy();
        SearchAlgorithm algorithm=switch(view.algorithm.getSelectedIndex()) {
            case 1 -> new DfsSearch(); case 2 -> new AStarSearch(); default -> new BfsSearch();
        };
        view.status.setText("Đang tìm đường…");
        worker=new SwingWorker<>() {
            @Override protected SearchResult doInBackground() { return algorithm.search(snapshot,this::isCancelled); }
            @Override protected void done() {
                if(token!=generation || isCancelled()) return;
                try { animate(get(),token); }
                catch(CancellationException ex) { setBusy(false); }
                catch(InterruptedException ex) { Thread.currentThread().interrupt(); setBusy(false); }
                catch(ExecutionException ex) { setBusy(false); error("Lỗi tìm kiếm: "+ex.getCause().getMessage()); }
            }
        };
        worker.execute();
    }
    private void animate(SearchResult result,long token) {
        view.metrics.setText(String.format("Số bước: %s | Ô mở rộng: %d | Thời gian thuật toán: %.3f ms",
                result.found()?result.steps():"—",result.expanded().size(),result.elapsedNanos()/1_000_000.0));
        view.status.setText("Đang phát hoạt ảnh… (có thể chỉnh tốc độ hoặc hủy)");
        int[] index={0};
        animation=new Timer(16,e->{
            if(token!=generation) { ((Timer)e.getSource()).stop(); return; }
            int speed=view.speed.getValue();
            ((Timer)e.getSource()).setDelay(speed<50 ? 20+(50-speed)*4 : 16);
            int batch=speed<50?1:1+(speed-50)*4;
            for(int i=0;i<batch;i++) {
                int pos=index[0]++;
                if(pos<result.expanded().size()) view.board.showExpanded(result.expanded().get(pos));
                else if(pos<result.expanded().size()+result.path().size()) view.board.showPath(result.path().get(pos-result.expanded().size()));
                else {
                    ((Timer)e.getSource()).stop(); animation=null; setBusy(false);
                    view.status.setText(result.found()?"Đã tìm thấy đường. DFS không đảm bảo tối ưu.":"Không có đường từ S đến G."); break;
                }
            }
            view.board.repaint();
        });
        animation.start();
    }
    private void open() {
        JFileChooser chooser=new JFileChooser();
        if(chooser.showOpenDialog(view)!=JFileChooser.APPROVE_OPTION) return;
        try { Maze next=MazeFile.read(chooser.getSelectedFile().toPath()); replace(next); }
        catch(Exception ex) { error(ex.getMessage()); }
    }
    private void save() {
        JFileChooser chooser=new JFileChooser();
        if(chooser.showSaveDialog(view)!=JFileChooser.APPROVE_OPTION) return;
        Path path=chooser.getSelectedFile().toPath();
        if(java.nio.file.Files.exists(path) && JOptionPane.showConfirmDialog(view,"Tệp đã tồn tại. Ghi đè?","Xác nhận",JOptionPane.YES_NO_OPTION)!=JOptionPane.YES_OPTION) return;
        try { MazeFile.write(maze,path); view.status.setText("Đã lưu: "+path.getFileName()); }
        catch(Exception ex) { error("Không lưu được: "+ex.getMessage()); }
    }
    private void error(String message) { JOptionPane.showMessageDialog(view,message,"Thông báo",JOptionPane.ERROR_MESSAGE); }
}
