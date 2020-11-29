package org.abos.sc.core;

/**
 * Thrown when an argument is parsed and happens to be of the wrong type.
 * This exception is often caused by {@link NumberFormatException}.
 * @author Sebastian Koch
 * @version %I%
 * @since 0.1
 * @see ParseException
 * @see NumberFormatException
 */
public class IllegalArgumentTypeException extends IllegalArgumentRangeException {

	/**
	 * the serial version UID
	 */
	private static final long serialVersionUID = 1727688910567459289L;

	/**
	 * Constructs a {@link IllegalArgumentTypeException} with no detail message.
	 */
	public IllegalArgumentTypeException() {}

	/**
	 * Constructs a {@link IllegalArgumentTypeException} with the specified detail message.
	 * @param message the detail message
	 */
	public IllegalArgumentTypeException(String message) {
		super(message);
	}

	/**
	 * Constructs a {@link IllegalArgumentTypeException} with the specified cause and a detail message of <code>(cause==null ? null : cause.toString())</code> 
	 * (which typically contains the class and detail message of cause).
	 * This constructor is useful for exceptions that are little more than wrappers for other throwables 
	 * (for example, {@link java.security.PrivilegedActionException}).
	 * @param cause the cause (which is saved for later retrieval by {@link #getCause()} method). 
	 * (A <code>null</code> value is permitted, and indicates that the cause is nonexistent or unknown.)
	 */
	public IllegalArgumentTypeException(Throwable cause) {
		super(cause);
	}

	/**
	 * Constructs a {@link IllegalArgumentTypeException} with the specified detail message an dcause. <br/><br/>
	 * Note that the detail message associated with <code>cause</code> is not automatically incorporated in this exception's detail message.
	 * @param message the detail message (which is saved for later retrieval by the {@link #getMessage()} method).
	 * @param cause the cause (which is saved for later retrieval by {@link #getCause()} method). 
	 * (A <code>null</code> value is permitted, and indicates that the cause is nonexistent or unknown.)
	 */
	public IllegalArgumentTypeException(String message, Throwable cause) {
		super(message, cause);
	}

}
