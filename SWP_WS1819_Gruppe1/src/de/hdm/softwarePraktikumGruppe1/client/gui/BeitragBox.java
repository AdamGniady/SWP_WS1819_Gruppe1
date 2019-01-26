package de.hdm.softwarePraktikumGruppe1.client.gui;

import java.sql.Timestamp;
import java.util.Vector;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.VerticalPanel;

import de.hdm.softwarePraktikumGruppe1.client.ClientsideSettings;
import de.hdm.softwarePraktikumGruppe1.shared.PinnwandverwaltungAsync;
import de.hdm.softwarePraktikumGruppe1.shared.bo.Beitrag;
import de.hdm.softwarePraktikumGruppe1.shared.bo.Kommentar;
import de.hdm.softwarePraktikumGruppe1.shared.bo.Like;
import de.hdm.softwarePraktikumGruppe1.shared.bo.User;

/**
 * Die <code>Beitrag</code>-Klasse ist eine Custom-Widget-Class die daf�r verwendet wird, 
 * um einen Beitrag im System korrekt anzuzeigen
 * 
 * @author AdamGniady
 * @version 1.0
 */

public class BeitragBox extends FlowPanel {
	PinnwandverwaltungAsync pinnwandVerwaltung = ClientsideSettings.getPinnwandverwaltung();


	private Vector<KommentarBox> kommentarsOfBeitrag = new Vector<KommentarBox>();

	private Vector<KommentarBox> kommentarBoxesOfBeitrag = new Vector<KommentarBox>();
	private Vector<Like> likes = new Vector<Like>();
	private Vector<Kommentar> kommentareOfBeitrag = new Vector<Kommentar>();

	
	// Panels for the Element
	private VerticalPanel parentVerticalPanel = new VerticalPanel();
	private FlowPanel userInfoWrapper = new FlowPanel();
	private FlowPanel creationInfoWrapper = new FlowPanel();
	private FlowPanel contentWrapper = new FlowPanel();
	private FlowPanel socialWrapper = new FlowPanel();
	private HorizontalPanel likeInfoWrapper = new HorizontalPanel();
	private HTML hrElement = new HTML("<hr/>");
	
	// Labels
	private Label accountName = new Label();
	private Label nickName = new Label();
	private Label creationDate = new Label();
	private Label likeCountText = new Label();
	private int likeCount = 0;
	
	// Paragraph Elements
	private Label beitragContent = new Label();
	
	// Images for the Buttons
	private Image likeHeart = new Image("images/SVG/heart.svg");
	private Image likeHeartBtn = new Image("images/SVG/heart.svg");
	private Image replyBtn = new Image("images/SVG/reply.svg");
	private Image editPenBtn = new Image("images/SVG/pen.svg");
	
	// Other Elements for this Widget
	private FlowPanel heartWrapper = new FlowPanel();
	private FlowPanel replyWrapper = new FlowPanel();
	private PinnwandBox parentPinnwandBox;
	
	// Creating Kommentar
	private VerticalPanel createKommentarWrapper = new VerticalPanel();
	private HTML hrElementKommentar = new HTML("<hr/>");
	private TextArea kommentarTextArea = new TextArea();
	private Button addKommentarBtn = new Button("Poste Kommentar");
	
	// Additional Information for interacting with a Beitrag
	private int beitragId;
	private int userId;
	private Timestamp timestamp;
	private User user;
	private Beitrag beitrag;
	private Like likeCheck;
	private int currentUserId;
	
	
	// Constructor for the creation of Beitrag
	public BeitragBox(String content, PinnwandBox pb, User user) {
		timestamp = new Timestamp(System.currentTimeMillis());
		this.parentPinnwandBox = pb;
		this.beitragContent.setText(content);
		this.user = user;
		
		Window.alert("Content: " + "\n" +
						this.beitragContent.getText()
						+ " \n" + " Von User: " + "\n"
						+ this.user.toString()
				);
		pinnwandVerwaltung.createBeitrag(this.beitragContent.getText(), this.user, timestamp, new CreateBeitragCallback());
	}
	
	public class CreateBeitragCallback implements AsyncCallback<Beitrag> {

		@Override
		public void onFailure(Throwable caught) {
			Window.alert("Problem with CreateBeitragCallback" + "\n"
					+ caught.toString()
					);
		}

		@Override
		public void onSuccess(Beitrag result) {
			GWT.log("beitrag Id: " + result.getBeitragId());
			beitragId = result.getBeitragId();
			userId = result.getOwnerId();
			//String ts = String.format("%1$TD %1$TT", result.getCreationTimeStamp());
			creationDate.setText("Erstellzeitpunkt: " + result.getCreationTimeStamp());
			pinnwandVerwaltung.getUserById(result.getOwnerId(), new SetNamesCallback());
		}
		
	}
	
	public class SetNamesCallback implements AsyncCallback<User> {

		@Override
		public void onFailure(Throwable caught) {
			Window.alert("Problem with SetNamesCallback");
		}

		@Override
		public void onSuccess(User result) {
			user = result;
			userId = result.getUserId();
			accountName.setText(result.getFirstName() + " " + result.getLastName());
			nickName.setText("@" + result.getNickname());
		}
		
	}
	
	
	
	public BeitragBox() {
		
	}
	
	public void onLoad() {
		currentUserId = Integer.parseInt(Cookies.getCookie("userId"));
		Beitrag thisBeitrag = new Beitrag();
		thisBeitrag.setBeitragID(beitragId);
		pinnwandVerwaltung.getAllKommentareOfBeitrag(thisBeitrag, new GetAllKommentareCallback(this));
		pinnwandVerwaltung.getUserById(this.userId, new SetNamesCallback());
		// Date
//		Date now = new Date();
//		DateTimeFormat fmt = DateTimeFormat.getFormat("HH:mm:ss, EEEE, dd MMMM, yyyy");
//		String date = fmt.format(now).toString();
		
		// Stylingelements for this Widget
		this.addStyleName("box radiusless");
		parentVerticalPanel.addStyleName("post_content");
		accountName.addStyleName("is-size-4");
		nickName.addStyleName("is-size-6");
		creationDate.addStyleName("is-size-7");
		userInfoWrapper.addStyleName("grid_box content_margin");
		creationInfoWrapper.addStyleName("content_margin");
		contentWrapper.addStyleName("content_margin");
		
		// Social Wrapper
		socialWrapper.addStyleName("grid_box_links");
		likeInfoWrapper.addStyleName("grid_box");
		
		editPenBtn.addStyleName("grid_box_element");
		editPenBtn.addClickHandler(new EditBeitragBoxClickHandler(this));
		editPenBtn.getElement().setPropertyString("style", "max-width: 25px;");
		
		// Social Wrapper
		heartWrapper.addStyleName("grid_box_element");
		replyWrapper.addStyleName("grid_box_element");
		likeHeartBtn.getElement().setPropertyString("style", "max-width: 25px;");
		replyBtn.getElement().setPropertyString("style", "max-width: 25px;");
		replyBtn.addClickHandler(new showKommentarWrapperClickHandler());
		
		heartWrapper.add(likeHeartBtn);
		replyWrapper.add(replyBtn);
		
		// ClickHandler Call for Like Action
		likeHeartBtn.addClickHandler(new LikeCountClickHandler(this));
		socialWrapper.add(heartWrapper);
		socialWrapper.add(replyWrapper);
//		creationDate.setText("Erstellungszeitpunkt: " + date);
		
		// Likecount info
		pinnwandVerwaltung.countLikes(thisBeitrag, new CountLikeCallback());
		likeHeart.setWidth("1rem");
		likeHeart.addStyleName("small-padding-right");
		likeCountText.addStyleName("is-size-6 is-italic");
		likeCountText.setText(" auf diesem Beitrag: " + likeCount);
		
		
		// Adding Elements to the Wrapper
		likeInfoWrapper.add(likeHeart);
		likeInfoWrapper.add(likeCountText);
		
		// Here we can create a Kommentar
		createKommentarWrapper.setWidth("100%");
		createKommentarWrapper.addStyleName("post_content");
		kommentarTextArea.getElement().setPropertyString("placeholder", "Erstelle hier deinen Kommentar!");
		kommentarTextArea.setWidth("100%");
		kommentarTextArea.addStyleName("textarea content_margin control");
		addKommentarBtn.addStyleName("button bg-primary");
		addKommentarBtn.addClickHandler(new addKommentarClickHandler());
		
		// Adding Elements to KommentarParent
		createKommentarWrapper.add(hrElementKommentar);
		createKommentarWrapper.add(kommentarTextArea);
		createKommentarWrapper.add(addKommentarBtn);
		createKommentarWrapper.setVisible(false);
		
		// Add Elements to Wrapper
		userInfoWrapper.add(accountName);
		userInfoWrapper.add(nickName);
		if(this.userId == this.currentUserId) {
			userInfoWrapper.add(editPenBtn);
		}
		
	
		
		creationInfoWrapper.add(creationDate);
		contentWrapper.add(beitragContent);
		
		// Add Wrappers to Element
		this.add(userInfoWrapper);
		this.add(creationInfoWrapper);
		this.add(contentWrapper);
		this.add(likeInfoWrapper);
		this.add(hrElement);
		this.add(socialWrapper);
		this.add(createKommentarWrapper);
		
		// Loading
		pinnwandVerwaltung.getUserById(userId, new GetUserByIdCallback());
		
		this.beitrag = new Beitrag();
		beitrag.setBeitragId(beitragId);
		pinnwandVerwaltung.likeCheck(user, beitrag, new LikeCheckCallback());
	}
	
	private class LikeCheckCallback implements AsyncCallback<Like> {

		@Override
		public void onFailure(Throwable caught) {
			GWT.log(caught.toString());
		}

		@Override
		public void onSuccess(Like result) {
			if(result != null ) {
				likeCheck = result;
				GWT.log(likeCheck.toString() + " ist der LikeCheck");
			} else {
				GWT.log("No Likes");
			}
			
		}
		
	}
	
	private class GetUserByIdCallback implements AsyncCallback<User> {

		@Override
		public void onFailure(Throwable caught) {
		}

		@Override
		public void onSuccess(User result) {
			user = result;
		}
		
	}
	
	public class CountLikeCallback implements AsyncCallback<Integer> {
//		Beitrag currentBeitrag = new Beitrag();
		
		@Override
		public void onFailure(Throwable caught) {
			Window.alert("Problem with CountLikeCallback");
			
		}

		@Override
		public void onSuccess(Integer result) {
			//likeCount = result;
			GWT.log("LikeCount is: " + result);
//			likeCount = likeCount + result;
			likeCountText.setText(" auf diesem Beitrag: " + result);

			}
			
		
		
	}
	
	
	/**
	 * Die innere Klasse <code>LikeCountClickHandler</code> implementiert das Clickhandler 
	 * Interface und dessen dazugehörige <code>onClick(ClickEvent event)</code> Methode.
	 * Diese Methode arbeitet mit der Anzahl der Likes auf einem Beitrag und passt die Anzahl auf den 
	 * dazugehörigen Beitrag an. 
	 * @author Adam Gniady
	 *
	 */
	private class LikeCountClickHandler implements ClickHandler {
		private BeitragBox parentBB;
		private Beitrag parentBeitrag = new Beitrag();
		private User likingUser = new User();
		
		/*
		 * @param bb
		 */
		public LikeCountClickHandler(BeitragBox bb) {
			parentBB = bb;
			this.parentBeitrag.setBeitragId(parentBB.beitragId);
			// COOKIE
			this.likingUser.setUserId(1);
			
		}
			
		@Override
		public void onClick(ClickEvent event) {			
			pinnwandVerwaltung.likeCheck(likingUser, parentBeitrag , new IsLikedCallback());
			
		}
		
		public class IsLikedCallback implements AsyncCallback<Like> {
			
			Timestamp timestamp = new Timestamp(beitragId);
			User currentUser = new User();
			Beitrag currentBeitrag = new Beitrag();
			
			
			@Override
			public void onFailure(Throwable caught) {
				Window.alert(caught.toString());
			}

			
			@Override
			public void onSuccess(Like result) {
				if(result != null) {
					currentUser.setUserId(1);
					currentBeitrag.setBeitragId(beitragId);
					GWT.log(result.toString());
					pinnwandVerwaltung.deleteLike(result, new DeleteLikeCallback());
				}else {
					currentUser.setUserId(1);
					currentBeitrag.setBeitragId(beitragId);
					pinnwandVerwaltung.createLike(currentUser, currentBeitrag, timestamp, new CreateLikeCallback());
				}
				
				

				
			}
			
		
		
		public class CreateLikeCallback implements AsyncCallback<Like>{

			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Problem with CreateLikeCallback");
				
			}

			@Override
			public void onSuccess(Like result) {
				Window.alert("Beitrag wurde geliked");	
				pinnwandVerwaltung.countLikes(currentBeitrag, new CountLikeCallback());
				}
				
			}
			
		}
			
		public class DeleteLikeCallback implements AsyncCallback<Boolean> {
			
			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Problem with DeleteLikeCallback");
				
			}

			@Override
			public void onSuccess(Boolean result) {
				if(result == true) {
					Window.alert("Like wurde entfernt");
					Beitrag currentBeitrag = new Beitrag();
					currentBeitrag.setBeitragId(beitragId);
					pinnwandVerwaltung.countLikes(currentBeitrag, new CountLikeCallback());
				}else {
					Window.alert("Like wurde nicht entfernt");
				}
				
		}
				
		}
			
	}
		
	
	
	/**
	 * Die innere Klasse <code>EditBeitragBoxClickHandler</code> ist zuständig für die Editierbarkeit
	 * der BeitragBox. 
	 *  
	 * @author AdamGniady
	 */
	private class EditBeitragBoxClickHandler implements ClickHandler {
		private BeitragBox parentBB;
		
		/*
		 * @param bb
		 */
		public EditBeitragBoxClickHandler(BeitragBox bb) {
			parentBB = bb;
		}
		
		public void onClick(ClickEvent event) {
			EditBeitragDialogBox dlg = new EditBeitragDialogBox(parentBB);
			dlg.center();
		}
	}
	
	/**
	 * Die innere Klasse <code>EditBeitragDialogBox</code> implementiert das Clickhandler 
	 * Interface und dessen dazugehörige <code>onClick(ClickEvent event)</code> Methode.
	 * Diese Methode ist dafür zuständig die Editierung eines Beitrags zu erm�glichen.
	 * @author AdamGniady
	 *
	 */
	private class EditBeitragDialogBox extends DialogBox implements ClickHandler {
		BeitragBox parentBB;
		
		public EditBeitragDialogBox(BeitragBox bb) {
			parentBB = bb;
			
			setText("Editiere deinen Beitrag");

			Button safeButton = new Button("Speichere den Edit", this);
			safeButton.addStyleName("button bg-primary");
			Image cancelImage = new Image("images/SVG/timesCircle.png");
			cancelImage.getElement().setPropertyString("style", "max-width: 25px;");
			cancelImage.addClickHandler(this);
			
			// Creating TextArea and filling it with the content of the "Beitrag".
			String beitragText = parentBB.beitragContent.getText();
			TextArea beitragTextArea = new TextArea();
			beitragTextArea.getElement().setPropertyString("style", "min-width: 590px;");
			beitragTextArea.setText(beitragText);
			HTML msg = new HTML("Hier kannst du deinen Text editieren",true);
			
			// Create the Button to make Beitrag deletable
			Button deleteBtn = new Button("Delete");
			deleteBtn.addStyleName("button is-danger");
			deleteBtn.addClickHandler(new removeBeitragFromParent(parentBB, this));

			DockPanel dock = new DockPanel();
			dock.setSpacing(6);
			dock.add(beitragTextArea, DockPanel.CENTER);
			dock.add(safeButton, DockPanel.SOUTH);
			dock.add(cancelImage, DockPanel.EAST);
			dock.add(deleteBtn, DockPanel.EAST);
			dock.add(msg, DockPanel.NORTH);
			
			safeButton.addClickHandler(new SafeEditedContentClickHandler(parentBB, beitragTextArea));

			dock.setCellHorizontalAlignment(safeButton, DockPanel.ALIGN_CENTER);
			dock.setWidth("600px");
			setWidget(dock);
		}
		
		/**
		 * Die Methode HideElement() wird nur dann genutzt um von außen auf die <code>hide()</code>-Methode
		 * der DialogBox zuzugreifen. 
		 * @author AdamGniady
		 */
		public void hideElement() {
			hide();
		}

		@Override
		public void onClick(ClickEvent event) {
			hide();
		}
		
		/**
		 * Die innere Klasse <code>SafeEditedContentClickHandler</code> implementiert das ClickHandler-Interface
		 * und ermöglicht es die Änderungen die der User eingegeben hat auch dauerhaft zu speichern.  
		 * @author AdamGniady
		 *
		 */
		private class SafeEditedContentClickHandler implements ClickHandler {
			BeitragBox parentBB;
			TextArea newContent;
			
			/*
			 * @param bb
			 * @param textArea
			 */
			public SafeEditedContentClickHandler(BeitragBox bb, TextArea textArea) {
				parentBB = bb;
				newContent = textArea;
			}
			
			@Override
			public void onClick(ClickEvent event) {
				GWT.log(newContent.getValue());
				Beitrag tempBeitrag = new Beitrag();
				tempBeitrag.setBeitragID(beitragId);
				tempBeitrag.setInhalt(newContent.getValue());
				pinnwandVerwaltung.editBeitrag(tempBeitrag, new EditBeitragCallback());
				parentBB.beitragContent.setText(newContent.getValue());
			}
		}
	}
	
	/**
	 * Private Klasse, die das AsyncCallback-Interface implementiert und so die 
	 * Möglichkeit bietet die Editierung eines Beitrages zu ermöglichen. 
	 * @author AdamGniady
	 *
	 */
	private class EditBeitragCallback implements AsyncCallback<Beitrag> {

		@Override
		public void onFailure(Throwable caught) {
			Window.alert("Problems with the EditBeitragCallback");
		}

		@Override
		public void onSuccess(Beitrag result) {
			
		}
		
	}
	
	/**
	 * 
	 * 
	 * @author AdamGniady
	 *
	 */
	private class showKommentarWrapperClickHandler implements ClickHandler {

		@Override
		public void onClick(ClickEvent event) {
			createKommentarWrapper.setVisible(true);
		}
		
	}
	
	private class addKommentarClickHandler implements ClickHandler {

		@Override
		public void onClick(ClickEvent event) {
			createKommentarWrapper.setVisible(false);
			String kommentarContent = kommentarTextArea.getValue();
			KommentarBox tempKB = createKommentar(kommentarContent);
			GWT.log(kommentarBoxesOfBeitrag.toString());
			kommentarTextArea.setText("");
		}
		
	}
	
	/**
	 * Die Methode <code>createKommentar</code> erstellt einen Kommentar mit dem eingegebenen
	 * Inhalt den Kommentar erstellt und an die <code>BeitragBox</code> anhängt. 
	 * 
	 * @param commentarContent
	 * @return newKommentarBox an der BeitragBox
	 */
	private KommentarBox createKommentar(String commentarContent) {
		KommentarBox newKommentarBox = new KommentarBox(commentarContent, this);
		kommentarBoxesOfBeitrag.addElement(newKommentarBox);
		this.add(kommentarBoxesOfBeitrag.lastElement());
		
		return newKommentarBox; 
	}
	
	/**
	 * This Method enables the User to delete KommentarBox-Elements from the
	 * <code>kommentarsOfBeitrag</code> Vector. It gets its parameters from 
	 * the KommentarBox and its <code>ClickHandler</code> called <code>removeFromParent</code>.
	 * 
	 * @param deletableKB passed by the ClickHandler of the KommentarBox Class.
	 * @author AdamGniady
	 */
	public void deleteKommentar(KommentarBox deletableKB) {
		deletableKB.removeFromParent();
		
		kommentarBoxesOfBeitrag.removeElement(deletableKB);
	}
	
	/**
	 * This Method calls a the <code>deleteBeitrag</code> Method in the 
	 * parent PinnwandBox. 
	 * 
	 * @author AdamGniady
	 */
	private class removeBeitragFromParent implements ClickHandler {
		BeitragBox thisBeitragBox;
		EditBeitragDialogBox parentDialogBox;
		
		
		/*
		 * @param thisBB
	     * @param beitragDialogBox
	     * 
		 */
		public removeBeitragFromParent(BeitragBox thisBB, EditBeitragDialogBox beitragDialogBox) {
			thisBeitragBox = thisBB;
			this.parentDialogBox = beitragDialogBox;
		}
		
		@Override
		public void onClick(ClickEvent event) {
			Beitrag tempBeitrag = new Beitrag();
			tempBeitrag.setBeitragID(beitragId);
			pinnwandVerwaltung.deleteBeitrag(tempBeitrag, new DeleteBeitragCallback());
			parentPinnwandBox.deleteBeitrag(thisBeitragBox);
			parentDialogBox.hideElement();
			parentDialogBox.hide();
		}
		
	}
	
	private class DeleteBeitragCallback implements AsyncCallback<Void> {

		@Override
		public void onFailure(Throwable caught) {
			Window.alert("Problems with DeleteBeitragCallback");
		}

		@Override
		public void onSuccess(Void result) {
			//Window.alert("Beitrag wurde deleted");
		}
		
	}

	/*
	 * Methode die den User anhand der UserId holt
	 */
	public int getUserId() {
		return userId;
	}

	/*
	 * Methode die den User anhand der UserId setzt
	 * @param userId
	 */
	public void setUserId(int userId) {
		this.userId = userId;
	}
	
	/*
	 * Methode die den accountName holt
	 * @return accountName
	 */
	public Label getAccountName() {
		return accountName;
	}
	
	/*
	 * Methode die den AccountName setzt
	 * @param firstName
	 * @param lastName
	 * 
	 */
	 public void setAccountName(String firstName, String lastName) {
		this.accountName.setText(firstName + " " + lastName);
	}

	 /*
	  * Methode die das Erstellungsdatum holt
	  * @return creationDate
	  */
	public Label getCreationDate() {
		return creationDate;
	}

	/*
	 * Methode die das Erstellungsdatum für den Text setzt
	 * @param creationDate
	 */
	public void setCreationDate(String creationDate) {
		this.creationDate.setText(creationDate);;
	}
	/*
	 * Methode die den Content(Inhalt) des Beitrags holt
	 * @return beitragContent
	 */
	public Label getBeitragContent() {
		return beitragContent;
	}
	
	/*
	 * Methode die den Content(Inhalt) setzt
	 * @param beitragContent
	 */
	public void setBeitragContent(String beitragContent) {
		this.beitragContent.setText(beitragContent);
	}

	/*
	 * Methode die die Id des Beitrags holt
	 * @return beitragId
	 */
	public int getBeitragId() {
		return beitragId;
	}

	/*
	 * Methode die die Dd des Beitrags setzt
	 */
	public void setBeitragId(int beitragId) {
		this.beitragId = beitragId;
	}
	
	/*
	 * Methode die den NickNamen setzt
	 * @param nickName
	 */
	public void setNickName(String nickName) {
		this.nickName.setText(nickName);
	}
	
	private class GetAllKommentareCallback implements AsyncCallback<Vector<Kommentar>> {
		private BeitragBox pB;
		
		public GetAllKommentareCallback(BeitragBox pB) {
			this.pB = pB;
		}

		@Override
		public void onFailure(Throwable caught) {
			Window.alert("Problem with GetAllKommentareCallback");
		}

		@Override
		public void onSuccess(Vector<Kommentar> result) {
			kommentareOfBeitrag = result;
			GWT.log(kommentareOfBeitrag.toString());
			showOldKommentare();
		}
		
	}
	
	private void showOldKommentare() {
		for (Kommentar k : this.kommentareOfBeitrag) {
			KommentarBox tempKommentarBox = new KommentarBox();
			
			tempKommentarBox.setKommentarContent(k.getInhalt());
			tempKommentarBox.setParentBeitragBox(this);
			tempKommentarBox.setKommentarId(k.getKommentarId());
			tempKommentarBox.setOwnerId(k.getOwnerId());
			
			this.add(tempKommentarBox);
		}
	}
	
	
	
}
