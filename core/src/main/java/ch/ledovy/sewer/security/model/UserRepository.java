package ch.ledovy.sewer.security.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
	User findByUsername(String username);
	
	List<User> findByUsernameLikeIgnoreCase(String namePart);
	
	int countByUsernameLikeIgnoreCase(String name);
}
