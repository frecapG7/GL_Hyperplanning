package student;

import global.MysqlConnection;
import global.ClassCalendar;


import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TabSheet.SelectedTabChangeEvent;
import com.vaadin.ui.VerticalLayout;

public class Student extends CustomComponent {
	
	public Button quit = new Button("Quitter", this, "quit");
	public VerticalLayout vl = new VerticalLayout();
	private MysqlConnection con;
	

	public Student() {
		setCompositionRoot(vl);
		vl.setSizeFull();
		vl.setSpacing(true);
		vl.setMargin(true);
		Panel panel = new Panel("Planning");
		vl.addComponent(quit);
		vl.addComponent(panel);
		try {
			panel.setContent(new ClassCalendar());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void quit() {
		getApplication().close();
	}

}
