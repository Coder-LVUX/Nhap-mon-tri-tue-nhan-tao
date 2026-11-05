# Ứng dụng tìm đường trong mê cung

Đồ án Lập trình hướng đối tượng cho nhóm 3 sinh viên. Java 17, Swing, Maven, JUnit 5; không có thư viện chạy ngoài JDK.

## Chạy nhanh trên Windows

1. Cài **JDK 17 trở lên** (ví dụ Eclipse Temurin 17), thêm thư mục `bin` vào PATH. Kiểm tra bằng `java -version`. Muốn build cần cả `javac -version`.
2. JAR đã được tạo ở `target/maze-search-1.0.0.jar`. Nhấp đúp `run.bat`, hoặc mở PowerShell trong thư mục dự án:

```powershell
.\run.bat
# Hoặc:
java -jar .\target\maze-search-1.0.0.jar
```

Nếu đóng gói gửi nhóm khác, phải giữ thư mục `target` chứa JAR cùng với hai script chạy.

## Build và kiểm thử

Cài Maven 3.9 trở lên, giải nén và thêm `bin` vào PATH; kiểm tra `mvn -version`. Lần build đầu cần Internet để tải plugin và JUnit.

```powershell
mvn clean package
mvn test
java -jar target/maze-search-1.0.0.jar
```

Trong workspace này có Maven 3.9.9 cục bộ (không cần thêm PATH):

```powershell
.\.tools\apache-maven-3.9.9\bin\mvn.cmd clean package
```

Linux/macOS, cài JDK và Maven tương tự rồi chạy:

```sh
mvn clean package
sh run.sh
# Hoặc chmod +x run.sh rồi ./run.sh
```

Compiler dùng `--release 17`. Phiên kiểm chứng thực tế dùng Temurin JDK 21.0.11 trên Windows, không khẳng định đã chạy bằng JDK 17. JAR được tạo ở định dạng bytecode Java 17.

## Sử dụng

- Nhập số hàng/cột 5–100, bấm **Tạo lưới**. Tạo lưới mới thay mê cung hiện tại.
- Chọn **Vẽ tường** hoặc **Xóa tường**, nhấn/kéo chuột. Chọn **Đặt S / Đặt G** rồi nhấn ô trống. Không đặt tường lên S/G. Giao diện chọn S/G khác nhau; tầng mô hình, tệp và thuật toán hỗ trợ S trùng G.
- Chọn BFS, DFS hoặc A*, bấm **Chạy**. Màu xanh dương là ô đã mở rộng, vàng là đường đi. Kéo thanh tốc độ trong lúc phát hoạt ảnh.
- **Hủy** dừng tìm kiếm hoặc hoạt ảnh, giữ hình đang xem. **Đặt lại kết quả** hủy tác vụ và xóa màu kết quả, giữ tường/S/G. **Xóa mê cung** hủy tác vụ, xóa tường và kết quả, giữ S/G.
- Trong lúc chạy không sửa ô; vẫn có thể đặt lại, xóa, tạo hoặc mở mê cung khác. Kết quả tác vụ cũ bị bỏ qua.
- **Mở tệp** đọc và kiểm tra toàn bộ trước khi thay mô hình; lỗi giữ nguyên mê cung. **Lưu tệp** hỏi trước khi ghi đè. Chưa có cảnh báo thay đổi chưa lưu khi tạo/mở/thoát.
- Độ dài là số bước. Ô mở rộng là số ô lấy hợp lệ khỏi cấu trúc chờ, gồm S và G nếu tìm thấy. Thời gian thuật toán dùng `nanoTime`, gồm tìm và truy vết, không gồm sao chép mô hình, SwingWorker chờ hoặc hoạt ảnh.

## Định dạng tệp

UTF-8, tối đa 64 KiB. Tọa độ bắt đầu từ 0. Dòng 1 là `MAZE 1`; dòng 2 số hàng/cột; dòng 3 tọa độ S; dòng 4 tọa độ G; tiếp theo đúng số hàng, mỗi hàng đúng số cột ký tự `.` (trống) hoặc `#` (tường). Không có dòng chú thích hoặc dòng rỗng thừa.

```text
MAZE 1
5 5
0 0
4 4
.....
.###.
...#.
.#...
.....
```

Thử `samples/co-duong.txt` và `samples/khong-co-duong.txt`.

## Tệp bàn giao

```text
pom.xml, .gitignore, README.md, run.bat, run.sh
src/main/java/vn/edu/maze/
  Main.java
  model/Cell.java, Maze.java
  search/SearchAlgorithm.java, AbstractSearch.java, SearchResult.java
  search/BfsSearch.java, DfsSearch.java, AStarSearch.java
  controller/MazeController.java
  ui/MainFrame.java, MazePanel.java
  io/MazeFile.java
src/test/java/vn/edu/maze/
  model/MazeTest.java
  search/SearchTest.java
  io/MazeFileTest.java
  ui/UiSmokeTest.java
samples/co-duong.txt, khong-co-duong.txt
docs/BAO_CAO.docx, UML.md, KIEM_THU.md
docs/giao-dien.png, uml-bao-cao.png, build.log
target/maze-search-1.0.0.jar
target/surefire-reports/ (kết quả JUnit do Maven sinh)
```

Xem [báo cáo Word](docs/BAO_CAO.docx), [UML](docs/UML.md), [biên bản kiểm thử](docs/KIEM_THU.md). Ảnh giao diện được xuất từ cửa sổ Swing thực trong kiểm thử. Kiểm thử Swing tự bỏ qua nếu máy headless; các kiểm thử còn lại vẫn chạy.

Báo cáo đã được biên soạn lại cho nhóm 3 sinh viên, có bìa, mục lục tự động, 7 chương, bảng phân công và phụ lục bảo vệ. Bản Word `docs/BAO_CAO.docx` có bảng và ảnh nhúng. Điền thông tin trường, giảng viên, họ tên/MSSV và xác nhận phân công trước khi nộp. Định dạng A4, Times New Roman 13, thân bài giãn dòng 1,5; bảng cỡ 11.

Thư mục docs giữ báo cáo, UML, ảnh minh họa và minh chứng kiểm thử. Các script xuất, xem trước và metadata đã được xóa. Có thể chỉnh sửa và xuất PDF trực tiếp từ bản Word. Tệp bắt đầu bằng `~$` là tệp khóa tạm do Word tạo, thường tự mất khi đóng tài liệu.
