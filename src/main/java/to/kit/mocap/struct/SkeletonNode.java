package to.kit.mocap.struct;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

public abstract class SkeletonNode {
	private String name;
	private List<SkeletonNode> joint = new ArrayList<>();

	protected List<SkeletonNode> getJoint() {
		return this.joint;
	}

	public abstract void draw(Graphics2D g, P3D pt); 

	public void add(SkeletonNode node) {
		this.joint.add(node);
	}

	/**
	 * Get the name.
	 * @return the name
	 */
	public String getName() {
		return this.name;
	}
	/**
	 * Set the name.
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
}
