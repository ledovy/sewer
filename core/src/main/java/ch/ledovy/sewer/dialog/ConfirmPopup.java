package ch.ledovy.sewer.dialog;

import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.dialogs.ConfirmDialog;
import org.vaadin.spring.annotation.PrototypeScope;

import com.vaadin.navigator.View;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.UI;

@SpringComponent
@PrototypeScope
public class ConfirmPopup {
	
	@Autowired
	public ConfirmDialogFactory confirmDialogFactory;
	
	/**
	 * Shows the standard before leave confirm dialog on given ui. If the user
	 * confirms the the navigation, the given {@literal runOnConfirm} will be
	 * executed. Otherwise, nothing will be done.
	 *
	 * @param view
	 *            the view in which to show the dialog
	 * @param runOnConfirm
	 *            the runnable to execute if the user presses {@literal confirm}
	 *            in the dialog
	 */
	public void showLeaveViewConfirmDialog(final View view, final Runnable runOnConfirm) {
		showLeaveViewConfirmDialog(view, runOnConfirm, () -> {
			// Do nothing on cancel
		});
	}
	
	/**
	 * Shows the standard before leave confirm dialog on given ui. If the user
	 * confirms the the navigation, the given {@literal runOnConfirm} will be
	 * executed. Otherwise, the given {@literal runOnCancel} runnable will be
	 * executed.
	 *
	 * @param view
	 *            the view in which to show the dialog
	 * @param runOnConfirm
	 *            the runnable to execute if the user presses {@literal confirm}
	 *            in the dialog
	 * @param runOnCancel
	 *            the runnable to execute if the user presses {@literal cancel}
	 *            in the dialog
	 */
	public void showLeaveViewConfirmDialog(final View view, final Runnable runOnConfirm, final Runnable runOnCancel) {
		UI ui = view.getViewComponent().getUI();
		showLeaveViewConfirmDialog(ui, runOnConfirm, runOnCancel);
	}
	
	public void showLeaveViewConfirmDialog(final UI ui, final Runnable runOnConfirm, final Runnable runOnCancel) {
		ConfirmDialog dialog = this.confirmDialogFactory.create("Please confirm", "You have unsaved changes that will be discarded if you navigate away.", "Discard Changes", "Cancel", null);
		dialog.show(ui, event -> {
			if (event.isConfirmed()) {
				runOnConfirm.run();
			} else {
				runOnCancel.run();
			}
		}, true);
	}
	
}
