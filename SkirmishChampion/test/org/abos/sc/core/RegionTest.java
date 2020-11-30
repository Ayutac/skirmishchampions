package org.abos.sc.core;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.file.Path;

import org.abos.util.Utilities;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * @author Sebastian Koch
 * @version %I%
 * @since 0.1
 */
public class RegionTest {
	
	@Test
	public void testParseData() throws IOException {
		Path path = Utilities.getBinaryDirectory().resolveSibling("resources").resolve("regions_twi.txt");
		Utilities.loadFromFile(path, RegionBase::parse);
		System.out.println(String.format("%d: %s", Region.REGIONS.size(), Region.REGIONS));
		//System.out.println(Character.CHARACTERS.lookup("twi_erin").getPreferredAttackStat());
		//System.out.println(Character.CHARACTERS.lookup("twi_erin").getPreferredDamageStat());
		Region.REGIONS.clear();
	}

}
