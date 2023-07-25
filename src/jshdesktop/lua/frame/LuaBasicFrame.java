package jshdesktop.lua.frame;

import java.util.function.Consumer;

import com.hk.lua.Lua;
import com.hk.lua.Lua.LuaMethod;
import com.hk.lua.LuaInterpreter;
import com.hk.lua.LuaObject;
import com.hk.lua.LuaType;
import com.hk.lua.LuaUserdata;

import jshdesktop.desktop.frame.BasicFrame;
import jshdesktop.lua.LuaComponent;

public class LuaBasicFrame extends LuaUserdata {
	private BasicFrame wrappedFrame;
	private LuaInterpreter interp;
	private static final LuaObject luaBasicFrameMetatable = Lua.newTable();

	public LuaBasicFrame(LuaInterpreter interp, LuaComponent contentComponent) {
		wrappedFrame = new BasicFrame() {

			@Override
			public void create() {
				this.setContentPane(contentComponent.getComponent());
				setSize(contentComponent.getComponent().getSize());
				finish();
			}

		};
		metatable = luaBasicFrameMetatable;
	}

	static {
		luaBasicFrameMetatable.rawSet("__name", "FRAME");
		luaBasicFrameMetatable.rawSet("__index", luaBasicFrameMetatable);

		LuaObject setVisibleFunction = Lua.newFunc(new Consumer<LuaObject[]>() {

			@Override
			public void accept(LuaObject[] args) {
				Lua.checkArgs("SetVisible", args, LuaType.USERDATA, LuaType.BOOLEAN);
				LuaBasicFrame lf = (LuaBasicFrame) args[0];
				lf.wrappedFrame.setVisible(args[1].getBoolean());
			}

		});

		LuaObject getVisibleFunction = Lua.newMethod(new LuaMethod() {

			@Override
			public LuaObject call(LuaInterpreter arg0, LuaObject[] args) {
				Lua.checkArgs("GetVisible", args, LuaType.USERDATA);
				LuaBasicFrame lf = (LuaBasicFrame) args[0];
				return Lua.newBool(lf.wrappedFrame.isVisible());
			}

		});

		LuaObject setSizeFunction = Lua.newFunc(new Consumer<LuaObject[]>() {

			@Override
			public void accept(LuaObject[] args) {
				Lua.checkArgs("SetSize", args, LuaType.USERDATA, LuaType.INTEGER, LuaType.INTEGER);
				LuaBasicFrame lf = (LuaBasicFrame) args[0];
				lf.wrappedFrame.setSize(args[1].getInt(), args[2].getInt());
			}

		});

		LuaObject getSizeFunction = Lua.newMethod(new LuaMethod() {

			@Override
			public LuaObject call(LuaInterpreter arg0, LuaObject[] args) {
				Lua.checkArgs("GetSize", args, LuaType.USERDATA);
				LuaBasicFrame lf = (LuaBasicFrame) args[0];

				LuaObject dimensionTable = Lua.newTable();
				dimensionTable.rawSet(0, lf.wrappedFrame.getWidth());
				dimensionTable.rawSet(1, lf.wrappedFrame.getHeight());

				return dimensionTable;
			}

		});

		LuaObject addLuaComponentFunction = Lua.newFunc(new Consumer<LuaObject[]>() {

			@Override
			public void accept(LuaObject[] args) {
				Lua.checkArgs("AddComponent", args, LuaType.USERDATA, LuaType.USERDATA);
				LuaBasicFrame lf = (LuaBasicFrame) args[0];
				LuaComponent toAdd = (LuaComponent) args[1];

			}

		});

		LuaObject removeLuaComponentFunction = Lua.newFunc(new Consumer<LuaObject[]>() {

			@Override
			public void accept(LuaObject[] args) {
				Lua.checkArgs("RemoveComponent", args, LuaType.USERDATA, LuaType.USERDATA);
				LuaBasicFrame lf = (LuaBasicFrame) args[0];
				LuaComponent toAdd = (LuaComponent) args[1];
				lf.wrappedFrame.remove(toAdd.getComponent());
			}

		});

		LuaObject setTitleFunction = Lua.newFunc(new Consumer<LuaObject[]>() {

			@Override
			public void accept(LuaObject[] args) {
				Lua.checkArgs("SetTitle", args, LuaType.USERDATA, LuaType.STRING);
				LuaBasicFrame lf = (LuaBasicFrame) args[0];
				lf.wrappedFrame.setTitle(args[1].getString());
			}

		});

		LuaObject setLocationFunction = Lua.newFunc(new Consumer<LuaObject[]>() {

			@Override
			public void accept(LuaObject[] args) {
				Lua.checkArgs("SetLocation", args, LuaType.USERDATA, LuaType.INTEGER, LuaType.INTEGER);
				LuaBasicFrame lf = (LuaBasicFrame) args[0];
				lf.wrappedFrame.setSize(args[1].getInt(), args[2].getInt());
			}

		});

		LuaObject repaintFunction = Lua.newFunc(new Consumer<LuaObject[]>() {

			@Override
			public void accept(LuaObject[] args) {
				Lua.checkArgs("RepaintFunction", args, LuaType.USERDATA);
				LuaBasicFrame lf = (LuaBasicFrame) args[0];
				lf.wrappedFrame.repaint();
			}

		});

		luaBasicFrameMetatable.rawSet("SetVisible", setVisibleFunction);
		luaBasicFrameMetatable.rawSet("GetVisible", getVisibleFunction);
		luaBasicFrameMetatable.rawSet("SetSize", setSizeFunction);
		luaBasicFrameMetatable.rawSet("GetSize", getSizeFunction);
		luaBasicFrameMetatable.rawSet("AddComponent", addLuaComponentFunction);
		luaBasicFrameMetatable.rawSet("RemoveComponent", removeLuaComponentFunction);
		luaBasicFrameMetatable.rawSet("SetTitle", setTitleFunction);
		luaBasicFrameMetatable.rawSet("SetLocation", setLocationFunction);
		luaBasicFrameMetatable.rawSet("Repaint", repaintFunction);
	}

	@Override
	public Object getUserdata() {
		return this;
	}

	@Override
	public String name() {
		return "BasicFrame";
	}

}
