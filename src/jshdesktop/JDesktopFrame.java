package jshdesktop;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.GraphicsEnvironment;

import javax.swing.ImageIcon;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JLabel;

import jshdesktop.desktop.DecoratedDesktopPane;
import jshdesktop.desktop.menu.DesktopMenuBar;
import jshdesktop.events.InitCompletionEvent;
import terra.shell.logging.LogManager;
import terra.shell.logging.Logger;
import terra.shell.utils.system.EventManager;

public class JDesktopFrame extends JFrame {
	private Logger log = LogManager.getLogger("DesktopFrame");
	private DecoratedDesktopPane desktopPane;
	private DesktopMenuBar menuBar;
	private JFrame splashFrame;

	public JDesktopFrame(JFrame splashFrame) {
		log.debug("Creating JDesktopFrame top level...");
		this.splashFrame = splashFrame;
		desktopPane = new DecoratedDesktopPane();
		this.setUndecorated(true);
		super.add(desktopPane);
		setSize(GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().width,
				GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().height);
		menuBar = desktopPane.getMenuBar();
		super.add(menuBar, BorderLayout.SOUTH);
		if (splashFrame != null)
			splashFrame.setVisible(false);
		splashFrame = null;
		setVisible(true);
		EventManager.invokeEvent(new InitCompletionEvent(null));
	}

	@Override
	public Component add(Component c) {
		if (c.getCursor().getType() == Cursor.DEFAULT_CURSOR)
			c.setCursor(desktopPane.getCursor());
		return desktopPane.add(c);
	}

	public JDesktopPane getDesktopPane() {
		return desktopPane;
	}

	public void toggleMenuBar() {
		if (menuBar.isVisible())
			menuBar.setVisible(false);
		else
			menuBar.setVisible(true);
	}
}
