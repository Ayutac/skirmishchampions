package org.abos.sc.core;

import static org.junit.jupiter.api.Assertions.*;

import java.nio.file.Path;

import org.abos.sc.core.battle.Formation;
import org.abos.util.Utilities;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * @author Sebastian Koch
 * @version %I%
 * @since 1.0
 */
class BattleFormationTest {

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeAll
	public static void setUpBeforeClass() throws Exception {
		Path path = Utilities.loadApplicationDirectory().resolve("resources").resolve("characters").resolve("chars_twi.txt");
		Utilities.loadFromFile(path, CharacterBase::parse);
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterAll
	public static void tearDownAfterClass() throws Exception {
		Character.CHARACTERS.clear();
	}
	
	public static final String REDFANG_FIVE = "twi_headscratcher,twi_rabbiteater,twi_shorthilt,twi_badarrow,twi_numbtongue,,";

	@Test
	public void parseTest() {
		Formation formation = new Formation(new Character[][] {
			{new Character(Character.CHARACTERS.lookup("twi_headscratcher")),
				new Character(Character.CHARACTERS.lookup("twi_rabbiteater")),
				new Character(Character.CHARACTERS.lookup("twi_shorthilt"))
			},

			{new Character(Character.CHARACTERS.lookup("twi_badarrow")),
				new Character(Character.CHARACTERS.lookup("twi_numbtongue")),
				null
			}
		});
		assertEquals(formation, Formation.parse(REDFANG_FIVE));
	}

	@Test
	public void parseTest2() {
		
		assertEquals(Formation.parse(REDFANG_FIVE).toSaveString(), REDFANG_FIVE);
	}

}
