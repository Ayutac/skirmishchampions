package org.abos.util.gui;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
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
	 * Returns a hash code for a grid bag constraints object, taking into account all constraints. It's guarantied that
	 * the hash codes of two grid bag constraints objects are equal if the grid bag constraints objects themselves are equal
	 * as evaluated by {@link GUIUtilities}.
	 * @param gbc the grid bag constraints object to generate a hash code of, not <code>null</code>
	 * @return a hash code for the specified grid bag constraints object.
	 * @throws NullPointerException If <code>gbc</code> refers to <code>null</code>.
	 * @see #equalsGBC(GridBagConstraints, GridBagConstraints)
	 */
	public static int hashCodeGBC(GridBagConstraints gbc) {
		Utilities.requireNonNull(gbc, "gbc");
		final int prime = 31;
		int result = 1;
		result = prime * result + gbc.anchor;
		result = prime * result + gbc.fill;
		result = prime * result + gbc.gridheight;
		result = prime * result + gbc.gridwidth;
		result = prime * result + gbc.gridx;
		result = prime * result + gbc.gridy;
		result = prime * result + ((gbc.insets == null) ? 0 : gbc.insets.hashCode());
		result = prime * result + gbc.ipadx;
		result = prime * result + gbc.ipady;
		long temp;
		temp = Double.doubleToLongBits(gbc.weightx);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(gbc.weighty);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}
	
	/**
	 * Compares two grid bag constraints objects for equality.
	 * If the two objects are equal, then their hash codes given by {@link GUIUtilities} return the same number.
	 * @param gbc1 the first grid bag constraints object to compare
	 * @param gbc2 the second grid bag constraints object to compare
	 * @return <code>true</code> if both objects refer to <code>null</code> or if each corresponding constraint pair 
	 * of the two objects are equal, else <code>false</code>
	 * @see #hashCodeGBC(GridBagConstraints)
	 */
	public static boolean equalsGBC(GridBagConstraints gbc1, GridBagConstraints gbc2) {
		if (gbc1 == gbc2)
			return true;
		if (gbc1 == null || gbc2 == null)
			return false;
		if (gbc1.gridx != gbc2.gridx)
			return false;
		if (gbc1.gridy != gbc2.gridy)
			return false;
		if (gbc1.gridwidth != gbc2.gridwidth)
			return false;
		if (gbc1.gridheight != gbc2.gridheight)
			return false;
		if (Double.doubleToLongBits(gbc1.weightx) != Double.doubleToLongBits(gbc2.weightx))
			return false;
		if (Double.doubleToLongBits(gbc1.weighty) != Double.doubleToLongBits(gbc2.weighty))
			return false;
		if (gbc1.anchor != gbc2.anchor)
			return false;
		if (gbc1.fill != gbc2.fill)
			return false;
		if (gbc1.insets == null) {
			if (gbc2.insets != null)
				return false;
		} else if (!gbc1.insets.equals(gbc2.insets))
			return false;
		if (gbc1.ipadx != gbc2.ipadx)
			return false;
		if (gbc1.ipady != gbc2.ipady)
			return false;
		return true;
	}
	
	/**
	 * If {@link #LOGOS} is unloaded, loads logos of sizes 16, 32, 64 and 128 from
	 * <code>resources/images/logoX.png</code> (where <code>X</code> is the size) relative to the
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
	 * Returns a path to <code>resources/images/title_screen.png</code>.
	 * @return the path to the title screen image
	 * @throws IllegalStateException If {@link Utilities#loadApplicationDirectory()} hasn't successfully been called yet.
	 */
	public static Path getTitleScreenPath() {
		Utilities.checkApplicationDirectory();
		return Utilities.getApplicationDirectory().resolve("resources").resolve("images").resolve("title_screen.png");
	}
	
	/**
	 * Returns a path to <code>resources/images/characters</code>.
	 * @return the path to the character images
	 * @throws IllegalStateException If {@link Utilities#loadApplicationDirectory()} hasn't successfully been called yet.
	 */
	public static Path getCharacterImagesPath() {
		Utilities.checkApplicationDirectory();
		return Utilities.getApplicationDirectory().resolve("resources").resolve("images").resolve("characters");
	}
	
	/**
	 * Tells if the given JFileChooser has its accept all filter currently selected.
	 * @param chooser the chooser to check
	 * @return <code>true</code> if <code>chooser</code> is not <code>null</code> and has its accept all filter
	 * currently selected, else <code>false</code>.
	 */
	public static boolean isAcceptAllFilterSelected(JFileChooser chooser) {
		if (chooser == null)
			return false;
		return chooser.getFileFilter().equals(chooser.getAcceptAllFileFilter());
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
