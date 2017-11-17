package ch.ledovy.sewer.data.view;

public interface HasValue<T> extends ValueConsumer<T>, ValueProvider<T> {

	T newValue();

	default void reset() {
		setValue(newValue());
	}
}
