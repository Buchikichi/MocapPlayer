package to.kit.mocap.struct;

import java.awt.Color;
import java.awt.Graphics2D;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SkeletonBone extends SkeletonNode {
	/** Logger. */
	private static final Logger LOG = LoggerFactory.getLogger(SkeletonBone.class);

	private int id;
	private double[] direction;
	private double tX;
	private double tY;
	private double tZ;
	private double length;
	private double axisX;
	private double axisY;
	private double axisZ;
	/** Degrees of Freedom. */
	private String[] dof;
	private String limits;
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
	 * @return the direction
	 */
	public double[] getDirection() {
		return this.direction;
	}
	/**
	 * @param values the direction to set
	 */
	public void setDirection(double[] values) {
		this.direction = values;
		this.tX = values[0];
		this.tY = values[1];
		this.tZ = values[2];
	}
	/**
	 * @return the tX
	 */
	public double gettX() {
		return this.tX;
	}
	/**
	 * @param tX the tX to set
	 */
	public void settX(double tX) {
		this.tX = tX;
	}
	/**
	 * @return the tY
	 */
	public double gettY() {
		return this.tY;
	}
	/**
	 * @param tY the tY to set
	 */
	public void settY(double tY) {
		this.tY = tY;
	}
	/**
	 * @return the tZ
	 */
	public double gettZ() {
		return this.tZ;
	}
	/**
	 * @param tZ the tZ to set
	 */
	public void settZ(double tZ) {
		this.tZ = tZ;
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
	 * @return the axisX
	 */
	public double getAxisX() {
		return this.axisX;
	}
	/**
	 * @param axisX the axisX to set
	 */
	public void setAxisX(double axisX) {
		this.axisX = axisX;
	}
	/**
	 * @return the axisY
	 */
	public double getAxisY() {
		return this.axisY;
	}
	/**
	 * @param axisY the axisY to set
	 */
	public void setAxisY(double axisY) {
		this.axisY = axisY;
	}
	/**
	 * @return the axisZ
	 */
	public double getAxisZ() {
		return this.axisZ;
	}
	/**
	 * @param axisZ the axisZ to set
	 */
	public void setAxisZ(double axisZ) {
		this.axisZ = axisZ;
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
	 * @return the limits
	 */
	public String getLimits() {
		return this.limits;
	}
	/**
	 * @param limits the limits to set
	 */
	public void setLimits(String limits) {
		this.limits = limits;
	}

	static Color[] cols = { Color.BLUE, Color.RED, Color.MAGENTA, Color.GREEN, Color.CYAN, Color.YELLOW };

	@Override
	public void draw(Graphics2D g, P3D pt) {
		double r = this.length * 10;
		double x = Math.cos(this.tY) * r;
		double z = Math.sin(this.tY) * r;
		double y = Math.sin(this.tZ) * x;

		g.setColor(cols[this.id % cols.length]);
		x = Math.cos(this.tZ) * x;
		int x1 = (int) pt.x;
		int y1 = (int) pt.y;
		int x2 = (int) x;
		int y2 = (int) y;
		g.drawLine(x1, y1, x2, y2);
		P3D nextPt = new P3D(pt.x + x, pt.y + y, pt.z + z);
//LOG.info("[{}]{},{},{}", this.getName(), Double.valueOf(x), Double.valueOf(y), Double.valueOf(z));

		for (SkeletonNode node : getJoint()) {
			node.draw(g, nextPt);
		}
	}
}
