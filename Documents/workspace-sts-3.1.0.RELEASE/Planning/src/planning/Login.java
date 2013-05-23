package planning;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.util.Date;

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
	public static int statut = 0;
	public static int identifiant = 0;
	public TextField login;
	public PasswordField pwd;
	public Label wrongMessage= new Label("");

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
		vl.addComponent(wrongMessage);
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
			statut = rs.getInt("statut");
			identifiant = rs.getInt("id_identifiant");
			/*Label l1 = new Label("" + statut);
			vl.addComponent(l1);
			Label l2 = new Label("" + identifiant);
			vl.addComponent(l2);*/
			if (statut == 1) {
				Label l = new Label("test");
				vl.addComponent(l);
				getMainWindow().setContent(new ClassCalendar());
			} else if (statut == 2) {
				//
			} else if (statut == 3) {
				getMainWindow().setContent(new Administration());
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			wrongMessage.setCaption("Identifiant ou Mot de passe pas correct");			
			getMainWindow().setContent(vl);
		}
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
