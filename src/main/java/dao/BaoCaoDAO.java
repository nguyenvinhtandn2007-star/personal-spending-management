package dao;

import model.BaoCaoNgay;
import ConnectSever.DBConnect;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class BaoCaoDAO {

    public List<BaoCaoNgay> getBaoCaoTheoNgay(int idTaiKhoan) {
        List<BaoCaoNgay> list = new ArrayList<>();
        String sql = "SELECT CAST(thoi_gian AS DATE) AS ngay, "
                + "SUM(CASE WHEN id_loai = 2 THEN so_tien ELSE 0 END) AS tong_thu, "
                + "SUM(CASE WHEN id_loai = 1 THEN so_tien ELSE 0 END) AS tong_chi "
                + "FROM GIAO_DICH "
                + "WHERE id_tai_khoan = ? "
                + "GROUP BY CAST(thoi_gian AS DATE) "
                + "ORDER BY ngay DESC";

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DBConnect.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, idTaiKhoan);
            rs = ps.executeQuery();

            while (rs.next()) {
                String ngay = rs.getDate("ngay").toString();
                long thu = rs.getLong("tong_thu");
                long chi = rs.getLong("tong_chi");

                list.add(new BaoCaoNgay(ngay, thu, chi));
            }

            rs.close();
            ps.close();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public List<BaoCaoNgay> getBaoCaoGiaDinhTheoNgay() {
        List<BaoCaoNgay> list = new ArrayList<>();
        String sql = "SELECT CAST(thoi_gian AS DATE) AS ngay, "
                + "SUM(CASE WHEN id_loai = 2 THEN so_tien ELSE 0 END) AS tong_thu, "
                + "SUM(CASE WHEN id_loai = 1 THEN so_tien ELSE 0 END) AS tong_chi "
                + "FROM GIAO_DICH "
                + "GROUP BY CAST(thoi_gian AS DATE) "
                + "ORDER BY ngay DESC";

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String ngay = rs.getDate("ngay").toString();
                long thu = rs.getLong("tong_thu");
                long chi = rs.getLong("tong_chi");
                list.add(new BaoCaoNgay(ngay, thu, chi));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public long tinhTongThu(List<BaoCaoNgay> list) {
        long tong = 0;
        for (int i = 0; i < list.size(); i++) {
            BaoCaoNgay b = list.get(i);
            tong += b.getTongThu();
        }
        return tong;
    }

    public long tinhTongChi(List<BaoCaoNgay> list) {
        long tong = 0;
        for (int i = 0; i < list.size(); i++) {
            BaoCaoNgay b = list.get(i);
            tong += b.getTongChi();
        }
        return tong;
    }
}
