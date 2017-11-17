package ch.ledovy.sewer.data.view;

import com.vaadin.event.selection.SelectionEvent;
import com.vaadin.event.selection.SelectionListener;

import ch.ledovy.sewer.action.Action;
import ch.ledovy.sewer.action.Executor;

final class SelectionActionEnabler<T, P> implements SelectionListener<T> {
	private final Action	action;
	private final Executor	executor;
	
	public SelectionActionEnabler(Action action, Executor executor) {
		this.action = action;
		this.executor = executor;
	}
	
	@Override
	public void selectionChange(SelectionEvent<T> event) {
		try {
			this.action.validate();
			this.executor.setEnabled(true);
		} catch (ch.ledovy.sewer.action.Action.ValidationException e) {
			this.executor.setEnabled(false);
		}
	}
}