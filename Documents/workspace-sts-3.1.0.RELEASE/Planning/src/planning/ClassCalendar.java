package planning;

import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.vaadin.addon.calendar.event.BasicEvent;
import com.vaadin.addon.calendar.event.BasicEventProvider;
import com.vaadin.addon.calendar.ui.Calendar;

public class ClassCalendar {
	private MysqlConnection con;
	
	public Calendar getCalendarClass(String nom) throws Exception{
		Calendar cal = new Calendar();		
		cal.setFirstVisibleDayOfWeek(2);
		cal.setLastVisibleDayOfWeek(6);
		cal.setFirstVisibleHourOfDay(8);
		cal.setLastVisibleHourOfDay(19);
		
		BasicEventProvider eventProvider = (BasicEventProvider)				
				cal.getEventProvider();				
		con = new MysqlConnection();
		
		ResultSet rs = con.queryTable("SELECT cours.Nom,cours.Date_debut,cours.Date_Fin FROM cours,groupe,groupe_eleve,eleve " +
				"Where cours.ID_groupe_cours=groupe.ID AND " +
				"groupe.ID=groupe_eleve.ID_groupe AND " +
				"groupe_eleve.ID_eleve=eleve.ID AND eleve.nom='"+nom+"'");
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");				
		
		while (rs.next()) {	
			Date date_debut=sdf.parse(rs.getString("Date_debut"));
			Date date_fin=sdf.parse(rs.getString("Date_Fin"));
			BasicEvent event=new BasicEvent(rs.getString("Nom"),"",date_debut,date_fin);
			event.setStyleName("mycolor");
			eventProvider.addEvent(event);
			/*System.out.println(rs.getString("Date_debut"));*/
		}
		
		
		return cal;
	}
}
