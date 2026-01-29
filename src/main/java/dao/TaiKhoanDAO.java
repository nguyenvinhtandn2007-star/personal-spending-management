package dao;

import model.TaiKhoan;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import ConnectSever.DBConnect;

public class TaiKhoanDAO {

    public boolean dangKy(TaiKhoan taiKhoan) {
        String sql = "INSERT INTO TAI_KHOAN (TEN_DANG_NHAP, MAT_KHAU, HO_TEN) VALUES (?, ?, ?)";

        Connection conn = null;
        PreparedStatement pst = null;

        try {
            conn = DBConnect.getConnection();
            pst = conn.prepareStatement(sql);

            pst.setString(1, taiKhoan.getTenDangNhap());
            pst.setString(2, taiKhoan.getMatKhau());
            pst.setString(3, taiKhoan.getHoTen());

            int ketQua = pst.executeUpdate();

            pst.close();
            conn.close();

            if (ketQua > 0) {
                System.out.println("Đăng ký thành công!");
                return true;
            } else {
                System.out.println("Đăng ký thất bại, vui lòng thử lại!");
                return false;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public TaiKhoan dangNhap(String tenDangNhap, String matKhau) {
        String sql = "SELECT * FROM TAI_KHOAN WHERE TEN_DANG_NHAP=? AND MAT_KHAU=?";

        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rsTaiKhoan = null;
        TaiKhoan ketQua = null;

        try {
            conn = DBConnect.getConnection();
            pst = conn.prepareStatement(sql);

            pst.setString(1, tenDangNhap);
            pst.setString(2, matKhau);

            rsTaiKhoan = pst.executeQuery();

            if (rsTaiKhoan.next()) {
                ketQua = new TaiKhoan();
                ketQua.setIdTaiKhoan(rsTaiKhoan.getInt("ID_TAI_KHOAN"));
                ketQua.setTenDangNhap(rsTaiKhoan.getString("TEN_DANG_NHAP"));
                ketQua.setHoTen(rsTaiKhoan.getString("HO_TEN"));

                System.out.println("Đăng nhập thành công: " + ketQua.getHoTen());
            } else {
                System.out.println("Sai tên đăng nhập hoặc mật khẩu!");
            }

            rsTaiKhoan.close();
            pst.close();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return ketQua;
    }
}