package ch.ledovy.sewer.security.view;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.data.Binder;
import com.vaadin.data.ValidationException;
import com.vaadin.navigator.View;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

import ch.ledovy.sewer.navigation.Navigator;
import ch.ledovy.sewer.security.model.User;
import ch.ledovy.sewer.security.service.SecurityService;

@UIScope
@SpringView(name = "register")
public class RegistrationView extends VerticalLayout implements View {
	
	@Autowired
	public RegistrationView(SecurityService service, Navigator navigator) {
		Binder<User> binder = new Binder<>(User.class);
		User user = new User();
		binder.readBean(user);
		
		TextField username = new TextField("User");
		binder.bind(username, User::getUsername, User::setUsername);
		addComponent(username);
		
		PasswordField password = new PasswordField("Password");
		binder.bind(password, User::getPassword, User::setPassword);
		addComponent(password);
		
		PasswordField passwordConfirm = new PasswordField("Confirm Password");
		binder.bind(passwordConfirm, User::getPasswordConfirm, User::setPasswordConfirm);
		addComponent(passwordConfirm);
		
		Button login = new Button("Login");
		login.addClickListener(event -> {
			navigator.navigateTo(LoginView.class);
		});
		//		addComponent(login);
		
		Button register = new Button("Register");
		register.addClickListener(event -> {
			try {
				binder.validate();
				binder.writeBean(user);
				service.register(user);
				navigator.navigateTo(LoginView.class);
			} catch (ValidationException ex) {
				Notification.show(ex.getMessage());
			}
		});
		//		addComponent(register);
		addComponent(new HorizontalLayout(login, register));
	}
}
