package org.abos.sc.core;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.file.Path;

import org.abos.util.Utilities;
import org.junit.jupiter.api.Test;

/**
 * @author Sebastian Koch
 * @version %I%
 * @since 0.1
 */
public class StageTest {

//	@Test
//	public void testParsing() {
//		
//	}
	
	@Test
	public void testParseData() throws IOException {
		Path path = Utilities.loadBinaryDirectory().resolveSibling("resources").resolve("stages_twi.txt");
		Utilities.loadFromFile(path, StageBase::parse);
		System.out.println(String.format("%d: %s", Stage.STAGES.size(), Stage.STAGES));
		//System.out.println(Character.CHARACTERS.lookup("twi_erin").getPreferredAttackStat());
		//System.out.println(Character.CHARACTERS.lookup("twi_erin").getPreferredDamageStat());
		Stage.STAGES.clear();
	}

}
