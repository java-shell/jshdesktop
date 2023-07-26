package jshdesktop.lua.components;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.function.Consumer;

import javax.swing.JToggleButton;

import com.hk.lua.Lua;
import com.hk.lua.Lua.LuaMethod;
import com.hk.lua.LuaInterpreter;
import com.hk.lua.LuaObject;
import com.hk.lua.LuaType;

import jshdesktop.lua.LuaComponent;

public class LuaToggleButton extends LuaComponent {
	private JToggleButton button;

	public LuaToggleButton(LuaInterpreter interp) {
		super(interp);
		button = new JToggleButton();
	}

	public LuaToggleButton(LuaInterpreter interp, JToggleButton button) {
		super(interp);
		this.button = button;
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				LuaObject eventTable = Lua.newTable();
				eventTable.rawSet("EventType", "ButtonToggle");
				eventTable.rawSet("Boolean", button.isSelected());
				eventCallback.call(interp, eventTable);
			}
		});
	}

	public void appendMetatable() {
		LuaObject setSelectedFunction = Lua.newFunc(new Consumer<LuaObject[]>() {

			@Override
			public void accept(LuaObject[] args) {
				Lua.checkArgs("SetSelected", args, LuaType.USERDATA, LuaType.BOOLEAN);
				LuaToggleButton ltb = (LuaToggleButton) args[0];
				ltb.button.setSelected(args[1].getBoolean());
			}

		});

		LuaObject getSelectedFunction = Lua.newMethod(new LuaMethod() {

			@Override
			public LuaObject call(LuaInterpreter arg0, LuaObject[] args) {
				Lua.checkArgs("GetSelected", args, LuaType.USERDATA);
				return Lua.newBool(button.isSelected());
			}

		});

		metatable.rawSet("SetSelected", setSelectedFunction);
	}

}
