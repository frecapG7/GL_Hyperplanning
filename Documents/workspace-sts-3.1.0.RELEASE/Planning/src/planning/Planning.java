package planning;


import java.util.Date;

import com.vaadin.Application;
import com.vaadin.addon.calendar.event.BasicEvent;
import com.vaadin.addon.calendar.event.BasicEventProvider;
import com.vaadin.addon.calendar.ui.Calendar;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.SplitPanel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class Planning extends Application {
	private Button quit = new Button("Quitter", this, "quit");
	private String nom="Simon";
	public VerticalLayout vl = new VerticalLayout();
	
	@Override
	public void init() {
		
		try {
			buildMainLayout();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void buildMainLayout() throws Exception {
		setMainWindow(new Window("Planning"));
		//vl.setSizeFull();
		vl.setSpacing(true);
		vl.setMargin(true);
		vl.addComponent(createToolbar());
		
		ClassCalendar calendarclass=new ClassCalendar();
		Calendar cal = calendarclass.getCalendarClass(nom);				
		vl.addComponent(cal);
		
		vl.addComponent(new CalendarForm());
		getMainWindow().setContent(vl);
	}

	
	public HorizontalLayout createToolbar() {
		HorizontalLayout hl = new HorizontalLayout();
		hl.addComponent(quit);
		return hl;
	}
	
	public void quit(){
		close();
	}
}
