package org.abos.sc.gui;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.file.Path;

import javax.swing.JFrame;

import org.abos.sc.core.CharacterBase;
import org.abos.sc.core.Companion;
import org.abos.sc.core.FandomBase;
import org.abos.sc.core.Player;
import org.abos.sc.core.RegionBase;
import org.abos.sc.core.StageBase;
import org.abos.sc.core.Utilities;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * @author Sebastian Koch
 * @version %I%
 * @since 0.1
 */
public class StageSelectionFrameTest {

	public static void main(String[] args) throws IOException {
		Path path = Utilities.getBinaryDirectory().resolveSibling("resources");
		Utilities.loadFromFile(path.resolve("chars_twi.txt"), CharacterBase::parse);
		Utilities.loadFromFile(path.resolve("stages_twi.txt"), StageBase::parse);
		Utilities.loadFromFile(path.resolve("regions_twi.txt"), RegionBase::parse);
		Utilities.loadFromFile(path.resolve("fandoms.txt"), FandomBase::parse);
		Player player = new Player(FandomBase.FANDOMS.lookup("twi"), new Companion(CharacterBase.CHARACTERS.lookup("twi_erin")));
		StageSelectionFrame frame = new StageSelectionFrame(player, true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}

}
