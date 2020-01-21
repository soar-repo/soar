package com.soar.actions;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.codehaus.plexus.util.ExceptionUtils;
import org.json.JSONObject;

import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import com.hp.oo.sdk.content.constants.OutputNames;
import com.hp.oo.sdk.content.constants.ResponseNames;
import com.hp.oo.sdk.content.constants.ReturnCodes;
import com.hp.oo.sdk.content.plugin.ActionMetadata.MatchType;
import com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.soar.arcsight.TicketManagement;
import com.soar.firewalls.FirewallCommon;
import com.soar.ips.IPHandler;
import com.soar.utils.Log;

public class SoarActions {

	private static Logger logger = Logger.getGlobal();
	private static Logger methoodLog = Logger.getLogger("Methods");

	@Action(name = "reputationCheckIPBasedActions", description = "Check Ip on Popular Websites", outputs = {
			@Output(OutputNames.RETURN_RESULT), @Output("result_message"), @Output("IPCounter") }, responses = {
					@Response(text = ResponseNames.SUCCESS, field = OutputNames.RETURN_RESULT, value = ReturnCodes.RETURN_CODE_SUCCESS, matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.RESOLVED),
					@Response(text = ResponseNames.FAILURE, field = OutputNames.RETURN_RESULT, value = ReturnCodes.RETURN_CODE_FAILURE, matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.ERROR) })
	public Map<String, String> reputationCheckIPBasedActions(@Param("IpForChecking") String badIp,
			@Param("ProxyIP") String proxyIP, @Param("ProxyPort") String proxyPort) {
		Map<String, String> resultMap = new HashMap<String, String>();

		methoodLog.addHandler(Log.setupLoggerForStatus());
		logger.addHandler(Log.setupLoggerForDeveloper());

		if (badIp.isEmpty() || proxyIP.isEmpty() || proxyIP.isEmpty()) {
			logger.info("checkIpReputation method's perameter are null.. ");
			resultMap.put("result_message", "Fileds values are required!!");
			resultMap.put(OutputNames.RETURN_RESULT, ReturnCodes.RETURN_CODE_FAILURE);
		} else {
			methoodLog.info("*************STEP-1*****************");
			methoodLog.info("Start Check IP Reputation of " + badIp);
			logger.info("Methods execution start");
			int badIpCounter = 0;
			IPHandler ipHandler = new IPHandler();
			try {
				badIpCounter = ipHandler.checkIpReputation(badIp, proxyIP, Integer.parseInt(proxyPort));
				resultMap.put("result_message", "success");
				resultMap.put("IPCounter", String.valueOf(badIpCounter));
				resultMap.put(OutputNames.RETURN_RESULT, ReturnCodes.RETURN_CODE_SUCCESS);
				logger.info("BadIpCounter : " + badIpCounter);
			} catch (Exception e) {
				resultMap.put("result_message", "failed" + e.getMessage());
				resultMap.put(OutputNames.RETURN_RESULT, ReturnCodes.RETURN_CODE_FAILURE);
				logger.info("Opps !! .. Exception " + e.getMessage());
			}
		}
		methoodLog.info("Stop Check IP Reputation");
		return resultMap;
	}

	@Action(name = "makingDecisionToBlockIP", description = "Making Decision To block IP", outputs = {
			@Output(OutputNames.RETURN_RESULT), @Output("result_message"), @Output("decision") }, responses = {
					@Response(text = ResponseNames.SUCCESS, field = OutputNames.RETURN_RESULT, value = ReturnCodes.RETURN_CODE_SUCCESS, matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.RESOLVED),
					@Response(text = ResponseNames.FAILURE, field = OutputNames.RETURN_RESULT, value = ReturnCodes.RETURN_CODE_FAILURE, matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.ERROR) })
	public Map<String, String> makingDecisionToBlockIP(@Param("BadIPCounter") String badIpCounter,
			@Param("BadIP") String badIp) {
		Map<String, String> resultMap = new HashMap<String, String>();
		if (badIp.isEmpty() || badIpCounter.isEmpty()) {
			logger.info("makingDecisionToBlockIP method's perameter are null.. ");
			resultMap.put("result_message", "Fileds values are required!!");
			resultMap.put(OutputNames.RETURN_RESULT, ReturnCodes.RETURN_CODE_FAILURE);
		} else {
			methoodLog.info("*************STEP-2*****************");
			methoodLog.info("Start Decision Making Proccess");
			methoodLog.info(badIp + " Listed in " + badIpCounter + " websites");
			methoodLog.info("Stop Decision Making Proccess");
			logger.info("Bad IP listed in " + badIpCounter + "websites");
			if (Integer.parseInt(badIpCounter) > 0) {
				logger.info("Bad Ip Count greter then 5");
				resultMap.put("result_message", "success");
				resultMap.put(OutputNames.RETURN_RESULT, ReturnCodes.RETURN_CODE_SUCCESS);
				resultMap.put("decision", "true");
			} else {
				logger.info("Bad Ip Count less then 5");
				resultMap.put("result_message", "failure");
				resultMap.put(OutputNames.RETURN_RESULT, ReturnCodes.RETURN_CODE_FAILURE);
				resultMap.put("decision", "false");
			}
		}
		return resultMap;
	}

	@Action(name = "ticketCreationOnArcSight", description = "Ticket Raise on ArcSight", outputs = {
			@Output(OutputNames.RETURN_RESULT), @Output("result_message"), @Output("ticket_name"),
			@Output("resource_id") }, responses = {
					@Response(text = ResponseNames.SUCCESS, field = OutputNames.RETURN_RESULT, value = ReturnCodes.RETURN_CODE_SUCCESS, matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.RESOLVED),
					@Response(text = ResponseNames.FAILURE, field = OutputNames.RETURN_RESULT, value = ReturnCodes.RETURN_CODE_FAILURE, matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.ERROR) })
	public Map<String, String> ticketCreationOnArcSight(@Param("ArcSightHostIP") String arcsightHostIP,
			@Param("ArcSightPort") String arcSightPort, @Param("DeviceName") String deviceName,
			@Param("eventId") String eventId, @Param("ArcSightUsername") String arcsightUsername,
			@Param("ArcSightPassword") String arcsightPassword)
			throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException, UnirestException {

		Map<String, String> resultMap = new HashMap<String, String>();
		if (arcsightHostIP.isEmpty() || arcSightPort.isEmpty() || deviceName.isEmpty() || eventId.isEmpty()
				|| arcsightUsername.isEmpty() || arcsightPassword.isEmpty()) {
			logger.info("ticketCreationOnArcSight method's perameter are null.. ");
			resultMap.put("result_message", "Fileds values are required!!");
			resultMap.put(OutputNames.RETURN_RESULT, ReturnCodes.RETURN_CODE_FAILURE);
		} else {
			methoodLog.info("*************STEP-3*****************");
			methoodLog.info("Creating Logs to ArcSight...");
			JSONObject jsonObject = new JSONObject();
			TicketManagement tickets = new TicketManagement();
			try {
				jsonObject = tickets.createResource(arcsightHostIP, arcSightPort, deviceName, eventId, arcsightUsername,
						arcsightPassword);
				JSONObject data = new JSONObject();
				data.put("ticket_name", jsonObject.getJSONObject("ns4:return").getString("name"));
				data.put("resource_id", jsonObject.getJSONObject("ns4:return").getString("resourceid"));

				if (jsonObject.isEmpty()) {
					resultMap.put("result_message", "failed");
					resultMap.put(OutputNames.RETURN_RESULT, ReturnCodes.RETURN_CODE_FAILURE);
					methoodLog.info("Created logs to ArcSight..:= False");
				} else {
					resultMap.put("result_message", "Ticket Created");
					resultMap.put(OutputNames.RETURN_RESULT, ReturnCodes.RETURN_CODE_SUCCESS);
					resultMap.put("ticket_name", jsonObject.getJSONObject("ns4:return").getString("name"));
					resultMap.put("resource_id", jsonObject.getJSONObject("ns4:return").getString("resourceid"));
					methoodLog.info("Created logs to ArcSight..:= True");
				}
			} catch (Exception e) {
				resultMap.put("result_message", "Failed");
				resultMap.put(OutputNames.RETURN_RESULT, ReturnCodes.RETURN_CODE_FAILURE);
				logger.info("Opps !! .. Exception " + ExceptionUtils.getFullStackTrace(e));

			}
		}
		return resultMap;
	}

	@Action(name = "blockIpOnFirewall", description = "Block IP on Firewall", outputs = {
			@Output(OutputNames.RETURN_RESULT) }, responses = {
					@Response(text = ResponseNames.SUCCESS, field = OutputNames.RETURN_RESULT, value = ReturnCodes.RETURN_CODE_SUCCESS, matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.RESOLVED),
					@Response(text = ResponseNames.FAILURE, field = OutputNames.RETURN_RESULT, value = ReturnCodes.RETURN_CODE_FAILURE, matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.ERROR) })
	public Map<String, String> blockIpOnFirewall(@Param("BadIP") String badIp, @Param("HostIP") String hostName,
			@Param("DeviceName") String deviceId, @Param("FirewallUsername") String username,
			@Param("FirewallPassword") String password, @Param("Command") String command)
			throws SecurityException, IOException {
		Map<String, String> resultMap = new HashMap<String, String>();
		if (badIp.isEmpty() || hostName.isEmpty() || deviceId.isEmpty() || username.isEmpty() || password.isEmpty()
				|| command.isEmpty()) {
			logger.info("blockIpOnFirewall method's perameter are null.. ");
			resultMap.put("result_message", "Fileds values are required!!");
			resultMap.put(OutputNames.RETURN_RESULT, ReturnCodes.RETURN_CODE_FAILURE);
		} else {
			methoodLog.info("*************STEP-4*****************");
			methoodLog.info("Bad IP count greter then 5");
			methoodLog.info(badIp + "listed in 5 website");
			FirewallCommon common = new FirewallCommon();
			boolean valid = false;
			switch (deviceId) {
			case "Pfsense":
				methoodLog.info("Signin into PFSense..");
				logger.info("Start Device ID PFSense");
				resultMap.put(OutputNames.RETURN_RESULT, ReturnCodes.RETURN_CODE_SUCCESS);
				valid = common.blockIP(hostName, username, password, command);
				if (valid) {
					methoodLog.info("IP Successfully blocked on Firewall");
					resultMap.put("result_message", "Success");
				} else {
					methoodLog.info("IP Failed blocked on Firewall");
					resultMap.put("result_message", "failed");
				}

				logger.info("End Device ID PFSense");

				break;

			case "Fortigate":
				methoodLog.info("Signin into Fortigate..");
				logger.info("Start Device ID Fortigate");
				resultMap.put(OutputNames.RETURN_RESULT, ReturnCodes.RETURN_CODE_SUCCESS);
				valid = common.blockIP(hostName, username, password, command);
				if (valid) {
					methoodLog.info("IP Successfully blocked on Firewall");
					resultMap.put("result_message", "Success");
				} else {
					methoodLog.info("IP Failed blocked on Firewall");
					resultMap.put("result_message", "failed");
				}

				logger.info("End Device ID Fortigate");
				break;

			default:
				methoodLog.info("Unable to found device");
				resultMap.put(OutputNames.RETURN_RESULT, ReturnCodes.RETURN_CODE_SUCCESS);
				resultMap.put("result_message", "Device not found");
				logger.info("End Device ID unknown");
				break;
			}
		}
		return resultMap;
	}

	@Action(name = "updateTicketOnArcSight", description = "Update ticket on arcsight", outputs = {
			@Output(OutputNames.RETURN_RESULT) }, responses = {
					@Response(text = ResponseNames.SUCCESS, field = OutputNames.RETURN_RESULT, value = ReturnCodes.RETURN_CODE_SUCCESS, matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.RESOLVED),
					@Response(text = ResponseNames.FAILURE, field = OutputNames.RETURN_RESULT, value = ReturnCodes.RETURN_CODE_FAILURE, matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.ERROR) })
	public Map<String, String> updateTicketOnArcSight(@Param("ArcSightHostIP") String arcsightHostIP,
			@Param("ArcSightPort") String arcSightPort, @Param("ticketName") String ticketName,
			@Param("resourceId") String resourceId, @Param("ArcSightUsername") String arcsightUsername,
			@Param("ArcSightPassword") String arcsightPassword) {
		Map<String, String> resultMap = new HashMap<String, String>();

		if (arcsightHostIP.isEmpty() || arcSightPort.isEmpty() || ticketName.isEmpty() || resourceId.isEmpty()
				|| arcsightUsername.isEmpty() || arcsightPassword.isEmpty()) {
			logger.info("updateTicketOnArcSight method's perameter are null.. ");
			resultMap.put("result_message", "Fileds values are required!!");
			resultMap.put(OutputNames.RETURN_RESULT, ReturnCodes.RETURN_CODE_FAILURE);
		} else {
			methoodLog.info("*************STEP-5*****************");
			methoodLog.info("Updating Logs to ArcSight...");
			TicketManagement tickets = new TicketManagement();
			try {

				boolean result = tickets.updateResource(arcsightHostIP, arcSightPort, ticketName, resourceId,
						arcsightUsername, arcsightPassword);
				logger.info("Update Result : " + result);
				if (result) {
					resultMap.put("result_message", "true");
					resultMap.put(OutputNames.RETURN_RESULT, ReturnCodes.RETURN_CODE_SUCCESS);
					methoodLog.info("Updated logs to ArcSight..:= True");
				} else {
					resultMap.put("result_message", "failed");
					resultMap.put(OutputNames.RETURN_RESULT, ReturnCodes.RETURN_CODE_FAILURE);
					methoodLog.info("Updated logs to ArcSight..:= False");
				}
			} catch (Exception e) {
				resultMap.put("result_message", "failed");
				resultMap.put(OutputNames.RETURN_RESULT, ReturnCodes.RETURN_CODE_FAILURE);
				methoodLog.info("Updated logs to ArcSight..:= False " + e.getMessage());
				logger.info("Opps !! .. Exception " + ExceptionUtils.getFullStackTrace(e));
			}
		}
		return resultMap;
	}

}
