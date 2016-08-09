package to.kit.mocap.struct;

public class SkeletonRoot {
	private String order;
	private String axis;
	private String position;
	private String orientation;

	/**
	 * @return the order
	 */
	public String getOrder() {
		return this.order;
	}
	/**
	 * @param order the order to set
	 */
	public void setOrder(String order) {
		this.order = order;
	}
	/**
	 * @return the axis
	 */
	public String getAxis() {
		return this.axis;
	}
	/**
	 * @param axis the axis to set
	 */
	public void setAxis(String axis) {
		this.axis = axis;
	}
	/**
	 * @return the position
	 */
	public String getPosition() {
		return this.position;
	}
	/**
	 * @param position the position to set
	 */
	public void setPosition(String position) {
		this.position = position;
	}
	/**
	 * @return the orientation
	 */
	public String getOrientation() {
		return this.orientation;
	}
	/**
	 * @param orientation the orientation to set
	 */
	public void setOrientation(String orientation) {
		this.orientation = orientation;
	}
}
