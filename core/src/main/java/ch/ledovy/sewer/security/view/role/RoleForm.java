package ch.ledovy.sewer.security.view.role;

import java.util.Arrays;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.ViewScope;
import com.vaadin.ui.Component;
import com.vaadin.ui.Component.Focusable;

import ch.ledovy.sewer.data.view.form.AbstractForm;
import ch.ledovy.sewer.i18n.Messages;
import ch.ledovy.sewer.security.model.Role;

import com.vaadin.ui.TextField;
import com.vaadin.ui.TwinColSelect;
import com.vaadin.ui.VerticalLayout;

@SpringComponent
@ViewScope
public class RoleForm extends AbstractForm<Role> {
	public final TextField				name;
	public final TwinColSelect<String>	permissions;
	private VerticalLayout				root;
	private Messages					messages;
	
	@Autowired
	public RoleForm(Messages messages) {
		this.messages = messages;
		this.name = messages.registerCaption("view.security.role.name.caption", new TextField(), this);
		this.permissions = messages.registerCaption("view.security.role.permissions.caption", new TwinColSelect<>(), this);
		this.permissions.setItems(Arrays.asList("Recht 1A", "Recht 2A", "Recht 2B", "Recht 3"));
		
		this.root = new VerticalLayout(this.name, this.permissions);
		
		this.binder.bind(this.name, Role::getName, Role::setName);
		this.binder.bind(this.permissions, Role::getPermissions, Role::setPermissions);
	}
	
	@PostConstruct
	public void shutdown() {
		this.messages.unregister(this);
	}
	
	@Override
	public Focusable getFirstFormField() {
		return this.name;
	}
	
	
	@Override
	public Component getForm() {
		return this.root;
	}
	
	@Override
	public Role newValue() {
		return new Role();
	}
}
