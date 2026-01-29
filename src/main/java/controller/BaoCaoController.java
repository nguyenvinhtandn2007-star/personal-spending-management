package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import dao.BaoCaoDAO;
import model.BaoCaoNgay;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import java.util.List;
import javafx.scene.paint.Color;

public class BaoCaoController {

    @FXML Label lbThu;
    @FXML Label lbChi;
    @FXML Label lbDu;

    @FXML VBox vbList;
    @FXML VBox nutLichSu;
    @FXML VBox nutBaoCao;

    BaoCaoDAO dao = new BaoCaoDAO();

    @FXML
    public void initialize() {
        List<BaoCaoNgay> ls = dao.getBaoCaoTheoNgay(
                DangNhapController.taiKhoanDangNhap.getIdTaiKhoan()
        );

        long t = dao.tinhTongThu(ls);
        long c = dao.tinhTongChi(ls);
        long sd = t - c;

        lbThu.setText(String.format("%,d", t).replace(',', '.') + " đ");
        lbChi.setText(String.format("%,d", c).replace(',', '.') + " đ");
        lbDu.setText(String.format("%,d", sd).replace(',', '.') + " đ");

        vbList.getChildren().clear();

        for (int i = 0; i < ls.size(); i++) {
            BaoCaoNgay bc = ls.get(i);

            HBox h1 = new HBox(15);

            Label lbDate = new Label(bc.getNgay());

            String thuCham = String.format("%,d", bc.getTongThu()).replace(',', '.');
            Label l1 = new Label("+" + thuCham);
            l1.setTextFill(Color.GREEN);

            String chiCham = String.format("%,d", bc.getTongChi()).replace(',', '.');
            Label l2 = new Label("-" + chiCham);
            l2.setTextFill(Color.RED);

            h1.getChildren().addAll(lbDate, l1, l2);

            vbList.getChildren().add(h1);
        }

        nutLichSu.setOnMouseClicked(e -> {
            try {
                Node source = (Node) e.getSource();
                Stage stage = (Stage) source.getScene().getWindow();

                Parent root = FXMLLoader.load(getClass().getResource("/TrangChu.fxml"));
                stage.setScene(new Scene(root));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }
}