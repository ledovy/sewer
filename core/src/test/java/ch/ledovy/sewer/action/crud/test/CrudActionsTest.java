package ch.ledovy.sewer.action.crud.test;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.SpringRunner;
import org.vaadin.spring.events.EventBus;

import com.vaadin.data.provider.Query;
import com.vaadin.event.selection.SelectionListener;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;

import ch.ledovy.sewer.action.Action;
import ch.ledovy.sewer.action.Action.ValidationException;
import ch.ledovy.sewer.action.Executor;
import ch.ledovy.sewer.action.ExecutorFactory;
import ch.ledovy.sewer.action.crud.AddItemEvent;
import ch.ledovy.sewer.action.crud.GridFactory;
import ch.ledovy.sewer.action.crud.RemoveItemEvent;
import ch.ledovy.sewer.action.crud.SewerGrid;
import ch.ledovy.sewer.action.crud.UpdateItemEvent;
import ch.ledovy.sewer.action.crud.legacy.CrudActions;
import ch.ledovy.sewer.action.crud.test.model.Planet;
import ch.ledovy.sewer.data.view.ValueProvider;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CrudActionsTest {
	
	@Configuration
	@ComponentScan(basePackages = {"ch.ledovy.sewer.action.crud", "org.vaadin.spring.events"})
	public static class TestConfig {
		
	}
	
	@Autowired
	private EventBus.ApplicationEventBus eventBus;
	@Autowired
	private GridFactory factory;
	private Grid<Planet> grid;
	private Button button;
	private SewerGrid<Planet> sewerGrid;
	
	@Before
	public void setUp() throws Exception {
		this.sewerGrid = this.factory.createVolatileGrid(grid -> {
		});
		this.grid = this.sewerGrid.getView();
		List<Planet> items = getGridItems(this.grid);
		Assert.assertEquals(0, items.size());
		Assert.assertEquals(items.size(), items.size());
		this.button = new Button();
	}
	@After
	public void tearDown() throws Exception {
		this.sewerGrid.shutdown();
	}
	
	@Test
	public void testNewItems() {
		CrudActions.createAction(ExecutorFactory.create(this.button), () -> new Planet(), p -> addItem(p));
		assertSizeOfGrid(0);
		
		executeAction();
		assertSizeOfGrid(1);
		
		executeAction();
		assertSizeOfGrid(2);
	}
	
	@Test
	public void testAddOneSpecificItem() {
		Planet example = new Planet();
		
		CrudActions.createAction(ExecutorFactory.create(this.button), () -> example, p -> addItem(p));
		assertSizeOfGrid(0);
		
		executeAction();
		assertSizeOfGrid(1);
		assertGridElements(example);
	}
	
	@Test
	public void testAddTwoDifferentItems() {
		Planet p1 = new Planet();
		Planet p2 = new Planet();
		Iterator<Planet> iterator = Arrays.asList(p1, p2).iterator();
		
		CrudActions.createAction(ExecutorFactory.create(this.button), () -> iterator.next(), p -> addItem(p), null);
		assertSizeOfGrid(0);
		
		executeAction();
		assertGridElements(p1);
		
		executeAction();
		assertGridElements(p1, p2);
	}
	@Test
	public void testAddTheSameItemTwice() {
		Planet itemSingleton = new Planet();
		
		CrudActions.createAction(ExecutorFactory.create(this.button), () -> itemSingleton, p -> addItem(p));
		assertSizeOfGrid(0);
		
		executeAction();
		assertGridElements(itemSingleton);
		
		executeAction();
		assertGridElements(itemSingleton);
		
	}
	
	@Test
	public void testRemovePreviouslyAddedItem() {
		Planet itemUnchanged = addPlanet();
		Planet itemToRemove = addPlanet();
		addItem(itemToRemove);
		
		CrudActions.createAction(ExecutorFactory.create(this.button), () -> itemToRemove, p -> removeItem(p));
		assertGridElements(itemUnchanged, itemToRemove);
		
		executeAction();
		assertGridElements(itemUnchanged);
	}
	@Test
	public void testRemoveInexistentItem() {
		Planet itemExistent1 = addPlanet();
		Planet itemExistent2 = addPlanet();
		Planet itemInexistent = new Planet();
		
		CrudActions.createAction(ExecutorFactory.create(this.button), () -> itemInexistent, p -> removeItem(p));
		assertGridElements(itemExistent1, itemExistent2);
		
		executeAction();
		assertGridElements(itemExistent1, itemExistent2);
	}
	@Test
	public void testUpdateSelectedItem() {
		String nameUnchanged = "A";
		String nameBefore = "B";
		String nameAfter = "Bb";
		Planet itemUnchanged = addPlanet(nameUnchanged);
		Planet itemToUpdate = addPlanet(nameBefore);
		
		//add update action
		Executor executor = ExecutorFactory.create(this.button);
		ValueProvider<Planet> source = () -> this.grid.getSelectedItems().iterator().next();
		Action action = CrudActions.createAction(executor, source, p -> {
			p.setName(nameAfter);
			//			updateItem(p);
		}, () -> {
			if (source.getValue() == null) {
				throw new ValidationException("no value");
			}
		});
		action.validate();
		SelectionListener<Planet> listener = event -> {
			action.validate();
		};
		this.grid.addSelectionListener(listener);
		assertGridElements(itemUnchanged, itemToUpdate);
		Assert.assertEquals(nameUnchanged, itemUnchanged.getName());
		Assert.assertEquals(nameBefore, itemToUpdate.getName());
		
		this.grid.deselectAll();
		Assert.assertFalse(this.button.isEnabled());
		
		this.grid.select(itemToUpdate);
		Assert.assertTrue(this.button.isEnabled());
		
		//execute update action
		executeAction();
		//		assertGridElements(itemUnchanged, itemToUpdate);
		//		Assert.assertEquals(nameUnchanged, itemUnchanged.getName());
		//		Assert.assertEquals(nameBefore, itemToUpdate.getName());
		//		
		//		updateItem(itemToUpdate);
		assertGridElements(itemUnchanged, itemToUpdate);
		Assert.assertEquals(nameUnchanged, itemUnchanged.getName());
		Assert.assertEquals(nameAfter, itemToUpdate.getName());
		
	}
	
	@Test
	public void testUpdateInexistentItem() {
		String nameBefore = "B";
		String nameAfter = "Bb";
		
		//prepare
		Planet itemExistent1 = addPlanet();
		Planet itemExistent2 = addPlanet();
		Planet itemInexistent = createPlanet(nameBefore);
		
		//add update action
		CrudActions.createAction(ExecutorFactory.create(this.button), () -> itemInexistent, p -> {
			p.setName(nameAfter);
			updateItem(p);
		}, null);
		assertGridElements(itemExistent1, itemExistent2);
		
		//execute update action
		executeAction();
		assertGridElements(itemExistent1, itemExistent2);
		Assert.assertEquals(nameAfter, itemInexistent.getName());
	}
	
	/*
	 * FLAVORS
	 * - use these test for the different flavors (different setup)
	 * - open editor in between (wizard)
	 * - different dataprovider -> service/repository
	 * - feedback-handling / eventbus
	 * - async
	 * - use a service for add/update/remove
	 * - batch-processing
	 * ASSERTIONS
	 * - items in grid
	 * - values in column
	 * - state of the button
	 * IDEAS
	 * ActionFactory.create()
	 * 		.withSource(grid)
	 * 		.withValidator(p -> if(p==null) throw new NPE)
	 * 		.withExecution(p -> service.update(p))
	 * 		.forButton(button);
	 * 
	 */
	
	/*
	 * GRID-CUSTOMIZATION
	 * class SewerGrid<T> extends? Grid<T>: @ViewScope, EventBus-Handler
	 * interface GridConfigurator: void configure(Grid<T> grid)
	 * VolatileDataProvider: inmem-filter
	 * PersistentDataProvider: db-filter
	 * GridFactory.createVolatileGrid(config) { return factory.getBean(SewerGrid.class).withConfiguration(config).withDataProvider(inmem); }
	 * GridFactory.createPersistentGrid(config) return factory.getBean(SewerGrid.class).withConfiguration(config).withDataProvider(db);
	 */
	
	private List<Planet> getGridItems(final Grid<Planet> grid) {
		return grid.getDataProvider().fetch(new Query<>()).collect(Collectors.toList());
	}
	private Planet addPlanet(final String name) {
		Planet itemUnchanged = createPlanet(name);
		addItem(itemUnchanged);
		return itemUnchanged;
	}
	private Planet addPlanet() {
		return addPlanet(null);
	}
	private Planet createPlanet(final String name) {
		Planet itemUnchanged = new Planet();
		itemUnchanged.setName(name);
		return itemUnchanged;
	}
	private Planet addItem(final Planet p) {
		this.eventBus.publish(this, (new AddItemEvent<Planet>(p)));
		return p;
	}
	private Planet updateItem(final Planet item) {
		this.eventBus.publish(this, (new UpdateItemEvent<Planet>(item)));
		return item;
	}
	private Planet removeItem(final Planet p) {
		this.eventBus.publish(this, (new RemoveItemEvent<Planet>(p)));
		return p;
	}
	private void executeAction() {
		this.button.click();
	}
	private void assertSizeOfGrid(final int size) {
		List<Planet> items = getGridItems(this.grid);
		Assert.assertEquals(size, items.size());
	}
	
	private void assertGridElements(final Planet... example) {
		List<Planet> items = getGridItems(this.grid);
		Assert.assertEquals(example.length, items.size());
		for (int i = 0; i < example.length; i++) {
			Assert.assertEquals(example[i], items.get(i));
		}
	}
}
