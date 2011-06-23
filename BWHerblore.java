import org.rsbot.script.Script;
import org.rsbot.script.ScriptManifest;
import org.rsbot.script.methods.Game;
import org.rsbot.script.methods.Skills;
import org.rsbot.script.wrappers.RSComponent;
import org.rsbot.script.wrappers.RSItem;
import org.rsbot.event.events.*;
import org.rsbot.event.listeners.*;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.net.URL;

@ScriptManifest(authors = "BlackWood", name = "Ultra Herblore", version = 2.2, description = "Herblore Done Right!")
public class UltraHerblore extends Script implements PaintListener, MessageListener, MouseListener {
	
	long curTime = System.currentTimeMillis();

	boolean isPaintShowing = true;
	boolean StartedScript = false;
	boolean HasItemIDs = false;
	boolean canLoop = true;
	
	int VialID = 227;
	int CleanID;
	int UnfID;
	int IngredientID;
	int CompleteID;
	int GrimyID;
	int OutcomeID;
	int SwampTarID = 1939;
	int Mortar = 233;
	
	int StartEXP = 0;
	int EXPGained = 0;
	int Made = 0;
	
	long Duration;
	String Role;

	boolean Unfs = false;
	
	boolean Completes = false;
	
	boolean FullCompletes = false;
	
	boolean Cleaning = false;
	
	boolean Extremes = false;
	
	boolean Taring = false;
	
	UltraHerbloreGUI gui;
	
	PotionStats Chosen;
	ExtremeStats EChosen;
	TarStats TChosen;
	
	// Order: Grimy, Clean, Unf, Ingredient, Complete
	enum PotionStats {
		ATTACK(199, 249, 91, 221, 121),
		ANTIPOISON(201, 251, 93, 235, 175),
		STRENGTH(203, 253, 95, 225, 115),
		SERUM207(203, 253, 95, 592, 3410),
		STATRESTORE(205, 255, 97, 223, 127),
		ENERGY(205, 255, 97, 1975, 3010),
		DEFENCE(207, 257, 99, 239, 133),
		AGILITY(3049, 2998, 3002, 2152, 3034),
		COMBAT(205, 255, 97, 9736, 9741),
		PRAYER(207, 257, 99, 231, 139),
		SUMMONING(12174, 12172, 12181, 12109, 12142),
		SUPERATTACK(209, 259, 101, 221, 145),
		SUPERANTIPOISON(209, 259, 101, 235, 181),
		FISHING(211, 261, 103, 231, 151),
		SUPERENERGY(211, 261, 103, 2970, 3018),
		HUNTER(211, 261, 103, 10111, 10000),
		SUPERSTRENGTH(213, 263, 105, 225, 157),
		FLETCHING(14836, 14854, 14856, 11525, 14848),
		WEAPONPOISON(213, 263, 105, 241, 187),
		SUPERRESTORE(3051, 3000, 3004, 223, 3026),
		SUPERDEFENCE(215, 265, 107, 239, 163),
		ANTIFIRE(2485, 2481, 2483, 241, 2454),
		RANGING(217, 267, 109, 245, 169),
		MAGIC(2485, 2481, 2483, 3138, 3042),
		ZAMORAK(219, 269, 111, 247, 189),
		SARADOMIN(3049, 2998, 3002, 6693, 6687);
		
		private int GrimyID;
		private int CleanID;
		private int UnfID;
		private int IngredientID;
		private int CompleteID;

		private PotionStats(int GrimyID, int CleanID, int UnfID, int IngredientID, int CompleteID) {
			this.GrimyID = GrimyID;
			this.CleanID = CleanID;
			this.UnfID = UnfID;
			this.IngredientID = IngredientID;
			this.CompleteID = CompleteID;
		}	
	}
	
	// Order: Complete, Ingredient, Outcome (Finished, Product, etc.)
	enum ExtremeStats {
		ATTACK(145, 261, 15309),
		STRENGTH(157, 267, 15313),
		DEFENCE(163, 2481, 15317),
		MAGIC(3042, 9594, 15321),
		RANGING(169, 12539, 15325);
		
		private int CompleteID;
		private int IngredientID;
		private int OutcomeID;
		
		private ExtremeStats(int CompleteID, int IngredientID, int OutcomeID) {
			this.CompleteID = CompleteID;
			this.IngredientID = IngredientID;
			this.OutcomeID = OutcomeID;
		}	
	}
	
	// Order: Clean, Outcome (Finished, Product, etc.)
	enum TarStats {
		GUAM(249, 10142),
		MARRENTILL(251, 10143),
		TARROMIN(253, 10144),
		HARRALANDER(255, 10145);
		
		private int CleanID;
		private int OutcomeID;
		
		private TarStats(int CleanID, int OutcomeID) {
			this.CleanID = CleanID;
			this.OutcomeID = OutcomeID;
		}
	}
	
	@Override
	public boolean onStart() {
    	try {
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					gui = new UltraHerbloreGUI();
					gui.setVisible(true);
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
        return true;
    }
	
	@Override
	public void onFinish() {
		env.takeScreenshot(true);
	}
	
	boolean clickInterface(int Parent, int Child) {
		return (interfaces.get(Parent).getComponent(Child).isValid() && interfaces.get(Parent).getComponent(Child).doClick(true));
	}
	
	boolean inventoryContainsBoth(int ID1, int ID2) {
		return (inventory.contains(ID1) && inventory.contains(ID2));
	}
	
	boolean MakeUnfs() {
		return (inventoryContainsBoth(VialID, CleanID) && !interfaces.get(905).getComponent(14).isValid() && getMyPlayer().isIdle() && (System.currentTimeMillis() - this.curTime > 4000L) && inventory.useItem(inventory.getItem(VialID), inventory.getItem(CleanID)));
	}
	
	boolean MakeCompletes() {
		return (inventoryContainsBoth(UnfID, IngredientID) && !interfaces.get(905).getComponent(14).isValid() && getMyPlayer().isIdle() && (System.currentTimeMillis() - this.curTime > 4000L) && inventory.useItem(inventory.getItem(UnfID), inventory.getItem(IngredientID)));
	}
	
	boolean MakeExtremes() {
		return (inventoryContainsBoth(CompleteID, IngredientID) && !interfaces.get(905).getComponent(14).isValid() && getMyPlayer().isIdle() && (System.currentTimeMillis() - this.curTime > 4000L) && inventory.useItem(inventory.getItem(CompleteID), inventory.getItem(IngredientID)));
	}
	
	boolean MakeTars() {
		return (inventoryContainsBoth(SwampTarID, CleanID) && !interfaces.get(905).getComponent(14).isValid() && getMyPlayer().isIdle() && (System.currentTimeMillis() - this.curTime > 4000L) && inventory.useItem(inventory.getItem(SwampTarID), inventory.getItem(CleanID)));
	}
	
	@SuppressWarnings("static-access")
	boolean withdraw(final int itemID, final int count) {
		if (!bank.isOpen()) {
			return false;
		}
		if (count < -1) {
			throw new IllegalArgumentException("count (" + count + ") < -1");
		}
		final RSItem rsi = bank.getItem(itemID);
		if (rsi == null || rsi.getID() == -1) {
			return false;
		}
		final RSComponent item = rsi.getComponent();
		if (item == null) {
			return false;
		}
		int t = 0;
		while (item.getRelativeX() == 0 && bank.getCurrentTab() != 0 && t < 5) {
			if (interfaces.getComponent(bank.INTERFACE_BANK, bank.INTERFACE_BANK_TAB[0]).doClick()) {
				sleep(1000, 1300);
			}
			t++;
		}
		if (!interfaces.scrollTo(item, (bank.INTERFACE_BANK << 16) + bank.INTERFACE_BANK_SCROLLBAR)) {
			return false;
		}
		final int invCount = inventory.getCount(true);
        switch (count) {
            case 1:
                item.doClick(true);
                break;
            case 0:
            case 5:
            case 10:
                item.interact("Withdraw-" + (count == 0 ? "All" : count));
                break;
            default:
                String exactAction = "Withdraw-" + count;
                boolean typeAmount = true;
                for (String action : item.getActions()) {
                    if (exactAction.equals(action)) {
                        item.interact(action);
                        typeAmount = false;
                        break;
                    }
                }
                if (!typeAmount) {
                    if (item.interact("Withdraw-X")) {
                        sleep(1000, 1300);
                        keyboard.sendText(String.valueOf(count), true);
                    }
                }
        }
        for (int i = 0; i < 1500; i += 20) {
            sleep(20);
            int newInvCount = inventory.getCount(true);
            if (newInvCount > invCount || inventory.isFull()) {
            	return true;
            }
        }
        return false;
	}
	
	// If you use my MouseKey Method.. GIVE CREDIT Or don't use it! ~ BlackWood ~
	boolean MouseKeyClean(int ID) {
		int[] MousePath = {};
		int[] MousePath1 = {0, 1, 2, 3, 7, 6, 5, 4, 8, 9, 10, 11, 15, 14, 13, 12, 16, 17, 18, 19, 23, 22, 21, 20, 24, 25, 26, 27};
		int[] MousePath2 = {0, 4, 8, 12, 16, 20, 24, 25, 21, 17, 13, 9, 5, 1, 2, 6, 10, 14, 18, 22, 26, 27, 23, 19, 15, 11, 7, 3};
		int[] MousePath3 = {3, 2, 1, 0, 4, 5, 6, 7, 11, 10, 9, 8, 12, 13, 14, 15, 19, 18, 17, 16, 20, 21, 22, 23, 27, 26, 25, 24};
		int[] MousePath4 = {3, 7, 11, 15, 19, 23, 27, 26, 22, 18, 14, 10, 6, 2, 1, 5, 9, 13, 17, 21, 25, 24, 20, 16, 12, 8, 4, 0};
		switch (random(1, 4)) {
			case 1:
				MousePath = MousePath1;
			break;
			case 2:
				MousePath = MousePath2;
			break;
			case 3:
				MousePath = MousePath3;
			break;
			case 4:
				MousePath = MousePath4;
			break;
        }
		Point Slot1 = new Point(inventory.getItemAt(MousePath[0]).getComponent().getCenter().x, inventory.getItemAt(MousePath[0]).getComponent().getCenter().y);
		mouse.move(Slot1.x, Slot1.y);
		for (int Slot = 0; Slot <= 27; Slot++) {
			if (inventory.contains(ID)) {
				if (inventory.getItemAt(MousePath[Slot]).getID() == ID)  {
					Point Item = new Point(inventory.getItemAt(MousePath[Slot]).getComponent().getCenter().x, inventory.getItemAt(MousePath[Slot]).getComponent().getCenter().y);
					mouse.hop(Item.x, Item.y, 2, 2);
					sleep(50, 60);
					mouse.click(true);
				}
			}
		}
		return bank.open();
	}
	
	public class AntiBanThread extends Thread {

		@SuppressWarnings("deprecation")
		public void Start() {
			try {
				if (game.isLoggedIn()) {
					switch (random(1, 10)) {
					case 3: // Turn Screen
						if (getMyPlayer().getAnimation() != -1) {
							camera.setAngle(random(100, 359));
							sleep(500, 1500);
						}
						break;
					case 6:
						if (bank.isOpen()) {
							mouse.moveRandomly(450);
							sleep(500, 1500);
						}
						break;
					default:
						sleep(10);
						break;
					}
					switch (random(1, 10000)) {
					case 2961:
						log("Checking EXP And Taking A Small Break (Anti-Ban)");
						canLoop = false;
						if (game.openTab(Game.TAB_STATS, true)) {
							skills.doHover(Skills.INTERFACE_HERBLORE);
							sleep(6000, 7500);
						}
						canLoop = true;
						log("Finished Checking EXP And Taking A Small Break");
						break;
					case 6487:
						log("Taking A Break (Anti-Ban)");
						canLoop = false;
						if (random(1, 2) == 2) {
							mouse.moveOffScreen();
						}
						sleep(17500, 37500);
						canLoop = true;
						log("Break Finished");
						break;
					default:
						sleep(10);
						break;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}
	
	public long timeFromMark(long fromMark) {
		return System.currentTimeMillis() - fromMark;
	}

	public boolean waitFor(boolean bool, int timeout) {
		long t = System.currentTimeMillis();
		while (timeFromMark(t) < timeout) {
			if (bool == true) {
				return true;
			}
			sleep(10);
		}
		return false;
	}

	@Override
	public int loop() {
		try {
			new AntiBanThread().Start();
			if (interfaces.canContinue()) {
				interfaces.clickContinue();
			}
			if (Cleaning == true && StartedScript == true && HasItemIDs == false) {
				GrimyID = Chosen.GrimyID;
				CleanID = Chosen.CleanID;
				if (inventory.contains(GrimyID)) {
					HasItemIDs = true;
				} else {
					log.severe("Start with an inventory full of grimy herbs!");
					StartedScript = false;
					stopScript();
				}
			}
			if ((Unfs == true || FullCompletes == true) && StartedScript == true && HasItemIDs == false) {
				CleanID = Chosen.CleanID;
				UnfID = Chosen.UnfID;
				if (FullCompletes == true) {
					IngredientID = Chosen.IngredientID;
					CompleteID = Chosen.CompleteID;
				}
				if (inventoryContainsBoth(VialID, CleanID)) {
					HasItemIDs = true;
				} else {
					log.severe("Start with an inventory full of 14 Vials Of Water and 14 Clean Herbs");
					StartedScript = false;
					stopScript();
				}
			}
			if (Completes == true && StartedScript == true && HasItemIDs == false) {
				UnfID = Chosen.UnfID;
				IngredientID = Chosen.IngredientID;
				CompleteID = Chosen.CompleteID;
				if (inventoryContainsBoth(UnfID, IngredientID)) {
					HasItemIDs = true;
				} else {
					log.severe("Start with an inventory full of 14 Unf Potions and 14 Ingredients!");
					StartedScript = false;
					stopScript();
				}
			}
			if (Extremes == true && StartedScript == true && HasItemIDs == false) {
				CompleteID = EChosen.CompleteID;
				IngredientID = EChosen.IngredientID;
				OutcomeID = EChosen.OutcomeID;
				if (inventoryContainsBoth(CompleteID, IngredientID)) {
					HasItemIDs = true;
				} else {
					log.severe("Start with the amount of potions and ingredients needed, in your inventory!");
					StartedScript = false;
					stopScript();
				}
			}
			if (Taring == true && StartedScript == true && HasItemIDs == false) {
				CleanID = TChosen.CleanID;
				OutcomeID = TChosen.OutcomeID;
				if (inventoryContainsBoth(CleanID, SwampTarID) && inventory.contains(Mortar)) {
					HasItemIDs = true;
				} else {
					log.severe("Start with your inventory full of Swamp Tar, A Mortar, and Clean Herbs!");
					StartedScript = false;
					stopScript();
				}
			}
			if (HasItemIDs == true && canLoop == true) {
				if (Cleaning == true) {
					while (bank.isOpen()) {
						if (inventory.contains(CleanID)) {
							if (bank.depositAll()) {
								waitFor(!inventory.contains(CleanID), 1000);
							}
						}
						if (!inventory.contains(CleanID) && bank.getItem(GrimyID) != null && withdraw(GrimyID, 0)) {
							if (bank.close()) {
								waitFor(!bank.isOpen(), 5000);
							}
						} else {
							if (bank.getItem(GrimyID) == null) {
								log("Out of supplies...");
								game.logout(false);
								stopScript();
							}
						}
						if (inventory.contains(GrimyID) && !inventory.contains(CleanID)) {
							if (bank.close()) {
								waitFor(!bank.isOpen(), 5000);
							}
						}
						break;
					} 
					while (!bank.isOpen()) {
						MouseKeyClean(GrimyID);
						break;
					}
				}
				if (Unfs == true) {
					while (bank.isOpen()) {
						if (inventory.contains(UnfID)) {
							if (bank.depositAll()) {
								waitFor(!inventory.contains(UnfID), 1000);
							}
						}
						if (inventory.getCount() == 0 && bank.getItem(VialID) != null) {
							if (bank.withdraw(VialID, 14)) {
								waitFor(inventory.contains(VialID), 5000);
							}
						} else {
							if (bank.getItem(VialID) == null) {
								log("Out of supplies...");
								game.logout(false);
								stopScript();
							}
						}
						if (inventory.contains(VialID) && inventory.getCount(VialID) < 15 && !inventory.contains(CleanID) && bank.getItem(CleanID) != null) {
							if (bank.withdraw(CleanID, 0)) {
								waitFor(inventory.contains(CleanID), 5000);
							}
						} else {
							if (inventory.getCount(VialID) > 14) {
								if (bank.depositAll()) {
									waitFor(!inventory.contains(VialID), 5000);
								}
							}
							if (bank.getItem(CleanID) == null) {
								log("Out of supplies...");
								game.logout(false);
								stopScript();
							}
						}
						if (inventoryContainsBoth(VialID, CleanID)) {
							if (bank.close()) {
								waitFor(!bank.isOpen(), 5000);
							}
						}
						break;
					}
					while (!bank.isOpen()) {
						if (getMyPlayer().getAnimation() != -1) {
							this.curTime = System.currentTimeMillis();
						}
						if (!interfaces.get(905).getComponent(14).isValid() && inventoryContainsBoth(VialID, CleanID)) {
							if (MakeUnfs()) {
								waitFor(interfaces.get(905).getComponent(14).isValid(), 1200);
							}
						}
						if (interfaces.get(905).getComponent(14).isValid()) {
							if (clickInterface(905, 14)) {
								waitFor(inventory.contains(UnfID), 5000);
							}
						}
						if (!inventoryContainsBoth(VialID, CleanID) && inventory.contains(UnfID) && bank.open()) {
							bank.depositAll();
						}
						break;
					}
				}
				if (Completes == true) {
					while (bank.isOpen()) {
						if (inventory.contains(CompleteID) && bank.depositAll()) {
							if (bank.depositAll()) {
								waitFor(!inventory.contains(CompleteID), 1000);
							}
						}
						if (inventory.getCount() == 0 && bank.getItem(UnfID) != null) {
							if (bank.withdraw(UnfID, 14)) {
								waitFor(inventory.contains(UnfID), 5000);
							}
						} else {
							if (bank.getItem(UnfID) == null) {
								log("Out of supplies...");
								game.logout(false);
								stopScript();
							}
						}
						if (inventory.contains(UnfID) && inventory.getCount(UnfID) < 15 && !inventory.contains(IngredientID) && bank.getItem(IngredientID) != null) {
							if (bank.withdraw(IngredientID, 0)) {
								waitFor(inventory.contains(IngredientID), 5000);
							}
						} else {
							if (inventory.getCount(UnfID) > 14) {
								if (bank.depositAll()) {
									waitFor(inventory.contains(UnfID), 5000);
								}
							}
							if (bank.getItem(IngredientID) == null) {
								log("Out of supplies...");
								game.logout(false);
								stopScript();
							}
						}
						if (inventoryContainsBoth(UnfID, IngredientID)) {
							if (bank.close()) {
								waitFor(!bank.isOpen(), 5000);
							}
						}
						break;
					}
					while (!bank.isOpen()) {
						if (getMyPlayer().getAnimation() != -1) {
							this.curTime = System.currentTimeMillis();
						}
						if (!interfaces.get(905).getComponent(14).isValid() && inventoryContainsBoth(UnfID, IngredientID)) {
							if (MakeCompletes()) {
								waitFor(interfaces.get(905).getComponent(14).isValid(), 1200);
							}
						}
						if (interfaces.get(905).getComponent(14).isValid()) {
							if (clickInterface(905, 14)) {
								waitFor(inventory.contains(CompleteID), 5000);
							}
						}
						if (!inventoryContainsBoth(UnfID, IngredientID) && inventory.contains(CompleteID) && bank.open()) {
							bank.depositAll();
						}
						break;
					}
				}
				if (FullCompletes == true) {
					while (bank.isOpen()) {
						if (inventory.contains(CompleteID)) {
							if (bank.depositAll()) {
								waitFor(inventory.getCount() == 0, 5000);
							}
						}
						if (inventory.getCount() == 0 && bank.getItem(VialID) != null) {
							if (bank.withdraw(VialID, 14)) {
								waitFor(inventory.contains(VialID), 5000);
							}
						} else {
							if (bank.getItem(VialID) == null) {
								log("Out of supplies...");
								game.logout(false);
								stopScript();
							}
						}
						if (inventory.contains(VialID) && inventory.getCount(VialID) < 15 && !inventory.contains(CleanID) && bank.getItem(CleanID) != null) {
							if (bank.withdraw(CleanID, 0)) {
								waitFor(inventory.contains(CleanID), 5000);
							}
						} else {
							if (inventory.getCount(VialID) > 14) {
								if (bank.depositAll()) {
									waitFor(!inventory.contains(VialID), 1200);
								}
							}
							if (bank.getItem(CleanID) == null) {
								log("Out of supplies...");
								game.logout(false);
								stopScript();
							}
						}
						if (inventoryContainsBoth(VialID, CleanID) || inventoryContainsBoth (UnfID, IngredientID)) {
							if (bank.close()) {
								waitFor(!bank.isOpen(), 5000);
							}
						}
						if (inventory.contains(UnfID) && !inventory.contains(IngredientID) && bank.getItem(IngredientID) != null) {
							if (bank.withdraw(IngredientID, 0)) {
								waitFor(inventory.contains(IngredientID), 5000);
							}
						} else {
							if (bank.getItem(IngredientID) == null) {
								log("Out of supplies...");
								game.logout(false);
								stopScript();
							}
						}
						break;
					}
					while (!bank.isOpen()) {
						if (getMyPlayer().getAnimation() != -1) {
							this.curTime = System.currentTimeMillis();
						}
						if (!interfaces.get(905).getComponent(14).isValid() && inventoryContainsBoth(VialID, CleanID)) {
							if (MakeUnfs()) {
								waitFor(interfaces.get(905).getComponent(14).isValid(), 1200);
							}
						}
						if (!interfaces.get(905).getComponent(14).isValid() && inventoryContainsBoth(UnfID, IngredientID)) {
							if (MakeCompletes()) {
								waitFor(interfaces.get(905).getComponent(14).isValid(), 1200);
							}
						}
						if (interfaces.get(905).getComponent(14).isValid() && clickInterface(905, 14)) {
							if (clickInterface(905,14)) {
								waitFor(inventory.contains(UnfID) || inventory.contains(CompleteID), 5000);
							}
						}
						if ((!inventoryContainsBoth(VialID, CleanID) && inventory.contains(UnfID) && !inventory.contains(IngredientID) || !inventoryContainsBoth(UnfID, IngredientID) && inventory.contains(CompleteID)) && bank.open()) {
							if (inventory.contains(CompleteID)) {
								bank.depositAll();
							}
						}
						break;
					}
				}
				if (Extremes == true) {
					if (EChosen != ExtremeStats.RANGING) {
						while (bank.isOpen()) {
							if (inventory.contains(OutcomeID)) {
								if (bank.depositAll()) {
									waitFor(!inventory.contains(OutcomeID), 1000);
								}
							}
							if (inventory.getCount() == 0 && bank.getItem(CompleteID) != null) {
								if (bank.withdraw(CompleteID, 14)) {
									waitFor(inventory.contains(CompleteID), 5000);
								}
							} else {
								if (bank.getItem(CompleteID) == null) {
									log("Out of supplies...");
									game.logout(false);
									stopScript();
								}
							}
							if (inventory.contains(CompleteID) && inventory.getCount(CompleteID) < 15 && !inventory.contains(IngredientID) && bank.getItem(IngredientID) != null) {
								if (bank.withdraw(IngredientID, 0)) {
									waitFor(inventory.contains(IngredientID), 5000);
								}
							} else {
								if (inventory.getCount(CompleteID) > 14) {
									if (bank.depositAll()) {
										waitFor(!inventory.contains(CompleteID), 1000);
									}
								}
								if (bank.getItem(IngredientID) == null) {
									log("Out of supplies...");
									game.logout(false);
									stopScript();
								}
							}
							if (inventoryContainsBoth(CompleteID, IngredientID)) {
								if (bank.close()) {
									waitFor(!bank.isOpen(), 5000);
								}
							}
							break;
						}
						while (!bank.isOpen()) {
							if (getMyPlayer().getAnimation() != -1) {
								this.curTime = System.currentTimeMillis();
							}
							if (!interfaces.get(905).getComponent(14).isValid() && inventoryContainsBoth(CompleteID, IngredientID)) {
								if (MakeExtremes()) {
									waitFor(interfaces.get(905).getComponent(14).isValid(), 1200);
								}
							}
							if (interfaces.get(905).getComponent(14).isValid()) {
								if (clickInterface(905, 14)) {
									waitFor(inventory.contains(OutcomeID), 5000);
								}
							}
							if (!inventoryContainsBoth(CompleteID, IngredientID) && inventory.contains(OutcomeID) && bank.open()) {
								bank.depositAll();
							}
							break;
						}
					}
					if (EChosen == ExtremeStats.RANGING) {
						while (bank.isOpen()) {
							if (inventory.contains(OutcomeID)) {
								if (bank.depositAllExcept(IngredientID)) {
									waitFor(!inventory.contains(OutcomeID), 1000);
								}
							}
							if (!inventory.contains(OutcomeID) && inventory.contains(IngredientID) && !inventory.contains(CompleteID) && bank.getItem(CompleteID) != null) {
								if (bank.withdraw(CompleteID, 0)) {
									waitFor(inventory.contains(CompleteID), 5000);
								}
							} else {
								if (bank.getItem(CompleteID) == null) {
									log("Out of supplies...");
									game.logout(false);
									stopScript();
								}
							}
							if (inventoryContainsBoth(CompleteID, IngredientID)) {
								if (bank.close()) {
									waitFor(!bank.isOpen(), 5000);
								}
							}
							break;
						}
						while (!bank.isOpen()) {
							if (getMyPlayer().getAnimation() != -1) {
								this.curTime = System.currentTimeMillis();
							}
							if (!interfaces.get(905).getComponent(14).isValid() && inventoryContainsBoth(CompleteID, IngredientID)) {
								if (MakeExtremes()) {
									waitFor(interfaces.get(905).getComponent(14).isValid(), 1200);
								}
							}
							if (interfaces.get(905).getComponent(14).isValid()) {
								if (clickInterface(905, 14)) {
									waitFor(inventory.contains(OutcomeID), 5000);
								}
							}
							if (!inventoryContainsBoth(CompleteID, IngredientID) && inventory.contains(OutcomeID) && bank.open()) {
								bank.depositAll();
							}
							break;
						}
					}
				}
				if (Taring == true) {
					while (bank.isOpen()) {
						if (inventory.contains(OutcomeID)) {
							if (bank.depositAllExcept(Mortar, SwampTarID, CleanID)) {
								waitFor(!inventory.contains(OutcomeID), 1000);
							}
						}
						if (inventoryContainsBoth(Mortar, SwampTarID) && !inventory.contains(CleanID) && bank.getItem(CleanID) != null) {
							if (bank.withdraw(CleanID, 0)) {
								waitFor(inventory.contains(CleanID), 5000);
							}
						} else {
							if (bank.getItem(CleanID) == null) {
								log("Out of supplies...");
								game.logout(false);
								stopScript();
							}
						}
						if (inventoryContainsBoth(Mortar, SwampTarID) && inventory.contains(CleanID)) {
							if (bank.close()) {
								waitFor(!bank.isOpen(), 5000);
							}
						}
						break;
					}
					while (!bank.isOpen()) {
						if (getMyPlayer().getAnimation() != -1) {
							this.curTime = System.currentTimeMillis();
						}
						if (!interfaces.get(905).getComponent(14).isValid() && inventoryContainsBoth(SwampTarID, CleanID)) {
							if (MakeTars()) {
								waitFor(interfaces.get(905).getComponent(14).isValid(), 1200);
							}
						}
						if (interfaces.get(905).getComponent(14).isValid()) {
							if (clickInterface(905, 14)) {
								waitFor(inventory.contains(OutcomeID), 5000);
							}
						}
						if (!inventoryContainsBoth(SwampTarID, CleanID) && inventory.contains(OutcomeID) && bank.open()) {
							if (bank.depositAllExcept(Mortar, SwampTarID, CleanID)) {
								waitFor(inventory.getCountExcept(Mortar, SwampTarID, CleanID) == 0, 5000);
							}
						}
						break;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	String convertDurationToString(long t) {
		t /= 1000;
		StringBuilder s = new StringBuilder();
		s.append(':');
		if (t % 60 < 10)
			s.append('0');
		s.append(t % 60);
		t /= 60;
		s.insert(0, t % 60).insert(0, ':');
		if (t % 60 < 10)
			s.insert(1, '0');
		t /= 60;
		s.insert(0, t % 24);
		if (t % 24 < 10)
			s.insert(0, '0');
		return new String(s);
	}

	private String formatNumber(String str) {
		if (str.length() < 4) {
			return str;
		}
		return formatNumber(str.substring(0, str.length() - 3)) + ","
				+ str.substring(str.length() - 3, str.length());
	}

	private Image getImage(String url) {
		try {
			return ImageIO.read(new URL(url));
		} catch (IOException e) {
			return null;
		}
	}

	private final Color color1 = new Color(184, 155, 127);
	private final Color color2 = new Color(64, 34, 5);
	private final Color color3 = new Color(149, 109, 69);

	private final BasicStroke stroke1 = new BasicStroke(1);

	private final Font font1 = new Font("Arial", 1, 15);

	private final Image img1 = getImage("http://i842.photobucket.com/albums/zz348/monkey123502/ExitButton.png");
	private final Image img2 = getImage("http://dl.dropbox.com/u/22127840/Pictures/Open%20Button.png");

	public void onRepaint(Graphics g1) {
		Graphics2D g = (Graphics2D) g1;
		if (StartedScript == true) {
			if (isPaintShowing == true) {
				if (StartEXP == 0) {
					StartEXP = skills.getCurrentExp(Skills.getIndex("Herblore"));
				}
				EXPGained = skills.getCurrentExp(Skills.getIndex("Herblore")) - StartEXP;
				g.setColor(color1);
				g.fillRect(7, 345, 506, 128);
				g.setColor(color2);
				g.setStroke(stroke1);
				g.drawRect(18, 351, 481, 33);
				g.setColor(color3);
				g.fillRect(21, 354, ((int) (skills.getPercentToNextLevel(Skills.getIndex("Herblore")) * 4.75)), 28);
				g.setFont(font1);
				g.setColor(color2);
				g.drawString("Interactions: " + formatNumber(Made + "") + " | Interactions/H: " + formatNumber((int) ((Made) * 3600000D / (System.currentTimeMillis() - Duration)) + ""), 20, 407);
				g.drawString("EXP Gained: " + formatNumber(EXPGained + "") + " | EXP/H: " + formatNumber((int) ((EXPGained) * 3600000D / (System.currentTimeMillis() - Duration)) + ""), 20, 427);
				g.drawString("Role: " + Role, 20, 445);
				g.drawString("Duration: " + convertDurationToString(System.currentTimeMillis() - Duration), 20, 462);
				g.drawString(skills.getPercentToNextLevel(Skills.getIndex("Herblore")) + "% " + "(" + formatNumber(skills.getExpToNextLevel(Skills.getIndex("Herblore")) + "") + ") " + "To " + (skills.getCurrentLevel(Skills.getIndex("Herblore")) + 1), 200, 375);
				g.drawImage(img1, 488, 452, null);
			}
			if (isPaintShowing == false) {
				g.drawImage(img2, 488, 452, null);
			}
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();
		if (x >= 488 && x < 488 + 16 && y >= 452 && y < 452 + 15) {
			isPaintShowing = !isPaintShowing;
		}
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
	}
	
	public void messageReceived(MessageEvent arg0) {
		String message = arg0.getMessage().toLowerCase();
		if (Cleaning == true && message.contains("clean")) {
			Made++;
		}
		if (Unfs == true && message.contains("into the vial")) {
			Made++;
		}
		if (Completes == true && message.contains("into your potion") || FullCompletes == true && message.contains("into your potion")) {
			Made++;
		}
		if (Taring == true && message.contains("mix the")) {
			Made = Made + 15;
		}
		if (Extremes == true && message.contains("You carefully mix the")) {
			Made++;
		}
	}
	
	public class UltraHerbloreGUI extends JFrame {
		
		private static final long serialVersionUID = 1L;
		
		public UltraHerbloreGUI() {
			initComponents();
		}
		
		void StartPressed(ActionEvent e) {
			StartedScript = true;
			Duration = System.currentTimeMillis();
			if (comboBox1.getSelectedItem().equals("Cleaning")) {
				Cleaning = true;
				Role = "Herb Cleaning";
				if (comboBox2.getSelectedItem().equals("Guam")) {
					Chosen = PotionStats.ATTACK;
				}
				if (comboBox2.getSelectedItem().equals("Marrentill")) {
					Chosen = PotionStats.ANTIPOISON;
				}
				if (comboBox2.getSelectedItem().equals("Tarromin")) {
					Chosen = PotionStats.STRENGTH;
				}
				if (comboBox2.getSelectedItem().equals("Harralander")) {
					Chosen = PotionStats.STATRESTORE;
				}
				if (comboBox2.getSelectedItem().equals("Ranarr")) {
					Chosen = PotionStats.DEFENCE;
				}
				if (comboBox2.getSelectedItem().equals("Toadflax")) {
					Chosen = PotionStats.AGILITY;
				}
				if (comboBox2.getSelectedItem().equals("Spirit Weed")) {
					Chosen = PotionStats.SUMMONING;
				}
				if (comboBox2.getSelectedItem().equals("Irit")) {
					Chosen = PotionStats.SUPERATTACK;
				}
				if (comboBox2.getSelectedItem().equals("Avantoe")) {
					Chosen = PotionStats.FISHING;
				}
				if (comboBox2.getSelectedItem().equals("Kwuarm")) {
					Chosen = PotionStats.SUPERSTRENGTH;
				}
				if (comboBox2.getSelectedItem().equals("Snapdragon")) {
					Chosen = PotionStats.SUPERRESTORE;
				}
				if (comboBox2.getSelectedItem().equals("Cadantine")) {
					Chosen = PotionStats.SUPERDEFENCE;
				}
				if (comboBox2.getSelectedItem().equals("Lantadyme")) {
					Chosen = PotionStats.ANTIFIRE;
				}
				if (comboBox2.getSelectedItem().equals("Dwarf Weed")) {
					Chosen = PotionStats.RANGING;
				}
				if (comboBox2.getSelectedItem().equals("Torstol")) {
					Chosen = PotionStats.ZAMORAK;
				}
			}
			if (comboBox1.getSelectedItem().equals("Unfs")) {
				Unfs = true;
				Role = "Unfs";
				if (comboBox2.getSelectedItem().equals("Guam Potion")) {
					Chosen = PotionStats.ATTACK;
				}
				if (comboBox2.getSelectedItem().equals("Marrentill Potion")) {
					Chosen = PotionStats.ANTIPOISON;
				}
				if (comboBox2.getSelectedItem().equals("Tarromin Potion")) {
					Chosen = PotionStats.STRENGTH;
				}
				if (comboBox2.getSelectedItem().equals("Harralander Potion")) {
					Chosen = PotionStats.STATRESTORE;
				}
				if (comboBox2.getSelectedItem().equals("Ranarr Potion")) {
					Chosen = PotionStats.DEFENCE;
				}
				if (comboBox2.getSelectedItem().equals("Toadflax Potion")) {
					Chosen = PotionStats.AGILITY;
				}
				if (comboBox2.getSelectedItem().equals("Spirit Weed Potion")) {
					Chosen = PotionStats.SUMMONING;
				}
				if (comboBox2.getSelectedItem().equals("Irit Potion")) {
					Chosen = PotionStats.SUPERATTACK;
				}
				if (comboBox2.getSelectedItem().equals("Avantoe Potion")) {
					Chosen = PotionStats.FISHING;
				}
				if (comboBox2.getSelectedItem().equals("Kwuarm Potion")) {
					Chosen = PotionStats.SUPERSTRENGTH;
				}
				if (comboBox2.getSelectedItem().equals("Snapdragon Potion")) {
					Chosen = PotionStats.SUPERRESTORE;
				}
				if (comboBox2.getSelectedItem().equals("Cadantine Potion")) {
					Chosen = PotionStats.SUPERDEFENCE;
				}
				if (comboBox2.getSelectedItem().equals("Lantadyme Potion")) {
					Chosen = PotionStats.ANTIFIRE;
				}
				if (comboBox2.getSelectedItem().equals("Dwarf Weed Potion")) {
					Chosen = PotionStats.RANGING;
				}
				if (comboBox2.getSelectedItem().equals("Torstol Potion")) {
					Chosen = PotionStats.ZAMORAK;
				}
			}
			if (comboBox1.getSelectedItem().equals("Completes")) {
				Completes = true;
				Role = "Completes";
				if (comboBox2.getSelectedItem().equals("Attack")) {
					Chosen = PotionStats.ATTACK;
				}
				if (comboBox2.getSelectedItem().equals("Anti-Poison")) {
					Chosen = PotionStats.ANTIPOISON;
				}
				if (comboBox2.getSelectedItem().equals("Strength")) {
					Chosen = PotionStats.STRENGTH;
				}
				if (comboBox2.getSelectedItem().equals("Serum 207")) {
					Chosen = PotionStats.SERUM207;
				}
				if (comboBox2.getSelectedItem().equals("Stat Restore")) {
					Chosen = PotionStats.STATRESTORE;
				}
				if (comboBox2.getSelectedItem().equals("Energy")) {
					Chosen = PotionStats.ENERGY;
				}
				if (comboBox2.getSelectedItem().equals("Defence")) {
					Chosen = PotionStats.DEFENCE;
				}
				if (comboBox2.getSelectedItem().equals("Agility")) {
					Chosen = PotionStats.AGILITY;
				}
				if (comboBox2.getSelectedItem().equals("Combat")) {
					Chosen = PotionStats.COMBAT;
				}
				if (comboBox2.getSelectedItem().equals("Prayer")) {
					Chosen = PotionStats.PRAYER;
				}
				if (comboBox2.getSelectedItem().equals("Summoning")) {
					Chosen = PotionStats.SUMMONING;
				}
				if (comboBox2.getSelectedItem().equals("Super Attack")) {
					Chosen = PotionStats.SUPERATTACK;
				}
				if (comboBox2.getSelectedItem().equals("Super Anti-Poison")) {
					Chosen = PotionStats.SUPERANTIPOISON;
				}
				if (comboBox2.getSelectedItem().equals("Fishing")) {
					Chosen = PotionStats.FISHING;
				}
				if (comboBox2.getSelectedItem().equals("Super Energy")) {
					Chosen = PotionStats.SUPERENERGY;
				}
				if (comboBox2.getSelectedItem().equals("Hunter")) {
					Chosen = PotionStats.HUNTER;
				}
				if (comboBox2.getSelectedItem().equals("Super Strength")) {
					Chosen = PotionStats.SUPERSTRENGTH;
				}
				if (comboBox2.getSelectedItem().equals("Fletching")) {
					Chosen = PotionStats.FLETCHING;
				}
				if (comboBox2.getSelectedItem().equals("Weapon Poison")) {
					Chosen = PotionStats.WEAPONPOISON;
				}
				if (comboBox2.getSelectedItem().equals("Super Restore")) {
					Chosen = PotionStats.SUPERRESTORE;
				}
				if (comboBox2.getSelectedItem().equals("Super Defence")) {
					Chosen = PotionStats.SUPERDEFENCE;
				}
				if (comboBox2.getSelectedItem().equals("Anti-Fire")) {
					Chosen = PotionStats.ANTIFIRE;
				}
				if (comboBox2.getSelectedItem().equals("Ranging")) {
					Chosen = PotionStats.RANGING;
				}
				if (comboBox2.getSelectedItem().equals("Magic")) {
					Chosen = PotionStats.MAGIC;
				}
				if (comboBox2.getSelectedItem().equals("Zamorak")) {
					Chosen = PotionStats.ZAMORAK;
				}
				if (comboBox2.getSelectedItem().equals("Saradomin")) {
					Chosen = PotionStats.SARADOMIN;
				}
			}
			if (comboBox1.getSelectedItem() == "Full Completes") {
				FullCompletes = true;
				Role = "Full Completes";
				if (comboBox2.getSelectedItem().equals("Attack")) {
					Chosen = PotionStats.ATTACK;
				}
				if (comboBox2.getSelectedItem().equals("Anti-Poison")) {
					Chosen = PotionStats.ANTIPOISON;
				}
				if (comboBox2.getSelectedItem().equals("Strength")) {
					Chosen = PotionStats.STRENGTH;
				}
				if (comboBox2.getSelectedItem().equals("Serum 207")) {
					Chosen = PotionStats.SERUM207;
				}
				if (comboBox2.getSelectedItem().equals("Stat Restore")) {
					Chosen = PotionStats.STATRESTORE;
				}
				if (comboBox2.getSelectedItem().equals("Energy")) {
					Chosen = PotionStats.ENERGY;
				}
				if (comboBox2.getSelectedItem().equals("Defence")) {
					Chosen = PotionStats.DEFENCE;
				}
				if (comboBox2.getSelectedItem().equals("Agility")) {
					Chosen = PotionStats.AGILITY;
				}
				if (comboBox2.getSelectedItem().equals("Combat")) {
					Chosen = PotionStats.COMBAT;
				}
				if (comboBox2.getSelectedItem().equals("Prayer")) {
					Chosen = PotionStats.PRAYER;
				}
				if (comboBox2.getSelectedItem().equals("Summoning")) {
					Chosen = PotionStats.SUMMONING;
				}
				if (comboBox2.getSelectedItem().equals("Super Attack")) {
					Chosen = PotionStats.SUPERATTACK;
				}
				if (comboBox2.getSelectedItem().equals("Super Anti-Poison")) {
					Chosen = PotionStats.SUPERANTIPOISON;
				}
				if (comboBox2.getSelectedItem().equals("Fishing")) {
					Chosen = PotionStats.FISHING;
				}
				if (comboBox2.getSelectedItem().equals("Super Energy")) {
					Chosen = PotionStats.SUPERENERGY;
				}
				if (comboBox2.getSelectedItem().equals("Hunter")) {
					Chosen = PotionStats.HUNTER;
				}
				if (comboBox2.getSelectedItem().equals("Super Strength")) {
					Chosen = PotionStats.SUPERSTRENGTH;
				}
				if (comboBox2.getSelectedItem().equals("Fletching")) {
					Chosen = PotionStats.FLETCHING;
				}
				if (comboBox2.getSelectedItem().equals("Weapon Poison")) {
					Chosen = PotionStats.WEAPONPOISON;
				}
				if (comboBox2.getSelectedItem().equals("Super Restore")) {
					Chosen = PotionStats.SUPERRESTORE;
				}
				if (comboBox2.getSelectedItem().equals("Super Defence")) {
					Chosen = PotionStats.SUPERDEFENCE;
				}
				if (comboBox2.getSelectedItem().equals("Anti-Fire")) {
					Chosen = PotionStats.ANTIFIRE;
				}
				if (comboBox2.getSelectedItem().equals("Ranging")) {
					Chosen = PotionStats.RANGING;
				}
				if (comboBox2.getSelectedItem().equals("Magic")) {
					Chosen = PotionStats.MAGIC;
				}
				if (comboBox2.getSelectedItem().equals("Zamorak")) {
					Chosen = PotionStats.ZAMORAK;
				}
				if (comboBox2.getSelectedItem().equals("Saradomin")) {
					Chosen = PotionStats.SARADOMIN;
				}
			}
			if (comboBox1.getSelectedItem().equals("Extremes")) {
				Extremes = true;
				Role = "Extremes";
				if (comboBox2.getSelectedItem().equals("Attack")) {
					EChosen = ExtremeStats.ATTACK;
				}
				if (comboBox2.getSelectedItem().equals("Strength")) {
					EChosen = ExtremeStats.STRENGTH;
				}
				if (comboBox2.getSelectedItem().equals("Defence")) {
					EChosen = ExtremeStats.DEFENCE;
				}
				if (comboBox2.getSelectedItem().equals("Magic")) {
					EChosen = ExtremeStats.MAGIC;
				}
				if (comboBox2.getSelectedItem().equals("Ranging")) {
					EChosen = ExtremeStats.RANGING;
				}
			}
			if (comboBox1.getSelectedItem().equals("Tars")) {
				Taring = true;
				Role = "Tar Making";
				if (comboBox2.getSelectedItem().equals("Guam")) {
					TChosen = TarStats.GUAM;
				}
				if (comboBox2.getSelectedItem().equals("Marrentill")) {
					TChosen = TarStats.MARRENTILL;
				}
				if (comboBox2.getSelectedItem().equals("Tarromin")) {
					TChosen = TarStats.TARROMIN;
				}
				if (comboBox2.getSelectedItem().equals("Harralander")) {
					TChosen = TarStats.HARRALANDER;
				}
			}
			dispose();
		}

		private void initComponents() {
			final String[] MethodList = {"Cleaning", "Unfs", "Completes", "Full Completes", "Extremes", "Tars"};
			final String[] HerbList = {"Guam", "Marrentill", "Tarromin", "Harralander", "Ranarr", "Toadflax", "Spirit Weed", "Irit", "Avantoe", "Kwuarm", "Snapdragon", "Cadantine", "Lantadyme", "Dwarf Weed", "Torstol"};
			final String[] UnfList = {"Guam Potion", "Marrentill Potion", "Tarromin Potion", "Harralander Potion", "Ranarr Potion", "Toadflax Potion", "Spirit Weed Potion", "Irit Potion", "Avantoe Potion", "Kwuarm Potion", "Snapdragon Potion", "Cadantine Potion", "Lantadyme Potion", "Dwarf Weed Potion", "Torstol Potion"};
			final String[] CompleteList = {"Attack", "Anti-Poison", "Strength", "Serum 207", "Stat Restore", "Energy", "Defence", "Agility", "Combat", "Prayer", "Summoning", "Super Attack", "Super Anti-Poison", "Fishing", "Super Energy", "Hunter", "Super Strength", "Fletching", "Weapon Poison", "Super Restore", "Super Defence", "Anti-Fire", "Ranging", "Magic", "Zamorak", "Saradomin"};	
			final String[] ExtremeList = {"Attack", "Strength", "Defence", "Magic", "Ranging"};
			final String[] TarList = {"Guam", "Marrentill", "Tarromin", "Harralander"};
			comboBox1 = new JComboBox(MethodList);
			comboBox2 = new JComboBox(HerbList);
			button1 = new JButton("Start");

			//======== this ========
			Container contentPane = getContentPane();
			contentPane.setLayout(null);

			contentPane.add(comboBox1);
			comboBox1.setBounds(25, 5, 115, 30);
			comboBox1.setSelectedItem(MethodList[0]);
			contentPane.add(comboBox2);
			comboBox2.setBounds(25, 45, 115, 30);
			comboBox2.setSelectedItem(UnfList[0]);
			comboBox1.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
					if (comboBox1.getSelectedItem() == "Cleaning") {
						comboBox2.removeAllItems();
						for (String ListPart : HerbList) {
							comboBox2.addItem(ListPart);
						} 
					}
                    if (comboBox1.getSelectedItem() == "Unfs") {
						comboBox2.removeAllItems();
						for (String ListPart : UnfList) {
							comboBox2.addItem(ListPart);
						} 
					}
					 if (comboBox1.getSelectedItem() == "Completes") {
						comboBox2.removeAllItems();
						for (String ListPart : CompleteList) {
							comboBox2.addItem(ListPart);
						} 
					}
					if (comboBox1.getSelectedItem() == "Full Completes") {
						comboBox2.removeAllItems();
						for (String ListPart : CompleteList) {
							comboBox2.addItem(ListPart);
						} 
					}
					if (comboBox1.getSelectedItem() == "Extremes") {
						comboBox2.removeAllItems();
						for (String ListPart : ExtremeList) {
							comboBox2.addItem(ListPart);
						} 
					}
					if (comboBox1.getSelectedItem() == "Tars") {
						comboBox2.removeAllItems();
						for (String ListPart : TarList) {
							comboBox2.addItem(ListPart);
						} 
					}
                }
            });	
			contentPane.add(button1);
			button1.setBounds(25, 85, 115, 30);
			button1.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    StartPressed(e);
                }
            });	

			{ // compute preferred size
				Dimension preferredSize = new Dimension();
				for(int i = 0; i < contentPane.getComponentCount(); i++) {
					Rectangle bounds = contentPane.getComponent(i).getBounds();
					preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
					preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
				}
				Insets insets = contentPane.getInsets();
				preferredSize.width += insets.right + 25;
				preferredSize.height += insets.bottom + 5;
				contentPane.setMinimumSize(preferredSize);
				contentPane.setPreferredSize(preferredSize);
			}
			pack();
			setLocationRelativeTo(getOwner());
		}
		private JComboBox comboBox1;
		private JComboBox comboBox2;
		private JButton button1;
	}
	
}