package com.colombo.fileUpdater;

import java.lang.reflect.Field;

public class DTO_INI_Information {
	String serverFileVersionPath = null;
	String serverFilePath = null;
	String localFileVersion = null;
	String localFilePath = null;

	public String getServerFileVersionPath() {
		return serverFileVersionPath;
	}

	public void setServerFileVersionPath(String serverFileVersionPath) {
		this.serverFileVersionPath = serverFileVersionPath;
	}

	public String getServerFilePath() {
		return serverFilePath;
	}

	public void setServerFilePath(String serverFilePath) {
		this.serverFilePath = serverFilePath;
	}

	public String getLocalFileVersion() {
		return localFileVersion;
	}

	public void setLocalFileVersion(String localFileVersion) {
		this.localFileVersion = localFileVersion;
	}

	public String getLocalFilePath() {
		return localFilePath;
	}

	public void setLocalFilePath(String localFilePath) {
		this.localFilePath = localFilePath;
	}

	public String toString() {
		return serverFileVersionPath + " " + serverFilePath + " " + localFileVersion + " " + localFilePath;

	}

	public boolean isAnyNullField() {

		for (Field f : this.getClass().getDeclaredFields())
			try {
				if (f.get(this) == null)
					return true;
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return false;

	}

}
