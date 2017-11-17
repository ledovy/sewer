package ch.ledovy.sewer.data.view.form;

import com.vaadin.data.Binder;
import com.vaadin.data.ValidationException;
import com.vaadin.ui.Component;
import com.vaadin.ui.Component.Focusable;

import ch.ledovy.sewer.data.view.ValueConsumer;

public interface Form<T> extends ValueConsumer<T> {
	
	Focusable getFirstFormField();
	
	boolean hasChanges();
	
	Component getForm();
	
	void validate() throws ValidationException;
	
	T newValue();
	
	T getValue() throws ValidationException;
	
	default void reset() {
		setValue(newValue());
	}
	
	Binder<T> getBinder();
}
