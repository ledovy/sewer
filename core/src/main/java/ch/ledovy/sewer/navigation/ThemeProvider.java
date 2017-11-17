package ch.ledovy.sewer.navigation;

import java.util.Collection;
import java.util.SortedSet;
import java.util.TreeSet;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;

@SpringComponent
@UIScope
public class ThemeProvider {
	private final SortedSet<String> themes = new TreeSet<>();
	private String defaultTheme = null;
	
	public Collection<String> getThemes() {
		return this.themes;
	}
	
	public String getDefaultTheme() {
		return this.defaultTheme;
	}
	
	public void registerTheme(final String themeName) {
		this.themes.add(themeName);
		if (this.defaultTheme == null) {
			this.defaultTheme = themeName;
		}
	}
	
}
