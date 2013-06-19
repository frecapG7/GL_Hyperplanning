package student;

import java.sql.ResultSet;
import global.CreateCalendar;
import global.LoginInformation;
import global.MysqlConnection;
import com.vaadin.addon.calendar.ui.Calendar;
import com.vaadin.event.FieldEvents.BlurEvent;
import com.vaadin.event.FieldEvents.BlurListener;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Select;
import com.vaadin.ui.VerticalLayout;

public class PlanningRoom extends CustomComponent {
	private MysqlConnection con;
	public VerticalLayout vl = new VerticalLayout();
	// public Button quit = new Button("Quitter", this, "quit");
	int id = LoginInformation.identifiant;
	public Calendar cal;
	public Select salle;

	public PlanningRoom() throws Exception {
		setCompositionRoot(vl);
		// vl.setSizeFull();
		vl.setSpacing(true);
		vl.setMargin(true);

		salle = new Select("Choix de le salle: ");
		getRoom();
		vl.addComponent(salle);
		salle.addListener(new BlurListener() {
			public void blur(BlurEvent event) {
				if (cal != null) {
					vl.removeComponent(cal);
				}
				Object salleId = salle.getValue();
				if (salleId != null) {
					cal = new Calendar();
					try {
						ResultSet rs = getScheduleRoom(salleId);
						new CreateCalendar(rs, cal);
					} catch (Exception e) {
						e.printStackTrace();
					}
					cal.setHeight("450px");
					vl.addComponent(cal);
				}
			}
		});

	}

	private void getRoom() throws Exception {
		con = new MysqlConnection();
		ResultSet rs = con.queryTable("SELECT ID, Nom FROM salles");
		while (rs.next()) {
			String name = rs.getString("Nom");
			int id = rs.getInt("ID");
			salle.addItem(id);
			salle.setItemCaption(id, name);
		}
	}

	private ResultSet getScheduleRoom(Object salleId) throws Exception {
		con = new MysqlConnection();
		ResultSet rs = con
				.queryTable("SELECT m.ID as idMatiere,m.Nom AS Nom,c.Date_debut,c.Date_Fin,"
						+ " tc.type, s.Nom AS salle, c.remarque"
						+ " FROM cours c"
						+ " INNER JOIN type_cours tc ON tc.id = c.type"
						+ " INNER JOIN salles s ON s.ID = c.ID_salle"
						+ " LEFT JOIN matiere m ON m.ID = c.ID_matiere"
						+ " WHERE c.actif = 1"
						+ " AND  s.ID = " + salleId);
		return rs;

	}

}
