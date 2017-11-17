package ch.ledovy.sewer.security;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.spring.access.ViewAccessControl;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.UI;

import ch.ledovy.sewer.navigation.service.ViewAccessService;
import ch.ledovy.sewer.security.service.SecurityService;

@SpringComponent
public class SecurityAccessControl implements ViewAccessControl {
	
	private ViewAccessService	accessService;
	private SecurityService		securityService;
	
	@Autowired
	public SecurityAccessControl(ViewAccessService accessService, SecurityService securityService) {
		this.accessService = accessService;
		this.securityService = securityService;
	}
	
	@Override
	public boolean isAccessGranted(UI ui, String beanName) {
		return isViewAllowed(beanName);
	}
	
	private boolean isViewAllowed(String viewName) {
		boolean inRoles = true;
		Set<String> neededRoles = this.accessService.getNeededRoles(viewName);
		for (String role : neededRoles) {
			//TODO all or any???
			inRoles &= this.securityService.isCurrentUserInRole(role);
		}
		return inRoles;
	}
}
