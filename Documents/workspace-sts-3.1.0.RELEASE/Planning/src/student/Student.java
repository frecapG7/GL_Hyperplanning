package student;

import global.MysqlConnection;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TabSheet.SelectedTabChangeEvent;
import com.vaadin.ui.VerticalLayout;

public class Student extends CustomComponent implements TabSheet.SelectedTabChangeListener {	
	public Button quit = new Button("Quitter", this, "quit");
	public VerticalLayout vl = new VerticalLayout();
	private MysqlConnection con;
	public Panel myCalendar;
	public Panel calendarPromotion;
	public Panel calendarRoom;
	

	public Student() {
		setCompositionRoot(vl);
		vl.setMargin(true);
		vl.setSpacing(true);
		vl.setSizeFull();
		Panel panel = new Panel("Planning");
		vl.addComponent(panel);
		panel.addComponent(createToolbar());		
	}
	
	public HorizontalLayout createToolbar() {
		HorizontalLayout hl = new HorizontalLayout();
		TabSheet tabsheet = new TabSheet();
		tabsheet.addListener(this);
		myCalendar = myCalendar();
		calendarPromotion = calendarPromotion();
		calendarRoom = calendarRoom();
		tabsheet.addTab(myCalendar, "Mon planning", null);
		tabsheet.addTab(calendarPromotion, "Planning promotion", null);
        tabsheet.addTab(calendarRoom, "Planning salle", null); 
        hl.addComponent(tabsheet);
        quit.addStyleName("quitButton");
        hl.addComponent(quit);
		return hl;
	}
	
	private Panel calendarRoom() {
		Panel panel = new Panel();
		panel.setHeight("550px");
		panel.setWidth("1200px");
		try {
			panel.setContent(new PlanningRoom());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return panel;
	}

	private Panel calendarPromotion() {
		Panel panel = new Panel();
		panel.setHeight("550px");
		panel.setWidth("1200px");
		try {
			panel.setContent(new PlanningPromotion());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return panel;
	}

	private Panel myCalendar() {
		Panel panel = new Panel();
		panel.setHeight("550px");
		panel.setWidth("1200px");
		try {
			panel.setContent(new PlanningStudent());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return panel;
	}

	public void quit() {
		getApplication().close();
	}

	public void selectedTabChange(SelectedTabChangeEvent event) {
		// TODO Auto-generated method stub
		
	}

}
