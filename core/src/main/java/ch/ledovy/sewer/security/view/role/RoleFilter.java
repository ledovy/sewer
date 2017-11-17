package ch.ledovy.sewer.security.view.role;

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
public class RoleFilter extends AbstractForm<RoleParameter> {
	public final TextField name;
	private Messages messages;
	
	@Autowired
	public RoleFilter(final Messages messages) {
		this(new RoleParameter(), messages);
		
	}
	
	public RoleFilter(final RoleParameter parameter, final Messages messages) {
		this.messages = messages;
		this.name = messages.registerPlaceholder("view.model.ingredient.filter.name", new TextField(), this);
		
		this.binder.bind(this.name, RoleParameter::getName, RoleParameter::setName);
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
	public RoleParameter newValue() {
		return new RoleParameter();
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
