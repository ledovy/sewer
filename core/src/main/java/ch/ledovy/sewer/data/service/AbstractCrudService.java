package ch.ledovy.sewer.data.service;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import ch.ledovy.sewer.data.model.ParameterRepository;

public abstract class AbstractCrudService<T, P, R extends JpaRepository<T, Long>> implements CrudService<T>, ParameterRepository<T, P> {
	
	private final Class<T> entityType;
	protected final R repository;
	
	public AbstractCrudService(final Class<T> entityType, final R repository) {
		this.entityType = entityType;
		this.repository = repository;
	}
	
	@Override
	public List<T> findAll() {
		return this.repository.findAll();
	}
	
	@Override
	public long count() {
		return this.repository.count();
	}
	
	@Override
	public T create() {
		try {
			return this.entityType.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			throw new UnsupportedOperationException("Entity of type " + this.entityType.getName() + " is missing a public no-args constructor", e);
		}
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public T save(final T entity) {
		return this.repository.save(entity);
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public void delete(final long id) {
		this.repository.delete(id);
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public T load(final long id) {
		// T found = this.repository.findOne(id);
		T loaded = this.repository.getOne(id);
		return loaded;
	}
	
	protected String sanitizeWildcards(final String value) {
		String sanitized = value == null ? "" : value;
		sanitized = StringUtils.isEmpty(sanitized) ? "%" : "%" + sanitized + "%";
		return sanitized;
	}
	
}
