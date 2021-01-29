package org.abos.util;

/**
 * Thrown when an identifier is parsed and expected to be known/registered already, but isn't.
 * @author Sebastian Koch
 * @version %I%
 * @since Skirmish Champions 0.4
 * @see ParseException
 */
public class ParsedIdNotFoundException extends ParseException {

	/**
	 * the serial version UID
	 */
	private static final long serialVersionUID = -6458879856207071158L;

	/**
	 * Constructs a {@link ParsedIdNotFoundException} with no detail message.
	 */
	public ParsedIdNotFoundException() {}

	/**
	 * Constructs a {@link ParsedIdNotFoundException} with the specified detail message.
	 * @param message the detail message
	 */
	public ParsedIdNotFoundException(String message) {
		super(message);
	}

	/**
	 * Constructs a {@link ParsedIdNotFoundException} with the specified cause and a detail message of <code>(cause==null ? null : cause.toString())</code> 
	 * (which typically contains the class and detail message of cause).
	 * This constructor is useful for exceptions that are little more than wrappers for other throwables 
	 * (for example, {@link java.security.PrivilegedActionException}).
	 * @param cause the cause (which is saved for later retrieval by {@link #getCause()} method). 
	 * (A <code>null</code> value is permitted, and indicates that the cause is nonexistent or unknown.)
	 */
	public ParsedIdNotFoundException(Throwable cause) {
		super(cause);
	}

	/**
	 * Constructs a {@link ParsedIdNotFoundException} with the specified detail message an dcause. <br/><br/>
	 * Note that the detail message associated with <code>cause</code> is not automatically incorporated in this exception's detail message.
	 * @param message the detail message (which is saved for later retrieval by the {@link #getMessage()} method).
	 * @param cause the cause (which is saved for later retrieval by {@link #getCause()} method). 
	 * (A <code>null</code> value is permitted, and indicates that the cause is nonexistent or unknown.)
	 */
	public ParsedIdNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

}
