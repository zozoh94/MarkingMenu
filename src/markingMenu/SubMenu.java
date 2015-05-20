package markingMenu;

import commands.Action;

import java.util.ArrayList;

import exceptions.TooMuchSubMenuException;

@SuppressWarnings("serial")
public class SubMenu extends Action {

	private ArrayList<Action> childs;
	private Menu rootMenu;
	private Menu parent;
	private Menu menu;
	private double angleButton;
	public static final int childsLimit = 10;
	
	public SubMenu(String nom, Menu root) {
		super(nom);
		this.rootMenu = root;
		this.parent = root;
		childs = new ArrayList<>();
		menu=null;
		angleButton = 0;
	}
	
	private SubMenu(Menu root) {
		this("root", root);
	}
	
	@Override
	public void doAction() {
		SubMenu root = new SubMenu(rootMenu);
		root.setChilds(childs);
		int dx = (int)(Menu.rayon*1*Math.cos(angleButton));
		int dy = (int)(Menu.rayon*1*Math.sin(angleButton));
		menu = new Menu((int)(rootMenu.getPositionX()+dx), (int)(rootMenu.getPositionY()+dy), parent, root, Math.PI/childs.size());
		root.parent = menu;
		Menu.getPane().add(menu);
		Menu.getPane().repaint();
		parent.deleteButtonsToFrame();
		Menu.getPane().repaint();
	}

	@Override
	public void undoAction() {
		Menu.getPane().remove(menu);
	}

	public Action getChild(int index) {
		return childs.get(index);
	}

	public void add(Action child) throws TooMuchSubMenuException {
		if(getChildCount() == childsLimit)
			throw new TooMuchSubMenuException();
		else
			childs.add(child);
	}
	
	public int getChildCount() {
		return childs.size();
	}
	
	private void setChilds(ArrayList<Action> childs) {
		this.childs = childs;
	}

	/**
	 * @return the angleButton
	 */
	public double getAngleButton() {
		return angleButton;
	}

	/**
	 * @param angleButton the angleButton to set
	 */
	public void setAngleButton(double angleButton) {
		this.angleButton = angleButton;
	}
	
}
