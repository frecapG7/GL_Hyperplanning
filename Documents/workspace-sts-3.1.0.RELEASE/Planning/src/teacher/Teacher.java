package teacher;

import global.MysqlConnection;
import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TabSheet.SelectedTabChangeEvent;
import com.vaadin.ui.VerticalLayout;

public class Teacher extends CustomComponent implements TabSheet.SelectedTabChangeListener {
	private Button quit = new Button("Quitter", this, "quit");
	public VerticalLayout vl = new VerticalLayout();
	private MysqlConnection con;
	public Panel planning;
	public Panel room;
	public static Panel admin;
	

	public Teacher() {
		setCompositionRoot(vl);
		Panel panel = new Panel("Planning");
		vl.setSpacing(true);
		vl.setMargin(true);
		vl.addComponent(panel);
		panel.addComponent(createToolbar());
		
	}
	
	public void quit() {
		getApplication().close();
	}

	public void selectedTabChange(SelectedTabChangeEvent event) {
		// TODO Auto-generated method stub
		
	}
	
	public HorizontalLayout createToolbar() {
		HorizontalLayout hl = new HorizontalLayout();
		TabSheet tabsheet = new TabSheet();
		tabsheet.addListener(this);
		admin = admin();
		room = room();
		planning = planning();
        // This will cause a selectedTabChange() call.
		tabsheet.addTab(planning, "Consulter planning", null);
		tabsheet.addTab(room, "Reserver salle", null);
        tabsheet.addTab(admin, "Administration", null);
		hl.addComponent(tabsheet);
		quit.addStyleName("quitButton");
		hl.addComponent(quit);
		return hl;
	}
	
	public Panel admin() {
		Panel panel = new Panel();
		panel.setHeight("550px");
		panel.setWidth("1200px");
		try {
			panel.setContent(new ChangeProfesseur());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return panel;
	}
	
	public Panel planning() {
		Panel panel = new Panel();
		panel.setHeight("550px");
		panel.setWidth("1200px");
		try {
			panel.setContent(new PlanningTeacher());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return panel;
	}
	
	public Panel room() {
		Panel panel = new Panel();
		panel.setHeight("550px");
		panel.setWidth("1200px");
		try {
			panel.setContent(new BookRoom());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return panel;
	}
	

}
