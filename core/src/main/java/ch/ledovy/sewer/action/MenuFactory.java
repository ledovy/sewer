package ch.ledovy.sewer.action;

import com.vaadin.contextmenu.ContextMenu;
import com.vaadin.contextmenu.MenuItem;
import com.vaadin.ui.MenuBar;

import ch.ledovy.sewer.i18n.Messages;

public class MenuFactory {
	
	public static Menu forContextMenu(final ContextMenu context, final Messages messages) {
		return new Menu() {
			
			@Override
			public Executor addItem(final String caption) {
				MenuItem item = context.addItem(caption, null);
				messages.registerContextItem(caption, item, context);
				return ExecutorFactory.create(item);
			}
			
			@Override
			public Menu addMenu(final String caption) {
				MenuItem item = context.addItem(caption, null);
				messages.registerContextItem(caption, item, context);
				return MenuFactory.forMenuItem(item, messages);
			}
		};
	}
	
	public static Menu forMenuItem(final MenuItem context, final Messages messages) {
		return new Menu() {
			
			@Override
			public Executor addItem(final String caption) {
				MenuItem item = context.addItem(caption, null);
				messages.registerContextItem(caption, item, context);
				return ExecutorFactory.create(item);
			}
			
			@Override
			public Menu addMenu(final String caption) {
				MenuItem item = context.addItem(caption, null);
				messages.registerContextItem(caption, item, context);
				return MenuFactory.forMenuItem(item, messages);
			}
		};
	}
	
	public static Menu forMenuItem(final com.vaadin.ui.MenuBar.MenuItem menu, final Messages messages) {
		return new Menu() {
			
			@Override
			public Menu addMenu(final String caption) {
				com.vaadin.ui.MenuBar.MenuItem item = menu.addItem(caption, null);
				messages.registerMenuItem(caption, item, menu);
				return MenuFactory.forMenuItem(item, messages);
			}
			
			@Override
			public Executor addItem(final String caption) {
				com.vaadin.ui.MenuBar.MenuItem item = menu.addItem(caption, null);
				messages.registerMenuItem(caption, item, menu);
				return ExecutorFactory.create(item);
			}
		};
	}
	
	public static Menu forMenuBar(final MenuBar menu, final Messages messages) {
		return new Menu() {
			
			@Override
			public Executor addItem(final String caption) {
				com.vaadin.ui.MenuBar.MenuItem item = menu.addItem(caption, null);
				messages.registerMenuItem(caption, item, menu);
				return ExecutorFactory.create(item);
			}
			
			@Override
			public Menu addMenu(final String caption) {
				com.vaadin.ui.MenuBar.MenuItem item = menu.addItem(caption, null);
				messages.registerMenuItem(caption, item, menu);
				return MenuFactory.forMenuItem(item, messages);
			}
		};
	}
	
}
