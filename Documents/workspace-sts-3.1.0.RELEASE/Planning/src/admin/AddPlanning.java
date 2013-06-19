package admin;

import java.sql.ResultSet;
import global.MysqlConnection;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Select;
import com.vaadin.ui.VerticalLayout;

public class AddPlanning extends CustomComponent {
	private VerticalLayout vl = new VerticalLayout();
	private VerticalLayout vlCalendar = new VerticalLayout();
	private Select select = new Select("Choix du groupe");
	private MysqlConnection con;

	public AddPlanning() {
		vl.setSpacing(true);
		vl.setMargin(true);
		setCompositionRoot(vl);
		select.setNullSelectionItemId("Sélectionner");
		select.setImmediate(true);
		select.removeAllItems();
		try {
			con = new MysqlConnection();
			ResultSet rs = con.queryTable("select nom from groupe");
			while (rs.next()) {
				select.addItem(rs.getString("Nom"));
			}

			select.addListener(new Property.ValueChangeListener() {
				public void valueChange(ValueChangeEvent event) {
					try {
						vl.removeComponent(vlCalendar);
						vlCalendar.removeAllComponents();
						vlCalendar.addComponent(new AddPlanningCalendar(event
								.getProperty().toString(), "groupe"));
						vl.addComponent(vlCalendar);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		vl.addComponent(select);
		vl.addComponent(vlCalendar);
	}
}
