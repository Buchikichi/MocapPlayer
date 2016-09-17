package to.kit.mocap.struct;

import java.awt.Graphics2D;

import org.apache.commons.math3.linear.RealMatrix;

/**
 * SkeletonRoot.
 * @author Hidetaka Sasai
 */
public final class SkeletonRoot extends SkeletonNode {
	private String order;
	private String axis;
	private double[] position;
	private String orientation;

	@Override
	protected RealMatrix getAccum() {
		RealMatrix dx = getTranslateMatrix();
		RealMatrix mx = this.thetaX.rotateX();
		RealMatrix my = this.thetaY.rotateY();
		RealMatrix mz = this.thetaZ.rotateZ();
		RealMatrix tx = mz.multiply(my).multiply(mx);

		return dx.multiply(tx);
	}

	/**
	 * Create an instance.
	 * @param name the Name
	 */
	public SkeletonRoot(String name) {
		setName(name);
	}

	/**
	 * @return the order
	 */
	public String getOrder() {
		return this.order;
	}
	/**
	 * @param order the order to set
	 */
	public void setOrder(String order) {
		this.order = order;
	}
	/**
	 * @return the axis
	 */
	public String getAxis() {
		return this.axis;
	}
	/**
	 * @param axis the axis to set
	 */
	public void setAxis(String axis) {
		this.axis = axis;
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

	@Override
	public void draw(Graphics2D g) {
		P3D pt = P3D.ORIGIN.affine(getAccum());

		setPoint(pt);
		for (SkeletonNode node : getJoint()) {
			node.draw(g);
		}
	}
}
