package to.kit.mocap.struct;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;

import net.arnx.jsonic.JSONHint;
import to.kit.mocap.struct.Skeleton.CalcOrder;

/**
 * Node.
 * @author Hidetaka Sasai
 */
public abstract class SkeletonNode {
	private String name;
	private int depth;
	private String order;
	private SkeletonNode parent;
	private final List<SkeletonNode> joint = new ArrayList<>();
	private final Rotation axis = new Rotation();
	protected RealMatrix axisMatrix = Radian.NO_EFFECT;
	protected RealMatrix thetaMatrix = Radian.NO_EFFECT;
	protected P3D translate = P3D.ORIGIN;
	private P3D point = P3D.ORIGIN;
	private Skeleton skeleton;

	protected abstract void calculate();
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

	protected RealMatrix getAccum() {
		RealMatrix tm = getThetaMatrix();
		RealMatrix tr = getTranslateMatrix();
		RealMatrix mat;

		if (this.skeleton.getCalcOrder() == CalcOrder.RotateTranslate) {
			mat = getAxisMatrix().multiply(tm).multiply(tr);
		} else {
			mat = getAxisMatrix().multiply(tr).multiply(tm);
		}
		return this.parent.getAccum().multiply(mat);
	}

	/**
	 * @return the depth
	 */
	protected int getDepth() {
		return this.depth;
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
	 * @return the thetaMatrix
	 */
	protected RealMatrix getThetaMatrix() {
		return this.thetaMatrix;
	}
	/**
	 * @param thetaMatrix the thetaMatrix to set
	 */
	protected void setThetaMatrix(RealMatrix thetaMatrix) {
		this.thetaMatrix = thetaMatrix;
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
	 * @return the parent
	 */
	@JSONHint(ignore = true)
	public SkeletonNode getParent() {
		return this.parent;
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
}
