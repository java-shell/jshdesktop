package jshdesktop.iconography;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class LoadableIcon extends ImageIcon {

	private static final long serialVersionUID = 1L;
	private File iconFile;
	private long lastPainted, deletionInterval = 20000;
	private Image image = null;
	private final Image clearImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
	private Timer checkTimer;
	private int width, height;
	private TimerTask checkPaintedTask = new TimerTask() {

		@Override
		public void run() {
			final long paintInterval = System.currentTimeMillis() - lastPainted;
			if (paintInterval >= deletionInterval) {
				image = clearImage;
				return;
			}
			checkTimer.schedule(checkPaintedTask, deletionInterval);
		}

	};

	public LoadableIcon(File iconFile) {
		this.iconFile = iconFile;
		checkTimer = new Timer();
		final Image tmp = Toolkit.getDefaultToolkit().createImage(iconFile.getAbsolutePath());
		width = tmp.getWidth(null);
		height = tmp.getHeight(null);

	}

	@Override
	public int getIconWidth() {
		return width;
	}

	@Override
	public int getIconHeight() {
		return height;
	}

	@Override
	protected void loadImage(Image image) {
	}

	protected void loadImage() {
		Thread t = new Thread(() -> {
			final Image img = Toolkit.getDefaultToolkit().createImage(iconFile.getAbsolutePath());
			synchronized (image) {
				image = img;
			}
			synchronized (checkTimer) {
				checkTimer.schedule(checkPaintedTask, deletionInterval);
			}
		});
		t.setName("IconLoader");
		t.start();
	}

	public int getImageLoadStatus() {
		return MediaTracker.COMPLETE;
	}

	public Image getImage() {
		BufferedImage tmp;
		try {
			tmp = ImageIO.read(iconFile);
			return tmp;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void setImage(Image i) {
		try {
			File tmpImage = File.createTempFile(iconFile.getName(), "");
			BufferedImage outImg = new BufferedImage(i.getWidth(null), i.getHeight(null), BufferedImage.TYPE_INT_ARGB);
			outImg.getGraphics().drawImage(i, 0, 0, null);
			ImageIO.write(outImg, "png", tmpImage);
			outImg = null;
			iconFile = tmpImage;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void paintIcon(Component c, Graphics g, int x, int y) {
		if (image == clearImage) {
			loadImage();
		}
		synchronized (image) {
			if (getImageObserver() == null) {
				g.drawImage(image, x, y, c);
			} else {
				g.drawImage(image, x, y, getImageObserver());
			}
		}
		lastPainted = System.currentTimeMillis();
	}

}
