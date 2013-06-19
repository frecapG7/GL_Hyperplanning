package student;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import global.CreateCalendar;
import global.LoginInformation;
import global.MysqlConnection;
import com.vaadin.addon.calendar.event.BasicEvent;
import com.vaadin.addon.calendar.event.BasicEventProvider;
import com.vaadin.addon.calendar.event.CalendarEventEditor;
import com.vaadin.addon.calendar.ui.Calendar;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.event.FieldEvents.BlurEvent;
import com.vaadin.event.FieldEvents.BlurListener;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Select;
import com.vaadin.ui.VerticalLayout;

public class PlanningPromotion extends CustomComponent {
	private MysqlConnection con;
	public VerticalLayout vl = new VerticalLayout();
	// public Button quit = new Button("Quitter", this, "quit");
	int id = LoginInformation.identifiant;
	public Calendar cal;
	public Select promotion;

	public PlanningPromotion() throws Exception {
		setCompositionRoot(vl);
		// vl.setSizeFull();
		vl.setSpacing(true);
		vl.setMargin(true);

		promotion = new Select("Choix de parcours : ");
		getPromotion();
		vl.addComponent(promotion);
		promotion.addListener(new BlurListener() {
			public void blur(BlurEvent event) {
				if (cal != null) {
					vl.removeComponent(cal);
				}
				Object promotionId = promotion.getValue();
				if (promotionId != null) {
					cal = new Calendar();
					try {
						ResultSet rs = getSchedulePromotion(promotionId);
						new CreateCalendar(rs, cal);
						getCalendarPromotion(promotionId);
					} catch (Exception e) {
						e.printStackTrace();
					}
					cal.setHeight("450px");
					vl.addComponent(cal);
				}
			}
		});

	}

	private void getPromotion() throws Exception {
		con = new MysqlConnection();
		ResultSet rs = con.queryTable("SELECT id, nom FROM parcours");
		while (rs.next()) {
			String name = rs.getString("nom");
			int id = rs.getInt("id");
			promotion.addItem(id);
			promotion.setItemCaption(id, name);
		}
	}

	private ResultSet getSchedulePromotion(Object promotionId) throws Exception {
		con = new MysqlConnection();
		ResultSet rs = con
				.queryTable("SELECT m.ID as idMatiere,m.Nom AS Nom,c.Date_debut,c.Date_Fin,"
						+ " tc.type, s.Nom AS salle, c.remarque"
						+ " FROM cours c"
						+ " INNER JOIN type_cours tc ON tc.id = c.type"
						+ " INNER JOIN salles s ON s.ID = c.ID_salle"
						+ " LEFT JOIN matiere m ON m.ID = c.ID_matiere"
						+ " LEFT JOIN groupe_eleve g ON g.id_groupe = c.ID_groupe_cours"
						+ " LEFT JOIN eleve e ON e.id_eleve = g.ID_eleve"
						+ " WHERE c.actif = 1"
						+ " AND  e.id_parcours = "
						+ promotionId);
		return rs;
	}

	private void getCalendarPromotion(Object promotionId) throws Exception {
		con = new MysqlConnection();
		ResultSet rs = con
				.queryTable("SELECT v.date_debut, v.date_fin FROM eleve e"
						+ " INNER JOIN parcours p ON p.id =  e.id_parcours"
						+ " INNER JOIN vacances v ON v.id_calendar = p.id_calendrier"
						+ " WHERE e.id_parcours = " + promotionId);

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
			eventHoliday.addEvent(new BasicEvent("Vacances", "", date_debut,
					date_fin));
		}

	}

}
