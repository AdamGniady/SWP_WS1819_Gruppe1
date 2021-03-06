/**
 * 
 */
package de.hdm.softwarePraktikumGruppe1.shared.bo;

import java.sql.Timestamp;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * @author GianlucaBernert
 * @author Yesin Soufi
 * @author SebastianHermann
 * Klasse eines User Objekts das BusinessObject als Superklasse besitzt
 */
public class User implements IsSerializable{
	
	private static final long serialVersionUID = 1L;
	private int userId;
	private String nickname;
	private String firstName;
	private String lastName;
	private String gMail;
	private Timestamp creationTimeStamp;

	/**
	 * Leerer Konstruktor. Die Zuweisung der Attribute wird über die Setter-Methoden realisiert.
	 */
	
	public User() {
		
	}
	
	/**
	 * Methode die den Nicknamen eines Users zurück gibt
	 * @return nickname
	 */
	public String getNickname() {
		return nickname;
	}
	
	/**
	 * Methode die den Nicknamen eines Users setzt
	 * @param nickname
	 */
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	
	/**
	 * Methode die den Vornamen des Users zurück gibt
	 * @return firstName
	 */
	
	public String getFirstName() {
		return firstName;
	}
	
	/**
	 * Methode die den Vornamen eines Users setzt
	 * @param firstName
	 */
	
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	/**
	 * Methode die den Nachnamen eines Users zurück gibt
	 * @return lastName
	 */
	
	public String getLastName() {
		return lastName;
	}
	
	/**
	 * Methode die den Nachnamen eines Users setzt
	 * @param lastName
	 */
	
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	/**
	 * Methode die die E-Mail eines Users zurück gibt
	 *@return gMail
	 */
	public String getGMail() {
		return gMail;
	}
	
	/**
	 * Methode die die E-Mail eines Users setzt
	 * @param gMail
	 */
	
	public void setGMail(String gMail) {
		this.gMail = gMail;
	}
	
	
	/**
	 * Methode um eine textuelle Dastellung der jeweiligen Instanz zu erzeugen
	 */
	public String toString() {
		return "UserID #U" + this.getUserId() + " " + this.firstName + " " + this.lastName;
	}
	
	/**
	 * Methode die die User ID zurueck gibt
	 * @return userId
	 */
	
	public int getUserId() {
		return userId;
	}
	
	/**
	 * Methode die die User ID setzt
	 * @param userId
	 */
	public void setUserId(int userId) {
		this.userId= userId;
	}
	
	/**
	 * Methode die das Erstellungsdatum zurückgibt
	 * @return creationTimeStamp
	 */

	public Timestamp getCreationTimeStamp() {
		return creationTimeStamp;
	}
	
	/**
	 * Methode die das Erstellungsdatum setzt
	 * @param creationTimeStamp
	 */

	public void setCreationTimeStamp(Timestamp creationTimeStamp) {
		this.creationTimeStamp = creationTimeStamp;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof User) {
			User u = (User) obj;
			if((this.getUserId()== u.getUserId()) && (this.getFirstName().equals(u.getFirstName())) && (this.getLastName().equals(u.getLastName()))&& (this.getNickname().equals(u.getNickname()))){				
				System.out.println("Firstname: "+ u.getFirstName() +" Lastname: "+u.getLastName() + " Nickname: " + u.getNickname());
				return true;	
			}
			else {
				return false;
			}
		}
		return false;
	}

	@Override
	public int hashCode() {
		int result = this.getUserId();
		return result;
	}
	
	
}
