package jshdesktop;

import java.awt.GraphicsEnvironment;
import java.io.File;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

import jshdesktop.commands.LaunchButtonPreviewCommand;
import jshdesktop.commands.LaunchImageViewerCommand;
import jshdesktop.commands.LaunchTextEditorCommand;
import jshdesktop.commands.LaunchUpdaterCommand;
import jshdesktop.commands.LaunchVirtualConsoleCommand;
import jshdesktop.desktop.frame.DialogFrame;
import jshdesktop.desktop.frame.DialogFrame.DialogType;
import terra.shell.config.Configuration;
import terra.shell.launch.Launch;
import terra.shell.modules.ModuleEvent;
import terra.shell.modules.ModuleEvent.DummyEvent;
import terra.shell.utils.keys.Event;
import terra.shell.utils.system.EventManager;
import terra.shell.utils.system.GeneralVariable;
import terra.shell.utils.system.Variables;

public class module extends terra.shell.modules.Module {
	private static JDesktopFrame main;
	private static JFrame splashFrame;
	private static Configuration conf;

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "jshdesktop";
	}

	@Override
	public void run() {
		log.debug("Run");
		if (((String) conf.getValue("doStart")).equalsIgnoreCase("false")) {
			return;
		}

		EventManager.registerEvType("jshdesktop_initcompletion");
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				main = new JDesktopFrame(splashFrame);
			}
		});
	}

	@Override
	public String getVersion() {
		return null;
	}

	@Override
	public String getAuthor() {
		return null;
	}

	@Override
	public String getOrg() {
		return null;
	}

	@Override
	public void onEnable() {
		conf = Launch.getConfig("JSHDesktop/main.conf");
		if (conf == null) {
			log.debug("First time setup, generating main configuration with defaults");
			File confFile = new File(Launch.getConfD(), "JSHDesktop/main.conf");
			if (!confFile.getParentFile().exists())
				confFile.getParentFile().mkdir();
			conf = new Configuration(confFile);
			conf.setValue("doStart", "true");
			conf.setValue("splashImage",
					Launch.getConfD().getParent() + "/modules/jshdesktop/assets/java_shell_logo.gif");
		}

		ImageIcon splashImageFile = null;
		try {
			splashImageFile = new ImageIcon(new URL("file:///" + (String) conf.getValue("splashImage")));
			JFrame splashFrame = new JFrame();
			log.log("Starting splash screen");
			splashFrame = new JFrame();
			splashFrame.setUndecorated(true);
			splashFrame.setSize(GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().width,
					GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().height);

			splashFrame.setLocationRelativeTo(null);

			JLabel splashLabel = new JLabel(splashImageFile);
			splashFrame.getContentPane().add(splashLabel);
			splashFrame.pack();
			splashFrame.setAlwaysOnTop(true);
			splashFrame.setVisible(true);
		} catch (Exception e) {
			log.err("No splash image found...");
			log.err(e.getLocalizedMessage());
			splashFrame = null;
		}

		Variables.setVar(new GeneralVariable("jshDesktop", "true"));
		log.log("Registering JSHDesktop commands...");
		LaunchTextEditorCommand textEdit = new LaunchTextEditorCommand();
		Launch.registerCommand(textEdit.getName(), textEdit);
		LaunchVirtualConsoleCommand vterm = new LaunchVirtualConsoleCommand();
		Launch.registerCommand(vterm.getName(), vterm);
		LaunchImageViewerCommand imageViewer = new LaunchImageViewerCommand();
		Launch.registerCommand(imageViewer.getName(), imageViewer);
		LaunchUpdaterCommand updater = new LaunchUpdaterCommand();
		Launch.registerCommand(updater.getName(), updater, null);
		LaunchButtonPreviewCommand buttonPreview = new LaunchButtonPreviewCommand();
		Launch.registerCommand(buttonPreview.getName(), buttonPreview);
	}

	@Override
	public void trigger(Event event) {
		log.debug("Got Event");
		if (event instanceof DummyEvent) {
			log.debug("Got ModuleEvent");
			ModuleEvent me = ((DummyEvent) event).getME();
			Object[] args = me.getArgs();
			if (args.length != 1 || !(args[0] instanceof String)) {
				log.err("Invalid arguments from ModuleEvent");
				return;
			}
			String arg = (String) args[0];
			if (arg.equals("END_SESSION")) {
				DialogFrame.createDialog(DialogType.OK, "Ending Session");
				main.setVisible(false);
				main = null;
			}
		}
	}

	public static JDesktopFrame getDesktopFrame() {
		return main;
	}

}
