package jshdesktop.lua;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

import javax.swing.JComponent;

import com.hk.lua.Lua;
import com.hk.lua.Lua.LuaMethod;
import com.hk.lua.LuaInterpreter;
import com.hk.lua.LuaObject;
import com.hk.lua.LuaType;
import com.hk.lua.LuaUserdata;

import jshdesktop.lua.image.LuaImageWrapper;

public class LuaComponent extends LuaUserdata {
	private JComponent internalComp;
	private LuaInterpreter interp;
	private LuaObject paintFunction = null;

	private static final LuaObject luaComponentMetatable = Lua.newTable();

	public LuaComponent(LuaInterpreter interp) {
		this.interp = interp;
		internalComp = new _LuaComponent();
		metatable = luaComponentMetatable;
	}

	public LuaComponent(LuaInterpreter interp, JComponent internalComp) {
		this.interp = interp;
		this.internalComp = internalComp;
		metatable = luaComponentMetatable;
	}

	public void setJComponent(JComponent internalComp) {
		if (internalComp == null)
			internalComp = new _LuaComponent();
		else
			this.internalComp = internalComp;
	}

	@Override
	public Object getUserdata() {
		return internalComp;
	}

	@Override
	public String name() {
		return "LuaComponent";
	}

	private class _LuaComponent extends JComponent {

		private static final long serialVersionUID = -8757032357422851131L;

		@Override
		public void paintComponent(Graphics g) {
			if (paintFunction != null) {
				LuaGraphicsWrapper lgw = new LuaGraphicsWrapper(g);
				paintFunction.call(interp, lgw);
			}
		}

	}

	public JComponent getComponent() {
		return internalComp;
	}

	public class LuaGraphicsWrapper extends LuaUserdata {
		private Graphics g;
		private static final LuaObject luaGraphicsWrapperMetatable = Lua.newTable();

		public LuaGraphicsWrapper(Graphics g) {
			this.g = g;
			metatable = luaGraphicsWrapperMetatable;
		}

		@Override
		public Object getUserdata() {
			return this;
		}

		private Graphics getGraphics() {
			return g;
		}

		@Override
		public String name() {
			return "Graphics";
		}

		static {
			luaGraphicsWrapperMetatable.rawSet("__name", Lua.newString("GRAPHICS"));
			luaGraphicsWrapperMetatable.rawSet("__index", luaGraphicsWrapperMetatable);

			LuaObject clearRectFunction = Lua.newFunc(new Consumer<LuaObject[]>() {

				@Override
				public void accept(LuaObject[] args) {
					Lua.checkArgs("ClearRect", args, LuaType.USERDATA, LuaType.INTEGER, LuaType.INTEGER,
							LuaType.INTEGER, LuaType.INTEGER);

					LuaGraphicsWrapper lgw = (LuaGraphicsWrapper) args[0];
					int x = args[1].getInt();
					int y = args[2].getInt();
					int width = args[3].getInt();
					int height = args[4].getInt();
					lgw.getGraphics().clearRect(x, y, width, height);
				}

			});

			LuaObject clipRectFunction = Lua.newFunc(new Consumer<LuaObject[]>() {

				@Override
				public void accept(LuaObject[] args) {
					Lua.checkArgs("ClipRect", args, LuaType.USERDATA, LuaType.INTEGER, LuaType.INTEGER, LuaType.INTEGER,
							LuaType.INTEGER);

					LuaGraphicsWrapper lgw = (LuaGraphicsWrapper) args[0];
					int x = args[1].getInt();
					int y = args[2].getInt();
					int width = args[3].getInt();
					int height = args[4].getInt();
					lgw.getGraphics().clipRect(x, y, width, height);
				}

			});

			LuaObject copyAreaFunction = Lua.newFunc(new Consumer<LuaObject[]>() {

				@Override
				public void accept(LuaObject[] args) {
					Lua.checkArgs("CopyArea", args, LuaType.USERDATA, LuaType.INTEGER, LuaType.INTEGER, LuaType.INTEGER,
							LuaType.INTEGER, LuaType.INTEGER, LuaType.INTEGER);

					LuaGraphicsWrapper lgw = (LuaGraphicsWrapper) args[0];
					int x = args[1].getInt();
					int y = args[2].getInt();
					int width = args[3].getInt();
					int height = args[4].getInt();
					int dx = args[5].getInt();
					int dy = args[6].getInt();
					lgw.getGraphics().copyArea(x, y, width, height, dx, dy);
				}

			});

			LuaObject draw3DRectFunction = Lua.newFunc(new Consumer<LuaObject[]>() {

				@Override
				public void accept(LuaObject[] args) {
					Lua.checkArgs("Draw3dRect", args, LuaType.USERDATA, LuaType.INTEGER, LuaType.INTEGER,
							LuaType.INTEGER, LuaType.INTEGER, LuaType.BOOLEAN);

					LuaGraphicsWrapper lgw = (LuaGraphicsWrapper) args[0];
					int x = args[1].getInt();
					int y = args[2].getInt();
					int width = args[3].getInt();
					int height = args[4].getInt();
					boolean raised = args[5].getBoolean();
					lgw.getGraphics().draw3DRect(x, y, width, height, raised);
				}

			});

			LuaObject drawArcFunction = Lua.newFunc(new Consumer<LuaObject[]>() {

				@Override
				public void accept(LuaObject[] args) {
					Lua.checkArgs("DrawArc", args, LuaType.USERDATA, LuaType.INTEGER, LuaType.INTEGER, LuaType.INTEGER,
							LuaType.INTEGER, LuaType.INTEGER, LuaType.INTEGER);

					LuaGraphicsWrapper lgw = (LuaGraphicsWrapper) args[0];
					int x = args[1].getInt();
					int y = args[2].getInt();
					int width = args[3].getInt();
					int height = args[4].getInt();
					int startAngle = args[5].getInt();
					int arcAngle = args[6].getInt();
					lgw.getGraphics().drawArc(x, y, width, height, startAngle, arcAngle);
				}

			});

			LuaObject drawImageFunction = Lua.newFunc(new Consumer<LuaObject[]>() {

				@Override
				public void accept(LuaObject[] args) {
					Lua.checkArgs("DrawImage", args, LuaType.USERDATA, LuaType.USERDATA, LuaType.INTEGER,
							LuaType.INTEGER);

					LuaGraphicsWrapper lgw = (LuaGraphicsWrapper) args[0];
					LuaImageWrapper li = (LuaImageWrapper) args[1];
					int x = args[2].getInt();
					int y = args[3].getInt();
					lgw.getGraphics().drawImage(li.getImage(), x, y, null);
				}

			});

			LuaObject drawLineFunction = Lua.newFunc(new Consumer<LuaObject[]>() {

				@Override
				public void accept(LuaObject[] args) {
					Lua.checkArgs("DrawLine", args, LuaType.USERDATA, LuaType.INTEGER, LuaType.INTEGER, LuaType.INTEGER,
							LuaType.INTEGER);

					LuaGraphicsWrapper lgw = (LuaGraphicsWrapper) args[0];
					int x1 = args[1].getInt();
					int y1 = args[2].getInt();
					int x2 = args[3].getInt();
					int y2 = args[4].getInt();
					lgw.getGraphics().drawLine(x1, y1, x2, y2);
				}

			});

			LuaObject drawOvalFunction = Lua.newFunc(new Consumer<LuaObject[]>() {

				@Override
				public void accept(LuaObject[] args) {
					Lua.checkArgs("DrawOval", args, LuaType.USERDATA, LuaType.INTEGER, LuaType.INTEGER, LuaType.INTEGER,
							LuaType.INTEGER);

					LuaGraphicsWrapper lgw = (LuaGraphicsWrapper) args[0];
					int x = args[1].getInt();
					int y = args[2].getInt();
					int width = args[3].getInt();
					int height = args[4].getInt();
					lgw.getGraphics().drawOval(x, y, width, height);
				}

			});

			LuaObject drawPolygonFunction = Lua.newFunc(new Consumer<LuaObject[]>() {

				@Override
				public void accept(LuaObject[] args) {
					Lua.checkArgs("DrawPolygon", args, LuaType.USERDATA, LuaType.TABLE);

					Set<Map.Entry<LuaObject, LuaObject>> table = args[1].getEntries();
					Iterator<Map.Entry<LuaObject, LuaObject>> iterator = table.iterator();
					int[] xPoints = new int[table.size()];
					int[] yPoints = new int[table.size()];
					int nPoints = table.size();

					int i = 0;
					while (iterator.hasNext()) {
						Map.Entry<LuaObject, LuaObject> entry = iterator.next();
						xPoints[i] = entry.getKey().getInt();
						yPoints[i] = entry.getValue().getInt();
						i++;
					}

					LuaGraphicsWrapper lgw = (LuaGraphicsWrapper) args[0];
					lgw.getGraphics().drawPolygon(xPoints, yPoints, nPoints);
				}

			});

			LuaObject drawPolyLinesFunction = Lua.newFunc(new Consumer<LuaObject[]>() {

				@Override
				public void accept(LuaObject[] args) {
					Lua.checkArgs("DrawPolyLines", args, LuaType.USERDATA, LuaType.TABLE);

					Set<Map.Entry<LuaObject, LuaObject>> table = args[1].getEntries();
					Iterator<Map.Entry<LuaObject, LuaObject>> iterator = table.iterator();
					int[] xPoints = new int[table.size()];
					int[] yPoints = new int[table.size()];
					int nPoints = table.size();

					int i = 0;
					while (iterator.hasNext()) {
						Map.Entry<LuaObject, LuaObject> entry = iterator.next();
						xPoints[i] = entry.getKey().getInt();
						yPoints[i] = entry.getValue().getInt();
						i++;
					}

					LuaGraphicsWrapper lgw = (LuaGraphicsWrapper) args[0];
					lgw.getGraphics().drawPolyline(xPoints, yPoints, nPoints);
				}

			});

			LuaObject drawRectFunction = Lua.newFunc(new Consumer<LuaObject[]>() {

				@Override
				public void accept(LuaObject[] args) {
					Lua.checkArgs("DrawRect", args, LuaType.USERDATA, LuaType.INTEGER, LuaType.INTEGER, LuaType.INTEGER,
							LuaType.INTEGER);

					LuaGraphicsWrapper lgw = (LuaGraphicsWrapper) args[0];
					int x = args[1].getInt();
					int y = args[2].getInt();
					int width = args[3].getInt();
					int height = args[4].getInt();
					lgw.getGraphics().drawRect(x, y, width, height);
				}

			});

			LuaObject drawRoundRectFunction = Lua.newFunc(new Consumer<LuaObject[]>() {

				@Override
				public void accept(LuaObject[] args) {
					Lua.checkArgs("DrawRoundRect", args, LuaType.USERDATA, LuaType.INTEGER, LuaType.INTEGER,
							LuaType.INTEGER, LuaType.INTEGER, LuaType.INTEGER, LuaType.INTEGER);

					LuaGraphicsWrapper lgw = (LuaGraphicsWrapper) args[0];
					int x = args[1].getInt();
					int y = args[2].getInt();
					int width = args[3].getInt();
					int height = args[4].getInt();
					int arcWidth = args[5].getInt();
					int arcHeight = args[6].getInt();
					lgw.getGraphics().drawRoundRect(x, y, width, height, arcWidth, arcHeight);
				}

			});

			LuaObject drawStringFunction = Lua.newFunc(new Consumer<LuaObject[]>() {

				@Override
				public void accept(LuaObject[] args) {
					Lua.checkArgs("DrawString", args, LuaType.USERDATA, LuaType.ANY, LuaType.INTEGER, LuaType.INTEGER);

					LuaGraphicsWrapper lgw = (LuaGraphicsWrapper) args[0];
					String text = args[1].getString();
					int x = args[2].getInt();
					int y = args[3].getInt();

					lgw.getGraphics().drawString(text, x, y);
				}

			});

			LuaObject fill3DRectFunction = Lua.newFunc(new Consumer<LuaObject[]>() {

				@Override
				public void accept(LuaObject[] args) {
					Lua.checkArgs("Fill3DRect", args, LuaType.USERDATA, LuaType.INTEGER, LuaType.INTEGER,
							LuaType.INTEGER, LuaType.INTEGER, LuaType.BOOLEAN);

					LuaGraphicsWrapper lgw = (LuaGraphicsWrapper) args[0];
					int x = args[1].getInt();
					int y = args[2].getInt();
					int width = args[3].getInt();
					int height = args[4].getInt();
					boolean raised = args[5].getBoolean();
					lgw.getGraphics().fill3DRect(x, y, width, height, raised);
				}

			});

			LuaObject fillArcFunction = Lua.newFunc(new Consumer<LuaObject[]>() {

				@Override
				public void accept(LuaObject[] args) {
					Lua.checkArgs("FillArc", args, LuaType.USERDATA, LuaType.INTEGER, LuaType.INTEGER, LuaType.INTEGER,
							LuaType.INTEGER, LuaType.INTEGER, LuaType.INTEGER);

					LuaGraphicsWrapper lgw = (LuaGraphicsWrapper) args[0];
					int x = args[1].getInt();
					int y = args[2].getInt();
					int width = args[3].getInt();
					int height = args[4].getInt();
					int startAngle = args[5].getInt();
					int arcAngle = args[6].getInt();
					lgw.getGraphics().fillArc(x, y, width, height, startAngle, arcAngle);
				}

			});

			LuaObject fillOvalFunction = Lua.newFunc(new Consumer<LuaObject[]>() {

				@Override
				public void accept(LuaObject[] args) {
					Lua.checkArgs("FillOval", args, LuaType.USERDATA, LuaType.INTEGER, LuaType.INTEGER, LuaType.INTEGER,
							LuaType.INTEGER);

					LuaGraphicsWrapper lgw = (LuaGraphicsWrapper) args[0];
					int x = args[1].getInt();
					int y = args[2].getInt();
					int width = args[3].getInt();
					int height = args[4].getInt();
					lgw.getGraphics().fillOval(x, y, width, height);
				}

			});

			LuaObject fillPolygonFunction = Lua.newFunc(new Consumer<LuaObject[]>() {

				@Override
				public void accept(LuaObject[] args) {
					Lua.checkArgs("FillPolygon", args, LuaType.USERDATA, LuaType.TABLE);

					Set<Map.Entry<LuaObject, LuaObject>> table = args[1].getEntries();
					Iterator<Map.Entry<LuaObject, LuaObject>> iterator = table.iterator();
					int[] xPoints = new int[table.size()];
					int[] yPoints = new int[table.size()];
					int nPoints = table.size();

					int i = 0;
					while (iterator.hasNext()) {
						Map.Entry<LuaObject, LuaObject> entry = iterator.next();
						xPoints[i] = entry.getKey().getInt();
						yPoints[i] = entry.getValue().getInt();
						i++;
					}

					LuaGraphicsWrapper lgw = (LuaGraphicsWrapper) args[0];
					lgw.getGraphics().fillPolygon(xPoints, yPoints, nPoints);
				}

			});

			LuaObject fillRectFunction = Lua.newFunc(new Consumer<LuaObject[]>() {

				@Override
				public void accept(LuaObject[] args) {
					Lua.checkArgs("FillRect", args, LuaType.USERDATA, LuaType.INTEGER, LuaType.INTEGER, LuaType.INTEGER,
							LuaType.INTEGER);

					LuaGraphicsWrapper lgw = (LuaGraphicsWrapper) args[0];
					int x = args[1].getInt();
					int y = args[2].getInt();
					int width = args[3].getInt();
					int height = args[4].getInt();
					lgw.getGraphics().fillRect(x, y, width, height);
				}

			});

			LuaObject fillRoundRectFunction = Lua.newFunc(new Consumer<LuaObject[]>() {

				@Override
				public void accept(LuaObject[] args) {
					Lua.checkArgs("DrawRoundRect", args, LuaType.USERDATA, LuaType.INTEGER, LuaType.INTEGER,
							LuaType.INTEGER, LuaType.INTEGER, LuaType.INTEGER, LuaType.INTEGER);

					LuaGraphicsWrapper lgw = (LuaGraphicsWrapper) args[0];
					int x = args[1].getInt();
					int y = args[2].getInt();
					int width = args[3].getInt();
					int height = args[4].getInt();
					int arcWidth = args[5].getInt();
					int arcHeight = args[6].getInt();
					lgw.getGraphics().fillRoundRect(x, y, width, height, arcWidth, arcHeight);
				}

			});

			LuaObject setColorFunction = Lua.newFunc(new Consumer<LuaObject[]>() {

				@Override
				public void accept(LuaObject[] args) {
					Lua.checkArgs("SetColor", args, LuaType.USERDATA, LuaType.STRING);

					LuaGraphicsWrapper lgw = (LuaGraphicsWrapper) args[0];

					Color newColor = null;

					try {
						Field colorField = Class.forName("java.awt.Color").getField(args[1].getString());
						newColor = (Color) colorField.get(null);
					} catch (Exception e) {
						newColor = null;
					}

					lgw.getGraphics().setColor(newColor);
				}

			});

			LuaObject getColorFunction = Lua.newMethod(new LuaMethod() {

				@Override
				public LuaObject call(LuaInterpreter arg0, LuaObject[] args) {
					Lua.checkArgs("GetColor", args, LuaType.USERDATA);

					LuaGraphicsWrapper lgw = (LuaGraphicsWrapper) args[0];
					return Lua.newString(lgw.getGraphics().getColor().toString());
				}

			});

			LuaObject setFontFunction = Lua.newFunc(new Consumer<LuaObject[]>() {

				@Override
				public void accept(LuaObject[] args) {
					Lua.checkArgs("SetFont", args, LuaType.USERDATA, LuaType.STRING);

					LuaGraphicsWrapper lgw = (LuaGraphicsWrapper) args[0];
					lgw.getGraphics().setFont(Font.getFont(args[1].getString()));
				}

			});

			LuaObject getFontFunction = Lua.newMethod(new LuaMethod() {

				@Override
				public LuaObject call(LuaInterpreter arg0, LuaObject[] args) {
					Lua.checkArgs("DrawRoundRect", args, LuaType.USERDATA);

					LuaGraphicsWrapper lgw = (LuaGraphicsWrapper) args[0];
					return Lua.newString(lgw.getGraphics().getFont().toString());
				}

			});

			luaGraphicsWrapperMetatable.rawSet("ClearRect", clearRectFunction);
			luaGraphicsWrapperMetatable.rawSet("ClipRect", clipRectFunction);
			luaGraphicsWrapperMetatable.rawSet("CopyArea", copyAreaFunction);
			luaGraphicsWrapperMetatable.rawSet("Draw3dRect", draw3DRectFunction);
			luaGraphicsWrapperMetatable.rawSet("DrawArc", drawArcFunction);
			luaGraphicsWrapperMetatable.rawSet("DrawImage", drawImageFunction);
			luaGraphicsWrapperMetatable.rawSet("DrawLine", drawLineFunction);
			luaGraphicsWrapperMetatable.rawSet("DrawOval", drawOvalFunction);
			luaGraphicsWrapperMetatable.rawSet("DrawPolygon", drawPolygonFunction);
			luaGraphicsWrapperMetatable.rawSet("DrawPolyLines", drawPolyLinesFunction);
			luaGraphicsWrapperMetatable.rawSet("DrawRect", drawRectFunction);
			luaGraphicsWrapperMetatable.rawSet("DrawRoundRect", drawRoundRectFunction);
			luaGraphicsWrapperMetatable.rawSet("DrawString", drawStringFunction);
			luaGraphicsWrapperMetatable.rawSet("Fill3dRect", fill3DRectFunction);
			luaGraphicsWrapperMetatable.rawSet("FillArc", fillArcFunction);
			luaGraphicsWrapperMetatable.rawSet("FillOval", fillOvalFunction);
			luaGraphicsWrapperMetatable.rawSet("FillPolygon", fillPolygonFunction);
			luaGraphicsWrapperMetatable.rawSet("FillRect", fillRectFunction);
			luaGraphicsWrapperMetatable.rawSet("FillRoundRect", fillRoundRectFunction);
			luaGraphicsWrapperMetatable.rawSet("SetColor", setColorFunction);
			luaGraphicsWrapperMetatable.rawSet("GetColor", getColorFunction);
			luaGraphicsWrapperMetatable.rawSet("SetFont", setFontFunction);
			luaGraphicsWrapperMetatable.rawSet("GetFont", getFontFunction);
		}
	}

	static {
		luaComponentMetatable.rawSet("__name", Lua.newString("COMPONENT"));
		luaComponentMetatable.rawSet("__index", luaComponentMetatable);

		LuaObject setSizeFunction = Lua.newFunc(new Consumer<LuaObject[]>() {

			@Override
			public void accept(LuaObject[] args) {
				Lua.checkArgs("SetSize", args, LuaType.USERDATA, LuaType.INTEGER, LuaType.INTEGER);
				LuaComponent lc = (LuaComponent) args[0];
				lc.internalComp.setSize(args[1].getInt(), args[2].getInt());
			}

		});

		LuaObject getSizeFunction = Lua.newMethod(new LuaMethod() {

			@Override
			public LuaObject call(LuaInterpreter arg0, LuaObject[] args) {
				Lua.checkArgs("GetSize", args, LuaType.USERDATA);
				LuaComponent lc = (LuaComponent) args[0];

				LuaObject dimensionTable = Lua.newTable();
				dimensionTable.rawSet(0, lc.internalComp.getWidth());
				dimensionTable.rawSet(1, lc.internalComp.getHeight());
				return dimensionTable;
			}

		});

		LuaObject addLuaComponentFunction = Lua.newFunc(new Consumer<LuaObject[]>() {

			@Override
			public void accept(LuaObject[] args) {
				Lua.checkArgs("AddComponent", args, LuaType.USERDATA, LuaType.USERDATA);
				LuaComponent main = (LuaComponent) args[0];
				LuaComponent toAdd = (LuaComponent) args[1];
				main.internalComp.add(toAdd.internalComp);
			}

		});

		LuaObject removeLuaComponentFunction = Lua.newFunc(new Consumer<LuaObject[]>() {

			@Override
			public void accept(LuaObject[] args) {
				Lua.checkArgs("RemoveComponent", args, LuaType.USERDATA, LuaType.USERDATA);
				LuaComponent main = (LuaComponent) args[0];
				LuaComponent toAdd = (LuaComponent) args[1];
				main.internalComp.remove(toAdd.internalComp);
			}

		});

		LuaObject setBoundsFunction = Lua.newFunc(new Consumer<LuaObject[]>() {

			@Override
			public void accept(LuaObject[] args) {
				Lua.checkArgs("SetBounds", args, LuaType.USERDATA, LuaType.INTEGER, LuaType.INTEGER, LuaType.INTEGER,
						LuaType.INTEGER);
				LuaComponent lc = (LuaComponent) args[0];
				int width = args[3].getInt();
				int height = args[4].getInt();
				int x = args[1].getInt();
				int y = args[2].getInt();
				lc.internalComp.setBounds(x, y, width, height);
			}

		});

		LuaObject setGraphicsEventHandler = Lua.newFunc(new Consumer<LuaObject[]>() {

			@Override
			public void accept(LuaObject[] args) {
				Lua.checkArgs("SetGraphicsEventHandler", args, LuaType.USERDATA, LuaType.ANY);
				LuaComponent lc = (LuaComponent) args[0];
				lc.paintFunction = args[1];
			}

		});

		LuaObject repaintFunction = Lua.newFunc(new Consumer<LuaObject[]>() {

			@Override
			public void accept(LuaObject[] args) {
				Lua.checkArgs("Repaint", args, LuaType.USERDATA);
				LuaComponent lc = (LuaComponent) args[0];
				lc.internalComp.repaint();
			}

		});

		LuaObject setLocationFunction = Lua.newFunc(new Consumer<LuaObject[]>() {

			@Override
			public void accept(LuaObject[] args) {
				Lua.checkArgs("SetLocation", args, LuaType.USERDATA, LuaType.INTEGER, LuaType.INTEGER);
				LuaComponent lc = (LuaComponent) args[0];
				lc.getComponent().setLocation(args[1].getInt(), args[2].getInt());
			}

		});

		LuaObject getLocationFunction = Lua.newMethod(new LuaMethod() {

			@Override
			public LuaObject call(LuaInterpreter interp, LuaObject[] args) {
				Lua.checkArgs("GetLocation", args, LuaType.USERDATA);
				LuaComponent lc = (LuaComponent) args[0];
				LuaObject positionTable = Lua.newTable();
				positionTable.rawSet(0, lc.internalComp.getX());
				positionTable.rawSet(1, lc.internalComp.getY());
				return positionTable;
			}

		});

		luaComponentMetatable.rawSet("SetSize", setSizeFunction);
		luaComponentMetatable.rawSet("GetSize", getSizeFunction);
		luaComponentMetatable.rawSet("AddComponent", addLuaComponentFunction);
		luaComponentMetatable.rawSet("RemoveComponent", removeLuaComponentFunction);
		luaComponentMetatable.rawSet("SetBounds", setBoundsFunction);
		luaComponentMetatable.rawSet("SetGraphicsEventHandler", setGraphicsEventHandler);
		luaComponentMetatable.rawSet("Repaint", repaintFunction);
		luaComponentMetatable.rawSet("SetLocation", setLocationFunction);
		luaComponentMetatable.rawSet("GetLocation", getLocationFunction);
	}

}
