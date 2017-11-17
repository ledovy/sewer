package ch.ledovy.sewer.security.service;

import ch.ledovy.sewer.security.model.User;

public interface UserService {
	
	void save(User user);
	
	User findByUsername(String username);
	
}
