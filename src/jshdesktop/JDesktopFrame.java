package jshdesktop;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.GraphicsEnvironment;

import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;

import jshdesktop.desktop.DecoratedDesktopPane;
import jshdesktop.desktop.menu.DesktopMenuBar;
import jshdesktop.events.InitCompletionEvent;
import terra.shell.logging.LogManager;
import terra.shell.logging.Logger;
import terra.shell.utils.system.EventManager;

public class JDesktopFrame extends JFrame {
	private static Logger log = LogManager.getLogger("DesktopFrame");
	private final DecoratedDesktopPane desktopPane = new DecoratedDesktopPane();
	private static DesktopMenuBar menuBar;
	private final JDesktopFrame thisFrame = this;
	private static volatile boolean isAlreadyInitialized = false;
	private final module m;
	private static Thread monitorAddThread;

	public JDesktopFrame(module m) {
		this.m = m;
	}

	public void init() {
		if (isAlreadyInitialized) {
			return;
		}
		isAlreadyInitialized = true;

		log.debug("Creating JDesktopFrame top level...");
		this.setUndecorated(true);
		super.add(desktopPane);
		setSize(GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().width,
				GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().height);
		menuBar = desktopPane.getMenuBar();
		super.add(menuBar, BorderLayout.SOUTH);
		setVisible(true);

		EventManager.invokeEvent(new InitCompletionEvent(null));

		monitorAddThread = new Thread(new Runnable() {
			public void run() {
				synchronized (monitorAddThread) {
					while (thisFrame.isVisible()) {
						try {
							monitorAddThread.wait();
							JInternalFrame toAdd = module.toAdd.poll();
							if (toAdd != null) {
								add(toAdd);
							} else {
								log.debug("Received null component");
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}
		});
		monitorAddThread.start();
	}

	public Thread getMonitorThread() {
		return monitorAddThread;
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
