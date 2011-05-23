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
import org.rsbot.script.methods.Skills;
import org.rsbot.script.util.Filter;
import org.rsbot.script.wrappers.RSArea;
import org.rsbot.script.wrappers.RSComponent;
import org.rsbot.script.wrappers.RSInterface;
import org.rsbot.script.wrappers.RSItem;
import org.rsbot.script.wrappers.RSNPC;
import org.rsbot.script.wrappers.RSObject;
import org.rsbot.script.wrappers.RSPath;
import org.rsbot.script.wrappers.RSTile;

@ScriptManifest(authors = { "hellomot0123" }, keywords = { "Combat" }, name = "BanditKiller", description = "Start somewhere in Bandit Camp.", version = 2.1, website = "http://www.powerbot.org/vb/showthread.php?t=764939", requiresVersion = 244)
public class BanditKillerNew extends Script implements PaintListener,
		MessageListener {

	public class GUI extends javax.swing.JFrame {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private javax.swing.JCheckBox enablefood;

		private javax.swing.JCheckBox enablepotion;

		private javax.swing.JCheckBox enablepray;
		private javax.swing.JCheckBox enablespec;
		private javax.swing.JTextField foodid;
		private javax.swing.JLabel jLabel1;
		private javax.swing.JLabel jLabel10;
		private javax.swing.JLabel jLabel11;
		private javax.swing.JLabel jLabel12;
		private javax.swing.JLabel jLabel13;
		private javax.swing.JLabel jLabel14;
		private javax.swing.JLabel jLabel15;
		private javax.swing.JLabel jLabel16;
		private javax.swing.JLabel jLabel17;
		private javax.swing.JLabel jLabel18;
		private javax.swing.JLabel jLabel19;
		private javax.swing.JLabel jLabel2;
		private javax.swing.JLabel jLabel20;
		private javax.swing.JLabel jLabel21;
		private javax.swing.JLabel jLabel22;
		private javax.swing.JLabel jLabel23;
		private javax.swing.JLabel jLabel24;
		private javax.swing.JLabel jLabel25;
		private javax.swing.JLabel jLabel3;
		private javax.swing.JLabel jLabel4;
		private javax.swing.JLabel jLabel5;
		private javax.swing.JLabel jLabel8;
		private javax.swing.JLabel jLabel9;
		private javax.swing.JPanel jPanel1;
		private javax.swing.JPanel jPanel2;
		private javax.swing.JPanel jPanel3;
		private javax.swing.JPanel jPanel4;
		private javax.swing.JPanel jPanel5;
		private javax.swing.JTabbedPane jTabbedPane1;
		private javax.swing.JComboBox location;
		private javax.swing.JTextField potionquant;
		private javax.swing.JCheckBox specequipped;
		private javax.swing.JCheckBox specinventory;
		private javax.swing.JTextField specinventoryid;
		private javax.swing.JTextField specpercent;
		private javax.swing.JButton startButton;

		public GUI() {
			initComponents();
		}

		private void initComponents() {
			jTabbedPane1 = new javax.swing.JTabbedPane();
			jPanel5 = new javax.swing.JPanel();
			jLabel16 = new javax.swing.JLabel();
			jLabel17 = new javax.swing.JLabel();
			startButton = new javax.swing.JButton();
			jPanel1 = new javax.swing.JPanel();
			jLabel1 = new javax.swing.JLabel();
			jLabel2 = new javax.swing.JLabel();
			jLabel3 = new javax.swing.JLabel();
			jLabel4 = new javax.swing.JLabel();
			enablefood = new javax.swing.JCheckBox();
			jLabel5 = new javax.swing.JLabel();
			foodid = new javax.swing.JTextField();
			enablepray = new javax.swing.JCheckBox();
			jLabel24 = new javax.swing.JLabel();
			jLabel25 = new javax.swing.JLabel();
			jPanel2 = new javax.swing.JPanel();
			jLabel8 = new javax.swing.JLabel();
			location = new javax.swing.JComboBox();
			jLabel22 = new javax.swing.JLabel();
			jPanel3 = new javax.swing.JPanel();
			jLabel9 = new javax.swing.JLabel();
			jLabel10 = new javax.swing.JLabel();
			enablespec = new javax.swing.JCheckBox();
			jLabel11 = new javax.swing.JLabel();
			specequipped = new javax.swing.JCheckBox();
			jLabel12 = new javax.swing.JLabel();
			specinventory = new javax.swing.JCheckBox();
			jLabel13 = new javax.swing.JLabel();
			specinventoryid = new javax.swing.JTextField();
			jLabel14 = new javax.swing.JLabel();
			specpercent = new javax.swing.JTextField();
			jLabel15 = new javax.swing.JLabel();
			jPanel4 = new javax.swing.JPanel();
			jLabel18 = new javax.swing.JLabel();
			jLabel19 = new javax.swing.JLabel();
			enablepotion = new javax.swing.JCheckBox();
			jLabel20 = new javax.swing.JLabel();
			potionquant = new javax.swing.JTextField();
			jLabel21 = new javax.swing.JLabel();
			jLabel23 = new javax.swing.JLabel();
			setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
			jLabel16.setFont(new java.awt.Font("Tahoma", 1, 18));
			jLabel16.setText("BanditKiller v2.1");
			jLabel17.setFont(new java.awt.Font("Tahoma", 1, 14));
			jLabel17.setText("by hellomot0123");
			startButton.setText("START");
			startButton.addActionListener(new java.awt.event.ActionListener() {

				public void actionPerformed(final java.awt.event.ActionEvent evt) {
					startButtonActionPerformed(evt);
				}
			});

			final javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
			jPanel5.setLayout(jPanel5Layout);
			jPanel5Layout.setHorizontalGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel5Layout.createSequentialGroup().addGap(107, 107, 107).addComponent(jLabel16).addContainerGap(141, Short.MAX_VALUE)).addGroup(jPanel5Layout.createSequentialGroup().addGap(36, 36, 36).addComponent(jLabel17).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 62, Short.MAX_VALUE).addComponent(startButton).addGap(126, 126, 126)));
			jPanel5Layout.setVerticalGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel5Layout.createSequentialGroup().addGap(43, 43, 43).addComponent(jLabel16).addGap(81, 81, 81).addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addComponent(jLabel17).addComponent(startButton)).addContainerGap(106, Short.MAX_VALUE)));

			jTabbedPane1.addTab("Start", jPanel5);

			jLabel1.setFont(new java.awt.Font("Tahoma", 1, 18));
			jLabel1.setText("Noted Food Mode:");

			jLabel2.setFont(new java.awt.Font("Tahoma", 1, 18));
			jLabel2.setText("Prayer Mode:");

			jLabel3.setFont(new java.awt.Font("Tahoma", 1, 12));
			jLabel3.setText("Enable:");

			jLabel4.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
			jLabel4.setText("Enable:");

			jLabel5.setFont(new java.awt.Font("Tahoma", 1, 12));
			jLabel5.setText("Un-Noted Food ID:");

			jLabel24.setText("There must be noted prayer potion (4) to work.");

			jLabel25.setText("Please choose your desired quickprayers.");

			final javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
			jPanel1.setLayout(jPanel1Layout);
			jPanel1Layout.setHorizontalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addContainerGap().addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jLabel1).addGroup(jPanel1Layout.createSequentialGroup().addComponent(jLabel3).addGap(18, 18, 18).addComponent(enablefood)).addComponent(jLabel5).addComponent(foodid, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup().addComponent(jLabel2).addGap(52, 52, 52)).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup().addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addComponent(jLabel24).addGroup(jPanel1Layout.createSequentialGroup().addComponent(jLabel4).addGap(55, 55, 55).addComponent(enablepray, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)).addComponent(jLabel25)).addGap(31, 31, 31)))));
			jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addGap(30, 30, 30).addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel1).addComponent(jLabel2)).addGap(41, 41, 41).addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jLabel3).addComponent(enablefood)).addGroup(jPanel1Layout.createSequentialGroup().addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addComponent(jLabel4).addComponent(enablepray)).addGap(18, 18, 18))).addGap(31, 31, 31).addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addGroup(jPanel1Layout.createSequentialGroup().addComponent(jLabel5).addGap(18, 18, 18).addComponent(foodid, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addGap(18, 18, 18)).addGroup(jPanel1Layout.createSequentialGroup().addComponent(jLabel25).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))).addComponent(jLabel24).addContainerGap(27, Short.MAX_VALUE)));

			jTabbedPane1.addTab("Food/Pray", jPanel1);

			jLabel8.setFont(new java.awt.Font("Tahoma", 1, 18));
			jLabel8.setText("Location:");

			location.setModel(new javax.swing.DefaultComboBoxModel(new String[] {
					"Choose", "South Room" }));

			jLabel22.setText("Only supports south room for now.");

			final javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
			jPanel2.setLayout(jPanel2Layout);
			jPanel2Layout.setHorizontalGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel2Layout.createSequentialGroup().addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel2Layout.createSequentialGroup().addGap(36, 36, 36).addComponent(jLabel8)).addGroup(jPanel2Layout.createSequentialGroup().addGap(50, 50, 50).addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jLabel22).addComponent(location, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))).addContainerGap(190, Short.MAX_VALUE)));
			jPanel2Layout.setVerticalGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel2Layout.createSequentialGroup().addGap(37, 37, 37).addComponent(jLabel8).addGap(30, 30, 30).addComponent(location, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addGap(66, 66, 66).addComponent(jLabel22).addContainerGap(84, Short.MAX_VALUE)));

			jTabbedPane1.addTab("Location", jPanel2);

			jLabel9.setFont(new java.awt.Font("Tahoma", 1, 18));
			jLabel9.setText("Special Attack:");

			jLabel10.setFont(new java.awt.Font("Tahoma", 1, 12));
			jLabel10.setText("Enable:");

			jLabel11.setFont(new java.awt.Font("Tahoma", 1, 12));
			jLabel11.setText("Is Special Attack Wep already equipped? :");

			jLabel12.setFont(new java.awt.Font("Tahoma", 1, 12));
			jLabel12.setText("Is Special Attack Wep in Inventory? :");

			jLabel13.setFont(new java.awt.Font("Tahoma", 1, 12));
			jLabel13.setText("Inventory Wep ID:");

			jLabel14.setFont(new java.awt.Font("Tahoma", 1, 12));
			jLabel14.setText("How much of Special Bar needed? :");

			jLabel15.setFont(new java.awt.Font("Tahoma", 1, 12));
			jLabel15.setText("%");

			final javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
			jPanel3.setLayout(jPanel3Layout);
			jPanel3Layout.setHorizontalGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel3Layout.createSequentialGroup().addContainerGap().addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel3Layout.createSequentialGroup().addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jLabel9).addGroup(jPanel3Layout.createSequentialGroup().addComponent(jLabel10).addGap(18, 18, 18).addComponent(enablespec)).addGroup(jPanel3Layout.createSequentialGroup().addComponent(jLabel11).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 66, Short.MAX_VALUE).addComponent(specequipped)).addGroup(jPanel3Layout.createSequentialGroup().addComponent(jLabel12).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 95, Short.MAX_VALUE).addComponent(specinventory))).addContainerGap(54, Short.MAX_VALUE)).addGroup(jPanel3Layout.createSequentialGroup().addComponent(jLabel13).addGap(44, 44, 44).addComponent(specinventoryid, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE).addContainerGap()).addGroup(jPanel3Layout.createSequentialGroup().addComponent(jLabel14).addGap(18, 18, 18).addComponent(specpercent, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jLabel15).addGap(123, 123, 123)))));
			jPanel3Layout.setVerticalGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel3Layout.createSequentialGroup().addGap(21, 21, 21).addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addGroup(jPanel3Layout.createSequentialGroup().addComponent(jLabel9).addGap(20, 20, 20).addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addComponent(jLabel10).addComponent(enablespec)).addGap(18, 18, 18).addComponent(jLabel11)).addComponent(specequipped)).addGap(18, 18, 18).addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jLabel12).addComponent(specinventory)).addGap(18, 18, 18).addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jLabel13).addComponent(specinventoryid, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(22, 22, 22).addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel14).addComponent(specpercent, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel15)).addContainerGap(39, Short.MAX_VALUE)));

			jTabbedPane1.addTab("Special Attack", jPanel3);

			jLabel18.setFont(new java.awt.Font("Tahoma", 1, 18));
			jLabel18.setText("Potions:");

			jLabel19.setFont(new java.awt.Font("Tahoma", 1, 12));
			jLabel19.setText("Enable:");

			jLabel20.setFont(new java.awt.Font("Tahoma", 1, 12));
			jLabel20.setText("Un-note how many potions?");

			jLabel21.setText("Only supports combat potion for now.");

			jLabel23.setText("There must be noted combat potion (4) to work.");

			final javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
			jPanel4.setLayout(jPanel4Layout);
			jPanel4Layout.setHorizontalGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel4Layout.createSequentialGroup().addGap(23, 23, 23).addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jLabel23).addComponent(jLabel21).addGroup(jPanel4Layout.createSequentialGroup().addComponent(jLabel20).addGap(18, 18, 18).addComponent(potionquant, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)).addGroup(jPanel4Layout.createSequentialGroup().addComponent(jLabel19).addGap(18, 18, 18).addComponent(enablepotion)).addComponent(jLabel18)).addContainerGap(106, Short.MAX_VALUE)));
			jPanel4Layout.setVerticalGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel4Layout.createSequentialGroup().addGap(22, 22, 22).addComponent(jLabel18).addGap(33, 33, 33).addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jLabel19).addComponent(enablepotion)).addGap(18, 18, 18).addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel20).addComponent(potionquant, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(57, 57, 57).addComponent(jLabel21).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(jLabel23).addContainerGap(43, Short.MAX_VALUE)));

			jTabbedPane1.addTab("Potions", jPanel4);

			final javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
			getContentPane().setLayout(layout);
			layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jTabbedPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 413, Short.MAX_VALUE));
			layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE));

			pack();
		}// </editor-fold>

		private void startButtonActionPerformed(
				final java.awt.event.ActionEvent evt) {
			if (enablefood.isSelected()) {
				foodMode = true;
				foodID = Integer.parseInt(foodid.getText());
			}
			if (enablepotion.isSelected()) {
				potMode = true;
				potQuant = Integer.parseInt(potionquant.getText());
			}
			if (enablepray.isSelected()) {
				prayMode = true;
			}
			if (enablespec.isSelected()) {
				specMode = true;
				if (specequipped.isSelected()) {
					specEquip = true;
				}
				if (specinventory.isSelected()) {
					specInven = true;
					wepID = Integer.parseInt(specinventoryid.getText());
				}
				specPercent = Integer.parseInt(specpercent.getText());
			}
			if (location.getSelectedItem() == "South Room") {
				fightAt = "SR";
			}
			dispose();
			startScript = true;
		}
	}

	RSItem noted, shield, weapon, noted2;
	RSTile shop0 = new RSTile(3175, 2987), shop1 = new RSTile(3175, 2988),
			shop2 = new RSTile(3175, 2989), shop3 = new RSTile(3176, 2985),
			shop4 = new RSTile(3176, 2986), shop5 = new RSTile(3176, 2987),
			shop6 = new RSTile(3177, 2985), shop7 = new RSTile(3177, 2986),
			shop8 = new RSTile(3177, 2987), shop9 = new RSTile(3178, 2986),
			shop = new RSTile(3176, 2987),
			outsideroom = new RSTile(3172, 2977),
			insideroom = new RSTile(3172, 2976),
			fight = new RSTile(3176, 2972);
	RSTile[] shopTiles = { shop0, shop1, shop2, shop3, shop4, shop5, shop6,
			shop7, shop8, shop9 };
	public int foodID, notedID, temp, freespaces, specPercent, wepID,
			howManySpecs, potQuant;
	public int shopkeeperID = 1917, opened = 1529, closed = 1528, temp2;
	public int pray4 = 2434, pray3 = 139, pray2 = 141, pray1 = 143,
			notedPrayID, notedPotID, pot4 = 9739, pot3 = 9741, pot2 = 9743,
			pot1 = 9745;
	public boolean foodMode = false, prayMode = false, specEquip = false,
			specInven = false, specWepEquipped = false, waiting = false,
			specMode = false, potMode = false, startScript = false,
			stop = false;
	public String state = "Loading", monster = "Bandit", fightAt;
	public long startTime = 0, runTime = 0, hours = 0, minutes = 0,
			seconds = 0, nextAntiban = 0, timer = 0;
	public int startXPAttack = 0, startXPDefence = 0, startXPStrength = 0,
			startXPHP = 0, startXPRange = 0, startXPMagic = 0,
			levelsGained = 0;
	private final Color color1 = new Color(204, 0, 255, 44);
	private final Color color2 = new Color(0, 0, 0);
	private final Color color3 = new Color(255, 255, 255);
	private final BasicStroke stroke1 = new BasicStroke(2);
	private final Font font1 = new Font("Arial", 0, 18);

	private final Font font2 = new Font("Arial", 0, 10);

	private final Filter<RSNPC> monsterFilter = new Filter<RSNPC>() {

		@Override
		public boolean accept(final RSNPC t) {
			if (correctMonster(t) && t.getHPPercent() != 0 && t.isValid()
					&& t.getAnimation() != 836 && insideSouthHouse(t)) {
				return true;
			} else {
				return false;
			}
		}
	};

	public boolean aggressiveBandit() {

		if (getMyPlayer().isInCombat()) {
			return true;

		}
		return false;
	}

	private void antiban() {
		if (System.currentTimeMillis() > nextAntiban) {
			nextAntiban = System.currentTimeMillis() + random(2000, 30000);
		} else {
			return;
		}
		final Thread mouseThread = new Thread() {

			@Override
			public void run() {
				switch (random(0, 5)) {
				case 0:
					mouse.moveOffScreen();
					break;
				case 1:
					mouse.move(random(0, game.getWidth()), random(0, game.getHeight()));
					break;
				case 2:
					mouse.move(random(0, game.getWidth()), random(0, game.getHeight()));
					break;
				}
			}
		};
		final Thread keyThread = new Thread() {

			@Override
			public void run() {
				switch (random(0, 4)) {
				case 0:
					camera.setAngle(camera.getAngle() + random(-100, 100));
					break;
				case 1:
					camera.setAngle(camera.getAngle() + random(-100, 100));
					break;
				case 2:
					camera.setAngle(camera.getAngle() + random(-100, 100));
					break;
				}
			}
		};

		if (random(0, 2) == 0) {
			keyThread.start();
			sleep(random(0, 600));
			mouseThread.start();

		} else {
			mouseThread.start();
			sleep(random(0, 600));
			keyThread.start();
		}
		while (keyThread.isAlive() || mouseThread.isAlive()) {
			sleep(random(30, 100));
		}
	}

	public boolean atShop() {
		final RSArea area1 = new RSArea(shopTiles);
		if (area1.contains(getMyPlayer().getLocation())) {
			return true;
		}
		return false;
	}

	public boolean atShop(final RSNPC i) {
		final RSArea area1 = new RSArea(shopTiles);
		if (area1.contains(i.getLocation())) {
			return true;
		}
		return false;
	}

	public boolean buy(final int id) {
		if (store.isOpen()) {
			if (store.buy(id, 50)) {
				sleep(2000);
				return true;
			} else {
				return false;
			}
		}
		return false;
	}

	public boolean canWeSpecial() {
		if (combat.getSpecialBarEnergy() >= specPercent) {
			return true;
		} else {
			return false;
		}
	}

	public void checkRun() {
		if (!walking.isRunEnabled() && walking.getEnergy() > 30) {
			walking.setRun(true);
		}
	}

	private boolean correctMonster(final RSNPC t) {
		if (t.getName().equals(monster)) {
			return true;
		} else {
			return false;
		}
	}

	public void drinkPot() {
		if (potMode) {
			if (needToPot()) {
				if (inventory.getItem(pot1) != null) {
					final RSItem praypot = inventory.getItem(pot1);
					praypot.doClick(true);
					sleep(800);
					return;
				}
				if (inventory.getItem(pot2) != null) {
					final RSItem praypot = inventory.getItem(pot2);
					praypot.doClick(true);
					sleep(800);
					return;
				}
				if (inventory.getItem(pot3) != null) {
					final RSItem praypot = inventory.getItem(pot3);
					praypot.doClick(true);
					sleep(800);
					return;
				}
				if (inventory.getItem(pot4) != null) {
					final RSItem praypot = inventory.getItem(pot4);
					praypot.doClick(true);
					sleep(800);
					return;
				}
			}
		}
	}

	public void DrinkPray() {
		if (needPrayPot() == true) {
			if (inventory.getItem(pray1) != null) {
				final RSItem praypot = inventory.getItem(pray1);
				praypot.doClick(true);
				sleep(800);
				return;
			}
			if (inventory.getItem(pray2) != null) {
				final RSItem praypot = inventory.getItem(pray2);
				praypot.doClick(true);
				sleep(800);
				return;
			}
			if (inventory.getItem(pray3) != null) {
				final RSItem praypot = inventory.getItem(pray3);
				praypot.doClick(true);
				sleep(800);
				return;
			}
			if (inventory.getItem(pray4) != null) {
				final RSItem praypot = inventory.getItem(pray4);
				praypot.doClick(true);
				sleep(800);
				return;
			}
		}
	}

	public void dropVials() {
		for (final RSItem vial : inventory.getItems(229)) {
			vial.doAction("Drop");
		}
	}

	public void Eat() {
		if (needEat()) {
			final RSItem food = inventory.getItem(foodID);
			food.doClick(true);
			sleep(800);
		}
	}

	public void equipGear() {
		game.openTab(4, true);
		while (inventory.contains(weapon.getID())) {
			inventory.getItem(weapon.getID()).doClick(true);
			sleep(1000);
		}
		if (shield != null) {
			if (inventory.contains(shield.getID())) {
				inventory.getItem(shield.getID()).doClick(true);
				sleep(1000);
			}
		}
		specWepEquipped = false;
	}

	public void Fight() {
		state = "Fighting!";
		if (foodMode) {
			if (!needEat()) {
				if (underAttack()) {
					sleep(100);
				} else {
					final RSNPC target = npcs.getNearest(monsterFilter);
					if (target != null) {
						if (target.isOnScreen() == true) {
							target.doAction("Attack " + target.getName());
							sleep(1000);
						} else {
							final RSPath zomg4 = walking.getPath(target.getLocation());
							zomg4.traverse();
						}
					}
				}
			} else {
				Eat();
			}
		}
		if (prayMode) {
			if (interfaces.get(749).getComponent(1).containsAction("Turn quick prayers on")) {
				interfaces.get(749).getComponent(1).doAction("Turn quick prayers on");
			}
			if (needPrayPot() == false) {
				if (underAttack() == true) {
					sleep(100);
				} else {
					final RSNPC target = npcs.getNearest(monsterFilter);
					if (target != null) {
						if (target.isOnScreen() == true) {
							target.doAction("Attack " + target.getName());
							sleep(1000);
						} else {
							final RSPath zomg4 = walking.getPath(target.getLocation());
							zomg4.traverse();
						}
					}
				}
			} else {
				DrinkPray();
			}
		}
	}

	public int getItemStacksize(final int id) {
		if (store.isOpen()) {
			final RSComponent[] items = interfaces.get(620).getComponent(25).getComponents();
			for (final RSComponent i : items) {
				if (i.getComponentID() == id) {
					return i.getComponentStackSize();
				}
			}
		}
		return 0;
	}

	public boolean insideSouthHouse() {
		final RSArea area1 = new RSArea(new RSTile(3172, 2970), new RSTile(3176, 2972));
		final RSArea area2 = new RSArea(new RSTile(3169, 2973), new RSTile(3176, 2976));
		if (area1.contains(getMyPlayer().getLocation())
				|| area2.contains(getMyPlayer().getLocation())) {
			return true;
		}
		return false;
	}

	public boolean insideSouthHouse(final RSNPC i) {
		final RSArea area1 = new RSArea(new RSTile(3172, 2970), new RSTile(3176, 2972));
		final RSArea area2 = new RSArea(new RSTile(3169, 2973), new RSTile(3176, 2976));
		if (area1.contains(i.getLocation()) || area2.contains(i.getLocation())) {
			return true;
		}
		return false;
	}

	public boolean isCurtainOpen() {
		if (objects.getNearest(opened) != null
				&& calc.distanceTo(objects.getNearest(opened)) < 3) {
			return true;
		}
		return false;
	}

	@Override
	public int loop() {
		try {
			stopScript();
			antiban();
			checkRun();
			dropVials();
			if (insideSouthHouse()) {
				if (needMoreFood() || needMorePray() || needMorePots()) {
					combat.setAutoRetaliate(false);
					walkToDoor();
					if (isCurtainOpen()) {
						walkToShop();
					} else {
						final RSObject closedDoor = objects.getNearest(closed);
						closedDoor.doAction("Open");
						sleep(500);
					}
				} else {
					specialAttack();
					drinkPot();
					Fight();
				}
			} else {
				if (!inventory.contains(14664)) {
					if (needMoreFood() || needMorePray() || needMorePots()) {
						walkToShop();
						shopping();
					} else {
						temp = 0;
						temp2 = 0;
						walkToRoom();
						combat.setAutoRetaliate(true);
						if (isCurtainOpen()) {
							walkToFight();
						} else {
							final RSObject closedDoor = objects.getNearest(closed);
							closedDoor.doAction("Open");
							sleep(500);
						}
					}
				} else {
					if (store.isOpen()) {
						store.close();
					}
				}
			}
			return 100;
		} catch (final Exception e) {
			return 1;
		}
	}

	public void messageReceived(final MessageEvent me) {
		if (me.getMessage().toLowerCase().contains("just advanced a")) {
			levelsGained++;
		}
		if (me.getMessage().toLowerCase().contains("listen, just hop")) {
			stop = true;
		}
	}

	public boolean needEat() {
		if (combat.getHealth() < 60) {
			return true;
		}
		return false;
	}

	public boolean needMoreFood() {
		if (foodMode) {
			if (inventory.getCount(foodID) < 3) {
				return true;
			}
		}
		return false;
	}

	public boolean needMorePots() {
		if (potMode) {
			if (inventory.getCount(pot1) < 1 && inventory.getCount(pot2) < 1
					&& inventory.getCount(pot3) < 1
					&& inventory.getCount(pot4) < 1) {
				if (28 - inventory.getCount() >= potQuant) {
					return true;
				}
			}
		}
		return false;
	}

	public boolean needMorePray() {
		if (prayMode) {
			if (inventory.getCount(pray4) < 3) {
				return true;
			}
		}
		return false;
	}

	public boolean needPrayPot() {
		if (Integer.parseInt(interfaces.get(749).getComponent(4).getText())
				* 100 / skills.getRealLevel(5) < 60) {
			return true;
		}
		return false;
	}

	public boolean needToPot() {
		if (skills.getCurrentLevel(Skills.ATTACK) == skills.getRealLevel(Skills.ATTACK)
				|| skills.getCurrentLevel(Skills.STRENGTH) == skills.getRealLevel(Skills.STRENGTH)) {
			return true;
		}
		return false;
	}

	@Override
	public void onRepaint(final Graphics g1) {
		final Graphics2D g = (Graphics2D) g1;
		runTime = System.currentTimeMillis() - startTime;
		hours = runTime / (1000 * 60 * 60);
		runTime -= hours * 1000 * 60 * 60;
		minutes = runTime / (1000 * 60);
		runTime -= minutes * 1000 * 60;
		seconds = runTime / 1000;
		int attackPerHour = 0;
		final int XPChangeAttack = skills.getCurrentExp(0) - startXPAttack;
		final int XPChangeDefence = skills.getCurrentExp(1) - startXPDefence;
		int defencePerHour = 0;
		final int XPChangeMagic = skills.getCurrentExp(6) - startXPMagic;
		int magicPerHour = 0;
		final int XPChangeStrength = skills.getCurrentExp(2) - startXPStrength;
		int strengthPerHour = 0;
		final int XPChangeHP = skills.getCurrentExp(3) - startXPHP;
		int hpPerHour = 0;
		final int XPChangeRange = skills.getCurrentExp(4) - startXPRange;
		int rangePerHour = 0;
		attackPerHour = (int) (XPChangeAttack * 3600000D / (System.currentTimeMillis() - startTime));
		strengthPerHour = (int) (XPChangeStrength * 3600000D / (System.currentTimeMillis() - startTime));
		defencePerHour = (int) (XPChangeDefence * 3600000D / (System.currentTimeMillis() - startTime));
		hpPerHour = (int) (XPChangeHP * 3600000D / (System.currentTimeMillis() - startTime));
		rangePerHour = (int) (XPChangeRange * 3600000D / (System.currentTimeMillis() - startTime));
		magicPerHour = (int) (XPChangeMagic * 3600000D / (System.currentTimeMillis() - startTime));
		g.setColor(color1);
		g.fillRoundRect(546, 205, 194, 262, 16, 16);
		g.setColor(color2);
		g.setStroke(stroke1);
		g.drawRoundRect(546, 205, 194, 262, 16, 16);
		g.setFont(font1);
		g.setColor(color3);
		g.drawString("BanditKiller v2.1", 550, 225);
		g.setFont(font2);
		g.drawString("by hellomot0123", 550, 235);
		g.drawString("Time Running: " + hours + ":" + minutes + ":" + seconds, 550, 250);
		g.drawString("Srength XP gained : " + XPChangeStrength, 550, 265);
		g.drawString("Strength XP/HR : " + strengthPerHour, 550, 280);
		g.drawString("Attack XP gained : " + XPChangeAttack, 550, 295);
		g.drawString("Attack XP/HR : " + attackPerHour, 550, 310);
		g.drawString("Defence XP gained : " + XPChangeDefence, 550, 325);
		g.drawString("Defence XP/HR : " + defencePerHour, 550, 340);
		g.drawString("Ranged XP gained : " + XPChangeRange, 550, 355);
		g.drawString("Ranged XP/HR : " + rangePerHour, 550, 370);
		g.drawString("Magic XP gained : " + XPChangeMagic, 550, 385);
		g.drawString("Magic XP/HR : " + magicPerHour, 550, 400);
		g.drawString("HP XP gained : " + XPChangeHP, 550, 415);
		g.drawString("HP XP/HR : " + hpPerHour, 550, 430);
		g.drawString("Total levels gained: " + levelsGained, 550, 445);
		g.drawString("Status: " + state, 550, 460);
	}

	@Override
	public boolean onStart() {
		final GUI gui = new GUI();
		gui.setVisible(true);
		while (!startScript) {
			game.sleep(100);
		}
		startTime = System.currentTimeMillis();
		startXPAttack = skills.getCurrentExp(0);
		startXPDefence = skills.getCurrentExp(1);
		startXPStrength = skills.getCurrentExp(2);
		startXPHP = skills.getCurrentExp(3);
		startXPRange = skills.getCurrentExp(4);
		startXPMagic = skills.getCurrentExp(6);
		notedID = foodID + 1;
		notedPrayID = pray4 + 1;
		notedPotID = pot4 + 1;
		shield = equipment.getItem(23);
		weapon = equipment.getItem(17);
		return true;
	}

	public void openShop() {
		final RSNPC shopkeeper = npcs.getNearest(shopkeeperID);
		shopkeeper.doAction("Trade");
	}

	public void sell() {
		if (interfaces.get(620).getComponent(25).isValid()) {
			if (foodMode) {
				noted = inventory.getItem(notedID);
				temp = getItemStacksize(foodID);
			}
			if (prayMode) {
				noted = inventory.getItem(notedPrayID);
				temp = getItemStacksize(pray4);
			}
			if (potMode) {
				noted2 = inventory.getItem(notedPotID);
				temp2 = getItemStacksize(pot4);
			}
		}
		if (potMode && needMorePots()) {
			if (temp2 < potQuant) {
				noted2.doAction("Sell 1");
				sleep(1000);
				return;
			}
			return;
		}
		if (prayMode && needMorePray() || foodMode && needMoreFood()) {
			if (freespaces - temp >= 0) {
				if (freespaces - temp >= 10) {
					noted.doAction("Sell 10");
					sleep(1000);
					return;
				}
				if (freespaces - temp >= 5) {
					noted.doAction("Sell 5");
					sleep(1000);
					return;
				}
				if (freespaces - temp >= 1) {
					noted.doAction("Sell 1");
					sleep(1000);
					return;
				}
			}
		}
	}

	public void shopping() {
		state = "Shopping!";
		if (aggressiveBandit()) {
			while (aggressiveBandit()) {
				sleep(100);
				state = "Killing bandits in tent!";
				combat.setAutoRetaliate(true);
			}
			combat.setAutoRetaliate(false);
		}
		freespaces = 28 - inventory.getCount();
		if (store.isOpen()) {
			if (needMorePots()) {
				if (temp2 < potQuant) {
					sell();
				} else {
					buy(pot4);
				}
				return;
			}
			if (needMoreFood()) {
				if (freespaces - temp > 0) {
					sell();
				} else {
					buy(foodID);
				}
				return;
			}
			if (needMorePray()) {
				if (freespaces - temp > 0) {
					sell();
				} else {
					buy(pray4);
				}
				return;
			}
		} else {
			openShop();
		}
	}

	public void spec() {
		if (specEquip) {
			game.openTab(0, true);
			final RSInterface inter = interfaces.get(884);
			final RSComponent comp = inter.getComponent(4);
			mouse.click(comp.getCenter(), true);
			sleep(500);

		} else {
			if (!specWepEquipped) {
				game.openTab(4, true);
				final RSItem specwep = inventory.getItem(wepID);
				if (inventory.contains(specwep.getID())) {
					specwep.doAction("Wield");
					sleep(1000);
				}
				specWepEquipped = true;
			}
			game.openTab(0, true);
			final RSInterface inter = interfaces.get(884);
			final RSComponent comp = inter.getComponent(4);
			mouse.click(comp.getCenter(), true);
			sleep(500);
		}
	}

	public void specialAttack() {
		if (specMode) {
			if (canWeSpecial()) {
				if (waiting && System.currentTimeMillis() - timer < 15000) {
					if (timer == 0) {
						timer = System.currentTimeMillis();
					}
					if (combat.getSpecialBarEnergy() / specPercent != howManySpecs - 1) {
						sleep(100);
					} else {
						waiting = false;
						timer = 0;
					}
				} else {
					timer = 0;
					howManySpecs = combat.getSpecialBarEnergy() / specPercent;
					if (howManySpecs > 0) {
						spec();
						waiting = true;
					}
				}
			} else {
				if (specInven) {
					equipGear();
				}
				waiting = false;
			}
		}
	}

	@Override
	public void stopScript() {
		if (stop) {
			game.logout(true);
		}
	}

	public boolean underAttack() {
		if (getMyPlayer().getInteracting() != null) {
			return true;
		}
		return false;
	}

	public void walkToDoor() {
		while (calc.distanceTo(insideroom) > 3) {
			state = "Going to Shop!";
			final RSPath path1 = walking.getPath(insideroom);
			path1.traverse();
			sleep(1500);
		}
	}

	public void walkToFight() {
		while (calc.distanceTo(fight) > 2) {
			state = "Going to South Room!";
			final RSPath path1 = walking.getPath(fight);
			path1.traverse();
			sleep(1500);
		}
	}

	public void walkToRoom() {
		while (calc.distanceTo(outsideroom) > 3) {
			state = "Going to South Room!";
			final RSPath path1 = walking.getPath(outsideroom);
			path1.traverse();
			sleep(1500);
		}
	}

	public void walkToShop() {
		while (calc.distanceTo(shop) > 2) {
			state = "Going to Shop!";
			final RSPath path1 = walking.getPath(shop);
			path1.traverse();
			sleep(1500);
		}
	}
}
