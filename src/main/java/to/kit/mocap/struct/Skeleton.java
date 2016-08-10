package to.kit.mocap.struct;

import java.awt.Graphics2D;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Skeleton {
	/** Logger. */
	private static final Logger LOG = LoggerFactory.getLogger(Skeleton.class);

	private SkeletonRoot root;
	private Map<String, SkeletonNode> nodeMap = new HashMap<>();

	public void add(SkeletonRoot rootNode) {
		this.root = rootNode;
		this.nodeMap.put(rootNode.getName(), rootNode);
	}

	public void add(SkeletonNode node) {
		this.nodeMap.put(node.getName(), node);
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
			SkeletonBone node = (SkeletonBone)this.nodeMap.get(name);
			Double tx = bone.getThetaX();
			Double ty = bone.getThetaY();
			Double tz = bone.getThetaZ();

			node.settX(tx.doubleValue());
			if (ty != null) {
				node.settY(ty.doubleValue());
			}
			if (tz != null) {
				node.settZ(tz.doubleValue());
			}
		}
	}

	public void draw(Graphics2D g) {
		if (this.root == null) {
			return;
		}
		this.root.draw(g, null);
	}
}
