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
	 * @return the id
	 */
	public int getId() {
		return this.id;
	}
	/**
	 * @param value the id to set
	 */
	public void setId(int value) {
		this.id = value;
	}
	/**
	 * @return the dir
	 */
	public double[] getDir() {
		return this.dir;
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
	/**
	 * @return the dof
	 */
	public String[] getDof() {
		return this.dof;
	}
	/**
	 * @param dof the dof to set
	 */
	public void setDof(String[] dof) {
		this.dof = dof;
	}
	/**
	 * @return the limitX
	 */
	public Limit getLimitX() {
		return this.limitX;
	}
	/**
	 * @param limitX the limitX to set
	 */
	public void setLimitX(Limit limitX) {
		this.limitX = limitX;
	}
	/**
	 * @return the limitY
	 */
	public Limit getLimitY() {
		return this.limitY;
	}
	/**
	 * @param limitY the limitY to set
	 */
	public void setLimitY(Limit limitY) {
		this.limitY = limitY;
	}
	/**
	 * @return the limitZ
	 */
	public Limit getLimitZ() {
		return this.limitZ;
	}
	/**
	 * @param limitZ the limitZ to set
	 */
	public void setLimitZ(Limit limitZ) {
		this.limitZ = limitZ;
	}

	@Override
	public void draw(Graphics2D g) {
		double s1 = 10;
		double s2 = 10;
		SkeletonNode parent = getParent();
		RealMatrix mat = getAccum();
		P3D pt = P3D.ORIGIN.affine(mat);

		setPoint(pt);
		P3D nextPt = pt.rotate(getSkeleton().rotateV, getSkeleton().rotateH, 0);
		int nextX = (int) (nextPt.x * s2);
		int nextY = (int) (-nextPt.y * s2);

		P3D prevPt = parent.getPoint().rotate(getSkeleton().rotateV, getSkeleton().rotateH, 0);
		int prevX = (int) (prevPt.x * s1);
		int prevY = (int) (-prevPt.y * s1);

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
