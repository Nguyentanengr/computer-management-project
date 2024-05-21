
package controller;


import javax.mail.*;
import javax.mail.internet.HeaderTokenizer;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class Gmailer {
    public static void sendOTPMail(String emailTo, String otp) {
        String emailFrom = "phamtannguyen16102004@gmail.com";
        String username = "phamtannguyen16102004@gmail.com";
        String password = "zjnnqbpukmojcfwe";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props,
                new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress((emailFrom)));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress((emailTo)));

            message.setSubject("Yêu cầu thay đổi mật khẩu");
            message.setText("Xin chào !\n"
                    + "\n"
                    + "Ai đó đã yêu cầu đặt lại mật khẩu cho tài khoản của bạn, nếu đây không phải là bạn, vui lòng bỏ qua email này.\n"
                    + "\n"
                    + "Sử dụng mã kích hoạt này để khôi phục mật khẩu của bạn: " + otp);

            Transport.send(message);
            System.out.println("gui thanh cong");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}