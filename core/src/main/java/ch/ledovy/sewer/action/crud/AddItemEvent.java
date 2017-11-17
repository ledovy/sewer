package ch.ledovy.sewer.action.crud;

public class AddItemEvent<T> extends DataChangeEvent<T> {
	
	public AddItemEvent(final T data) {
		super(data);
	}
	
}
