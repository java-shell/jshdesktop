package jshdesktop.lua;

import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Base64;
import java.util.function.BiConsumer;

import javax.imageio.ImageIO;

import com.hk.lua.Environment;
import com.hk.lua.Lua;
import com.hk.lua.Lua.LuaMethod;
import com.hk.lua.LuaInterpreter;
import com.hk.lua.LuaObject;
import com.hk.lua.LuaType;

import jshdesktop.lua.frame.LuaFrame;
import jshdesktop.lua.image.LuaImageWrapper;

public enum LuaDesktopLibrary implements BiConsumer<Environment, LuaObject>, LuaMethod {
	CreateComponent {
		public LuaObject call(LuaInterpreter interp, LuaObject[] args) {
			return new LuaComponent(interp);
		}
	},
	CreateFrame {
		public LuaObject call(LuaInterpreter interp, LuaObject[] args) {
			return new LuaFrame(interp);
		}
	},
	GetImageFromBase64 {
		public LuaObject call(LuaInterpreter interp, LuaObject[] args) {
			if (args[0].type() != LuaType.STRING)
				Lua.badArgument(0, "GetImageFromBase64", "Base64 Encoded String Expected");

			byte[] imageBytes = Base64.getDecoder().decode(args[0].getString().getBytes());

			try {
				return new LuaImageWrapper(ImageIO.read(new ByteArrayInputStream(imageBytes)));
			} catch (IOException e) {
				e.printStackTrace();
			}
			return Lua.NIL;
		}
	},
	GetImageFromURL {
		public LuaObject call(LuaInterpreter interp, LuaObject[] args) {
			if (args[0].type() != LuaType.STRING)
				Lua.badArgument(0, "GetImageFromURL", "URL String Expected");

			try {
				URL imageUrl = new URL(args[0].getString());
				return new LuaImageWrapper(ImageIO.read(imageUrl));
			} catch (Exception e) {
				e.printStackTrace();
			}
			return Lua.NIL;
		}
	},
	GetScreenCenter {
		public LuaObject call(LuaInterpreter interp, LuaObject[] args) {
			LuaObject dimensionTable = Lua.newTable();
			Point center = GraphicsEnvironment.getLocalGraphicsEnvironment().getCenterPoint();
			dimensionTable.rawSet(0, center.x);
			dimensionTable.rawSet(1, center.y);
			return dimensionTable;
		}
	};

	@Override
	public LuaObject call(LuaInterpreter arg0, LuaObject[] arg1) {
		return null;
	}

	@Override
	public void accept(Environment t, LuaObject table) {
		String name = toString();
		if (name != null && !name.trim().isEmpty())
			table.rawSet(name, Lua.newMethod(this));
	}

}
