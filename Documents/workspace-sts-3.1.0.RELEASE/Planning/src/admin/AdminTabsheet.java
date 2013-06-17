package admin;

import java.sql.ResultSet;

import global.MysqlConnection;

import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.VerticalLayout;

public class AdminTabsheet extends CustomComponent {
	private VerticalLayout vl = new VerticalLayout();

	public AdminTabsheet() {
		setCompositionRoot(vl);
		vl.setSizeFull();
		vl.setSpacing(true);
		vl.setMargin(true);
		Button b = new Button("Modifier/créer calendrier", this, "calendar");
		b.setStyleName("link"); 
		vl.addComponent(b);
		
		int i = getNbRoomBook();
		Button roomBook = new Button("Demande de reservation d'une salle (" + i + ")", this, "room");
		roomBook.setStyleName("link"); 
		vl.addComponent(roomBook);
	}
	
	private int getNbRoomBook() {
		try {
			MysqlConnection con = new MysqlConnection();
			ResultSet rs = con.queryTable("SELECT COUNT(*) AS nb" +
					" FROM cours" +
					" WHERE actif = 0");
			rs.next();
			return rs.getInt("nb");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return 0;
	}
	
	public void room() {
		setCompositionRoot(new ViewRoomBooked());
	}

	public void calendar() {
		setCompositionRoot(new CalendarView());
	}
}
