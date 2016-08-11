package to.kit.mocap.struct;

import java.awt.Graphics2D;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import to.kit.mocap.util.MathExt;

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

	private void setRootPoint(MotionRoot motionRoot) {
		P3D pt = motionRoot.getPoint();
		double x = pt.x;
		double y = pt.y;
		double z = pt.x;
		double[] theta = motionRoot.getTheta();
		double rx = theta[0];
		double ry = MathExt.trim(theta[1] + this.rotateY);
		double rz = theta[2];
		P2D yz = new P2D(y, z).rotate(rx);
		y = yz.x;
		z = yz.y;
		P2D zx = new P2D(z, x).rotate(ry);
		z = zx.x;
		x = zx.y;
		P2D xy = new P2D(x, y).rotate(rz);
		x = xy.x;
		y = xy.y;

		this.root.setPoint(new P3D(x / 50, y / 50, z / 50));
	}

	public void shift(Motion motion) {
		for (MotionBone motionBone : motion) {
			String name = motionBone.getName();

			if (!this.nodeMap.containsKey(name)) {
				LOG.error("Bad parent name [{}].", name);
				continue;
			}
			if (motionBone instanceof MotionRoot) {
				setRootPoint((MotionRoot) motionBone);
				continue;
			}
			SkeletonBone bone = (SkeletonBone) this.nodeMap.get(name);
			double[] theta = motionBone.getTheta();
			int ix = 0;
			for (String deg : bone.getDof()) {
				double val = theta[ix++];

				if ("rx".equals(deg)) {
					bone.setThetaX(val);
				} else if ("ry".equals(deg)) {
					bone.setThetaY(val);
				} else if ("rz".equals(deg)) {
					bone.setThetaZ(val);
				} else {
					LOG.error("Unknown [{}].", deg);
				}
			}
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
