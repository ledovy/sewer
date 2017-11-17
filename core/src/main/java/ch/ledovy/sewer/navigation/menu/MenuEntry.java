package ch.ledovy.sewer.navigation.menu;

import com.vaadin.navigator.View;
import com.vaadin.server.Resource;

public class MenuEntry {
	
	private final String caption;
	private final Resource icon;
	private final Class<? extends View> viewClass;
	
	public MenuEntry(final String caption, final Resource icon, final Class<? extends View> viewClass) {
		this.caption = caption;
		this.icon = icon;
		this.viewClass = viewClass;
	}
	
	public String getCaption() {
		return this.caption;
	}
	
	public Resource getIcon() {
		return this.icon;
	}
	
	public Class<? extends View> getViewClass() {
		return this.viewClass;
	}
	
}
