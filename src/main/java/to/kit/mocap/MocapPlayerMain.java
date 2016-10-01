package to.kit.mocap;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
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
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import to.kit.mocap.component.MocapCanvas;
import to.kit.mocap.io.MotionLoader;
import to.kit.mocap.io.MotionWriter;
import to.kit.mocap.io.SkeletonLoader;
import to.kit.mocap.io.SkeletonWriter;
import to.kit.mocap.struct.Motion;
import to.kit.mocap.struct.Skeleton;

/**
 * Motion Capture Data Player.
 * @author Hidetaka Sasai
 */
public class MocapPlayerMain extends JFrame {
	private static final int FRAME_WIDTH = 1600;
	private static final int FRAME_HEIGHT = 1024;
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
			this.canvas.repaint();
		}
	}

	private void loadMotion(File file) {
		Skeleton skeleton = this.canvas.getSkeleton();

		if (skeleton == null) {
			return;
		}
		MotionLoader loader = new MotionLoader();
		List<Motion> motionList = loader.load(file, skeleton);

		this.canvas.set(motionList);
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

	protected void saveFile() {
		Skeleton skeleton = this.canvas.getSkeleton();

		if (skeleton == null) {
			return;
		}
		int res = this.chooser.showSaveDialog(this);

		if (res != JFileChooser.APPROVE_OPTION) {
			return;
		}
		File file = this.chooser.getSelectedFile();

		try (SkeletonWriter out = new SkeletonWriter(file)) {
			out.write(skeleton);
		} catch (IOException e) {
			e.printStackTrace();
		}
		//
		File motionFile = new File(file.getParentFile(), "motion.json");

		try (MotionWriter out = new MotionWriter(motionFile)) {
			out.write(this.canvas.getMotionList());
		} catch (IOException e) {
			e.printStackTrace();
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
		JMenuItem mntmSave = new JMenuItem("Save");
		mntmSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				saveFile();
			}
		});
		mntmSave.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK));
		mnFile.add(mntmSave);
		mnFile.addSeparator();

		JMenu mnEdit = new JMenu("Edit");
		menuBar.add(mnEdit);
		JMenuItem mntmRemoveTheFront = new JMenuItem("Remove the front");
		mntmRemoveTheFront.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MocapCanvas cv = MocapPlayerMain.this.canvas;

				cv.removeTheFront();
				MocapPlayerMain.this.slider.setMaximum(cv.getMotionList().size() - 1);
				MocapPlayerMain.this.slider.setValue(0);
			}
		});
		mnEdit.add(mntmRemoveTheFront);
		JMenuItem mntmRmoveTheRear = new JMenuItem("Rmove the rear");
		mntmRmoveTheRear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MocapCanvas cv = MocapPlayerMain.this.canvas;

				cv.removeTheRear();
				int max = cv.getMotionList().size() - 1;
				MocapPlayerMain.this.slider.setMaximum(max);
				MocapPlayerMain.this.slider.setValue(max);
			}
		});
		mnEdit.add(mntmRmoveTheRear);

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
		this.sliderV.setValue(0);
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
//				sld.setValue(val);
			}
		}, 0, 33);
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
