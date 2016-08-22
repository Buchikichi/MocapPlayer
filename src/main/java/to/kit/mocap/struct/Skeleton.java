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

	private P3D pos = new P3D(0, 0, 0);
	private SkeletonRoot root;
	private Map<String, SkeletonNode> nodeMap = new HashMap<>();
	double rotateH;
	double rotateV;

	/**
	 * @deprecated 削除予定
	 */
	@Deprecated
	public void expedient() {
		String[] nameList = {"lhumerus", "rhumerus"};
		for (String name : nameList) {
			SkeletonNode node = this.nodeMap.get(name);
//			double axisX = node.getAxisX();
//
//			axisX += Math.PI;
//			node.setAxisX(axisX);
		}
	}

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
		Double[] theta = motionRoot.getTheta();

		this.root.setPoint(pt);
		this.root.setThetaX(new Radian(theta[0]));
		this.root.setThetaY(new Radian(theta[1]));
		this.root.setThetaZ(new Radian(theta[2]));
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
			Double[] theta = motionBone.getTheta();
			int ix = 0;

			for (String deg : bone.getDof()) {
				Radian val = new Radian(theta[ix++]);

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
		String[] nameList = { /*"lhumerus", "rhumerus",*/"lradius" };
		for (String name : nameList) {
			SkeletonNode node = this.nodeMap.get(name);
			Radian thetaX = node.getThetaX();
			Radian thetaY = node.getThetaY();
			Radian thetaZ = node.getThetaZ();

//			thetaX *= -1;
//			thetaY *= -1;
//			thetaZ *= -1;
//			thetaX = new Radian(thetaX.getRadian() + 160 * Math.PI / 180);
//			thetaY = new Radian(32 * Math.PI / 180);
//			thetaZ = new Radian(-161 * Math.PI / 180);
			node.setThetaX(thetaX);
			node.setThetaY(thetaY);
			node.setThetaZ(thetaZ);
		}
	}

	public void draw(Graphics2D g) {
		if (this.root == null) {
			return;
		}
		this.root.draw(g);
	}
	/**
	 * @return the pos
	 */
	public P3D getPos() {
		return this.pos;
	}
	/**
	 * @param pos the pos to set
	 */
	public void setPos(P3D pos) {
		this.pos = pos;
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
