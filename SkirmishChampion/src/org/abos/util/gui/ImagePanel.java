package org.abos.util.gui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.IOException;
import java.nio.file.Path;

import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.JPanel;

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
	protected Image image = null;

	/**
	 * Creates an empty image panel.
	 * @param size the preferred size for images, likely to be the images' dimensions
	 */
	public ImagePanel(Dimension size) {
		setPreferredSize(size);
	}

	/**
	 * Creates a new image panel with the specified image.
	 * @param path the path to the image
	 * @param size the preferred size for the image, likely to be the image's dimensions
	 * @throws NullPointerException If <code>path</code> refers to <code>null</code>.
	 * @throws IOException If the image specified by <code>path</code> couldn't be loaded.
	 */
	public ImagePanel(Path path, Dimension size) throws IOException {
		this(size);
		loadImage(path);
	}
	
	/**
	 * Sets the image for this panel.
	 * @param image the image to set
	 */
	public void setImage(Image image) {
		this.image = image;
		repaint();
	}
	
	/**
	 * Loads an image from the given path if one is provided. This method will always attempt to load the
	 * specified image anew, using {@link ImageIO#read(java.io.File)}, and throw an {@link IOException}
	 * if that doesn't work.
	 * @param path The path to load the image from. If <code>null</code>, the current image will vanish.
	 * @throws IOException If the image specified by <code>path</code> couldn't be loaded.
	 * @see #loadImageLazy(Path)
	 * @see ImageIO#read(java.io.File)
	 */
	public void loadImage(Path path) throws IOException {
		if (path == null)
			setImage(null);
		else {
			setImage(ImageIO.read(path.toFile()));
		}
	}
	
	/**
	 * Loads an image from the given path if one is provided. This method will attempt to look up
	 * if the image has already been loaded into the memory and use that one. If there is none,
	 * an attempt will be made to load the image using {@link Toolkit#getImage(String)}.
	 * If that fails, the current image vanishes. No exception will be thrown.
	 * @param path The path to load the image from. If <code>null</code>, the current image will vanish.
	 * @see #loadImage(Path)
	 * @see Toolkit#getImage(String)
	 */
	public void loadImageLazy(Path path) {
		if (path == null)
			setImage(null);
		else {
			setImage(Toolkit.getDefaultToolkit().getImage(path.toString()));
		}
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
        if (image != null)
        	g.drawImage(image, 0, 0, this); // see javadoc for more info on the parameters            
    }

}
