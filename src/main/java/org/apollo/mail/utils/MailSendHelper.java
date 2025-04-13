package org.apollo.mail.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * @author liuf
 * @date 2022年06月10日 9:39
 */
public class MailSendHelper {
    private static Logger log = LoggerFactory.getLogger(MailSendHelper.class);

    /**
     * 发送邮件
     * @param subject 邮件主题
     * @param content 邮件内容
     * @param toAddress 收件人地址
     * @param fromAddress 发件人地址
     * @param password 发件人密码
     * @param host SMTP服务器地址
     * @param port SMTP服务器端口
     */
    public static void sendMail(String subject, String content, String toAddress, String fromAddress, String password, String host, int port) {
        try {
            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.host", host);
            props.put("mail.smtp.port", port);
            props.put("mail.smtp.starttls.enable", "true");

            Session session = Session.getInstance(props, new javax.mail.Authenticator() {
                protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
                    return new javax.mail.PasswordAuthentication(fromAddress, password);
                }
            });

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromAddress));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toAddress));
            message.setSubject(subject);
            message.setText(content);

            Transport.send(message);

            log.info("邮件发送成功：{}", subject);
        } catch (Exception e) {
            log.error("邮件发送失败：{}", e.getMessage());
            throw new RuntimeException("邮件发送失败", e);
        }
    }
}