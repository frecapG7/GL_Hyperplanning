package teacher;

import java.sql.ResultSet;
import java.util.Date;
import global.LoginInformation;
import global.MysqlConnection;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.event.FieldEvents.FocusEvent;
import com.vaadin.event.FieldEvents.FocusListener;
import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.DateField;
import com.vaadin.ui.Select;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window.Notification;

public class BookRoom extends CustomComponent {

	public VerticalLayout vl = new VerticalLayout();
	private MysqlConnection con;
	private Select matiere;
	private Select group;
	private Select typeClass;
	private DateField begin;
	private DateField end;
	private Select batiment;
	private Select room;
	private Button valid = new Button("Valider", this, "valid");
	private Button quit = new Button("Reset", this, "reset");
	private TextArea note;
	private int id = LoginInformation.identifiant;

	
	public BookRoom() {
		setCompositionRoot(vl);
		vl.setSpacing(true);
		vl.setMargin(true);
		vl.addComponent(quit);

		matiere = new Select("Choix de la matière");
		try {
			getMatiere();
		} catch (Exception e) {
			e.printStackTrace();
		}
		vl.addComponent(matiere);
		
		group = new Select("Choisisser le groupe d'élève");
		group.addListener(new ValueChangeListener () {
			public void valueChange(ValueChangeEvent event) {
				// TODO Auto-generated method stub
				seeDisponibility();
			}
		});
		vl.addComponent(group);
		
		typeClass = new Select("Type de cours");
		getTypeClass();
		vl.addComponent(typeClass); 
		
		begin = new DateField("Date du début du cours");
		begin.setDateFormat("dd/MM/yyyy HH:mm");
		begin.setResolution(DateField.RESOLUTION_MIN);
		begin.setRequired(true);
		begin.setRequiredError("The Field may not be empty.");
		begin.addListener(new FocusListener() {
			public void focus(FocusEvent event) {
				setEndDate();
				addRoom();
				seeDisponibility();
			}
		});
		vl.addComponent(begin);

		end = new DateField("Heure de fin des cours");
		end.setDateFormat("HH:mm");
		end.setResolution(DateField.RESOLUTION_MIN);
		end.setRequired(true);
		end.setRequiredError("The Field may not be empty.");
		end.addStyleName("timefield");
		end.addListener(new FocusListener() {
			public void focus(FocusEvent event) {
				Date beginDate = (Date) begin.getValue();
				if (beginDate != null) {
					verifyEndDate();
					addRoom();
					seeDisponibility();
				}
			}
		});
		vl.addComponent(end);

		batiment = new Select("Choix du batiment");
		addItemBatiment();
		batiment.setNullSelectionAllowed(false);
		batiment.setRequired(true);
		batiment.setRequiredError("The Field may not be empty.");
		vl.addComponent(batiment);
		batiment.addListener(new ValueChangeListener () {
			public void valueChange(ValueChangeEvent event) {
				// TODO Auto-generated method stub
				addRoom();
			}
		});
		room = new Select("Salle disponible");
		room.setRequired(true);
		room.setRequiredError("The Field may not be empty.");
		vl.addComponent(room);
		
		note = new TextArea("Remarque sur le cours :");
		note.setWidth("500px");
		vl.addComponent(note);
		vl.addComponent(valid);
	}

	private void getTypeClass() {
		try {
			con = new MysqlConnection();
			ResultSet rs = con.queryTable("SELECT DISTINCT id, type" +
					" FROM type_cours");
			while (rs.next()) {
				String type = rs.getString("type");
				int id = rs.getInt("id");
				typeClass.addItem(id);
				typeClass.setItemCaption(id, type);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}

	private void setEndDate() {
		Date beginDate = (Date) begin.getValue();
		end.setValue(beginDate);
	}

	private void addRoom() {
		Date beginDate = (Date) begin.getValue();
		Date endDate = (Date) end.getValue();
		Object idBatiment = batiment.getValue();
		if (beginDate != null && endDate != null && idBatiment != null) {
			roomAvailable(beginDate, endDate, idBatiment);
		}
		end.addStyleName("timefield");
	}
	
	private void roomAvailable(Date beginDate, Date endDate, Object idBatiment) {
		java.sql.Timestamp sqlDate_begin = new java.sql.Timestamp(beginDate.getTime());
		java.sql.Timestamp sqlDate_end = new java.sql.Timestamp(endDate.getTime());
		room.removeAllItems();
		room.setValue(null);
		try {
			con = new MysqlConnection();
			ResultSet rs = con.queryTable("SELECT DISTINCT s.ID, s.Nom" +
					" FROM salles s" +
					" LEFT JOIN cours c ON c.ID_salle = s.ID" +
					" WHERE s.id_batiment = " + idBatiment +
					" AND s.ID NOT IN (" +
					" SELECT ID_salle FROM cours" +
					" WHERE actif = 1" +
					" AND (Date_Debut >= '" + sqlDate_begin + "'" +
					" AND Date_Debut <= '" + sqlDate_end +"')" +
					" OR (c.Date_Fin >= '" + sqlDate_begin + "'" +
					" AND Date_Fin < '" + sqlDate_end + "'))");
			while (rs.next()) {
				String name = rs.getString("Nom");
				int id = rs.getInt("ID");
				room.addItem(id);
				room.setItemCaption(id, name);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void addItemBatiment() {
		try {
			con = new MysqlConnection();
			ResultSet rs = con.queryTable("SELECT Nom, ID FROM batiment");
			while (rs.next()) {
				String name = rs.getString("Nom");
				int id_batiment = rs.getInt("ID");
				batiment.addItem(id_batiment);
				batiment.setItemCaption(id_batiment, name);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void getMatiere() throws Exception {
		con = new MysqlConnection();
		ResultSet rs = con.queryTable("SELECT m.Nom, m.ID" +
				" FROM matiere m" +
				" INNER JOIN matiere_prof p ON p.ID_matiere = m.ID" +
				" WHERE p.ID_prof = " + id);
		while (rs.next()) {
			String name = rs.getString("Nom");
			String id_matiere = rs.getString("ID");
			matiere.addItem(id_matiere);
			matiere.setItemCaption(id_matiere, name);
		}
		matiere.addListener(new ValueChangeListener () {
			public void valueChange(ValueChangeEvent event) {
				// TODO Auto-generated method stub
				try {
					getGroup();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}
	
	private void getGroup() throws Exception {
		group.removeAllItems();
		group.setValue(null);
		con = new MysqlConnection();
		Object id_matiere = matiere.getValue();
		ResultSet rs = con.queryTable("SELECT g.ID, g.Nom" +
				" FROM groupe g" +
				" INNER JOIN matiere_groupe m ON m.ID_groupe = g.ID" +
				" WHERE m.ID_matiere = '" + id_matiere + "'");
		while (rs.next()) {
			String name = rs.getString("Nom");
			int id_group = rs.getInt("ID");
			group.addItem(id_group);
			group.setItemCaption(id_group, name);
		}
	}

	public void valid() {
		Date beginDate = (Date) begin.getValue();
		Date endDate = (Date) end.getValue();
		Object idGroup = group.getValue();
		Object idMatiere = matiere.getValue();
		Object typeClassId = typeClass.getValue();
		Object roomId = room.getValue();
		Object noteText = note.getValue();
		
		if (beginDate != null && endDate != null
			&& beginDate != endDate && roomId != null
		) {
			if (idMatiere == null) {
				idMatiere = 0;
			}
			if (idGroup == null) {
				idGroup = 0;
			}
			if (typeClassId == null) {
				typeClassId = 0;
			}
			try {
				con = new MysqlConnection();
				java.sql.Timestamp sqlDate_begin = new java.sql.Timestamp(beginDate.getTime());
				java.sql.Timestamp sqlDate_end = new java.sql.Timestamp(endDate.getTime());
				con.executeTable("INSERT INTO cours (Date_Debut, Date_Fin, ID_salle, ID_matiere, ID_professeur, ID_groupe_cours, type, actif, remarque)" +
						" VALUES ('" + sqlDate_begin + "', '" + sqlDate_end + "', " + roomId + ", '" + idMatiere + "', " + id + ", " + idGroup + ", " + typeClassId +", 0, '" + noteText + "')");
				
				Notification notif = new Notification(
						"",
						"Une demande de réservation de la salle a été effectué.",
						Notification.TYPE_HUMANIZED_MESSAGE);
				notif.setDelayMsec(20000);
				getApplication().getMainWindow().showNotification(notif);
				reset();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				Notification notif = new Notification(
					    "Warning",
					    "<br/>Une erreur est survenue.",
					    Notification.TYPE_WARNING_MESSAGE);
				notif.setDelayMsec(20000);
				getApplication().getMainWindow().showNotification(notif);
			}
		}
	}
	
	private void seeDisponibility() {
		Date beginDate = (Date) begin.getValue();
		Date endDate = (Date) end.getValue();
		Object idGroup = group.getValue();
		if (beginDate != null && endDate != null && beginDate != endDate) {
			if (idGroup == null) {
				idGroup = -1;
			}
			java.sql.Timestamp sqlDate_begin = new java.sql.Timestamp(beginDate.getTime());
			java.sql.Timestamp sqlDate_end = new java.sql.Timestamp(endDate.getTime());
			try {
				con = new MysqlConnection();
				ResultSet rs = con.queryTable("SELECT COUNT(*) as Nb" +
						" FROM cours" +
						" WHERE (ID_groupe_cours = " + idGroup +
						" OR ID_professeur = " + id + ")" +
						" AND actif = 1" +
						" AND (Date_Debut >= '" + sqlDate_begin + "'" +
						" AND Date_Debut <= '" + sqlDate_end +"')" +
						" OR (Date_Fin >= '" + sqlDate_begin + "'" +
						" AND Date_Fin <= '" + sqlDate_end + "')" +
						" OR EXISTS (" +
						" (SELECT DISTINCT v.date_debut" +
						" FROM vacances v" +
						" RIGHT JOIN parcours p ON p.id_calendrier = v.id_calendar" +
						" RIGHT JOIN eleve e ON id_parcours = p.id" +
						" RIGHT JOIN  groupe_eleve ge ON ge.ID_eleve = e.id_eleve" +
						" WHERE ge.ID_groupe = " + idGroup +
						" AND v.date_debut <= '" + sqlDate_begin + "' AND  v.date_fin >= '" + sqlDate_begin + "'))");
				rs.next();
				int i = rs.getInt("Nb");
				if (i != 0) {
					Notification notif = new Notification(
						    "Erreur",
						    "<br/>Le créneau horaire n'est pas disponible.",
						    Notification.TYPE_WARNING_MESSAGE);

					notif.setDelayMsec(20000);
					getApplication().getMainWindow().showNotification(notif);
					begin.setValue(null);
					end.setValue(null);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	private void verifyEndDate() {
		Date beginDate = (Date) begin.getValue();
		Date endDate = (Date) end.getValue();
		if (endDate.before(beginDate)) {
			getApplication().getMainWindow().showNotification("Erreur, Date invalide");
			end.setValue(beginDate);
		}
	}
	
	public void reset() {
		setCompositionRoot(new BookRoom());
	}

}
