import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Properties;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Store;

public class POP3Client {

    public static void saveInputToFile(OutputStream fileContent, String fileName) throws Exception {
	File file = new File("GMAIL_BULKMAIL" + fileName);
	FileOutputStream fos = (FileOutputStream) fileContent;
	fos = new FileOutputStream(file);
	int b;
	// while ((b = fileContent.read()) != -1) {
	// fos.write(b);
	// }

	fos.flush();
	fos.close();
    }

    public static void main(String[] args) {

	Properties props = new Properties();

	String host = "GMAIL_POP3_URL";// "utopia.poly.edu";
	String username = "GMAIL_USR";
	String password = "GMAIL_PSW";
	String provider = "GMAIL_PROVIDER";

	String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";

	props.setProperty("mail.pop3.socketFactory.class", SSL_FACTORY);
	props.setProperty("mail.pop3.socketFactory.fallback", "false");
	props.setProperty("mail.pop3.port", "995");
	props.setProperty("mail.pop3.socketFactory.port", "995");
	try {

	    // Connect to the POP3 server
	    Session session = Session.getDefaultInstance(props, null);
	    Store store = session.getStore(provider);
	    store.connect(host, 995, username, password);

	    // Open the folder
	    Folder inbox = store.getFolder("INBOX");
	    if (inbox == null) {
		System.out.println("No INBOX");
		System.exit(1);
	    }
	    inbox.open(Folder.READ_ONLY);

	    // Get the messages from the server
	    Message[] messages = inbox.getMessages();
	    for (int i = 0; i < messages.length; i++) {
		System.out.println("------------ Message " + (i + 1) + " ------------");

		FileOutputStream fos = new FileOutputStream("GMAIL_BULKMAIL" + messages[i].getHeader("Message-ID")[0].toString());
		messages[i].writeTo(fos);
		fos.flush();
		fos.close();
		// saveInputToFile(fos, fileName);
		// saveInputToFile(
		// //new
		// StringBufferInputStream(messages[i].getAllHeaders().toString()),
		// messages[i].getHeader("Message-ID")[0].toString());
	    }

	    // Close the connection
	    // but don't remove the messages from the server
	    inbox.close(false);
	    store.close();

	} catch (Exception ex) {
	    ex.printStackTrace();
	}
    }
}
