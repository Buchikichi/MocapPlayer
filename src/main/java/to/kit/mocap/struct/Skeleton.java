package to.kit.mocap.struct;

import java.awt.Color;
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

	private String name;
	private SkeletonRoot root;
	private Color color = Color.LIGHT_GRAY;
	private CalcOrder calcOrder = CalcOrder.RotateTranslate;
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
		this.root.setThetaMatrix(theta.getMatrix());
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
			SkeletonBone bone = (SkeletonBone) getNode(motionBone.getName());
			Rotation theta = motionBone.getTheta();

			bone.setThetaMatrix(theta.getMatrix());
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
		if (this.name != null) {
			double scale = 10;
			P3D pt = this.root.getPoint();
			P3D nextPt = pt.rotate(this.rotateV, this.rotateH, 0);
			int x = (int) (nextPt.x * scale);
			int y = (int) (-nextPt.y * scale);

			g.setColor(Color.GRAY);
			g.drawString(this.name, x, y);
		}
		this.root.draw(g);
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return this.name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
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
	 * @return the color
	 */
	public Color getColor() {
		return this.color;
	}
	/**
	 * @param color the color to set
	 */
	public void setColor(Color color) {
		this.color = color;
	}
	/**
	 * @return the calcOrder
	 */
	public CalcOrder getCalcOrder() {
		return this.calcOrder;
	}
	/**
	 * @param calcOrder the calcOrder to set
	 */
	public void setCalcOrder(CalcOrder calcOrder) {
		this.calcOrder = calcOrder;
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

	public enum CalcOrder {
		RotateTranslate,
		TranslateRotate,
	}
}
