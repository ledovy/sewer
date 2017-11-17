package ch.ledovy.sewer.i18n;

import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.vaadin.spring.events.Event;

import com.vaadin.server.VaadinSession;
import com.vaadin.ui.AbstractListing;
import com.vaadin.ui.AbstractTextField;
import com.vaadin.ui.Component;
import com.vaadin.ui.Grid.Column;
import com.vaadin.ui.Label;
import com.vaadin.ui.MenuBar.MenuItem;

import ch.ledovy.sewer.log.HasLogger;

public abstract class ComponentMessages implements Messages, HasLogger {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private final Map<Object, Set<Object>> sourceObjects = new ConcurrentHashMap<>();
	
	private final Map<Component, String> components = new ConcurrentHashMap<>();
	private final Map<MenuItem, String> menuItems = new ConcurrentHashMap<>();
	private final Map<com.vaadin.contextmenu.MenuItem, String> contextItems = new ConcurrentHashMap<>();
	private final Set<AbstractListing<?>> listings = new HashSet<>();
	private final Map<Label, String> labels = new ConcurrentHashMap<>();
	private final Map<Column<?, ?>, String> columns = new ConcurrentHashMap<>();
	private final Map<AbstractTextField, String> placeholders = new ConcurrentHashMap<>();
	
	@Override
	public String get(final String id, final Object... args) {
		Locale locale = VaadinSession.getCurrent().getLocale();
		try {
			return getSource().getMessage(id, args, locale);
		} catch (NullPointerException e) {
			getLogger().error("no message-source set");
			return id;
		} catch (NoSuchMessageException e) {
			getLogger().warn("no message found for key '" + id + "'");
			return id;
		}
	}
	
	@Override
	public <T extends Component> T registerCaption(final String id, final T component, final Object source) {
		getLogger().debug("register caption for " + component + " with key " + id);
		String caption = get(id);
		component.setCaption(caption);
		this.components.put(component, id);
		registerObject(component, source);
		return component;
	}
	
	@Override
	public MenuItem registerMenuItem(final String id, final MenuItem item, final Object source) {
		getLogger().debug("register menuitem " + item + " with key " + id);
		String caption = get(id);
		item.setText(caption);
		this.menuItems.put(item, id);
		registerObject(item, source);
		return item;
	}
	
	@Override
	public com.vaadin.contextmenu.MenuItem registerContextItem(final String id, final com.vaadin.contextmenu.MenuItem item, final Object source) {
		getLogger().debug("register contextitem " + item + " with key " + id);
		String caption = get(id);
		item.setText(caption);
		this.contextItems.put(item, id);
		registerObject(item, source);
		return item;
	}
	
	@Override
	public Label registerLabel(final String id, final Label label, final Object source) {
		getLogger().debug("register label " + label + " with key " + id);
		String caption = get(id);
		label.setValue(caption);
		this.labels.put(label, id);
		registerObject(label, source);
		return label;
	}
	
	@Override
	public <T, P> Column<T, P> registerColumn(final String id, final Column<T, P> column, final Object source) {
		getLogger().debug("register column " + column + " with key " + id);
		String caption = get(id);
		column.setCaption(caption);
		this.columns.put(column, id);
		registerObject(column, source);
		return column;
	}
	
	@Override
	public <T extends AbstractListing<?>> T registerListing(final T listing, final Object source) {
		//TODO maybe move this functionality to an ItemCaptionGenerator
		this.listings.add(listing);
		registerObject(listing, source);
		return listing;
	}
	
	@Override
	public <T extends AbstractTextField> T registerPlaceholder(final String id, final T field, final Object source) {
		getLogger().debug("register placeholder for " + field + " with key " + id);
		String caption = get(id);
		field.setPlaceholder(caption);
		this.placeholders.put(field, id);
		registerObject(field, source);
		return field;
	}
	
	private void registerObject(final Object component, final Object source) {
		if (!this.sourceObjects.containsKey(source)) {
			this.sourceObjects.put(source, new HashSet<>());
		}
		this.sourceObjects.get(source).add(component);
	}
	
	@Override
	public void onEvent(final Event<LocaleChangeEvent> event) {
		getLogger().debug("locale changed to " + event.getPayload().getLocale());
		for (Component c : this.components.keySet()) {
			String id = this.components.get(c);
			c.setCaption(get(id));
		}
		for (MenuItem c : this.menuItems.keySet()) {
			String id = get(this.menuItems.get(c));
			c.setText(id);
		}
		for (com.vaadin.contextmenu.MenuItem c : this.contextItems.keySet()) {
			String id = get(this.contextItems.get(c));
			c.setText(id);
		}
		for (Label c : this.labels.keySet()) {
			String id = get(this.labels.get(c));
			c.setValue(id);
		}
		for (Column<?, ?> c : this.columns.keySet()) {
			String id = get(this.columns.get(c));
			c.setCaption(id);
		}
		for (AbstractTextField c : this.placeholders.keySet()) {
			String id = get(this.placeholders.get(c));
			c.setPlaceholder(id);
		}
		for (AbstractListing<?> listing : this.listings) {
			listing.getDataCommunicator().reset();
		}
	}
	
	@Override
	public void unregister(final Object source) {
		if (this.sourceObjects.containsKey(source)) {
			Set<Object> objects = this.sourceObjects.get(source);
			for (Object o : objects) {
				if (this.components.containsKey(o)) {
					this.components.remove(o);
				}
				if (this.menuItems.containsKey(o)) {
					this.menuItems.remove(o);
				}
				if (this.contextItems.containsKey(o)) {
					this.contextItems.remove(o);
				}
				if (this.labels.containsKey(o)) {
					this.labels.remove(o);
				}
				if (this.columns.containsKey(o)) {
					this.columns.remove(o);
				}
				if (this.listings.contains(o)) {
					this.listings.remove(o);
				}
			}
		}
	}
	
	protected abstract MessageSource getSource();
}
