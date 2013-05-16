package planning;

import com.vaadin.Application;
import com.vaadin.data.util.sqlcontainer.RowId;
import com.vaadin.data.util.sqlcontainer.SQLContainer;
import com.vaadin.ui.*;

import java.awt.BorderLayout;
import java.sql.ResultSet;
import java.util.Date;


public class Accueil extends Application {
	private Window mainWindow;
	private VerticalLayout vl = new VerticalLayout();
	TextField login;
	PasswordField pwd;

	public void init() {
		mainWindow = new Window("Acceuil");
		setMainWindow(mainWindow);
				
		// layout of the main window
		vl.setSpacing(true);
		vl.setMargin(true);
		mainWindow.setContent(vl);
		
		
		Button validButton = new Button("Valider", this, "validate");
		vl.addComponent(formLogin());
		vl.addComponent(validButton);

	}
	
	public HorizontalLayout formLogin(){
		Label labelLogin = new Label("Identifiant");
		login = new TextField();
		Label labelPwd = new Label("Mot de passe");
		pwd = new PasswordField();
		HorizontalLayout hl = new HorizontalLayout();
		hl.setSpacing(true);
		hl.addComponent(labelLogin);
		hl.addComponent(login);
		hl.addComponent(labelPwd);
		hl.addComponent(pwd);
		return hl;
	}
	
		
	public void validate() {
		String name = (String) login.getValue();
		String password = (String) pwd.getValue();
		MysqlConnection con;
		
		try {
			con = new MysqlConnection();
			ResultSet rs = con.queryTable("SELECT count(*) as nb FROM identification WHERE login = '" + name + "' AND password = '" + password + "'");
			rs.next();
			int s = rs.getInt("nb");
			if (s == 1) {
								
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


}
