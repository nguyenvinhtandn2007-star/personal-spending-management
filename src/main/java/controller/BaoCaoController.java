package controller;

import client.ClientService;
import dao.BaoCaoDAO;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import model.BaoCaoNgay;

import java.util.List;

public class BaoCaoController {

    @FXML Label lblTieuDe;
    @FXML Label lbThu;
    @FXML Label lbChi;
    @FXML Label lbDu;
    @FXML VBox vbList;
    @FXML VBox nutLichSu;
    @FXML VBox nutBaoCao;

    private final ClientService clientService = ClientService.getInstance();
    private final BaoCaoDAO tinhToanBaoCao = new BaoCaoDAO();

    @FXML
    public void initialize() {
        lblTieuDe.setText(isAdmin() ? "BAO CAO THU CHI GIA DINH" : "BAO CAO THU CHI CA NHAN");

        Task<List<BaoCaoNgay>> task = new Task<>() {
            @Override
            protected List<BaoCaoNgay> call() {
                // Admin xem tong thu chi ca gia dinh, User chi xem bao cao cua minh.
                if (isAdmin()) {
                    return clientService.getBaoCaoGiaDinh();
                }
                return clientService.getBaoCaoNgay(DangNhapController.taiKhoanDangNhap.getIdTaiKhoan());
            }
        };

        task.setOnSucceeded(e -> hienThiBaoCao(task.getValue()));
        task.setOnFailed(e -> thongBao(Alert.AlertType.ERROR, task.getException().getMessage()));
        new Thread(task).start();

        nutLichSu.setOnMouseClicked(e -> quayLaiTrangChu(e.getSource()));
    }

    private void hienThiBaoCao(List<BaoCaoNgay> ls) {
        long t = tinhToanBaoCao.tinhTongThu(ls);
        long c = tinhToanBaoCao.tinhTongChi(ls);
        long sd = t - c;

        lbThu.setText(formatTien(t));
        lbChi.setText(formatTien(c));
        lbDu.setText(formatTien(sd));

        vbList.getChildren().clear();
        for (BaoCaoNgay bc : ls) {
            HBox row = new HBox(15);
            Label lbDate = new Label(bc.getNgay());

            Label thu = new Label("+" + formatTien(bc.getTongThu()));
            thu.setTextFill(Color.GREEN);

            Label chi = new Label("-" + formatTien(bc.getTongChi()));
            chi.setTextFill(Color.RED);

            row.getChildren().addAll(lbDate, thu, chi);
            vbList.getChildren().add(row);
        }
    }

    private void quayLaiTrangChu(Object sourceObject) {
        try {
            Node source = (Node) sourceObject;
            Stage stage = (Stage) source.getScene().getWindow();

            Parent root = FXMLLoader.load(getClass().getResource("/TrangChu.fxml"));
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            thongBao(Alert.AlertType.ERROR, e.getMessage());
        }
    }

    private boolean isAdmin() {
        return DangNhapController.taiKhoanDangNhap != null
                && "ADMIN".equalsIgnoreCase(DangNhapController.taiKhoanDangNhap.getVaiTro());
    }

    private String formatTien(long value) {
        return String.format("%,d", value).replace(',', '.') + " d";
    }

    private void thongBao(Alert.AlertType type, String message) {
        Alert alert = new Alert(type);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
