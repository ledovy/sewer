package ch.ledovy.sewer.navigation;

import java.util.Collection;
import java.util.List;

import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.navigator.View;
import com.vaadin.server.Resource;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.spring.navigator.SpringViewProvider;
import com.vaadin.ui.Button;
import com.vaadin.ui.VerticalLayout;

import ch.ledovy.sewer.i18n.Messages;
import ch.ledovy.sewer.navigation.menu.Menu;
import ch.ledovy.sewer.navigation.menu.MenuEntry;

@SpringComponent
@UIScope
public class ButtonNavigation extends VerticalLayout implements Navigation {
	
	private final Navigator		navigator;
	private SpringViewProvider	viewProvider;
	private Messages			messages;
	
	@Autowired
	public ButtonNavigation(Navigator navigator, SpringViewProvider viewProvider, Messages messages) {
		this.navigator = navigator;
		this.viewProvider = viewProvider;
		this.messages = messages;
		setSpacing(false);
		setMargin(false);
	}
	
	@PreDestroy
	public void shutdown() {
		this.messages.unregister(this);
	}
	
	@Override
	public void createMenu(Menu providedMenu) {
		removeAllComponents();
		List<String> menus = providedMenu.getMenus();
		for (String menu : menus) {
			List<MenuEntry> menuEntries = providedMenu.getMenuEntries(menu);
			VerticalLayout menuLayout = this.messages.registerCaption(menu, new VerticalLayout(), this);
			menuLayout.setSpacing(true);
			menuLayout.setMargin(false);
			for (MenuEntry entry : menuEntries) {
				Button button = createButton(entry.getCaption(), entry.getIcon(), entry.getViewClass());
				menuLayout.addComponent(button);
			}
			addComponent(menuLayout);
		}
	}
	
	private Button createButton(String caption, Resource icon, Class<? extends View> view) {
		Button button = this.messages.registerCaption(caption, new Button(icon), this);
		if (isViewAvailable(view)) {
			button.addClickListener(event -> {
				this.navigator.navigateTo(view);
			});
		} else {
			button.setEnabled(false);
		}
		return button;
	}
	
	private boolean isViewAvailable(Class<? extends View> view) {
		String viewId = this.navigator.getViewId(view);
		Collection<String> availableViews = this.viewProvider.getViewNamesForCurrentUI();
		return availableViews.contains(viewId);
	}
	
}
