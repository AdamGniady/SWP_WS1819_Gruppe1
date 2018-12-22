package de.hdm.softwarePraktikumGruppe1.client.service.ReportGenerator;

import java.util.Date;

import com.google.gwt.user.client.rpc.AsyncCallback;

import de.hdm.softwarePraktikumGruppe1.shared.bo.Beitrag;
import de.hdm.softwarePraktikumGruppe1.shared.bo.Pinnwand;
import de.hdm.softwarePraktikumGruppe1.shared.bo.User;

public interface ReportGeneratorServiceInt {

	/*
	 * Methode die die Beitraege eines Users zurück gibt
	 */
	public void getBeitraegeFromUser(User u, Date d);
	
	/**
	 * Methode die die Likes eines Users zurück gibt
	 */
	public void getLikesFromUser(User u, Date d);
	
	/**
	 * Methode die das Abonnement eines Users zurück gibt
	 */
	public void getAbonnementFromPinnwand(Pinnwand p, Date d);
	
	/**
	 * Methode die den Kommentar eines Beitrags zurück gibt
	 */
	public void getKommentarFromBeitrag(Beitrag b, Date d);
	
	/**
	 * Methode die die Likes eines Beitrags zurück gibt
	 */
	public void getLikesFromBeitrag(Beitrag b, Date d);

}
