package ch.ledovy.sewer.action;

import ch.ledovy.sewer.log.HasLogger;

public interface Action extends HasLogger {
	
	void execute();
	
	default void authorize() throws AuthorizationException {
	}
	
	default void validate() throws ValidationException {
	}
	
	default void runAction() {
		try {
			authorize();
			validate();
			execute();
		} catch (Exception e) {
			fail(e);
		}
	}
	
	default void fail(final Exception e) {
		getLogger().warn(e.getMessage(), e);
	}
	
	public class AuthorizationException extends Exception {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
		public AuthorizationException(final String msg) {
			super(msg);
		}
	}
	
	public class ValidationException extends Exception {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
		public ValidationException(final String msg) {
			super(msg);
		}
		
		public ValidationException(final String msg, final Throwable e) {
			super(msg, e);
		}
	}
	
}
