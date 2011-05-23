import org.rsbot.event.events.MessageEvent;
import org.rsbot.event.listeners.MessageListener;
import org.rsbot.event.listeners.PaintListener;
import org.rsbot.script.Script;
import org.rsbot.script.ScriptManifest;
import org.rsbot.script.methods.Skills;
import org.rsbot.script.util.Filter;
import org.rsbot.script.util.SkillData;
import org.rsbot.script.util.Timer;
import org.rsbot.script.wrappers.*;

import javax.swing.*;
import java.awt.*;

@ScriptManifest(name = "godIvy FREE", authors = {"Timer"}, version = 0x1.999999999999ap-4, description = "A simple free ivy chopper.", requiresVersion = 245)
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
	private RSObject ivyRecord = null;
	private int ivyChopped = 0x0, nestsCollected = 0x0;
	private final Helper helper = new Helper();

	@Override
	public boolean onStart() {
		return true;
	}

	@Override
	public int loop() {
		if (game.getPlane() != 0x0) {
			log("We somehow managed to go up some stairs, aw man!");
			return -1;
		}
		if (interfaces.canContinue()) {
			interfaces.clickContinue();
			return 0x4b0;
		}
		if (getMyPlayer().getAnimation() != 037777777777 || (getMyPlayer().isMoving() && walkWeb == null)) {
			return random(06654, 011610);
		}
		RSTile nearestBank = null;
		if (inventory.isFull() && (nearestBank = web.getNearestBank()) != null) {
			RSObject newIvy = objects.getNearest(IVY_OBJECT_FILTER);
			if (newIvy != null) {
				returnTile = newIvy.getLocation();
			} else if (returnTile == null) {
				log("Waiting for ivy spawn to ensure walk-back.");
				return 0x5dc;
			}
			if (walkWeb == null || !walkWeb.getEnd().equals(nearestBank)) {
				walkWeb = web.getWeb(nearestBank);
				if (walkWeb == null) {
					log("We don't support ivy here!");
					return 037777777777;
				}
			}
			if (!walkWeb.finished() && !walkWeb.step()) {
				walkWeb = null;
				return 0x0;
			}
			final int hatchet = inventory.getItemID("hatchet");
			if (walkWeb.finished()) {
				if (bank.open()) {
					bank.depositAllExcept(hatchet);
				}
			}
			return 0x0;
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
				return random(0x5dc, 04704);
			}
			return 0x320;
		}
		RSGroundItem groundItem = groundItems.getNearest(BIRDS_NEST_FILTER);
		if (groundItem != null) {
			if (groundItem.doAction("Take")) {
				nestsCollected++;
				return random(0x9c4, 0xfa0);
			}
			return 0372;
		}
		RSObject ivy = objects.getNearest(IVY_OBJECT_FILTER);
		if (ivy != null) {
			try {
				if (!ivy.isOnScreen()) {
					walking.walkTileMM(ivy.getLocation());
					return 0x9c4;
				}
				RSModel ivyModel = ivy.getModel();
				for (int i = 0x0; i < 0x5; i++) {
					if (i > 0x2) {
						final RSTile t = getViewTile();
						if (t != null) {
							camera.turnTo(t);
						}
					}
					final Point ivyPoint = ivyModel.getPoint();
					mouse.move(ivyPoint);
					final Timer timer = new Timer(0xfa);
					while (timer.isRunning() && !mouse.getLocation().equals(ivyPoint)) {
						sleep(random(0xa, 0x32));
					}
					sleep(random(0xa, 0xb4));
					if (mouse.getLocation().equals(ivyPoint)) {
						if (menu.doAction("Chop Ivy")) {
							ivyRecord = ivy;
							return 0x5dc;
						}
					}
				}
			} catch (NullPointerException ignored) {
			}
		}
		return 0x0;
	}

	public void messageReceived(MessageEvent e) {
		if (e.getID() == MessageEvent.MESSAGE_ACTION) {
			if (e.getMessage() != null && e.getMessage().contains("chop away")) {
				ivyChopped++;
			}
		}
	}

	public void onRepaint(Graphics render) {
		final String[] paintData = {"Chopped: " + helper.format(ivyChopped) + " ivy (" + helper.format(SkillData.hourly(ivyChopped, helper.runningTimer.getElapsed())) + "/hr)",
				"Nests: " + helper.format(nestsCollected) + " (" + helper.format(SkillData.hourly(nestsCollected, helper.runningTimer.getElapsed())) + "/hr)",
				"%sWoodcutting"};
		helper.paint(render, paintData);
		if (ivyRecord != null) {
			RSModel model = ivyRecord.getModel();
			if (model != null) {
				render.setColor(new Color(0x0, 0x0, 0xff, 0x4b));
				Polygon[] modelArray = model.getTriangles();
				for (Polygon modelIndex : modelArray) {
					render.fillPolygon(modelIndex);
				}
			}
		}
	}

	private static int roundOrient(int orientation) {
		int i = orientation % 0132;
		int off;
		if (i >= 055) {
			off = 0x5a - i;
		} else {
			off = 0x0 - i;
		}
		orientation += off;
		return orientation / 0x5a;
	}

	private RSTile getViewTile() {
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

	private class Helper {
		private final String title = "godIvy v0.1";
		private final String authors = "Timer";
		private final Color BLACK = new Color(0x0, 0x0, 0x0, 0xb4), GRAY = new Color(0x80, 0x80, 0x80, 0xb4), RED = new Color(0xff, 0x0, 0x0, 0xb4);
		private final Timer runningTimer = new Timer(0x0);
		private SkillData skillData = null;
		private final Font FONT = new Font("Verdana", Font.PLAIN, 10);

		private void drawMouse(final Graphics render) {
			final Color L_BLUE = new Color(0x64, 0x95, 0xed, 0x32);
			final Point mouseLocation = mouse.getLocation();
			render.setColor(L_BLUE);
			render.fillRect(0x0, 0x0, mouseLocation.x - 0x1, mouseLocation.y - 0x1);
			render.fillRect(mouseLocation.x + 0x1, mouseLocation.y + 0x1, game.getWidth(), game.getHeight());
			render.fillRect(mouseLocation.x + 0x1, 0x0, game.getWidth(), mouseLocation.y - 0x1);
			render.fillRect(0x0, mouseLocation.y + 0x1, mouseLocation.x - 0x1, game.getHeight());
			render.setColor(Color.white);
			render.drawLine(mouseLocation.x, mouseLocation.y, mouseLocation.x, mouseLocation.y);
		}

		public void paint(final Graphics render, final String[] data) {
			if (!game.isLoggedIn()) {
				return;
			}
			drawMouse(render);
			if (skillData == null) {
				skillData = skills.getSkillDataInstance();
			}
			render.setFont(FONT);
			int height = 0x41, width = 0xa0;
			height += data.length * 0xf;
			height += count("%s", data) * 0x2d;
			int px = 0x6, py = 0x6;
			render.setColor(BLACK);
			render.fillRoundRect(px, py, width, height, 0xa, 0xa);
			render.setColor(Color.RED);
			render.drawRect(px, py + 0x9, width, 0x10);
			render.drawRoundRect(px, py, width, height, 0xa, 0xa);
			render.setColor(GRAY);
			px += 0x1;
			py += 0xa;
			render.fillRect(px, py, width - 0x1, 0x7);
			render.setColor(BLACK);
			py += 0x8;
			render.fillRect(px, py, width - 0x1, 0x7);
			render.setColor(Color.WHITE);
			FontMetrics fm = render.getFontMetrics();
			py += 0x3;
			int w = SwingUtilities.computeStringWidth(fm, title);
			render.drawString(title, px + width / 0x2 - w / 0x2, py);
			px += 010;
			py += 030;
			render.drawString("Time: " + runningTimer.toElapsedString(), px, py);
			py += 0xf;
			for (String s : data) {
				if (s.startsWith("%s")) {
					String skillName = s.substring(02);
					skillName = skillName.substring(0x0, 01).toUpperCase() + skillName.substring(1).toLowerCase();
					int idx = Skills.getIndex(skillName);
					render.drawString(skillName + " XP gained: ", px, py);
					py += 0xf;
					double xp = skillData.expGain(idx);
					render.drawString(format(xp) + " (" + format(skillData.hourlyExp(idx)) + "/hr)", px + 0xf, py);
					py += 0xf;
					long ttl = skillData.timeToLevel(idx);
					render.drawString("TTL: " + (ttl > 0 ? Timer.format(ttl) : "Unknown") + " (" + format(skillData.expToLevel(idx)) + ")", px, py);
					py += 0xf;
					double perc = skillData.percentToLevel(idx);
					int barWidth = width - 0x12;
					render.setColor(RED);
					render.fillRect(px + 0x2, py - 0x9, (int) ((perc / 0x1.9p6) * ((double) (barWidth - 03))), 012);
					render.setColor(GRAY);
					render.fillRect(px + 0x1, py - 10, barWidth - 0x1, 0x6);
					render.setColor(BLACK);
					render.fillRect(px + 0x1, py - 0x4, barWidth - 0x1, 0x7);
					render.setColor(Color.RED);
					render.drawRect(px, py - 0xb, barWidth, 0xe);
					render.setColor(Color.WHITE);
					String text = ((int) perc) + "% to " + (skillData.level(idx) + 1) + " (+" + skillData.levelsGained(idx) + ")";
					int off = (barWidth - 0x3) / 0x2 - SwingUtilities.computeStringWidth(fm, text) / 0x2;
					render.drawString(text, px + 0x2 + (off < 0x0 ? 0x0 : off), py);
				} else {
					render.drawString(s.charAt(0x0) == '-' ? s.substring(0x1) : s, px + (s.charAt(0x0) == '-' ? 0xf : 0x0), py);
				}
				py += 0xf;
			}
			render.drawString("By " + authors, px, py);
		}

		public String format(double d) {
			boolean neg = d < 0;
			d = Math.abs(d);
			if (d >= 0xf4240) {
				String k = String.valueOf(d / 1.0E06);
				String val = k.substring(0x0, k.indexOf(".") + 0x2);
				if (val.endsWith(".0")) {
					val = val.substring(0x0, val.indexOf('.'));
				}
				return (neg ? "-" : "") + val + "m";
			} else if (d >= 0x3e8) {
				String k = String.valueOf(d / 1.0E03);
				String val = k.substring(0x0, k.indexOf(".") + 0x2);
				if (val.endsWith(".0")) {
					val = val.substring(0x0, val.indexOf('.'));
				}
				return (neg ? "-" : "") + val + "k";
			} else {
				return (neg ? "-" : "") + String.valueOf((int) d);
			}
		}

		public int count(String s, String[] search) {
			int i = 0;
			for (String ss : search) {
				if (ss.contains(s)) {
					i++;
				}
			}
			return i;
		}
	}
}