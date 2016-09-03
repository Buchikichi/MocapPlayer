package to.kit.mocap.struct;

import java.awt.Color;
import java.awt.Graphics2D;

import org.apache.commons.math3.complex.Quaternion;
import org.apache.commons.math3.linear.RealMatrix;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class SkeletonBone extends SkeletonNode {
	/** Logger. */
	private static final Logger LOG = LoggerFactory.getLogger(SkeletonBone.class);

	private int id;
	private final double[] dir = new double[3];
	private double length;
	/** Degrees of Freedom. */
	private String[] dof;
	private Limit limitX = new Limit();
	private Limit limitY = new Limit();
	private Limit limitZ = new Limit();

	private double[] cross(double[] a, double[] b) {
		return new double[] {
			a[1] * b[2] - a[2] * b[1],
			a[2] * b[0] - a[0] * b[2],
			a[0] * b[1] - a[1] * b[0],
		};
	}

	private double dot(double[] a, double[] b) {
		return (a[0] * b[0] + a[1] * b[1] + a[2] * b[2]);
	}

	private double mag(double[] a) {
		return (Math.sqrt(a[0] * a[0] + a[1] * a[1] + a[2] * a[2]));
	}

	private double getAngle(double[] v1, double[] v2, double[] axis) {
		double dot_prod = dot(v1, v2);
		double r_axis_len = mag(axis);

		return Math.atan2(r_axis_len, dot_prod);
	}

	@Override
	protected RealMatrix getAccum() {
		RealMatrix ax = getAxisMatrix();
		RealMatrix tx = getThetaMatrix();
		RealMatrix ma = ax.multiply(tx);
		SkeletonNode parent = getParent();
		double[] a = { 0.0, 0.0, 1.0 };
		double cx = a[1] * this.dir[2] - a[2] * this.dir[1];
		double cy = a[2] * this.dir[0] - a[0] * this.dir[2];
		double cz = a[0] * this.dir[1] - a[1] * this.dir[0];
		double[] axis = { cx, cy, cz };
		double theta = getAngle(a, this.dir, axis);

		if (parent instanceof SkeletonBone) {
			ma = parent.getAccum().multiply(ma);
		}
		return ma;
	}

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

	private double[][] getMatrix(double w, double x, double y, double z) {
		return new double[][] {
			{ 1 - 2 * (y * y + z * z), 2 * (x * y + w * z), 2 * (x * z - w * y), 0 },
			{ 2 * (x * y - w * z), 1 - 2 * (z * z + x * x), 2 * (y * z + w * x), 0 },
			{ 2 * (x * z + w * y), 2 * (y * z - w * x), 1 - 2 * (x * x + y * y), 0 },
			{ 0, 0, 0, 1 }
		};
//		return new double[][] {
//			{ 1 - 2 * (y * y + z * z), 2 * (x * y - w * z), 2 * (x * z + w * y), 0 },
//			{ 2 * (x * y + w * z), 1 - 2 * (x * x + z * z), 2 * (y * z - w * x), 0 },
//			{ 2 * (x * z - w * y), 2 * (y * z + w * x), 1 - 2 * (x * x + y * y), 0 },
//			{ 0, 0, 0, 1 }
//		};
//		double m11 = w * w + x * x - y * y - z * z;
//		double m22 = w * w - x * x + y * y - z * z;
//		double m33 = w * w - x * x - y * y + z * z;
//		return new double[][] {
//			{ m11, 2 * (x * y - w * z), 2 * (x * z + w * y), 0 },
//			{ 2 * (x * y + w * z), m22, 2 * (y * z - w * x), 0 },
//			{ 2 * (x * z - w * y), 2 * (y * z + w * x), m33, 0 },
//			{ 0, 0, 0, 1 }
//		};
	}

	private double[][] getConjugateMatrix(double w, double x, double y, double z) {
		return new double[][] {
			{ 1 - 2 * (y * y - z * z), 2 * (x * y + w * z), 2 * (x * z - w * y), 0 },
			{ 2 * (x * y - w * z), 1 - 2 * (z * z - x * x), 2 * (y * z + w * x), 0 },
			{ 2 * (x * z + w * y), 2 * (y * z - w * x), 1 - 2 * (x * x - y * y), 0 },
			{ 0, 0, 0, 1 }
		};
	}

	private P3D getNextPoint() {
		double[] a = { 0, 0, 1 };
		double[] direction = { -this.dir[0], -this.dir[1], -this.dir[2] };
		double[] axis = cross(a, direction);
		double theta = getAngle(a, direction, axis) / 2;
		double sin = Math.sin(theta);
		double w = Math.cos(theta);
		double x = axis[0] * sin;
		double y = axis[1] * sin;
		double z = axis[2] * sin;
		Quaternion base = Quaternion.IDENTITY;
		Quaternion pt = new Quaternion(0, new double[] { 0, 0, this.length });
		Quaternion quat = new Quaternion(w, new double[] { x, y, z });
		Quaternion conj = quat.getConjugate();
		Quaternion q = base.multiply(quat).multiply(pt).multiply(conj);
		double[] part = q.getVectorPart();
		double[][] mat1 = getMatrix(0, 0, 0, this.length);
		double[][] mat2 = getMatrix(w, x, y, z);
		double[][] mat3 = getMatrix(w, -x, -y, -z);
//		double[][] matQ = getMatrix(quat.getQ0(), q[0], q[1], q[2]);

//		return new P3D(0, 0, 1).affine(mat2).affine(mat1).affine(mat3);
		return new P3D(part[0], part[1], part[2]);
//		return new P3D(-this.dir[0] * this.length, -this.dir[1] * this.length, -this.dir[2] * this.length);
	}

	private P3D getAxis(Graphics2D g, int prevX, int prevY) {
		SkeletonNode parent = getParent();
		String name = getName();
		int depth = this.getDepth();
		double len = 20;
		RealMatrix ax = getAxisMatrix();
		RealMatrix mat = parent.getAccum().multiply(ax);
		double[][] par = mat.getData();
		double x = this.dir[0] * len;
		double y = this.dir[1] * len;
		double z = this.dir[2] * len;
		P3D px = new P3D(len, 0, 0).affine(par);
		P3D py = new P3D(0, len, 0).affine(par);
		P3D pz = new P3D(0, 0, len).affine(par);
		P3D nx = px.rotate(getSkeleton().rotateV, getSkeleton().rotateH, 0);
		P3D ny = py.rotate(getSkeleton().rotateV, getSkeleton().rotateH, 0);
		P3D nz = pz.rotate(getSkeleton().rotateV, getSkeleton().rotateH, 0);

		g.setColor(Color.RED);
		g.drawLine(prevX, prevY, (int) (prevX + nx.x), (int) (prevY + nx.y));
		g.setColor(Color.GREEN);
		g.drawLine(prevX, prevY, (int) (prevX + ny.x), (int) (prevY + ny.y));
		g.setColor(Color.BLUE);
		g.drawLine(prevX, prevY, (int) (prevX + nz.x), (int) (prevY + nz.y));
		Double degX = getAxisX().toDegree();
		Double degY = getAxisY().toDegree();
		Double degZ = getAxisZ().toDegree();
		String axis = String.format("(%3.2f,%3.2f,%3.2f)", degX, degY, degZ);
		g.setColor(Color.BLACK);
		if (name.startsWith("l")) {
			axis += name;
			g.drawString(axis, prevX + 10 * (depth - 1), prevY + 0 * (depth - 1));
		}
		//*/
		return new P3D(px.x, py.y, pz.z);
	}

	@Override
	public void draw(Graphics2D g) {
		double s1 = 15;
		double s2 = 15;
		SkeletonNode parent = getParent();
		P3D prevPt = parent.getPoint();
		int prevX = (int) (prevPt.x * s1);
		int prevY = (int) (prevPt.y * s1);
		String name = getName();
		int depth = this.getDepth();
		P3D axi = getAxis(g, prevX, prevY);

		RealMatrix ax = getAxisMatrix();
		RealMatrix tx = getThetaMatrix();
		RealMatrix mat = parent.getAccum().multiply(ax).multiply(tx);
		double[][] parX = mat.getData();
		double x = this.dir[0] * this.length;
		double y = this.dir[1] * this.length;
		double z = this.dir[2] * this.length;

		P3D pt = getNextPoint().affine(parX);
		x = pt.x;
		y = pt.y;
		z = pt.z;

/*		if (parent instanceof SkeletonBone) {
			SkeletonBone pb = (SkeletonBone) parent;
			pb = this;
			double[] pdir = { axi.x, axi.y, axi.z };
			double[] rax = new double[] { -pdir[1], pdir[0], 0 };
			double dot_prod = pdir[2];
			double r_axis_len = Math.sqrt(rax[0] * rax[0] + rax[1] * rax[1]);
			double th = Math.atan2(r_axis_len, dot_prod) / 2;
			double sin = Math.sin(th);
			Quaternion p = new Quaternion(0, new double[] { x, y, z });
//			p.x = x;
//			p.y = y;
//			p.z = z;
//			p.w = 0;
			Quaternion qx = new Quaternion(Math.cos(th), pdir[0] * sin, pdir[1] * sin, pdir[2] * sin);
			Quaternion r = new Quaternion(Math.cos(th), -pdir[0] * sin, -pdir[1] * sin, -pdir[2] * sin);
			r.multiply(p);
			r.multiply(qx);
			//
//			x = r.x;
//			y = r.y;
//			z = r.z;
		}
		//*/

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

		P3D nextPt = prevPt.add(-x, -y, z);
		int x2 = (int) (nextPt.x * s2);
		int y2 = (int) (nextPt.y * s2);

//		String info = String.format("[%3.2f,%3.2f,%3.2f]", tx.toDegree(), ty.toDegree(), tz.toDegree());
//		g.setColor(Color.GRAY);
//		if (name.startsWith("l")) {
//			g.drawString(info + name, prevX + 10 * (depth - 1), prevY + 10);
//		}

		g.setColor(Color.GRAY);
		g.drawRoundRect(x2, y2, 3, 3, 3, 3);
		g.drawLine(prevX, prevY, x2, y2);

		setPoint(nextPt);
		for (SkeletonNode node : getJoint()) {
			node.draw(g);
		}
	}
}
