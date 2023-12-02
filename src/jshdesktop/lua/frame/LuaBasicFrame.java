package jshdesktop.lua.frame;

import java.io.Serializable;
import java.util.function.Consumer;

import javax.swing.JLayeredPane;

import com.hk.lua.Lua;
import com.hk.lua.Lua.LuaMethod;
import com.hk.lua.LuaInterpreter;
import com.hk.lua.LuaObject;
import com.hk.lua.LuaType;
import com.hk.lua.LuaUserdata;

import jshdesktop.desktop.frame.BasicFrame;
import jshdesktop.lua.LuaComponent;

public class LuaBasicFrame extends LuaUserdata implements Serializable {
	private BasicFrame wrappedFrame;
	private JLayeredPane layeredContentPane;
	private LuaInterpreter interp;
	private static final LuaObject luaBasicFrameMetatable = Lua.newTable();

	public LuaBasicFrame(LuaInterpreter interp, LuaComponent contentComponent) {
		layeredContentPane = new JLayeredPane();
		wrappedFrame = new BasicFrame() {

			@Override
			public void create() {
				this.setContentPane(layeredContentPane);
				layeredContentPane.add(contentComponent.getComponent(), JLayeredPane.DEFAULT_LAYER);
				setSize(contentComponent.getComponent().getSize());
				finish();
			}

		};
		metatable = luaBasicFrameMetatable;
	}

	public LuaBasicFrame(LuaInterpreter interp, BasicFrame frame) {
		wrappedFrame = frame;
		metatable = luaBasicFrameMetatable;
	}

	public BasicFrame getBasicFrame() {
		return wrappedFrame;
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

				if (args.length == 3 && args[2].type() == LuaType.INTEGER) {
					lf.layeredContentPane.add(toAdd.getComponent(), args[2].getInt());
				} else
					lf.layeredContentPane.add(toAdd.getComponent());
			}

		});

		LuaObject removeLuaComponentFunction = Lua.newFunc(new Consumer<LuaObject[]>() {

			@Override
			public void accept(LuaObject[] args) {
				Lua.checkArgs("RemoveComponent", args, LuaType.USERDATA, LuaType.USERDATA);
				LuaBasicFrame lf = (LuaBasicFrame) args[0];
				LuaComponent toAdd = (LuaComponent) args[1];
				lf.layeredContentPane.remove(toAdd.getComponent());
			}

		});

		LuaObject changeComponentLayerFunction = Lua.newFunc(new Consumer<LuaObject[]>() {

			@Override
			public void accept(LuaObject[] args) {
				Lua.checkArgs("SetLayer", args, LuaType.USERDATA, LuaType.USERDATA, LuaType.INTEGER);
				LuaBasicFrame lbf = (LuaBasicFrame) args[0];
				LuaComponent toMove = (LuaComponent) args[1];
				int newLayer = args[2].getInt();
				lbf.layeredContentPane.setLayer(toMove.getComponent(), newLayer);
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
		luaBasicFrameMetatable.rawSet("SetLayer", changeComponentLayerFunction);
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
