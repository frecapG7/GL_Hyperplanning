package teacher;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;

import com.vaadin.addon.calendar.event.BasicEvent;
import com.vaadin.addon.calendar.ui.Calendar;
import com.vaadin.addon.calendar.ui.CalendarComponentEvents;
import com.vaadin.addon.calendar.ui.CalendarComponentEvents.EventClick;
import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Select;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;

import global.ClassCalendar;
import global.LoginInformation;
import global.MysqlConnection;

public class ChangeProfesseur extends ClassCalendar {
	VerticalLayout vl = new VerticalLayout();
	private Select newProfesseur = new Select("Nouveau professeur");
	private Button valid;
	private Window windowChangeTeacher;

	public ChangeProfesseur() throws Exception {
		
		cal.setHandler(new CalendarComponentEvents.EventClickHandler() {
			public void eventClick(EventClick event) {
				VerticalLayout layout = new VerticalLayout();
				layout.setSpacing(true);
				layout.setMargin(true);
				layout.addComponent(newProfesseur);
				
				BasicEvent e = (BasicEvent) event.getCalendarEvent();
				String idMatiere = e.getCaption().split(" ", 2)[0];
				getTeacher(idMatiere);
				
				windowChangeTeacher = new Window("Changer de professeur :");
				windowChangeTeacher.center();
				windowChangeTeacher.setWidth("250px");
				windowChangeTeacher.setHeight("180px");
				windowChangeTeacher.setContent(layout);
				getApplication().getMainWindow().addWindow(windowChangeTeacher);
				
				final java.sql.Timestamp sqlDate_begin = new java.sql.Timestamp(e.getStart().getTime());
				valid = new Button("Valider");
				valid.addListener(new Button.ClickListener() {
					public void buttonClick(ClickEvent event) {
						validate(sqlDate_begin);
					}
				});
				layout.addComponent(valid);
			}
			});
	}
	
	private void getTeacher(Object idMatiere) {
		try {
			int idIdentifiant = LoginInformation.identifiant;
			MysqlConnection con = new MysqlConnection();
			ResultSet rs = con.queryTable("SELECT p.nom, p.prenom, p.id_identifiant"
					+ " FROM personne p"
					+ " INNER JOIN matiere_prof m ON m.ID_prof = p.id_identifiant"
					+ " WHERE p.statut = 2"
					+ " AND m.ID_matiere = '" + idMatiere + "'"
					+ " AND p.id_identifiant != " + idIdentifiant);
			while(rs.next()) {
				String nameTeacher = rs.getString("nom");
				String firstNameTeacher = rs.getString("prenom");
				int idTeacher = rs.getInt("id_identifiant");
				newProfesseur.addItem(idTeacher);
				newProfesseur.setItemCaption(idTeacher, nameTeacher + " " + firstNameTeacher);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void validate(java.sql.Timestamp sqlDate_begin) {
		try {
			Object idTeacher = newProfesseur.getValue();
			int idIdentifiant = LoginInformation.identifiant;
			MysqlConnection con = new MysqlConnection();
			con.executeTable("UPDATE cours"
					+ " SET ID_professeur = " + idTeacher
					+ " WHERE Date_Debut = '" + sqlDate_begin + "'"
					+ " AND ID_professeur = " + idIdentifiant);
			getApplication().getMainWindow().removeWindow(windowChangeTeacher);
			Teacher.admin.setContent(new ChangeProfesseur());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
