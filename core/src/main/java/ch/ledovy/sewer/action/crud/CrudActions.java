package ch.ledovy.sewer.action.crud;

import com.vaadin.data.StatusChangeListener;
import com.vaadin.event.selection.SelectionListener;
import com.vaadin.ui.Grid;

import ch.ledovy.sewer.action.Action;
import ch.ledovy.sewer.action.Executor;
import ch.ledovy.sewer.action.crud.AddAction.ItemCreator;
import ch.ledovy.sewer.data.model.HasId;
import ch.ledovy.sewer.data.service.CrudService;
import ch.ledovy.sewer.data.view.UserInteraction;
import ch.ledovy.sewer.data.view.ValueConsumer;
import ch.ledovy.sewer.data.view.ValueSource;
import ch.ledovy.sewer.data.view.form.Form;

public class CrudActions {
	//TODO write tests to create this stuff... (probably not static methods?)
	// - add new item (to table)
	// - delete item with confirmation
	// - update item through editor
	// - create item through wizard
	// - execute simple service-function
	// - execute service-function with parameter and return-value
	//TODO issues
	// - validation
	// - authentication
	// - error handling
	// - react on/update results
	// - generic (save-)action
	//TODO future targets
	// - crud-menu-creation with a single line
	// - grid with detail-view and editor in max. three lines
	public static <T> void createAction(final Executor trigger, final ValueSource<T> source, final Action processing) {
		trigger.setAction(processing);
		//		processing.setValueProvider(source);
		//		source.addChangeListener(t -> {
		//			try {
		//				processing.validate();
		//				trigger.setEnabled(true);
		//			} catch (ch.ledovy.sewer.action.Action.ValidationException e) {
		//				trigger.setEnabled(false);
		//			}
		//		});
	}
	
	public static <T> void createInteractiveAction(final Executor trigger, final ValueSource<T> source, final UserInteraction<T> target, final Action processing) {
		//		target.setProcessingAction(processing);
		//		createAction(trigger, source, () -> {
		//			target.presentToUser();
		//		});
	}
	
	// FIGHT FOR SIMPLICITY!
	
	public static <T> AddAction<T> createAddAction(final Executor executor, final ValueConsumer<T> form, final ItemCreator<T> service) {
		AddAction<T> action = new AddAction<>(form, service);
		executor.setAction(action);
		return action;
	}
	
	public static <T extends HasId<Long>, P> SaveAction<T> createSaveAction(final Executor executor, final Grid<T> grid, final Form<T> form, final CrudService<T> service) {
		SaveAction<T> action = new SaveAction<>(grid, form, service);
		StatusChangeListener listener = event -> {
			if (event != null) {
				// boolean hasChanges = event.getBinder().hasChanges();
				boolean hasValidationErrors = event.hasValidationErrors();
				// executor.setEnabled(hasChanges && !hasValidationErrors);
				executor.setEnabled(!hasValidationErrors);
			} else {
				executor.setEnabled(false);
			}
		};
		form.getBinder().addStatusChangeListener(listener);
		listener.statusChange(null);
		executor.setAction(action);
		return action;
	}
	
	public static <T extends HasId<Long>, P> CancelAction<T> createCancelAction(final Executor executor, final Form<T> form) {
		CancelAction<T> action = new CancelAction<>(form);
		StatusChangeListener listener = event -> {
			if (event != null) {
				boolean hasChanges = event.getBinder().hasChanges();
				executor.setEnabled(hasChanges);
			} else {
				executor.setEnabled(false);
			}
		};
		form.getBinder().addStatusChangeListener(listener);
		listener.statusChange(null);
		executor.setAction(action);
		return action;
	}
	
	public static <T extends HasId<Long>, P> DeleteAction<T> createDeleteAction(final Executor executor, final Grid<T> grid, final CrudService<T> service) {
		DeleteAction<T> action = new DeleteAction<>(grid, service);
		SelectionListener<T> listener = new SelectionActionEnabler<T, P>(action, executor);
		grid.addSelectionListener(listener);
		listener.selectionChange(null);
		executor.setAction(action);
		return action;
	}
	
	public static <T extends HasId<Long>, P> EditAction<T> createEditAction(final Executor executor, final Grid<T> grid, final Form<T> form, final CrudService<T> service) {
		EditAction<T> action = new EditAction<>(grid, form, service);
		SelectionListener<T> listener = new SelectionActionEnabler<T, P>(action, executor);
		grid.addSelectionListener(listener);
		listener.selectionChange(null);
		executor.setAction(action);
		return action;
	}
	
}
