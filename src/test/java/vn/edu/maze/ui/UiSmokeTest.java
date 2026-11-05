package vn.edu.maze.ui;

import org.junit.jupiter.api.*;
import vn.edu.maze.model.Maze;
import vn.edu.maze.controller.MazeController;
import javax.swing.*;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.nio.file.*;
import java.util.concurrent.atomic.AtomicBoolean;
import static org.junit.jupiter.api.Assertions.*;

class UiSmokeTest {
    private MainFrame view;
    @AfterEach void close() throws Exception {
        if(view!=null) SwingUtilities.invokeAndWait(()->{ view.cancel.doClick(); view.dispose(); });
    }
    @Test void runCancelResetAndNewRunOnEdt() throws Exception {
        Assumptions.assumeFalse(GraphicsEnvironment.isHeadless(),"Cần màn hình để kiểm tra Swing");
        SwingUtilities.invokeAndWait(()->{
            Maze maze=new Maze(5,5); view=new MainFrame(maze); new MazeController(view,maze); view.setVisible(true);
            view.speed.setValue(100); view.run.doClick();
        });
        waitFinished();
        SwingUtilities.invokeAndWait(()->{
            assertTrue(view.status.getText().startsWith("Đã tìm thấy"));
            assertTrue(view.metrics.getText().contains("Số bước: 8"));
            view.rows.setValue(100); view.cols.setValue(100); view.create.doClick();
            view.speed.setValue(1); view.run.doClick(); view.reset.doClick();
            assertEquals("Sẵn sàng.",view.status.getText()); assertTrue(view.run.isEnabled());
            view.rows.setValue(5); view.cols.setValue(5); view.create.doClick();
            view.speed.setValue(100); view.algorithm.setSelectedIndex(2); view.run.doClick();
        });
        waitFinished();
        SwingUtilities.invokeAndWait(()->{
            assertTrue(view.metrics.getText().contains("Số bước: 8"));
            assertTrue(view.status.getText().startsWith("Đã tìm thấy"));
            try {
                Files.createDirectories(Path.of("docs"));
                BufferedImage image=new BufferedImage(view.getWidth(),view.getHeight(),BufferedImage.TYPE_INT_RGB);
                Graphics2D graphics=image.createGraphics(); view.paint(graphics); graphics.dispose();
                ImageIO.write(image,"png",Path.of("docs","giao-dien.png").toFile());
            } catch(Exception ex) { throw new RuntimeException(ex); }
            view.speed.setValue(1); view.run.doClick();
        });
        // Đợi thuật toán xong, hoạt ảnh vẫn đang chạy ở tốc độ thấp.
        Thread.sleep(250);
        SwingUtilities.invokeAndWait(()->{
            assertTrue(view.cancel.isEnabled()); view.cancel.doClick();
            assertFalse(view.cancel.isEnabled()); assertTrue(view.status.getText().startsWith("Đã hủy"));
            view.reset.doClick(); assertTrue(view.metrics.getText().contains("Số bước: —"));
        });
    }
    private void waitFinished() throws Exception {
        long deadline=System.nanoTime()+5_000_000_000L;
        AtomicBoolean done=new AtomicBoolean();
        do {
            SwingUtilities.invokeAndWait(()->done.set(view.run.isEnabled()));
            if(done.get()) return;
            Thread.sleep(20);
        } while(System.nanoTime()<deadline);
        fail("Giao diện không hoàn thành trong 5 giây");
    }
}
