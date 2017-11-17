package ch.ledovy.sewer.data.model;

import java.util.List;

public interface ParameterRepository<T, P> {
	List<T> findAll();
	
	long count();
	
	List<T> applyFindByParameter(P parameter);
	
	int applyCountByParameter(P parameter);
	
}
