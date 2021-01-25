package org.abos.util.gui;

import java.awt.Component;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
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
	
	/**
	 * If {@link #LOGOS} is unloaded, loads logos of sizes 16, 32, 64 and 128 from
	 * <code>resources/logoX.png</code> (where <code>X</code> is the size) relative to the
	 * directory where the application is located.
	 * @throws IllegalStateException If {@link Utilities#loadApplicationDirectory()} hasn't successfully been called yet.
	 * @throws IOException If at least one image couldn't be read in.
	 */
	public static void loadLogos() throws IOException {
		Utilities.checkApplicationDirectory();
		if (!LOGOS.isEmpty())
			return;
		String path = Utilities.getApplicationDirectory().resolve("resources").resolve("images").resolve("logo").toString();
		for (int i = 16; i < 256; i *= 2) {
			LOGOS.add(ImageIO.read(new File(path+i+".png")));
		}
	}
	
	/**
	 * Returns a path to <code>resources/title_screen.png</code>.
	 * @return the path to the title screen image
	 * @throws IllegalStateException If {@link Utilities#loadApplicationDirectory()} hasn't successfully been called yet.
	 */
	public static Path getTitleScreenPath() {
		Utilities.checkApplicationDirectory();
		return Utilities.getApplicationDirectory().resolve("resources").resolve("images").resolve("title_screen.png");
	}
	
	/**
	 * Displays an error message based on an exception and prints the stacktrace to the console.
	 * @param parent the parent component for the error message
	 * @param title the title of the error message
	 * @param messageStart Start of the error message. Will be followed by the error message from the exception.
	 * @param ex the exception to display
	 * @see JOptionPane#showMessageDialog(Component, Object, String, int)
	 * @see Throwable#printStackTrace()
	 */
	public static void errorMessage(Component parent, String title, String messageStart, Exception ex) {
		StringBuilder message = new StringBuilder(messageStart);
		if (ex != null) {
			message.append(" Error:");
			message.append(System.lineSeparator());
			message.append(ex.getMessage() == null ? "-no error message-" : ex.getMessage());
			message.append(System.lineSeparator());
			message.append("For more information see console.");
			ex.printStackTrace();
		}
		JOptionPane.showMessageDialog(parent, message.toString(), title, JOptionPane.ERROR_MESSAGE);
	}
	
	/**
	 * Displays an error message without a parent based on an exception and prints the stacktrace to the console.
	 * @param title the title of the error message
	 * @param messageStart Start of the error message. Will be followed by the error message from the exception.
	 * @param ex the exception to display
	 * @see #errorMessage(Component, String, String, Exception)
	 * @see JOptionPane#showMessageDialog(Component, Object, String, int)
	 * @see Throwable#printStackTrace()
	 */
	public static void errorMessage(String title, String messageStart, Exception ex) {
		errorMessage(null, title, messageStart, ex);
	}

}
