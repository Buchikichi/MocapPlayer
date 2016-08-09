package to.kit.mocap.struct;

public class SkeletonBone {
	private int id;
	private String name;
	private String direction;
	private String length;
	private String axis;
	/** Degrees of Freedom. */
	private String dof;
	private String limits;
	/**
	 * @return the id
	 */
	public int getId() {
		return this.id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
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
	 * @return the direction
	 */
	public String getDirection() {
		return this.direction;
	}
	/**
	 * @param direction the direction to set
	 */
	public void setDirection(String direction) {
		this.direction = direction;
	}
	/**
	 * @return the length
	 */
	public String getLength() {
		return this.length;
	}
	/**
	 * @param length the length to set
	 */
	public void setLength(String length) {
		this.length = length;
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
	 * @return the dof
	 */
	public String getDof() {
		return this.dof;
	}
	/**
	 * @param dof the dof to set
	 */
	public void setDof(String dof) {
		this.dof = dof;
	}
	/**
	 * @return the limits
	 */
	public String getLimits() {
		return this.limits;
	}
	/**
	 * @param limits the limits to set
	 */
	public void setLimits(String limits) {
		this.limits = limits;
	}
}
