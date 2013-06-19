import global.LoginInformation;
import global.MysqlConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import student.Student;
import teacher.Teacher;
import admin.Administration;
import com.vaadin.Application;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class Login extends Application {

	public Window mainWindow;
	public VerticalLayout layout = new VerticalLayout();
	public TextField login;
	public PasswordField pwd;
	public Panel panel = new Panel("Se connecter");

	public void init() {
		mainWindow = new Window("Acceuil");
		setMainWindow(mainWindow);
		setTheme("mytheme");
		
		mainWindow.setContent(layout);
		layout.addComponent(panel);
		layout.setSizeFull();
		layout.setComponentAlignment(panel, Alignment.MIDDLE_CENTER);
		panel.setWidth("600px");
		
		VerticalLayout vl = new VerticalLayout();
		panel.addComponent(vl);
		vl.setMargin(true);
		vl.setSpacing(true);
		vl.setSizeFull();
		Button validButton = new Button("Valider", this, "validate");
		HorizontalLayout hl = formLogin();
		vl.addComponent(hl);
		vl.setComponentAlignment(hl, Alignment.MIDDLE_CENTER);
		vl.addComponent(validButton);
		vl.setComponentAlignment(validButton, Alignment.MIDDLE_CENTER);
	}

	public HorizontalLayout formLogin() {
		Label labelLogin = new Label("Identifiant");
		login = new TextField();
		Label labelPwd = new Label("Mot de passe");
		pwd = new PasswordField();
		HorizontalLayout hl = new HorizontalLayout();
		hl.setSpacing(true);
		hl.setMargin(true);
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

		try {
			con = new MysqlConnection();

			ResultSet rs = con
					.queryTable("SELECT statut, id_identifiant FROM personne WHERE login = '"
							+ name + "' AND mdp = '" + password + "'");
			rs.next();
			int statut = rs.getInt("statut");
			int identifiant = rs.getInt("id_identifiant");
			new LoginInformation(statut,identifiant);
			if (statut == 1) {
				//student
				getMainWindow().setContent(new Student());
			} else if (statut == 2) {
				//prof
				getMainWindow().setContent(new Teacher());
			} else if (statut == 3) {
				//admin
				getMainWindow().setContent(new Administration());
			}
		} catch (Exception e) {
			//e.printStackTrace();
			getMainWindow().showNotification("Identifiant ou Mot de passe pas correct");
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
