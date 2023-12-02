package jshdesktop.desktop.frame.utilities;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeListener;

import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

import jshdesktop.desktop.frame.BasicFrame;
import terra.shell.utils.system.user.InvalidUserException;
import terra.shell.utils.system.user.User;
import terra.shell.utils.system.user.UserManagement;

public class AuthenticatorPopup extends BasicFrame {
	private final Action authEvent;
	private final AuthenticatorPopup thisPopup;

	public AuthenticatorPopup(Action authEvent) {
		this.authEvent = authEvent;
		this.thisPopup = this;
	}

	@Override
	public void create() {
		JPanel mainPanel = new JPanel();
		JPanel authPanel = new JPanel();
		JPasswordField passField = new JPasswordField();
		JTextField userField = new JTextField();
		JButton enterButton = new JButton("Authenticate");

		passField.setColumns(10);
		userField.setColumns(10);

		ActionListener authActionListener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					User user = UserManagement.logIn(userField.getText(), new String(passField.getPassword()));
					userField.setText("");
					passField.setText("");
					thisPopup.setVisible(false);
					thisPopup.dispose();
					authEvent.actionPerformed(new ActionEvent(user, 1, "auth"));
				} catch (InvalidUserException e1) {
					userField.setText("");
					passField.setText("");
				}
			}
		};

		registerKeyboardAction(authActionListener, KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0),
				WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

		enterButton.addActionListener(authActionListener);

		authPanel.setLayout(new BorderLayout());
		authPanel.add(userField, BorderLayout.WEST);
		authPanel.add(passField, BorderLayout.EAST);

		mainPanel.setLayout(new BorderLayout());
		mainPanel.add(authPanel, BorderLayout.NORTH);
		mainPanel.add(enterButton, BorderLayout.CENTER);

		add(mainPanel);
		setSize(300, 150);
		finish();
	}

}
