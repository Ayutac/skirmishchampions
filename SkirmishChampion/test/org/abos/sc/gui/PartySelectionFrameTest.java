package org.abos.sc.gui;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.file.Path;

import javax.swing.JFrame;

import org.abos.sc.core.Character;
import org.abos.sc.core.CharacterBase;
import org.abos.sc.core.Companion;
import org.abos.sc.core.StatsPrimary;
import org.abos.sc.core.StatsSecondary;
import org.abos.util.Registry;
import org.abos.util.Utilities;
import org.junit.jupiter.api.Test;

/**
 * @author Sebastian Koch
 * @version %I%
 * @since 0.1
 */
public class PartySelectionFrameTest {

	public static void main(String[] args) throws IOException {
//		Companion erin = new Companion(new CharacterBase("twi_erin", "Erin Solstice", "TWI", new String[] {"Inn"}, 
//				new int[] {50,40,40,40,10,60,80,30}, StatsPrimary.CHARISMA, StatsSecondary.ELOQUENCE, false));
//		Companion mrsha = new Companion(new CharacterBase("twi_mrsha", "Mrsha", "TWI", new String[] {"Inn"}, 
//				new int[] {20,20,20,10,0,30,40,50}, StatsPrimary.CHARISMA, StatsSecondary.ELOQUENCE, false));
		Path path = Utilities.loadApplicationDirectory().resolve("resources").resolve("characters").resolve("chars_twi.txt");
		Utilities.loadFromFile(path, CharacterBase::parse);
		Registry<Companion> companionPool = new Registry<>();
		for (CharacterBase character : Character.CHARACTERS)
			companionPool.add(new Companion(character));
		PartySelectionFrame frame = new PartySelectionFrame(companionPool);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		frame.selectionPanel.positionCheckBox[0][0].requestFocus();
	}

}
