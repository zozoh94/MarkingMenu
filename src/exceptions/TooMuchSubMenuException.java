package exceptions;

import markingMenu.SubMenu;

@SuppressWarnings("serial")
public class TooMuchSubMenuException extends Exception {
	public TooMuchSubMenuException(){
		super("Il y a deja "+SubMenu.childsLimit+" sous-menu.");
	}
}
