package admin;

import global.MysqlConnection;
import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TabSheet.SelectedTabChangeEvent;
import com.vaadin.ui.VerticalLayout;

public class Administration extends CustomComponent implements TabSheet.SelectedTabChangeListener{
	
	private Button quit = new Button("Quitter", this, "quit");
	public VerticalLayout vl = new VerticalLayout();
	private MysqlConnection con;
	public Panel planning;
	public Panel admin;
	

	public Administration() {
		setCompositionRoot(vl);
		Panel panel = new Panel("Planning");
		vl.addComponent(panel);
		panel.addComponent(createToolbar());
		
	}
	
	public void quit() {
		getApplication().close();
	}
	
	public HorizontalLayout createToolbar() {
		HorizontalLayout hl = new HorizontalLayout();
		TabSheet tabsheet = new TabSheet();
		tabsheet.addListener(this);
		admin = admin();
		planning = planning();
        // This will cause a selectedTabChange() call.
		tabsheet.addTab(planning, "Ajout planning", null);
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
		panel.setContent(new AdminTabsheet());
		return panel;
	}
	
	public Panel planning() {
		Panel panel = new Panel();
		panel.setHeight("550px");
		panel.setWidth("1200px");
		panel.setContent(new AddPlanning());
		return panel;
	}

	public void selectedTabChange(SelectedTabChangeEvent event) {
		// TODO Auto-generated method stub
		
	}

}
