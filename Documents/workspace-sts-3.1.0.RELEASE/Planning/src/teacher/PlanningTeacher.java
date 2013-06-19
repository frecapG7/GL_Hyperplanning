package teacher;

import java.sql.ResultSet;
import global.CreateCalendar;
import global.LoginInformation;
import global.MysqlConnection;
import com.vaadin.addon.calendar.ui.Calendar;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.VerticalLayout;

public class PlanningTeacher extends CustomComponent {
	private MysqlConnection con;
	public VerticalLayout vl = new VerticalLayout();
	//public Button quit = new Button("Quitter", this, "quit");
	int id = LoginInformation.identifiant;
	public Calendar cal;
	
	public PlanningTeacher() {
		setCompositionRoot(vl);
		//vl.setSizeFull();
		vl.setSpacing(true);
		vl.setMargin(true);
		
		cal = new Calendar();
		try {
			ResultSet rs = getScheduleTeacher();
			new CreateCalendar(rs, cal);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		vl.addComponent(cal);
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

}
