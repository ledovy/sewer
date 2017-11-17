package ch.ledovy.sewer.action.crud;

public class RemoveItemEvent<T> extends DataChangeEvent<T> {
	
	public RemoveItemEvent(final T data) {
		super(data);
	}
	
}
