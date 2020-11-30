package org.abos.util;

/**
 * Thrown when a parsing error occurs.
 * @author Sebastian Koch
 * @version %I%
 * @since 0.1
 * @see IllegalNumberOfArgumentsException
 * @see IllegalArgumentTypeException
 */
public class ParseException extends RuntimeException {

	/**
	 * the serial version UID
	 */
	private static final long serialVersionUID = 8142842924566021531L;

	/**
	 * Constructs a {@link ParseException} with no detail message.
	 */
	public ParseException() {}

	/**
	 * Constructs a {@link ParseException} with the specified detail message.
	 * @param message the detail message
	 */
	public ParseException(String message) {
		super(message);
	}

	/**
	 * Constructs a {@link ParseException} with the specified cause and a detail message of <code>(cause==null ? null : cause.toString())</code> 
	 * (which typically contains the class and detail message of cause).
	 * This constructor is useful for exceptions that are little more than wrappers for other throwables 
	 * (for example, {@link java.security.PrivilegedActionException}).
	 * @param cause the cause (which is saved for later retrieval by {@link #getCause()} method). 
	 * (A <code>null</code> value is permitted, and indicates that the cause is nonexistent or unknown.)
	 */
	public ParseException(Throwable cause) {
		super(cause);
	}

	/**
	 * Constructs a {@link ParseException} with the specified detail message and cause. <br/><br/>
	 * Note that the detail message associated with <code>cause</code> is not automatically incorporated in this exception's detail message.
	 * @param message the detail message (which is saved for later retrieval by the {@link #getMessage()} method).
	 * @param cause the cause (which is saved for later retrieval by {@link #getCause()} method). 
	 * (A <code>null</code> value is permitted, and indicates that the cause is nonexistent or unknown.)
	 */
	public ParseException(String message, Throwable cause) {
		super(message, cause);
	}

}
