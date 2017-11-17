package ch.ledovy.sewer.action;

public interface Menu {
	
	Executor addItem(String caption);
	
	Menu addMenu(String caption);
	
}
