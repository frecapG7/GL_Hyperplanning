package student;

import global.MysqlConnection;
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
		vl.setSizeFull();
		vl.setSpacing(true);
		vl.setMargin(true);
		Panel panel = new Panel("Planning");
		vl.addComponent(quit);
		vl.addComponent(panel);
		panel.addComponent(createToolbar());		
	}
	
	public HorizontalLayout createToolbar() {
		HorizontalLayout hl = new HorizontalLayout();
		
		hl.addComponent(quit);
		
		TabSheet tabsheet = new TabSheet();
		
		tabsheet.addListener(this);
		
		myCalendar = myCalendar();
		calendarPromotion = calendarPromotion();
		calendarRoom = calendarRoom();
      
        // This will cause a selectedTabChange() call.
		
		tabsheet.addTab(myCalendar, "Mon planning", null);
		tabsheet.addTab(calendarPromotion, "Planning promotion", null);
        tabsheet.addTab(calendarRoom, "Planning salle", null);
        
		hl.addComponent(tabsheet);
		return hl;
	}
	
	private Panel calendarRoom() {
		Panel panel = new Panel();
		panel.setHeight("550px");
		panel.setWidth("1150px");
		try {
			//panel.setContent();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return panel;
	}

	private Panel calendarPromotion() {
		Panel panel = new Panel();
		panel.setHeight("550px");
		panel.setWidth("1150px");
		try {
			//panel.setContent();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return panel;
	}

	private Panel myCalendar() {
		Panel panel = new Panel();
		panel.setHeight("550px");
		panel.setWidth("1150px");
		try {
			panel.setContent(new PlanningStudent());
		} catch (Exception e) {
			// TODO Auto-generated catch block
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
