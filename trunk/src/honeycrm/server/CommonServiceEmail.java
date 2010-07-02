package honeycrm.server;

import java.io.UnsupportedEncodingException;
import java.util.Properties;
import java.util.logging.Logger;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class CommonServiceEmail extends AbstractCommonService {
	private static final Logger log = Logger.getLogger(CommonServiceEmail.class.getName());
	private static final String EMAIL_FROM = "ingo.jaeckel@googlemail.com"; // "honeycrm.feedback@honeyyycrm.appspot.com";
	private static final String EMAIL_TO = "ingo.jaeckel@googlemail.com"; // "honeycrm-users@googlegroups.com";
	private static final long serialVersionUID = 9119624817628593056L;

	public void feedback(final String message) {
		log.fine("Start sending mail..");

		Properties props = new Properties();
		Session session = Session.getDefaultInstance(props, null);
		Message msg = new MimeMessage(session);

		try {
			msg.setFrom(new InternetAddress(EMAIL_FROM, "HoneyCRM App"));
			msg.addRecipient(Message.RecipientType.TO, new InternetAddress(EMAIL_TO, "HoneyCRM Mailing List"));
			msg.setSubject("HoneyCRM Feedback E-Mail");
			msg.setText(message);
			Transport.send(msg);

			log.info("E-Mail has been send.");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}
}
