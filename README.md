# MarkingMenu
Un menu circulaire avec un mode d'apprentissage en JAVA.

Afin de créer un menu il faut créer une classe qui hérite du Menu, voici un exemple de menu (NewIcon est une action qui herite de commands.Action) :

```
package markingMenu;

import mainPackage.Icon;
import mainPackage.Explorer;
import commands.Action;
import commands.ActionList;
import commands.icons.NewIcon;

@SuppressWarnings("serial")
public class DesktopMenu extends Menu {
	public DesktopMenu(Explorer pane, int x, int y, ActionList actions) {
		super(pane, x, y, actions);
	}
	public DesktopMenu(Explorer pane, int x, int y, ActionList actions, boolean visibleButtons) {
		super(pane, x, y, actions, visibleButtons);
	}

	@Override
	public void buildMenu() throws exceptions.TooMuchSubMenuException {
		SubMenu nouveau = new SubMenu("Nouveau", this);
		nouveau.add(new NewIcon(new Icon(((Explorer)Menu.getPane()).getFolder()+"/Nouveau fichier", true), Menu.getPane()));
		nouveau.add(new NewIcon(new Icon(((Explorer)Menu.getPane()).getFolder()+"/Nouveau dossier", false), Menu.getPane()));
		root.add(nouveau);
		root.add(new Action("Selection") {
			@Override
			public void doAction() {
				//changer le mode de selection du bureau
			}
			@Override
			public void undoAction() {
			}
		});
	}
}
```

Vous n'avez plus qu'à implementer vos ActionListener avec un add du menu sur votre JPanel.
