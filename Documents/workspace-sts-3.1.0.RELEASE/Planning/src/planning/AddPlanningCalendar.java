package planning;

import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.vaadin.addon.calendar.event.BasicEvent;
import com.vaadin.addon.calendar.event.BasicEventProvider;
import com.vaadin.addon.calendar.ui.Calendar;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.VerticalLayout;

public class AddPlanningCalendar extends CustomComponent{
	public VerticalLayout vl = new VerticalLayout();
	private MysqlConnection con;
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
			BasicEventProvider eventProvider = (BasicEventProvider) cal
					.getEventProvider();
			con = new MysqlConnection();
			ResultSet rs = con.queryTable("select cours.Nom,cours.Date_debut,cours.Date_Fin FROM cours,groupe " +
					"Where cours.ID_groupe_cours=groupe.ID AND groupe.Nom='"
					+nom+"'");
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

			while (rs.next()) {
				Date date_debut = sdf.parse(rs.getString("Date_debut"));
				Date date_fin = sdf.parse(rs.getString("Date_Fin"));
				eventProvider.addEvent(new BasicEvent(rs.getString("Nom"), "",
						date_debut, date_fin));				
			}
		}
		vl.addComponent(cal);
	}
}
