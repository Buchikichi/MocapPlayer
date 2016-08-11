package to.kit.mocap.struct;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

public abstract class SkeletonNode {
	private String name;
	private int depth;
	private SkeletonNode parent;
	private List<SkeletonNode> joint = new ArrayList<>();
	private double axisX;
	private double axisY;
	private double axisZ;
	private P3D point;
	private Skeleton skeleton;

	protected List<SkeletonNode> getJoint() {
		return this.joint;
	}

	public abstract void draw(Graphics2D g, SkeletonNode parent); 

	public void add(SkeletonNode node) {
		node.depth = this.depth + 1;
		node.parent = this;
		this.joint.add(node);
	}

	/**
	 * Get the name.
	 * @return the name
	 */
	public String getName() {
		return this.name;
	}
	/**
	 * Set the name.
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the depth
	 */
	public int getDepth() {
		return this.depth;
	}
	/**
	 * @return the parent
	 */
	public SkeletonNode getParent() {
		return this.parent;
	}
	/**
	 * @return the axisX
	 */
	public double getAxisX() {
		return this.axisX;
	}
	/**
	 * @param value the axisX to set
	 */
	public void setAxisX(double value) {
		this.axisX = value;
	}
	/**
	 * @return the axisY
	 */
	public double getAxisY() {
		return this.axisY;
	}

	/**
	 * @param value the axisY to set
	 */
	public void setAxisY(double value) {
		this.axisY = value;
	}

	/**
	 * @return the axisZ
	 */
	public double getAxisZ() {
		return this.axisZ;
	}

	/**
	 * @param value the axisZ to set
	 */
	public void setAxisZ(double value) {
		this.axisZ = value;
	}

	/**
	 * @return the point
	 */
	public P3D getPoint() {
		return this.point;
	}
	/**
	 * @param point the point to set
	 */
	public void setPoint(P3D point) {
		this.point = point;
	}
	/**
	 * @return the skeleton
	 */
	public Skeleton getSkeleton() {
		return this.skeleton;
	}
	/**
	 * @param skeleton the skeleton to set
	 */
	public void setSkeleton(Skeleton skeleton) {
		this.skeleton = skeleton;
	}
}
