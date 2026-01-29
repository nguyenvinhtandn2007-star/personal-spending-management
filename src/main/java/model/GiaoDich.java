package model;

import javafx.beans.property.*;
import java.time.LocalDateTime;

public class GiaoDich {

    private int idTaiKhoan;
    private DanhMuc danhMuc;
    private LoaiGiaoDich loai;
    private long soTien;
    private String ghiChu;
    private LocalDateTime thoiGian;
    private int idGiaoDich;

    private StringProperty ngay = new SimpleStringProperty();
    private StringProperty tenDanhMuc = new SimpleStringProperty();
    private StringProperty tenLoai = new SimpleStringProperty();
    private StringProperty ghiChuProperty = new SimpleStringProperty();
    private LongProperty soTienProperty = new SimpleLongProperty();

    public GiaoDich() {}

    public GiaoDich(int idTaiKhoan, DanhMuc danhMuc,LoaiGiaoDich loai, long soTien, String ghiChu, LocalDateTime thoiGian) {
        this.idTaiKhoan = idTaiKhoan;
        this.danhMuc = danhMuc;
        this.loai = loai;
        this.soTien = soTien;
        this.ghiChu = ghiChu;
        this.thoiGian = thoiGian;

        this.soTienProperty.set(soTien);
        this.ghiChuProperty.set(ghiChu);
        if (thoiGian != null) {
            this.ngay.set(thoiGian.toLocalDate().toString());
        }
    }

    public int getIdTaiKhoan() {
        return idTaiKhoan;
    }

    public void setIdTaiKhoan(int idTaiKhoan) {
        this.idTaiKhoan = idTaiKhoan;
    }

    public DanhMuc getDanhMuc() {
        return danhMuc;
    }

    public void setDanhMuc(DanhMuc danhMuc) {
        this.danhMuc = danhMuc;
    }

    public LoaiGiaoDich getLoai() {
        return loai;
    }

    public void setLoai(LoaiGiaoDich loai) {
        this.loai = loai;
    }

    public long getSoTien() {
        return soTien;
    }

    public void setSoTien(long soTien) {
        this.soTien = soTien;
        this.soTienProperty.set(soTien);
    }

    public String getGhiChu() {
        return ghiChu;
    }

    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
        this.ghiChuProperty.set(ghiChu);
    }

    public LocalDateTime getThoiGian() {
        return thoiGian;
    }

    public void setThoiGian(LocalDateTime thoiGian) {
        this.thoiGian = thoiGian;
        if (thoiGian != null) {
            this.ngay.set(thoiGian.toLocalDate().toString());
        }
    }

    public String getNgay() {
        return ngay.get();
    }

    public void setNgay(String ngay) {
        this.ngay.set(ngay);
    }

    public String getTenDanhMuc() {
        return tenDanhMuc.get();
    }

    public void setTenDanhMuc(String tenDanhMuc) {
        this.tenDanhMuc.set(tenDanhMuc);
    }

    public String getTenLoai() {
        return tenLoai.get();
    }

    public void setTenLoai(String tenLoai) {
        this.tenLoai.set(tenLoai);
    }
    public int getIdGiaoDich() {
        return idGiaoDich;
    }
    public void setIdGiaoDich(int idGiaoDich) {
        this.idGiaoDich = idGiaoDich;
    }

    public StringProperty ngayProperty() {
        return ngay;
    }

    public StringProperty tenDanhMucProperty() {
        return tenDanhMuc;
    }

    public StringProperty tenLoaiProperty() {
        return tenLoai;
    }

    public LongProperty soTienProperty() {
        return soTienProperty;
    }

    public StringProperty ghiChuProperty() {
        return ghiChuProperty;
    }
}
