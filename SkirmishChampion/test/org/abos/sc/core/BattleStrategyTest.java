package org.abos.sc.core;

import static org.junit.jupiter.api.Assertions.*;

import org.abos.sc.core.battle.Strategy;
import org.junit.jupiter.api.Test;

public class BattleStrategyTest {

	@Test
	public void testSaveAndLoad() {
		Strategy strategy = Strategy.createRowAssault();
		assertEquals(strategy, Strategy.parse(strategy.toSaveString()));
	}

}
