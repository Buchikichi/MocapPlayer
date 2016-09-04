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
	protected Radian axisX = new Radian(null);
	protected Radian axisY = new Radian(null);
	protected Radian axisZ = new Radian(null);
	protected Radian thetaX = new Radian(null);
	protected Radian thetaY = new Radian(null);
	protected Radian thetaZ = new Radian(null);
	private P3D point = P3D.ORIGIN;
	private Skeleton skeleton;

	protected List<SkeletonNode> getJoint() {
		return this.joint;
	}

	public abstract void draw(Graphics2D g); 

	public void add(SkeletonNode node) {
		node.depth = this.depth + 1;
		node.parent = this;
		this.joint.add(node);
	}

	public SkeletonRoot getRoot() {
		return this.skeleton.getRoot();
	}

	protected RealMatrix getPositionMatrix() {
		return MatrixUtils.createRealMatrix(new double[][] {
			{ 1, 0, 0, -this.point.x },
			{ 0, 1, 0, -this.point.x },
			{ 0, 0, 1, -this.point.z },
			{ 0, 0, 0, 1 },
		});
	}

	public RealMatrix getAxisMatrix() {
		RealMatrix mx = this.axisX.rotateX();
		RealMatrix my = this.axisY.rotateY();
		RealMatrix mz = this.axisZ.rotateZ();

		return mx.multiply(my).multiply(mz);
	}

	public RealMatrix getAxisRevMatrix() {
		RealMatrix mx = this.axisX.rotateX();
		RealMatrix my = this.axisY.rotateY();
		RealMatrix mz = this.axisZ.rotateZ();

		return mz.multiply(my).multiply(mx);
	}

	public RealMatrix getAccumAxis() {
		RealMatrix ma = getAxisMatrix();

		if (this.parent != null) {
			return this.parent.getAccumAxis().multiply(ma);
		}
		return ma;
	}

	public RealMatrix getAccumAxisRev() {
		RealMatrix ax = this.axisX.rev().rotateX();
		RealMatrix ay = this.axisY.rev().rotateY();
		RealMatrix az = this.axisZ.rev().rotateZ();
		RealMatrix ma = az.multiply(ay).multiply(ax);

		if (this.parent != null) {
//			return this.parent.getAccumAxisRev().multiply(ma);
			return ma.multiply(this.parent.getAccumAxisRev());
		}
		return ma;
	}

	public RealMatrix getThetaMatrix() {
		RealMatrix mx = this.thetaX.rotateX();
		RealMatrix my = this.thetaY.rotateY();
		RealMatrix mz = this.thetaZ.rotateZ();

		return mx.multiply(my).multiply(mz);
	}

	protected RealMatrix getAccum() {
		RealMatrix dx = getPositionMatrix();
		RealMatrix ax = getAxisMatrix();
		RealMatrix tx = getThetaMatrix();

		if (this.parent != null) {
			return this.parent.getAccum().multiply(ax).multiply(tx).multiply(dx);
		}
		return ax.multiply(tx).multiply(dx);
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
