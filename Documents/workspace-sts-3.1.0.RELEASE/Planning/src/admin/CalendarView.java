package admin;


import global.MysqlConnection;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;

public class CalendarView extends CustomComponent {
	private Button quit = new Button("Retour", this, "back");
	public VerticalLayout vl = new VerticalLayout();
	private MysqlConnection con;
	private Panel panel = new Panel();

	public CalendarView() {

		setCompositionRoot(vl);
		vl.setSizeFull();
		vl.setSpacing(true);
		vl.setMargin(true);
		vl.addComponent(createToolbar());
		Button newCalendar = new Button("Ajouter un calendrier", this, "add");
		vl.addComponent(newCalendar);
		vl.addComponent(createPanel());
	}
	
	public Panel createPanel() {
		VerticalLayout v = new VerticalLayout();

		try {
			con = new MysqlConnection();
			ResultSet rs = con.queryTable("SELECT * FROM calendar");
			DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
			while (rs.next()) {
				HorizontalLayout hl = new HorizontalLayout();
				String name = rs.getString("Nom");
				String beginDate = df.format(rs.getDate("Date_Debut"));
				String endDate = df.format(rs.getDate("Date_Fin"));
				final int id = rs.getInt("id");

				Label label = new Label("Calendrier : " + name
						+ " Date de début : " + beginDate + " Date de fin : "
						+ endDate);
				hl.addComponent(label);

				Button changeCalendarButton = new Button("Modifier");
				changeCalendarButton.addListener(new Button.ClickListener() {
					public void buttonClick(ClickEvent event) {
						changeCalendar(id);
					}
				});
				hl.addComponent(changeCalendarButton);

				Button removeCalendarButton = new Button("Supprimer");
				removeCalendarButton
						.setIcon(new ThemeResource("img/delete.png"));
				removeCalendarButton.addListener(new Button.ClickListener() {
					public void buttonClick(ClickEvent event) {
						removeCalendar(id);
					}
				});

				hl.addComponent(removeCalendarButton);
				v.addComponent(hl);
			}
			panel.setContent(v);
			con.endConnection();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return panel;
	}

	public void removeCalendar(int id) {
		try {
			con = new MysqlConnection();
			con.executeTable("DELETE FROM calendar WHERE ID = " + id);
			con.executeTable("DELETE FROM vacances WHERE id_calendar = " + id);
			con.endConnection();
			panel = createPanel();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	};

	public void changeCalendar(int id) {
		//getApplication().getMainWindow().setContent(new CalendarForm(id));
		setCompositionRoot(new CalendarForm(id));
	}

	public void add() {
		setCompositionRoot(new CalendarForm());
	}

	public HorizontalLayout createToolbar() {
		HorizontalLayout hl = new HorizontalLayout();
		hl.addComponent(quit);
		return hl;
	}

	public void back() {
		setCompositionRoot(new AdminTabsheet());
	}
}
