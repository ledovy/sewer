package ch.ledovy.sewer.action;

import ch.ledovy.sewer.action.Action.ValidationException;

public interface Validator {
	void validate() throws ValidationException;
}
