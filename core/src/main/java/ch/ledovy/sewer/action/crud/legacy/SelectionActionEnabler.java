package ch.ledovy.sewer.action.crud.legacy;

import com.vaadin.event.selection.SelectionEvent;
import com.vaadin.event.selection.SelectionListener;

import ch.ledovy.sewer.action.Action;
import ch.ledovy.sewer.action.Executor;

final class SelectionActionEnabler<T, P> implements SelectionListener<T> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final Action action;
	private final Executor executor;
	
	public SelectionActionEnabler(final Action action, final Executor executor) {
		this.action = action;
		this.executor = executor;
	}
	
	@Override
	public void selectionChange(final SelectionEvent<T> event) {
		try {
			this.action.validate();
			this.executor.setEnabled(true);
		} catch (Exception e) {
			this.executor.setEnabled(false);
		}
	}
}