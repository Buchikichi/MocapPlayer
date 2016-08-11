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

public final class MotionLoader {
	/** Logger. */
	private static final Logger LOG = LoggerFactory.getLogger(MotionLoader.class);

	private void loadDegrees(MotionBone bone, String[] param) {
		double[] theta = bone.getTheta();

		for (int ix = 0; ix < theta.length && ix < param.length; ix++) {
			double deg = NumberUtils.toDouble(param[ix]);

			theta[ix] = deg * Math.PI / 180;
		}
	}

	public List<Motion> load(File file) {
		List<Motion> list = new ArrayList<>();
		Motion motion = null;

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
					//section = line.substring(1);
					continue;
				}
				String[] elements = line.split("[\\s()]");
				String id = elements[0];
				String[] param = Arrays.copyOfRange(elements, 1, elements.length);

				if (id.matches("[0-9]+")) {
					motion = new Motion();
					list.add(motion);
					continue;
				}
				if (motion == null) {
					continue;
				}
				if ("root".equals(id)) {
					MotionRoot root = new MotionRoot(id);
					double x = NumberUtils.toDouble(param[3]) / 10;
					double y = NumberUtils.toDouble(param[4]) / 10;
					double z = NumberUtils.toDouble(param[5]) / 10;
					P3D pt = new P3D(x, y, z);

					loadDegrees(root, param);
					root.setPoint(pt);
					motion.add(root);
				} else {
					MotionBone bone = new MotionBone(id);

					loadDegrees(bone, param);
					motion.add(bone);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return list;
	}
}
