package ch.ledovy.sewer.action.crud.test;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.vaadin.data.provider.Query;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;

import ch.ledovy.sewer.action.ExecutorFactory;
import ch.ledovy.sewer.action.crud.CrudActions;
import ch.ledovy.sewer.action.crud.test.model.Planet;

@SpringBootTest
public class CrudActionsTest {
	
	private Grid<Planet> grid;
	private Button button;
	
	@Before
	public void setUp() throws Exception {
		this.grid = new Grid<>(Planet.class);
		Assert.assertNotNull(this.grid);
		List<Planet> items = getGridItems(this.grid);
		Assert.assertEquals(0, items.size());
		this.button = new Button();
		Assert.assertNotNull(this.button);
	}
	
	private List<Planet> getGridItems(final Grid<Planet> grid) {
		return grid.getDataProvider().fetch(new Query<>()).collect(Collectors.toList());
	}
	
	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void testCreateAction() {
		//prepare
		CrudActions.createAction(ExecutorFactory.create(this.button), () -> new Planet(), p -> this.grid.setItems(p));
		
		//run
		this.button.click();
		
		//assert
		List<Planet> items = getGridItems(this.grid);
		Assert.assertEquals(1, items.size());
	}
	
}
