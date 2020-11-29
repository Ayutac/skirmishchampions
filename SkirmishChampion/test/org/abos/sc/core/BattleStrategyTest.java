package org.abos.sc.core;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class BattleStrategyTest {

	@Test
	public void testSaveAndLoad() {
		BattleStrategy strategy = BattleStrategy.createRowAssault();
		assertEquals(strategy, BattleStrategy.parse(strategy.toSaveString()));
	}

}
