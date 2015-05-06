package controllers;

import play.Logger;
import play.Play;
import play.mvc.Controller;
import play.mvc.Result;

import com.twilio.sdk.TwilioRestClient;
import com.twilio.sdk.TwilioRestException;
import com.twilio.sdk.resource.factory.MessageFactory;
import com.twilio.sdk.resource.instance.Message;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

public class SmsController extends Controller {

	public static final String twilioSid = Play.application().configuration()
			.getString("twilioSID");
	public static final String twilioToken = Play.application().configuration()
			.getString("twilioTOKEN");
	public static final String numberFrom = Play.application().configuration()
			.getString("numberFrom");

	public static Result messages(Long deliveryTime, String name) {

		TwilioRestClient client = new TwilioRestClient(twilioSid, twilioToken);

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("Body",
				"Thank you, your order will be delivered in: " + deliveryTime
						+ " minutes," + " from Restaurant: " + name));
		params.add(new BasicNameValuePair("To", "+38765695813"));
		params.add(new BasicNameValuePair("From", numberFrom));

		MessageFactory messageFactory = client.getAccount().getMessageFactory();
		Message message;
		try {
			message = messageFactory.create(params);
			System.out.println("message");
		} catch (TwilioRestException e) {
			Logger.info("Order delivery sms: " + e.getErrorCode());
			e.printStackTrace();
		}
		return redirect("/");
	}

	public static Result confirmNumber(String pin) {
		TwilioRestClient client = new TwilioRestClient(twilioSid, twilioToken);

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("Body", "Verification PIN is: " + pin));
		params.add(new BasicNameValuePair("To", "+38765695813"));
		params.add(new BasicNameValuePair("From", numberFrom));

		MessageFactory messageFactory = client.getAccount().getMessageFactory();
		Message message;
		try {
			message = messageFactory.create(params);
			System.out.println("message");
		} catch (TwilioRestException e) {
			Logger.info("Pin confirmation via sms: " + e.getErrorCode());
			e.printStackTrace();
		}
		return redirect("/");
	}
}