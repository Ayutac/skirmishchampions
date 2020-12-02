package org.abos.sc.core;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class BattleTacticTest {

	@Test
	public void testToSaveString() {
		assertEquals("0,1,2,3,4,5", BattleTactic.createConcentratedAssault().toSaveString());
	}

	@Test
	public void testColAssault1() {
		assertEquals(new BattleTactic(new int[] {0,3,1,4,2,5}), BattleTactic.createColAssault(0));
	}

	@Test
	public void testColAssault2() {
		assertEquals(new BattleTactic(new int[] {1,4,0,3,2,5}), BattleTactic.createColAssault(1));
	}

	@Test
	public void testColAssault3() {
		assertEquals(new BattleTactic(new int[] {2,5,1,4,0,3}), BattleTactic.createColAssault(2));
	}

	@Test
	public void testRowAssault1() {
		assertEquals(new BattleTactic(new int[] {0,1,2,3,4,5}), BattleTactic.createRowAssault(0));
	}

	@Test
	public void testRowAssault2() {
		assertEquals(new BattleTactic(new int[] {1,0,2,4,3,5}), BattleTactic.createRowAssault(1));
	}

	@Test
	public void testRowAssault3() {
		assertEquals(new BattleTactic(new int[] {2,1,0,5,4,3}), BattleTactic.createRowAssault(2));
	}
	
	public void testConcentratedAssault() {
		assertEquals(new BattleTactic(new int[] {0,1,2,3,4,5}), BattleTactic.createConcentratedAssault());
	}

}
