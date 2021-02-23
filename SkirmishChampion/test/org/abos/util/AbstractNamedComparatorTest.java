package org.abos.util;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Comparator;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * @author Sebastian Koch
 * @version %I%
 * @since Skirmish Champions 0.7
 */
public class AbstractNamedComparatorTest {
	
	public static AbstractNamedComparator<String> getComparator() {
		return new AbstractNamedComparator<String>("test") {
			@Override public int compare(String o1, String o2) {
				if (o1 == o2)
					return 0;
				if (o1 == null)
					return Integer.MIN_VALUE;
				if (o2 == null)
					return Integer.MAX_VALUE;
				return o1.compareTo(o2);
			}
		};
	}
	
	public static AbstractNamedComparator<String> getAnotherComparator() {
		return new AbstractNamedComparator<String>("test") {
			@Override public int compare(String o1, String o2) {
				if (o1 == o2)
					return 0;
				if (o1 == null)
					return Integer.MIN_VALUE;
				if (o2 == null)
					return Integer.MAX_VALUE;
				return o1.compareTo(o2);
			}
		};
	}
	
	public static Comparator<String> getPlainComparator() {
		return new Comparator<String>() {
			@Override public int compare(String o1, String o2) {
				if (o1 == o2)
					return 0;
				if (o1 == null)
					return Integer.MIN_VALUE;
				if (o2 == null)
					return Integer.MAX_VALUE;
				return o1.compareTo(o2);
			}
		};
	}

	@Test
	public void hashCodeTest1() {
		Comparator<String> t1 = getComparator();
		Comparator<String> t2 = getComparator();
		assertTrue(t1.hashCode() == t2.hashCode());
	}

	@Test
	public void hashCodeTest2() {
		Comparator<String> t1 = getComparator();
		Comparator<String> t2 = getAnotherComparator();
		assertFalse(t1.hashCode() == t2.hashCode());
	}

	@Test
	public void hashCodeTest3() {
		Comparator<String> t1 = getPlainComparator();
		Comparator<String> t2 = getPlainComparator();
		assertFalse(t1.hashCode() == t2.hashCode());
	}

}
