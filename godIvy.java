import org.rsbot.event.events.MessageEvent;
import org.rsbot.event.listeners.MessageListener;
import org.rsbot.event.listeners.PaintListener;
import org.rsbot.script.Script;
import org.rsbot.script.ScriptManifest;
import org.rsbot.script.methods.Skills;
import org.rsbot.script.util.Filter;
import org.rsbot.script.util.Timer;
import org.rsbot.script.wrappers.*;

import java.awt.*;

@ScriptManifest(name = "godIvy FREE", authors = {"Timer"}, version = 0.1, description = "A simple free ivy chopper.", requiresVersion = 243)
public class godIvy extends Script implements PaintListener, MessageListener {
	private static final Filter<RSGroundItem> BIRDS_NEST_FILTER = new Filter<RSGroundItem>() {
		public boolean accept(final RSGroundItem rsGroundItem) {
			try {
				return rsGroundItem != null && rsGroundItem.getItem().getName().contains("nest");
			} catch (NullPointerException ignored) {
				return false;
			}
		}
	};
	private static final Filter<RSObject> IVY_OBJECT_FILTER = new Filter<RSObject>() {
		public boolean accept(RSObject rsObject) {
			try {
				return rsObject != null && rsObject.getName().equalsIgnoreCase("ivy");
			} catch (NullPointerException ignored) {
				return false;
			}
		}
	};
	private RSTile returnTile = null;
	private RSWeb walkWeb = null;
	private RSTile nearestBank = null;
	private RSObject ivyRecord = null;
	private int ivyChopped = 0;
	private Timer runningTimer = new Timer(0);
	private int startXP = -1;

	@Override
	public boolean onStart() {
		runningTimer = new Timer(0);
		return true;
	}

	@Override
	public int loop() {
		if (game.getPlane() != 0) {
			log("We somehow managed to go up some stairs, aw man!");
			return -1;
		}
		if (getMyPlayer().getAnimation() != -1 || (getMyPlayer().isMoving() && walkWeb == null)) {
			return random(2500, 3500);
		}
		if (inventory.isFull() && (nearestBank = web.getNearestBank()) != null) {
			RSObject newIvy = objects.getNearest(IVY_OBJECT_FILTER);
			if (newIvy != null) {
				returnTile = newIvy.getLocation();
			} else if (returnTile == null) {
				log("Waiting for ivy spawn to ensure walk-back.");
				return 1500;
			}
			if (walkWeb == null || !walkWeb.getEnd().equals(nearestBank)) {
				walkWeb = web.getWeb(nearestBank);
				if (walkWeb == null) {
					log("We don't support ivy here!");
					return -1;
				}
			}
			if (!walkWeb.finished() && !walkWeb.step()) {
				walkWeb = null;
				return 0;
			}
			final int hatchet = inventory.getItemID("hatchet");
			if (walkWeb.finished()) {
				if (bank.open()) {
					bank.depositAllExcept(hatchet);
				}
			}
			return 0;
		}
		if (returnTile != null) {
			if (walkWeb == null || !walkWeb.getEnd().equals(returnTile)) {
				walkWeb = web.getWeb(returnTile);
				if (walkWeb == null) {
					log("We don't support ivy here!");
					return -1;
				}
			}
			if (!walkWeb.finished() && !walkWeb.step()) {
				walkWeb = null;
				return 0;
			}
			if (walkWeb.finished()) {
				returnTile = null;
				return random(1500, 2500);
			}
			return 800;
		}
		RSGroundItem groundItem = groundItems.getNearest(BIRDS_NEST_FILTER);
		if (groundItem != null) {
			if (groundItem.doAction("Take")) {
				return random(2500, 4000);
			}
			return 250;
		}
		RSObject ivy = objects.getNearest(IVY_OBJECT_FILTER);
		if (ivy != null) {
			try {
				if (!ivy.isOnScreen()) {
					walking.walkTileMM(ivy.getLocation());
					return 2500;
				}
				RSModel ivyModel = ivy.getModel();
				for (int i = 0; i < 5; i++) {
					if (i > 2) {
						final RSTile t = getViewTile();
						if (t != null) {
							camera.turnTo(t);
						}
					}
					final Point ivyPoint = ivyModel.getPoint();
					mouse.move(ivyPoint);
					final Timer timer = new Timer(250);
					while (timer.isRunning() && !mouse.getLocation().equals(ivyPoint)) {
						sleep(random(10, 50));
					}
					sleep(random(10, 180));
					if (mouse.getLocation().equals(ivyPoint)) {
						if (menu.doAction("Chop Ivy")) {
							ivyRecord = ivy;
							return 1500;
						}
					}
				}
			} catch (NullPointerException ignored) {
			}
		}
		return 0;
	}

	public void messageReceived(MessageEvent e) {
		if (e.getID() == MessageEvent.MESSAGE_ACTION) {
			if (e.getMessage() != null && e.getMessage().contains("chop away")) {
				ivyChopped++;
			}
		}
	}

	public void onRepaint(Graphics render) {
		if (ivyRecord != null) {
			RSModel model = ivyRecord.getModel();
			if (model != null) {
				render.setColor(new Color(0, 0, 255, 75));
				Polygon[] modelArray = model.getTriangles();
				for (Polygon modelIndex : modelArray) {
					render.fillPolygon(modelIndex);
				}
			}
		}
		long millis = runningTimer.getElapsed();
		long hours = millis / (1000 * 60 * 60);
		millis -= hours * (1000 * 60 * 60);
		long minutes = millis / (1000 * 60);
		millis -= minutes * (1000 * 60);
		long seconds = millis / 1000;
		if (startXP == -1 && game.isLoggedIn()) {
			startXP = skills.getCurrentExp(Skills.WOODCUTTING);
		}
		int xpGained = skills.getCurrentExp(Skills.WOODCUTTING) - startXP;
		int xpToLevel = skills.getExpToNextLevel(Skills.WOODCUTTING);
		float xpsec = ((float) xpGained) / (float) (seconds + (minutes * 60) + (hours * 60 * 60));
		float expperMinute = xpsec * 60;
		float expPerHour = expperMinute * 60;
		float expPerDay = expPerHour * 24;
		int daysTNL = 0, hoursTNL = 0, minsTNL = 0, secTNL = 0;
		if (xpGained > 0) {
			daysTNL = (int) Math.floor(xpToLevel / expPerDay);
			hoursTNL = (int) Math.floor(xpToLevel / expPerHour);
			minsTNL = (int) Math.floor(((xpToLevel / expPerHour) - hoursTNL) * 60);
			secTNL = (int) Math.floor(((((xpToLevel / expPerHour) - hoursTNL) * 60) - minsTNL) * 60);
		}
		render.setColor(Color.BLACK);
		render.drawRect(5, 5, 220, 68);
		render.setColor(new Color(0, 0, 0, 127));
		render.fillRect(5, 5, 220, 68);
		render.fillRect(5, 5, 220, 34);
		render.setColor(Color.white);
		render.drawString("godIvy Free (" + runningTimer.toElapsedString() + ")", 8, 17);
		render.drawString("EXP Gained [Ivy Chopped]: " + xpGained + " [" + ivyChopped + "]", 8, 32);
		render.drawString("EXP Hour: " + expPerHour, 8, 47);
		render.drawString("Time until level: " + daysTNL + ":" + hoursTNL + ":" + minsTNL + ":" + secTNL, 8, 62);
	}

	private static final int roundOrient(int orientation) {
		int i = orientation % 90;
		int off;
		if (i >= 45) {
			off = 90 - i;
		} else {
			off = 0 - i;
		}
		orientation += off;
		return orientation / 90;
	}

	private final RSTile getViewTile() {
		int orient = roundOrient(getMyPlayer().getOrientation());
		RSTile ourTile = getMyPlayer().getLocation();
		switch (orient) {
			case 0:
				return new RSTile(ourTile.getX() + 1, ourTile.getY());
			case 1:
				return new RSTile(ourTile.getX(), ourTile.getY() + 1);
			case 2:
				return new RSTile(ourTile.getX() - 1, ourTile.getY());
			case 3:
				return new RSTile(ourTile.getX(), ourTile.getY() - 1);
		}
		return null;
	}
}
