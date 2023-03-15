/**
 * 
 */
package Exceptions;

/**
 * @author Rioni
 *
 */
public class NotImplementedException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static String getMethodNameStackTrace() {
		return Thread.currentThread().getStackTrace()[3].getMethodName();
	}

	private static String getClassNameStackTrace() {
		return Thread.currentThread().getStackTrace()[3].getClassName();
	}

	public NotImplementedException(String message) {
		super(message);
	}

	public NotImplementedException() {
		super("Method not implemented: " + getMethodNameStackTrace() + " of " + getClassNameStackTrace()  + ".");
	}
}
