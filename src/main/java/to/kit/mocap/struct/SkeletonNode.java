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
	private final Rotation axis = new Rotation();
	protected RealMatrix axisMatrix;
	protected Radian thetaX = new Radian(null);
	protected Radian thetaY = new Radian(null);
	protected Radian thetaZ = new Radian(null);
	protected P3D translate = P3D.ORIGIN;
	private P3D point = P3D.ORIGIN;
	private Skeleton skeleton;

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

	protected RealMatrix getThetaMatrix() {
		RealMatrix mx = this.thetaX.rotateX();
		RealMatrix my = this.thetaY.rotateY();
		RealMatrix mz = this.thetaZ.rotateZ();

		return mz.multiply(my).multiply(mx);
	}

	protected RealMatrix getAccum() {
		RealMatrix tm = getThetaMatrix();
		RealMatrix tr = getTranslateMatrix();
		RealMatrix mat = getAxisMatrix().multiply(tm).multiply(tr);

		return this.parent.getAccum().multiply(mat);
	}

	/**
	 * @return the depth
	 */
	protected int getDepth() {
		return this.depth;
	}
	/**
	 * @return the parent
	 */
	protected SkeletonNode getParent() {
		return this.parent;
	}
	/**
	 * @return the axisMatrix
	 */
	protected RealMatrix getAxisMatrix() {
		return this.axisMatrix;
	}
	/**
	 * @param axisMatrix the axisMatrix to set
	 */
	protected void setAxisMatrix(RealMatrix axisMatrix) {
		this.axisMatrix = axisMatrix;
	}
	/**
	 * @return the tX
	 */
	protected Radian getThetaX() {
		return this.thetaX;
	}
	/**
	 * @param tX the tX to set
	 */
	protected void setThetaX(Radian tX) {
		this.thetaX = tX;
	}
	/**
	 * @return the tY
	 */
	protected Radian getThetaY() {
		return this.thetaY;
	}
	/**
	 * @param tY the tY to set
	 */
	protected void setThetaY(Radian tY) {
		this.thetaY = tY;
	}
	/**
	 * @return the tZ
	 */
	protected Radian getThetaZ() {
		return this.thetaZ;
	}
	/**
	 * @param tZ the tZ to set
	 */
	protected void setThetaZ(Radian tZ) {
		this.thetaZ = tZ;
	}
	/**
	 * @return the point
	 */
	protected P3D getPoint() {
		return this.point;
	}
	/**
	 * @param point the point to set
	 */
	protected void setPoint(P3D point) {
		this.point = point;
	}
	/**
	 * @return the skeleton
	 */
	protected Skeleton getSkeleton() {
		return this.skeleton;
	}
	/**
	 * @param skeleton the skeleton to set
	 */
	protected void setSkeleton(Skeleton skeleton) {
		this.skeleton = skeleton;
	}

	/**
	 * @return the axis
	 */
	public Rotation getAxis() {
		return this.axis;
	}
	/**
	 * Get the joint list.
	 * @return joint list
	 */
	public List<SkeletonNode> getJoint() {
		return this.joint;
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
}
