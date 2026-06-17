package server;

import model.DanhMuc;
import model.GiaoDich;
import model.TaiKhoan;
import network.Action;
import network.Request;
import network.Response;
import service.AdminService;
import service.AuthService;
import service.CsvService;
import service.GiaoDichService;
import util.AppLogger;

public class RequestRouter {
    private final AuthService authService = new AuthService();
    private final AdminService adminService = new AdminService();
    private final GiaoDichService giaoDichService = new GiaoDichService();
    private final CsvService csvService = new CsvService();

    public Response handle(Request request) {
        try {
            String action = request.getAction();
            Object data = request.getData();
            AppLogger.log("client", action, "RECEIVED");

            switch (action) {
                case Action.LOGIN -> {
                    Object[] args = (Object[]) data;
                    TaiKhoan tk = authService.login((String) args[0], (String) args[1]);
                    return tk == null ? Response.fail("Sai ten dang nhap hoac mat khau.") : Response.ok("Dang nhap thanh cong.", tk);
                }
                case Action.REGISTER -> {
                    boolean ok = authService.register((TaiKhoan) data);
                    return ok ? Response.ok("Dang ky thanh cong.", null) : Response.fail("Dang ky that bai.");
                }
                case Action.RESET_PASSWORD -> {
                    Object[] args = (Object[]) data;
                    boolean ok = authService.resetPassword((String) args[0], (String) args[1], (String) args[2]);
                    return ok ? Response.ok("Dat lai mat khau thanh cong.", null) : Response.fail("Ten dang nhap hoac ho ten khong dung.");
                }
                case Action.CATEGORY_FIND_ALL -> {
                    return Response.ok("Lay danh muc thanh cong.", giaoDichService.danhMuc());
                }
                case Action.TYPE_FIND_ALL -> {
                    return Response.ok("Lay loai giao dich thanh cong.", giaoDichService.loaiGiaoDich());
                }
                case Action.TRANSACTION_FIND_BY_USER -> {
                    return Response.ok("Lay giao dich thanh cong.", giaoDichService.danhSachTheoTaiKhoan((Integer) data));
                }
                case Action.TRANSACTION_FIND_ALL -> {
                    return Response.ok("Lay toan bo giao dich thanh cong.", giaoDichService.danhSachTatCa());
                }
                case Action.TRANSACTION_CREATE -> {
                    boolean ok = giaoDichService.them((GiaoDich) data);
                    return ok ? Response.ok("Them giao dich thanh cong.", null) : Response.fail("Khong them duoc giao dich.");
                }
                case Action.TRANSACTION_UPDATE -> {
                    boolean ok = giaoDichService.capNhat((GiaoDich) data);
                    return ok ? Response.ok("Cap nhat giao dich thanh cong.", null) : Response.fail("Khong cap nhat duoc giao dich.");
                }
                case Action.TRANSACTION_DELETE -> {
                    boolean ok = giaoDichService.xoa((Integer) data);
                    return ok ? Response.ok("Xoa giao dich thanh cong.", null) : Response.fail("Khong xoa duoc giao dich.");
                }
                case Action.TRANSACTION_SEARCH -> {
                    Object[] args = (Object[]) data;
                    Integer idTaiKhoan = (Integer) args[0];
                    String keyword = (String) args[1];
                    Object result = idTaiKhoan == null
                            ? giaoDichService.timKiemTatCa(keyword)
                            : giaoDichService.timKiem(idTaiKhoan, keyword);
                    return Response.ok("Tim kiem thanh cong.", result);
                }
                case Action.REPORT_BY_DAY -> {
                    return Response.ok("Lay bao cao thanh cong.", giaoDichService.baoCaoNgay((Integer) data));
                }
                case Action.REPORT_FAMILY_BY_DAY -> {
                    return Response.ok("Lay bao cao gia dinh thanh cong.", giaoDichService.baoCaoGiaDinhTheoNgay());
                }
                case Action.EXPORT_TRANSACTIONS_CSV -> {
                    return Response.ok("Export CSV thanh cong.", csvService.exportGiaoDich((Integer) data));
                }
                case Action.IMPORT_TRANSACTIONS_CSV -> {
                    Object[] args = (Object[]) data;
                    int count = csvService.importGiaoDich((Integer) args[0], (String) args[1]);
                    return Response.ok("Import thanh cong " + count + " dong.", count);
                }
                case Action.USER_FIND_ALL -> {
                    return Response.ok("Lay danh sach tai khoan thanh cong.", adminService.danhSachTaiKhoan());
                }
                case Action.USER_CREATE -> {
                    boolean ok = adminService.themTaiKhoan((TaiKhoan) data);
                    return ok ? Response.ok("Them tai khoan thanh cong.", null) : Response.fail("Khong them duoc tai khoan.");
                }
                case Action.USER_UPDATE -> {
                    boolean ok = adminService.capNhatTaiKhoan((TaiKhoan) data);
                    return ok ? Response.ok("Cap nhat tai khoan thanh cong.", null) : Response.fail("Khong cap nhat duoc tai khoan.");
                }
                case Action.USER_DELETE -> {
                    boolean ok = adminService.xoaTaiKhoan((Integer) data);
                    return ok ? Response.ok("Xoa tai khoan thanh cong.", null) : Response.fail("Khong xoa duoc tai khoan.");
                }
                case Action.CATEGORY_CREATE -> {
                    boolean ok = adminService.themDanhMuc((DanhMuc) data);
                    return ok ? Response.ok("Them danh muc thanh cong.", null) : Response.fail("Khong them duoc danh muc.");
                }
                case Action.CATEGORY_UPDATE -> {
                    boolean ok = adminService.capNhatDanhMuc((DanhMuc) data);
                    return ok ? Response.ok("Cap nhat danh muc thanh cong.", null) : Response.fail("Khong cap nhat duoc danh muc.");
                }
                case Action.CATEGORY_DELETE -> {
                    boolean ok = adminService.xoaDanhMuc((Integer) data);
                    return ok ? Response.ok("Xoa danh muc thanh cong.", null) : Response.fail("Khong xoa duoc danh muc.");
                }
                default -> {
                    return Response.fail("Action khong hop le: " + action);
                }
            }
        } catch (Exception e) {
            AppLogger.log("client", request == null ? "NULL" : request.getAction(), "ERROR: " + e.getMessage());
            return Response.fail(e.getMessage());
        }
    }
}
