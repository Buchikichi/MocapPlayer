package to.kit.mocap.struct;

import java.awt.Graphics2D;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Skeleton.
 * @author Hidetaka Sasai
 */
public final class Skeleton {
	/** Logger. */
	private static final Logger LOG = LoggerFactory.getLogger(Skeleton.class);

	private SkeletonRoot root;
	private Map<String, SkeletonNode> nodeMap = new HashMap<>();
	double rotateH;
	double rotateV;

	public void add(SkeletonNode node) {
		String name = node.getName();

		node.setSkeleton(this);
		if (node instanceof SkeletonRoot) {
			this.root = (SkeletonRoot) node;
		}
		this.nodeMap.put(name, node);
	}

	public void addHierarchy(String parent, String... children) {
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

	private void setRootPoint(MotionRoot motionRoot, int direction) {
		Rotation theta = motionRoot.getTheta();
		P3D motn = motionRoot.getPoint();
		P3D prev = this.root.getPoint();
		P3D next = new P3D(prev.x + motn.x * direction, prev.y + motn.y * direction, prev.z + motn.z * direction);

		this.root.setTranslate(next);
		this.root.setThetaX(new Radian(theta.x));
		this.root.setThetaY(new Radian(theta.y));
		this.root.setThetaZ(new Radian(theta.z));
	}

	public SkeletonNode getNode(String name) {
		if (!this.nodeMap.containsKey(name)) {
			LOG.error("Bad parent name [{}].", name);
			return null;
		}
		return this.nodeMap.get(name);
	}

	public void shift(Motion motion, int direction) {
		for (MotionBone motionBone : motion) {
			if (motionBone instanceof MotionRoot) {
				setRootPoint((MotionRoot) motionBone, direction);
				if (motion.isReduction()) {
					break;
				}
				continue;
			}
			String name = motionBone.getName();
			SkeletonBone bone = (SkeletonBone) getNode(name);
			Rotation theta = motionBone.getTheta();

			bone.setThetaX(new Radian(theta.x));
			bone.setThetaY(new Radian(theta.y));
			bone.setThetaZ(new Radian(theta.z));
		}
		if (motion.isReduction()) {
			this.root.calculateSimple();
			return;
		}
		this.root.calculate();
	}

	/**
	 * Draw.
	 * @param g Graphics2D
	 */
	public void draw(Graphics2D g) {
		if (this.root == null) {
			return;
		}
		this.root.calculate();
		this.root.draw(g);
	}

	/**
	 * @return the root
	 */
	public SkeletonRoot getRoot() {
		return this.root;
	}
	/**
	 * @param root the root to set
	 */
	public void setRoot(SkeletonRoot root) {
		this.root = root;
	}
	/**
	 * @param rad the rotateH to set
	 */
	public void setRotateH(double rad) {
		this.rotateH = rad;
	}
	/**
	 * @param rad the rotateH to set
	 */
	public void setRotateV(double rad) {
		this.rotateV = rad;
	}
}
