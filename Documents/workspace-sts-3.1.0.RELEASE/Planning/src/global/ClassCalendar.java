package global;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;


import com.vaadin.addon.calendar.event.BasicEvent;
import com.vaadin.addon.calendar.event.BasicEventProvider;
import com.vaadin.addon.calendar.ui.Calendar;

import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;

public class ClassCalendar extends CustomComponent {
	private MysqlConnection con;
	public VerticalLayout vl = new VerticalLayout();
	public Button quit = new Button("Quitter", this, "quit");
	int id = LoginInformation.identifiant;
	int statut = LoginInformation.statut;
	public Calendar cal = new Calendar();

	public ClassCalendar() throws Exception {
		setCompositionRoot(vl);
		//vl.setSizeFull();
		vl.setSpacing(true);
		vl.setMargin(true);
		//vl.addComponent(quit);

		cal.setFirstVisibleDayOfWeek(1);
		cal.setLastVisibleDayOfWeek(6);
		cal.setFirstVisibleHourOfDay(8);
		cal.setLastVisibleHourOfDay(20);
		cal.setHeight("600px");

		BasicEventProvider eventProvider = (BasicEventProvider) cal
				.getEventProvider();
		
		//get the date of the calendar of the promotion
		if (statut == 1) {
			getCalendarStudent();
		}
		
		ResultSet rs;
		
		if (statut == 1) {
			cal.setWidth("1200px");
			rs = getScheduleStudent();
			//erreur setStartDate();
		} else {
			cal.setWidth("1000px");
			cal.setHeight("500px");
			rs = getScheduleTeacher();
		}
		
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
			eventProvider.addEvent(new BasicEvent(idMatiere + " " + nom, "" + date_debut,
					date_debut, date_fin));
			/*
			 * "Salle : " + salle + "<br />Type de cours : " + type + "<br>Remarque : " + remarque +
					"<br>Heure début : " + hoursBegin + "<br>Heure fin : " + hoursEnd
			*/
			// System.out.println(rs.getString("Date_debut"));
		}

		vl.addComponent(cal);
	}
	
	private ResultSet getScheduleStudent() throws Exception {
		con = new MysqlConnection();
		ResultSet rs = con
				.queryTable("SELECT matiere.ID as idMatiere,matiere.Nom AS Nom,cours.Date_debut,cours.Date_Fin, type_cours.type, salles.Nom AS salle, cours.remarque"
						+ " FROM cours,matiere,groupe,groupe_eleve,eleve,type_cours,salles"
						+ " Where matiere.ID = cours.ID_matiere"
						+ " AND cours.ID_groupe_cours=groupe.ID AND "
						+ " groupe.ID=groupe_eleve.ID_groupe AND "
						+ " type_cours.id = cours.type AND salles.ID = cours.ID_salle"
						+ " AND groupe_eleve.ID_eleve=eleve.id_eleve AND eleve.id_eleve =" + id
						+ " AND actif = 1");
		return rs;
	}
	
	private ResultSet getScheduleTeacher() throws Exception {
		con = new MysqlConnection();
		ResultSet rs = con
				.queryTable("SELECT m.ID as idMatiere,m.Nom AS Nom, c.Date_debut, c.Date_Fin, tc.type, s.Nom AS salle, c.remarque FROM cours c"
						+ " LEFT JOIN matiere m ON m.ID = c.ID_matiere"
						+ " LEFT JOIN type_cours tc ON tc.id = c.type"
						+ " LEFT JOIN salles s ON s.ID = c.ID_salle"
						+ " WHERE ID_professeur = " + id
						+ " AND actif = 1");
		return rs;
	}
	
	private void getCalendarStudent() throws Exception {
		con = new MysqlConnection();
		ResultSet rs = con
				.queryTable("SELECT v.date_debut, v.date_fin FROM eleve e"
						+ " INNER JOIN parcours p ON p.id =  e.id_parcours"
						+ " INNER JOIN vacances v ON v.id_calendar = p.id_calendrier"
						+ " WHERE e.id_eleve = " + id);

		BasicEventProvider eventProvider = (BasicEventProvider) cal
				.getEventProvider();
		
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		while (rs.next()) {
			Timestamp ts = rs.getTimestamp("date_debut");
			ts.setHours(8);
			Date date_debut = df.parse("" + ts);
			Timestamp ts_end = rs.getTimestamp("date_fin");
			ts_end.setHours(21);
			Date date_fin = df.parse("" + ts_end);
			Button test = new Button("test");
			/*test.addListener(new Button.ClickListener() {
				public void buttonClick(ClickEvent event) {
					getApplication().getMainWindow().showNotification("" + date_fin);
				}
			});*/
			//vl.addComponent(test);
			eventProvider.addEvent(new BasicEvent("Vacances", "", date_debut, date_fin));
		}


	}

	public void quit() {
		getApplication().close();
	}
}
