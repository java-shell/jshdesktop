package jshdesktop.json;

import java.awt.Component;
import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Base64;
import java.util.Hashtable;
import java.util.UUID;

import javax.swing.AbstractButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.border.Border;
import javax.swing.text.JTextComponent;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.hk.lua.Lua;
import com.hk.lua.LuaInterpreter;

import jshdesktop.lua.LuaComponent;
import jshdesktop.lua.LuaComponent._LuaComponent;
import jshdesktop.lua.frame.LuaBasicFrame;
import jshdesktop.lua.frame.LuaFrame;
import terra.shell.logging.LogManager;
import terra.shell.modules.ModuleManagement;

public class SwingJSONParser {

	private static final JSONParser parser = new JSONParser();

	public static JComponent parseComponent(String jsonDescriptor)
			throws ParseException, ClassNotFoundException, InstantiationException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException, SecurityException, NoSuchMethodException {
		JSONObject json = (JSONObject) parser.parse(jsonDescriptor);
		return parseComponent(json);
	}

	public static JComponent parseComponent(JSONObject json)
			throws ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, SecurityException, NoSuchMethodException {
		JSONObject topLevel = (JSONObject) json.get("header");
		String topLevelType = (String) topLevel.get("type");

		Class<?> topLevelClass = ModuleManagement.getLoader().loadClass(topLevelType);
		LogManager.out.println(topLevelType);

		JComponent topLevelComp;
		LuaInterpreter interp;

		if (topLevelType.equals(_LuaComponent.class.getName())) {
			interp = Lua.interpreter("");
			Constructor<?> topLevelConstructor = topLevelClass.getDeclaredConstructor(LuaComponent.class);
			topLevelComp = (JComponent) topLevelConstructor.newInstance(new LuaComponent(interp));
		} else
			topLevelComp = (JComponent) topLevelClass.getDeclaredConstructor().newInstance();

		return topLevelComp;
	}

	@SuppressWarnings("unchecked")
	public static JSONObject convertToJSON(Object comp, Hashtable<Object, UUID> compIds) {
		JSONObject json;
		if (comp instanceof JComponent) {
			json = parseJComponent((JComponent) comp, compIds);
		} else if (comp instanceof LuaComponent) {
			json = parseLuaComponent((LuaComponent) comp, compIds);
		} else if (comp instanceof LuaBasicFrame) {
			json = parseLuaBasicFrame((LuaBasicFrame) comp, compIds);
		} else if (comp instanceof LuaFrame) {
			json = parseLuaFrame((LuaFrame) comp, compIds);
		} else {
			json = null;
		}

		return json;
	}

	@SuppressWarnings("unchecked")
	private static JSONObject parseJComponent(JComponent comp, Hashtable<Object, UUID> compIds) {
		JSONObject json = new JSONObject();
		JSONObject compHeader = new JSONObject();

		JSONObject sizeTable = new JSONObject();
		sizeTable.put("width", comp.getSize().getWidth());
		sizeTable.put("height", comp.getSize().getHeight());
		compHeader.put("size", sizeTable);

		JSONObject locationTable = new JSONObject();
		locationTable.put("x", comp.getLocation().getX());
		locationTable.put("y", comp.getLocation().getY());
		compHeader.put("location", locationTable);

		JSONObject extendedDataTable = new JSONObject();
		extendedDataTable.put("foreground", comp.getForeground().toString());
		extendedDataTable.put("background", comp.getBackground().toString());
		if (comp instanceof JComponent) {
			JComponent jcomp = (JComponent) comp;
			extendedDataTable.put("tooltiptext", jcomp.getToolTipText());

			compHeader.put("type", jcomp.getClass().getName());

			Component[] components = jcomp.getComponents();
			JSONObject[] descriptors = new JSONObject[components.length];
			for (int i = 0; i < components.length; i++) {
				descriptors[i] = convertToJSON(components[i], compIds);
			}

			if (jcomp instanceof AbstractButton) {
				extendedDataTable.put("text", ((AbstractButton) jcomp).getText());
			}

			if (jcomp instanceof JLabel) {
				extendedDataTable.put("text", ((JLabel) jcomp).getText());
			}

			if (jcomp instanceof JTextComponent) {
				extendedDataTable.put("text", ((JTextComponent) jcomp).getText());
			}

			try {
				Border border = jcomp.getBorder();

				if (border == null) {
					extendedDataTable.put("border", null);
				} else if (border instanceof Serializable) {
					ByteArrayOutputStream bout = new ByteArrayOutputStream();
					ObjectOutputStream objOut = new ObjectOutputStream(bout);
					objOut.writeObject(border);
					bout.flush();
					bout.close();

					String borderString = Base64.getEncoder().encodeToString(bout.toByteArray());

					extendedDataTable.put("border", borderString);
				}
			} catch (Exception e) {
			}

			compHeader.put("subcomps", descriptors);
		}

		if (compIds != null && compIds.containsKey(comp)) {
			json.put("compid", compIds.get(comp).toString());
		}

		json.put("header", compHeader);
		json.put("extended", extendedDataTable);
		return json;
	}

	@SuppressWarnings("unchecked")
	private static JSONObject parseLuaComponent(LuaComponent lc, Hashtable<Object, UUID> compIds) {
		JSONObject json = new JSONObject();
		JSONObject compHeader = new JSONObject();

		JSONObject sizeTable = new JSONObject();
		sizeTable.put("width", lc.getComponent().getSize().getWidth());
		sizeTable.put("height", lc.getComponent().getSize().getHeight());
		compHeader.put("size", sizeTable);

		JSONObject locationTable = new JSONObject();
		locationTable.put("x", lc.getComponent().getLocation().getX());
		locationTable.put("y", lc.getComponent().getLocation().getY());
		compHeader.put("location", locationTable);

		JSONObject extendedDataTable = new JSONObject();
		extendedDataTable.put("foreground", lc.getComponent().getForeground().toString());
		extendedDataTable.put("background", lc.getComponent().getBackground().toString());
		if (lc.getComponent() instanceof JComponent) {
			JComponent jcomp = lc.getComponent();
			extendedDataTable.put("tooltiptext", jcomp.getToolTipText());

			compHeader.put("type", jcomp.getClass().getName());

			Component[] components = jcomp.getComponents();
			JSONObject[] descriptors = new JSONObject[components.length];
			for (int i = 0; i < components.length; i++) {
				descriptors[i] = convertToJSON(components[i], compIds);
			}

			if (jcomp instanceof AbstractButton) {
				extendedDataTable.put("text", ((AbstractButton) jcomp).getText());
			}

			if (jcomp instanceof JLabel) {
				extendedDataTable.put("text", ((JLabel) jcomp).getText());
			}

			if (jcomp instanceof JTextComponent) {
				extendedDataTable.put("text", ((JTextComponent) jcomp).getText());
			}

			try {
				Border border = jcomp.getBorder();

				if (border == null) {
					extendedDataTable.put("border", null);
				} else if (border instanceof Serializable) {
					ByteArrayOutputStream bout = new ByteArrayOutputStream();
					ObjectOutputStream objOut = new ObjectOutputStream(bout);
					objOut.writeObject(border);
					bout.flush();
					bout.close();

					String borderString = Base64.getEncoder().encodeToString(bout.toByteArray());

					extendedDataTable.put("border", borderString);
				}
			} catch (Exception e) {
			}

			compHeader.put("subcomps", descriptors);
		}

		if (compIds != null && compIds.containsKey(lc)) {
			json.put("compid", compIds.get(lc).toString());
		}

		try {
			ByteArrayOutputStream lcOut = new ByteArrayOutputStream();
			ObjectOutputStream lcObjOut = new ObjectOutputStream(lcOut);
			lcObjOut.writeObject(lc);
			lcOut.flush();
			lcOut.close();

			String lcString = Base64.getEncoder().encodeToString(lcOut.toByteArray());
			compHeader.put("luadescriptor", lcString);
		} catch (Exception e) {
			e.printStackTrace();
		}

		compHeader.put("luatype", lc.getClass().getName());

		json.put("header", compHeader);
		json.put("extended", extendedDataTable);
		return json;
	}

	@SuppressWarnings("unchecked")
	private static JSONObject parseLuaBasicFrame(LuaBasicFrame lb, Hashtable<Object, UUID> compIds) {
		JSONObject json = new JSONObject();
		JSONObject compHeader = new JSONObject();

		JSONObject sizeTable = new JSONObject();
		sizeTable.put("width", lb.getBasicFrame().getSize().getWidth());
		sizeTable.put("height", lb.getBasicFrame().getSize().getHeight());
		compHeader.put("size", sizeTable);

		JSONObject locationTable = new JSONObject();
		locationTable.put("x", lb.getBasicFrame().getLocation().getX());
		locationTable.put("y", lb.getBasicFrame().getLocation().getY());
		compHeader.put("location", locationTable);

		JSONObject extendedDataTable = new JSONObject();
		extendedDataTable.put("foreground", lb.getBasicFrame().getForeground().toString());
		extendedDataTable.put("background", lb.getBasicFrame().getBackground().toString());
		if (lb.getBasicFrame() instanceof JComponent) {
			JComponent jcomp = lb.getBasicFrame();
			extendedDataTable.put("tooltiptext", jcomp.getToolTipText());

			compHeader.put("type", jcomp.getClass().getName());

			Component[] components = jcomp.getComponents();
			JSONObject[] descriptors = new JSONObject[components.length];
			for (int i = 0; i < components.length; i++) {
				descriptors[i] = convertToJSON(components[i], compIds);
			}

			if (jcomp instanceof AbstractButton) {
				extendedDataTable.put("text", ((AbstractButton) jcomp).getText());
			}

			if (jcomp instanceof JLabel) {
				extendedDataTable.put("text", ((JLabel) jcomp).getText());
			}

			if (jcomp instanceof JTextComponent) {
				extendedDataTable.put("text", ((JTextComponent) jcomp).getText());
			}

			try {
				Border border = jcomp.getBorder();

				if (border == null) {
					extendedDataTable.put("border", null);
				} else if (border instanceof Serializable) {
					ByteArrayOutputStream bout = new ByteArrayOutputStream();
					ObjectOutputStream objOut = new ObjectOutputStream(bout);
					objOut.writeObject(border);
					bout.flush();
					bout.close();

					String borderString = Base64.getEncoder().encodeToString(bout.toByteArray());

					extendedDataTable.put("border", borderString);
				}
			} catch (Exception e) {
			}

			compHeader.put("subcomps", descriptors);
		}

		if (compIds != null && compIds.containsKey(lb)) {
			json.put("compid", compIds.get(lb).toString());
		}

		try {
			ByteArrayOutputStream lcOut = new ByteArrayOutputStream();
			ObjectOutputStream lcObjOut = new ObjectOutputStream(lcOut);
			lcObjOut.writeObject(lb);
			lcOut.flush();
			lcOut.close();

			String lcString = Base64.getEncoder().encodeToString(lcOut.toByteArray());
			compHeader.put("luadescriptor", lcString);
		} catch (Exception e) {
			e.printStackTrace();
		}

		compHeader.put("luatype", lb.getClass().getName());

		json.put("header", compHeader);
		json.put("extended", extendedDataTable);
		return json;
	}

	@SuppressWarnings("unchecked")
	private static JSONObject parseLuaFrame(LuaFrame lf, Hashtable<Object, UUID> compIds) {
		JSONObject json = new JSONObject();
		JSONObject compHeader = new JSONObject();

		JSONObject sizeTable = new JSONObject();
		sizeTable.put("width", lf.getFrame().getSize().getWidth());
		sizeTable.put("height", lf.getFrame().getSize().getHeight());
		compHeader.put("size", sizeTable);

		JSONObject locationTable = new JSONObject();
		locationTable.put("x", lf.getFrame().getLocation().getX());
		locationTable.put("y", lf.getFrame().getLocation().getY());
		compHeader.put("location", locationTable);

		JSONObject extendedDataTable = new JSONObject();
		extendedDataTable.put("foreground", lf.getFrame().getForeground().toString());
		extendedDataTable.put("background", lf.getFrame().getBackground().toString());
		JFrame jcomp = lf.getFrame();

		compHeader.put("type", jcomp.getClass().getName());

		Component[] components = jcomp.getComponents();
		JSONObject[] descriptors = new JSONObject[components.length];
		for (int i = 0; i < components.length; i++) {
			descriptors[i] = convertToJSON(components[i], compIds);
		}

		compHeader.put("subcomps", descriptors);

		if (compIds != null && compIds.containsKey(lf))

		{
			json.put("compid", compIds.get(lf).toString());
		}

		try {
			ByteArrayOutputStream lcOut = new ByteArrayOutputStream();
			ObjectOutputStream lcObjOut = new ObjectOutputStream(lcOut);
			lcObjOut.writeObject(lf);
			lcOut.flush();
			lcOut.close();

			String lcString = Base64.getEncoder().encodeToString(lcOut.toByteArray());
			compHeader.put("luadescriptor", lcString);
		} catch (Exception e) {
			e.printStackTrace();
		}

		compHeader.put("luatype", lf.getClass().getName());

		json.put("header", compHeader);
		json.put("extended", extendedDataTable);
		return json;
	}

}
