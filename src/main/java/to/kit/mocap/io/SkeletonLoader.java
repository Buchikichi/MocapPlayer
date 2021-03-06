package to.kit.mocap.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.math.NumberUtils;

import to.kit.mocap.struct.Limit;
import to.kit.mocap.struct.Motion;
import to.kit.mocap.struct.Rotation;
import to.kit.mocap.struct.Skeleton;
import to.kit.mocap.struct.SkeletonBone;
import to.kit.mocap.struct.SkeletonNode;
import to.kit.mocap.struct.SkeletonRoot;

/**
 * SkeletonLoader.
 * @author Hidetaka Sasai
 */
public final class SkeletonLoader implements Loader {
	private Skeleton skeleton;
	private String section;
	private String segment;
	private SkeletonBone bone;
	private int cnt;

	private void adjustDirection(SkeletonNode node) {
		if (node instanceof SkeletonBone) {
			SkeletonBone aBone = (SkeletonBone) node;

			aBone.adjust();
		}
		for (SkeletonNode child : node.getJoint()) {
			adjustDirection(child);
		}
	}

	private void processPosition(String[] param) {
		int ix = 0;
		double[] position = new double[param.length];

		for (String val : param) {
			position[ix++] = NumberUtils.toDouble(val);
		}
		this.skeleton.getRoot().setPosition(position);
	}

	private void processRoot(String[] param) {
		SkeletonRoot root;

		if (this.skeleton.getRoot() == null) {
			root = new SkeletonRoot("root");
			this.skeleton.add(root);
		} else {
			root = this.skeleton.getRoot();
		}
		if ("axis".equals(this.segment)) {
			root.setAxisOrder(param[0]);
		} else if ("order".equals(this.segment)) {
			root.setOrder(param[0]);
			root.setOrder("zyx"); // TODO 正しく設定
		} else if ("position".equals(this.segment)) {
			processPosition(param);
		} else if ("orientation".equals(this.segment)) {
			root.setOrientation(param[0]);
		}
	}

	private void processDirection(String[] param) {
		double[] dir = this.bone.getDir();

		for (int ix = 0; ix < dir.length && ix < param.length; ix++) {
			String val = param[ix];
			double deg = NumberUtils.toDouble(val);

			dir[ix] = deg;
		}
	}

	private void processBoneAxis(String[] param) {
		Double[] values = new Double[3];
		List<String> order = new ArrayList<>();

		for (int ix = 0; ix < param.length; ix++) {
			if (ix < 3) {
				double deg = NumberUtils.toDouble(param[ix]);
				Double rad = Double.valueOf(deg * Math.PI / 180);

				values[ix] = rad;
				continue;
			}
			for (char c : param[ix].toCharArray()) {
				order.add(String.valueOf(c).toUpperCase());
			}
		}
		//
		Rotation axis = this.bone.getAxis();

		for (int ix = 0; ix < values.length && ix < order.size(); ix++) {
			String c = order.get(ix);
			Double val = values[ix];

			if ("X".equals(c)) {
				axis.x = val;
			} else if ("Y".equals(c)) {
				axis.y = val;
			} else if ("Z".equals(c)) {
				axis.z = val;
			}
		}
		//this.bone.setOrder(StringUtils.join(order, StringUtils.EMPTY).toLowerCase());
		this.bone.setOrder("zyx"); // TODO 正しく設定
	}

	private void processLimit(String[] param) {
		Limit limit = new Limit();
		double min = NumberUtils.toDouble(param[0]);
		double max = NumberUtils.toDouble(param[1]);
		String pos = this.bone.getDof()[this.cnt];

		limit.setMin(min * Math.PI / 180);
		limit.setMax(max * Math.PI / 180);
		if ("rx".equals(pos)) {
			this.bone.setLimitX(limit);
		} else if ("ry".equals(pos)) {
			this.bone.setLimitY(limit);
		} else if ("rz".equals(pos)) {
			this.bone.setLimitZ(limit);
		}
	}

	private void processBonedata(String[] param) {
		if ("begin".equals(this.segment)) {
			this.bone = new SkeletonBone();
		} else if ("end".equals(this.segment)) {
			this.skeleton.add(this.bone);
			this.bone = null;
		}
		if (this.bone == null) {
			return;
		}
		if ("id".equals(this.segment)) {
			int id = NumberUtils.toInt(param[0]);
			this.bone.setId(id);
		} else if ("name".equals(this.segment)) {
			this.bone.setName(param[0]);
		} else if ("direction".equals(this.segment)) {
			processDirection(param);
		} else if ("length".equals(this.segment)) {
			this.bone.setLength(NumberUtils.toDouble(param[0]));
		} else if ("axis".equals(this.segment)) {
			processBoneAxis(param);
		} else if ("dof".equals(this.segment)) {
			this.bone.setDof(param);
		} else if ("limits".equals(this.segment)) {
			processLimit(param);
		}
	}

	/**
	 * Load ASF file.
	 * @param file ASF file
	 */
	@Override
	public void load(File file) {
		this.skeleton = new Skeleton();
		this.skeleton.setScale(10);
		try (BufferedReader in = new BufferedReader(new FileReader(file))) {
			for (;;) {
				String line = in.readLine();

				if (line == null) {
					break;
				}
				line = line.trim();
				if (line.startsWith("#") || line.isEmpty()) {
					continue;
				}
				if (line.startsWith(":")) {
					this.section = line.substring(1);
					continue;
				}
				String[] elements = line.split("[\\s()]+");
				String seg = elements[0];
				String[] param = Arrays.copyOfRange(elements, 1, elements.length);

				if (seg.matches("[a-z]+")) {
					this.segment = seg;
					this.cnt = 0;
				}
				if ("root".equals(this.section)) {
					processRoot(param);
				} else if ("bonedata".equals(this.section)) {
					processBonedata(param);
				} else if ("hierarchy".equals(this.section)) {
					if (!"begin".equals(seg) && !"end".equals(seg)) {
						this.skeleton.addHierarchy(seg, param);
					}
				}
				this.cnt++;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		adjustDirection(this.skeleton.getRoot());
	}

	@Override
	public boolean isAcceptable(String filename) {
		return filename.endsWith(".asf");
	}

	@Override
	public Skeleton getSkeleton() {
		return this.skeleton;
	}

	@Override
	public List<Motion> getMotionList() {
		return null;
	}
}
