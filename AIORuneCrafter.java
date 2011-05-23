import java.awt.AWTException;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Robot;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.GeneralPath;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.LayoutStyle;

import org.rsbot.event.events.MessageEvent;
import org.rsbot.event.listeners.MessageListener;
import org.rsbot.event.listeners.PaintListener;
import org.rsbot.script.Script;
import org.rsbot.script.ScriptManifest;
import org.rsbot.script.methods.Methods;
import org.rsbot.script.methods.Skills;
import org.rsbot.script.util.Timer;
import org.rsbot.script.wrappers.RSArea;
import org.rsbot.script.wrappers.RSModel;
import org.rsbot.script.wrappers.RSNPC;
import org.rsbot.script.wrappers.RSObject;
import org.rsbot.script.wrappers.RSTile;

@ScriptManifest(authors = "Ubuntu4life", name = "AIORuneCrafter", version = 0.4, description = "Crafts all F2P runes.", website = "http://www.powerbot.org/vb/showthread.php?t=764525&p", requiresVersion = 244)
public class AIORuneCrafter extends Script implements PaintListener,
MessageListener, MouseListener {

	private enum Action {
		OPENBANK, DEPOSIT, WITHDRAW, WALKTORUINS, ENTERRUINS, WALKTOALTAR, CRAFTRUNE, WALKTOPORTAL, LEAVEALTAR, WALKTOBANK, SLEEP, UNKNOWN
	}

	public Action getAction() {
		try {
			if (game.getClientState() != 10)
				return Action.SLEEP;
			else {
				if (inventory.getCount(runeEssence) != 28) {
					RSObject Booth = objects.getNearest(bankBoothID);
					RSObject Chest = objects.getNearest(chestID);
					if (Rune == 554 ? Chest != null // Fire chest
							&& isOnScreen(Chest.getModel()) : Booth != null
							&& isOnScreen(Booth.getModel())) {
						if (!bank.isOpen())
							return Action.OPENBANK;
						if (inventory.getCountExcept(runeEssence) != 0)
							return Action.DEPOSIT;
						return Action.WITHDRAW;
					} else {
						if (inAltarArea()) {
							RSObject Portal = objects.getNearest(portalID);
							if (Portal != null && isOnScreen(Portal.getModel()))
								return Action.LEAVEALTAR;
							return Action.WALKTOPORTAL;
						} else
							return Action.WALKTOBANK;
					}
				} else {
					if (inAltarArea()) {
						RSObject Altar = objects.getNearest(altarID);
						if (Altar != null && isOnScreen(Altar.getModel()))
							return Action.CRAFTRUNE;
						return Action.WALKTOALTAR;
					} else {
						RSObject Ruins = objects.getNearest(ruinsID);
						if (Ruins != null && isOnScreen(Ruins.getModel()))
							return Action.ENTERRUINS;
						return Action.WALKTORUINS;
					}
				}
			}
		} catch (Exception e) {
			return Action.UNKNOWN;
		}
	}

	public String Status = "Starting", runeString = "Air runes";
	private boolean Pressed = true, Crafted = false, stoppingScript = false;
	private final Rectangle hideRect = new Rectangle(477, 336, 34, 37),
	tabOneRect = new Rectangle(177, 335, 147, 37),
	tabTwoRect = new Rectangle(327, 336, 148, 37);
	private final Color lineColor = new Color(90, 15, 15),
	fillColor = new Color(0, 0, 0, 50), Succes = new Color(0, 0, 255),
	Fail = new Color(255, 0, 0), Mouse = new Color(20, 26, 167, 85);
	private long startTime, startExp, startLevel, expGained;
	private int[] bankerID = { 553, 2759 };
	private int bankBoothID = 782, chestID = 27663, runeEssence = 1436,
	Rune = 556, Tiara = 5527, ruinsID = 2452, altarID = 2478,
	portalID = 2465, musicianID = 8699, stairsID = 24353,
	runesCrafted = 0, essenceUsed = 0, essenceInBank = 0, tab = 1,
	uniqueNumber, screenLevel, essencePrice, runePrice,
	nextPercentageRun, nextPercentageRest;
	private java.util.Random Unique, f = new java.util.Random();
	private PerfectGUI GUI;
	private Timer hourTimer;
	RSArea ALTAR_AREA = new RSArea(new RSTile(2835, 4823), new RSTile(2851,
			4843));
	RSTile altarWalkTile = new RSTile(2843, 4832), portalWalkTile = new RSTile(
			2841, 4829), ruinsTile = new RSTile(3187, 3164),
			bankTile = new RSTile(3269, 3169), errorTile1 = new RSTile(3250,
					3419), errorTile2 = new RSTile(3257, 3419);// Two tiles
					// where banking
	// is not
	// possible in
	// Varrock east
	// bank.
	Thread screenShotter = new Thread(new screenShotter());
	Thread freeMemory = new Thread(new freeMemory());
	Thread antiBan = new Thread(new antiBan());
	Image img1, img2, tabOne, tabTwo, closed;

	private ArrayList<RSTile> pathList;
	private Line[] TOBANK = { // Fire is special
			new Line(3323, 3247, 3323, 3248), new Line(3325, 3247, 3325, 3264),
			new Line(3328, 3264, 3328, 3269),
			new Line(3335, 3265, 3335, 3270),
			new Line(3348, 3265, 3348, 3271),
			new Line(3353, 3262, 3353, 3267),
			new Line(3367, 3262, 3367, 3269),
			new Line(3372, 3264, 3372, 3267),
			new Line(3380, 3265, 3380, 3266),
			new Line(3382, 3267, 3382, 3272),
			new Line(3383, 3267, 3383, 3272) },
			TORUINS = { // Air
			new Line(3182, 3435, 3182, 3440), new Line(3173, 3425, 3173, 3433),
			new Line(3164, 3418, 3163, 3425),
			new Line(3153, 3415, 3153, 3425),
			new Line(3145, 3413, 3145, 3418),
			new Line(3136, 3403, 3136, 3417),
			new Line(3127, 3403, 3127, 3407) };

	@Override
	public boolean onStart() {
		if (!game.isFixed()) {
			log
			.severe("It is highly recommended to bot with fixed screen mode.");
			return false;
		}
		while (!game.isLoggedIn()) {
			game.login();
		}
		GUI = new PerfectGUI();
		GUI.setAlwaysOnTop(true);
		while (!GUI.startScript) {
			if (GUI.close)
				return false;
			else {
				try {
					camera.setPitch(true);
					Unique = generateSeededRandom(getMyPlayer().getName());
					uniqueNumber = Unique.nextInt(16);
					Methods.sleep(100);
				} catch (Exception e) {
				}
			}
		}
		log.info("Loading AIORuneCrafter "
				+ getClass().getAnnotation(ScriptManifest.class).version());
		if (inventory.contains(Tiara))
			inventory.getItem(Tiara).doAction("Wear");
		if (equipment.containsOneOf(Tiara))
			log(Succes, "Water tiara detected");
		else {
			log(Fail, "Cannot detect your water tiara. Script stopping");
			return false;
		}
		startExp = (long) skills.getCurrentExp(Skills.RUNECRAFTING);
		startLevel = (long) skills.getRealLevel(Skills.RUNECRAFTING);
		startTime = (long) System.currentTimeMillis();
		new Thread(new Runnable() {
			public void run() {
				img1 = getImage("http://www.rw-designer.com/cursor-view/13449.png");
				tabOne = getImage("http://img163.imageshack.us/img163/9814/aiorunegen.png");
				tabTwo = getImage("http://img21.imageshack.us/img21/4733/aioruneexp.png");
				closed = getImage("http://img39.imageshack.us/img39/3215/aioruneclosed.png");
				img2 = getImage("http://www.ubuntu.com/sites/default/themes/ubuntu10/images/footer_logo.png");
				runePrice = grandExchange.lookup(Rune).getGuidePrice();
				essencePrice = grandExchange.lookup(runeEssence)
				.getGuidePrice();
			}
		}).start();
		if (inventory.containsOneOf(runeEssence))
			pathList = generatePath(TORUINS);
		else
			pathList = generatePath(reverse(TORUINS));
		nextPercentageRun = randomGauss(3, 43, (uniqueNumber + 14), 24);
		nextPercentageRest = randomGauss(0, 28, (uniqueNumber + 5), 14);
		freeMemory.start();
		return true;
	}

	@Override
	public void onFinish() {
		if (GUI.screenOnFinish) {
			env.saveScreenshot(true);
		}
		stoppingScript = true;
		if (GUI.screenOnHour || GUI.screenOnLevel) {
			while (screenShotter.isAlive()) {
				if (screenShotter.isAlive()) {
					screenShotter.interrupt();
					if (screenShotter.isInterrupted()) {
						break;
					}
				}
			}
		}
		while (antiBan.isAlive()) {
			if (antiBan.isAlive()) {
				antiBan.interrupt();
				if (antiBan.isInterrupted()) {
					break;
				}
			}
		}
		while (freeMemory.isAlive()) {
			if (freeMemory.isAlive()) {
				freeMemory.interrupt();
				if (freeMemory.isInterrupted()) {
					break;
				}
			}
		}
		long runTime = System.currentTimeMillis() - startTime;
		long gainedXP = skills.getCurrentExp(Skills.RUNECRAFTING) - startExp;
		long gainedLevels = skills.getRealLevel(Skills.RUNECRAFTING)
		- startLevel;
		int profit = (runePrice * runesCrafted) - (essencePrice * essenceUsed);
		log("Total XP gained: " + getFormattedMoney(gainedXP) + ".");
		log("Total levels gained: " + gainedLevels + ".");
		log("Total runes crafted: " + getFormattedMoney(runesCrafted) + ".");
		log("Total essence used: " + getFormattedMoney(essenceUsed) + ".");
		log("Total profit made: " + getFormattedMoney(profit) + ".");
		log("Total run time: " + getFormattedTime(runTime) + ".");
		stopScript();
	}

	public interface Condition {
		public boolean isTrue();
	}

	/**
	 * Sleeps the script under tight restrictions. Breaks directly when
	 * condition is no longer true and keeps lag in count.
	 * 
	 * @Author Ubuntu4life
	 * 
	 * @param Condition
	 *            The condition(s) under which the script has to sleep
	 * @param targetObject
	 *            The RSObject we want to click or go through
	 * @param tileBuffer
	 *            The minimum amount of tiles from the targetObject center tile
	 *            that you will have to walk (1 tile between player and object)
	 * @param Threshold
	 *            The reaction time when things are not going as planned
	 * @return true when everything went fine. False when something went wrong
	 */
	private boolean sleepWhile(final Condition condition,
			RSObject targetObject, int tileBuffer, int Threshold) {
		final int Irritations = Threshold / 25;
		final int minimalSleepTillMove = 1500;
		final int MaximalSleepTillMove = 2000;
		if (!players.getMyPlayer().isMoving()
				&& Threshold < ((minimalSleepTillMove + MaximalSleepTillMove) / 2)) {
			if (targetObject != null) {
				if (calc.distanceTo(targetObject) >= tileBuffer) {
					long endTime = System.currentTimeMillis()
					+ random(minimalSleepTillMove, MaximalSleepTillMove);
					while (System.currentTimeMillis() < endTime) {
						if (getMyPlayer().isMoving()) {
							break;
						}
						Methods.sleep(Methods.random(20, 30));
					}
					if (!players.getMyPlayer().isMoving()) {
						return false;
					}
				}
			} else {
				return false;
			}
		}
		for (int i = 0; i < Irritations && condition.isTrue(); i++) {
			if (players.getMyPlayer().isMoving() || game.getClientState() != 10) {
				i = 0;
			}
			Methods.sleep(Methods.random(20, 30));
		}
		if (condition.isTrue()) {
			return false;
		}
		return true;
	}

	/**
	 * 
	 * @param Condition
	 * @param targetNPC
	 * @param Threshold
	 * @return
	 */
	private boolean sleepWhile(final Condition condition, RSNPC targetNPC,
			int Threshold) {
		int Irritations = Threshold / 25;
		if (!players.getMyPlayer().isMoving()
				&& calc.distanceTo(targetNPC) >= 1) {
			long endTime = System.currentTimeMillis() + random(1500, 2000);
			while (System.currentTimeMillis() < endTime) {
				if (getMyPlayer().isMoving()) {
					break;
				}
				Methods.sleep(Methods.random(20, 30));
			}
			if (!players.getMyPlayer().isMoving()) {
				return false;
			}
		}
		for (int i = 0; i < Irritations && condition.isTrue(); i++) {
			if (players.getMyPlayer().isMoving() || game.getClientState() != 10) {
				i = 0;
			}
			Methods.sleep(Methods.random(20, 30));
		}
		if (condition.isTrue()) {
			return false;
		}
		return true;
	}

	/**
	 * 
	 * @param Condition
	 *            The condition(s) under which the script has to sleep
	 * @param Threshold
	 *            The reaction time when things are not going as planned
	 * @return true when everything went fine.
	 */
	private boolean sleepWhile(final Condition condition, int Threshold) {
		int Irritations = Threshold / 25;
		for (int i = 0; i < Irritations && condition.isTrue(); i++) {
			Methods.sleep(Methods.random(20, 30));
		}
		if (condition.isTrue()) {
			return false;
		}
		return true;
	}

	@Override
	public int loop() {
		try {
			if (!antiBan.isAlive() && GUI.antiban) {
				log.info("Antiban Engaging");
				antiBan.start();
			}
			mouse.setSpeed(Methods.random(GUI.minMouse, GUI.maxMouse));
			if (GUI.antiban) {
				antiBan();
			}
			if (Crafted) {
				runesCrafted = runesCrafted + inventory.getCount(true, Rune);
				essenceUsed = essenceUsed + 28;
				Crafted = false;
			}

			if (GUI.restMode) {
				if (Rune == 556 || Rune == 557 || Rune == 558) { // Resting at a
					// musician
					RSNPC Musician = npcs.getNearest(musicianID);
					if (walking.getEnergy() < Methods.random(29, 38)
							&& Musician != null
							&& calc.distanceTo(Musician) < Methods.random(19,
									21) && walkingTowardsMusician(Musician)) {
						if (isOnScreen(Musician.getModel())) {
							if (clickNPC(Musician, "Listen-to")) {
								if (sleepWhile(new Condition() {
									public boolean isTrue() {
										return players.getMyPlayer()
										.getAnimation() == -1;
									}
								}, Musician, 1000)) {
									Status = "Resting";
									int energy = walking.getEnergy();
									int maxsleep = 0;
									int targetEnergy = randomGauss(69, 99,
											(uniqueNumber + 77), 12);
									while (energy <= targetEnergy
											&& maxsleep++ <= 1401) {
										Methods.sleep(Methods.random(20, 30));
										energy = walking.getEnergy();
									}
								}
							}
						}
						walking.walkTo(Musician.getLocation().randomize(3, 3));
						return 50;
					}
				} else { // Normal resting
					if (walking.getEnergy() <= nextPercentageRest) {
						Status = "Resting";
						walking.rest(random(69, 99));
						nextPercentageRest = randomGauss(0, 28,
								(uniqueNumber + 5), 14);
					}
				}
			}
			if (!walking.isRunEnabled()
					&& walking.getEnergy() >= nextPercentageRun
					&& !bank.isOpen()) {
				walking.setRun(true);
				nextPercentageRun = randomGauss(3, 43, (uniqueNumber + 14), 24);
			}
			if (Rune == 557) { // Earth runecrafting failsafes
				if (game.getPlane() == 1) {
					RSObject Stairs = objects.getNearest(stairsID);
					for (int i = 0; i < 150; i++) {
						if (Stairs != null) {
							if (isOnScreen(Stairs.getModel())) {
								Status = "Getting of stairs";
								if (Stairs.doAction("Climb")) {
									sleepWhile(new Condition() {
										public boolean isTrue() {
											return game.getPlane() == 1;
										}
									}, Stairs, 1, 1000);
								}
							} else {
								walking.walkTo(Stairs.getLocation());
							}
						}
						if (i == 149 && Stairs == null) {
							log("Stuck somewhere.");
							stoppingScript = true;
							stopScript(true);
						}
						Methods.sleep(Methods.random(20, 30));
					}
				}
				if (players.getMyPlayer().getLocation().equals(errorTile1)
						&& getAction() == Action.OPENBANK
						|| players.getMyPlayer().getLocation().equals(
								errorTile2) && getAction() == Action.OPENBANK) {
					RSNPC Banker = npcs.getNearest(bankerID);
					if (clickNPC(Banker, "Bank Banker")) {
						Status = "Opening bank";
						sleepWhile(new Condition() {
							public boolean isTrue() {
								return !bank.isOpen();
							}
						}, 1000);
					}
				}
			}
			switch (getAction()) {
			case OPENBANK:
				Status = "Opening bank";
				if (Rune == 554) { // Fire runecrafting banks in a chest
					RSObject Chest = objects.getNearest(chestID);
					if (clickObject(Chest, "Bank Bank Chest")) {
						sleepWhile(new Condition() {
							public boolean isTrue() {
								return !bank.isOpen();
							}
						}, Chest, 1, 1000);
					}
				} else { // Other runes use the normal bankBooth
					RSObject Booth = objects.getNearest(bankBoothID);
					if (clickObject(Booth, "Use-quickly")) {
						sleepWhile(new Condition() {
							public boolean isTrue() {
								return !bank.isOpen();
							}
						}, Booth, 1, 1000);
					}
				}
				break;
			case DEPOSIT:
				Status = "Depositing";
				mouse.setSpeed(Methods.random(5, 8));
				bank.depositAllExcept(runeEssence);
				sleepWhile(new Condition() {
					public boolean isTrue() {
						return inventory.getCountExcept(runeEssence) != 0;
					}
				}, 1875);
				break;
			case WITHDRAW:
				Status = "Withdrawing";
				mouse.setSpeed(Methods.random(5, 8));
				for (int i = 0; i < 150; i++) {
					if (bank.getCount(runeEssence) >= 1) {
						break;
					}
					if (i == 149 && bank.getCount(runeEssence) == 0) {
						log("Ran out of rune essence.");
						stoppingScript = true;
						stopScript(true);
					}
					Methods.sleep(Methods.random(20, 30));
				}
				final int invCount = inventory.getCount();
				bank.withdraw(runeEssence, 0);
				if (sleepWhile(new Condition() {
					public boolean isTrue() {
						return invCount == inventory.getCount();
					}
				}, 1875)) {
					for (int i = 0; i < 75; i++) {
						if (inventory.getCount(true, runeEssence) >= 1) {
							essenceInBank = bank.getCount(runeEssence);
							pathList = generatePath(TORUINS);
							break;
						}
						Methods.sleep(Methods.random(20, 30));
					}
				}
				break;
			case WALKTORUINS:
				Status = "Walking to ruins";
				step(pathList);
				break;
			case ENTERRUINS:
				Status = "Entering ruins";
				RSObject Ruins = objects.getNearest(ruinsID);
				if (clickObject(Ruins, "Enter")) {
					sleepWhile(new Condition() {
						public boolean isTrue() {
							return !inAltarArea();
						}
					}, Ruins, 3, 1000);
				}
				break;
			case WALKTOALTAR:
				Status = "Walking to altar";
				if (walking.walkTo(altarWalkTile.randomize(3, 3))) {
					RSObject Altar = objects.getNearest(altarID);
					sleepWhile(new Condition() {
						RSObject Altar = objects.getNearest(altarID);

						public boolean isTrue() {
							return !isOnScreen(Altar.getModel())
							&& Altar != null;
						}
					}, Altar, 3, 1000);
				}
				break;
			case CRAFTRUNE:
				Status = "Crafting essence";
				RSObject Altar = objects.getNearest(altarID);
				if (clickObject(Altar, "Craft-rune")) {
					if (Rune == 554)
						pathList = generatePath(TOBANK);
					else
						pathList = generatePath(reverse(TORUINS));
					final int count = inventory.getCount(Rune);
					if (sleepWhile(new Condition() {
						public boolean isTrue() {
							return count == inventory.getCount(Rune);
						}
					}, Altar, 3, 1000)) {
						Methods.sleep(Methods.random(763, 2163));
					}
				}
				break;
			case WALKTOPORTAL:
				Status = "Walking to portal";
				if (walking.walkTo(portalWalkTile.randomize(3, 3))) {
					RSObject Portal = objects.getNearest(portalID);
					sleepWhile(new Condition() {
						RSObject Portal = objects.getNearest(portalID);

						public boolean isTrue() {
							return !isOnScreen(Portal.getModel())
							&& Portal != null;
						}
					}, Portal, 1, 1000);
				}
				break;
			case LEAVEALTAR:
				Status = "Leaving altar";
				RSObject Portal = objects.getNearest(portalID);
				if (clickObject(Portal, "Enter")) {
					sleepWhile(new Condition() {
						public boolean isTrue() {
							return inAltarArea();
						}
					}, Portal, 1, 1000);
				}
				break;
			case WALKTOBANK:
				Status = "Walking to bank";
				step(pathList);
				break;
			case SLEEP:
				Methods.sleep(Methods.random(180, 420));
				break;
			}
		} catch (final Exception e) {
			return 1;
		}
		return (randomGauss(Methods.random(189, 191), Methods.random(428, 431),
				251, 40));
	}

	private void antiBan() {
		int random = Methods.random(1, 9);
		while (game.getClientState() == 10) {
			switch (random) {
			case 1:
				if (Methods.random(1, 17) != 1) {
					return;
				}
				mouse.move(random(10, 750), random(10, 495));
				if (Methods.random(1, 7) == 1) {
					mouse.move(random(10, 750), random(10, 495));
				}
				return;
			case 2:
				if (Methods.random(1, 12) != 1) {
					return;
				}
				mouse.moveSlightly();
				return;
			case 3:
				if (Methods.random(1, 18) != 1) {
					return;
				}
				if (Methods.random(1, 11) == 1) {
					camera.setPitch(true);
				}
				int angle = camera.getAngle() + Methods.random(-45, 45);
				if (angle < 0) {
					angle = Methods.random(0, 10);
				}
				if (angle > 359) {
					angle = Methods.random(0, 10);
				}
				char whichDir = 37;
				if (Methods.random(0, 100) < 50) {
					whichDir = 39;
				}
				keyboard.pressKey(whichDir);
				Methods.sleep(Methods.random(100, 700));
				keyboard.releaseKey(whichDir);
				return;
			default:
				return;
			}
		}
	}

	private class antiBan implements Runnable {

		private Random randomGenerator;

		@Override
		public void run() {
			randomGenerator = new Random();
			try {
				while (game.isLoggedIn() && !stoppingScript && !isPaused()) {
					int rand = randomGenerator.nextInt(23);
					if (rand == 1) {
						final char[] LR = new char[] { KeyEvent.VK_LEFT,
								KeyEvent.VK_RIGHT };
						final char[] UD = new char[] { KeyEvent.VK_DOWN,
								KeyEvent.VK_UP };
						final char[] LRUD = new char[] { KeyEvent.VK_LEFT,
								KeyEvent.VK_RIGHT, KeyEvent.VK_UP,
								KeyEvent.VK_UP };
						final int random1 = random(0, 2);
						final int random2 = random(0, 2);
						final int random3 = random(0, 4);
						if (random(0, 3) == 1) {
							keyboard.pressKey(LR[random1]);
							Thread.sleep(random(104, 403));
							keyboard.pressKey(UD[random2]);
							Thread.sleep(random(308, 601));
							keyboard.releaseKey(UD[random2]);
							Thread.sleep(random(106, 405));
							keyboard.releaseKey(LR[random1]);
						} else {
							keyboard.pressKey(LRUD[random3]);
							if (random3 > 1) {
								Thread.sleep(random(301, 604));
							} else {
								Thread.sleep(random(502, 906));
								keyboard.releaseKey(LRUD[random3]);
							}
						}
					} else {
						Thread.sleep(random(189, 2163));
					}
				}
			} catch (final Exception e) {
			}
		}
	}

	class freeMemory implements Runnable {

		@Override
		public void run() {
			while (game.isLoggedIn() && !stoppingScript) {
				try {
					if (Methods.random(0, 450) == 1) {
						Runtime r = Runtime.getRuntime();
						r.gc();
					}
					Thread.sleep(Methods.random(1000, 1500));
				} catch (InterruptedException e) {
				}
			}
		}
	}

	private class screenShotter implements Runnable {

		@Override
		public void run() {
			while (game.isLoggedIn() && !game.isLoginScreen()
					&& !stoppingScript) {
				try {
					if (GUI.screenOnLevel) {
						if (skills.getRealLevel(Skills.RUNECRAFTING)
								- screenLevel > 0) {
							screenLevel = skills
							.getRealLevel(Skills.RUNECRAFTING);
							env.saveScreenshot(true);
						}
					}
					if (GUI.screenOnHour) {
						if (!hourTimer.isRunning()) {
							hourTimer.reset();
							env.saveScreenshot(true);
						}
					}
					Thread.sleep(Methods.random(1000, 3000));
				} catch (InterruptedException e) {
				}
			}
		}
	}

	@Override
	public void messageReceived(final MessageEvent e) {
		final String msg = e.getMessage();
		if (msg.contains("runes")) {
			Crafted = true;
		}
		if (msg.contains("advanced a Runecrafting level")) {
			log("Congratulations on level up !");
			log("Your level is now "
					+ skills.getCurrentLevel(Skills.RUNECRAFTING) + ".");
		}
		if (msg.contains("the next Runescape")) {
			log("There will be a system update soon, logged out.");
			stoppingScript = true;
			stopScript(true);
		}
		if (msg.contains("oh dear")) {
			stoppingScript = true;
			stopScript(true);
			log("Dead :/");
		}
	}

	private int randomGauss(int lo, int hi, double d, double sd) {
		int rand;
		do {
			rand = (int) (f.nextGaussian() * sd + d);
		} while (rand < lo || rand >= hi);
		return rand;
	}

	private boolean mouseOnObjectModel(Point p, RSObject object) {
		if (object == null) {
			return false;
		}
		Polygon[] triangles = object.getModel().getTriangles();
		for (Polygon poly : triangles) {
			if (poly.contains(p)) {
				return true;
			}
		}
		return false;
	}

	private boolean clickObject(RSObject object, String action) {
		try {
			Point p;
			for (int i = 0; i < 15; i++) {
				for (p = players.getMyPlayer().isMoving() ? object.getModel()
						.getCentralPoint() : object.getModel().getPoint(); !menu
						.contains(action); p = players.getMyPlayer().isMoving() ? object
								.getModel().getCentralPoint()
								: object.getModel().getPoint()) {
					if (!isOnScreen(object.getModel()) || bank.isOpen()) {
						return false;
					}
					moveMouse(Methods.random(20, 30), p);
				}
				String[] items = menu.getItems();
				if (items.length > 0
						&& items[0].toLowerCase().startsWith(
								action.toLowerCase())) {
					if (mouseOnObjectModel(mouse.getLocation(), object)) {
						mouse.click(true);
						if (didMisClick()) {
							log("Miss clicked !");
							Methods.sleep(Methods.random(180, 400));
							i = 0;
						} else {
							return true;
						}
					}
				} else if (menu.contains(action)) {
					mouse.setSpeed(Methods.random(5, 8));
					mouse.click(false);
					for (int x = 0; x < 4; x++) {
						if (menu.doAction(action)) {
							return true;
						}
					}
				}
				if (!menu.contains(action) && menu.isOpen()) {
					do {
						mouse.moveRandomly(Methods.random(180, 400));
					} while (menu.isOpen());
					i = 0;
				}
			}
		} catch (Exception e) {
		}
		return false;
	}

	private boolean clickNPC(RSNPC npc, String action) {
		try {
			Point p;
			for (int i = 0; i < 15; i++) {
				for (p = players.getMyPlayer().isMoving() ? npc.getModel()
						.getCentralPoint() : npc.getModel().getPoint(); !menu
						.contains(action); p = players.getMyPlayer().isMoving() ? npc
								.getModel().getCentralPoint()
								: npc.getModel().getPoint()) {
					if (!isOnScreen(npc.getModel()) || bank.isOpen()) {
						return false;
					}
					moveMouse(Methods.random(20, 30), p);
				}
				if (menu.contains(action)) {
					mouse.setSpeed(Methods.random(5, 8));
					mouse.click(false);
					for (int x = 0; x < 4; x++) {
						if (menu.doAction(action)) {
							return true;
						}
					}
				}
				if (menu.isOpen() && !menu.contains(action)) {
					do {
						mouse.moveRandomly(Methods.random(180, 422));
					} while (menu.isOpen());
					i = 0;
				}
			}
		} catch (Exception e) {
		}
		return false;
	}

	private boolean didMisClick() {
		Methods.sleep(1);
		try {
			Robot colorSampler = new Robot();
			Point mousePos = mouse.getPressLocation();
			Color pixelColor = colorSampler.getPixelColor(
					(int) mousePos.getX() - 1, (int) mousePos.getY() + 1);
			Color misClick = new Color(254, 254, 0);
			return pixelColor.equals(misClick);
		} catch (AWTException e) {
			log("Failed to match colors");
			e.printStackTrace();
		}
		return false;
	}

	private boolean isOnScreen(RSModel m) {
		return getPointOnScreen(m, true) != null;
	}

	public Point getPointOnScreen(RSModel m, boolean first) {
		final ArrayList<Point> list = new ArrayList<Point>();
		try {
			final Polygon[] tris = m.getTriangles();
			for (final Polygon p : tris) {
				for (int j = 0; j < p.xpoints.length; j++) {
					final Point firstPoint = new Point(p.xpoints[j],
							p.ypoints[j]);
					if (calc.pointOnScreen(firstPoint))
						return firstPoint;
					list.add(firstPoint);
				}
			}
		} catch (final Exception ignored) {
		}
		return list.size() > 0 ? list.get(Methods.random(0, list.size()))
				: null;
	}

	private boolean walkTile(RSTile t, int x, int y) {
		RSTile dest = new RSTile(t.getX() + random(0, x), t.getY()
				+ random(0, y));
		Point p = calc.tileToMinimap(dest);
		while (!mouse.getLocation().equals(p)) {
			p = calc.tileToMinimap(dest);
			if (p.x == -1 || p.y == -1)
				return false;
			moveMouse(10, p);
		}
		mouse.click(true);
		return true;
	}

	private void moveMouse(int time, Point p) {
		long endTime = System.currentTimeMillis() + time;
		while (System.currentTimeMillis() < endTime) {
			mouse.move(p, 0, 0);
		}
	}

	private boolean sleepToMove(int timeout) {
		long endTime = System.currentTimeMillis() + timeout;
		while (System.currentTimeMillis() < endTime) {
			if (getMyPlayer().isMoving())
				return true;
			Methods.sleep(Methods.random(20, 30));
		}
		return false;
	}

	private boolean walkingTowardsMusician(RSNPC npc) {
		int Delta = 0;
		int test1 = calc.distanceTo(npc); // 7
		while (Delta == 0 && players.getMyPlayer().isMoving()) {
			int test2 = calc.distanceTo(npc); // 5
			Delta = test1 - test2; // 7-5 = 3
		}
		if (!players.getMyPlayer().isMoving()) {
			return false;
		}
		if (Delta >= 1) {
			return true;
		}
		return false;
	}

	// Credits go to Enfilade for this epic walking method
	private int nextStep = randomGauss(16, 19, 17.5, 0.9);

	private boolean tileInNextRange(RSTile t) {
		return calc.distanceBetween(t, getMyPlayer().getLocation()) < nextStep;
	}

	private int step(ArrayList<RSTile> path) {
		if (calc.distanceBetween(getMyPlayer().getLocation(), path.get(path
				.size() - 1)) < 2)
			return path.size();
		RSTile dest = walking.getDestination();
		int index = -1;
		int shortestDist = 0, dist, shortest = -21;
		if (dest != null) {
			for (int i = 0; i < path.size(); i++) {
				dist = (int) calc.distanceBetween(path.get(i), dest);
				if (shortest < 0 || shortestDist > dist) {
					shortest = i;
					shortestDist = dist;
				}
			}
		}
		for (int i = path.size() - 1; i >= 0; i--) {
			if (tileInNextRange(path.get(i))) {
				index = i;
				break;
			}
		}
		if (index >= 0
				&& (dest == null || (index > shortest) || !players
						.getMyPlayer().isMoving())) {
			if (walkTile(path.get(index), 0, 0))
				sleepToMove(Methods.random(600, 900));
			nextStep = randomGauss(16, 19, 17.5, 0.9);
			return index;
		}
		if (!sleepToMove(Methods.random(5000, 8000)))
			path.get(0);
		return -1;
	}

	@SuppressWarnings("unused")
	private int getNearestTile(ArrayList<RSTile> path) {
		int shortest = 0;
		int shortestDist = calc.distanceTo(path.get(0)), dist;
		for (int i = 1; i < path.size(); i++) {
			dist = calc.distanceTo(path.get(i));
			if (dist < shortestDist) {
				shortestDist = dist;
				shortest = i;
			}
		}
		return shortest;
	}

	private Line[] reverse(Line[] lines) {
		Line[] rev = new Line[lines.length];
		for (int i = 0; i < lines.length; i++)
			rev[i] = lines[lines.length - (i + 1)];
		return rev;
	}

	private ArrayList<RSTile> generatePath(Line[] lines) {
		double minStep = 3, maxStep = 16;
		int wander = Methods.random(3, 6);
		if (lines.length < 2)
			return null;
		ArrayList<RSTile> path = new ArrayList<RSTile>();
		Line l1, l2 = lines[0];
		double distFromCenter = random(0, l2.getDistance() + 1);
		RSTile p = l2.translate((int) distFromCenter);
		distFromCenter = l2.getDistance() / 2 - distFromCenter;
		double centerXdist, centerYdist, line1Xdist, line1Ydist, line2Xdist, line2Ydist;
		double line1dist, line2dist, centerDist;
		double x, y;
		double distOnLine, last, cap1, cap2, move;
		double distFromCenterX1, distFromCenterY1, distFromCenterX2, distFromCenterY2;
		double force1, force2, slopeX, slopeY, slopeDist;
		boolean finished;
		int lastX = p.getX(), lastY = p.getY(), curX, curY;
		double dist, xdist, ydist;
		for (int i = 1; i < lines.length; i++) {
			l1 = l2;
			l2 = lines[i];
			centerXdist = l2.getCenterX() - l1.getCenterX();
			centerYdist = l2.getCenterY() - l1.getCenterY();
			centerDist = Math.sqrt(centerXdist * centerXdist + centerYdist
					* centerYdist);
			line1Xdist = l2.getX() - l1.getX();
			line1Ydist = l2.getY() - l1.getY();
			line2Xdist = l2.getX2() - l1.getX2();
			line2Ydist = l2.getY2() - l1.getY2();
			centerXdist /= centerDist;
			centerYdist /= centerDist;
			line1Xdist /= centerDist;
			line1Ydist /= centerDist;
			line2Xdist /= centerDist;
			line2Ydist /= centerDist;
			distOnLine = 0;
			last = 0;
			finished = false;
			while (!finished) {
				distOnLine += random(minStep, maxStep);
				if (distOnLine >= centerDist) {
					distOnLine = centerDist;
					finished = true;
				}
				x = centerXdist * distOnLine + l1.getCenterX();
				y = centerYdist * distOnLine + l1.getCenterY();
				distFromCenterX1 = x - (line1Xdist * distOnLine + l1.getX());
				distFromCenterY1 = y - (line1Ydist * distOnLine + l1.getY());
				distFromCenterX2 = x - (line2Xdist * distOnLine + l1.getX2());
				distFromCenterY2 = y - (line2Ydist * distOnLine + l1.getY2());
				slopeX = distFromCenterX2 - distFromCenterX1;
				slopeY = distFromCenterY2 - distFromCenterY1;
				slopeDist = Math.sqrt(slopeX * slopeX + slopeY * slopeY);
				slopeX /= slopeDist;
				slopeY /= slopeDist;
				line1dist = Math.sqrt(distFromCenterX1 * distFromCenterX1
						+ distFromCenterY1 * distFromCenterY1);
				line2dist = Math.sqrt(distFromCenterX2 * distFromCenterX2
						+ distFromCenterY2 * distFromCenterY2);
				move = (distOnLine - last) / maxStep * wander;
				force1 = line1dist + distFromCenter;
				force2 = line2dist - distFromCenter;
				cap1 = Math.min(move, force1);
				cap2 = Math.min(move, force2);
				if (force1 < 0)
					distFromCenter -= force1;
				else if (force2 < 0)
					distFromCenter += force2;
				else
					distFromCenter += random(-cap1, cap2);
				if (finished) {
					RSTile t = l2.translateFromCenter(distFromCenter);
					curX = t.getX();
					curY = t.getY();
				} else {
					curX = (int) Math.round(distOnLine * centerXdist
							+ l1.getCenterX() + distFromCenter * slopeX);
					curY = (int) Math.round(distOnLine * centerYdist
							+ l1.getCenterY() + distFromCenter * slopeY);
				}
				xdist = curX - lastX;
				ydist = curY - lastY;
				dist = Math.sqrt(xdist * xdist + ydist * ydist);
				xdist /= dist;
				ydist /= dist;
				for (int j = 0; j < dist; j++) {
					path.add(new RSTile((int) Math.round(xdist * j + lastX),
							(int) Math.round(ydist * j + lastY)));
				}
				last = distOnLine;
				lastX = curX;
				lastY = curY;
			}
		}
		return cutUp(path);
	}

	public ArrayList<RSTile> cutUp(ArrayList<RSTile> tiles) {
		ArrayList<RSTile> path = new ArrayList<RSTile>();
		int index = 0;
		while (index < tiles.size()) {
			path.add(tiles.get(index));
			index += Methods.random(8, 12);
		}
		if (!path.get(path.size() - 1).equals(tiles.get(tiles.size() - 1)))
			path.add(tiles.get(tiles.size() - 1));
		return path;
	}

	private class Line {
		private int x, y, xdist, ydist, x2, y2, centerX, centerY;
		private RSTile t1, t2;
		private double dist;

		public Line(int x1, int y1, int x2, int y2) {
			t1 = new RSTile(x1, y1);
			t2 = new RSTile(x2, y2);
			x = x1;
			y = y1;
			this.x2 = x2;
			this.y2 = y2;
			xdist = x2 - x1;
			ydist = y2 - y1;
			centerX = x + (int) (0.5 * xdist);
			centerY = y + (int) (0.5 * ydist);
			dist = Math.sqrt(xdist * xdist + ydist * ydist);
		}

		public int getCenterX() {
			return centerX;
		}

		public int getCenterY() {
			return centerY;
		}

		public RSTile getTile1() {
			return t1;
		}

		public RSTile getTile2() {
			return t2;
		}

		public void drawTo(Graphics g, Line line) {
			((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON);
			((Graphics2D) g).setRenderingHint(
					RenderingHints.KEY_COLOR_RENDERING,
					RenderingHints.VALUE_COLOR_RENDER_QUALITY);
			((Graphics2D) g).setRenderingHint(RenderingHints.KEY_RENDERING,
					RenderingHints.VALUE_RENDER_QUALITY);
			if (!calc.tileOnMap(t1) || !calc.tileOnMap(t2))
				return;
			if (calc.tileOnMap(line.getTile1())
					&& calc.tileOnMap(line.getTile2())) {
				Point p1 = calc.tileToMinimap(t1);
				Point p2 = calc.tileToMinimap(t2);
				Point p3 = calc.tileToMinimap(line.getTile2());
				Point p4 = calc.tileToMinimap(line.getTile1());
				GeneralPath path = new GeneralPath();
				path.moveTo(p1.x, p1.y);
				path.lineTo(p2.x, p2.y);
				path.lineTo(p3.x, p3.y);
				path.lineTo(p4.x, p4.y);
				path.closePath();
				g.setColor(fillColor);
				((Graphics2D) g).fill(path);
				((Graphics2D) g).draw(path);
			}
			Point last = null, p;
			g.setColor(lineColor);
			for (RSTile t : pathList) {
				if (calc.tileOnMap(t)) {
					p = calc.tileToMinimap(t);
					g.fillOval(p.x - 2, p.y - 2, 5, 5);
					if (last != null)
						g.drawLine(p.x, p.y, last.x, last.y);
					last = p;
				} else
					last = null;
			}
		}

		public int getX() {
			return x;
		}

		public int getY() {
			return y;
		}

		public int getX2() {
			return x2;
		}

		public int getY2() {
			return y2;
		}

		public double getDistance() {
			return dist;
		}

		public RSTile translate(double length) {
			return new RSTile((int) Math.round(length * (xdist / dist)) + x,
					(int) Math.round(length * (ydist / dist)) + y);
		}

		public RSTile translateFromCenter(double length) {
			return new RSTile((int) Math.round(centerX - (xdist / dist)
					* length), (int) Math.round(centerY - (ydist / dist)
							* length));
		}
	}

	@Override
	public void onRepaint(Graphics g) {
		if (game.isLoggedIn() && !game.isLoginScreen() && !stoppingScript) {

			Graphics2D g2 = (Graphics2D) g;

			long millis = System.currentTimeMillis() - startTime;
			if (millis <= 0) {
				millis = 1;
			}
			if ((skills.getCurrentExp(Skills.RUNECRAFTING) - startExp) > 0
					&& startExp > 0) {
				expGained = skills.getCurrentExp(Skills.RUNECRAFTING)
				- startExp;
			}
			g2.setColor(Mouse);
			g2.drawImage(img1, ((int) (mouse.getLocation().getX()) - 31),
					((int) (mouse.getLocation().getY()) - 31), null);
			g2.drawImage(img2, 396, 5, null);
			if (Rune == 554) { // Fire rune
				if (getAction() == Action.WALKTORUINS)
					for (int i = 1; i < TORUINS.length; i++)
						TORUINS[i].drawTo(g2, TORUINS[i - 1]);
				else
					for (int i = 1; i < TOBANK.length; i++)
						TOBANK[i].drawTo(g2, TOBANK[i - 1]);
			} else
				for (int i = 1; i < TORUINS.length; i++)
					TORUINS[i].drawTo(g2, TORUINS[i - 1]);

			long epxTillLevel = skills.getExpToNextLevel(Skills.RUNECRAFTING);
			int profit = (runePrice * runesCrafted)
			- (essencePrice * essenceUsed);
			long expPerHour = (expGained * 3600000l) / millis;
			long craftedPerHour = (runesCrafted * 3600000l) / millis;
			long essUsedPerHour = (essenceUsed * 3600000l) / millis;
			long profitPerHour = (profit * 3600000l) / millis;
			long essTillLevel = (skills.getExpToNextLevel(Skills.RUNECRAFTING) / 6);

			int x = 15;

			if (tab == 1) { // General
				g2.drawImage(tabOne, -1, 293, null);

				drawString(g2, "Status: " + Status, x, 365);
				drawString(g2, "Time running: " + getFormattedTime(millis), x,
						381);

				drawString(g2, "Rune crafting: " + runeString, x, 417);
				if (runesCrafted > 0) {
					drawString(g2, "Runes crafted: "
							+ getFormattedMoney(runesCrafted) + " ("
							+ getFormattedMoney(craftedPerHour) + "/hr)", x,
							433);
				} else {
					drawString(g2, "Runes crafted: 0 (0/h)", x, 433);
				}
				if (runesCrafted > 0) {
					drawString(g2, "Essence used: "
							+ getFormattedMoney(essenceUsed) + " ("
							+ getFormattedMoney(essUsedPerHour) + "/hr)", x,
							449);
				} else {
					drawString(g2, "Essence used: 0 (0/h)", x, 449);
				}
				if (essenceInBank > 0) {
					drawString(g2, "Essence in bank: "
							+ getFormattedMoney(essenceInBank), x, 465);
				} else {
					drawString(g2, "Essence in bank: Unknown", x, 465);
				}

			} else if (tab == 2) { // exp and cash
				g2.drawImage(tabTwo, -1, 293, null);

				String string = skills
				.getPercentToNextLevel(Skills.RUNECRAFTING)
				+ "%";
				FontMetrics fontMetrics = g2.getFontMetrics();
				Rectangle2D stringWidth = fontMetrics.getStringBounds(string,
						g2);

				g2.setColor(new Color(90, 15, 15, 100));
				g2.drawRect(x, 390, 250, 16);
				g2.setColor(new Color(0, 0, 0, 50));
				g2.fillRect(x, 390, (int) (skills
						.getPercentToNextLevel(Skills.RUNECRAFTING) * 2.5), 16);
				drawString(g2, string, x
						+ ((250 / 2) - ((int) stringWidth.getWidth() / 2)),
						390 + 13);

				long timeToLevel = 0;
				String timeToLevel2 = "Calculating";
				if (expPerHour > 0) {
					timeToLevel = (long) (skills
							.getExpToNextLevel(Skills.RUNECRAFTING) * 60 / expPerHour);
					if (timeToLevel >= 60) {
						long thours = (int) timeToLevel / 60;
						long tmin = (timeToLevel - (thours * 60));
						timeToLevel2 = thours + " Hours, " + tmin + " Minutes";
					} else {
						timeToLevel2 = timeToLevel + " Minutes";
					}
				}
				drawString(g2, "Time untill level: " + timeToLevel2, x, 433);
				drawString(g2, "XP gained: " + getFormattedMoney(expGained)
						+ " (" + getFormattedMoney(expPerHour) + "/hr)", x, 449);
				if (runesCrafted > 0) {
					drawString(g2, "Profit made: " + getFormattedMoney(profit)
							+ " gp" + " (" + getFormattedMoney(profitPerHour)
							+ "/hr)", x, 465);
				} else {
					drawString(g2, "Profit made: 0 (0/h)", x, 465);
				}

				drawString(g2, "Essence untill level: "
						+ getFormattedMoney(essTillLevel), 275, 433);
				drawString(g2, "XP untill level: "
						+ getFormattedMoney(epxTillLevel), 275, 449);
				if (runesCrafted > 0) {
					int runeRatio = (runesCrafted / essenceUsed);
					drawString(g2, "Rune per Essence ratio: " + runeRatio
							+ "/1", 275, 465);
				} else {
					drawString(g2, "Rune per Essence ratio: Calculating", 275,
							465);
				}
			} else { // closed
				g.drawImage(closed, 162, 293, null);
			}
		}
	}

	private void drawString(Graphics g2, String s, int x, int y) {
		g2.setColor(new Color(90, 15, 15));
		g2.setFont(new Font("Serif", 0, 12));
		g2.drawString(s, x, y);
		g2.setColor(new Color(255, 255, 255, 90));
		g2.drawString(s, x + 1, y + 1);
	}

	public static String getFormattedTime(final long timeMillis, String sep,
			boolean millisInc) {
		long millis = timeMillis;
		final long days = millis / (24 * 1000 * 60 * 60);
		millis -= days * (24 * 1000 * 60 * 60);
		final long hours = millis / (1000 * 60 * 60);
		millis -= hours * 1000 * 60 * 60;
		final long minutes = millis / (1000 * 60);
		millis -= minutes * 1000 * 60;
		final long _seconds = millis / 1000;
		millis -= _seconds * 1000;
		String dayString = String.valueOf(days);
		String hoursString = String.valueOf(hours);
		String minutesString = String.valueOf(minutes);
		String secondsString = String.valueOf(_seconds);
		if (hours < 10) {
			hoursString = 0 + hoursString;
		}
		if (minutes < 10) {
			minutesString = 0 + minutesString;
		}
		if (_seconds < 10) {
			secondsString = 0 + secondsString;
		}
		String s = "";
		if (days > 0) {
			s = dayString + sep;
		}
		s += hoursString + sep + minutesString + sep + secondsString;
		if (millisInc) {
			s += sep + String.valueOf(millis);
		}
		return s;
	}

	public static String getFormattedTime(final long timeMillis) {
		return getFormattedTime(timeMillis, ":", false);
	}

	public static String getFormattedMoney(long money) {
		return new DecimalFormat("###,###,###,###,###,###").format(money);
	}

	private Image getImage(String url) {
		try {
			return ImageIO.read(new URL(url));
		} catch (IOException e) {
			return null;
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (tabOneRect.contains(e.getPoint())) {
			tab = 1;
		}
		if (tabTwoRect.contains(e.getPoint())) {
			tab = 2;
		}
		if (hideRect.contains(e.getPoint())) {
			tab = 3;
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
		Point mp = e.getPoint();
		final Rectangle toggleRectangle = new Rectangle(493, 347, 16, 15);
		if (toggleRectangle.contains(mp)) {
			if (Pressed) {
				Pressed = false;
			} else {
				Pressed = true;
			}
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	private boolean inAltarArea() {
		return ALTAR_AREA.contains(getMyPlayer().getLocation());
	}

	private java.util.Random generateSeededRandom(String name) {
		MessageDigest coder;
		try {
			coder = MessageDigest.getInstance("MD5");
			coder.update(name.getBytes());
			byte[] bytes = coder.digest();
			long result1 = 0, result2 = 0;
			for (int i = 0; i < 8; i++) {
				result1 = (result1 << 8) | bytes[i];
			}
			for (int i = 8; i < 16; i++) {
				result2 = (result2 << 8) | bytes[i];
			}
			return new java.util.Random(result1 - result2);
		} catch (NoSuchAlgorithmException ex) {
		}
		return new java.util.Random();
	}

	public final class PerfectGUI extends JFrame implements ActionListener {

		private static final long serialVersionUID = 1L;
		public boolean antiban = true, startScript = false, close = false,
		screenOnLevel, screenOnHour, screenOnFinish, restMode;
		public int minMouse, maxMouse;
		private javax.swing.JButton jButton1;
		private javax.swing.JButton jButton2;
		private javax.swing.JCheckBox jCheckBox1;
		private javax.swing.JCheckBox jCheckBox2;
		private javax.swing.JCheckBox jCheckBox3;
		private javax.swing.JCheckBox jCheckBox4;
		private javax.swing.JCheckBox jCheckBox5;
		private javax.swing.JComboBox jComboBox1;
		private javax.swing.JLabel jLabel1;
		private javax.swing.JLabel jLabel2;
		private javax.swing.JLabel jLabel4;
		private javax.swing.JTextField jTextField1;
		private javax.swing.JTextField jTextField2;

		public PerfectGUI() {
			initComponents();
		}

		public void initComponents() {
			jLabel2 = new javax.swing.JLabel();
			jCheckBox1 = new JCheckBox();
			jCheckBox2 = new JCheckBox();
			jCheckBox3 = new JCheckBox();
			jCheckBox4 = new JCheckBox();
			jCheckBox5 = new JCheckBox();
			jLabel4 = new JLabel();
			jTextField1 = new JTextField();
			jTextField2 = new JTextField();
			jButton1 = new JButton();
			jButton2 = new JButton();
			jLabel1 = new JLabel();
			jComboBox1 = new JComboBox();
			setTitle("AIORuneCrafter");
			jLabel2.setFont(new Font("Tahoma", 0, 18));
			jLabel2.setText("AIORuneCrafter");
			jLabel1.setText("Rune");
			jComboBox1.setModel(new DefaultComboBoxModel(new String[] { "Air",
					"Mind", "Water", "Earth", "Fire", "Body" }));
			jCheckBox1.setText("Antiban");
			jCheckBox1.setSelected(true);
			jCheckBox2.setText("Rest");
			jCheckBox3.setText("Screenshot on level");
			jCheckBox4.setText("Screenshot every hour");
			jCheckBox5.setText("Screenshot on finish");
			jLabel4.setText("Mouse speed");
			jTextField1.setFont(new Font("Monospaced", 0, 11));
			jTextField1.setText("4");
			jTextField2.setFont(new Font("Monospaced", 0, 11));
			jTextField2.setText("7");
			jButton1.setText("Start");
			jButton2.setText("Cancel");
			jComboBox1.addActionListener(this);
			jCheckBox1.addActionListener(this);
			jCheckBox2.addActionListener(this);
			jCheckBox3.addActionListener(this);
			jCheckBox4.addActionListener(this);
			jCheckBox5.addActionListener(this);
			jTextField1.addActionListener(this);
			jTextField2.addActionListener(this);
			jButton1.addActionListener(this);
			jButton2.addActionListener(this);
			GroupLayout layout = new GroupLayout(getContentPane());
			getContentPane().setLayout(layout);
			layout
			.setHorizontalGroup(layout
					.createParallelGroup(GroupLayout.Alignment.LEADING)
					.addGroup(
							layout
							.createSequentialGroup()
							.addGroup(
									layout
									.createParallelGroup(
											GroupLayout.Alignment.LEADING)
											.addGroup(
													layout
													.createSequentialGroup()
													.addContainerGap()
													.addComponent(
															jButton2,
															GroupLayout.DEFAULT_SIZE,
															141,
															Short.MAX_VALUE))
															.addGroup(
																	layout
																	.createSequentialGroup()
																	.addContainerGap()
																	.addComponent(
																			jButton1,
																			GroupLayout.DEFAULT_SIZE,
																			141,
																			Short.MAX_VALUE))
																			.addGroup(
																					layout
																					.createSequentialGroup()
																					.addGap(
																							60,
																							60,
																							60)
																							.addGroup(
																									layout
																									.createParallelGroup(
																											GroupLayout.Alignment.LEADING)
																											.addComponent(
																													jComboBox1,
																													GroupLayout.PREFERRED_SIZE,
																													GroupLayout.DEFAULT_SIZE,
																													GroupLayout.PREFERRED_SIZE)
																													.addComponent(
																															jLabel2))))
																															.addContainerGap())
																															.addGroup(
																																	GroupLayout.Alignment.TRAILING,
																																	layout
																																	.createSequentialGroup()
																																	.addContainerGap()
																																	.addComponent(jLabel4)
																																	.addPreferredGap(
																																			LayoutStyle.ComponentPlacement.RELATED,
																																			17, Short.MAX_VALUE)
																																			.addComponent(
																																					jTextField1,
																																					javax.swing.GroupLayout.PREFERRED_SIZE,
																																					23,
																																					GroupLayout.PREFERRED_SIZE)
																																					.addPreferredGap(
																																							LayoutStyle.ComponentPlacement.RELATED)
																																							.addComponent(
																																									jTextField2,
																																									javax.swing.GroupLayout.PREFERRED_SIZE,
																																									23,
																																									GroupLayout.PREFERRED_SIZE)
																																									.addGap(14, 14, 14)).addGroup(
																																											layout.createSequentialGroup()
																																											.addContainerGap().addComponent(
																																													jCheckBox5)
																																													.addContainerGap(28,
																																															Short.MAX_VALUE)).addGroup(
																																																	layout.createSequentialGroup()
																																																	.addContainerGap().addComponent(
																																																			jCheckBox4)
																																																			.addContainerGap(14,
																																																					Short.MAX_VALUE)).addGroup(
																																																							layout.createSequentialGroup()
																																																							.addContainerGap().addComponent(
																																																									jCheckBox3)
																																																									.addContainerGap(30,
																																																											Short.MAX_VALUE)).addGroup(
																																																													layout.createSequentialGroup()
																																																													.addContainerGap().addComponent(
																																																															jCheckBox2)
																																																															.addContainerGap(74,
																																																																	Short.MAX_VALUE)).addGroup(
																																																																			layout.createSequentialGroup()
																																																																			.addContainerGap().addComponent(
																																																																					jLabel1).addContainerGap(
																																																																							121, Short.MAX_VALUE))
																																																																							.addGroup(
																																																																									layout.createSequentialGroup()
																																																																									.addContainerGap().addComponent(
																																																																											jCheckBox1)
																																																																											.addContainerGap(88,
																																																																													Short.MAX_VALUE)));
			layout
			.setVerticalGroup(layout
					.createParallelGroup(GroupLayout.Alignment.LEADING)
					.addGroup(
							layout
							.createSequentialGroup()
							.addComponent(jLabel2)
							.addGap(7, 7, 7)
							.addGroup(
									layout
									.createParallelGroup(
											GroupLayout.Alignment.BASELINE)
											.addComponent(
													jLabel1)
													.addComponent(
															jComboBox1,
															GroupLayout.PREFERRED_SIZE,
															GroupLayout.DEFAULT_SIZE,
															GroupLayout.PREFERRED_SIZE))
															.addPreferredGap(
																	LayoutStyle.ComponentPlacement.RELATED)
																	.addComponent(jCheckBox1)
																	.addPreferredGap(
																			LayoutStyle.ComponentPlacement.RELATED)
																			.addComponent(jCheckBox2)
																			.addPreferredGap(
																					LayoutStyle.ComponentPlacement.RELATED)
																					.addComponent(jCheckBox3)
																					.addPreferredGap(
																							LayoutStyle.ComponentPlacement.UNRELATED)
																							.addComponent(jCheckBox4)
																							.addPreferredGap(
																									LayoutStyle.ComponentPlacement.RELATED)
																									.addComponent(jCheckBox5)
																									.addPreferredGap(
																											LayoutStyle.ComponentPlacement.UNRELATED)
																											.addGroup(
																													layout
																													.createParallelGroup(
																															GroupLayout.Alignment.TRAILING)
																															.addGroup(
																																	layout
																																	.createParallelGroup(
																																			GroupLayout.Alignment.BASELINE)
																																			.addComponent(
																																					jTextField1,
																																					GroupLayout.PREFERRED_SIZE,
																																					GroupLayout.DEFAULT_SIZE,
																																					GroupLayout.PREFERRED_SIZE)
																																					.addComponent(
																																							jTextField2,
																																							GroupLayout.PREFERRED_SIZE,
																																							GroupLayout.DEFAULT_SIZE,
																																							GroupLayout.PREFERRED_SIZE))
																																							.addComponent(
																																									jLabel4))
																																									.addPreferredGap(
																																											LayoutStyle.ComponentPlacement.UNRELATED)
																																											.addComponent(jButton1)
																																											.addPreferredGap(
																																													LayoutStyle.ComponentPlacement.RELATED)
																																													.addComponent(jButton2)
																																													.addContainerGap(
																																															GroupLayout.DEFAULT_SIZE,
																																															Short.MAX_VALUE)));
			pack();
			setLocationRelativeTo(getOwner());
			setVisible(true);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == jComboBox1) {
				if (jComboBox1.getSelectedItem() == "Mind") {
					Rune = 558;
					Tiara = 5529;
					bankBoothID = 11758;
					ruinsID = 2453;
					altarID = 2479;
					portalID = 2466;
					musicianID = 5442;
					altarWalkTile = new RSTile(2788, 4839);
					portalWalkTile = new RSTile(2793, 4828);
					ALTAR_AREA = new RSArea(new RSTile(2781, 4824), new RSTile(
							2797, 4847));
					TORUINS = new Line[] { new Line(2944, 3368, 2949, 3368),
							new Line(2945, 3374, 2949, 3374),
							new Line(2949, 3377, 2954, 3377),
							new Line(2955, 3384, 2955, 3379),
							new Line(2960, 3384, 2960, 3380),
							new Line(2961, 3385, 2969, 3385),
							new Line(2964, 3393, 2967, 3393),
							new Line(2962, 3402, 2968, 3402),
							new Line(2965, 3408, 2970, 3408),
							new Line(2965, 3414, 2970, 3411),
							new Line(2974, 3415, 2974, 3412),
							new Line(2979, 3420, 2985, 3414),
							new Line(2986, 3421, 2989, 3421),
							new Line(2986, 3427, 2989, 3427),
							new Line(2981, 3431, 2990, 3438),
							new Line(2967, 3445, 2985, 3447),
							new Line(2961, 3453, 2977, 3455),
							new Line(2963, 3472, 2974, 3472),
							new Line(2969, 3488, 2979, 3483),
							new Line(2971, 3497, 2986, 3494),
							new Line(2974, 3507, 2985, 3504),
							new Line(2980, 3514, 2984, 3514) };
				}
				if (jComboBox1.getSelectedItem() == "Water") {
					runeString = "Water runes";
					Rune = 555;
					Tiara = 5531;
					bankBoothID = 35647;
					ruinsID = 2454;
					altarID = 2480;
					portalID = 2467;
					ruinsTile = new RSTile(3187, 3164);
					bankTile = new RSTile(3269, 3169);
					altarWalkTile = new RSTile(3486, 4836);
					portalWalkTile = new RSTile(3496, 4832);
					ALTAR_AREA = new RSArea(new RSTile(3474, 4845), new RSTile(
							3500, 4822));
					TORUINS = new Line[] { new Line(3269, 3172, 3270, 3162),
							new Line(3262, 3176, 3262, 3171),
							new Line(3255, 3174, 3255, 3167),
							new Line(3249, 3174, 3249, 3167),
							new Line(3242, 3171, 3242, 3166),
							new Line(3235, 3174, 3235, 3153),
							new Line(3220, 3178, 3220, 3154),
							new Line(3216, 3178, 3216, 3153),
							new Line(3206, 3176, 3206, 3153),
							new Line(3197, 3174, 3197, 3151),
							new Line(3189, 3169, 3189, 3160),
							new Line(3186, 3168, 3186, 3162) };
				}
				if (jComboBox1.getSelectedItem() == "Earth") {
					runeString = "Earth runes";
					Rune = 557;
					Tiara = 5535;
					bankBoothID = 782;
					ruinsID = 2455;
					altarID = 2481;
					portalID = 2468;
					musicianID = 8700;
					altarWalkTile = new RSTile(2658, 4839);
					portalWalkTile = new RSTile(2655, 4830);
					ALTAR_AREA = new RSArea(new RSTile(2651, 4821), new RSTile(
							2665, 4845));
					TORUINS = new Line[] { new Line(3251, 3420, 3256, 3420),
							new Line(3258, 3431, 3260, 3421),
							new Line(3267, 3430, 3268, 3426),
							new Line(3275, 3431, 3275, 3426),
							new Line(3275, 3439, 3288, 3429),
							new Line(3276, 3446, 3294, 3444),
							new Line(3283, 3463, 3294, 3455),
							new Line(3293, 3470, 3299, 3459),
							new Line(3304, 3474, 3306, 3472) };
				}
				if (jComboBox1.getSelectedItem() == "Fire") {
					runeString = "Fire runes";
					Rune = 554;
					Tiara = 5537;
					ruinsID = 2456;
					altarID = 2482;
					portalID = 2469;
					altarWalkTile = new RSTile(2583, 4839);
					portalWalkTile = new RSTile(2577, 4845);
					ALTAR_AREA = new RSArea(new RSTile(2574, 4832), new RSTile(
							2595, 4851));
					TORUINS = new Line[] { new Line(3383, 3271, 3383, 3268),
							new Line(3379, 3266, 3379, 3265),
							new Line(3373, 3267, 3373, 3265),
							new Line(3367, 3269, 3367, 3263),
							new Line(3361, 3267, 3361, 3263),
							new Line(3354, 3267, 3354, 3263),
							new Line(3336, 3270, 3336, 3265),
							new Line(3326, 3266, 3328, 3264),
							new Line(3324, 3263, 3326, 3262),
							new Line(3317, 3262, 3319, 3252),
							new Line(3313, 3257, 3313, 3253) };
				}
				if (jComboBox1.getSelectedItem() == "Body") {
					runeString = "Body runes";
					Rune = 559;
					Tiara = 5533;
					bankBoothID = 26972;
					ruinsID = 2457;
					altarID = 2483;
					portalID = 2470;
					altarWalkTile = new RSTile(2521, 4842);
					portalWalkTile = new RSTile(2521, 4834);
					ALTAR_AREA = new RSArea(new RSTile(2515, 4831), new RSTile(
							2531, 4849));
					TORUINS = new Line[] { new Line(3094, 3489, 3094, 3493),
							new Line(3090, 3483, 3084, 3487),
							new Line(3082, 3482, 3078, 3482),
							new Line(3083, 3467, 3079, 3467),
							new Line(3082, 3461, 3069, 3461),
							new Line(3075, 3459, 3070, 3459),
							new Line(3072, 3450, 3066, 3449),
							new Line(3060, 3441, 3062, 3447),
							new Line(3053, 3443, 3053, 3447) };
				}
			}
			if (e.getSource() == jCheckBox1) {
				if (jCheckBox1.isSelected()) {
					antiban = true;
				} else {
					antiban = false;
				}
			}
			if (e.getSource() == jCheckBox2) {
				if (jCheckBox2.isSelected()) {
					restMode = true;
				} else {
					restMode = false;
				}
			}
			if (e.getSource() == jCheckBox3) {
				if (jCheckBox3.isSelected()) {
					screenOnLevel = true;
				} else {
					screenOnLevel = false;
				}
			}
			if (e.getSource() == jCheckBox4) {
				if (jCheckBox4.isSelected()) {
					screenOnHour = true;
				} else {
					screenOnHour = false;
				}
			}
			if (e.getSource() == jCheckBox5) {
				if (jCheckBox5.isSelected()) {
					screenOnFinish = true;
				} else {
					screenOnFinish = false;
				}
			}
			if (e.getSource() == jButton1) {
				Start(e);
				minMouse = Integer.parseInt(jTextField1.getText());
				maxMouse = Integer.parseInt(jTextField2.getText());
				dispose();
			}
			if (e.getSource() == jButton2) {
				setVisible(false);
				dispose();
				close = true;
			}
		}

		private void Start(ActionEvent e) {
			setVisible(false);
			startScript = true;
		}
	}
}