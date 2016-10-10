package to.kit.mocap.io;

/**
 * Loader factory.
 * @author Hidetaka Sasai
 */
public final class LoaderFactory {
	private Loader[] loaderList = { new SkeletonLoader(), new MotionLoader(), new BvhLoader() };

	/**
	 * Create a instance of the loader.
	 * @param filename
	 * @return loader
	 */
	public Loader create(String filename) {
		Loader result = null;

		for (Loader loader : this.loaderList) {
			if (loader.isAcceptable(filename)) {
				result = loader;
				break;
			}
		}
		return result;
	}
}
