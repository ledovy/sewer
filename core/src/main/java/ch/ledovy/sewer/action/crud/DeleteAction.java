package ch.ledovy.sewer.action.crud;

import java.util.Set;

import org.springframework.dao.DataIntegrityViolationException;

import com.vaadin.ui.Grid;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;

import ch.ledovy.sewer.action.Action;
import ch.ledovy.sewer.data.model.HasId;
import ch.ledovy.sewer.data.service.CrudService;

public class DeleteAction<T extends HasId<Long>> implements Action {
	private Grid<T> grid;
	private CrudService<T> service;
	
	public DeleteAction(final Grid<T> grid, final CrudService<T> service) {
		this.grid = grid;
		this.service = service;
	}
	
	@Override
	public void execute() {
		delete();
	}
	
	@Override
	public void validate() throws ValidationException {
		Set<T> selectedItems = this.grid.getSelectedItems();
		if (selectedItems.isEmpty()) {
			throw new ValidationException("Nichts ausgewählt");
		}
	}
	
	private void delete() {
		Set<T> selectedItems = this.grid.getSelectedItems();
		try {
			for (T item : selectedItems) {
				deleteItem(item);
			}
		} catch (DataIntegrityViolationException e) {
			Notification.show("The given entity cannot be deleted as there are references to it in the database", Type.ERROR_MESSAGE);
			getLogger().error("Unable to delete entity", e);
			return;
		}
		this.grid.getDataProvider().refreshAll();
		// revertToInitialState();
		// TODO send event?
	}
	
	private void deleteItem(final T item) {
		if (item.isNew()) {
			getLogger().error("Unable to delete new entity: " + item);
		} else {
			Long id = item.getId();
			this.service.delete(id);
		}
	}
}
