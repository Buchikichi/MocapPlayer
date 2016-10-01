package to.kit.mocap.struct;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.arnx.jsonic.JSONHint;

/**
 * Motion set.
 * @author Hidetaka Sasai
 */
public final class Motion implements Iterable<MotionBone> {
	private List<MotionBone> list = new ArrayList<>();
	private boolean isReduction;

	/**
	 * Add a MotionBone.
	 * @param bone MotionBone
	 */
	public void add(MotionBone bone) {
		this.list.add(bone);
	}

	@Override
	public Iterator<MotionBone> iterator() {
		return this.list.iterator();
	}

	/**
	 * @return the isReduction
	 */
	@JSONHint(ignore=true)
	public boolean isReduction() {
		return this.isReduction;
	}

	/**
	 * @param isReduction the isReduction to set
	 */
	public void setReduction(boolean isReduction) {
		this.isReduction = isReduction;
	}
}
