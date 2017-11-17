package ch.ledovy.sewer.navigation;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.navigator.View;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.spring.internal.Conventions;
import com.vaadin.spring.navigator.SpringNavigator;

import ch.ledovy.sewer.security.service.SecurityService;
import ch.ledovy.sewer.security.view.LoginView;

@SpringComponent
@UIScope
public class Navigator extends SpringNavigator {
	
	private SecurityService service;
	private String defaultView = "";
	
	@Autowired
	public Navigator(final SecurityService service) {
		this.service = service;
	}
	
	/**
	 * Find the view id (URI fragment) used for a given view class.
	 *
	 * @param viewClass
	 *            the view class to find the id for
	 * @return the URI fragment for the view
	 */
	public String getViewId(final Class<? extends View> viewClass) {
		SpringView springView = viewClass.getAnnotation(SpringView.class);
		if (springView == null) {
			throw new IllegalArgumentException("The target class must be a @SpringView");
		}
		
		return Conventions.deriveMappingForView(viewClass, springView);
	}
	
	/**
	 * Navigate to the given view class.
	 *
	 * @param viewClass
	 *            the class of the target view, must be annotated using
	 *            {@link SpringView @SpringView}
	 */
	public void navigateTo(final Class<? extends View> targetView) {
		String viewId = getViewId(targetView);
		navigateTo(viewId);
	}
	
	public void navigateToDefaultView() {
		// if (!getState().isEmpty()) {
		// return;
		// }
		if (service.isLoggedIn()) {
			navigateTo(defaultView);
		} else {
			navigateTo(LoginView.class);
		}
	}
	
	public void setDefaultView(final String defaultView) {
		this.defaultView = defaultView;
	}
}
