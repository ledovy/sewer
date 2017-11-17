package ch.ledovy.sewer.i18n;

import java.util.Locale;

public class LocaleChangeEvent {
	
	private Locale locale;
	
	public LocaleChangeEvent(Locale locale) {
		this.locale = locale;
	}
	
	public Locale getLocale() {
		return this.locale;
	}
}
