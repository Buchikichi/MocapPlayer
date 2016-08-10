package to.kit.mocap.struct;

public class MotionBone {
	private String name;
	private Double thetaX;
	private Double thetaY;
	private Double thetaZ;

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
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the tX
	 */
	public Double getThetaX() {
		return this.thetaX;
	}
	/**
	 * @param tX the tX to set
	 */
	public void setThetaX(Double tX) {
		this.thetaX = tX;
	}
	/**
	 * @return the tY
	 */
	public Double getThetaY() {
		return this.thetaY;
	}
	/**
	 * @param tY the tY to set
	 */
	public void setThetaY(Double tY) {
		this.thetaY = tY;
	}
	/**
	 * @return the tZ
	 */
	public Double getThetaZ() {
		return this.thetaZ;
	}
	/**
	 * @param tZ the tZ to set
	 */
	public void setThetaZ(Double tZ) {
		this.thetaZ = tZ;
	}
}
