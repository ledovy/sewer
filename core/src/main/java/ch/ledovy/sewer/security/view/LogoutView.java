package ch.ledovy.sewer.security.view;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.navigator.View;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.VerticalLayout;

import ch.ledovy.sewer.security.service.SecurityService;

@UIScope
@SpringView(name = "logout")
public class LogoutView extends VerticalLayout implements View {
	
	@Autowired
	public LogoutView(SecurityService service) {
		service.logout();
	}
	
}
