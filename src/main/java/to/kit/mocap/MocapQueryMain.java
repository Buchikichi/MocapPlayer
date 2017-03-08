package to.kit.mocap;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * Query生成.
 * @author H.Sasai
 */
public class MocapQueryMain {
	private String toValues(List<String> valueList) {
		List<String> elementList = new ArrayList<>();

		for (String value : valueList) {
			elementList.add("'" + value + "'");
		}
		return StringUtils.join(elementList, ",");
	}

	private String makeQuery(File file, String asfid) throws IOException {
		String json = IOUtils.toString(file.toURI(), Charset.defaultCharset());
		List<String> fieldList = Arrays.asList("asfid", "name", "description", "data");
		String fields = StringUtils.join(fieldList, ",");
		List<String> valueList = Arrays.asList(asfid, file.getName(), "", json);
		String values = toValues(valueList);

		return String.format("INSERT INTO amc(%s) VALUES(%s);", fields, values);
	}

	private void execute(File inDir, File outDir) throws IOException {
		List<File> dirList = new ArrayList<>();

		for (File file : inDir.listFiles()) {
			if (file.isDirectory()) {
				dirList.add(file);
			}
		}
		for (File dir : dirList) {
			List<String> queryList = new ArrayList<>();
			String asfid = dir.getName();

			for (File file : dir.listFiles()) {
				if (file.isDirectory()) {
					continue;
				}
				queryList.add(makeQuery(file, asfid));
			}
			File outFile = new File(outDir, asfid + ".sql");
			String queries = StringUtils.join(queryList, "\n");

			try (Writer writer = new FileWriter(outFile)) {
				IOUtils.write(queries, writer);
			}
		}
	}

	public static void main(String[] args) throws Exception {
		if (args.length < 2) {
			return;
		}
		MocapQueryMain app = new MocapQueryMain();
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
