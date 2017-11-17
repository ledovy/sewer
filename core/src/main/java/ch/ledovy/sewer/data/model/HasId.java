package ch.ledovy.sewer.data.model;

import java.io.Serializable;

public interface HasId<T extends Serializable> {
	T getId();
	
	boolean isNew();
}
