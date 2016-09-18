package to.kit.mocap.struct;

import net.arnx.jsonic.JSONHint;

public class MotionRoot extends MotionBone {
	private P3D point;

	public MotionRoot(String name) {
		super(name);
	}

	/**
	 * @return the point
	 */
	@JSONHint(name="p")
	public P3D getPoint() {
		return this.point;
	}
	/**
	 * @param point the point to set
	 */
	public void setPoint(P3D point) {
		this.point = point;
	}
}
