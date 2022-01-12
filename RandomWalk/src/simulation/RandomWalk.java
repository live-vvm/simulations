package simulation;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

public class RandomWalk {

	private static int XMAX = 800;
	private static int YMAX = 800;
	private static int UNIT = 10;
	private static int XSTART = XMAX / 2;
	private static int YSTART = YMAX / 2;
	private static int MAX_SIMULATION = 10000;

	private final int[] x = new int[MAX_SIMULATION + 4];
	private final int[] y = new int[MAX_SIMULATION + 4];

	private final JFrame main = new JFrame();
	private AtomicBoolean isRunning = new AtomicBoolean();
	private final JTextArea ta = new JTextArea();
	private AtomicInteger step = new AtomicInteger(0);

	private JPanel canvas;

	void init() {
		x[0] = XSTART;
		y[0] = YSTART;
		setText();

		canvas = new JPanel() {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				drawWalk(g);
			}

			@Override
			public Dimension getPreferredSize() {
				return new Dimension(XMAX, YMAX);
			}
		};

		JButton startButton = new JButton("Start");
		startButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				// TODO Auto-generated method stub
				Thread nt = new Thread("hrllo") {

					public void run() {
						isRunning.set(true);
						while (step.get() < MAX_SIMULATION && isRunning.get()) {
							SwingUtilities.invokeLater(new Runnable() {

								@Override
								public void run() {
									// TODO Auto-generated method stub
									canvas.repaint();

								}

							});

							try {
								Thread.sleep(50);
							} catch (InterruptedException ex) {
								// TODO Auto-generated catch block
								ex.printStackTrace();
							}

						}

					}

				};
				nt.start();
			}
		});

		JButton stopButton = new JButton("Stop");
		stopButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				isRunning.set(false);

			}
		});

		JButton nextButton = new JButton("Next");
		nextButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				isRunning.set(false);
				canvas.repaint();

			}
		});

		JPanel north = new JPanel();
		north.add(startButton);
		north.add(stopButton);
		north.add(nextButton);
		north.setBackground(Color.blue);
		main.add(north, BorderLayout.NORTH);
		main.add(canvas, BorderLayout.CENTER);
        canvas.setBackground(Color.WHITE);
        ta.setBackground(Color.yellow);
		main.add(ta, BorderLayout.SOUTH);
		main.pack();
		main.setVisible(true);

	}

	private void setText() {
		ta.setText("Step " + step + "\n");
	}

	public static void main(String[] args) {
		RandomWalk simulation = new RandomWalk();
		simulation.init();
	}

	private void drawWalk(Graphics g) {
		g.setColor(Color.red);
		int k = step.getAndAdd(1);
		int nx = x[k];
		int ny = y[k];
		int item = new Random().nextInt(4);
		switch (item) {
		case 0:
			ny -= UNIT;
			break;
		case 1:
			nx += UNIT;
			break;
		case 2:
			ny += UNIT;
			break;
		case 3:
			nx -= UNIT;
			break;

		}
		x[k + 1] = nx;
		y[k + 1] = ny;
		for (int i = 0; i <= k; i++) {
			g.drawLine(x[i], y[i], x[i + 1], y[i + 1]);
		}
		setText();

	}

}
