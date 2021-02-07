package org.abos.sc.core;

import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;

import org.abos.util.IllegalNumberOfArgumentsException;
import org.abos.util.ParseException;
import org.abos.util.ParsedIdNotFoundException;
import org.abos.util.Utilities;

/**
 * Contains a bunch of character in a battle formation.
 * The formation is a deciding factor in battles to find
 * out which characters get attacked. Only non null entries
 * will be iterated when objects of this class are used
 * in for each loops.
 * @author Sebastian Koch
 * @version %I%
 * @since 0.1
 */
public class BattleFormation implements Iterable<Character>, Cloneable, ChallengeRatable {
	
	/**
	 * The maximum number of rows in formations.
	 */
	public static final int ROW_NUMBER = 2;
	
	/**
	 * The maximum number of columns in formations.
	 */
	public static final int COL_NUMBER = 3;
	
	/**
	 * The maximum number of characters in a battle formation. Equals <code>ROW_NUMBER*COL_NUMBER</code>.
	 * @see #ROW_NUMBER
	 * @see #COL_NUMBER
	 */
	public static final int MAX_CHAR_NUMBER = ROW_NUMBER*COL_NUMBER;
	
	/**
	 * The separator between different characters when putting them into a save string.
	 * @see #toSaveString(StringBuilder)
	 */
	public static final char CHARACTER_SEPARATOR = ',';
	
	/**
	 * Provides an iterator over all non <code>null</code> characters in a formation.
	 * @author Sebastian Koch
	 * @version %I%
	 * @since 0.1
	 */
	protected class BattleFormationIterator implements Iterator<Character> {
		
		/**
		 * The current postion of the iterator. Modulo {@link BattleFormation#COL_NUMBER} is the column,
		 * divided by it is the row.
		 */
		protected int pos = 0;
		
		/**
		 * The number of characters already iterated over, including the current one.
		 */
		protected int count = 0;
		
		/**
		 * Returns the next non <code>null</code> character of this formation if there is any left.
		 * @return the next non <code>null</code> character of this formation
		 * @throws NoSuchElementException If there are no non <code>null</code> characters left in the formation.
		 */
		@Override
		public Character next() {
			if (!hasNext())
				throw new NoSuchElementException("No more non null characters in this formation!");
			while (pos < MAX_CHAR_NUMBER && characters[pos / COL_NUMBER][pos % COL_NUMBER] == null) {
				pos++;
			}
			count++;
			return characters[pos / COL_NUMBER][pos++ % COL_NUMBER];
		}
		
		/**
		 * Returns <code>true</code> if there is at least one non <code>null</code> character left in this formation.
		 * @return <code>true</code> if there is at least one non <code>null</code> character left in this formation, else <code>false</code>
		 */
		@Override
		public boolean hasNext() {
			return count < size;
		}
	}
	
	/**
	 * The two-dimensional array of characters in this formation.
	 */
	protected Character[][] characters;
	
	/**
	 * The number of non <code>null</code> characters in this formation.
	 */
	private final int size;
	
	/**
	 * Creates a new battle formation with the given characters. The characters are copied by reference, the array is not.
	 * @param characters A two-dimensional array of characters in this formation, non <code>null</code>, rectangular and must contain at least one non <code>null</code> entry (formations can't be empty).
	 * <code>character[i][j]</code> is treated as the character on row <code>i</code> and column <code>j</code>, where <code>i == 0</code> indicates the front and <code>j == 0</code> indicates the left flank of the formation.
	 * @throws NullPointerException If <code>characters</code> or any of its one-dimensional subarrays refers to <code>null</code>.
	 * @throws IllegalArgumentException If <code>characters</code> isn't rectangular or contains no non <code>null</code> character.
	 */
	public BattleFormation(Character[][] characters) {
		Utilities.requireNonNull(characters, "characters");
		if (characters.length != ROW_NUMBER)
			throw new IllegalArgumentException("characters must be a rectangular array!");
		int size = 0;
		for (int row = 0; row < ROW_NUMBER; row++) {
			Utilities.requireNonNull(characters[row], "All subarrays of characters");
			if (characters[row].length != COL_NUMBER) 
				throw new IllegalArgumentException("characters must be a rectangular array!"); 
			for (int col = 0; col < COL_NUMBER; col++) {
				if (characters[row][col] != null)
					size++;
			}
		}
		if (size == 0)
			throw new IllegalArgumentException("There must be at least 1 specified character in characters!");
		
		this.characters = new Character[ROW_NUMBER][];
		for (int row = 0; row < ROW_NUMBER; row++)
			this.characters[row] = Arrays.copyOf(characters[row], COL_NUMBER);
		this.size = size;
	}
	
	/**
	 * Returns an iterator over all non <code>null</code> characters of this formation.
	 * @return an iterator over all non <code>null</code> characters of this formation
	 * @see BattleFormationIterator
	 */
	@Override
	public Iterator<Character> iterator() {
		return new BattleFormationIterator();
	}
	
	/**
	 * Returns the character at the specified position.
	 * @param row the row of the character
	 * @param col the column of the character
	 * @return the character at the specified position, may be <code>null</code>
	 * @throws ArrayIndexOutOfBoundsException If <code>row</code> or <code>col</code> is out of bounds.
	 */
	public Character getCharacter(int row, int col) {
		return characters[row][col]; // throws AIOOBE
	}
	
	/**
	 * Returns the number of non <code>null</code> characters in this formation.
	 * @return the number of non <code>null</code> characters in this formation
	 */
	public int getSize() {
		return size;
	}
	
	/**
	 * Returns <code>true</code> if the character at the specified position is <code>null</code> or defeated.
	 * @param row the row of the character
	 * @param col the column of the character
	 * @return <code>true</code> if the character at the specified position is <code>null</code> or defeated, else <code>false</code>
	 * @throws IndexOutOfBoundsException If <code>row</code> or <code>col</code> is out of bounds.
	 * @see #isDefeated()
	 * @see Character#isDefeated()
	 */
	public boolean isDefeated(int row, int col) {
		if (characters[row][col] == null) // throws AIOOBE
			return true;
		return characters[row][col].isDefeated();
	}
	
	/**
	 * Returns <code>true</code> if the entire formation is defeated. 
	 * @return <code>true</code> if the entire formation is defeated, else <code>false</code>
	 * @see #isDefeated(int, int)
	 * @see Character#isDefeated()
	 */
	public boolean isDefeated() {
		for (Character character : this)
			if (!character.isDefeated())
				return false;
		return true;
	}
	
	/**
	 * Restores all characters in this formation.
	 * @see Character#restore()
	 */
	public void restoreAll() {
		for (Character character : this)
			character.restore();
	}
	
	/**
	 * Returns the challenge rating of this formation.
	 * @return the challenge rating of this formation
	 */
	@Override
	public int getChallengeRating() {
		int sum = 0;
		for (Character character : this)
			sum += character.getChallengeRating();
		return sum;
	}

	/**
	 * Returns a hash code value for this formation, computed based on the hash code values
	 * of the underlying characters. Two equal formations will return the same hash code value.
	 * @return a hash code value for this formation
	 * @see #equals(Object)
	 * @see Character#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.deepHashCode(characters);
		return result;
	}

	/**
	 * Tests if another object is equal to this encounter. Two encounters are equal if and only if
	 * their underlying characters are equal.
	 * @return <code>true</code> if the other object is an encounter equal to this one, else <code>false</code>
	 * @see #hashCode()
	 * @see Character#equals(Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BattleFormation other = (BattleFormation) obj;
		if (!Arrays.deepEquals(characters, other.characters))
			return false;
		return true;
	}
	
	/**
	 * Returns a deep clone of this formation.
	 * @return a deep clone of this formation
	 * @see Character#clone()
	 */
	@Override
	public Object clone() {
		Character[][] clonedChars = new Character[ROW_NUMBER][COL_NUMBER];
		for (int row = 0; row < ROW_NUMBER; row++)
			for (int col = 0; col < COL_NUMBER; col++)
				if (characters[row][col] != null)
					clonedChars[row][col] = (Character)characters[row][col].clone();
		return new BattleFormation(clonedChars);
	}
	
	/**
	 * Saves the battle formation to a string builder.
	 * @param s the string builder to append to
	 * @throws NullPointerException If <code>s</code> refers to <code>null</code>.
	 * @see #toSaveString()
	 * @see #parse(String)
	 */
	public void toSaveString(StringBuilder s) {
		Utilities.requireNonNull(s, "s");
		// if changed, also change the parse function and the documentation of it
		for (int row = 0; row < ROW_NUMBER; row++)
			for (int col = 0; col < COL_NUMBER; col++) {
				if (characters[row][col] != null)
					s.append(characters[row][col].getId());
				// note that the last separator gets ignored by String#split()
				s.append(CHARACTER_SEPARATOR);
			}
	}
	
	/**
	 * Returns the battle formation as a string for saving purposes, i.e. in the form needed for {@link #parse(String)}.
	 * @return the battle formation as a string for saving purposes
	 * @see #toSaveString(StringBuilder)
	 * @see #parse(String)
	 */
	public String toSaveString() {
		StringBuilder s = new StringBuilder();
		toSaveString(s);
		return s.toString();
	}

	/**
	 * Adds the specified character with their health to the string builder. 
	 * @param row the row of the character in the formation
	 * @param col the column of the character in the formation
	 * @param s the string builder to append to
	 * @throws NullPointerException If <code>s</code> refers to <code>null</code>.
	 * @throws ArrayIndexOutOfBoundsException If either <code>row</code> or <code>col</code> is out of bounds.
	 * @see Character#toBattleStatString()
	 * @see #toString()
	 */
	protected void appendToStringBuilder(int row, int col, StringBuilder s) {
		Utilities.requireNonNull(s, "s");
		if (characters[row][col] == null) {
			s.append("-empty-");
			return;
		}
		s.append(characters[row][col].toBattleStatString());
	}
	
	/**
	 * Returns a string representing this formation, which is especially nice if
	 * {@link #ROW_NUMBER} equals 2 and {@link #COL_NUMBER} equals 3, else same as
	 * calling {@link #toSaveString()}.
	 * @return a string representing this formation
	 */
	@Override
	public String toString() {
		// a bit convoluted way to check that ROW == 2 && COL == 3 but this way there is no compiler warning :D
		if (ROW_NUMBER < COL_NUMBER && Math.min(ROW_NUMBER, COL_NUMBER) == 2 && Math.max(ROW_NUMBER, COL_NUMBER) == 3) {
			StringBuilder s = new StringBuilder();
			s.append("Front Left: ");
			appendToStringBuilder(0, 0, s);
			s.append("; Front Middle: ");
			appendToStringBuilder(0, 1, s);
			s.append("; Front Right: ");
			appendToStringBuilder(0, 2, s);
			s.append("; Back Left: ");
			appendToStringBuilder(1, 0, s);
			s.append("; Back Middle: ");
			appendToStringBuilder(1, 1, s);
			s.append("; Back Right: ");
			appendToStringBuilder(1, 2, s);
			return s.toString();
		}
		else
			return toSaveString();
	}
	
	/**
	 * Creates a battle formation with the first {@value #MAX_CHAR_NUMBER} specified characters. Any additional character will be ignored.
	 * First the first row will be filled starting from first column, then the second row etc.
	 * If not enough characters are supplied, the rest will be filled with <code>null</code>.
	 * @param characters the characters to put into the battle formation
	 * @return a battle formation with the first {@value #MAX_CHAR_NUMBER} specified characters
	 * @throws NullPointerException If the array of characters refers to <code>null</code>.
	 * @throws IllegalArgumentException If no character is given or all characters refer to <code>null</code>.
	 * @see #BattleFormation(Character[][])
	 */
	public static BattleFormation createFormation(Character... characters) {
		Utilities.requireNonNull(characters, "characters");
		if (characters.length == 0)
			throw new IllegalArgumentException("At least one character must be given!");
		Character[][] chars = new Character[ROW_NUMBER][COL_NUMBER];
		int index = 0;
		for (int row = 0; row < ROW_NUMBER; row++) {
			for (int col = 0; col < COL_NUMBER; col++) {
				chars[row][col] = characters[index++];
				if (index >= characters.length)
					break;
			}
			if (index >= characters.length)
				break;
		}
		return new BattleFormation(chars); // throws IAE if all characters are null
	}
	
	/**
	 * Parses a string representation of a battle formation into a proper formation.
	 * The formation is given by a list of at least 1 and up to {@value #MAX_CHAR_NUMBER} 
	 * character IDs, separated by the {@value #CHARACTER_SEPARATOR} character without additional
	 * whitespaces (a trailing {@value #CHARACTER_SEPARATOR} is allowed). A {@link ParseException} will be thrown if the string cannot be parsed correctly.
	 * @param s the string to parse
	 * @return a battle formation matching the string
	 * @see #toSaveString()
	 * @throws NullPointerException If <code>s</code> refers to <code>null</code>.
	 * @throws IllegalNumberOfArgumentsException If there are more than {@value #MAX_CHAR_NUMBER} characters supplied or none at all.
	 * @throws ParsedIdNotFoundException If a character ID supplied by the string isn't found in {@link CharacterBase#CHARACTERS}.
	 */
	public static BattleFormation parse(String s) {
		Utilities.requireNonNull(s, "s");
		Character[][] characters = new Character[ROW_NUMBER][COL_NUMBER];
		String[] split = s.split(String.valueOf(CHARACTER_SEPARATOR));
		assert split.length != 0;
		if (split.length > MAX_CHAR_NUMBER)
			throw new IllegalNumberOfArgumentsException("Too many characters in formation, are "+split.length+" instead of "+MAX_CHAR_NUMBER+" or less!");
		split = Arrays.copyOf(split, MAX_CHAR_NUMBER);
		int index = 0;
		CharacterBase base;
		for (int row = 0; row < ROW_NUMBER; row++)
			for (int col = 0; col < COL_NUMBER; col++) {
				if (split[index] != null && !split[index].isEmpty()) {
					base = CharacterBase.CHARACTERS.lookup(split[index]);
					if (base == null)
						throw new ParsedIdNotFoundException("Unregistered character "+split[index]+" detected for formation!");
					characters[row][col] = new Character(base);
				}
				index++;
			}
		try {
			return new BattleFormation(characters);
		}
		catch (IllegalArgumentException ex) {
			throw new IllegalNumberOfArgumentsException("At least one character must be specified!");
		}
	}

}
