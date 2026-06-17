package dao;

import ConnectSever.DBConnect;
import model.GiaoDich;
import security.AESUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class GiaoDichDAO {

    public boolean themGiaoDich(GiaoDich gd) {
        String sql = "INSERT INTO GIAO_DICH (ID_TAI_KHOAN, ID_DANH_MUC, ID_LOAI, SO_TIEN, GHI_CHU, THOI_GIAN) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            // DAO chỉ làm việc với database, không biết gì về giao diện client.
            ps.setInt(1, gd.getIdTaiKhoan());
            ps.setInt(2, gd.getDanhMuc().getIdDanhMuc());
            ps.setInt(3, gd.getLoai().getIdLoai());
            ps.setLong(4, gd.getSoTien());
            ps.setString(5, AESUtil.encrypt(gd.getGhiChu()));
            ps.setTimestamp(6, Timestamp.valueOf(gd.getThoiGian()));

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean capNhatGiaoDich(GiaoDich gd) {
        String sql = "UPDATE GIAO_DICH SET ID_DANH_MUC=?, ID_LOAI=?, SO_TIEN=?, GHI_CHU=?, THOI_GIAN=? WHERE ID_GIAO_DICH=? AND ID_TAI_KHOAN=?";

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, gd.getDanhMuc().getIdDanhMuc());
            ps.setInt(2, gd.getLoai().getIdLoai());
            ps.setLong(3, gd.getSoTien());
            ps.setString(4, AESUtil.encrypt(gd.getGhiChu()));
            ps.setTimestamp(5, Timestamp.valueOf(gd.getThoiGian()));
            ps.setInt(6, gd.getIdGiaoDich());
            ps.setInt(7, gd.getIdTaiKhoan());

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<GiaoDich> getTheoTaiKhoan(int idTaiKhoan) {
        String sql = "SELECT gd.ID_GIAO_DICH, gd.ID_TAI_KHOAN, tk.TEN_DANG_NHAP, tk.HO_TEN, gd.THOI_GIAN, gd.SO_TIEN, gd.GHI_CHU, "
                + "dm.ID_DANH_MUC, dm.TEN_DANH_MUC, l.ID_LOAI, l.TEN_LOAI "
                + "FROM GIAO_DICH gd "
                + "JOIN TAI_KHOAN tk ON gd.ID_TAI_KHOAN = tk.ID_TAI_KHOAN "
                + "JOIN DANH_MUC dm ON gd.ID_DANH_MUC = dm.ID_DANH_MUC "
                + "JOIN LOAI_GIAO_DICH l ON gd.ID_LOAI = l.ID_LOAI "
                + "WHERE gd.ID_TAI_KHOAN = ? "
                + "ORDER BY gd.THOI_GIAN DESC";
        return query(sql, idTaiKhoan);
    }

    public List<GiaoDich> getAll() {
        String sql = "SELECT gd.ID_GIAO_DICH, gd.ID_TAI_KHOAN, tk.TEN_DANG_NHAP, tk.HO_TEN, gd.THOI_GIAN, gd.SO_TIEN, gd.GHI_CHU, "
                + "dm.ID_DANH_MUC, dm.TEN_DANH_MUC, l.ID_LOAI, l.TEN_LOAI "
                + "FROM GIAO_DICH gd "
                + "JOIN TAI_KHOAN tk ON gd.ID_TAI_KHOAN = tk.ID_TAI_KHOAN "
                + "JOIN DANH_MUC dm ON gd.ID_DANH_MUC = dm.ID_DANH_MUC "
                + "JOIN LOAI_GIAO_DICH l ON gd.ID_LOAI = l.ID_LOAI "
                + "ORDER BY gd.THOI_GIAN DESC";
        return queryNoParam(sql);
    }

    public List<GiaoDich> timKiem(int idTaiKhoan, String keyword) {
        String sql = "SELECT gd.ID_GIAO_DICH, gd.ID_TAI_KHOAN, tk.TEN_DANG_NHAP, tk.HO_TEN, gd.THOI_GIAN, gd.SO_TIEN, gd.GHI_CHU, "
                + "dm.ID_DANH_MUC, dm.TEN_DANH_MUC, l.ID_LOAI, l.TEN_LOAI "
                + "FROM GIAO_DICH gd "
                + "JOIN TAI_KHOAN tk ON gd.ID_TAI_KHOAN = tk.ID_TAI_KHOAN "
                + "JOIN DANH_MUC dm ON gd.ID_DANH_MUC = dm.ID_DANH_MUC "
                + "JOIN LOAI_GIAO_DICH l ON gd.ID_LOAI = l.ID_LOAI "
                + "WHERE gd.ID_TAI_KHOAN = ? AND (dm.TEN_DANH_MUC LIKE ? OR l.TEN_LOAI LIKE ? OR gd.GHI_CHU LIKE ?) "
                + "ORDER BY gd.THOI_GIAN DESC";

        List<GiaoDich> list = new ArrayList<>();
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            String kw = "%" + keyword + "%";
            ps.setInt(1, idTaiKhoan);
            ps.setString(2, kw);
            ps.setString(3, kw);
            ps.setString(4, kw);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<GiaoDich> timKiemTatCa(String keyword) {
        String sql = "SELECT gd.ID_GIAO_DICH, gd.ID_TAI_KHOAN, tk.TEN_DANG_NHAP, tk.HO_TEN, gd.THOI_GIAN, gd.SO_TIEN, gd.GHI_CHU, "
                + "dm.ID_DANH_MUC, dm.TEN_DANH_MUC, l.ID_LOAI, l.TEN_LOAI "
                + "FROM GIAO_DICH gd "
                + "JOIN TAI_KHOAN tk ON gd.ID_TAI_KHOAN = tk.ID_TAI_KHOAN "
                + "JOIN DANH_MUC dm ON gd.ID_DANH_MUC = dm.ID_DANH_MUC "
                + "JOIN LOAI_GIAO_DICH l ON gd.ID_LOAI = l.ID_LOAI "
                + "WHERE dm.TEN_DANH_MUC LIKE ? OR l.TEN_LOAI LIKE ? OR gd.GHI_CHU LIKE ? "
                + "ORDER BY gd.THOI_GIAN DESC";

        List<GiaoDich> list = new ArrayList<>();
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            String kw = "%" + keyword + "%";
            ps.setString(1, kw);
            ps.setString(2, kw);
            ps.setString(3, kw);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean xoaGiaoDich(int idGiaoDich) {
        String sql = "DELETE FROM GIAO_DICH WHERE ID_GIAO_DICH=?";

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idGiaoDich);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private List<GiaoDich> query(String sql, int idTaiKhoan) {
        List<GiaoDich> list = new ArrayList<>();
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idTaiKhoan);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    private List<GiaoDich> queryNoParam(String sql) {
        List<GiaoDich> list = new ArrayList<>();
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    private GiaoDich mapRow(ResultSet rs) throws Exception {
        GiaoDich gd = new GiaoDich();
        gd.setIdGiaoDich(rs.getInt("ID_GIAO_DICH"));
        gd.setIdTaiKhoan(rs.getInt("ID_TAI_KHOAN"));
        gd.setTenDangNhap(rs.getString("TEN_DANG_NHAP"));
        gd.setHoTenTaiKhoan(rs.getString("HO_TEN"));
        gd.setThoiGian(rs.getTimestamp("THOI_GIAN").toLocalDateTime());
        gd.setSoTien(rs.getLong("SO_TIEN"));
        gd.setGhiChu(AESUtil.decrypt(rs.getString("GHI_CHU")));
        gd.setTenDanhMuc(rs.getString("TEN_DANH_MUC"));
        gd.setTenLoai(rs.getString("TEN_LOAI"));
        gd.setDanhMuc(new model.DanhMuc(rs.getInt("ID_DANH_MUC"), rs.getString("TEN_DANH_MUC")));
        gd.setLoai(new model.LoaiGiaoDich(rs.getInt("ID_LOAI"), rs.getString("TEN_LOAI")));
        return gd;
    }
}
