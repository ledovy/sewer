package ch.ledovy.sewer.security.view.user;

public class UserParameter {
	private String name;
	
	public UserParameter() {
		this(null);
	}
	
	public UserParameter(final String name) {
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}
	
	public void setName(final String name) {
		this.name = name;
	}
	
}
