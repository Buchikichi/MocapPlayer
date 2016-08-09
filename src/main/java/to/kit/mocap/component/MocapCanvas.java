package to.kit.mocap.component;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import to.kit.mocap.struct.Skeleton;

public class MocapCanvas extends Canvas {
	private List<Skeleton> skeletonList = new ArrayList<>();

	public MocapCanvas() {
		setBackground(Color.LIGHT_GRAY);
	}

	@Override
	public void paint(Graphics g) {
		Dimension size = getSize();
		int originX = size.width / 2;
		int originY = size.height / 2;
		Graphics2D g2 = (Graphics2D) g;
		super.paint(g);
		g2.scale(1, 1);
		g.translate(originX, originY);
//		g.setColor(Color.BLUE);
//		g.drawRect(0, 0, 10, 10);

		for (Skeleton skeleton : this.skeletonList) {
			skeleton.draw(g2);
		}
	}

	/**
	 * Add the Skeleton.
	 * @param skeleton the Skeleton
	 */
	public void add(Skeleton skeleton) {
		this.skeletonList.add(skeleton);
	}
}
