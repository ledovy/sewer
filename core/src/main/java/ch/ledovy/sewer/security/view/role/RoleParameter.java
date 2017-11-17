package ch.ledovy.sewer.security.view.role;

public class RoleParameter {
	private String name;
	
	public RoleParameter() {
		this(null);
	}
	
	public RoleParameter(final String name) {
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}
	
	public void setName(final String name) {
		this.name = name;
	}
	
}
