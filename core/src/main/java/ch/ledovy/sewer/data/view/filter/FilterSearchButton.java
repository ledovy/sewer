package ch.ledovy.sewer.data.view.filter;

import com.vaadin.ui.Button;

public class FilterSearchButton extends Button {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public FilterSearchButton(final FilterPresenter<?, ?> filter) {
		super();
		addClickListener(e -> filter.filter());
	}
}
