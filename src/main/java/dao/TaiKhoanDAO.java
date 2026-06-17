package dao;

import ConnectSever.DBConnect;
import model.TaiKhoan;
import security.PasswordUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class TaiKhoanDAO {

    public boolean dangKy(TaiKhoan taiKhoan) {
        String sql = "INSERT INTO TAI_KHOAN (TEN_DANG_NHAP, MAT_KHAU, HO_TEN, VAI_TRO) VALUES (?, ?, ?, ?)";

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            // Password luôn được hash trước khi lưu database.
            pst.setString(1, taiKhoan.getTenDangNhap());
            pst.setString(2, PasswordUtil.hashPassword(taiKhoan.getMatKhau()));
            pst.setString(3, taiKhoan.getHoTen());
            pst.setString(4, taiKhoan.getVaiTro() == null ? "USER" : taiKhoan.getVaiTro());

            return pst.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public TaiKhoan dangNhap(String tenDangNhap, String matKhau) {
        String sql = "SELECT * FROM TAI_KHOAN WHERE TEN_DANG_NHAP=?";

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, tenDangNhap);

            try (ResultSet rs = pst.executeQuery()) {
                if (!rs.next()) {
                    return null;
                }

                String storedHash = rs.getString("MAT_KHAU");
                if (!PasswordUtil.verifyPassword(matKhau, storedHash)) {
                    return null;
                }

                TaiKhoan ketQua = new TaiKhoan();
                ketQua.setIdTaiKhoan(rs.getInt("ID_TAI_KHOAN"));
                ketQua.setTenDangNhap(rs.getString("TEN_DANG_NHAP"));
                ketQua.setHoTen(rs.getString("HO_TEN"));
                ketQua.setVaiTro(rs.getString("VAI_TRO"));
                return ketQua;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean datLaiMatKhau(String tenDangNhap, String hoTen, String matKhauMoi) {
        String sql = "UPDATE TAI_KHOAN SET MAT_KHAU=? WHERE TEN_DANG_NHAP=? AND LOWER(HO_TEN)=LOWER(?)";

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            // Mat khau moi van duoc hash truoc khi cap nhat vao database.
            pst.setString(1, PasswordUtil.hashPassword(matKhauMoi));
            pst.setString(2, tenDangNhap);
            pst.setString(3, hoTen);
            return pst.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<TaiKhoan> getAll() {
        List<TaiKhoan> list = new ArrayList<>();
        String sql = "SELECT ID_TAI_KHOAN, TEN_DANG_NHAP, HO_TEN, VAI_TRO, NGAY_TAO FROM TAI_KHOAN ORDER BY ID_TAI_KHOAN";

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                TaiKhoan tk = new TaiKhoan();
                tk.setIdTaiKhoan(rs.getInt("ID_TAI_KHOAN"));
                tk.setTenDangNhap(rs.getString("TEN_DANG_NHAP"));
                tk.setHoTen(rs.getString("HO_TEN"));
                tk.setVaiTro(rs.getString("VAI_TRO"));
                if (rs.getTimestamp("NGAY_TAO") != null) {
                    tk.setNgayTao(rs.getTimestamp("NGAY_TAO").toLocalDateTime());
                }
                list.add(tk);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean capNhat(TaiKhoan taiKhoan) {
        String sql = "UPDATE TAI_KHOAN SET HO_TEN=?, VAI_TRO=? WHERE ID_TAI_KHOAN=?";

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            // Admin chỉ sửa họ tên và vai trò; mật khẩu được đổi qua luồng riêng nếu cần mở rộng.
            pst.setString(1, taiKhoan.getHoTen());
            pst.setString(2, taiKhoan.getVaiTro());
            pst.setInt(3, taiKhoan.getIdTaiKhoan());
            return pst.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean xoa(int idTaiKhoan) {
        String deleteGiaoDich = "DELETE FROM GIAO_DICH WHERE ID_TAI_KHOAN=?";
        String deleteTaiKhoan = "DELETE FROM TAI_KHOAN WHERE ID_TAI_KHOAN=?";

        try (Connection conn = DBConnect.getConnection()) {
            conn.setAutoCommit(false);

            // Xoa tai khoan la nghiep vu nhieu buoc nen dung transaction.
            try (PreparedStatement psGiaoDich = conn.prepareStatement(deleteGiaoDich);
                 PreparedStatement psTaiKhoan = conn.prepareStatement(deleteTaiKhoan)) {
                psGiaoDich.setInt(1, idTaiKhoan);
                psGiaoDich.executeUpdate();

                psTaiKhoan.setInt(1, idTaiKhoan);
                boolean ok = psTaiKhoan.executeUpdate() > 0;
                conn.commit();
                return ok;
            } catch (Exception e) {
                conn.rollback();
                throw e;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
