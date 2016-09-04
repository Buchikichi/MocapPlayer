package to.kit.mocap.struct;

import org.apache.commons.math3.linear.RealMatrix;

/**
 * The position in 3D.
 * @author Hidetaka Sasai
 */
public final class P3D {
	/** origin. */
	public static final P3D ORIGIN = new P3D(0, 0, 0);

	/** x. */
	public double x;
	/** y. */
	public double y;
	/** z. */
	public double z;

	/**
	 * Add.
	 * @param dx
	 * @param dy
	 * @param dz
	 * @return new position
	 */
	public P3D add(double dx, double dy, double dz) {
		return new P3D(this.x + dx, this.y + dy, this.z + dz);
	}

	/**
	 * @param x
	 * @param y
	 * @param z
	 */
	public P3D(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public P3D affine(double m[][]) {
		if (m == null) {
			return this;
		}
		double nx = m[0][0] * this.x + m[0][1] * this.y + m[0][2] * this.z + m[0][3];
		double ny = m[1][0] * this.x + m[1][1] * this.y + m[1][2] * this.z + m[1][3];
		double nz = m[2][0] * this.x + m[2][1] * this.y + m[2][2] * this.z + m[2][3];
		return new P3D(nx, ny, nz);
	}

	public P3D affine(RealMatrix mx) {
		return affine(mx.getData());
	}

	public P3D rotate(double tx, double ty, double tz) {
		double nx = this.x;
		double ny = this.y;
		double nz = this.z;
		P2D xy = new P2D(nx, ny).rotate(tz);
		nx = xy.x;
		ny = xy.y;
		P2D zx = new P2D(nz, nx).rotate(ty);
		nz = zx.x;
		nx = zx.y;
		P2D yz = new P2D(ny, nz).rotate(tx);
		ny = yz.x;
		nz = yz.y;
		return new P3D(nx, ny, nz);
	}

	@Override
	public String toString() {
		return "P3D [x=" + this.x + ", y=" + this.y + ", z=" + this.z + "]";
	}
}
