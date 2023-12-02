package jshdesktop.lua.frame;

import java.awt.Graphics;
import java.io.Serializable;
import java.util.function.Consumer;

import com.hk.lua.Lua;
import com.hk.lua.Lua.LuaMethod;
import com.hk.lua.LuaInterpreter;
import com.hk.lua.LuaObject;
import com.hk.lua.LuaType;
import com.hk.lua.LuaUserdata;

import jshdesktop.lua.LuaComponent.LuaGraphicsWrapper;
import jshdesktop.widgets.WidgetFrame;

public class LuaWidgetFrame extends LuaUserdata implements Serializable {
	private WidgetFrame wrappedFrame;
	private LuaObject paintFunction;
	private static final LuaObject luaWidgetFrameMetatable = Lua.newTable();

	public LuaWidgetFrame(LuaInterpreter interp, int width, int height, boolean movable) {
		wrappedFrame = new WidgetFrame() {

			@Override
			public void create() {
				setSize(width, height);
				setMovable(movable);
				finish();
			}

			@Override
			public void paintComponent(Graphics g) {
				if (paintFunction != null)
					paintFunction.call(interp, new LuaGraphicsWrapper(g));
			}

		};
		metatable = luaWidgetFrameMetatable;

	}

	@Override
	public Object getUserdata() {
		return this;
	}

	@Override
	public String name() {
		return "WidgetFrame";
	}

	static {
		luaWidgetFrameMetatable.rawSet("__name", "WIDGETFRAME");
		luaWidgetFrameMetatable.rawSet("__index", luaWidgetFrameMetatable);

		LuaObject setVisibleFunction = Lua.newFunc(new Consumer<LuaObject[]>() {

			@Override
			public void accept(LuaObject[] args) {
				Lua.checkArgs("SetVisible", args, LuaType.USERDATA, LuaType.BOOLEAN);
				LuaWidgetFrame lf = (LuaWidgetFrame) args[0];
				lf.wrappedFrame.setVisible(args[1].getBoolean());
			}

		});

		LuaObject getVisibleFunction = Lua.newMethod(new LuaMethod() {

			@Override
			public LuaObject call(LuaInterpreter arg0, LuaObject[] args) {
				Lua.checkArgs("GetVisible", args, LuaType.USERDATA);
				LuaWidgetFrame lf = (LuaWidgetFrame) args[0];
				return Lua.newBool(lf.wrappedFrame.isVisible());
			}

		});

		LuaObject setSizeFunction = Lua.newFunc(new Consumer<LuaObject[]>() {

			@Override
			public void accept(LuaObject[] args) {
				Lua.checkArgs("SetSize", args, LuaType.USERDATA, LuaType.INTEGER, LuaType.INTEGER);
				LuaWidgetFrame lf = (LuaWidgetFrame) args[0];
				lf.wrappedFrame.setSize(args[1].getInt(), args[2].getInt());
			}

		});

		LuaObject getSizeFunction = Lua.newMethod(new LuaMethod() {

			@Override
			public LuaObject call(LuaInterpreter arg0, LuaObject[] args) {
				Lua.checkArgs("GetSize", args, LuaType.USERDATA);
				LuaWidgetFrame lf = (LuaWidgetFrame) args[0];

				LuaObject dimensionTable = Lua.newTable();
				dimensionTable.rawSet(0, lf.wrappedFrame.getWidth());
				dimensionTable.rawSet(1, lf.wrappedFrame.getHeight());

				return dimensionTable;
			}

		});

		LuaObject setLocationFunction = Lua.newFunc(new Consumer<LuaObject[]>() {

			@Override
			public void accept(LuaObject[] args) {
				Lua.checkArgs("SetLocation", args, LuaType.USERDATA, LuaType.INTEGER, LuaType.INTEGER);
				LuaWidgetFrame lf = (LuaWidgetFrame) args[0];
				lf.wrappedFrame.setSize(args[1].getInt(), args[2].getInt());
			}

		});

		LuaObject repaintFunction = Lua.newFunc(new Consumer<LuaObject[]>() {

			@Override
			public void accept(LuaObject[] args) {
				Lua.checkArgs("RepaintFunction", args, LuaType.USERDATA);
				LuaWidgetFrame lf = (LuaWidgetFrame) args[0];
				lf.wrappedFrame.repaint();
			}

		});

		LuaObject setGraphicsEventHandler = Lua.newFunc(new Consumer<LuaObject[]>() {

			@Override
			public void accept(LuaObject[] args) {
				Lua.checkArgs("SetGraphicsEventHandler", args, LuaType.USERDATA, LuaType.ANY);
				LuaWidgetFrame lf = (LuaWidgetFrame) args[0];
				lf.paintFunction = args[1];
			}

		});

		luaWidgetFrameMetatable.rawSet("SetVisible", setVisibleFunction);
		luaWidgetFrameMetatable.rawSet("GetVisible", getVisibleFunction);
		luaWidgetFrameMetatable.rawSet("SetSize", setSizeFunction);
		luaWidgetFrameMetatable.rawSet("GetSize", getSizeFunction);
		luaWidgetFrameMetatable.rawSet("SetLocation", setLocationFunction);
		luaWidgetFrameMetatable.rawSet("Repaint", repaintFunction);
		luaWidgetFrameMetatable.rawSet("SetGraphicsEventHandler", setGraphicsEventHandler);
	}

}
