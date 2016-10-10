package to.kit.mocap.io;

import java.io.File;
import java.util.List;

import to.kit.mocap.struct.Motion;
import to.kit.mocap.struct.Skeleton;

/**
 * Loader.
 * @author Hidetaka Sasai
 */
public interface Loader {
	/**
	 * Acceptable.
	 * @param filename File name
	 * @return true, if acceptable.
	 */
	boolean isAcceptable(String filename);

	/**
	 * Load a file.
	 * @param file
	 */
	void load(File file);

	
	/**
	 * Get the skeleton.
	 * @return skeleton
	 */
	Skeleton getSkeleton();

	/**
	 * Get the list of motion.
	 * @return list
	 */
	List<Motion> getMotionList();
}
