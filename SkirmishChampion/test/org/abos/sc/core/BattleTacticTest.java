package org.abos.sc.core;

import static org.junit.jupiter.api.Assertions.*;

import org.abos.sc.core.battle.Tactic;
import org.junit.jupiter.api.Test;

public class BattleTacticTest {

	@Test
	public void testToSaveString() {
		assertEquals("0,1,2,3,4,5", Tactic.createConcentratedAssault().toSaveString());
	}

	@Test
	public void testColAssault1() {
		assertEquals(new Tactic(new int[] {0,3,1,4,2,5}), Tactic.createColAssault(0));
	}

	@Test
	public void testColAssault2() {
		assertEquals(new Tactic(new int[] {1,4,0,3,2,5}), Tactic.createColAssault(1));
	}

	@Test
	public void testColAssault3() {
		assertEquals(new Tactic(new int[] {2,5,1,4,0,3}), Tactic.createColAssault(2));
	}

	@Test
	public void testRowAssault1() {
		assertEquals(new Tactic(new int[] {0,1,2,3,4,5}), Tactic.createRowAssault(0));
	}

	@Test
	public void testRowAssault2() {
		assertEquals(new Tactic(new int[] {1,0,2,4,3,5}), Tactic.createRowAssault(1));
	}

	@Test
	public void testRowAssault3() {
		assertEquals(new Tactic(new int[] {2,1,0,5,4,3}), Tactic.createRowAssault(2));
	}
	
	public void testConcentratedAssault() {
		assertEquals(new Tactic(new int[] {0,1,2,3,4,5}), Tactic.createConcentratedAssault());
	}

}
