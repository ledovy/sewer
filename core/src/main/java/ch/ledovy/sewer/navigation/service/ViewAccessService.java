package ch.ledovy.sewer.navigation.service;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.access.annotation.Secured;
import org.vaadin.spring.annotation.PrototypeScope;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.SpringView;

@SpringComponent
@PrototypeScope
//@UIScope
public class ViewAccessService {
	private final Map<String, Set<String>> rolesPerView = new ConcurrentHashMap<>();
	private final ApplicationContext context;
	
	@Autowired
	public ViewAccessService(final ApplicationContext context) {
		this.context = context;
		init();
		
	}
	
	public void init() {
		this.rolesPerView.clear();
		if (this.context != null) {
			String[] springViews = this.context.getBeanNamesForAnnotation(SpringView.class);
			for (String view : springViews) {
				Class<?> viewType = this.context.getType(view);
				Set<String> roles = new HashSet<>();
				Secured securedInfo = viewType.getAnnotation(Secured.class);
				if (securedInfo != null) {
					String[] neededRoles = securedInfo.value();
					if (neededRoles != null) {
						roles.addAll(Arrays.asList(neededRoles));
					}
				}
				this.rolesPerView.put(view, roles);
			}
		}
	}
	
	public Set<String> getNeededRoles(final String viewName) {
		if (this.rolesPerView.containsKey(viewName)) {
			return this.rolesPerView.get(viewName);
		} else {
			//TODO throw exception?
			return Collections.emptySet();
		}
	}
}
