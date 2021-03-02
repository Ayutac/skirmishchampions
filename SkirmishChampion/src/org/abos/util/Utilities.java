package org.abos.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Random;
import java.util.function.Consumer;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

/**
 * Contains several utility methods.
 * @author Sebastian Koch
 * @version %I%
 * @since Skirmish Champions 0.1
 */
public class Utilities {
	
	/**
	 * The default encoding.
	 */
	public static final Charset ENCODING = StandardCharsets.UTF_8;
	
	/**
	 * the path to the binary folder
	 * @see #getBinaryDirectory()
	 */
	private static Path BINARY_DIRECTORY = null;
	
	/**
	 * the path to the JAR folder
	 * @see #getJarDirectory()
	 */
	private static Path JAR_DIRECTORY = null;
	
	/**
	 * the path to the application folder
	 * @see #getApplicationDirectory()
	 */
	private static Path APPLICATION_DIRECTORY = null;
	
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
	 * Checks if the given iterable contains <code>null</code> 
	 * and if so, throws an exception featuring the specified name and position within the iterable.
	 * @param iterable The iterable to check for <code>null</code> entries. If either <code>null</code> or empty, this method will return without an exception thrown.
	 * @param iterableName the name of the iterable parameter
	 * @throws NullPointerException If any entry of <code>iterable</code> refers to <code>null</code>.
	 * @see #requireNonNull(Object, String)
	 */
	public static void requireNonNullEntries(Iterable<?> iterable, String iterableName) {
		if (iterable == null)
			return;
		Iterator<?> it = iterable.iterator();
		if (!it.hasNext())
			return;
		int index = 0;
		while (it.hasNext()) {
			if (it.next() == null)
				throw new NullPointerException(String.format(
						"All entries of %s must be specified, but the one at position %d wasn't!", iterableName, index));
			index++;
		}
	}
	
	/**
	 * Checks if the given iterable contains duplicate entries (compared with {@link Objects#equals(Object, Object)})
	 * and if so, throws an exception featuring the specified name and positions within the iterable.
	 * @param iterable The iterable to check for duplicate entries. If either <code>null</code> or empty or only with one entry, this method will return without an exception thrown.
	 * @param iterableName the name of the iterable parameter
	 * @param ignoreNull if suplicate <code>null</code> should be ignored
	 * @throws IllegalArgumentException If any two entries of <code>iterable</code> are equal.
	 * @see Objects#equals(Object, Object)
	 */
	public static void requireDifferentEntries(Iterable<?> iterable, String iterableName, boolean ignoreNull) {
		if (iterable == null)
			return;
		Iterator<?> it1 = iterable.iterator(), it2;
		if (!it1.hasNext())
			return;
		int i1 = 0, i2;
		Object obj1, obj2;
		while (it1.hasNext()) {
			obj1 = it1.next();
			it2 = iterable.iterator();
			for (i2 = 0; i2 <= i1; i2++)
				it2.next();
			while (it2.hasNext()) {
				obj2 = it2.next();
				if (!(ignoreNull && obj1 == null) && Objects.equals(obj1, obj2))
					throw new IllegalArgumentException(String.format(
							"All entries of %s must be different, but these at position %d and %d are equal!", iterableName, i1, i2));
				i2++;
			}
			i1++;
		}
	}
	
	/**
	 * Checks if the given iterable contains duplicate entries (compared with {@link Objects#equals(Object, Object)})
	 * and if so, throws an exception featuring the specified name and positions within the iterable.
	 * @param iterable The iterable to check for duplicate entries. If either <code>null</code> or empty or only with one entry, this method will return without an exception thrown.
	 * @param iterableName the name of the iterable parameter
	 * @throws IllegalArgumentException If any two entries of <code>iterable</code> are equal.
	 * @see Objects#equals(Object, Object)
	 */
	public static void requireDifferentEntries(Iterable<?> iterable, String iterableName) {
		requireDifferentEntries(iterable, iterableName, false);
	}
	
	/**
	 * Sums two integers. When overflow occurs in either direction, the maximal or minimal integer is returned instead.
	 * More specifically, if both integers are positive (negative) and the sum is smaller (greater) than one of the summands,
	 * {@link Integer#MAX_VALUE} ({@link Integer#MIN_VALUE}) is returned instead, and if the first summand is positive (negative)
	 * while the second one is negative (positive) and the sum is greater (smaller) than the first summand, 
	 * {@link Integer#MIN_VALUE} ({@link Integer#MAX_VALUE}) is returned. If none of these cases apply, i.e. if no overflow
	 * happens, <code>a+b</code> is returned.
	 * @param a the first summand
	 * @param b the second summand
	 * @return the sum of the summands or the max/min int value if an overflow occurs
	 * @see #addWithoutOverflow(int...)
	 * @see Integer#MAX_VALUE
	 * @see Integer#MIN_VALUE
	 */
	public static int addWithoutOverflow(int a, int b) {
		int sum = a+b;
		if (a >= 0) {
			if (b >= 0) {
				if (sum < a)
					return Integer.MAX_VALUE;
				return sum;
			}
			else { // if b < 0
				if (sum >= a)
					return Integer.MIN_VALUE;
				return sum;
			}
		}
		else { // if a < 0
			if (b <= 0) {
				if (sum > a)
					return Integer.MIN_VALUE;
				return sum;
			}
			else { // if b > 0
				if (sum <= a) {
					return Integer.MAX_VALUE;
				}
				return sum;
			}
		}
	}
	
	/**
	 * Sums a number of integers. If all integers are positive, sum will be capped at {@link Integer#MAX_VALUE}.
	 * If all integers are negative, sum will be capped at {@link Integer#MIN_VALUE}. If the summands contain
	 * different signs the result of this method is as unpredictable as with normal overflow summation, but 
	 * unlike there in this case the result of this method can depend on the order of the summands.
	 * @param summands the summands to sum
	 * @return a sum of the given integers with overflow caps; an empty array will return <code>0</code>
	 * @throws NullPointerException If <code>summands</code> refers to <code>null</code>
	 * @see #addWithoutOverflow(int, int)
	 * @see Integer#MAX_VALUE
	 * @see Integer#MIN_VALUE
	 */
	public static int addWithoutOverflow(int... summands) {
		requireNonNull(summands, "summands");
		if (summands.length == 0)
			return 0;
		if (summands.length == 1)
			return summands[0];
		int sum = summands[0];
		for (int i = 1; i < summands.length; i++)
			sum = addWithoutOverflow(sum, summands[i]);
		return sum;
	}
	
	/**
	 * Multiplies two integers. When overflow occurs in either direction, the maximal or minimal integer is returned instead.
	 * More specifically, if the product divided by the second factor doesn't equal the first one, if both integers are positive/negative,
	 * {@link Integer#MAX_VALUE} is returned instead, and if only one factor is negative {@link Integer#MIN_VALUE} is returned. 
	 * If none of these cases apply, i.e. if no overflow happens, <code>a*b</code> is returned. Be aware that for
	 * checking a division is done, so this might not be optimal for runtime critical applications.
	 * @param a the first factor
	 * @param b the second factor
	 * @return the product of the factors or the max/min int value if an overflow occurs
	 * @see #multWithoutOverflow(int...)
	 * @see Integer#MAX_VALUE
	 * @see Integer#MIN_VALUE
	 */
	public static int multWithoutOverflow(int a, int b) {
		if (b == 0) // avoid division by zero
			return 0;
		int product = a*b;
		if (a == product / b)
			return product;
		if ((a >= 0 && b < 0) || (a <= 0 && b > 0))
			return Integer.MIN_VALUE;
		assert (a >= 0 && b > 0) || (a <= 0 && b < 0);
		return Integer.MAX_VALUE;
	}
	
	/**
	 * Multiplies a number of integers. If all integers are positive, sum will be capped at {@link Integer#MAX_VALUE}.
	 * In other cases the result of this method is as unpredictable as with normal overflow summation, but 
	 * unlike there in this case the result of this method can depend on the order of the factors.
	 * @param factors the factors to multiply
	 * @return a product of the given integers with overflow cap; an empty array will return <code>1</code>
	 * @throws NullPointerException If <code>factors</code> refers to <code>null</code>
	 * @see #multWithoutOverflow(int, int)
	 * @see Integer#MAX_VALUE
	 */
	public static int multWithoutOverflow(int... factors) {
		requireNonNull(factors, "summands");
		if (factors.length == 0)
			return 1;
		if (factors.length == 1)
			return factors[0];
		int prod = factors[0];
		for (int i = 1; i < factors.length; i++)
			prod = multWithoutOverflow(prod, factors[i]);
		return prod;
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
	 * Chooses a random element of a collection.
	 * @param <T> the object type to choose from
	 * @param collection the collection to choose from, not <code>null</code>.
	 * @param rng The RNG to use. If <code>null</code>, one will be created.
	 * @return one element of <code>collection</code> chosen at random.
	 * @throws NullPointerException If <code>collection</code> refers to <code>null</code>.
	 * @throws IllegalArgumentException If <code>collection</code> is empty.
	 * @see #randomlyChoose(List, Random)
	 * @see #randomlyChoose(Map, Random)
	 */
	public static <T> T randomlyChoose(Collection<T> collection, Random rng) {
		requireNonNull(collection, "collection");
		if (collection.size() == 0)
			throw new IllegalArgumentException("distribution can't be empty!");
		if (rng == null)
			rng = new Random();
		int choosenIndex = rng.nextInt(collection.size());
		// find the selected element
		Iterator<T> it = collection.iterator();
		for (int k = 0; k < choosenIndex; k++)
			it.next();
		return it.next();
	}
	
	/**
	 * Chooses a random element of a list.
	 * @param <T> the object type to choose from
	 * @param list the list to choose from, not <code>null</code>.
	 * @param rng The RNG to use. If <code>null</code>, one will be created.
	 * @return one element of <code>list</code> chosen at random.
	 * @throws NullPointerException If <code>list</code> refers to <code>null</code>.
	 * @throws IllegalArgumentException If <code>list</code> is empty.
	 * @see #randomlyChoose(Collection, Random)
	 * @see #randomlyChoose(Map, Random)
	 */
	public static <T> T randomlyChoose(List<T> list, Random rng) {
		requireNonNull(list, "collection");
		if (list.size() == 0)
			throw new IllegalArgumentException("distribution can't be empty!");
		if (rng == null)
			rng = new Random();
		return list.get(rng.nextInt(list.size()));
	}
	
	/**
	 * Chooses a random key of a map with the entries being weights.
	 * @param <T> the object type to choose from
	 * @param distribution the distribution to choose from, not <code>null</code> and without negative entry values.
	 * @param rng The RNG to use. If <code>null</code>, one will be created.
	 * @return one of the keys from <code>distribution</code> chosen at random with the weights respected.
	 * @throws NullPointerException If <code>distribution</code> refers to <code>null</code>.
	 * @throws IllegalArgumentException If <code>distribution</code> is empty OR any value of <code>distribution</code> is negative OR
	 * if the sum of the weights causes an overflow.
	 * @see #randomlyChoose(Collection, Random)
	 * @see #randomlyChoose(List, Random)
	 */
	public static <T> T randomlyChoose(Map<T,Integer> distribution, Random rng) {
		requireNonNull(distribution, "distribution");
		if (distribution.size() == 0)
			throw new IllegalArgumentException("distribution can't be empty!");
		if (rng == null)
			rng = new Random();
		// converted to list to ensure the iteration stays the same
		List<Entry<T,Integer>> entries = new ArrayList<>(distribution.entrySet());
		// calculate cumulative sum
		int[] culSum = new int[entries.size()];
		Integer currentValue = entries.get(0).getValue();
		culSum[0] = currentValue == null ? 0 : currentValue;
		for (int i = 1; i < culSum.length; i++) {
			currentValue = entries.get(i).getValue();
			if (currentValue == null)
				currentValue = 0;
			else if (currentValue < 0)
				throw new IllegalArgumentException("No weight can be negative!");
			culSum[i] = addWithoutOverflow(culSum[i-1], currentValue);
			if ((currentValue != 0 && culSum[i] == culSum[i-1]) || (culSum[i-1] != 0 && culSum[i] == currentValue)) 
				throw new IllegalArgumentException("Overflow in weights detected! Reduce weights!");
		}
		// draw random
		int chosen = rng.nextInt(culSum[culSum.length-1]);
		// find entry of draw
		int chosenIndex = 0;
		while (chosenIndex < culSum.length && chosen >= culSum[chosenIndex])
			chosenIndex++;
		// the first condition of the while loop should ALWAYS be fulfilled, but if not the endless loop will be hard to detect without it
		// hence the condition and assertion
		assert chosenIndex < culSum.length;
		return entries.get(chosenIndex).getKey();
	}
	
	/**
	 * Returns the directory of binaries where Utilities.class is located if the currently running application wasn't started from a JAR.
	 * If the currently running application was started from a JAR, the behaviour of this method is undefined.
	 * @return the path to the binary folder
	 * @throws IOException If the location of the source is especially badly malformed. 
	 * @throws SecurityException if a security manager exists and its checkPermission method doesn't allow getting the Protection Domain of <code>Utilities</code>.
	 * @see #loadJarDirectory()
	 * @see #loadApplicationDirectory()
	 */
	public static Path loadBinaryDirectory() throws IOException {
		try {
			BINARY_DIRECTORY = new File(Utilities.class.getProtectionDomain().getCodeSource().getLocation().toURI()).toPath();
			return BINARY_DIRECTORY;
		}
		catch (URISyntaxException ex) {
			throw new IOException(ex);
		}
	}
	
	/**
	 * Returns the directory of binaries where Utilities.class is located if the currently running application wasn't started from a JAR.
	 * If the currently running application was started from a JAR, the behaviour of this method is undefined.
	 * @return the path to the binary folder, might be <code>null</code> if the directory wasn't loaded with {@link #loadBinaryDirectory()}
	 * @see #loadBinaryDirectory()
	 * @see #checkBinaryDirectory()
	 * @see #getJarDirectory()
	 * @see #getApplicationDirectory()
	 */
	public static Path getBinaryDirectory() {
		return BINARY_DIRECTORY;
	}
	
	/**
	 * Checks if the binary folder has been loaded yet and throws an exception if not.
	 * @throws IllegalStateException If the binary folder hasn't been loaded yet.
	 * @see #loadBinaryDirectory()
	 * @see #getBinaryDirectory()
	 * @see #checkJarDirectory()
	 * @see #checkApplicationDirectory()
	 */
	public static void checkBinaryDirectory() {
		if (BINARY_DIRECTORY == null)
			throw new IllegalStateException("Binary folder hasn't been located yet!");
	}
	
	/**
	 * Returns the directory of the JAR of the currently running application, if it was started from a JAR.
	 * If the currently running application wasn't started from a JAR, the behaviour of this method is undefined.
	 * @return the path to the JAR folder
	 * @throws IOException If the location of the source is especially badly malformed. 
	 * @throws SecurityException if a security manager exists and its checkPermission method doesn't allow getting the Protection Domain of <code>Utilities</code>.
	 * @see #loadBinaryDirectory()
	 * @see #loadApplicationDirectory()
	 */
	public static Path loadJarDirectory() throws IOException {
		if (BINARY_DIRECTORY == null)
			loadBinaryDirectory();
		JAR_DIRECTORY = BINARY_DIRECTORY.getParent();
		return JAR_DIRECTORY;
	}
	
	/**
	 * Returns the directory of the JAR of the currently running application, if it was started from a JAR.
	 * If the currently running application wasn't started from a JAR, the behaviour of this method is undefined.
	 * @return the path to the JAR folder, might be <code>null</code> if the directory wasn't loaded with {@link #loadJarDirectory()}
	 * @see #loadJarDirectory()
	 * @see #getBinaryDirectory()
	 * @see #getApplicationDirectory()
	 */
	public static Path getJarDirectory() {
		return JAR_DIRECTORY;
	}
	
	/**
	 * Checks if the JAR folder has been loaded yet and throws an exception if not.
	 * @throws IllegalStateException If the JAR folder hasn't been loaded yet.
	 * @see #loadJarDirectory()
	 * @see #getJarDirectory()
	 * @see #checkBinaryDirectory()
	 * @see #checkApplicationDirectory()
	 */
	public static void checkJarDirectory() {
		if (JAR_DIRECTORY == null)
			throw new IllegalStateException("JAR folder hasn't been located yet!");
	}
	
	/**
	 * Returns the directory of the currently running application. If it was started from a JAR,
	 * this is the folder where the JAR is in. Else it is the parent folder of the binary folder.
	 * If the currently running application wasn't started from a JAR, the behaviour of this method is undefined.
	 * @return the path to the application folder
	 * @throws IOException If the location of the source is especially badly malformed. 
	 * @throws SecurityException if a security manager exists and its checkPermission method doesn't allow getting the Protection Domain of <code>Utilities</code>.
	 * @see #loadBinaryDirectory()
	 * @see #loadJarDirectory()
	 */
	public static Path loadApplicationDirectory() throws IOException {
		if ("jar".equals(Utilities.class.getResource("").getProtocol())) {
			if (JAR_DIRECTORY == null)
				loadJarDirectory();
			APPLICATION_DIRECTORY = JAR_DIRECTORY;
		}
		else {
			if (BINARY_DIRECTORY == null)
				loadBinaryDirectory();
			APPLICATION_DIRECTORY = BINARY_DIRECTORY.getParent();
		}
		return APPLICATION_DIRECTORY;
	}
	
	/**
	 * Returns the directory of the currently running application. If it was started from a JAR,
	 * this is the folder where the JAR is in. Else it is the parent folder of the binary folder.
	 * If the currently running application wasn't started from a JAR, the behaviour of this method is undefined.
	 * @return the path to the application folder, might be <code>null</code> if the directory wasn't loaded with {@link #loadApplicationDirectory()}
	 * @see #getBinaryDirectory()
	 * @see #getJarDirectory()
	 * @see #loadApplicationDirectory()
	 */
	public static Path getApplicationDirectory() {
		return APPLICATION_DIRECTORY;
	}
	
	/**
	 * Checks if the application folder has been loaded yet and throws an exception if not.
	 * @throws IllegalStateException If the application folder hasn't been loaded yet.
	 * @see #loadApplicationDirectory()
	 * @see #getApplicationDirectory()
	 * @see #checkBinaryDirectory()
	 * @see #checkJarDirectory()
	 */
	public static void checkApplicationDirectory() {
		if (APPLICATION_DIRECTORY == null)
			throw new IllegalStateException("Application folder hasn't been located yet!");
	}
	
	/**
	 * Iterates the savables and writes them into the string builder, separated by <code>separator</code>.
	 * @param iterable the iterable to save
	 * @param separator The separator between two entries. It is not checked if the save string of the entries contains
	 * the separator, which might lead to issues if the save string method is not implemented with that in mind.
	 * @param s the string builder to append to
	 * @throws NullPointerException If any parameter or entry of the iterable refer to <code>null</code>.
	 */
	public static void iterableToSaveString(Iterable<? extends SaveString> iterable, String separator, StringBuilder s) {
		requireNonNull(iterable, "iterable");
		requireNonNullEntries(iterable, "iterable");
		requireNonNull(separator, "separator");
		requireNonNull(s, "s");
		Iterator<? extends SaveString> it = iterable.iterator();
		while (it.hasNext()) {
			it.next().toSaveString(s);
			if (it.hasNext())
				s.append(separator);
		}
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
	 * @param html if an additional HTML linebreak should be inserted
	 * @return an instance of the (anonymous) simplest formatter class
	 */
	public static Formatter createSimplestFormatter(boolean html) {
		if (html)
			return new Formatter() {
				@Override public String format(LogRecord record) {
					return record.getMessage()+"<br>"+System.lineSeparator();
				}
			};
		return new Formatter() {
			@Override public String format(LogRecord record) {
				return record.getMessage()+System.lineSeparator();
			}
		};
	}

}
