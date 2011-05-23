/**
 * @author Aaimister
 * @version 1.11 ©2010-2011 Aaimister, No one except Aaimister has the right to
 *          modify and/or spread this script without the permission of Aaimister.
 *          I'm not held responsible for any damage that may occur to your
 *          property.
 */

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.RenderingHints;
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
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import org.rsbot.event.events.MessageEvent;
import org.rsbot.event.listeners.MessageListener;
import org.rsbot.event.listeners.PaintListener;
import org.rsbot.gui.AccountManager;
import org.rsbot.script.Script;
import org.rsbot.script.ScriptManifest;
import org.rsbot.script.util.Filter;
import org.rsbot.script.wrappers.RSArea;
import org.rsbot.script.wrappers.RSGroundItem;
import org.rsbot.script.wrappers.RSItem;
import org.rsbot.script.wrappers.RSNPC;
import org.rsbot.script.wrappers.RSObject;
import org.rsbot.script.wrappers.RSPath;
import org.rsbot.script.wrappers.RSTile;

@ScriptManifest(authors = { "Aaimister" }, name = "Aaimister's Roach Killer", keywords = "Combat", version = 1.11, description = "Kills roaches in Edgville.", website = "http://www.powerbot.org/vb/showthread.php?t=769805", requiresVersion = 244)
public class AaimistersRoaches extends Script implements PaintListener,
		MouseListener, MessageListener {

	public class AaimistersGUI {

		private JFrame AaimistersGUI;

		private JPanel contentPane;

		private JComboBox colorBox;
		private JCheckBox antibanBox;
		private JCheckBox paintBox;
		private JCheckBox breakBox;
		private JCheckBox randomBox;
		private JCheckBox freeBox;
		private JCheckBox room2Box;
		private JSpinner healText;
		private JSpinner foodText;
		private JSpinner withText;
		private JSpinner maxTimeBeBox;
		private JSpinner minTimeBeBox;
		private JSpinner maxBreakBox;
		private JSpinner minBreakBox;
		private JButton submit;

		private AaimistersGUI() {
			initComponents();
		}

		public void initComponents() {
			AaimistersGUI = new JFrame();
			contentPane = new JPanel();
			colorBox = new JComboBox();
			antibanBox = new JCheckBox();
			paintBox = new JCheckBox();
			breakBox = new JCheckBox();
			room2Box = new JCheckBox();
			freeBox = new JCheckBox();
			randomBox = new JCheckBox();
			healText = new JSpinner();
			foodText = new JSpinner();
			withText = new JSpinner();
			maxTimeBeBox = new JSpinner();
			minTimeBeBox = new JSpinner();
			maxBreakBox = new JSpinner();
			minBreakBox = new JSpinner();
			submit = new JButton();

			AaimistersGUI.setResizable(false);
			AaimistersGUI.setTitle("Aaimister's Roach Killer");
			AaimistersGUI.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			AaimistersGUI.setBounds(100, 100, 450, 344);
			contentPane = new JPanel();
			contentPane.setBackground(UIManager.getColor("Button.background"));
			contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
			AaimistersGUI.setContentPane(contentPane);
			// Listeners
			AaimistersGUI.addWindowListener(new WindowAdapter() {
				public void windowClosing(final WindowEvent e) {
					closed = true;
				}
			});

			final JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);

			final JLabel lblAaimistersRoachKiller = new JLabel("Aaimister's Roach Killer v1.11");
			lblAaimistersRoachKiller.setHorizontalAlignment(SwingConstants.CENTER);
			lblAaimistersRoachKiller.setFont(new Font("Comic Sans MS", Font.PLAIN, 20));

			submit.setText("Start");
			submit.setFont(new Font("Comic Sans MS", Font.PLAIN, 15));
			submit.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(final ActionEvent e) {
					submitActionPerformed(e);
				}
			});
			final GroupLayout gl_contentPane = new GroupLayout(contentPane);
			gl_contentPane.setHorizontalGroup(gl_contentPane.createParallelGroup(Alignment.LEADING).addComponent(lblAaimistersRoachKiller, GroupLayout.DEFAULT_SIZE, 434, Short.MAX_VALUE).addComponent(tabbedPane, GroupLayout.DEFAULT_SIZE, 434, Short.MAX_VALUE).addGroup(gl_contentPane.createSequentialGroup().addGap(169).addComponent(submit, GroupLayout.PREFERRED_SIZE, 95, GroupLayout.PREFERRED_SIZE).addContainerGap(170, Short.MAX_VALUE)));
			gl_contentPane.setVerticalGroup(gl_contentPane.createParallelGroup(Alignment.LEADING).addGroup(gl_contentPane.createSequentialGroup().addComponent(lblAaimistersRoachKiller, GroupLayout.PREFERRED_SIZE, 39, GroupLayout.PREFERRED_SIZE).addPreferredGap(ComponentPlacement.RELATED).addComponent(tabbedPane, GroupLayout.PREFERRED_SIZE, 214, GroupLayout.PREFERRED_SIZE).addPreferredGap(ComponentPlacement.UNRELATED).addComponent(submit).addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));

			final JPanel panel = new JPanel();
			tabbedPane.addTab("General", null, panel, null);

			room2Box.setText("Room Two");

			antibanBox.setText("Anti Ban");
			antibanBox.setSelected(true);

			freeBox.setText("Only Attack Free Roaches");

			paintBox.setText("Anti Aliasing");
			paintBox.setSelected(true);

			final JLabel lblFoodId = new JLabel("Food ID:");
			lblFoodId.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));

			final JLabel lblEatWhenBelow = new JLabel("Eat When Below:");
			lblEatWhenBelow.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));

			final JLabel lblPaintColor = new JLabel("Paint Color:");
			lblPaintColor.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));

			healText.setModel(new SpinnerNumberModel(new Integer(200), null, null, new Integer(1)));

			colorBox.setModel(new DefaultComboBoxModel(colorstring));

			foodText.setModel(new SpinnerNumberModel(new Integer(379), null, null, new Integer(1)));

			final JLabel lblWitdraw = new JLabel("Witdraw:");
			lblWitdraw.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));

			withText.setModel(new SpinnerNumberModel(new Integer(20), null, null, new Integer(1)));
			final GroupLayout gl_panel = new GroupLayout(panel);
			gl_panel.setHorizontalGroup(gl_panel.createParallelGroup(Alignment.LEADING).addGroup(gl_panel.createSequentialGroup().addContainerGap().addGroup(gl_panel.createParallelGroup(Alignment.LEADING).addGroup(gl_panel.createSequentialGroup().addComponent(freeBox).addGap(18).addComponent(lblWitdraw)).addGroup(gl_panel.createSequentialGroup().addGroup(gl_panel.createParallelGroup(Alignment.LEADING).addComponent(room2Box).addComponent(antibanBox).addComponent(paintBox)).addGap(85).addGroup(gl_panel.createParallelGroup(Alignment.LEADING).addGroup(Alignment.TRAILING, gl_panel.createSequentialGroup().addGroup(gl_panel.createParallelGroup(Alignment.TRAILING).addGroup(Alignment.LEADING, gl_panel.createSequentialGroup().addComponent(lblEatWhenBelow).addPreferredGap(ComponentPlacement.RELATED, 46, Short.MAX_VALUE).addComponent(healText, GroupLayout.PREFERRED_SIZE, 52, GroupLayout.PREFERRED_SIZE)).addGroup(Alignment.LEADING, gl_panel.createSequentialGroup().addComponent(lblPaintColor).addPreferredGap(ComponentPlacement.RELATED, 40, Short.MAX_VALUE).addComponent(colorBox, GroupLayout.PREFERRED_SIZE, 94, GroupLayout.PREFERRED_SIZE))).addGap(36)).addGroup(gl_panel.createSequentialGroup().addComponent(lblFoodId).addGap(46).addGroup(gl_panel.createParallelGroup(Alignment.LEADING).addComponent(withText, GroupLayout.PREFERRED_SIZE, 54, GroupLayout.PREFERRED_SIZE).addComponent(foodText, GroupLayout.PREFERRED_SIZE, 54, GroupLayout.PREFERRED_SIZE)))))).addContainerGap()));
			gl_panel.setVerticalGroup(gl_panel.createParallelGroup(Alignment.TRAILING).addGroup(gl_panel.createSequentialGroup().addGap(21).addGroup(gl_panel.createParallelGroup(Alignment.LEADING).addGroup(gl_panel.createSequentialGroup().addComponent(room2Box).addGap(18).addGroup(gl_panel.createParallelGroup(Alignment.BASELINE).addComponent(antibanBox).addComponent(lblPaintColor).addComponent(colorBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)).addGap(18).addGroup(gl_panel.createParallelGroup(Alignment.BASELINE).addComponent(paintBox).addComponent(lblFoodId).addComponent(foodText, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))).addGroup(gl_panel.createParallelGroup(Alignment.BASELINE).addComponent(lblEatWhenBelow).addComponent(healText, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))).addGroup(gl_panel.createParallelGroup(Alignment.LEADING).addGroup(gl_panel.createSequentialGroup().addGap(18).addGroup(gl_panel.createParallelGroup(Alignment.BASELINE).addComponent(freeBox).addComponent(lblWitdraw))).addGroup(gl_panel.createSequentialGroup().addGap(18).addComponent(withText, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))).addGap(22)));
			panel.setLayout(gl_panel);

			final JPanel panel_1 = new JPanel();
			tabbedPane.addTab("Breaks", null, panel_1, null);

			breakBox.setText("Custom Breaks");

			randomBox.setText("Random Breaks");

			final JLabel lblTimeBetweenBreaks = new JLabel("Time Between Breaks:");
			lblTimeBetweenBreaks.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));

			minTimeBeBox.setModel(new SpinnerNumberModel(new Integer(60), null, null, new Integer(1)));

			final JLabel lblMins = new JLabel("mins");
			lblMins.setFont(new Font("Comic Sans MS", Font.PLAIN, 12));

			final JLabel lblTo = new JLabel("to");
			lblTo.setFont(new Font("Comic Sans MS", Font.PLAIN, 12));

			maxTimeBeBox.setModel(new SpinnerNumberModel(new Integer(120), null, null, new Integer(1)));

			final JLabel label = new JLabel("mins");
			label.setFont(new Font("Comic Sans MS", Font.PLAIN, 12));

			final JLabel lblBreakLengths = new JLabel("Break Lengths:");
			lblBreakLengths.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));

			minBreakBox.setModel(new SpinnerNumberModel(new Integer(25), null, null, new Integer(1)));

			final JLabel label_1 = new JLabel("mins");
			label_1.setFont(new Font("Comic Sans MS", Font.PLAIN, 12));

			final JLabel label_2 = new JLabel("to");
			label_2.setFont(new Font("Comic Sans MS", Font.PLAIN, 12));

			maxBreakBox.setModel(new SpinnerNumberModel(new Integer(70), null, null, new Integer(1)));

			final JLabel label_3 = new JLabel("mins");
			label_3.setFont(new Font("Comic Sans MS", Font.PLAIN, 12));
			final GroupLayout gl_panel_1 = new GroupLayout(panel_1);
			gl_panel_1.setHorizontalGroup(gl_panel_1.createParallelGroup(Alignment.LEADING).addGroup(gl_panel_1.createSequentialGroup().addGap(42).addComponent(breakBox).addPreferredGap(ComponentPlacement.RELATED, 56, Short.MAX_VALUE).addComponent(randomBox).addGap(62)).addGroup(gl_panel_1.createSequentialGroup().addContainerGap().addComponent(lblTimeBetweenBreaks).addContainerGap(269, Short.MAX_VALUE)).addGroup(gl_panel_1.createSequentialGroup().addContainerGap().addComponent(lblBreakLengths).addContainerGap(371, Short.MAX_VALUE)).addGroup(Alignment.TRAILING, gl_panel_1.createSequentialGroup().addGroup(gl_panel_1.createParallelGroup(Alignment.TRAILING).addGroup(gl_panel_1.createSequentialGroup().addContainerGap().addComponent(minBreakBox, GroupLayout.PREFERRED_SIZE, 55, GroupLayout.PREFERRED_SIZE).addGap(5).addComponent(label_1, GroupLayout.PREFERRED_SIZE, 24, GroupLayout.PREFERRED_SIZE).addGap(60).addComponent(label_2, GroupLayout.PREFERRED_SIZE, 12, GroupLayout.PREFERRED_SIZE).addGap(108).addComponent(maxBreakBox, GroupLayout.PREFERRED_SIZE, 55, GroupLayout.PREFERRED_SIZE).addGap(5).addComponent(label_3, GroupLayout.PREFERRED_SIZE, 24, GroupLayout.PREFERRED_SIZE)).addGroup(gl_panel_1.createSequentialGroup().addGap(41).addGroup(gl_panel_1.createSequentialGroup().addComponent(minTimeBeBox, GroupLayout.PREFERRED_SIZE, 55, GroupLayout.PREFERRED_SIZE).addPreferredGap(ComponentPlacement.RELATED).addComponent(lblMins).addGap(60).addComponent(lblTo)).addPreferredGap(ComponentPlacement.RELATED, 56, Short.MAX_VALUE).addComponent(maxTimeBeBox, GroupLayout.PREFERRED_SIZE, 55, GroupLayout.PREFERRED_SIZE).addPreferredGap(ComponentPlacement.RELATED).addComponent(label, GroupLayout.PREFERRED_SIZE, 24, GroupLayout.PREFERRED_SIZE))).addGap(40)));
			gl_panel_1.setVerticalGroup(gl_panel_1.createParallelGroup(Alignment.LEADING).addGroup(gl_panel_1.createSequentialGroup().addContainerGap().addGroup(gl_panel_1.createParallelGroup(Alignment.BASELINE).addComponent(breakBox).addComponent(randomBox)).addPreferredGap(ComponentPlacement.UNRELATED).addComponent(lblTimeBetweenBreaks).addPreferredGap(ComponentPlacement.UNRELATED).addGroup(gl_panel_1.createParallelGroup(Alignment.TRAILING).addComponent(label, GroupLayout.PREFERRED_SIZE, 18, GroupLayout.PREFERRED_SIZE).addGroup(gl_panel_1.createParallelGroup(Alignment.BASELINE).addComponent(lblTo).addComponent(lblMins)).addComponent(minTimeBeBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE).addComponent(maxTimeBeBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)).addGap(18).addComponent(lblBreakLengths).addPreferredGap(ComponentPlacement.UNRELATED).addGroup(gl_panel_1.createParallelGroup(Alignment.LEADING).addComponent(minBreakBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE).addGroup(gl_panel_1.createSequentialGroup().addGap(2).addComponent(label_1, GroupLayout.PREFERRED_SIZE, 18, GroupLayout.PREFERRED_SIZE)).addGroup(gl_panel_1.createSequentialGroup().addGap(2).addComponent(label_2, GroupLayout.PREFERRED_SIZE, 18, GroupLayout.PREFERRED_SIZE)).addComponent(maxBreakBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE).addGroup(gl_panel_1.createSequentialGroup().addGap(2).addComponent(label_3, GroupLayout.PREFERRED_SIZE, 18, GroupLayout.PREFERRED_SIZE))).addContainerGap(95, Short.MAX_VALUE)));
			panel_1.setLayout(gl_panel_1);
			contentPane.setLayout(gl_contentPane);
			// LOAD SAVED SELECTION INFO
			try {
				final String filename = getCacheDirectory()
						+ "\\AaimistersRKillerSettings.txt";
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
					if (opts[5].equals("true")) {
						breakBox.setSelected(true);
						if (opts[6].equals("false")) {
							randomBox.setSelected(false);
							maxTimeBeBox.setValue(Integer.parseInt(opts[7]));
							minTimeBeBox.setValue(Integer.parseInt(opts[8]));
							maxBreakBox.setValue(Integer.parseInt(opts[9]));
							minBreakBox.setValue(Integer.parseInt(opts[10]));
						} else {
							randomBox.setSelected(true);
						}
					} else {
						breakBox.setSelected(false);
					}
					if (opts[0].equals("true")) {
						room2Box.setSelected(true);
					} else {
						room2Box.setSelected(false);
					}
					if (opts[1].equals("true")) {
						freeBox.setSelected(true);
					} else {
						freeBox.setSelected(false);
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
					healText.setValue(Integer.parseInt(opts[11]));
					foodText.setValue(Integer.parseInt(opts[12]));
					withText.setValue(Integer.parseInt(opts[13]));
				}
			} catch (final Exception e2) {
				// e2.printStackTrace();
				log.warning("Error loading settings.  If this is first time running script, ignore.");
			}
			// END LOAD SAVED SELECTION INFO
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
			if (paintBox.isSelected()) {
				painting = true;
			}
			if (antibanBox.isSelected()) {
				antiBanOn = true;
			}
			if (room2Box.isSelected()) {
				room2 = true;
				rArea = AM.rArea2;
			} else {
				room2 = false;
				rArea = AM.rArea1;
			}
			if (freeBox.isSelected()) {
				free = true;
			}
			X = Integer.parseInt(withText.getValue().toString());
			food = Integer.parseInt(foodText.getValue().toString());
			minHealth = Integer.parseInt(healText.getValue().toString());
			if (breakBox.isSelected()) {
				doBreak = true;
				if (randomBox.isSelected()) {
					randomBreaks = true;
				} else {
					maxBetween = Integer.parseInt(maxTimeBeBox.getValue().toString());
					minBetween = Integer.parseInt(minTimeBeBox.getValue().toString());
					maxLength = Integer.parseInt(maxBreakBox.getValue().toString());
					minLength = Integer.parseInt(minBreakBox.getValue().toString());
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
			}

			// Write settings
			try {
				final BufferedWriter out = new BufferedWriter(new FileWriter(settingsFile));
				out.write((room2Box.isSelected() ? true : false) + ":" // 0
						+ (freeBox.isSelected() ? true : false) + ":" // 1
						+ colorBox.getSelectedIndex() + ":" // 2
						+ (antibanBox.isSelected() ? true : false) + ":" // 3
						+ (paintBox.isSelected() ? true : false) + ":" // 4
						+ (breakBox.isSelected() ? true : false) + ":" // 5
						+ (randomBox.isSelected() ? true : false) + ":" // 6
						+ maxTimeBeBox.getValue().toString() + ":" // 7
						+ minTimeBeBox.getValue().toString() + ":" // 8
						+ maxBreakBox.getValue().toString() + ":" // 9
						+ minBreakBox.getValue().toString() + ":" // 10
						+ healText.getValue().toString() + ":" // 11
						+ foodText.getValue().toString() + ":" // 12
						+ withText.getValue().toString()
				// 13
				);
				out.close();
			} catch (final Exception e1) {
				log.warning("Error saving setting.");
			}
			// End write settings

			AaimistersGUI.dispose();
		}
	}

	private static interface AM {

		// Paths
		final RSTile toBank[] = { new RSTile(3080, 3471),
				new RSTile(3080, 3480), new RSTile(3086, 3486),
				new RSTile(3093, 3490) };
		final RSTile toCave[] = { new RSTile(3093, 3490),
				new RSTile(3084, 3484), new RSTile(3080, 3479),
				new RSTile(3080, 3473), new RSTile(3078, 3464) };

		// Areas
		final RSArea bankArea = new RSArea(new RSTile(3090, 3488), new RSTile(3098, 3499));
		final RSArea rArea1 = new RSArea(new RSTile(3146, 4274), new RSTile(3160, 4281));
		final RSArea rArea2 = new RSArea(new RSTile(3170, 4229), new RSTile(3196, 4273));

		// Tiles
		final RSTile bankTile = new RSTile(3093, 3490);
		final RSTile dropTile = new RSTile(3078, 3462);
	}

	private enum State {
		TOROACH, TOBANK, ATTACK, EAT, BANK, LOOT, ERROR
	}

	private RSArea rArea;
	private long nextBreak = System.currentTimeMillis();
	private long nextLength = 60000;
	private long totalBreakTime;
	private long lastBreakTime;
	private long nextBreakT;
	private long startTime;

	private long runTime;
	private long now;
	private final String[] colorstring = { "Black", "Blue", "Brown", "Cyan",
			"Green", "Lime", "Orange", "Pink", "Purple", "Red", "White",
			"Yellow" };

	AaimistersGUI g = new AaimistersGUI();

	public final File settingsFile = new File(getCacheDirectory(), "AaimistersRKillerSettings.txt");
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
	private final String currentNPC = "Roach";
	private String currentStat;

	private String status = "";
	// All Arrows
	int Aitems[] = { 890, 882, 11212, 19157, 884, 888, 2866, 892, 19152, 886,
			19162 };
	// Coins, Mushroom, Clue Scrool (m), Nature Tail, Shield Left Half, Loop
	// Half Key, Tooth half Key
	int Oitems[] = { 995, 6004, 0, 1462, 2366, 987, 985 };
	// JangerBerry, Wildblood, Limpwurt, Avantoe, Belladonna, Cactus, Cadantine,
	// Irit, Kwuarm
	// Marrentill, Mushroom, Poison ivy, Strawberry, Tarromin, Toadflax,
	// Whiteberry, Watermelon, Harralander
	// Spirit weed, Dwarf weed, Lantadyme, Snapdragon, Torstol, Ranarr
	int Sitems[] = { 5104, 5311, 5100, 5298, 5281, 5280, 5301, 5297, 5299,
			5292, 5282, 5106, 5323, 5293, 5296, 5105, 5321, 5294, 12176, 5303,
			5302, 5300, 5304, 5295 };
	// Crimson, Blue, Gold, Green, All
	int Citems[] = { 12160, 12163, 12158, 12159, 12161, 12162, 12164, 12165,
			12166, 12167 };
	// Sapphire, Emerald, Ruby, Diamond, Dragonstone
	int Gitems[] = { 1623, 1621, 1619, 1617, 1631 };
	// Mithril, Law, Death, Adamantite, Fire, Blood, Chaos
	int Ritems[] = { 448, 563, 560, 450, 554, 565, 562 };
	// Black helm, Mithril med, Rune sq, Rune scimi, Rune javelin, Dragon spear,
	// Rune spear
	int Witems[] = { 1165, 1143, 1185, 1333, 830, 1249, 1247 };

	// All
	int Zitems[] = { 995, 6004, 1462, 2366, 987, 985, 5104, 5311, 5100, 5298,
			5281, 5280, 5301, 5297, 5299, 5292, 5282, 5106, 5323, 5293, 5296,
			5105, 5321, 5294, 12176, 5303, 5302, 5300, 5304, 5295, 12160,
			12163, 12158, 12159, 12161, 12162, 12164, 12165, 12166, 12167,
			1623, 1621, 1619, 1617, 1631, 448, 563, 560, 450, 554, 565, 562,
			1165, 1143, 1185, 1333, 830, 1249, 1247 };
	int incave = 29728;
	// "Enter"
	int outcave = 29729;
	// "Climb"
	int roach = 7160;
	// "Attack"
	int upstairs = 29672;
	// "Climb-up"
	int dwstairs = 29671;
	// "Climb-down"
	int rCount;
	int rHour;
	int boo = 26972;
	int banker = 2759;
	int idle;
	int food = 379;
	int minHealth = 200;
	int X = 20;
	int v, z, x;
	int id;
	int maxBetween;
	int minBetween;
	int maxLength;
	int minLength;
	// Other
	int charmsHour;
	int totalCharms;
	int totalItems;
	int itemsHour;
	int GPHour;
	int totalPrice;
	int xpGained;
	int xpHour;
	int priceO;
	int priceS;
	int priceG;
	int priceR;
	int priceW;
	// Defense
	int dfxpHour;
	int dfxpToLvl;
	int dfcurrentXP;
	int dfgainedLvl;
	int dfxpGained;
	int dfstartEXP;
	int dftimeToLvl;
	// Strength
	int stxpHour;
	int stxpToLvl;
	int stcurrentXP;
	int stgainedLvl;
	int stxpGained;
	int ststartEXP;
	int sttimeToLvl;
	// Attack
	int atxpHour;
	int atxpToLvl;
	int atcurrentXP;
	int atgainedLvl;
	int atxpGained;
	int atstartEXP;
	int attimeToLvl;
	// Range
	int rgxpHour;
	int rgxpToLvl;
	int rgcurrentXP;
	int rggainedLvl;
	int rgxpGained;
	int rgstartEXP;
	int rgtimeToLvl;
	// Cons.
	int coxpHour;
	int coxpToLvl;
	int cocurrentXP;
	int cogainedLvl;
	int coxpGained;
	int costartEXP;

	int cotimeToLvl;
	boolean checkMem = true;
	boolean member;
	boolean currentlyBreaking;
	boolean randomBreaks;
	boolean painting;
	boolean antiBanOn;
	boolean free;
	boolean equip;
	boolean room2;
	boolean notChosen = true;
	boolean doBreak;
	boolean bankedOpen;
	boolean useBanker;
	boolean attacked;
	boolean useBooth;
	boolean clicked;
	boolean checked;
	boolean logTime;
	boolean checkIn;
	boolean opened;
	boolean closed;
	boolean wLoot;
	// Paint Buttons
	boolean xButton;;
	boolean StatAT;
	boolean StatCO;
	boolean StatDF;
	boolean StatST;
	boolean StatRG;

	boolean Main = true;;

	private final Image logo = getImage("http://i88.photobucket.com/albums/k170/aaimister/AaimistersRoaches.gif");

	private final Image atom = getImage("http://i88.photobucket.com/albums/k170/aaimister/Atomm.png");

	private RSNPC banker() {
		return npcs.getNearest(new Filter<RSNPC>() {
			public boolean accept(final RSNPC n) {
				return n.getID() == banker;
			}
		});
	}

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
			nextLength = nextBreakT;
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

	private void checkPrice(final int x, int y) {
		if (x == 995) {
			if (inventory.getCount(true, x) > y) {
				totalPrice += inventory.getCount(true, x) - y;
				y = inventory.getCount(true, x);
			}
		} else {
			if (inventory.getCount(true, x) > y) {
				if (x == 448) {
					totalPrice += (inventory.getCount(true, x) - y)
							* getGuidePrice(447);
					y = inventory.getCount(true, x);
				} else if (x == 450) {
					totalPrice += (inventory.getCount(true, x) - y)
							* getGuidePrice(449);
					y = inventory.getCount(true, x);
				} else {
					totalPrice += (inventory.getCount(true, x) - y)
							* getGuidePrice(x);
					y = inventory.getCount(true, x);
				}
			}
		}
	}

	public void checkXP() {
		if (game.getCurrentTab() != 2) {
			game.openTab(2);
			sleep(500, 900);
		}
		x = random(1, 4);
		if (atxpGained == 0) {
			if (x == 2) {
				x = random(3, 4);
			}
		} else if (stxpGained == 0) {
			if (x == 3) {
				x = random(1, 2);
			}
		} else if (dfxpGained == 0) {
			if (x == 4) {
				x = random(1, 3);
			}
		}
		if (x == 1) {
			// Cons.
			mouse.move(random(617, 667), random(214, 232));
		} else if (x == 2) {
			// Att.
			mouse.move(random(555, 605), random(213, 232));
		} else if (x == 3) {
			// Str.
			mouse.move(random(555, 604), random(241, 260));
		} else if (x == 4) {
			// Def.
			mouse.move(random(555, 606), random(271, 288));
		}
		sleep(2800, 5500);
		game.openTab(4);
		sleep(50, 100);
		mouse.moveRandomly(50, 900);
	}

	private void clickNPC(final RSNPC x) {
		try {
			if (x != null) {
				if (x.isOnScreen()) {
					x.doAction("Att");
					sleep(50);
				} else {
					if (!getMyPlayer().isMoving()
							|| calc.distanceTo(walking.getDestination()) < 4) {
						walking.walkTileMM(walking.getClosestTileOnMap(x.getLocation()).randomize(1, 1));
					}
				}
			}
		} catch (final Exception e) {
			return;
		}
	}

	private void clickObj(final RSObject x, final String y) {
		try {
			if (x != null) {
				if (x.isOnScreen()) {
					x.doAction(y);
					sleep(50);
				} else {
					if (!getMyPlayer().isMoving()
							|| calc.distanceTo(walking.getDestination()) < 4) {
						walking.walkTileMM(walking.getClosestTileOnMap(x.getLocation()).randomize(1, 1));
					}
				}
			}
		} catch (final Exception e) {
			return;
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

		if (random(0, 8) != random(0, 8)) {
			return;
		}

		if (!antiBanOn) {
			return;
		}

		final int action = random(0, 6);

		switch (action) {
		case 0:
			if (random(0, 3) == random(1, 3)) {
				rotateCamera();
				sleep(200, 400);
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
			if (random(0, 10) == random(0, 10)) {
				checkXP();
				sleep(200, 400);
			}
			break;
		case 4:
			mouse.moveRandomly(100, 900);
			sleep(200, 400);
			break;
		case 5:
			if (random(0, 3) == random(1, 3)) {
				rotateCamera();
				sleep(200, 400);
			}
			break;
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

	// Credits Aion
	private int getGuidePrice(final int itemID) {
		try {
			final URL url = new URL("http://services.runescape.com/m=itemdb_rs/viewitem.ws?obj="
					+ itemID);
			final BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
			String line = null;

			while ((line = br.readLine()) != null) {
				if (line.contains("<b>Current guide price:</b>")) {
					line = line.replace("<b>Current guide price:</b>", "");
					return (int) parse(line);
				}
			}
		} catch (final IOException e) {
		}
		return -1;
	}

	private Image getImage(final String url) {
		try {
			return ImageIO.read(new URL(url));
		} catch (final IOException e) {
			return null;
		}
	}

	private int getStat() {
		if (StatDF) {
			currentStat = " Defense";
			return 1;
		} else if (StatST) {
			currentStat = " Strength";
			return 2;
		} else if (StatAT) {
			currentStat = " Attack";
			return 0;
		} else if (StatCO) {
			currentStat = " Cons.";
			return 3;
		} else if (StatRG) {
			currentStat = " Range";
			return 4;
		} else {
			currentStat = " Cons.";
			return 3;
		}
	}

	private State getState() {
		try {
			if (Integer.parseInt(interfaces.getComponent(748, 8).getText()) < minHealth
					+ random(-10, 10)) {
				return State.EAT;
			}
		} catch (final Exception e) {

		}
		if (inventory.contains(food)) {
			if (!room2) {
				if (game.getPlane() == 3) {
					if (loot()) {
						return State.LOOT;
					} else {
						return State.ATTACK;
					}
				} else {
					return State.TOROACH;
				}
			} else {
				if (game.getPlane() == 2) {
					if (loot()) {
						return State.LOOT;
					} else {
						return State.ATTACK;
					}
				} else {
					return State.TOROACH;
				}
			}
		} else {
			if (!AM.bankArea.contains(getMyPlayer().getLocation())) {
				return State.TOBANK;
			} else {
				return State.BANK;
			}
		}
	}

	public double getVersion() {
		return 1.11;
	}

	private String Location() {
		if (AM.bankArea.contains(getMyPlayer().getLocation())) {
			return "Bank";
		} else if (game.getPlane() == 3 || game.getPlane() == 2) {
			return "Cave";
		} else if (calc.distanceTo(AM.bankTile) < 300) {
			return "Edgeville";
		} else if (!game.isLoggedIn()) {
			return "Login Screen";
		} else {
			return "Unknown";
		}
	}

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
			status = "Breaking...";
			return 3000;
		}

		if (startTime == 0 && skills.getCurrentLevel(8) != 0) {
			startTime = System.currentTimeMillis();
			dfstartEXP = skills.getCurrentExp(1);
			dfcurrentXP = skills.getExpToNextLevel(1);
			ststartEXP = skills.getCurrentExp(2);
			stcurrentXP = skills.getExpToNextLevel(2);
			atstartEXP = skills.getCurrentExp(0);
			atcurrentXP = skills.getExpToNextLevel(0);
			costartEXP = skills.getCurrentExp(3);
			cocurrentXP = skills.getExpToNextLevel(3);
			rgstartEXP = skills.getCurrentExp(4);
			rgcurrentXP = skills.getExpToNextLevel(4);
		}

		currentlyBreaking = false;

		if (logTime) {
			log("Next Break In: " + formatTime((int) nextBreakT) + " For: "
					+ formatTime((int) nextLength) + ".");
			logTime = false;
		}

		mouse.setSpeed(random(4, 8));
		setCamera();
		setRun();

		if (checkMem) {
			if (AccountManager.isMember(account.getName())) {
				member = true;
			}
			if (doBreak) {
				if (AccountManager.isTakingBreaks(account.getName())) {
					log.severe("Turn Off Bot Breaks!");
					log.severe("Turning off custom breaker...");
					doBreak = false;
				}
			}
			checkMem = false;
		}

		if (equip) {
			if (inventory.containsOneOf(Aitems)) {
				final RSItem i = inventory.getItem(Aitems);
				i.doAction("Wield");
				equip = false;
				return random(500, 1000);
			} else {
				log.severe("Out of Ammo. =O");
				game.logout(false);
				sleep(500);
				stopScript();
			}
		}

		if (checkIn) {
			if (inventory.getCount(true, v) > z) {
				checkPrice(v, z);
				checkIn = false;
			} else {
				checkIn = true;
			}
		}

		switch (getState()) {
		case EAT:
			if (inventory.contains(food)) {
				final RSItem foo = inventory.getItem(food);
				foo.doAction("Eat");
				return random(1200, 2000);
			}

			break;
		case TOROACH:
			status = "Walking to roaches...";
			if (idle > 5) {
				clicked = false;
				idle = 0;
			}
			try {
				if (game.getPlane() != 3) {
					if (calc.distanceTo(AM.dropTile) > 3) {
						walkPath(AM.toCave);
						return 50;
					} else {
						final RSObject rope = objects.getNearest(incave);
						final RSTile loc = rope.getArea().getNearestTile(getMyPlayer().getLocation());
						if (calc.distanceTo(loc) > 3) {
							if (!getMyPlayer().isMoving()
									|| calc.distanceTo(walking.getDestination()) < 4) {
								walking.walkTileMM(walking.getClosestTileOnMap(loc.randomize(2, 2)));
								return random(150, 300);
							}
						} else {
							if (rope.isOnScreen()) {
								idle++;
								if (!clicked) {
									clickObj(rope, "Enter");
									clicked = true;
									return 10;
								}
							} else {
								camera.turnTo(rope);
								return random(200, 400);
							}
						}
					}
				} else if (room2) {
					if (game.getPlane() == 3) {
						final RSObject stair = objects.getNearest(dwstairs);
						final RSTile loc = stair.getArea().getNearestTile(getMyPlayer().getLocation());
						if (calc.distanceTo(loc) > 3) {
							if (!getMyPlayer().isMoving()
									|| calc.distanceTo(walking.getDestination()) < 4) {
								walking.walkTileMM(walking.getClosestTileOnMap(loc.randomize(1, 1)));
								return random(150, 300);
							}
						} else {
							if (stair.isOnScreen()) {
								clickObj(stair, "Climb-down");
								return random(1000, 1500);
							} else {
								camera.turnTo(stair);
								return random(300, 500);
							}
						}
					}
				}
			} catch (final Exception e) {

			}
			break;
		case TOBANK:
			notChosen = true;
			if (idle > 8) {
				clicked = false;
				idle = 0;
			}
			status = "Walking to bank...";
			try {
				if (game.getPlane() == 3) {
					final RSObject rope = objects.getNearest(outcave);
					final RSTile loc = rope.getArea().getNearestTile(getMyPlayer().getLocation());
					if (calc.distanceTo(loc) > 3) {
						if (getMyPlayer().isMoving()
								&& !getMyPlayer().isInCombat()) {
							return 10;
						}
						if (!getMyPlayer().isMoving()
								|| calc.distanceTo(walking.getDestination()) < 4) {
							walking.walkTileMM(walking.getClosestTileOnMap(loc.randomize(1, 1)));
							return random(150, 300);
						}
					} else {
						if (rope.isOnScreen()) {
							idle++;
							if (!clicked) {
								clickObj(rope, "Climb");
								clicked = true;
								idle = 0;
								return 10;
							}
						} else {
							camera.turnTo(rope);
							return random(200, 400);
						}
					}
				} else if (game.getPlane() == 2) {
					final RSObject stair = objects.getNearest(upstairs);
					final RSTile loc = stair.getArea().getNearestTile(getMyPlayer().getLocation());
					if (calc.distanceTo(loc) > 3) {
						if (!getMyPlayer().isMoving()
								|| calc.distanceTo(walking.getDestination()) < 4) {
							walking.walkTileMM(walking.getClosestTileOnMap(loc.randomize(1, 1)));
							return random(150, 300);
						}
					} else {
						if (stair.isOnScreen()) {
							clickObj(stair, "Climb-up");
							return random(1000, 1500);
						} else {
							camera.turnTo(stair);
							return random(300, 500);
						}
					}
				} else {
					walkPath(AM.toBank);
					return 50;
				}
			} catch (final Exception e) {

			}

			break;
		case ATTACK:
			status = "Attacking roaches...";
			clicked = false;
			if (idle > 8) {
				if (loot()) {
					return 10;
				}
				attacked = false;
				idle = 0;
			}
			if (getMyPlayer().getInteracting() == null) {
				if (wLoot && loot() == false) {
					sleep(1800, 2500);
					wLoot = false;
				} else if (loot() != false) {
					return 10;
				}
				if (roach() != null && !loot()) {
					if (roach().isOnScreen()) {
						idle++;
						if (!attacked) {
							clickNPC(roach());
							rCount++;
							attacked = true;
							wLoot = true;
							idle = 0;
							return random(500, 1000);
						}
					} else {
						if (!getMyPlayer().isMoving()
								|| calc.distanceTo(walking.getDestination()) < 4) {
							walking.walkTileMM(walking.getClosestTileOnMap(roach().getLocation().randomize(1, 1)));
							return random(1000, 1300);
						}
					}
				} else {
					attacked = false;
					return 10;
				}
			} else {
				idle = 0;
				if (random(0, 20) == random(0, 20)) {
					doAntiBan();
				}
				return 10;
			}

			break;
		case BANK:
			status = "Banking...";
			clicked = false;
			if (idle > 3) {
				opened = false;
				bankedOpen = false;
				idle = 0;
			}
			if (notChosen) {
				if (random(0, 5) == 0 || random(0, 5) == 2) {
					useBanker = true;
				} else {
					useBooth = true;
				}
				notChosen = false;
			}
			if (useBooth) {
				final RSObject booth = objects.getNearest(boo);
				if (AM.bankArea.contains(getMyPlayer().getLocation())
						&& booth.isOnScreen()) {
					if (!bank.isOpen()) {
						idle++;
						if (!opened) {
							booth.doAction("Use-quickly");
							opened = true;
							return random(200, 500);
						}
					} else {
						opened = false;
						idle++;
						if (!bankedOpen) {
							bank.depositAll();
							sleep(350, 500);
							if (bank.getItem(food) != null) {
								bank.withdraw(food, X);
								idle = 0;
								sleep(350, 500);
							} else {
								log.severe("Out of Food!");
								game.logout(false);
								sleep(200, 500);
								stopScript();
							}
							bankedOpen = true;
							return random(100, 150);
						}
					}
				} else {
					if (!getMyPlayer().isMoving()
							|| calc.distanceTo(walking.getDestination()) < 4) {
						walking.walkTileMM(walking.getClosestTileOnMap(booth.getLocation().randomize(1, 1)));
						return random(1200, 1500);
					}
				}
			}
			if (useBanker) {
				final RSNPC bankP = banker();
				if (AM.bankArea.contains(getMyPlayer().getLocation())
						&& calc.tileOnScreen(banker().getLocation())) {
					if (!bank.isOpen()) {
						idle++;
						if (!opened) {
							bankP.doAction("Bank Banker");
							opened = true;
							return random(200, 500);
						}
					} else {
						opened = false;
						idle++;
						if (!bankedOpen) {
							bank.depositAll();
							sleep(350, 500);
							if (bank.getItem(food) != null) {
								bank.withdraw(food, X);
								idle = 0;
								sleep(350, 500);
							} else {
								log.severe("Out of Food!");
								game.logout(false);
								sleep(200, 500);
								stopScript();
							}
							bankedOpen = true;
							return random(100, 150);
						}
					}
				} else {
					if (!getMyPlayer().isMoving()
							|| calc.distanceTo(walking.getDestination()) < 4) {
						walking.walkTileMM(walking.getClosestTileOnMap(banker().getLocation().randomize(1, 1)));
						return random(150, 300);
					}
				}
			}

			break;
		case LOOT:
			attacked = false;
			status = "Picking up loot...";
			if (idle > 3) {
				clicked = false;
				idle = 0;
			}
			if (inventory.isFull()) {
				final RSItem foo = inventory.getItem(food);
				foo.doAction("Eat");
				return random(1000, 1300);
			}
			final RSGroundItem a = groundItems.getNearest(Aitems);
			final RSGroundItem o = groundItems.getNearest(Oitems);
			final RSGroundItem s = groundItems.getNearest(Sitems);
			final RSGroundItem c = groundItems.getNearest(Citems);
			final RSGroundItem g = groundItems.getNearest(Gitems);
			final RSGroundItem r = groundItems.getNearest(Ritems);
			final RSGroundItem w = groundItems.getNearest(Witems);
			if (rgxpGained != 0) {
				if (a != null && rArea.contains(a.getLocation())) {
					if (a.isOnScreen()) {
						idle++;
						if (!clicked) {
							if (!getMyPlayer().isMoving()) {
								lootItem(a);
							} else {
								return 50;
							}
							clicked = true;
							idle = 0;
							equip = true;
							return calc.distanceTo(a.getLocation()) * 1000;
						}
					} else {
						if (!getMyPlayer().isMoving()
								|| calc.distanceTo(walking.getDestination()) < 4) {
							walking.walkTileMM(a.getLocation().randomize(1, 1));
							return random(150, 300);
						}
					}
				}
			}
			if (o != null && rArea.contains(o.getLocation())) {
				if (o.isOnScreen()) {
					idle++;
					if (!clicked) {
						if (!getMyPlayer().isMoving()) {
							lootItem(o);
						} else {
							return 50;
						}
						if (o.getItem().getID() == 995) {
							v = 995;
							z = inventory.getCount(true, v);
							checkIn = true;
							clicked = true;
							idle = 0;
							totalItems++;
							return calc.distanceTo(o.getLocation()) * 1000;
						}
						totalPrice += getGuidePrice(o.getItem().getID());
						clicked = true;
						idle = 0;
						totalItems++;
						return random(1750, 2200);
					}
				} else {
					if (!getMyPlayer().isMoving()
							|| calc.distanceTo(walking.getDestination()) < 4) {
						walking.walkTileMM(o.getLocation().randomize(1, 1));
						return random(150, 300);
					}
				}
			} else if (s != null && rArea.contains(s.getLocation())) {
				if (s.isOnScreen()) {
					idle++;
					if (!clicked) {
						if (!getMyPlayer().isMoving()) {
							lootItem(s);
						} else {
							return 50;
						}
						totalPrice += getGuidePrice(s.getItem().getID());
						clicked = true;
						idle = 0;
						totalItems++;
						return calc.distanceTo(s.getLocation()) * 1000;
					}
				} else {
					if (!getMyPlayer().isMoving()
							|| calc.distanceTo(walking.getDestination()) < 4) {
						walking.walkTileMM(s.getLocation().randomize(1, 1));
						return random(150, 300);
					}
				}
			} else if (c != null && rArea.contains(c.getLocation())) {
				if (c.isOnScreen()) {
					idle++;
					if (!clicked) {
						if (!getMyPlayer().isMoving()) {
							lootItem(c);
						} else {
							return 50;
						}
						clicked = true;
						idle = 0;
						totalItems++;
						totalCharms++;
						return calc.distanceTo(c.getLocation()) * 1000;
					}
				} else {
					if (!getMyPlayer().isMoving()
							|| calc.distanceTo(walking.getDestination()) < 4) {
						walking.walkTileMM(c.getLocation().randomize(1, 1));
						return random(150, 300);
					}
				}
			} else if (g != null && rArea.contains(g.getLocation())) {
				if (g.isOnScreen()) {
					idle++;
					if (!clicked) {
						if (!getMyPlayer().isMoving()) {
							lootItem(g);
						} else {
							return 50;
						}
						totalPrice += getGuidePrice(g.getItem().getID());
						clicked = true;
						idle = 0;
						totalItems++;
						return calc.distanceTo(g.getLocation()) * 1000;
					}
				} else {
					if (!getMyPlayer().isMoving()
							|| calc.distanceTo(walking.getDestination()) < 4) {
						walking.walkTileMM(g.getLocation().randomize(1, 1));
						return random(150, 300);
					}
				}
			} else if (r != null && rArea.contains(r.getLocation())) {
				if (r.isOnScreen()) {
					idle++;
					if (!clicked) {
						if (!getMyPlayer().isMoving()) {
							lootItem(r);
						} else {
							return 50;
						}
						v = r.getItem().getID();
						z = inventory.getCount(true, v);
						checkIn = true;
						clicked = true;
						idle = 0;
						totalItems++;
						return calc.distanceTo(r.getLocation()) * 1000;
					}
				} else {
					if (!getMyPlayer().isMoving()
							|| calc.distanceTo(walking.getDestination()) < 4) {
						walking.walkTileMM(r.getLocation().randomize(1, 1));
						return random(150, 300);
					}
				}
			} else if (w != null && rArea.contains(w.getLocation())) {
				if (w.isOnScreen()) {
					idle++;
					if (!clicked) {
						if (!getMyPlayer().isMoving()) {
							lootItem(w);
						} else {
							return 50;
						}
						totalPrice += getGuidePrice(w.getItem().getID());
						clicked = true;
						idle = 0;
						totalItems++;
						return calc.distanceTo(w.getLocation()) * 1000;
					}
				} else {
					if (!getMyPlayer().isMoving()
							|| calc.distanceTo(walking.getDestination()) < 4) {
						walking.walkTileMM(w.getLocation().randomize(1, 1));
						return random(150, 300);
					}
				}
			}

			break;
		case ERROR:

			break;
		}
		return random(300, 600);
	}

	private boolean loot() {
		final RSGroundItem a = groundItems.getNearest(Aitems);
		final RSGroundItem z = groundItems.getNearest(Zitems);
		if (z != null && rArea.contains(z.getLocation())
				&& getMyPlayer().getInteracting() == null) {
			return true;
		} else if (rgxpGained != 0 && a != null
				&& rArea.contains(a.getLocation())
				&& getMyPlayer().getInteracting() == null) {
			return true;
		} else {
			return false;
		}
	}

	private void lootItem(final RSGroundItem x) {
		try {
			final Point c = calc.tileToScreen(x.getLocation());
			if (c != null && !x.doAction("Take")) {
				mouse.move(c);
				sleep(150, 300);
				x.doAction("Take");
			}
		} catch (final Exception e) {

		}
	}

	public void main(final Graphics g) {
		final long totalTime = System.currentTimeMillis() - startTime;
		final String formattedTime = formatTime((int) totalTime);
		g.setColor(LineColor);
		g.drawString("Time running: " + formattedTime, 63, 390);
		g.drawString("Location: " + Location(), 63, 404);
		g.drawString("Status: " + status, 63, 418);
		g.drawString("Current NPC: " + currentNPC, 63, 433);
		g.drawString("Total XP: " + formatter.format((long) xpGained), 63, 447);
		g.drawString("Total XP/h: " + formatter.format((long) xpHour), 63, 463);
	}

	@Override
	public void messageReceived(final MessageEvent e) {
		if (e.getMessage().contains("You've just advanced an At")) {
			atgainedLvl++;
		}
		if (e.getMessage().contains("You've just advanced a Str")) {
			stgainedLvl++;
		}
		if (e.getMessage().contains("You've just advanced a Con")) {
			cogainedLvl++;
		}
		if (e.getMessage().contains("You've just advanced a Ran")) {
			rggainedLvl++;
		}
		if (e.getMessage().contains("You've just advanced a Def")) {
			dfgainedLvl++;
		}
		if (e.getMessage().contains("There is no ammo")) {
			equip = true;
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
				if (atxpGained != 0) {
					StatAT = true;
				} else if (stxpGained != 0) {
					StatST = true;
				} else if (dfxpGained != 0) {
					StatDF = true;
				} else if (rgxpGained != 0) {
					StatRG = true;
				} else {
					StatCO = true;
				}
			} else if (StatAT) {
				StatAT = false;
				if (stxpGained != 0) {
					StatST = true;
				} else if (dfxpGained != 0) {
					StatDF = true;
				} else if (rgxpGained != 0) {
					StatRG = true;
				} else {
					StatCO = true;
				}
			} else if (StatST) {
				StatST = false;
				if (dfxpGained != 0) {
					StatDF = true;
				} else if (rgxpGained != 0) {
					StatRG = true;
				} else {
					StatCO = true;
				}
			} else if (StatDF) {
				StatDF = false;
				if (rgxpGained != 0) {
					StatRG = true;
				} else {
					StatCO = true;
				}
			} else if (StatRG) {
				StatRG = false;
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
				if (dfxpGained != 0) {
					StatDF = true;
				} else if (stxpGained != 0) {
					StatST = true;
				} else if (atxpGained != 0) {
					StatAT = true;
				} else if (rgxpGained != 0) {
					StatRG = true;
				} else {
					Main = true;
				}
			} else if (StatDF) {
				StatDF = false;
				if (stxpGained != 0) {
					StatST = true;
				} else if (atxpGained != 0) {
					StatAT = true;
				} else if (rgxpGained != 0) {
					StatRG = true;
				} else {
					Main = true;
				}
			} else if (StatST) {
				StatST = false;
				if (atxpGained != 0) {
					StatAT = true;
				} else if (rgxpGained != 0) {
					StatRG = true;
				} else {
					Main = true;
				}
			} else if (StatAT) {
				StatAT = false;
				if (rgxpGained != 0) {
					StatRG = true;
				} else {
					Main = true;
				}
			} else if (StatRG) {
				StatRG = false;
				Main = true;
			}
		}

	}

	public void mouseReleased(final MouseEvent e) {
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
			// AT
			atcurrentXP = skills.getExpToNextLevel(0);
			atxpGained = skills.getCurrentExp(0) - atstartEXP;
			atxpToLvl = skills.getExpToNextLevel(0);
			atxpHour = (int) (3600000.0 / (double) runTime * atxpGained);
			if (atxpHour != 0) {
				attimeToLvl = (int) ((double) atcurrentXP / (double) atxpHour * 3600000.0);
			}
			// ST
			stcurrentXP = skills.getExpToNextLevel(2);
			stxpGained = skills.getCurrentExp(2) - ststartEXP;
			stxpToLvl = skills.getExpToNextLevel(2);
			stxpHour = (int) (3600000.0 / (double) runTime * stxpGained);
			if (stxpHour != 0) {
				sttimeToLvl = (int) ((double) stcurrentXP / (double) stxpHour * 3600000.0);
			}
			// DF
			dfcurrentXP = skills.getExpToNextLevel(1);
			dfxpGained = skills.getCurrentExp(1) - dfstartEXP;
			dfxpToLvl = skills.getExpToNextLevel(1);
			dfxpHour = (int) (3600000.0 / (double) runTime * dfxpGained);
			if (dfxpHour != 0) {
				dftimeToLvl = (int) ((double) dfcurrentXP / (double) dfxpHour * 3600000.0);
			}
			// RG
			rgcurrentXP = skills.getExpToNextLevel(4);
			rgxpGained = skills.getCurrentExp(4) - rgstartEXP;
			rgxpToLvl = skills.getExpToNextLevel(4);
			rgxpHour = (int) (3600000.0 / (double) runTime * rgxpGained);
			if (rgxpHour != 0) {
				rgtimeToLvl = (int) ((double) rgcurrentXP / (double) rgxpHour * 3600000.0);
			}
			// CO
			cocurrentXP = skills.getExpToNextLevel(3);
			coxpGained = skills.getCurrentExp(3) - costartEXP;
			coxpToLvl = skills.getExpToNextLevel(3);
			coxpHour = (int) (3600000.0 / (double) runTime * coxpGained);
			if (coxpHour != 0) {
				cotimeToLvl = (int) ((double) cocurrentXP / (double) coxpHour * 3600000.0);
			}
			xpGained = dfxpGained + stxpGained + atxpGained + coxpGained;
			xpHour = (int) (3600000.0 / (double) runTime * xpGained);
			charmsHour = (int) (3600000.0 / (double) runTime * totalCharms);
			itemsHour = (int) (3600000.0 / (double) runTime * totalItems);
			GPHour = (int) (3600000.0 / (double) runTime * totalPrice);
			rHour = (int) (3600000.0 / (double) runTime * rCount);
		}

		if (painting) {
			((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			((Graphics2D) g).setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
			((Graphics2D) g).setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		}

		// Objects
		// drawObjects(g);

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
				g.drawString("Total Money: $"
						+ formatter.format((long) totalPrice), 264, 390);
				g.drawString("Money / Hour: $"
						+ formatter.format((long) GPHour), 264, 404);
				g.drawString("Total Item(s): "
						+ formatter.format((long) totalItems), 264, 418);
				g.drawString("Item(s) / Hour: "
						+ formatter.format((long) itemsHour), 264, 433);
				if (member) {
					g.drawString("Total Charm(s): "
							+ formatter.format((long) totalCharms), 264, 447);
					g.drawString("Charm(s) / Hour: "
							+ formatter.format((long) charmsHour), 264, 463);
				} else {
					g.drawString("Roaches Attacked: "
							+ formatter.format((long) rCount), 264, 447);
					g.drawString("Roaches / Hour: "
							+ formatter.format((long) rHour), 264, 463);
				}
			}
			if (StatAT) {
				// Column 1
				main(g);
				// Column 2
				g.drawString("Total Attack XP: "
						+ formatter.format((long) atxpGained), 264, 390);
				g.drawString("Attack XP/h: "
						+ formatter.format((long) atxpHour), 264, 404);
				g.drawString("Level In: " + formatTime(attimeToLvl), 264, 418);
				g.drawString("Attack XP to Lvl: "
						+ formatter.format((long) atxpToLvl), 264, 433);
				g.drawString("Current Lvl: " + skills.getCurrentLevel(0), 264, 447);
				g.drawString("Gained Lvl(s): "
						+ formatter.format((long) atgainedLvl), 264, 463);
			}
			if (StatST) {
				// Column 1
				main(g);
				// Column 2
				g.drawString("Total Strength XP: "
						+ formatter.format((long) stxpGained), 264, 390);
				g.drawString("Strength XP/h: "
						+ formatter.format((long) stxpHour), 264, 404);
				g.drawString("Level In: " + formatTime(sttimeToLvl), 264, 418);
				g.drawString("Strength XP to Lvl: "
						+ formatter.format((long) stxpToLvl), 264, 433);
				g.drawString("Current Lvl: " + skills.getCurrentLevel(2), 264, 447);
				g.drawString("Gained Lvl(s): "
						+ formatter.format((long) stgainedLvl), 264, 463);
			}
			if (StatDF) {
				// Column 1
				main(g);
				// Column 2
				g.drawString("Total Defence XP: "
						+ formatter.format((long) dfxpGained), 264, 390);
				g.drawString("Defence XP/h: "
						+ formatter.format((long) dfxpHour), 264, 404);
				g.drawString("Level In: " + formatTime(dftimeToLvl), 264, 418);
				g.drawString("Defence XP to Lvl: "
						+ formatter.format((long) dfxpToLvl), 264, 433);
				g.drawString("Current Lvl: " + skills.getCurrentLevel(1), 264, 447);
				g.drawString("Gained Lvl(s): "
						+ formatter.format((long) dfgainedLvl), 264, 463);
			}
			if (StatRG) {
				// Column 1
				main(g);
				// Column 2
				g.drawString("Total Range XP: "
						+ formatter.format((long) rgxpGained), 264, 390);
				g.drawString("Range XP/h: " + formatter.format((long) rgxpHour), 264, 404);
				g.drawString("Level In: " + formatTime(rgtimeToLvl), 264, 418);
				g.drawString("Range XP to Lvl: "
						+ formatter.format((long) rgxpToLvl), 264, 433);
				g.drawString("Current Lvl: " + skills.getCurrentLevel(4), 264, 447);
				g.drawString("Gained Lvl(s): "
						+ formatter.format((long) rggainedLvl), 264, 463);
			}
			if (StatCO) {
				// Column 1
				main(g);
				// Column 2
				g.drawString("Total Cons. XP: "
						+ formatter.format((long) coxpGained), 264, 390);
				g.drawString("Cons. XP/h: " + formatter.format((long) coxpHour), 264, 404);
				g.drawString("Level In: " + formatTime(cotimeToLvl), 264, 418);
				g.drawString("Cons. XP to Lvl: "
						+ formatter.format((long) coxpToLvl), 264, 433);
				g.drawString("Current Lvl: " + skills.getCurrentLevel(3), 264, 447);
				g.drawString("Gained Lvl(s): "
						+ formatter.format((long) cogainedLvl), 264, 463);
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
					+ currentStat, 194, 332);
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
	}

	public boolean onStart() {
		status = "Starting up...";

		URLConnection url = null;
		BufferedReader in = null;
		BufferedWriter out = null;

		// Check right away...
		try {
			// Open the version text file
			url = new URL("http://aaimister.webs.com/scripts/AaimistersRoachVersion.txt").openConnection();
			// Create an input stream for it
			in = new BufferedReader(new InputStreamReader(url.getInputStream()));
			// Check if the current version is outdated
			if (Double.parseDouble(in.readLine()) > getVersion()) {
				if (JOptionPane.showConfirmDialog(null, "Update found. Do you want to update?") == 0) {
					// If so, allow the user to choose the file to be updated.
					JOptionPane.showMessageDialog(null, "Please choose 'AaimistersRoaches.java' in your scripts folder and hit 'Open'");
					final JFileChooser fc = new JFileChooser();
					// Make sure "Open" was clicked.
					if (fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
						// If so, set up the URL for the .java file and set up
						// the IO.
						url = new URL("http://aaimister.webs.com/scripts/AaimistersRoaches.java").openConnection();
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

		dfstartEXP = skills.getCurrentExp(1);
		dfcurrentXP = skills.getExpToNextLevel(1);
		ststartEXP = skills.getCurrentExp(2);
		stcurrentXP = skills.getExpToNextLevel(2);
		atstartEXP = skills.getCurrentExp(0);
		atcurrentXP = skills.getExpToNextLevel(0);
		costartEXP = skills.getCurrentExp(3);
		cocurrentXP = skills.getExpToNextLevel(3);
		rgstartEXP = skills.getCurrentExp(4);
		rgcurrentXP = skills.getExpToNextLevel(4);

		if (doBreak) {
			breakingNew();
		}

		return true;
	}

	// Credits Aion
	private double parse(String str) {
		if (str != null && !str.isEmpty()) {
			str = stripFormatting(str);
			str = str.substring(str.indexOf(58) + 2, str.length());
			str = str.replace(",", "");
			if (!str.endsWith("%")) {
				if (!str.endsWith("k") && !str.endsWith("m")) {
					return Double.parseDouble(str);
				}
				return Double.parseDouble(str.substring(0, str.length() - 1))
						* (str.endsWith("m") ? 1000000 : 1000);
			}
			final int k = str.startsWith("+") ? 1 : -1;
			str = str.substring(1);
			return Double.parseDouble(str.substring(0, str.length() - 1)) * k;
		}
		return -1D;
	}

	private RSNPC roach() {
		return npcs.getNearest(new Filter<RSNPC>() {
			public boolean accept(final RSNPC r) {
				if (!free) {
					return r.getID() == roach && r.getHPPercent() > 0;
				} else {
					return r.getID() == roach && r.getHPPercent() > 0
							&& !r.isInCombat();
				}
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
		if (camera.getPitch() < 2) {
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
		}
	}

	private boolean sleepCR(final int amtOfHalfSecs) {
		for (int x = 0; x < amtOfHalfSecs + 1; x++) {
			sleep(random(48, 53));
		}
		return true;
	}

	// Credits Aion
	private String stripFormatting(final String str) {
		if (str != null && !str.isEmpty()) {
			return str.replaceAll("(^[^<]+>|<[^>]+>|<[^>]+$)", "");
		}
		return "";
	}

	private boolean walkPath(final RSTile[] tiles) {
		final RSPath walkPath = walking.newTilePath(tiles).randomize(1, 1);
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
