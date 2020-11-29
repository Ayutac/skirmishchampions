package org.abos.sc.gui;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.file.Files;

import javax.swing.JFrame;

import org.abos.sc.core.Utilities;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * @author Sebastian Koch
 * @version %I%
 * @since 0.1
 */
public class DisplayTextAreaTest {

	public static void main(String[] args) throws IOException {
		TextAreaFrame credits = new TextAreaFrame("Credits");
		credits.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		credits.setText(Files.readString(Utilities.getApplicationDirectory().resolve("credits.txt"), Utilities.ENCODING));
		credits.setVisible(true);
	}

}
