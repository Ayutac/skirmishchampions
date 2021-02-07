package org.abos.util;

import static org.junit.jupiter.api.Assertions.*;

import org.abos.util.Utilities;
import org.junit.jupiter.api.Test;

/**
 * @author Sebastian Koch
 * @version %I%
 * @since 0.1
 */
public class UtilitiesTest {
	
	@Test
	public void testAlterating1() {
		assertArrayEquals(new int[]{0,1,2}, Utilities.createAlteratingLowerFirst(0,3));
	}
	
	@Test
	public void testAlterating2() {
		assertArrayEquals(new int[]{1,0,2}, Utilities.createAlteratingLowerFirst(1,3));
	}
	
	@Test
	public void testAlterating3() {
		assertArrayEquals(new int[]{2,1,0}, Utilities.createAlteratingLowerFirst(2,3));
	}
	
	@Test
	public void testParsingIllegalInts() {
		assertThrows(NumberFormatException.class, () -> Integer.parseInt(Integer.toString(Integer.MAX_VALUE)+"0"));
	}

}
