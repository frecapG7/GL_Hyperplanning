package planning;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Button.*;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Button;
import com.vaadin.ui.DateField;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

public class CalendarForm extends FormLayout {

	private Button valid = new Button("Valider", this, "validate");
	private Button add = new Button("Ajouter", this, "add");
	private Button reset = new Button("Reset", this, "reset");
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

	public CalendarForm() {
		setCaption("Nouveau calendrier");
		setSizeFull();

		name = new TextField("Nom du calendrier");
		name.setRequired(true);
		name.setRequiredError("The Field may not be empty.");

		beginDate = new DateField("Date du début de l'année");
		beginDate.setDateFormat("dd/MM/yyyy");
		beginDate.setRequired(true);
		beginDate.setRequiredError("The Field may not be empty.");

		endDate = new DateField("Date de la fin de l'année");
		endDate.setDateFormat("dd/MM/yyyy");
		endDate.setRequired(true);
		endDate.setRequiredError("The Field may not be empty.");

		beginHoliday = new DateField("Début des vacances");
		beginHoliday.setDateFormat("dd/MM/yyyy");

		endHoliday = new DateField("Fin des vacances");
		endHoliday.setDateFormat("dd/MM/yyyy");

		// add the component
		addComponent(name);
		addComponent(beginDate);
		addComponent(endDate);
		addComponent(beginHoliday);
		addComponent(endHoliday);
		addComponent(add);
		addComponent(valid);
		addComponent(reset);

	}

	public void add() {

		begin = (Date) beginDate.getValue();
		end = (Date) endDate.getValue();
		Date holidayBegin = (Date) beginHoliday.getValue();
		Date holidayEnd = (Date) endHoliday.getValue();

		 if (holidayBegin == null || holidayEnd == null) {
			//mainWindow.showNotification("Les champs vacances doivent être rempli.");
		} else if (holidayBegin.before(begin) || holidayBegin.after(end)
				|| holidayEnd.before(begin) || holidayEnd.after(end)) {
			// mainWindow.showNotification("Les vacances doivent être comprises entre le début et la fin de l'année.");
		} else if (holidayBegin.after(holidayEnd)) {
			// mainWindow.showNotification("Erreur la date de début des vacances doît être avant la date de fin");
		} else {
			Date[] arrayDate = new Date[2];
			arrayDate[0] = holidayBegin;
			arrayDate[1] = holidayEnd;
			arrList.add(arrayDate);
			displayHoliday();
		}
	}

	public void displayHoliday() {
		addComponent(panel);
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
				vac = new Label("Vacances du " + begin_holiday + " au " + end_holiday);
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
	
	private boolean verify() {
		calendar_title = (String) name.getValue();
		begin = (Date) beginDate.getValue();
		end = (Date) endDate.getValue();
		
		if (calendar_title == null) {
			//mainWindow.showNotification("Le calendrier doît avoir un nom.");
			return false;
		} else if (begin == null || end == null) {
			return false;
		}
		
		return true;
	}

	public void validate() throws Exception {
		boolean b = verify();
		if (b) {
			java.sql.Date sqlDate_begin = new java.sql.Date(begin.getTime());
			java.sql.Date sqlDate_end = new java.sql.Date(end.getTime());

			MysqlConnection con = new MysqlConnection();
			int id = con.insertTableReturnId("INSERT INTO calendar (Nom,Date_Debut,Date_Fin) VALUES ('" + calendar_title + "','" + sqlDate_begin + "','" + sqlDate_end + "')");
			int i = arrList.size();
			int j;
			Date[] arrayDate;
			for (j = 0; j < i; j++) {
				arrayDate = arrList.get(j);
				java.sql.Date sqlBegin_holiday = new java.sql.Date(arrayDate[0].getTime());
				java.sql.Date sqlEnd_holiday = new java.sql.Date(arrayDate[1].getTime());
				con.executeTable("INSERT INTO vacances (id_calendar,date_debut,date_fin) VALUES (" + id + ",'" + sqlBegin_holiday + "','" + sqlEnd_holiday + "')");
			}
			con.endConnection();
		}
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
