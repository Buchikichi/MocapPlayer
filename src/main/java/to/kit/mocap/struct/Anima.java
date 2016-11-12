package to.kit.mocap.struct;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import net.arnx.jsonic.JSONHint;

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
		Motion motion = null;

		if (0 < sign) {
			for (; cur != this.current;) {
				cur += sign;
				motion = this.motionList.get(cur);
				motion.setReduction(true);
				list.add(motion);
			}
		} else {
			for (; cur != this.current;) {
				motion = this.motionList.get(cur);
				motion.setReduction(true);
				list.add(motion);
				cur += sign;
			}
		}
		if (motion != null) {
			motion.setReduction(false);
		}
		this.previous = this.current;
		return list;
	}

	/**
	 * Get motion for JSON.
	 * @return motion list
	 */
	public List<List<Double[]>> getMotion() {
		List<List<Double[]>> list = new ArrayList<>();

		for (Motion motion : this.motionList) {
			List<Double[]> boneList = new ArrayList<>();

			for (MotionBone bone : motion) {
				if (bone == null) {
					boneList.add(null);
					continue;
				}
				Rotation rotation = bone.getTheta();

				if (bone instanceof MotionRoot) {
					MotionRoot root = (MotionRoot) bone;
					P3D pt = root.getPoint();

					boneList.add(new Double[] {
							rotation.x, rotation.y, rotation.z,
							Double.valueOf(pt.x), Double.valueOf(pt.y), Double.valueOf(pt.z), });
				} else {
					boneList.add(new Double[] { rotation.x, rotation.y, rotation.z });
				}
			}
			list.add(boneList);
		}
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
	@JSONHint(ignore = true)
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
