import java.awt.Color;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class SMSSending {
	public void sendSMS(String phoneNumber, String message) throws IOException {
		String requestUrl = "https://smsmisr.com/api/webapi/?Username=" + "biSjapU0"
				+ "&password=" + "xaTjNacsJn" + "&language=1&sender="
				+ URLEncoder.encode("HealthAlert", "UTF-8") + "&Mobile=" + phoneNumber
				+ "&message=" + URLEncoder.encode(message, "UTF-8");

		URL url = new URL(requestUrl);
		
		HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
		  httpCon.setDoOutput(true);
		  httpCon.setRequestMethod("POST");
		  OutputStreamWriter out = new OutputStreamWriter(
		      httpCon.getOutputStream());
		  System.out.println(httpCon.getResponseCode());
		  System.out.println(httpCon.getResponseMessage());
		  out.close();
		  System.out.println("SMS Sent Successfully");
		  Pop p = new Pop("SMS sent successfully",Color.green);
		
	}
	public static void main(String[] args) throws IOException {
		SMSSending sms = new SMSSending();
		sms.sendSMS("201287926744", "Test the message system");
	}
}
