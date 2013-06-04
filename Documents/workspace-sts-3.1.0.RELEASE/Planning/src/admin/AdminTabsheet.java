package admin;




import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.VerticalLayout;

public class AdminTabsheet extends CustomComponent {
	private VerticalLayout vl = new VerticalLayout();

	public AdminTabsheet() {
		setCompositionRoot(vl);
		vl.setSizeFull();
		vl.setSpacing(true);
		vl.setMargin(true);
		Button b = new Button("Modifier/créer calendrier", this, "calendar");
		b.setStyleName("link"); 
		vl.addComponent(b);
	}
	
	public void calendar() {
		setCompositionRoot(new CalendarView());
	}
}
