package planning;

import java.sql.ResultSet;

import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Select;
import com.vaadin.ui.VerticalLayout;

public class AddPlanning extends CustomComponent {
	private VerticalLayout vl = new VerticalLayout();
	private VerticalLayout vlCalendar = new VerticalLayout();
	private Select select = new Select ("Select something here");
	private Select select2 = new Select ("Choisir groupe");
	private MysqlConnection con;
	
	public AddPlanning() {
		setCompositionRoot(vl);
		//select.setNullSelectionItemId("Sélectionner");
		select2.setNullSelectionItemId("Sélectionner");
		//select.addItem("parcour");
		//select.addItem("groupe");
		//select.setImmediate(true);
		select2.setImmediate(true);
		/*select.addListener(new Property.ValueChangeListener() {

			public void valueChange(ValueChangeEvent event) 
			    {   */        
			     			    			     
			    /* if (event.getProperty().toString()=="parcour"){
			    	 select2.removeAllItems();
			    	 try {
						con = new MysqlConnection();
						ResultSet rs = con.queryTable("select nom from parcour");
						while (rs.next()) {
								select2.addItem(rs.getString("nom"));						
						}
						select2.addListener(new Property.ValueChangeListener(){
							public void valueChange(ValueChangeEvent event){
								try {
									vl.removeComponent(vlCalendar);
									vlCalendar.removeAllComponents();
									vlCalendar.addComponent(new AddPlanningCalendar(event.getProperty().toString(),"parcour"));
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
				 }*/
			     
			     
			    // if (event.getProperty().toString()=="groupe"){
			    	 select2.removeAllItems();
			    	 try {
							con = new MysqlConnection();
							ResultSet rs = con.queryTable("select nom from groupe");
							while (rs.next()) {
									select2.addItem(rs.getString("Nom"));						
							}
							
							select2.addListener(new Property.ValueChangeListener(){
								public void valueChange(ValueChangeEvent event){
									try {
										vl.removeComponent(vlCalendar);
										vlCalendar.removeAllComponents();
										vlCalendar.addComponent(new AddPlanningCalendar(event.getProperty().toString(),"groupe"));
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
			//	  }
			     
			//    }
		//	});		
		//vl.addComponent(select);
		vl.addComponent(select2);
		vl.addComponent(vlCalendar);
	}
}
