package ch.ledovy.sewer.security.view.user;

import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.VerticalLayout;

import ch.ledovy.sewer.action.ExecutorFactory;
import ch.ledovy.sewer.data.view.CrudActions;
import ch.ledovy.sewer.data.view.CrudView;
import ch.ledovy.sewer.data.view.filter.FilterClearButton;
import ch.ledovy.sewer.data.view.filter.FilterFactory;
import ch.ledovy.sewer.data.view.filter.FilterPresenter;
import ch.ledovy.sewer.data.view.filter.FilterSearchButton;
import ch.ledovy.sewer.i18n.Messages;
import ch.ledovy.sewer.security.model.User;

@SpringView
public class UserAdminView extends VerticalLayout implements CrudView {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Messages messages;
	
	@Autowired
	public UserAdminView(final UserCrudService service, final Messages messages, final UserForm form, final UserAdminGrid list, final UserFilter filterGui) {
		this.messages = messages;
		//Filter
		FilterPresenter<User, UserParameter> filter = FilterFactory.createBackendFilter(filterGui, list.getDataProvider());
		Button search = this.messages.registerCaption("view.model.button.filter.search", new FilterSearchButton(filter), this);
		Button clear = this.messages.registerCaption("view.model.button.filter.clear", new FilterClearButton(filter), this);
		//Buttons
		Button add = this.messages.registerCaption("view.model.button.crud.new", new Button(), this);
		Button delete = this.messages.registerCaption("view.model.button.crud.delete", new Button(), this);
		Button cancel = this.messages.registerCaption("view.model.button.crud.cancel", new Button(), this);
		Button update = this.messages.registerCaption("view.model.button.crud.update", new Button(), this);
		//Layout
		HorizontalLayout actionBar = new HorizontalLayout(filterGui.getForm(), new HorizontalLayout(clear, search), add);
		VerticalLayout editor = new VerticalLayout(form.getForm(), new HorizontalLayout(delete, cancel, update));
		addComponent(actionBar);
		addComponent(new HorizontalSplitPanel(list, editor));
		//Actions
		CrudActions.createDeleteAction(ExecutorFactory.create(delete), list, service);
		CrudActions.createSaveAction(ExecutorFactory.create(update), list, form, service);
		CrudActions.createCancelAction(ExecutorFactory.create(cancel), form);
		CrudActions.createAddAction(ExecutorFactory.create(add), form, service);
		CrudActions.createEditAction(ExecutorFactory.create(list), list, form, service);
	}
	
	@PreDestroy
	public void shutdown() {
		this.messages.unregister(this);
	}
}
