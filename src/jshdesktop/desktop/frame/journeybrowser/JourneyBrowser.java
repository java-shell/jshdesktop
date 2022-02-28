package jshdesktop.desktop.frame.journeybrowser;

import java.awt.BorderLayout;

import javax.swing.SwingUtilities;

import com.codebrig.journey.JourneyBrowserView;

import jshdesktop.desktop.frame.BasicFrame;

public class JourneyBrowser extends BasicFrame {
	private JourneyBrowserView browser;

	@Override
	public void create() {
		browser = new JourneyBrowserView("https://wiki.java-shell.com");
		add(browser, BorderLayout.CENTER);
		setSize(750, 500);
		finish();
	}

}
