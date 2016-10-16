package to.kit.mocap.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import to.kit.mocap.struct.Motion;
import to.kit.mocap.struct.MotionBone;
import to.kit.mocap.struct.MotionRoot;
import to.kit.mocap.struct.P3D;
import to.kit.mocap.struct.Rotation;
import to.kit.mocap.struct.Skeleton;
import to.kit.mocap.struct.SkeletonBone;

/**
 * Motion loader.
 * @author Hidetaka Sasai
 */
public final class MotionLoader implements Loader {
	/** Logger. */
	private static final Logger LOG = LoggerFactory.getLogger(MotionLoader.class);
	private Skeleton skeleton;
	private List<Motion> motionList = new ArrayList<>();

	private void loadDegrees(Rotation theta, String[] param, String[] dof) {
		Double[] values = new Double[3];

		for (int ix = 0; ix < 3 && ix < param.length; ix++) {
			double deg = NumberUtils.toDouble(param[ix]);
			BigDecimal rad = BigDecimal.valueOf(deg * Math.PI / 180).setScale(3, BigDecimal.ROUND_HALF_UP);

			values[ix] = Double.valueOf(rad.doubleValue());
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
	 */
	@Override
	public void load(final File file) {
		Motion motion = null;
		P3D prev = null;

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
					this.motionList.add(motion);
					continue;
				}
				if (motion == null) {
					continue;
				}
				if (!"root".equals(id)) {
					MotionBone motionBone = new MotionBone(id);
					Rotation theta = motionBone.getTheta();
					SkeletonBone bone = (SkeletonBone) this.skeleton.getNode(id);

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

				if (prev == null) {
					pt = P3D.ORIGIN;
				} else {
					pt = new P3D(x - prev.x, y - prev.y, z - prev.z);
				}
				prev = new P3D(x, y, z);
				loadDegrees(theta, degrees, null);
				root.setPoint(pt);
				motion.add(root);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean isAcceptable(String filename) {
		return filename.endsWith(".amc");
	}

	@Override
	public Skeleton getSkeleton() {
		return null;
	}

	/**
	 * @param skeleton the skeleton to set
	 */
	public void setSkeleton(Skeleton skeleton) {
		this.skeleton = skeleton;
	}

	@Override
	public List<Motion> getMotionList() {
		return this.motionList;
	}
}
