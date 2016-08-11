package to.kit.mocap.struct;

public class MotionBone {
	private final String name;
	private final double[] theta = { 0.0, 0.0, 0.0 };

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
	public double[] getTheta() {
		return this.theta;
	}
}
