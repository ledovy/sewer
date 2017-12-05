package ch.ledovy.sewer.action.crud.legacy;

import java.util.Set;

import com.vaadin.ui.Grid;

import ch.ledovy.sewer.action.Action;
import ch.ledovy.sewer.data.model.HasId;
import ch.ledovy.sewer.data.service.CrudService;
import ch.ledovy.sewer.data.view.form.Form;
import ch.ledovy.sewer.data.view.form.FormActivationListener;

public class EditAction<T extends HasId<Long>> implements Action {
	
	private Grid<T> grid;
	private Form<T> form;
	private CrudService<T> service;
	private FormActivationListener formActivationListener;
	
	public EditAction(final Grid<T> grid, final Form<T> form, final CrudService<T> service) {
		this.grid = grid;
		this.form = form;
		this.service = service;
	}
	
	@Override
	public void execute() {
		edit();
	}
	
	@Override
	public void validate() {
		Set<T> selectedItems = this.grid.getSelectedItems();
		if (selectedItems.isEmpty()) {
			//			throw new ValidationException("Nichts ausgewählt");
		}
		if (selectedItems.size() > 1) {
			//			throw new ValidationException("Zuviel ausgewählt");
		}
	}
	
	@Override
	public void fail(final Exception e) {
		this.form.reset();
	}
	
	private void edit() {
		// Grid<T> grid = this.view.getGrid();
		Set<T> selectedItems = this.grid.getSelectedItems();
		if (selectedItems.size() == 1) {
			T entity = selectedItems.iterator().next();
			T freshEntity = this.service.load(entity.getId());
			// editItem(freshEntity);
			this.form.setValue(freshEntity);
			if (this.formActivationListener != null) {
				this.formActivationListener.formActivated();
			}
		}
	}
	
	public EditAction<T> withFormActivationListener(final FormActivationListener formActivationListener) {
		this.formActivationListener = formActivationListener;
		return this;
	}
}
