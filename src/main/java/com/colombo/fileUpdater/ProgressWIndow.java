package com.colombo.fileUpdater;

import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

public class ProgressWIndow implements Runnable {

	private JProgressBar progressBar;
	private int procentValue = 0;
	public ProgressWIndow() {
		initialize();
	}

	private void initialize() {
		JFrame frame = new JFrame("File updating...");
		frame.setResizable(false);

		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setPreferredSize(new Dimension(400, 300));
		frame.setLocation(dim.width / 2 - 200, dim.height / 2 - 150);

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());

		progressBar = new JProgressBar(0, 100);
		progressBar.setPreferredSize(new Dimension(250, 40));
		progressBar.setValue(0);
		progressBar.setStringPainted(true);

		panel.add(progressBar);

		frame.getContentPane().add(panel);

		frame.pack();
		frame.setVisible(true);
	}

	public void setProgressBar (int value) {
		progressBar.setValue(value);
	}
	
	public void run () {
		while (procentValue == 100) {
			try {
				Thread.sleep(100);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
