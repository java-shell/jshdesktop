package jshdesktop.lua.frame;

import java.util.function.Consumer;

import javax.swing.JFrame;

import com.hk.lua.Lua;
import com.hk.lua.Lua.LuaMethod;
import com.hk.lua.LuaInterpreter;
import com.hk.lua.LuaObject;
import com.hk.lua.LuaType;
import com.hk.lua.LuaUserdata;

import jshdesktop.lua.LuaComponent;

public class LuaFrame extends LuaUserdata {
	private JFrame wrappedFrame;
	private LuaInterpreter interp;
	private static final LuaObject luaFrameMetatable = Lua.newTable();

	public LuaFrame(LuaInterpreter interp) {
		this.wrappedFrame = new JFrame();
		this.interp = interp;
		metatable = luaFrameMetatable;
	}

	public LuaFrame(LuaInterpreter interp, JFrame frame) {
		this.wrappedFrame = frame;
		this.interp = interp;
		metatable = luaFrameMetatable;
	}

	@Override
	public Object getUserdata() {
		return wrappedFrame;
	}

	@Override
	public String name() {
		return "FRAME";
	}

	public JFrame getFrame() {
		return wrappedFrame;
	}

	static {
		luaFrameMetatable.rawSet("__name", "FRAME");
		luaFrameMetatable.rawSet("__index", luaFrameMetatable);

		LuaObject setContentPane = Lua.newFunc(new Consumer<LuaObject[]>() {

			@Override
			public void accept(LuaObject[] args) {
			}

		});

		LuaObject setVisibleFunction = Lua.newFunc(new Consumer<LuaObject[]>() {

			@Override
			public void accept(LuaObject[] args) {
				Lua.checkArgs("SetVisible", args, LuaType.USERDATA, LuaType.BOOLEAN);
				LuaFrame lf = (LuaFrame) args[0];
				lf.wrappedFrame.setVisible(args[1].getBoolean());
			}

		});

		LuaObject getVisibleFunction = Lua.newMethod(new LuaMethod() {

			@Override
			public LuaObject call(LuaInterpreter arg0, LuaObject[] args) {
				Lua.checkArgs("GetVisible", args, LuaType.USERDATA);
				LuaFrame lf = (LuaFrame) args[0];
				return Lua.newBool(lf.wrappedFrame.isVisible());
			}

		});

		LuaObject setSizeFunction = Lua.newFunc(new Consumer<LuaObject[]>() {

			@Override
			public void accept(LuaObject[] args) {
				Lua.checkArgs("SetSize", args, LuaType.USERDATA, LuaType.INTEGER, LuaType.INTEGER);
				LuaFrame lf = (LuaFrame) args[0];
				lf.wrappedFrame.setSize(args[1].getInt(), args[2].getInt());
			}

		});

		LuaObject getSizeFunction = Lua.newMethod(new LuaMethod() {

			@Override
			public LuaObject call(LuaInterpreter arg0, LuaObject[] args) {
				Lua.checkArgs("GetSize", args, LuaType.USERDATA);
				LuaFrame lf = (LuaFrame) args[0];

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
				LuaFrame lf = (LuaFrame) args[0];
				LuaComponent toAdd = (LuaComponent) args[1];
				lf.wrappedFrame.add(toAdd.getComponent());
			}

		});

		LuaObject removeLuaComponentFunction = Lua.newFunc(new Consumer<LuaObject[]>() {

			@Override
			public void accept(LuaObject[] args) {
				Lua.checkArgs("RemoveComponent", args, LuaType.USERDATA, LuaType.USERDATA);
				LuaFrame lf = (LuaFrame) args[0];
				LuaComponent toAdd = (LuaComponent) args[1];
				lf.wrappedFrame.remove(toAdd.getComponent());
			}

		});

		LuaObject setTitleFunction = Lua.newFunc(new Consumer<LuaObject[]>() {

			@Override
			public void accept(LuaObject[] args) {
				Lua.checkArgs("SetTitle", args, LuaType.USERDATA, LuaType.STRING);
				LuaFrame lf = (LuaFrame) args[0];
				lf.wrappedFrame.setTitle(args[1].getString());
			}

		});

		LuaObject setLocationFunction = Lua.newFunc(new Consumer<LuaObject[]>() {

			@Override
			public void accept(LuaObject[] args) {
				Lua.checkArgs("SetLocation", args, LuaType.USERDATA, LuaType.INTEGER, LuaType.INTEGER);
				LuaFrame lf = (LuaFrame) args[0];
				lf.wrappedFrame.setSize(args[1].getInt(), args[2].getInt());
			}

		});

		LuaObject repaintFunction = Lua.newFunc(new Consumer<LuaObject[]>() {

			@Override
			public void accept(LuaObject[] args) {
				Lua.checkArgs("RepaintFunction", args, LuaType.USERDATA);
				LuaFrame lf = (LuaFrame) args[0];
				lf.wrappedFrame.repaint();
			}

		});

		luaFrameMetatable.rawSet("SetVisible", setVisibleFunction);
		luaFrameMetatable.rawSet("GetVisible", getVisibleFunction);
		luaFrameMetatable.rawSet("SetSize", setSizeFunction);
		luaFrameMetatable.rawSet("GetSize", getSizeFunction);
		luaFrameMetatable.rawSet("AddComponent", addLuaComponentFunction);
		luaFrameMetatable.rawSet("RemoveComponent", removeLuaComponentFunction);
		luaFrameMetatable.rawSet("SetTitle", setTitleFunction);
		luaFrameMetatable.rawSet("SetLocation", setLocationFunction);
		luaFrameMetatable.rawSet("Repaint", repaintFunction);
	}

}
