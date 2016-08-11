package to.kit.mocap.struct;

import java.awt.Color;
import java.awt.Graphics2D;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import to.kit.mocap.util.MathExt;

public class SkeletonBone extends SkeletonNode {
	/** Logger. */
	private static final Logger LOG = LoggerFactory.getLogger(SkeletonBone.class);

	private int id;
	private double dirX;
	private double dirY;
	private double dirZ;
	private double thetaX;
	private double thetaY;
	private double thetaZ;
	private double length;
	private String order;
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
	 * @param values the direction to set
	 */
	public void setDirection(double[] values) {
		this.dirX = values[0];
		this.dirY = values[1];
		this.dirZ = values[2];
	}
	/**
	 * @return the tX
	 */
	public double getThetaX() {
		return this.thetaX;
	}
	/**
	 * @param tX the tX to set
	 */
	public void setThetaX(double tX) {
		this.thetaX = tX;
	}
	/**
	 * @return the tY
	 */
	public double getThetaY() {
		return this.thetaY;
	}
	/**
	 * @param tY the tY to set
	 */
	public void setThetaY(double tY) {
		this.thetaY = tY;
	}
	/**
	 * @return the tZ
	 */
	public double getThetaZ() {
		return this.thetaZ;
	}
	/**
	 * @param tZ the tZ to set
	 */
	public void setThetaZ(double tZ) {
		this.thetaZ = tZ;
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
	 * @param values the values to set
	 */
	public void setAxis(double[] values) {
		setAxisX(values[0]);
		setAxisY(values[1]);
		setAxisZ(values[2]);
	}
	/**
	 * @return the order
	 */
	public String getOrder() {
		return this.order;
	}
	/**
	 * @param order
	 */
	public void setOrder(String order) {
		this.order = order;
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
	public String toString() {
		double tx = this.limitX.trim(this.thetaX) * 180 / Math.PI;
		double ty = this.limitY.trim(this.thetaY) * 180 / Math.PI;
		double tz = this.limitZ.trim(this.thetaZ) * 180 / Math.PI;

		return StringUtils.repeat(' ', getDepth()) + getName() + "[" + tx + "/" + this.limitX + ", " + ty + "/" + this.limitY + "]";
	}

	static Color[] cols = { Color.BLUE, Color.RED, Color.MAGENTA, Color.GREEN, Color.CYAN, Color.YELLOW, Color.WHITE };

	@Override
	public void draw(Graphics2D g, SkeletonNode parent) {
		int depth = this.getDepth();
		double x = this.dirX * this.length;
		double y = this.dirY * this.length;
		double z = this.dirZ * this.length;
		P2D yz = new P2D(y, z).rotate(getAxisX());
		y = yz.x;
		z = yz.y;
		P2D zx = new P2D(z, x).rotate(getAxisY());
		z = zx.x;
		x = zx.y;
		P2D xy = new P2D(x, y).rotate(getAxisZ());
		x = xy.x;
		y = xy.y;

		yz = new P2D(y, z).rotate(this.limitX.trim(this.thetaX));
		y = yz.x;
		z = yz.y;
		zx = new P2D(z, x).rotate(this.limitY.trim(this.thetaY));
		z = zx.x;
		x = zx.y;
		xy = new P2D(x, y).rotate(this.limitZ.trim(this.thetaZ));
		x = xy.x;
		y = xy.y;

		double rx = MathExt.trim(0);
		double ry = MathExt.trim(getSkeleton().rotateY);
		double rz = MathExt.trim(0);
		yz = new P2D(y, z).rotate(rx);
		y = yz.x;
		z = yz.y;
		zx = new P2D(z, x).rotate(ry);
		z = zx.x;
		x = zx.y;
		xy = new P2D(x, y).rotate(rz);
		x = xy.x;
		y = xy.y;

		P3D prevPt = parent.getPoint();
		P3D nextPt = prevPt.add(x, -y, z);
		double s1 = 1000;
		double s2 = 1000;
		int prevX = (int) (prevPt.x * s1);
		int prevY = (int) (prevPt.y * s1);
		int x2 = (int) (nextPt.x * s2);
		int y2 = (int) (nextPt.y * s2);

//		LOG.debug("{}", toString());
//		g.setColor(Color.LIGHT_GRAY);
		g.setColor(cols[depth % cols.length]);
		g.drawString(this.getName(), x2, y2);
		g.drawRoundRect(x2, y2, 4, 4, 3, 3);
		g.drawLine(prevX, prevY, x2, y2);

		setPoint(nextPt);
		for (SkeletonNode node : getJoint()) {
			node.draw(g, this);
		}
	}
}
