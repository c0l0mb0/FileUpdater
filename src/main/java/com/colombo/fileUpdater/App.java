package com.colombo.fileUpdater;

import java.io.IOException;

public class App {

	public static void main(String[] args) {
//		Logic logic = new Logic();
//		logic.go();
		try {
			INIHelper iNIHelper = new  INIHelper ("data\\localInf.ini");
			iNIHelper.setValue("Local","localFileVersion","3");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
