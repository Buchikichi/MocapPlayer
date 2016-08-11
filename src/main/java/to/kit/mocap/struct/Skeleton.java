package to.kit.mocap.struct;

import java.awt.Graphics2D;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class Skeleton {
	/** Logger. */
	private static final Logger LOG = LoggerFactory.getLogger(Skeleton.class);

	private SkeletonRoot root;
	private Map<String, SkeletonNode> nodeMap = new HashMap<>();
	double rotateY;

	public void add(SkeletonNode node) {
		String name = node.getName();

		node.setSkeleton(this);
		if (node instanceof SkeletonRoot) {
			this.root = (SkeletonRoot) node;
		}
		this.nodeMap.put(name, node);
	}

	public void addHierarchy(String parent, String[] children) {
		if (!this.nodeMap.containsKey(parent)) {
			LOG.error("Bad parent name [{}].", parent);
			return;
		}
		SkeletonNode parentNode = this.nodeMap.get(parent);

		for (String child : children) {
			if (!this.nodeMap.containsKey(child)) {
				LOG.error("Bad child name [{}].", child);
				continue;
			}
			SkeletonNode childNode = this.nodeMap.get(child);

			parentNode.add(childNode);
		}
	}

	public void shift(Motion motion) {
		for (MotionBone bone : motion) {
			String name = bone.getName();

			if (!this.nodeMap.containsKey(name)) {
				LOG.error("Bad parent name [{}].", name);
				continue;
			}
			SkeletonBone node = (SkeletonBone) this.nodeMap.get(name);
			Double tx = bone.getThetaX();
			Double ty = bone.getThetaY();
			Double tz = bone.getThetaZ();

			node.setThetaX(tx.doubleValue());
			if (ty != null) {
				node.setThetaY(ty.doubleValue());
			}
			else node.setThetaY(0);
			if (tz != null) {
				node.setThetaZ(tz.doubleValue());
			}
			else node.setThetaZ(0);
		}
	}

	public void draw(Graphics2D g) {
		if (this.root == null) {
			return;
		}
		this.root.draw(g, null);
	}
	/**
	 * @param rotateY the rotateY to set
	 */
	public void setRotateY(double rotateY) {
		this.rotateY = rotateY;
	}
}
