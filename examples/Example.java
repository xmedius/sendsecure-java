import com.xmedius.sendsecure.*;
import com.xmedius.sendsecure.helper.*;
import com.xmedius.sendsecure.exception.*;

import java.io.File;
import java.io.IOException;

public class Example {
	public static void main(String[] args) throws Exception {
		/**
		 * Example 1: Authentication (Retrieving API Token)
		 *
		 */
		try {
			String userToken = Client.getUserToken("deathstar", "darthvader", "d@Rk$1De");
			System.out.println(userToken);
		} catch (IOException | SendSecureException e) {
			throw e;
		}

		/**
		 * Example 2: SafeBox Creation
		 */
		try {
			String userEmail = "darthvader@empire.com";
			String token = "USER|1d495165-4953-4457-8b5b-4fcf801e621a";
			String enterpriseAccount = "deathstar";
			String endpoint = "https://portal.xmedius.com";

			Safebox safebox = new Safebox(userEmail);
			safebox.setSubject("Family matters");
			safebox.setMessage("Son, you will find attached the evidence.");

			Recipient recipient = new Recipient("lukeskywalker@rebels.com");
			safebox.getRecipients().add(recipient);

			Attachment attachment = new Attachment(new File("Birth_Certificate.pdf"), "application/pdf");
			safebox.getAttachments().add(attachment);

			Client client = new Client(token, enterpriseAccount, endpoint);
			SafeboxResponse response = client.submitSafebox(safebox);
			System.out.println(response.getGuid());
			System.out.println(response.getPreviewUrl());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
