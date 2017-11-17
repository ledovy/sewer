package ch.ledovy.sewer.data.view.filter;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import com.vaadin.data.provider.AbstractBackEndDataProvider;
import com.vaadin.data.provider.Query;
import com.vaadin.server.SerializablePredicate;

import ch.ledovy.sewer.data.model.ParameterRepository;

public class FilterableRepositoryProvider<I, P> extends AbstractBackEndDataProvider<I, P> {
	private final ParameterRepository<I, P> repo;
	private Optional<P> backendParameter = Optional.empty();
	private Optional<SerializablePredicate<I>> memoryParameter = Optional.empty();

	public FilterableRepositoryProvider(ParameterRepository<I, P> repo) {
		this.repo = repo;
	}

	@Override
	protected Stream<I> fetchFromBackEnd(Query<I, P> query) {
		List<I> list;
		if (backendParameter.isPresent()) {
			list = repo.applyFindByParameter(backendParameter.get());
		} else {
			list = repo.findAll();
		}
		Stream<I> stream = list.stream();
		if (memoryParameter.isPresent()) {
			return stream.filter(memoryParameter.get());
		} else {
			return stream;
		}
	}

	@Override
	protected int sizeInBackEnd(Query<I, P> query) {
		return (int) fetchFromBackEnd(query).count();
		//		if (backendParameter.isPresent()) {
		//			return repo.applyCountByParameter(backendParameter.get());
		//		} else {
		//			return (int) repo.count();
		//		}
	}

	public void setBackendParameter(P value) {
		this.backendParameter = Optional.ofNullable(value);
	}

	public void setInMemoryParameter(SerializablePredicate<I> value) {
		memoryParameter = Optional.ofNullable(value);
	}
}