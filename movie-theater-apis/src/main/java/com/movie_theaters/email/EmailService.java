package com.movie_theaters.email;

import com.movie_theaters.entity.*;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;
    public void sendEmail(String email, User user, String confirmationUrl){
        MimeMessage message = javaMailSender.createMimeMessage();
        try{
            MimeMessageHelper helper = new MimeMessageHelper(message,true);
            helper.setFrom("nguyenvanvi2952002@gmail.com");
            helper.setTo(email);
            helper.setSubject("Xác nhận đăng ký tài khoản!");

            String emailBody = "<html><body>"
                    + "<p>Chào " + user.getUsername() + ",</p>"
                    + "<p>Vui lòng nhấn vào nút bên dưới để xác nhận đăng ký tài khoản của bạn.</p>"
                    + "<p><strong>Thông tin tài khoản:</strong></p>"
                    + "<p><strong>Username:</strong> " + user.getUsername() + "</p>"
                    + "<p><strong>Password:</strong> " + user.getPassword() + "</p>" // Cẩn thận với việc gửi mật khẩu qua email
                    + "<p><a href=\"" + confirmationUrl + "\" style=\""
                    + "background-color: #4CAF50; color: white; padding: 15px 32px; text-align: center; "
                    + "text-decoration: none; display: inline-block; font-size: 16px; margin: 4px 2px; cursor: pointer; "
                    + "border-radius: 4px;\">Xác Nhận Đăng Ký</a></p>"
                    + "</body></html>";

            helper.setText(emailBody, true);
            javaMailSender.send(message);
        }catch (MessagingException ex){
            ex.printStackTrace();
        }
    }

    public void sendForgotPassword(String email, String code){
        MimeMessage message = javaMailSender.createMimeMessage();
        try{
            MimeMessageHelper helper = new MimeMessageHelper(message,true);
            helper.setFrom("nguyenvanvi2952002@gmail.com");
            helper.setTo(email);
            helper.setSubject("Thay đổi mật khẩu!");
            String emailBody = "<html><body>"
                    + "<p>Vui lòng nhập mã xác nhận sau đây để đặt lại mật khẩu của bạn:</p>"
                    + "<p><strong>Mã Xác Nhận:</strong><span style='font-size:30px; font-weight:bold;'>" + code + "</span></p>"
                    + "</body></html>";
            helper.setText(emailBody, true);
            javaMailSender.send(message);
        }catch (MessagingException ex){
            ex.printStackTrace();
        }
    }

    public void sendBill(String email, Bill bill, Schedule schedule, List<BillDetail> billDetails, List<BillFood> billFoods, Room room) {
        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom("nguyenvanvi2952002@gmail.com");
            helper.setTo(email);
            helper.setSubject("PCV cinemas! Hóa đơn.");

            // Tính tổng tiền vé
            double ticketPrice = schedule.getPrice();
            int seatCount = billDetails.size();
            double totalTicketPrice = ticketPrice * seatCount;

            // Tính tổng tiền đồ ăn
            double totalFoodPrice = billFoods.stream()
                    .mapToDouble(billFood -> billFood.getQuantity() * billFood.getFood().getPrice())
                    .sum();

            // Tính tổng tiền tất cả
            double totalAmount = totalTicketPrice + totalFoodPrice;

            // Tạo nội dung email với CSS inline
            StringBuilder emailBody = new StringBuilder("<html><body>");
            emailBody.append("<div style='width: 60%; margin: auto; padding: 10px; border: 1px solid #ddd; border-radius: 10px; background-color: #082159; color: #333;'>")
                    .append("<h1 style='text-align: center; color: rgb(211, 211, 78);'>HÓA ĐƠN</h1>")
                    .append("<hr style='border: 0; border-top: 2px solid #2c3e50; margin-bottom: 20px;'>")
                    .append("<div style='padding: 20px;'>")
                    .append("<p style='font-size: 16px; color: red; font-weight: bold;'><strong style='color: white;'>Mã Hóa Đơn:</strong> ").append(bill.getBillCode()).append("</p>")
                    .append("<p style='font-size: 16px;'><strong>Ngày tạo:</strong> ").append(formatDateTime(String.valueOf(bill.getCreatedDateTime()),"dd/MM/yyyy HH:mm:ss")).append("</p>")
                    .append("</div>")
                    .append("<hr style='border: 0; border-top: 2px solid #2c3e50; margin-top: 20px; margin-bottom: 20px;'>")
                    .append("<h2 style='color: rgb(211, 211, 78); text-align: center;'>THÔNG TIN VÉ</h2>")
                    .append("<div style='padding:20px; padding-top:0'>")
                    .append("<table style='width: 100%; border-collapse: collapse;'>")
                    .append("<tr>")
                    .append("<td style='width: 50%; vertical-align: top;'>")
                    .append("<p style='font-size: 16px;'><strong>Rạp:</strong> ").append(room.getCinema().getName()).append("</p>")
                    .append("<p style='font-size: 16px;'><strong>Phòng:</strong> ").append(room.getName()).append("</p>")
                    .append("<p style='font-size: 20px; font-weight: bold; color:red;'><strong style='font-size: 16px; color:white;'>Số Ghế:</strong> ").append(
                            billDetails.stream()
                                    .map(detail -> (detail.getSeat().getColumnName() + detail.getSeat().getRowName()))
                                    .collect(Collectors.joining(", "))
                    ).append("</p>")
                    .append("</td>")
                    .append("<td style='width: 50%; vertical-align: top;'>")
                    .append("<p style='font-size: 16px;'><strong>Tên Phim:</strong> ").append(schedule.getMovie().getName()).append("</p>")
                    .append("<p style='font-size: 16px;'><strong>Ngày Chiếu:</strong> ").append(schedule.getDate()).append("</p>")
                    .append("<p style='font-size: 16px;'><strong>Giờ Chiếu:</strong> ").append(schedule.getTime()).append("</p>")
                    .append("</td>")
                    .append("</tr>")
                    .append("</table>")
                    .append("<p style='font-size: 16px; color: rgb(211, 211, 78); font-weight:bold;'><strong style='color: white;'>TIỀN VÉ:</strong> ").append(formatCurrency(totalTicketPrice)).append("</p>")
                    .append("</div>")
                    .append("<hr style='border: 0; border-top: 2px solid #2c3e50; margin-bottom: 20px;'>")
                    .append("<h2 style='color: rgb(211, 211, 78); text-align: center; margin-bottom:20px;'>THÔNG TIN ĐỒ ĂN</h2>")
                    .append("<div style='padding:20px;'>")
                    .append("<table style='width: 100%; border-collapse: collapse;'>")
                    .append("<thead style='color: #ffffff;'><tr><th style='padding: 10px; border: 1px solid #ddd;'>Tên</th><th style='padding: 10px; border: 1px solid #ddd;'>Số Lượng</th><th style='padding: 10px; border: 1px solid #ddd;'>Đơn Giá(VND)</th><th style='padding: 10px; border: 1px solid #ddd;'>Tổng Tiền</th></tr></thead>")
                    .append("<tbody>");

            // Thêm các món ăn vào nội dung email
            for (BillFood billFood : billFoods) {
                Food food = billFood.getFood();
                double foodTotal = billFood.getQuantity() * food.getPrice();
                emailBody.append("<tr>")
                        .append("<td style='padding: 10px; border: 1px solid #ddd;'>").append(food.getName()).append("</td>")
                        .append("<td style='padding: 10px; border: 1px solid #ddd;'>").append(billFood.getQuantity()).append("</td>")
                        .append("<td style='padding: 10px; border: 1px solid #ddd;'>").append(formatCurrency(food.getPrice())).append("</td>")
                        .append("<td style='padding: 10px; border: 1px solid #ddd;'>").append(formatCurrency(foodTotal)).append("</td>")
                        .append("</tr>");
            }
            emailBody.append("</tbody></table>")
                    .append("<p style='font-size: 16px; color: rgb(211, 211, 78); font-weight:bold; margin-top:20px;'><strong style='color: white;'>TIỀN ĐỒ ĂN:</strong> ").append(formatCurrency(totalFoodPrice)).append("</p>")
                    .append("</div>")
                    .append("<hr style='border: 0; border-top: 2px solid #2c3e50; margin-bottom: 20px;'>")
                    .append("<p style='font-size: 18px; color: rgb(211, 211, 78); font-weight:bold; margin-left:20px;'><strong style='color: white;'>TỔNG TIỀN:</strong> ").append(formatCurrency(totalAmount)).append("</p>")
                    .append("<p style='text-align: center; font-size: 18px; color: rgb(211, 211, 78); margin-top: 30px; font-weight: bold;'>PCV Cinemas - Cảm ơn bạn đã sử dụng dịch vụ của chúng tôi!</p>")
                    .append("</div></body></html>");

            helper.setText(emailBody.toString(), true);
            javaMailSender.send(message);
        } catch (MessagingException ex) {
            ex.printStackTrace();
        }
    }

    // Hàm formatCurrency để định dạng giá
    private String formatCurrency(Double amount) {
        // Implement your currency formatting here
        return String.format("%,.0f", amount) + " VNĐ"; // Ví dụ định dạng tiền Việt Nam Đồng
    }

    public static String formatDateTime(String isoDateTime, String pattern) {
        // Phân tích chuỗi ngày giờ ISO 8601 thành đối tượng LocalDateTime
        LocalDateTime dateTime = LocalDateTime.parse(isoDateTime);

        // Tạo định dạng ngày giờ theo mẫu đã cho
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);

        // Định dạng đối tượng LocalDateTime thành chuỗi theo mẫu đã cho
        return dateTime.format(formatter);
    }
}
