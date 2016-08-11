package to.kit.mocap.struct;

import java.awt.Color;
import java.awt.Graphics2D;

public class SkeletonRoot extends SkeletonNode {
	private String order;
	private String axis;
	private double[] position;
	private String orientation;

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
		P3D p;

		if (this.position != null && 3 <= this.position.length) {
			p = new P3D(this.position[0], this.position[1], this.position[2]);
		} else {
			p = new P3D(0, 0, 0);
		}
		setPoint(p);
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
	public void draw(Graphics2D g, SkeletonNode parent) {
		for (SkeletonNode node : getJoint()) {
			node.draw(g, this);
		}
	}
}
