package com.soar.commons;

import java.util.regex.Pattern;

public class Validation {

	private static final Pattern IPV4_PATTERN = Pattern.compile("^(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})$");
	private static final Pattern PORT_PATTERN = Pattern.compile("[0-9]+");
	private static final Pattern PARAMS_PATTERN = Pattern.compile("^[A-Za-z\\s]+$");
	private static final Pattern USERNAME_PATTERN=Pattern.compile("^[a-z0-9_-]{3,16}$");

	public static boolean isValidIP(String ip) {
		System.out.println(ip);
		System.out.println(IPV4_PATTERN.matcher(ip).matches());
		return IPV4_PATTERN.matcher(ip).matches();
	}

	public static boolean isValidPORT(String port) {
		System.out.println(port);
		System.out.println(PORT_PATTERN.matcher(port).matches());
		return PORT_PATTERN.matcher(port).matches();
	}

	public static boolean isValidPARAMS(String params) {
		System.out.println(params);
		System.out.println(PARAMS_PATTERN.matcher(params).matches());
		return PARAMS_PATTERN.matcher(params).matches();
	}
	
	public static boolean isValidUSERNAME(String username) {
		System.out.println(username);
		System.out.println(USERNAME_PATTERN.matcher(username).matches());
		return USERNAME_PATTERN.matcher(username).matches();
	}
	
	public static boolean isValidPASSWORD(String password) {
		System.out.println(password);
		System.out.println(PORT_PATTERN.matcher(password).matches());
		return PORT_PATTERN.matcher(password).matches();
	}
	
	public static boolean isValidNUMBER(String params) {
		System.out.println(params);
		System.out.println(PORT_PATTERN.matcher(params).matches());
		return PORT_PATTERN.matcher(params).matches();
	}

}
