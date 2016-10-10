package to.kit.mocap.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;

import org.apache.commons.lang3.math.NumberUtils;

import to.kit.mocap.struct.Motion;
import to.kit.mocap.struct.MotionBone;
import to.kit.mocap.struct.MotionRoot;
import to.kit.mocap.struct.P3D;
import to.kit.mocap.struct.Rotation;
import to.kit.mocap.struct.Skeleton;
import to.kit.mocap.struct.SkeletonBone;
import to.kit.mocap.struct.SkeletonNode;
import to.kit.mocap.struct.SkeletonRoot;

/**
 * BVH loader.
 * @author Hidetaka Sasai
 */
public final class BvhLoader implements Loader {
	private Skeleton skeleton = new Skeleton();
	private SkeletonNode currentNode;
	private int channels;
	private List<String> nameList = new ArrayList<>();
	private Deque<String> queue = new ArrayDeque<>();
	private P3D prev;

	private List<Motion> motionList = new ArrayList<>();

	private void precessRotation(MotionBone motionBone, String[] params) {
		double ry = NumberUtils.toDouble(params[0]);
		double rx = NumberUtils.toDouble(params[1]);
		double rz = NumberUtils.toDouble(params[2]);
		Rotation rotation = motionBone.getTheta();

		rotation.x = Double.valueOf(rx * Math.PI / 180);
		rotation.y = Double.valueOf(ry * Math.PI / 180);
		rotation.z = Double.valueOf(rz * Math.PI / 180);
	}

	private void processMotion(String mnemonic, String[] elements) {
		if ("Frames:".equals(mnemonic)) {
			System.out.println("Frames:" + elements[1]);
			return;
		}
		if ("Frame".equals(mnemonic)) {
			System.out.println("Frame Time:" + elements[2]);
			return;
		}
		Motion motion = new Motion();
		int ix = 0;

		for (String name : this.nameList) {
			if (ix == 0) {
				double x = NumberUtils.toDouble(elements[ix]) * .3;
				double y = NumberUtils.toDouble(elements[ix + 1]) * .3;
				double z = NumberUtils.toDouble(elements[ix + 2]) * .3;
				String[] param = Arrays.copyOfRange(elements, ix + 3, ix + 6);
				MotionRoot motionRoot = new MotionRoot(name);
				P3D pt;

				if (this.prev == null) {
					pt = P3D.ORIGIN;
				} else {
					pt = new P3D(x - this.prev.x, y - this.prev.y, z - this.prev.z);
				}
				this.prev = new P3D(x, y, z);
				motionRoot.setPoint(pt);
				precessRotation(motionRoot, param);
				motion.add(motionRoot);
				ix += 3;
			} else {
				String[] param = Arrays.copyOfRange(elements, ix, ix + 3);
				MotionBone motionBone = new MotionBone(name);

				precessRotation(motionBone, param);
				motion.add(motionBone);
			}
			ix += 3;
		}
		this.motionList.add(motion);
	}

	private void adjustDirection(SkeletonNode node) {
		if (node instanceof SkeletonBone) {
			SkeletonBone aBone = (SkeletonBone) node;

			aBone.adjust();
		}
		for (SkeletonNode child : node.getJoint()) {
			adjustDirection(child);
		}
	}

	private void processHierarchy(String mnemonic, String[] param) {
		if ("ROOT".equals(mnemonic)) {
			String name = param[0];

			this.currentNode = new SkeletonRoot(name);
			this.skeleton.add(this.currentNode);
			this.nameList.add(name);
		} else if ("JOINT".equals(mnemonic)) {
			String parent = this.queue.peekLast();
			String name = param[0];

			this.currentNode = new SkeletonBone();
			this.currentNode.setName(name);
			this.skeleton.add(this.currentNode);
			this.skeleton.addHierarchy(parent, name);
			this.nameList.add(name);
		} else if ("End".equals(mnemonic)) {
			String parent = this.queue.peekLast();
			String name = parent + "9";

			this.currentNode = new SkeletonBone();
			this.currentNode.setName(name);
//			this.skeleton.add(this.currentNode);
//			this.skeleton.addHierarchy(parent, name);
		} else if ("OFFSET".equals(mnemonic) && this.currentNode != null) {
			double x = NumberUtils.toDouble(param[0]);
			double y = NumberUtils.toDouble(param[1]);
			double z = NumberUtils.toDouble(param[2]);

			if (this.currentNode instanceof SkeletonRoot) {
				this.currentNode.setTranslate(new P3D(x, y, z));
			} else {
				SkeletonBone bone = (SkeletonBone) this.currentNode;
				double[] dir = bone.getDir();

				dir[0] = x;
				dir[1] = y;
				dir[2] = z;
				bone.setLength(.3);
			}
		} else if ("CHANNELS".equals(mnemonic)) {
			this.channels += NumberUtils.toInt(param[0]);
		} else if ("{".equals(mnemonic)) {
			this.queue.add(this.currentNode.getName());
//			System.out.println("add:" + StringUtils.join(this.queue, ","));
		} else if ("}".equals(mnemonic)) {
			this.queue.pollLast();
//			System.out.println("poll:" + StringUtils.join(this.queue, ","));
		}
	}

	@Override
	public void load(File file) {
		Phase phase = null;

		try (BufferedReader in = new BufferedReader(new FileReader(file))) {
			for (;;) {
				String line = in.readLine();

				if (line == null) {
					break;
				}
				String[] elements = line.trim().split("[\\s()]+");
				String mnemonic = elements[0];
				String[] param = Arrays.copyOfRange(elements, 1, elements.length);

				if (Phase.HIERARCHY.toString().equals(mnemonic)) {
					phase = Phase.HIERARCHY;
					continue;
				}
				if (Phase.MOTION.toString().equals(mnemonic)) {
					adjustDirection(this.skeleton.getRoot());
					System.out.println("channels:" + this.channels);
					phase = Phase.MOTION;
					continue;
				}
				if (phase == Phase.HIERARCHY) {
					processHierarchy(mnemonic, param);
				} else if (phase == Phase.MOTION) {
					processMotion(mnemonic, elements);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Skeleton getSkeleton() {
		return this.skeleton;
	}

	@Override
	public boolean isAcceptable(String filename) {
		return filename.endsWith("bvh");
	}

	@Override
	public List<Motion> getMotionList() {
		return this.motionList;
	}

	enum Phase {
		HIERARCHY,
		MOTION,
	}
}
