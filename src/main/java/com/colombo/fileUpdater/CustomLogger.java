package com.colombo.fileUpdater;

import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class CustomLogger {
	private static final Logger logger = Logger.getLogger("MyLogger");
	private static FileHandler fh = null;

	public CustomLogger() {
		try {

			fh = new FileHandler("data/MyLogFile.log", true);

		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.addHandler(fh);
		SimpleFormatter formatter = new SimpleFormatter();
		fh.setFormatter(formatter);
	}

	public void log(Level _level, String msg) {
		this.logger.log(_level, msg);
	}
}
