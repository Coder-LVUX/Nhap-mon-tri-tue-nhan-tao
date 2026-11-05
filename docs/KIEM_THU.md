# Biên bản kiểm chứng

- Lần kiểm chứng ban đầu: 03/11/2026; chạy lại `mvn -B package` ngày 05/11/2026 để đối chiếu báo cáo, vẫn 14 tests đạt, 0 lỗi, 0 bỏ qua. Windows, Temurin JDK 21.0.11, Maven 3.9.9 cục bộ.
- Java compiler: release 17. Chưa chạy trên runtime JDK 17.
- Lệnh: `.\.tools\apache-maven-3.9.9\bin\mvn.cmd -B clean package`.
- Kết quả: BUILD SUCCESS; 14 tests, 0 failures, 0 errors, 0 skipped.
- `SearchTest`: 8 phương thức; một phương thức lặp 150 mê cung, seed `20261103L`, kích thước 5–30 mỗi chiều, xác suất tường 0.29, giữ S/G trống. Kiểm tra BFS/A* bằng nhau về found/steps, DFS bằng nhau về found và đường hợp lệ.
- `MazeTest`: 2 phương thức kiểm tra dữ liệu và sao chép.
- `MazeFileTest`: 3 phương thức kiểm tra round-trip, nhiều đầu vào lỗi, tệp thiếu.
- `UiSmokeTest`: 1 phương thức thực sự tạo cửa sổ Swing trên EDT, chạy/hủy/đặt lại/chạy mới và xuất `giao-dien.png`. Không bị bỏ qua trong môi trường này.
- Báo cáo JUnit chi tiết ở `target/surefire-reports`. Log build lưu trong `docs/build.log`.
- Đã khởi chạy JAR bằng `java -jar target/maze-search-1.0.0.jar`, tiến trình tồn tại sau 3 giây, stderr rỗng; sau đó đóng cửa sổ. `javap` xác nhận Main có major version 61 (Java 17).
- Báo cáo được xuất lại bằng Microsoft Word 16.0 ngày 05/11/2026: DOCX có bảng/ảnh nhúng, mục lục tự động, giấy A4 và số trang; từng xuất PDF để kiểm tra. Đã xem các trang bìa, mục lục, UML và giao diện. Sau khi dọn tài liệu, giữ bản Word; metadata xuất và script hỗ trợ đã được xóa.

Ảnh Swing đã được mở và xem trực tiếp khi rà soát. Kiểm thử UI dùng doClick trên EDT, không mô phỏng toàn bộ thao tác chuột/keyboard của người dùng. Chưa kiểm thử tự động các hộp thoại tệp, thông báo lỗi và xác nhận ghi đè; logic đọc tệp đã được kiểm tra độc lập. Chưa xác nhận Linux/macOS và runtime JDK 17.
