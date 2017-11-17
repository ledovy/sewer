package ch.ledovy.sewer.action.crud;

import ch.ledovy.sewer.action.Action;
import ch.ledovy.sewer.action.Action.ValidationException;
import ch.ledovy.sewer.data.view.form.Form;
import ch.ledovy.sewer.data.view.form.FormDeactivationListener;

public class CancelAction<T> implements Action {
	private Form<T>						form;
	private FormDeactivationListener	formDeactivationListener;
	
	public CancelAction(Form<T> form) {
		this.form = form;
	}
	
	@Override
	public void execute() {
		cancel();
	}
	
	@Override
	public void validate() throws ValidationException {
		try {
			this.form.validate();
		} catch (Exception e) {
			throw new ValidationException(e.getMessage());
		}
	}
	
	private void cancel() {
		this.form.reset();
		this.formDeactivationListener.formDeactivated();
	}
	
	public CancelAction<T> withFormDeactivationListener(FormDeactivationListener formDeactivationListener) {
		this.formDeactivationListener = formDeactivationListener;
		return this;
	}
}
