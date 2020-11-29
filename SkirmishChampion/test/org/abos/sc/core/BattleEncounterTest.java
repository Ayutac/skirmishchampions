package org.abos.sc.core;

import static org.junit.jupiter.api.Assertions.*;

import java.nio.file.Path;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * @author Sebastian Koch
 * @version %I%
 * @since 0.1
 */
public class BattleEncounterTest {

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeAll
	public static void setUpBeforeClass() throws Exception {
		Path path = Utilities.getBinaryDirectory().resolveSibling("resources").resolve("chars_twi.txt");
		Utilities.loadFromFile(path, CharacterBase::parse);
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterAll
	public static void tearDownAfterClass() throws Exception {
		Character.CHARACTERS.clear();
	}

	@Test
	public void parseTest() {
		BattleFormation formation = new BattleFormation(new Character[][] {
			{new Character(Character.CHARACTERS.lookup("twi_headscratcher")),
				new Character(Character.CHARACTERS.lookup("twi_rabbiteater")),
				new Character(Character.CHARACTERS.lookup("twi_shorthilt"))
			},

			{new Character(Character.CHARACTERS.lookup("twi_badarrow")),
				new Character(Character.CHARACTERS.lookup("twi_numbtongue")),
				null
			}
		});
		assertEquals(new BattleEncounter(formation, BattleStrategy.createRowAssault()), 
				BattleEncounter.parse("twi_headscratcher,twi_rabbiteater,twi_shorthilt,twi_badarrow,twi_numbtongue,|ROW"));
	}

}
