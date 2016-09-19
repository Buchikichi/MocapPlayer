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

/**
 * Motion loader.
 * @author Hidetaka Sasai
 */
public final class MotionLoader {
	/** Logger. */
	private static final Logger LOG = LoggerFactory.getLogger(MotionLoader.class);

	private void loadDegrees(Rotation theta, String[] param, String[] dof) {
		Double[] values = new Double[3];

		for (int ix = 0; ix < 3 && ix < param.length; ix++) {
			double deg = NumberUtils.toDouble(param[ix]);
			double rad = deg * Math.PI / 180;

			values[ix] = Double.valueOf(rad);
		}
		if (dof == null) {
			// root
			theta.x = values[0];
			theta.y = values[1];
			theta.z = values[2];
			return;
		}
		int ix = 0;

		for(String deg : dof) {
			Double val = values[ix++];

			if ("rx".equals(deg)) {
				theta.x = val;
			} else if ("ry".equals(deg)) {
				theta.y = val;
			} else if ("rz".equals(deg)) {
				theta.z = val;
			} else {
				LOG.error("Unknown [{}].", deg);
			}
		}
	}

	/**
	 * Load a motion file.
	 * @param file AMC
	 * @param skeleton 
	 * @return motion list
	 */
	public List<Motion> load(final File file, Skeleton skeleton) {
		List<Motion> list = new ArrayList<>();
		Motion motion = null;
		P3D origin = null;

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
				if (!"root".equals(id)) {
					MotionBone motionBone = new MotionBone(id);
					Rotation theta = motionBone.getTheta();
					SkeletonBone bone = (SkeletonBone) skeleton.getNode(id);

					loadDegrees(theta, param, bone.getDof());
					motion.add(motionBone);
					continue;
				}
				MotionRoot root = new MotionRoot(id);
				Rotation theta = root.getTheta();
				double x = NumberUtils.toDouble(param[0]);
				double y = NumberUtils.toDouble(param[1]);
				double z = NumberUtils.toDouble(param[2]);
				String[] degrees = Arrays.copyOfRange(param, 3, param.length);
				P3D pt;

				if (origin == null) {
					origin = new P3D(x, y, z);
					pt = P3D.ORIGIN;
				} else {
					pt = new P3D(x - origin.x, y - origin.y, z - origin.z);
				}
				loadDegrees(theta, degrees, null);
				root.setPoint(pt);
				motion.add(root);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return list;
	}
}
