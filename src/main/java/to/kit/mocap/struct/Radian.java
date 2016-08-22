package to.kit.mocap.struct;

public final class Radian {
	private static final double[][] NO_EFFECT = { { 1, 0, 0, 0 }, { 0, 1, 0, 0 }, { 0, 0, 1, 0 }, { 0, 0, 0, 1 } };
	private Double radian;

	public Radian(Double rad) {
		this.radian = rad;
	}

	public Radian rev() {
		if (this.radian != null) {
			return new Radian(Double.valueOf(-this.radian.doubleValue()));
		}
		return this;
	}

	public Radian add(Double rad) {
		if (rad != null) {
			if (this.radian == null) {
				this.radian = rad;
			} else {
				this.radian = Double.valueOf(this.radian.doubleValue() + rad.doubleValue());
			}
		}
		return this;
	}

	public double[][] rotateX() {
		if (this.radian == null) {
			return NO_EFFECT;
		}
		double t = this.radian.doubleValue();

		return new double[][] {
			{ 1, 0, 0, 0 },
			{ 0, Math.cos(t), -Math.sin(t), 0 },
			{ 0, Math.sin(t), Math.cos(t), 0 },
			{ 0, 0, 0, 1 },
		};
	}

	public double[][] rotateY() {
		if (this.radian == null) {
			return NO_EFFECT;
		}
		double t = this.radian.doubleValue();

		return new double[][] {
			{ Math.cos(t), 0, Math.sin(t), 0 },
			{ 0, 1, 0, 0 },
			{ -Math.sin(t), 0, Math.cos(t), 0 },
			{ 0, 0, 0, 1 },
		};
	}

	public double[][] rotateZ() {
		if (this.radian == null) {
			return NO_EFFECT;
		}
		double t = this.radian.doubleValue();

		return new double[][] {
			{ Math.cos(t), -Math.sin(t), 0, 0 },
			{ Math.sin(t), Math.cos(t), 0, 0 },
			{ 0, 0, 1, 0 },
			{ 0, 0, 0, 1 },
		};
	}

	/**
	 * @param deg
	 */
	public void setDegree(Double deg) {
		if (deg != null) {
			this.radian = Double.valueOf(deg.doubleValue() * Math.PI / 180);
		}
	}
	/**
	 * @return the degree
	 */
	public Double toDegree() {
		if (this.radian == null) {
			return null;
		}
		return Double.valueOf(this.radian.doubleValue() * 180 / Math.PI);
	}
	/**
	 * @return the radian
	 */
	public Double getRadian() {
		return this.radian;
	}
	/**
	 * @param rad the radian to set
	 */
	public void setRadian(Double rad) {
		this.radian = rad;
	}
}
