package to.kit.mocap.struct;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

/**
 * Anima.
 * @author Hidetaka Sasai
 */
public final class Anima {
	private final Skeleton skeleton;
	private final List<Motion> motionList = new ArrayList<>();
	private int previous;
	private int current;

	private List<Motion> getCumulateList() {
		List<Motion> list = new ArrayList<>();

		if (this.motionList.isEmpty()) {
			return list;
		}
		if (this.current == this.previous) {
			return list;
		}
		int sign = this.current - this.previous < 0 ? -1 : 1;
		int cur = this.previous;

		for (;;) {
			cur += sign;
			boolean isCurrent = cur == this.current;
			Motion motion = this.motionList.get(cur);

			motion.setReduction(!isCurrent);
			list.add(motion);
			if (isCurrent) {
				break;
			}
		}
		this.previous = this.current;
		return list;
	}

	/**
	 * @param g2d
	 */
	public void draw(Graphics2D g2d) {
		int sign = this.current - this.previous < 0 ? -1 : 1;
		List<Motion> cumulateList = getCumulateList();

		for (Motion motion : cumulateList) {
			this.skeleton.shift(motion, sign);
		}
		this.skeleton.draw(g2d);
	}

	/**
	 * Add motions.
	 * @param list MotionList
	 */
	public void addMotion(List<Motion> list) {
		this.motionList.addAll(list);
	}

	/**
	 * @return the skeleton
	 */
	public Skeleton getSkeleton() {
		return this.skeleton;
	}
	/**
	 * @return the motionList
	 */
	public List<Motion> getMotionList() {
		return this.motionList;
	}
	/**
	 * @param value the rotateH to set
	 */
	public void setRotateH(double value) {
		this.skeleton.setRotateH(value);
	}
	/**
	 * @param value the rotateV to set
	 */
	public void setRotateV(double value) {
		this.skeleton.setRotateV(value);
	}
	/**
	 * @param current the current to set
	 */
	public void setCurrent(int current) {
		if (!this.motionList.isEmpty()) {
			this.current = current % this.motionList.size();
		}
	}

	/**
	 * インスタンス生成.
	 * @param skeleton Skeleton
	 */
	public Anima(final Skeleton skeleton) {
		this.skeleton = skeleton;
	}
}
