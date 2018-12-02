package de.hdm.softwarePraktikumGruppe1.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.RootPanel;

import de.hdm.softwarePraktikumGruppe1.client.gui.BeitragBox;
import de.hdm.softwarePraktikumGruppe1.client.gui.CreateBeitragBox;
import de.hdm.softwarePraktikumGruppe1.client.gui.Header;
import de.hdm.softwarePraktikumGruppe1.client.gui.KommentarBox;
import de.hdm.softwarePraktikumGruppe1.client.gui.PinnwandBox;
import de.hdm.softwarePraktikumGruppe1.client.gui.ProfileBox;

/**
 * Die Klasse <code>SWP_WS1819_Gruppe1</code> enth�lt alle Elemente zur Darstellung des
 * Dashboards f�r das KontaktSystem bzw. den Systemteil des <em>Editor</em>.
 * Dieses ist in mehrere Bereiche aufgeteilt.
 * Dabei ist es aufgeteilt in einen Header-Bereich und einen ein- bis zweiteiligen 
 * Dashboardteil.  
 *@author Adam Gniady
 */
public class SWP_WS1819_Gruppe1 implements EntryPoint {
	public void onModuleLoad() {
		// RootPanels
		RootPanel rootPanelHeader = RootPanel.get("header");
		RootPanel rootPanelContainer = RootPanel.get("container");
		
		FlowPanel linkeSeite = new FlowPanel();
		PinnwandBox rechteSeite = new PinnwandBox();
		
		FlowPanel ganzeSeite = new FlowPanel();
		FlowPanel mittlereVier = new FlowPanel();
		
		linkeSeite.addStyleName("linkeSeite");
		ganzeSeite.addStyleName("ganzeSeite");
		mittlereVier.addStyleName("mittlereVier");
		
		// GUI Elements
		Header h1 = new Header();
		ProfileBox pB = new ProfileBox();
		
		linkeSeite.add(pB);
		
		rootPanelHeader.add(h1);
		rootPanelContainer.add(linkeSeite);
		rootPanelContainer.add(rechteSeite);
	}
}
