package ch.ledovy.sewer.data.view.filter;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import com.vaadin.data.provider.AbstractBackEndDataProvider;
import com.vaadin.data.provider.Query;
import com.vaadin.server.SerializablePredicate;

import ch.ledovy.sewer.data.model.ParameterRepository;

public class FilterableRepositoryProvider<I, P> extends AbstractBackEndDataProvider<I, P> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final ParameterRepository<I, P> repo;
	private Optional<P> backendParameter = Optional.empty();
	private Optional<SerializablePredicate<I>> memoryParameter = Optional.empty();
	
	public FilterableRepositoryProvider(final ParameterRepository<I, P> repo) {
		this.repo = repo;
	}
	
	@Override
	protected Stream<I> fetchFromBackEnd(final Query<I, P> query) {
		List<I> list;
		if (this.backendParameter.isPresent()) {
			list = this.repo.applyFindByParameter(this.backendParameter.get());
		} else {
			list = this.repo.findAll();
		}
		Stream<I> stream = list.stream();
		if (this.memoryParameter.isPresent()) {
			return stream.filter(this.memoryParameter.get());
		} else {
			return stream;
		}
	}
	
	@Override
	protected int sizeInBackEnd(final Query<I, P> query) {
		return (int) fetchFromBackEnd(query).count();
		//		if (backendParameter.isPresent()) {
		//			return repo.applyCountByParameter(backendParameter.get());
		//		} else {
		//			return (int) repo.count();
		//		}
	}
	
	public void setBackendParameter(final P value) {
		this.backendParameter = Optional.ofNullable(value);
	}
	
	public void setInMemoryParameter(final SerializablePredicate<I> value) {
		this.memoryParameter = Optional.ofNullable(value);
	}
}