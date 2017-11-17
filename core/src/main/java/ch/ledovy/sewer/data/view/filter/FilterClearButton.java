package ch.ledovy.sewer.data.view.filter;

import com.vaadin.ui.Button;

public class FilterClearButton extends Button {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public FilterClearButton(final FilterPresenter<?, ?> filter) {
		super();
		addClickListener(e -> filter.clear());
	}
}
