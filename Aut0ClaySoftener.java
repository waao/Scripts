import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.geom.GeneralPath;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Properties;
import java.util.Random;
import java.util.Set;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import org.rsbot.event.events.MessageEvent;
import org.rsbot.event.listeners.MessageListener;
import org.rsbot.event.listeners.PaintListener;
import org.rsbot.script.Script;
import org.rsbot.script.ScriptManifest;
import org.rsbot.script.internal.MouseHandler;
import org.rsbot.script.methods.Bank;
import org.rsbot.script.methods.GrandExchange.GEItem;
import org.rsbot.script.methods.Methods;
import org.rsbot.script.util.Filter;
import org.rsbot.script.wrappers.RSArea;
import org.rsbot.script.wrappers.RSComponent;
import org.rsbot.script.wrappers.RSItem;
import org.rsbot.script.wrappers.RSModel;
import org.rsbot.script.wrappers.RSNPC;
import org.rsbot.script.wrappers.RSObject;
import org.rsbot.script.wrappers.RSObjectDef;
import org.rsbot.script.wrappers.RSPlayer;
import org.rsbot.script.wrappers.RSTile;
import org.rsbot.script.wrappers.RSTilePath;
import org.rsbot.util.GlobalConfiguration;

@ScriptManifest(authors = { "icnhzabot", "Aut0r" }, keywords = "Clay, Softener", name = "Aut0ClaySoftener", version = 1.0, description = "Softens clay at multiple locations!")
public class Aut0ClaySoftener extends Script implements PaintListener,
		MessageListener {

	public class ABSettings extends javax.swing.JFrame {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		// Variables declaration - do not modify
		private javax.swing.JRadioButton apimethods;

		private javax.swing.JRadioButton beziercurves;

		private javax.swing.JLabel jLabel1;

		private javax.swing.JLabel jLabel2;

		private javax.swing.JComboBox numABthreads;

		private javax.swing.JButton ok;
		private javax.swing.JRadioButton shortsplines;

		// End of variables declaration
		public ABSettings(final SettingsManager sm) {
			initComponents();
			sm.add("Use Bezier Curves", beziercurves);
			sm.add("Use short splines to points on a linde", shortsplines);
			sm.add("Number of Antiban Threads", numABthreads);
		}

		public int ABThreads() {
			return numABthreads.getSelectedIndex();
		}

		private void initComponents() {

			jLabel1 = new javax.swing.JLabel();
			apimethods = new javax.swing.JRadioButton();
			beziercurves = new javax.swing.JRadioButton();
			shortsplines = new javax.swing.JRadioButton();
			jLabel2 = new javax.swing.JLabel();
			numABthreads = new javax.swing.JComboBox();
			ok = new javax.swing.JButton();

			jLabel1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
			jLabel1.setText("Mouse Spline Generation (How the mouse moves)");

			apimethods.setSelected(true);
			apimethods.setText("RSBot's API's Methods");
			apimethods.setEnabled(false);

			beziercurves.setSelected(true);
			beziercurves
					.setText("Cubic Bezier Curves (A basic curve with 4 control points)");

			shortsplines.setSelected(true);
			shortsplines
					.setText("<html><body><font size=\\\"3\\\">Short Splines to and from points on a straight line (Generates<br/>the splines with RSBot's methods. The mouse moves fairly straight<br/>and very squiggly)</font></body></html>");

			jLabel2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
			jLabel2.setText("<html><body><font size=\\\"3\\\">Number of Antiban Threads: <br/>(1-2 is Recommended.)</font></body></html>");

			numABthreads.setModel(new javax.swing.DefaultComboBoxModel(
					new String[] { "0", "1", "2", "3", "4", "5" }));
			numABthreads.setSelectedIndex(2);

			ok.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
			ok.setText("OK");
			ok.addActionListener(new java.awt.event.ActionListener() {

				public void actionPerformed(final java.awt.event.ActionEvent evt) {
					okActionPerformed(evt);
				}
			});

			final javax.swing.GroupLayout layout = new javax.swing.GroupLayout(
					getContentPane());
			getContentPane().setLayout(layout);
			layout.setHorizontalGroup(layout
					.createParallelGroup(
							javax.swing.GroupLayout.Alignment.LEADING)
					.addGroup(
							layout.createSequentialGroup()
									.addContainerGap()
									.addGroup(
											layout.createParallelGroup(
													javax.swing.GroupLayout.Alignment.TRAILING)
													.addComponent(
															ok,
															javax.swing.GroupLayout.Alignment.LEADING,
															javax.swing.GroupLayout.DEFAULT_SIZE,
															343,
															Short.MAX_VALUE)
													.addComponent(
															jLabel1,
															javax.swing.GroupLayout.Alignment.LEADING)
													.addComponent(
															apimethods,
															javax.swing.GroupLayout.Alignment.LEADING)
													.addComponent(
															beziercurves,
															javax.swing.GroupLayout.Alignment.LEADING)
													.addComponent(
															shortsplines,
															javax.swing.GroupLayout.Alignment.LEADING,
															javax.swing.GroupLayout.PREFERRED_SIZE,
															javax.swing.GroupLayout.DEFAULT_SIZE,
															javax.swing.GroupLayout.PREFERRED_SIZE)
													.addGroup(
															javax.swing.GroupLayout.Alignment.LEADING,
															layout.createSequentialGroup()
																	.addComponent(
																			jLabel2,
																			javax.swing.GroupLayout.PREFERRED_SIZE,
																			javax.swing.GroupLayout.DEFAULT_SIZE,
																			javax.swing.GroupLayout.PREFERRED_SIZE)
																	.addGap(10,
																			10,
																			10)
																	.addComponent(
																			numABthreads,
																			0,
																			177,
																			Short.MAX_VALUE)))
									.addContainerGap()));
			layout.setVerticalGroup(layout
					.createParallelGroup(
							javax.swing.GroupLayout.Alignment.LEADING)
					.addGroup(
							layout.createSequentialGroup()
									.addContainerGap()
									.addComponent(jLabel1)
									.addGap(10, 10, 10)
									.addComponent(apimethods)
									.addPreferredGap(
											javax.swing.LayoutStyle.ComponentPlacement.RELATED)
									.addComponent(beziercurves)
									.addPreferredGap(
											javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
									.addComponent(
											shortsplines,
											javax.swing.GroupLayout.PREFERRED_SIZE,
											javax.swing.GroupLayout.DEFAULT_SIZE,
											javax.swing.GroupLayout.PREFERRED_SIZE)
									.addGap(35, 35, 35)
									.addGroup(
											layout.createParallelGroup(
													javax.swing.GroupLayout.Alignment.LEADING)
													.addComponent(
															jLabel2,
															javax.swing.GroupLayout.PREFERRED_SIZE,
															javax.swing.GroupLayout.DEFAULT_SIZE,
															javax.swing.GroupLayout.PREFERRED_SIZE)
													.addComponent(
															numABthreads,
															javax.swing.GroupLayout.PREFERRED_SIZE,
															32,
															javax.swing.GroupLayout.PREFERRED_SIZE))
									.addGap(28, 28, 28)
									.addComponent(
											ok,
											javax.swing.GroupLayout.PREFERRED_SIZE,
											37,
											javax.swing.GroupLayout.PREFERRED_SIZE)
									.addContainerGap(
											javax.swing.GroupLayout.DEFAULT_SIZE,
											Short.MAX_VALUE)));

			pack();
		}

		private void okActionPerformed(final java.awt.event.ActionEvent evt) {
			setVisible(false);
		}

		public boolean shortSplines() {
			return shortsplines.isSelected();
		}

		public boolean useBeziers() {
			return beziercurves.isSelected();
		}
	}

	private class antiban implements Runnable {

		public antiban() {
			switch (numABThreads) {
			case 0: {
				break;
			}
			case 1: {
				new Thread(this).start();
				break;
			}
			case 2: {
				new Thread(this).start();
				new Thread(this).start();
				break;
			}
			case 3: {
				new Thread(this).start();
				new Thread(this).start();
				new Thread(this).start();
				break;
			}
			case 4: {
				new Thread(this).start();
				new Thread(this).start();
				new Thread(this).start();
				new Thread(this).start();
				break;
			}
			case 5: {
				new Thread(this).start();
				new Thread(this).start();
				new Thread(this).start();
				new Thread(this).start();
				new Thread(this).start();
				break;
			}
			default: {
				new Thread(this).start();
				break;
			}
			}
		}

		private void antibancamera() {
			if (bank.isOpen()) {
				return;
			}
			final int rand = Methods.random(1, 30);
			switch (rand) {
			case 1: {
				cameraHandler.pitchRandomly(Methods.random(500, 800));
				break;
			}
			case 2: {
				cameraHandler.pitchRandomly(Methods.random(100, 600));
				break;
			}
			case 3: {
				cameraHandler.pitchRandomly(Methods.random(800, 1100));
				break;
			}
			case 4: {
				cameraHandler.turnRandomly(Methods.random(500, 800));
				break;
			}
			case 5: {
				cameraHandler.turnRandomly(Methods.random(100, 600));
				break;
			}
			case 6: {
				cameraHandler.turnRandomly(Methods.random(800, 1100));
				break;
			}
			case 7: {
				cameraHandler.moveRandomly(Methods.random(600, 900),
						(Methods.random(1, 4) >= 2));
				break;
			}
			case 8: {
				cameraHandler.moveRandomly(Methods.random(300, 600),
						(Methods.random(1, 4) >= 2));
				break;
			}
			case 9: {
				cameraHandler.moveRandomly(Methods.random(950, 1475),
						(Methods.random(1, 5) >= 2));
				break;
			}
			case 10: {
				camera.setPitch(Methods.random(1, 2) == Methods.random(1, 2));
				break;
			}
			case 11: {
				camera.setPitch(Methods.random(1, 2) == Methods.random(1, 2));
				break;
			}
			case 12: {
				camera.moveRandomly(Methods.random(800, 1200));
				break;
			}
			case 13: {
				camera.setAngle(Methods.random(13, 43));
				sleep(Methods.random(5, 30));
				camera.setAngle(Methods.random(13, 43));
				break;
			}
			case 14: {
				camera.moveRandomly(Methods.random(200, 800));
				break;
			}
			case 15: {
				camera.moveRandomly(Methods.random(200, 800));
				break;
			}
			case 16: {
				cameraHandler.lookAtRandThings();
				break;
			}
			case 17: {
				cameraHandler.lookAtRandThings();
				break;
			}
			case 18: {
				try {
					camera.turnTo(loc.getRSObject());
				} catch (final NullPointerException npe) {
					cameraHandler.lookAtRandThings();
				}
				break;
			}
			case 19: {
				try {
					camera.turnTo(loc.getRSObject());
				} catch (final NullPointerException npe) {
					cameraHandler.lookAtRandThings();
				}
				break;
			}
			}
			if (rand >= 28) {
				camera.moveRandomly(Methods.random(250, 500));
			}
		}

		public void antibanmouse() {
			final int rand = Methods.random(1, 26);
			switch (rand) {
			case 1: {
				mouse.moveSlightly();
				mouse.moveSlightly();
				mousemovet = System.currentTimeMillis();
				break;
			}
			case 2: {
				if (Methods.random(1, 2) == Methods.random(1, 2)
						|| Methods.random(1, 3) == Methods.random(1, 3)
						|| !calc.pointOnScreen(mouse.getLocation())) {
					final Point dest = new Point(Methods.random(
							Methods.random(0, game.getWidth()),
							Methods.random(game.getWidth() / 2, 150)),
							Methods.random(Methods.random(0, game.getHeight()),
									Methods.random(game.getHeight() / 2, 150)));
					mouseMove(dest);
				} else {
					mouse.moveRandomly(Methods.random(15, 300));
				}
				mousemovet = System.currentTimeMillis();
				break;
			}
			case 3: {
				final Point p = new Point(Methods.random(175, 325),
						Methods.random(75, 225));
				mouseMove(p);
				mousemovet = System.currentTimeMillis();
				break;
			}
			case 4: {
				mouse.moveSlightly();
				mouse.moveRandomly(Methods.random(30, 39),
						Methods.random(40, 60));
				mousemovet = System.currentTimeMillis();
				break;
			}
			case 5: {
				mouse.moveSlightly();
				mouse.moveSlightly();
				mouse.moveSlightly();
				mouse.moveSlightly();
				mousemovet = System.currentTimeMillis();
				break;
			}
			case 6: {
				final Point p = new Point(Methods.random(120, 350),
						Methods.random(50, 300));
				mouseMove(p);
				mouse.moveRandomly(Methods.random(5, 10),
						Methods.random(20, 30));
				mousemovet = System.currentTimeMillis();
				break;
			}
			case 7: {
				mouse.moveOffScreen();
				mouse.moveSlightly();
				mousemovet = System.currentTimeMillis();
				break;
			}
			case 8: {
				mouse.moveRandomly(Methods.random(5, 10),
						Methods.random(20, 30));
				mouse.moveOffScreen();
				mousemovet = System.currentTimeMillis();
				break;
			}
			case 9: {
				mouse.moveSlightly();
				mouse.moveRandomly(Methods.random(5, 10),
						Methods.random(20, 30));
				mouse.moveSlightly();
				mousemovet = System.currentTimeMillis();
				break;
			}
			case 10: {
				final Point p = new Point(Methods.random(40, 200),
						Methods.random(10, 200));
				mouseMove(p);
				mousemovet = System.currentTimeMillis();
				break;
			}
			case 11: {
				final int rand2 = Methods.random(1, 6);
				switch (rand2) {
				case 1: {
					mouse.moveSlightly();
					break;
				}
				case 2: {
					mouse.moveRandomly(Methods.random(15, 300));
					break;
				}
				case 3: {
					final Point p = new Point(Methods.random(40, 200),
							Methods.random(10, 200));
					mouseMove(p);
					break;
				}
				case 4: {
					final Point p = new Point(Methods.random(175, 325),
							Methods.random(75, 225));
					mouseMove(p);
					break;
				}
				case 5: {
					mouse.moveRandomly(Methods.random(5, 10),
							Methods.random(20, 30));
					break;
				}
				}
				mouse.moveOffScreen();
				mousemovet = System.currentTimeMillis();
				break;
			}
			case 12: {
				/* Wiggle Mouse */
				final Point loc = mouse.getLocation();
				int x = Methods.random(500, 650);
				int y = Methods.random(300, 420);
				mouseMove(new Point(x, y));
				final int randm = Methods.random(1, 2);
				sleep(Methods.random(38, 642));
				if (randm == 1) {
					x = Math.abs(x - Methods.random(75, 175));
					final int random = Methods.random(1, 2);
					if (random == 1) {
						y = Math.abs(y - Methods.random(75, 175));
					} else {
						y = Math.abs(y + Methods.random(75, 175));
					}
				} else {
					x = Math.abs(x + Methods.random(75, 175));
					final int random = Methods.random(1, 2);
					if (random == 1) {
						y = Math.abs(y + Methods.random(75, 175));
					} else {
						y = Math.abs(y - Methods.random(75, 175));
					}
				}
				mouseMove(new Point(x, y));
				sleep(Methods.random(0, 320));
				x = loc.x;
				y = loc.y;
				final int rand2 = Methods.random(1, 2);
				if (rand2 == 1) {
					x = Math.abs(x - Methods.random(50, 200));
					final int random = Methods.random(1, 2);
					if (random == 1) {
						y = Math.abs(y - Methods.random(50, 200));
					} else {
						y = Math.abs(y + Methods.random(50, 200));
					}
				} else {
					x = Math.abs(x + Methods.random(50, 200));
					final int random = Methods.random(1, 2);
					if (random == 1) {
						y = Math.abs(y + Methods.random(50, 200));
					} else {
						y = Math.abs(y - Methods.random(50, 200));
					}
				}
				mouseMove(new Point(x, y));
				sleep(Methods.random(0, 200));
				mousemovet = System.currentTimeMillis();
				break;
			}
			case 13: {
				/* Wiggle Mouse */
				final Point loc = mouse.getLocation();
				int x = Methods.random(300, 450);
				int y = Methods.random(100, 220);
				mouseMove(new Point(x, y));
				final int randm = Methods.random(1, 2);
				sleep(Methods.random(38, 642));
				if (randm == 1) {
					x = Math.abs(x - Methods.random(75, 175));
					final int random = Methods.random(1, 2);
					if (random == 1) {
						y = Math.abs(y - Methods.random(75, 175));
					} else {
						y = Math.abs(y + Methods.random(75, 175));
					}
				} else {
					x = Math.abs(x + Methods.random(75, 175));
					final int random = Methods.random(1, 2);
					if (random == 1) {
						y = Math.abs(y + Methods.random(75, 175));
					} else {
						y = Math.abs(y - Methods.random(75, 175));
					}
				}
				mouseMove(new Point(x, y));
				sleep(Methods.random(0, 320));
				x = loc.x;
				y = loc.y;
				final int rand2 = Methods.random(1, 2);
				if (rand2 == 1) {
					x = Math.abs(x - Methods.random(50, 200));
					final int random = Methods.random(1, 2);
					if (random == 1) {
						y = Math.abs(y - Methods.random(50, 200));
					} else {
						y = Math.abs(y + Methods.random(50, 200));
					}
				} else {
					x = Math.abs(x + Methods.random(50, 200));
					final int random = Methods.random(1, 2);
					if (random == 1) {
						y = Math.abs(y + Methods.random(50, 200));
					} else {
						y = Math.abs(y - Methods.random(50, 200));
					}
				}
				mouseMove(new Point(x, y));
				sleep(Methods.random(0, 200));
				mousemovet = System.currentTimeMillis();
				break;
			}
			}
			if (rand >= 25) {
				mouse.moveSlightly();
			}
		}

		private void doAntiban() {
			final int rand = Methods.random(1, 6);
			if (rand <= 2 && !bank.isOpen()) {
				antibancamera();
			} else if (rand >= 5) {
				antibanmouse();
			} else {
				if (Methods.random(1, 3) >= Methods.random(1, 3)) {
					antibanmouse();
				} else if (!bank.isOpen()) {
					antibancamera();
				} else {
					antibanmouse();
				}
			}
		}

		public void kill() {
			running = false;
			Thread.interrupted();
		}

		public void run() {
			while (running) {
				try {
					sleep(Methods.random(90, 210));
					if (isPaused()) {
						sleep(Methods.random(900, 1100));
						continue;
					}
					if (!game.isLoggedIn() || game.isWelcomeScreen()
							|| game.isLoginScreen()) {
						env.enableRandom("Login");
						sleep(Methods.random(900, 1100));
						continue;
					}
					final int rand = Methods.random(1, 45);
					switch (rand) {
					case 1: {
						if (canAB && !mouseCheck.movedRecently()
								&& !cameraCheck.movedRecently()) {
							doAntiban();
						}
					}
						break;
					case 2:
					case 3: {
						if (canAB && !mouseCheck.movedRecently()) {
							antibanmouse();
						}
					}
						break;
					case 4:
					case 5: {
						if (canAB && !cameraCheck.movedRecently()) {
							antibancamera();
						}
					}
						break;
					}
				} catch (final Exception e) {
				} catch (final Error e) {
				}
				sleep(Methods.random(90, 210));
			}
		}

		private void sleep(final long timeout) {
			try {
				Thread.sleep(timeout);
			} catch (final InterruptedException ie) {
			}
		}
	}

	/* A camera handler for Antiban purposes */
	private class Camera {

		private final char up = KeyEvent.VK_UP;
		private final char down = KeyEvent.VK_DOWN;
		private final char left = KeyEvent.VK_LEFT;
		private final char right = KeyEvent.VK_RIGHT;

		private int angleToTile(final RSTile tile) {
			final int angle = (calc.angleToTile(tile) - 90) % 360;
			return angle < 0 ? angle + 360 : angle;
		}

		public void lookAtRandThings() {
			try {
				final RSObject[] allPossibleObjects = objects
						.getAll(new Filter<RSObject>() {

							@Override
							public boolean accept(final RSObject o) {
								return calc.distanceTo(o) <= 8
										&& (o.getType().equals(
												RSObject.Type.INTERACTABLE) || o
												.getType().equals(
														RSObject.Type.BOUNDARY));
							}
						});
				RSObject objecttochoose = allPossibleObjects[Methods.random(0,
						allPossibleObjects.length - 1)];
				final RSNPC[] allPossibleNPCs = npcs
						.getAll(new Filter<RSNPC>() {

							@Override
							public boolean accept(final RSNPC npc) {
								return calc.distanceTo(npc) <= 8
										&& npc.isValid();
							}
						});
				RSNPC npctochoose = allPossibleNPCs[Methods.random(0,
						allPossibleNPCs.length - 1)];
				if (npctochoose == null || objecttochoose == null) {
					npctochoose = allPossibleNPCs[0];
					objecttochoose = allPossibleObjects[0];
				}
				boolean useobj = true;
				if (calc.distanceTo(npctochoose) <= 5) {
					useobj = false;
				}
				if (calc.distanceTo(objecttochoose) <= 5) {
					useobj = true;
				} else if (!(calc.distanceTo(npctochoose) <= 5)) {
					useobj = Methods.random(1, 2) == Methods.random(1, 2) ? true
							: false;
				}
				if (useobj) {
					final RSTile loc = objecttochoose.getLocation();
					final int angle = angleToTile(loc);
					setAngle(angle);
				} else {
					final RSTile loc = npctochoose.getLocation();
					final int angle = angleToTile(loc);
					setAngle(angle);
				}
			} catch (final Exception e) {
			}
		}

		public void moveRandomly(final long averageTimeout,
				final boolean doubledir) {
			try {
				final int sleep = Math.abs((int) Methods.random(
						averageTimeout - 200, averageTimeout + 200));
				final int sleep2 = Math.abs((int) Methods.random(
						averageTimeout - 200, averageTimeout + 200));
				char begin = 0;
				char next = 0;
				final int rand = Methods.random(1, 4);
				final int rand2 = Methods.random(1, 2);
				if (rand == 1) {
					begin = right;
					if (rand2 == 1) {
						next = down;
					} else if (rand2 == 2) {
						next = up;
					}
				} else if (rand == 2) {
					begin = left;
					if (rand2 == 1) {
						next = down;
					} else if (rand2 == 2) {
						next = up;
					}
				} else if (rand == 3) {
					begin = up;
					if (rand2 == 1) {
						next = left;
					} else if (rand2 == 2) {
						next = right;
					}
				} else if (rand == 4) {
					begin = down;
					if (rand2 == 1) {
						next = left;
					} else if (rand2 == 2) {
						next = right;
					}
				} else {
					begin = up;
					if (rand2 == 1) {
						next = left;
					} else if (rand2 == 2) {
						next = right;
					}
				}
				if (doubledir) {
					new Thread(new charpresser(sleep, begin)).start();
					Methods.sleep(Methods.random(0, 5));
					new Thread(new charpresser(sleep2, next)).start();
				} else {
					new Thread(new charpresser(sleep, begin)).start();
				}
			} catch (final NullPointerException ignored) {
			}
		}

		public void pitchRandomly(final long averageTimeout) {
			int pitch = Methods.random(0, 100);
			pitch = Math.abs(Methods.random(pitch - 20, pitch + 20));
			if (pitch > 100) {
				pitch = 100;
			}
			if (Math.abs(pitch - camera.getPitch()) >= 15) {
				camera.setPitch(pitch);
			} else {
				camera.setPitch(Methods.random(1, 2) == Methods.random(1, 2) ? true
						: false);
			}
		}

		private void setAngle(final int angle) {
			if (camera.getAngleTo(angle) > 5) {
				final charpresser presser = new charpresser(
						(char) KeyEvent.VK_LEFT);
				new Thread(presser).start();
				while (camera.getAngleTo(angle) >= Methods.random(5, 8)) {
					Methods.sleep(10);
				}
				presser.stop = true;
				presser.stopThread();
				keyboard.releaseKey((char) KeyEvent.VK_LEFT);
			} else if (camera.getAngleTo(angle) <= -5) {
				final charpresser presser = new charpresser(
						(char) KeyEvent.VK_RIGHT);
				new Thread(presser).start();
				while (camera.getAngleTo(angle) <= -6) {
					Methods.sleep(10);
				}
				presser.stop = true;
				presser.stopThread();
				keyboard.releaseKey((char) KeyEvent.VK_RIGHT);
			} else {
				camera.setAngle(angle);
			}
		}

		public void turnRandomly(final long averageTimeout) {
			final int sleep = Math.abs((int) Methods.random(
					averageTimeout - 200, averageTimeout + 200));
			final char dir = Methods.random(1, 2) == Methods.random(1, 2) ? left
					: right;
			new Thread(new charpresser(sleep, dir)).start();
		}
	}

	private class CameraMovementChecker implements Runnable {

		private int x = 0, y = 0, z = 0;
		private boolean movement = false;

		public CameraMovementChecker() {
			new Thread(this).start();
		}

		public boolean movedRecently() {
			return movement;
		}

		public void run() {
			while (running) {
				try {
					if (isPaused()) {
						sleep(Methods.random(900, 1100));
						continue;
					}
					x = camera.getX();
					y = camera.getY();
					z = camera.getZ();
					if (camera.getX() != x || camera.getY() != y
							|| camera.getZ() != z) {
						movement = true;
					} else {
						movement = false;
					}
					sleep(Methods.random(200, 300));
					if (camera.getX() != x || camera.getY() != y
							|| camera.getZ() != z) {
						movement = true;
					} else {
						movement = false;
					}
				} catch (final Exception e) {
				}
			}
		}

		private void sleep(final int t) {
			try {
				Thread.sleep(t);
			} catch (final InterruptedException e) {
			}
		}
	}

	/* Runnables */
	private class charpresser implements Runnable {

		private final char topress;
		private long tohold = -1;
		public boolean stop = false;

		public charpresser(final char press) {
			topress = press;
		}

		public charpresser(final long timeout, final char press) {
			topress = press;
			tohold = timeout;
		}

		public void run() {
			try {
				Thread.sleep(Methods.random(0, 70));
				if (tohold != -1) {
					keyboard.pressKey(topress);
					Thread.sleep(tohold);
					keyboard.releaseKey(topress);
				} else {
					keyboard.pressKey(topress);
					while (!stop) {
						Thread.sleep(10);
					}
					keyboard.releaseKey(topress);
				}
			} catch (final InterruptedException ie) {
			}
		}

		public void stopThread() {
			stop = true;
			Thread.interrupted();
		}
	}

	/* Edgeville Well */
	private class Edgeville extends Locations {

		private final RSArea bank = new RSArea(new RSTile[] {
				new RSTile(3098, 3498), new RSTile(3090, 3499),
				new RSTile(3090, 3491), new RSTile(3093, 3489),
				new RSTile(3098, 3487), new RSTile(3099, 3492) });
		private final RSArea object = new RSArea(new RSTile[] {
				new RSTile(3084, 3496), new RSTile(3089, 3501),
				new RSTile(3086, 3506), new RSTile(3081, 3504),
				new RSTile(3081, 3497) });
		private final RSTile bankTile = new RSTile(3093, 3493);
		private final RSTile objectTile = new RSTile(3085, 3500);
		private final RSTile onObjTile = new RSTile(3084, 3501);
		private final String objectName = "Well";

		@Override
		public RSArea getBankArea() {
			return bank;
		}

		@Override
		public RSArea getObjectArea() {
			return object;
		}

		@Override
		public String getObjectName() {
			return objectName;
		}

		@Override
		public RSModel getRSModel() {
			return getRSObject().getModel();
		}

		@Override
		public RSObject getRSObject() {
			RSObject well = objects.getNearest(new Filter<RSObject>() {

				@Override
				public boolean accept(final RSObject check) {
					RSObjectDef def = check.getDef();
					if (def == null) {
						def = check.getDef();
					}
					if (def == null) {
						return false;
					}
					String name = def.getName();
					if (name == null) {
						name = def.getName();
					}
					if (name == null) {
						return false;
					}
					if (name.toLowerCase().equals(objectName.toLowerCase())
							&& check.getLocation().equals(onObjTile)) {
						return true;
					}
					return false;
				}
			});
			if (well == null) {
				final RSObject obj = objects.getTopAt(onObjTile);
				well = obj != null ? obj : objects.getNearest(objectName);
			}
			return well;
		}

		@Override
		public void walkToBank() {
			walker.walkTo(bankTile, true);
			canAB = true;
		}

		@Override
		public void walkToObject() {
			walker.walkTo(objectTile, true);
			canAB = true;
		}
	}

	/* Falador West */
	private class Falador extends Locations {

		private final RSArea object = new RSArea(new RSTile(2948, 3382),
				new RSTile(2950, 3384));
		private final RSArea bank = new RSArea(new RSTile(2945, 3368),
				new RSTile(2947, 3370));
		private final RSTile bankTile = new RSTile(2946, 3368);
		private final RSTile objectTile = new RSTile(2949, 3382);
		private final int objectId = 11661;
		private final String objectName = "Waterpump";

		@Override
		public RSArea getBankArea() {
			return bank;
		}

		@Override
		public RSArea getObjectArea() {
			return object;
		}

		@Override
		public String getObjectName() {
			return objectName;
		}

		@Override
		public RSModel getRSModel() {
			return objects.getNearest(objectId).getModel();
		}

		@Override
		public RSObject getRSObject() {
			return objects.getNearest(objectId);
		}

		@Override
		public void walkToBank() {
			walker.walkTo(bankTile, true);
			canAB = true;
		}

		@Override
		public void walkToObject() {
			walker.walkTo(objectTile, true);
			canAB = true;
		}
	}

	private class GUI extends javax.swing.JFrame {

		private final PaintOptions paintoptions;
		private final ABSettings antibanoptions;
		private static final long serialVersionUID = 1L;
		private boolean isCanceled = false;

		// Variables declaration - do not modify
		private javax.swing.JButton antiban;

		private javax.swing.JButton cancel;

		private javax.swing.JLabel jLabel1;

		private javax.swing.JLabel jLabel2;

		private javax.swing.JLabel jLabel3;

		private javax.swing.JLabel jLabel4;

		private javax.swing.JComboBox location;

		private javax.swing.JComboBox mousehi;

		private javax.swing.JComboBox mouselo;
		private javax.swing.JButton paint;
		private javax.swing.JButton start;

		// End of variables declaration
		public GUI(final SettingsManager sm) {
			initComponents();
			sm.add("Location", location);
			sm.add("Mouse Low", mouselo);
			sm.add("Mouse High", mousehi);
			paintoptions = new PaintOptions(sm);
			antibanoptions = new ABSettings(sm);
			sm.load();
		}

		private void antibanActionPerformed(final java.awt.event.ActionEvent evt) {
			if (!antibanoptions.isVisible()) {
				antibanoptions.setVisible(true);
			}
		}

		private void cancelActionPerformed(final java.awt.event.ActionEvent evt) {
			isCanceled = true;
			paintoptions.dispose();
			antibanoptions.dispose();
			dispose();
		}

		private void initComponents() {

			jLabel1 = new javax.swing.JLabel();
			jLabel2 = new javax.swing.JLabel();
			location = new javax.swing.JComboBox();
			jLabel3 = new javax.swing.JLabel();
			mouselo = new javax.swing.JComboBox();
			mousehi = new javax.swing.JComboBox();
			jLabel4 = new javax.swing.JLabel();
			paint = new javax.swing.JButton();
			antiban = new javax.swing.JButton();
			start = new javax.swing.JButton();
			cancel = new javax.swing.JButton();

			setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

			jLabel1.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
			jLabel1.setText("Aut0ClaySoftener");

			jLabel2.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
			jLabel2.setText("Location:");

			location.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
			location.setModel(new javax.swing.DefaultComboBoxModel(
					new String[] { "West Falador", "Edgeville" }));

			jLabel3.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
			jLabel3.setText("Mouse Lo / Mouse Hi:");

			mouselo.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
			mouselo.setModel(new javax.swing.DefaultComboBoxModel(new String[] {
					"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10",
					"11", "12", "13", "14", "15", "16", "17", "18", "19", "20",
					"21", "22", "23", "24", "25" }));
			mouselo.setSelectedIndex(5);

			mousehi.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
			mousehi.setModel(new javax.swing.DefaultComboBoxModel(new String[] {
					"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10",
					"11", "12", "13", "14", "15", "16", "17", "18", "19", "20",
					"21", "22", "23", "24", "25" }));
			mousehi.setSelectedIndex(10);

			jLabel4.setText("/");

			paint.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
			paint.setText("Paint Options");
			paint.addActionListener(new java.awt.event.ActionListener() {

				public void actionPerformed(final java.awt.event.ActionEvent evt) {
					paintActionPerformed(evt);
				}
			});

			antiban.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
			antiban.setText("Antiban Options");
			antiban.addActionListener(new java.awt.event.ActionListener() {

				public void actionPerformed(final java.awt.event.ActionEvent evt) {
					antibanActionPerformed(evt);
				}
			});

			start.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
			start.setText("Start!");
			start.addActionListener(new java.awt.event.ActionListener() {

				public void actionPerformed(final java.awt.event.ActionEvent evt) {
					startActionPerformed(evt);
				}
			});

			cancel.setText("Cancel");
			cancel.addActionListener(new java.awt.event.ActionListener() {

				public void actionPerformed(final java.awt.event.ActionEvent evt) {
					cancelActionPerformed(evt);
				}
			});

			final javax.swing.GroupLayout layout = new javax.swing.GroupLayout(
					getContentPane());
			getContentPane().setLayout(layout);
			layout.setHorizontalGroup(layout
					.createParallelGroup(
							javax.swing.GroupLayout.Alignment.LEADING)
					.addGroup(
							layout.createSequentialGroup()
									.addGroup(
											layout.createParallelGroup(
													javax.swing.GroupLayout.Alignment.LEADING)
													.addGroup(
															layout.createSequentialGroup()
																	.addGap(55,
																			55,
																			55)
																	.addComponent(
																			jLabel1))
													.addGroup(
															layout.createSequentialGroup()
																	.addContainerGap()
																	.addComponent(
																			jLabel2)
																	.addPreferredGap(
																			javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
																	.addComponent(
																			location,
																			0,
																			199,
																			Short.MAX_VALUE))
													.addGroup(
															layout.createSequentialGroup()
																	.addContainerGap()
																	.addGroup(
																			layout.createParallelGroup(
																					javax.swing.GroupLayout.Alignment.TRAILING,
																					false)
																					.addComponent(
																							antiban,
																							javax.swing.GroupLayout.Alignment.LEADING,
																							javax.swing.GroupLayout.DEFAULT_SIZE,
																							javax.swing.GroupLayout.DEFAULT_SIZE,
																							Short.MAX_VALUE)
																					.addComponent(
																							jLabel3,
																							javax.swing.GroupLayout.Alignment.LEADING,
																							javax.swing.GroupLayout.DEFAULT_SIZE,
																							javax.swing.GroupLayout.DEFAULT_SIZE,
																							Short.MAX_VALUE))
																	.addPreferredGap(
																			javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
																	.addGroup(
																			layout.createParallelGroup(
																					javax.swing.GroupLayout.Alignment.LEADING)
																					.addGroup(
																							layout.createSequentialGroup()
																									.addComponent(
																											mouselo,
																											javax.swing.GroupLayout.PREFERRED_SIZE,
																											52,
																											javax.swing.GroupLayout.PREFERRED_SIZE)
																									.addPreferredGap(
																											javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
																									.addComponent(
																											jLabel4,
																											javax.swing.GroupLayout.PREFERRED_SIZE,
																											11,
																											javax.swing.GroupLayout.PREFERRED_SIZE)
																									.addGap(3,
																											3,
																											3)
																									.addComponent(
																											mousehi,
																											0,
																											50,
																											Short.MAX_VALUE))
																					.addComponent(
																							paint,
																							javax.swing.GroupLayout.DEFAULT_SIZE,
																							126,
																							Short.MAX_VALUE))))
									.addContainerGap())
					.addGroup(
							layout.createSequentialGroup()
									.addGap(45, 45, 45)
									.addComponent(
											start,
											javax.swing.GroupLayout.DEFAULT_SIZE,
											193, Short.MAX_VALUE)
									.addGap(48, 48, 48))
					.addGroup(
							layout.createSequentialGroup()
									.addGap(76, 76, 76)
									.addComponent(
											cancel,
											javax.swing.GroupLayout.PREFERRED_SIZE,
											130,
											javax.swing.GroupLayout.PREFERRED_SIZE)
									.addContainerGap(80, Short.MAX_VALUE)));
			layout.setVerticalGroup(layout
					.createParallelGroup(
							javax.swing.GroupLayout.Alignment.LEADING)
					.addGroup(
							layout.createSequentialGroup()
									.addContainerGap()
									.addComponent(
											jLabel1,
											javax.swing.GroupLayout.PREFERRED_SIZE,
											28,
											javax.swing.GroupLayout.PREFERRED_SIZE)
									.addGap(18, 18, 18)
									.addGroup(
											layout.createParallelGroup(
													javax.swing.GroupLayout.Alignment.BASELINE)
													.addComponent(jLabel2)
													.addComponent(
															location,
															javax.swing.GroupLayout.PREFERRED_SIZE,
															javax.swing.GroupLayout.DEFAULT_SIZE,
															javax.swing.GroupLayout.PREFERRED_SIZE))
									.addGap(11, 11, 11)
									.addGroup(
											layout.createParallelGroup(
													javax.swing.GroupLayout.Alignment.BASELINE)
													.addComponent(jLabel3)
													.addComponent(
															mouselo,
															javax.swing.GroupLayout.PREFERRED_SIZE,
															javax.swing.GroupLayout.DEFAULT_SIZE,
															javax.swing.GroupLayout.PREFERRED_SIZE)
													.addComponent(
															mousehi,
															javax.swing.GroupLayout.PREFERRED_SIZE,
															javax.swing.GroupLayout.DEFAULT_SIZE,
															javax.swing.GroupLayout.PREFERRED_SIZE)
													.addComponent(
															jLabel4,
															javax.swing.GroupLayout.PREFERRED_SIZE,
															24,
															javax.swing.GroupLayout.PREFERRED_SIZE))
									.addGap(11, 11, 11)
									.addGroup(
											layout.createParallelGroup(
													javax.swing.GroupLayout.Alignment.BASELINE)
													.addComponent(antiban)
													.addComponent(paint))
									.addPreferredGap(
											javax.swing.LayoutStyle.ComponentPlacement.RELATED,
											35, Short.MAX_VALUE)
									.addComponent(
											start,
											javax.swing.GroupLayout.PREFERRED_SIZE,
											39,
											javax.swing.GroupLayout.PREFERRED_SIZE)
									.addPreferredGap(
											javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
									.addComponent(cancel).addGap(18, 18, 18)));

			pack();
		}

		public boolean isCanceled() {
			return isCanceled;
		}

		private void paintActionPerformed(final java.awt.event.ActionEvent evt) {
			if (!paintoptions.isVisible()) {
				paintoptions.setVisible(true);
			}
		}

		private void setVars() {
			clicks = paintoptions.clicks();
			defmouse = paintoptions.defaultMouse();
			rotmouse = paintoptions.rotateMouse();
			showmouse = paintoptions.showMouse();
			paths = paintoptions.drawPaths();
			stats = paintoptions.drawStats();
			beziers = antibanoptions.useBeziers();
			shortsplines = antibanoptions.shortSplines();
			numABThreads = antibanoptions.ABThreads();
			mouseLo = mouselo.getSelectedIndex();
			mouseHi = mousehi.getSelectedIndex();
		}

		public int softenLocation() {
			return location.getSelectedIndex();
		}

		private void startActionPerformed(final java.awt.event.ActionEvent evt) {
			isCanceled = false;
			paintoptions.dispose();
			antibanoptions.dispose();
			dispose();
			setVars();
		}
	}

	private class InventoryListener implements Runnable {

		private long idle = 50;
		private int lastCount = 0;
		private int lastCount2 = 0;

		public long idle() {
			return idle;
		}

		public void kill() {
			running = false;
			Thread.interrupted();
		}

		public void reset(final long t) {
			idle = t;
		}

		public void run() {
			while (running) {
				sleep(90);
				if (isPaused()) {
					continue;
				}
				if (lastCount < inventory.getCount(softClay)
						|| lastCount2 > inventory.getCount(clay)) {
					idle = 0;
				} else {
					idle++;
				}
				lastCount = inventory.getCount(softClay);
				lastCount2 = inventory.getCount(clay);
			}
		}

		private void sleep(final int t) {
			try {
				Thread.sleep(t);
			} catch (final Exception e) {
			}
		}
	}

	private abstract class Locations {

		public abstract RSArea getBankArea();

		public abstract RSArea getObjectArea();

		public abstract String getObjectName();

		public abstract RSModel getRSModel();

		public abstract RSObject getRSObject();

		public abstract void walkToBank();

		public abstract void walkToObject();
	}

	protected static class locVar {

		public static final int fally = 0;
		public static final int edge = 1;
	}

	private class MouseMovementChecker implements Runnable {

		public boolean movement = false;
		private long time = 0, mouseTime = 0;
		private Point mousePoint = new Point(-1, -1);

		public MouseMovementChecker() {
			new Thread(this).start();
		}

		public boolean movedRecently() {
			return movement && System.currentTimeMillis() - mousemovet >= 800;
		}

		public void run() {
			while (running) {
				try {
					if (isPaused()) {
						sleep(Methods.random(900, 1100));
						continue;
					}
					time = System.currentTimeMillis();
					mouseTime = mouse.getPressTime();
					mousePoint = mouse.getLocation();
					if (time - mouseTime <= 900) {
						movement = true;
					} else {
						movement = false;
					}
					sleep(Methods.random(100, 150));
					if (System.currentTimeMillis() - mouseTime >= 1100) {
						movement = false;
					} else {
						movement = true;
					}
					sleep(Methods.random(200, 150));
					if (System.currentTimeMillis() - mouseTime >= 1225) {
						movement = false;
					} else {
						movement = true;
					}
					if (!mousePoint.equals(mouse.getLocation())) {
						movement = true;
					} else {
						movement = false;
					}
					if (System.currentTimeMillis() - mouse.getPressTime() >= 1000) {
						movement = false;
					} else {
						movement = true;
					}
				} catch (final Exception e) {
				}
			}
		}

		private void sleep(final int t) {
			try {
				Thread.sleep(t);
			} catch (final InterruptedException e) {
			}
		}
	}

	public class PaintOptions extends javax.swing.JFrame {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		// Variables declaration - do not modify
		private javax.swing.ButtonGroup buttonGroup1;

		private javax.swing.JRadioButton defaultmouse;

		private javax.swing.JRadioButton nexusmouse;

		private javax.swing.JButton ok;

		private javax.swing.JRadioButton rotatemouse;

		private javax.swing.JRadioButton showclicks;

		private javax.swing.JRadioButton showmouse;

		private javax.swing.JRadioButton showpaths;

		private javax.swing.JRadioButton showstats;

		// End of variables declaration

		public PaintOptions(final SettingsManager sm) {
			initComponents();
			sm.add("Show Mouse", showmouse);
			sm.add("Show Stats", showstats);
			sm.add("Show Clicks", showclicks);
			sm.add("Draw Paths", showpaths);
			sm.add("Default Mouse", defaultmouse);
			sm.add("NeXus Mouse", nexusmouse);
			sm.add("Rotate Mouse", rotatemouse);
		}

		public boolean clicks() {
			return showmouse.isSelected() && showclicks.isSelected();
		}

		public boolean defaultMouse() {
			return showmouse.isSelected() && defaultmouse.isSelected();
		}

		public boolean drawPaths() {
			return showpaths.isSelected();
		}

		public boolean drawStats() {
			return showstats.isSelected();
		}

		private void initComponents() {

			buttonGroup1 = new javax.swing.ButtonGroup();
			showmouse = new javax.swing.JRadioButton();
			showstats = new javax.swing.JRadioButton();
			showclicks = new javax.swing.JRadioButton();
			showpaths = new javax.swing.JRadioButton();
			defaultmouse = new javax.swing.JRadioButton();
			nexusmouse = new javax.swing.JRadioButton();
			rotatemouse = new javax.swing.JRadioButton();
			ok = new javax.swing.JButton();

			showmouse.setSelected(true);
			showmouse.setText("Paint Mouse");
			showmouse.addActionListener(new java.awt.event.ActionListener() {

				public void actionPerformed(final java.awt.event.ActionEvent evt) {
					showmouseActionPerformed(evt);
				}
			});

			showstats.setSelected(true);
			showstats.setText("Paint Statistics (Runtime, Profit, etc)");

			showclicks.setSelected(true);
			showclicks.setText("Paint Mouse Clicks");

			showpaths.setText("Paint Walk Paths");

			buttonGroup1.add(defaultmouse);
			defaultmouse.setSelected(true);
			defaultmouse
					.setText("Mouse Type: Wacky colors and lines. Best with rotation");

			buttonGroup1.add(nexusmouse);
			nexusmouse.setText("Mouse Type: iBot (NeXus) mouse");

			rotatemouse.setSelected(true);
			rotatemouse.setText("Rotate Mouse");

			ok.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
			ok.setText("OK");
			ok.addActionListener(new java.awt.event.ActionListener() {

				public void actionPerformed(final java.awt.event.ActionEvent evt) {
					okActionPerformed(evt);
				}
			});

			final javax.swing.GroupLayout layout = new javax.swing.GroupLayout(
					getContentPane());
			getContentPane().setLayout(layout);
			layout.setHorizontalGroup(layout
					.createParallelGroup(
							javax.swing.GroupLayout.Alignment.LEADING)
					.addGroup(
							layout.createSequentialGroup()
									.addContainerGap(
											javax.swing.GroupLayout.DEFAULT_SIZE,
											Short.MAX_VALUE)
									.addGroup(
											layout.createParallelGroup(
													javax.swing.GroupLayout.Alignment.LEADING)
													.addComponent(showmouse)
													.addComponent(showstats)
													.addComponent(showclicks)
													.addComponent(showpaths))
									.addGap(32, 32, 32)
									.addGroup(
											layout.createParallelGroup(
													javax.swing.GroupLayout.Alignment.LEADING,
													false)
													.addComponent(nexusmouse)
													.addComponent(rotatemouse)
													.addComponent(defaultmouse)
													.addGroup(
															layout.createSequentialGroup()
																	.addComponent(
																			ok,
																			javax.swing.GroupLayout.DEFAULT_SIZE,
																			javax.swing.GroupLayout.DEFAULT_SIZE,
																			Short.MAX_VALUE)
																	.addContainerGap()))));
			layout.setVerticalGroup(layout
					.createParallelGroup(
							javax.swing.GroupLayout.Alignment.LEADING)
					.addGroup(
							layout.createSequentialGroup()
									.addGroup(
											layout.createParallelGroup(
													javax.swing.GroupLayout.Alignment.LEADING)
													.addGroup(
															layout.createSequentialGroup()
																	.addContainerGap()
																	.addComponent(
																			showmouse)
																	.addPreferredGap(
																			javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
																	.addComponent(
																			showstats)
																	.addPreferredGap(
																			javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
																	.addComponent(
																			showclicks)
																	.addPreferredGap(
																			javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
																	.addComponent(
																			showpaths))
													.addGroup(
															layout.createSequentialGroup()
																	.addComponent(
																			defaultmouse)
																	.addPreferredGap(
																			javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
																	.addComponent(
																			nexusmouse)
																	.addPreferredGap(
																			javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
																	.addComponent(
																			rotatemouse)
																	.addPreferredGap(
																			javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
																	.addComponent(
																			ok)))
									.addContainerGap(11, Short.MAX_VALUE)));

			pack();
		}

		private void okActionPerformed(final java.awt.event.ActionEvent evt) {
			setVisible(false);
		}

		public boolean rotateMouse() {
			return showmouse.isSelected() && rotatemouse.isSelected();
		}

		public boolean showMouse() {
			return showmouse.isSelected();
		}

		private void showmouseActionPerformed(
				final java.awt.event.ActionEvent evt) {
			final boolean b = showmouse.isSelected();
			nexusmouse.setEnabled(b);
			defaultmouse.setEnabled(b);
			rotatemouse.setEnabled(b);
		}
	}

	private class PriceLoader implements Runnable {

		public void run() {
			try {
				final GEItem softclay = grandExchange.lookup(softClay);
				final GEItem clayitem = grandExchange.lookup(clay);
				if (softclay == null || clayitem == null) {
					return;
				}
				final int softprice = softclay.getGuidePrice();
				final int price = clayitem.getGuidePrice();
				if (softprice == -1 || price == -1) {
					return;
				}
				softClayPrice = softprice;
				clayPrice = price;
				final URL mouseurl = new URL(
						"http://i49.tinypic.com/35bh2rq.png");
				final URL clickedurl = new URL(
						"http://i50.tinypic.com/rk0b3r.png");
				nmouse = ImageIO.read(mouseurl);
				nclicked = ImageIO.read(clickedurl);
			} catch (final MalformedURLException mue) {
				return;
			} catch (final IOException ioe) {
				return;
			}
		}
	}

	private class SettingsManager {

		class Pair {

			String key;
			JComponent component;

			public Pair(final String key, final JComponent component) {
				this.key = key;
				this.component = component;
			}
		}

		private final String name;

		private final LinkedList<Pair> pairs = new LinkedList<Pair>();

		public SettingsManager(final String name) {
			this.name = name;
		}

		public void add(final String key, final JComponent component) {
			pairs.add(new Pair(key, component));
		}

		public void load() {
			try {
				final File file = new File(name);
				if (!file.exists()) {
					return;
				}
				final FileReader rd = new FileReader(file);
				final Properties prop = new Properties();
				prop.load(rd);
				for (final Pair pair : pairs) {
					final String value = prop.getProperty(pair.key);
					if (value == null) {
						continue;
					}
					if (pair.component instanceof JComboBox) {
						((JComboBox) pair.component).setSelectedIndex(Integer
								.parseInt(value));
					} else if (pair.component instanceof JCheckBox) {
						((JCheckBox) pair.component).setSelected(Boolean
								.parseBoolean(value));
					} else if (pair.component instanceof JTextField) {
						((JTextField) pair.component).setText(value);
					} else if (pair.component instanceof JTextArea) {
						((JTextArea) pair.component).setText(value);
					} else if (pair.component instanceof JRadioButton) {
						((JRadioButton) pair.component).setSelected(Boolean
								.parseBoolean(value));
					}
				}
				rd.close();
			} catch (final Exception e) {
			}
		}

		public void save() {
			try {
				final File file = new File(name);
				final FileWriter wr = new FileWriter(file);
				final Properties prop = new Properties();
				for (final Pair pair : pairs) {
					String value = "";
					if (pair.component instanceof JComboBox) {
						value = Integer.toString(((JComboBox) pair.component)
								.getSelectedIndex());
					} else if (pair.component instanceof JCheckBox) {
						value = Boolean.toString(((JCheckBox) pair.component)
								.isSelected());
					} else if (pair.component instanceof JTextField) {
						value = ((JTextField) pair.component).getText();
					} else if (pair.component instanceof JTextArea) {
						value = ((JTextArea) pair.component).getText();
					} else if (pair.component instanceof JRadioButton) {
						value = Boolean
								.toString(((JRadioButton) pair.component)
										.isSelected());
					}
					prop.setProperty(pair.key, value);
				}
				prop.store(
						wr,
						"SettingsManager by NoEffex, edited by icnhzabot. Feel free to use with credits.");
				wr.close();
			} catch (final Exception e) {
			}
		}
	}

	/*
	 * For Antiban purposes; Less botlike if mouse isn't always moved using the
	 * same spline-generating algorithm
	 */
	private class splines {

		/*
		 * Generates controls which are evenly spaced, not completely random.
		 * 
		 * Partially copied from MouseHandler.java and edited
		 */
		private final Random rand = new Random();

		private waypoint[] adaptivemids(final waypoint[] points1) {
			int i = 0;
			final ArrayList<waypoint> points = new ArrayList<waypoint>(
					Arrays.asList(points1));
			while (i < points.size() - 1) {
				final waypoint a = points.get(i++);
				final waypoint b = points.get(i);
				if (Math.abs(a.x - b.x) > 1 || Math.abs(a.y - b.y) > 1) {
					if (Math.abs(a.x - b.x) != 0) {
						final double slope = (double) (a.y - b.y)
								/ (double) (a.x - b.x);
						final double incpt = a.y - slope * a.x;
						for (double c = a.x < b.x ? a.x + 1 : b.x - 1; a.x < b.x ? c < b.x
								: c > a.x; c += a.x < b.x ? 1 : -1) {
							points.add(
									i++,
									new waypoint(c, Math.round(incpt + slope
											* c)));
						}
					} else {
						for (double c = a.y < b.y ? a.y + 1 : b.y - 1; a.y < b.y ? c < b.y
								: c > a.y; c += a.y < b.y ? 1 : -1) {
							points.add(i++, new waypoint(a.x, c));
						}
					}
				}
			}
			return points.toArray(new waypoint[points.size()]);
		}

		public waypoint[] applyDynamism(final waypoint[] spline,
				final int msForMove, final int msPerMove) {
			final int numPoints = spline.length;
			final double msPerPoint = (double) msForMove / (double) numPoints;
			final double undistStep = msPerMove / msPerPoint;
			final int steps = (int) Math.floor(numPoints / undistStep);
			final waypoint[] result = new waypoint[steps];
			final double[] gaussValues = gaussTable(result.length);
			double currentPercent = 0;
			for (int i = 0; i < steps; i++) {
				currentPercent += gaussValues[i];
				final int nextIndex = (int) Math.floor(numPoints
						* currentPercent);
				if (nextIndex < numPoints) {
					result[i] = spline[nextIndex];
				} else {
					result[i] = spline[numPoints - 1];
				}
			}
			if (currentPercent < 1D) {
				result[steps - 1] = spline[numPoints - 1];
			}
			return result;
		}

		private Point[] clean(final ArrayList<waypoint> points) {
			final Vector<Point> cleaned = new Vector<Point>();
			for (final waypoint p : points) {
				try {
					if (!p.topoint().equals(cleaned.lastElement())) {
						cleaned.add(p.topoint());
					}
				} catch (final Exception e) {
					cleaned.add(p.topoint());
				}
			}
			return cleaned.toArray(new Point[cleaned.size()]);
		}

		private double gaussian(double t) {
			t = 10D * t - 5D;
			return 1D / (Math.sqrt(5D) * Math.sqrt(2D * Math.PI))
					* Math.exp(-t * t / 20D);
		}

		private double[] gaussTable(final int steps) {
			final double[] table = new double[steps];
			final double step = 1D / steps;
			double sum = 0;
			for (int i = 0; i < steps; i++) {
				sum += gaussian(i * step);
			}
			for (int i = 0; i < steps; i++) {
				table[i] = gaussian(i * step) / sum;
			}
			return table;
		}

		private waypoint[] genBezier(final Point[] controls) {
			final Point[] coordlist = controls;
			double x1, x2, y1, y2;
			x1 = coordlist[0].x;
			y1 = coordlist[0].y;
			final ArrayList<waypoint> points = new ArrayList<waypoint>();
			points.add(new waypoint(x1, y1));
			for (double t = 0; t <= 1; t += 0.01) {
				/*
				 * BERNSTEIN POLYNOMINALS.
				 * 
				 * Check out
				 * http://math.fullerton.edu/mathews/n2003/BezierCurveMod.html
				 */
				x2 = coordlist[0].x
						+ t
						* (-coordlist[0].x * 3 + t
								* (3 * coordlist[0].x - coordlist[0].x * t))
						+ t
						* (3 * coordlist[1].x + t
								* (-6 * coordlist[1].x + coordlist[1].x * 3 * t))
						+ t * t * (coordlist[2].x * 3 - coordlist[2].x * 3 * t)
						+ coordlist[3].x * t * t * t;
				y2 = coordlist[0].y
						+ t
						* (-coordlist[0].y * 3 + t
								* (3 * coordlist[0].y - coordlist[0].y * t))
						+ t
						* (3 * coordlist[1].y + t
								* (-6 * coordlist[1].y + coordlist[1].y * 3 * t))
						+ t * t * (coordlist[2].y * 3 - coordlist[2].y * 3 * t)
						+ coordlist[3].y * t * t * t;
				points.add(new waypoint(x2, y2));
			}
			if (!points.get(points.size() - 1).equals(
					new waypoint(coordlist[3].getX(), coordlist[3].getY()))) {
				points.add(new waypoint(coordlist[3].getX(), coordlist[3]
						.getY()));
			}
			return adaptivemids(points.toArray(new waypoint[points.size()]));
		}

		private Point[] genControls(final Point start, final Point end) {
			Methods.random(1, 8);
			final ArrayList<Point> controls = new ArrayList<Point>();
			controls.add(start);
			/* Cubic Bezier */
			if (Methods.random(1, 2) == 1) {
				int x = Methods.random(0, game.getWidth());
				int y = Methods.random(0, game.getHeight());
				final Point cp1 = new Point(x, y);
				Point cp2 = new Point(x, y);
				int loops = 0;
				while (calc.distanceBetween(cp1, cp2) <= 200 && loops++ < 200) {
					if (loops >= 200) {
						break;
					}
					x = Methods.random(0, game.getWidth() + 200);
					x = x - Methods.random(0, 200);
					y = Methods.random(0, game.getHeight() + 200);
					y = y - Methods.random(0, 200);
					cp2 = new Point(x, y);
				}
				if (loops >= 200) {
					cp2 = new Point(cp2.y, cp2.x);
				}
				if (Methods.random(1, 2) == Methods.random(1, 2)) {
					controls.add(cp1);
					controls.add(cp2);
				} else {
					controls.add(cp2);
					controls.add(cp1);
				}
				controls.add(end);
				return controls.toArray(new Point[controls.size()]);
			} else {
				final Point[] controls2 = genRelativeControls(start, end, 4);
				return controls2;

			}
		}

		private Point[] genRelativeControls(final Point start, final Point end,
				final int numofcontrols) {
			if (numofcontrols < 3 || numofcontrols > 4) { // Can't make a curve
															// with 2 points.
															// More than 4
															// points is not
															// supported.
				return null;
			}
			calc.distanceBetween(start, end);
			final double angle = Math.atan2(end.y - start.y, end.x - start.x);
			final ArrayList<Point> result = new ArrayList<Point>();
			result.add(start);
			int ctrlSpacing = Methods.random(70, 80);
			for (int i = 1; i < numofcontrols; i++) {
				ctrlSpacing = Methods.random(70, 80);
				final double radius = ctrlSpacing * i;
				final Point cur = new Point((int) (start.x + radius
						* Math.cos(angle)), (int) (start.y + radius
						* Math.sin(angle)));
				double percent = 1D - (double) (i - 1) / (double) numofcontrols;
				percent = percent > 0.5 ? percent - 0.5 : percent;
				percent += 0.25;
				final int curVariance = (int) (Methods.random(115, 130) * percent);
				cur.setLocation(
						(int) (cur.y + curVariance * 2 * rand.nextDouble() - curVariance),
						(int) (cur.x + curVariance * 2 * rand.nextDouble() - curVariance));
				result.add(cur);
			}
			if (numofcontrols == 3) {
				result.add(result.get(result.size() - 1));
			}
			result.add(end);
			return result.toArray(new Point[result.size()]);
		}

		private void hop(final Point hopdest) {
			final Point mloc = mouse.getLocation();
			final Point[] controls = MouseHandler.generateControls(mloc.x,
					mloc.y, hopdest.x, hopdest.y, 50, 100);
			final Point[] spline = MouseHandler.generateSpline(controls);
			final ArrayList<Point> path = new ArrayList<Point>();
			path.addAll(Arrays.asList(spline));
			path.add(hopdest);
			for (final Point onPath : path) {
				mouse.hop(onPath);
			}
		}

		private void mouseTo(final Point dest) {
			final waypoint[] path = genBezier(genControls(mouse.getLocation(),
					dest));
			final int x1 = mouse.getLocation().x, y1 = mouse.getLocation().y, x2 = dest.x, y2 = dest.y;
			final int timeToMove = (int) MouseHandler.fittsLaw(
					Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2)), 10);
			final waypoint[] curve1 = applyDynamism(path, timeToMove,
					mouse.getSpeed() + 1);
			final Point[] curve = clean(new ArrayList<waypoint>(
					Arrays.asList(curve1)));
			for (final Point curr : curve) {
				hop(curr);
				try {
					Thread.sleep(Methods.random(1, 3) >= 2 ? Math.max(1,
							mouse.getSpeed() - 2 + rand.nextInt(4)) : Math.max(
							1, mouse.getSpeed() - 3 + rand.nextInt(6)));
				} catch (final InterruptedException ie) {
				}
			}
			if (!mouse.getLocation().equals(dest)) {
				mouseMove(dest);
				Methods.sleep(Methods.random(20, 60));
				if (!mouse.getLocation().equals(dest)) {
					hop(dest);
					Methods.sleep(Methods.random(20, 60));
					if (!mouse.getLocation().equals(dest)) {
						mouse.hop(dest);
					}
				}
			}
		}
	}

	/* STATES */
	private enum States {

		OPENBANK, BANK, SOFTEN, TOBANK, FROMBANK, WAITFORINVENTORY, CHILL, REWARDSBOX
	}

	private class Timekeeper {

		long startTime = System.currentTimeMillis();
		long pausedTime = 0;
		long pausedTemp = 0;
		int state = 0;

		public long calcPerHour(final double i) {
			final double elapsed_millis = getMillis();
			return (long) (i / elapsed_millis * 3600000);
		}

		public long calcPerHour(final long i) {
			return calcPerHour((double) i);
		}

		public long getHours() {
			return getMinutes() / 60;
		}

		public long getMillis() {
			if (state == 1) {
				return System.currentTimeMillis() - startTime - getPausedTime();
			} else {
				return System.currentTimeMillis() - startTime - pausedTime;
			}
		}

		public long getMinutes() {
			return getSeconds() / 60;
		}

		private long getPausedTime() {
			if (pausedTemp != 0) {
				return System.currentTimeMillis() - pausedTemp;
			} else {
				return 0;
			}
		}

		public String getRuntimeString() {
			final StringBuilder builder = new StringBuilder();
			final long HoursRan = getHours();
			long MinutesRan = getMinutes();
			long SecondsRan = getSeconds();
			MinutesRan = MinutesRan % 60;
			SecondsRan = SecondsRan % 60;
			if (HoursRan < 10) {
				builder.append("0");
			}
			builder.append(HoursRan);
			builder.append(":");
			if (MinutesRan < 10) {
				builder.append("0");
			}
			builder.append(MinutesRan);
			builder.append(":");
			if (SecondsRan < 10) {
				builder.append("0");
			}
			builder.append(SecondsRan);
			return builder.toString();
		}

		public long getSeconds() {
			return getMillis() / 1000;
		}

		public int getState() {
			return state;
		}

		public void setPaused() {
			state = 1;
			pausedTemp = System.currentTimeMillis();
		}

		private void setPausedTime(final long setTime) {
			pausedTime += setTime;
		}

		public void setResumed() {
			state = 0;
			setPausedTime(getPausedTime());
			pausedTemp = 0;
		}
	}

	private class Walking {

		private class pathfinder {
			/*
			 * This was copied from RSLocalPath's A* methods. I copied because
			 * The methods are deprecated and only work for generating paths
			 * from the current player's location. All credits to Jacmob forthis
			 * class.
			 */

			protected class Node {

				public int x, y;
				public Node prev;
				public double g, f;

				public Node(final int x, final int y) {
					this.x = x;
					this.y = y;
					g = f = 0;
				}

				@Override
				public boolean equals(final Object o) {
					if (o instanceof Node) {
						final Node n = (Node) o;
						return x == n.x && y == n.y;
					}
					return false;
				}

				@Override
				public int hashCode() {
					return x << 4 | y;
				}

				public RSTile toRSTile(final int baseX, final int baseY) {
					return new RSTile(x + baseX, y + baseY);
				}

				@Override
				public String toString() {
					return "(" + x + "," + y + ")";
				}
			}

			public static final int WALL_NORTH_WEST = 0x1;
			public static final int WALL_NORTH = 0x2;
			public static final int WALL_NORTH_EAST = 0x4;
			public static final int WALL_EAST = 0x8;
			public static final int WALL_SOUTH_EAST = 0x10;
			public static final int WALL_SOUTH = 0x20;
			public static final int WALL_SOUTH_WEST = 0x40;
			public static final int WALL_WEST = 0x80;
			public static final int BLOCKED = 0x100;
			protected RSTile base;
			protected int[][] flags;

			protected int offX, offY;

			private double dist(final Node start, final Node end) {
				if (start.x != end.x && start.y != end.y) {
					return 1.41421356;
				} else {
					return 1.0;
				}
			}

			protected RSTile[] findPath(final RSTile start, final RSTile end) {
				return findPath(start, end, false);
			}

			protected RSTile[] findPath(final RSTile start, final RSTile end,
					boolean remote) {
				base = game.getMapBase();
				final int base_x = base.getX(), base_y = base.getY();
				final int curr_x = start.getX() - base_x, curr_y = start.getY()
						- base_y;
				int dest_x = end.getX() - base_x, dest_y = end.getY() - base_y;
				final int plane = game.getPlane();
				flags = walking.getCollisionFlags(plane);
				final RSTile offset = walking.getCollisionOffset(plane);
				offX = offset.getX();
				offY = offset.getY();
				if (flags == null || curr_x < 0 || curr_y < 0
						|| curr_x >= flags.length || curr_y >= flags.length) {
					return null;
				} else if (dest_x < 0 || dest_y < 0 || dest_x >= flags.length
						|| dest_y >= flags.length) {
					remote = true;
					if (dest_x < 0) {
						dest_x = 0;
					} else if (dest_x >= flags.length) {
						dest_x = flags.length - 1;
					}
					if (dest_y < 0) {
						dest_y = 0;
					} else if (dest_y >= flags.length) {
						dest_y = flags.length - 1;
					}
				}

				// structs
				final HashSet<Node> open = new HashSet<Node>();
				final HashSet<Node> closed = new HashSet<Node>();
				Node curr = new Node(curr_x, curr_y);
				final Node dest = new Node(dest_x, dest_y);

				curr.f = heuristic(curr, dest);
				open.add(curr);

				// search
				while (!open.isEmpty()) {
					curr = lowest_f(open);
					if (curr.equals(dest)) {
						// reconstruct from pred tree
						return path(curr, base_x, base_y);
					}
					open.remove(curr);
					closed.add(curr);
					for (final Node next : successors(curr)) {
						if (!closed.contains(next)) {
							final double t = curr.g + dist(curr, next);
							boolean use_t = false;
							if (!open.contains(next)) {
								open.add(next);
								use_t = true;
							} else if (t < next.g) {
								use_t = true;
							}
							if (use_t) {
								next.prev = curr;
								next.g = t;
								next.f = t + heuristic(next, dest);
							}
						}
					}
				}

				// no path
				if (!remote || calc.distanceTo(end) < 10) {
					return null;
				}
				return findPath(start, pull(end));
			}

			private double heuristic(final Node start, final Node end) {
				double dx = start.x - end.x;
				double dy = start.y - end.y;
				if (dx < 0) {
					dx = -dx;
				}
				if (dy < 0) {
					dy = -dy;
				}
				return dx < dy ? dy : dx;
				// double diagonal = dx > dy ? dy : dx;
				// double manhattan = dx + dy;
				// return 1.41421356 * diagonal + (manhattan - 2 * diagonal);
			}

			private Node lowest_f(final Set<Node> open) {
				Node best = null;
				for (final Node t : open) {
					if (best == null || t.f < best.f) {
						best = t;
					}
				}
				return best;
			}

			private RSTile[] path(final Node end, final int base_x,
					final int base_y) {
				final LinkedList<RSTile> path = new LinkedList<RSTile>();
				Node p = end;
				while (p != null) {
					path.addFirst(p.toRSTile(base_x, base_y));
					p = p.prev;
				}
				return path.toArray(new RSTile[path.size()]);
			}

			private RSTile pull(final RSTile tile) {
				final RSTile p = getMyPlayer().getLocation();
				int x = tile.getX(), y = tile.getY();
				if (p.getX() < x) {
					x -= 2;
				} else if (p.getX() > x) {
					x += 2;
				}
				if (p.getY() < y) {
					y -= 2;
				} else if (p.getY() > y) {
					y += 2;
				}
				return new RSTile(x, y);
			}

			private java.util.List<Node> successors(final Node t) {
				final LinkedList<Node> tiles = new LinkedList<Node>();
				final int x = t.x, y = t.y;
				final int f_x = x - offX, f_y = y - offY;
				final int here = flags[f_x][f_y];
				final int upper = flags.length - 1;
				if (f_y > 0 && (here & pathfinder.WALL_SOUTH) == 0
						&& (flags[f_x][f_y - 1] & pathfinder.BLOCKED) == 0) {
					tiles.add(new Node(x, y - 1));
				}
				if (f_x > 0 && (here & pathfinder.WALL_WEST) == 0
						&& (flags[f_x - 1][f_y] & pathfinder.BLOCKED) == 0) {
					tiles.add(new Node(x - 1, y));
				}
				if (f_y < upper && (here & pathfinder.WALL_NORTH) == 0
						&& (flags[f_x][f_y + 1] & pathfinder.BLOCKED) == 0) {
					tiles.add(new Node(x, y + 1));
				}
				if (f_x < upper && (here & pathfinder.WALL_EAST) == 0
						&& (flags[f_x + 1][f_y] & pathfinder.BLOCKED) == 0) {
					tiles.add(new Node(x + 1, y));
				}
				if (f_x > 0
						&& f_y > 0
						&& (here & (pathfinder.WALL_SOUTH_WEST
								| pathfinder.WALL_SOUTH | pathfinder.WALL_WEST)) == 0
						&& (flags[f_x - 1][f_y - 1] & pathfinder.BLOCKED) == 0
						&& (flags[f_x][f_y - 1] & (pathfinder.BLOCKED | pathfinder.WALL_WEST)) == 0
						&& (flags[f_x - 1][f_y] & (pathfinder.BLOCKED | pathfinder.WALL_SOUTH)) == 0) {
					tiles.add(new Node(x - 1, y - 1));
				}
				if (f_x > 0
						&& f_y < upper
						&& (here & (pathfinder.WALL_NORTH_WEST
								| pathfinder.WALL_NORTH | pathfinder.WALL_WEST)) == 0
						&& (flags[f_x - 1][f_y + 1] & pathfinder.BLOCKED) == 0
						&& (flags[f_x][f_y + 1] & (pathfinder.BLOCKED | pathfinder.WALL_WEST)) == 0
						&& (flags[f_x - 1][f_y] & (pathfinder.BLOCKED | pathfinder.WALL_NORTH)) == 0) {
					tiles.add(new Node(x - 1, y + 1));
				}
				if (f_x < upper
						&& f_y > 0
						&& (here & (pathfinder.WALL_SOUTH_EAST
								| pathfinder.WALL_SOUTH | pathfinder.WALL_EAST)) == 0
						&& (flags[f_x + 1][f_y - 1] & pathfinder.BLOCKED) == 0
						&& (flags[f_x][f_y - 1] & (pathfinder.BLOCKED | pathfinder.WALL_EAST)) == 0
						&& (flags[f_x + 1][f_y] & (pathfinder.BLOCKED | pathfinder.WALL_SOUTH)) == 0) {
					tiles.add(new Node(x + 1, y - 1));
				}
				if (f_x > 0
						&& f_y < upper
						&& (here & (pathfinder.WALL_NORTH_EAST
								| pathfinder.WALL_NORTH | pathfinder.WALL_EAST)) == 0
						&& (flags[f_x + 1][f_y + 1] & pathfinder.BLOCKED) == 0
						&& (flags[f_x][f_y + 1] & (pathfinder.BLOCKED | pathfinder.WALL_EAST)) == 0
						&& (flags[f_x + 1][f_y] & (pathfinder.BLOCKED | pathfinder.WALL_NORTH)) == 0) {
					tiles.add(new Node(x + 1, y + 1));
				}
				return tiles;
			}
		}

		private class utils {

			private RSTile[] clean(final RSTile[] path) {
				final ArrayList<RSTile> cleanpath = new ArrayList<RSTile>();
				final ArrayList<Integer> badidx = new ArrayList<Integer>();
				for (int i = 0; i < path.length; i++) {
					try {
						if (!badidx.contains(i)) {
							cleanpath.add(path[i]);
							badidx.add(i);
						}
						if (cleanpath.contains(path[i + 1])) {
							badidx.add(i + 1);
						}
						if (cleanpath.contains(path[i + 2])) {
							badidx.add(i + 2);
						}
					} catch (final Exception e) {
						continue;
					}
				}
				if (!cleanpath.get(cleanpath.size() - 1).equals(
						path[path.length - 1])) {
					cleanpath.add(path[path.length - 1]);
				}
				final RSTile[] cleaned = cleanpath.toArray(new RSTile[cleanpath
						.size()]);
				return cleaned != null ? cleaned : path;
			}

			public RSTile[] generatePath(final RSTile destination,
					final int randomize) {
				try {
					RSTile[] path = pfinder.findPath(getMyPlayer()
							.getLocation(), destination);
					if (randomize != 0) {
						path = randPath(path, randomize);
					}
					path = lengthenPath(path);
					path = clean(path);
					final ArrayList<RSTile> tiles = new ArrayList<RSTile>(
							Arrays.asList(path));
					if (!tiles.get(tiles.size() - 1).equals(destination)) {
						tiles.add(destination);
					}
					return tiles.toArray(new RSTile[tiles.size()]);
				} catch (final Exception e) {
					return null;
				}
			}

			private RSTile[] lengthenPath(final RSTile[] path) {
				final ArrayList<RSTile> tiles = new ArrayList<RSTile>();
				try {
					for (int i = 0; i < path.length; i++) {
						final RSTile current = path[i];
						if (current == null) {
							continue;
						}
						tiles.add(current);
						final RSTile next = path[i + 1];
						if (next != null && walking.isLocal(current)
								&& walking.isLocal(next)) {
							RSTile[] pathTo = pfinder.findPath(current, next,
									false);
							if (pathTo != null) {
								tiles.addAll(Arrays.asList(pathTo));
							} else {
								pathTo = pfinder.findPath(current, next, false);
								if (pathTo != null) {
									tiles.addAll(Arrays.asList(pathTo));
								}
							}
						}
					}
				} catch (final ArrayIndexOutOfBoundsException aioe) {
				}
				if (!tiles.get(tiles.size() - 1).equals(path[path.length - 1])) {
					tiles.add(path[path.length - 1]);
				}
				return tiles.toArray(new RSTile[tiles.size()]);
			}

			private RSTile[] randPath(final RSTile[] path, final int randomize) {
				final ArrayList<RSTile> randomized = new ArrayList<RSTile>();
				final RSTile start = path[0];
				final RSTile finish = path[path.length - 1];
				randomized.add(start);
				for (final RSTile current : path) {
					int x = current.getX(), y = current.getY();
					final int z = current.getZ();
					if (Methods.random(1, 2) == 1) {
						x -= Methods.random(0, randomize);
					} else {
						x += Methods.random(0, randomize);
					}
					if (Methods.random(1, 2) == 1) {
						y -= Methods.random(0, randomize);
					} else {
						y += Methods.random(0, randomize);
					}
					randomized.add(new RSTile(x, y, z));
				}
				randomized.add(finish);
				final RSTile[] randPath = randomized
						.toArray(new RSTile[randomized.size()]);
				return lengthenPath(randPath);
			}
		}

		private class walker {

			public boolean walkTo(final RSTile destination,
					final boolean randomize) {
				return walkTo(destination, randomize ? 2 : 0);
			}

			public boolean walkTo(final RSTile destination, final int randomize) {
				long timeout = 9000;
				final long walktimeout = 10000;
				final long runtimeout = 9000;
				int loops = 0;
				final long starttime = System.currentTimeMillis();
				canAB = false;
				RSTile[] path = walkutils.generatePath(destination, randomize);
				if (path == null) {
					return false;
				}
				RSTilePath tp = walking.newTilePath(path);
				while (tp.traverse() && isRunning() && isActive()
						&& !isPaused() && calc.distanceTo(destination) > 3) {
					drawPath = path;
					if (path == null) {
						path = walkutils.generatePath(destination, randomize);
						if (path == null) {
							return false;
						}
					}
					drawPath = path;
					tp = walking.newTilePath(path);
					loops++;
					Methods.sleep(Methods.random(60, 80));
					canAB = true;
					if (!getMyPlayer().isMoving()) {
						path = walkutils.generatePath(destination, randomize);
						drawPath = path;
						tp = walking.newTilePath(path);
						Methods.sleep(200);
						if (!getMyPlayer().isMoving()) {
							tp.traverse();
							Methods.sleep(300);
							if (!getMyPlayer().isMoving()) {
								canAB = true;
								return calc.distanceTo(destination) <= 3;
							}
						}
					}
					if (walking.isRunEnabled()) {
						timeout = runtimeout;
					} else {
						timeout = walktimeout;
					}
					if (System.currentTimeMillis() - starttime > timeout
							* (tp.toArray().length - 1)) {
						canAB = true;
						return calc.distanceTo(destination) <= 3;
					}
					if (loops >= 2) {
						path = walkutils.generatePath(destination, randomize);
						drawPath = path;
						tp = walking.newTilePath(path);
						loops = 0;
					}
					canAB = false;
					Methods.sleep(Methods.random(100, 200));
					drawPath = path;
				}
				canAB = true;
				return calc.distanceTo(destination) <= 3;
			}
		}

		private final utils walkutils = new utils();

		private final walker Walker = new walker();

		private final pathfinder pfinder = new pathfinder();

		public boolean walkTo(final RSTile destination, final boolean randomize) {
			canAB = false;
			if (Walker.walkTo(destination, randomize)) {
				canAB = true;
				return true;
			} else {
				canAB = true;
				return false;
			}
		}
	}

	/* GUI */
	private class waypoint {

		public double x, y;

		public waypoint(final double x1, final double y1) {
			x = x1;
			y = y1;
		}

		public Point topoint() {
			return new Point((int) x, (int) y);
		}
	}

	private GUI gui = null;
	private InventoryListener inv;

	private Thread invThread;
	private Locations loc;
	private boolean running;
	private int failCounter;
	private int numDone;
	private final int softClay = 1761, clay = 434;
	private int softClayPrice = -1, clayPrice = -1;
	private final Timekeeper timekeeper = new Timekeeper();
	private antiban Antiban;
	private CameraMovementChecker cameraCheck;
	private MouseMovementChecker mouseCheck;

	private final Camera cameraHandler = new Camera();

	private final splines alternateSplineGen = new splines();
	private boolean canAB = false;
	private long mousemovet = System.currentTimeMillis();
	private final Walking walker = new Walking();
	private RSTile[] drawPath = null;
	private SettingsManager sm = new SettingsManager(
			GlobalConfiguration.Paths.getSettingsDirectory() + File.separator
					+ "Aut0ClaySoftener.SETTINGS");
	BufferedImage nmouse = null;
	BufferedImage nclicked = null;
	/* GUI Options */
	private boolean clicks = true;

	private boolean defmouse = true;

	private boolean rotmouse = true;

	private boolean showmouse = true;

	private boolean paths = false;

	private boolean stats = true;

	private boolean beziers = true;

	private boolean shortsplines = true;

	private int numABThreads = 2;

	private int mouseLo = 5;

	/* Defined Methods */

	private int mouseHi = 10;

	private final RenderingHints antialiasing = new RenderingHints(
			RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

	private final BasicStroke stroke = new BasicStroke(1);

	private final Font calibri20 = new Font("Calibri", 0, 20);

	private final Color color1 = new Color(0, 0, 0);

	private final Color color2 = new Color(0, 0, 255, 108);

	private final Color blue = Color.BLUE;

	private final Color cyan = Color.CYAN;

	private final Color darkCyan = Color.CYAN.darker();

	private boolean atBank() {
		return loc.getBankArea().contains(players.getMyPlayer().getLocation());
	}

	private boolean atObject() {
		return loc.getObjectArea()
				.contains(players.getMyPlayer().getLocation());
	}

	private boolean clickmenuidx(final int idx) {
		final Point beginloc = mouse.getLocation();
		if (idx == 0) {
			mouse.click(true);
			return true;
		}
		if (menu.isOpen()) {
			if (menu.getItems().length < idx) {
				return menu.doAction("Cancel");
			}
		} else {
			mouse.click(false);
			for (int i = 0; i < 100 && !menu.isOpen(); i++) {
				Methods.sleep(20);
			}
		}
		String[] items = menu.getItems();
		if (idx >= items.length) {
			return false;
		}
		if (!menu.clickIndex(idx)) {
			mouseMove(beginloc);
			mouse.click(beginloc, false);
			for (int i = 0; i < 100 && !menu.isOpen(); i++) {
				Methods.sleep(20);
			}
			items = menu.getItems();
			int index = 0;
			for (final String current : items) {
				if (menu.getIndex(current) == idx) {
					break;
				}
				index++;
			}
			if (index >= items.length) {
				return false;
			}
			if (idx != index) {
				return false;
			}
			return menu.clickIndex(index);
		}
		return true;
	}

	private void createAndWaitforGUI() {
		if (sm == null) {
			sm = new SettingsManager(
					GlobalConfiguration.Paths.getSettingsDirectory()
							+ File.separator + "Aut0ClaySoftener.dat");
		}
		if (SwingUtilities.isEventDispatchThread()) {
			gui = new GUI(sm);
			gui.setVisible(true);
		} else {
			try {
				SwingUtilities.invokeAndWait(new Runnable() {

					@Override
					public void run() {
						gui = new GUI(sm);
						gui.setVisible(true);
					}
				});
			} catch (final InvocationTargetException ite) {
			} catch (final InterruptedException ie) {
			}
		}
		Methods.sleep(100);
		while (gui.isVisible()) {
			Methods.sleep(100);
		}
		if (!gui.isCanceled()) {
			sm.save();
		}
	}

	/* Classes */

	private boolean doAction(final RSObject object, final String doaction,
			final String item, final String obj) {
		canAB = false;
		final String action = doaction + " " + item + " -> " + obj;
		Point loc = getLocation(object);
		int rand = Methods.random(10, 13);
		while (--rand > 0 && !menu.contains(action)) {
			mouseMove(loc);
			Methods.sleep(Methods.random(30, 70));
			if (menu.contains(action)) {
				Methods.sleep(Methods.random(30, 70));
				if (menu.contains(action)) {
					Methods.sleep(Methods.random(30, 70));
					if (menu.contains(action)) {
						break;
					}
				}
			}
			loc = getLocation(object);
		}
		if (menu.contains(action)) {
			return doMenuAction(action);
		} else {
			final RSModel model = object.getModel();
			Point move;
			if (model == null) {
				move = getLocation(object);
			} else {
				move = model.getCentralPoint();
			}
			mouseMove(move);
			Methods.sleep(Methods.random(40, 70));
			if (!doMenuAction(action)) {
				Methods.sleep(Methods.random(30, 70));
				loc = getLocation(object);
				mouse.move(loc);
				Methods.sleep(Methods.random(30, 60));
				return menu.doAction(action);
			} else {
				canAB = true;
				return true;
			}
		}
	}

	private boolean doMenuAction(final String action) {
		if (menu.isOpen()) {
			if (!menu.contains(action)) {
				menu.doAction("Cancel");
			}
		}
		final String[] items = menu.getItems();
		if (items[0].contains(action)) {
			return clickmenuidx(0);
		}
		boolean contains = false;
		int index = 0;
		for (final String check : items) {
			if (check.contains(action)) {
				contains = true;
				break;
			} else if (items[index].contains(action)) {
				contains = true;
				break;
			}
			index++;
		}
		if (!contains || index < items.length) {
			mouse.click(false);
			for (int i = 0; i < 100 && !menu.isOpen(); i++) {
				Methods.sleep(20);
			}
			try {
				final int idx = menu.getIndex(action);
				if (idx < items.length) {
					return clickmenuidx(idx);
				} else {
					return false;
				}
			} catch (final ArrayIndexOutOfBoundsException aioe) {
				return false;
			}
		}
		return clickmenuidx(index);
	}

	private void drawPath(final Graphics2D g) {
		g.setColor(Color.GREEN.brighter());
		try {
			final RSTile[] path = drawPath;
			if (path != null) {
				Point last = tileToMap(path[0]);
				for (final RSTile curr : path) {
					final Point onmap = tileToMap(curr);
					((Graphics2D) g).drawLine(last.x, last.y, onmap.x, onmap.y);
					last = onmap;
				}
			}
		} catch (final Exception e) {
		}
	}

	private RSItem getHardClay() {
		final RSItem[] allClay = inventory.getItems(clay);
		final int rand = Methods.random(0, allClay.length - 1);
		RSItem randClay = allClay[rand];
		if (randClay == null) {
			randClay = inventory.getItem(clay);
		}
		return randClay;
	}

	private Point getLocation(final RSObject obj) {
		if (obj == null) {
			return null;
		}
		RSModel model = obj.getModel();
		if (model == null) {
			model = obj.getModel();
			if (model == null) {
				return calc.tileToScreen(obj.getLocation(), 0.5, 0.5, 0);
			}
		}
		Point toreturn = pointOnScreen(model);
		if (toreturn == null) {
			toreturn = model.getPointOnScreen();
			if (toreturn == null) {
				final Point[] points = model.getPoints();
				toreturn = points[points.length / 2];
			}
		}
		return toreturn;
	}

	private double getRotate(final int ticks) {
		return System.currentTimeMillis() % (360 * ticks) / ticks;
	}

	private States getState() {
		/* START: Super-Massive getState() method! */
		canAB = true;
		if (isPaused() || !game.isLoggedIn() || getMyPlayer().isMoving()
				|| !isRunning() || !isActive()) {
			return States.CHILL;
		}
		if (inventory.contains(softClay)) {
			if (atBank()) {
				return !bank.isOpen() ? States.OPENBANK : States.BANK;
			} else if (atObject()) {
				if (inventory.contains(clay)) {
					if (inv.idle() >= 60 || !inventory.contains(softClay)) {
						return States.SOFTEN;
					} else if (inv.idle() < 60) {
						return States.WAITFORINVENTORY;
					}
				} else if (inventory.getCount(clay) < 1) {
					return States.TOBANK;
				}
			} else if (!atObject() && !atBank()) {
				if (inventory.contains(softClay)) {
					return States.TOBANK;
				} else if (inventory.contains(clay)
						&& !inventory.contains(softClay)) {
					return States.FROMBANK;
				} else {
					return States.TOBANK;
				}
			}
		} else if (inventory.contains(clay)) {
			if (atBank()) {
				if (inventory.contains(softClay)) {
					return !bank.isOpen() ? States.OPENBANK : States.BANK;
				} else if (inventory.contains(clay)) {
					return States.FROMBANK;
				} else if (!inventory.contains(softClay)
						&& !inventory.contains(clay)) {
					return !bank.isOpen() ? States.OPENBANK : States.BANK;
				}
			} else if (atObject()) {
				if (inventory.contains(clay)) {
					if (inv.idle() > 60 || !inventory.contains(softClay)) {
						return States.SOFTEN;
					} else if (inv.idle() < 60) {
						return States.WAITFORINVENTORY;
					}
				} else if (inventory.getCount(clay) < 1) {
					return States.TOBANK;
				}
			} else if (!atObject() && !atBank()) {
				if (inventory.contains(softClay)) {
					return States.TOBANK;
				} else if (inventory.contains(clay)
						&& !inventory.contains(softClay)) {
					return States.FROMBANK;
				} else {
					return States.TOBANK;
				}
			}
		} else if (!inventory.contains(clay) && !inventory.contains(softClay)) {
			if (atBank()) {
				return !bank.isOpen() ? States.OPENBANK : States.BANK;
			}
			return States.TOBANK;
		}
		if (atBank()) {
			if (inventory.contains(softClay)) {
				if (!bank.isOpen()) {
					return States.OPENBANK;
				} else {
					return States.BANK;
				}
			} else if (atBank() && inventory.contains(softClay)
					&& !inventory.contains(clay)) {
				if (!bank.isOpen()) {
					return States.OPENBANK;
				} else {
					return States.BANK;
				}
			} else if (atBank() && inventory.contains(clay)) {
				return States.FROMBANK;
			}
		}
		if (atObject()) {
			if (inventory.contains(clay)) {
				if (inv.idle() > 60 || !inventory.contains(softClay)) {
					return States.SOFTEN;
				} else if (inv.idle() < 60) {
					return States.WAITFORINVENTORY;
				}
			} else if (inventory.getCount(clay) < 1) {
				return States.TOBANK;
			}
		}
		if (!atBank() && !atObject()) {
			if (inventory.getCount(clay) >= 1) {
				return States.FROMBANK;
			} else if (!inventory.contains(clay)
					|| inventory.contains(softClay)) {
				return States.TOBANK;
			}
		}
		return States.CHILL;
	}

	/* LOOP */
	@Override
	public int loop() {
		canAB = true;
		if (isPaused() || !isRunning() || !isActive() || !game.isLoggedIn()
				|| game.isWelcomeScreen() || game.isLoginScreen()) {
			return 1;
		}
		try {
			mouse.setSpeed(Methods.random(mouseLo, mouseHi));
			final States state = getState();
			switch (state) {
			case OPENBANK:
				canAB = false;
				if (!bank.isOpen()) {
					if (!openBank()) {
						canAB = true;
						break;
					}
					for (int i = 0; i < 100 && !bank.isOpen(); i++) {
						Methods.sleep(20);
					}
					canAB = true;
					Methods.sleep(Methods.random(200, 320));
				}
				canAB = true;
				break;
			case BANK:
				if (inventory.contains(softClay)) {
					canAB = false;
					Methods.sleep(Methods.random(100, 195));
					bank.depositAll();
					for (int i = 0; i < 100 && !inventory.contains(softClay); i++) {
						Methods.sleep(20);
					}
					canAB = true;
					Methods.sleep(Methods.random(320, 450));
				}
				if (isPaused() || !isActive() || !isRunning()) {
					break;
				}
				if (bank.getCount(clay) > 28 && inventory.getCount(clay) <= 27) {
					canAB = false;
					withdraw(clay, 28);
					for (int i = 0; i < 100 && !inventory.contains(clay); i++) {
						Methods.sleep(20);
					}
					canAB = true;
				} else {
					if (bank.getCount(clay) <= 27
							&& inventory.getCount(clay) < 1) {
						running = false;
						canAB = false;
						inv.kill();
						Antiban.kill();
						log.severe("Out of clay to soften. Stopping script...");
						bank.close();
						stopScript(true);
						game.logout(false);
						Methods.sleep(800);
						stopScript();
					}
				}
				if (isPaused() || !isActive() || !isRunning()) {
					break;
				}
				if (bank.isOpen() && inventory.contains(clay)) {
					canAB = false;
					Methods.sleep(Methods.random(120, 210));
					bank.close();
					for (int i = 0; i > 100 && !bank.isOpen(); i++) {
						Methods.sleep(20);
					}
					canAB = true;
					break;
				}
				canAB = true;
				break;
			case SOFTEN:
				final boolean isWFally = loc instanceof Falador;
				canAB = false;
				Methods.sleep(Methods.random(800, 1000));
				if (interfaces.getComponent(905, 14).isValid()) {
					final Rectangle area = interfaces.getComponent(905, 14)
							.getArea();
					final int randx = (int) Methods.random(area.getMinX(),
							area.getMaxX());
					final int randy = (int) Methods.random(area.getMinY(),
							area.getMaxY());
					mouseMove(new Point(randx, randy));
					interfaces.getComponent(905, 14).doClick();
					Methods.sleep(Methods.random(400, 1000));
					if (Methods.random(1, 2) == Methods.random(1, 2)) {
						Antiban.antibanmouse();
					}
					canAB = true;
					inv.reset(0);
					failCounter = 0;
					Methods.sleep(Methods.random(230, 450));
					break;
				}
				if (isPaused() || !isRunning() || !isActive()) {
					break;
				}
				if (inv.idle() < 60) {
					canAB = true;
					if (!inventory.contains(clay)) {
						break;
					}
					Methods.sleep(Methods.random(173, 357));
					if (!inventory.contains(clay)) {
						break;
					}
					if (inv.idle() < 60) {
						return Methods.random(400, 700);
					}
				}
				if (inv.idle() >= 60 && inventory.contains(clay)
						&& !interfaces.getComponent(905, 14).isValid()) {
					final RSItem clayItem = getHardClay();
					if (loc.getRSObject() != null && clayItem != null
							&& inventory.contains(clay)) {
						canAB = false;
						if (!inventory.isItemSelected()) {
							final Rectangle area = clayItem.getComponent()
									.getArea();
							final int randx = (int) Methods.random(
									area.getMinX(), area.getMaxX());
							final int randy = (int) Methods.random(
									area.getMinY(), area.getMaxY());
							mouseMove(new Point(randx, randy));
							clayItem.doAction("Use");
							for (int i = 0; i < 100
									&& !inventory.isItemSelected(); i++) {
								Methods.sleep(20);
							}
							Methods.sleep(20);
						}
						if (isPaused() || !isActive() || !isRunning()) {
							break;
						}
						canAB = true;
						if (inventory.isItemSelected()
								&& inventory.getSelectedItem().getID() != clay) {
							final Rectangle area = inventory.getSelectedItem()
									.getComponent().getArea();
							final int randx = (int) Methods.random(
									area.getMinX(), area.getMaxX());
							final int randy = (int) Methods.random(
									area.getMinY(), area.getMaxY());
							canAB = false;
							mouseMove(new Point(randx, randy));
							inventory.getSelectedItem().doClick(true);
							if (isPaused() || !isActive() || !isRunning()) {
								break;
							}
							for (int i = 0; i < 100
									&& inventory.isItemSelected(); i++) {
								Methods.sleep(20);
							}
							Methods.sleep(20);
							final Rectangle area2 = clayItem.getComponent()
									.getArea();
							final int randx2 = (int) Methods.random(
									area2.getMinX(), area2.getMaxX());
							final int randy2 = (int) Methods.random(
									area2.getMinY(), area2.getMaxY());
							mouseMove(new Point(randx2, randy2));
							clayItem.doAction("Use");
							for (int i = 0; i < 100
									&& !inventory.isItemSelected(); i++) {
								Methods.sleep(20);
							}
							canAB = true;
							Methods.sleep(20);
						}
						if (isPaused() || !isActive() || !isRunning()) {
							break;
						}
						canAB = true;
						if (isWFally && camera.getPitch() < 94) {
							final int rand = Methods.random(1, 6);
							switch (rand) {
							case 1: {
								camera.setCompass('w');
							}
								break;
							case 2: {
								camera.setPitch(Methods.random(94,
										Methods.random(98, 100)));
							}
								break;
							case 3: {
								final int rand2 = Methods.random(1, 2);
								if (rand2 == 1) {
									camera.setPitch(true);
								}
								camera.setCompass('e');
								if (camera.getPitch() <= 94) {
									camera.setPitch(true);
								}
							}
								break;
							default: {
								try {
									camera.turnTo(loc.getRSObject());
								} catch (final NullPointerException npe) {
									camera.setPitch(true);
								}
							}
								break;
							}
						}
						if (isPaused() || !isActive() || !isRunning()) {
							break;
						}
						if (loc.getRSModel() != null) {
							if (failCounter > 2) {
								failCounter = 0;
								canAB = true;
								inventory.clickSelectedItem();
								break;
							}
							canAB = false;
							if (doAction(loc.getRSObject(), "Use", "Clay",
									loc.getObjectName())) {
								for (int i = 0; i < 100
										&& !interfaces.getComponent(905, 14)
												.isValid(); i++) {
									Methods.sleep(35);
								}
								if (isPaused() || !isActive() || !isRunning()) {
									break;
								}
								canAB = true;
								Methods.sleep(Methods.random(160, 250));
								canAB = false;
								if (interfaces.getComponent(905, 14).isValid()) {
									final Rectangle area = interfaces
											.getComponent(905, 14).getArea();
									final int randx = (int) Methods.random(
											area.getMinX(), area.getMaxX());
									final int randy = (int) Methods.random(
											area.getMinY(), area.getMaxY());
									mouseMove(new Point(randx, randy));
									interfaces.getComponent(905, 14).doClick();
									Methods.sleep(Methods.random(400, 1000));
									if (Methods.random(1, 2) == Methods.random(
											1, 2)) {
										Antiban.antibanmouse();
									}
									canAB = true;
									inv.reset(0);
									failCounter = 0;
									if (isPaused() || !isRunning()
											|| !isActive()) {
										break;
									}
									Methods.sleep(Methods.random(230, 450));
									break;
								}
								failCounter++;
								canAB = true;
								break;
							} else {
								canAB = false;
								final Point ploc = getLocation(loc
										.getRSObject());
								mouseMove(ploc);
								if (!loc.getRSObject().doAction(
										"Use Clay -> " + loc.getObjectName())) {
									if (isPaused() || !isActive()
											|| !isRunning()) {
										break;
									}
									camera.turnTo(loc.getRSObject());
									failCounter++;
									canAB = true;
									break;
								} else {
									for (int i = 0; i < 200
											&& !interfaces
													.getComponent(905, 14)
													.isValid(); i++) {
										Methods.sleep(35);
									}
									if (isPaused() || !isActive()
											|| !isRunning()) {
										break;
									}
									canAB = true;
									Methods.sleep(Methods.random(160, 250));
									canAB = false;
									if (interfaces.getComponent(905, 14)
											.isValid()) {
										final Rectangle area = interfaces
												.getComponent(905, 14)
												.getArea();
										final int randx = (int) Methods.random(
												area.getMinX(), area.getMaxX());
										final int randy = (int) Methods.random(
												area.getMinY(), area.getMaxY());
										mouseMove(new Point(randx, randy));
										interfaces.getComponent(905, 14)
												.doClick();
										Methods.sleep(Methods.random(400, 1000));
										if (Methods.random(1, 2) == Methods
												.random(1, 2)) {
											Antiban.antibanmouse();
										}
										canAB = true;
										inv.reset(0);
										failCounter = 0;
										if (isPaused() || !isActive()
												|| !isRunning()) {
											break;
										}
										Methods.sleep(Methods.random(230, 450));
										break;
									}
									failCounter++;
									canAB = true;
									break;
								}
							}
						}
					}
				}
				canAB = true;
				break;
			case TOBANK:
				loc.walkToBank();
				break;
			case FROMBANK:
				loc.walkToObject();
				break;
			case CHILL:
				Methods.sleep(500);
				break;
			case WAITFORINVENTORY: {
				canAB = true;
				if (isPaused() || !isActive() || !isRunning()) {
					break;
				}
				if (inv.idle() < 60) {
					if (!inventory.contains(clay)) {
						break;
					}
					Methods.sleep(Methods.random(100, 225));
					if (!inventory.contains(clay)) {
						break;
					}
					if (isPaused() || !isActive() || !isRunning()) {
						break;
					}
					if (inv.idle() < 60) {
						return Methods.random(700, 1100);
					}
				}
			}
				break;
			}
		} catch (final NullPointerException npe) {
			return 1;
		} catch (final ArrayIndexOutOfBoundsException aio) {
			return 1;
		} catch (final IllegalArgumentException iae) {
		}
		return Methods.random(95, 175);
	}

	@Override
	public void messageReceived(final MessageEvent event) {
		final int id = event.getID();
		if (id == MessageEvent.MESSAGE_ACTION) {
			final String message = event.getMessage().toLowerCase();
			if (message.contains("soft, workable") && message.contains("clay")) {
				numDone++;
			}
		}
	}

	/*
	 * Moves the mouse using one of three methods: RSBot's API's mouse.move(),
	 * hopping along points of a bezier curve,and moveMouse(), which kind of
	 * squiggles the mouse in a fairly straight line to the destination.
	 */
	private void mouseMove(final Point destination) {
		try {
			canAB = false;
			mousemovet = System.currentTimeMillis();
			Methods.sleep(Methods.random(40, 60));
			if (!beziers && !shortsplines) {
				mouse.move(destination);
				canAB = true;
				return;
			} else if (beziers && shortsplines) {
				int rand = Methods.random(1, 18);
				if (rand <= 3
						&& calc.distanceBetween(mouse.getLocation(),
								destination) >= 300) {
					rand = Methods.random(4, 18);
				}
				switch (rand) {
				case 1:
				case 2:
				case 3: {
					moveMouse(destination);
					break;
				}
				case 4:
				case 5:
				case 6:
				case 7:
				case 8: {
					alternateSplineGen.mouseTo(destination);
					break;
				}
				default: {
					mouse.move(destination);
					break;
				}
				}
				canAB = true;
				return;
			} else if (beziers && !shortsplines) {
				final int rand = Methods.random(1, 15);
				switch (rand) {
				case 1:
				case 2:
				case 3:
				case 4: {
					alternateSplineGen.mouseTo(destination);
					break;
				}
				default: {
					mouse.move(destination);
					break;
				}
				}
				canAB = true;
				return;
			} else {
				int rand = Methods.random(1, 13);
				if (rand <= 2
						&& calc.distanceBetween(mouse.getLocation(),
								destination) >= 300) {
					rand = Methods.random(3, 13);
				}
				switch (rand) {
				case 1:
				case 2: {
					moveMouse(destination);
					break;
				}
				default: {
					mouse.move(destination);
					break;
				}
				}
				canAB = true;
				return;
			}
		} catch (final Exception e) {
			mouse.move(destination);
			canAB = true;
			return;
		}
	}

	/* For moving the mouse precisely (In a fairly straight line) */
	private void moveMouse(final Point location) {
		final Point mlocation = mouse.getLocation();
		final Point mid = pointInMiddle(mlocation, location);
		final ArrayList<Point> midPoints = new ArrayList<Point>();
		midPoints.add(mlocation);
		if (Methods.random(1, 3) == Methods.random(1, 3)) {
			midPoints.add(pointInMiddle(mlocation, mid));
		}
		midPoints.add(mid);
		if (Methods.random(1, 2) == Methods.random(1, 2)
				|| midPoints.size() <= 1 && Methods.random(1, 3) >= 2) {
			midPoints.add(pointInMiddle(mid, location));
		}
		midPoints.add(location);
		for (final Point curr : midPoints) {
			mouse.move(curr);
		}
	}

	@Override
	public void onFinish() {
		running = false;
		inv.kill();
		Antiban.kill();
		log("Thank you for using Aut0ClaySoftener!");
		running = false;
	}

	@Override
	public void onRepaint(final Graphics g1) {
		final Graphics2D g = (Graphics2D) g1;
		if (isPaused() || !isActive() || !isRunning() || !game.isLoggedIn()
				|| game.isLoginScreen() || game.isWelcomeScreen()) {
			if (timekeeper.getState() == 0) {
				timekeeper.setPaused();
			}
		} else {
			if (timekeeper.getState() == 1) {
				timekeeper.setResumed();
			}
		}
		final long totalProfit = numDone * softClayPrice - numDone * clayPrice;
		g.setRenderingHints(antialiasing);
		if (stats) {
			g.setStroke(stroke);
			g.setColor(color1);
			g.drawOval(5, 135, 280, 205);
			g.setColor(color2);
			g.fillOval(5, 135, 280, 205);
		}
		if (clicks && showmouse) {
			final long time = System.currentTimeMillis() - mouse.getPressTime();
			if (defmouse) {
				if (time <= 1200) {
					g.setColor(Color.RED);
					final Point clicked = mouse.getPressLocation();
					g.drawLine(clicked.x, clicked.y, clicked.x + 5, clicked.y);
					g.drawLine(clicked.x, clicked.y, clicked.x - 5, clicked.y);
					g.drawLine(clicked.x, clicked.y, clicked.x, clicked.y + 5);
					g.drawLine(clicked.x, clicked.y, clicked.x, clicked.y - 5);
				}
			} else {
				if (time <= 1200) {
					final Point clicked = mouse.getPressLocation();
					g.drawImage(nclicked, clicked.x - 6, clicked.y - 6, null);
				}
			}
		}
		g.setColor(System.currentTimeMillis() - mouse.getPressTime() > 175 ? cyan
				: Color.RED.darker());
		Point loc = mouse.getLocation();
		if (showmouse) {
			if (defmouse) {
				final int midUp = (int) (game.getHeight() / 2);
				final int midSide = (int) (game.getWidth() / 2);
				final Point midLeft = new Point(0, midUp);
				final Point midRight = new Point(game.getWidth(), midUp);
				final Point midUpp = new Point(midSide, game.getHeight());
				final Point midDown = new Point(midSide, 0);
				final GeneralPath mouseDrawer = new GeneralPath();
				mouseDrawer.moveTo(loc.x, loc.y);
				Point pointBetween = pointInMiddle(loc, midLeft);
				mouseDrawer.lineTo(pointBetween.x, pointBetween.y);
				mouseDrawer.moveTo(loc.x, loc.y);
				pointBetween = pointInMiddle(loc, midRight);
				mouseDrawer.lineTo(pointBetween.x, pointBetween.y);
				mouseDrawer.moveTo(loc.x, loc.y);
				pointBetween = pointInMiddle(loc, midUpp);
				mouseDrawer.lineTo(pointBetween.x, pointBetween.y);
				mouseDrawer.moveTo(loc.x, loc.y);
				pointBetween = pointInMiddle(loc, midDown);
				mouseDrawer.lineTo(pointBetween.x, pointBetween.y);
				g.draw(mouseDrawer);
			}
		}
		if (stats) {
			g.setColor(Color.WHITE);
			g.setFont(calibri20);
			g.drawString(
					"Runtime: "
							+ (timekeeper.getState() == 0 ? timekeeper
									.getRuntimeString() : "Paused"), 30, 195);
			g.drawString("Clay Softened: " + numDone, 15, 220);
			g.drawString(
					"Soft Clay / Hour: " + timekeeper.calcPerHour(numDone), 10,
					245);
			g.drawString("Total Profit: " + totalProfit, 20, 270);
			g.drawString(
					"Profit / Hour: " + timekeeper.calcPerHour(totalProfit),
					30, 295);
		}
		if (paths) {
			drawPath(g);
		}
		if (showmouse) {
			if (defmouse) {
				loc = mouse.getLocation();
				g.setColor(System.currentTimeMillis() - mouse.getPressTime() > 175 ? color2
						: Color.RED);
				g.drawOval(loc.x - 9, loc.y - 9, 18, 18);
				g.setColor(System.currentTimeMillis() - mouse.getPressTime() > 175 ? blue
						: Color.RED);
				if (rotmouse) {
					g.rotate(Math.toRadians(getRotate(5)), loc.x, loc.y);
				}
				loc = mouse.getLocation();
				g.drawRect(loc.x - 6, loc.y - 6, 12, 12);
				g.setColor(System.currentTimeMillis() - mouse.getPressTime() > 175 ? darkCyan
						: Color.RED);
				if (rotmouse) {
					g.rotate(Math.toRadians(getRotate(5)), loc.x, loc.y);
				}
				loc = mouse.getLocation();
				g.drawRect(loc.x - 3, loc.y - 3, 6, 6);
			} else {
				loc = mouse.getLocation();
				if (rotmouse) {
					g.rotate(Math.toRadians(getRotate(5)), loc.x, loc.y);
				}
				g.drawImage(nmouse, loc.x - 8, loc.y - 8, null);
			}
		}
		g.setColor(color1);
	}

	@Override
	public boolean onStart() {
		log("Welcome to Aut0ClaySoftener. Starting GUI and loading prices...");
		Methods.sleep(100);
		if (!game.isLoggedIn() || game.isLoginScreen()
				|| game.isWelcomeScreen()) {
			env.enableRandom("Login");
		}
		final Thread priceloader = new Thread(new PriceLoader());
		priceloader.start();
		createAndWaitforGUI();
		Methods.sleep(75);
		if (gui.isCanceled() == true) {
			return false;
		}
		final int pp = (int) gui.softenLocation();
		switch (pp) {
		case locVar.fally:
			loc = new Falador();
			break;
		case locVar.edge:
			loc = new Edgeville();
			break;
		default:
			return false;
		}
		running = true;
		inv = new InventoryListener();
		invThread = new Thread(inv);
		invThread.start();
		while (priceloader.isAlive()) {
			Methods.sleep(100);
		}
		if (softClayPrice == -1 || clayPrice == -1) {
			log.severe("Unable to load G.E. item info. Please try again.");
			return false;
		} else {
			if (nclicked == null || nmouse == null) {
				log.severe("Unable to load images. Please try again. If this keeps happening,");
				log.severe("please PM icnhzabot.");
				return false;
			}
			log("Done. Soft clay is " + softClayPrice + " GP. Clay is "
					+ clayPrice + " GP.");
		}
		mouse.setSpeed(Methods.random(mouseLo, mouseHi));
		mouseCheck = new MouseMovementChecker();
		cameraCheck = new CameraMovementChecker();
		Antiban = new antiban();
		canAB = true;
		System.currentTimeMillis();
		return true;
	}

	private boolean openBank() {
		try {
			if (!bank.isOpen()) {
				if (menu.isOpen()) {
					mouse.moveSlightly();
					Methods.sleep(Methods.random(40, 60));
				}
				RSObject bankBooth = objects.getNearest(Bank.BANK_BOOTHS);
				RSNPC banker = npcs.getNearest(Bank.BANKERS);
				final RSObject bankChest = objects.getNearest(Bank.BANK_CHESTS);
				final int dist = calc.distanceTo(bankBooth);
				if (banker != null && bankBooth != null
						&& calc.distanceTo(banker) < dist) {
					if (calc.distanceBetween(banker.getLocation(),
							bankBooth.getLocation()) <= 1) {
						if (Methods.random(1, 3) >= 2) {
							banker = null;
						} else {
							bankBooth = null;
						}
					} else {
						bankBooth = null;
					}
				}
				if (bankChest != null && calc.distanceTo(bankChest) < dist) {
					bankBooth = null;
					banker = null;
				}
				if (bankBooth != null && calc.distanceTo(bankBooth) < 5
						&& calc.tileOnMap(bankBooth.getLocation())
						&& calc.canReach(bankBooth.getLocation(), true)
						|| banker != null && calc.distanceTo(banker) < 8
						&& calc.tileOnMap(banker.getLocation())
						&& calc.canReach(banker.getLocation(), true)
						|| bankChest != null && calc.distanceTo(bankChest) < 8
						&& calc.tileOnMap(bankChest.getLocation())
						&& calc.canReach(bankChest.getLocation(), true)
						&& !bank.isOpen()) {
					if (bankBooth != null) {
						final Point loc = getLocation(bankBooth);
						for (int i = 0; i < 10 && !menu.contains("Use-Quickly"); i++) {
							mouseMove(loc);
							if (menu.contains("Use-Quickly")) {
								Methods.sleep(Methods.random(20, 60));
								if (menu.contains("Use-Quickly")) {
									Methods.sleep(Methods.random(20, 60));
									if (menu.contains("Use-Quickly")) {
										break;
									}
								}
							}
						}
						if (doMenuAction("Use-Quickly")) {
							int count = 0;
							while (!bank.isOpen() && ++count <= 10) {
								Methods.sleep(Methods.random(200, 300));
								if (getMyPlayer().isMoving()) {
									count = 0;
								}
							}
						} else {
							camera.turnTo(bankBooth);
						}
					} else if (banker != null) {
						RSModel m = banker.getModel();
						if (m == null) {
							m = banker.getModel();
							if (m == null) {
								return false;
							}
						}
						final Point loc = pointOnScreen(m);
						for (int i = 0; i < 10 && !menu.contains("Bank Banker"); i++) {
							mouseMove(loc);
							if (menu.contains("Bank Banker")) {
								Methods.sleep(Methods.random(20, 60));
								if (menu.contains("Bank Banker")) {
									Methods.sleep(Methods.random(20, 60));
									if (menu.contains("Bank Banker")) {
										break;
									}
								}
							}
						}
						if (doMenuAction("Use-Quickly")) {
							int count = 0;
							while (!bank.isOpen() && ++count <= 10) {
								Methods.sleep(Methods.random(200, 300));
								if (getMyPlayer().isMoving()) {
									count = 0;
								}
							}
						} else {
							camera.turnTo(banker);
						}
					} else if (bankChest != null) {
						final Point loc = getLocation(bankChest);
						for (int i = 0; i < 10 && !menu.contains("Bank")
								&& !menu.contains("Use"); i++) {
							mouseMove(loc);
							if (menu.contains("Bank") || menu.contains("Use")) {
								Methods.sleep(Methods.random(20, 60));
								if (menu.contains("Bank")
										|| menu.contains("Use")) {
									Methods.sleep(Methods.random(20, 60));
									if (menu.contains("Bank")
											|| menu.contains("Use")) {
										break;
									}
								}
							}
						}
						if (doMenuAction("Bank") || doMenuAction("Use")) {
							int count = 0;
							while (!bank.isOpen() && ++count <= 10) {
								Methods.sleep(Methods.random(200, 300));
								if (getMyPlayer().isMoving()) {
									count = 0;
								}
							}
						} else {
							camera.turnTo(bankBooth);
						}
					}
				} else {
					if (bankBooth != null) {
						walking.walkTo(bankBooth.getLocation());
					} else if (banker != null) {
						walking.walkTo(banker.getLocation());
					} else if (bankChest != null) {
						walking.walkTo(bankChest.getLocation());
					} else {
						return false;
					}
				}
			}
			return bank.isOpen();
		} catch (final Exception e) {
			return false;
		}
	}

	/* Calculations for paint */
	private Point pointInMiddle(final Point start, final Point end) {
		return new Point((int) ((start.x + end.x) / 2),
				(int) ((start.y + end.y) / 2));
	}

	/*
	 * Creates, shows, and waits for the GUI. Does not give thousands of lines
	 * of errors if users have installedthemes.
	 */
	private Point pointOnScreen(final RSModel m) {
		if (m == null) {
			return new Point(-1, -1);
		}
		final Point[] all = m.getPoints();
		if (all == null) {
			return new Point(-1, -1);
		}
		final ArrayList<Point> onscreen = new ArrayList<Point>();
		for (final Point p : all) {
			if (calc.pointOnScreen(p)) {
				onscreen.add(p);
			}
		}
		if (onscreen.isEmpty()) {
			return new Point(-1, -1);
		}
		return onscreen.get(Methods.random(0, onscreen.size() - 1));
	}

	private Point tileToMap(final RSTile tile) {
		final RSPlayer player = getMyPlayer();
		final double minimapAngle = -1 * Math.toRadians(camera.getAngle());
		final int x = (tile.getX() - player.getLocation().getX()) * 4 - 2;
		final int y = (player.getLocation().getY() - tile.getY()) * 4 - 2;
		return new Point((int) Math.round(x * Math.cos(minimapAngle) + y
				* Math.sin(minimapAngle) + 628), (int) Math.round(y
				* Math.cos(minimapAngle) - x * Math.sin(minimapAngle) + 87));
	}

	private boolean withdraw(final int itemID, final int count) {
		if (count < 0) {
			throw new IllegalArgumentException("count < 0 (" + count + ")");
		}
		if (!bank.isOpen() || isPaused() || !isActive() || !isRunning()) {
			return false;
		}
		final RSItem item = bank.getItem(itemID);
		if (item == null) {
			return false;
		}
		final RSComponent comp = item.getComponent();
		if (comp == null) {
			return false;
		}
		while (comp.getRelativeX() == 0 && bank.getCurrentTab() != 0) {
			interfaces.getComponent(Bank.INTERFACE_BANK,
					Bank.INTERFACE_BANK_TAB[0]).doClick();
			Methods.sleep(Methods.random(600, 1100));
		}
		if (!interfaces.scrollTo(comp, (Bank.INTERFACE_BANK << 16)
				+ Bank.INTERFACE_BANK_SCROLLBAR)) {
			return false;
		}
		Methods.sleep(Methods.random(60, 200));
		final Rectangle area = comp.getArea();
		final int randx = (int) Methods.random(area.getMinX(), area.getMaxX());
		final int randy = (int) Methods.random(area.getMinY(), area.getMaxY());
		final Point itemPoint = new Point(randx, randy);
		final int inventoryCount = inventory.getCount(true);
		switch (count) {
		case 0: // Withdraw All
			mouseMove(itemPoint);
			doMenuAction("Withdraw-All");
			break;
		case 1: // Withdraw 1
			mouseMove(itemPoint);
			item.doClick(true);
			break;
		case 5: // Withdraw 5
		case 10: // Withdraw 10
			mouseMove(itemPoint);
			doMenuAction("Withdraw-" + count);
			break;
		default: // Withdraw x
			mouseMove(itemPoint);
			Methods.sleep(Methods.random(100, 500));
			if (menu.contains("Withdraw-" + count)) {
				if (doMenuAction("Withdraw-" + count)) {
					Methods.sleep(Methods.random(100, 200));
					return true;
				}
				return false;
			}
			if (!area.contains(mouse.getLocation())) {
				mouseMove(itemPoint);
			}
			if (doMenuAction("Withdraw-X")) {
				Methods.sleep(Methods.random(1000, 1300));
				keyboard.sendText(Integer.toString(count), true);
			}
			Methods.sleep(Methods.random(100, 200));
			break;
		}
		return inventory.getCount(true) > inventoryCount
				|| inventory.getCount(true) == 28;
	}
}
