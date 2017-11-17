package ch.ledovy.sewer.security.view.role;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import ch.ledovy.sewer.data.service.AbstractCrudService;
import ch.ledovy.sewer.security.model.Role;
import ch.ledovy.sewer.security.model.RoleRepository;

@Service
@Transactional(propagation = Propagation.REQUIRED)
public class RoleCrudService extends AbstractCrudService<Role, RoleParameter, RoleRepository> {
	@PersistenceContext
	private EntityManager em;
	
	@Autowired
	public RoleCrudService(final RoleRepository repository) {
		super(Role.class, repository);
	}
	
	@Override
	public List<Role> applyFindByParameter(final RoleParameter parameter) {
		RoleParameter p = sanitizeParameter(parameter);
		return repository.findByNameLikeIgnoreCase(p.getName());
	}
	
	@Override
	public int applyCountByParameter(final RoleParameter parameter) {
		RoleParameter p = sanitizeParameter(parameter);
		return repository.countByNameLikeIgnoreCase(p.getName());
	}
	
	private RoleParameter sanitizeParameter(final RoleParameter p) {
		RoleParameter parameter = p == null ? new RoleParameter() : p;
		String namePart = parameter.getName() == null ? "" : parameter.getName();
		namePart = StringUtils.isEmpty(namePart) ? "%" : "%" + namePart + "%";
		return new RoleParameter(namePart);
	}
	
	@Override
	public Role load(final long id) {
		Role found = repository.findOne(id);
		return found;
	}
}
