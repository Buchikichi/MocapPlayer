package to.kit.mocap.io;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import net.arnx.jsonic.JSON;
import to.kit.mocap.struct.Skeleton;

/**
 * スケルトン書き出し.
 * @author Hidetaka Sasai
 */
public final class SkeletonWriter extends FileWriter {
	/**
	 * インスタンス生成.
	 * @param file ファイル
	 * @throws IOException 入出力例外
	 */
	public SkeletonWriter(File file) throws IOException {
		super(file);
	}

	/**
	 * スケルトンをJSON形式で書き出し.
	 * @param skeleton モーション
	 * @throws IOException 入出力例外
	 */
	public void write(Skeleton skeleton) throws IOException {
		String json = JSON.encode(skeleton);

		write(json);
	}
}
