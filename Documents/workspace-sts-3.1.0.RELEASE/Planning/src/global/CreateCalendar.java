package global;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import com.vaadin.addon.calendar.event.BasicEvent;
import com.vaadin.addon.calendar.event.BasicEventProvider;
import com.vaadin.addon.calendar.ui.Calendar;

public class CreateCalendar {
	
	public CreateCalendar( ResultSet rs, Calendar cal) throws SQLException, ParseException {
		
		cal.setFirstVisibleDayOfWeek(1);
		cal.setLastVisibleDayOfWeek(6);
		cal.setFirstVisibleHourOfDay(8);
		cal.setLastVisibleHourOfDay(20);
		cal.setWidth("1000px");
		cal.setHeight("500px");

		BasicEventProvider eventProvider = (BasicEventProvider) cal
				.getEventProvider();
		
		//get the date of the calendar of the promotion
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat hf = new SimpleDateFormat("HH:mm");
		while (rs.next()) {
			Date date_debut = sdf.parse(rs.getString("Date_debut"));
			Date date_fin = sdf.parse(rs.getString("Date_Fin"));
			String salle = rs.getString("salle");
			String type = rs.getString("type");
			String remarque = rs.getString("remarque");
			String idMatiere = rs.getString("idMatiere");
			String nom = rs.getString("Nom");
			String hoursBegin = hf.format(rs.getTimestamp("Date_debut"));
			String hoursEnd = hf.format(rs.getTimestamp("Date_Fin"));
			if (idMatiere == null) {
				idMatiere = "";
			}
			if (nom == null) {
				nom = "";
			}
			if (type == null) {
				type = "";
			}
			eventProvider.addEvent(new BasicEvent(idMatiere + " " + nom, "Salle : " + salle + "<br />Type de cours : " + type + "<br>Remarque : " + remarque +
					"<br>Heure début : " + hoursBegin + "<br>Heure fin : " + hoursEnd,		
					date_debut, date_fin));
		}
	}
}
