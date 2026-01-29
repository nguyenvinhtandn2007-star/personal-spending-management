package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import dao.TaiKhoanDAO;
import model.TaiKhoan;

public class DangKyController {

    @FXML private TextField txtTenDangNhap;
    @FXML private TextField txtHoTen;
    @FXML private PasswordField txtMatKhau;

    TaiKhoanDAO dao = new TaiKhoanDAO();

    @FXML
    public void xuLyDangKy() {
        String user = txtTenDangNhap.getText();
        String pass = txtMatKhau.getText();
        String ten = txtHoTen.getText();

        if (user.equals("") || pass.equals("") || ten.equals("")) {
            System.out.println("NHẬP THIẾU");
            return;
        }

        TaiKhoan tk = new TaiKhoan();
        tk.setTenDangNhap(user);
        tk.setMatKhau(pass);
        tk.setHoTen(ten);

        boolean ketQua = dao.dangKy(tk);

        if (ketQua) {
            System.out.println("ĐĂNG KÝ THÀNH CÔNG");
            quayLaiDangNhap();
        } else {
            System.out.println("LỖI ĐĂNG KÝ");
        }
    }

    @FXML
    public void quayLaiDangNhap() {
        try {
            Scene sc = txtTenDangNhap.getScene();
            Stage stage = (Stage) sc.getWindow();

            Parent root = FXMLLoader.load(getClass().getResource("/DangNhap.fxml"));
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}