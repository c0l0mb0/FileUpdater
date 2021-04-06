package com.colombo.fileUpdater;

import java.awt.Desktop;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import javax.swing.JOptionPane;

public class Logic {

	INIHelper iNIHelper = null;
	DTO_INI_Information dTO_INI_Information = new DTO_INI_Information();
	CustomLogger customLogger = new CustomLogger();
	private static final String pathToLocal_INI_File = "data\\localInf.ini";
	private static final String dataDir = System.getProperty("user.dir") + "\\data\\";
	private static final CheckSumHelper checkSumHelper = new CheckSumHelper();
	String serverFileVersion = null;

	public void go() {

		getLocalIniInformation();
		areFilesExist(dTO_INI_Information.getServerFilePath(), dTO_INI_Information.getServerFileVersionPath(),
				dTO_INI_Information.getLocalFilePath());
		getServerFileVersion(dTO_INI_Information.getServerFileVersionPath());

		if (needToUpdate(dTO_INI_Information.getLocalFileVersion(), serverFileVersion)) {
			update();
		}
		openFile();

	}

	private void openFile() {
		try {
			Desktop.getDesktop().open(new File(dTO_INI_Information.getLocalFilePath()));
		} catch (IOException e) {
			customLogger.log(Level.WARNING, "Error while opening file" + " " + Thread.currentThread().getStackTrace());
			showMessageAndTerminate("Error while opening file ");
		}

	}

	private boolean needToUpdate(String LocalFileVersion, String ServerFileVersion) {
		if (Integer.parseInt(dTO_INI_Information.getLocalFileVersion()) < Integer.parseInt(serverFileVersion)) {
			customLogger.log(Level.INFO, "LocalFileVersion < serverFileVersion) ");
			return true;
		}
		customLogger.log(Level.INFO, "LocalFileVersion >= serverFileVersion ");
		return false;

	}

	private void getServerFileVersion(String serverFileVersionPath) {

		try {
			iNIHelper = new INIHelper(serverFileVersionPath);
		} catch (IOException e) {
			customLogger.log(Level.WARNING,
					"error while initiating INIHelper" + " " + Thread.currentThread().getStackTrace());
			showMessageAndTerminate("error while initiating INIHelper ");

		}
		Map<String, Map<String, String>> _entries = new HashMap<>();
		_entries = iNIHelper.get_entries();

		serverFileVersion = _entries.get("Flie").get("version");

		if (serverFileVersion == null) {

			customLogger.log(Level.WARNING, "Error while reading ini serverFileVersion");
			showMessageAndTerminate("Error while reading ini serverFileVersion ");
		}
	}

	public void update() {
		try {
			copyFile(dTO_INI_Information.getServerFilePath(), dTO_INI_Information.getLocalFilePath() + "_");
		} catch (IOException e) {
			customLogger.log(Level.WARNING, "Error while copping ");
		}

		compareFiles(dTO_INI_Information.getServerFilePath(), dTO_INI_Information.getLocalFilePath() + "_");
		deleteFile(dTO_INI_Information.getLocalFilePath());
		renameFile(dTO_INI_Information.getLocalFilePath() + "_", dTO_INI_Information.getLocalFilePath());
		updateLocalFileVersion();
	}

	private void updateLocalFileVersion() {
		// TODO Auto-generated method stub
		
	}

	private void renameFile(String oldFileName, String newFileName) {
		// File (or directory) with old name
		File oldFile = new File(oldFileName);

		// File (or directory) with new name
		File newFile = new File(newFileName);

		if (newFile.exists()) {
			customLogger.log(Level.WARNING, "Error while renaming. File exists");
			showMessageAndTerminate("Error while renaming. File exists");
		}

		// Rename file (or directory)
		boolean success = oldFile.renameTo(newFile);

		if (!success) {
			customLogger.log(Level.WARNING, "Error while renaming.");
			showMessageAndTerminate("Error while renaming.");
		}
		customLogger.log(Level.INFO, "File renamed ");
	}

	private void compareFiles(String firstFilePath, String SecondFilePath) {
		try {
			if (checkSumHelper.isEqualHash(firstFilePath, SecondFilePath)) {
				customLogger.log(Level.INFO, "HASHes are same ");
				return;
			}
		} catch (Exception e) {
			customLogger.log(Level.WARNING, "Error while HASH comparing");
			showMessageAndTerminate("Error while HASH comparing");
		}
		customLogger.log(Level.WARNING, "Error hashes are NOT same");
		showMessageAndTerminate("Hashes are NOT same");
	}

	private void deleteFile(String pathTolFile) {
		File file = new File(pathTolFile);
		if (file.delete()) {
			customLogger.log(Level.INFO, "File deleted successfully " + pathTolFile);
		} else {
			customLogger.log(Level.WARNING, "Failed to delete the file " + pathTolFile);
		}
	}

	private void getLocalIniInformation() {

		areFilesExist(pathToLocal_INI_File);
		try {
			iNIHelper = new INIHelper(pathToLocal_INI_File);
		} catch (IOException e) {
			customLogger.log(Level.WARNING,
					"error while initiating INIHelper" + " " + Thread.currentThread().getStackTrace());
			showMessageAndTerminate("error while initiating INIHelper ");

		}
		Map<String, Map<String, String>> _entries = new HashMap<>();
		_entries = iNIHelper.get_entries();

		dTO_INI_Information.setServerFileVersionPath(_entries.get("Server").get("ServerFileVersionPath"));
		dTO_INI_Information.setServerFilePath(_entries.get("Server").get("ServerFilePath"));
		dTO_INI_Information.setLocalFileVersion(_entries.get("Local").get("localFileVersion"));
		dTO_INI_Information.setLocalFilePath(dataDir + _entries.get("Local").get("localFilePath"));

		if (dTO_INI_Information.isAnyNullField() == true) {

			customLogger.log(Level.WARNING,
					"error while reading ini keys" + " " + Thread.currentThread().getStackTrace());
			showMessageAndTerminate("error while reading ini keys ");
		}

	}

	private void areFilesExist(String... filesPath) {
		ArrayList<String> notExistingFiles = new ArrayList<>();
		for (String filePath : filesPath) {
			if (filePath != null) {
				File f = new File(filePath);

				if (!f.exists())
					notExistingFiles.add(filePath);
			}
		}
		if (notExistingFiles.size() > 0) {

			notExistingFiles.forEach(file -> {
				customLogger.log(Level.WARNING, "file  is not exists " + " " + notExistingFiles.toString());
			});
			showMessageAndTerminate("file  is not exists ");
		}
	}

	private void showMessageAndTerminate(String msgText) {
		JOptionPane.showMessageDialog(null, msgText, "InfoBox: " + "Error", JOptionPane.INFORMATION_MESSAGE);
		System.exit(0);
	}

	private void copyFile(String srcPath, String dstPath) throws IOException {

		File src = new File(srcPath);
		File dst = new File(dstPath);
		InputStream in = new FileInputStream(src);
		OutputStream out = new FileOutputStream(dst);
		long expectedBytes = src.length();
		long totalBytesCopied = 0;
		byte[] buf = new byte[1024];
		int len = 0;
		ProgressWIndow progressWIndow = new ProgressWIndow();
		Thread thread = new Thread(progressWIndow);
		thread.start();

		while ((len = in.read(buf)) > 0) {
			out.write(buf, 0, len);
			totalBytesCopied += len;
			int progress = (int) Math.round(((double) totalBytesCopied / (double) expectedBytes) * 100);
			progressWIndow.setProgressBar(progress);
		}
		in.close();
		out.close();
		customLogger.log(Level.INFO, "file is copied " + dstPath);
	}
}
