package jshdesktop.lua;

import java.awt.Color;
import java.awt.Component;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.Base64;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;
import java.util.function.BiConsumer;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.border.BevelBorder;
import javax.swing.border.EtchedBorder;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.hk.lua.Environment;
import com.hk.lua.Lua;
import com.hk.lua.Lua.LuaMethod;
import com.hk.lua.LuaInterpreter;
import com.hk.lua.LuaObject;
import com.hk.lua.LuaType;

import jshdesktop.com.pump.plaf.PulsingCirclesThrobberUI;
import jshdesktop.com.pump.plaf.ThrobberUI;
import jshdesktop.json.SwingJSONParser;
import jshdesktop.lua.components.LuaBorder;
import jshdesktop.lua.components.LuaCheckBox;
import jshdesktop.lua.components.LuaComboBox;
import jshdesktop.lua.components.LuaLabel;
import jshdesktop.lua.components.LuaPopover;
import jshdesktop.lua.components.LuaTextArea;
import jshdesktop.lua.components.LuaTextField;
import jshdesktop.lua.components.LuaThrobber;
import jshdesktop.lua.components.LuaToggleButton;
import jshdesktop.lua.frame.LuaBasicFrame;
import jshdesktop.lua.frame.LuaFrame;
import jshdesktop.lua.frame.LuaWidgetFrame;
import jshdesktop.lua.image.LuaImageWrapper;
import terra.shell.utils.lua.builtin.LuaUUID;

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
	CreateWidgetFrame {
		public LuaObject call(LuaInterpreter interp, LuaObject[] args) {
			Lua.checkArgs("CreateWidgetFrame", args, LuaType.INTEGER, LuaType.INTEGER, LuaType.BOOLEAN);
			return new LuaWidgetFrame(interp, args[0].getInt(), args[1].getInt(), args[2].getBoolean());
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

				return new LuaThrobber(interp, ui, color);
			}
			return new LuaThrobber(interp);
		}

	},
	CreateToggleButton {
		public LuaObject call(LuaInterpreter interp, LuaObject[] args) {
			return new LuaToggleButton(interp);
		}
	},
	CreateCheckBox {
		public LuaObject call(LuaInterpreter interp, LuaObject[] args) {
			return new LuaCheckBox(interp);
		}
	},
	CreateComboBox {
		public LuaObject call(LuaInterpreter interp, LuaObject[] args) {
			Lua.checkArgs("CreateComboBox", args, LuaType.TABLE);
			final Set<Entry<LuaObject, LuaObject>> entries = args[0].getEntries();
			String[] newOptions = new String[entries.size()];
			final Iterator<Entry<LuaObject, LuaObject>> iter = entries.iterator();
			int i = 0;
			while (iter.hasNext()) {
				Entry<LuaObject, LuaObject> entry = iter.next();
				newOptions[i] = entry.getValue().getString();
				i++;
			}
			return new LuaComboBox(interp, newOptions);
		}
	},
	CreateLabel {
		public LuaObject call(LuaInterpreter interp, LuaObject[] args) {
			return new LuaLabel(interp);
		}
	},
	CreateTextField {
		public LuaObject call(LuaInterpreter interp, LuaObject[] args) {
			return new LuaTextField(interp);
		}
	},
	CreateTextArea {
		public LuaObject call(LuaInterpreter interp, LuaObject[] args) {
			return new LuaTextArea(interp);
		}
	},
	CreateLineBorder {
		public LuaObject call(LuaInterpreter interp, LuaObject[] args) {
			Lua.checkArgs("CreateLineBorder", args, LuaType.STRING);

			Color color = null;
			try {
				Field colorField = Class.forName("java.awt.Color").getField(args[0].getString());
				color = (Color) colorField.get(null);
			} catch (Exception e) {
				color = Color.BLACK;
			}

			return new LuaBorder(BorderFactory.createLineBorder(color));
		}
	},
	CreateLoweredEtchedBorder {
		public LuaObject call(LuaInterpreter interp, LuaObject[] args) {
			return new LuaBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		}
	},
	CreateRaisedEtchedBorder {
		public LuaObject call(LuaInterpreter interp, LuaObject[] args) {
			return new LuaBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
		}
	},
	CreateLoweredBevelBorder {
		public LuaObject call(LuaInterpreter interp, LuaObject[] args) {
			return new LuaBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
		}
	},
	CreateRaisedBevelBorder {
		public LuaObject call(LuaInterpreter interp, LuaObject[] args) {
			return new LuaBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
		}
	},
	AssignPopover {
		public LuaObject call(LuaInterpreter interp, LuaObject[] args) {
			Lua.checkArgs("AssignPopover", args, LuaType.USERDATA, LuaType.USERDATA);
			LuaComponent lcMain = (LuaComponent) args[0];
			LuaComponent lcPopover = (LuaComponent) args[1];
			return new LuaPopover(interp, lcMain.getComponent(), lcPopover.getComponent());
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
	},
	ConvertJSONToComponent {
		public LuaObject call(LuaInterpreter interp, LuaObject[] args) {
			Lua.checkArgs("ConvertJSONToComponent", args, LuaType.STRING);
			String jsonPath = args[0].getString();
			File jsonFile = new File(jsonPath);

			JSONParser parser = new JSONParser();

			try {
				Reader in = new FileReader(jsonFile);
				JSONObject json = (JSONObject) parser.parse(in);
				JComponent comp = SwingJSONParser.parseComponent(json);
				return Lua.newString(comp.toString());
			} catch (Exception e) {
				e.printStackTrace();
			}
			return Lua.NIL;
		}
	},
	ConvertComponentToJSON {
		public LuaObject call(LuaInterpreter interp, LuaObject[] args) {
			Lua.checkArgs("ConvertComponentToJSON", args, LuaType.USERDATA);
			if (!((args[0] instanceof LuaComponent) || (args[0] instanceof LuaBasicFrame)
					|| (args[0] instanceof LuaFrame))) {
				throw Lua.badArgument(0, "ConvertComponentToJSON", "LuaComponent expected");
			}
			Hashtable<Object, UUID> compIds = null;

			if (args.length > 1) {
				compIds = new Hashtable<Object, UUID>();
				Set<Entry<LuaObject, LuaObject>> entries = args[1].getEntries();
				for (Entry<LuaObject, LuaObject> e : entries) {
					LuaObject compObj = e.getKey();
					LuaObject uuidObj = e.getValue();
					UUID uuid;
					if (!(uuidObj instanceof LuaUUID))
						continue;
					uuid = ((LuaUUID) uuidObj).getUUID();

					if (compObj == null)
						continue;
					compIds.put(compObj, uuid);
				}
			}
			JSONObject json = SwingJSONParser.convertToJSON(args[0], compIds);
			return Lua.newString(json.toJSONString());
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
