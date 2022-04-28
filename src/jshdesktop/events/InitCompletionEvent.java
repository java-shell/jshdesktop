package jshdesktop.events;

import terra.shell.utils.system.GeneralEvent;

public class InitCompletionEvent extends GeneralEvent{

	@EventPriority(id=GENERAL_TYPE, value=5)
	public InitCompletionEvent(Object[] args) {
		super("jshdesktop_initcompletion", args);
	}

}
