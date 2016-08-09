package to.kit.mocap.struct;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SkeletonLoader {
	/** Logger. */
	private static final Logger LOG = LoggerFactory.getLogger(SkeletonLoader.class);

	private String section;
	private String segment;
	private SkeletonRoot root;
	private SkeletonBone bone;

	private void processPosition(SkeletonRoot root, String[] param) {
		int ix = 0;
		double[] position = new double[param.length];

		for (String val : param) {
			position[ix++] = NumberUtils.toDouble(val);
		}
		root.setPosition(position);
	}

	private void processRoot(Skeleton skeleton, String[] param) {
		if (this.root == null) {
			this.root = new SkeletonRoot("root");
			skeleton.add(this.root);
		}
		if ("axis".equals(this.segment)) {
			this.root.setAxis(param[0]);
		} else if ("order".equals(this.segment)) {
			this.root.setOrder(param[0]);
		} else if ("position".equals(this.segment)) {
			processPosition(this.root, param);
		} else if ("orientation".equals(this.segment)) {
			this.root.setOrientation(param[0]);
		}
	}

	private void processDirection(SkeletonBone bone, String[] param) {
		int ix = 0;
		double[] direction = new double[param.length];

		for (String val : param) {
			double deg = NumberUtils.toDouble(val);

			direction[ix++] = deg * Math.PI / 180;
//			direction[ix++] = deg;
		}
		bone.setDirection(direction);
	}

	private void processBoneAxis(SkeletonBone bone, String[] param) {
		double[] values = new double[3];
		List<String> order = new ArrayList<>();

		for (int ix = 0; ix < param.length; ix++) {
			if (ix < 3) {
				values[ix] = NumberUtils.toDouble(param[ix]);
				continue;
			}
			for (char c : param[ix].toCharArray()) {
				order.add(String.valueOf(c).toUpperCase());
			}
		}
		int ix = 0;
		for (String c : order) {
			double value = values[ix];
			if ("X".equals(c)) {
				bone.setAxisX(value);
			} else if ("Y".equals(c)) {
				bone.setAxisY(value);
			} else if ("Z".equals(c)) {
				bone.setAxisZ(value);
			}
			ix++;
		}
	}

	private void processBonedata(Skeleton skeleton, String[] param) {
		if ("begin".equals(this.segment)) {
			this.bone = new SkeletonBone();
		} else if ("end".equals(this.segment)) {
			skeleton.add(this.bone);
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
			processDirection(this.bone, param);
		} else if ("length".equals(this.segment)) {
			double length = NumberUtils.toDouble(param[0]);
			this.bone.setLength(length);
		} else if ("axis".equals(this.segment)) {
			processBoneAxis(this.bone, param);
		} else if ("dof".equals(this.segment)) {
			this.bone.setDof(param);
		} else if ("limits".equals(this.segment)) {
			this.bone.setLimits(param[0]);
		}
	}

	public Skeleton load(File file) {
		Skeleton skeleton = new Skeleton();

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
				String[] elements = line.split("[\\s()]");
				String seg = elements[0];
				String[] param = Arrays.copyOfRange(elements, 1, elements.length);

				if (seg.matches("[a-z]+")) {
					this.segment = seg;
				}
				if ("root".equals(this.section)) {
					processRoot(skeleton, param);
				} else if ("bonedata".equals(this.section)) {
					processBonedata(skeleton, param);
				} else if ("hierarchy".equals(this.section)) {
					if (!"begin".equals(seg) && !"end".equals(seg)) {
						skeleton.addHierarchy(seg, param);
					}
				}
//				System.out.println(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return skeleton;
	}
}
