package to.kit.mocap.struct;

import org.apache.commons.math3.linear.RealMatrix;

/**
 * Rotation information.
 * @author Hidetaka Sasai
 */
public final class Rotation {
	/** X-axis rotation. */
	public Double x;
	/** Y-axis rotation. */
	public Double y;
	/** Z-axis rotation. */
	public Double z;

	protected RealMatrix getMatrix() {
		RealMatrix mx = new Radian(this.x).rotateX();
		RealMatrix my = new Radian(this.y).rotateY();
		RealMatrix mz = new Radian(this.z).rotateZ();

		return mz.multiply(my).multiply(mx);
	}

	protected RealMatrix getRevMatrix() {
		RealMatrix mx = new Radian(this.x).rev().rotateX();
		RealMatrix my = new Radian(this.y).rev().rotateY();
		RealMatrix mz = new Radian(this.z).rev().rotateZ();

		return mx.multiply(my).multiply(mz);
	}
}
