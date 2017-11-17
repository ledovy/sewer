package ch.ledovy.sewer.action.crud;

public class DataChangeEvent<T> {
	private T data;
	
	public DataChangeEvent(final T data) {
		this.data = data;
	}
	
	public T getData() {
		return this.data;
	}
}
