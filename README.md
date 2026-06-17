# QLLL_CHI_TIEU_CA_NHAN

Ứng dụng JavaFX quản lý thu chi cá nhân, đã tách theo mô hình client/server TCP/IP.

## Kiến trúc

- `client`: client service gửi `Request` đến server qua Socket TCP/IP.
- `controller`: màn hình JavaFX, chỉ gọi `ClientService`, không kết nối database.
- `server`: `ServerMain`, `ClientHandler`, `RequestRouter`, xử lý nhiều client bằng `ExecutorService`.
- `service`: xử lý nghiệp vụ, validate dữ liệu, đăng nhập, import/export.
- `dao`: JDBC DAO làm việc với SQL Server.
- `model`: DTO/model gửi qua socket.
- `security`: hash password bằng PBKDF2, mã hoá ghi chú bằng AES.
- `network`: `Request`, `Response`, danh sách action.
- `config`: đọc `app.properties`.

## Cấu hình

Sửa file `src/main/resources/app.properties` nếu máy SQL Server khác:

```properties
server.host=localhost
server.port=9000
database.url=jdbc:sqlserver://ASUSF15;instanceName=SQLEXPRESS;databaseName=QLLL_CHI_TIEU_CA_NHAN;encrypt=true;trustServerCertificate=true;
database.user=sa
database.password=Demo@123
```

## Tạo database

Chạy file:

```text
C:\Users\ASUS\OneDrive\Desktop\vku2\QLLL_CHI_TIEU_CA_NHAN.sql
```

Tài khoản mẫu:

- Admin: `admin` / `admin123`
- User: `user` / `user123`

Password trong database là hash PBKDF2, không lưu plain text.

## Chạy chương trình

Cần JDK 21. Nếu dùng IntelliJ, có thể dùng JDK đi kèm IntelliJ.

Chạy server trước:

```powershell
$env:JAVA_HOME='C:\Program Files\JetBrains\IntelliJ IDEA Community Edition 2025.2.1\jbr'
.\mvnw.cmd exec:java -Dexec.mainClass=server.ServerMain
```

Mở terminal khác, chạy client:

```powershell
$env:JAVA_HOME='C:\Program Files\JetBrains\IntelliJ IDEA Community Edition 2025.2.1\jbr'
.\mvnw.cmd javafx:run
```

## Chức năng đã có

- Đăng nhập, đăng ký tài khoản.
- Phân quyền lưu trong database: `ADMIN`, `USER`.
- Client/server TCP/IP bằng `Socket`, `ObjectInputStream`, `ObjectOutputStream`.
- Server xử lý nhiều client bằng ThreadPool.
- CRUD giao dịch: thêm, xem danh sách, xoá; lớp DAO có sẵn hàm cập nhật để mở rộng màn hình sửa.
- Tìm kiếm giao dịch theo danh mục, loại, ghi chú.
- Dashboard/báo cáo tổng thu, tổng chi, số dư theo ngày.
- Import/export CSV giao dịch.
- Hash password bằng PBKDF2.
- Mã hoá ghi chú giao dịch bằng AES trước khi lưu database.
- Ghi log server vào `server.log`.
- Cấu hình server/database trong `app.properties`.

## Kiểm tra nhanh

```powershell
$env:JAVA_HOME='C:\Program Files\JetBrains\IntelliJ IDEA Community Edition 2025.2.1\jbr'
.\mvnw.cmd -DskipTests compile
```
