package dao;

import model.GiaoDich;
import ConnectSever.DBConnect;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GiaoDichDAO {

    public void themGiaoDich(GiaoDich gd) {
        String sql = "INSERT INTO GIAO_DICH " +
                "(id_tai_khoan, id_danh_muc, id_loai, so_tien, ghi_chu, thoi_gian) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = DBConnect.getConnection();
            ps = conn.prepareStatement(sql);

            ps.setInt(1, gd.getIdTaiKhoan());
            ps.setInt(2, gd.getDanhMuc().getIdDanhMuc());
            ps.setInt(3, gd.getLoai().getIdLoai());
            ps.setLong(4, gd.getSoTien());
            ps.setString(5, gd.getGhiChu());
            ps.setTimestamp(6, Timestamp.valueOf(gd.getThoiGian()));

            ps.executeUpdate();
            System.out.println(">>> Đã thêm giao dịch");

            ps.close();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<GiaoDich> getTheoTaiKhoan(int idTaiKhoan) {
        List<GiaoDich> list = new ArrayList<>();
        String sql = "SELECT gd.ID_GIAO_DICH, gd.thoi_gian, dm.ten_danh_muc, l.ten_loai, gd.so_tien, gd.ghi_chu " +
                "FROM GIAO_DICH gd " +
                "JOIN DANH_MUC dm ON gd.id_danh_muc = dm.id_danh_muc " +
                "JOIN LOAI_GIAO_DICH l ON gd.id_loai = l.id_loai " +
                "WHERE gd.id_tai_khoan = ? " +
                "ORDER BY gd.thoi_gian DESC";

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBConnect.getConnection();
            ps = conn.prepareStatement(sql);

            ps.setInt(1, idTaiKhoan);
            rs = ps.executeQuery();

            while (rs.next()) {
                GiaoDich gd = new GiaoDich();
                gd.setThoiGian(rs.getTimestamp("thoi_gian").toLocalDateTime());
                gd.setTenDanhMuc(rs.getString("ten_danh_muc"));
                gd.setTenLoai(rs.getString("ten_loai"));
                gd.setSoTien(rs.getLong("so_tien"));
                gd.setGhiChu(rs.getString("ghi_chu"));
                gd.setIdGiaoDich(rs.getInt("id_giao_dich"));

                list.add(gd);
            }

            rs.close();
            ps.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    public void xoaGiaoDich(int idGiaoDich) {
        String sql = "DELETE FROM GIAO_DICH WHERE ID_GIAO_DICH=?";

        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = DBConnect.getConnection();
            ps = conn.prepareStatement(sql);

            ps.setInt(1, idGiaoDich);
            ps.executeUpdate();

            ps.close();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}