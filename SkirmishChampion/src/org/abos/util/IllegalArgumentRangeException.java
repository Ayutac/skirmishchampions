package org.abos.util;

/**
 * Thrown when an argument is parsed and happens to be outside of the valid ranges.
 * This includes wrong types.
 * @author Sebastian Koch
 * @version %I%
 * @since Skirmish Champions 0.1
 * @see ParseException
 * @see IllegalArgumentTypeException
 */
public class IllegalArgumentRangeException extends ParseException {

	/**
	 * the serial version UID
	 */
	private static final long serialVersionUID = 5810920687793799944L;

	/**
	 * Constructs a {@link IllegalArgumentRangeException} with no detail message.
	 */
	public IllegalArgumentRangeException() {}

	/**
	 * Constructs a {@link IllegalArgumentRangeException} with the specified detail message.
	 * @param message the detail message
	 */
	public IllegalArgumentRangeException(String message) {
		super(message);
	}

	/**
	 * Constructs a {@link IllegalArgumentRangeException} with the specified cause and a detail message of <code>(cause==null ? null : cause.toString())</code> 
	 * (which typically contains the class and detail message of cause).
	 * This constructor is useful for exceptions that are little more than wrappers for other throwables 
	 * (for example, {@link java.security.PrivilegedActionException}).
	 * @param cause the cause (which is saved for later retrieval by {@link #getCause()} method). 
	 * (A <code>null</code> value is permitted, and indicates that the cause is nonexistent or unknown.)
	 */
	public IllegalArgumentRangeException(Throwable cause) {
		super(cause);
	}

	/**
	 * Constructs a {@link IllegalArgumentRangeException} with the specified detail message an dcause. <br/><br/>
	 * Note that the detail message associated with <code>cause</code> is not automatically incorporated in this exception's detail message.
	 * @param message the detail message (which is saved for later retrieval by the {@link #getMessage()} method).
	 * @param cause the cause (which is saved for later retrieval by {@link #getCause()} method). 
	 * (A <code>null</code> value is permitted, and indicates that the cause is nonexistent or unknown.)
	 */
	public IllegalArgumentRangeException(String message, Throwable cause) {
		super(message, cause);
	}

}
