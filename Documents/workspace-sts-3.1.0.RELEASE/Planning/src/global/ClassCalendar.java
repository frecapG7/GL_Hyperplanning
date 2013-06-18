package global;

import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;


import com.vaadin.addon.calendar.event.BasicEvent;
import com.vaadin.addon.calendar.event.BasicEventProvider;
import com.vaadin.addon.calendar.ui.Calendar;

import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

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

		while (rs.next()) {
			Date date_debut = sdf.parse(rs.getString("Date_debut"));
			Date date_fin = sdf.parse(rs.getString("Date_Fin"));
			String salle = rs.getString("salle");
			String type = rs.getString("type");
			String remarque = rs.getString("remarque");
			String idMatiere = rs.getString("idMatiere");
			String nom = rs.getString("Nom");
			eventProvider.addEvent(new BasicEvent(idMatiere + " " + nom, "Salle : " + salle + "<br />Type de cours : " + type + "<br>Remarque : " + remarque,
					date_debut, date_fin));
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
						+ " AND groupe_eleve.ID_eleve=eleve.id_eleve AND eleve.id_eleve ="
						+ id);
		return rs;
	}
	
	private ResultSet getScheduleTeacher() throws Exception {
		con = new MysqlConnection();
		ResultSet rs = con
				.queryTable("SELECT m.ID as idMatiere,m.Nom AS Nom, c.Date_debut, c.Date_Fin, tc.type, s.Nom AS salle, c.remarque FROM cours c"
						+ " INNER JOIN matiere m ON m.ID = c.ID_matiere"
						+ " INNER JOIN type_cours tc ON tc.id = c.type"
						+ " INNER JOIN salles s ON s.ID = c.ID_salle"
						+ " WHERE ID_professeur = " + id);
		return rs;
	}
	
	private void getCalendarStudent() throws Exception {
		con = new MysqlConnection();
		ResultSet rs = con
				.queryTable("SELECT c.Date_Debut, c.Date_Fin, c.id FROM eleve e"
						+ " INNER JOIN parcours p ON p.id =  e.id_parcours"
						+ " INNER JOIN calendar c ON c.ID = p.id_calendrier"
						+ " WHERE e.id_eleve = " + id);
		

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		while (rs.next()) {
			Date date_debut = rs.getDate("Date_Debut");
			Date date_fin = rs.getDate("Date_Fin");
			//Date date_fin = rs.getDate("Date_Fin");
			//Label l1 = new Label(""+date_debut);
			//vl.addComponent(l1);
			//cal.setStartDate(date_debut);
			//cal.setEndDate(date_fin);
		}

	}
	
	private ResultSet getCalendarTeacher() throws Exception {
		con = new MysqlConnection();
		ResultSet rs = con
				.queryTable("SELECT c.Date_debut, c.Date_Fin, c.id FROM professeur p"
						+ " INNER JOIN cours co ON co.ID_professeur = p.id_prof"
						+ " INNER JOIN groupe_eleve g ON g.ID_groupe = co.ID_groupe_cours"
						+ " INNER JOIN eleve e ON e.id_eleve = g.ID_eleve"
						+ " INNER JOIN parcours pa ON pa.id =  e.id_parcours"
						+ " INNER JOIN calendar c ON c.ID = pa.id_calendrier"
						+ " WHERE e.id_eleve = " + id);
		return rs;
	}

	public void quit() {
		getApplication().close();
	}
}
