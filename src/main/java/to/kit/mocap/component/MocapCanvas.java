package to.kit.mocap.component;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import to.kit.mocap.struct.Anima;

/**
 * Mocap canvas.
 * @author Hidetaka Sasai
 */
public final class MocapCanvas extends Canvas {
	private final List<Anima> animaList = new ArrayList<>();
	private double rotateH;
	private double rotateV;
	private int current;

	/**
	 * Add the Anima.
	 * @param anima the Anima
	 */
	public void add(Anima anima) {
		synchronized (this.animaList) {
			this.animaList.add(anima);
		}
	}
	/**
	 * Remove the front motion.
	 */
	public void removeTheFront() {
//		this.motionList.subList(0, this.current).clear();
	}
	/**
	 * Remove the rear motion.
	 */
	public void removeTheRear() {
//		this.motionList.subList(this.current, this.motionList.size()).clear();
	}
	/**
	 * @param value the rotateH to set
	 */
	public void setRotateH(double value) {
		for (Anima anima : this.animaList) {
			anima.setRotateH(value);
		}
		this.rotateH = value;
	}
	/**
	 * @param value the rotateV to set
	 */
	public void setRotateV(double value) {
		for (Anima anima : this.animaList) {
			anima.setRotateV(value);
		}
		this.rotateV = value;
	}
	/**
	 * @param value the current to set
	 */
	public void setCurrent(int value) {
		for (Anima anima : this.animaList) {
			anima.setCurrent(value);
		}
		this.current = value;
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

		g2d.setBackground(Color.DARK_GRAY);
		g2d.clearRect(0, 0, size.width, size.height);
		g2d.setColor(Color.WHITE);
		g2d.drawString(String.valueOf(this.current + 1), 0, 10);
		g2d.drawString("V:" + String.valueOf(Math.floor(this.rotateV * 180 / Math.PI)), 0, 20);
		g2d.drawString("H:" + String.valueOf(Math.floor(this.rotateH * 180 / Math.PI)), 0, 30);
		g2d.translate(originX, originY);
		synchronized (this.animaList) {
			for (Anima anima : this.animaList) {
				anima.draw(g2d);
			}
		}
		g.drawImage(img, 0, 0, null);
		g2d.dispose();
	}
}
