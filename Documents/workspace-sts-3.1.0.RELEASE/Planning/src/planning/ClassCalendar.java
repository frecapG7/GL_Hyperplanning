package planning;

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

	public ClassCalendar() throws Exception {
		int id = Login.identifiant;

		setCompositionRoot(vl);
		//vl.setSizeFull();
		vl.setSpacing(true);
		vl.setMargin(true);
		vl.addComponent(quit);

		Calendar cal = new Calendar();
		cal.setFirstVisibleDayOfWeek(1);
		cal.setLastVisibleDayOfWeek(6);
		cal.setFirstVisibleHourOfDay(8);
		cal.setLastVisibleHourOfDay(20);
		cal.setHeight("600px");

		BasicEventProvider eventProvider = (BasicEventProvider) cal
				.getEventProvider();
		con = new MysqlConnection();

		ResultSet rs = con
				.queryTable("SELECT cours.Nom,cours.Date_debut,cours.Date_Fin FROM cours,groupe,groupe_eleve,eleve "
						+ "Where cours.ID_groupe_cours=groupe.ID AND "
						+ "groupe.ID=groupe_eleve.ID_groupe AND "
						+ "groupe_eleve.ID_eleve=eleve.id_eleve AND eleve.id_eleve ="
						+ id);

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		while (rs.next()) {
			Date date_debut = sdf.parse(rs.getString("Date_debut"));
			Date date_fin = sdf.parse(rs.getString("Date_Fin"));
			eventProvider.addEvent(new BasicEvent(rs.getString("Nom"), "",
					date_debut, date_fin));
			// System.out.println(rs.getString("Date_debut"));
		}

		vl.addComponent(cal);
	}

	public void quit() {
		getApplication().close();
	}
}
