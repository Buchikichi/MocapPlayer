package to.kit.mocap.struct;

import java.awt.Graphics2D;

import org.apache.commons.math3.linear.RealMatrix;

/**
 * SkeletonRoot.
 * @author Hidetaka Sasai
 */
public final class SkeletonRoot extends SkeletonNode {
	private String axisOrder;
	private double[] position;
	private String orientation;

	@Override
	protected RealMatrix getAccum() {
		RealMatrix dx = getTranslateMatrix();
		RealMatrix tm = getThetaMatrix();

		return dx.multiply(tm);
	}

	/**
	 * Create an instance.
	 * @param name the Name
	 */
	public SkeletonRoot(String name) {
		setName(name);
	}

	/**
	 * @return the axis
	 */
	public String getAxisOrder() {
		return this.axisOrder;
	}
	/**
	 * @param axis the axis to set
	 */
	public void setAxisOrder(String axis) {
		this.axisOrder = axis;
	}
	/**
	 * @return the position
	 */
	public double[] getPosition() {
		return this.position;
	}
	/**
	 * @param values the position to set
	 */
	public void setPosition(double[] values) {
		if (this.position != null && 3 <= this.position.length) {
			this.setTranslate(new P3D(this.position[0], this.position[1], this.position[2]));
		}
		this.position = values;
	}
	/**
	 * @return the orientation
	 */
	public String getOrientation() {
		return this.orientation;
	}
	/**
	 * @param orientation the orientation to set
	 */
	public void setOrientation(String orientation) {
		this.orientation = orientation;
	}

	protected void calculateSimple() {
		setPoint(P3D.ORIGIN.affine(getAccum()));
	}

	@Override
	protected void calculate() {
		calculateSimple();
		for (SkeletonNode node : getJoint()) {
			node.calculate();
		}
	}

	@Override
	public void draw(Graphics2D g) {
		for (SkeletonNode node : getJoint()) {
			node.draw(g);
		}
	}
}
