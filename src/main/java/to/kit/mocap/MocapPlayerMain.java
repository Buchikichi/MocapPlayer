package to.kit.mocap;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import to.kit.mocap.component.MocapCanvas;
import to.kit.mocap.struct.Skeleton;
import to.kit.mocap.struct.SkeletonLoader;

/**
 * Motion Capture Data Player.
 * @author Hidetaka Sasai
 */
public class MocapPlayerMain extends JFrame {
	private JFileChooser chooser = new JFileChooser();
	private MocapCanvas canvas = new MocapCanvas();

	private void loadSkeleton(File file) {
		SkeletonLoader loader = new SkeletonLoader();
		Skeleton skeleton = loader.load(file);

		if (skeleton != null) {
			this.canvas.add(skeleton);
		}
	}

	protected void openFile() {
		int res = this.chooser.showOpenDialog(this);

		if (res != JFileChooser.APPROVE_OPTION) {
			return;
		}
		File file = this.chooser.getSelectedFile();

		loadSkeleton(file);
	}

	public MocapPlayerMain() {
		setTitle("MocapPlayer");
		setBounds(0, 0, 640, 480);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(new BorderLayout(0, 0));
		
		JMenuBar menuBar = new JMenuBar();
		getContentPane().add(menuBar, BorderLayout.PAGE_START);
		
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

		getContentPane().add(this.canvas, BorderLayout.CENTER);
	}

	public static void main(String[] args) {
		MocapPlayerMain frame = new MocapPlayerMain();

		frame.setVisible(true);
	}
}
