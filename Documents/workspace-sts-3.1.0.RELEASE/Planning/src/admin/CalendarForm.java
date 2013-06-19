package admin;

import global.MysqlConnection;

import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Button.*;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Button;
import com.vaadin.ui.DateField;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

public class CalendarForm extends CustomComponent {
	private Button valid = new Button("Valider", this, "validate");
	private Button add = new Button("Ajouter", this, "addHoliday");
	private Button reset = new Button("Reset", this, "reset");
	private Button quit = new Button("Retour", this, "back");
	private TextField name;
	private DateField beginDate;
	private DateField endDate;
	private DateField beginHoliday;
	private DateField endHoliday;
	private ArrayList<Date[]> arrList = new ArrayList<Date[]>();
	private Panel panel = new Panel("Vacances");
	private String calendar_title;
	private Date begin;
	private Date end;
	private int id_calendar = 0;
	private VerticalLayout mainLayout = new VerticalLayout();

	public CalendarForm() {
		setCompositionRoot(mainLayout);
		mainLayout.setSizeFull();
		mainLayout.setSpacing(true);
		mainLayout.setMargin(true);
		initializeFields();
		addComponents();
	}

	public CalendarForm(int id) {
		setCompositionRoot(mainLayout);
		mainLayout.setSizeFull();
		mainLayout.setSpacing(true);
		mainLayout.setMargin(true);
		id_calendar = id;
		initializeFields();
		MysqlConnection con;
		try {
			con = new MysqlConnection();

			ResultSet rs = con.queryTable("SELECT * FROM calendar WHERE id = "
					+ id_calendar);

			String title = null;
			Date begin = null;
			Date end = null;

			while (rs.next()) {
				begin = rs.getDate("Date_Debut");
				end = rs.getDate("Date_Fin");
				title = rs.getString("Nom");
			}

			name.setValue(title);
			beginDate.setValue(begin);
			endDate.setValue(end);

			ResultSet rs2 = con
					.queryTable("SELECT * FROM vacances WHERE id_calendar = "
							+ id_calendar);

			while (rs2.next()) {
				Date[] arrayDate = new Date[2];
				arrayDate[0] = rs2.getDate("date_debut");
				arrayDate[1] = rs2.getDate("date_fin");
				arrList.add(arrayDate);
			}

			addComponents();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void initializeFields() {
		setCaption("Nouveau calendrier");
		setSizeFull();

		name = new TextField("Nom du calendrier");
		name.setRequired(true);
		name.setRequiredError("The Field may not be empty.");

		beginDate = new DateField("Date du début de l'année");
		beginDate.setDateFormat("dd/MM/yyyy");
		beginDate.setResolution(DateField.RESOLUTION_DAY);
		beginDate.setRequired(true);
		beginDate.setRequiredError("The Field may not be empty.");

		endDate = new DateField("Date de la fin de l'année");
		endDate.setDateFormat("dd/MM/yyyy");
		endDate.setResolution(DateField.RESOLUTION_DAY);
		endDate.setRequired(true);
		endDate.setRequiredError("The Field may not be empty.");

		beginHoliday = new DateField("Début des vacances");
		beginHoliday.setResolution(DateField.RESOLUTION_DAY);
		beginHoliday.setDateFormat("dd/MM/yyyy");

		endHoliday = new DateField("Fin des vacances");
		endHoliday.setResolution(DateField.RESOLUTION_DAY);
		endHoliday.setDateFormat("dd/MM/yyyy");
		
	}

	private void addComponents() {
		mainLayout.addComponent(quit);
		mainLayout.addComponent(name);
		mainLayout.addComponent(beginDate);
		mainLayout.addComponent(endDate);
		mainLayout.addComponent(beginHoliday);
		mainLayout.addComponent(endHoliday);
		mainLayout.addComponent(add);
		if (!arrList.isEmpty()) {
			displayHoliday();
		}
		mainLayout.addComponent(valid);
		mainLayout.addComponent(reset);
	}

	public void addHoliday() {

		begin = (Date) beginDate.getValue();
		end = (Date) endDate.getValue();
		Date holidayBegin = (Date) beginHoliday.getValue();
		Date holidayEnd = (Date) endHoliday.getValue();
		beginHoliday.setValue(null);
		endHoliday.setValue(null);

		if (holidayBegin == null || holidayEnd == null) {
			getApplication().getMainWindow().showNotification(
					"Les champs vacances doivent être rempli.");
		} else if (holidayBegin.before(begin) || holidayBegin.after(end)
				|| holidayEnd.before(begin) || holidayEnd.after(end)) {
			getApplication()
					.getMainWindow()
					.showNotification(
							"Les vacances doivent être comprises entre le début et la fin de l'année.");
		} else if (holidayBegin.after(holidayEnd)) {
			getApplication()
					.getMainWindow()
					.showNotification(
							"Erreur la date de début des vacances doît être avant la date de fin");
		} else {
			Date[] arrayDate = new Date[2];
			arrayDate[0] = holidayBegin;
			arrayDate[1] = holidayEnd;
			arrList.add(arrayDate);
			displayHoliday();
		}
	}

	public void displayHoliday() {
		mainLayout.addComponent(panel);
		VerticalLayout vl = new VerticalLayout();
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		int i = arrList.size();
		int j;
		Date[] arrayDate;
		for (j = 0; j < i; j++) {
			arrayDate = arrList.get(j);
			String begin_holiday = df.format(arrayDate[0]);
			String end_holiday = df.format(arrayDate[1]);
			HorizontalLayout hl = new HorizontalLayout();
			Label vac;
			if (begin_holiday.equals(end_holiday)) {
				vac = new Label("Jours fériés le " + begin_holiday);
			} else {
				vac = new Label("Vacances du " + begin_holiday + " au "
						+ end_holiday);
			}
			hl.addComponent(vac);
			final int intHoliday = j;

			Button removeHolidayButton = new Button("Supprimer");
			removeHolidayButton.setIcon(new ThemeResource("img/delete.png"));
			removeHolidayButton.addListener(new Button.ClickListener() {
				public void buttonClick(ClickEvent event) {
					arrList.remove(intHoliday);
					displayHoliday();
				}
			});
			hl.addComponent(removeHolidayButton);
			vl.addComponent(hl);
		}
		vl.setMargin(true);
		panel.setContent(vl);
	}

	public void back() {
		setCompositionRoot(new CalendarView());
		// getApplication().getMainWindow().setContent(new CalendarView());
	}

	private boolean verify() {
		calendar_title = (String) name.getValue();
		begin = (Date) beginDate.getValue();
		end = (Date) endDate.getValue();

		if (calendar_title == null) {
			getApplication().getMainWindow().showNotification(
					"Le calendrier doît avoir un nom.");
			return false;
		} else if (begin == null || end == null) {
			return false;
		}

		return true;
	}

	public void validate() {
		boolean b = verify();
		if (b) {
			java.sql.Date sqlDate_begin = new java.sql.Date(begin.getTime());
			java.sql.Date sqlDate_end = new java.sql.Date(end.getTime());

			if (id_calendar != 0) {
				try {
					update(sqlDate_begin, sqlDate_end);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				try {
					insert(sqlDate_begin, sqlDate_end);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			setCompositionRoot(new CalendarView());
		}
	}

	private void insert(java.sql.Date sqlDate_begin, java.sql.Date sqlDate_end)
			throws Exception {
		MysqlConnection con = new MysqlConnection();
		int id = con
				.insertTableReturnId("INSERT INTO calendar (Nom,Date_Debut,Date_Fin) VALUES ('"
						+ calendar_title
						+ "','"
						+ sqlDate_begin
						+ "','"
						+ sqlDate_end + "')");
		insertHoliday(id);
		con.endConnection();
	}

	private void update(java.sql.Date sqlDate_begin, java.sql.Date sqlDate_end)
			throws Exception {
		MysqlConnection con = new MysqlConnection();
		con.executeTable("UPDATE calendar SET Nom = '" + calendar_title
				+ "', Date_Debut = '" + sqlDate_begin + "', Date_Fin = '"
				+ sqlDate_end + "' WHERE id = " + id_calendar);
		con.executeTable("DELETE FROM vacances WHERE id_calendar = "
				+ id_calendar);
		insertHoliday(id_calendar);
		con.endConnection();
	}

	private void insertHoliday(int id) throws Exception {
		MysqlConnection con = new MysqlConnection();
		int i = arrList.size();
		int j;
		Date[] arrayDate;
		for (j = 0; j < i; j++) {
			arrayDate = arrList.get(j);
			java.sql.Date sqlBegin_holiday = new java.sql.Date(
					arrayDate[0].getTime());
			java.sql.Date sqlEnd_holiday = new java.sql.Date(
					arrayDate[1].getTime());
			con.executeTable("INSERT INTO vacances (id_calendar,date_debut,date_fin) VALUES ("
					+ id
					+ ",'"
					+ sqlBegin_holiday
					+ "','"
					+ sqlEnd_holiday
					+ "')");
		}
		con.endConnection();
	}

	public void reset() {
		panel.setContent(null);
		arrList.clear();
		beginDate.setValue(null);
		endDate.setValue(null);
		beginHoliday.setValue(null);
		endHoliday.setValue(null);
	}
}
