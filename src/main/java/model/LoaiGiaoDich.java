package model;

import java.io.Serializable;

public class LoaiGiaoDich implements Serializable {
    private int idLoai;
    private String tenLoai;

    public LoaiGiaoDich() {}

    public LoaiGiaoDich(int idLoai, String tenLoai) {
        this.idLoai = idLoai;
        this.tenLoai = tenLoai;
    }

    public int getIdLoai() {
        return idLoai;
    }

    public void setIdLoai(int idLoai) {
        this.idLoai = idLoai;
    }

    public String getTenLoai() {
        return tenLoai;
    }

    public void setTenLoai(String tenLoai) {
        this.tenLoai = tenLoai;
    }

    @Override
    public String toString() {
        return tenLoai;
    }
}

