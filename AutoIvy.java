import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;

import org.rsbot.event.events.MessageEvent;
import org.rsbot.event.listeners.MessageListener;
import org.rsbot.event.listeners.PaintListener;
import org.rsbot.script.Script;
import org.rsbot.script.ScriptManifest;
import org.rsbot.script.methods.Methods;
import org.rsbot.script.methods.Skills;
import org.rsbot.script.util.Timer;
import org.rsbot.script.wrappers.RSItem;
import org.rsbot.script.wrappers.RSObject;

@ScriptManifest(authors = { "LastCoder" }, keywords = "Auto", name = "AutoIvy", version = 2.0, description = "Start next to ivy!")
public class AutoIvy extends Script implements MessageListener, PaintListener {

	private enum state {
		CHOP, SLEEP, DROP
	}

	private static final int[] IVY_ID = new int[] { 46318, 46320, 46322, 46324 };
	private static final int[] IVY_NEST_ID = new int[] { 5070, 5071, 5072,
			5073, 5074, 5075, 5076, 7413, 11966 };
	private static final Color COLOR_1 = new Color(0, 0, 0, 155);
	private static final Color COLOR_2 = new Color(0, 0, 0);
	private static final Color COLOR_3 = new Color(255, 255, 255);
	private static final BasicStroke STROKE = new BasicStroke(1);
	private static final Font FONT_1 = new Font("Arial", 0, 17);

	private static final Font FONT_2 = new Font("Arial", 0, 9);
	private long activityTime;
	private long startExp;
	private long expGained;
	private long startTime;

	private int expHour;

	private void antiBan() {
		final int random = Methods.random(1, 5);
		switch (random) {
		case 1:
			if (Methods.random(1, 25) != 1) {
				return;
			}
			mouse.move(Methods.random(10, 750), Methods.random(10, 495));
			return;
		case 2:
			if (Methods.random(1, 6) != 1) {
				return;
			}
			int angle = camera.getAngle() + Methods.random(-45, 45);
			if (angle < 0) {
				angle = Methods.random(0, 10);
			}
			if (angle > 359) {
				angle = Methods.random(0, 10);
			}
			char whichDir = 37; // left
			if (Methods.random(0, 100) < 50) {
				whichDir = 39; // right
			}
			keyboard.pressKey(whichDir);
			Methods.sleep(Methods.random(100, 500));
			keyboard.releaseKey(whichDir);
			return;
		case 3:
			if (Methods.random(1, 15) != 1) {
				return;
			}
			mouse.moveSlightly();
			return;
		default:
			return;
		}
	}

	private boolean busy() {
		return System.currentTimeMillis() - activityTime < 8000;
	}

	private state getState() {
		if (inventory.isFull()) {
			return state.DROP;
		} else {
			if (!busy()) {
				return state.CHOP;
			} else {
				return state.SLEEP;
			}
		}
	}

	@Override
	public int loop() {
		switch (getState()) {
		case DROP:
			final RSItem[] all = inventory.getItems();
			for (int i = 0; i < all.length; i++) {
				if (all[i] == null) {
					continue;
				}
				if (all[i].getID() == AutoIvy.IVY_NEST_ID[i]) {
					all[i].doAction("Drop");
				}
			}
			break;
		case CHOP:
			final RSObject ivy = objects.getNearest(AutoIvy.IVY_ID);
			if (ivy != null) {
				if (!ivy.isOnScreen()) {
					camera.turnTo(ivy);
					for (int i = 0; i < 100 && !ivy.isOnScreen(); i++) {
						Methods.sleep(20);
					}
				} else if (getMyPlayer().getAnimation() != -1) {
					activityTime = System.currentTimeMillis();
				} else {
					ivy.doAction("Chop");
				}
			}
			break;
		case SLEEP:
			Methods.sleep(20);
			antiBan();
			break;
		}
		return Methods.random(600, 1200);
	}

	@Override
	public void messageReceived(final MessageEvent e) {
		final String msg = e.getMessage();
		if (msg.contains("you")) {
			activityTime = System.currentTimeMillis();
		}
	}

	@Override
	public void onRepaint(final Graphics g1) {
		final Graphics2D g = (Graphics2D) g1;
		final long millis = System.currentTimeMillis() - startTime;
		final String time = Timer.format(millis);
		if (skills.getCurrentExp(Skills.WOODCUTTING) - startExp > 0
				&& startExp > 0) {
			expGained = skills.getCurrentExp(Skills.WOODCUTTING) - startExp;
		}
		if (expGained > 0 && millis > 0) {
			expHour = (int) (3600 * expGained / millis);
		}
		g.setColor(AutoIvy.COLOR_1);
		g.fillRect(14, 350, 474, 99);
		g.setColor(AutoIvy.COLOR_2);
		g.setStroke(AutoIvy.STROKE);
		g.drawRect(14, 350, 474, 99);
		g.setFont(AutoIvy.FONT_1);
		g.setColor(AutoIvy.COLOR_3);
		g.drawString("AutoIvy", 209, 374);
		g.setFont(AutoIvy.FONT_2);
		g.drawString("EXP/Hr: " + expHour, 18, 390);
		g.drawString("EXP Gained: " + expGained, 18, 400);
		g.drawString("Time Ran: " + time, 182, 390);
		g.drawString("Status: " + getState().toString(), 182, 400);

	}

	@Override
	public boolean onStart() {
		log("Start next to ivy!");
		startExp = skills.getCurrentExp(Skills.WOODCUTTING);
		startTime = System.currentTimeMillis();
		return game.isLoggedIn();
	}

}
