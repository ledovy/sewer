package ch.ledovy.sewer.action.crud;

import java.util.ArrayList;
import java.util.Collection;

import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.annotation.PrototypeScope;
import org.vaadin.spring.events.EventBus.ApplicationEventBus;
import org.vaadin.spring.events.annotation.EventBusListenerMethod;

import com.vaadin.data.provider.DataProvider;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.Grid;

@SpringComponent
@PrototypeScope
public class SewerGrid<T> {
	private ApplicationEventBus eventBus;
	private final Grid<T> grid;
	private Collection<T> items;
	
	@Autowired
	public SewerGrid(final ApplicationEventBus eventBus) {
		this.eventBus = eventBus;
		eventBus.subscribe(this);
		this.grid = new Grid<T>() {
			@Override
			public void setItems(final Collection<T> items) {
				SewerGrid.this.items = items;
				super.setItems(items);
			}
		};
		this.grid.setItems(new ArrayList<>());
	}
	
	@PreDestroy
	public void shutdown() {
		this.eventBus.unsubscribe(this);
	}
	
	public Grid<T> getView() {
		return this.grid;
	}
	public SewerGrid<T> withConfiguration(final GridConfigurator<T> config) {
		config.configure(this.grid);
		return this;
	}
	
	public SewerGrid<T> withDataProvider(final DataProvider<T, ?> provider) {
		//		this.grid.setDataProvider(provider);
		return this;
	}
	
	@EventBusListenerMethod
	public void handleAddItem(final AddItemEvent<T> event) {
		addItem(event.getData());
	}
	@EventBusListenerMethod
	public void handleUpdateItem(final UpdateItemEvent<T> event) {
		updateItem(event.getData());
	}
	@EventBusListenerMethod
	public void handleRemoveItem(final RemoveItemEvent<T> event) {
		removeItem(event.getData());
	}
	
	private void addItem(final T item) {
		if (!this.items.contains(item)) {
			this.items.add(item);
		}
	}
	private void updateItem(final T item) {
		if (this.items.contains(item)) {
			removeItem(item);
			addItem(item);
		}
	}
	private void removeItem(final T item) {
		this.items.remove(item);
	}
}
