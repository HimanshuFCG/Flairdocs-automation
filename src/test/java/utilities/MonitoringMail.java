package utilities;

import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;

public class MonitoringMail {
    public void sendMail(String server, String from, String to, String subject, String messageBody)
            throws AddressException, MessagingException {

        Properties props = new Properties();
        props.put("mail.smtp.host", server);
        props.put("mail.smtp.port", "25");  // Ya jo port ho

        Session session = Session.getDefaultInstance(props, null);
        Message message = new MimeMessage(session);

        message.setFrom(new InternetAddress(from));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
        message.setSubject(subject);
        message.setText(messageBody);

        Transport.send(message);
    }
}