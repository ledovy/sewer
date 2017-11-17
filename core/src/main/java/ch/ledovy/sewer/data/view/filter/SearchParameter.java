package ch.ledovy.sewer.data.view.filter;

public interface SearchParameter<T> {
	
	boolean matches(T item);
	
}
