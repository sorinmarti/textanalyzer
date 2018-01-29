package com.sm.textanalyzer;

import com.sm.textanalyzer.gui.AnalyzerWindow;

import javax.swing.*;
import java.awt.*;

public class MainClass {

	public static final String APP_TITLE      = "Linguistic Text File Analyzer";
	public static final String HELP_URL       = "http://www.sorinmarti.com/de/uni";
	public static final String APP_VERSION    = "0.2b";
	
	public static void main(String[] args) {
		
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
}
