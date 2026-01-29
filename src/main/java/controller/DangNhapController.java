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

public class DangNhapController {

    @FXML private TextField txtTenDangNhap;
    @FXML private PasswordField txtMatKhau;

    public static TaiKhoan taiKhoanDangNhap;

    TaiKhoanDAO dao = new TaiKhoanDAO();

    @FXML
    public void xuLyDangNhap() {
        String user = txtTenDangNhap.getText();
        String pass = txtMatKhau.getText();

        if (user.equals("") || pass.equals("")) {
            return;
        }

        TaiKhoan tk = dao.dangNhap(user, pass);

        if (tk != null) {
            taiKhoanDangNhap = tk;
            chuyenManHinh("/TrangChu.fxml");
        } else {
            System.out.println("SAI MẬT KHẨU");
        }
    }

    @FXML
    public void moDangKy() {
        chuyenManHinh("/DangKy.fxml");
    }

    private void chuyenManHinh(String fxml) {
        try {
            Scene sc = txtTenDangNhap.getScene();
            Stage stage = (Stage) sc.getWindow();

            Parent root = FXMLLoader.load(getClass().getResource(fxml));
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}