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
	private int previous;
	private int current;

	private List<Motion> getCumulateList() {
		List<Motion> list = new ArrayList<>();

		if (this.motionList.isEmpty()) {
			return list;
		}
		if (this.current == this.previous) {
			return list;
		}
		int sign = this.current - this.previous < 0 ? -1 : 1;
		int cur = this.previous;

		for (;;) {
			cur += sign;
			boolean isCurrent = cur == this.current;
			Motion motion = this.motionList.get(cur);

			motion.setReduction(!isCurrent);
			list.add(motion);
			if (isCurrent) {
				break;
			}
		}
		this.previous = this.current;
		return list;
	}

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
	 * Remove the front motion.
	 */
	public void removeTheFront() {
		this.motionList.subList(0, this.current).clear();
	}
	/**
	 * Remove the rear motion.
	 */
	public void removeTheRear() {
		this.motionList.subList(this.current, this.motionList.size()).clear();
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
		int sign = this.current - this.previous < 0 ? -1 : 1;
		List<Motion> cumulateList = getCumulateList();

		g2d.setBackground(Color.DARK_GRAY);
		g2d.clearRect(0, 0, size.width, size.height);
		g2d.setColor(Color.WHITE);
		g2d.drawString(String.valueOf(this.current + 1), 0, 10);
		g2d.drawString("V:" + String.valueOf(Math.floor(this.rotateV * 180 / Math.PI)), 0, 20);
		g2d.drawString("H:" + String.valueOf(Math.floor(this.rotateH * 180 / Math.PI)), 0, 30);
		g2d.translate(originX, originY);
		for (Skeleton skeleton : this.skeletonList) {
			skeleton.setRotateH(this.rotateH);
			skeleton.setRotateV(this.rotateV);
			for (Motion motion : cumulateList) {
				skeleton.shift(motion, sign);
			}
			skeleton.draw(g2d);
		}
		g.drawImage(img, 0, 0, null);
		g2d.dispose();
	}
}
