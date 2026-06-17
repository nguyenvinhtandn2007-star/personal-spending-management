package model;

import java.io.Serializable;

public class BaoCaoNgay implements Serializable {

    private String ngay;
    private long tongThu;
    private long tongChi;

    public BaoCaoNgay() {
    }

    public BaoCaoNgay(String ngay, long tongThu, long tongChi) {
        this.ngay = ngay;
        this.tongThu = tongThu;
        this.tongChi = tongChi;
    }

    public String getNgay() {
        return ngay;
    }

    public void setNgay(String ngay) {
        this.ngay = ngay;
    }

    public long getTongThu() {
        return tongThu;
    }

    public void setTongThu(long tongThu) {
        this.tongThu = tongThu;
    }

    public long getTongChi() {
        return tongChi;
    }

    public void setTongChi(long tongChi) {
        this.tongChi = tongChi;
    }
}
