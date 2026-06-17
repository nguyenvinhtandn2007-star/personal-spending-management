package model;

import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.time.LocalDateTime;

public class GiaoDich implements Serializable {

    private int idTaiKhoan;
    private DanhMuc danhMuc;
    private LoaiGiaoDich loai;
    private long soTien;
    private String ghiChu;
    private LocalDateTime thoiGian;
    private int idGiaoDich;

    private String tenDanhMuc;
    private String tenLoai;
    private String tenDangNhap;
    private String hoTenTaiKhoan;

    // Các JavaFX Property chỉ phục vụ hiển thị bảng ở client, không gửi qua socket.
    private transient StringProperty ngayProperty;
    private transient StringProperty tenDanhMucProperty;
    private transient StringProperty tenLoaiProperty;
    private transient StringProperty ghiChuProperty;
    private transient LongProperty soTienProperty;

    public GiaoDich() {
        initProperties();
    }

    public GiaoDich(int idTaiKhoan, DanhMuc danhMuc, LoaiGiaoDich loai, long soTien, String ghiChu, LocalDateTime thoiGian) {
        this.idTaiKhoan = idTaiKhoan;
        this.danhMuc = danhMuc;
        this.loai = loai;
        this.soTien = soTien;
        this.ghiChu = ghiChu;
        this.thoiGian = thoiGian;
        this.tenDanhMuc = danhMuc == null ? null : danhMuc.getTenDanhMuc();
        this.tenLoai = loai == null ? null : loai.getTenLoai();
        initProperties();
    }

    private void initProperties() {
        ngayProperty = new SimpleStringProperty(thoiGian == null ? "" : thoiGian.toLocalDate().toString());
        tenDanhMucProperty = new SimpleStringProperty(tenDanhMuc == null ? "" : tenDanhMuc);
        tenLoaiProperty = new SimpleStringProperty(tenLoai == null ? "" : tenLoai);
        ghiChuProperty = new SimpleStringProperty(ghiChu == null ? "" : ghiChu);
        soTienProperty = new SimpleLongProperty(soTien);
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        initProperties();
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
        this.tenDanhMuc = danhMuc == null ? null : danhMuc.getTenDanhMuc();
        initProperties();
    }

    public LoaiGiaoDich getLoai() {
        return loai;
    }

    public void setLoai(LoaiGiaoDich loai) {
        this.loai = loai;
        this.tenLoai = loai == null ? null : loai.getTenLoai();
        initProperties();
    }

    public long getSoTien() {
        return soTien;
    }

    public void setSoTien(long soTien) {
        this.soTien = soTien;
        initProperties();
    }

    public String getGhiChu() {
        return ghiChu;
    }

    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
        initProperties();
    }

    public LocalDateTime getThoiGian() {
        return thoiGian;
    }

    public void setThoiGian(LocalDateTime thoiGian) {
        this.thoiGian = thoiGian;
        initProperties();
    }

    public int getIdGiaoDich() {
        return idGiaoDich;
    }

    public void setIdGiaoDich(int idGiaoDich) {
        this.idGiaoDich = idGiaoDich;
    }

    public String getNgay() {
        return ngayProperty.get();
    }

    public String getTenDanhMuc() {
        return tenDanhMuc;
    }

    public void setTenDanhMuc(String tenDanhMuc) {
        this.tenDanhMuc = tenDanhMuc;
        initProperties();
    }

    public String getTenLoai() {
        return tenLoai;
    }

    public void setTenLoai(String tenLoai) {
        this.tenLoai = tenLoai;
        initProperties();
    }

    public String getTenDangNhap() {
        return tenDangNhap;
    }

    public void setTenDangNhap(String tenDangNhap) {
        this.tenDangNhap = tenDangNhap;
    }

    public String getHoTenTaiKhoan() {
        return hoTenTaiKhoan;
    }

    public void setHoTenTaiKhoan(String hoTenTaiKhoan) {
        this.hoTenTaiKhoan = hoTenTaiKhoan;
    }

    public StringProperty ngayProperty() {
        return ngayProperty;
    }

    public StringProperty tenDanhMucProperty() {
        return tenDanhMucProperty;
    }

    public StringProperty tenLoaiProperty() {
        return tenLoaiProperty;
    }

    public LongProperty soTienProperty() {
        return soTienProperty;
    }

    public StringProperty ghiChuProperty() {
        return ghiChuProperty;
    }
}
