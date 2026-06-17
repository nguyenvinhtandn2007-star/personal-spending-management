package service;

import model.DanhMuc;
import model.GiaoDich;
import model.LoaiGiaoDich;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CsvService {
    private final GiaoDichService giaoDichService = new GiaoDichService();

    public String exportGiaoDich(int idTaiKhoan) {
        StringBuilder sb = new StringBuilder("id,danh_muc,loai,so_tien,ghi_chu,thoi_gian\n");
        for (GiaoDich gd : giaoDichService.danhSachTheoTaiKhoan(idTaiKhoan)) {
            sb.append(gd.getIdGiaoDich()).append(',')
                    .append(csv(gd.getTenDanhMuc())).append(',')
                    .append(csv(gd.getTenLoai())).append(',')
                    .append(gd.getSoTien()).append(',')
                    .append(csv(gd.getGhiChu())).append(',')
                    .append(gd.getThoiGian()).append('\n');
        }
        return sb.toString();
    }

    public int importGiaoDich(int idTaiKhoan, String csvContent) {
        List<GiaoDich> pending = parse(idTaiKhoan, csvContent);
        int count = 0;
        for (GiaoDich gd : pending) {
            if (giaoDichService.them(gd)) {
                count++;
            }
        }
        return count;
    }

    private List<GiaoDich> parse(int idTaiKhoan, String csvContent) {
        List<GiaoDich> list = new ArrayList<>();
        if (csvContent == null || csvContent.isBlank()) {
            throw new IllegalArgumentException("File CSV trống.");
        }
        String[] lines = csvContent.split("\\R");
        for (int i = 1; i < lines.length; i++) {
            if (lines[i].isBlank()) {
                continue;
            }
            String[] cols = lines[i].split(",", -1);
            if (cols.length < 6) {
                throw new IllegalArgumentException("Dòng CSV sai định dạng: " + (i + 1));
            }
            GiaoDich gd = new GiaoDich();
            gd.setIdTaiKhoan(idTaiKhoan);
            gd.setDanhMuc(findDanhMuc(cols[1]));
            gd.setLoai(findLoai(cols[2]));
            gd.setSoTien(Long.parseLong(cols[3]));
            gd.setGhiChu(cols[4]);
            gd.setThoiGian(LocalDateTime.parse(cols[5]));
            list.add(gd);
        }
        return list;
    }

    private DanhMuc findDanhMuc(String name) {
        return giaoDichService.danhMuc().stream()
                .filter(dm -> dm.getTenDanhMuc().equalsIgnoreCase(name))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Không có danh mục: " + name));
    }

    private LoaiGiaoDich findLoai(String name) {
        return giaoDichService.loaiGiaoDich().stream()
                .filter(loai -> loai.getTenLoai().equalsIgnoreCase(name))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Không có loại giao dịch: " + name));
    }

    private String csv(String value) {
        return value == null ? "" : value.replace(",", " ");
    }
}
