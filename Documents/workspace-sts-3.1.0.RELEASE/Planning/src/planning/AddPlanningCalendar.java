package planning;

import com.vaadin.addon.calendar.ui.Calendar;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.VerticalLayout;

public class AddPlanningCalendar extends CustomComponent{
	public VerticalLayout vl = new VerticalLayout();
	public AddPlanningCalendar(String nom,String type) throws Exception {
		setCompositionRoot(vl);
		Calendar cal = new Calendar();
		cal.setFirstVisibleDayOfWeek(2);
		cal.setLastVisibleDayOfWeek(6);
		cal.setFirstVisibleHourOfDay(8);
		cal.setLastVisibleHourOfDay(20);
		cal.setHeight("600px");
		if(type=="eleve") {
			
		}
		
		if(type=="parcour") {
			
		}
		
		if(type=="groupe") {
			
		}
		vl.addComponent(cal);
	}
}
