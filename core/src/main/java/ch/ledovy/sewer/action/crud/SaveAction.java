package ch.ledovy.sewer.action.crud;

import java.util.List;

import org.springframework.dao.OptimisticLockingFailureException;

import com.vaadin.data.BindingValidationStatus;
import com.vaadin.data.HasValue;
import com.vaadin.data.ValidationResult;
import com.vaadin.data.provider.DataProvider;
import com.vaadin.ui.Component.Focusable;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;

import ch.ledovy.sewer.action.Action;
import ch.ledovy.sewer.data.model.HasId;
import ch.ledovy.sewer.data.service.CrudService;
import ch.ledovy.sewer.data.view.form.Form;
import ch.ledovy.sewer.data.view.form.FormDeactivationListener;

public class SaveAction<T extends HasId<Long>> implements Action {
	private Form<T> form;
	private CrudService<T> service;
	private Grid<T> grid;
	private FormDeactivationListener formDeactivationListener = () -> {
	};
	
	public SaveAction(final Grid<T> grid, final Form<T> form, final CrudService<T> service) {
		this.grid = grid;
		this.form = form;
		this.service = service;
	}
	
	@Override
	public void execute() {
		save();
	}
	
	@Override
	public void validate() throws ValidationException {
		try {
			this.form.validate();
		} catch (Exception e) {
			throw new ValidationException(e.getMessage());
		}
	}
	
	public void save() {
		T item;
		try {
			// The validate() call is needed only to ensure that the error
			// indicator is properly shown for the field in case of an error
			// this.binder.validate();
			// this.binder.writeBean(this.editItem);
			item = this.form.getValue();
		} catch (com.vaadin.data.ValidationException e) {
			// Commit failed because of validation errors
			List<BindingValidationStatus<?>> fieldErrors = e.getFieldValidationErrors();
			if (!fieldErrors.isEmpty()) {
				// Field level error
				HasValue<?> firstErrorField = fieldErrors.get(0).getField();
				if (firstErrorField instanceof Focusable) {
					((Focusable) firstErrorField).focus();
				} else {
					getLogger().warn("Unable to focus field of type " + firstErrorField.getClass().getName());
				}
			} else {
				// Bean validation error
				ValidationResult firstError = e.getBeanValidationErrors().get(0);
				Notification.show(firstError.getErrorMessage(), Type.ERROR_MESSAGE);
			}
			return;
		}
		if (item == null) {
			return;
		}
		
		// boolean isNew = item.isNew();
		T entity;
		try {
			entity = this.service.save(item);
		} catch (OptimisticLockingFailureException e) {
			// Somebody else probably edited the data at the same time
			Notification.show("Somebody else might have updated the data. Please refresh and try again.", Type.ERROR_MESSAGE);
			getLogger().debug("Optimistic locking error while saving entity of type " + item.getClass().getName(), e);
			return;
		} catch (Exception e) {
			// Something went wrong, no idea what
			Notification.show("A problem occured while saving the data. Please check the fields.", Type.ERROR_MESSAGE);
			getLogger().error("Unable to save entity of type " + item.getClass().getName(), e);
			return;
		}
		
		DataProvider<T, ?> dataProvider = this.grid.getDataProvider();
		// if (isNew) {
		dataProvider.refreshAll();
		// } else {
		// dataProvider.refreshItem(entity);
		// }
		this.grid.select(entity);
		// setEditItem(entity);
		this.formDeactivationListener.formDeactivated();
	}
	
	public SaveAction<T> withFormDeactivationListener(final FormDeactivationListener formDeactivationListener) {
		this.formDeactivationListener = formDeactivationListener;
		return this;
	}
}
