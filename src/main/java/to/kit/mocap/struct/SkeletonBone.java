package to.kit.mocap.struct;

import java.awt.Color;
import java.awt.Graphics2D;

import org.apache.commons.math3.linear.RealMatrix;

/**
 * Bone.
 * @author Hidetaka Sasai
 */
public final class SkeletonBone extends SkeletonNode {
	private int id;
	private final double[] dir = new double[3];
	private double length;
	/** Degrees of Freedom. */
	private String[] dof;
	private Limit limitX = new Limit();
	private Limit limitY = new Limit();
	private Limit limitZ = new Limit();

	/**
	 * adjust.
	 */
	public void adjust() {
		double x = this.dir[0] * this.length;
		double y = this.dir[1] * this.length;
		double z = this.dir[2] * this.length;

		this.translate = new P3D(x, y, z).affine(getAxis().getRevMatrix());
		//
		RealMatrix pm = getParent().getAxis().getRevMatrix();
		RealMatrix am = getAxis().getMatrix();

		this.axisMatrix = pm.multiply(am);
	}

	/**
	 * @return the dir
	 */
	protected double[] getDir() {
		return this.dir;
	}
	/**
	 * @return the id
	 */
	protected int getId() {
		return this.id;
	}
	/**
	 * @param value the id to set
	 */
	protected void setId(int value) {
		this.id = value;
	}
	/**
	 * @return the dof
	 */
	protected String[] getDof() {
		return this.dof;
	}
	/**
	 * @param dof the dof to set
	 */
	protected void setDof(String[] dof) {
		this.dof = dof;
	}
	/**
	 * @return the limitX
	 */
	protected Limit getLimitX() {
		return this.limitX;
	}
	/**
	 * @param limitX the limitX to set
	 */
	protected void setLimitX(Limit limitX) {
		this.limitX = limitX;
	}
	/**
	 * @return the limitY
	 */
	protected Limit getLimitY() {
		return this.limitY;
	}
	/**
	 * @param limitY the limitY to set
	 */
	protected void setLimitY(Limit limitY) {
		this.limitY = limitY;
	}
	/**
	 * @return the limitZ
	 */
	protected Limit getLimitZ() {
		return this.limitZ;
	}
	/**
	 * @param limitZ the limitZ to set
	 */
	protected void setLimitZ(Limit limitZ) {
		this.limitZ = limitZ;
	}
	/**
	 * @return the length
	 */
	public double getLength() {
		return this.length;
	}
	/**
	 * @param length the length to set
	 */
	public void setLength(double length) {
		this.length = length;
	}

	@Override
	protected void calculate() {
		RealMatrix mat = getAccum();
		P3D pt = P3D.ORIGIN.affine(mat);

		setPoint(pt);
		for (SkeletonNode node : getJoint()) {
			node.calculate();
		}
	}

	@Override
	public void draw(Graphics2D g) {
		double scale = 10;
		SkeletonNode parent = getParent();
		P3D nextPt = getPoint().rotate(getSkeleton().rotateV, getSkeleton().rotateH, 0);
		int nextX = (int) (nextPt.x * scale);
		int nextY = (int) (-nextPt.y * scale);

		P3D prevPt = parent.getPoint().rotate(getSkeleton().rotateV, getSkeleton().rotateH, 0);
		int prevX = (int) (prevPt.x * scale);
		int prevY = (int) (-prevPt.y * scale);

//		String name = getName();
//		String dofs = StringUtils.join(this.dof, ",");
//		String info = dofs + String.format("[%3.2f,%3.2f,%3.2f]", this.thetaX.toDegree(), this.thetaY.toDegree(), this.thetaZ.toDegree());
//		g.setColor(Color.GRAY);
//		if (name.startsWith("l")) {
//			g.drawString(info + name, prevX + 10 * (depth - 1), prevY + 10);
//		}
		g.setColor(Color.LIGHT_GRAY);
		g.drawRoundRect(nextX, nextY, 3, 3, 3, 3);
		g.drawLine(prevX, prevY, nextX, nextY);
		for (SkeletonNode node : getJoint()) {
			node.draw(g);
		}
	}
}
