package to.kit.mocap.struct;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

import org.apache.commons.lang3.math.NumberUtils;

public class SkeletonLoader {
	private String section;
	private String segment;
	private SkeletonRoot root;
	private SkeletonBone bone;

	private void processRoot(Skeleton skeleton, String[] param) {
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
			this.bone.setDirection(param[0]);
		} else if ("length".equals(this.segment)) {
			this.bone.setLength(param[0]);
		} else if ("axis".equals(this.segment)) {
			this.bone.setAxis(param[0]);
		} else if ("dof".equals(this.segment)) {
			this.bone.setDof(param[0]);
		} else if ("limits".equals(this.segment)) {
			this.bone.setLimits(param[0]);
		}
	}

	private void processHierarchy(Skeleton skeleton, String[] param) {
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
					processHierarchy(skeleton, param);
				}
//				System.out.println(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return skeleton;
	}
}
