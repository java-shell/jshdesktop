package jshdesktop.commands;

import java.util.ArrayList;

import jshdesktop.desktop.frame.journeybrowser.JourneyBrowser;
import terra.shell.command.BasicCommand;
import terra.shell.utils.perms.Permissions;

public class LaunchJourneyCommand extends BasicCommand {

	@Override
	public String getName() {
		return "journey";
	}

	@Override
	public String getVersion() {
		return "0.1";
	}

	@Override
	public String getAuthor() {
		return "D.S";
	}

	@Override
	public String getOrg() {
		return "java-shell";
	}

	@Override
	public boolean isBlocking() {
		return false;
	}

	@Override
	public ArrayList<String> getAliases() {
		return null;
	}

	@Override
	public ArrayList<Permissions> getPerms() {
		return null;
	}

	@Override
	public boolean start() {
		new JourneyBrowser();
		return true;
	}

}
