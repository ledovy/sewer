package ch.ledovy.sewer.security.view.user;

import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.ViewScope;
import com.vaadin.ui.Component;
import com.vaadin.ui.Component.Focusable;
import com.vaadin.ui.TextField;
import com.vaadin.ui.TwinColSelect;
import com.vaadin.ui.VerticalLayout;

import ch.ledovy.sewer.data.view.form.AbstractForm;
import ch.ledovy.sewer.i18n.Messages;
import ch.ledovy.sewer.security.model.Role;
import ch.ledovy.sewer.security.model.RoleRepository;
import ch.ledovy.sewer.security.model.User;

@SpringComponent
@ViewScope
public class UserForm extends AbstractForm<User> {
	public final TextField username;
	public final TwinColSelect<Role> roles;
	private final VerticalLayout root;
	private Messages messages;
	
	@Autowired
	public UserForm(final Messages messages, final RoleRepository roleRepo) {
		this.messages = messages;
		this.username = messages.registerCaption("view.security.user.name.caption", new TextField(), this);
		this.roles = messages.registerCaption("view.security.user.roles.caption", new TwinColSelect<>(), this);
		this.roles.setItemCaptionGenerator(r -> {
			return r.getName();
		});
		this.roles.setItems(roleRepo.findAll());
		
		this.root = new VerticalLayout(this.username, this.roles);
		
		this.binder.bind(this.username, User::getUsername, User::setUsername);
		this.binder.bind(this.roles, User::getRoles, User::setRoles);
	}
	
	@PreDestroy
	public void shutdown() {
		this.messages.unregister(this);
	}
	
	@Override
	public Focusable getFirstFormField() {
		return this.username;
	}
	
	@Override
	public User newValue() {
		return new User();
	}
	
	@Override
	public Component getForm() {
		return this.root;
	}
}
