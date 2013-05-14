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
	public VerticalLayout vl = new VerticalLayout();
	
	@Override
	public void init() {
		buildMainLayout();
	}
	
	private void buildMainLayout() {
		setMainWindow(new Window("Planning"));
		//vl.setSizeFull();
		vl.setSpacing(true);
		vl.setMargin(true);
		vl.addComponent(createToolbar());
		
		
		/**Calendar cal = new Calendar();
		cal.setStartDate(new Date());
		cal.setEndDate(new Date());
		BasicEvent event = new BasicEvent();
		java.util.Calendar calendar =
		java.util.Calendar.getInstance();
		calendar.setTime(new Date());
		event.setStart(calendar.getTime());
		calendar.add(java.util.Calendar.HOUR, 3);
		event.setEnd(calendar.getTime());
		event.setCaption("FooBar");
		vl.addComponent(cal);

		BasicEventProvider eventProvider = (BasicEventProvider)
		cal.getEventProvider();
		eventProvider.addEvent(event);*/
		
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
