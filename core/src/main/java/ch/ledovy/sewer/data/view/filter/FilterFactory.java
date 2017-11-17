package ch.ledovy.sewer.data.view.filter;

import com.vaadin.data.ValidationException;

import ch.ledovy.sewer.data.view.form.Form;

public class FilterFactory {
	public static <I, P> FilterPresenter<I, P> createBackendFilter(final Form<P> searchBar, final FilterableRepositoryProvider<I, P> provider) {
		FilterPresenter<I, P> filter = new FilterPresenter<I, P>() {
			
			@Override
			public void filter() {
				P value;
				try {
					value = searchBar.getValue();
				} catch (ValidationException e) {
					value = searchBar.newValue();
				}
				provider.setBackendParameter(value);
				provider.refreshAll();
			}
			
			@Override
			public void clear() {
				searchBar.reset();
				filter();
			}
			
		};
		filter.clear();
		return filter;
	}
	
	public static <I, P extends SearchParameter<I>> FilterPresenter<I, P> createInMemoryFilter(final Form<P> searchBar, final FilterableRepositoryProvider<I, P> provider) {
		FilterPresenter<I, P> filter = new FilterPresenter<I, P>() {
			
			@Override
			public void filter() {
				P parameter;
				try {
					parameter = searchBar.getValue();
				} catch (ValidationException e) {
					parameter = searchBar.newValue();
				}
				final P p = parameter;
				provider.setInMemoryParameter(item -> {
					return p.matches(item);
				});
				provider.refreshAll();
			}
			
			@Override
			public void clear() {
				searchBar.reset();
				filter();
			}
			
		};
		filter.clear();
		return filter;
	}
	
}
