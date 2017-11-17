package ch.ledovy.sewer.security.service;

import java.util.HashSet;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import ch.ledovy.sewer.security.model.Role;
import ch.ledovy.sewer.security.model.RoleRepository;
import ch.ledovy.sewer.security.model.User;
import ch.ledovy.sewer.security.model.UserRepository;

@Service
public class UserServiceImpl implements UserService {
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@PostConstruct
	public void initUsers() {
		createUserIfNeeded("admin", "admin", new HashSet<>(this.roleRepository.findAll()));
	}
	
	private void createUserIfNeeded(final String username, final String password, final HashSet<Role> roles) {
		User user = this.userRepository.findByUsername(username);
		if (user == null) {
			user = new User();
			user.setUsername(username);
			user.setPassword(password);
			user.setRoles(roles);
			save(user);
		}
	}
	
	@Override
	public void save(final User user) {
		user.setPassword(this.bCryptPasswordEncoder.encode(user.getPassword()));
		user.setRoles(new HashSet<>(this.roleRepository.findAll()));
		this.userRepository.save(user);
	}
	
	@Override
	public User findByUsername(final String username) {
		return this.userRepository.findByUsername(username);
	}
}
