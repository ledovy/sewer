package ch.ledovy.sewer.action;

import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.MenuBar.MenuItem;

public class ExecutorFactory {
	public static Executor create(Button button) {
		return new Executor() {
			
			@Override
			public void setVisible(boolean visible) {
				button.setVisible(visible);
			}
			
			@Override
			public void setEnabled(boolean enabled) {
				button.setEnabled(enabled);
			}
			
			@Override
			public void setAction(Action action) {
				button.addClickListener(e -> action.runAction());
			}
		};
	}
	
	public static Executor create(Grid<?> list) {
		return new Executor() {
			
			@Override
			public void setVisible(boolean visible) {
				//TODO enabled/visible grid
				//				list.setEnabled(false);
			}
			
			@Override
			public void setEnabled(boolean enabled) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void setAction(Action action) {
				list.addSelectionListener(e -> action.runAction());
			}
		};
	}
	
	public static Executor create(MenuItem item) {
		return new Executor() {
			@Override
			public void setAction(Action action) {
				item.setCommand(selectedItem -> action.runAction());
			}
			
			@Override
			public void setEnabled(boolean enabled) {
				item.setEnabled(enabled);
			}
			
			@Override
			public void setVisible(boolean visible) {
				item.setVisible(visible);
			}
			
		};
	}
	
	public static Executor create(com.vaadin.contextmenu.MenuItem item) {
		return new Executor() {
			@Override
			public void setAction(Action action) {
				item.setCommand(selectedItem -> action.runAction());
			}
			
			@Override
			public void setEnabled(boolean enabled) {
				item.setEnabled(enabled);
			}
			
			@Override
			public void setVisible(boolean visible) {
				item.setVisible(visible);
			}
			
		};
	}
}
