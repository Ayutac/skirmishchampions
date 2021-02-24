package org.abos.sc.gui;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;

import org.abos.sc.core.Character;
import org.abos.sc.core.CharacterBase;
import org.abos.sc.core.StatsPrimary;
import org.abos.sc.core.StatsSecondary;
import org.abos.sc.core.battle.Encounter;
import org.abos.sc.core.battle.Formation;
import org.abos.sc.core.battle.Strategy;
import org.abos.sc.core.cards.Rarity;
import org.abos.util.Utilities;
import org.junit.jupiter.api.Test;

/**
 * @author Sebastian Koch
 * @version %I%
 * @since 0.1
 */
public class StageBattleFrameTest {

	public static void main(String[] args) throws IOException {
		Utilities.loadApplicationDirectory();
//		Character erin = new Character(new CharacterBase("twi_erin", "Erin Solstice", "TWI", new String[] {"Inn"}, 
//				new int[] {50,40,40,40,10,60,80,30}, StatsPrimary.CHARISMA, StatsSecondary.ELOQUENCE, false));
//		Character mrsha = new Character(new CharacterBase("twi_mrsha", "Mrsha", "TWI", new String[] {"Inn"}, 
//				new int[] {20,20,20,10,0,30,40,50}, StatsPrimary.CHARISMA, StatsSecondary.ELOQUENCE, false));
//		BattleFormation playerForm = new BattleFormation(new Character[][] {{erin, null, null}, {null, null, null}});
//		BattleStrategy playerStrat = BattleStrategy.createConcentratedAssault();
//		BattleEncounter player = new BattleEncounter(playerForm, playerStrat);
//		BattleFormation computerForm = new BattleFormation(new Character[][] {{mrsha, null, null}, {null, null, null}});
//		BattleStrategy computerStrat = BattleStrategy.createConcentratedAssault();
//		BattleEncounter computer = new BattleEncounter(computerForm, computerStrat);
		System.out.println(new JTextArea().getFont());
		System.out.println(new JLabel().getFont());
		Character[][] silverRanks = new Character[Formation.ROW_NUMBER][Formation.COL_NUMBER];
		int count = 0;
		for (int row = 0; row < Formation.ROW_NUMBER; row++) {
			for (int col = 0; col < Formation.COL_NUMBER; col++) {
				silverRanks[row][col] = new Character(new CharacterBase("twi_erin", "Silver "+count, "TWI", new String[] {"Inn"}, 
						new int[] {80,60,40,40,10,60,80,40}, StatsPrimary.STRENGTH, StatsSecondary.CONSTITUTION, Rarity.COMMON, "", false));
				if (++count >= 5)
					break;
			}
			if (count >= 5) {
				break;
			}
		}
		Character gold = new Character(new CharacterBase("twi_erin", "Gold", "TWI", new String[] {"Inn"}, 
		new int[] {300,300,40,40,10,60,80,80}, StatsPrimary.STRENGTH, StatsSecondary.CONSTITUTION, Rarity.COMMON, "", false));
		Formation playerForm = new Formation(new Character[][] {{gold, null, null}, {null, null, null}});
		Strategy playerStrat = Strategy.createConcentratedAssault();
		Encounter player = new Encounter(playerForm, playerStrat);
		Formation computerForm = new Formation(silverRanks);
		Strategy computerStrat = Strategy.createConcentratedAssault();
		Encounter computer = new Encounter(computerForm, computerStrat);
		
		StageBattleFrame frame = new StageBattleFrame();
		frame.stageLabel.setText("Test Battle");
		frame.setFirstParty(player);
		frame.setSecondParty(computer);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}

}
