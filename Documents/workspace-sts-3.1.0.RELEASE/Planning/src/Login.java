

import global.LoginInformation;
import global.MysqlConnection;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.util.Date;

import student.Student;
import teacher.Teacher;


import admin.Administration;

import com.vaadin.Application;
import com.vaadin.addon.calendar.event.BasicEvent;
import com.vaadin.addon.calendar.event.BasicEventProvider;
import com.vaadin.addon.calendar.ui.Calendar;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.LoginForm;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.SplitPanel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class Login extends Application {

	public Window mainWindow;
	public VerticalLayout vl = new VerticalLayout();
	public TextField login;
	public PasswordField pwd;

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

	public HorizontalLayout formLogin() {
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
		String password = encode((String) pwd.getValue());
		MysqlConnection con;
		/*Label l = new Label(password);
		vl.addComponent(l);*/

		try {
			con = new MysqlConnection();

			ResultSet rs = con
					.queryTable("SELECT statut, id_identifiant FROM personne WHERE login = '"
							+ name + "' AND mdp = '" + password + "'");
			rs.next();
			int statut = rs.getInt("statut");
			int identifiant = rs.getInt("id_identifiant");
			new LoginInformation(statut,identifiant);
			/*Label l1 = new Label("" + statut);
			vl.addComponent(l1);
			Label l2 = new Label("" + identifiant);
			vl.addComponent(l2);*/
			if (statut == 1) {
				//student
				//test();
				getMainWindow().setContent(new Student());
			} else if (statut == 2) {
				//prof
				getMainWindow().setContent(new Teacher());
			} else if (statut == 3) {
				//admin
				getMainWindow().setContent(new Administration());
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			getMainWindow().showNotification("Identifiant ou Mot de passe pas correct");
		}
	}
	
	public void test() {
		
		Label l = new Label("" + statut);
		vl.addComponent(l);
	}

	public String encode(String password) {
		byte[] hash = null;
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			hash = md.digest(password.getBytes());
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < hash.length; ++i) {
			String hex = Integer.toHexString(hash[i]);
			if (hex.length() == 1) {
				sb.append(0);
				sb.append(hex.charAt(hex.length() - 1));
			} else {
				sb.append(hex.substring(hex.length() - 2));
			}
		}
		return sb.toString();
	}
}