package planning;

import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.vaadin.Application;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;

public class CalendarView extends Application {

	private Button quit = new Button("Quitter", this, "quit");
	public VerticalLayout vl = new VerticalLayout();
	private MysqlConnection con;

	@Override
	public void init() {
		try {
			buildMainLayout();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void buildMainLayout() throws Exception {
		setMainWindow(new Window("Planning"));
		// vl.setSizeFull();
		vl.setSpacing(true);
		vl.setMargin(true);
		vl.addComponent(createToolbar());
		vl.addComponent(createPanel());

		getMainWindow().setContent(vl);
	}

	public Panel createPanel() throws Exception {
		Panel panel = new Panel();
		con = new MysqlConnection();
		ResultSet rs = con.queryTable("SELECT * FROM calendar");
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		while (rs.next()) {
			String name = rs.getString("Nom");
			String beginDate = df.format(rs.getDate("Date_Debut"));
			String endDate = df.format(rs.getDate("Date_Fin"));
			final int id = rs.getInt("id");
			
			Label label = new Label("Calendrier : " + name +
					" Date de début : " + beginDate +
					" Date de fin : " + endDate);
			
			Button changeCalendarButton = new Button("Modifier");
			changeCalendarButton.addListener(new Button.ClickListener() {
				public void buttonClick(ClickEvent event) {
						changeCalendar(id);
				}
			});
			
			Button removeCalendarButton = new Button("Supprimer");
			removeCalendarButton.setIcon(new ThemeResource("img/delete.png"));
			removeCalendarButton.addListener(new Button.ClickListener() {
				public void buttonClick(ClickEvent event) {
					try {
						removeCalendar(id);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			    }
			});
			
		}
		con.endConnection();
		return panel;
	}
	
	public void removeCalendar(int id) throws Exception {
		con = new MysqlConnection();
		con.executeTable("DELETE CALENDAR WHERE id = " + id);
		con.endConnection();
	};
	
	public void changeCalendar(int id) {
		
	}

	public HorizontalLayout createToolbar() {
		HorizontalLayout hl = new HorizontalLayout();
		hl.addComponent(quit);
		return hl;
	}

	public void quit() {
		close();
	}

}
