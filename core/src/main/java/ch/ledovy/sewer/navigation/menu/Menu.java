package ch.ledovy.sewer.navigation.menu;

import java.util.List;

public interface Menu {
	
	List<String> getMenus();
	
	List<MenuEntry> getMenuEntries(String menu);
	
}
