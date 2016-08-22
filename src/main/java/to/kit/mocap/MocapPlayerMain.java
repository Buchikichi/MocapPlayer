package to.kit.mocap;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

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
import javax.swing.SwingConstants;

/**
 * Motion Capture Data Player.
 * @author Hidetaka Sasai
 */
public class MocapPlayerMain extends JFrame {
	private static final int FRAME_WIDTH = 1600;
	private static final int FRAME_HEIGHT = 1200;
	final MocapCanvas canvas = new MocapCanvas();
	final JSlider sliderH = new JSlider();
	final JSlider sliderV = new JSlider();
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

	protected void changeRotateH() {
		double rad = this.sliderH.getValue() * Math.PI / 180;

		this.canvas.setRotateH(rad);
		this.canvas.repaint();
	}

	protected void changeRotateV() {
		double rad = this.sliderV.getValue() * Math.PI / 180;

		this.canvas.setRotateV(rad);
		this.canvas.repaint();
	}

	/**
	 * Create an instance.
	 */
	public MocapPlayerMain() {
		setTitle("MocapPlayer");
		setBounds(0, 0, FRAME_WIDTH, FRAME_HEIGHT);
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
		this.sliderH.setMinimum(-179);
		this.sliderH.setValue(0);
		this.sliderH.setMaximum(180);
		this.sliderH.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				changeRotateH();
			}
		});
		panel.add(this.sliderH, BorderLayout.NORTH);
		changeRotateH();
		this.sliderV.setMinimum(-179);
		this.sliderV.setValue(-179);
		this.sliderV.setMaximum(180);
		this.sliderV.setOrientation(SwingConstants.VERTICAL);
		this.sliderV.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				changeRotateV();
			}
		});
		getContentPane().add(this.sliderV, BorderLayout.WEST);
		changeRotateV();
		Timer timer = new Timer();

		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				JSlider sld = MocapPlayerMain.this.slider;

				if (!sld.isEnabled()) {
					return;
				}
				int val = sld.getValue();
				int max = sld.getMaximum();

				if (max < ++val) {
					val = sld.getMinimum();
				}
				sld.setValue(val);
			}
		}, 0, 100);
	}

	/**
	 * Main.
	 * @param args Arguments.
	 */
	public static void main(String[] args) {
		MocapPlayerMain frame = new MocapPlayerMain();

		frame.setVisible(true);
	}
}
