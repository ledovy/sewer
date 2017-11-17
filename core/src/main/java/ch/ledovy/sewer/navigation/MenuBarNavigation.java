package ch.ledovy.sewer.navigation;

import java.util.Collection;
import java.util.List;

import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.navigator.View;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.spring.navigator.SpringViewProvider;
import com.vaadin.ui.MenuBar;

import ch.ledovy.sewer.i18n.Messages;
import ch.ledovy.sewer.navigation.menu.Menu;
import ch.ledovy.sewer.navigation.menu.MenuEntry;

@SpringComponent
@UIScope
public class MenuBarNavigation extends MenuBar implements Navigation {
	
	private final Navigator		navigator;
	private SpringViewProvider	viewProvider;
	private Messages			messages;
	
	@Autowired
	public MenuBarNavigation(Navigator navigator, SpringViewProvider viewProvider, Messages messages) {
		this.navigator = navigator;
		this.viewProvider = viewProvider;
		this.messages = messages;
	}
	
	@PreDestroy
	public void shutdown() {
		this.messages.unregister(this);
	}
	
	@Override
	public void createMenu(Menu providedMenu) {
		removeItems();
		List<String> menus = providedMenu.getMenus();
		for (String menu : menus) {
			MenuItem menuItem = addItem(menu, null);
			this.messages.registerMenuItem(menu, menuItem, this);
			List<MenuEntry> menuEntries = providedMenu.getMenuEntries(menu);
			for (MenuEntry entry : menuEntries) {
				Class<? extends View> viewClass = entry.getViewClass();
				String caption = entry.getCaption();
				MenuItem entryItem = menuItem.addItem(caption, entry.getIcon(), selectedItem -> this.navigator.navigateTo(viewClass));
				this.messages.registerMenuItem(caption, entryItem, this);
				if (!isViewAvailable(viewClass)) {
					entryItem.setEnabled(false);
				}
			}
		}
	}
	
	private boolean isViewAvailable(Class<? extends View> view) {
		String viewId = this.navigator.getViewId(view);
		Collection<String> availableViews = this.viewProvider.getViewNamesForCurrentUI();
		return availableViews.contains(viewId);
	}
	
}
