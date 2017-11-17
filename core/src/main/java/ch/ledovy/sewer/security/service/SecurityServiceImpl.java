package ch.ledovy.sewer.security.service;

import java.util.Collection;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.vaadin.server.VaadinService;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.communication.PushMode;
import com.vaadin.shared.ui.ui.Transport;
import com.vaadin.ui.UI;

import ch.ledovy.sewer.log.HasLogger;
import ch.ledovy.sewer.security.model.User;

@Service
public class SecurityServiceImpl implements SecurityService, HasLogger {
	private AuthenticationManager authenticationManager;
	private UserService userService;
	
	@Autowired
	public SecurityServiceImpl(final AuthenticationManager authenticationManager, final UserService userService) {
		this.authenticationManager = authenticationManager;
		this.userService = userService;
	}
	
	/**
	 * Check if currently signed-in user is in the role with the given role
	 * name.
	 *
	 * @param role
	 *            the role to check for
	 * @return <code>true</code> if user is in the role, <code>false</code>
	 *         otherwise
	 */
	@Override
	public boolean isCurrentUserInRole(final String role) {
		return getUserRoles().stream().filter(roleName -> roleName.equals(Objects.requireNonNull(role))).findAny().isPresent();
	}
	
	/**
	 * Gets the roles the currently signed-in user belongs to.
	 *
	 * @return a set of all roles the currently signed-in user belongs to.
	 */
	@Override
	public Set<String> getUserRoles() {
		SecurityContext context = SecurityContextHolder.getContext();
		Authentication authentication = context.getAuthentication();
		Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
		Set<String> roles = authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet());
		return roles;
	}
	
	@Override
	public boolean isLoggedIn() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if ((authentication != null) && UsernamePasswordAuthenticationToken.class.isInstance(authentication)) {
			return ((UsernamePasswordAuthenticationToken) authentication).isAuthenticated();
		}
		return false;
	}
	
	@Override
	public void login(final String username, final String password) {
		//		try {
		UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(username, password);
		Authentication token = this.authenticationManager.authenticate(authentication);
		// Reinitialize the session to protect against session fixation attacks. This does not work
		// with websocket communication.
		VaadinService.reinitializeSession(VaadinService.getCurrentRequest());
		SecurityContextHolder.getContext().setAuthentication(token);
		// Now when the session is reinitialized, we can enable websocket communication. Or we could have just
		// used WEBSOCKET_XHR and skipped this step completely.
		UI ui = UI.getCurrent();
		ui.getPushConfiguration().setTransport(Transport.LONG_POLLING);
		ui.getPushConfiguration().setPushMode(PushMode.AUTOMATIC);
		// Show the main UI
		reload();
		//		} catch (AuthenticationException ex) {
		//			getLogger().warn("login failed", ex);
		//		}
	}
	
	@Override
	public void logout() {
		SecurityContextHolder.clearContext();
		VaadinSession.getCurrent().close();
		reload();
	}
	
	@Override
	public void register(final User user) {
		this.userService.save(user);
		//		login(user.getUsername(), user.getPassword());
		//		reload();
	}
	
	private void reload() {
		UI ui = UI.getCurrent();
		if (ui != null) {
			ui.getPage().reload();
		}
	}
}
