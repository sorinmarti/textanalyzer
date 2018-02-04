package com.sm.textanalyzer;

import com.sm.textanalyzer.app.Project;
import com.sm.textanalyzer.app.TextLibrary;
import com.sm.textanalyzer.gui.AnalyzerWindow;
import com.sm.textanalyzer.gui.ProjectTreeComponent;

import javax.swing.*;
import java.awt.*;
import java.util.Locale;
import java.util.ResourceBundle;

public class MainClass {

	public static final String APP_TITLE      = "Linguistic Text File Analyzer"; //NON-NLS
    public static final String HELP_URL       = "http://www.sorinmarti.com/de/uni"; //NON-NLS
	public static final String APP_VERSION    = "0.2b"; //NON-NLS
	private static ResourceBundle messages;

	public static void main(String[] args) {
        Locale currentLocale = new Locale("en", "US"); //NON-NLS //NON-NLS
        messages = ResourceBundle.getBundle("MessagesBundle", currentLocale); //NON-NLS

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
	 	} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

        TextLibrary.getInstance().setProjectFile( new Project() );
		
		EventQueue.invokeLater(() -> {
            try {
                AnalyzerWindow frame = new AnalyzerWindow();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        JFrame frame = new JFrame("ProjectTreeComponent");
        ProjectTreeComponent comp = new ProjectTreeComponent();
        comp.setData(new Project());
        frame.setContentPane(comp.contentPane);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
	}

    public static ResourceBundle getResourceBundle() {
        return messages;
    }
}
