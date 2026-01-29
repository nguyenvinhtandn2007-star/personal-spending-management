package dao;

import model.LoaiGiaoDich;
import ConnectSever.DBConnect;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class LoaiGiaoDichDAO {

    public List<LoaiGiaoDich> getAll() {
        List<LoaiGiaoDich> list = new ArrayList<>();
        String sql = "SELECT * FROM LOAI_GIAO_DICH";

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = DBConnect.getConnection();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                list.add(new LoaiGiaoDich(
                        rs.getInt("ID_LOAI"),
                        rs.getString("TEN_LOAI")
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
}