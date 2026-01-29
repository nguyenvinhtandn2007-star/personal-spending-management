package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import dao.DanhMucDAO;
import dao.GiaoDichDAO;
import dao.LoaiGiaoDichDAO;
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

    DanhMucDAO daoDM = new DanhMucDAO();
    LoaiGiaoDichDAO daoLoai = new LoaiGiaoDichDAO();
    GiaoDichDAO daoGD = new GiaoDichDAO();

    @FXML
    public void initialize() {
        List<DanhMuc> listDM = daoDM.getAll();
        cbDM.getItems().addAll(listDM);

        List<LoaiGiaoDich> listLoai = daoLoai.getAll();
        cbLoai.getItems().addAll(listLoai);

        dp.setValue(LocalDate.now());

        LocalTime time = LocalTime.now();
        int gioHienTai = time.getHour();
        int phutHienTai = time.getMinute();

        SpinnerValueFactory<Integer> valGio = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23, gioHienTai);
        spGio.setValueFactory(valGio);

        SpinnerValueFactory<Integer> valPhut = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59, phutHienTai);
        spPhut.setValueFactory(valPhut);
    }
    @FXML
    public void xuLyThemGiaoDich() {
        try {
            String tienText = tfTien.getText();
            DanhMuc dm = cbDM.getValue();
            LoaiGiaoDich loai = cbLoai.getValue();
            LocalDate ngay = dp.getValue();

            if (tienText.equals("") || dm == null || loai == null || ngay == null) {
                System.out.println("NHẬP THIẾU THÔNG TIN");
                return;
            }

            long tien = Long.parseLong(tienText);
            int gio = spGio.getValue();
            int phut = spPhut.getValue();
            String ghiChu = taGhiChu.getText();
            int idTaiKhoan = DangNhapController.taiKhoanDangNhap.getIdTaiKhoan();

            LocalTime gioPhut = LocalTime.of(gio, phut);
            LocalDateTime thoiGian = LocalDateTime.of(ngay, gioPhut);

            GiaoDich gd = new GiaoDich(
                    idTaiKhoan,
                    dm,
                    loai,
                    tien,
                    ghiChu,
                    thoiGian
            );

            daoGD.themGiaoDich(gd);
            System.out.println("THÊM THÀNH CÔNG");

            quayLaiTrangChu();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @FXML
    public void quayLaiTrangChu() {
        try {
            Scene sc = tfTien.getScene();
            Stage stage = (Stage) sc.getWindow();

            Parent root = FXMLLoader.load(getClass().getResource("/TrangChu.fxml"));
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}