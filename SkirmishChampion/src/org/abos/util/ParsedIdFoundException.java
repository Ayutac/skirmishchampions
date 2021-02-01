package org.abos.util;

/**
 * Thrown when an identifier is parsed and expected not to be known/registered already, but is.
 * @author Sebastian Koch
 * @version %I%
 * @since Skirmish Champions 0.4
 * @see ParseException
 * @see ParsedIdNotFoundException
 */
public class ParsedIdFoundException extends ParseException {

	/**
	 * the serial version UID
	 */
	private static final long serialVersionUID = -6458879856207071158L;

	/**
	 * Constructs a {@link ParsedIdFoundException} with no detail message.
	 */
	public ParsedIdFoundException() {}

	/**
	 * Constructs a {@link ParsedIdFoundException} with the specified detail message.
	 * @param message the detail message
	 */
	public ParsedIdFoundException(String message) {
		super(message);
	}

	/**
	 * Constructs a {@link ParsedIdFoundException} with the specified cause and a detail message of <code>(cause==null ? null : cause.toString())</code> 
	 * (which typically contains the class and detail message of cause).
	 * This constructor is useful for exceptions that are little more than wrappers for other throwables 
	 * (for example, {@link java.security.PrivilegedActionException}).
	 * @param cause the cause (which is saved for later retrieval by {@link #getCause()} method). 
	 * (A <code>null</code> value is permitted, and indicates that the cause is nonexistent or unknown.)
	 */
	public ParsedIdFoundException(Throwable cause) {
		super(cause);
	}

	/**
	 * Constructs a {@link ParsedIdFoundException} with the specified detail message an dcause. <br/><br/>
	 * Note that the detail message associated with <code>cause</code> is not automatically incorporated in this exception's detail message.
	 * @param message the detail message (which is saved for later retrieval by the {@link #getMessage()} method).
	 * @param cause the cause (which is saved for later retrieval by {@link #getCause()} method). 
	 * (A <code>null</code> value is permitted, and indicates that the cause is nonexistent or unknown.)
	 */
	public ParsedIdFoundException(String message, Throwable cause) {
		super(message, cause);
	}

}
