package to.kit.mocap.struct;

import java.awt.Color;
import java.awt.Graphics2D;

import javax.vecmath.Quat4d;

import org.apache.commons.math3.linear.RealMatrix;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class SkeletonBone extends SkeletonNode {
	/** Logger. */
	private static final Logger LOG = LoggerFactory.getLogger(SkeletonBone.class);

	private int id;
	private final double[] dir = new double[3];
	private double dirY;
	private double dirZ;
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

	private P3D getAxis(Graphics2D g, int prevX, int prevY) {
		SkeletonNode parent = getParent();
		String name = getName();
		int depth = this.getDepth();
		double len = 20;
		Radian ax = getAxisX();
		Radian ay = getAxisY();
		Radian az = getAxisZ();
		RealMatrix matX = parent.getAccumX();//.multiply(MatrixUtils.createRealMatrix(ax.rotateX()));
		RealMatrix matY = parent.getAccumY();//.multiply(MatrixUtils.createRealMatrix(ay.rotateY()));
		RealMatrix matZ = parent.getAccumZ();//.multiply(MatrixUtils.createRealMatrix(az.rotateZ()));
		double[][] parX = matX.getData();
		double[][] parY = matY.getData();
		double[][] parZ = matZ.getData();

		P3D px = new P3D(len, 0, 0).affine(parX).affine(parY).affine(parZ)
				.affine(ax.rotateX()).affine(ay.rotateY()).affine(az.rotateZ())
				;
		P3D py = new P3D(0, len, 0).affine(parX).affine(parY).affine(parZ)
				.affine(ax.rotateX()).affine(ay.rotateY()).affine(az.rotateZ())
				;
		P3D pz = new P3D(0, 0, len).affine(parX).affine(parY).affine(parZ)
				.affine(ax.rotateX()).affine(ay.rotateY()).affine(az.rotateZ())
				;
		P3D nx = px.rotate(getSkeleton().rotateV, getSkeleton().rotateH, 0);
		P3D ny = py.rotate(getSkeleton().rotateV, getSkeleton().rotateH, 0);
		P3D nz = pz.rotate(getSkeleton().rotateV, getSkeleton().rotateH, 0);

		g.setColor(Color.RED);
		g.drawLine(prevX, prevY, (int) (prevX + nx.x), (int) (prevY + nx.y));
		g.setColor(Color.GREEN);
		g.drawLine(prevX, prevY, (int) (prevX + ny.x), (int) (prevY + ny.y));
		g.setColor(Color.BLUE);
		g.drawLine(prevX, prevY, (int) (prevX + nz.x), (int) (prevY + nz.y));
		Double degX = ax.toDegree();
		Double degY = ay.toDegree();
		Double degZ = az.toDegree();
		String axis = String.format("(%3.2f,%3.2f,%3.2f)", degX, degY, degZ);
		g.setColor(Color.BLACK);
		if (name.startsWith("l")) {
			axis += name;
			g.drawString(axis, prevX + (name.startsWith("r") ? -150 : 0), prevY + 0 * (depth - 1));
		}
		return new P3D(px.x, py.y, pz.z);
	}

	@Override
	public void draw(Graphics2D g, SkeletonNode parent) {
		double s1 = 27;
		double s2 = 27;
		P3D prevPt = parent.getPoint();
		int prevX = (int) (prevPt.x * s1);
		int prevY = (int) (prevPt.y * s1);
		String name = getName();
		int depth = this.getDepth();
		P3D axi = getAxis(g, prevX, prevY);

		double x = this.dir[0] * this.length;
		double y = this.dir[1] * this.length;
		double z = this.dir[2] * this.length;
//		double ax = 0*parent.getAxisX() + 1*getAxisX() + 0*getAccumAxisX();
//		double ay = 0*parent.getAxisY() + 1*getAxisY() + 0*getAccumAxisY();
//		double az = 0*parent.getAxisZ() + 1*getAxisZ() + 0*getAccumAxisZ();
		Radian ax = getAxisX();
		Radian ay = getAxisY();
		Radian az = getAxisZ();
		RealMatrix matX = parent.getAccumX();
		RealMatrix matY = parent.getAccumY();
		RealMatrix matZ = parent.getAccumZ();
		double[][] parX = matX.getData();
		double[][] parY = matY.getData();
		double[][] parZ = matZ.getData();
		Radian tx = this.thetaX;
		Radian ty = this.thetaY;
		Radian tz = this.thetaZ;

		P3D pm = new P3D(x, y, z).affine(tx.rotateX()).affine(ty.rotateY()).affine(tz.rotateZ());
		x = pm.x;
		y = pm.y;
		z = pm.z;
		P3D pt = new P3D(x, y, z).affine(parX).affine(parY).affine(parZ)
				.affine(ax.rotateX()).affine(ay.rotateY()).affine(az.rotateZ());
//		P3D pt = new P3D(x, y, z).affine(ax.rotateX()).affine(ay.rotateY()).affine(az.rotateZ());
		x = pt.x;
		y = pt.y;
		z = pt.z;

		if (parent instanceof SkeletonBone) {
			SkeletonBone pb = (SkeletonBone) parent;
			pb = this;
			double[] pdir = { axi.x, axi.y, axi.z };
			double[] rax = new double[] { -pdir[1], pdir[0], 0 };
			double dot_prod = pdir[2];
			double r_axis_len = Math.sqrt(rax[0] * rax[0] + rax[1] * rax[1]);
			double theta = Math.atan2(r_axis_len, dot_prod) / 2;
			double sin = Math.sin(theta);
			Quat4d p = new Quat4d(x, y, z, 0);
//			p.x = x;
//			p.y = y;
//			p.z = z;
//			p.w = 0;
			Quat4d qx = new Quat4d(pdir[0] * sin, pdir[1] * sin, pdir[2] * sin, Math.cos(theta));
			Quat4d r = new Quat4d(-pdir[0] * sin, -pdir[1] * sin, -pdir[2] * sin, Math.cos(theta));
			r.mul(p);
			r.mul(qx);
			//
//			x = r.x;
//			y = r.y;
//			z = r.z;
		}

//		SkeletonRoot root = getRoot();
//		double rx = MathExt.trim(getSkeleton().rotateV + 0*root.getThetaX());
//		double ry = MathExt.trim(getSkeleton().rotateH + 0*root.getThetaY());
//		double rz = MathExt.trim(0*root.getThetaZ());
//		yz = new P2D(y, z).rotate(rx);
//		y = yz.x;
//		z = yz.y;
//		zx = new P2D(z, x).rotate(ry);
//		z = zx.x;
//		x = zx.y;
//		xy = new P2D(x, y).rotate(rz);
//		x = xy.x;
//		y = xy.y;
		P3D rp = new P3D(x, y, z).rotate(getSkeleton().rotateV, getSkeleton().rotateH, 0);
		x = rp.x;
		y = rp.y;
		z = rp.z;

		P3D nextPt = prevPt.add(x, y, z);
		int x2 = (int) (nextPt.x * s2);
		int y2 = (int) (nextPt.y * s2);

//		LOG.debug("{}", toString());
		String info = String.format("[%3.2f,%3.2f,%3.2f]", tx.toDegree(), ty.toDegree(), tz.toDegree());
		
		g.setColor(Color.GRAY);
		if (name.startsWith("l")) {
			g.drawString(info + name, prevX + (name.startsWith("r") ? -150 : 0), prevY + 0 * (depth - 1) + 10);
		}

		g.setColor(Color.LIGHT_GRAY);
//		g.drawString(this.getName(), x2, y2);
		g.drawRoundRect(x2, y2, 3, 3, 3, 3);
		g.drawLine(prevX, prevY, x2, y2);

		setPoint(nextPt);
		for (SkeletonNode node : getJoint()) {
			node.draw(g, this);
		}
	}
}
