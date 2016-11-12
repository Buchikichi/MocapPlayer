package to.kit.mocap;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.arnx.jsonic.JSON;
import to.kit.mocap.io.Loader;
import to.kit.mocap.io.LoaderFactory;
import to.kit.mocap.io.MotionLoader;
import to.kit.mocap.struct.Anima;
import to.kit.mocap.struct.Motion;
import to.kit.mocap.struct.Skeleton;

/**
 * Mocap Converter.
 * @author Hidetaka Sasai
 */
public final class MocapConverterMain {
	private LoaderFactory factory = new LoaderFactory();
	private Anima anima = null;

	private void save(File file) throws IOException {
		String json = JSON.encode(this.anima);

		file.getParentFile().mkdirs();
		try (FileWriter out = new FileWriter(file)) {
			out.write(json);
		}
		this.anima.getMotionList().clear();
	}

	private boolean load(File file){
		Loader loader = this.factory.create(file.getName());

		if (loader == null) {
			return false;
		}
		if (loader instanceof MotionLoader) {
			if (this.anima != null) {
				((MotionLoader) loader).setSkeleton(this.anima.getSkeleton());
			}
		}
System.out.println(file.getAbsolutePath());
		loader.load(file);
		Skeleton skeleton = loader.getSkeleton();

		if (skeleton != null) {
			this.anima = new Anima(skeleton);
		}
		if (this.anima != null) {
			List<Motion> motionList = loader.getMotionList();

			if (motionList != null) {
				this.anima.addMotion(motionList);
			}
		}
		return true;
	}

	private void scan(File dir, List<File> list) {
		for (File file : dir.listFiles()) {
			if (file.isDirectory()) {
				scan(file, list);
			}
			if (file.isFile()) {
				list.add(file);
			}
		}
	}

	private void execute(File inDir, File outDir) throws IOException {
		String originPath = inDir.getAbsolutePath();
		int len = originPath.length();
		List<File> list = new ArrayList<>();

		scan(inDir, list);
		for (File file : list) {
			if (!load(file)) {
				continue;
			}
			boolean ready = this.anima != null && this.anima.getSkeleton() != null && !this.anima.getMotionList().isEmpty();

			if (ready) {
				String relativePath = file.getAbsolutePath().substring(len);
				File out = new File(outDir, relativePath + ".json");

				save(out);
			}
		}
	}

	/**
	 * Converter Main.
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		if (args.length < 2) {
			return;
		}
		MocapConverterMain app = new MocapConverterMain();
		File inDir = new File(args[0]);
		File outDir = new File(args[1]);

		if (!inDir.isDirectory()) {
			return;
		}
		if (!outDir.isDirectory()) {
			return;
		}
		app.execute(inDir, outDir);
	}
}
