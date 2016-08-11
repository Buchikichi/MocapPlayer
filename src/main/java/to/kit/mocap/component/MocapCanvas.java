package to.kit.mocap.component;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import to.kit.mocap.struct.Motion;
import to.kit.mocap.struct.Skeleton;

/**
 * Mocap canvas.
 * @author Hidetaka Sasai
 */
public final class MocapCanvas extends Canvas {
	private final List<Skeleton> skeletonList = new ArrayList<>();
	private final List<Motion> motionList = new ArrayList<>();
	private double rotateY;
	private int current;

	/**
	 * Add the Skeleton.
	 * @param skeleton the Skeleton
	 */
	public void add(Skeleton skeleton) {
		this.skeletonList.add(skeleton);
	}
	/**
	 * @param list
	 */
	public void add(List<Motion> list) {
		this.motionList.addAll(list);
	}
	/**
	 * @param rotateY the rotateY to set
	 */
	public void setRotateY(double rotateY) {
		this.rotateY = rotateY;
	}
	/**
	 * @param current the current to set
	 */
	public void setCurrent(int current) {
		this.current = current;
	}

	@Override
	public void update(Graphics g) {
		paint(g);
	}

	@Override
	public void paint(Graphics g) {
		Dimension size = getSize();
		BufferedImage img = new BufferedImage(size.width, size.height, BufferedImage.TYPE_INT_BGR);
		Graphics2D g2d = (Graphics2D) img.getGraphics();
		int originX = size.width / 2;
		int originY = size.height / 2;
		Motion motion = null;

		g2d.setBackground(Color.DARK_GRAY);
		g2d.clearRect(0, 0, size.width, size.height);
		g2d.translate(originX, originY);
		if (!this.motionList.isEmpty()) {
			motion = this.motionList.get(this.current);
		}
		for (Skeleton skeleton : this.skeletonList) {
			skeleton.setRotateY(this.rotateY);
			skeleton.draw(g2d);
			if (motion != null) {
				skeleton.shift(motion);
			}
		}
		g.drawImage(img, 0, 0, null);
		g2d.dispose();
	}
}
