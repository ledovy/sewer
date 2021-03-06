package ch.ledovy.sewer.security.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;

import ch.ledovy.sewer.data.model.AbstractEntity;

@Entity
public class Role extends AbstractEntity {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name;
	private Set<String> permissions = new HashSet<>();
	
	public String getName() {
		return this.name;
	}
	
	public void setName(final String name) {
		this.name = name;
	}
	
	@ElementCollection
	public Set<String> getPermissions() {
		return this.permissions;
	}
	
	public void setPermissions(final Set<String> permissions) {
		this.permissions = permissions;
	}
	
}
