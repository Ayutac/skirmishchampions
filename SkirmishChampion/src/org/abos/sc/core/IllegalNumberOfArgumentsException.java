package org.abos.sc.core;

/**
 * Thrown when the wrong number of arguments is detected during parsing.
 * @author Sebastian Koch
 * @version %I%
 * @since 0.1
 * @see ParseException
 */
public class IllegalNumberOfArgumentsException extends ParseException {

	/**
	 * the serial version UID
	 */
	private static final long serialVersionUID = 5546012270633166331L;

	/**
	 * Constructs a {@link IllegalNumberOfArgumentsException} with no detail message.
	 */
	public IllegalNumberOfArgumentsException() {}

	/**
	 * Constructs a {@link IllegalNumberOfArgumentsException} with the specified detail message.
	 * @param message the detail message
	 */
	public IllegalNumberOfArgumentsException(String message) {
		super(message);
	}

	/**
	 * Constructs a {@link IllegalNumberOfArgumentsException} with the specified cause and a detail message of <code>(cause==null ? null : cause.toString())</code> 
	 * (which typically contains the class and detail message of cause).
	 * This constructor is useful for exceptions that are little more than wrappers for other throwables 
	 * (for example, {@link java.security.PrivilegedActionException}).
	 * @param cause the cause (which is saved for later retrieval by {@link #getCause()} method). 
	 * (A <code>null</code> value is permitted, and indicates that the cause is nonexistent or unknown.)
	 */
	public IllegalNumberOfArgumentsException(Throwable cause) {
		super(cause);
	}

	/**
	 * Constructs a {@link IllegalNumberOfArgumentsException} with the specified detail message an dcause. <br/><br/>
	 * Note that the detail message associated with <code>cause</code> is not automatically incorporated in this exception's detail message.
	 * @param message the detail message (which is saved for later retrieval by the {@link #getMessage()} method).
	 * @param cause the cause (which is saved for later retrieval by {@link #getCause()} method). 
	 * (A <code>null</code> value is permitted, and indicates that the cause is nonexistent or unknown.)
	 */
	public IllegalNumberOfArgumentsException(String message, Throwable cause) {
		super(message, cause);
	}

}
