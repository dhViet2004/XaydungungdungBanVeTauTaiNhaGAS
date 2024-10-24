package GUI;

import DAO.DAO_BanVe;
import Database.ConnectDatabase;
import Entity.*;
import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import javax.swing.Timer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;

public class FrmBanVe extends JFrame implements ActionListener {
    private final JButton btnBanVe;
    private final JButton btnTraCuu;
    private final JButton btnThongKeTheoCa;
    private final JButton btnQuanLyKhachHang;
    private final JButton btnQuanLyNhanVien;
    private final JButton btnQuanLyChuyenTau;
    private final JButton btnQuanLyKhuyenMai;
    private final JButton btnQuanLyDoanhThu;
    private JPanel contain;
    private JPanel JPanel_Menu;
    private JPanel Jpanel_Main;
    private JLabel lab_Title;
    private JPanel JPanel_BanVe;
    private JPanel JPanel_DSTau;
    private JTextField txt_GaDen;
    private JButton btnTimChuyen;
    private JPanel JPanel_NgayDi;
    private JPanel JPanel_NgayVe;
    private JRadioButton btnRadio_MotChieu;
    private JRadioButton btnRadio_KhuHoi;
    private JPanel JPanelKetQuaTimTau;
    private JPanel JPanelTau;  // JPanel để hiển thị các nút chuyến tàu
    private JPanel JPanel_Toa;
    private JPanel JPanel_ChoNgoi;
    private JPanel JPanel_ChoNgoi_A;
    private JPanel JPanel_ChoNgoi_B;
    private JPanel JPanel_ChoNgoi_C;
    private JPanel JPanel_ChoNgoi_D;
    private JPanel JPanel_ChoNgoi_E;
    private JButton btnTiepTheo;
    private JButton btnChonTatCa;
    private JTextField txtGaDi;
    private JLabel labGaDi;
    private JPanel JPanel_XacNhanCho;
    private JLabel lab_TenToaTau;
    private JLabel lab_tb_ChoNgoiNull;
    private JPanel JPanel_Tienich;
    private JDateChooser dateChooserNgayDi = new JDateChooser();
    private JDateChooser dateChooserNgayVe = new JDateChooser();
    private DAO_BanVe daoBanVe;
    private ButtonGroup btnGroup;
    private List<ChoNgoi> danhSachChoDaChon = new ArrayList<>();
    private Map<String, JButton> buttonMap = new HashMap<>(); // Để lưu trữ mối quan hệ giữa mã chỗ ngồi và JButton
    private List<LichTrinhTau> lichTrinhTaus = null;
    private LichTrinhTau lichKhiChonTau = null;
    private double tongThanhTien = 0.0; // Biến để lưu tổng thành tiền
    private static int ticketCount = 0; // Số vé đã tạo trong ngày
    private static LocalDate lastDate = LocalDate.now(); // Ngày cuối cùng đã tạo vé
    private int customerCount = 0; // Biến đếm số khách hàng
    private Component temp;
    Component chuyenTauPanel = new ChuyenTau().getjPanelMain();
    public FrmBanVe() {
        setTitle("Bán Vé");
        temp= Jpanel_Main;
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        add(contain);

        // MENU
        JPanel_Menu.setLayout(new BoxLayout(JPanel_Menu, BoxLayout.Y_AXIS));
        Color colorXanhDam = new Color(0,131,66);
        JPanel_Menu.setBackground(colorXanhDam); // Màu nền của MENU
        add(JPanel_Menu, BorderLayout.WEST);

        // tạo logo
        ImageIcon iconLogo = new ImageIcon(getClass().getResource("/Anh_HeThong/logo.png")); //SRC LOGO
        Image imgUser = iconLogo.getImage();
        Image scaledLogo = imgUser.getScaledInstance(250, 350, Image.SCALE_SMOOTH);
        ImageIcon scaledIconLogo = new ImageIcon(scaledLogo);

        // Tạo label chứa ảnh
        JLabel lblLogo = new JLabel(scaledIconLogo);
        lblLogo.setAlignmentX(Component.CENTER_ALIGNMENT);
        JPanel_Menu.add(lblLogo);

        // Thêm khoảng cách giữa logo và menu
        JPanel_Menu.add(Box.createRigidArea(new Dimension(0, 20))); // Khoảng cách dọc (10px)

        // Tạo các nút cho từng phần quản lý
        btnBanVe = createButton("Bán vé");
        btnTraCuu = createButton("Tra cứu");
        btnQuanLyKhachHang = createButton("Quản lý khách hàng");
        btnThongKeTheoCa = createButton("Thống kê theo ca");
        btnQuanLyChuyenTau = createButton("Quản lý chuyến tàu");
        btnQuanLyKhuyenMai = createButton("Quản lý chương trình khuyến mãi");
        btnQuanLyDoanhThu = createButton("Quản lý doanh thu");
        btnQuanLyNhanVien = createButton("Quản lý nhân viên");

        // Format nút với cùng kích thước, phông chữ và căn chỉnh
        Dimension buttonSize = new Dimension(200, 60); // Tăng kích thước chiều cao của nút lên 60px
        Font fontMenu = new Font("Arial", Font.PLAIN, 16); // Đặt font chung cho tất cả các nút

        // Định dạng cho từng nút
        JButton[] buttons = {btnBanVe, btnTraCuu, btnThongKeTheoCa, btnQuanLyChuyenTau, btnQuanLyKhachHang, btnQuanLyKhuyenMai, btnQuanLyDoanhThu, btnQuanLyNhanVien};
        for (JButton btn : buttons) {
            btn.setPreferredSize(buttonSize); // Đặt kích thước cố định cho nút
            btn.setFont(fontMenu); // Đặt font
            btn.setHorizontalAlignment(SwingConstants.CENTER); // Căn giữa text trên nút
            btn.setAlignmentX(Component.CENTER_ALIGNMENT); // Căn giữa nút trong JPanel_Menu
            setMaximumSize(buttonSize);
            JPanel_Menu.add(Box.createRigidArea(new Dimension(0, 10))); // Thêm khoảng cách giữa các nút (10px)
            JPanel_Menu.add(btn); // Thêm nút vào JPanel_Menu
        }
        //MENU
        // Calendar
        JPanel_NgayDi.add(dateChooserNgayDi);
        JPanel_NgayVe.add(dateChooserNgayVe);
        // Lấy ngày hiện tại
        Date today = new Date();

        // Thiết lập ngày tối đa cho date chooser
        dateChooserNgayDi.setMinSelectableDate(today); // Chỉ cho phép chọn ngày bằng hoặc trước hôm nay
        dateChooserNgayVe.setMinSelectableDate(today); // Tương tự cho ngày về
        // RadioGroup
        btnGroup = new ButtonGroup();
        btnGroup.add(btnRadio_MotChieu);
        btnGroup.add(btnRadio_KhuHoi);

        // Tạo URL cho biểu tượng của nút btnTiepTheo
        URL imageUrlBuocTiepTheo = getClass().getResource("/Anh_HeThong/Next.png");
        setButtonIcon(btnTiepTheo, imageUrlBuocTiepTheo, 100, 100);
        btnTiepTheo.setVerticalTextPosition(SwingConstants.CENTER);

        URL imageUrlTimChuyen = getClass().getResource("/Anh_HeThong/search_Train.png");
        setButtonIcon(btnTimChuyen, imageUrlTimChuyen, 50, 50); // Gọi phương thức với kích thước tùy chọn
        btnTimChuyen.setVerticalTextPosition(SwingConstants.CENTER);
        btnTimChuyen.setHorizontalTextPosition(SwingConstants.RIGHT);

        ConnectDatabase.getInstance().connect(); // Kết nối database

        daoBanVe = new DAO_BanVe(); // Khởi tạo DAO_BanVe
        btnTimChuyen.addActionListener(this); // Thêm sự kiện cho nút tìm tàu
        btnRadio_KhuHoi.addActionListener(this);
        btnTiepTheo.addActionListener(this);


        btnBanVe.addActionListener(this);
        btnTraCuu.addActionListener(this);
        btnThongKeTheoCa.addActionListener(this);
        btnQuanLyChuyenTau.addActionListener(this);
        btnQuanLyKhuyenMai.addActionListener(this);
        btnQuanLyKhachHang.addActionListener(this);
        btnQuanLyDoanhThu.addActionListener(this);
        btnQuanLyNhanVien.addActionListener(this);

    }

    public static void main(String[] args) {
        FrmBanVe frm = new FrmBanVe();
        frm.setVisible(true);
    }

    public void setButtonIcon(JButton button, URL imageUrl, int width, int height) {
        if (imageUrl != null) {
            ImageIcon icon = new ImageIcon(imageUrl); // Tạo ImageIcon từ URL
            Image image = icon.getImage(); // Lấy hình ảnh từ ImageIcon
            Image resizedImage = image.getScaledInstance(width, height, Image.SCALE_SMOOTH); // Thay đổi kích thước hình ảnh
            icon = new ImageIcon(resizedImage); // Tạo lại ImageIcon với hình ảnh đã thay đổi kích thước
            button.setIcon(icon); // Gán biểu tượng vào nút

            // Tùy chọn: Căn chỉnh văn bản và biểu tượng
            button.setHorizontalTextPosition(SwingConstants.CENTER);
            button.setVerticalTextPosition(SwingConstants.BOTTOM);
            button.setContentAreaFilled(false); // Loại bỏ vùng nội dung mặc định
            button.setBorderPainted(false); // Loại bỏ viền mặc định của nút
        } else {
            System.out.println("URL hình ảnh không hợp lệ.");
        }
    }

    // CREATE A BUTTON
    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 20)); // Đặt font
        button.setPreferredSize(new Dimension(200, 60)); // Kích thước nút
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60)); // Chiếm hết chiều ngang
        button.setAlignmentX(Component.CENTER_ALIGNMENT); // Căn giữa nút
        button.setBackground(Color.cyan); // Màu nền
//        button.setOpaque(false); // Làm cho nền trong suốt
        button.setForeground(Color.white); // Màu chữ
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.WHITE, 1), // Viền trắng
                BorderFactory.createEmptyBorder(0, 0, 0, 0) // Padding nếu cần
        ));
//        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setOpaque(false);
//        button.setBorderPainted(false); // Tắt viền nút
//        button.setFocusable(false); // Tắt chế độ focus
        return button;
    }

    private void updateConfirmationPanel(LichTrinhTau lichTrinhTau) throws SQLException {
        JPanel_XacNhanCho.removeAll(); // Xóa nội dung cũ
        JPanel_XacNhanCho.setLayout(new BoxLayout(JPanel_XacNhanCho, BoxLayout.Y_AXIS)); // Đặt layout theo chiều dọc

        if (danhSachChoDaChon.isEmpty()) {
            JLabel label = new JLabel("Bạn chưa chọn chỗ ngồi nào.");
            JPanel_XacNhanCho.add(label);
        } else {
            Tau tau = daoBanVe.getThongTin(lichTrinhTau.getTau().getMaTau());

            for (ChoNgoi choNgoi : danhSachChoDaChon) {
                // Tạo một panel cho từng chỗ ngồi
                JPanel choPanel = new JPanel();
                choPanel.setLayout(new BoxLayout(choPanel, BoxLayout.Y_AXIS)); // Đặt layout theo chiều dọc

                // Tạo JLabel hiển thị thông tin chỗ ngồi
                JLabel choLabel = new JLabel("| Chỗ ngồi: " + choNgoi.getTenCho());
                JLabel gaDiLabel = new JLabel("| Ga đi: " + tau.getTuyenTau().getDiaDiemDi() + "  | " + "Ga đến: " + tau.getTuyenTau().getDiaDiemDen());
                JLabel ngayDiLabel = new JLabel("| Ngày đi: " + lichTrinhTau.getNgayDi() + " | " + "Giờ đi: " + lichTrinhTau.getGioDi());
                choLabel.setFont(new Font("Arial", Font.PLAIN, 20));
                gaDiLabel.setFont(new Font("Arial", Font.PLAIN, 20));
                ngayDiLabel.setFont(new Font("Arial", Font.PLAIN, 20));
                // Thêm các JLabel vào panel
                choPanel.add(choLabel);
                choPanel.add(gaDiLabel);
                choPanel.add(ngayDiLabel);

                // Tạo nút "Xóa"
                JButton deleteButton = new JButton();
                URL imageUrl = getClass().getResource("/Anh_HeThong/remove.png");
                setButtonIcon(deleteButton,imageUrl,50,50);
                deleteButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        // Xóa chỗ ngồi khỏi danh sách đã chọn
                        danhSachChoDaChon.remove(choNgoi);

                        // Cập nhật màu sắc nút chỗ ngồi tương ứng
                        JButton correspondingButton = getButtonForSeat(choNgoi); // Hàm giả định để lấy nút chỗ ngồi
                        if (correspondingButton != null) {
                            correspondingButton.setBackground(choNgoi.getTinhTrang() ? Color.WHITE : Color.PINK); // Đặt lại màu sắc
                        }

                        try {
                            updateConfirmationPanel(lichTrinhTau); // Cập nhật lại panel xác nhận
                        } catch (SQLException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                });

                // Thêm nút "Xóa" vào panel
                choPanel.add(deleteButton); // Nút xóa nằm dưới các thông tin chỗ ngồi

                // Thêm panel chỗ ngồi vào JPanel_XacNhanCho
                JPanel_XacNhanCho.add(choPanel);

                // Tạo JLabel hiển thị bộ đếm ngược
                JLabel countdownLabel = new JLabel("Thời gian còn lại: 500 giây");
                choPanel.add(countdownLabel);
                JLabel lineBot = new JLabel("|------------------------------------------------------------------------------------");
                choPanel.add(lineBot);
                // Khởi tạo bộ đếm ngược
                int countdownTime = 500; // Thời gian đếm ngược (500 giây)
                Timer timer = new Timer(1000, new ActionListener() {
                    int timeLeft = countdownTime;

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (timeLeft > 0) {
                            timeLeft--;
                            countdownLabel.setText("Thời gian còn lại: " + timeLeft + " giây");
                        } else {
                            ((Timer) e.getSource()).stop(); // Dừng timer

                            // Tự động nhấn nút "Xóa" cho chỗ ngồi hiện tại
                            deleteButton.doClick(); // Gọi hành động nút xóa
                        }
                    }
                });
                timer.start(); // Bắt đầu bộ đếm ngược
            }
        }
        JPanel_XacNhanCho.revalidate(); // Cập nhật giao diện
        JPanel_XacNhanCho.repaint(); // Vẽ lại panel
    }


    // Hàm để lấy nút tương ứng với chỗ ngồi
    private JButton getButtonForSeat(ChoNgoi choNgoi) {
        return buttonMap.get(choNgoi.getMaCho()); // Trả về nút tương ứng
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnTimChuyen) {
            JPanelTau.removeAll();
            JPanel_Toa.removeAll();
            JPanel_ChoNgoi_A.removeAll();
            JPanel_ChoNgoi_B.removeAll();
            JPanel_ChoNgoi_C.removeAll();
            JPanel_ChoNgoi_D.removeAll();
            lab_TenToaTau.setText("");
            // Lấy thông tin ga đến và ngày đi
            String gaDen = txt_GaDen.getText();

            // Định dạng ngày từ JDateChooser
            if (dateChooserNgayDi.getDate() == null) {
                JOptionPane.showMessageDialog(this, "Bạn chưa chọn ngày đi.");
                return; // Ngừng thực hiện nếu không có ngày
            }
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String ngayDi = sdf.format(dateChooserNgayDi.getDate());
            // Gọi DAO để tìm các chuyến tàu

            try {
                lichTrinhTaus = daoBanVe.getTrainsByDateAndDestination(gaDen, ngayDi);

            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }

            // Hiển thị kết quả dưới dạng các JButton có hình ảnh
            JPanelTau.removeAll(); // Xóa các nút trước đó nếu có
            JPanelTau.setLayout(new FlowLayout());

            if (lichTrinhTaus.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Không tìm thấy chuyến tàu nào.");
            } else {
                for (LichTrinhTau lichTrinhTau : lichTrinhTaus) {
                    String tauInfo = "Tên tàu: " + lichTrinhTau.getTau().getTenTau() + ", Giờ chạy: " + lichTrinhTau.getGioDi();
                    JButton tauButton = new JButton(tauInfo);

                    // Thêm hình ảnh đại diện cho nút
                    URL imageUrl = getClass().getResource("/Anh_HeThong/DauTauHoa.png");
                    if (imageUrl != null) {
                        ImageIcon icon = new ImageIcon(imageUrl);
                        Image image = icon.getImage(); // Lấy hình ảnh từ ImageIcon
                        Image resizedImage = image.getScaledInstance(100, 100, Image.SCALE_SMOOTH); // Thay đổi kích thước
                        icon = new ImageIcon(resizedImage); // Tạo lại ImageIcon với hình ảnh đã thay đổi kích thước
                        tauButton.setIcon(icon);
                    } else {
                        System.out.println("File ảnh không tồn tại hoặc đường dẫn không chính xác.");
                    }
                    // Thêm ActionListener cho mỗi nút tàu
                    tauButton.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent event) {
                            if(!danhSachChoDaChon.isEmpty()){
                                JOptionPane.showMessageDialog(tauButton,"Vui lòng xóa hết vé trong giỏ hàng để chọn tàu");
                            }else{
                                DAO_BanVe daoBanVe = new DAO_BanVe();
                                // Gọi DAO để tìm các toa của tàu
                                List<ToaTau> danhSachToa = null;
                                try {
                                    danhSachToa = daoBanVe.getToaByMaTau(lichTrinhTau.getTau().getMaTau());
                                } catch (SQLException ex) {
                                    throw new RuntimeException(ex);
                                }
                                // Hiển thị toa dưới dạng hình ảnh
                                JPanel_Toa.removeAll();
                                JPanel_Toa.setLayout(new FlowLayout());
                                if (danhSachToa.isEmpty()) {
                                    JOptionPane.showMessageDialog(null, "Không tìm thấy toa tàu.");
                                } else {
                                    // Kiểm tra thứ tự của toa để gán hình ảnh tương ứng
                                    URL imageDauTau = getClass().getResource("/Anh_HeThong/DauTauHoaMauVang.png");
                                    // Thêm hình ảnh cho nút
                                    if (imageDauTau != null) {
                                        //Tao Đầu tau
                                        ImageIcon icon = new ImageIcon(imageDauTau);
                                        Image img = icon.getImage();
                                        Image resizedImage = img.getScaledInstance(100, 50, Image.SCALE_SMOOTH); // Tăng kích thước hình ảnh
                                        icon = new ImageIcon(resizedImage);
                                        JButton DautoaButton = new JButton(lichTrinhTau.getTau().getMaTau());
                                        DautoaButton.setIcon(icon);
                                        JPanel_Toa.add(DautoaButton);
                                        //Căng chỉnh btn
                                        DautoaButton.setHorizontalTextPosition(SwingConstants.CENTER);
                                        DautoaButton.setVerticalTextPosition(SwingConstants.BOTTOM);
                                        DautoaButton.setContentAreaFilled(false);
                                        DautoaButton.setBorderPainted(false);
                                        // Căn chỉnh biểu tượng với viền của nút
                                        DautoaButton.setIconTextGap(0);  // Không có khoảng cách giữa biểu tượng và văn bản
                                        DautoaButton.setMargin(new Insets(0, 0, 0, 0));  // Loại bỏ phần đệm của nút
                                        DautoaButton.setContentAreaFilled(false);  // Loại bỏ vùng nội dung mặc định của nút
                                        DautoaButton.setBorderPainted(false);  // Loại bỏ viền mặc định của nút
                                    } else {
                                        System.out.println("Ảnh không tồn tại.");
                                    }
                                    for (ToaTau toaTau : danhSachToa) {
                                        URL image = getClass().getResource("/Anh_HeThong/ThanToaMauVang.png");
                                        JButton toaButton = new JButton("Toa: " + toaTau.getThuTu()); // Hiển thị mã toa
                                        // Thêm hình ảnh cho nút
                                        if (image != null) {
                                            ImageIcon icon = new ImageIcon(image);
                                            Image img = icon.getImage();
                                            Image resizedImage = img.getScaledInstance(100, 50, Image.SCALE_SMOOTH); // Tăng kích thước hình ảnh
                                            icon = new ImageIcon(resizedImage);
                                            toaButton.setIcon(icon);
                                        } else {
                                            System.out.println("Ảnh không tồn tại.");
                                        }
                                        // Thêm hiệu ứng hover
                                        Color originalBackground = toaButton.getBackground(); // Lưu màu nền ban đầu
                                        Color hoverBackground = new Color(250, 196, 58); // Màu nền khi hover
                                        toaButton.setToolTipText(toaTau.getLoaiToa().getTenLoai()); // Đây là văn bản chú thích
                                        toaButton.addMouseListener(new java.awt.event.MouseAdapter() {
                                            @Override
                                            public void mouseEntered(java.awt.event.MouseEvent evt) {
                                                toaButton.setBackground(hoverBackground); // Đổi màu khi hover
                                                toaButton.setOpaque(true); // Đảm bảo màu được hiển thị
                                            }

                                            @Override
                                            public void mouseExited(java.awt.event.MouseEvent evt) {
                                                toaButton.setBackground(originalBackground); // Trả lại màu nền ban đầu
                                                toaButton.setOpaque(false); // Xóa màu nền khi không hover
                                            }
                                        });

                                        // Trong phần xử lý nút toa
                                        toaButton.addActionListener(new ActionListener() {
                                            @Override
                                            public void actionPerformed(ActionEvent event) {
                                                lab_TenToaTau.setText("Toa tàu số " + toaTau.getThuTu() + ": " + toaTau.getLoaiToa().getTenLoai());
                                                DAO_BanVe daoBanVe = new DAO_BanVe();
                                                List<ChoNgoi> danhSachChoNgoi = null;
                                                lichKhiChonTau = lichTrinhTau;

                                                try {
                                                    // Gọi DAO để tìm các chỗ ngồi của toa
                                                    danhSachChoNgoi = daoBanVe.getSeatsByMaToa(toaTau.getMaToa());
                                                } catch (SQLException ex) {
                                                    throw new RuntimeException(ex);
                                                }

                                                // Hiển thị chỗ ngồi trong JPanel_ChoNgoi
                                                JPanel_ChoNgoi_A.removeAll();
                                                JPanel_ChoNgoi_A.setLayout(new FlowLayout());
                                                JPanel_ChoNgoi_B.removeAll();
                                                JPanel_ChoNgoi_B.setLayout(new FlowLayout());
                                                JPanel_ChoNgoi_C.removeAll();
                                                JPanel_ChoNgoi_C.setLayout(new FlowLayout());
                                                JPanel_ChoNgoi_D.removeAll();
                                                JPanel_ChoNgoi_D.setLayout(new FlowLayout());

                                                if (danhSachChoNgoi.isEmpty()) {
                                                    JOptionPane.showMessageDialog(null, "Không tìm thấy chỗ ngồi.");
                                                } else {
                                                    for (ChoNgoi choNgoi : danhSachChoNgoi) {
                                                        JButton choButton = new JButton();
                                                        choButton.setText(choNgoi.getTenCho()); // Gán tên chỗ

                                                        // Lưu nút vào buttonMap để dễ dàng truy cập sau này
                                                        buttonMap.put(choNgoi.getMaCho(), choButton); // Lưu nút vào map

                                                        // Kiểm tra xem chỗ ngồi đã được chọn hay chưa
                                                        if (danhSachChoDaChon.stream().anyMatch(c -> c.getMaCho().equals(choNgoi.getMaCho()))) {
                                                            choButton.setBackground(Color.YELLOW); // Nếu đã chọn, đổi màu thành vàng
                                                        } else {
                                                            // Thay đổi màu nền dựa trên tình trạng chỗ ngồi
                                                            if (choNgoi.getTinhTrang()) {
                                                                choButton.setBackground(Color.WHITE); // Còn trống
                                                            } else {
                                                                choButton.setBackground(Color.PINK); // Đã đặt
                                                            }
                                                        }
                                                        choButton.setPreferredSize(new Dimension(60, 60)); // Kích thước nút hình vuông
                                                        choButton.setOpaque(true);

                                                        // Kiểm tra vé cho mã chỗ ngồi
                                                        VeTau veTau = daoBanVe.getVeTaubyLichTrinhTauandMaCho(lichTrinhTau.getMaLichTrinh(), choNgoi.getMaCho());
                                                        if (veTau != null) {
                                                            // Nếu có vé, khóa nút và đổi màu
                                                            choButton.setEnabled(false); // Khóa nút
                                                            choButton.setBackground(Color.pink); // Đổi màu nút thành đỏ
                                                            choButton.setText("Đã có vé"); // Cập nhật văn bản để thể hiện trạng thái đã đặt
                                                        } else {
                                                            // Nếu không có vé, thêm ActionListener cho nút chỗ ngồi
                                                            choButton.addActionListener(new ActionListener() {
                                                                @Override
                                                                public void actionPerformed(ActionEvent e) {
                                                                    // Kiểm tra xem chỗ ngồi đã được chọn hay chưa
                                                                    if (danhSachChoDaChon.stream().anyMatch(c -> c.getMaCho().equals(choNgoi.getMaCho()))) {
                                                                        // Nếu chỗ ngồi đang được chọn, hủy chọn và đổi màu lại như ban đầu
                                                                        choButton.setBackground(choNgoi.getTinhTrang() ? Color.WHITE : Color.PINK); // Màu nền dựa trên tình trạng
                                                                        danhSachChoDaChon.removeIf(c -> c.getMaCho().equals(choNgoi.getMaCho())); // Xóa khỏi danh sách đã chọn
                                                                    } else {
                                                                        // Nếu chỗ ngồi chưa được chọn, đổi màu sang vàng khi được chọn
                                                                        choButton.setBackground(Color.YELLOW); // Màu vàng khi được chọn
                                                                        danhSachChoDaChon.add(choNgoi); // Thêm vào danh sách đã chọn
                                                                    }

                                                                    try {
                                                                        updateConfirmationPanel(lichTrinhTau); // Cập nhật panel xác nhận chỗ
                                                                    } catch (SQLException ex) {
                                                                        throw new RuntimeException(ex);
                                                                    }
                                                                }
                                                            });
                                                        }

                                                        // Gán chỗ ngồi vào JPanel tương ứng dựa trên ký tự cuối cùng
                                                        String tenCho = choNgoi.getTenCho(); // Lấy tên chỗ
                                                        if (tenCho != null && !tenCho.isEmpty()) {
                                                            char cuoi = tenCho.charAt(tenCho.length() - 1); // Lấy ký tự cuối cùng
                                                            switch (cuoi) {
                                                                case 'A':
                                                                    JPanel_ChoNgoi_A.add(choButton);
                                                                    break;
                                                                case 'B':
                                                                    JPanel_ChoNgoi_B.add(choButton);
                                                                    break;
                                                                case 'C':
                                                                    JPanel_ChoNgoi_C.add(choButton);
                                                                    break;
                                                                case 'D':
                                                                    JPanel_ChoNgoi_D.add(choButton);
                                                                    break;
                                                                default:
                                                                    // Nếu không khớp với các ký tự A, B, C, D thì có thể bỏ qua hoặc xử lý tùy ý
                                                                    break;
                                                            }
                                                        }
                                                    }
                                                    // Cập nhật giao diện cho các JPanel
                                                    JPanel_ChoNgoi_A.revalidate();
                                                    JPanel_ChoNgoi_A.repaint();
                                                    JPanel_ChoNgoi_B.revalidate();
                                                    JPanel_ChoNgoi_B.repaint();
                                                    JPanel_ChoNgoi_C.revalidate();
                                                    JPanel_ChoNgoi_C.repaint();
                                                    JPanel_ChoNgoi_D.revalidate();
                                                    JPanel_ChoNgoi_D.repaint();
                                                }
                                            }
                                        });


                                        // Thêm nút toa vào JPanel_Toa
                                        JPanel_Toa.add(toaButton);
                                        toaButton.setHorizontalTextPosition(SwingConstants.CENTER);
                                        toaButton.setVerticalTextPosition(SwingConstants.BOTTOM);
                                        toaButton.setContentAreaFilled(false);
                                        toaButton.setBorderPainted(false);

                                        // Căn chỉnh biểu tượng với viền của nút
                                        toaButton.setIconTextGap(0);  // Không có khoảng cách giữa biểu tượng và văn bản
                                        toaButton.setMargin(new Insets(0, 0, 0, 0));  // Loại bỏ phần đệm của nút
                                        toaButton.setContentAreaFilled(false);  // Loại bỏ vùng nội dung mặc định của nút
                                        toaButton.setBorderPainted(false);  // Loại bỏ viền mặc định của nút
                                    }

                                    // Cập nhật giao diện
                                    JPanel_Toa.revalidate();
                                    JPanel_Toa.repaint();
                                }
                            }
                        }
                    });

                    // Thêm nút vào JPanelTau
                    JPanelTau.add(tauButton);
                    tauButton.setHorizontalTextPosition(SwingConstants.CENTER);
                    tauButton.setVerticalTextPosition(SwingConstants.BOTTOM);
                    tauButton.setContentAreaFilled(false);
                    tauButton.setBorderPainted(false);
                    // Thêm hiệu ứng hover
                    Color originalBackground = tauButton.getBackground(); // Lưu màu nền ban đầu
                    Color hoverBackground = new Color(250, 196, 58); // Màu nền khi hover


                }
                JPanelTau.revalidate(); // Cập nhật giao diện
                JPanelTau.repaint();
            }
        }  else if (e.getSource() == btnRadio_KhuHoi) {

        } else if (e.getSource() == btnTiepTheo) {
            // Kiểm tra xem danh sách chỗ ngồi đã chọn có trống không
            if (danhSachChoDaChon.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Bạn chưa chọn chỗ ngồi nào.");
                return;
            }

            // Tạo cửa sổ mới để hiển thị danh sách chỗ ngồi đã chọn
            JDialog dialog = new JDialog(this, "Danh sách chỗ ngồi đã chọn", true);
            dialog.setSize(1050, 700); // Kích thước của cửa sổ
            dialog.setLocationRelativeTo(this); // Hiển thị ở giữa màn hình

            // Tạo tiêu đề các cột
            String[] columnNames = {"Họ tên", "Thông tin chỗ ngồi", "Giá vé", "Giảm đối tượng", "Khuyến mại", "Thành tiền"};

            // Tạo mô hình bảng
            DefaultTableModel model = new DefaultTableModel(columnNames, 0);

            // Tạo danh sách lưu trữ các CustomPanel cho từng hàng trong bảng
            List<CustomPanel> danhSachPanel = new ArrayList<>();

            // Trong vòng lặp thêm hàng vào bảng
            for (ChoNgoi choNgoi : danhSachChoDaChon) {
                CustomPanel panel = new CustomPanel();
                panel.setHoTen(""); // Thiết lập các thuộc tính cần thiết
                panel.setTrangThai("Chưa xác nhận", choNgoi.getGia());
                panel.setCCCD("");
                panel.setGiaVe(choNgoi.getGia());

                danhSachPanel.add(panel); // Lưu lại panel này vào danh sách

                // Cập nhật discount và tính toán thành tiền
                double discount = panel.getDiscount();
                double thanhTien = choNgoi.getGia() - discount;
                tongThanhTien += thanhTien; // Cộng dồn vào tổng thành tiền
                Object[] rowData = new Object[]{
                        panel, // Đưa panel vào bảng
                        choNgoi.getMaCho(),
                        choNgoi.getGia(),
                        discount,
                        "", // Khuyến mại
                        thanhTien
                };
                model.addRow(rowData);
            }
            // Tạo bảng với mô hình dữ liệu
            JTable table = new JTable(model);

            // Tùy chỉnh tiêu đề bảng
            JTableHeader header = table.getTableHeader();
            Color colorTieuDeBang = new Color(0,131,66);
            header.setBackground(colorTieuDeBang); // Đặt màu nền cho tiêu đề
            header.setForeground(Color.WHITE); // Đặt màu chữ cho tiêu đề

            header.setFont(new Font("Arial", Font.BOLD, 20)); // Đặt font cho tiêu đề (cỡ 16)

            // Tùy chỉnh font cho các dòng trong bảng
            table.setFont(new Font("Arial", Font.PLAIN, 18)); // Đặt font cho các dòng (cỡ 14)

            // Thiết lập renderer và editor cho cột đầu tiên
            table.getColumnModel().getColumn(0).setCellRenderer(new CustomCellRenderer());
            table.getColumnModel().getColumn(0).setCellEditor(new CustomCellEditor());

            // Tùy chỉnh chiều cao các hàng
            table.setRowHeight(120); // Chiều cao mỗi hàng

            // Tùy chỉnh chiều rộng các cột
            table.getColumnModel().getColumn(0).setPreferredWidth(200); // Họ tên
            table.getColumnModel().getColumn(1).setPreferredWidth(200); // Thông tin chỗ ngồi
            table.getColumnModel().getColumn(2).setPreferredWidth(100); // Giá vé
            table.getColumnModel().getColumn(3).setPreferredWidth(150); // Giảm đối tượng
            table.getColumnModel().getColumn(4).setPreferredWidth(100); // Khuyến mại
            table.getColumnModel().getColumn(5).setPreferredWidth(100); // Thành tiền

            // Tạo JScrollPane cho bảng
            JScrollPane scrollPane = new JScrollPane(table);
            dialog.add(scrollPane, BorderLayout.CENTER); // Thêm JScrollPane vào cửa sổ

            // Tạo JPanel để chứa thông tin người mua
            JPanel buyerInfoPanel = new JPanel(new BorderLayout()); // Tạo layout 4 hàng, 2 cột với khoảng cách 10px

            JPanel JpanelThanhTien = new JPanel(new BorderLayout());
            JpanelThanhTien.setBackground(colorTieuDeBang);
            JpanelThanhTien.setPreferredSize(new Dimension(buyerInfoPanel.getWidth(), 50)); // Thay đổi kích thước

            JLabel lbl_ThanhTien = new JLabel("Tổng thành tiền: "+tongThanhTien+" VND");
            lbl_ThanhTien.setForeground(Color.WHITE);
            lbl_ThanhTien.setFont(new Font("Arial", Font.BOLD, 24)); // Đặt cỡ chữ lớn hơn
            JpanelThanhTien.add(lbl_ThanhTien,BorderLayout.EAST);

            buyerInfoPanel.add(JpanelThanhTien, BorderLayout.NORTH);

            JPanel thongTinNguoiMua = new JPanel(new GridLayout(4, 2, 5, 5));
            // Tạo các label và text field cho thông tin người mua
            JLabel lblHoTenNguoiMua = new JLabel("Họ tên người mua:");
            lblHoTenNguoiMua.setFont(new Font("Arial", Font.BOLD, 16)); // Tăng cỡ chữ
            JTextField txtHoTenNguoiMua = new JTextField();
            txtHoTenNguoiMua.setPreferredSize(new Dimension(200, 30)); // Tăng kích thước text field

            JLabel lblCCCDNguoiMua = new JLabel("CCCD người mua:");
            lblCCCDNguoiMua.setFont(new Font("Arial", Font.BOLD, 16)); // Tăng cỡ chữ
            JTextField txtCCCDNguoiMua = new JTextField();
            txtCCCDNguoiMua.setPreferredSize(new Dimension(200, 30)); // Tăng kích thước text field

            JLabel lblSoDienThoai = new JLabel("Số điện thoại:");
            lblSoDienThoai.setFont(new Font("Arial", Font.BOLD, 16)); // Tăng cỡ chữ
            JTextField txtSoDienThoai = new JTextField();
            txtSoDienThoai.setPreferredSize(new Dimension(200, 30)); // Tăng kích thước text field

            JLabel lblDiaChi = new JLabel("Địa chỉ:");
            lblDiaChi.setFont(new Font("Arial", Font.BOLD, 16)); // Tăng cỡ chữ
            JTextField txtDiaChi = new JTextField();
            txtDiaChi.setPreferredSize(new Dimension(200, 30)); // Tăng kích thước text field

            // Thêm các label và text field vào panel
            thongTinNguoiMua.add(lblHoTenNguoiMua);
            thongTinNguoiMua.add(txtHoTenNguoiMua);

            thongTinNguoiMua.add(lblCCCDNguoiMua);
            thongTinNguoiMua.add(txtCCCDNguoiMua);

            thongTinNguoiMua.add(lblSoDienThoai);
            thongTinNguoiMua.add(txtSoDienThoai);

            thongTinNguoiMua.add(lblDiaChi);
            thongTinNguoiMua.add(txtDiaChi);
            buyerInfoPanel.add(thongTinNguoiMua,BorderLayout.CENTER);

            JButton btnThanToan = new JButton("Thanh Toán"); // Cộng dồn vào tổng thành tiền);

            // Đặt kích thước cho nút
            btnThanToan.setPreferredSize(new Dimension(150, 50)); // Kích thước nút: rộng 150, cao 50
            btnThanToan.setMaximumSize(new Dimension(150, 50));

            // Đặt màu nền và màu chữ cho nút
            btnThanToan.setBackground(new Color(0, 131, 66)); // Màu nền nút (cùng màu với tiêu đề bảng)
            btnThanToan.setForeground(Color.WHITE); // Màu chữ nút

            // Tùy chỉnh font cho nút
            btnThanToan.setFont(new Font("Arial", Font.BOLD, 18)); // Đặt font cho nú

            JPanel Jpanel_NutThanhToan = new JPanel(new FlowLayout());
            Jpanel_NutThanhToan.add(btnThanToan, BorderLayout.CENTER);

            buyerInfoPanel.add(Jpanel_NutThanhToan,BorderLayout.SOUTH); // Thêm nút In vào phía dưới cùng

            dialog.add(buyerInfoPanel, BorderLayout.SOUTH); // Thêm ở phía trên
            buyerInfoPanel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    updateTotalAmount(lbl_ThanhTien,table); // Cập nhật tổng thành tiền khi chuột vào panel
                }
            });
            btnThanToan.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // Lấy thông tin từ các trường nhập
                    String hoTenNguoiMua = txtHoTenNguoiMua.getText();
                    String cccdNguoiMua = txtCCCDNguoiMua.getText();
                    String soDienThoai = txtSoDienThoai.getText();
                    String diaChi = txtDiaChi.getText();

// Kiểm tra xem thông tin đã được điền đầy đủ chưa
                    if (hoTenNguoiMua.isEmpty() || cccdNguoiMua.isEmpty() || soDienThoai.isEmpty() || diaChi.isEmpty()) {
                        JOptionPane.showMessageDialog(dialog, "Vui lòng điền đầy đủ thông tin người mua.");
                        return;
                    }

// Tạo mã khách hàng và đối tượng KhachHang
                    String maKH = generateCustomerCode(); // Sinh mã khách hàng mới
                    KhachHang khachHang = new KhachHang(
                            maKH,
                            new LoaiKhachHang("KH001"), // Mã loại khách hàng mặc định
                            soDienThoai,
                            hoTenNguoiMua,
                            cccdNguoiMua,
                            diaChi,
                            0.0, // Điểm tích lũy ban đầu
                            LocalDate.of(1990, 1, 1), // Ngày sinh mẫu
                            LocalDate.now(), // Ngày tham gia
                            "Silver" // Hạng thành viên mặc định
                    );

                    try {
                        DAO_BanVe daoBanVe = new DAO_BanVe(); // Tạo đối tượng DAO

                        // Lưu khách hàng vào cơ sở dữ liệu
                        daoBanVe.saveCustomer(khachHang); // Gọi phương thức lưu khách hàng trước khi lưu hóa đơn

                        // Tạo danh sách vé cần lưu
                        List<VeTau> ticketsToSave = new ArrayList<>();
                        List<ChiTietHoaDon> chiTietHoaDonList = new ArrayList<>();
                        double tongTien = 0;

                        // Lặp qua tất cả các hàng trong bảng để lấy thông tin vé
                        for (int row = 0; row < table.getRowCount(); row++) {
                            CustomPanel customPanel = (CustomPanel) table.getValueAt(row, 0);
                            String hoTen = customPanel.getHoTen();
                            String doiTuong = customPanel.getTrangThai();
                            String cccd = customPanel.getCCCD();
                            String thongTinChoNgoi = (String) table.getValueAt(row, 1);
                            float giaVe = (float) table.getValueAt(row, 2);
                            double thanhTien = (double) table.getValueAt(row, 5);

                            // Tạo mã vé và đối tượng VeTau
                            String maVe = generateTicketCode(lichKhiChonTau.getTau().getMaTau());
                            ChoNgoi cn = new ChoNgoi(thongTinChoNgoi);
                            VeTau ticket = new VeTau(maVe, lichKhiChonTau, cn, hoTen, cccd, lichKhiChonTau.getNgayDi(), doiTuong, giaVe, "Đã thanh toán");

                            ticketsToSave.add(ticket);
                            tongTien += thanhTien;

                            // Tạo chi tiết hóa đơn với mã vé tương ứng
                            ChiTietHoaDon chiTiet = new ChiTietHoaDon(maVe, "", 1, 0.0, thanhTien, 0.0); // Mã hóa đơn sẽ thêm sau
                            chiTietHoaDonList.add(chiTiet);
                        }

                        // Lưu vé vào cơ sở dữ liệu trước
                        daoBanVe.saveTickets(ticketsToSave); // Phải lưu trước để mã vé tồn tại

                        // Tạo mã hóa đơn
                        String maHD = generateInvoiceCode();
                        LoaiHoaDon loaiHoaDon = new LoaiHoaDon("LHD01");
                        // Lưu hóa đơn
                        HoaDon hoaDon = new HoaDon(maHD, khachHang, null, null,loaiHoaDon, LocalDate.now(), 0, tongTien);
                        daoBanVe.saveInvoice(hoaDon); // Lưu hóa đơn

                        // Lưu chi tiết hóa đơn
                        for (ChiTietHoaDon ct : chiTietHoaDonList) {
                            ct.setMaHD(maHD); // Gán mã hóa đơn cho chi tiết
                            daoBanVe.saveInvoiceDetail(ct); // Lưu từng chi tiết hóa đơn
                        }

                        JOptionPane.showMessageDialog(dialog, "Lưu vé và hóa đơn thành công!");

                        // Cập nhật giao diện sau khi lưu
                        danhSachChoDaChon.clear();
                        JPanel_XacNhanCho.removeAll();
                        JPanel_XacNhanCho.revalidate();
                        JPanel_XacNhanCho.repaint();

                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(dialog, "Lỗi khi lưu vé và hóa đơn: " + ex.getMessage());
                    }


                }
            });

            dialog.setVisible(true); // Hiển thị cửa sổ
        } else if (e.getSource()==btnQuanLyKhachHang) {
            Frm_KhachHang frm_KhachHang = new Frm_KhachHang();
            frm_KhachHang.setVisible(true);
        } else if (e.getSource()==btnQuanLyKhuyenMai) {
            FrmKhuyenMai frm_KhuyenMai = new FrmKhuyenMai();
            frm_KhuyenMai.setVisible(true);
        } else if (e.getSource()==btnQuanLyChuyenTau) {
            JPanel_XacNhanCho.setVisible(false);
            lab_Title.setVisible(false);
            JPanel_BanVe.setVisible(false);
            // Clear Jpanel_Main
           // Clear existing components

            // Create a new instance of ChuyenTau (make sure it extends JPanel)


            // Thêm ChuyenTau vào jPanel_Main
            Jpanel_Main.add(chuyenTauPanel);
            Jpanel_Main.setVisible(true);
            // Cập nhật lại giao diện người dùng
            Jpanel_Main.revalidate(); // Cập nhật layout
            Jpanel_Main.repaint();    //
        } else if (e.getSource()==btnBanVe) {
            Jpanel_Main.remove(chuyenTauPanel);
            JPanel_XacNhanCho.setVisible(true);
            lab_Title.setVisible(true);
            JPanel_BanVe.setVisible(true);
            Jpanel_Main.revalidate(); // Cập nhật layout
            Jpanel_Main.repaint();
        }
    }
    private void updateTotalAmount(JLabel lblThanhTien,JTable table) {
        double totalAmount = 0;

        // Lặp qua từng hàng trong bảng để tính tổng thành tiền
        for (int row = 0; row < table.getRowCount(); row++) {
            double thanhTien = (double) table.getValueAt(row, 5); // Cột 5 là "Thành tiền"
            totalAmount += thanhTien;
        }

        // Cập nhật label với tổng thành tiền
        lblThanhTien.setText("Tổng thành tiền: " + totalAmount + " VND");
    }
    private String generateTicketCode(String maTau) {
        // Lấy thời gian hiện tại bao gồm ngày tháng và giờ phút giây
        LocalDateTime currentDateTime = LocalDateTime.now();

        // Nếu là ngày mới, đặt lại số đếm về 0
        if (!currentDateTime.toLocalDate().isEqual(lastDate)) {
            ticketCount = 0; // Đặt lại số vé
            lastDate = currentDateTime.toLocalDate(); // Cập nhật ngày cuối cùng
        }
        // Định dạng ngày tháng giờ phút giây theo "yyyyMMddHHmmss"
        String dateTimePart = currentDateTime.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));

        // Tăng số đếm nếu có nhiều vé trong cùng một giây (lên đến 1000)
        if (ticketCount >= 1000) {
            ticketCount = 1; // Đặt lại số đếm nếu vượt quá 1000
        } else {
            ticketCount++; // Tăng số đếm
        }

        // Tạo phần số đếm, đảm bảo có tối đa 3 chữ số
        String countPart = String.format("%03d", ticketCount);

        // Tạo mã vé với cấu trúc: mã tàu + ngày tháng giờ phút giây + số đếm
        return maTau + dateTimePart + "-" + countPart;
    }
    private String generateInvoiceCode() {
        LocalDate today = LocalDate.now();
        LocalTime now = LocalTime.now();
        int randomSuffix = (int) (Math.random() * 10000); // Tạo số ngẫu nhiên từ 0000 đến 9999
        return "HD" + today.toString().replace("-", "") + now.toString().replace(":", "").substring(0, 4) + String.format("%04d", randomSuffix);
    }
    private String generateCustomerCode() {
        LocalDateTime now = LocalDateTime.now();
        int randomSuffix = (int) (Math.random() * 10000); // Tạo số ngẫu nhiên từ 0000 đến 9999
        String datePart = now.format(DateTimeFormatter.ofPattern("yyyyMMddHHmm")); // Lấy ngày giờ hiện tại theo định dạng
        return "KH" + datePart + String.format("%04d", randomSuffix);
    }


}
