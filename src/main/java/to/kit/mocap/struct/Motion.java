package to.kit.mocap.struct;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Motion implements Iterable<MotionBone> {
	private List<MotionBone> list = new ArrayList<>();

	public void add(MotionBone bone) {
		this.list.add(bone);
	}

	@Override
	public Iterator<MotionBone> iterator() {
		return this.list.iterator();
	}
}
