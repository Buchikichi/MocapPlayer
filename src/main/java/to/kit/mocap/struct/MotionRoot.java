package to.kit.mocap.struct;

public class MotionRoot extends MotionBone {
	private P3D point;

	public MotionRoot(String name) {
		super(name);
	}

	/**
	 * @return the point
	 */
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
