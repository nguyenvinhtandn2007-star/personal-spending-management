package controller;

import client.ClientService;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.TaiKhoan;

public class DangNhapController {

    @FXML private TextField txtTenDangNhap;
    @FXML private PasswordField txtMatKhau;

    public static TaiKhoan taiKhoanDangNhap;

    private final ClientService clientService = ClientService.getInstance();

    @FXML
    public void xuLyDangNhap() {
        String user = txtTenDangNhap.getText().trim();
        String pass = txtMatKhau.getText();

        if (user.isEmpty() || pass.isEmpty()) {
            thongBao(Alert.AlertType.WARNING, "Vui lòng nhập tên đăng nhập và mật khẩu.");
            return;
        }

        Task<TaiKhoan> task = new Task<>() {
            @Override
            protected TaiKhoan call() {
                // Gọi server ở thread nền để giao diện JavaFX không bị treo.
                return clientService.login(user, pass);
            }
        };

        task.setOnSucceeded(e -> {
            taiKhoanDangNhap = task.getValue();
            chuyenManHinh("/TrangChu.fxml");
        });
        task.setOnFailed(e -> thongBao(Alert.AlertType.ERROR, task.getException().getMessage()));
        new Thread(task).start();
    }

    @FXML
    public void moDangKy() {
        chuyenManHinh("/DangKy.fxml");
    }

    @FXML
    public void moQuenMatKhau() {
        chuyenManHinh("/QuenMatKhau.fxml");
    }

    private void chuyenManHinh(String fxml) {
        try {
            Scene sc = txtTenDangNhap.getScene();
            Stage stage = (Stage) sc.getWindow();

            Parent root = FXMLLoader.load(getClass().getResource(fxml));
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            thongBao(Alert.AlertType.ERROR, e.getMessage());
        }
    }

    private void thongBao(Alert.AlertType type, String message) {
        Alert alert = new Alert(type);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
