/**
 * This software is released as part of the Pumpernickel project.
 * 
 * All com.pump resources in the Pumpernickel project are distributed under the
 * MIT License:
 * https://raw.githubusercontent.com/mickleness/pumpernickel/master/License.txt
 * 
 * More information about the Pumpernickel project is available here:
 * https://mickleness.github.io/pumpernickel/
 */
package jshdesktop.com.pump.showcase.demo;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.LookAndFeel;
import javax.swing.SwingConstants;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.plaf.ButtonUI;

import jshdesktop.com.pump.icon.GlyphIcon;
import jshdesktop.com.pump.icon.RefreshIcon;
import jshdesktop.com.pump.image.ImageLoader;
import jshdesktop.com.pump.image.pixel.Scaling;
import jshdesktop.com.pump.inspector.AnimatingInspectorPanel;
import jshdesktop.com.pump.inspector.Inspector;
import jshdesktop.com.pump.inspector.InspectorRowPanel;
import jshdesktop.com.pump.plaf.button.BevelButtonUI;
import jshdesktop.com.pump.plaf.button.ButtonCluster;
import jshdesktop.com.pump.plaf.button.GradientButtonUI;
import jshdesktop.com.pump.plaf.button.QButtonUI;
import jshdesktop.com.pump.plaf.button.QButtonUI.PaintFocus;
import jshdesktop.com.pump.plaf.button.RecessedButtonUI;
import jshdesktop.com.pump.plaf.button.RoundRectButtonUI;
import jshdesktop.com.pump.plaf.button.SquareButtonUI;
import jshdesktop.com.pump.plaf.combobox.QComboBoxUI;
import jshdesktop.com.pump.reflect.Reflection;
import jshdesktop.com.pump.util.JVM;

public class JButtonDemo extends ShowcaseExampleDemo {
	private static final long serialVersionUID = 1L;

	private static final String NONE = "none";

	public enum Horizontal {
		Right(SwingConstants.RIGHT), Left(SwingConstants.LEFT), Center(
				SwingConstants.CENTER), Leading(
						SwingConstants.LEADING), Trailing(
								SwingConstants.TRAILING);

		public static Horizontal valueOf(int constant) {
			for (Horizontal h : Horizontal.values()) {
				if (h.constant == constant)
					return h;
			}
			return null;
		}

		int constant;

		Horizontal(int constant) {
			this.constant = constant;
		}
	}

	public enum Vertical {
		Top(SwingConstants.TOP), Center(SwingConstants.CENTER), Bottom(
				SwingConstants.BOTTOM);

		public static Vertical valueOf(int constant) {
			for (Vertical v : Vertical.values()) {
				if (v.constant == constant)
					return v;
			}
			return null;
		}

		int constant;

		Vertical(int constant) {
			this.constant = constant;
		}
	}

	Map<String, Class> buttonUITypeMap = new HashMap<>();
	JComboBox<String> buttonUIClassComboBox = new JComboBox<>();
	JComboBox<String> iconComboBox = new JComboBox<>();
	JTextField text = new JTextField("Name");
	JCheckBox paintBorderCheckbox = new JCheckBox("Border", true);
	JCheckBox paintStrokeCheckbox = new JCheckBox("Stroke", true);
	JCheckBox paintContentCheckbox = new JCheckBox("Content", true);
	JCheckBox paintFocusCheckbox = new JCheckBox("Focus", true);
	JComboBox<Horizontal> horizontalAlignmentComboBox = new JComboBox<>(
			Horizontal.values());
	JComboBox<Horizontal> horizontalTextPositionComboBox = new JComboBox<>(
			Horizontal.values());
	JComboBox<Vertical> verticalAlignmentComboBox = new JComboBox<>(
			Vertical.values());
	JComboBox<Vertical> verticalTextPositionComboBox = new JComboBox<>(
			Vertical.values());
	JComboBox<String> aquaTypeComboBox;
	JComboBox<String> sizeVariantComboBox = new JComboBox<String>(
			new String[] { "regular", "small", "mini" });
	JCheckBox comboBoxPopDownCheckBox = new JCheckBox("Pop Down", false);
	JRadioButton paintFocusInside = new JRadioButton("Inside", false);
	JRadioButton paintFocusOutside = new JRadioButton("Outside", true);
	Collection<InspectorRowPanel> aquaRows = new HashSet<>();
	Collection<InspectorRowPanel> qbuttonRows = new HashSet<>();

	Collection<String> aquaTypes = Arrays.asList(NONE, "bevel", "capsule",
			"combobox", "comboboxEndCap", "comboboxInternal", "disclosure",
			"gradient", "help", "icon", "recessed", "round", "roundRect",
			"scrollColumnSizer", "segmented", "segmentedCapsule",
			"segmentedGradient", "segmentedRoundRect", "segmentedTextured",
			"segmentedTexturedRounded", "square", "text", "textured",
			"texturedRound", "toggle", "toolbar", "well");

	public JButtonDemo() {
		JButton dummyButton = new JButton();

		Collection<Class> buttonUITypes = new LinkedHashSet<>();

		LookAndFeelInfo[] lafs = UIManager.getInstalledLookAndFeels();
		for (LookAndFeelInfo lafInfo : lafs) {
			try {
				LookAndFeel laf = (LookAndFeel) Class
						.forName(lafInfo.getClassName()).newInstance();
				laf.initialize();
				UIDefaults defaults = laf.getDefaults();
				JButton testButton = new JButton("test");
				ButtonUI ui = (ButtonUI) defaults.getUI(testButton);
				try {
					testButton.setUI(ui);
					// only keep the UI if the call to setUI didn't throw an
					// exception:
					buttonUITypes.add(ui.getClass());
				} catch (Exception e) {
					// Nimbus throws an exception resembling:

					// @formatter:off
					// java.lang.ClassCastException: com.apple.laf.AquaLookAndFeel cannot be cast to javax.swing.plaf.nimbus.NimbusLookAndFeel
					// at javax.swing.plaf.nimbus.NimbusStyle.validate(NimbusStyle.java:250)
					// at javax.swing.plaf.nimbus.NimbusStyle.getValues(NimbusStyle.java:806)
					// at javax.swing.plaf.nimbus.NimbusStyle.getInsets(NimbusStyle.java:485)
					// at javax.swing.plaf.synth.SynthStyle.installDefaults(SynthStyle.java:913)
					// at javax.swing.plaf.synth.SynthLookAndFeel.updateStyle(SynthLookAndFeel.java:265)
					// at javax.swing.plaf.synth.SynthButtonUI.updateStyle(SynthButtonUI.java:79)
					// at javax.swing.plaf.synth.SynthButtonUI.installDefaults(SynthButtonUI.java:62)
					// at javax.swing.plaf.basic.BasicButtonUI.installUI(BasicButtonUI.java:88)
					// at javax.swing.JComponent.setUI(JComponent.java:666)
					// @formatter:on
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		buttonUITypes.add(BevelButtonUI.class);
		buttonUITypes.add(GradientButtonUI.class);
		buttonUITypes.add(RecessedButtonUI.class);
		buttonUITypes.add(RoundRectButtonUI.class);
		buttonUITypes.add(SquareButtonUI.class);

		for (Class buttonUIType : buttonUITypes) {
			buttonUITypeMap.put(buttonUIType.getSimpleName(), buttonUIType);
			buttonUIClassComboBox.addItem(buttonUIType.getSimpleName());
		}
		buttonUIClassComboBox.setSelectedItem(
				dummyButton.getUI().getClass().getSimpleName());

		ActionListener actionRefreshListener = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				refreshButton();
			}

		};

		ButtonGroup g = new ButtonGroup();
		g.add(paintFocusInside);
		g.add(paintFocusOutside);

		JPanel animatingInspectorPanel = new AnimatingInspectorPanel();
		configurationPanel.add(animatingInspectorPanel);

		Inspector inspector = new Inspector(animatingInspectorPanel);
		inspector.addRow(new JLabel("ButtonUI:"), buttonUIClassComboBox);
		inspector.addRow(new JLabel("Icon:"), iconComboBox);
		inspector.addRow(new JLabel("Text:"), text, true);
		inspector.addRow(new JLabel("Horizontal Alignment:"),
				horizontalAlignmentComboBox);
		inspector.addRow(new JLabel("Horizontal Text Position:"),
				horizontalTextPositionComboBox);
		inspector.addRow(new JLabel("Vertical Alignment:"),
				verticalAlignmentComboBox);
		inspector.addRow(new JLabel("Vertical Text Position:"),
				verticalTextPositionComboBox);
		inspector.addRow(new JLabel("Paint:"), paintBorderCheckbox,
				paintContentCheckbox, paintFocusCheckbox, paintStrokeCheckbox);

		horizontalAlignmentComboBox.setSelectedItem(
				Horizontal.valueOf(dummyButton.getHorizontalAlignment()));
		horizontalTextPositionComboBox.setSelectedItem(
				Horizontal.valueOf(dummyButton.getHorizontalTextPosition()));
		verticalAlignmentComboBox.setSelectedItem(
				Vertical.valueOf(dummyButton.getVerticalAlignment()));
		verticalTextPositionComboBox.setSelectedItem(
				Vertical.valueOf(dummyButton.getVerticalTextPosition()));

		aquaTypeComboBox = new JComboBox<String>();
		for (String aquaType : aquaTypes) {
			if (!aquaType.startsWith("segmented"))
				aquaTypeComboBox.addItem(aquaType);
		}

		aquaRows.add(
				inspector.addRow(new JLabel("Button Type:"), aquaTypeComboBox));
		aquaRows.add(inspector.addRow(new JLabel("Size Variant:"),
				sizeVariantComboBox));
		qbuttonRows.add(inspector.addRow(new JLabel("Paint Focus:"),
				paintFocusInside, paintFocusOutside));
		qbuttonRows.add(inspector.addRow(new JLabel("JComboBox:"),
				comboBoxPopDownCheckBox));

		paintFocusInside.addActionListener(actionRefreshListener);
		paintFocusOutside.addActionListener(actionRefreshListener);
		comboBoxPopDownCheckBox.addActionListener(actionRefreshListener);
		aquaTypeComboBox.addActionListener(actionRefreshListener);
		sizeVariantComboBox.addActionListener(actionRefreshListener);

		iconComboBox.addItem("None");
		iconComboBox.addItem("Thumbnail");
		iconComboBox.addItem("Refresh");

		buttonUIClassComboBox.addActionListener(actionRefreshListener);
		iconComboBox.addActionListener(actionRefreshListener);
		paintBorderCheckbox.addActionListener(actionRefreshListener);
		paintStrokeCheckbox.addActionListener(actionRefreshListener);
		paintContentCheckbox.addActionListener(actionRefreshListener);
		paintFocusCheckbox.addActionListener(actionRefreshListener);
		horizontalAlignmentComboBox.addActionListener(actionRefreshListener);
		horizontalTextPositionComboBox.addActionListener(actionRefreshListener);
		verticalAlignmentComboBox.addActionListener(actionRefreshListener);
		verticalTextPositionComboBox.addActionListener(actionRefreshListener);

		text.getDocument().addDocumentListener(new DocumentListener() {

			@Override
			public void insertUpdate(DocumentEvent e) {
				refreshButton();
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				refreshButton();
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				refreshButton();
			}

		});

		paintBorderCheckbox.setToolTipText(
				"This controls AbstractButton#setBorderPainted(boolean)");
		paintContentCheckbox.setToolTipText(
				"This controls AbstractButton#setContentAreaFilled(boolean)");
		paintFocusCheckbox.setToolTipText(
				"This controls AbstractButton#setFocusPainted(boolean)");
		paintStrokeCheckbox.setToolTipText(
				"This QButtonUI option toggles the stroke painted in the border.");

		refreshButton();
	}

	/**
	 * Return the different types of Aqua buttons we can render.
	 * <p>
	 * This method looks up a static map in com.apple.laf classes to identify
	 * its list of values.
	 */
	private static String[] getAquaTypes() {
		if (!JVM.isMac)
			return new String[] {};
		try {
			Class c = Class.forName("com.apple.laf.AquaButtonExtendedTypes");
			Map map = (Map) Reflection.invokeMethod(c, null, "getAllTypes");
			SortedSet<String> names = new TreeSet<>();
			names.addAll(map.keySet());
			return names.toArray(new String[names.size()]);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new String[] {};
	}

	private AbstractButton lastButton;
	boolean isAqua, isQButton;
	String buttonUIClass;

	private void refreshButton() {
		try {
			JComboBox<String> comboBox = null;
			JButton circleButton = null;

			AbstractButton button = createButton(text.getText());
			if (button.getUI() instanceof QButtonUI) {
				comboBox = new JComboBox<String>();
				comboBox.addItem("Item 1");
				comboBox.addItem("Item 2");
				comboBox.addItem("Item 3");

				QButtonUI q = (QButtonUI) button.getUI();
				comboBox.setUI(q.createComboBoxUI());

				comboBox.putClientProperty(QComboBoxUI.PROPERTY_IS_POP_DOWN,
						comboBoxPopDownCheckBox.isSelected());
				comboBox.putClientProperty(QButtonUI.PROPERTY_STROKE_PAINTED,
						button.getClientProperty(
								QButtonUI.PROPERTY_STROKE_PAINTED));

				Icon icon = button.getIcon() == null
						? new GlyphIcon(button.getFont(), '?', 15, Color.black)
						: button.getIcon();
				circleButton = new JButton(icon);
				circleButton.setUI(button.getUI());
				circleButton.putClientProperty(QButtonUI.PROPERTY_IS_CIRCLE,
						Boolean.TRUE);
				circleButton.setContentAreaFilled(button.isContentAreaFilled());
				circleButton.setFocusPainted(button.isFocusPainted());
				circleButton.setBorderPainted(button.isBorderPainted());
				circleButton.putClientProperty(
						QButtonUI.PROPERTY_STROKE_PAINTED,
						button.getClientProperty(
								QButtonUI.PROPERTY_STROKE_PAINTED));
			}

			examplePanel.removeAll();
			examplePanel.setLayout(new GridBagLayout());
			GridBagConstraints c = new GridBagConstraints();
			c.gridx = 0;
			c.gridy = 0;
			c.anchor = GridBagConstraints.BASELINE;
			c.weightx = 1;
			c.weighty = 1;
			c.insets = new Insets(3, 3, 3, 3);
			examplePanel.add(button, c);

			String type = (String) button
					.getClientProperty("JButton.buttonType");
			String aquaSegmentedType;
			if (type == null) {
				aquaSegmentedType = "segmented";
			} else {
				aquaSegmentedType = "segmented"
						+ Character.toUpperCase(type.charAt(0))
						+ type.substring(1);
			}

			if (isQButton) {
				c.gridx++;
				JButton left = createButton("Left");
				JButton middle = createButton("Middle");
				JButton right = createButton("Right");
				ButtonCluster cluster = new ButtonCluster(left, middle, right);
				examplePanel.add(cluster.createContainer(), c);
			} else if (isAqua && aquaTypes.contains(aquaSegmentedType)) {
				c.gridx++;
				JButton left = createButton("Left");
				JButton middle = createButton("Middle");
				JButton right = createButton("Right");
				left.putClientProperty("JButton.segmentPosition", "first");
				middle.putClientProperty("JButton.segmentPosition", "middle");
				right.putClientProperty("JButton.segmentPosition", "last");
				left.putClientProperty("JButton.buttonType", aquaSegmentedType);
				middle.putClientProperty("JButton.buttonType",
						aquaSegmentedType);
				right.putClientProperty("JButton.buttonType",
						aquaSegmentedType);
				examplePanel.add(createPanel(left, middle, right), c);
			}

			c.gridx++;
			if (circleButton != null) {
				examplePanel.add(circleButton, c);
				c.gridx++;
			}
			if (comboBox != null) {
				examplePanel.add(comboBox, c);
				c.gridx++;
			}
		} catch (Exception e) {
			e.printStackTrace();
			examplePanel.removeAll();
			examplePanel.add(new JLabel("Error: see console"));
		} finally {
			examplePanel.revalidate();
			examplePanel.repaint();
		}
	}

	JPanel createPanel(JComponent... components) {
		JPanel p = new JPanel(new GridBagLayout());
		p.setOpaque(false);
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 1;
		c.weighty = 1;
		c.anchor = GridBagConstraints.BASELINE;

		for (JComponent component : components) {
			p.add(component, c);
			c.gridx++;
		}
		return p;
	}

	JButton createButton(String text)
			throws InstantiationException, IllegalAccessException {
		JButton button = new JButton();

		buttonUIClass = (String) buttonUIClassComboBox.getSelectedItem();
		ButtonUI buttonUI = (ButtonUI) buttonUITypeMap.get(buttonUIClass)
				.newInstance();
		button.setUI(buttonUI);

		if (lastButton != null)
			button.setSelected(lastButton.isSelected());
		button.setText(text);
		lastButton = button;

		if (iconComboBox.getSelectedIndex() == 1) {
			button.setIcon(new ImageIcon(getThumbnail()));
		} else if (iconComboBox.getSelectedIndex() == 2) {
			button.setIcon(new RefreshIcon(30));
		}

		Horizontal hAlign = (Horizontal) horizontalAlignmentComboBox
				.getSelectedItem();
		button.setHorizontalAlignment(hAlign.constant);

		Horizontal hTextPos = (Horizontal) horizontalTextPositionComboBox
				.getSelectedItem();
		button.setHorizontalTextPosition(hTextPos.constant);

		Vertical vAlign = (Vertical) verticalAlignmentComboBox
				.getSelectedItem();
		button.setVerticalAlignment(vAlign.constant);

		Vertical vTextPos = (Vertical) verticalTextPositionComboBox
				.getSelectedItem();
		button.setVerticalTextPosition(vTextPos.constant);

		button.setBorderPainted(paintBorderCheckbox.isSelected());
		button.setContentAreaFilled(paintContentCheckbox.isSelected());
		button.setFocusPainted(paintFocusCheckbox.isSelected());
		button.putClientProperty(QButtonUI.PROPERTY_STROKE_PAINTED,
				paintStrokeCheckbox.isSelected());

		isAqua = buttonUIClass.toLowerCase().contains("aqua");
		isQButton = QButtonUI.class.isInstance(button.getUI());
		for (InspectorRowPanel p : aquaRows) {
			p.setVisible(isAqua);
		}
		for (InspectorRowPanel p : qbuttonRows) {
			p.setVisible(isQButton);
		}
		paintStrokeCheckbox.setVisible(isQButton);
		if (isAqua) {
			String buttonType = (String) aquaTypeComboBox.getSelectedItem();
			if (!NONE.equalsIgnoreCase(buttonType)) {
				button.putClientProperty("JButton.buttonType", buttonType);
			}

			button.putClientProperty("JComponent.sizeVariant",
					(String) sizeVariantComboBox.getSelectedItem());
		} else if (isQButton) {
			if (paintFocusInside.isSelected()) {
				QButtonUI ui = (QButtonUI) button.getUI();
				ui.setPaintFocus(PaintFocus.INSIDE);
			}
		}

		return button;
	}

	static BufferedImage thumbnail = null;

	private static BufferedImage getThumbnail() {
		if (thumbnail == null) {
			BufferedImage bi = ImageLoader.createImage(
					AlphaCompositeDemo.class.getResource("balloon.png"));
			thumbnail = Scaling.scaleProportionally(bi, new Dimension(30, 30));
		}
		return thumbnail;
	}

	@Override
	public String getTitle() {
		return "JButton, QButtonUI Demo";
	}

	@Override
	public String getSummary() {
		return "This demonstrates button configurations and new QButtonUI implementations.";
	}

	@Override
	public URL getHelpURL() {
		return getClass().getResource("jbuttonDemo.html");
	}

	@Override
	public String[] getKeywords() {
		return new String[] { "button", "ux", "ui", "Swing" };
	}

	@Override
	public Class<?>[] getClasses() {
		return new Class[] { JButton.class, QButtonUI.class };
	}
}