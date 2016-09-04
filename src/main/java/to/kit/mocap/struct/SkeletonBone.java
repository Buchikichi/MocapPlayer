package to.kit.mocap.struct;

import java.awt.Color;
import java.awt.Graphics2D;

import org.apache.commons.math3.complex.Quaternion;
import org.apache.commons.math3.linear.MatrixUtils;
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
		double len = 10;
		RealMatrix mat = parent.getAccum().multiply(getAxisMatrix()).multiply(getThetaMatrix());
		double[][] par = mat.getData();
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
	protected RealMatrix getPositionMatrix() {
		return MatrixUtils.createRealMatrix(new double[][] {
			{ 1, 0, 0, -this.dir[0] * this.length },
			{ 0, 1, 0, -this.dir[1] * this.length },
			{ 0, 0, 1, -this.dir[2] * this.length },
			{ 0, 0, 0, 1 },
		});
	}

	@Override
	protected RealMatrix getAccum() {
		RealMatrix pm = getPositionMatrix();
		RealMatrix am = getAxisMatrix();

		RealMatrix tx = this.thetaX.rotateX();
		RealMatrix ty = this.thetaY.rotateY();
		RealMatrix tz = this.thetaZ.rotateZ();
		RealMatrix tm = tx.multiply(ty).multiply(tz);

		RealMatrix mat = am.multiply(tm).multiply(pm);

		SkeletonNode parent = getParent();

		if (parent != null) {
			return parent.getAccum().multiply(mat);
		}
		return mat;
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
//		P3D axi = getAxis(g, prevX, prevY);

		RealMatrix pa = parent.getAccum();
		RealMatrix pm = getPositionMatrix();
		RealMatrix am = getAxisMatrix();
		RealMatrix tm = getThetaMatrix();
		RealMatrix mat = pa.multiply(am).multiply(tm).multiply(pm);

//		P3D pt = getNextPoint().affine(getAccum());
//		P3D pt = P3D.ORIGIN.affine(getDirMatrix()).affine(ax.multiply(tx)).affine(px);
		P3D pt = P3D.ORIGIN.affine(getAccum());
//		P3D pt = P3D.ORIGIN.affine(mat);

		P3D rp = pt.rotate(getSkeleton().rotateV, getSkeleton().rotateH, 0);
		double x = rp.x;
		double y = rp.y;
		double z = rp.z;
		P3D nextPt = new P3D(-x, y, z);
		int nextX = (int) (nextPt.x * s2);
		int nextY = (int) (nextPt.y * s2);

//		String info = String.format("[%3.2f,%3.2f,%3.2f]", tx.toDegree(), ty.toDegree(), tz.toDegree());
//		g.setColor(Color.GRAY);
//		if (name.startsWith("l")) {
//			g.drawString(info + name, prevX + 10 * (depth - 1), prevY + 10);
//		}

		g.setColor(Color.LIGHT_GRAY);
		g.drawRoundRect(nextX, nextY, 3, 3, 3, 3);
		g.drawLine(prevX, prevY, nextX, nextY);

		setPoint(nextPt);
		for (SkeletonNode node : getJoint()) {
			node.draw(g);
		}
	}
}
