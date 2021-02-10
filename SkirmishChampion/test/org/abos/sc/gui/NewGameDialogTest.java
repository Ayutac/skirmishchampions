package org.abos.sc.gui;

import static org.junit.jupiter.api.Assertions.*;

import javax.swing.JDialog;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * @author Sebastian Koch
 * @version %I%
 * @since 0.5
 */
class NewGameDialogTest {

	public static void main(String[] args) {
		NewGameDialog frame = new NewGameDialog(null);
		frame.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		frame.setVisible(true);
	}

}
