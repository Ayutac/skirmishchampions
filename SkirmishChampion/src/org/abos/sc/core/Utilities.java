package org.abos.sc.core;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

/**
 * Contains several utility methods for Skirmish Champion.
 * @author Sebastian Koch
 * @version %I%
 * @since 0.1
 */
public class Utilities {
	
	public static final Charset ENCODING = StandardCharsets.UTF_8;
	
	/**
	 * Private constructor to avoid instantiation.
	 */
	private Utilities() {}
	
	/**
	 * Checks if the given object is <code>null</code> 
	 * and if so, throws an exception featuring the specified name.
	 * The difference between this method and {@link Objects#requireNonNull(Object, String)}
	 * is that this one automatically adds " must be specified!" after <code>objName</code>
	 * in the exception, making this method a bit more compact in comparison.
	 * @param obj the object to check for <code>null</code>
	 * @param objName the name of the object parameter
	 * @throws NullPointerException If <code>obj</code> refers to <code>null</code>.
	 * @see Objects#requireNonNull(Object, String)
	 */
	public static void requireNonNull(Object obj, String objName) {
		if (obj == null)
			throw new NullPointerException(objName+" must be specified!");
	}
	
	/**
	 * Converts a given array to a string by writing the array entries behind one another
	 * into the given string builder, only separated by the separator. No additional whitespaces
	 * or enclosing brackets will be added, in opposite to {@link Arrays#toString(Object[])}.
	 * Since this method calls {@link StringBuilder#append(Object)}, the objects will be
	 * written as if provided by {@link String#valueOf(Object)}. Note that array entries
	 * which are arrays themselves will not be expanded; this is not a deep "to string" method. 
	 * @param array the array to convert, not <code>null</code>
	 * @param s the string builder to append the array to, not <code>null</code>
	 * @param separator the separator character for between the array entries
	 * @throws NullPointerException If <code>array</code> or <code>s</code> refers to <code>null</code>.
	 * @see #arrayToString(int[], StringBuilder, char)
	 */
	public static void arrayToString(Object[] array, StringBuilder s, char separator) {
		for (int i = 0; i+1 < array.length; i++) {
			s.append(array[i]);
			s.append(separator);
		}
		s.append(array[array.length-1]);
	}
	
	/**
	 * Converts a given array to a string by writing the array entries behind one another,
	 * only separated by the separator. No additional whitespaces
	 * or enclosing brackets will be added, in opposite to {@link Arrays#toString(Object[])}.
	 * Since this method calls {@link StringBuilder#append(Object)}, the objects will be
	 * written as if provided by {@link String#valueOf(Object)}. Note that array entries
	 * which are arrays themselves will not be expanded; this is not a deep "to string" method. 
	 * @param array the array to convert, not <code>null</code>
	 * @param separator the separator character for between the array entries
	 * @return a string describing this array
	 * @throws NullPointerException If <code>array</code> refers to <code>null</code>.
	 * @see #arrayToString(Object[], StringBuilder, char)
	 * @see #arrayToString(int[], char)
	 */
	public static String arrayToString(Object[] array, char separator) {
		StringBuilder s = new StringBuilder();
		arrayToString(array, s, separator);
		return s.toString();
	}
	
	/**
	 * Converts a given array to a string by writing the array entries behind one another
	 * into the given string builder, only separated by the separator. No additional whitespaces
	 * or enclosing brackets will be added, in opposite to {@link Arrays#toString(int[])}. 
	 * @param array the array to convert, not <code>null</code>
	 * @param s the string builder to append the array to, not <code>null</code>
	 * @param separator the separator character for between the array entries
	 * @throws NullPointerException If <code>array</code> or <code>s</code> refers to <code>null</code>.
	 * @see #arrayToString(Object[], StringBuilder, char)
	 */
	public static void arrayToString(int[] array, StringBuilder s, char separator) {
		for (int i = 0; i+1 < array.length; i++) {
			s.append(array[i]);
			s.append(separator);
		}
		s.append(array[array.length-1]);
	}
	
	/**
	 * Converts a given array to a string by writing the array entries behind one another
	 * into the given string builder, only separated by the separator. No additional whitespaces
	 * or enclosing brackets will be added, in opposite to {@link Arrays#toString(int[])}. 
	 * @param array the array to convert, not <code>null</code>
	 * @param separator the separator character for between the array entries
	 * @return a string describing this array
	 * @throws NullPointerException If <code>array</code> refers to <code>null</code>.
	 * @see #arrayToString(int[], StringBuilder, char)
	 * @see #arrayToString(Object[], char)
	 */
	public static String arrayToString(int[] array, char separator) {
		StringBuilder s = new StringBuilder();
		arrayToString(array, s, separator);
		return s.toString();
	}
	
	/**
	 * Converts the given String array into an int array.
	 * @param array the array to convert
	 * @return <code>null</code> if <code>array</code> was <code>null</code>, 
	 * else an int array of the same size as <code>array</code> with the entries parsed accordingly.
	 * @throws NumberFormatException If any array entry does not contain a parsable integer.
	 */
	public static int[] arrayToInt(String[] array) {
		if (array == null)
			return null;
		int[] result = new int[array.length];
		for (int i = 0; i < array.length; i++)
			result[i] = Integer.parseInt(array[i]); // throws NFE
		return result;
	}
	
	/**
	 * Permutes the numbers from <code>0</code> to <code>size</code>.
	 * The permutation works as follows: <code>0</code> is mapped to <code>start</code>, the next entry is <code>start-1</code>, then <code>start+1</code>, then <code>start-2</code> and so on, 
	 * until the bounderies <code>0</code> and <code>size</code> are reached.
	 * @param start the permutation of <code>0</code>
	 * @param size the size of the permutation
	 * @return an array with the permuted entries
	 * @throws IllegalArgumentException If <code>!(0 <= start && start < size)</code>.
	 * @see #createAlteratingHigherFirst(int, int)
	 */
	public static int[] createAlteratingLowerFirst(int start, int size) {
		if (!(0 <= start && start < size))
			throw new IllegalArgumentException("start must be between 0 (inclusive) and max (exclusive)!");
		int[] sel = new int[size];
		sel[0] = start;
		int down = start - 1;
		int up = start + 1;
		int index = 1;
		while (index < size) {
			if (down >= 0)
				sel[index++] = down--;
			if (up < size)
				sel[index++] = up++;
		}
		return sel;
	}
	
	/**
	 * Permutes the numbers from <code>0</code> to <code>size</code>.
	 * The permutation works as follows: <code>0</code> is mapped to <code>start</code>, the next entry is <code>start+1</code>, then <code>start-1</code>, then <code>start+2</code> and so on, 
	 * until the bounderies <code>0</code> and <code>size</code> are reached.
	 * @param start the permutation of <code>0</code>
	 * @param size the size of the permutation
	 * @return an array with the permuted entries
	 * @throws IllegalArgumentException If <code>!(0 <= start && start < size)</code>.
	 * @see #createAlteratingLowerFirst(int, int)
	 */
	public static int[] createAlteratingHigherFirst(int start, int size) {
		if (!(0 <= start && start < size))
			throw new IllegalArgumentException("start must be between 0 (inclusive) and max (exclusive)!");
		int[] sel = new int[size];
		sel[0] = start;
		int down = start - 1;
		int up = start + 1;
		int index = 1;
		while (index < size) {
			if (up < size)
				sel[index++] = up++;
			if (down >= 0)
				sel[index++] = down--;
		}
		return sel;
	}
	
	/**
	 * Checks if the given array is a permutation, i.e. each number from <code>0</code> to <code>array.length-1</code> must appear exactly once in <code>array</code>.
	 * @param array the array to check
	 * @return <code>true</code> if the given array constitutes a permutation of the numbers from <code>0</code> to <code>array.length-1</code>, else false.
	 */
	public static boolean checkPermutation(int[] array) {
		if (array == null)
			return true;
		boolean[] check = new boolean[array.length];
		for (int i = 0; i < array.length; i++) {
			if (array[i] < 0 || array[i] >= array.length) 
				return false;
			check[array[i]] = true;
		}
		for (int i = 0; i < check.length; i++)
			if (!check[i])
				return false;
		return true;
	}
	
	/**
	 * Returns the binary folder where Utilities.class is located if the current program wasn't started from a jar.
	 * @return the path to the binary folder
	 * @throws IOException If the location of the source is especially badly malformed. 
	 * @throws SecurityException if a security manager exists and its checkPermission method doesn't allow getting the Protection Domain of <code>Utilities</code>.
	 */
	public static Path getBinaryDirectory() throws IOException {
		try {
		  return new File(Utilities.class.getProtectionDomain().getCodeSource().getLocation().toURI()).toPath();
		}
		catch (URISyntaxException ex) {
			throw new IOException(ex);
		}
	}
	
	public static Path getJarDirectory() throws IOException {
		return getBinaryDirectory().getParent();
	}
	
	public static Path getApplicationDirectory() throws IOException {
		if ("jar".equals(Utilities.class.getResource("").getProtocol()))
			return getJarDirectory();
		else
			return getBinaryDirectory().getParent();
	}
	
	/**
	 * Reads in lines from a file that are all to be parsed in the same way as specified by the parser.
	 * The file is expected to be encoded in {@link #ENCODING}. Empty lines and lines starting with "//" for comments
	 * are permitted and will not be parsed.
	 * @param path The path to the file to read in
	 * @param lineParser the parser for a single line
	 * @throws NullPointerException If <code>path</code> or <code>lineParser</code> refers to <code>null</code>.
	 * @throws ParseException If an error occurs during the parsing. <code>lineParser</code> is expected to only 
	 * throw {@link RuntimeException}s of this type, anything else will propagate without an additional {@link ParseException}
	 * wrapper which indicates the line where the parsing error occured.
	 * @throws FileNotFoundException If the file specified by <code>path</code> could not be found.
	 * @throws IOException If any I/O error occurs.
	 * @see #ENCODING
	 */
	public static void loadFromFile(Path path, Consumer<String> lineParser) throws IOException {
		requireNonNull(path, "path");
		requireNonNull(lineParser, "lineParser");
		int lineCounter = 0;
		for (String line : Files.readAllLines(path, ENCODING)) {
			lineCounter++;
			if (line.isBlank() || line.startsWith("//"))
				continue;
			try {
				lineParser.accept(line);
			}
			catch (ParseException ex) {
				throw new ParseException(String.format("Parsing Error in line %d: %s", lineCounter, line),ex); 
			}
		}
	}
	
	/**
	 * Creates the simplest formatter for logs possible, i.e. one that disregards all parts of the log record except the message.
	 * A line break as given by {@link System#lineSeparator()} is appended to the message.
	 * @return an instance of the (anonymous) simplest formatter class
	 */
	public static Formatter createSimplestFormatter() {
		return new Formatter() {
			@Override public String format(LogRecord record) {
				return record.getMessage()+System.lineSeparator();
			}
		};
	}
	
	/**
	 * Returns a comparator that sorts objects with names alphabetically by their names.
	 * <code>null</code> is permitted for both objects to compare and names of the objects to compare.
	 * <code>null</code> is smaller than any other instance of <code>T</code> and an object with 
	 * a <code>null</code> name is smaller than any other non <code>null</code> instance of <code>T</code>,
	 * except of course in both cases if both objects respectively their names are <code>null</code>,
	 * in which this comparator evaluates them as equal.  
	 * @param <T> the class extending {@link Name}
	 * @return A comparator for <code>T</code>.
	 */
	public static <T extends Name> Comparator<T> createNameComparator() {
		return new Comparator<T>() {
			@Override
			public int compare(T o1, T o2) {
				if (o1 == o2)
					return 0;
				if (o1 == null)
					return Integer.MIN_VALUE;
				if (o2 == null)
					return Integer.MAX_VALUE;
				if (o1.getName() == o2.getName())
					return 0;
				if (o1.getName() == null)
					return Integer.MIN_VALUE;
				if (o2.getName() == null)
					return Integer.MAX_VALUE;
				return o1.getName().compareTo(o2.getName());
			}
		};
	}

}
