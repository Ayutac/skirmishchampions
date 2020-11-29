package org.abos.sc.core;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class BattleTacticTest {

	@Test
	public void testToSaveString() {
		assertEquals("S:0,1,2,3,4,5", BattleTactic.createConcentratedAssault().toSaveString());
	}

}
