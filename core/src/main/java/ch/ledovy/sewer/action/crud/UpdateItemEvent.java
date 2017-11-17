package ch.ledovy.sewer.action.crud;

public class UpdateItemEvent<T> extends DataChangeEvent<T> {
	
	public UpdateItemEvent(final T data) {
		super(data);
	}
	
}
