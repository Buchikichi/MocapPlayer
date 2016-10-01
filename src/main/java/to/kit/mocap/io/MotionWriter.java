package to.kit.mocap.io;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import net.arnx.jsonic.JSON;
import to.kit.mocap.struct.Motion;

/**
 * モーション書き出し.
 * @author Hidetaka Sasai
 */
public final class MotionWriter extends FileWriter {
	/**
	 * インスタンス生成.
	 * @param file ファイル
	 * @throws IOException 入出力例外
	 */
	public MotionWriter(File file) throws IOException {
		super(file);
	}

	/**
	 * モーションをJSON形式で書き出し.
	 * @param motionList モーション
	 * @throws IOException 入出力例外
	 */
	public void write(List<Motion> motionList) throws IOException {
		String json = JSON.encode(motionList);

		write(json);
	}
}
