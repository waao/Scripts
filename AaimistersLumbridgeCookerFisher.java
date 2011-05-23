/**
 * @author Aaimister
 * @version 1.07 ©2010-2011 Aaimister, No one except Aaimister has the right to
 *          modify and/or spread this script without the permission of Aaimister.
 *          I'm not held responsible for any damage that may occur to your
 *          property.
 */

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;

import org.rsbot.event.events.MessageEvent;
import org.rsbot.event.listeners.MessageListener;
import org.rsbot.event.listeners.PaintListener;
import org.rsbot.script.Script;
import org.rsbot.script.ScriptManifest;
import org.rsbot.script.util.Filter;
import org.rsbot.script.wrappers.RSArea;
import org.rsbot.script.wrappers.RSComponent;
import org.rsbot.script.wrappers.RSItem;
import org.rsbot.script.wrappers.RSNPC;
import org.rsbot.script.wrappers.RSObject;
import org.rsbot.script.wrappers.RSPath;
import org.rsbot.script.wrappers.RSPlayer;
import org.rsbot.script.wrappers.RSTile;

@ScriptManifest(authors = { "Aaimister" }, name = "Aaimisters Lumbridge Cooker & Fisher", keywords = "Cooking & Fishing", version = 1.07, description = "Fishes and cooks crayfish behind Lumbridge Castle.", website = "http://www.powerbot.org/vb/showthread.php?t=684846", requiresVersion = 244)
public class AaimistersLumbridgeCookerFisher extends Script implements
		PaintListener, MessageListener, MouseListener {

	public class AaimistersGUI {

		private JFrame AaimistersGUI;

		private JPanel contentPane;

		private JComboBox colorBox;

		private JCheckBox antibanBox;

		private JCheckBox paintBox;
		private JCheckBox restBox;
		private JCheckBox powerBox;
		private JCheckBox breakBox;
		private JCheckBox randomBox;
		private JTextArea maxTimeBeBox;
		private JTextArea minTimeBeBox;
		private JTextArea maxBreakBox;
		private JTextArea minBreakBox;
		private JPanel panel;
		private JPanel panel_1;
		private JPanel panel_2;
		private JPanel panel_4;
		private JLabel lblAaimistersEssenceMiner;
		private JLabel lblPaintColor;
		private JLabel lblTimeBetweenBreaks;
		private JLabel lblBreakLengths;
		private JLabel lblTo;
		private JLabel lblMins;
		private JLabel label_3;
		private JLabel label_6;
		private JLabel label_4;
		private JLabel label_5;
		private JTabbedPane tabbedPane;
		private JButton submit;

		private AaimistersGUI() {
			initComponents();
		}

		private void breakBoxActionPerformed(final ActionEvent e) {
			doBreak = breakBox.isSelected();
			randomBreaks = randomBox.isSelected();
			if (!doBreak) {
				randomBox.setEnabled(false);
				randomBox.setSelected(false);
				maxTimeBeBox.setEnabled(false);
				minTimeBeBox.setEnabled(false);
				maxBreakBox.setEnabled(false);
				minBreakBox.setEnabled(false);
			} else {
				randomBox.setEnabled(true);
				if (!randomBreaks) {
					maxTimeBeBox.setEnabled(true);
					minTimeBeBox.setEnabled(true);
					maxBreakBox.setEnabled(true);
					minBreakBox.setEnabled(true);
				}
			}
		}

		private void initComponents() {
			AaimistersGUI = new JFrame();
			contentPane = new JPanel();
			powerBox = new JCheckBox();
			colorBox = new JComboBox();
			antibanBox = new JCheckBox();
			restBox = new JCheckBox();
			paintBox = new JCheckBox();
			breakBox = new JCheckBox();
			randomBox = new JCheckBox();
			maxTimeBeBox = new JTextArea();
			minTimeBeBox = new JTextArea();
			maxBreakBox = new JTextArea();
			minBreakBox = new JTextArea();
			panel = new JPanel();
			panel_1 = new JPanel();
			panel_2 = new JPanel();
			panel_4 = new JPanel();
			lblAaimistersEssenceMiner = new JLabel();
			lblPaintColor = new JLabel();
			lblTimeBetweenBreaks = new JLabel();
			lblBreakLengths = new JLabel();
			lblTo = new JLabel();
			lblMins = new JLabel();
			label_3 = new JLabel();
			label_6 = new JLabel();
			label_4 = new JLabel();
			label_5 = new JLabel();
			tabbedPane = new JTabbedPane(JTabbedPane.TOP);
			submit = new JButton();

			// Listeners
			AaimistersGUI.addWindowListener(new WindowAdapter() {
				public void windowClosing(final WindowEvent e) {
					closed = true;
				}
			});

			AaimistersGUI.setTitle("Aaimister's Lum. Cooker & Fisher v1.07");
			AaimistersGUI.setForeground(new Color(255, 255, 255));
			AaimistersGUI.setBackground(Color.LIGHT_GRAY);
			AaimistersGUI.setResizable(false);
			AaimistersGUI.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			AaimistersGUI.setBounds(100, 100, 300, 305);
			contentPane.setBackground(SystemColor.menu);
			contentPane.setForeground(Color.LIGHT_GRAY);
			contentPane.setFont(new Font("Cambria Math", Font.PLAIN, 17));
			contentPane.setBorder(new EmptyBorder(5, 8, 8, 8));
			AaimistersGUI.setContentPane(contentPane);
			contentPane.setLayout(null);

			panel.setBorder(new MatteBorder(0, 0, 1, 0, (Color) new Color(0, 0, 0)));
			panel.setBounds(4, 0, 296, 40);
			contentPane.add(panel);
			panel.setLayout(null);

			lblAaimistersEssenceMiner.setText("Aaimister's Lum. Cooker & Fisher v1.07");
			lblAaimistersEssenceMiner.setBounds(0, 0, 286, 40);
			panel.add(lblAaimistersEssenceMiner);
			lblAaimistersEssenceMiner.setHorizontalAlignment(SwingConstants.CENTER);
			lblAaimistersEssenceMiner.setForeground(SystemColor.infoText);
			lblAaimistersEssenceMiner.setFont(new Font("Calibri", Font.BOLD, 17));

			tabbedPane.setBounds(4, 51, 286, 183);
			contentPane.add(tabbedPane);

			tabbedPane.addTab("General", null, panel_1, null);

			powerBox.setText("Power Fish");
			powerBox.setForeground(Color.BLACK);
			powerBox.setFont(new Font("Cambria Math", Font.PLAIN, 12));

			restBox.setText("Use Rest");
			restBox.setForeground(Color.BLACK);
			restBox.setFont(new Font("Cambria Math", Font.PLAIN, 12));
			restBox.setSelected(true);

			paintBox.setText("Enable Anti-Aliasing");
			paintBox.setForeground(Color.BLACK);
			paintBox.setFont(new Font("Cambria Math", Font.PLAIN, 12));
			paintBox.setSelected(true);

			lblPaintColor.setText("Paint Color:");
			lblPaintColor.setForeground(Color.BLACK);
			lblPaintColor.setFont(new Font("Cambria Math", Font.PLAIN, 15));

			colorBox.setModel(new DefaultComboBoxModel(colorstring));

			antibanBox.setText("Use Anti-Ban");
			antibanBox.setSelected(true);
			antibanBox.setForeground(Color.BLACK);
			antibanBox.setFont(new Font("Cambria Math", Font.PLAIN, 12));
			final GroupLayout gl_panel_1 = new GroupLayout(panel_1);
			gl_panel_1.setHorizontalGroup(gl_panel_1.createParallelGroup(Alignment.LEADING).addGroup(gl_panel_1.createSequentialGroup().addGap(28).addGroup(gl_panel_1.createParallelGroup(Alignment.LEADING).addComponent(restBox, GroupLayout.PREFERRED_SIZE, 107, GroupLayout.PREFERRED_SIZE).addComponent(powerBox).addComponent(lblPaintColor, GroupLayout.PREFERRED_SIZE, 79, GroupLayout.PREFERRED_SIZE)).addPreferredGap(ComponentPlacement.UNRELATED).addGroup(gl_panel_1.createParallelGroup(Alignment.LEADING).addComponent(antibanBox, GroupLayout.PREFERRED_SIZE, 107, GroupLayout.PREFERRED_SIZE).addComponent(paintBox, GroupLayout.DEFAULT_SIZE, 138, Short.MAX_VALUE).addComponent(colorBox, GroupLayout.PREFERRED_SIZE, 102, GroupLayout.PREFERRED_SIZE)).addContainerGap()));
			gl_panel_1.setVerticalGroup(gl_panel_1.createParallelGroup(Alignment.LEADING).addGroup(gl_panel_1.createSequentialGroup().addGap(17).addGroup(gl_panel_1.createParallelGroup(Alignment.BASELINE).addComponent(powerBox).addComponent(antibanBox)).addGap(18).addGroup(gl_panel_1.createParallelGroup(Alignment.BASELINE).addComponent(restBox).addComponent(paintBox)).addGap(26).addGroup(gl_panel_1.createParallelGroup(Alignment.BASELINE).addComponent(colorBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE).addComponent(lblPaintColor)).addGap(127)));
			panel_1.setLayout(gl_panel_1);

			tabbedPane.addTab("Breaks", null, panel_2, null);

			breakBox.setText("Use Custom Breaks");
			breakBox.setForeground(Color.BLACK);
			breakBox.setFont(new Font("Cambria Math", Font.PLAIN, 12));
			breakBox.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(final ActionEvent e) {
					breakBoxActionPerformed(e);
				}
			});

			lblTimeBetweenBreaks.setText("Time Between Breaks:");
			lblTimeBetweenBreaks.setForeground(Color.BLACK);
			lblTimeBetweenBreaks.setFont(new Font("Cambria Math", Font.PLAIN, 13));

			lblBreakLengths.setText("Break Lengths:");
			lblBreakLengths.setForeground(Color.BLACK);
			lblBreakLengths.setFont(new Font("Cambria Math", Font.PLAIN, 13));

			randomBox.setText("Random Breaks");
			randomBox.setForeground(Color.BLACK);
			randomBox.setFont(new Font("Cambria Math", Font.PLAIN, 12));
			if (!doBreak) {
				randomBox.setEnabled(false);
			} else {
				randomBox.setEnabled(true);
			}
			randomBox.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(final ActionEvent e) {
					randomBoxActionPerformed(e);
				}
			});

			minTimeBeBox.setForeground(Color.BLACK);
			minTimeBeBox.setText("60");
			minTimeBeBox.setFont(new Font("Cambria Math", Font.PLAIN, 13));
			if (!doBreak || randomBreaks) {
				minTimeBeBox.setEnabled(false);
			}

			lblTo.setText("to");
			lblTo.setForeground(Color.GRAY);
			lblTo.setFont(new Font("Cambria Math", Font.PLAIN, 13));

			maxTimeBeBox.setText("90");
			maxTimeBeBox.setForeground(Color.BLACK);
			maxTimeBeBox.setFont(new Font("Cambria Math", Font.PLAIN, 13));
			if (!doBreak || randomBreaks) {
				maxTimeBeBox.setEnabled(false);
			}

			lblMins.setText("mins");
			lblMins.setForeground(Color.GRAY);
			lblMins.setFont(new Font("Cambria Math", Font.PLAIN, 13));

			label_3.setText("mins");
			label_3.setForeground(Color.GRAY);
			label_3.setFont(new Font("Cambria Math", Font.PLAIN, 13));

			minBreakBox.setText("15");
			minBreakBox.setForeground(Color.BLACK);
			minBreakBox.setFont(new Font("Cambria Math", Font.PLAIN, 13));
			if (!doBreak || randomBreaks) {
				minBreakBox.setEnabled(false);
			}

			label_4.setText("mins");
			label_4.setForeground(Color.GRAY);
			label_4.setFont(new Font("Cambria Math", Font.PLAIN, 13));

			label_5.setText("to");
			label_5.setForeground(Color.GRAY);
			label_5.setFont(new Font("Cambria Math", Font.PLAIN, 13));

			maxBreakBox.setText("60");
			maxBreakBox.setForeground(Color.BLACK);
			maxBreakBox.setFont(new Font("Cambria Math", Font.PLAIN, 13));
			if (!doBreak || randomBreaks) {
				maxBreakBox.setEnabled(false);
			}

			label_6.setText("mins");
			label_6.setForeground(Color.GRAY);
			label_6.setFont(new Font("Cambria Math", Font.PLAIN, 13));
			final GroupLayout gl_panel_4 = new GroupLayout(panel_4);
			gl_panel_4.setHorizontalGroup(gl_panel_4.createParallelGroup(Alignment.LEADING).addGroup(gl_panel_4.createSequentialGroup().addGap(10).addComponent(breakBox).addGap(18).addComponent(randomBox, GroupLayout.DEFAULT_SIZE, 123, Short.MAX_VALUE).addContainerGap()).addGroup(gl_panel_4.createSequentialGroup().addContainerGap().addGroup(gl_panel_4.createParallelGroup(Alignment.LEADING).addComponent(lblTimeBetweenBreaks, GroupLayout.DEFAULT_SIZE, 151, Short.MAX_VALUE).addGroup(gl_panel_4.createSequentialGroup().addGap(10).addGroup(gl_panel_4.createParallelGroup(Alignment.LEADING, false).addGroup(gl_panel_4.createSequentialGroup().addPreferredGap(ComponentPlacement.RELATED).addComponent(minBreakBox, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE).addPreferredGap(ComponentPlacement.RELATED).addComponent(label_4, GroupLayout.PREFERRED_SIZE, 31, GroupLayout.PREFERRED_SIZE).addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(label_5)).addGroup(gl_panel_4.createSequentialGroup().addComponent(minTimeBeBox, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE).addPreferredGap(ComponentPlacement.RELATED).addComponent(lblMins, GroupLayout.PREFERRED_SIZE, 31, GroupLayout.PREFERRED_SIZE).addGap(27).addComponent(lblTo))))).addPreferredGap(ComponentPlacement.RELATED).addGroup(gl_panel_4.createParallelGroup(Alignment.LEADING).addGroup(gl_panel_4.createSequentialGroup().addComponent(maxTimeBeBox, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE).addPreferredGap(ComponentPlacement.RELATED).addComponent(label_3, GroupLayout.PREFERRED_SIZE, 31, GroupLayout.PREFERRED_SIZE)).addGroup(gl_panel_4.createSequentialGroup().addComponent(maxBreakBox, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE).addPreferredGap(ComponentPlacement.RELATED).addComponent(label_6, GroupLayout.PREFERRED_SIZE, 31, GroupLayout.PREFERRED_SIZE))).addGap(47)).addGroup(gl_panel_4.createSequentialGroup().addContainerGap().addComponent(lblBreakLengths, GroupLayout.DEFAULT_SIZE, 105, Short.MAX_VALUE).addGap(167)));
			gl_panel_4.setVerticalGroup(gl_panel_4.createParallelGroup(Alignment.LEADING).addGroup(gl_panel_4.createSequentialGroup().addContainerGap().addGroup(gl_panel_4.createParallelGroup(Alignment.BASELINE).addComponent(breakBox).addComponent(randomBox)).addPreferredGap(ComponentPlacement.UNRELATED).addComponent(lblTimeBetweenBreaks).addGap(14).addGroup(gl_panel_4.createParallelGroup(Alignment.LEADING).addGroup(gl_panel_4.createParallelGroup(Alignment.BASELINE).addComponent(lblTo).addComponent(maxTimeBeBox, GroupLayout.PREFERRED_SIZE, 20, GroupLayout.PREFERRED_SIZE).addComponent(label_3, GroupLayout.PREFERRED_SIZE, 16, GroupLayout.PREFERRED_SIZE)).addGroup(gl_panel_4.createParallelGroup(Alignment.BASELINE).addComponent(minTimeBeBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE).addComponent(lblMins, GroupLayout.PREFERRED_SIZE, 16, GroupLayout.PREFERRED_SIZE))).addGap(36).addGroup(gl_panel_4.createParallelGroup(Alignment.TRAILING).addGroup(gl_panel_4.createSequentialGroup().addPreferredGap(ComponentPlacement.RELATED).addGroup(gl_panel_4.createParallelGroup(Alignment.BASELINE).addComponent(label_4, GroupLayout.PREFERRED_SIZE, 16, GroupLayout.PREFERRED_SIZE).addComponent(label_5, GroupLayout.PREFERRED_SIZE, 16, GroupLayout.PREFERRED_SIZE)).addContainerGap()).addGroup(gl_panel_4.createSequentialGroup().addPreferredGap(ComponentPlacement.RELATED).addGroup(gl_panel_4.createParallelGroup(Alignment.BASELINE).addComponent(maxBreakBox, GroupLayout.PREFERRED_SIZE, 20, GroupLayout.PREFERRED_SIZE).addComponent(label_6, GroupLayout.PREFERRED_SIZE, 16, GroupLayout.PREFERRED_SIZE)).addContainerGap()).addGroup(gl_panel_4.createSequentialGroup().addPreferredGap(ComponentPlacement.RELATED).addComponent(minBreakBox, GroupLayout.PREFERRED_SIZE, 20, GroupLayout.PREFERRED_SIZE).addContainerGap()))).addGroup(gl_panel_4.createSequentialGroup().addGap(98).addComponent(lblBreakLengths).addContainerGap(40, Short.MAX_VALUE)));
			gl_panel_4.linkSize(SwingConstants.VERTICAL, new Component[] {
					lblTo, label_5 });
			panel_4.setLayout(gl_panel_4);
			final GroupLayout gl_panel_2 = new GroupLayout(panel_2);
			gl_panel_2.setHorizontalGroup(gl_panel_2.createParallelGroup(Alignment.LEADING).addGroup(gl_panel_2.createSequentialGroup().addComponent(panel_4, GroupLayout.PREFERRED_SIZE, 282, GroupLayout.PREFERRED_SIZE).addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
			gl_panel_2.setVerticalGroup(gl_panel_2.createParallelGroup(Alignment.LEADING).addGroup(gl_panel_2.createSequentialGroup().addComponent(panel_4, GroupLayout.PREFERRED_SIZE, 154, GroupLayout.PREFERRED_SIZE).addContainerGap(64, Short.MAX_VALUE)));
			panel_2.setLayout(gl_panel_2);

			submit.setText("Start");
			submit.setFont(new Font("Cambria Math", Font.BOLD, 12));
			submit.setBounds(100, 245, 89, 23);
			contentPane.add(submit);
			submit.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(final ActionEvent e) {
					submitActionPerformed(e);
				}
			});
			// LOAD SAVED SELECTION INFO
			try {
				final String filename = getCacheDirectory()
						+ "\\AaimistersLCookFishSettings.txt";
				final Scanner in = new Scanner(new BufferedReader(new FileReader(filename)));
				String line;
				String[] opts = {};
				while (in.hasNext()) {
					line = in.next();
					if (line.contains(":")) {
						opts = line.split(":");
					}
				}
				in.close();
				if (opts.length > 1) {
					if (opts[0].equals("true")) {
						powerBox.setSelected(true);
					} else {
						powerBox.setSelected(false);
					}
					if (opts[5].equals("true")) {
						randomBox.setEnabled(true);
						if (opts[6].equals("false")) {
							maxTimeBeBox.setText(opts[7]);
							minTimeBeBox.setText(opts[8]);
							maxBreakBox.setText(opts[9]);
							minBreakBox.setText(opts[10]);
							maxTimeBeBox.setEnabled(true);
							minTimeBeBox.setEnabled(true);
							maxBreakBox.setEnabled(true);
							minBreakBox.setEnabled(true);
						}
					}
					if (opts[1].equals("true")) {
						restBox.setSelected(true);
					} else {
						restBox.setSelected(false);
					}
					colorBox.setSelectedIndex(Integer.parseInt(opts[2]));
					if (opts[3].equals("true")) {
						antibanBox.setSelected(true);
					} else {
						antibanBox.setSelected(false);
					}
					if (opts[4].equals("true")) {
						paintBox.setSelected(true);
					} else {
						paintBox.setSelected(false);
					}
					if (opts[5].equals("true")) {
						breakBox.setSelected(true);
					} else {
						breakBox.setSelected(false);
						randomBox.setEnabled(false);
					}
					if (opts[6].equals("true")) {
						randomBox.setSelected(true);
						randomBox.setEnabled(true);
					} else {
						randomBox.setSelected(false);
					}
				}
			} catch (final Exception e2) {
				// e2.printStackTrace();
				log.warning("Error loading settings.");
			}
			// END LOAD SAVED SELECTION INFO
		}

		private void randomBoxActionPerformed(final ActionEvent e) {
			doBreak = breakBox.isSelected();
			randomBreaks = randomBox.isSelected();
			if (randomBreaks == true) {
				maxTimeBeBox.setEnabled(false);
				minTimeBeBox.setEnabled(false);
				maxBreakBox.setEnabled(false);
				minBreakBox.setEnabled(false);
			} else {
				if (doBreak) {
					maxTimeBeBox.setEnabled(true);
					minTimeBeBox.setEnabled(true);
					maxBreakBox.setEnabled(true);
					minBreakBox.setEnabled(true);
				}
			}
		}

		public void submitActionPerformed(final ActionEvent e) {
			final String color = (String) colorBox.getSelectedItem();
			if (color.contains("Blue")) {
				MainColor = new Color(0, 0, 100);
				ThinColor = new Color(0, 0, 100, 70);
				LineColor = new Color(255, 255, 255);
				BoxColor = MainColor;
			} else if (color.contains("Black")) {
				MainColor = new Color(0, 0, 0);
				ThinColor = new Color(0, 0, 0, 70);
				LineColor = new Color(255, 255, 255);
				BoxColor = MainColor;
			} else if (color.contains("Brown")) {
				MainColor = new Color(92, 51, 23);
				ThinColor = new Color(92, 51, 23, 70);
				BoxColor = MainColor;
			} else if (color.contains("Cyan")) {
				MainColor = new Color(0, 255, 255);
				ThinColor = new Color(0, 255, 255, 70);
				BoxColor = MainColor;
				LineColor = new Color(0, 0, 0);
			} else if (color.contains("Green")) {
				MainColor = new Color(0, 100, 0);
				ThinColor = new Color(0, 100, 0, 70);
				BoxColor = MainColor;
			} else if (color.contains("Lime")) {
				MainColor = new Color(0, 220, 0);
				ThinColor = new Color(0, 220, 0, 70);
				BoxColor = MainColor;
				LineColor = new Color(0, 0, 0);
			} else if (color.contains("Orange")) {
				MainColor = new Color(255, 127, 0);
				ThinColor = new Color(255, 127, 0, 70);
				BoxColor = MainColor;
				LineColor = new Color(0, 0, 0);
			} else if (color.contains("Pink")) {
				MainColor = new Color(238, 18, 137);
				ThinColor = new Color(238, 18, 137, 70);
				BoxColor = MainColor;
				LineColor = new Color(0, 0, 0);
			} else if (color.contains("Purple")) {
				MainColor = new Color(104, 34, 139);
				ThinColor = new Color(104, 34, 139, 70);
				BoxColor = MainColor;
			} else if (color.contains("Red")) {
				MainColor = new Color(100, 0, 0);
				ThinColor = new Color(100, 0, 0, 70);
				ClickC = Black;
				BoxColor = MainColor;
			} else if (color.contains("White")) {
				MainColor = new Color(255, 255, 255);
				ThinColor = new Color(255, 255, 255, 70);
				LineColor = new Color(0, 0, 0);
				BoxColor = new Color(140, 140, 140);
				LineColor = new Color(0, 0, 0);
			} else if (color.contains("Yellow")) {
				MainColor = new Color(238, 201, 0);
				ThinColor = new Color(238, 201, 0, 70);
				BoxColor = MainColor;
				LineColor = new Color(0, 0, 0);
			}
			if (restBox.isSelected()) {
				rest = true;
			}
			if (powerBox.isSelected()) {
				powerFish = true;
			}
			if (paintBox.isSelected()) {
				painting = true;
			}
			if (antibanBox.isSelected()) {
				antiBanOn = true;
			}
			if (breakBox.isSelected()) {
				doBreak = true;
				if (randomBox.isSelected()) {
					randomBreaks = true;
				}
				maxBetween = Integer.parseInt(maxTimeBeBox.getText());
				minBetween = Integer.parseInt(minTimeBeBox.getText());
				maxLength = Integer.parseInt(maxBreakBox.getText());
				minLength = Integer.parseInt(minBreakBox.getText());
				if (minBetween < 1) {
					minBetween = 1;
				}
				if (minLength < 1) {
					minLength = 1;
				}
				if (maxBetween > 5000) {
					maxBetween = 5000;
				} else if (maxBetween < 6) {
					maxBetween = 6;
				}
				if (maxLength > 5000) {
					maxLength = 5000;
				} else if (maxLength < 5) {
					maxLength = 5;
				}
			}

			// Write settings
			try {
				final BufferedWriter out = new BufferedWriter(new FileWriter(settingsFile));
				out.write((powerBox.isSelected() ? true : false) + ":" // 0
						+ (restBox.isSelected() ? true : false) + ":" // 1
						+ colorBox.getSelectedIndex() + ":" // 2
						+ (antibanBox.isSelected() ? true : false) + ":" // 3
						+ (paintBox.isSelected() ? true : false) + ":" // 4
						+ (breakBox.isSelected() ? true : false) + ":" // 5
						+ (randomBox.isSelected() ? true : false) + ":" // 6
						+ maxTimeBeBox.getText() + ":" // 7
						+ minTimeBeBox.getText() + ":" // 8
						+ maxBreakBox.getText() + ":" // 9
						+ minBreakBox.getText());// 10
				out.close();
			} catch (final Exception e1) {
				log.warning("Error saving setting.");
			}
			// End write settings

			AaimistersGUI.dispose();
		}
	}

	private static interface AM {
		final RSArea AtFire = new RSArea(new RSTile(3180, 3250), new RSTile(3185, 3256));
		final RSArea AtFish = new RSArea(new RSTile(3163, 3261), new RSTile(3180, 3277));
		// final RSTile toFire1 = new RSTile(3177, 3263);
		// final RSTile toFire2 = new RSTile(3182, 3253);
		final RSTile FishT = new RSTile(3172, 3266);
		final RSTile FireT = new RSTile(3183, 3254);
	}

	private enum State {
		FISH, TOFISH, TOFIRE, COOK, DROP, ERROR
	}

	private final String[] colorstring = { "Black", "Blue", "Brown", "Cyan",
			"Green", "Lime", "Orange", "Pink", "Purple", "Red", "White",
			"Yellow" };

	AaimistersGUI g = new AaimistersGUI();
	public final File settingsFile = new File(getCacheDirectory(), "AaimistersLCookFishSettings.txt");
	private long nextBreak = System.currentTimeMillis();
	private long nextLength = 60000;
	private long totalBreakTime;
	private long lastBreakTime;
	private long nextBreakT;
	private long startTime;

	private long runTime;

	private long now;

	NumberFormat formatter = new DecimalFormat("#,###,###");
	Font Cam10 = new Font("Cambria Math", Font.BOLD, 10);

	Font Cam = new Font("Cambria Math", Font.BOLD, 12);
	Color PercentGreen = new Color(0, 163, 4, 150);
	Color PercentRed = new Color(163, 4, 0, 150);
	Color White150 = new Color(255, 255, 255, 150);
	Color White90 = new Color(255, 255, 255, 90);
	Color White = new Color(255, 255, 255);
	Color Background = new Color(219, 200, 167);
	Color UpGreen = new Color(0, 169, 0);
	// Color LineColor = new Color(0, 0, 0);
	Color ClickC = new Color(187, 0, 0);
	Color UpRed = new Color(169, 0, 0);
	Color Black = new Color(0, 0, 0);
	Color MainColor = Black;
	Color ThinColor = new Color(0, 0, 0, 70);
	Color BoxColor = Black;

	Color LineColor = White;

	final NumberFormat nf = NumberFormat.getInstance();
	private String currentStat;

	private String status = "";
	public boolean currentlyBreaking = false;
	public boolean randomBreaks = false;
	public boolean buttonStats = false;
	public boolean buttonMain = false;
	public boolean buttonInfo = false;
	public boolean antiBanOn = false;
	public boolean buttonAll = false;
	public boolean cookPaint = false;
	public boolean clickNext = false;
	public boolean powerFish = false;
	public boolean fishPaint = true;
	public boolean painting = false;
	public boolean resting = false;
	public boolean doBreak = false;
	public boolean cooking = false;
	public boolean checked = false;
	public boolean fishing = false;
	public boolean updated = false;
	public boolean check = true;
	public boolean rest = false;
	public boolean logTime;
	// Paint Buttons
	boolean xButton;
	boolean StatCO;

	boolean Main = true;

	private boolean closed;
	int fire = 2732;
	int cage = 13431;
	int fishingpool = 6996;
	int rawShrimp = 13435;
	int cookedShrimp = 13433;
	int burnShrimp = 13437;
	int xpShrimp = 10;
	int shrimpCount;
	int burntCount;
	int shrimpCookCount;
	int fishLevel;
	int cookLevel;
	int maxBetween;
	int minBetween;
	int maxLength;
	int minLength;
	int errorCount;
	int idle;
	int startFishXP;
	int currentFishXP;
	int startCookXP;
	int currentCookXP;
	int xpToFishLvl;
	int timeToFishLvl;
	int xpFishGained;
	int xpFishHour;
	int shrimpHour;
	int shrimpCHour;
	int shrimpToLvl;
	int xpToCookLvl;
	int timeToCookLvl;
	int xpCookGained;
	int xpCookHour;
	int xpGained;

	int xpHour;;

	private final Image logo = getImage("http://i88.photobucket.com/albums/k170/aaimister/AaimistersLumCookerFisher.png");

	private final Image atom = getImage("http://i88.photobucket.com/albums/k170/aaimister/Atomm.png");

	private boolean breakingCheck() {
		if (nextBreak <= System.currentTimeMillis()) {
			return true;
		}
		return false;
	}

	private void breakingNew() {
		if (randomBreaks) {
			final long varTime = random(3660000, 10800000);
			nextBreak = System.currentTimeMillis() + varTime;
			nextBreakT = varTime;
			final long varLength = random(900000, 3600000);
			nextLength = varLength;
		} else {
			final int diff = random(0, 5) * 1000 * 60;
			final long varTime = random(minBetween * 1000 * 60 + diff, maxBetween
					* 1000 * 60 - diff);
			nextBreak = System.currentTimeMillis() + varTime;
			nextBreakT = varTime;
			final int diff2 = random(0, 5) * 1000 * 60;
			final long varLength = random(minLength * 1000 * 60 + diff2, maxLength
					* 1000 * 60 - diff2);
			nextLength = varLength;
		}
		logTime = true;
	}

	public void checkPlayer() {
		if (!check) {
			return;
		}
		final RSPlayer near = playerNear();
		if (near != null) {
			if (!getMyPlayer().isMoving()) {
				if (near.getScreenLocation() != null) {
					if (mouse.getLocation() != near.getScreenLocation()) {
						mouse.move(near.getScreenLocation());
						sleep(300, 550);
					}
					mouse.click(false);
					sleep(300, 500);
					if (menu.contains("Follow")) {
						final Point menuu = menu.getLocation();
						final int Mx = menuu.y;
						final int My = menuu.y;
						final int x = Mx + random(3, 120);
						final int y = My + random(3, 98);
						mouse.move(x, y);
						sleep(2320, 3520);
						mouse.moveRandomly(100, 900);
						if (menu.isOpen()) {
							mouse.moveRandomly(100, 900);
							sleep(50);
						}
						if (menu.isOpen()) {
							mouse.moveRandomly(100, 900);
							sleep(50);
						}
					} else {
						mouse.moveRandomly(100, 900);
					}
				}
			} else {
				return;
			}
		}
	}

	public void checkXP() {
		if (!check) {
			return;
		}
		if (fishing) {
			if (game.getCurrentTab() != 2) {
				game.openTab(2);
				sleep(500, 900);
			}
			mouse.move(random(677, 729), random(270, 287));
			sleep(2800, 5500);
			game.openTab(4);
			sleep(50, 100);
			mouse.moveRandomly(50, 900);
		}
		if (cooking && !powerFish) {
			if (game.getCurrentTab() != 2) {
				game.openTab(2);
				sleep(500, 900);
			}
			mouse.move(random(678, 729), random(299, 317));
			sleep(2800, 5500);
			game.openTab(4);
			sleep(50, 100);
			mouse.moveRandomly(50, 900);
		}
	}

	private void createAndWaitforGUI() {
		if (SwingUtilities.isEventDispatchThread()) {
			g.AaimistersGUI.setVisible(true);
		} else {
			try {
				SwingUtilities.invokeAndWait(new Runnable() {

					public void run() {
						g.AaimistersGUI.setVisible(true);
					}
				});
			} catch (final InvocationTargetException ite) {
			} catch (final InterruptedException ie) {
			}
		}
		sleep(100);
		while (g.AaimistersGUI.isVisible()) {
			sleep(100);
		}
	}

	public void doAntiBan() {

		if (!antiBanOn) {
			return;
		}

		final int action = random(0, 6);

		switch (action) {
		case 0:
			if (random(1, 3) == random(1, 3)) {
				rotateCamera();
				sleep(200, 400);
			} else {
				return;
			}
			break;
		case 1:
			mouse.moveRandomly(100, 900);
			sleep(200, 400);
			break;
		case 2:
			mouse.moveRandomly(100, 900);
			sleep(200, 400);
			break;
		case 3:
			if (random(0, 7) == random(0, 7)) {
				checkXP();
				sleep(200, 400);
			} else {
				return;
			}
			break;
		case 4:
			mouse.moveRandomly(100, 900);
			sleep(200, 400);
			break;
		case 5:
			if (random(0, 4) == random(0, 4)) {
				checkPlayer();
				sleep(200, 400);
			} else {
				return;
			}
		}
	}

	private void doRest() {
		if (walking.getEnergy() < random(10, 30)) {
			if (!resting && !cooking && !fishing) {
				status = "Resting..";
				interfaces.getComponent(750, 6).doAction("Rest");
				mouse.moveSlightly();
				resting = true;
				sleep(400, 600);
			}
		}
		if (resting) {
			if (getMyPlayer().getAnimation() == -1) {
				resting = false;
			}
			if (walking.getEnergy() > random(93, 100)) {
				resting = false;
			}
			if (random(1, 7) == random(1, 7)) {
				check = false;
				doAntiBan();
			}
		}
	}

	public void drawMouse(final Graphics g) {
		final Point loc = mouse.getLocation();
		final long mpt = System.currentTimeMillis() - mouse.getPressTime();
		if (mouse.getPressTime() == -1 || mpt >= 1000) {
			g.setColor(ThinColor);
			g.drawLine(0, loc.y, 766, loc.y);
			g.drawLine(loc.x, 0, loc.x, 505);
			g.setColor(MainColor);
			g.drawLine(0, loc.y + 1, 766, loc.y + 1);
			g.drawLine(0, loc.y - 1, 766, loc.y - 1);
			g.drawLine(loc.x + 1, 0, loc.x + 1, 505);
			g.drawLine(loc.x - 1, 0, loc.x - 1, 505);
		}
		if (mpt < 1000) {
			g.setColor(ClickC);
			g.drawLine(0, loc.y, 766, loc.y);
			g.drawLine(loc.x, 0, loc.x, 505);
			g.setColor(MainColor);
			g.drawLine(0, loc.y + 1, 766, loc.y + 1);
			g.drawLine(0, loc.y - 1, 766, loc.y - 1);
			g.drawLine(loc.x + 1, 0, loc.x + 1, 505);
			g.drawLine(loc.x - 1, 0, loc.x - 1, 505);
		}
	}

	public void drawObjects(final Graphics g) {
		// Fire
		if (calc.distanceTo(AM.FireT) <= 8) {
			final RSObject cooker = goodFire();
			final RSTile t = cooker.getLocation();
			final RSTile tx = new RSTile(t.getX() + 1, t.getY());
			final RSTile ty = new RSTile(t.getX(), t.getY() + 1);
			final RSTile txy = new RSTile(t.getX() + 1, t.getY() + 1);
			calc.tileToScreen(t);
			calc.tileToScreen(tx);
			calc.tileToScreen(ty);
			calc.tileToScreen(txy);
			final Point pn = calc.tileToScreen(t, 0, 0, 0);
			final Point px = calc.tileToScreen(tx, 0, 0, 0);
			final Point py = calc.tileToScreen(ty, 0, 0, 0);
			final Point pxy = calc.tileToScreen(txy, 0, 0, 0);
			if (calc.pointOnScreen(pn) && calc.pointOnScreen(px)
					&& calc.pointOnScreen(py) && calc.pointOnScreen(pxy)) {
				g.setColor(ThinColor);
				g.fillPolygon(new int[] { py.x, pxy.x, px.x, pn.x }, new int[] {
						py.y, pxy.y, px.y, pn.y }, 4);
				g.setColor(Black);
				g.drawPolygon(new int[] { py.x, pxy.x, px.x, pn.x }, new int[] {
						py.y, pxy.y, px.y, pn.y }, 4);
			}
		}

		// Pool
		if (calc.distanceTo(AM.FishT) <= 8) {
			final RSNPC pool = fishPool();
			final RSTile t = pool.getLocation();
			final RSTile tx = new RSTile(t.getX() + 1, t.getY());
			final RSTile ty = new RSTile(t.getX(), t.getY() + 1);
			final RSTile txy = new RSTile(t.getX() + 1, t.getY() + 1);
			calc.tileToScreen(t);
			calc.tileToScreen(tx);
			calc.tileToScreen(ty);
			calc.tileToScreen(txy);
			final Point pn = calc.tileToScreen(t, 0, 0, 0);
			final Point px = calc.tileToScreen(tx, 0, 0, 0);
			final Point py = calc.tileToScreen(ty, 0, 0, 0);
			final Point pxy = calc.tileToScreen(txy, 0, 0, 0);
			if (calc.pointOnScreen(pn) && calc.pointOnScreen(px)
					&& calc.pointOnScreen(py) && calc.pointOnScreen(pxy)) {
				g.setColor(ThinColor);
				g.fillPolygon(new int[] { py.x, pxy.x, px.x, pn.x }, new int[] {
						py.y, pxy.y, px.y, pn.y }, 4);
				g.setColor(Black);
				g.drawPolygon(new int[] { py.x, pxy.x, px.x, pn.x }, new int[] {
						py.y, pxy.y, px.y, pn.y }, 4);
			}
		}
	}

	private void drop() {
		try {
			status = "Dropping..";
			mouse.setSpeed(random(8, 13));
			inventory.dropAllExcept(true, cage);
			sleep(100, 300);
		} catch (final Exception e) {

		}
	}

	private RSNPC fishPool() {
		return npcs.getNearest(new Filter<RSNPC>() {
			public boolean accept(final RSNPC g) {
				return g.getID() == fishingpool
						&& AM.AtFish.contains(g.getLocation());
			}
		});
	}

	String formatTime(final int milliseconds) {
		final long t_seconds = milliseconds / 1000;
		final long t_minutes = t_seconds / 60;
		final long t_hours = t_minutes / 60;
		final int seconds = (int) (t_seconds % 60);
		final int minutes = (int) (t_minutes % 60);
		final int hours = (int) (t_hours % 60);
		return nf.format(hours) + ":" + nf.format(minutes) + ":"
				+ nf.format(seconds);
	}

	private String getGained() {
		if (Main) {
			return " (" + fishLevel + ")";
		} else {
			return " (" + cookLevel + ")";
		}
	}

	private Image getImage(final String url) {
		try {
			return ImageIO.read(new URL(url));
		} catch (final IOException e) {
			return null;
		}
	}

	private int getStat() {
		if (Main) {
			currentStat = " Fishing";
			return 10;
		} else {
			currentStat = " Cooking";
			return 7;
		}
	}

	private State getState() {
		if (!powerFish) {
			if (inventory.contains(cage) && !inventory.isFull()
					&& !inventory.containsOneOf(cookedShrimp, burnShrimp)) {
				if (AM.AtFish.contains(getMyPlayer().getLocation())) {
					return State.FISH;
				} else {
					status = "Walking to Fish..";
					return State.TOFISH;
				}
			} else if (inventory.containsOneOf(rawShrimp) && inventory.isFull()) {
				if (AM.AtFire.contains(getMyPlayer().getLocation())) {
					return State.COOK;
				} else {
					status = "Walking to Fire..";
					return State.TOFIRE;
				}
			} else if (inventory.containsOneOf(cookedShrimp, burnShrimp)
					&& !inventory.contains(rawShrimp)) {
				status = "Dropping..";
				return State.DROP;
			} else {
				return State.ERROR;
			}
		} else {
			if (inventory.contains(cage) && !inventory.isFull()) {
				status = "Fishing..";
				return State.FISH;
			} else if (inventory.isFull()) {
				status = "Dropping..";
				return State.DROP;
			} else {
				return State.ERROR;
			}
		}
	}

	public double getVersion() {
		return 1.07;
	}

	private RSObject goodFire() {
		return objects.getNearest(new Filter<RSObject>() {
			public boolean accept(final RSObject g) {
				return g.getID() == fire && AM.AtFire.contains(g.getLocation());
			}
		});
	}

	private String Location() {
		final RSTile locationP = getMyPlayer().getLocation();
		if (AM.AtFire.contains(locationP)) {
			return "Fire Area";
		} else if (AM.AtFish.contains(locationP)) {
			return "Fishing Area";
		} else if (calc.distanceTo(AM.FireT) > 100) {
			if (!game.isLoggedIn()) {
				return "Login Screen";
			} else {
				return "Unknown";
			}
		} else {
			return "Lumbridge";
		}
	}

	@Override
	public int loop() {
		if (breakingCheck() && doBreak) {
			status = "Breaking...";
			final long endTime = System.currentTimeMillis() + nextLength;
			totalBreakTime += nextLength + 5000;
			lastBreakTime = totalBreakTime - (nextLength + 5000);
			currentlyBreaking = true;
			while (game.isLoggedIn()) {
				game.logout(false);
				sleep(50);
			}
			log("Taking a break for " + formatTime((int) nextLength));
			while (System.currentTimeMillis() < endTime
					&& currentlyBreaking == true) {
				sleep(1000);
			}
			currentlyBreaking = false;
			while (!game.isLoggedIn()) {
				try {
					breakingNew();
					game.login();
				} catch (final Exception e) {
					return 10;
				}
				sleep(50);
			}
			return 10;
		}

		if (!game.isLoggedIn()) {
			status = "Breaking..";
			return 3000;
		}

		if (startTime == 0 && skills.getCurrentLevel(10) != 0
				&& skills.getCurrentLevel(7) != 0) {
			startTime = System.currentTimeMillis();
			startFishXP = skills.getCurrentExp(10);
			currentFishXP = skills.getExpToNextLevel(10);
			startCookXP = skills.getCurrentExp(7);
			currentCookXP = skills.getExpToNextLevel(7);
		}

		if (logTime) {
			log("Next Break In: " + formatTime((int) nextBreakT) + " For: "
					+ formatTime((int) nextLength) + ".");
			logTime = false;
		}

		mouse.setSpeed(random(5, 12));
		setCamera();
		setRun();

		if (resting) {
			if (random(0, 7) == random(0, 7)) {
				check = false;
				doAntiBan();
			}
			if (getMyPlayer().getAnimation() == -1 && !cooking && !fishing) {
				doRest();
				sleep(200, 800);
			}
			return random(100, 250);
		}

		switch (getState()) {
		case FISH:
			try {
				final RSNPC pool = fishPool();
				if (idle > 2) {
					fishing = false;
					idle = 0;
				}
				if (interfaces.canContinue()) {
					interfaces.clickContinue();
					idle = 0;
				}
				if (calc.tileOnScreen(pool.getLocation())
						&& AM.AtFish.contains(getMyPlayer().getLocation())) {
					status = "Fishing..";
					if (getMyPlayer().getAnimation() == -1 && !fishing) {
						pool.doAction("Cage");
						fishing = true;
						return random(1000, 1200);
					} else {
						if (getMyPlayer().getAnimation() == -1) {
							idle++;
						}
						if (random(0, 6) == random(0, 6)) {
							check = true;
							doAntiBan();
						}
						return random(200, 500);
					}
				} else {
					stepTo(pool.getLocation());
					sleep(50);
				}
			} catch (final Exception e) {

			}
			break;
		case TOFISH:
			idle = 0;
			cooking = false;
			fishing = false;
			stepTo(AM.FishT);
			sleep(50);
			break;
		case TOFIRE:
			idle = 0;
			fishing = false;
			cooking = false;
			stepTo(AM.FireT);
			sleep(50);
			break;
		case COOK:
			try {
				status = "Cooking..";
				if (idle > 4) {
					cooking = false;
					idle = 0;
				}
				if (interfaces.canContinue()) {
					interfaces.clickContinue();
					idle = 0;
				}
				final RSObject cooker = goodFire();
				if (calc.tileOnScreen(cooker.getLocation())
						&& calc.distanceTo(AM.FireT) <= 4) {
					status = "Cooking..";
					if (inventory.getSelectedItem() == null && !cooking
							&& !interfaces.getComponent(905, 14).isValid()) {
						final RSItem shrimp = inventory.getItem(rawShrimp);
						shrimp.doAction("Use");
						return random(200, 800);
					}
					if (!interfaces.getComponent(905, 14).isValid()) {
						if (inventory.getSelectedItem() != null && !cooking) {
							cooker.doAction("Use");
							return random(1150, 1300);
						}
					} else {
						final RSComponent doCook = interfaces.getComponent(905, 14);
						doCook.doAction("Cook");
						cooking = true;
						return random(1000, 1200);
					}
					if (cooking) {
						if (random(0, 6) == random(0, 6)) {
							check = true;
							doAntiBan();
						}
						if (getMyPlayer().getAnimation() == -1) {
							idle++;
						}
					}
				} else {
					stepTo(cooker.getLocation());
					sleep(50);
				}
			} catch (final Exception e) {

			}
			break;
		case DROP:
			idle = 0;
			drop();
			break;
		case ERROR:

			break;
		}
		return random(300, 600);
	}

	public void main(final Graphics g) {
		final long totalTime = System.currentTimeMillis() - startTime;
		final String formattedTime = formatTime((int) totalTime);
		g.setColor(LineColor);
		g.drawString("Time running: " + formattedTime, 63, 390);
		g.drawString("Location: " + Location(), 63, 404);
		g.drawString("Status: " + status, 63, 418);
		g.drawString("Current Fish: Crayfish", 63, 433);
		g.drawString("Total XP: " + formatter.format((long) xpGained), 63, 447);
		g.drawString("Total XP/h: " + formatter.format((long) xpHour), 63, 463);
	}

	public void messageReceived(final MessageEvent e) {
		if (e.getMessage().contains("You've just advanced a Coo")) {
			cookLevel++;
		}
		if (e.getMessage().contains("You've just advanced a Fish")) {
			fishLevel++;
		}
		if (e.getMessage().contains("You successfully cook")) {
			idle = 0;
			shrimpCookCount++;
		}
		if (e.getMessage().contains("You accidently burn")) {
			idle = 0;
			burntCount++;
		}
		if (e.getMessage().contains("You catch")) {
			idle = 0;
			shrimpCount++;
		}
	}

	public void mouseClicked(final MouseEvent e) {

	}

	public void mouseEntered(final MouseEvent e) {
	}

	public void mouseExited(final MouseEvent e) {
	}

	public void mousePressed(final MouseEvent e) {
		// X Button
		if (e.getX() >= 497 && e.getX() < 497 + 16 && e.getY() >= 344
				&& e.getY() < 344 + 16) {
			if (!xButton) {
				xButton = true;
			} else {
				xButton = false;
			}
		}
		// Next Button
		if (e.getX() >= 478 && e.getX() < 478 + 16 && e.getY() >= 413
				&& e.getY() < 413 + 14) {
			if (Main) {
				Main = false;
				StatCO = true;
			} else if (StatCO) {
				StatCO = false;
				Main = true;
			}
		}
		// Prev Button
		if (e.getX() >= 25 && e.getX() < 25 + 16 && e.getY() >= 413
				&& e.getY() < 413 + 14) {
			if (Main) {
				Main = false;
				StatCO = true;
			} else if (StatCO) {
				StatCO = false;
				Main = true;
			}
		}
	}

	public void mouseReleased(final MouseEvent e) {
	}

	private RSPlayer myPlayer() {
		final String myName = players.getMyPlayer().getName();
		return players.getNearest(new Filter<RSPlayer>() {
			public boolean accept(final RSPlayer p) {
				return p.getName() != myName
						&& (AM.AtFish.contains(p.getLocation()) || AM.AtFire.contains(p.getLocation()));
			}
		});
	}

	public void onFinish() {
		runTime = System.currentTimeMillis() - startTime - totalBreakTime;
		final long totalTime = System.currentTimeMillis() - startTime;
		final String formattedTime = formatTime((int) totalTime);
		log("Thanks for using Aaimister's Lumbridge Cooker & Fisher!");
		if (!powerFish) {
			log("In " + formattedTime + " You cooked "
					+ formatter.format(shrimpCookCount)
					+ " unburnt Crayfish & Caught "
					+ formatter.format(shrimpCount) + " Crayfish!");
			log("You Gained: " + cookLevel + " level(s) in Cooking & "
					+ fishLevel + " level(s) in Fishing!");
		} else {
			log("In " + formattedTime + " You Caught "
					+ formatter.format(shrimpCount) + " Crayfish!");
			log("You Gained: " + fishLevel + " level(s) in Fishing!");
		}
	}

	public void onRepaint(final Graphics g) {
		final long totalTime = System.currentTimeMillis() - startTime;

		if (!currentlyBreaking) {
			runTime = System.currentTimeMillis() - startTime - totalBreakTime;
			now = totalTime;
			checked = false;
		} else {
			if (!game.isLoggedIn()) {
				if (!checked) {
					runTime = now - lastBreakTime;
					checked = true;
				}
			}
		}

		if (startTime != 0) {
			currentFishXP = skills.getExpToNextLevel(10);

			xpToFishLvl = skills.getExpToNextLevel(10);
			xpFishGained = skills.getCurrentExp(10) - startFishXP;
			xpFishHour = (int) (3600000.0 / (double) runTime * xpFishGained);
			if (xpFishHour != 0) {
				timeToFishLvl = (int) ((double) xpToFishLvl
						/ (double) xpFishHour * 3600000.0);
			}
			shrimpHour = (int) (3600000.0 / (double) runTime * shrimpCount);

			currentCookXP = skills.getExpToNextLevel(7);

			xpToCookLvl = skills.getExpToNextLevel(7);
			xpCookGained = skills.getCurrentExp(7) - startCookXP;
			xpCookHour = (int) (3600000.0 / (double) runTime * xpCookGained);
			if (xpCookHour != 0) {
				timeToCookLvl = (int) ((double) xpToCookLvl
						/ (double) xpCookHour * 3600000.0);
			}
			shrimpCHour = (int) (3600000.0 / (double) runTime * shrimpCookCount);

			xpGained = xpFishGained + xpCookGained;
			xpHour = (int) (3600000.0 / (double) runTime * xpGained);
		}

		if (painting) {
			((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			((Graphics2D) g).setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
			((Graphics2D) g).setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		}

		if (!xButton) {
			// Background
			g.setColor(MainColor);
			g.fillRect(6, 344, 507, 129);
			g.setColor(LineColor);
			g.drawRect(6, 344, 507, 129);
			// Logo
			g.drawImage(logo, 6, 348, null);
			g.drawImage(atom, 40, 358, null);
			g.setColor(LineColor);
			g.setFont(Cam10);
			g.drawString("By Aaimister © " + getVersion(), 379, 369);
			// Next Button
			g.setColor(BoxColor);
			g.fillRect(478, 413, 16, 14);
			g.setColor(LineColor);
			g.setFont(Cam);
			g.drawString(">", 481, 424);
			g.drawRect(478, 413, 16, 14);
			// Shadow
			g.setColor(White90);
			g.fillRect(478, 413, 16, 7);
			// Prev Button
			g.setColor(BoxColor);
			g.fillRect(25, 413, 16, 14);
			g.setColor(LineColor);
			g.setFont(Cam);
			g.drawString("<", 28, 424);
			g.drawRect(25, 413, 16, 14);
			// Shadow
			g.setColor(White90);
			g.fillRect(25, 413, 16, 7);
			// Main Box
			g.setColor(BoxColor);
			g.fillRect(59, 374, 401, 95);
			g.setColor(White90);
			g.fillRect(59, 374, 401, 46);
			// Text
			if (Main) {
				// Column 1
				main(g);
				// Column 2
				g.drawString("Crayfish Caught: "
						+ formatter.format((long) shrimpCount), 264, 390);
				g.drawString("Crayfish / Hour: "
						+ formatter.format((long) shrimpHour), 264, 404);
				g.drawString("Total XP: "
						+ formatter.format((long) xpFishGained), 264, 418);
				g.drawString("XP / Hour: "
						+ formatter.format((long) xpFishHour), 264, 433);
				g.drawString("XP to Lvl: "
						+ formatter.format((long) xpToFishLvl), 264, 447);
				g.drawString("Level In: " + formatTime(timeToFishLvl), 264, 463);
			}
			if (StatCO) {
				// Column 1
				main(g);
				// Column 2
				g.drawString("Crayfish Cooked: "
						+ formatter.format((long) shrimpCookCount), 264, 390);
				g.drawString("Crayfish / Hour: "
						+ formatter.format((long) shrimpCHour), 264, 404);
				g.drawString("Total XP: "
						+ formatter.format((long) xpCookGained), 264, 418);
				g.drawString("XP / Hour: "
						+ formatter.format((long) xpCookHour), 264, 433);
				g.drawString("XP to Lvl: "
						+ formatter.format((long) xpToCookLvl), 264, 447);
				g.drawString("Level In: " + formatTime(timeToCookLvl), 264, 463);
			}
			// % Bar
			g.setColor(MainColor);
			g.fillRect(4, 318, 512, 20);
			g.setColor(Black);
			g.fillRect(6, 320, 508, 16);
			g.setColor(PercentRed);
			g.fillRect(6, 320, 508, 16);
			g.setColor(PercentGreen);
			g.fillRect(6, 320, skills.getPercentToNextLevel(getStat())
					* (508 / 100), 16);
			g.setColor(White);
			g.setFont(Cam);
			g.drawString("" + skills.getPercentToNextLevel(getStat())
					+ "% to lvl " + (skills.getCurrentLevel(getStat()) + 1)
					+ currentStat + getGained(), 194, 332);
			// Shadow
			g.setColor(White90);
			g.fillRect(4, 318, 512, 10);
			// X
			g.setColor(LineColor);
			g.setFont(Cam);
			g.drawString("X", 501, 357);
			// Main Box Shadow
			g.setColor(LineColor);
			g.drawRect(59, 374, 401, 95);
			g.drawLine(260, 380, 260, 465);
		} else {
			// X Button
			g.setColor(MainColor);
			g.fillRect(497, 344, 16, 16);
			g.setColor(LineColor);
			g.drawRect(497, 344, 16, 16);
			// X
			g.setColor(LineColor);
			g.setFont(Cam);
			g.drawString("O", 501, 357);
			// Shadow
			g.setColor(White90);
			g.fillRect(497, 344, 17, 8);
		}

		// Mouse
		drawMouse(g);

		// Objects
		drawObjects(g);
	}

	public boolean onStart() {
		status = "Starting up..";

		URLConnection url = null;
		BufferedReader in = null;
		BufferedWriter out = null;

		// Check right away...
		try {
			// Open the version text file
			url = new URL("http://aaimister.webs.com/scripts/AaimistersLumbridgeCookerFisherVersion.txt").openConnection();
			// Create an input stream for it
			in = new BufferedReader(new InputStreamReader(url.getInputStream()));
			// Check if the current version is outdated
			if (Double.parseDouble(in.readLine()) > getVersion()) {
				if (JOptionPane.showConfirmDialog(null, "Update found. Do you want to update?") == 0) {
					// If so, allow the user to choose the file to be updated.
					JOptionPane.showMessageDialog(null, "Please choose 'AaimistersLumbridgeCookerFisher.java' in your scripts folder and hit 'Open'");
					final JFileChooser fc = new JFileChooser();
					// Make sure "Open" was clicked.
					if (fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
						// If so, set up the URL for the .java file and set up
						// the IO.
						url = new URL("http://aaimister.webs.com/scripts/AaimistersLumbridgeCookerFisher.java").openConnection();
						in = new BufferedReader(new InputStreamReader(url.getInputStream()));
						out = new BufferedWriter(new FileWriter(fc.getSelectedFile().getPath()));
						String inp;
						/*
						 * Until we reach the end of the file, write the next
						 * line in the file and add a new line. Then flush the
						 * buffer to ensure we lose no data in the process.
						 */
						while ((inp = in.readLine()) != null) {
							out.write(inp);
							out.newLine();
							out.flush();
						}
						// Notify the user that the script has been updated, and
						// a recompile and reload is needed.
						log("Script successfully downloaded. Please recompile and reload your scripts!");
						return false;
					} else {
						log("Update canceled");
					}
				} else {
					log("Update canceled");
				}
			} else {
				JOptionPane.showMessageDialog(null, "You have the latest version.");// User
																					// has
																					// the
																					// latest
																					// version.
																					// Tell
																					// them!
				if (in != null) {
					in.close();
				}
			}
		} catch (final IOException e) {
			log("Problem getting version. Please visit the forums.");
			return false; // Return false if there was a problem
		}

		try {
			settingsFile.createNewFile();
		} catch (final IOException ignored) {
		}

		createAndWaitforGUI();
		if (closed) {
			log.severe("The GUI window was closed!");
			return false;
		}

		startFishXP = skills.getCurrentExp(10);
		currentFishXP = skills.getExpToNextLevel(10);
		startCookXP = skills.getCurrentExp(7);
		currentCookXP = skills.getExpToNextLevel(7);
		if (doBreak) {
			breakingNew();
		}

		return true;
	}

	private RSPlayer playerNear() {
		final RSPlayer me = myPlayer();
		return me != null ? me : players.getNearest(new Filter<RSPlayer>() {
			public boolean accept(final RSPlayer p) {
				return (AM.AtFish.contains(p.getLocation()) || AM.AtFire.contains(p.getLocation()))
						&& !p.isMoving() && p.isOnScreen();
			}
		});
	}

	public void rotateCamera() {
		if (!antiBanOn) {
			return;
		}
		final char[] LR = new char[] { KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT };
		final char[] UD = new char[] { KeyEvent.VK_DOWN, KeyEvent.VK_UP };
		final char[] LRUD = new char[] { KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT,
				KeyEvent.VK_UP, KeyEvent.VK_UP };
		final int randomLR = random(0, 2);
		final int randomUD = random(0, 2);
		final int randomAll = random(0, 4);
		if (random(0, 3) == 0) {
			keyboard.pressKey(LR[randomLR]);
			sleepCR(random(2, 9));
			keyboard.pressKey(UD[randomUD]);
			sleepCR(random(6, 10));
			keyboard.releaseKey(UD[randomUD]);
			sleepCR(random(2, 7));
			keyboard.releaseKey(LR[randomLR]);
		} else {
			keyboard.pressKey(LRUD[randomAll]);
			if (randomAll > 1) {
				sleepCR(random(6, 11));
			} else {
				sleepCR(random(9, 12));
			}
			keyboard.releaseKey(LRUD[randomAll]);
		}
	}

	private void setCamera() {
		if (camera.getPitch() < 10) {
			camera.setPitch(true);
			sleep(1000, 1600);
		}
	}

	private void setRun() {
		if (!walking.isRunEnabled()) {
			if (walking.getEnergy() >= random(45, 100)) {
				walking.setRun(true);
				sleep(1000, 1600);
			}
		} else {
			if (rest) {
				doRest();
			}
		}
	}

	private boolean sleepCR(final int amtOfHalfSecs) {
		for (int x = 0; x < amtOfHalfSecs + 1; x++) {
			sleep(random(48, 53));
		}
		return true;
	}

	private boolean stepTo(final RSTile tile) {
		final RSPath walkPath = walking.getPath(tile.randomize(1, 1));
		try {
			if (walkPath != null) {
				if (!getMyPlayer().isMoving()
						|| calc.distanceTo(walking.getDestination()) < 4) {
					return walkPath.traverse();
				}
			}
		} catch (final Exception e) {

		}
		return false;
	}
}
