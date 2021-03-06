/**
 * 
 */
package de.hdm.softwarePraktikumGruppe1.server;



import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Vector;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import de.hdm.softwarePraktikumGruppe1.server.db.AbonnementMapper;
import de.hdm.softwarePraktikumGruppe1.server.db.BeitragMapper;
import de.hdm.softwarePraktikumGruppe1.server.db.KommentarMapper;
import de.hdm.softwarePraktikumGruppe1.server.db.LikeMapper;
import de.hdm.softwarePraktikumGruppe1.server.db.UserMapper;
import de.hdm.softwarePraktikumGruppe1.shared.ReportGeneratorService;
import de.hdm.softwarePraktikumGruppe1.shared.bo.Abonnement;
import de.hdm.softwarePraktikumGruppe1.shared.bo.Beitrag;
import de.hdm.softwarePraktikumGruppe1.shared.bo.Kommentar;
import de.hdm.softwarePraktikumGruppe1.shared.bo.Like;
import de.hdm.softwarePraktikumGruppe1.shared.bo.User;
import de.hdm.softwarePraktikumGruppe1.shared.report.BeitragReport;
import de.hdm.softwarePraktikumGruppe1.shared.report.Column;
import de.hdm.softwarePraktikumGruppe1.shared.report.CompositeParagraph;
import de.hdm.softwarePraktikumGruppe1.shared.report.GenericReport;
import de.hdm.softwarePraktikumGruppe1.shared.report.Row;
import de.hdm.softwarePraktikumGruppe1.shared.report.SimpleParagraph;
import de.hdm.softwarePraktikumGruppe1.shared.report.UserReport;



/**
 * 
 * Serverseite Implementierung des <code>ReportGeneratorService</code>.
 * Hier werden die Reports mit Daten befüllt.
 *
 * @author JakobBenkoe
 * 
 */
public class ReportGeneratorServiceImpl extends RemoteServiceServlet implements ReportGeneratorService{

	/**
	 *  
	 */
	private static final long serialVersionUID = 1L;
	
	/*
	 * Different Date Formats. Can be used globally on server-side
	 */
	public final static SimpleDateFormat yearMonthDayFormat = new SimpleDateFormat ("yyyy.MM.dd");
	public final static SimpleDateFormat dayMonthYearFormat = new SimpleDateFormat ("dd.MM.yyyy");
	public final static SimpleDateFormat dayMonthYearTimeFormat = new SimpleDateFormat ("dd.MM.yyyy HH:mm");
	public final static SimpleDateFormat sqlFormat = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");
	
	
	/*
	 * Reports will be created between start and end date
	 */
	Date start = null;
	Date end = null;
	
	/*
	 * Mapper Declaration
	 */
	UserMapper uMapper = UserMapper.userMapper();	
	BeitragMapper beitragMapper = BeitragMapper.beitragMapper();
	AbonnementMapper abonnementMapper = AbonnementMapper.abonnementMapper();
	LikeMapper likeMapper = LikeMapper.likeMapper();
	KommentarMapper kommentarMaper = KommentarMapper.kommentarMapper();
	
	


	/*
	 * Hier wird der UserReport erstellt
	 */
	@Override
	public UserReport createUserReport(String gmail, Date date1, Date date2) throws IllegalArgumentException {
		//convert Dates
		Date[] finalDates = this.convertDates(date1, date2);
		start = finalDates[0];
		end = finalDates[1];
		
		//create Report
		UserReport userReport = new UserReport();
		userReport.setImprint(new SimpleParagraph("Report über den Zeitraum vom " + dayMonthYearFormat.format(start) + 
				" bis zum " + dayMonthYearFormat.format(end)));
		
		//user of the userReport
		User user = null;
		int userID;
		//Make Sure User with selected ID exists
		try {
			user = uMapper.findUserByGmail(gmail);
			userReport.setTitle("Report Über den User " + user.getNickname());
			userID = user.getUserId();
		//Otherwise return report that indicates the missing beitrag	
		}catch(Exception e){
			CompositeParagraph header = new CompositeParagraph();
			header.addSubParagraph(new SimpleParagraph("Keinen User mit der eMail [ " + gmail + " ] gefunden."));
			header.addSubParagraph(new SimpleParagraph("Bitte gebe eine existierende User eMail an"));
			userReport.setHeaderData(header);
			return userReport;
		}
	

		//Erzeuge einen header
		CompositeParagraph header = new CompositeParagraph();
		header.addSubParagraph(new SimpleParagraph("Vorname: " + user.getFirstName()));
		header.addSubParagraph(new SimpleParagraph("Nachname: " + user.getLastName()));
		header.addSubParagraph(new SimpleParagraph("Nickname: " + user.getNickname()));
		header.addSubParagraph(new SimpleParagraph("eMail Adresse: " + user.getGMail()));
		//Fuege den Header zum UserReport Hinzu
		userReport.setHeaderData(header);

	
		
		//Abonnements
		Vector<Abonnement> abonnements = abonnementMapper.findAbonnementsOfUserBetweenDates(userID, start, end);
		//Erzeuge einen GenericReport welcher Informationen über Abonnenten speichert
		GenericReport abonnentenReport = new GenericReport();
		
		if (abonnements.size() == 0) {
			abonnentenReport.setTitle("Informationen über Abonnenten (0)");
			Row row = new Row();
			row.addColumn(new Column("Keine Abonnenten in dem angegebenen Zeitraum gefunden"));
			abonnentenReport.addRow(row);
		}else {
			abonnentenReport.setTitle("Informationen über Abonnenten (" + abonnements.size() + ")");
			for(int i = 0; i < abonnements.size(); i++) {
				Abonnement abonnement = abonnements.get(i);
				//Erzeuge eine Reihe für einen Abonnenten
				Row row = new Row(true);
				User abonnent = uMapper.findUserById(abonnement.getOwnerId());
				abonnentenReport.addRow(new Row(new Column("")));
				row.addColumn(new Column("Abonniert von " + abonnent.getNickname()));
				row.addColumn(new Column("Der Abonnent besitzt die eMail-Adresse " + abonnent.getGMail()));
				row.addColumn(new Column("Abonnement erhalten am " + dayMonthYearTimeFormat.format(abonnement.getCreationTimeStamp()).toString()));
				//Füge die Reihe dem abonnentenReport
				abonnentenReport.addRow(row);
			}
		}
		//Füge die Abonnenteninformationen dem userReport hinzu
		userReport.addSubReport(abonnentenReport);
		
				
		//Beiträge
		Vector<Beitrag> beitraege = beitragMapper.findBeitraegeOfUserBetweenDates(userID, start, end);

		//Erzeuge einen GenericReport welcher Informationen über Beiträge speichert
		GenericReport beitraegeReport = new GenericReport();
		
		
		if (beitraege.size() == 0) {
			beitraegeReport.setTitle("Informationen über Beiträge (0)");
			Row row = new Row();
			row.addColumn(new Column("Keine Beiträge in dem angegebenen Zeitraum gefunden"));
			beitraegeReport.addRow(row);
		}else {
			beitraegeReport.setTitle("Informationen über Beiträge (" + beitraege.size() + ")");
			for(int i = 0; i < beitraege.size(); i++) {
				Beitrag beitrag = beitraege.get(i);
				Row row = new Row();
				row.addColumn(new Column(dayMonthYearTimeFormat.format(beitrag.getCreationTimeStamp())));
				beitraegeReport.addRow(row);
				row = new Row(true);
				row.addColumn(new Column(beitrag.getInhalt()));
				//Füge die Reihe dem beitraegeReport hinzu
				beitraegeReport.addRow(row);
			}
		}
		//Füge die Abonnenteninformationen dem userReport hinzu
		userReport.addSubReport(beitraegeReport);
		
		
		//Likes
		Vector<Like> likes = likeMapper.findLikesOfUserBetweenDates(userID, start, end);
		//Erzeuge einen GenericReport welcher Informationen über Likes speichert
		GenericReport likeReport = new GenericReport();
				
		if (likes.size() == 0) {
			likeReport.setTitle("Informationen über Likes (0)");
			Row row = new Row();
			row.addColumn(new Column("Keine Likes in dem angegebenen Zeitraum gefunden"));
			likeReport.addRow(row);
		}else {
			likeReport.setTitle("Informationen über Likes (" + likes.size() + ")");
			for(int i = 0; i < likes.size(); i++) {
				Like like = likes.get(i);
				//Erzeuge eine Reihe für einen Abonnenten
				Row row = new Row(true);
				Beitrag gelikterBeitrag = beitragMapper.findBeitragById(like.getBeitragId());
				User gelikterUser = uMapper.findUserById(gelikterBeitrag.getOwnerId());
				row.addColumn(new Column("Ein Like wurde verteilt am " + dayMonthYearTimeFormat.format(like.getCreationTimeStamp()).toString()));
				if(gelikterBeitrag != null)row.addColumn(new Column("Ein Beitrag von @" + gelikterUser.getNickname() + " wurde gelikt"));
				//Füge die Reihe dem abonnentenReport
				likeReport.addRow(row);
				}
			}
		//Füge die Likesinformationen dem userReport hinzu
		userReport.addSubReport(likeReport);
		
		
		return userReport;
	}

	
	
	
	
	
	
	/*
	 * Hier wird der BeitragsReport erstellt
	 */
	@Override
	public BeitragReport createBeitragReport(int beitragID, Date date1, Date date2) throws IllegalArgumentException {
		//convert Dates
		Date[] finalDates = this.convertDates(date1, date2);
		start = finalDates[0];
		end = finalDates[1];
				
		//create result Report
		BeitragReport beitragReport = new BeitragReport();		
		beitragReport.setImprint(new SimpleParagraph("Report über den Zeitraum vom " + dayMonthYearFormat.format(start) + 
				" bis zum " + dayMonthYearFormat.format(end)));

		Beitrag beitrag = null;
		User inhaber = null;
		
		//Make Sure Beitrag with selected ID exists
		try {
			beitrag = beitragMapper.findBeitragById(beitragID);
			inhaber = uMapper.findUserById(beitrag.getOwnerId());
			
		//Otherwise return report that indicates the missing beitrag	
		}catch(Exception e){	
			CompositeParagraph header = new CompositeParagraph();
			header.addSubParagraph(new SimpleParagraph("Keinen Beitrag mit der ID [ " + beitragID + " ] gefunden."));
			header.addSubParagraph(new SimpleParagraph("Bitte gebe eine existierende Beitrags ID an"));
			beitragReport.setHeaderData(header);
			return beitragReport;
		}
		
		
		

			//Create header
			CompositeParagraph header = new CompositeParagraph();
				try {
					beitragReport.setTitle("Report Über einen Beitrag von @" + inhaber.getNickname());	
					header.addSubParagraph(new SimpleParagraph(inhaber.getFirstName() + " " + inhaber.getLastName() + " hat diesen Beitrag erstellt."));
					//header.addSubParagraph(new SimpleParagraph("@" + inhaber.getNickname()));
					header.addSubParagraph(new SimpleParagraph("Autor eMail: " + inhaber.getGMail()));					
				}catch(Exception e) {
					beitragReport.setTitle("Beitragsreport Kein Autor gefunden");	
					header.addSubParagraph(new SimpleParagraph("Beitrag erstellt von: Zu diesem Beitrag konnte kein Autor gefunden werden"));
				}
			header.addSubParagraph(new SimpleParagraph(""));
			header.addSubParagraph(new SimpleParagraph("Beitrag erstellt am  " + dayMonthYearTimeFormat.format(beitrag.getCreationTimeStamp()).toString()));
			header.addSubParagraph(new SimpleParagraph(beitrag.getInhalt()));
			//Add header to result report
			beitragReport.setHeaderData(header);
			
			
			
			//Kommentare
			Vector<Kommentar> kommentare = kommentarMaper.findKommentareOfBeitrag(beitragID, start, end);
			//Erzeuge einen GenericReport welcher Informationen über Kommentare speichert
			GenericReport kommentarReport = new GenericReport();
					
			if (kommentare.size() == 0) {
				kommentarReport.setTitle("Informationen über Kommentare (0)");
				Row row = new Row();
				row.addColumn(new Column("Keine Kommentare in dem angegebenen Zeitraum gefunden"));
				kommentarReport.addRow(row);
			}else {
				kommentarReport.setTitle("Informationen über Kommentare (" + kommentare.size() + ")");
				for(int i = 0; i < kommentare.size(); i++) {
					Kommentar kommentar = kommentare.get(i);
					//Erzeuge eine Reihe für einen Abonnenten
					Row row = new Row(true);
					User autor = uMapper.findUserById(kommentar.getOwnerId()); 
					row.addColumn(new Column("Kommentiert von " + autor.getNickname()));
					row.addColumn(new Column("Kommentar erstellt am " + dayMonthYearTimeFormat.format(kommentar.getCreationTimeStamp())));
					row.addColumn(new Column(kommentar.getInhalt()));
					//Füge die Reihe dem abonnentenReport
					kommentarReport.addRow(row);
					}
				}
			//Füge die Likesinformationen dem userReport hinzu
			beitragReport.addSubReport(kommentarReport);
			
			
			//Likes
			Vector<Like> likes = likeMapper.findLikesOfBeitragBetweenDates(beitragID, start, end);
			//Erzeuge einen GenericReport welcher Informationen über Likes speichert
			GenericReport likeReport = new GenericReport();
					
			if (likes.size() == 0) {
				likeReport.setTitle("Informationen über Likes (0)");
				Row row = new Row();
				row.addColumn(new Column("Keine Likes in dem angegebenen Zeitraum gefunden"));
				likeReport.addRow(row);
			}else {
				likeReport.setTitle("Informationen über Likes (" + likes.size() + ")");
				for(int i = 0; i < likes.size(); i++) {
					Like like = likes.get(i);
					//Erzeuge eine Reihe für einen Abonnenten
					Row row = new Row(true);
					User likeUser = uMapper.findUserById(like.getOwnerId());
					row.addColumn(new Column("Like erhalten am " + dayMonthYearTimeFormat.format(like.getCreationTimeStamp()).toString()));
					row.addColumn(new Column("Like erhalten von @" + likeUser.getNickname()));
					//Füge die Reihe dem abonnentenReport
					likeReport.addRow(row);
					}
				}
		//Füge die Likesinformationen dem userReport hinzu
		beitragReport.addSubReport(likeReport);
		
		
		//return result report
		return beitragReport;
	}
	
	
	
	
	/**Methode um einen User zu suchen
	 * @param searchQuery
	 * @return users gibt die User zurück die gefunden wurden.
	 */
	public Vector<User> searchUserFunction(String searchQuery){
		HashSet<User> hs = new HashSet<User>();
		Vector<User> users = new Vector<User>();
		String s = searchQuery;
		if(uMapper.findUserByFirstName(s) != null)hs.addAll(uMapper.findUserByFirstName(s));
		if(uMapper.findUserByLastName(s) != null)hs.addAll(uMapper.findUserByLastName(s));
		if(uMapper.findUserByNickname(s) != null)hs.addAll(uMapper.findUserByNickname(s));
		if(uMapper.findUserByGmail(s) != null)hs.add(uMapper.findUserByGmail(s));
		
		Iterator<User> it = hs.iterator();
	     while(it.hasNext()){
	        users.add(it.next());
	     }
		
		return users;
	}
	
	
	
	@Override
	public Vector<Beitrag> searchBeitragFunction(String gMail) {

		User tempUser = uMapper.findUserByGmail(gMail);
		if (tempUser != null)return beitragMapper.findBeitraegeOfUser(tempUser.getUserId());
		return null;
	}
	
	
	
	
	/*
	 * Method @Code convertDates converts two dates to match Period Specifications
	 * @param two dates
	 * @return sorted dates with time being set
	 */
	public Date[] convertDates(Date d1, Date d2) {
		/*
		 * finalDates[0] eqauls start date
		 * finalDates[1] eqauls end date
		 */
		Date[] finalDates = new Date[2];
		//make sure start date is before end date
		if(d1.before(d2)) {
			finalDates[0] = d1;
			finalDates[1] = d2;
		}else {
			finalDates[0] = d2;
			finalDates[1] = d1;
		}
		
		//Set Time for start date to 00:00:00
        Calendar mor = Calendar.getInstance();
        mor.setTime(finalDates[0]);
        mor.set(Calendar.HOUR_OF_DAY, 0);
        mor.set(Calendar.MINUTE, 0);
        mor.set(Calendar.SECOND, 0);  
        finalDates[0].setTime(mor.getTimeInMillis());
		
        
        //Set Time for end date to 23:59:59
        Calendar eve = Calendar.getInstance();
        eve.setTime(finalDates[1]);
        eve.set(Calendar.HOUR_OF_DAY, 23);
        eve.set(Calendar.MINUTE, 59);
        eve.set(Calendar.SECOND, 59);
        finalDates[1].setTime(eve.getTimeInMillis());        
        
        //Return start and end date
		return finalDates;
	}
}
