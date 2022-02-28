package jshdesktop.desktop.frame.journeybrowser;

import java.awt.BorderLayout;

import com.codebrig.journey.JourneyBrowserView;

import jshdesktop.desktop.frame.BasicFrame;

public class JourneyBrowser extends BasicFrame {
	private JourneyBrowserView browser;

	@Override
	public void create() {
		browser = new JourneyBrowserView("http://wiki.java-shell.com");
		add(browser, BorderLayout.CENTER);
		setSize(750, 500);
		finish();
	}

}
