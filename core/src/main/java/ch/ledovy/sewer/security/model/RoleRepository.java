package ch.ledovy.sewer.security.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
	Role findByName(String username);
	
	List<Role> findByNameLikeIgnoreCase(String namePart);
	
	int countByNameLikeIgnoreCase(String namePart);
}
