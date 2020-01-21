package com.soar.utils;

import java.io.File;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.SimpleFormatter;

public class Log {

	//private final static String logDir = "/home/sattrix/log/";
	private final static String logDir = "C:\\Program Files\\Micro Focus\\Operations Orchestration\\central\\var\\logs";
	
	
	private static FileHandler logForDeveloper;
	private static FileHandler logForClient;

	public static  FileHandler setupLoggerForDeveloper() {
		
		  File file=createDirectoryForLogs(logDir);
		try {
			logForDeveloper = new FileHandler(file.getAbsolutePath() + "/oo-cp.log", true);
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
		 File file=createDirectoryForLogs(logDir+"\\soar-logs");
		try {
			logForClient = new FileHandler(file + "\\status.log", true);
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
	
	public static File createDirectoryForLogs(String directory) {
	    File dir = new File(directory);
	    if (!dir.exists()) dir.mkdirs();
	    return new File(directory);
	}

}
