package jshdesktop.lua.components;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.function.Consumer;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;

import com.hk.lua.Lua;
import com.hk.lua.LuaInterpreter;
import com.hk.lua.LuaObject;
import com.hk.lua.LuaType;
import com.hk.lua.Lua.LuaMethod;

import jshdesktop.lua.LuaComponent;
import jshdesktop.lua.image.LuaImageWrapper;

public class LuaCheckBox extends LuaComponent {
	JCheckBox button;

	public LuaCheckBox(LuaInterpreter interp) {
		super(interp);
		button = new JCheckBox();
		appendMetatable();
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				LuaObject eventTable = Lua.newTable();
				eventTable.rawSet("EventType", "ButtonCheck");
				eventTable.rawSet("Boolean", button.isSelected());
				eventCallback.call(interp, eventTable);
			}
		});
	}

	private void appendMetatable() {
		LuaObject setSelectedFunction = Lua.newFunc(new Consumer<LuaObject[]>() {

			@Override
			public void accept(LuaObject[] args) {
				Lua.checkArgs("SetSelected", args, LuaType.USERDATA, LuaType.BOOLEAN);
				LuaCheckBox lcb = (LuaCheckBox) args[0];
				lcb.button.setSelected(args[1].getBoolean());
			}

		});

		LuaObject getSelectedFunction = Lua.newMethod(new LuaMethod() {

			@Override
			public LuaObject call(LuaInterpreter arg0, LuaObject[] args) {
				Lua.checkArgs("GetSelected", args, LuaType.USERDATA);
				return Lua.newBool(button.isSelected());
			}

		});

		LuaObject setIconFunction = Lua.newFunc(new Consumer<LuaObject[]>() {

			@Override
			public void accept(LuaObject[] args) {
				Lua.checkArgs("SetIcon", args, LuaType.USERDATA, LuaType.ANY);
				if (!(args[1] instanceof LuaImageWrapper))
					Lua.badArgument(1, "SetIcon", "LuaImageWrapper expected");
				LuaCheckBox lcb = (LuaCheckBox) args[0];
				LuaImageWrapper liw = (LuaImageWrapper) args[1];
				Icon icon = new ImageIcon(liw.getImage());
				lcb.button.setIcon(icon);
			}

		});

		LuaObject setPressedIconFunction = Lua.newFunc(new Consumer<LuaObject[]>() {

			@Override
			public void accept(LuaObject[] args) {
				Lua.checkArgs("SetPressedIcon", args, LuaType.USERDATA, LuaType.ANY);
				if (!(args[1] instanceof LuaImageWrapper))
					Lua.badArgument(1, "SetIcon", "LuaImageWrapper expected");
				LuaCheckBox lcb = (LuaCheckBox) args[0];
				LuaImageWrapper liw = (LuaImageWrapper) args[1];
				Icon icon = new ImageIcon(liw.getImage());
				lcb.button.setPressedIcon(icon);
			}

		});

		metatable.rawSet("SetSelected", setSelectedFunction);
		metatable.rawSet("GetSelected", getSelectedFunction);
		metatable.rawSet("SetIcon", setIconFunction);
		metatable.rawSet("SetPressedIcon", setPressedIconFunction);
	}

}
