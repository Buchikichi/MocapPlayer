package to.kit.mocap;

import java.awt.Canvas;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import to.kit.mocap.struct.Skeleton;
import to.kit.mocap.struct.SkeletonLoader;

/**
 * Motion Capture Data Player.
 * @author Hidetaka Sasai
 */
public class MocapPlayerMain extends JFrame {
	private JFileChooser chooser = new JFileChooser();
	private List<Skeleton> skeletonList = new ArrayList<>();

	protected void openFile() {
		int res = this.chooser.showOpenDialog(this);

		if (res != JFileChooser.APPROVE_OPTION) {
			return;
		}
		File file = this.chooser.getSelectedFile();
		SkeletonLoader loader = new SkeletonLoader();
		Skeleton skeleton = loader.load(file);

		if (skeleton != null) {
			this.skeletonList.add(skeleton);
		}
	}

	public MocapPlayerMain() {
		setTitle("MocapPlayer");
		setBounds(0, 0, 640, 480);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(null);
		
		JMenuBar menuBar = new JMenuBar();
		menuBar.setBounds(0, 0, 624, 21);
		getContentPane().add(menuBar);
		
		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		JMenuItem mntmOpen = new JMenuItem("Open File...");
		mntmOpen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				openFile();
			}
		});
		mnFile.add(mntmOpen);
		mnFile.addSeparator();
		
		JMenuItem mntmExit = new JMenuItem("Exit");
		mntmExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		mnFile.add(mntmExit);
		
		Canvas canvas = new Canvas();
		canvas.setBounds(0, 20, 624, 421);
		getContentPane().add(canvas);
	}

	public static void main(String[] args) {
		MocapPlayerMain frame = new MocapPlayerMain();

		frame.setVisible(true);
	}
}
