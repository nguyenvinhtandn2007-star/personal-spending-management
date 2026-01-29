package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.paint.Color;
import dao.GiaoDichDAO;
import model.GiaoDich;
import java.util.List;
import java.util.Optional;

public class TrangChuController {

    @FXML private VBox vboxDanhSach;
    @FXML private Button btnThem;
    @FXML private VBox btnLichSu;
    @FXML private VBox btnBaoCao;
    @FXML private Label lblXinChao;

    GiaoDichDAO dao = new GiaoDichDAO();

    @FXML
    public void initialize() {

        if (DangNhapController.taiKhoanDangNhap != null) {
            String ten = DangNhapController.taiKhoanDangNhap.getHoTen();
            lblXinChao.setText("Xin chào, " + ten);
        }

        btnThem.setOnAction(e -> moThemGiaoDich());
        btnBaoCao.setOnMouseClicked(e -> moBaoCao());
        btnLichSu.setOnMouseClicked(e -> loadDuLieu());

        loadDuLieu();
    }

    private void loadDuLieu() {
        vboxDanhSach.getChildren().clear();

        int id = DangNhapController.taiKhoanDangNhap.getIdTaiKhoan();
        List<GiaoDich> list = dao.getTheoTaiKhoan(id);

        for (int i = 0; i < list.size(); i++) {
            GiaoDich gd = list.get(i);
            HBox item = taoGiaoDichItem(gd);
            vboxDanhSach.getChildren().add(item);
        }
    }

    private HBox taoGiaoDichItem(GiaoDich gd) {
        HBox item = new HBox(10);
        item.setStyle("-fx-border-color: gray; -fx-border-width: 1px; -fx-padding: 10px;");
        item.setAlignment(Pos.CENTER_LEFT);

        VBox vbInfo = new VBox(3);
        vbInfo.setPrefWidth(140);

        String thoiGianStr = gd.getThoiGian().toString().replace("T", " ");
        Label lblTime = new Label(thoiGianStr);
        lblTime.setTextFill(Color.GRAY);

        Label lblTen = new Label(gd.getTenDanhMuc());

        Label lblGhiChu = new Label(gd.getGhiChu());
        lblGhiChu.setWrapText(true);

        vbInfo.getChildren().addAll(lblTime, lblTen, lblGhiChu);

        long soTien = gd.getSoTien();
        String formatSo = String.format("%,d", soTien);
        String tienCham = formatSo.replace(',', '.') + " đ";

        Label lblTien = new Label(tienCham);
        lblTien.setMinWidth(90);
        lblTien.setAlignment(Pos.CENTER_RIGHT);

        if (gd.getTenLoai().contains("Thu")) {
            lblTien.setTextFill(Color.GREEN);
        } else {
            lblTien.setTextFill(Color.RED);
        }

        Button btnXoa = new Button("Xóa");
        btnXoa.setMinWidth(40);
        btnXoa.setOnAction(e -> xoaGD(gd));

        item.getChildren().addAll(vbInfo, lblTien, btnXoa);

        return item;
    }

    private void xoaGD(GiaoDich gd) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText("XÓA GIAO DỊCH NÀY?");

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            dao.xoaGiaoDich(gd.getIdGiaoDich());
            loadDuLieu();
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
            e.printStackTrace();
        }
    }
}