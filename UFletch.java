import java.awt.Color;
import java.awt.Container;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;
import java.awt.MenuItem;
import java.awt.Point;
import java.awt.PopupMenu;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.util.LinkedList;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JSlider;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import org.rsbot.event.events.MessageEvent;
import org.rsbot.event.listeners.MessageListener;
import org.rsbot.event.listeners.PaintListener;
import org.rsbot.script.Script;
import org.rsbot.script.ScriptManifest;
import org.rsbot.script.methods.Environment;
import org.rsbot.script.methods.Game;
import org.rsbot.script.methods.Skills;
import org.rsbot.script.wrappers.RSInterface;
import org.rsbot.script.wrappers.RSTile;
import org.rsbot.util.GlobalConfiguration;
import org.rsbot.script.util.PaintUtil;

@ScriptManifest(authors = { "Fletch To 99" }, keywords = "Fletching", name = "UFletch", website = "http://www.universalscripts.org", version = 2.22, description = "The best fletcher!")
/**
 * All-in-One Fletching script for RSBot 2.XX
 * @author Fletch To 99
 */
public class UFletch extends Script implements PaintListener, MouseListener,
		MouseMotionListener, MessageListener {

	private static interface constants {
		String[] optionMethod = { "Fletch", "String", "Fletch&String",
				"Chop-Fletch-Drop/Shaft" };
		String[] optionLog = { "Normal", "Oak", "Willow", "Maple", "Yew",
				"Magic" };
		String[] optionBow = { "Short", "Long", "Shafts", "Stocks", "N/A" };
		String[] optionKnife = { "Normal", "clay", "N/A" };
		String[] optionAxe = { "Bronze", "Iron", "Black", "Mith", "Addy",
				"Rune", "Dragon", "N/A" };
		String[] optionColor = { "Black", "Red", "Orange", "Blue", "Green",
				"Yellow", "Pink", "White", "Tan" };
		final Color TAN = new Color(220, 202, 169);
		final int BOW_STRING_ID = 1777;
		MenuItem item1 = new MenuItem("Stop");
		MenuItem item2 = new MenuItem("Pause");
		MenuItem item3 = new MenuItem("Resume");
		MenuItem item4 = new MenuItem("Open Gui");
		MenuItem item5 = new MenuItem("Help");
	}

	private int amount = 0;
	private int startXP = 0;
	private int fletched = 0;
	private int strung = 0;
	private int xpIsClose = 13020000;
	private int currentexp = 0;
	private int Mouse1 = 50;
	private int Mouse2 = 8;
	private int xpGained = 0;
	private int xpToLevel = 0;
	private int hoursTNL = 0;
	private int minsTNL = 0;
	private int fail = 0;
	private int full = 0;
	private PaintUtil paintUT = null;

	private long startTime = System.currentTimeMillis();

	private Point p = null;
	private Point p2 = null;
	private Point z = null;

	private Image invPaint = null;
	private Image paintp = null;
	private Image hide = null;
	private Image show = null;
	private Image guiButton = null;
	private Image watermark = null;
	private Image icon = null;

	private boolean has99 = false;
	private boolean fullPaint = true;
	private boolean isClicking = false;
	private boolean fletchAndString = false;
	private boolean pause = false;

	private String status = "";
	private String name = null;

	private gui gui;
	private trayInfo trayInfo;
	private beeper beep;
	private Thread b;

	private RSTile[] path;

	private final LinkedList<MousePathPoint> mousePath = new LinkedList<MousePathPoint>();
	private final LinkedList<MousePathPoint2> mousePath2 = new LinkedList<MousePathPoint2>();
	private final LinkedList<MouseCirclePathPoint> mouseCirclePath = new LinkedList<MouseCirclePathPoint>();
	private final LinkedList<MouseCirclePathPoint2> mouseCirclePath2 = new LinkedList<MouseCirclePathPoint2>();

	private Color getColorText() {
		if (gui.comboBox12.getSelectedIndex() == 0) {
			return Color.BLACK;
		} else if (gui.comboBox12.getSelectedIndex() == 1) {
			return Color.RED;
		} else if (gui.comboBox12.getSelectedIndex() == 2) {
			return Color.ORANGE;
		} else if (gui.comboBox12.getSelectedIndex() == 3) {
			return Color.BLUE;
		} else if (gui.comboBox12.getSelectedIndex() == 4) {
			return Color.GREEN;
		} else if (gui.comboBox12.getSelectedIndex() == 5) {
			return Color.YELLOW;
		} else if (gui.comboBox12.getSelectedIndex() == 6) {
			return Color.PINK;
		} else if (gui.comboBox12.getSelectedIndex() == 7) {
			return Color.WHITE;
		} else if (gui.comboBox12.getSelectedIndex() == 8) {
			return constants.TAN;
		}
		return Color.BLACK;
	}

	private Color getColorPaint() {
		if (gui.comboBox13.getSelectedIndex() == 0) {
			return Color.BLACK;
		} else if (gui.comboBox13.getSelectedIndex() == 1) {
			return Color.RED;
		} else if (gui.comboBox13.getSelectedIndex() == 2) {
			return Color.ORANGE;
		} else if (gui.comboBox13.getSelectedIndex() == 3) {
			return Color.BLUE;
		} else if (gui.comboBox13.getSelectedIndex() == 4) {
			return Color.GREEN;
		} else if (gui.comboBox13.getSelectedIndex() == 5) {
			return Color.YELLOW;
		} else if (gui.comboBox13.getSelectedIndex() == 6) {
			return Color.PINK;
		} else if (gui.comboBox13.getSelectedIndex() == 7) {
			return Color.WHITE;
		} else if (gui.comboBox13.getSelectedIndex() == 8) {
			return constants.TAN;
		}
		return Color.BLACK;
	}

	private Color getColorProgressBarBelow() {
		if (gui.comboBox8.getSelectedIndex() == 0) {
			return Color.BLACK;
		} else if (gui.comboBox8.getSelectedIndex() == 1) {
			return Color.RED;
		} else if (gui.comboBox8.getSelectedIndex() == 2) {
			return Color.ORANGE;
		} else if (gui.comboBox8.getSelectedIndex() == 3) {
			return Color.BLUE;
		} else if (gui.comboBox8.getSelectedIndex() == 4) {
			return Color.GREEN;
		} else if (gui.comboBox8.getSelectedIndex() == 5) {
			return Color.YELLOW;
		} else if (gui.comboBox8.getSelectedIndex() == 6) {
			return Color.PINK;
		} else if (gui.comboBox8.getSelectedIndex() == 7) {
			return Color.WHITE;
		} else if (gui.comboBox8.getSelectedIndex() == 8) {
			return constants.TAN;
		}
		return Color.BLACK;
	}

	private Color getColorProgressBarOnTop() {
		if (gui.comboBox9.getSelectedIndex() == 0) {
			return Color.BLACK;
		} else if (gui.comboBox9.getSelectedIndex() == 1) {
			return Color.RED;
		} else if (gui.comboBox9.getSelectedIndex() == 2) {
			return Color.ORANGE;
		} else if (gui.comboBox9.getSelectedIndex() == 3) {
			return Color.BLUE;
		} else if (gui.comboBox9.getSelectedIndex() == 4) {
			return Color.GREEN;
		} else if (gui.comboBox9.getSelectedIndex() == 5) {
			return Color.YELLOW;
		} else if (gui.comboBox9.getSelectedIndex() == 6) {
			return Color.PINK;
		} else if (gui.comboBox9.getSelectedIndex() == 7) {
			return Color.WHITE;
		} else if (gui.comboBox9.getSelectedIndex() == 8) {
			return constants.TAN;
		}
		return Color.BLACK;
	}

	private Color getColorRSBotLine() {
		if (gui.comboBox10.getSelectedIndex() == 0) {
			return Color.BLACK;
		} else if (gui.comboBox10.getSelectedIndex() == 1) {
			return Color.RED;
		} else if (gui.comboBox10.getSelectedIndex() == 2) {
			return Color.ORANGE;
		} else if (gui.comboBox10.getSelectedIndex() == 3) {
			return Color.BLUE;
		} else if (gui.comboBox10.getSelectedIndex() == 4) {
			return Color.GREEN;
		} else if (gui.comboBox10.getSelectedIndex() == 5) {
			return Color.YELLOW;
		} else if (gui.comboBox10.getSelectedIndex() == 6) {
			return Color.PINK;
		} else if (gui.comboBox10.getSelectedIndex() == 7) {
			return Color.WHITE;
		} else if (gui.comboBox10.getSelectedIndex() == 8) {
			return constants.TAN;
		}
		return Color.BLACK;
	}

	private Color getColorRSBotCrosshair() {
		if (gui.comboBox11.getSelectedIndex() == 0) {
			return Color.BLACK;
		} else if (gui.comboBox11.getSelectedIndex() == 1) {
			return Color.RED;
		} else if (gui.comboBox11.getSelectedIndex() == 2) {
			return Color.ORANGE;
		} else if (gui.comboBox11.getSelectedIndex() == 3) {
			return Color.BLUE;
		} else if (gui.comboBox11.getSelectedIndex() == 4) {
			return Color.GREEN;
		} else if (gui.comboBox11.getSelectedIndex() == 5) {
			return Color.YELLOW;
		} else if (gui.comboBox11.getSelectedIndex() == 6) {
			return Color.PINK;
		} else if (gui.comboBox11.getSelectedIndex() == 7) {
			return Color.WHITE;
		} else if (gui.comboBox11.getSelectedIndex() == 8) {
			return constants.TAN;
		}
		return Color.BLACK;
	}

	private Color getColorUserLine() {
		if (gui.comboBox14.getSelectedIndex() == 0) {
			return Color.BLACK;
		} else if (gui.comboBox14.getSelectedIndex() == 1) {
			return Color.RED;
		} else if (gui.comboBox14.getSelectedIndex() == 2) {
			return Color.ORANGE;
		} else if (gui.comboBox14.getSelectedIndex() == 3) {
			return Color.BLUE;
		} else if (gui.comboBox14.getSelectedIndex() == 4) {
			return Color.GREEN;
		} else if (gui.comboBox14.getSelectedIndex() == 5) {
			return Color.YELLOW;
		} else if (gui.comboBox14.getSelectedIndex() == 6) {
			return Color.PINK;
		} else if (gui.comboBox14.getSelectedIndex() == 7) {
			return Color.WHITE;
		} else if (gui.comboBox14.getSelectedIndex() == 8) {
			return constants.TAN;
		}
		return Color.BLACK;
	}

	private Color getColorUserCrosshair() {
		if (gui.comboBox15.getSelectedIndex() == 0) {
			return Color.BLACK;
		} else if (gui.comboBox15.getSelectedIndex() == 1) {
			return Color.RED;
		} else if (gui.comboBox15.getSelectedIndex() == 2) {
			return Color.ORANGE;
		} else if (gui.comboBox15.getSelectedIndex() == 3) {
			return Color.BLUE;
		} else if (gui.comboBox15.getSelectedIndex() == 4) {
			return Color.GREEN;
		} else if (gui.comboBox15.getSelectedIndex() == 5) {
			return Color.YELLOW;
		} else if (gui.comboBox15.getSelectedIndex() == 6) {
			return Color.PINK;
		} else if (gui.comboBox15.getSelectedIndex() == 7) {
			return Color.WHITE;
		} else if (gui.comboBox15.getSelectedIndex() == 8) {
			return constants.TAN;
		}
		return Color.BLACK;
	}

	private int getMethod() {
		if (gui.comboBox1.getSelectedIndex() == 0) {
			return 1;
		} else if (gui.comboBox1.getSelectedIndex() == 1) {
			return 2;
		} else if (gui.comboBox1.getSelectedIndex() == 2) {
			return 3;
		} else if (gui.comboBox1.getSelectedIndex() == 3) {
			return 4;
		}
		return -1;
	}

	private int getLogId() {
		if (gui.comboBox2.getSelectedIndex() == 0) {
			return 1511;
		} else if (gui.comboBox2.getSelectedIndex() == 1) {
			return 1521;
		} else if (gui.comboBox2.getSelectedIndex() == 2) {
			return 1519;
		} else if (gui.comboBox2.getSelectedIndex() == 3) {
			return 1517;
		} else if (gui.comboBox2.getSelectedIndex() == 4) {
			return 1515;
		} else if (gui.comboBox2.getSelectedIndex() == 5) {
			return 1513;
		}
		return -1;
	}

	private int[] getTreeId() {
		if (gui.comboBox2.getSelectedIndex() == 0) {
			return new int[] { 1278, 1276, 38787, 38760, 38788, 38784, 38783,
					38782 };
		} else if (gui.comboBox2.getSelectedIndex() == 1) {
			return new int[] { 1281, 38731 };
		} else if (gui.comboBox2.getSelectedIndex() == 2) {
			return new int[] { 5551, 5552, 5553, 1308, 38616, 38617, 38627 };
		} else if (gui.comboBox2.getSelectedIndex() == 3) {
			return new int[] { 1307 };
		} else if (gui.comboBox2.getSelectedIndex() == 4) {
			return new int[] { 1309, 38755 };
		} else if (gui.comboBox2.getSelectedIndex() == 5) {
			return new int[] { 1306 };
		}
		return null;
	}

	private int getUnstrungId() {
		if (getBowType() == 1) { // 1 = Shortbows, 2 = Longbows
			if (gui.comboBox2.getSelectedIndex() == 0) {
				return 50;
			} else if (gui.comboBox2.getSelectedIndex() == 1) {
				return 54;
			} else if (gui.comboBox2.getSelectedIndex() == 2) {
				return 60;
			} else if (gui.comboBox2.getSelectedIndex() == 3) {
				return 64;
			} else if (gui.comboBox2.getSelectedIndex() == 4) {
				return 68;
			} else if (gui.comboBox2.getSelectedIndex() == 5) {
				return 72;
			}
		} else {
			if (gui.comboBox2.getSelectedIndex() == 0) {
				return 48;
			} else if (gui.comboBox2.getSelectedIndex() == 1) {
				return 56;
			} else if (gui.comboBox2.getSelectedIndex() == 2) {
				return 58;
			} else if (gui.comboBox2.getSelectedIndex() == 3) {
				return 62;
			} else if (gui.comboBox2.getSelectedIndex() == 4) {
				return 66;
			} else if (gui.comboBox2.getSelectedIndex() == 5) {
				return 70;
			}
		}
		return -1;
	}

	private int getBowType() {
		if (gui.comboBox3.getSelectedIndex() == 0) {
			return 1;
		} else if (gui.comboBox3.getSelectedIndex() == 1) {
			return 2;
		} else if (gui.comboBox3.getSelectedIndex() == 2) {
			return 3;
		} else if (gui.comboBox3.getSelectedIndex() == 3) {
			return 4;
		}
		return -1;
	}

	private int getKnifeId() {
		if (gui.comboBox4.getSelectedIndex() == 0) {
			return 946;
		} else if (gui.comboBox4.getSelectedIndex() == 1) {
			return 14111;
		}
		return -1;
	}

	private int getAxeId() {
		if (gui.comboBox5.getSelectedIndex() == 0) {
			return 1351;
		} else if (gui.comboBox5.getSelectedIndex() == 1) {
			return 1349;
		} else if (gui.comboBox5.getSelectedIndex() == 2) {
			return 1361;
		} else if (gui.comboBox5.getSelectedIndex() == 3) {
			return 1355;
		} else if (gui.comboBox5.getSelectedIndex() == 4) {
			return 1357;
		} else if (gui.comboBox5.getSelectedIndex() == 5) {
			return 1359;
		} else if (gui.comboBox5.getSelectedIndex() == 6) {
			return 6739;
		}
		return -1;
	}

	private boolean isBusy() {
		if (getMethod() == 2) {
			if (getMyPlayer().getAnimation() == -1) {
				for (int i = 0; i < 50; i++) {
					sleep(50);
					if (getMyPlayer().getAnimation() != -1
							|| inventory.getCount() == 28
							|| inventory.getCount() == 0) {
						break;
					}
				}
			}
		}
		sleep(25);
		return (getMyPlayer().getAnimation() != -1);
	}

	private boolean openBank() {
		return bank.open();
	}

	private boolean closeSWIFace() {
		if (interfaces.get(276).isValid()) {
			sleep(random(100, 200));
			interfaces.get(276).getComponent(76).doClick(true);
			sleep(random(300, 400));
		}
		return !interfaces.get(276).isValid();
	}

	public int loop() {
		if (getMethod() == 1) {
			fletch();
			sleep(random(200, 250));
		} else if (getMethod() == 2) {
			string();
			sleep(random(200, 250));
		} else if (getMethod() == 3) {
			fletchAndString();
			sleep(random(200, 250));
		} else if (getMethod() == 4) {
			cfd();
			sleep(random(200, 250));
		}
		closeSWIFace();
		pauseScript();
		return random(200, 300);
	}

	private void fletch() {
		amount = Integer.parseInt(gui.textField1.getText());
		if (!inventory.contains(getKnifeId()) && amount == 0 && !isBusy()
				&& !interfaces.get(905).isValid()) {
			withdrawKnife();
			sleep(random(200, 250));
		}
		if (!inventory.contains(getKnifeId()) && fletched <= amount
				&& !isBusy() && !interfaces.get(905).isValid()) {
			withdrawKnife();
			sleep(random(200, 250));
		}
		if (!inventory.contains(getLogId()) && amount == 0 && !isBusy()
				&& !interfaces.get(905).isValid()) {
			if (getBowType() == 1 || getBowType() == 2) {
				withdrawLogs();
			} else if (getBowType() == 3) {
				withdrawShafts();
			} else if (getBowType() == 4) {
				withdrawStocks();
			}
			sleep(random(200, 250));
		} else if (!inventory.contains(getLogId()) && fletched <= amount
				&& !isBusy() && !interfaces.get(905).isValid()) {
			if (getBowType() == 1 || getBowType() == 2) {
				withdrawLogs();
			} else if (getBowType() == 3) {
				withdrawShafts();
			} else if (getBowType() == 4) {
				withdrawStocks();
			}
			sleep(random(200, 250));
		} else if (fletchAndString && !isBusy() && fletched >= amount
				&& amount > 0) {
			gui.comboBox1.setSelectedItem("String");
		} else if (fletchAndString && !isBusy() && amount == 0
				&& bank.getItem(getLogId()) == null
				&& !interfaces.get(905).isValid()) {
			sleep(random(50, 100));
			if (fletchAndString && !isBusy() && amount == 0
					&& bank.getItem(getLogId()) == null
					&& !interfaces.get(905).isValid()
					&& inventory.getCount() < 1) {
				gui.comboBox1.setSelectedItem("String");
			}
		} else if (fletched >= amount && amount != 0 && !fletchAndString) {
			log("Fletched amount logging out!");
			stopScript();
		}
		if (inventory.contains(getLogId())
				&& inventory.containsOneOf(getKnifeId()) && amount == 0
				&& !isBusy()) {
			if (getBowType() == 1 || getBowType() == 2) {
				fletchLogs();
			} else if (getBowType() == 3) {
				fletchShafts();
			} else if (getBowType() == 4) {
				fletchStocks();
			}
			sleep(random(200, 250));
		} else if (inventory.contains(getLogId())
				&& inventory.containsOneOf(getKnifeId()) && fletched <= amount
				&& !isBusy()) {
			if (getBowType() == 1 || getBowType() == 2) {
				fletchLogs();
			} else if (getBowType() == 3) {
				fletchShafts();
			} else if (getBowType() == 4) {
				fletchStocks();
			}
			sleep(random(100, 250));
		} else if (fletchAndString && !isBusy() && fletched >= amount
				&& amount > 0) {
			gui.comboBox1.setSelectedItem("String");
		} else if (fletchAndString && !isBusy() && amount == 0
				&& bank.getItem(getLogId()) == null
				&& !interfaces.get(905).isValid()) {
			sleep(random(50, 100));
			if (fletchAndString && !isBusy() && amount == 0
					&& bank.getItem(getLogId()) == null
					&& !interfaces.get(905).isValid()
					&& inventory.getCount() < 1) {
				gui.comboBox1.setSelectedItem("String");
			}
		} else if (fletched >= amount && amount != 0 && !isBusy()
				&& !fletchAndString) {
			log("Fletched amount logging out!");
			stopScript();
		}
		if (isBusy() && !interfaces.get(740).isValid()) {
			antiban();
			sleep(random(200, 250));
		}
		clickContinue();
		if (gui.checkBox5.isSelected()) {
			checkfor99();
		}
	}

	private void string() {
		amount = Integer.parseInt(gui.textField1.getText());
		if (!inventory.contains(getUnstrungId()) || !inventory.contains(1777)
				&& amount == 0 && !isBusy() && !interfaces.get(905).isValid()) {
			withdrawStrings();
			sleep(random(200, 250));
		} else if (!inventory.contains(getUnstrungId())
				|| !inventory.contains(1777) && strung <= amount && !isBusy()
				&& !interfaces.get(905).isValid()) {
			withdrawStrings();
			sleep(random(200, 250));
		} else if (strung >= amount && amount != 0) {
			log("strung the chosen amount of bows!");
			stopScript();
		}
		if (inventory.contains(getUnstrungId()) && inventory.contains(1777)
				&& amount == 0 && !isBusy()) {
			stringBows();
			sleep(random(200, 250));
		} else if (inventory.contains(getUnstrungId())
				&& inventory.contains(1777) && strung <= amount && !isBusy()) {
			stringBows();
			sleep(random(200, 250));
		} else if (strung >= amount && amount != 0) {
			log("strung the chosen amount of bows!");
			stopScript();
		}

		if (isBusy() && !interfaces.get(740).isValid()
				&& inventory.contains(1777)
				&& inventory.contains(getUnstrungId())) {
			antiban();
			sleep(random(400, 500));
		}
		clickContinue();
		if (gui.checkBox5.isSelected()) {
			checkfor99();
		}
	}

	private void fletchAndString() {
		fletchAndString = true;
		gui.comboBox1.setSelectedItem("Fletch");
	}

	private void cfd() {
		amount = Integer.parseInt(gui.textField1.getText());
		if (!inventory.contains(getAxeId())
				|| !inventory.contains(getKnifeId())) {
			log("Get a axe and knife before starting");
			log("If you have the supplys...");
			log("select the right item in the gui!");
			log("Script stopping");
			sleep(2000);
			stopScript(false);
			sleep(500);
		}
		if (amount == 0 && !isBusy() && !interfaces.get(905).isValid()) {
			chopLogs();
		} else if (fletched <= amount && !isBusy()
				&& !interfaces.get(905).isValid()) {
			chopLogs();
		} else if (fletched >= amount && amount != 0) {
			log("Done the amount required!");
			stopScript();
		}

		if (inventory.contains(getLogId())
				&& inventory.containsOneOf(getKnifeId()) && amount == 0
				&& !isBusy() && inventory.isFull()) {
			if (getBowType() == 1 || getBowType() == 2) {
				fletchLogs();
				full = 0;
			} else if (getBowType() == 3) {
				fletchShafts();
				full = 0;
			} else if (getBowType() == 4) {
				fletchStocks();
				full = 0;
			}
		} else if (inventory.contains(getLogId()) && fletched <= amount
				&& !isBusy() && inventory.isFull()) {
			if (getBowType() == 1 || getBowType() == 2) {
				fletchLogs();
				full = 0;
			} else if (getBowType() == 3) {
				fletchShafts();
				full = 0;
			} else if (getBowType() == 4) {
				fletchStocks();
				full = 0;
			}
		} else if (fletched >= amount && amount != 0) {
			log("Done the amount required!");
			stopScript();
		}

		if (inventory.contains(getUnstrungId())
				&& inventory.containsOneOf(getKnifeId()) && amount == 0
				&& !isBusy() && inventory.isFull() && getBowType() != 3) {
			drop();
		} else if (inventory.contains(getUnstrungId()) && fletched <= amount
				&& !isBusy() && inventory.isFull()) {
			drop();
		} else if (fletched >= amount && amount != 0 && getBowType() != 3) {
			log("Done the amount required!");
			stopScript();
		}
		while (isBusy() && !interfaces.get(740).isValid()) {
			antiban();
			sleep(random(200, 250));
		}
		clickContinue();
		if (gui.checkBox5.isSelected()) {
			checkfor99();
		}
	}

	private void clickContinue() {
		if (interfaces.get(740).isValid()) {
			status = "Level up: Clicking Continue";
			sleep(50, 75);
			if (gui.checkBox2.isSelected()) {
				env.saveScreenshot(true);
			}
			if (gui.checkBox3.isSelected()
					&& skills.getRealLevel(Skills.FLETCHING) == 99 && !has99) {
				log("If you have 99 already, Disable at 99 for screenshots!");
				env.saveScreenshot(true);
				has99 = true;
			}
			trayInfo.systray.displayMessage("Level UP", "You are now level: "
					+ skills.getCurrentLevel(Skills.FLETCHING),
					TrayIcon.MessageType.INFO);
			sleep(150, 1500);
			interfaces.get(740).getComponent(3).doClick(true);
			sleep(150, 400);
		}
	}

	private void checkfor99() {
		currentexp = skills.getCurrentExp(Skills.FLETCHING);
		if (currentexp >= xpIsClose) {
			status = "Check 99: Logging out";
			if (bank.isOpen()) {
				bank.close();
			}
			stopScript(true);
		}
	}

	private void withdrawKnife() {
		status = "Banking: Knife";
		try {
			sleep(10, 20);
			openBank();
			sleep(200, 400);
			if (bank.isOpen()) {
				sleep(100, 250);
				if (!inventory.contains(getKnifeId())) {
					if (inventory.getCount() > 0)
						bank.depositAll();
					sleep(100, 150);
					if (getMethod() != 3) {
						if (bank.getItem(getKnifeId()) == null) {
							log("could not find a knife, logging out!");
							stopScript();
						}
					}
					bank.withdraw(getKnifeId(), 1);
					sleep(50, 100);
				}
			}
		} catch (Exception e) {
		}
	}

	private void withdrawLogs() {
		status = "Banking: Logs";
		try {
			sleep(10, 20);
			if (openBank()) {
				if (bank.isOpen()) {
					sleep(200, 400);
					if (bank.depositAllExcept(getKnifeId())) {
						for (int i = 0; i < 10; i++) {
							sleep(30);
							if (inventory.getCount() == 0) {
								break;
							}
						}
					}
					if (bank.getItem(getLogId()) == null) {
						if (fletchAndString) {
							gui.comboBox1.setSelectedItem("String");
						} else {
							log("could not find any Logs, logging out!");
							stopScript();
						}
					}
					bank.withdraw(getLogId(), 0);
					sleep(50, 100);
					for (int i = 0; i < 25; i++) {
						sleep(50);
						if (inventory.contains(getLogId())) {
							break;
						}
					}
				}
			}
		} catch (Exception e) {
		}
	}

	private void withdrawStrings() {
		status = "Banking: Stringing";
		try {
			if (!inventory.contains(1777)
					|| !inventory.contains(getUnstrungId())) {
				openBank();
				if (bank.isOpen()) {
					if (inventory.getCount() > 0) {
						bank.depositAll();
						sleep(50);
						for (int i = 0; i < 20; i++) {
							sleep(25);
							if (inventory.getCount() == 0) {
								break;
							}
						}
					}
					if (inventory.getCount(getUnstrungId()) != 14) {
						if (inventory.getCount(getUnstrungId()) > 0) {
							bank.deposit(getUnstrungId(), 0);
						}
						if (bank.getCount(getUnstrungId()) > 0) {
							bank.withdraw(getUnstrungId(), 14);
							sleep(100);
							for (int i = 0; i < 25; i++) {
								sleep(75);
								if (inventory.contains(getUnstrungId())) {
									break;
								}
							}
						} else if (bank.isOpen()) {
							if (bank.getCount(getUnstrungId()) == 0) {
								log("No more bows (u) in bank.");
								stopScript(true);
							}
						}
					}
					sleep(100);
					if (inventory.getCount(constants.BOW_STRING_ID) != 14) {
						if (inventory.getCount(constants.BOW_STRING_ID) > 0) {
							bank.deposit(getUnstrungId(), 0);
						}
						if (bank.getCount(constants.BOW_STRING_ID) > 0) {
							bank.withdraw(constants.BOW_STRING_ID, 14);
							sleep(100);
							for (int i = 0; i < 25; i++) {
								sleep(75);
								if (inventory.contains(constants.BOW_STRING_ID)) {
									break;
								}
							}
						} else if (bank.isOpen()) {
							if (bank.getCount(constants.BOW_STRING_ID) == 0) {
								log("No more bows (u) in bank.");
								stopScript(true);
							}
						}
					}
				}
			}
		} catch (Exception e) {
		}
	}

	private void withdrawShafts() {
		status = "Banking: Shafts";
		try {
			sleep(10, 20);
			if (openBank()) {
				if (getLogId() != 1511) {
					log("Please select normal logs!");
					stopScript();
				} else if (getLogId() == 1511 && bank.isOpen()) {
					sleep(200, 400);
					bank.depositAllExcept(getKnifeId());
					sleep(100, 150);
					if (bank.getItem(getLogId()) == null) {
						log("Out of logs, Logging out!");
						stopScript();
					}
					bank.withdraw(getLogId(), 0);
					for (int i = 0; i < 200; i++) {
						sleep(50);
						if (inventory.contains(getLogId())) {
							bank.close();
							break;
						}
					}
					sleep(30, 50);
				}
			}
		} catch (Exception e) {
		}
	}

	private void withdrawStocks() {
		status = "Banking: Stocks";
		try {
			sleep(10, 20);
			if (openBank()) {
				if (getLogId() == 1513) {
					log("Please select a different log!");
					stopScript();
				} else if (getLogId() != 1513 && bank.isOpen()) {
					sleep(200, 400);
					bank.depositAllExcept(getKnifeId());
					sleep(100, 150);
					if (bank.getItem(getLogId()) == null) {
						log("Out of logs, Logging out!");
						stopScript();
					}
					bank.withdraw(getLogId(), 0);
					for (int i = 0; i < 200; i++) {
						sleep(50);
						if (inventory.contains(getLogId())) {
							bank.close();
							break;
						}
					}
					sleep(30, 50);
				}
			}
		} catch (Exception e) {
		}
	}

	private void fletchLogs() {
		status = "Fletching: UBows";
		try {
			if (bank.isOpen()) {
				bank.close();
			}
			sleep(50, 100);
			if (!interfaces.get(905).isValid() && !isBusy()
					&& inventory.containsOneOf(getKnifeId())) {
				if (random(1, 2) == 1) {
					inventory.getItem(getLogId()).doClick(true);
					sleep(200, 400);
					inventory.getItem(getKnifeId()).doClick(true);
				} else {
					inventory.getItem(getKnifeId()).doClick(true);
					sleep(200, 400);
					inventory.getItem(getLogId()).doClick(true);
				}
			}
			sleep(50, 100);
			mouse.move(random(35, 448), random(500, 355));
			sleep(400, 450);
			if (interfaces.get(905).isValid()) {
				if (getBowType() == 1) {
					status = "Fletching: short";
					if (getLogId() == 1511) {
						sleep(200, 250);
						interfaces.get(905).getComponent(15)
								.doAction("Make All");
					} else {
						sleep(200, 250);
						interfaces.get(905).getComponent(14)
								.doAction("Make All");
					}
				}
				if (getBowType() == 2) {
					status = "Fletching: long";
					if (getLogId() == 1511) {
						sleep(200, 250);
						interfaces.get(905).getComponent(16)
								.doAction("Make All");
					} else {
						sleep(200, 250);
						interfaces.get(905).getComponent(15)
								.doAction("Make All");
					}
				}
			}
			sleep(50, 200);
		} catch (Exception e) {
		}
	}

	private void stringBows() {
		status = "Stringing: Bows";
		try {
			if (bank.isOpen()) {
				bank.close();
			}
			if (!interfaces.get(905).isValid() && !isBusy()
					&& inventory.contains(1777)
					&& inventory.contains(getUnstrungId())) {
				if (random(1, 2) == 1) {
					inventory.getItem(getUnstrungId()).doClick(true);
					sleep(200, 400);
					inventory.getItem(1777).doClick(true);
					sleep(random(200, 400));
				} else {
					inventory.getItem(1777).doClick(true);
					sleep(200, 400);
					inventory.getItem(getUnstrungId()).doClick(true);
					sleep(random(200, 400));
				}
			}
			sleep(50, 100);
			mouse.moveRandomly(150, 500);
			sleep(400, 450);
			if (interfaces.get(905).isValid()) {
				sleep(200, 250);
				interfaces.get(905).getComponent(14).doAction("Make All");
				sleep(50, 200);
			}
		} catch (Exception e) {
		}
	}

	private void fletchShafts() {
		status = "Fletching: Shafts";
		try {
			if (bank.isOpen())
				bank.close();
			sleep(50, 100);
			if (!interfaces.get(905).isValid() && !isBusy()) {
				if (random(1, 2) == 1) {
					inventory.getItem(getLogId()).doClick(true);
					sleep(200, 400);
					inventory.getItem(getKnifeId()).doClick(true);
				} else {
					inventory.getItem(getKnifeId()).doClick(true);
					sleep(200, 400);
					inventory.getItem(getLogId()).doClick(true);
				}
			}
			sleep(50, 100);
			mouse.moveRandomly(150, 500);
			sleep(400, 450);
			if (interfaces.get(905).isValid()) {
				if (getLogId() == 1511) {
					sleep(200, 250);
					interfaces.get(905).getComponent(14).doClick(true);
				} else if (getLogId() != 1511) {
					log("Please select normal logs!");
					stopScript();
				}
			}
			sleep(50, 200);
		} catch (Exception e) {
		}
	}

	private void fletchStocks() {
		status = "Fletching: Stocks";
		try {
			if (bank.isOpen())
				bank.close();
			sleep(50, 100);
			if (!interfaces.get(905).isValid() && !isBusy()) {
				if (random(1, 2) == 1) {
					inventory.getItem(getLogId()).doClick(true);
					sleep(200, 400);
					inventory.getItem(getKnifeId()).doClick(true);
				} else {
					inventory.getItem(getKnifeId()).doClick(true);
					sleep(200, 400);
					inventory.getItem(getLogId()).doClick(true);
				}
			}
			sleep(50, 100);
			mouse.moveRandomly(150, 500);
			sleep(400, 450);
			if (interfaces.get(905).isValid()) {
				if (getLogId() == 1513) {
					log("Please slect a different log!");
					stopScript();
				} else if (getLogId() != 1513) {
					if (getLogId() == 1511) {
						sleep(200, 250);
						interfaces.get(905).getComponent(17).doClick(true);
					} else {
						sleep(200, 250);
						interfaces.get(905).getComponent(17).doClick(true);
					}
				}
			}
			sleep(50, 200);
		} catch (Exception e) {
		}
	}

	private void chopLogs() {
		walk();
		status = "Chop: Logs";
		if (objects.getNearest(getTreeId()) != null
				&& getMyPlayer().getAnimation() == -1
				&& !getMyPlayer().isMoving()) {
			if (objects.getNearest(getTreeId()) != null
					&& !isBusy()
					&& calc.tileOnScreen(objects.getNearest(getTreeId())
							.getLocation())) {
				objects.getNearest(getTreeId()).doAction("Chop");
				sleep(random(1000, 1250));
				if (full > 5) {
					log("Inventory was to full, error!");
					log("Clearing out inventory!");
					full = 0;
					drop();
				}
				if (fail > 3) {
					status = "Fail: getting new tree.";
					walking.walkTileMM(getMyPlayer().getLocation().randomize(
							10, 10));
					walk();
					fail = 0;
				}
				inventory.dropAllExcept(getAxeId(), getKnifeId(), 52,
						getLogId(), 15544, 15545);
			}
		}
	}

	private void drop() {
		status = "Drop: Fletched items";
		inventory.dropAllExcept(getAxeId(), getKnifeId(), getLogId(), 15544,
				15545, 52);
	}

	@SuppressWarnings("deprecation")
	public void walk() {
		status = "Walking";
		if (objects.getNearest(getTreeId()) != null
				&& getMyPlayer().getAnimation() == -1
				&& !getMyPlayer().isMoving()) {
			if (objects.getNearest(getTreeId()) != null && !isBusy()) {
				camera.setPitch(random(90, 100));
				path = walking.findPath(objects.getNearest(getTreeId())
						.getLocation().randomize(3, 3));
				walking.newTilePath(path).traverse();
				sleep(random(600, 650));
				while (getMyPlayer().isMoving()) {
					sleep(random(150, 250));
				}
			}
		} else if (objects.getNearest(getTreeId()) == null) {
			log("Tree out of reach, please start closer to the tree!");
			stopScript();
		}
	}

	public void messageReceived(MessageEvent e) {
		try {
			String m = e.getMessage().toLowerCase();
			int person = e.getID();
			if (m.contains("you carefully cut")
					&& person == MessageEvent.MESSAGE_ACTION) {
				fletched++;
			}
			if (m.contains("you add a string to the bow")
					&& person == MessageEvent.MESSAGE_ACTION) {
				strung++;
			}
			if (m.contains("you can't reach that")
					&& person == MessageEvent.MESSAGE_ACTION) {
				fail++;
			}
			if (m.contains("your inventory is too full")
					&& person == MessageEvent.MESSAGE_ACTION) {
				fail++;
				full++;
			}
			if (m.contains("you need a")
					&& person == MessageEvent.MESSAGE_ACTION) {
				log("not high enough level! Stopping!");
				stopScript(true);
			}
			if (gui.checkBox15.isSelected()) {
				if (person == MessageEvent.MESSAGE_CHAT
						|| person == MessageEvent.MESSAGE_CLAN_CHAT
						|| person == MessageEvent.MESSAGE_PRIVATE_IN) {
					trayInfo.systray.displayMessage(e.getSender() + ":",
							e.getMessage(), TrayIcon.MessageType.WARNING);
				}
			}
		} catch (Exception e1) {
		}
	}

	private void antiban() {
		status = "Antiban:";
		int r = random(1, 200);
		if (r == 1) {
			status = "Antiban: Mouse";
			mouse.moveRandomly(100, 200);
			sleep(random(2000, 2500));
		}
		if (r == 6) {
			status = "Antiban: Mouse";
			mouse.moveRandomly(25, 150);
			sleep(random(1000, 2500));
		}
		if (r == 12) {
			status = "Antiban: Stats";
			if (game.getCurrentTab() != Game.TAB_STATS) {
				game.openTab(Game.TAB_STATS);
				sleep(350, 500);
				mouse.move(random(615, 665), random(350, 375));
				sleep(1000, 1200);
				if (game.getCurrentTab() != Game.TAB_INVENTORY) {
					game.openTab(4);
					sleep(random(100, 200));
				}
			}
		}
		if (r == 19) {
			status = "Antiban: AFK";
			sleep(random(2000, 2500));
		}
		if (r == 26) {
			status = "Antiban: Camera";

			camera.setAngle(random(0, 300));
			camera.setPitch(random(35, 85));
			sleep(random(1750, 1950));
		}
	}

	private void pauseScript() {
		if (pause) {
			log("Pausing...");
			status = "Paused";
			while (pause) {
				sleep(400, 600);
			}
		}
	}

	public boolean onStart() {
		for (int i = 0; i < 80; i++) {
			sleep(80);
			if (game.isLoggedIn()) {
				break;
			}
		}
		sleep(random(400, 425));
		if (!game.isLoggedIn()) {
			JOptionPane.showMessageDialog(null, "Please completely login!");
			return false;
		}
		JOptionPane.showMessageDialog(null, "Please wait while gui loads.");
		gui = new gui();
		gui.progressBar1.setValue(skills
				.getPercentToNextLevel(Skills.FLETCHING));
		gui.setVisible(true);
		loadSettings();
		gui.checkBox16.setSelected(false);
		if (gui.textField2.getText().equals("All")) {
			gui.textField2.setEnabled(true);
			gui.button4.setEnabled(true);
		} else {
			gui.textField2.setEnabled(false);
			gui.button4.setEnabled(false);
		}
		name = gui.textField2.getText();
		gui.label1
				.setText("<html><img src =http://universalscripts.org/UFletch_generate.php?user="
						+ name + "> </html>");
		while (gui.isVisible()) {
			sleep(random(200, 400));
		}
		if (gui.checkBox16.isSelected()) {
			beep = new beeper();
			b = new Thread(beep);
			b.start();
		}
		paintUT = paint.createPaint();
		getExtraInfo();
		trayInfo = new trayInfo();
		gui.checkBox16.setEnabled(false);
		sleep(random(50, 75));
		return true;
	}

	private void getExtraInfo() {
		invPaint = paintUT.getImage("ufletchpaint2.png", true,
				"http://www.universalscripts.org/UFletch/ufletchpaint2.png");
		paintp = paintUT.getImage("ufletchpaint.png", true,
				"http://www.universalscripts.org/UFletch/ufletchpaint.png");
		hide = paintUT.getImage("hidepaint.png", true,
				"http://www.universalscripts.org/UFletch/hidepaint.png");
		show = paintUT.getImage("showpaint.png", true,
				"http://www.universalscripts.org/UFletch/showpaint.png");
		guiButton = paintUT.getImage("button.png", true,
				"http://www.universalscripts.org/UFletch/button.png");
		watermark = paintUT.getImage("watermark.png", true,
				"http://www.universalscripts.org/UFletch/watermark.png");
		icon = paintUT.getImage("icon.png", true,
				"http://www.universalscripts.org/UFletch/icon.png");
		sleep(random(400, 500));
		startXP = skills.getCurrentExp(Skills.FLETCHING);
		sleep(random(100, 250));
	}

	private void createSignature() {
		try {
			URL url;
			URLConnection urlConn;
			url = new URL("http://www.universalscripts.org/UFletch_submit.php");
			urlConn = url.openConnection();
			urlConn.setRequestProperty("User-Agent", "UFletchAgent");
			urlConn.setDoInput(true);
			urlConn.setDoOutput(true);
			urlConn.setUseCaches(false);
			urlConn.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");
			String content = "";
			String[] stats = { "auth", "secs", "mins", "hours", "days",
					"fletched", "strung", "expgained" };
			Object[] data = { gui.textField2.getText(), 0, 0, 0, 0, 0, 0, 0 };
			for (int i = 0; i < stats.length; i++) {
				content += stats[i] + "=" + data[i] + "&";
			}
			content = content.substring(0, content.length() - 1);
			OutputStreamWriter wr = new OutputStreamWriter(
					urlConn.getOutputStream());
			wr.write(content);
			wr.flush();
			BufferedReader rd = new BufferedReader(new InputStreamReader(
					urlConn.getInputStream()));
			String line;
			while ((line = rd.readLine()) != null) {
				log(Color.GREEN, line);
			}
			wr.close();
			rd.close();
		} catch (Exception e) {
		}
	}

	private void updateSignature() {
		try {
			long xpGained = skills.getCurrentExp(Skills.FLETCHING) - startXP;
			long millis = System.currentTimeMillis() - startTime;
			long days = millis / (1000 * 60 * 60 * 24);
			millis -= days * (1000 * 60 * 60);
			long hours = millis / (1000 * 60 * 60);
			millis -= hours * (1000 * 60 * 60);
			long minutes = millis / (1000 * 60);
			millis -= minutes * (1000 * 60);
			long seconds = millis / 1000;
			URL url;
			URLConnection urlConn;
			url = new URL("http://www.universalscripts.org/UFletch_submit.php");
			urlConn = url.openConnection();
			urlConn.setRequestProperty("User-Agent", "UFletchAgent");
			urlConn.setDoInput(true);
			urlConn.setDoOutput(true);
			urlConn.setUseCaches(false);
			urlConn.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");
			String content = "";
			String[] stats = { "auth", "secs", "mins", "hours", "days",
					"fletched", "strung", "expgained" };
			Object[] data = { gui.textField2.getText(), seconds, minutes,
					hours, days, fletched, strung, xpGained };
			for (int i = 0; i < stats.length; i++) {
				content += stats[i] + "=" + data[i] + "&";
			}
			content = content.substring(0, content.length() - 1);
			OutputStreamWriter wr = new OutputStreamWriter(
					urlConn.getOutputStream());
			wr.write(content);
			wr.flush();
			BufferedReader rd = new BufferedReader(new InputStreamReader(
					urlConn.getInputStream()));
			String line;
			while ((line = rd.readLine()) != null) {
				if (line.contains("success")) {
					log(line);
				}
			}
			wr.close();
			rd.close();
		} catch (Exception e) {
		}
	}

	public void loadSettings() {
		Properties props = new Properties();
		File f = new File(GlobalConfiguration.Paths.getCacheDirectory()
				+ "UFletch.ini");
		if (f.exists()) {
			try {
				props.load(new FileInputStream(f));
			} catch (IOException e) {
			}
			if (props.getProperty("Method") != null) {
				gui.comboBox1.setSelectedItem(props.getProperty("Method"));
			}
			if (props.getProperty("LogType") != null) {
				gui.comboBox2.setSelectedItem(props.getProperty("LogType"));
			}
			if (props.getProperty("BowType") != null) {
				gui.comboBox3.setSelectedItem(props.getProperty("BowType"));
			}
			if (props.getProperty("Knife") != null) {
				gui.comboBox4.setSelectedItem(props.getProperty("Knife"));
			}
			if (props.getProperty("AxeType") != null) {
				gui.comboBox5.setSelectedItem(props.getProperty("AxeType"));
			}
			if (props.getProperty("Color1") != null) {
				gui.comboBox12.setSelectedItem(props.getProperty("Color1"));
			}
			if (props.getProperty("Color2") != null) {
				gui.comboBox13.setSelectedItem(props.getProperty("Color2"));
			}
			if (props.getProperty("Color3") != null) {
				gui.comboBox8.setSelectedItem(props.getProperty("Color3"));
			}
			if (props.getProperty("Color4") != null) {
				gui.comboBox9.setSelectedItem(props.getProperty("Color4"));
			}
			if (props.getProperty("Color5") != null) {
				gui.comboBox10.setSelectedItem(props.getProperty("Color5"));
			}
			if (props.getProperty("Color6") != null) {
				gui.comboBox11.setSelectedItem(props.getProperty("Color6"));
			}
			if (props.getProperty("Color7") != null) {
				gui.comboBox14.setSelectedItem(props.getProperty("Color7"));
			}
			if (props.getProperty("Color8") != null) {
				gui.comboBox15.setSelectedItem(props.getProperty("Color8"));
			}
			if (props.getProperty("Amount") != null) {
				gui.textField1.setText(props.getProperty("Amount"));
			}
			if (props.getProperty("Name") != null) {
				gui.textField2.setText(props.getProperty("Name"));
			}
			if (props.getProperty("WhenDone") != null) {
				if (props.getProperty("WhenDone").contains("true")) {
					gui.checkBox1.setSelected(true);
				}
			}
			if (props.getProperty("UponLvl") != null) {
				if (props.getProperty("UponLvl").contains("true")) {
					gui.checkBox2.setSelected(true);
				}
			}
			if (props.getProperty("Getting99") != null) {
				if (props.getProperty("Getting99").contains("true")) {
					gui.checkBox3.setSelected(true);
				}
			}
			if (props.getProperty("Before99") != null) {
				if (props.getProperty("Before99").contains("true")) {
					gui.checkBox5.setSelected(true);
				}
			}
			if (props.getProperty("Save") != null) {
				if (props.getProperty("Save").contains("true")) {
					gui.checkBox6.setSelected(true);
				}
			}
			if (props.getProperty("Load") != null) {
				if (props.getProperty("Load").contains("true")) {
					gui.checkBox7.setSelected(true);
				}
			}
			if (props.getProperty("Paint") != null) {
				if (props.getProperty("Paint").contains("true")) {
					gui.checkBox4.setSelected(true);
				}
			}
			if (props.getProperty("Chat") != null) {
				if (props.getProperty("Chat").contains("true")) {
					gui.checkBox8.setSelected(true);
				}
			}
			if (props.getProperty("Inventory") != null) {
				if (props.getProperty("Inventory").contains("true")) {
					gui.checkBox9.setSelected(true);
				}
			}
			if (props.getProperty("Bar") != null) {
				if (props.getProperty("Bar").contains("true")) {
					gui.checkBox10.setSelected(true);
				}
			}
			if (props.getProperty("BotLine") != null) {
				if (props.getProperty("BotLine").contains("true")) {
					gui.checkBox11.setSelected(true);
				}
			}
			if (props.getProperty("UserLine") != null) {
				if (props.getProperty("UserLine").contains("true")) {
					gui.checkBox13.setSelected(true);
				}
			}
			if (props.getProperty("BotCross") != null) {
				if (props.getProperty("BotCross").contains("true")) {
					gui.checkBox12.setSelected(true);
				}
			}
			if (props.getProperty("UserCross") != null) {
				if (props.getProperty("UserCross").contains("true")) {
					gui.checkBox14.setSelected(true);
				}
			}
			if (props.getProperty("BotCircle") != null) {
				if (props.getProperty("BotCircle").contains("true")) {
					gui.checkBox17.setSelected(true);
				}
			}
			if (props.getProperty("UserCircle") != null) {
				if (props.getProperty("UserCircle").contains("true")) {
					gui.checkBox18.setSelected(true);
				}
			}
			if (props.getProperty("Message") != null) {
				if (props.getProperty("Message").contains("true")) {
					gui.checkBox15.setSelected(true);
				}
			}
			if (props.getProperty("Beep") != null) {
				if (props.getProperty("Beep").contains("true")) {
					gui.checkBox16.setSelected(true);
				}
			}
			if (props.getProperty("Speed") != null) {
				if (props.getProperty("Speed").contains("true")) {
					gui.slider1.setValue(Integer.parseInt(props
							.getProperty("Speed")));
				}
			}
		}
	}

	public void saveSettings() {
		Properties p = new Properties();
		p.setProperty("Method", (String) gui.comboBox1.getSelectedItem());
		p.setProperty("LogType", (String) gui.comboBox2.getSelectedItem());
		p.setProperty("BowType", (String) gui.comboBox3.getSelectedItem());
		p.setProperty("Knife", (String) gui.comboBox4.getSelectedItem());
		p.setProperty("AxeType", (String) gui.comboBox5.getSelectedItem());
		p.setProperty("Color1", (String) gui.comboBox12.getSelectedItem());
		p.setProperty("Color2", (String) gui.comboBox13.getSelectedItem());
		p.setProperty("Color3", (String) gui.comboBox8.getSelectedItem());
		p.setProperty("Color4", (String) gui.comboBox9.getSelectedItem());
		p.setProperty("Color5", (String) gui.comboBox10.getSelectedItem());
		p.setProperty("Color6", (String) gui.comboBox11.getSelectedItem());
		p.setProperty("Color7", (String) gui.comboBox14.getSelectedItem());
		p.setProperty("Color8", (String) gui.comboBox15.getSelectedItem());
		p.setProperty("Amount", (String) gui.textField1.getText());
		p.setProperty("Name", (String) gui.textField2.getText());
		p.setProperty("WhenDone", getValue(gui.checkBox1.isSelected()));
		p.setProperty("UponLvl", getValue(gui.checkBox2.isSelected()));
		p.setProperty("Getting99", getValue(gui.checkBox3.isSelected()));
		p.setProperty("Before99", getValue(gui.checkBox5.isSelected()));
		p.setProperty("Save", getValue(gui.checkBox6.isSelected()));
		p.setProperty("Load", getValue(gui.checkBox7.isSelected()));
		p.setProperty("Paint", getValue(gui.checkBox4.isSelected()));
		p.setProperty("Chat", getValue(gui.checkBox8.isSelected()));
		p.setProperty("Inventory", getValue(gui.checkBox9.isSelected()));
		p.setProperty("Bar", getValue(gui.checkBox10.isSelected()));
		p.setProperty("BotLine", getValue(gui.checkBox11.isSelected()));
		p.setProperty("BotCross", getValue(gui.checkBox12.isSelected()));
		p.setProperty("UserLine", getValue(gui.checkBox13.isSelected()));
		p.setProperty("UserCross", getValue(gui.checkBox14.isSelected()));
		p.setProperty("BotCircle", getValue(gui.checkBox17.isSelected()));
		p.setProperty("UserCircle", getValue(gui.checkBox18.isSelected()));
		p.setProperty("Message", getValue(gui.checkBox15.isSelected()));
		p.setProperty("Beep", getValue(gui.checkBox16.isSelected()));
		p.setProperty("Speed", String.valueOf(gui.slider1.getValue()));
		try {
			p.store(new FileOutputStream(GlobalConfiguration.Paths
					.getCacheDirectory() + "UFletch.ini"), "UFletch settings");
		} catch (IOException e) {
		}
	}

	private String getValue(boolean selected) {
		if (selected) {
			return "true";
		}
		return "false";
	}

	@SuppressWarnings("deprecation")
	public void onFinish() {
		updateSignature();
		log.info("Thanks for using UFletch. Have a good one ;)");
		if (gui.checkBox1.isSelected()) {
			env.saveScreenshot(true);
		}
		SystemTray.getSystemTray().remove(trayInfo.systray);
		if (gui.checkBox16.isSelected()) {
			b.interrupt();
			b.suspend();
		}
	}

	@SuppressWarnings("serial")
	private class MousePathPoint extends Point {
		private int toColor(double d) {
			return Math.min(255, Math.max(0, (int) d));
		}

		private long finishTime;
		private double lastingTime;

		public MousePathPoint(int x, int y, int lastingTime) {
			super(x, y);
			this.lastingTime = lastingTime;
			finishTime = System.currentTimeMillis() + lastingTime;
		}

		public boolean isUp() {
			return System.currentTimeMillis() > finishTime;
		}

		public Color getColor() {
			return new Color(
					getColorRSBotLine().getRed(),
					getColorRSBotLine().getGreen(),
					getColorRSBotLine().getBlue(),
					toColor(256 * ((finishTime - System.currentTimeMillis()) / lastingTime)));
		}
	}

	private class MousePathPoint2 extends Point {
		/**
		 * 
		 */
		private static final long serialVersionUID = 3567008140194371837L;

		private int toColor(double d) {
			return Math.min(255, Math.max(0, (int) d));
		}

		private long finishTime;
		private double lastingTime;

		public MousePathPoint2(int x, int y, int lastingTime) {
			super(z.x, z.y);
			this.lastingTime = lastingTime;
			finishTime = System.currentTimeMillis() + lastingTime;
		}

		public boolean isUp2() {
			return System.currentTimeMillis() > finishTime;
		}

		public Color getColor2() {
			return new Color(
					getColorUserLine().getRed(),
					getColorUserLine().getGreen(),
					getColorUserLine().getBlue(),
					toColor(256 * ((finishTime - System.currentTimeMillis()) / lastingTime)));
		}
	}

	private class MouseCirclePathPoint extends Point {
		private static final long serialVersionUID = 1L;

		private int toColor(double d) {
			return Math.min(255, Math.max(0, (int) d));
		}

		private long finishTime;
		private double lastingTime;

		public MouseCirclePathPoint(int x, int y, int lastingTime) {
			super(x, y);
			this.lastingTime = lastingTime;
			finishTime = System.currentTimeMillis() + lastingTime;
		}

		public boolean isUp() {
			return System.currentTimeMillis() > finishTime;
		}
	}

	private class MouseCirclePathPoint2 extends Point {
		private static final long serialVersionUID = 1L;

		private int toColor(double d) {
			return Math.min(255, Math.max(0, (int) d));
		}

		private long finishTime;
		private double lastingTime;

		public MouseCirclePathPoint2(int x, int y, int lastingTime) {
			super(x, y);
			this.lastingTime = lastingTime;
			finishTime = System.currentTimeMillis() + lastingTime;
		}

		public boolean isUp() {
			return System.currentTimeMillis() > finishTime;
		}
	}

	private final RenderingHints rh = new RenderingHints(
			RenderingHints.KEY_TEXT_ANTIALIASING,
			RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

	public void onRepaint(Graphics g) {
		if (gui.checkBox4.isSelected()) {
			Graphics2D g2 = (Graphics2D) g;
			g2.setRenderingHints(rh);
			long millis = System.currentTimeMillis() - startTime;
			long hours = millis / (1000 * 60 * 60);
			millis -= hours * (1000 * 60 * 60);
			long minutes = millis / (1000 * 60);
			millis -= minutes * (1000 * 60);
			long seconds = millis / 1000;
			xpGained = skills.getCurrentExp(Skills.FLETCHING) - startXP;
			xpToLevel = skills.getExpToNextLevel(Skills.FLETCHING);
			float xpsec = ((float) xpGained)
					/ (float) (seconds + (minutes * 60) + (hours * 60 * 60));
			float xpmin = xpsec * 60;
			float xphour = xpmin * 60;
			if (xpGained > 0) {
				hoursTNL = (int) Math.floor(xpToLevel / xphour);
				minsTNL = (int) Math
						.floor(((xpToLevel / xphour) - hoursTNL) * 60);
			}

			// =========> Chat Paint <=========
			if (fullPaint && !isClicking && game.isLoggedIn()
					&& gui.checkBox8.isSelected()) {
				g2.setColor(getColorPaint());
				g2.fillRect(6, 344, 507, 129);
				g2.drawImage(paintp, 6, 344, null);
				g2.drawImage(hide, 9, 347, null);
				g2.drawImage(guiButton, 360, 440, null);
				g2.drawImage(watermark, 305, 315, null);
				g2.setFont(new Font("Arial", 1, 15));
				g2.setColor(getColorText());
				g2.drawString("Time Running: " + getRuntime(), 60, 372);
				g2.drawString(
						"Exp Gained: "
								+ (skills.getCurrentExp(Skills.FLETCHING) - startXP),
						60, 391);
				g2.drawString(
						"Exp/H: "
								+ getHourly(skills
										.getCurrentExp(Skills.FLETCHING)
										- startXP), 60, 409);
				if (getMethod() != 2) {
					g2.drawString("Fletched/Strung:  " + fletched, 60, 426);
					g2.drawString(
							"Fletched/Hr:  "
									+ ((int) (new Double(fletched)
											/ new Double(System
													.currentTimeMillis()
													- startTime) * new Double(
											60 * 60 * 1000))), 60, 444);
				} else if (getMethod() == 2) {
					g2.drawString("Fletched/Strung:  " + strung, 60, 426);
					g2.drawString(
							"Fletched/Hr:  "
									+ ((int) (new Double(strung)
											/ new Double(System
													.currentTimeMillis()
													- startTime) * new Double(
											60 * 60 * 1000))), 60, 444);
				}
				g2.drawString("Status:  " + status, 60, 466);
				g2.setFont(new Font("Arial", 1, 10));
				g2.drawString("Hide Permanitly (Click to Fade)", 30, 355);
				g2.setFont(new Font("Arial", 1, 15));
			} else if (!fullPaint && isClicking && game.isLoggedIn()
					&& gui.checkBox8.isSelected()) {
				g2.drawImage(show, 9, 347, null);
				g2.drawImage(watermark, 305, 315, null);
			} else if (fullPaint && isClicking && gui.checkBox8.isSelected()) {
				g2.setColor(new Color(getColorPaint().getRed(), getColorPaint()
						.getGreen(), getColorPaint().getBlue(), 127));
				g2.fillRect(6, 344, 507, 129);
				g2.drawImage(paintp, 6, 344, null);
				g2.drawImage(hide, 9, 347, null);
				g2.drawImage(guiButton, 360, 440, null);
				g2.drawImage(watermark, 305, 315, null);
				g2.setFont(new Font("Arial", 1, 15));
				g2.setColor(new Color(getColorText().getRed(), getColorText()
						.getGreen(), getColorText().getBlue(), 127));
				g2.drawString("Time Running: " + getRuntime(), 60, 372);
				g2.drawString(
						"Exp Gained: "
								+ (skills.getCurrentExp(Skills.FLETCHING) - startXP),
						60, 391);
				g2.drawString(
						"Exp/H: "
								+ getHourly(skills
										.getCurrentExp(Skills.FLETCHING)
										- startXP), 60, 409);
				g2.drawString("Fletched/Strung:  " + fletched, 60, 426);
				g2.drawString(
						"Fletched/Hr:  "
								+ ((int) (new Double(fletched)
										/ new Double(System.currentTimeMillis()
												- startTime) * new Double(
										60 * 60 * 1000))), 60, 444);
				g2.drawString("Status:  " + status, 60, 466);
				g2.setFont(new Font("Arial", 1, 10));
				g2.drawString("Hide Permanitly (Click to Fade)", 30, 355);
				g2.setFont(new Font("Arial", 1, 15));
			} else if (gui.checkBox8.isSelected()) {
				g2.drawImage(show, 9, 347, null);
				g2.drawImage(watermark, 305, 315, null);
			}

			// ============> Inv Paint <============
			if (fullPaint && !isClicking && game.isLoggedIn()
					&& gui.checkBox9.isSelected()) {
				g2.setColor(getColorPaint());
				g2.fillRoundRect(547, 203, 189, 264, 16, 16);
				g2.setColor(Color.BLACK);
				g2.drawRoundRect(547, 203, 189, 264, 16, 16);
				g2.drawImage(invPaint, 549, 206, null);
				g2.drawImage(guiButton, 606, 283, null);
				g2.drawImage(hide, 719, 451, null);
				g2.drawImage(watermark, 305, 315, null);
				g2.setColor(getColorText());
				g2.setFont(new Font("Arial", 1, 13));
				g2.drawString("Time Running: " + getRuntime(), 560, 330);
				g2.drawString(
						"Exp Gained: "
								+ (skills.getCurrentExp(Skills.FLETCHING) - startXP),
						560, 350);
				g2.drawString(
						"Exp/H: "
								+ getHourly(skills
										.getCurrentExp(Skills.FLETCHING)
										- startXP), 560, 370);
				g2.drawString("Fletched/Strung:  " + fletched, 560, 390);
				g2.drawString(
						"Fletched/Hr:  "
								+ ((int) (new Double(fletched)
										/ new Double(System.currentTimeMillis()
												- startTime) * new Double(
										60 * 60 * 1000))), 560, 410);
				g2.drawString("Status:  " + status, 560, 430);
				g2.setFont(new Font("Arial", 1, 10));
				g2.drawString("(Click to Fade) Hide Perminatly:", 560, 460);
				g2.setFont(new Font("Arial", 1, 13));
			} else if (!fullPaint && isClicking && game.isLoggedIn()
					&& gui.checkBox9.isSelected()) {
				g2.drawImage(show, 719, 451, null);
				g2.drawImage(watermark, 305, 315, null);
			} else if (fullPaint && isClicking && gui.checkBox9.isSelected()) {
				g2.setColor(new Color(getColorPaint().getRed(), getColorPaint()
						.getGreen(), getColorPaint().getBlue(), 127));
				g2.fillRoundRect(547, 203, 189, 264, 16, 16);
				g2.setColor(Color.BLACK);
				g2.drawRoundRect(547, 203, 189, 264, 16, 16);
				g2.drawImage(invPaint, 549, 206, null);
				g2.drawImage(invPaint, 549, 206, null);
				g2.drawImage(guiButton, 606, 283, null);
				g2.drawImage(watermark, 305, 315, null);
				g2.setColor(new Color(getColorText().getRed(), getColorText()
						.getGreen(), getColorText().getBlue(), 127));
				g2.setFont(new Font("Arial", 1, 13));
				g2.drawString("Time Running: " + getRuntime(), 560, 330);
				g2.drawString(
						"Exp Gained: "
								+ (skills.getCurrentExp(Skills.FLETCHING) - startXP),
						560, 350);
				g2.drawString(
						"Exp/H: "
								+ getHourly(skills
										.getCurrentExp(Skills.FLETCHING)
										- startXP), 560, 370);
				g2.drawString("Fletched/Strung:  " + fletched, 560, 390);
				g2.drawString(
						"Fletched/Hr:  "
								+ ((int) (new Double(fletched)
										/ new Double(System.currentTimeMillis()
												- startTime) * new Double(
										60 * 60 * 1000))), 560, 410);
				g2.drawString("Status:  " + status, 560, 430);
				g2.setFont(new Font("Arial", 1, 10));
				g2.drawString("(Click to Fade) Hide Perminatly:", 560, 461);
				g2.setFont(new Font("Arial", 1, 13));
			} else if (gui.checkBox9.isSelected()) {
				g2.drawImage(show, 719, 451, null);
				g2.drawImage(watermark, 305, 315, null);
			}

			if (game.isLoggedIn() && !isClicking && fullPaint
					&& gui.checkBox10.isSelected()) {
				// =========> PROGRESS <=========
				double percent = 512 * skills
						.getPercentToNextLevel(Skills.FLETCHING) / 100.0;
				GradientPaint base = new GradientPaint(4, 3, new Color(255,
						255, 255, 200), 4, 3 + 22 + 3,
						getColorProgressBarBelow());
				GradientPaint overlay = new GradientPaint(4, 3, new Color(255,
						255, 255, 200), 4, 3 + 22 + 3,
						getColorProgressBarOnTop());
				g2.setPaint(base);
				g2.fillRect(4, 3, 512, 22);
				g2.setPaint(overlay);
				g2.fillRect(4, 3, (int) percent, 22);
				g2.setColor(Color.black);
				g2.drawRect(4, 3, 512, 22);
				g2.setFont(new Font("Arial", 0, 13));
				String progress = skills
						.getPercentToNextLevel(Skills.FLETCHING)
						+ "% to "
						+ (skills.getCurrentLevel(Skills.FLETCHING) + 1)
						+ " Fletching | "
						+ skills.getExpToNextLevel(Skills.FLETCHING)
						+ "XP Until level | "
						+ hoursTNL
						+ " Hours, "
						+ minsTNL
						+ " Mins Until level";
				g2.setColor(getColorText());
				g2.drawString(progress, 12, 19);

			} else if (game.isLoggedIn() && isClicking && fullPaint
					&& gui.checkBox10.isSelected()) {
				double percent = 512 * skills
						.getPercentToNextLevel(Skills.FLETCHING) / 100.0;
				GradientPaint base = new GradientPaint(4, 3, new Color(200,
						200, 200, 100), 4, 3 + 22 + 3,
						getColorProgressBarBelow());
				GradientPaint overlay = new GradientPaint(4, 3, new Color(200,
						200, 200, 100), 4, 3 + 22 + 3,
						getColorProgressBarOnTop());
				g2.setPaint(base);
				g2.fillRect(4, 3, 512, 22);
				g2.setPaint(overlay);
				g2.fillRect(4, 3, (int) percent, 22);
				g2.setColor(Color.black);
				g2.drawRect(4, 3, 512, 22);
				g2.setFont(new Font("Arial", 0, 13));
				String progress = skills
						.getPercentToNextLevel(Skills.FLETCHING)
						+ "% to "
						+ (skills.getCurrentLevel(Skills.FLETCHING) + 1)
						+ " Fletching | "
						+ skills.getExpToNextLevel(Skills.FLETCHING)
						+ "XP Until level | "
						+ hoursTNL
						+ " Hours, "
						+ minsTNL
						+ " Mins Until level";
				g2.setColor(getColorText());
				g2.drawString(progress, 12, 19);
			}

			// ==========> MOUSE! <==========
			Point m = mouse.getLocation();
			g2.setColor(getColorText());
			g2.fillRect(m.x - 5, m.y, 12, 2);
			g2.fillRect(m.x, m.y - 5, 2, 12);
			if (gui.checkBox11.isSelected() && !gui.checkBox17.isSelected()) {
				while (!mousePath.isEmpty() && mousePath.peek().isUp())
					mousePath.remove();
				Point clientCursor = mouse.getLocation();
				MousePathPoint mpp = new MousePathPoint(clientCursor.x,
						clientCursor.y, 3000);
				if (mousePath.isEmpty() || !mousePath.getLast().equals(mpp))
					mousePath.add(mpp);
				MousePathPoint lastPoint = null;
				for (MousePathPoint a : mousePath) {
					if (lastPoint != null) {
						g2.setColor(a.getColor());
						g2.drawLine(a.x, a.y, lastPoint.x, lastPoint.y);
					}
					lastPoint = a;
				}
			} else if (gui.checkBox11.isSelected()
					&& gui.checkBox17.isSelected()) {
				while (!mouseCirclePath.isEmpty()
						&& mouseCirclePath.peek().isUp())
					mouseCirclePath.remove();
				MouseCirclePathPoint mp = new MouseCirclePathPoint(m.x, m.y,
						3000);
				if (mouseCirclePath.isEmpty()
						|| !mouseCirclePath.getLast().equals(mp))
					mouseCirclePath.add(mp);
				MouseCirclePathPoint lastPoint = null;
				for (MouseCirclePathPoint a : mouseCirclePath) {
					if (lastPoint != null) {
						g2.setColor(new Color(getColorRSBotLine().getRed(),
								getColorRSBotLine().getGreen(),
								getColorRSBotLine().getBlue(),
								a.toColor(156 * ((a.finishTime - System
										.currentTimeMillis()) / a.lastingTime))));
						g2.fillOval(
								a.x
										- a.toColor(15 * ((a.finishTime - System
												.currentTimeMillis()) / (a.lastingTime)))
										/ 2,
								a.y
										- a.toColor(15 * ((a.finishTime - System
												.currentTimeMillis()) / (a.lastingTime)))
										/ 2,
								a.toColor(15 * ((a.finishTime - System
										.currentTimeMillis()) / (a.lastingTime))),
								a.toColor(15 * ((a.finishTime - System
										.currentTimeMillis()) / (a.lastingTime))));
						g2.setColor(new Color(0, 0, 0, a
								.toColor(156 * ((a.finishTime - System
										.currentTimeMillis()) / a.lastingTime))));
						g2.drawOval(
								a.x
										- a.toColor(15 * ((a.finishTime - System
												.currentTimeMillis()) / (a.lastingTime)))
										/ 2,
								a.y
										- a.toColor(15 * ((a.finishTime - System
												.currentTimeMillis()) / (a.lastingTime)))
										/ 2,
								a.toColor(15 * ((a.finishTime - System
										.currentTimeMillis()) / (a.lastingTime))),
								a.toColor(15 * ((a.finishTime - System
										.currentTimeMillis()) / (a.lastingTime))));
					}
					lastPoint = a;
				}
			}

			if (gui.checkBox13.isSelected() && !gui.checkBox18.isSelected()) {
				while (!mousePath2.isEmpty() && mousePath2.peek().isUp2())
					mousePath2.remove();
				MousePathPoint2 mpp = new MousePathPoint2(z.x, z.y, 3000);
				if (mousePath2.isEmpty() || !mousePath2.getLast().equals(mpp))
					mousePath2.add(mpp);
				MousePathPoint2 lastPoint = null;
				for (MousePathPoint2 z : mousePath2) {
					if (lastPoint != null) {
						g2.setColor(z.getColor2());
						g2.drawLine(z.x, z.y, lastPoint.x, lastPoint.y);
					}
					lastPoint = z;
				}
			} else if (gui.checkBox13.isSelected()
					&& gui.checkBox18.isSelected()) {
				while (!mouseCirclePath2.isEmpty()
						&& mouseCirclePath2.peek().isUp())
					mouseCirclePath2.remove();
				MouseCirclePathPoint2 mp = new MouseCirclePathPoint2(z.x, z.y,
						3000);
				if (mouseCirclePath2.isEmpty()
						|| !mouseCirclePath2.getLast().equals(mp))
					mouseCirclePath2.add(mp);
				MouseCirclePathPoint2 lastPoint = null;
				for (MouseCirclePathPoint2 a : mouseCirclePath2) {
					if (lastPoint != null) {
						g2.setColor(new Color(getColorUserLine().getRed(),
								getColorUserLine().getGreen(),
								getColorUserLine().getBlue(),
								a.toColor(156 * ((a.finishTime - System
										.currentTimeMillis()) / a.lastingTime))));
						g2.fillOval(
								a.x
										- a.toColor(15 * ((a.finishTime - System
												.currentTimeMillis()) / (a.lastingTime)))
										/ 2,
								a.y
										- a.toColor(15 * ((a.finishTime - System
												.currentTimeMillis()) / (a.lastingTime)))
										/ 2,
								a.toColor(15 * ((a.finishTime - System
										.currentTimeMillis()) / (a.lastingTime))),
								a.toColor(15 * ((a.finishTime - System
										.currentTimeMillis()) / (a.lastingTime))));
						g2.setColor(new Color(0, 0, 0, a
								.toColor(156 * ((a.finishTime - System
										.currentTimeMillis()) / a.lastingTime))));
						g2.drawOval(
								a.x
										- a.toColor(15 * ((a.finishTime - System
												.currentTimeMillis()) / (a.lastingTime)))
										/ 2,
								a.y
										- a.toColor(15 * ((a.finishTime - System
												.currentTimeMillis()) / (a.lastingTime)))
										/ 2,
								a.toColor(15 * ((a.finishTime - System
										.currentTimeMillis()) / (a.lastingTime))),
								a.toColor(15 * ((a.finishTime - System
										.currentTimeMillis()) / (a.lastingTime))));
					}
					lastPoint = a;
				}
			}
			if (gui.checkBox12.isSelected()) {
				int gW = game.getWidth();
				int gH = game.getHeight();
				Point localPoint = mouse.getLocation();
				g2.setColor(getColorRSBotCrosshair());
				g2.drawLine(0, localPoint.y, gW, localPoint.y);
				g2.drawLine(localPoint.x, 0, localPoint.x, gH);
			}
			if (gui.checkBox14.isSelected()) {
				int gW = game.getWidth();
				int gH = game.getHeight();
				g2.setColor(getColorUserCrosshair());
				g2.drawLine(0, z.y, gW, z.y);
				g2.drawLine(z.x, 0, z.x, gH);
			}
		}
	}

	private int getHourly(final int input) {
		double millis = System.currentTimeMillis() - startTime;
		return (int) ((input / millis) * 3600000);
	}

	private String getRuntime() {
		try {
			long millis = System.currentTimeMillis() - startTime;
			long days = millis / (1000 * 60 * 60 * 24);
			millis -= days * (1000 * 60 * 60);
			long hours = millis / (1000 * 60 * 60);
			millis -= hours * (1000 * 60 * 60);
			long minutes = millis / (1000 * 60);
			millis -= minutes * (1000 * 60);
			long seconds = millis / 1000;
			return ("" + (hours < 10 ? "0" : "") + hours + ":"
					+ (minutes < 10 ? "0" : "") + minutes + ":"
					+ (seconds < 10 ? "0" : "") + seconds + "");
		} catch (Exception e) {
			return "";
		}
	}

	public class trayInfo extends MenuItem {
		private static final long serialVersionUID = 1L;
		private PopupMenu menu = new PopupMenu();
		public TrayIcon systray;

		public trayInfo() {
			initComponents();
		}

		private void item1ActionPerformed(ActionEvent e) {
			stopScript(false);
		}

		private void item2ActionPerformed(ActionEvent e) {
			env.setUserInput(Environment.INPUT_KEYBOARD
					| Environment.INPUT_MOUSE);
			pause = true;
		}

		private void item3ActionPerformed(ActionEvent e) {
			pause = false;
			env.setUserInput(Environment.INPUT_KEYBOARD);
			log("Resuming..");
		}

		private void item4ActionPerformed(ActionEvent e) {
			gui.button1.setText("Update");
			gui.setVisible(true);
		}

		private void item5ActionPerformed(ActionEvent e) {
			gui.tabbedPane1.setSelectedIndex(4);
			gui.button1.setText("Update");
			gui.setVisible(true);
		}

		private void initComponents() {
			if (!SystemTray.isSupported()) {
				JOptionPane.showMessageDialog(null, "SystemTray not supported");
			} else {
				menu.add(constants.item1);
				constants.item1.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						item1ActionPerformed(e);
					}
				});
				menu.add(constants.item2);
				constants.item2.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						item2ActionPerformed(e);
					}
				});
				menu.add(constants.item3);
				constants.item3.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						item3ActionPerformed(e);
					}
				});
				menu.add(constants.item4);
				constants.item4.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						item4ActionPerformed(e);
					}
				});
				menu.add(constants.item5);
				constants.item5.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						item5ActionPerformed(e);
					}
				});
				try {
					systray = new TrayIcon(
							icon.getScaledInstance(SystemTray.getSystemTray()
									.getTrayIconSize().width, SystemTray
									.getSystemTray().getTrayIconSize().height,
									0), "UFletch", menu);
					SystemTray.getSystemTray().add(systray);
				} catch (Exception e) {
					log("Error setting up system tray!");
				}
			}
		}
	}

	public class gui extends JFrame {
		private static final long serialVersionUID = 1L;

		public gui() {
			initComponents();
		}

		private String getMessage() {

			URLConnection url = null;
			BufferedReader in = null;
			try {
				url = new URL(
						"http://www.universalscripts.org/UFletch/message.txt")
						.openConnection();
				in = new BufferedReader(new InputStreamReader(
						url.getInputStream()));
				return in.readLine();
			} catch (MalformedURLException e) {
			} catch (IOException e) {
			}
			return null;
		}

		private void button3ActionPerformed(ActionEvent e) {
			try {
				Desktop.getDesktop().browse(
						new URL("http://www.universalscripts.org").toURI());
			} catch (MalformedURLException e1) {
			} catch (IOException e1) {
			} catch (URISyntaxException e1) {
			}
		}

		private void label1MouseClicked(MouseEvent e) {
			try {
				Desktop.getDesktop().browse(
						new URL(
								"http://www.universalscripts.org/UFletch_generate.php?user="
										+ gui.textField2.getText()).toURI());
			} catch (MalformedURLException e1) {
			} catch (IOException e1) {
			} catch (URISyntaxException e1) {
			}
		}

		private void button4ActionPerformed(ActionEvent e) {
			createSignature();
			name = textField2.getText();
			label1.setText("<html><img src =http://universalscripts.org/UFletch_generate.php?user="
					+ name + "> </html>");
		}

		private void button2ActionPerformed(ActionEvent e) {
			try {
				Desktop.getDesktop().browse(
						new URL("http://universalscripts.org/highscores.php")
								.toURI());
			} catch (MalformedURLException e1) {
			} catch (IOException e1) {
			} catch (URISyntaxException e1) {
			}
		}

		private void button1ActionPerformed(ActionEvent e) {
			setVisible(false);
			log("Task: "
					+ constants.optionLog[gui.comboBox2.getSelectedIndex()]
					+ " "
					+ constants.optionBow[gui.comboBox3.getSelectedIndex()]
					+ " "
					+ constants.optionMethod[gui.comboBox1.getSelectedIndex()]);
			Mouse1 = (int) slider1.getValue();
			if (Mouse1 == 100) {
				Mouse2 = random(1, 2);
			} else if (Mouse1 == 90) {
				Mouse2 = random(2, 4);
			} else if (Mouse1 == 80) {
				Mouse2 = random(3, 5);
			} else if (Mouse1 == 70) {
				Mouse2 = random(4, 6);
			} else if (Mouse1 == 60) {
				Mouse2 = random(5, 7);
			} else if (Mouse1 == 50) {
				Mouse2 = random(6, 8);
			} else if (Mouse1 == 40) {
				Mouse2 = random(7, 9);
			} else if (Mouse1 == 30) {
				Mouse2 = random(8, 10);
			} else if (Mouse1 == 20) {
				Mouse2 = random(10, 12);
			} else if (Mouse1 == 10) {
				Mouse2 = random(12, 14);
			}
			mouse.setSpeed(Mouse2);
			button4.setEnabled(false);
			textField2.setEnabled(false);
			saveSettings();
		}

		private void initComponents() {
			// JFormDesigner - Component initialization - DO NOT MODIFY
			// //GEN-BEGIN:initComponents
			tabbedPane1 = new JTabbedPane();
			panel4 = new JPanel();
			label24 = new JLabel();
			label25 = new JLabel();
			label2 = new JLabel();
			comboBox2 = new JComboBox(constants.optionLog);
			label3 = new JLabel();
			comboBox3 = new JComboBox(constants.optionBow);
			label4 = new JLabel();
			label5 = new JLabel();
			comboBox4 = new JComboBox(constants.optionKnife);
			comboBox5 = new JComboBox(constants.optionAxe);
			label6 = new JLabel();
			label7 = new JLabel();
			comboBox1 = new JComboBox(constants.optionMethod);
			label8 = new JLabel();
			label9 = new JLabel();
			label10 = new JLabel();
			label11 = new JLabel();
			label14 = new JLabel();
			label12 = new JLabel();
			textField1 = new JTextField();
			label13 = new JLabel();
			panel2 = new JPanel();
			label31 = new JLabel();
			label32 = new JLabel();
			label33 = new JLabel();
			label34 = new JLabel();
			label35 = new JLabel();
			label36 = new JLabel();
			label37 = new JLabel();
			label38 = new JLabel();
			button3 = new JButton();
			label39 = new JLabel();
			label40 = new JLabel();
			checkBox4 = new JCheckBox();
			checkBox8 = new JCheckBox();
			checkBox9 = new JCheckBox();
			checkBox10 = new JCheckBox();
			checkBox11 = new JCheckBox();
			checkBox12 = new JCheckBox();
			checkBox13 = new JCheckBox();
			checkBox14 = new JCheckBox();
			label41 = new JLabel();
			comboBox8 = new JComboBox(constants.optionColor);
			label42 = new JLabel();
			comboBox9 = new JComboBox(constants.optionColor);
			label43 = new JLabel();
			label44 = new JLabel();
			label45 = new JLabel();
			label46 = new JLabel();
			comboBox10 = new JComboBox(constants.optionColor);
			comboBox11 = new JComboBox(constants.optionColor);
			comboBox12 = new JComboBox(constants.optionColor);
			comboBox13 = new JComboBox(constants.optionColor);
			comboBox14 = new JComboBox(constants.optionColor);
			comboBox15 = new JComboBox(constants.optionColor);
			label47 = new JLabel();
			label64 = new JLabel();
			checkBox17 = new JCheckBox();
			label65 = new JLabel();
			checkBox18 = new JCheckBox();
			panel1 = new JPanel();
			label17 = new JLabel();
			textField2 = new JTextField();
			label18 = new JLabel();
			label1 = new JLabel();
			button4 = new JButton();
			panel3 = new JPanel();
			slider1 = new JSlider();
			label15 = new JLabel();
			label16 = new JLabel();
			checkBox6 = new JCheckBox();
			label23 = new JLabel();
			checkBox5 = new JCheckBox();
			label27 = new JLabel();
			checkBox1 = new JCheckBox();
			checkBox2 = new JCheckBox();
			label28 = new JLabel();
			checkBox3 = new JCheckBox();
			label29 = new JLabel();
			label30 = new JLabel();
			label48 = new JLabel();
			checkBox15 = new JCheckBox();
			label49 = new JLabel();
			checkBox16 = new JCheckBox();
			label19 = new JLabel();
			checkBox7 = new JCheckBox();
			progressBar1 = new JProgressBar();
			label20 = new JLabel();
			button2 = new JButton();
			button1 = new JButton();

			// ======== this ========
			Container contentPane = getContentPane();
			contentPane.setLayout(null);

			// ======== tabbedPane1 ========
			{
				tabbedPane1.setTabPlacement(SwingConstants.LEFT);

				// ======== panel4 ========
				{
					panel4.setLayout(null);

					// ---- label24 ----
					label24.setText("Message:");
					label24.setFont(new Font("Tahoma", Font.PLAIN, 17));
					label24.setForeground(Color.red);
					panel4.add(label24);
					label24.setBounds(0, 265, 75, 25);

					// ---- label25 ----
					label25.setText(getMessage());
					label25.setFont(new Font("Tahoma", Font.PLAIN, 17));
					label25.setForeground(Color.blue);
					panel4.add(label25);
					label25.setBounds(80, 265, 340, 25);

					// ---- label2 ----
					label2.setText("<html> <img src = http://images.wikia.com/runescape/images/5/58/MagicLogs.png> </html>");
					panel4.add(label2);
					label2.setBounds(new Rectangle(new Point(5, 35), label2
							.getPreferredSize()));

					// ---- comboBox2 ----
					comboBox2.setForeground(Color.cyan);
					panel4.add(comboBox2);
					comboBox2.setBounds(50, 35, 160, 30);

					// ---- label3 ----
					label3.setText("<html> <img src=http://images.wikia.com/runescape/images/7/7f/Magic_longbow.png> </html>");
					panel4.add(label3);
					label3.setBounds(new Rectangle(new Point(5, 110), label3
							.getPreferredSize()));

					// ---- comboBox3 ----
					comboBox3.setForeground(Color.blue);
					panel4.add(comboBox3);
					comboBox3.setBounds(50, 110, 160, 30);

					// ---- label4 ----
					label4.setText("<html> <img src = http://images.wikia.com/runescape/images/c/cf/Knife_inventory.png> </html>");
					panel4.add(label4);
					label4.setBounds(220, 110, 25,
							label4.getPreferredSize().height);

					// ---- label5 ----
					label5.setText("<html> <img src= http://images.wikia.com/runescape/images/0/0e/Dragon_hatchet.png> </html>");
					panel4.add(label5);
					label5.setBounds(220, 35, label5.getPreferredSize().width,
							30);

					// ---- comboBox4 ----
					comboBox4.setForeground(Color.green);
					panel4.add(comboBox4);
					comboBox4.setBounds(255, 110, 170, 30);

					// ---- comboBox5 ----
					comboBox5.setForeground(Color.red);
					panel4.add(comboBox5);
					comboBox5.setBounds(255, 35, 170, 30);

					// ---- label6 ----
					label6.setText("<html> <img src = http://www.veryicon.com/icon/preview/Application/Apollo/Settings%20Icon.jpg> </html>");
					panel4.add(label6);
					label6.setBounds(new Rectangle(new Point(5, 180), label6
							.getPreferredSize()));

					// ---- label7 ----
					label7.setText("<html> <img src = http://www.veryicon.com/icon/preview/Application/Apollo/Settings%20Icon.jpg> </html>");
					panel4.add(label7);
					label7.setBounds(375, 180, 47, 50);

					// ---- comboBox1 ----
					comboBox1.setForeground(Color.magenta);
					panel4.add(comboBox1);
					comboBox1.setBounds(60, 180, 310, 50);

					// ---- label8 ----
					label8.setText("Method To Perform");
					label8.setFont(label8.getFont().deriveFont(Font.PLAIN,
							label8.getFont().getSize() + 17f));
					panel4.add(label8);
					label8.setBounds(95, 145, 240, 40);

					// ---- label9 ----
					label9.setText("Bow Type");
					label9.setFont(label9.getFont().deriveFont(
							label9.getFont().getSize() + 17f));
					panel4.add(label9);
					label9.setBounds(65, 75, 125, 35);

					// ---- label10 ----
					label10.setText("Log Type");
					label10.setFont(label10.getFont().deriveFont(
							label10.getFont().getSize() + 17f));
					panel4.add(label10);
					label10.setBounds(new Rectangle(new Point(70, 0), label10
							.getPreferredSize()));

					// ---- label11 ----
					label11.setText("Axe Type");
					label11.setFont(label11.getFont().deriveFont(
							label11.getFont().getSize() + 17f));
					panel4.add(label11);
					label11.setBounds(new Rectangle(new Point(280, 0), label11
							.getPreferredSize()));

					// ---- label14 ----
					label14.setText("Knife Type");
					label14.setFont(label14.getFont().deriveFont(
							label14.getFont().getSize() + 17f));
					panel4.add(label14);
					label14.setBounds(new Rectangle(new Point(275, 75), label14
							.getPreferredSize()));

					// ---- label12 ----
					label12.setText("Amount to Fletch:");
					label12.setFont(new Font("Tahoma", Font.PLAIN, 16));
					panel4.add(label12);
					label12.setBounds(0, 235, 135, 25);

					// ---- textField1 ----
					textField1.setText("0");
					panel4.add(textField1);
					textField1.setBounds(135, 235, 90, 25);

					// ---- label13 ----
					label13.setText("0 for Unilimted Fletching!");
					label13.setFont(new Font("Tahoma", Font.PLAIN, 16));
					panel4.add(label13);
					label13.setBounds(230, 235, 190, 25);
				}
				tabbedPane1.addTab("Main Settings", panel4);

				// ======== panel2 ========
				{
					panel2.setLayout(null);

					// ---- label31 ----
					label31.setText("Enable Paint:");
					label31.setFont(new Font("Tahoma", Font.PLAIN, 16));
					panel2.add(label31);
					label31.setBounds(5, 0, label31.getPreferredSize().width,
							25);

					// ---- label32 ----
					label32.setText("Over Chatbox:");
					label32.setFont(new Font("Tahoma", Font.PLAIN, 16));
					panel2.add(label32);
					label32.setBounds(new Rectangle(new Point(5, 65), label32
							.getPreferredSize()));

					// ---- label33 ----
					label33.setText("Over Inventory:");
					label33.setFont(new Font("Tahoma", Font.PLAIN, 16));
					panel2.add(label33);
					label33.setBounds(new Rectangle(new Point(5, 85), label33
							.getPreferredSize()));

					// ---- label34 ----
					label34.setText("Progress bar:");
					label34.setFont(new Font("Tahoma", Font.PLAIN, 16));
					panel2.add(label34);
					label34.setBounds(new Rectangle(new Point(5, 105), label34
							.getPreferredSize()));

					// ---- label35 ----
					label35.setText("RSBot Mouse Lines:");
					label35.setFont(new Font("Tahoma", Font.PLAIN, 16));
					panel2.add(label35);
					label35.setBounds(new Rectangle(new Point(5, 125), label35
							.getPreferredSize()));

					// ---- label36 ----
					label36.setText("RSBot Mouse Crosshair:");
					label36.setFont(new Font("Tahoma", Font.PLAIN, 16));
					panel2.add(label36);
					label36.setBounds(new Rectangle(new Point(5, 145), label36
							.getPreferredSize()));

					// ---- label37 ----
					label37.setText("Your Mouse Lines:");
					label37.setFont(new Font("Tahoma", Font.PLAIN, 16));
					panel2.add(label37);
					label37.setBounds(new Rectangle(new Point(5, 165), label37
							.getPreferredSize()));

					// ---- label38 ----
					label38.setText("Your Mouse Crosshair:");
					label38.setFont(new Font("Tahoma", Font.PLAIN, 16));
					panel2.add(label38);
					label38.setBounds(5, 185, 165,
							label38.getPreferredSize().height);

					// ---- button3 ----
					button3.setText("Visit Universalscripts.org! :D");
					button3.setFont(button3.getFont().deriveFont(
							button3.getFont().getSize() + 19f));
					button3.setForeground(Color.red);
					button3.setBackground(new Color(255, 0, 51));
					button3.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							button3ActionPerformed(e);
						}
					});
					panel2.add(button3);
					button3.setBounds(0, 210, 425, 80);

					// ---- label39 ----
					label39.setText("Text Color:");
					label39.setFont(new Font("Tahoma", Font.PLAIN, 16));
					panel2.add(label39);
					label39.setBounds(new Rectangle(new Point(5, 25), label39
							.getPreferredSize()));

					// ---- label40 ----
					label40.setText("Paint Main Color:");
					label40.setFont(new Font("Tahoma", Font.PLAIN, 16));
					panel2.add(label40);
					label40.setBounds(new Rectangle(new Point(5, 45), label40
							.getPreferredSize()));

					// ---- checkBox4 ----
					checkBox4.setFont(new Font("Tahoma", Font.PLAIN, 16));
					checkBox4.setSelected(true);
					panel2.add(checkBox4);
					checkBox4.setBounds(100, 0,
							checkBox4.getPreferredSize().width, 25);

					// ---- checkBox8 ----
					checkBox8.setFont(new Font("Tahoma", Font.PLAIN, 16));
					checkBox8.setSelected(true);
					panel2.add(checkBox8);
					checkBox8.setBounds(new Rectangle(new Point(110, 65),
							checkBox8.getPreferredSize()));

					// ---- checkBox9 ----
					checkBox9.setFont(new Font("Tahoma", Font.PLAIN, 16));
					panel2.add(checkBox9);
					checkBox9.setBounds(new Rectangle(new Point(120, 85),
							checkBox9.getPreferredSize()));

					// ---- checkBox10 ----
					checkBox10.setFont(new Font("Tahoma", Font.PLAIN, 16));
					checkBox10.setSelected(true);
					panel2.add(checkBox10);
					checkBox10.setBounds(new Rectangle(new Point(100, 105),
							checkBox10.getPreferredSize()));

					// ---- checkBox11 ----
					checkBox11.setFont(new Font("Tahoma", Font.PLAIN, 16));
					checkBox11.setSelected(true);
					panel2.add(checkBox11);
					checkBox11.setBounds(new Rectangle(new Point(145, 125),
							checkBox11.getPreferredSize()));

					// ---- checkBox12 ----
					checkBox12.setFont(new Font("Tahoma", Font.PLAIN, 16));
					checkBox12.setSelected(true);
					panel2.add(checkBox12);
					checkBox12.setBounds(new Rectangle(new Point(175, 145),
							checkBox12.getPreferredSize()));

					// ---- checkBox13 ----
					checkBox13.setFont(new Font("Tahoma", Font.PLAIN, 16));
					checkBox13.setSelected(true);
					panel2.add(checkBox13);
					checkBox13.setBounds(new Rectangle(new Point(135, 165),
							checkBox13.getPreferredSize()));

					// ---- checkBox14 ----
					checkBox14.setFont(new Font("Tahoma", Font.PLAIN, 16));
					checkBox14.setSelected(true);
					panel2.add(checkBox14);
					checkBox14.setBounds(new Rectangle(new Point(165, 185),
							checkBox14.getPreferredSize()));

					// ---- label41 ----
					label41.setText("Color Below:");
					label41.setFont(new Font("Tahoma", Font.PLAIN, 16));
					panel2.add(label41);
					label41.setBounds(new Rectangle(new Point(120, 105),
							label41.getPreferredSize()));
					panel2.add(comboBox8);
					comboBox8.setBounds(215, 105, 70,
							comboBox8.getPreferredSize().height);

					// ---- label42 ----
					label42.setText("On Top:");
					label42.setFont(new Font("Tahoma", Font.PLAIN, 16));
					panel2.add(label42);
					label42.setBounds(new Rectangle(new Point(290, 105),
							label42.getPreferredSize()));
					panel2.add(comboBox9);
					comboBox9.setBounds(355, 105, 70,
							comboBox9.getPreferredSize().height);

					// ---- label43 ----
					label43.setText("Color:");
					label43.setFont(new Font("Tahoma", Font.PLAIN, 16));
					panel2.add(label43);
					label43.setBounds(new Rectangle(new Point(240, 125),
							label43.getPreferredSize()));

					// ---- label44 ----
					label44.setText("Color:");
					label44.setFont(new Font("Tahoma", Font.PLAIN, 16));
					panel2.add(label44);
					label44.setBounds(new Rectangle(new Point(195, 145),
							label44.getPreferredSize()));

					// ---- label45 ----
					label45.setText("Color:");
					label45.setFont(new Font("Tahoma", Font.PLAIN, 16));
					panel2.add(label45);
					label45.setBounds(235, 165, 45,
							label45.getPreferredSize().height);

					// ---- label46 ----
					label46.setText("Color:");
					label46.setFont(new Font("Tahoma", Font.PLAIN, 16));
					panel2.add(label46);
					label46.setBounds(new Rectangle(new Point(185, 185),
							label46.getPreferredSize()));
					panel2.add(comboBox10);
					comboBox10.setBounds(285, 125, 70,
							comboBox10.getPreferredSize().height);
					panel2.add(comboBox11);
					comboBox11.setBounds(240, 145, 70,
							comboBox11.getPreferredSize().height);
					panel2.add(comboBox12);
					comboBox12.setBounds(90, 25, 70,
							comboBox12.getPreferredSize().height);
					panel2.add(comboBox13);
					comboBox13.setBounds(130, 45, 70,
							comboBox13.getPreferredSize().height);
					panel2.add(comboBox14);
					comboBox14.setBounds(280, 165, 70,
							comboBox14.getPreferredSize().height);
					panel2.add(comboBox15);
					comboBox15.setBounds(230, 185, 65,
							comboBox15.getPreferredSize().height);

					// ---- label47 ----
					label47.setText("<html> <img src= http://4.bp.blogspot.com/_xlKcL0Tlp-E/SHbVnh5BQHI/AAAAAAAAAQU/dUFrkZcvXWQ/s400/paintbrush.png> </html>");
					panel2.add(label47);
					label47.setBounds(265, -5, 125, 115);

					// ---- label64 ----
					label64.setText("Circles:");
					label64.setFont(new Font("Tahoma", Font.PLAIN, 16));
					panel2.add(label64);
					label64.setBounds(new Rectangle(new Point(165, 125),
							label64.getPreferredSize()));

					// ---- checkBox17 ----
					checkBox17.setFont(new Font("Tahoma", Font.PLAIN, 16));
					checkBox17.setSelected(true);
					panel2.add(checkBox17);
					checkBox17.setBounds(new Rectangle(new Point(220, 125),
							checkBox17.getPreferredSize()));

					// ---- label65 ----
					label65.setText("Cricles:");
					label65.setFont(new Font("Tahoma", Font.PLAIN, 16));
					panel2.add(label65);
					label65.setBounds(new Rectangle(new Point(155, 165),
							label65.getPreferredSize()));

					// ---- checkBox18 ----
					checkBox18.setFont(new Font("Tahoma", Font.PLAIN, 16));
					panel2.add(checkBox18);
					checkBox18.setBounds(new Rectangle(new Point(210, 165),
							checkBox18.getPreferredSize()));
				}
				tabbedPane1.addTab("Paint Settings", panel2);

				// ======== panel1 ========
				{
					panel1.setLayout(null);

					// ---- label17 ----
					label17.setText("Signature name:");
					label17.setFont(label17.getFont().deriveFont(
							label17.getFont().getStyle() & ~Font.BOLD));
					panel1.add(label17);
					label17.setBounds(0, 5, 80, 25);

					// ---- textField2 ----
					textField2.setText("All");
					panel1.add(textField2);
					textField2.setBounds(80, 5, 95, 25);

					// ---- label18 ----
					label18.setText("No Spaces Please!");
					panel1.add(label18);
					label18.setBounds(180, 5, 88, 25);

					// ---- label1 ----
					label1.setText("<html><img src =http://universalscripts.org/UFletch_generate.php?user=All> </html>");
					label1.addMouseListener(new MouseAdapter() {
						@Override
						public void mouseClicked(MouseEvent e) {
							label1MouseClicked(e);
						}
					});
					panel1.add(label1);
					label1.setBounds(0, 25, 425, 265);

					// ---- button4 ----
					button4.setText("Generate Signature");
					button4.setForeground(new Color(0, 204, 0));
					button4.setBackground(Color.green);
					button4.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							button4ActionPerformed(e);
						}
					});
					panel1.add(button4);
					button4.setBounds(275, 5, 145, 25);
				}
				tabbedPane1.addTab("Signature", panel1);

				// ======== panel3 ========
				{
					panel3.setLayout(null);

					// ---- slider1 ----
					slider1.setSnapToTicks(true);
					slider1.setPaintTicks(true);
					slider1.setMajorTickSpacing(10);
					panel3.add(slider1);
					slider1.setBounds(105, 130, 315, 30);

					// ---- label15 ----
					label15.setText("Mousespeed:");
					label15.setFont(new Font("Tahoma", Font.PLAIN, 16));
					panel3.add(label15);
					label15.setBounds(5, 130, 100, 30);

					// ---- label16 ----
					label16.setText("Save Settings for this account:");
					label16.setFont(new Font("Tahoma", Font.PLAIN, 16));
					panel3.add(label16);
					label16.setBounds(0, 195, 215, 25);

					// ---- checkBox6 ----
					checkBox6.setSelected(true);
					checkBox6.setFont(new Font("Tahoma", Font.PLAIN, 16));
					panel3.add(checkBox6);
					checkBox6.setBounds(215, 200, 21, 16);

					// ---- label23 ----
					label23.setText("logout 20k experince before 99 fletching:");
					label23.setFont(new Font("Tahoma", Font.PLAIN, 16));
					panel3.add(label23);
					label23.setBounds(5, 85, 300, 20);

					// ---- checkBox5 ----
					checkBox5.setSelected(true);
					panel3.add(checkBox5);
					checkBox5.setBounds(300, 85, 21, 21);

					// ---- label27 ----
					label27.setText("when done:");
					label27.setFont(new Font("Tahoma", Font.PLAIN, 16));
					panel3.add(label27);
					label27.setBounds(35, 165, 90, 25);

					// ---- checkBox1 ----
					checkBox1.setText(" Upon Level:");
					checkBox1.setSelected(true);
					checkBox1.setFont(new Font("Tahoma", Font.PLAIN, 16));
					panel3.add(checkBox1);
					checkBox1.setBounds(125, 165, 115, 25);

					// ---- checkBox2 ----
					checkBox2.setSelected(true);
					checkBox2.setFont(new Font("Tahoma", Font.PLAIN, 16));
					checkBox2.setText("Getting 99:");
					panel3.add(checkBox2);
					checkBox2.setBounds(240, 165, 110, 25);

					// ---- label28 ----
					label28.setText("<html> <img src =http://cemetery.canadagenweb.org/NB/NBC0002/camera.gif> </html>");
					panel3.add(label28);
					label28.setBounds(new Rectangle(new Point(0, 165), label28
							.getPreferredSize()));

					// ---- checkBox3 ----
					checkBox3.setFont(new Font("Tahoma", Font.PLAIN, 16));
					checkBox3.setSelected(true);
					panel3.add(checkBox3);
					checkBox3.setBounds(350, 165,
							checkBox3.getPreferredSize().width, 26);

					// ---- label29 ----
					label29.setText("Developed by: Fletch to 99");
					label29.setForeground(Color.blue);
					label29.setFont(label29.getFont().deriveFont(Font.BOLD,
							label29.getFont().getSize() + 20f));
					panel3.add(label29);
					label29.setBounds(5, 35, 415, 50);

					// ---- label30 ----
					label30.setText("UFletch: FREE, AIO, FLAWLESS!");
					label30.setForeground(Color.green);
					label30.setFont(label30.getFont().deriveFont(
							label30.getFont().getStyle() | Font.BOLD,
							label30.getFont().getSize() + 15f));
					panel3.add(label30);
					label30.setBounds(new Rectangle(new Point(5, 5), label30
							.getPreferredSize()));

					// ---- label48 ----
					label48.setText("Message Notification:");
					label48.setFont(new Font("Tahoma", Font.PLAIN, 16));
					panel3.add(label48);
					label48.setBounds(new Rectangle(new Point(5, 105), label48
							.getPreferredSize()));

					// ---- checkBox15 ----
					checkBox15.setFont(new Font("Tahoma", Font.PLAIN, 16));
					panel3.add(checkBox15);
					checkBox15.setBounds(new Rectangle(new Point(160, 105),
							checkBox15.getPreferredSize()));

					// ---- label49 ----
					label49.setText("Message Beep:");
					label49.setFont(new Font("Tahoma", Font.PLAIN, 16));
					panel3.add(label49);
					label49.setBounds(new Rectangle(new Point(185, 105),
							label49.getPreferredSize()));

					// ---- checkBox16 ----
					checkBox16.setFont(new Font("Tahoma", Font.PLAIN, 16));
					panel3.add(checkBox16);
					checkBox16.setBounds(new Rectangle(new Point(290, 105),
							checkBox16.getPreferredSize()));

					// ---- label19 ----
					label19.setText("Load Settings:");
					label19.setFont(new Font("Tahoma", Font.PLAIN, 16));
					panel3.add(label19);
					label19.setBounds(245, 195,
							label19.getPreferredSize().width, 25);

					// ---- checkBox7 ----
					checkBox7.setFont(new Font("Tahoma", Font.PLAIN, 16));
					checkBox7.setSelected(true);
					panel3.add(checkBox7);
					checkBox7.setBounds(350, 200,
							checkBox7.getPreferredSize().width, 15);

					// ---- progressBar1 ----
					progressBar1.setStringPainted(true);
					panel3.add(progressBar1);
					progressBar1.setBounds(0, 240, 210, 50);

					// ---- label20 ----
					label20.setText("Percentage to next level");
					label20.setFont(new Font("Tahoma", Font.PLAIN, 16));
					label20.setForeground(Color.red);
					panel3.add(label20);
					label20.setBounds(new Rectangle(new Point(20, 220), label20
							.getPreferredSize()));

					// ---- button2 ----
					button2.setText("Highscores");
					button2.setFont(new Font("Tahoma", Font.PLAIN, 16));
					button2.setForeground(Color.red);
					button2.setBackground(new Color(255, 0, 51));
					panel3.add(button2);
					button2.setBounds(210, 225, 215, 65);
					button2.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							button2ActionPerformed(e);
						}
					});

					{ // compute preferred size
						Dimension preferredSize = new Dimension();
						for (int i = 0; i < panel3.getComponentCount(); i++) {
							Rectangle bounds = panel3.getComponent(i)
									.getBounds();
							preferredSize.width = Math.max(bounds.x
									+ bounds.width, preferredSize.width);
							preferredSize.height = Math.max(bounds.y
									+ bounds.height, preferredSize.height);
						}
						Insets insets = panel3.getInsets();
						preferredSize.width += insets.right;
						preferredSize.height += insets.bottom;
						panel3.setMinimumSize(preferredSize);
						panel3.setPreferredSize(preferredSize);
					}
				}
				tabbedPane1.addTab("Other Settings", panel3);

			}
			contentPane.add(tabbedPane1);
			tabbedPane1.setBounds(5, 0, 515, 295);

			// ---- button1 ----
			button1.setText("Start UFletch!");
			button1.setForeground(Color.blue);
			button1.setFont(new Font("Tahoma", Font.PLAIN, 26));
			button1.setBackground(new Color(51, 51, 255));
			button1.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					button1ActionPerformed(e);
				}
			});
			contentPane.add(button1);
			button1.setBounds(0, 295, 520, 45);

			comboBox12.setSelectedIndex(0);
			comboBox13.setSelectedIndex(8);
			comboBox8.setSelectedIndex(1);
			comboBox9.setSelectedIndex(3);
			comboBox10.setSelectedIndex(7);
			comboBox11.setSelectedIndex(6);
			comboBox14.setSelectedIndex(5);
			comboBox15.setSelectedIndex(2);

			{ // compute preferred size
				Dimension preferredSize = new Dimension();
				for (int i = 0; i < contentPane.getComponentCount(); i++) {
					Rectangle bounds = contentPane.getComponent(i).getBounds();
					preferredSize.width = Math.max(bounds.x + bounds.width,
							preferredSize.width);
					preferredSize.height = Math.max(bounds.y + bounds.height,
							preferredSize.height);
				}
				Insets insets = contentPane.getInsets();
				preferredSize.width += insets.right;
				preferredSize.height += insets.bottom;
				contentPane.setMinimumSize(preferredSize);
				contentPane.setPreferredSize(preferredSize);
			}
			pack();
			setLocationRelativeTo(getOwner());
			// JFormDesigner - End of component initialization
			// //GEN-END:initComponents
		}

		// JFormDesigner - Variables declaration - DO NOT MODIFY
		// //GEN-BEGIN:variables
		private JTabbedPane tabbedPane1;
		private JPanel panel4;
		private JLabel label24;
		private JLabel label25;
		private JLabel label2;
		private JComboBox comboBox2;
		private JLabel label3;
		private JComboBox comboBox3;
		private JLabel label4;
		private JLabel label5;
		private JComboBox comboBox4;
		private JComboBox comboBox5;
		private JLabel label6;
		private JLabel label7;
		private JComboBox comboBox1;
		private JLabel label8;
		private JLabel label9;
		private JLabel label10;
		private JLabel label11;
		private JLabel label14;
		private JLabel label12;
		private JTextField textField1;
		private JLabel label13;
		private JPanel panel2;
		private JLabel label31;
		private JLabel label32;
		private JLabel label33;
		private JLabel label34;
		private JLabel label35;
		private JLabel label36;
		private JLabel label37;
		private JLabel label38;
		private JButton button3;
		private JLabel label39;
		private JLabel label40;
		private JCheckBox checkBox4;
		private JCheckBox checkBox8;
		private JCheckBox checkBox9;
		private JCheckBox checkBox10;
		private JCheckBox checkBox11;
		private JCheckBox checkBox12;
		private JCheckBox checkBox13;
		private JCheckBox checkBox14;
		private JLabel label41;
		private JComboBox comboBox8;
		private JLabel label42;
		private JComboBox comboBox9;
		private JLabel label43;
		private JLabel label44;
		private JLabel label45;
		private JLabel label46;
		private JComboBox comboBox10;
		private JComboBox comboBox11;
		private JComboBox comboBox12;
		private JComboBox comboBox13;
		private JComboBox comboBox14;
		private JComboBox comboBox15;
		private JLabel label47;
		private JLabel label64;
		private JCheckBox checkBox17;
		private JLabel label65;
		private JCheckBox checkBox18;
		private JPanel panel1;
		private JLabel label17;
		private JTextField textField2;
		private JLabel label18;
		private JLabel label1;
		private JButton button4;
		private JPanel panel3;
		private JSlider slider1;
		private JLabel label15;
		private JLabel label16;
		private JCheckBox checkBox6;
		private JLabel label23;
		private JCheckBox checkBox5;
		private JLabel label27;
		private JCheckBox checkBox1;
		private JCheckBox checkBox2;
		private JLabel label28;
		private JCheckBox checkBox3;
		private JLabel label29;
		private JLabel label30;
		private JLabel label48;
		private JCheckBox checkBox15;
		private JLabel label49;
		private JCheckBox checkBox16;
		private JLabel label19;
		private JCheckBox checkBox7;
		private JProgressBar progressBar1;
		private JLabel label20;
		private JButton button2;
		private JButton button1;
		// JFormDesigner - End of variables declaration //GEN-END:variables

	}

	public void mouseClicked(MouseEvent e) {
		p = e.getPoint();
		if (p.x >= 9 && p.x <= 36 && p.y >= 347 && p.y <= 372 || p.x >= 716
				&& p.x <= 733 && p.y >= 451 && p.y <= 466) {
			if (fullPaint) {
				fullPaint = false;
			} else if (!fullPaint) {
				fullPaint = true;
			}
		}

		if (p.x >= 360 && p.x <= 460 && p.y >= 440 && p.y <= 464 || p.x >= 603
				&& p.x <= 704 && p.y >= 282 && p.y <= 307) {
			gui.button1.setText("Update!");
			gui.progressBar1.setValue(skills
					.getPercentToNextLevel(Skills.FLETCHING));
			gui.setVisible(true);
		}
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}

	public void mousePressed(MouseEvent e) {
		p2 = e.getPoint();
		if (p2.x >= 4 && p2.x <= 514 && p2.y >= 345 && p2.y <= 473
				|| p2.x >= 548 && p2.x <= 736 && p2.y >= 205 && p2.y <= 464) {
			isClicking = true;
		}
	}

	public void mouseReleased(MouseEvent e) {
		p2 = e.getPoint();
		if (p2.x >= 4 && p2.x <= 514 && p2.y >= 345 && p2.y <= 473
				|| p2.x >= 548 && p2.x <= 736 && p2.y >= 205 && p2.y <= 464) {
			isClicking = false;
		}
	}

	public void mouseDragged(MouseEvent e) {
	}

	public void mouseMoved(MouseEvent e) {
		z = e.getPoint();
	}

	public class beeper implements Runnable {
		private String firstMessage = "";

		public void text() {
			if (!m().toLowerCase().isEmpty()
					&& !m().toLowerCase().equals(firstMessage)) {
				beep();
				firstMessage = m().toLowerCase();
			}
		}

		public void beep() {
			try {
				for (int i = 0; i < 3; i++) {
					java.awt.Toolkit.getDefaultToolkit().beep();
					Thread.sleep(250);
				}
				Thread.sleep(random(100, 500));
			} catch (Exception e) {
			}
			return;
		}

		public void run() {
			while (!b.isInterrupted()) {
				text();
				try {
					Thread.sleep(random(50, 150));
				} catch (InterruptedException e) {
				}
			}
		}

		public String m() {
			RSInterface chatBox = interfaces.get(137);
			for (int i = 281; i >= 180; i--) {// Valid text is from 180 to 281
				String text = chatBox.getComponent(i).getText();
				if (!text.isEmpty() && text.contains("<")) {
					return text;
				}
			}
			return "";
		}
	}

}