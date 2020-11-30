package org.abos.util.gui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;

import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.JPanel;

import org.abos.util.Utilities;

/**
 * A panel for displaying an image.
 * @author Sebastian Koch
 * @version %I%
 * @since Skirmish Champions 0.1.1
 * @see #isLoaded()
 */
public class ImagePanel extends JPanel {
	
	
	/**
	 * the serial version UID
	 */
	private static final long serialVersionUID = 7084151051862365643L;
	
	/**
	 * the image of this image panel
	 * @see #isLoaded()
	 * @see #paintComponent(Graphics)
	 */
	protected BufferedImage image = null;

	/**
	 * Creates a new image panel with the specified image.
	 * @param path the path to the image
	 * @param size the preferred size for the image, likely to be the image's dimensions
	 * @throws NullPointerException If <code>path</code> refers to <code>null</code>.
	 * @throws IOException If the image specified by <code>path</code> couldn't be loaded.
	 */
	public ImagePanel(Path path, Dimension size) throws IOException {
		Utilities.requireNonNull(path, "path");
		setPreferredSize(size);
		image = ImageIO.read(path.toFile());
	}
	
	/**
	 * Returns <code>true</code> if the image of this image panel is loaded.
	 * @return <code>true</code> if the image of this image panel is loaded, else <code>false</code>.
	 */
	public boolean isLoaded() {
		return image != null;
	}

	/**
	 * Draws the image over anything else that has been drawn before in this panel.
	 * @see JComponent#paintComponent(Graphics)
	 */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, this); // see javadoc for more info on the parameters            
    }

}
