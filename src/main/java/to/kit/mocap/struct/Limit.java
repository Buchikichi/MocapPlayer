package to.kit.mocap.struct;

/**
 * @author Hidetaka Sasai
 */
public final class Limit {
	private double min = Double.MIN_VALUE;
	private double max = Double.MAX_VALUE;

	/**
	 * @param source
	 * @return trimmed value
	 */
	public double trim(double source) {
		if (source < this.min) {
			return this.min;
		}
		if (this.max < source) {
			return this.max;
		}
		return source;
	}

	/**
	 * @return the min
	 */
	public double getMin() {
		return this.min;
	}
	/**
	 * @param min the min to set
	 */
	public void setMin(double min) {
		this.min = min;
	}
	/**
	 * @return the max
	 */
	public double getMax() {
		return this.max;
	}
	/**
	 * @param max the max to set
	 */
	public void setMax(double max) {
		this.max = max;
	}

	@Override
	public String toString() {
		double mn = this.min * 180 / Math.PI;
		double mx = this.max * 180 / Math.PI;

		return "Limit[" + mn + ", " + mx + "]";
	}
}
