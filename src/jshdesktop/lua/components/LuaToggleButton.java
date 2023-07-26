package jshdesktop.lua.components;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.function.Consumer;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JToggleButton;

import com.hk.lua.Lua;
import com.hk.lua.Lua.LuaMethod;
import com.hk.lua.LuaInterpreter;
import com.hk.lua.LuaObject;
import com.hk.lua.LuaType;

import jshdesktop.lua.LuaComponent;
import jshdesktop.lua.image.LuaImageWrapper;

public class LuaToggleButton extends LuaComponent {
	private JToggleButton button;
	private LuaToggleButton reference;

	public LuaToggleButton(LuaInterpreter interp) {
		super(interp);
		reference = this;
		appendMetatable();
		button = new JToggleButton();
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				LuaObject eventTable = Lua.newTable();
				eventTable.rawSet("Source", reference);
				eventTable.rawSet("EventType", "ButtonToggle");
				eventTable.rawSet("Boolean", button.isSelected());
				eventCallback.call(interp, eventTable);
			}
		});
	}

	public LuaToggleButton(LuaInterpreter interp, JToggleButton button) {
		super(interp);
		reference = this;
		appendMetatable();
		this.button = button;
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				LuaObject eventTable = Lua.newTable();
				eventTable.rawSet("Source", reference);
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

		LuaObject setIconFunction = Lua.newFunc(new Consumer<LuaObject[]>() {

			@Override
			public void accept(LuaObject[] args) {
				Lua.checkArgs("SetIcon", args, LuaType.USERDATA, LuaType.ANY);
				if (!(args[1] instanceof LuaImageWrapper))
					Lua.badArgument(1, "SetIcon", "LuaImageWrapper expected");
				LuaToggleButton ltb = (LuaToggleButton) args[0];
				LuaImageWrapper liw = (LuaImageWrapper) args[1];
				Icon icon = new ImageIcon(liw.getImage());
				ltb.button.setIcon(icon);
			}

		});

		LuaObject setPressedIconFunction = Lua.newFunc(new Consumer<LuaObject[]>() {

			@Override
			public void accept(LuaObject[] args) {
				Lua.checkArgs("SetPressedIcon", args, LuaType.USERDATA, LuaType.ANY);
				if (!(args[1] instanceof LuaImageWrapper))
					Lua.badArgument(1, "SetIcon", "LuaImageWrapper expected");
				LuaToggleButton ltb = (LuaToggleButton) args[0];
				LuaImageWrapper liw = (LuaImageWrapper) args[1];
				Icon icon = new ImageIcon(liw.getImage());
				ltb.button.setPressedIcon(icon);
			}

		});

		metatable.rawSet("SetSelected", setSelectedFunction);
		metatable.rawSet("GetSelected", getSelectedFunction);
		metatable.rawSet("SetIcon", setIconFunction);
		metatable.rawSet("SetPressedIcon", setPressedIconFunction);
	}

}
