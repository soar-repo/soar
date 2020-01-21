package com.soar.arcsight;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.logging.Logger;

import org.codehaus.plexus.util.ExceptionUtils;
import org.json.JSONObject;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.soar.commons.UnirestMethods;

public class TicketManagement {

	private static Logger logger = Logger.getGlobal();

	public JSONObject createResource(String hostName, String port, String device, String eventId)
			throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException, UnirestException {

		logger.info("Start!..Sending Ticket to the ArchSite..");
		String token = getToken();
		logger.info("token: " + token);

		String body = "{\n" + "    \"cas.insertResource\": {\n" + "        \"cas.authToken\": \"" + token + "\",\n"
				+ "        \"cas.resource\": {\n" + "            \"name\": \""
				+ new StringBuilder(device).append(new SimpleDateFormat().getCalendar().getTimeInMillis()) + "\",\n"
				+ "            \"stage\":\"INITIAL\",\n" + "            \"eventId\":\"" + eventId + "\"\n"
				+ "        },\n" + "        \"cas.parentId\":\"01000100017777777\"\n" + "    }\n" + "}";

		String result = UnirestMethods.POST(
				"https://stxmss-esm01.sattrix.corp:8443/www/manager-service/rest/CaseService/insertResource", null,
				body);
		logger.info("Success!..Ticket was Created on the Archsite..");
		return UnirestMethods.getResourceId(result);
	}

	public boolean updateResource(String hostName, String port, String ticketName, String resourceId)
			throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException, UnirestException {
		logger.info("Start!..Updating Ticket to the ArchSite..");

		JSONObject resource = new JSONObject();

		resource.put("name", ticketName.toString());
		resource.put("stage", "CLOSED");
		resource.put("resourceid", resourceId.toString());
		JSONObject data = new JSONObject();

		data.put("cas.authToken", getToken());
		data.put("cas.resource", resource);

		JSONObject json = new JSONObject();
		json.put("cas.update", data);
		logger.info("Request Body For Update :" + json.toString());
		try {
			String result = UnirestMethods.POST(
					"https://stxmss-esm01.sattrix.corp:8443/www/manager-service/rest/CaseService/update", null,
					json.toString());
			logger.info("Result For TIcket Updation " + result);
			logger.info("Success!..Updated Ticket on the ArchSite..");
			return true;
		} catch (Exception e) {
			logger.warning("Exception " + ExceptionUtils.getFullStackTrace(e));
			return false;
		}

	}

	public String getToken()
			throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException, UnirestException {
		logger.info("Genrating Token....");

		String result = UnirestMethods.GET(
				"https://stxmss-esm01.sattrix.corp:8443/www/core-service/rest/LoginService/login?login=socl1&password=arcESML1@123",
				null);
		if (result.isEmpty()) {
			logger.info("Opps!!- Something went wrong!...." + result);
			System.out.println(result);
			return null;
		} else {
			logger.info("Token Genrated!!.." + result);
			return UnirestMethods.getTokenFromXML(result);

		}
	}

}
