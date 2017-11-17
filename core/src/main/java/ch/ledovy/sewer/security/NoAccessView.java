package ch.ledovy.sewer.security;

import javax.annotation.PostConstruct;

import com.vaadin.navigator.View;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

@UIScope
@SpringView
public class NoAccessView extends VerticalLayout implements View {
	
	@PostConstruct
	public void init() {
		addComponent(new Label("ACCESS DENIED"));
	}
}
