package controller;

import client.ClientService;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.GiaoDich;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;
import java.util.Optional;

public class TrangChuController {

    @FXML private VBox vboxDanhSach;
    @FXML private Button btnThem;
    @FXML private Button btnQuanTri;
    @FXML private VBox btnLichSu;
    @FXML private VBox btnBaoCao;
    @FXML private Label lblXinChao;
    @FXML private TextField txtTimKiem;

    private final ClientService clientService = ClientService.getInstance();

    @FXML
    public void initialize() {
        if (DangNhapController.taiKhoanDangNhap != null) {
            String ten = DangNhapController.taiKhoanDangNhap.getHoTen();
            String role = DangNhapController.taiKhoanDangNhap.getVaiTro();
            lblXinChao.setText("Xin chao, " + ten + " (" + role + ")");
            btnQuanTri.setVisible(isAdmin());
            btnQuanTri.setManaged(isAdmin());
        }

        btnThem.setOnAction(e -> moThemGiaoDich());
        btnBaoCao.setOnMouseClicked(e -> moBaoCao());
        btnLichSu.setOnMouseClicked(e -> loadDuLieu());

        loadDuLieu();
    }

    @FXML
    public void xuLyTimKiem() {
        String keyword = txtTimKiem.getText();
        Task<List<GiaoDich>> task = new Task<>() {
            @Override
            protected List<GiaoDich> call() {
                Integer id = isAdmin() ? null : DangNhapController.taiKhoanDangNhap.getIdTaiKhoan();
                return clientService.timKiemGiaoDich(id, keyword);
            }
        };

        task.setOnSucceeded(e -> hienThiDanhSach(task.getValue()));
        task.setOnFailed(e -> thongBao(Alert.AlertType.ERROR, task.getException().getMessage()));
        new Thread(task).start();
    }

    @FXML
    public void exportCsv() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Luu file CSV");
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV file", "*.csv"));
        File file = chooser.showSaveDialog(btnThem.getScene().getWindow());
        if (file == null) {
            return;
        }

        Task<String> task = new Task<>() {
            @Override
            protected String call() {
                int id = DangNhapController.taiKhoanDangNhap.getIdTaiKhoan();
                return clientService.exportCsv(id);
            }
        };
        task.setOnSucceeded(e -> {
            try {
                Files.writeString(file.toPath(), task.getValue(), StandardCharsets.UTF_8);
                thongBao(Alert.AlertType.INFORMATION, "Export CSV thanh cong.");
            } catch (Exception ex) {
                thongBao(Alert.AlertType.ERROR, ex.getMessage());
            }
        });
        task.setOnFailed(e -> thongBao(Alert.AlertType.ERROR, task.getException().getMessage()));
        new Thread(task).start();
    }

    @FXML
    public void importCsv() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Chon file CSV");
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV file", "*.csv"));
        File file = chooser.showOpenDialog(btnThem.getScene().getWindow());
        if (file == null) {
            return;
        }

        Task<Integer> task = new Task<>() {
            @Override
            protected Integer call() throws Exception {
                int id = DangNhapController.taiKhoanDangNhap.getIdTaiKhoan();
                String content = Files.readString(file.toPath(), StandardCharsets.UTF_8);
                return clientService.importCsv(id, content);
            }
        };
        task.setOnSucceeded(e -> {
            thongBao(Alert.AlertType.INFORMATION, "Import thanh cong " + task.getValue() + " dong.");
            loadDuLieu();
        });
        task.setOnFailed(e -> thongBao(Alert.AlertType.ERROR, task.getException().getMessage()));
        new Thread(task).start();
    }

    @FXML
    public void moQuanTri() {
        chuyenManHinh("/Admin.fxml");
    }

    private void loadDuLieu() {
        Task<List<GiaoDich>> task = new Task<>() {
            @Override
            protected List<GiaoDich> call() {
                if (isAdmin()) {
                    return clientService.getTatCaGiaoDich();
                }
                int id = DangNhapController.taiKhoanDangNhap.getIdTaiKhoan();
                return clientService.getGiaoDich(id);
            }
        };

        task.setOnSucceeded(e -> hienThiDanhSach(task.getValue()));
        task.setOnFailed(e -> thongBao(Alert.AlertType.ERROR, task.getException().getMessage()));
        new Thread(task).start();
    }

    private void hienThiDanhSach(List<GiaoDich> list) {
        vboxDanhSach.getChildren().clear();
        for (GiaoDich gd : list) {
            vboxDanhSach.getChildren().add(taoGiaoDichItem(gd));
        }
    }

    private HBox taoGiaoDichItem(GiaoDich gd) {
        HBox item = new HBox(10);
        item.setStyle("-fx-border-color: gray; -fx-border-width: 1px; -fx-padding: 10px;");
        item.setAlignment(Pos.CENTER_LEFT);

        VBox vbInfo = new VBox(3);
        vbInfo.setPrefWidth(190);

        Label lblTime = new Label(gd.getThoiGian().toString().replace("T", " "));
        lblTime.setTextFill(Color.GRAY);

        Label lblTen = new Label(tenHienThi(gd));
        Label lblGhiChu = new Label(gd.getGhiChu());
        lblGhiChu.setWrapText(true);

        vbInfo.getChildren().addAll(lblTime, lblTen, lblGhiChu);

        String tienCham = String.format("%,d", gd.getSoTien()).replace(',', '.') + " d";
        Label lblTien = new Label(tienCham);
        lblTien.setMinWidth(90);
        lblTien.setAlignment(Pos.CENTER_RIGHT);
        lblTien.setTextFill(gd.getTenLoai().contains("Thu") ? Color.GREEN : Color.RED);

        Button btnXoa = new Button("Xoa");
        btnXoa.setMinWidth(45);
        btnXoa.setOnAction(e -> xoaGD(gd));

        item.getChildren().addAll(vbInfo, lblTien, btnXoa);
        return item;
    }

    private void xoaGD(GiaoDich gd) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText(null);
        alert.setContentText("Xoa giao dich nay?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            Task<Void> task = new Task<>() {
                @Override
                protected Void call() {
                    clientService.xoaGiaoDich(gd.getIdGiaoDich());
                    return null;
                }
            };
            task.setOnSucceeded(e -> loadDuLieu());
            task.setOnFailed(e -> thongBao(Alert.AlertType.ERROR, task.getException().getMessage()));
            new Thread(task).start();
        }
    }

    public void moThemGiaoDich() {
        chuyenManHinh("/ThemGiaoDich.fxml");
    }

    public void moBaoCao() {
        chuyenManHinh("/BaoCao.fxml");
    }

    private void chuyenManHinh(String fxml) {
        try {
            Scene sc = btnThem.getScene();
            Stage stage = (Stage) sc.getWindow();

            Parent root = FXMLLoader.load(getClass().getResource(fxml));
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            thongBao(Alert.AlertType.ERROR, e.getMessage());
        }
    }

    private boolean isAdmin() {
        return DangNhapController.taiKhoanDangNhap != null
                && "ADMIN".equalsIgnoreCase(DangNhapController.taiKhoanDangNhap.getVaiTro());
    }

    private String tenHienThi(GiaoDich gd) {
        if (!isAdmin()) {
            return gd.getTenDanhMuc();
        }
        String hoTen = gd.getHoTenTaiKhoan();
        String username = gd.getTenDangNhap();
        if (hoTen == null || hoTen.isBlank()) {
            hoTen = username == null ? "Khong ro" : username;
        }
        if (username == null || username.isBlank()) {
            return gd.getTenDanhMuc() + " - " + hoTen;
        }
        return gd.getTenDanhMuc() + " - " + hoTen + " (" + username + ")";
    }

    private void thongBao(Alert.AlertType type, String message) {
        Alert alert = new Alert(type);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
