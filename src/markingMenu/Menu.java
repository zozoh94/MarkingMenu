package markingMenu;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JButton;
import javax.swing.JPanel;

import commands.Action;
import exceptions.TooMuchSubMenuException;

@SuppressWarnings("serial")
public class Menu extends JPanel {
	protected SubMenu root;
	protected Menu parent;
	private int positionX;
	private int positionY;
	public static final int rayon = 150;
	private static JPanel pane;
	private static ArrayList<Menu> menus = new ArrayList<>();
	private static double startAngle = 0;
	private ArrayList<JButton> buttons;
	private static boolean visibleButtons;
	
	private double angleAv;
	private double angleSum;
	private Point lastPoint;
	private int nbrCaptureAngle;
	private int nbrCapturePoint;
	private static final int intervalCapturePoint = 2;
	private ArrayList<Double> dragAngles;
	
	public Menu(JPanel pane, int x, int y) {
		this(pane, x, y, true, true);
	}
	
	public Menu(JPanel pane, int x, int y, boolean visibleButtons) {
		this(pane ,x ,y, visibleButtons, true);
	}
	
	public Menu(JPanel pane, int x, int y, boolean visibleButtons, boolean buildMenu) {
		super();
		positionX = x;
		positionY = y;
		setLayout(null);
		setSize(pane.getSize());
		parent = null;
		Menu.visibleButtons = visibleButtons;
		Menu.pane = pane;
		if(visibleButtons==true) {
			menus.add(this);
		}
		buttons = new ArrayList<>();
		root = new SubMenu("root", this);
		if(buildMenu) {
			try {
				buildMenu();
			}
			catch(TooMuchSubMenuException e) {
				System.out.println(e.getMessage());
			}
			init();
			repaint();
		}
		
		lastPoint = null;
		angleAv = 0;
		angleSum = 0;
		nbrCaptureAngle = 0;
		nbrCapturePoint = 0;
		dragAngles = new ArrayList<>();
	}
	
	public Menu(int x, int y, Menu parent, SubMenu root, double startAngle) {
		this(pane, x, y);
		this.parent = parent;
		this.root = root;
		Menu.startAngle += startAngle;
		init();
		repaint();
	}
	
	public void init() {
		Action action;
		JButton button;
		double availableAngle = 2*Math.PI;
		boolean blocage = false;
		Rectangle bounds;
		bounds = pane.getBounds();
		//Blocage à gauche
		if((positionX-rayon-20-100) < bounds.x) {
			availableAngle /= 2;
			startAngle = -Math.PI/2+Math.PI/6;
			availableAngle -= 2*Math.PI/6;
			blocage = true;
		}
		//à droite
		if((positionX+rayon+20) > bounds.x+bounds.width) {
			availableAngle /= 2;
			startAngle = Math.PI/2+Math.PI/6;
			availableAngle -= 2*Math.PI/6;
			blocage = true;
		}
		//en haut
		if((positionY-rayon-50) < bounds.y) {	
			availableAngle /= 2;
			startAngle = Math.PI/6;
			//à droite
			if((positionX+rayon+20) > bounds.x+bounds.width)
				startAngle = Math.PI/2;
			else if(!((positionX-rayon-20) < bounds.x))
				availableAngle -= 2*Math.PI/6;
			blocage = true;
		}
		//en bas
		if((positionY+rayon+50) > bounds.y+bounds.height) {
			availableAngle /= 2;
			startAngle = -Math.PI+Math.PI/6;
			//à droite
			if((positionX+rayon+20) > bounds.x+bounds.width)
				startAngle = -Math.PI;
			else if(((positionX-rayon-20) < bounds.x))
				startAngle = -Math.PI/2;
			else if(!((positionX-rayon-20) < bounds.x))
				availableAngle -= 2*Math.PI/6;
			blocage = true;
		}
		double dAngle = availableAngle/root.getChildCount();
		for(int i=0; i<root.getChildCount(); i++) {
			double angle = dAngle*i+startAngle;
			action = root.getChild(i);
			int dx = (int)(rayon*Math.cos(angle));
			int dy = (int)(rayon*Math.sin(angle));
			int width = action.toString().length()*getFont().getSize()+20;
			int height = 50;
			button = new JButton(action);
			if(action instanceof SubMenu)
				((SubMenu) action).setAngleButton(angle);
			button.setBounds(positionX+dx-(width/2)+(int)((height/2)*(Math.cos(angle))), positionY+dy-(height/2)+(int)((height/2)*Math.sin(angle)), width, height);
			button.setVisible(true);	
			button.setSize(new Dimension(width, height));
			button.setText(action.toString());
			buttons.add(button);
		}
		if(visibleButtons) {
			addButtonsToFrame();
		}
		if(blocage) {
			startAngle = 0;
		}
	}
	
	@Override
	public void paintComponent(Graphics g){  
		g.fillOval(positionX-15, positionY-15, 30, 30);
		if(parent != null) {
			g.drawLine(parent.getPositionX(), parent.getPositionY(), positionX, positionY);
		}
	}   
	
	/**
	 * @return the positionX
	 */
	public int getPositionX() {
		return positionX;
	}

	/**
	 * @param positionX the positionX to set
	 */
	public void setPositionX(int positionX) {
		this.positionX = positionX;
	}

	/**
	 * @return the positionY
	 */
	public int getPositionY() {
		return positionY;
	}

	/**
	 * @param positionY the positionY to set
	 */
	public void setPositionY(int positionY) {
		this.positionY = positionY;
	}

	/**
	 * @return the pane
	 */
	public static JPanel getPane() {
		return pane;
	}
	
	public static void remove() {
		Iterator<Menu> it = menus.iterator();
		Menu menu;
		while(it.hasNext()) {
			menu = it.next();
			Menu.getPane().remove(menu);
		}
		menus = new ArrayList<>();
		Menu.startAngle = 0;
	}
	
	public static int getNbrMenus() {
		return menus.size();
	}
	
	public void addButtonsToFrame() {
		Iterator<JButton> it = buttons.iterator();
		while(it.hasNext()) {
			JButton button = it.next();
			add(button);
			setComponentZOrder(button, 0);
		}
	}
	
	public void deleteButtonsToFrame() {
		Iterator<JButton> it = buttons.iterator();
		while(it.hasNext()) {
			JButton button = it.next();
			remove(button);
		}
	}
	
	public void buildMenu() throws exceptions.TooMuchSubMenuException {
		
	}
	
	public void addPointMove(Point point) {
		nbrCapturePoint++;
		double precision = 3;
		if(nbrCapturePoint % intervalCapturePoint == 0) {
			if(lastPoint!=null) {
				double angle = Math.atan2((lastPoint.getY()-point.getY()),(lastPoint.getX()-point.getX()));
				if(Math.abs(angleAv - angle) > precision*Math.PI/SubMenu.childsLimit && nbrCaptureAngle > 1) {
					dragAngles.add(angleAv);
					angleSum = 0;
					angleAv = 0;
					nbrCaptureAngle = 0;
				}
				angleSum += angle;
				nbrCaptureAngle++;
				angleAv = angleSum /  nbrCaptureAngle;
			}
			lastPoint = point;
		}
	}
	
	public void executeDragAction() {
		dragAngles.add(angleAv);
		Iterator<Double> it = dragAngles.iterator();
		SubMenu subMenu = root;
		double angle;
		double startAngleMenu = 0;
		double dAngle;
		boolean find;
		boolean act = false;
		while(it.hasNext() && !act) {
			dAngle = 2*Math.PI/subMenu.getChildCount();
			angle = it.next()+Math.PI;
			find = false;
			int i=0;
			while(i<subMenu.getChildCount() && !find) {
				double angleChild = dAngle*i+startAngleMenu;
				if(angleChild<-Math.PI)
					angleChild+=2*Math.PI;
				if(angleChild>Math.PI)
					angleChild-=2*Math.PI;
				double angleInf = (angleChild - (dAngle/2));
				double angleSup = (angleChild + (dAngle/2));
				
				if((angle > angleInf && angle < angleSup) || (angle > angleInf+2*Math.PI && angle < angleSup+2*Math.PI)) {
					if(subMenu.getChild(i) instanceof SubMenu) {
						subMenu = (SubMenu)subMenu.getChild(i);
						startAngleMenu += Math.PI/subMenu.getChildCount();	
						find = true;
					}
					else {
						subMenu.getChild(i).doAction();
						find = true;
						act = true;
					}
				}
				i++;
			}
		}
	}
}
