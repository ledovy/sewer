package ch.ledovy.sewer.security.view.user;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ch.ledovy.sewer.data.service.AbstractCrudService;
import ch.ledovy.sewer.security.model.User;
import ch.ledovy.sewer.security.model.UserRepository;

@Service
public class UserCrudService extends AbstractCrudService<User, UserParameter, UserRepository> {
	@PersistenceContext
	private EntityManager em;
	
	@Autowired
	public UserCrudService(final UserRepository repository) {
		super(User.class, repository);
	}
	
	@Override
	public List<User> findAll() {
		return repository.findAll();
	}
	
	@Override
	public long count() {
		return repository.count();
	}
	
	@Override
	public List<User> applyFindByParameter(final UserParameter parameter) {
		UserParameter p = sanitizeParameter(parameter);
		List<User> users = repository.findByUsernameLikeIgnoreCase(p.getName());
		for (User user : users) {
			user.getRoles();
		}
		return users;
	}
	
	@Override
	public int applyCountByParameter(final UserParameter parameter) {
		UserParameter p = sanitizeParameter(parameter);
		return repository.countByUsernameLikeIgnoreCase(p.getName());
	}
	
	private UserParameter sanitizeParameter(final UserParameter p) {
		UserParameter parameter = p == null ? new UserParameter() : p;
		String namePart = parameter.getName() == null ? "" : parameter.getName();
		namePart = StringUtils.isEmpty(namePart) ? "%" : "%" + namePart + "%";
		return new UserParameter(namePart);
	}
}
