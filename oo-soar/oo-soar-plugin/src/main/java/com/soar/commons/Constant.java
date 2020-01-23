package com.soar.commons;

public class Constant {

	private static String userAgent = "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:45.0) Gecko/20100101 Firefox/45.0";
	private static String dbHost = "127.0.0.1";
	private static String dbPort = "3306";
	private static String dbUsername = "root";
	private static String dbPassword = "";
	private static String dbName="soar";

	public static String getUserAgent() {
		return userAgent;
	}

	public static String getDbHost() {
		return dbHost;
	}

	public static String getDbPort() {
		return dbPort;
	}

	public static String getDbUsername() {
		return dbUsername;
	}

	public static String getDbPassword() {
		return dbPassword;
	}

	public static String getDbName() {
		return dbName;
	}

}
