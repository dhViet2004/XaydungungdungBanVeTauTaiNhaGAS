package Entity;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Properties;

public class EmailSender {

    // Phương thức tạo vé HTML và gửi email
    public static void generateAndSendTicketEmail(List<TicketDetails> ticketDetailsList, List<ChiTietHoaDon> chiTietHoaDonList, String toEmail, String subject) {
        // Tạo file HTML cho vé
        String htmlFileName = "ticket.html";  // Tên file HTML sẽ được tạo
        TicketHTMLGenerator.generateTicketHtml(htmlFileName, ticketDetailsList, chiTietHoaDonList);

        // Đọc nội dung file HTML
        String htmlContent = "";
        try {
            htmlContent = new String(Files.readAllBytes(Paths.get(htmlFileName)));
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        // Gửi email với nội dung HTML
        boolean emailSent = sendEmail(toEmail, subject, htmlContent);

        // Xóa file HTML sau khi gửi email
        if (emailSent) {
            try {
                Files.deleteIfExists(Paths.get(htmlFileName));
                System.out.println("Đã xóa file HTML sau khi gửi email.");
            } catch (IOException e) {
                System.err.println("Không thể xóa file HTML: " + e.getMessage());
            }
        }
    }

    // Phương thức gửi email
    public static boolean sendEmail(String to, String subject, String htmlContent) {
        String host = "smtp.gmail.com";
        String from = "duongsatlachong@gmail.com";
        String password = "gdrnsvkmtuiyrdxw";  // Cân nhắc sử dụng biến môi trường thay vì lưu mật khẩu trực tiếp

        Properties properties = System.getProperties();
        properties.setProperty("mail.smtp.host", host);
        properties.setProperty("mail.smtp.port", "587");
        properties.setProperty("mail.smtp.auth", "true");
        properties.setProperty("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.ssl.protocols", "TLSv1.2");

        Session session = Session.getDefaultInstance(properties, new javax.mail.Authenticator() {
            protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
                return new javax.mail.PasswordAuthentication(from, password);
            }
        });

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setSubject(subject);

            // Set the content of the email as HTML
            message.setContent(htmlContent, "text/html; charset=UTF-8");

            Transport.send(message);
            System.out.println("Gửi email thành công...");
            return true;
        } catch (MessagingException mex) {
            mex.printStackTrace();
            return false;
        }
    }
    // Phương thức gửi mật khẩu qua email
    public static boolean sendPasswordEmail(String toEmail, String password) {
        String subject = "Khôi phục mật khẩu";
        String htmlContent = "<p>Chào bạn,</p>" +
                "<p>Mật khẩu của bạn là: <strong>" + password + "</strong></p>" +
                "<p>Vui lòng bảo mật mật khẩu của bạn và không chia sẻ với người khác.</p>" +
                "<p>Trân trọng,<br>Đội ngũ hỗ trợ</p>";

        return sendEmail(toEmail, subject, htmlContent);
    }
    public static void main(String[] args) {
        // Ví dụ test
    }
}