/**
 * 
 */
package de.hdm.softwarePraktikumGruppe1.shared.bo;

/**
 * @author GianlucaBernert
 * Klasse eines Abonnement Objekts das BusinessObject als Superklasse besitzt
 */
public class Abonnement {
	
	private static final long serialVersionUID = 1L;
	private User owner;
	private Pinnwand pinnwand;
	private int ownerId;
	private int pinnwandId;
	private int User_UserID;
	private int Pinnwand_PinnwandID;
	/**
	 * Methode die den Besitzer des Abonnements zurueck gibt
	 */
	public User getOwner() {
		return owner;
	}
	
	/**
	 * Methode doe den Besitzer des Abonnements setzt
	 */
	public void setOwner(User owner) {
		this.owner = owner;
	}
	
	/**
	 * Methode die die abonnierte Pinnwand zurueck gibt
	 */
	public Pinnwand getPinnwand() {
		return pinnwand;
	}
	
	/**
	 * Methode die die zu abonierende Pinnwand setzt
	 */
	public void setPinnwand(Pinnwand pinnwand) {
		this.pinnwand = pinnwand;
	}
	
	/**
	 * Methode die die ID des Besitzers zurueck gibt
	 */
	public int getOwnerId() {
		return ownerId;
	}
	
	/**
	 * Methode die die ID des Besitzers setzt
	 */
	public void setOwnerId(int ownerId) {
		this.ownerId = ownerId;
	}
	
	/**
	 * Methode die die ID der abonierten Pinnwand zurueck gibt
	 */
	public int getPinnwandId() {
		return pinnwandId;
	}
	
	/**
	 * Methode die die ID der zu abonierenden Pinnwand setzt
	 */
	public void setPinnwandId(int pinnwandId) {
		this.pinnwandId = pinnwandId;
	}
	
	/*
	 * Methode die den PinnwandUserString abbildet
	 */
	public String pinnwandUserString() {
		return null;
	}
	
	/**
	 * Methode die die Fremdschlüssel ID der zu abonierenden Pinnwand setzt
	 */
	
	public void setUser_UserID(int User_UserID) {
		this.User_UserID = User_UserID;
	}
	
	/*
	 * Methode die den User_UserID abbildet
	 */
	
	public int getUser_UserID() {
		return User_UserID;
	}
	
	/**
	 * Methode die die Fremdschlüssel ID der zu abonierenden Pinnwand setzt
	 */
	
	public void setPinnwand_PinnwandID(int Pinnwand_PinnwandID) {
		this.Pinnwand_PinnwandID = Pinnwand_PinnwandID;
	}
	
	/*
	 * Methode die den Pinnwand_PinnwandID abbildet
	 */
	
	public int getPinnwand_PinnwandID() {
		return Pinnwand_PinnwandID;
	}
	
	

}
