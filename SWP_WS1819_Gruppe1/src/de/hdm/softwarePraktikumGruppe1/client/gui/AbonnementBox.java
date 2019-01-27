package de.hdm.softwarePraktikumGruppe1.client.gui;

import java.util.Vector;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;

import de.hdm.softwarePraktikumGruppe1.client.ClientsideSettings;
import de.hdm.softwarePraktikumGruppe1.client.gui.Header.GetUserByIdCallback;
import de.hdm.softwarePraktikumGruppe1.client.gui.Header.ShowAllAbonnementsByUserCallback;
import de.hdm.softwarePraktikumGruppe1.shared.PinnwandverwaltungAsync;
import de.hdm.softwarePraktikumGruppe1.shared.bo.Abonnement;
import de.hdm.softwarePraktikumGruppe1.shared.bo.Pinnwand;
import de.hdm.softwarePraktikumGruppe1.shared.bo.User;

/**
 * Die Klasse <code>AbonnementBox</code> ist zuständig für die Anzeige der Abonnements
 * die einem User zugeordnet sind. Diese Box hat über zwei Buttons die Möglichkeit 
 * auf die jeweiligen Pinnwände der Abo's zuzugreifen oder das Abonnement zu kündigen. 
 * 
 * @author AdamGniady
 * @param <ShowAbosDialogBox>
 */
public class AbonnementBox<ShowAbosDialogBox> extends FlowPanel {
	private String aboName;
	private String aboNickname;
	private Abonnement abo;
	private User shownUser;
	private ShowAbosDialogBox parent;
	
	private Label accountName = new Label("Abo Name");
	private Label nickName = new Label("@aboNickname");
	private Button pinnwandBtn = new Button("Pinnwand");
	private Button deaboBtn = new Button("Deabonnieren");
	
	private int pinnwandId;
	
	
	
	private FlowPanel accountWrapper = new FlowPanel();
	private FlowPanel nickWrapper = new FlowPanel();
	private FlowPanel pinnwandWrapper = new FlowPanel();
	private FlowPanel deaboWrapper = new FlowPanel();
	PinnwandverwaltungAsync pinnwandVerwaltung;
	
	public AbonnementBox(ShowAbosDialogBox parent, User u, Abonnement abonnement) {
		this.abo = abonnement;
		this.shownUser=u;
		this.parent=parent;
		
		
	}
	
	
	
	/**
	 * Der Konstruktor der Klasse ermöglicht die Übergabe von zwei Parametern um die richtigen 
	 * Namen des jeweiligen Abo's anzuzeigen. 
	 * 
	 * @param aboAccountName
	 * @param aboNickname
	 * 
	 * @author AdamGniady
	 */
	public AbonnementBox(String aboAccountName, String aboNickname, int pinnwandId) {
		this.accountName.setText(aboAccountName);
		this.nickName.setText(aboNickname);
		this.pinnwandId = pinnwandId;
		
		this.pinnwandBtn.setTitle("PinnwandId: " + this.pinnwandId);
	}
	
	
	public void onLoad() {
		pinnwandVerwaltung = ClientsideSettings.getPinnwandverwaltung();
		
		pinnwandVerwaltung.getUserById(this.abo.getPinnwandId(), new GetUserByPinnwandIdCallback());
		
		this.addStyleName("box grid_box radiusless");
		accountWrapper.addStyleName("box-item-ein-viertel");
		nickWrapper.addStyleName("box-item-ein-viertel");
		pinnwandWrapper.addStyleName("box-item-ein-viertel");
		deaboWrapper.addStyleName("box-item-ein-viertel");
		
		accountName.addStyleName("title is-size-4");
		nickName.addStyleName("is-size-5");
		pinnwandBtn.addStyleName("button hast-text-primary");
		deaboBtn.addStyleName("button bg-primary has-text-white");
		
		accountWrapper.add(accountName);
		nickWrapper.add(nickName);
		pinnwandWrapper.add(pinnwandBtn);
		deaboWrapper.add(deaboBtn);
		
		// Adding ClickHandlers to Buttons
		pinnwandBtn.addClickHandler(new ShowPinnwandClickHandler());
		
		this.add(accountWrapper);
		this.add(nickWrapper);
		this.add(pinnwandWrapper);
		this.add(deaboWrapper);
		deaboBtn.addClickHandler(new DeleteAboClickHandler());
	}
	
	class DeleteAboClickHandler implements ClickHandler{

		@Override
		public void onClick(ClickEvent event) {
			pinnwandVerwaltung.deleteAbonnement(abo, new DeleteAbonnementCallback());
			
		}
		
	}
	
	class DeleteAbonnementCallback implements AsyncCallback<Void>{

		@Override
		public void onFailure(Throwable caught) {
			Window.alert("Delete Abonnement RPC failed");
		}

		@Override
		public void onSuccess(Void result) {
//			accountWrapper.removeFromParent();
//			nickWrapper.removeFromParent();
//			pinnwandWrapper.removeFromParent();
//			deaboWrapper.removeFromParent();
			parent.removeAbonnementBox(this);
			
		}
		
	}
	
	
	
	public class GetUserByPinnwandIdCallback implements AsyncCallback<User> {

		@Override
		public void onFailure(Throwable caught) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onSuccess(User result) {
			
		}
		
	}
	
	/**
	 * Der <code>ShowPinnwandClickHandler</code> kümmert sich um die Anzeige 
	 * des Editors. Durch die Betätigung des ClickHandlers wird die entsprechende Pinnwand
	 * des Users im Editor angezeigt. 
	 * 
	 * @author AdamGniady
	 *
	 */
	private class ShowPinnwandClickHandler implements ClickHandler {
		@Override
		public void onClick(ClickEvent event) {
			RootPanel rootPinnwandPanel = RootPanel.get("rechteSeite");
			rootPinnwandPanel.clear();
			
			Label pinnwandName = new Label(nickName.getText() + "s" + " Pinnwand");
			pinnwandName.addStyleName("title is-size-2 text-color-primary content_margin");
			
			PinnwandBox aboPinnwand = new PinnwandBox(abo.getPinnwandId());
			
			rootPinnwandPanel.add(pinnwandName);
			rootPinnwandPanel.add(aboPinnwand);
			
			/**
			 * // Pinnwand Stuff
			Vector<BeitragBox> allBeitragBoxesOfPinnwand = new Vector<BeitragBox>();
			
			for(int i = 0; i < 10; i++) {
				BeitragBox tempBB = new BeitragBox();
				
				allBeitragBoxesOfPinnwand.add(tempBB);
			}
			
			PinnwandBox newPinnwand = new PinnwandBox(allBeitragBoxesOfPinnwand);
			 */
		}
	}
}
