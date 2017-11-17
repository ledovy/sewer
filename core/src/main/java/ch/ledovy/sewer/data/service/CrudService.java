package ch.ledovy.sewer.data.service;

import ch.ledovy.sewer.action.crud.AddAction.ItemCreator;

public interface CrudService<T> extends ItemCreator<T> {
	
	T save(T item);
	
	void delete(long id);
	
	T load(long id);
}
