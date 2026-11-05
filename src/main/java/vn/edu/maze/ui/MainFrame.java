package vn.edu.maze.ui;

import vn.edu.maze.model.Maze;
import javax.swing.*;
import java.awt.*;

public final class MainFrame extends JFrame {
    public final MazePanel board;
    public final JComboBox<String> algorithm=new JComboBox<>(new String[]{"BFS", "DFS", "A*"});
    public final JComboBox<String> editMode=new JComboBox<>(new String[]{"Vẽ tường", "Xóa tường", "Đặt S", "Đặt G"});
    public final JSpinner rows=new JSpinner(new SpinnerNumberModel(20,5,100,1));
    public final JSpinner cols=new JSpinner(new SpinnerNumberModel(25,5,100,1));
    public final JButton create=new JButton("Tạo lưới"), run=new JButton("Chạy"), cancel=new JButton("Hủy"),
            reset=new JButton("Đặt lại kết quả"), clear=new JButton("Xóa mê cung"), open=new JButton("Mở tệp"), save=new JButton("Lưu tệp");
    public final JSlider speed=new JSlider(1,100,75);
    public final JLabel status=new JLabel("Sẵn sàng. Chọn công cụ rồi nhấn hoặc kéo chuột trên lưới.");
    public final JLabel metrics=new JLabel("Số bước: — | Ô mở rộng: — | Thời gian thuật toán: —");
    public MainFrame(Maze maze) {
        super("Tìm đường mê cung • BFS / DFS / A*");
        board=new MazePanel(maze);
        JPanel tools=new JPanel(); tools.setLayout(new BoxLayout(tools,BoxLayout.Y_AXIS));
        tools.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        tools.add(new JLabel("Kích thước (5–100)"));
        JPanel dimensions=new JPanel(new GridLayout(2,2,5,5));
        dimensions.add(new JLabel("Số hàng")); dimensions.add(rows); dimensions.add(new JLabel("Số cột")); dimensions.add(cols);
        tools.add(dimensions); tools.add(create); tools.add(Box.createVerticalStrut(12));
        tools.add(new JLabel("Công cụ chuột")); tools.add(editMode);
        tools.add(new JLabel("Thuật toán")); tools.add(algorithm);
        for(JButton b:new JButton[]{run,cancel,reset,clear,open,save}) { tools.add(Box.createVerticalStrut(5)); tools.add(b); }
        tools.add(Box.createVerticalStrut(12)); tools.add(new JLabel("Tốc độ: chậm → nhanh")); tools.add(speed);
        tools.add(new JLabel("<html><br>■ Xám: tường<br>□ Trắng: ô trống<br><font color='#5595cc'>■ Xanh dương: đã duyệt</font><br><font color='#b08000'>■ Vàng: đường đi</font><br><font color='#238046'>S: bắt đầu (xanh lá)</font><br><font color='#c34545'>G: đích (đỏ)</font><br><br>DFS không đảm bảo<br>đường ngắn nhất.</html>"));
        // BoxLayout không kéo giãn chiều cao của spinner và hộp chọn.
        dimensions.setMaximumSize(new Dimension(230,65));
        for(JComboBox<?> box:new JComboBox<?>[]{editMode,algorithm}) box.setMaximumSize(new Dimension(230,28));
        speed.setMaximumSize(new Dimension(230,35));
        for(Component component:tools.getComponents())
            if(component instanceof JComponent item) item.setAlignmentX(Component.LEFT_ALIGNMENT);
        tools.add(Box.createVerticalGlue()); tools.setPreferredSize(new Dimension(230,650));
        JPanel bottom=new JPanel(new GridLayout(2,1)); bottom.setBorder(BorderFactory.createEmptyBorder(5,10,8,10)); bottom.add(metrics); bottom.add(status);
        add(tools,BorderLayout.WEST); add(board,BorderLayout.CENTER); add(bottom,BorderLayout.SOUTH);
        cancel.setEnabled(false); setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(900,700)); setSize(1100,800); setLocationRelativeTo(null);
    }
}
