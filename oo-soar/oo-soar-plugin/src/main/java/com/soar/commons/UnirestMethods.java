package com.soar.commons;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.SSLContext;

import org.apache.http.client.HttpClient;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.json.JSONObject;
import org.json.XML;

import com.mashape.unirest.http.Headers;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

public class UnirestMethods {

	public static String GET(String url, Headers headers)
			throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException, UnirestException {

		SSLContext sslcontext = SSLContexts.custom()
				.loadTrustMaterial(null, (TrustStrategy) new TrustSelfSignedStrategy()).build();

		SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext);
		CloseableHttpClient httpclient = HttpClients.custom()
				.setSSLSocketFactory((LayeredConnectionSocketFactory) sslsf).build();
		Unirest.setHttpClient((HttpClient) httpclient);

		HttpResponse<String> response = Unirest.get(url).header("Content-Type", "application/json")
				.header("alt", "json").asString();
		System.out.println(response.getStatus());
		return response.getBody().toString();
	}

	public static String POST(String url, Headers headers, String json)
			throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException, UnirestException {

		SSLContext sslcontext = SSLContexts.custom()
				.loadTrustMaterial(null, (TrustStrategy) new TrustSelfSignedStrategy()).build();

		SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext);
		CloseableHttpClient httpclient = HttpClients.custom()
				.setSSLSocketFactory((LayeredConnectionSocketFactory) sslsf).build();
		Unirest.setHttpClient((HttpClient) httpclient);

		HttpResponse<String> response = Unirest.post(url).header("Content-Type", "application/json")
				.header("alt", "json").body(json).asString();
		System.out.println(response.getStatus());
		return response.getBody().toString();
	}

	public static String getTokenFromXML(String xml) {

		return XML.toJSONObject(xml).getJSONObject("ns3:loginResponse").get("ns3:return").toString();
	}

	public static String getPerentIdFromXML(String xml) {

		return XML.toJSONObject(xml).getJSONObject("ns4:getSystemCasesGroupIDResponse").get("ns4:return").toString();
	}

	public static JSONObject getResourceId(String xml) {

		return XML.toJSONObject(xml).getJSONObject("ns4:insertResourceResponse");
	}

	public static String getParentIdByToken(String hostName, String port, String token)
			throws UnirestException, KeyManagementException, NoSuchAlgorithmException, KeyStoreException {

		SSLContext sslcontext = SSLContexts.custom()
				.loadTrustMaterial(null, (TrustStrategy) new TrustSelfSignedStrategy()).build();

		SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext);
		CloseableHttpClient httpclient = HttpClients.custom()
				.setSSLSocketFactory((LayeredConnectionSocketFactory) sslsf).build();
		Unirest.setHttpClient((HttpClient) httpclient);

		HttpResponse<String> response = Unirest.post(
				"https://"+ hostName +":"+ port +"/www/manager-service/rest/CaseService/getSystemCasesGroupID")
				.header("Content-Type", "application/json").header("alt", "json")
				.body("{\n  \"cas.getSystemCasesGroupID\": {\n    \"cas.authToken\":\"" + token + "\"\n    \n  }\n}")
				.asString();
		return response.getBody().toString();

	}

}
