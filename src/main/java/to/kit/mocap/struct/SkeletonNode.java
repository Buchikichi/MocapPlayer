package to.kit.mocap.struct;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;

/**
 * Node.
 * @author Hidetaka Sasai
 */
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
	protected P3D translate = P3D.ORIGIN;
	private P3D point = P3D.ORIGIN;
	private Skeleton skeleton;

	protected List<SkeletonNode> getJoint() {
		return this.joint;
	}

	protected abstract void draw(Graphics2D g); 

	protected void add(SkeletonNode node) {
		node.depth = this.depth + 1;
		node.parent = this;
		this.joint.add(node);
	}

	protected SkeletonRoot getRoot() {
		return this.skeleton.getRoot();
	}

	protected RealMatrix getTranslateMatrix() {
		return MatrixUtils.createRealMatrix(new double[][] {
			{ 1, 0, 0, this.translate.x },
			{ 0, 1, 0, this.translate.y },
			{ 0, 0, 1, this.translate.z },
			{ 0, 0, 0, 1 },
		});
	}

	protected RealMatrix getAxisMatrix() {
		RealMatrix mx = this.axisX.rotateX();
		RealMatrix my = this.axisY.rotateY();
		RealMatrix mz = this.axisZ.rotateZ();

		return mz.multiply(my).multiply(mx);
	}

	protected RealMatrix getAxisRevMatrix() {
		RealMatrix mx = this.axisX.rev().rotateX();
		RealMatrix my = this.axisY.rev().rotateY();
		RealMatrix mz = this.axisZ.rev().rotateZ();

		return mx.multiply(my).multiply(mz);
	}

	protected RealMatrix getThetaMatrix() {
		RealMatrix mx = this.thetaX.rotateX();
		RealMatrix my = this.thetaY.rotateY();
		RealMatrix mz = this.thetaZ.rotateZ();

		return mz.multiply(my).multiply(mx);
	}

	protected RealMatrix getAccum() {
		RealMatrix pm = this.parent.getAxisRevMatrix();
		RealMatrix am = getAxisMatrix();
		RealMatrix tm = getThetaMatrix();
		RealMatrix tr = getTranslateMatrix();

		RealMatrix mat = pm.multiply(am).multiply(tm).multiply(tr);
		return this.parent.getAccum().multiply(mat);
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
	 * @return the transform
	 */
	public P3D getTranslate() {
		return this.translate;
	}
	/**
	 * @param value the transform to set
	 */
	public void setTranslate(P3D value) {
		this.translate = value;
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
