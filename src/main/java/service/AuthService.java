package service;

import dao.TaiKhoanDAO;
import model.TaiKhoan;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AuthService {
    private final TaiKhoanDAO taiKhoanDAO = new TaiKhoanDAO();
    private final Map<String, Integer> failedAttempts = new ConcurrentHashMap<>();

    public TaiKhoan login(String username, String password) {
        // Khoá mềm sau 5 lần sai để đáp ứng bảo mật cơ bản.
        if (failedAttempts.getOrDefault(username, 0) >= 5) {
            throw new IllegalStateException("Tài khoản bị khoá tạm thời do nhập sai quá nhiều lần.");
        }

        TaiKhoan taiKhoan = taiKhoanDAO.dangNhap(username, password);
        if (taiKhoan == null) {
            failedAttempts.merge(username, 1, Integer::sum);
            return null;
        }

        failedAttempts.remove(username);
        return taiKhoan;
    }

    public boolean register(TaiKhoan taiKhoan) {
        if (taiKhoan.getVaiTro() == null || taiKhoan.getVaiTro().isBlank()) {
            taiKhoan.setVaiTro("USER");
        }
        return taiKhoanDAO.dangKy(taiKhoan);
    }

    public boolean resetPassword(String username, String fullName, String newPassword) {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Ten dang nhap khong duoc trong.");
        }
        if (fullName == null || fullName.isBlank()) {
            throw new IllegalArgumentException("Ho ten khong duoc trong.");
        }
        if (newPassword == null || newPassword.length() < 6) {
            throw new IllegalArgumentException("Mat khau moi phai co it nhat 6 ky tu.");
        }

        boolean ok = taiKhoanDAO.datLaiMatKhau(username.trim(), fullName.trim(), newPassword);
        if (ok) {
            failedAttempts.remove(username.trim());
        }
        return ok;
    }
}
