package jshdesktop.lua.components;

import java.awt.Color;
import java.lang.reflect.Field;
import java.util.function.Consumer;

import com.hk.lua.Lua;
import com.hk.lua.LuaInterpreter;
import com.hk.lua.LuaObject;
import com.hk.lua.LuaType;
import com.hk.lua.LuaUserdata;
import com.hk.lua.Lua.LuaMethod;

import jshdesktop.com.pump.plaf.PulsingCirclesThrobberUI;
import jshdesktop.com.pump.plaf.ThrobberUI;
import jshdesktop.com.pump.swing.JThrobber;
import jshdesktop.lua.LuaComponent;
import jshdesktop.lua.LuaComponent.LuaGraphicsWrapper;

public class LuaThrobber extends LuaUserdata {

	private JThrobber throbber;

	private static final LuaObject luaThrobberMetatable = Lua.newTable();

	public LuaThrobber() {
		this(new PulsingCirclesThrobberUI(), Color.WHITE);
	}

	public LuaThrobber(ThrobberUI ui) {
		this(ui, Color.WHITE);
	}

	public LuaThrobber(ThrobberUI ui, Color foreground) {
		throbber = new JThrobber();
		throbber.setUI(ui);
		throbber.setForeground(foreground);
		metatable = luaThrobberMetatable;
	}

	public LuaThrobber(Color foreground) {
		this(new PulsingCirclesThrobberUI(), foreground);
	}

	@Override
	public Object getUserdata() {
		return this;
	}

	@Override
	public String name() {
		return "Throbber";
	}

	static {
		luaThrobberMetatable.rawSet("__name", "Throbber");
		luaThrobberMetatable.rawSet("__index", luaThrobberMetatable);
		luaThrobberMetatable.rawSet("PULSING_CIRCLES", "PULSING_CIRCLES");
		luaThrobberMetatable.rawSet("AQUA", "AQUA");
		luaThrobberMetatable.rawSet("CHASING_ARROWS", "CHASING_ARROWS");

		LuaObject changeThrobberColorFunction = Lua.newFunc(new Consumer<LuaObject[]>() {

			@Override
			public void accept(LuaObject[] args) {
				Lua.checkArgs("Throbber", args, LuaType.USERDATA, LuaType.STRING);

				LuaThrobber lt = (LuaThrobber) args[0];
				Color newColor = null;

				try {
					Field colorField = Class.forName("java.awt.Color").getField(args[1].getString());
					newColor = (Color) colorField.get(null);
				} catch (Exception e) {
					newColor = null;
				}
				if (newColor != null)
					lt.throbber.setForeground(newColor);
			}

		});

		LuaObject getColorFunction = Lua.newMethod(new LuaMethod() {

			@Override
			public LuaObject call(LuaInterpreter arg0, LuaObject[] args) {
				Lua.checkArgs("GetColor", args, LuaType.USERDATA);

				LuaThrobber lt = (LuaThrobber) args[0];
				return Lua.newString(lt.throbber.getForeground().toString());
			}

		});

		LuaObject setSizeFunction = Lua.newFunc(new Consumer<LuaObject[]>() {

			@Override
			public void accept(LuaObject[] args) {
				Lua.checkArgs("SetSize", args, LuaType.USERDATA, LuaType.INTEGER, LuaType.INTEGER);
				LuaThrobber lt = (LuaThrobber) args[0];
				lt.throbber.setSize(args[1].getInt(), args[2].getInt());
			}

		});

		LuaObject getSizeFunction = Lua.newMethod(new LuaMethod() {

			@Override
			public LuaObject call(LuaInterpreter arg0, LuaObject[] args) {
				Lua.checkArgs("GetSize", args, LuaType.USERDATA);
				LuaThrobber lt = (LuaThrobber) args[0];

				LuaObject dimensionTable = Lua.newTable();
				dimensionTable.rawSet(0, lt.throbber.getWidth());
				dimensionTable.rawSet(1, lt.throbber.getHeight());
				return dimensionTable;
			}

		});

		luaThrobberMetatable.rawSet("SetColor", changeThrobberColorFunction);
		luaThrobberMetatable.rawSet("GetColor", getColorFunction);
		luaThrobberMetatable.rawSet("SetSize", setSizeFunction);
		luaThrobberMetatable.rawSet("GetSize", getSizeFunction);

	}

}
