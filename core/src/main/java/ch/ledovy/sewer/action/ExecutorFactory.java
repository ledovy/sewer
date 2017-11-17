package ch.ledovy.sewer.action;

import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.MenuBar.MenuItem;

public class ExecutorFactory {
	public static Executor create(final Button button) {
		return new Executor() {
			
			@Override
			public void setVisible(final boolean visible) {
				button.setVisible(visible);
			}
			
			@Override
			public void setEnabled(final boolean enabled) {
				button.setEnabled(enabled);
			}
			
			@Override
			public void setAction(final Action action) {
				button.addClickListener(e -> action.runAction());
			}
		};
	}
	
	public static Executor create(final Grid<?> list) {
		return new Executor() {
			
			@Override
			public void setVisible(final boolean visible) {
				//TODO enabled/visible grid
				//				list.setEnabled(false);
			}
			
			@Override
			public void setEnabled(final boolean enabled) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void setAction(final Action action) {
				list.addSelectionListener(e -> action.runAction());
			}
		};
	}
	
	public static Executor create(final MenuItem item) {
		return new Executor() {
			@Override
			public void setAction(final Action action) {
				item.setCommand(selectedItem -> action.runAction());
			}
			
			@Override
			public void setEnabled(final boolean enabled) {
				item.setEnabled(enabled);
			}
			
			@Override
			public void setVisible(final boolean visible) {
				item.setVisible(visible);
			}
			
		};
	}
	
	public static Executor create(final com.vaadin.contextmenu.MenuItem item) {
		return new Executor() {
			@Override
			public void setAction(final Action action) {
				item.setCommand(selectedItem -> action.runAction());
			}
			
			@Override
			public void setEnabled(final boolean enabled) {
				item.setEnabled(enabled);
			}
			
			@Override
			public void setVisible(final boolean visible) {
				item.setVisible(visible);
			}
			
		};
	}
}
