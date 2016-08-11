package to.kit.mocap;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import to.kit.mocap.component.MocapCanvas;
import to.kit.mocap.struct.Motion;
import to.kit.mocap.struct.MotionLoader;
import to.kit.mocap.struct.Skeleton;
import to.kit.mocap.struct.SkeletonLoader;

/**
 * Motion Capture Data Player.
 * @author Hidetaka Sasai
 */
public class MocapPlayerMain extends JFrame {
	final MocapCanvas canvas = new MocapCanvas();
	final JSlider rotationSlider = new JSlider();
	final JSlider slider = new JSlider();
	private JFileChooser chooser = new JFileChooser();

	private void loadSkeleton(File file) {
		SkeletonLoader loader = new SkeletonLoader();
		Skeleton skeleton = loader.load(file);

		if (skeleton != null) {
			this.canvas.add(skeleton);
		}
	}

	private void loadMotion(File file) {
		MotionLoader loader = new MotionLoader();
		List<Motion> motionList = loader.load(file);

		this.canvas.add(motionList);
		this.slider.setMinimum(0);
		this.slider.setMaximum(motionList.size() - 1);
		this.slider.setValue(0);
		this.slider.setEnabled(true);
	}

	protected void openFile() {
		int res = this.chooser.showOpenDialog(this);

		if (res != JFileChooser.APPROVE_OPTION) {
			return;
		}
		File file = this.chooser.getSelectedFile();
		String name = file.getName().toLowerCase();

		if (name.endsWith(".asf")) {
			loadSkeleton(file);
		} else if (name.endsWith(".amc")) {
			loadMotion(file);
		}
	}

	public MocapPlayerMain() {
		setTitle("MocapPlayer");
		setBounds(0, 0, 1024, 768);
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
		this.slider.setMinorTickSpacing(10);
		this.slider.setMajorTickSpacing(100);
		this.slider.setPaintTicks(true);
		this.slider.setEnabled(false);
		this.slider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				MocapCanvas cv = MocapPlayerMain.this.canvas;

				cv.setCurrent(MocapPlayerMain.this.slider.getValue());
				cv.repaint();
			}
		});
		JPanel panel = new JPanel();
		getContentPane().add(panel, BorderLayout.PAGE_END);
		panel.setLayout(new BorderLayout(0, 0));
		panel.add(this.slider);
		this.rotationSlider.setValue(0);
		this.rotationSlider.setMaximum(360);
		this.rotationSlider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				MocapCanvas cv = MocapPlayerMain.this.canvas;
				double rotateY = MocapPlayerMain.this.rotationSlider.getValue() * Math.PI / 180;

				cv.setRotateY(rotateY);
				cv.repaint();
			}
		});
		panel.add(this.rotationSlider, BorderLayout.NORTH);
	}

	public static void main(String[] args) {
		MocapPlayerMain frame = new MocapPlayerMain();

		frame.setVisible(true);
	}
}
