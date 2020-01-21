package com.soar.commons;

public class URLS {
	

	private static String managerService="www/manager-service/rest/CaseService";
	private static String coreService="/www/core-service/rest/LoginService";
	
	public static String managerServiceURL(String hostName,int port,String method) {
		return "https://"+hostName+":"+port+"/"+managerService+"/"+method;
	}
	
	public static String coreServiceURL(String hostName,int port,String method,String username,String password) {
		return "https://"+hostName+":"+port+"/"+coreService+"/"+method+"?login="+ username +"&password="+ password +"";
	}
	

}
