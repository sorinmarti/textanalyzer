package com.sm.textanalyzer;

import java.awt.EventQueue;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.sm.textanalyzer.gui.AnalyzerWindow;

public class MainClass {

	public static final String APP_TITLE      = "Linguistic Text File Analyzer";
	public static final String HELP_URL       = "http://www.sorinmarti.com/de/uni";
	public static final String APP_VERSION    = "0.2b";
	
	public static Properties prop = new Properties();
	
	public static void main(String[] args) {
		//createProperties();
		
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
	 	} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		EventQueue.invokeLater(() -> {
            try {
                AnalyzerWindow frame = new AnalyzerWindow();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
	}

	
	private static void createProperties() {
		OutputStream output = null;
		
		try {
			output = new FileOutputStream("config.properties");

			// set the properties value
			prop.setProperty("STRIP_NUMBERS", "true");
			prop.setProperty("NORMALIZE_VOCALS", "true");

			// save properties to project root folder
			prop.store(output, null);

		} catch (IOException io) {
			io.printStackTrace();
		} finally {
			if (output != null) {
				try {
					output.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}
	}
}
