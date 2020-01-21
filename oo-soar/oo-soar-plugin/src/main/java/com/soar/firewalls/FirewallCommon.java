package com.soar.firewalls;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import com.pastdev.jsch.DefaultSessionFactory;
import com.pastdev.jsch.command.CommandRunner;

public class FirewallCommon {

	private static Logger logger = Logger.getGlobal();

	public boolean blockIP(String hostName, String username, String password, String command)
			throws SecurityException, IOException {

		CommandRunner runner = null;
		try {
			logger.info("Init TO Logged in Firewall");
			DefaultSessionFactory sessionFactory = new DefaultSessionFactory(username, hostName, 22);
			Map<String, String> props = new HashMap<String, String>();
			props.put("StrictHostKeyChecking", "no");

			sessionFactory.setConfig(props);
			logger.info("Trying to logged in ");

			// sessionFactory.setProxy(new ProxyHTTP("192.168.6.11", 8080));
			sessionFactory.setPassword(password);
			runner = new CommandRunner(sessionFactory);
			logger.info("Success to logged in ");
			CommandRunner.ExecuteResult result = runner.execute(command);
			logger.info("Connected... " + result.getStdout());
			if (result.getStderr().isEmpty()) {
				System.out.println(result.getStdout());
				logger.info(result.getStdout());
				return true;
			} else {
				System.out.println(result.getStderr());
				logger.info(result.getStderr());
				return true;
			}
		} catch (Exception e) {
			logger.info(e.getMessage());
			System.out.println("Opps!!  Something went wrong!!..");
			System.out.println(e.getMessage());
			return false;
		} finally {
			logger.info("Attemting finnaly block");
			runner.close();

		}
	}

}
