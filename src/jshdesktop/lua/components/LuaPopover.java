package jshdesktop.lua.components;

import java.util.function.Consumer;

import javax.swing.JComponent;

import com.hk.lua.Lua;
import com.hk.lua.LuaInterpreter;
import com.hk.lua.LuaObject;
import com.hk.lua.LuaType;
import com.hk.lua.LuaUserdata;

import jshdesktop.com.pump.swing.popover.BasicPopoverVisibility;
import jshdesktop.com.pump.swing.popover.JPopover;

public class LuaPopover extends LuaUserdata {
	private JPopover popover;
	private static final LuaObject luaPopoverMetatable = Lua.newTable();

	public LuaPopover(LuaInterpreter interp, JComponent internalComp, JComponent popoverComp) {
		popover = new JPopover<JComponent>(internalComp, popoverComp, true);
		popover.setVisibility(new BasicPopoverVisibility());
		metatable = luaPopoverMetatable;
	}

	@Override
	public Object getUserdata() {
		return this;
	}

	@Override
	public String name() {
		return "Popover";
	}

	static {
		luaPopoverMetatable.rawSet("__name", "Popover");
		luaPopoverMetatable.rawSet("__index", luaPopoverMetatable);

		LuaObject diposeFunction = Lua.newFunc(new Consumer<LuaObject[]>() {

			@Override
			public void accept(LuaObject[] args) {
				Lua.checkArgs("Dispose", args, LuaType.USERDATA);
				LuaPopover lp = (LuaPopover) args[0];
				lp.popover.dispose();
			}

		});

		luaPopoverMetatable.rawSet("Dispose", diposeFunction);
	}

}
