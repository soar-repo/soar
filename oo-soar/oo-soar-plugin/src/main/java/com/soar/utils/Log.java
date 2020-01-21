package com.soar.utils;

import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.SimpleFormatter;

public class Log {

	//private final static String logDir = "/home/sattrix/log/";
	private final static String logDir = "C:\\Program Files\\Micro Focus\\Operations Orchestration\\central\\var\\logs\\";

	private static FileHandler logForDeveloper;
	private static FileHandler logForClient;

	public static  FileHandler setupLoggerForDeveloper() {
		
		try {
			logForDeveloper = new FileHandler(logDir + "oo-cp.log", true);
			logForDeveloper.setLevel(Level.FINE);
			logForDeveloper.setFormatter(new SimpleFormatter() {
				private static final String format = "[%1$tF %1$tT] [%2$-7s] %3$s %n";

				@Override
				public synchronized String format(LogRecord lr) {
					return String.format(format, new Date(lr.getMillis()), lr.getLevel().getLocalizedName(),
							lr.getMessage());
				}
			});
		} catch (java.io.IOException e) {

		}

		return logForDeveloper;
	}

	public static  FileHandler setupLoggerForStatus() {
	
		try {
			logForClient = new FileHandler(logDir + "status.log", false);
			logForClient.setLevel(Level.FINE);
			logForClient.setFormatter(new SimpleFormatter() {
				private static final String format = "[%1$tF %1$tT] [%2$-7s] %3$s %n";

				@Override
				public synchronized String format(LogRecord lr) {
					return String.format(format, new Date(lr.getMillis()), lr.getLevel().getLocalizedName(),
							lr.getMessage());
				}
			});
		} catch (java.io.IOException e) {

		}

		return logForClient;
	}

}
