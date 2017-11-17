package ch.ledovy.sewer.i18n;

import org.vaadin.spring.events.EventBusListener;

import com.vaadin.ui.AbstractListing;
import com.vaadin.ui.AbstractTextField;
import com.vaadin.ui.Component;
import com.vaadin.ui.Grid.Column;
import com.vaadin.ui.Label;
import com.vaadin.ui.MenuBar.MenuItem;

public interface Messages extends EventBusListener<LocaleChangeEvent> {
	
	String get(String id, Object... args);
	
	<T extends Component> T registerCaption(String id, T component, Object source);
	
	MenuItem registerMenuItem(String id, MenuItem item, Object source);
	
	Label registerLabel(String id, Label label, Object source);
	
	<T extends AbstractListing<?>> T registerListing(T listing, Object source);
	
	<T, P> Column<T, P> registerColumn(String id, Column<T, P> column, Object source);
	
	<T extends AbstractTextField> T registerPlaceholder(String id, T field, Object source);
	
	com.vaadin.contextmenu.MenuItem registerContextItem(String id, com.vaadin.contextmenu.MenuItem item, Object source);
	
	void unregister(Object source);
	
}
