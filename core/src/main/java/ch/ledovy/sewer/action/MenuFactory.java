package ch.ledovy.sewer.action;

import com.vaadin.contextmenu.ContextMenu;
import com.vaadin.contextmenu.MenuItem;
import com.vaadin.ui.MenuBar;

import ch.ledovy.sewer.i18n.Messages;

public class MenuFactory {

	public static Menu forContextMenu(ContextMenu context, Messages messages) {
		return new Menu() {

			@Override
			public Executor addItem(String caption) {
				MenuItem item = context.addItem(caption, null);
				messages.registerContextItem(caption, item, context);
				return ExecutorFactory.create(item);
			}

			@Override
			public Menu addMenu(String caption) {
				MenuItem item = context.addItem(caption, null);
				messages.registerContextItem(caption, item, context);
				return forMenuItem(item, messages);
			}
		};
	}

	public static Menu forMenuItem(MenuItem context, Messages messages) {
		return new Menu() {

			@Override
			public Executor addItem(String caption) {
				MenuItem item = context.addItem(caption, null);
				messages.registerContextItem(caption, item, context);
				return ExecutorFactory.create(item);
			}

			@Override
			public Menu addMenu(String caption) {
				MenuItem item = context.addItem(caption, null);
				messages.registerContextItem(caption, item, context);
				return forMenuItem(item, messages);
			}
		};
	}

	public static Menu forMenuItem(com.vaadin.ui.MenuBar.MenuItem menu, Messages messages) {
		return new Menu() {

			@Override
			public Menu addMenu(String caption) {
				com.vaadin.ui.MenuBar.MenuItem item = menu.addItem(caption, null);
				messages.registerMenuItem(caption, item, menu);
				return forMenuItem(item, messages);
			}

			@Override
			public Executor addItem(String caption) {
				com.vaadin.ui.MenuBar.MenuItem item = menu.addItem(caption, null);
				messages.registerMenuItem(caption, item, menu);
				return ExecutorFactory.create(item);
			}
		};
	}

	public static Menu forMenuBar(MenuBar menu, Messages messages) {
		return new Menu() {

			@Override
			public Executor addItem(String caption) {
				com.vaadin.ui.MenuBar.MenuItem item = menu.addItem(caption, null);
				messages.registerMenuItem(caption, item, menu);
				return ExecutorFactory.create(item);
			}

			@Override
			public Menu addMenu(String caption) {
				com.vaadin.ui.MenuBar.MenuItem item = menu.addItem(caption, null);
				messages.registerMenuItem(caption, item, menu);
				return forMenuItem(item, messages);
			}
		};
	}

}
