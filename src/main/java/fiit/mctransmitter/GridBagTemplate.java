package fiit.mctransmitter;

import java.awt.*;

/**
 * Táto trieda je odvodená od triedy GridBagConstraints. Trieda definuje vlastné konštruktory, ktoré efektívne
 * nastavujú atribúty odvodenej triedy na požiadavky ostatných tried. Cieľom je nastavenie umiestenia komponentu
 * na príslušnom paneli pri použití layout-u GridBagLayout.
 * 
 * @author 		Jaroslav Tóth
 * @version 	1.2
 * @since 		1.0
 */
@SuppressWarnings("serial")
public class GridBagTemplate extends GridBagConstraints {

	/**
	 * Konštruktor špecifikuje umiestnenie nejakého komponentu v sieti buniek.
	 * @param anchor	Zarovnanie komponentu v bunke (int).
	 * @param gridX		X-ová špecifikácia bunky, kde má byť komponent umiestnený (int).
	 * @param gridY		Y-ová špecifikácia bunky, kde má byť komponent umiestnený (int).
	 * @param top		Horné odsadenie komponentu (počet pixelov - int).
	 * @param left		Ľavé odsadenie komponentu (počet pixelov - int).
	 * @param bottom	Spodné odsadenie komponentu (počet pixelov - int).
	 * @param right		Pravé odsadenie komponentu (počet pixelov - int).
	 */
	public GridBagTemplate(int anchor, int gridX, int gridY, int top, int left,
			int bottom, int right) {
		super();
		this.anchor = anchor;
		this.gridx = gridY;
		this.gridy = gridX;
		this.fill = GridBagConstraints.BOTH;
		this.insets = new Insets(top, left, bottom, right);
	}

	/**
	 * Konštruktor špecifikuje umiestnenie nejakého komponentu v sieti buniek aj s rozsahmi buniek, ktoré môže
	 * prvok pokryť.
	 * @param anchor		Zarovnanie komponentu v bunke (int).
	 * @param gridX			X-ová špecifikácia bunky, kde má byť komponent umiestnený (int).
	 * @param gridY			Y-ová špecifikácia bunky, kde má byť komponent umiestnený (int).
	 * @param top			Horné odsadenie komponentu (počet pixelov - int).
	 * @param left			Ľavé odsadenie komponentu (počet pixelov - int).
	 * @param bottom		Spodné odsadenie komponentu (počet pixelov - int).
	 * @param right			Pravé odsadenie komponentu (počet pixelov - int).
	 * @param gridWidth		Agregácia pokrytia buniek po šírke (počet buniek - int).
	 * @param weightY		Priorita komponentu v bunke po vertikálnej osi (reálna hodnota - double).
	 */
	public GridBagTemplate(int anchor, int gridX, int gridY, int top, int left,
			int bottom, int right, int gridWidth, double weightY) {
		super();
		this.anchor = anchor;
		this.gridx = gridY;
		this.gridy = gridX;
		this.fill = GridBagConstraints.BOTH;
		this.insets = new Insets(top, left, bottom, right);
		this.gridwidth = gridWidth;
		this.weighty = weightY;
	}
	
	/**
	 * Konštruktor špecifikuje umiestnenie nejakého komponentu v sieti buniek aj s rozsahmi buniek, ktoré môže
	 * prvok pokryť.
	 * @param anchor		Zarovnanie komponentu v bunke (int).
	 * @param gridX			X-ová špecifikácia bunky, kde má byť komponent umiestnený (int).
	 * @param gridY			Y-ová špecifikácia bunky, kde má byť komponent umiestnený (int).
	 * @param top			Horné odsadenie komponentu (počet pixelov - int).
	 * @param left			Ľavé odsadenie komponentu (počet pixelov - int).
	 * @param bottom		Spodné odsadenie komponentu (počet pixelov - int).
	 * @param right			Pravé odsadenie komponentu (počet pixelov - int).
	 * @param gridWidth		Agregácia pokrytia buniek po šírke (počet buniek - int).
	 */
	public GridBagTemplate(int anchor, int gridX, int gridY, int top, int left,
			int bottom, int right, int gridWidth) {
		super();
		this.anchor = anchor;
		this.gridx = gridY;
		this.gridy = gridX;
		this.fill = GridBagConstraints.BOTH;
		this.insets = new Insets(top, left, bottom, right);
		this.gridwidth = gridWidth;
	}
	
}
