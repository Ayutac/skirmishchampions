package org.abos.util.gui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import org.abos.util.Utilities;

/**
 * @author Sebastian Koch
 * @version %I%
 * @since Skirmish Champions 0.1.1
 */
public class ImagePanel extends JPanel {
	
	protected BufferedImage image = null;

	/**
	 * @throws IOException 
	 * 
	 */
	public ImagePanel(Path path, Dimension size) throws IOException {
		Utilities.requireNonNull(path, "path");
		setPreferredSize(size);
		image = ImageIO.read(path.toFile());
	}
	
	/**
	 * @return the loaded
	 */
	public boolean isLoaded() {
		return image != null;
	}

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, this); // see javadoc for more info on the parameters            
    }

}
