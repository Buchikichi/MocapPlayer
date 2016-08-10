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

public class MotionLoader {
	/** Logger. */
	private static final Logger LOG = LoggerFactory.getLogger(MotionLoader.class);

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
				} else {
					MotionBone bone = new MotionBone(id);
					double tx = NumberUtils.toDouble(param[0]);

					bone.setThetaX(tx * Math.PI / 180);
					if (1 < param.length) {
						double ty = NumberUtils.toDouble(param[1]);

						bone.setThetaY(ty * Math.PI / 180);
					}
					if (2 < param.length) {
						double tz = NumberUtils.toDouble(param[2]);

						bone.setThetaZ(tz * Math.PI / 180);
					}
					motion.add(bone);
				}
//				System.out.println(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return list;
	}
}
