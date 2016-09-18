package to.kit.mocap.struct;

import net.arnx.jsonic.JSONHint;

/**
 * Motion information.
 * @author Hidetaka Sasai
 */
public class MotionBone {
	private final String name;
	private final Rotation theta = new Rotation();

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
	@JSONHint(name="r")
	public Rotation getTheta() {
		return this.theta;
	}
}
