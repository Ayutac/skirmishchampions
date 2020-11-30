package org.abos.util.gui;

import java.awt.Component;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import org.abos.util.Utilities;

/**
 * Contains several utility methods related to graphical user interfaces.
 * @author Sebastian Koch
 * @version %I%
 * @since Skirmish Champions 0.1.1
 */
public class GUIUtilities {

	/**
	 * Main logos for the currently running application. 
	 */
	public final static List<Image> LOGOS = new ArrayList<>(5);
	
	/**
	 * Private constructor to avoid instantiation.
	 */
	private GUIUtilities() {}
	
	public static void loadLogos() throws IOException {
		if (!LOGOS.isEmpty())
			return;
		String path = Utilities.getApplicationDirectory().resolve("resources").resolve("logo").toString();
		for (int i = 16; i < 256; i *= 2) {
			LOGOS.add(Toolkit.getDefaultToolkit().getImage(path+i+".png"));
		}
	}
	
	public static Path getTitleScreenPath() throws IOException {
		return Utilities.getApplicationDirectory().resolve("resources").resolve("title_screen.png");
	}
	
	public static Image loadTitleScreen() throws IOException {
		return Toolkit.getDefaultToolkit().getImage(getTitleScreenPath().toString());
	}
	
	public static void errorMessage(Component parent, String title, String messageStart, Exception ex) {
		StringBuilder message = new StringBuilder(messageStart);
		message.append(" Error:");
		message.append(System.lineSeparator());
		message.append(ex.getMessage() == null ? "-no error message-" : ex.getMessage());
		message.append(System.lineSeparator());
		message.append("For more information see console.");
		ex.printStackTrace();
		JOptionPane.showMessageDialog(parent, message.toString(), title, JOptionPane.ERROR_MESSAGE);
	}
	
	public static void errorMessage(String title, String messageStart, Exception ex) {
		errorMessage(null, title, messageStart, ex);
	}

}
