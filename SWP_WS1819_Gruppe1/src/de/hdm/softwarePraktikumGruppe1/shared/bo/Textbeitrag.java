/**
 * 
 */
package de.hdm.softwarePraktikumGruppe1.shared.bo;

/**
 * @author gianluca
 * Klasse eines Textbeitrags Objekts das BusinessObject als Superklasse besitzt 
 * und selbst Superklasse von Beitrag und Kommentar ist 
 */
public class Textbeitrag extends BusinessObject{
	
	private long serialVersionUID;
	private String text;
	
	/**
	 * Methode die den Text eines Textbeitrag Objekts zurück gibt
	 */
	public String getText() {
		return text;
	}
	
	/**
	 * Methode die den Text eines Textbeitrag Objekts setzt
	 */
	public void setText(String text) {
		this.text = text;
	}
	
	

}
