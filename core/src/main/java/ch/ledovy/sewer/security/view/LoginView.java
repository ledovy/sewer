package ch.ledovy.sewer.security.view;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.data.Binder;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.event.ShortcutListener;
import com.vaadin.navigator.View;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

import ch.ledovy.sewer.log.HasLogger;
import ch.ledovy.sewer.navigation.Navigator;
import ch.ledovy.sewer.security.model.User;
import ch.ledovy.sewer.security.service.SecurityService;

@UIScope
@SpringView(name = "login")
public class LoginView extends VerticalLayout implements View, HasLogger {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Binder<User> binder;
	private User user;
	private SecurityService service;
	private Navigator navigator;
	
	@Autowired
	public LoginView(final SecurityService service, final Navigator navigator) {
		this.service = service;
		this.navigator = navigator;
		this.binder = new Binder<>(User.class);
		this.user = new User();
		this.binder.readBean(this.user);
		
		ShortcutListener loginShortcut = new ShortcutListener("", KeyCode.ENTER, (int[]) null) {
			
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;
			
			@Override
			public void handleAction(final Object sender, final Object target) {
				login();
			}
		};
		TextField username = new TextField("User");
		this.binder.bind(username, User::getUsername, User::setUsername);
		//		username.addShortcutListener(loginShortcut);
		addComponent(username);
		
		PasswordField password = new PasswordField("Password");
		password.addShortcutListener(loginShortcut);
		this.binder.bind(password, User::getPassword, User::setPassword);
		addComponent(password);
		
		Button login = new Button("Login");
		login.addClickListener(event -> login());
		
		Button register = new Button("Register");
		register.addClickListener(event -> navigator.navigateTo(RegistrationView.class));
		addComponent(new HorizontalLayout(login, register));
	}
	
	private void login() {
		try {
			this.binder.validate();
			this.binder.writeBean(this.user);
			this.service.login(this.user.getUsername(), this.user.getPassword());
		} catch (Exception ex) {
			getLogger().warn(ex.getMessage(), ex);
			Notification.show(ex.getMessage(), Notification.Type.ERROR_MESSAGE);
		}
	}
	
}
