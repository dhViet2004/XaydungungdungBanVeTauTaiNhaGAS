package Entity;

public class TaiKhoan {
    private String maNV;
    private String password;

    public TaiKhoan(String maNV, String password) {
        this.maNV = maNV;
        this.password = password;
    }

    public TaiKhoan() {
        this.maNV = maNV;
        this.password = password;
    }

    public String getMaNV() {
        return this.maNV;
    }

    public void setMaNV(String maNV) {
        this.maNV = maNV;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String toString() {
        return "Entity_TaiKhoan{maNV='" + this.maNV + "', password='" + this.password + "'}";
    }
}
