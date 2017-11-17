package ch.ledovy.sewer.data.view.filter;

import com.vaadin.ui.Button;

public class FilterClearButton extends Button {
	public FilterClearButton(FilterPresenter<?, ?> filter) {
		super();
		addClickListener(e -> filter.clear());
	}
}
