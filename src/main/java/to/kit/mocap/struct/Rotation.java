package to.kit.mocap.struct;

import org.apache.commons.math3.linear.RealMatrix;

/**
 * Rotation information.
 * @author Hidetaka Sasai
 */
public final class Rotation {
	private char[] order = {'z', 'y', 'x'};
	/** X-axis rotation. */
	public Double x;
	/** Y-axis rotation. */
	public Double y;
	/** Z-axis rotation. */
	public Double z;

	protected RealMatrix getMatrix() {
		RealMatrix m = Radian.NO_EFFECT;
		RealMatrix mx = new Radian(this.x).rotateX();
		RealMatrix my = new Radian(this.y).rotateY();
		RealMatrix mz = new Radian(this.z).rotateZ();

		for (char c : this.order) {
			if (c == 'x') {
				m = m.multiply(mx);
			} else if (c == 'y') {
				m = m.multiply(my);
			} else if (c == 'z') {
				m = m.multiply(mz);
			}
		}
		return m;
	}

	protected RealMatrix getRevMatrix() {
		RealMatrix mx = new Radian(this.x).rev().rotateX();
		RealMatrix my = new Radian(this.y).rev().rotateY();
		RealMatrix mz = new Radian(this.z).rev().rotateZ();

		return mx.multiply(my).multiply(mz);
	}

	/**
	 * @param order the order to set
	 */
	public void setOrder(String order) {
		this.order = order.toCharArray();
	}
}
