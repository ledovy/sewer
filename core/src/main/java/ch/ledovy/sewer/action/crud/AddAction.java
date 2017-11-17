package ch.ledovy.sewer.action.crud;

import ch.ledovy.sewer.action.Action;
import ch.ledovy.sewer.data.view.ValueConsumer;
import ch.ledovy.sewer.data.view.form.FormActivationListener;

public class AddAction<T> implements Action {
	private ValueConsumer<T> form;
	private FormActivationListener formActivationListener;
	private ItemCreator<T> creator;
	
	public AddAction(final ValueConsumer<T> form, final ItemCreator<T> creator) {
		this.form = form;
		this.creator = creator;
	}
	
	@Override
	public void execute() {
		addNew();
	}
	
	private void addNew() {
		T entity = this.creator.create();
		this.form.setValue(entity);
		if (this.formActivationListener != null) {
			this.formActivationListener.formActivated();
		}
	}
	
	public AddAction<T> withFormActivationListener(final FormActivationListener formActivationListener) {
		this.formActivationListener = formActivationListener;
		return this;
	}
	
	public interface ItemCreator<T> {
		T create();
	}
	
}
