package org.abos.sc.core;

import static org.junit.jupiter.api.Assertions.*;

import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.StreamHandler;

import org.abos.sc.core.battle.Battle;
import org.abos.sc.core.battle.Encounter;
import org.abos.sc.core.battle.Formation;
import org.abos.sc.core.battle.Strategy;
import org.abos.util.Utilities;
import org.junit.jupiter.api.Test;

public class BattleTest {
	
	public static void main(String[] args) throws InterruptedException {
		Character erin = new Character(new CharacterBase("twi_erin", "Erin Solstice", "TWI", new String[] {"Inn"}, 
				new int[] {50,40,40,40,10,60,80,30}, StatsPrimary.CHARISMA, StatsSecondary.ELOQUENCE, false));
		Character mrsha = new Character(new CharacterBase("twi_mrsha", "Mrsha", "TWI", new String[] {"Inn"}, 
				new int[] {20,20,20,10,0,30,40,50}, StatsPrimary.CHARISMA, StatsSecondary.ELOQUENCE, false));
		Formation playerForm = new Formation(new Character[][] {{erin, null, null}, {null, null, null}});
		Strategy playerStrat = Strategy.createConcentratedAssault();
		Encounter player = new Encounter(playerForm, playerStrat);
		Formation computerForm = new Formation(new Character[][] {{mrsha, null, null}, {null, null, null}});
		Strategy computerStrat = Strategy.createConcentratedAssault();
		Encounter computer = new Encounter(computerForm, computerStrat);
		Handler consoleHandler = new StreamHandler(System.out, Utilities.createSimplestFormatter(false)) {
			public synchronized void publish(java.util.logging.LogRecord record) {
				super.publish(record);
				flush();
			}
		};
		Battle battle = new Battle(player, computer, Difficulty.of(null), consoleHandler);
		battle.run();
		battle.waitForEnd();
		battle.restoreCombatants();
		Character visma = new Character(new CharacterBase("twi_visma", "Visma", "TWI", new String[] {"Inn"}, 
				new int[] {10,20,10,0,0,20,10,40}, StatsPrimary.CHARISMA, StatsSecondary.ELOQUENCE, false));
		computer = new Encounter(new Formation(new Character[][] {{mrsha, visma, null}, {null, null, null}}), computerStrat);
		battle = new Battle(player, computer, Difficulty.of(null), consoleHandler);
		battle.run();
		battle.waitForEnd();
		battle.restoreCombatants();
		computer = new Encounter(new Formation(new Character[][] {{visma, mrsha, null}, {null, null, null}}), computerStrat);
		battle = new Battle(player, computer, Difficulty.of(null), consoleHandler);
		battle.run();
		battle.waitForEnd();
	}

}
