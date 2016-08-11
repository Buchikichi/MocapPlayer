package to.kit.mocap.util;

public class MathExt {
	public static double PI2 = Math.PI * 2;

	public static double trim(double radian) {
		double rad = radian;

		while (Math.PI < rad) {
			rad -= PI2;
		}
		while (rad < -Math.PI) {
			rad += PI2;
		}
		return rad;
	}
}
