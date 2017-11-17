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
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;

import ch.ledovy.sewer.action.ExecutorFactory;
import ch.ledovy.sewer.action.crud.AddItemEvent;
import ch.ledovy.sewer.action.crud.GridFactory;
import ch.ledovy.sewer.action.crud.RemoveItemEvent;
import ch.ledovy.sewer.action.crud.SewerGrid;
import ch.ledovy.sewer.action.crud.UpdateItemEvent;
import ch.ledovy.sewer.action.crud.legacy.CrudActions;
import ch.ledovy.sewer.action.crud.test.model.Planet;

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
	

	private List<Planet> getGridItems(final Grid<Planet> grid) {
		return grid.getDataProvider().fetch(new Query<>()).collect(Collectors.toList());
	}
	private void addItem(final Planet p) {
		this.eventBus.publish(this, (new AddItemEvent<Planet>(p)));
	}
	private void updateItem(final Planet item) {
		this.eventBus.publish(this, (new UpdateItemEvent<Planet>(item)));
	}
	private void removeItem(final Planet p) {
		this.eventBus.publish(this, (new RemoveItemEvent<Planet>(p)));
	}
	
	@Test
	public void testNewItems() {
		//prepare
		CrudActions.createAction(ExecutorFactory.create(this.button), () -> new Planet(), p -> addItem(p));
		assertSizeOfGrid(0);
		
		//run
		this.button.click();
		//assert
		assertSizeOfGrid(1);
		
		//run
		this.button.click();
		//assert
		assertSizeOfGrid(2);
	}
	
	@Test
	public void testAddOneSpecificItem() {
		//prepare
		Planet example = new Planet();
		CrudActions.createAction(ExecutorFactory.create(this.button), () -> example, p -> addItem(p));
		assertSizeOfGrid(0);
		
		//run
		this.button.click();
		//assert
		assertSizeOfGrid(1);
		assertGridElements(example);
	}
	
	@Test
	public void testAddTwoDifferentItems() {
		//prepare
		Planet p1 = new Planet();
		Planet p2 = new Planet();
		Iterator<Planet> iterator = Arrays.asList(p1, p2).iterator();
		CrudActions.createAction(ExecutorFactory.create(this.button), () -> iterator.next(), p -> addItem(p));
		assertSizeOfGrid(0);
		
		//run
		this.button.click();
		//assert
		assertSizeOfGrid(1);
		assertGridElements(p1);
		
		//run
		this.button.click();
		//assert
		assertSizeOfGrid(2);
		assertGridElements(p1, p2);
	}
	@Test
	public void testAddTheSameItemTwice() {
		//prepare
		Planet itemSingleton = new Planet();
		CrudActions.createAction(ExecutorFactory.create(this.button), () -> itemSingleton, p -> addItem(p));
		assertSizeOfGrid(0);
		
		//run
		this.button.click();
		
		//assert
		assertSizeOfGrid(1);
		assertGridElements(itemSingleton);
		
		//run
		this.button.click();
		//assert
		assertSizeOfGrid(1);
		assertGridElements(itemSingleton);
		
	}
	
	@Test
	public void testRemovePreviouslyAddedItem() {
		//prepare
		Planet itemUnchanged = new Planet();
		addItem(itemUnchanged);
		Planet itemToRemove = new Planet();
		addItem(itemToRemove);
		CrudActions.createAction(ExecutorFactory.create(this.button), () -> itemToRemove, p -> removeItem(p));
		assertSizeOfGrid(2);
		assertGridElements(itemUnchanged, itemToRemove);
		
		//run
		this.button.click();
		
		//assert
		assertGridElements(itemUnchanged);
	}
	@Test
	public void testRemoveInexistentItem() {
		//prepare
		Planet itemExistent1 = new Planet();
		addItem(itemExistent1);
		Planet itemExistent2 = new Planet();
		addItem(itemExistent2);
		Planet itemInexistent = new Planet();
		CrudActions.createAction(ExecutorFactory.create(this.button), () -> itemInexistent, p -> removeItem(p));
		assertSizeOfGrid(2);
		assertGridElements(itemExistent1, itemExistent2);
		
		//run
		this.button.click();
		
		//assert
		assertGridElements(itemExistent1, itemExistent2);
		
	}
	@Test
	public void testUpdateSelectedItem() {
		//prepare
		String nameUnchanged = "A";
		String nameBefore = "B";
		String nameAfter = "Bb";
		Planet itemUnchanged = new Planet();
		addItem(itemUnchanged);
		itemUnchanged.setName(nameUnchanged);
		Planet itemToUpdate = new Planet();
		addItem(itemToUpdate);
		itemToUpdate.setName(nameBefore);
		this.grid.select(itemToUpdate);
		CrudActions.createAction(ExecutorFactory.create(this.button), () -> this.grid.getSelectedItems().iterator().next(), p -> {
			p.setName(nameAfter);
			updateItem(p);
		});
		assertSizeOfGrid(2);
		assertGridElements(itemUnchanged, itemToUpdate);
		Assert.assertEquals(nameUnchanged, itemUnchanged.getName());
		Assert.assertEquals(nameBefore, itemToUpdate.getName());
		
		//run
		this.button.click();
		
		//assert
		assertGridElements(itemUnchanged, itemToUpdate);
		Assert.assertEquals(nameUnchanged, itemUnchanged.getName());
		Assert.assertEquals(nameAfter, itemToUpdate.getName());
		
	}
	
	@Test
	public void testUpdateInexistentItem() {
		//prepare
		Planet itemExistent1 = new Planet();
		addItem(itemExistent1);
		Planet itemExistent2 = new Planet();
		addItem(itemExistent2);
		String nameBefore = "B";
		String nameAfter = "Bb";
		Planet itemInexistent = new Planet();
		itemInexistent.setName(nameBefore);
		CrudActions.createAction(ExecutorFactory.create(this.button), () -> itemInexistent, p -> {
			p.setName(nameAfter);
			updateItem(p);
		});
		assertSizeOfGrid(2);
		assertGridElements(itemExistent1, itemExistent2);
		
		//run
		this.button.click();
		
		//assert
		assertGridElements(itemExistent1, itemExistent2);
		Assert.assertEquals(nameAfter, itemInexistent.getName());
	}
	/*
	 * FLAVORS
	 * - open editor in between
	 * - different dataprovider -> service/repository
	 * - feedback-handling / eventbus
	 * - async
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
