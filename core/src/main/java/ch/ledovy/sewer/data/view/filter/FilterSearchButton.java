package ch.ledovy.sewer.data.view.filter;

import com.vaadin.ui.Button;

public class FilterSearchButton extends Button {
	public FilterSearchButton(final FilterPresenter<?, ?> filter) {
		super();
		addClickListener(e -> filter.filter());
	}
}
