package ch.ledovy.sewer.data.view.form;

import com.vaadin.data.Binder;
import com.vaadin.data.ValidationException;

public abstract class AbstractForm<T> implements Form<T> {
	private T			parameter;
	protected Binder<T>	binder;
	
	public AbstractForm() {
		this.binder = new Binder<>();
		reset();
	}
	
	@Override
	public T getValue() throws ValidationException {
		validate();
		this.binder.writeBean(this.parameter);
		return this.parameter;
	}
	
	@Override
	public void validate() throws ValidationException {
		this.binder.validate();
	}
	
	@Override
	public void setValue(T value) {
		this.parameter = value;
		this.binder.readBean(this.parameter);
	}
	
	@Override
	public boolean hasChanges() {
		return this.binder != null && this.binder.hasChanges();
	}
	
	@Override
	public Binder<T> getBinder() {
		return this.binder;
	}
}
