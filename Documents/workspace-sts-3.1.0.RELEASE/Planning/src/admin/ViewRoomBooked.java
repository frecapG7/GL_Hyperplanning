package admin;

import global.MysqlConnection;

import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;

public class ViewRoomBooked extends CustomComponent {
	private Button quit = new Button("Retour", this, "back");
	private VerticalLayout vl = new VerticalLayout();
	private Table table;

	public ViewRoomBooked() {
		setCompositionRoot(vl);
		vl.setSizeFull();
		vl.setSpacing(true);
		vl.setMargin(true);		
		vl.addComponent(quit);
		vl.addComponent(getRoomBooked());
	}
	
	private Table getRoomBooked() {
		table = new Table();
		/* Define the names and data types of columns.
		 * The "default value" parameter is meaningless here. */
		table.addContainerProperty("Professeur", String.class,  null);
		table.addContainerProperty("Salle", String.class,  null);
		table.addContainerProperty("Début", Date.class,  null);
		table.addContainerProperty("Fin",  Date.class,  null);
		table.addContainerProperty("Matiere", String.class,  null);
		table.addContainerProperty("Groupe", String.class,  null);
		table.addContainerProperty("Type de cours", String.class,  null);
		table.addContainerProperty("Remarque", String.class,  null);
		table.addContainerProperty("Accepter", Button.class,  null);
		table.addContainerProperty("Refuser", Button.class,  null);
		
		try {
			MysqlConnection con = new MysqlConnection();
			ResultSet rs = con.queryTable("SELECT c.id, c.Date_Debut, c.Date_Fin, s.Nom AS nomSalle," +
					" m.Nom AS nomMatiere, p.Nom AS nomProfesseur, g.Nom as nomGroupe, tc.type," +
					" c.remarque" +
					" FROM cours c" +
					" LEFT JOIN salles s ON s.ID = c.ID_salle" +
					" LEFT JOIN matiere m ON m.ID = c.ID_matiere" +
					" LEFT JOIN personne p ON p.id_identifiant = c.ID_professeur" +
					" LEFT JOIN groupe g ON g.ID = c.ID_groupe_cours" +
					" LEFT JOIN type_cours tc ON tc.id = c.type" +
					" WHERE c.actif = 0 AND p.statut = 2");
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

			while (rs.next()) {
				final int id_cours = rs.getInt("ID");
				Date date_debut = sdf.parse(rs.getString("Date_Debut"));
				Date date_fin = sdf.parse(rs.getString("Date_Fin"));
				String salle = rs.getString("nomSalle");
				String matiere = rs.getString("nomMatiere");
				String professeur = rs.getString("nomProfesseur");
				String groupe = rs.getString("nomGroupe");
				String type = rs.getString("type");
				String remarque = rs.getString("remarque");
				
				Button validButton = new Button("Accepter");
				validButton.addListener(new Button.ClickListener() {
					public void buttonClick(ClickEvent event) {
						update(id_cours);
					}
				});
				
				Button deleteButton = new Button("Refuser");
				deleteButton.addListener(new Button.ClickListener() {
					public void buttonClick(ClickEvent event) {
						delete(id_cours);
					}
				});
				
				table.addItem(new Object[] {
						professeur,salle,date_debut,date_fin,matiere,groupe,type,remarque,validButton,deleteButton}, id_cours);	
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return table;
	}

	private void delete(int id) {
		try {
			MysqlConnection con = new MysqlConnection();
			con.executeTable("DELETE FROM cours WHERE ID = " + id);
			table.removeItem(id);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void update(int id) {
		try {
			MysqlConnection con = new MysqlConnection();
			con.executeTable("UPDATE cours set actif = 1 WHERE ID = " + id);
			table.removeItem(id);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void back() {
		setCompositionRoot(new AdminTabsheet());
	}
}
