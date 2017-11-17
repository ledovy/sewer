package ch.ledovy.sewer.security.view.user;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.PreDestroy;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.ViewScope;
import com.vaadin.ui.Grid;

import ch.ledovy.sewer.data.view.filter.FilterableRepositoryProvider;
import ch.ledovy.sewer.i18n.Messages;
import ch.ledovy.sewer.security.model.Role;
import ch.ledovy.sewer.security.model.User;

@SpringComponent
@ViewScope
public class UserAdminGrid extends Grid<User> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Messages messages;
	private FilterableRepositoryProvider<User, UserParameter> provider;
	
	@Autowired
	public UserAdminGrid(final UserCrudService service, final Messages messages) {
		super(User.class);
		this.messages = messages;
		
		this.provider = new FilterableRepositoryProvider<>(service);
		setDataProvider(this.provider);
		
		// The columns were configured automatically based on the bean.
		// Remove all and configure custom grid
		removeAllColumns();
		this.messages.registerColumn("view.security.user.caption.username", addColumn(User::getUsername), this);
		Column<User, Set<Role>> roleColumn = addColumn(User::getRoles, roles -> {
			List<String> roleNames = roles.stream().map(r -> r.getName()).collect(Collectors.toList());
			return StringUtils.join(roleNames, ",");
		});
		this.messages.registerColumn("view.security.user.caption.roles", roleColumn, this);
	}
	
	@PreDestroy
	public void shutdown() {
		this.messages.unregister(this);
	}
	
	@Override
	public FilterableRepositoryProvider<User, UserParameter> getDataProvider() {
		return this.provider;
	}
	
}
