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
	private double rotateH;
	private double rotateV;
	private int current;

	/**
	 * Get the Skeleton.
	 * @return skeleton
	 */
	public Skeleton getSkeleton() {
		if (this.skeletonList.isEmpty()) {
			return null;
		}
		return this.skeletonList.get(0);
	}
	/**
	 * Add the Skeleton.
	 * @param skeleton the Skeleton
	 */
	public void add(Skeleton skeleton) {
		this.skeletonList.add(skeleton);
	}
	/**
	 * @return the motionList
	 */
	public List<Motion> getMotionList() {
		return this.motionList;
	}
	/**
	 * @param list
	 */
	public void set(List<Motion> list) {
		this.motionList.clear();
		this.motionList.addAll(list);
	}
	/**
	 * @param value the rotateH to set
	 */
	public void setRotateH(double value) {
		this.rotateH = value;
	}
	/**
	 * @param value the rotateV to set
	 */
	public void setRotateV(double value) {
		this.rotateV = value;
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
		int originY = size.height / 2 - size.height / 8;
		Motion motion = null;

		g2d.setBackground(Color.DARK_GRAY);
		g2d.clearRect(0, 0, size.width, size.height);
		g2d.setColor(Color.WHITE);
		g2d.drawString(String.valueOf(this.current + 1), 0, 10);
		g2d.drawString("V:" + String.valueOf(Math.floor(this.rotateV * 180 / Math.PI)), 0, 20);
		g2d.drawString("H:" + String.valueOf(Math.floor(this.rotateH * 180 / Math.PI)), 0, 30);
		g2d.translate(originX, originY);
		if (!this.motionList.isEmpty()) {
			motion = this.motionList.get(this.current);
		}
		for (Skeleton skeleton : this.skeletonList) {
			skeleton.setRotateH(this.rotateH);
			skeleton.setRotateV(this.rotateV);
			skeleton.draw(g2d);
			if (motion != null) {
				skeleton.shift(motion);
			}
		}
		g.drawImage(img, 0, 0, null);
		g2d.dispose();
	}
}
