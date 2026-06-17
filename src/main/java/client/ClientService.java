package client;

import config.AppConfig;
import model.BaoCaoNgay;
import model.DanhMuc;
import model.GiaoDich;
import model.LoaiGiaoDich;
import model.TaiKhoan;
import network.Action;
import network.Request;
import network.Response;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

public class ClientService {
    private static final ClientService INSTANCE = new ClientService();

    private final String host = AppConfig.get("server.host", "localhost");
    private final int port = AppConfig.getInt("server.port", 9000);

    public static ClientService getInstance() {
        return INSTANCE;
    }

    public Response send(Request request) {
        try (Socket socket = new Socket(host, port);
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

            // Client chi gui request TCP/IP, khong ket noi truc tiep database.
            out.writeObject(request);
            out.flush();
            return (Response) in.readObject();
        } catch (Exception e) {
            return Response.fail("Khong ket noi duoc server: " + e.getMessage());
        }
    }

    public TaiKhoan login(String username, String password) {
        return (TaiKhoan) require(Action.LOGIN, new Object[]{username, password});
    }

    public void register(TaiKhoan taiKhoan) {
        require(Action.REGISTER, taiKhoan);
    }

    public void resetPassword(String username, String fullName, String newPassword) {
        require(Action.RESET_PASSWORD, new Object[]{username, fullName, newPassword});
    }

    @SuppressWarnings("unchecked")
    public List<DanhMuc> getDanhMuc() {
        return (List<DanhMuc>) require(Action.CATEGORY_FIND_ALL, null);
    }

    @SuppressWarnings("unchecked")
    public List<LoaiGiaoDich> getLoaiGiaoDich() {
        return (List<LoaiGiaoDich>) require(Action.TYPE_FIND_ALL, null);
    }

    @SuppressWarnings("unchecked")
    public List<GiaoDich> getGiaoDich(int idTaiKhoan) {
        return (List<GiaoDich>) require(Action.TRANSACTION_FIND_BY_USER, idTaiKhoan);
    }

    @SuppressWarnings("unchecked")
    public List<GiaoDich> getTatCaGiaoDich() {
        return (List<GiaoDich>) require(Action.TRANSACTION_FIND_ALL, null);
    }

    public void themGiaoDich(GiaoDich giaoDich) {
        require(Action.TRANSACTION_CREATE, giaoDich);
    }

    public void xoaGiaoDich(int idGiaoDich) {
        require(Action.TRANSACTION_DELETE, idGiaoDich);
    }

    @SuppressWarnings("unchecked")
    public List<GiaoDich> timKiemGiaoDich(Integer idTaiKhoan, String keyword) {
        return (List<GiaoDich>) require(Action.TRANSACTION_SEARCH, new Object[]{idTaiKhoan, keyword});
    }

    @SuppressWarnings("unchecked")
    public List<BaoCaoNgay> getBaoCaoNgay(int idTaiKhoan) {
        return (List<BaoCaoNgay>) require(Action.REPORT_BY_DAY, idTaiKhoan);
    }

    @SuppressWarnings("unchecked")
    public List<BaoCaoNgay> getBaoCaoGiaDinh() {
        return (List<BaoCaoNgay>) require(Action.REPORT_FAMILY_BY_DAY, null);
    }

    public String exportCsv(int idTaiKhoan) {
        return (String) require(Action.EXPORT_TRANSACTIONS_CSV, idTaiKhoan);
    }

    public int importCsv(int idTaiKhoan, String content) {
        return (Integer) require(Action.IMPORT_TRANSACTIONS_CSV, new Object[]{idTaiKhoan, content});
    }

    @SuppressWarnings("unchecked")
    public List<TaiKhoan> getTaiKhoan() {
        return (List<TaiKhoan>) require(Action.USER_FIND_ALL, null);
    }

    public void themTaiKhoan(TaiKhoan taiKhoan) {
        require(Action.USER_CREATE, taiKhoan);
    }

    public void capNhatTaiKhoan(TaiKhoan taiKhoan) {
        require(Action.USER_UPDATE, taiKhoan);
    }

    public void xoaTaiKhoan(int idTaiKhoan) {
        require(Action.USER_DELETE, idTaiKhoan);
    }

    public void themDanhMuc(DanhMuc danhMuc) {
        require(Action.CATEGORY_CREATE, danhMuc);
    }

    public void capNhatDanhMuc(DanhMuc danhMuc) {
        require(Action.CATEGORY_UPDATE, danhMuc);
    }

    public void xoaDanhMuc(int idDanhMuc) {
        require(Action.CATEGORY_DELETE, idDanhMuc);
    }

    private Object require(String action, Object data) {
        Response response = send(new Request(action, data));
        if (!response.isSuccess()) {
            throw new IllegalStateException(response.getMessage());
        }
        return response.getData();
    }
}
