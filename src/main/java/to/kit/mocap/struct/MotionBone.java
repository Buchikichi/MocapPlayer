package to.kit.mocap.struct;

public class MotionBone {
	private final String name;
	private final Double[] theta = { null, null, null };

	public MotionBone(String name) {
		this.name = name;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return this.name;
	}
	/**
	 * @return the theta
	 */
	public Double[] getTheta() {
		return this.theta;
	}
}
