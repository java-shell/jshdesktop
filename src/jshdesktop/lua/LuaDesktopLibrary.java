package jshdesktop.lua;

import java.awt.Color;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
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

import jshdesktop.com.pump.plaf.PulsingCirclesThrobberUI;
import jshdesktop.com.pump.plaf.ThrobberUI;
import jshdesktop.lua.components.LuaThrobber;
import jshdesktop.lua.frame.LuaBasicFrame;
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
	CreateBasicFrame {
		public LuaObject call(LuaInterpreter interp, LuaObject[] args) {
			Lua.checkArgs(name(), args, LuaType.USERDATA);
			if (!(args[0] instanceof LuaComponent))
				Lua.badArgument(0, "CreateBasicFrame", "Expected LuaComponent");
			return new LuaBasicFrame(interp, (LuaComponent) args[0]);
		}
	},
	CreateThrobber {
		public LuaObject call(LuaInterpreter interp, LuaObject[] args) {
			if (args.length == 1) {
				if (args[0].type() != LuaType.TABLE)
					Lua.badArgument(0, "CreateThrobber", "Table Expected");

				LuaObject colorObject = args[0].getIndex(interp, "Color");
				LuaObject UIObject = args[0].getIndex(interp, "UI");
				String colorString = "white";
				String UIString = "PULSING_CIRCLES";

				if (colorObject != null)
					colorString = colorObject.getString();

				if (UIObject != null)
					UIString = UIObject.getString();

				Color color = null;
				ThrobberUI ui = null;

				try {
					Field colorField = Class.forName("java.awt.Color").getField(colorString);
					color = (Color) colorField.get(null);
				} catch (Exception e) {
					color = Color.WHITE;
				}

				switch (UIString) {
				case "PULSING_CIRCLES":
					ui = new PulsingCirclesThrobberUI();
					break;
				case "AQUA":
					ui = new jshdesktop.com.pump.plaf.AquaThrobberUI();
					break;
				case "CHASING_ARROWS":
					ui = new jshdesktop.com.pump.plaf.ChasingArrowsThrobberUI();
					break;
				default:
					ui = new PulsingCirclesThrobberUI();
				}

				return new LuaThrobber(ui, color);
			}
			return new LuaThrobber();
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
