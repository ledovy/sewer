package ch.ledovy.sewer.security.view.user;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.ViewScope;
import com.vaadin.ui.Component;
import com.vaadin.ui.Component.Focusable;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;

import ch.ledovy.sewer.data.view.form.AbstractForm;
import ch.ledovy.sewer.i18n.Messages;

@SpringComponent
@ViewScope
public class UserFilter extends AbstractForm<UserParameter> {
	public final TextField name;
	private Messages messages;
	
	@Autowired
	public UserFilter(final Messages messages) {
		this(new UserParameter(), messages);
		
	}
	
	public UserFilter(final UserParameter parameter, final Messages messages) {
		this.messages = messages;
		this.name = messages.registerPlaceholder("view.model.ingredient.filter.name", new TextField(), this);
		
		this.binder.bind(this.name, UserParameter::getName, UserParameter::setName);
		reset();
	}
	
	@PostConstruct
	public void shutdown() {
		this.messages.unregister(this);
	}
	
	public TextField getName() {
		return this.name;
	}
	
	public HorizontalLayout createFields() {
		return new HorizontalLayout(this.name);
	}
	
	@Override
	public UserParameter newValue() {
		return new UserParameter();
	}
	
	@Override
	public Focusable getFirstFormField() {
		return this.name;
	}
	
	@Override
	public boolean hasChanges() {
		return (this.binder != null) && this.binder.hasChanges();
	}
	
	@Override
	public Component getForm() {
		return createFields();
	}
}
