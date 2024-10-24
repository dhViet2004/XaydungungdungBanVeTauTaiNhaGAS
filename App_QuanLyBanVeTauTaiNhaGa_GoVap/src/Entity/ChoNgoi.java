package Entity;

import java.util.Objects;

public class ChoNgoi {
    private String maCho;
    private LoaiCho loaiCho;
    private ToaTau toaTau;
    private String tenCho;
    private Boolean tinhTrang;
    private float gia;

    public ChoNgoi(String maCho, LoaiCho loaiCho, ToaTau toaTau, String tenCho, Boolean tinhTrang, float gia) {
        this.maCho = maCho;
        this.loaiCho = loaiCho;
        this.toaTau = toaTau;
        this.tenCho = tenCho;
        this.tinhTrang = tinhTrang;
        this.gia = gia;
    }
    public ChoNgoi (String maCho){
        this.maCho = maCho;
    }
    public String getMaCho() {
        return maCho;
    }

    public void setMaCho(String maCho) {
        this.maCho = maCho;
    }

    public LoaiCho getLoaiCho() {
        return loaiCho;
    }

    public void setLoaiCho(LoaiCho loaiCho) {
        this.loaiCho = loaiCho;
    }

    public ToaTau getToaTau() {
        return toaTau;
    }

    public void setToaTau(ToaTau toaTau) {
        this.toaTau = toaTau;
    }

    public String getTenCho() {
        return tenCho;
    }

    public void setTenCho(String tenCho) {
        this.tenCho = tenCho;
    }

    public Boolean getTinhTrang() {
        return tinhTrang;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChoNgoi choNgoi = (ChoNgoi) o;
        return Float.compare(gia, choNgoi.gia) == 0 && Objects.equals(maCho, choNgoi.maCho) && Objects.equals(loaiCho, choNgoi.loaiCho) && Objects.equals(toaTau, choNgoi.toaTau) && Objects.equals(tenCho, choNgoi.tenCho) && Objects.equals(tinhTrang, choNgoi.tinhTrang);
    }

    @Override
    public int hashCode() {
        return Objects.hash(maCho, loaiCho, toaTau, tenCho, tinhTrang, gia);
    }

    public void setTinhTrang(Boolean tinhTrang) {
        this.tinhTrang = tinhTrang;
    }

    public float getGia() {
        return gia;
    }

    public void setGia(float gia) {
        this.gia = gia;
    }

    @Override
    public String toString() {
        return "ChoNgoi{" +
                "maCho='" + maCho + '\'' +
                ", loaiCho=" + loaiCho +
                ", toaTau=" + toaTau +
                ", tenCho='" + tenCho + '\'' +
                ", tinhTrang=" + tinhTrang +
                ", gia=" + gia +
                '}';
    }
}
