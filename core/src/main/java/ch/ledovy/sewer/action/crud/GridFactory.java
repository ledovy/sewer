package ch.ledovy.sewer.action.crud;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.spring.annotation.SpringComponent;

@SpringComponent
public class GridFactory {
	private BeanFactory factory;
	
	@Autowired
	public GridFactory(final BeanFactory factory) {
		this.factory = factory;
	}
	
	public <T> SewerGrid<T> createVolatileGrid(final GridConfigurator config) {
		return this.factory.getBean(SewerGrid.class).withConfiguration(config).withDataProvider(null);
	}
	
}
