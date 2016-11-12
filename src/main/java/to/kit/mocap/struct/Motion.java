package to.kit.mocap.struct;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.arnx.jsonic.JSONHint;

/**
 * Motion set.
 * @author Hidetaka Sasai
 */
public final class Motion implements Iterable<MotionBone> {
	private final List<MotionBone> list = new ArrayList<>();
	private final Map<String, MotionBone> map = new HashMap<>();
	private boolean isReduction;

	/**
	 * Add a MotionBone.
	 * @param bone MotionBone
	 */
	public void add(MotionBone bone) {
		this.list.add(bone);
		if (bone != null) {
			this.map.put(bone.getName(), bone);
		}
	}

	public void clear() {
		this.list.clear();
	}

	@Override
	public Iterator<MotionBone> iterator() {
		return this.list.iterator();
	}

	/**
	 * @return
	 */
	@JSONHint(ignore=true)
	public Map<String, MotionBone> getMap() {
		return this.map;
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
