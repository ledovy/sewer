package ch.ledovy.sewer.navigation;

import java.util.Arrays;
import java.util.Locale;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.events.EventBus;
import org.vaadin.spring.events.EventBus.UIEventBus;
import org.vaadin.spring.events.EventScope;

import com.vaadin.server.VaadinSession;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.AbstractOrderedLayout;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.Panel;
import com.vaadin.ui.SingleComponentContainer;
import com.vaadin.ui.VerticalLayout;

import ch.ledovy.sewer.i18n.LocaleChangeEvent;
import ch.ledovy.sewer.i18n.Messages;
import ch.ledovy.sewer.navigation.menu.Menu;
import ch.ledovy.sewer.security.service.SecurityService;

@SpringComponent
@UIScope
public class Desktop extends Panel {
	
	//	public static final String							DEFAULT_THEME		= GreperyTheme.THEME_NAME;
	private static final Class<? extends Navigation>	DEFAULT_NAVIGATION	= MenuBarNavigation.class;
	private static final Locale							DEFAULT_LOCALE		= Locale.forLanguageTag("de");
	private BeanFactory									beanFactory;
	private UIEventBus									eventBus;
	private Panel										target;
	private ComboBox<String>							themeSelector;
	private ComboBox<Class<? extends Navigation>>		naviTypeSelector;
	private ComboBox<Locale>							localeSelector;
	private Messages									messages;
	private SecurityService								securityService;
	private Menu										menu;
	private ThemeProvider								themeProvider;
	
	@Autowired
	public Desktop(Navigator navigator, BeanFactory beanFactory, EventBus.UIEventBus eventBus, Messages messages, SecurityService securityService, ThemeProvider themeProvider) {
		this.beanFactory = beanFactory;
		this.eventBus = eventBus;
		this.messages = messages;
		this.securityService = securityService;
		this.target = new Panel();
		this.target.setSizeFull();
		this.themeProvider = themeProvider;
		this.menu = beanFactory.getBean(Menu.class);
		
		createSelectors();
		setLanguage(DEFAULT_LOCALE);
		setNavigationType(DEFAULT_NAVIGATION);
	}
	
	public void createSelectors() {
		this.themeSelector = this.messages.registerCaption("main.selector.theme", new ComboBox<>(), this);
		this.themeSelector.setItems(this.themeProvider.getThemes());
		this.themeSelector.setEmptySelectionAllowed(false);
		this.themeSelector.setValue(this.themeProvider.getDefaultTheme());
		this.themeSelector.addValueChangeListener(e -> getUI().setTheme(e.getValue()));
		this.messages.registerListing(this.themeSelector, this);
		
		this.naviTypeSelector = this.messages.registerCaption("main.selector.navi", new ComboBox<>(), this);
		this.naviTypeSelector.setItems(Arrays.asList(ButtonNavigation.class, MenuBarNavigation.class));
		this.naviTypeSelector.setItemCaptionGenerator(item -> {
			if (ButtonNavigation.class.equals(item)) {
				return this.messages.get("main.selector.navi.item.buttons");
			} else if (MenuBarNavigation.class.equals(item)) {
				return this.messages.get("main.selector.navi.item.menubar");
			} else {
				return this.messages.get("main.selector.navi.item.unknown");
			}
		});
		this.naviTypeSelector.setEmptySelectionAllowed(false);
		this.naviTypeSelector.setValue(MenuBarNavigation.class);
		this.naviTypeSelector.addValueChangeListener(e -> setNavigationType(e.getValue()));
		this.messages.registerListing(this.naviTypeSelector, this);
		
		this.localeSelector = this.messages.registerCaption("main.selector.locale", new ComboBox<>(), this);
		this.localeSelector.setItems(Arrays.asList(DEFAULT_LOCALE, Locale.forLanguageTag("en")));
		this.localeSelector.setEmptySelectionAllowed(false);
		this.localeSelector.setValue(DEFAULT_LOCALE);
		this.localeSelector.addValueChangeListener(e -> setLanguage(e.getValue()));
		this.localeSelector.setItemCaptionGenerator(item -> {
			if ("de".equals(item.toString())) {
				return this.messages.get("main.selector.locale.item.de");
			} else if ("en".equals(item.toString())) {
				return this.messages.get("main.selector.locale.item.en");
			} else {
				return this.messages.get("main.selector.locale.item.unknown");
			}
		});
		this.messages.registerListing(this.localeSelector, this);
	}
	
	private void setLanguage(Locale value) {
		VaadinSession.getCurrent().setLocale(value);
		setLocale(value);
		this.eventBus.publish(EventScope.UI, this, new LocaleChangeEvent(value));
	}
	
	private void setNavigationType(Class<? extends Navigation> value) {
		Navigation navigation = this.beanFactory.getBean(value);
		navigation.createMenu(this.menu);
		ComponentContainer oldContent = (ComponentContainer) getContent();
		if (oldContent != null) {
			oldContent.removeAllComponents();
		}
		AbstractOrderedLayout selectors;
		if (ButtonNavigation.class.isAssignableFrom(value)) {
			selectors = new VerticalLayout(this.themeSelector, this.naviTypeSelector, this.localeSelector);
		} else {
			selectors = new HorizontalLayout(this.themeSelector, this.naviTypeSelector, this.localeSelector);
		}
		selectors.setMargin(false);
		selectors.setSpacing(true);
		Component layout = createGui(this.target, navigation, selectors);
		setContent(layout);
	}
	
	public ComponentContainer createGui(Panel content, Navigation navigation, Component selectors) {
		if (!Component.class.isInstance(navigation)) {
			throw new IllegalArgumentException(navigation.getClass() + " is not a " + Component.class.getName());
		}
		Component naviComponent = (Component) navigation;
		final ComponentContainer menu;
		final ComponentContainer root;
		if (ButtonNavigation.class.isInstance(navigation)) {
			menu = new VerticalLayout(naviComponent, selectors);
			HorizontalSplitPanel layout = new HorizontalSplitPanel(menu, content);
			layout.setSizeFull();
			layout.setSplitPosition(15f);
			root = layout;
		} else if (MenuBarNavigation.class.isInstance(navigation)) {
			menu = new HorizontalLayout(naviComponent, selectors);
			VerticalLayout layout = new VerticalLayout(menu, content);
			root = layout;
		} else {
			throw new IllegalArgumentException("Unknown Navigation-type: " + navigation.getClass());
		}
		boolean loggedIn = this.securityService.isLoggedIn();
		menu.setVisible(loggedIn);
		return root;
	}
	
	public SingleComponentContainer getTarget() {
		return this.target;
	}
}
