package to.kit.mocap.struct;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;

public abstract class SkeletonNode {
	private String name;
	private int depth;
	private SkeletonNode parent;
	private List<SkeletonNode> joint = new ArrayList<>();
	private Radian axisX = new Radian(null);
	private Radian axisY = new Radian(null);
	private Radian axisZ = new Radian(null);
	protected Radian thetaX = new Radian(null);
	protected Radian thetaY = new Radian(null);
	protected Radian thetaZ = new Radian(null);
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

	public SkeletonRoot getRoot() {
		return this.skeleton.getRoot();
	}
	public Double getInhelitThetaX() {
		Double result = null;

		if (this.thetaX != null) {
			result = this.thetaX.getRadian();
		} else if (this.parent != null) {
			result = this.parent.getInhelitThetaX();
		}
		return result;
	}
	public Double getInhelitThetaY() {
		Double result = null;

		if (this.thetaY != null) {
			result = this.thetaY.getRadian();
		} else if (this.parent != null) {
			result = this.parent.getInhelitThetaY();
		}
		return result;
	}
	public Double getInhelitThetaZ() {
		Double result = null;

		if (this.thetaZ != null) {
			result = this.thetaZ.getRadian();
		} else if (this.parent != null) {
			result = this.parent.getInhelitThetaZ();
		}
		return result;
	}
	public RealMatrix getAccumAxisX() {
		RealMatrix ma = MatrixUtils.createRealMatrix(this.axisX.rotateX());

		if (this.parent != null) {
			return this.parent.getAccumAxisX().multiply(ma);
		}
		return ma;
	}
	public RealMatrix getAccumAxisY() {
		RealMatrix ma = MatrixUtils.createRealMatrix(this.axisY.rotateY());

		if (this.parent != null) {
			return this.parent.getAccumAxisY().multiply(ma);
		}
		return ma;
	}
	public RealMatrix getAccumAxisZ() {
		RealMatrix ma = MatrixUtils.createRealMatrix(this.axisZ.rotateZ());

		if (this.parent != null) {
			return this.parent.getAccumAxisZ().multiply(ma);
		}
		return ma;
	}

	public RealMatrix getAccumX() {
		double[][] result = this.thetaX.rotateX();
		RealMatrix ma = MatrixUtils.createRealMatrix(result);

		if (this.parent != null) {
			ma = this.parent.getAccumX().multiply(ma);
		}
//		ma = ma.multiply(this.getAccumAxisX());
		return ma;
	}
	public RealMatrix getAccumY() {
		double[][] result = this.thetaY.rotateY();
		RealMatrix ma = MatrixUtils.createRealMatrix(result);

		if (this.parent != null) {
			ma = this.parent.getAccumY().multiply(ma);
		}
//		ma = ma.multiply(this.getAccumAxisY());
		return ma;
	}
	public RealMatrix getAccumZ() {
		double[][] result = this.thetaZ.rotateZ();
		RealMatrix ma = MatrixUtils.createRealMatrix(result);

		if (this.parent != null) {
			ma = this.parent.getAccumZ().multiply(ma);
		}
//		ma = ma.multiply(this.getAccumAxisZ());
		return ma;
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
	public Radian getAxisX() {
		return this.axisX;
	}
	/**
	 * @param value the axisX to set
	 */
	public void setAxisX(Radian value) {
		this.axisX = value;
	}
	/**
	 * @return the axisY
	 */
	public Radian getAxisY() {
		return this.axisY;
	}
	/**
	 * @param value the axisY to set
	 */
	public void setAxisY(Radian value) {
		this.axisY = value;
	}
	/**
	 * @return the axisZ
	 */
	public Radian getAxisZ() {
		return this.axisZ;
	}
	/**
	 * @param value the axisZ to set
	 */
	public void setAxisZ(Radian value) {
		this.axisZ = value;
	}
	/**
	 * @return the tX
	 */
	public Radian getThetaX() {
		return this.thetaX;
	}
	/**
	 * @param tX the tX to set
	 */
	public void setThetaX(Radian tX) {
		this.thetaX = tX;
	}
	/**
	 * @return the tY
	 */
	public Radian getThetaY() {
		return this.thetaY;
	}
	/**
	 * @param tY the tY to set
	 */
	public void setThetaY(Radian tY) {
		this.thetaY = tY;
	}
	/**
	 * @return the tZ
	 */
	public Radian getThetaZ() {
		return this.thetaZ;
	}
	/**
	 * @param tZ the tZ to set
	 */
	public void setThetaZ(Radian tZ) {
		this.thetaZ = tZ;
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
