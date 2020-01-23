package com.soar.ips;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import java.net.InetSocketAddress;
import java.net.Proxy;
import com.soar.commons.Constant;

public class IPHandler {
	private static Logger logger = Logger.getGlobal();
	/*
	 * @version: 1.0.0
	 * 
	 * @Purpose: The purpose behind this method is to check Ip whether it's bad or
	 * good!!..
	 */

	public int checkIpReputation(String badIp, String proxyIp, int proxyPort) throws SecurityException, IOException {

		Map<Integer, String[]> urls = null;
		int ipCounter = 0;
		System.out.println("Request proccesing....");

		logger.log(Level.INFO, "Requesting to find URL");
		urls = getUrls();
		for (int i = 0; i < urls.size(); i++) {
			String[] single = urls.get(i + 1);
			try {
				if (checkIp(single[1], single[0], badIp, proxyIp, proxyPort)) {
					ipCounter++;
				}
			} catch (Exception e) {
				System.out.println(e.getMessage());
				logger.log(Level.WARNING, e.getMessage());
				continue;
			}
		}
		System.out.println("Request procced....");
		return ipCounter;
	}

	/*
	 * @version: 1.0.0
	 * 
	 * @Purpose: The purpose behind this method is to store IP databases list.
	 */
	private static Map<Integer, String[]> getUrls() throws SecurityException, IOException {

		Map<Integer, String[]> urls = null;
		urls = new HashMap<>();
		logger.log(Level.INFO, "Get All the URL From The Internet Strats!!...");
		urls.put(1, new String[] { "AlienVault", "http://reputation.alienvault.com/reputation.data" });
		urls.put(2, new String[] { "BlocklistDE", "http://www.blocklist.de/lists/bruteforcelogin.txt" });
		urls.put(3,
				new String[] { "Dragon Research Group - VNC", "http://dragonresearchgroup.org/insight/vncprobe.txt" });
		urls.put(4, new String[] { "NoThink Malware", "http://www.nothink.org/blacklist/blacklist_malware_http.txt" });
		urls.put(5, new String[] { "antispam.imp.ch", "http://antispam.imp.ch/spamlist" });
		urls.put(6, new String[] { "MalWareBytes", "http://hosts-file.net/rss.asp" });
		urls.put(7, new String[] { "Spamhaus DROP", "https://www.spamhaus.org/drop/drop.txt" });
		urls.put(8, new String[] { "Spamhaus EDROP", "https://www.spamhaus.org/drop/edrop.txt" });
		logger.log(Level.INFO, "All URLS Fetched!!...");

		return urls;
	}

	/*
	 * @Version: 1.0.0
	 * 
	 * @Purpose: The purpose behind this method is to check Ip form perticuler url
	 * stored in getURL method.
	 */
	private boolean checkIp(String url, String name, String badIp, String proxyIp, int proxyPort) throws IOException {

		logger.log(Level.INFO, "Searching for " + badIp + "...");
		try {
			org.jsoup.Connection.Response reponse = null;
			reponse = Jsoup.connect(url).header("User-Agent", Constant.getUserAgent()).ignoreContentType(true)
					.ignoreHttpErrors(true)
					.proxy(new Proxy(Proxy.Type.HTTP, InetSocketAddress.createUnresolved(proxyIp, proxyPort)))
					.timeout(0).execute();
			if (Pattern.compile(badIp).matcher(reponse.body().toString()).find()) {
				logger.log(Level.INFO, badIp + " is listed on " + name);
				return true;
			} else {
				logger.log(Level.INFO, badIp + " is not listed on " + name);
				return false;
			}
		} catch (Exception e) {
			logger.log(Level.WARNING, e.toString());
			return false;
		}
	}

}
