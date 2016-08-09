package to.kit.mocap.struct;

public class P2D {
	public double x;
	public double y;

	public P2D(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public P2D rotate(double rad) {
		double c = Math.cos(rad);
		double s = Math.sin(rad);
		double nx = c * this.x - s * this.y;
		double ny = s * this.x + c * this.y;

		return new P2D(nx, ny);
	}
}
