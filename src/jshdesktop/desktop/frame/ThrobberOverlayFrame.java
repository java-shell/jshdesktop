package jshdesktop.desktop.frame;

import java.awt.Color;

import jshdesktop.com.pump.plaf.PulsingCirclesThrobberUI;
import jshdesktop.com.pump.plaf.ThrobberUI;
import jshdesktop.com.pump.swing.JThrobber;
import jshdesktop.widgets.WidgetFrame;

public class ThrobberOverlayFrame extends WidgetFrame {
	private Color throbberForeground;
	private ThrobberUI ui;

	public ThrobberOverlayFrame() {
		this(Color.WHITE, new PulsingCirclesThrobberUI());
	}

	public ThrobberOverlayFrame(Color foreground) {
		this(foreground, new PulsingCirclesThrobberUI());

	}

	public ThrobberOverlayFrame(Color foreground, ThrobberUI ui) {
		this.throbberForeground = foreground;
		this.ui = ui;
	}

	public ThrobberOverlayFrame(ThrobberUI ui) {
		this(Color.WHITE, ui);
	}

	@Override
	public void create() {
		JThrobber throbber = new JThrobber();
		throbber.setUI(ui);
		throbber.setForeground(throbberForeground);
		add(throbber);
	}

}
