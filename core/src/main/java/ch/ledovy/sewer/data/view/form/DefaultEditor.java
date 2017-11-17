package ch.ledovy.sewer.data.view.form;

import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

import ch.ledovy.sewer.i18n.Messages;

public class DefaultEditor<T> extends Window implements FormActivationListener, FormDeactivationListener {
	private Button cancel;
	private Button save;
	private final Panel formContainer;
	private Form<T> form;
	
	public DefaultEditor(final Form<T> form, final Messages messages) {
		cancel = messages.registerCaption("view.books.form.cancel", new Button(), this);
		save = messages.registerCaption("view.books.form.save", new Button(), this);
		
		setModal(true);
		formContainer = new Panel();
		setForm(form);
		setContent(new Panel(new VerticalLayout(formContainer, new HorizontalLayout(cancel, save))));
	}
	
	public Form<T> getForm() {
		return form;
	}
	
	public void setForm(final Form<T> form) {
		this.form = form;
		formContainer.setContent(form.getForm());
	}
	
	@Override
	public void formActivated() {
		open();
	}
	
	@Override
	public void formDeactivated() {
		close();
	}
	
	public void open() {
		UI.getCurrent().addWindow(this);
	}
	
//	@Override
//	public void close() {
//		UI.getCurrent().removeWindow(this);
//	}
	
	public Button getCancel() {
		return cancel;
	}
	
	public Button getSave() {
		return save;
	}
	
}
