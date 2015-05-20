/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package commands;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;

import markingMenu.Menu;
import markingMenu.SubMenu;

/**
 *
 * @author clement
 */
@SuppressWarnings("serial")
public abstract class Action extends AbstractAction {
	private String nom;
	
	public Action(String nom) {
		this.nom = nom;
	}
	public String toString() {
		return nom;
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() instanceof JButton) {
			JButton source = (JButton)(e.getSource());
			if(source.getAction() instanceof SubMenu == false){
				Menu.remove();
				Menu.getPane().repaint();
			}
			//Sinon on peut ajouter l'action au stack d'Undo/Redo
		}
		doAction();
	}
    public abstract void doAction();
    public abstract void undoAction();
}
