package service;

import dao.DanhMucDAO;
import dao.TaiKhoanDAO;
import model.DanhMuc;
import model.TaiKhoan;

import java.util.List;

public class AdminService {
    private final TaiKhoanDAO taiKhoanDAO = new TaiKhoanDAO();
    private final DanhMucDAO danhMucDAO = new DanhMucDAO();

    public List<TaiKhoan> danhSachTaiKhoan() {
        return taiKhoanDAO.getAll();
    }

    public boolean themTaiKhoan(TaiKhoan taiKhoan) {
        validateTaiKhoan(taiKhoan, true);
        return taiKhoanDAO.dangKy(taiKhoan);
    }

    public boolean capNhatTaiKhoan(TaiKhoan taiKhoan) {
        validateTaiKhoan(taiKhoan, false);
        return taiKhoanDAO.capNhat(taiKhoan);
    }

    public boolean xoaTaiKhoan(int idTaiKhoan) {
        return taiKhoanDAO.xoa(idTaiKhoan);
    }

    public boolean themDanhMuc(DanhMuc danhMuc) {
        validateDanhMuc(danhMuc);
        return danhMucDAO.them(danhMuc);
    }

    public boolean capNhatDanhMuc(DanhMuc danhMuc) {
        validateDanhMuc(danhMuc);
        return danhMucDAO.capNhat(danhMuc);
    }

    public boolean xoaDanhMuc(int idDanhMuc) {
        return danhMucDAO.xoa(idDanhMuc);
    }

    private void validateTaiKhoan(TaiKhoan taiKhoan, boolean requirePassword) {
        if (taiKhoan == null) {
            throw new IllegalArgumentException("Dữ liệu tài khoản trống.");
        }
        if (taiKhoan.getTenDangNhap() == null || taiKhoan.getTenDangNhap().isBlank()) {
            throw new IllegalArgumentException("Tên đăng nhập không được trống.");
        }
        if (taiKhoan.getHoTen() == null || taiKhoan.getHoTen().isBlank()) {
            throw new IllegalArgumentException("Họ tên không được trống.");
        }
        if (requirePassword && (taiKhoan.getMatKhau() == null || taiKhoan.getMatKhau().isBlank())) {
            throw new IllegalArgumentException("Mật khẩu không được trống.");
        }
        if (!"ADMIN".equals(taiKhoan.getVaiTro()) && !"USER".equals(taiKhoan.getVaiTro())) {
            throw new IllegalArgumentException("Vai trò chỉ được là ADMIN hoặc USER.");
        }
    }

    private void validateDanhMuc(DanhMuc danhMuc) {
        if (danhMuc == null || danhMuc.getTenDanhMuc() == null || danhMuc.getTenDanhMuc().isBlank()) {
            throw new IllegalArgumentException("Tên danh mục không được trống.");
        }
    }
}
