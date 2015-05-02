package controllers;

import play.Play;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.Map;
import java.util.HashMap;

import com.twilio.sdk.resource.instance.Account;
import com.twilio.sdk.TwilioRestClient;
import com.twilio.sdk.TwilioRestException;
import com.twilio.sdk.resource.factory.MessageFactory;
import com.twilio.sdk.resource.instance.Message;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

public class SmsController extends Controller {

	public static Result messages(Long deliveryTime, String name) {

		final String twilioSid = Play.application().configuration()
				.getString("twilioSID");
		final String twilioToken = Play.application().configuration()
				.getString("twilioTOKEN");
		final String numberFrom = Play.application().configuration()
				.getString("numberFrom");

		TwilioRestClient client = new TwilioRestClient(twilioSid, twilioToken);

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("Body",
				"Amra please?! Hau ar ju? :) njam.ba" + name + deliveryTime));
		params.add(new BasicNameValuePair("To", "+38765695813"));
		params.add(new BasicNameValuePair("From", numberFrom));

		MessageFactory messageFactory = client.getAccount().getMessageFactory();
		Message message;
		try {
			message = messageFactory.create(params);
			System.out.println("message");
		} catch (TwilioRestException e) {
			System.out.println(e.getErrorMessage() + e.getErrorCode());
			e.printStackTrace();
		}
		return redirect("/");
	}
}
