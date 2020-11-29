package org.abos.sc.core;

import static org.junit.jupiter.api.Assertions.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;

public class CharacterTest {
	
	public static String ERIN = "twi_erin;Erin Solstice;The Wandering Inn;Earthers,Liscor,Innfamily;50,40,40,40,10,60,80,30;5;2";
	
	@Test
	public void testParsing() {
		CharacterBase c = CharacterBase.parse(ERIN, false);
		assertEquals(50, c.getPrimaryStat(StatsPrimary.STRENGTH));
	}
	
	@Test
	public void testParseAndUnparse() {
		CharacterBase c = CharacterBase.parse(ERIN, false);
		assertEquals(ERIN, c.toSaveString());
	}
	
	@Test
	public void testUnparseAndParse() {
		CharacterBase c = new CharacterBase("twi_erin","Erin Solstice","The Wandering Inn",new String[] {"Earthers","Liscor","Innfamily"},new int[] {50,40,40,40,10,60,80,30},StatsPrimary.CHARISMA,StatsSecondary.ELOQUENCE, false);
		assertEquals(c, CharacterBase.parse(c.toSaveString(), false));
	}
	
	@Test
	public void testParseData() throws IOException {
		Path path = Utilities.getBinaryDirectory().resolveSibling("resources").resolve("chars_twi.txt");
		Utilities.loadFromFile(path, CharacterBase::parse);
		System.out.println(String.format("%d: %s", Character.CHARACTERS.size(), Character.CHARACTERS));
		//System.out.println(Character.CHARACTERS.lookup("twi_erin").getPreferredAttackStat());
		//System.out.println(Character.CHARACTERS.lookup("twi_erin").getPreferredDamageStat());
		Character.CHARACTERS.clear();
	}

}
