package planning;

import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import com.vaadin.addon.calendar.event.BasicEvent;
import com.vaadin.addon.calendar.event.BasicEventProvider;
import com.vaadin.addon.calendar.ui.Calendar;
import com.vaadin.addon.calendar.ui.CalendarTargetDetails;
import com.vaadin.data.Item;
import com.vaadin.event.dd.DragAndDropEvent;
import com.vaadin.event.dd.DropHandler;
import com.vaadin.event.dd.acceptcriteria.AcceptAll;
import com.vaadin.event.dd.acceptcriteria.AcceptCriterion;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.DateField;
import com.vaadin.ui.DragAndDropWrapper;
import com.vaadin.ui.DragAndDropWrapper.DragStartMode;
import com.vaadin.ui.DragAndDropWrapper.WrapperTransferable;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Select;
import com.vaadin.ui.Table.TableTransferable;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

public class AddPlanningCalendar extends CustomComponent{
	public HorizontalLayout hl = new HorizontalLayout();
	public HorizontalLayout hl2 = new HorizontalLayout();
	public VerticalLayout vl = new VerticalLayout();	
	public TextField classNameInputField=new TextField("Le nom de cour:");	
	public TextField classTimeInputField=new TextField("Duree(minute):");
	public TextField classWeekInputField=new TextField("Semaine:");
	public Select classrommInputField=new Select("Salle:");
	public Select classTeacherInputField=new Select("Professeur:");
	public Button moveMe=new Button("Move me to calendar");
	public Button confirmAddPlanning=new Button("Valider");
	public Button reset=new Button("Reset");
	private MysqlConnection con= new MysqlConnection();;
	public AddPlanningCalendar(String nom,String type) throws Exception {
		setCompositionRoot(hl);
		Calendar cal = new Calendar();
		cal.setFirstVisibleDayOfWeek(2);
		cal.setLastVisibleDayOfWeek(6);
		cal.setFirstVisibleHourOfDay(8);
		cal.setLastVisibleHourOfDay(20);
		//cal.setTimeZone(TimeZone.getTimeZone("Europe/Paris"));
		cal.setHeight("450px");
		cal.setWidth("850px");
		if(type=="eleve") {
			
		}
		
		if(type=="parcour") {
			
		}
		
		if(type=="groupe") {			
			BasicEventProvider eventProvider = (BasicEventProvider) cal
					.getEventProvider();			
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
		}
			cal.setDropHandler(new DropHandler(){

				public void drop(DragAndDropEvent event) {
					CalendarTargetDetails details =
							(CalendarTargetDetails) event.getTargetDetails();
					WrapperTransferable transferable =
							(WrapperTransferable) event.getTransferable();
				try {
					createEvent(details, transferable);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				}

				public AcceptCriterion getAcceptCriterion() {
					return AcceptAll.get();
				}});		
		ResultSet rs = con.queryTable("select Nom FROM salles ");
		while(rs.next()){
			classrommInputField.addItem(rs.getString("Nom"));
		}
		rs=con.queryTable("select Nom FROM personne where id_identifiant=2 ");
		while(rs.next()){
			classTeacherInputField.addItem(rs.getString("Nom"));
		}
		classrommInputField.setNullSelectionItemId("Sélectionner");
		classTeacherInputField.setNullSelectionItemId("Sélectionner");
		classNameInputField.setRequired(true);
		classNameInputField.setRequiredError("The Field may not be empty.");
		classTimeInputField.setRequired(true);
		classTimeInputField.setRequiredError("The Field may not be empty.");
		classWeekInputField.setRequired(true);
		classWeekInputField.setRequiredError("The Field may not be empty.");
		classrommInputField.setRequired(true);
		classrommInputField.setRequiredError("The Field may not be empty.");		
		moveMe.setVisible(false);		
		confirmAddPlanning.addListener(new Button.ClickListener() {

			public void buttonClick(ClickEvent event) {
				if(classNameInputField.getValue()!=""&&classTimeInputField.getValue()!=""&&classWeekInputField.getValue()!=""&&classrommInputField.getValue()!=null){
					//moveMe.setWidth("20px");
					//moveMe.setHeight("30px");
					//System.out.println(classrommInputField.getValue());
					classNameInputField.setReadOnly(true);
					classTimeInputField.setReadOnly(true);
					classWeekInputField.setReadOnly(true);
					classrommInputField.setReadOnly(true);
					classTeacherInputField.setReadOnly(true);
				moveMe.setVisible(true);	}
				else
				{
					getApplication().getMainWindow().showNotification(
							"Les champs doivent pas null.");
				}
			}
			
		});
		reset.addListener(new Button.ClickListener() {

			public void buttonClick(ClickEvent event) {
				moveMe.setVisible(false);
				classNameInputField.setReadOnly(false);
				classTimeInputField.setReadOnly(false);
				classWeekInputField.setReadOnly(false);
				classrommInputField.setReadOnly(false);
				classTeacherInputField.setReadOnly(false);
				classNameInputField.setValue("");
				classTimeInputField.setValue("");
				classWeekInputField.setValue("");				
			}
			
		});
				
		hl.addComponent(cal);				
		vl.addComponent(classNameInputField);
		vl.addComponent(classTimeInputField);
		vl.addComponent(classWeekInputField);
		vl.addComponent(classrommInputField);
		vl.addComponent(classTeacherInputField);
		hl2.addComponent(confirmAddPlanning);
		hl2.addComponent(reset);
		vl.addComponent(hl2);
		vl.addComponent(moveMe);
		final DragAndDropWrapper classWrap = new DragAndDropWrapper(moveMe);
		classWrap.setDragStartMode(DragStartMode.COMPONENT);
		classWrap.setSizeUndefined();
		vl.addComponent(classWrap);
		hl.addComponent(vl);
		
	}
	protected void createEvent(CalendarTargetDetails details,
			WrapperTransferable transferable) throws Exception {
		Date dropTime = details.getDropTime();
		java.util.Calendar timeCalendar = details.getTargetCalendar()
				.getInternalCalendar();		
		timeCalendar.setTime(dropTime);
		timeCalendar.add(java.util.Calendar.HOUR, 32);//the original time is not the time i put on the calendar, there always exist 32 hours between them
		int week=Integer.parseInt(classWeekInputField.getValue().toString());
		for(int i=0;i<week;i++){		
		Date startTime=timeCalendar.getTime();
		timeCalendar.add(java.util.Calendar.MINUTE, Integer.parseInt(classTimeInputField.getValue().toString()));//
		Date endTime = timeCalendar.getTime();
		java.sql.Timestamp sqlDate_begin = new java.sql.Timestamp(startTime.getTime());
		//System.out.println(sqlDate_begin);
		java.sql.Timestamp sqlDate_end = new java.sql.Timestamp(endTime.getTime());
		//System.out.println(classrommInputField.getValue());		
		ResultSet rs=con.queryTable("select id from salles where nom='"+classrommInputField.getValue()+"'");
		int id_salle=1;
		while(rs.next()){
		id_salle=Integer.parseInt(rs.getString("id"));}				
		rs=con.queryTable("select professeur.id_prof from professeur,personne where personne.nom='"
		+classTeacherInputField.getValue()
		+"' and personne.id_eleve_prof=professeur.id_prof");
		int id_prof=1;
		while(rs.next()){
		id_prof=Integer.parseInt(rs.getString("id_prof"));}
		con.executeTable("INSERT INTO cours (Nom,Date_Debut,Date_Fin,Id_salle,id_matiere,id_professeur,id_groupe_cours) VALUES ('"
						+ (String)classNameInputField.getValue()
						+ "','"
						+ sqlDate_begin
						+ "','"
						+ sqlDate_end + "',"+id_salle+",1,"+id_prof+",1)");
		BasicEventProvider ep = (BasicEventProvider) details
				.getTargetCalendar().getEventProvider();		
		ep.addEvent(new BasicEvent((String)classNameInputField.getValue(), "",
				startTime, endTime));
		timeCalendar.add(java.util.Calendar.MINUTE, 24*7*60-Integer.parseInt(classTimeInputField.getValue().toString()));//For adding several weeks planning
		}
	}
	

}
