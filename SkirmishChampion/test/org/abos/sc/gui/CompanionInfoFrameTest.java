package org.abos.sc.gui;

import static org.junit.jupiter.api.Assertions.*;

import javax.swing.JFrame;

import org.abos.sc.core.Character;
import org.abos.sc.core.CharacterBase;
import org.abos.sc.core.Companion;
import org.abos.sc.core.Difficulty;
import org.abos.sc.core.FandomBase;
import org.abos.sc.core.Player;
import org.abos.sc.core.RegionBase;
import org.abos.sc.core.StageBase;
import org.abos.sc.core.StatsPrimary;
import org.abos.sc.core.StatsSecondary;
import org.abos.sc.core.cards.Rarity;
import org.abos.util.Registry;
import org.junit.jupiter.api.Test;

/**
 * @author Sebastian Koch
 * @version %I%
 * @since 0.1
 */
public class CompanionInfoFrameTest {

	public static void main(String[] args) {
		Companion erin = new Companion(new CharacterBase("twi_erin", "Erin Solstice", "TWI", new String[] {"Inn"}, 
				new int[] {50,40,40,40,10,60,80,30}, StatsPrimary.CHARISMA, StatsSecondary.ELOQUENCE, Rarity.COMMON, "", false));
		Companion mrsha = new Companion(new CharacterBase("twi_mrsha", "Mrsha", "TWI", new String[] {"Inn"}, 
				new int[] {20,20,20,10,0,30,40,50}, StatsPrimary.CHARISMA, StatsSecondary.ELOQUENCE, Rarity.COMMON, "", false));
		new FandomBase("twi", "The Wandering Inn", "twi_twi", "twi_erin", true);
		new RegionBase("twi_twi", "The Wandering Inn", "twi", "twi_erins_bedroom", true);
		new StageBase("twi_erins_bedroom", "Erin's Bedroom", "twi_twi", new String[0], new String[0], new String[0], "twi_mrsha|ROW", true);
		Player player = new Player(Difficulty.MEDIUM, FandomBase.FANDOMS.lookup("twi"), new Registry<Companion>(erin, mrsha));
		CompanionInfoFrame frame = new CompanionInfoFrame(player);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}

}
