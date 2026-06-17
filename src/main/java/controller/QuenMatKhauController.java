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

public class QuenMatKhauController {
    @FXML private TextField txtTenDangNhap;
    @FXML private TextField txtHoTen;
    @FXML private PasswordField txtMatKhauMoi;
    @FXML private PasswordField txtNhapLaiMatKhau;

    private final ClientService clientService = ClientService.getInstance();

    @FXML
    public void xuLyDatLaiMatKhau() {
        String username = txtTenDangNhap.getText().trim();
        String fullName = txtHoTen.getText().trim();
        String newPassword = txtMatKhauMoi.getText();
        String confirmPassword = txtNhapLaiMatKhau.getText();

        if (username.isEmpty() || fullName.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
            thongBao(Alert.AlertType.WARNING, "Vui long nhap day du thong tin.");
            return;
        }
        if (!newPassword.equals(confirmPassword)) {
            thongBao(Alert.AlertType.WARNING, "Mat khau nhap lai khong khop.");
            return;
        }
        if (newPassword.length() < 6) {
            thongBao(Alert.AlertType.WARNING, "Mat khau moi phai co it nhat 6 ky tu.");
            return;
        }

        Task<Void> task = new Task<>() {
            @Override
            protected Void call() {
                // Server kiem tra username + ho ten, sau do hash mat khau moi.
                clientService.resetPassword(username, fullName, newPassword);
                return null;
            }
        };

        task.setOnSucceeded(e -> {
            thongBao(Alert.AlertType.INFORMATION, "Dat lai mat khau thanh cong. Hay dang nhap bang mat khau moi.");
            quayLaiDangNhap();
        });
        task.setOnFailed(e -> thongBao(Alert.AlertType.ERROR, task.getException().getMessage()));
        new Thread(task).start();
    }

    @FXML
    public void quayLaiDangNhap() {
        try {
            Stage stage = (Stage) txtTenDangNhap.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("/DangNhap.fxml"));
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
