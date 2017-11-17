package ch.ledovy.sewer.dialog;

import java.util.Arrays;
import java.util.List;

import org.vaadin.dialogs.DefaultConfirmDialogFactory;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.Button;
import com.vaadin.ui.themes.ValoTheme;

/**
 * Singleton factory for creating the "are you sure"-type confirmation dialogs
 * in the application.
 */
@SpringComponent
public class ConfirmDialogFactory extends DefaultConfirmDialogFactory {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Override
	protected List<Button> orderButtons(final Button cancel, final Button notOk, final Button ok) {
		return Arrays.asList(ok, cancel);
	}
	
	@Override
	protected Button buildOkButton(final String okCaption) {
		Button okButton = super.buildOkButton(okCaption);
		okButton.addStyleName(ValoTheme.BUTTON_DANGER);
		return okButton;
	}
}
