package com.colombo.fileUpdater;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;

public class CheckSumHelper {

	public boolean isEqualHash(String firstFilePath, String secondFilePath) throws Exception {
		File firstFile = new File(firstFilePath);
		File secondFile = new File(secondFilePath);

		MessageDigest md5Digest = MessageDigest.getInstance("MD5");
		String checksumFisrt = getFileChecksum(md5Digest, firstFile);
		String checksumSecond = getFileChecksum(md5Digest, secondFile);
		if (checksumFisrt.equals(checksumSecond))
			return true;

		return false;
	}

	private static String getFileChecksum(MessageDigest digest, File file) throws IOException {
		// Get file input stream for reading the file content
		FileInputStream fis = new FileInputStream(file);

		// Create byte array to read data in chunks
		byte[] byteArray = new byte[1024];
		int bytesCount = 0;

		// Read file data and update in message digest
		while ((bytesCount = fis.read(byteArray)) != -1) {
			digest.update(byteArray, 0, bytesCount);
		}
		;

		// close the stream; We don't need it now.
		fis.close();

		// Get the hash's bytes
		byte[] bytes = digest.digest();

		// This bytes[] has bytes in decimal format;
		// Convert it to hexadecimal format
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < bytes.length; i++) {
			sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
		}

		// return complete hash
		return sb.toString();
	}
}
