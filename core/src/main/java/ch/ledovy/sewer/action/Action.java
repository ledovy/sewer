package ch.ledovy.sewer.action;

import ch.ledovy.sewer.log.HasLogger;

public interface Action extends HasLogger {
	
	void execute();
	
	default void authorize() throws AuthorizationException {}
	
	default void validate() throws ValidationException {}
	
	default void runAction() {
		try {
			authorize();
			validate();
			execute();
		} catch (Exception e) {
			fail(e);
		}
	}
	
	default void fail(Exception e) {
		getLogger().warn(e.getMessage(), e);
	}
	
	public class AuthorizationException extends Exception {
		public AuthorizationException(String msg) {
			super(msg);
		}
	}
	
	public class ValidationException extends Exception {
		public ValidationException(String msg) {
			super(msg);
		}
		
		public ValidationException(String msg, Throwable e) {
			super(msg, e);
		}
	}
	
}
