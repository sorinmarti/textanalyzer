package com.sm.textanalyzer;

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
    private static JFrame frame;

	public static void main(String[] args) {
        Locale currentLocale = new Locale("en", "US"); //NON-NLS //NON-NLS
        messages = ResourceBundle.getBundle("MessagesBundle", currentLocale); //NON-NLS

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
	 	} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		EventQueue.invokeLater(() -> {
            try {
                //AnalyzerWindow frame = new AnalyzerWindow();
                //frame.setVisible(true);

                frame = new JFrame("ProjectTreeComponent");
                ProjectTreeComponent comp = new ProjectTreeComponent();
                frame.setContentPane(comp.contentPane);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.pack();
                frame.setVisible(true);

            } catch (Exception e) {
                e.printStackTrace();
            }
        });


	}

    public static void setWindowTitle(boolean projectModified) {
        // No project is opened
        if(!DataPool.projectOpen()) {
            frame.setTitle(MainClass.APP_TITLE);
            return;
        }

        // A project is open
        String string;
        if(DataPool.project.getProjectFile()==null) {
            // The opened project was not yet saved
            string = String.format("[%s]","unsaved project"); //NON-NLS
        }
        else {
            // The opened project has been saved
            string = DataPool.project.getProjectFile().getName();
        }

        if(projectModified) {
            string += "*";
        }

        frame.setTitle(MainClass.APP_TITLE + " " + string);
    }

    public static ResourceBundle getResourceBundle() {
        return messages;
    }
}
