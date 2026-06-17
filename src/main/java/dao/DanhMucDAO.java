package dao;

import model.DanhMuc;
import ConnectSever.DBConnect;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class DanhMucDAO {
    public List<DanhMuc> getAll() {
        List<DanhMuc> list = new ArrayList<>();
        String sql = "SELECT * FROM DANH_MUC";

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = DBConnect.getConnection();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                list.add(new DanhMuc(
                        rs.getInt("ID_DANH_MUC"),
                        rs.getString("TEN_DANH_MUC")
                ));
            }
            rs.close();
            ps.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean them(DanhMuc danhMuc) {
        String sql = "INSERT INTO DANH_MUC (TEN_DANH_MUC) VALUES (?)";

        try (Connection con = DBConnect.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, danhMuc.getTenDanhMuc());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean capNhat(DanhMuc danhMuc) {
        String sql = "UPDATE DANH_MUC SET TEN_DANH_MUC=? WHERE ID_DANH_MUC=?";

        try (Connection con = DBConnect.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, danhMuc.getTenDanhMuc());
            ps.setInt(2, danhMuc.getIdDanhMuc());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean xoa(int idDanhMuc) {
        String sql = "DELETE FROM DANH_MUC WHERE ID_DANH_MUC=?";

        try (Connection con = DBConnect.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idDanhMuc);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
