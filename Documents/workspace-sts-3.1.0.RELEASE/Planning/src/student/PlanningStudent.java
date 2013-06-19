package student;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import global.CreateCalendar;
import global.LoginInformation;
import global.MysqlConnection;
import com.vaadin.addon.calendar.event.BasicEvent;
import com.vaadin.addon.calendar.event.BasicEventProvider;
import com.vaadin.addon.calendar.event.CalendarEventEditor;
import com.vaadin.addon.calendar.ui.Calendar;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.VerticalLayout;

public class PlanningStudent extends CustomComponent {
	private MysqlConnection con;
	public VerticalLayout vl = new VerticalLayout();
	//public Button quit = new Button("Quitter", this, "quit");
	int id = LoginInformation.identifiant;
	public Calendar cal;
	
	public PlanningStudent() {
		setCompositionRoot(vl);
		//vl.setSizeFull();
		vl.setSpacing(true);
		vl.setMargin(true);
		cal = new Calendar();
		try {
			ResultSet rs = getScheduleStudent();
			new CreateCalendar(rs, cal);
			getCalendarStudent();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		vl.addComponent(cal);
	}
	
	private ResultSet getScheduleStudent() throws Exception {
		con = new MysqlConnection();
		ResultSet rs = con
				.queryTable("SELECT matiere.ID as idMatiere,matiere.Nom AS Nom,cours.Date_debut,cours.Date_Fin,"
						+ " type_cours.type, salles.Nom AS salle, cours.remarque, p.nom AS nomProf"
						+ " FROM cours,matiere,groupe,groupe_eleve,eleve,type_cours,salles, personne p"
						+ " Where matiere.ID = cours.ID_matiere"
						+ " AND p.id_identifiant = cours.ID_professeur"
						+ " AND cours.ID_groupe_cours=groupe.ID AND "
						+ " groupe.ID=groupe_eleve.ID_groupe AND "
						+ " type_cours.id = cours.type AND salles.ID = cours.ID_salle"
						+ " AND groupe_eleve.ID_eleve=eleve.id_eleve AND eleve.id_eleve =" + id
						+ " AND actif = 1"
						+ " AND p.statut = 2");
		return rs;
	}
	
	private void getCalendarStudent() throws Exception {
		con = new MysqlConnection();
		ResultSet rs = con
				.queryTable("SELECT v.date_debut, v.date_fin FROM eleve e"
						+ " INNER JOIN parcours p ON p.id =  e.id_parcours"
						+ " INNER JOIN vacances v ON v.id_calendar = p.id_calendrier"
						+ " WHERE e.id_eleve = " + id);

		BasicEventProvider eventHoliday = (BasicEventProvider) cal
				.getEventProvider();
		
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		while (rs.next()) {
			Timestamp ts = rs.getTimestamp("date_debut");
			ts.setHours(8);
			Date date_debut = df.parse("" + ts);
			Timestamp ts_end = rs.getTimestamp("date_fin");
			ts_end.setHours(21);
			Date date_fin = df.parse("" + ts_end);
			eventHoliday.addEvent(new BasicEvent("Vacances", "", date_debut, date_fin));
		}

	}

}
