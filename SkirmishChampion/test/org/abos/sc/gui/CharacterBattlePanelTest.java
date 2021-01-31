package org.abos.sc.gui;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.file.Path;

import javax.swing.JFrame;

import org.abos.sc.core.Character;
import org.abos.sc.core.CharacterBase;
import org.abos.util.Utilities;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * @author Sebastian Koch
 * @version %I%
 * @since 1.0
 */
class CharacterBattlePanelTest {

	public static void main(String[] args) throws IOException {
		Path path = Utilities.loadApplicationDirectory().resolve("resources").resolve("characters").resolve("chars_twi.txt");
		Utilities.loadFromFile(path, CharacterBase::parse);
		CharacterBattlePanel panel = new CharacterBattlePanel(new Character(CharacterBase.CHARACTERS.lookup("twi_erin")));
		JFrame frame = new JFrame("CharacterBattlePanelTest");
		frame.add(panel);
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}

}
