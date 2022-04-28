package jshdesktop.events;

import terra.shell.utils.system.GeneralEvent;

public class InitCompletionEvent extends GeneralEvent{

	public InitCompletionEvent(Object[] args) {
		super("jshdesktop_initcompletion", args);
	}

}
