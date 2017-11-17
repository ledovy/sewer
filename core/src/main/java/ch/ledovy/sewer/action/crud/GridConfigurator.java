package ch.ledovy.sewer.action.crud;

import com.vaadin.ui.Grid;

public interface GridConfigurator<T> {
	void configure(Grid<T> grid);
}
