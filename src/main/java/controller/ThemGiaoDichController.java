package controller;

import client.ClientService;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.DanhMuc;
import model.GiaoDich;
import model.LoaiGiaoDich;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public class ThemGiaoDichController {

    @FXML TextField tfTien;
    @FXML TextArea taGhiChu;
    @FXML DatePicker dp;
    @FXML ComboBox<DanhMuc> cbDM;
    @FXML ComboBox<LoaiGiaoDich> cbLoai;
    @FXML Spinner<Integer> spGio;
    @FXML Spinner<Integer> spPhut;

    private final ClientService clientService = ClientService.getInstance();

    @FXML
    public void initialize() {
        Task<Object[]> task = new Task<>() {
            @Override
            protected Object[] call() {
                // Lấy danh mục/loại giao dịch từ server thay vì query database ở client.
                return new Object[]{clientService.getDanhMuc(), clientService.getLoaiGiaoDich()};
            }
        };
        task.setOnSucceeded(e -> {
            cbDM.getItems().setAll((List<DanhMuc>) task.getValue()[0]);
            cbLoai.getItems().setAll((List<LoaiGiaoDich>) task.getValue()[1]);
        });
        task.setOnFailed(e -> thongBao(Alert.AlertType.ERROR, task.getException().getMessage()));
        new Thread(task).start();

        dp.setValue(LocalDate.now());
        LocalTime time = LocalTime.now();
        spGio.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23, time.getHour()));
        spPhut.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59, time.getMinute()));
    }

    @FXML
    public void xuLyThemGiaoDich() {
        try {
            GiaoDich gd = taoGiaoDichTuForm();
            Task<Void> task = new Task<>() {
                @Override
                protected Void call() {
                    clientService.themGiaoDich(gd);
                    return null;
                }
            };
            task.setOnSucceeded(e -> {
                thongBao(Alert.AlertType.INFORMATION, "Thêm giao dịch thành công.");
                quayLaiTrangChu();
            });
            task.setOnFailed(e -> thongBao(Alert.AlertType.ERROR, task.getException().getMessage()));
            new Thread(task).start();
        } catch (Exception e) {
            thongBao(Alert.AlertType.WARNING, e.getMessage());
        }
    }

    private GiaoDich taoGiaoDichTuForm() {
        String tienText = tfTien.getText().trim();
        DanhMuc dm = cbDM.getValue();
        LoaiGiaoDich loai = cbLoai.getValue();
        LocalDate ngay = dp.getValue();

        if (tienText.isEmpty() || dm == null || loai == null || ngay == null) {
            throw new IllegalArgumentException("Vui lòng nhập đầy đủ thông tin.");
        }

        long tien = Long.parseLong(tienText);
        if (tien <= 0) {
            throw new IllegalArgumentException("Số tiền phải lớn hơn 0.");
        }

        int idTaiKhoan = DangNhapController.taiKhoanDangNhap.getIdTaiKhoan();
        LocalDateTime thoiGian = LocalDateTime.of(ngay, LocalTime.of(spGio.getValue(), spPhut.getValue()));

        return new GiaoDich(idTaiKhoan, dm, loai, tien, taGhiChu.getText(), thoiGian);
    }

    @FXML
    public void quayLaiTrangChu() {
        try {
            Scene sc = tfTien.getScene();
            Stage stage = (Stage) sc.getWindow();

            Parent root = FXMLLoader.load(getClass().getResource("/TrangChu.fxml"));
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
