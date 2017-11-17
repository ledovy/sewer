package ch.ledovy.sewer.security.view.role;

import java.util.Set;

import javax.annotation.PreDestroy;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.ViewScope;
import com.vaadin.ui.Grid;

import ch.ledovy.sewer.data.view.filter.FilterableRepositoryProvider;
import ch.ledovy.sewer.i18n.Messages;
import ch.ledovy.sewer.security.model.Role;

@SpringComponent
@ViewScope
public class RoleAdminGrid extends Grid<Role> {
	private Messages											messages;
	private FilterableRepositoryProvider<Role, RoleParameter>	provider;
	
	@Autowired
	public RoleAdminGrid(Messages messages, RoleCrudService service) {
		super(Role.class);
		this.provider = new FilterableRepositoryProvider<>(service);
		setDataProvider(this.provider);
		
		this.messages = messages;
		// The columns were configured automatically based on the bean.
		// Remove all and configure custom grid
		removeAllColumns();
		this.messages.registerColumn("view.security.role.name.caption", addColumn(Role::getName), this);
		Column<Role, Set<String>> permissionsColumn = addColumn(Role::getPermissions, p -> {
			return StringUtils.join(p, ", ");
		});
		this.messages.registerColumn("view.security.role.permissions.caption", permissionsColumn, this);
	}
	
	@PreDestroy
	public void shutdown() {
		this.messages.unregister(this);
	}
	
	@Override
	public FilterableRepositoryProvider<Role, RoleParameter> getDataProvider() {
		return this.provider;
	}
}
