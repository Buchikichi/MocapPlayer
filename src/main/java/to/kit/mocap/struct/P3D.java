package to.kit.mocap.struct;

/**
 * The position in 3D.
 * @author Hidetaka Sasai
 */
public final class P3D {
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
}
