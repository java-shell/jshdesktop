package jshdesktop.desktop.frame;

import javax.swing.JInternalFrame;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;

import jshdesktop.JDesktopFrame;
import jshdesktop.module;
import terra.shell.utils.system.JSHProcesses;

public abstract class BasicFrame extends JInternalFrame implements JProcessComponent {
	private JGuiProcess local;
	private JDesktopFrame mainFrame = null;

	public BasicFrame() {
		setName("BasicFrame");
		this.local = new JGuiProcess(this);
		local.run();
		JSHProcesses.addProcess(local);
	}

	public BasicFrame(JDesktopFrame mainFrame) {
		setName("BasicFrame");
		this.local = new JGuiProcess(this);
		local.run();
		JSHProcesses.addProcess(local);
		this.mainFrame = mainFrame;
	}

	public String getName() {
		return "BasicFrame";
	}

	public void finish() {
		setVisible(true);
		setResizable(true);
		setClosable(true);
		setIconifiable(true);
		if (mainFrame != null) {
			mainFrame.add(this);
		} else
			module.addToDesktopFrame(this);
		// module.getDesktopFrame().add(this);
		toFront();
	}

	@Override
	public void setIcon(boolean icon) {
		if (icon) {
			for (InternalFrameListener l : this.getInternalFrameListeners()) {
				l.internalFrameIconified(new InternalFrameEvent(this, InternalFrameEvent.INTERNAL_FRAME_ICONIFIED));
			}
			this.isIcon = true;
			this.setVisible(false);
			return;
		}
		for (InternalFrameListener l : this.getInternalFrameListeners()) {
			l.internalFrameDeiconified(new InternalFrameEvent(this, InternalFrameEvent.INTERNAL_FRAME_DEICONIFIED));
		}
		this.isIcon = false;
		this.setVisible(true);
	}

	public abstract void create();

}
