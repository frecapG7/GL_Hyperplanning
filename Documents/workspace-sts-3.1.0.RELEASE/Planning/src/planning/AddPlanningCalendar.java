package planning;

import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.vaadin.addon.calendar.event.BasicEvent;
import com.vaadin.addon.calendar.event.BasicEventProvider;
import com.vaadin.addon.calendar.ui.Calendar;
import com.vaadin.addon.calendar.ui.CalendarTargetDetails;
import com.vaadin.data.Item;
import com.vaadin.event.dd.DragAndDropEvent;
import com.vaadin.event.dd.DropHandler;
import com.vaadin.event.dd.acceptcriteria.AcceptAll;
import com.vaadin.event.dd.acceptcriteria.AcceptCriterion;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.DateField;
import com.vaadin.ui.DragAndDropWrapper;
import com.vaadin.ui.DragAndDropWrapper.DragStartMode;
import com.vaadin.ui.DragAndDropWrapper.WrapperTransferable;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table.TableTransferable;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

public class AddPlanningCalendar extends CustomComponent{
	public HorizontalLayout hl = new HorizontalLayout();
	public VerticalLayout vl = new VerticalLayout();	
	public TextField classNameInputField=new TextField("Le nom de cour:");	
	public TextField classTimeInputField=new TextField("Duree(minute):");
	public TextField classWeekInputField=new TextField("Semaine:");
	public Label moveMe=new Label("Move me to calendar");
	private MysqlConnection con;
	public AddPlanningCalendar(String nom,String type) throws Exception {
		setCompositionRoot(hl);
		Calendar cal = new Calendar();
		cal.setFirstVisibleDayOfWeek(2);
		cal.setLastVisibleDayOfWeek(6);
		cal.setFirstVisibleHourOfDay(8);
		cal.setLastVisibleHourOfDay(20);
		cal.setHeight("450px");
		cal.setWidth("850px");
		if(type=="eleve") {
			
		}
		
		if(type=="parcour") {
			
		}
		
		if(type=="groupe") {			
			BasicEventProvider eventProvider = (BasicEventProvider) cal
					.getEventProvider();
			con = new MysqlConnection();
			ResultSet rs = con.queryTable("select cours.Nom,cours.Date_debut,cours.Date_Fin FROM cours,groupe " +
					"Where cours.ID_groupe_cours=groupe.ID AND groupe.Nom='"
					+nom+"'");
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

			while (rs.next()) {
				Date date_debut = sdf.parse(rs.getString("Date_debut"));
				Date date_fin = sdf.parse(rs.getString("Date_Fin"));
				eventProvider.addEvent(new BasicEvent(rs.getString("Nom"), "",
						date_debut, date_fin));				
			}
			
			cal.setDropHandler(new DropHandler(){

				public void drop(DragAndDropEvent event) {
					CalendarTargetDetails details =
							(CalendarTargetDetails) event.getTargetDetails();
					WrapperTransferable transferable =
							(WrapperTransferable) event.getTransferable();
				createEvent(details, transferable);
				}

				public AcceptCriterion getAcceptCriterion() {
					return AcceptAll.get();
				}});		
		}
		
		hl.addComponent(cal);				
		vl.addComponent(classNameInputField);
		vl.addComponent(classTimeInputField);
		vl.addComponent(classWeekInputField);
		vl.addComponent(moveMe);
		final DragAndDropWrapper classWrap = new DragAndDropWrapper(moveMe);
		classWrap.setDragStartMode(DragStartMode.COMPONENT);
		classWrap.setSizeUndefined();
		vl.addComponent(classWrap);
		hl.addComponent(vl);
		
	}
	protected void createEvent(CalendarTargetDetails details,
			WrapperTransferable transferable) {
		Date dropTime = details.getDropTime();
		java.util.Calendar timeCalendar = details.getTargetCalendar()
				.getInternalCalendar();
		timeCalendar.setTime(dropTime);	
		
		timeCalendar.add(java.util.Calendar.MINUTE, Integer.parseInt(classTimeInputField.getValue().toString()));//
		Date endTime = timeCalendar.getTime();			
		BasicEventProvider ep = (BasicEventProvider) details
				.getTargetCalendar().getEventProvider();
		//System.out.println((String)classNameInputField.getValue());
		//System.out.println((String)classTimeInputField.getValue());
		ep.addEvent(new BasicEvent((String)classNameInputField.getValue(), "",
				dropTime, endTime));
		
	}
	

}
