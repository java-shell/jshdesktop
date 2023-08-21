package jshdesktop.json;

import java.awt.Component;
import java.util.Hashtable;
import java.util.UUID;

import javax.swing.AbstractButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.text.JTextComponent;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class SwingJSONParser {

	private static final JSONParser parser = new JSONParser();

	public static JComponent parseComponent(String jsonDescriptor) throws ParseException {
		JSONObject json = (JSONObject) parser.parse(jsonDescriptor);
		return parseComponent(json);
	}

	public static JComponent parseComponent(JSONObject json) {
		return null;
	}

	@SuppressWarnings("unchecked")
	public static JSONObject convertToJSON(Component comp, Hashtable<Component, UUID> compIds) {
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

			compHeader.put("subcomps", descriptors);
		}

		if (compIds != null && compIds.containsKey(comp)) {
			json.put("compid", compIds.get(comp).toString());
		}

		json.put("header", compHeader);
		json.put("extended", extendedDataTable);

		return json;
	}

}
