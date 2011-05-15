import org.rsbot.event.listeners.PaintListener;
import org.rsbot.script.Script;
import org.rsbot.script.ScriptManifest;
import org.rsbot.script.wrappers.*;

import java.awt.*;

/**
 * A simple Flax picker for free on RSBot.
 * Please remember paid scripts are always better!
 */

@ScriptManifest(name = "godFlax", authors = {"Timer"}, description = "Picks and banks flax.", website = "http://www.powerbot.org/vb/showthread.php?t=783724", version = 0.1, keywords = {"bank", "flax", "money", "coins"}, requiresVersion = 242)
public class godFlax extends Script implements PaintListener {
	private static final RSArea FLAX_FIELD = new RSArea(new RSTile(2737, 3437), new RSTile(2751, 3451));
	private static final RSTile BANK_TILE = new RSTile(2726, 3492, 0), FLAX_TILE = FLAX_FIELD.getCentralTile();

	private RSObject ob = null;

	@Override
	public int loop() {
		if (inventory.isFull()) {
			if (calc.distanceTo(BANK_TILE) < 3) {
				if (bank.isOpen() || bank.open()) {
					bank.depositAll();
					return 1500;
				}
			} else {
				RSWeb walkingWeb = web.getWeb(getMyPlayer().getLocation(), BANK_TILE);
				while (!walkingWeb.finished()) {
					if (!walkingWeb.step()) {
						break;
					}
				}
			}
			return 1500;
		}
		if (!FLAX_FIELD.contains(getMyPlayer().getLocation())) {
			RSWeb walkingWeb = web.getWeb(getMyPlayer().getLocation(), FLAX_TILE);
			while (!walkingWeb.finished()) {
				if (!walkingWeb.step()) {
					break;
				}
			}
			return 300;
		}
		RSObject flax = objects.getNearest(2646);
		if (flax != null) {
			if (flax.doAction("Pick")) {
				ob = flax;
				return random(800, 1800);
			}
		}
		return 0;
	}

	public void onRepaint(Graphics render) {
		if (ob != null) {
			RSModel model = ob.getModel();
			if (model != null) {
				render.setColor(new Color(0, 0, 255, 75));
				Polygon[] modelArray = model.getTriangles();
				for (Polygon modelIndex : modelArray) {
					render.fillPolygon(modelIndex);
				}
			}
		}
	}
}
