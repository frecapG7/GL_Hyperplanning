package planning;

import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Select;
import com.vaadin.ui.VerticalLayout;

public class AddPlanning extends CustomComponent {
	private VerticalLayout vl = new VerticalLayout();
	Select select = new Select ("Select something here");
	Select select2 = new Select ("Select something here");
	
	public AddPlanning() {
		setCompositionRoot(vl);
		select.setNullSelectionItemId("Sélectionner");
		select2.setNullSelectionItemId("Sélectionner");
		select.addItem("parcours");
		select.addItem("eleve");
		select.addItem("groupe");
		select.setImmediate(true);
		select.addListener(new Property.ValueChangeListener() {

			public void valueChange(ValueChangeEvent event) 
			    {           
			     if (event.getProperty().toString()=="eleve"){
			     select2.removeAllItems();
			     select2.addItem("dai");
			     select2.addItem("simon");
			     }
			     if (event.getProperty().toString()=="parcours"){
			    	 select2.removeAllItems();
				     select2.addItem("system d'information");
				     select2.addItem("ligiciel engineering");
				 }
			     if (event.getProperty().toString()=="groupe"){
			    	 select2.removeAllItems();
				     select2.addItem("g5");
				     select2.addItem("g6");
				  }
			    }
			});
		
		vl.addComponent(select);
		vl.addComponent(select2);
	}
}
