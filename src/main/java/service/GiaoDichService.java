package service;

import dao.BaoCaoDAO;
import dao.DanhMucDAO;
import dao.GiaoDichDAO;
import dao.LoaiGiaoDichDAO;
import model.BaoCaoNgay;
import model.DanhMuc;
import model.GiaoDich;
import model.LoaiGiaoDich;

import java.util.List;

public class GiaoDichService {
    private final GiaoDichDAO giaoDichDAO = new GiaoDichDAO();
    private final DanhMucDAO danhMucDAO = new DanhMucDAO();
    private final LoaiGiaoDichDAO loaiGiaoDichDAO = new LoaiGiaoDichDAO();
    private final BaoCaoDAO baoCaoDAO = new BaoCaoDAO();

    public boolean them(GiaoDich giaoDich) {
        validate(giaoDich);
        return giaoDichDAO.themGiaoDich(giaoDich);
    }

    public boolean capNhat(GiaoDich giaoDich) {
        validate(giaoDich);
        return giaoDichDAO.capNhatGiaoDich(giaoDich);
    }

    public boolean xoa(int idGiaoDich) {
        return giaoDichDAO.xoaGiaoDich(idGiaoDich);
    }

    public List<GiaoDich> danhSachTheoTaiKhoan(int idTaiKhoan) {
        return giaoDichDAO.getTheoTaiKhoan(idTaiKhoan);
    }

    public List<GiaoDich> danhSachTatCa() {
        return giaoDichDAO.getAll();
    }

    public List<GiaoDich> timKiem(int idTaiKhoan, String keyword) {
        return giaoDichDAO.timKiem(idTaiKhoan, keyword == null ? "" : keyword.trim());
    }

    public List<GiaoDich> timKiemTatCa(String keyword) {
        return giaoDichDAO.timKiemTatCa(keyword == null ? "" : keyword.trim());
    }

    public List<DanhMuc> danhMuc() {
        return danhMucDAO.getAll();
    }

    public List<LoaiGiaoDich> loaiGiaoDich() {
        return loaiGiaoDichDAO.getAll();
    }

    public List<BaoCaoNgay> baoCaoNgay(int idTaiKhoan) {
        return baoCaoDAO.getBaoCaoTheoNgay(idTaiKhoan);
    }

    public List<BaoCaoNgay> baoCaoGiaDinhTheoNgay() {
        return baoCaoDAO.getBaoCaoGiaDinhTheoNgay();
    }

    private void validate(GiaoDich giaoDich) {
        // Business layer kiểm tra nghiệp vụ trước khi gọi DAO.
        if (giaoDich == null) {
            throw new IllegalArgumentException("Dữ liệu giao dịch trống.");
        }
        if (giaoDich.getDanhMuc() == null || giaoDich.getLoai() == null) {
            throw new IllegalArgumentException("Vui lòng chọn danh mục và loại giao dịch.");
        }
        if (giaoDich.getSoTien() <= 0) {
            throw new IllegalArgumentException("Số tiền phải lớn hơn 0.");
        }
        if (giaoDich.getThoiGian() == null) {
            throw new IllegalArgumentException("Vui lòng chọn thời gian giao dịch.");
        }
    }
}
