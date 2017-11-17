package ch.ledovy.sewer.security.service;

import java.util.Set;

import ch.ledovy.sewer.security.model.User;

public interface SecurityService {
	
	void login(String username, String password);
	
	void logout();
	
	boolean isLoggedIn();
	
	Set<String> getUserRoles();
	
	boolean isCurrentUserInRole(String role);
	
	void register(User user);
	
}
