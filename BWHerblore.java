import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.*;

import org.rsbot.script.Script;
import org.rsbot.script.ScriptManifest;
import org.rsbot.script.methods.Game;
import org.rsbot.script.methods.Skills;
import org.rsbot.script.wrappers.RSComponent;
import org.rsbot.script.wrappers.RSItem;
import org.rsbot.event.events.*;
import org.rsbot.event.listeners.*;

@ScriptManifest(authors = "BlackWood", name = "BW Herblore", version = 2.1, description = "Herblore Done Right!", website = "http://www.powerbot.org/vb/showthread.php?t=660521")
@SuppressWarnings("deprecation")
public class BWHerblore extends Script implements PaintListener, MessageListener {
	
	long curTime = System.currentTimeMillis();

	boolean StartedScript = false;
	boolean HasItemIDs = false;
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
	
	BWHerbloreGUI gui;
	
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
	
	public boolean onStart() {
    	try {
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					gui = new BWHerbloreGUI();
					gui.setVisible(true);
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
        return true;
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
	
	public void superAntiMoveMouse() {
        switch (random(0, 10)) {
            case 0:
                mouse.moveSlightly();
                mouse.moveSlightly();
                mouse.moveSlightly();
            break;
            case 1:
                mouse.moveSlightly();
                mouse.moveSlightly();
            break;
            case 2:
                mouse.moveSlightly();
                mouse.moveSlightly();
                mouse.moveSlightly();
                mouse.moveSlightly();
                mouse.moveSlightly();
                mouse.moveSlightly();
            break;
        }
    }
	
	private void AntiBanCamera() {
		int randomNum = random(1, 50);
		if (randomNum == 1 || randomNum == 2 || randomNum == 3) {
			camera.moveRandomly(random(2000, 5500));
		}
		if (randomNum == 4 || randomNum == 5) {
		    camera.setAngle(random(10, 40));
		}
		if (randomNum == 6) {
		    camera.setPitch(random(40, 68));
		}
	    if (randomNum == 7) {
		    camera.setPitch(random(20, 45));
		}
		if (randomNum == 8) {
			camera.setPitch(random(68, 90));
		} else {
		    sleep(50, 70);
		}
	}
	
	public void XPcheck() {
		if (random(0, 248) == 137) {
			game.openTab(Game.TAB_STATS);
			skills.doHover(Skills.INTERFACE_HERBLORE);
			sleep(random(1500, 2250));
			game.openTab(Game.TAB_INVENTORY);
		}
    }
	
	 public int AntiBans() {
        switch (random(0, 17)) {
            case 8:
                superAntiMoveMouse();
                AntiBanCamera();
            break;
            case 9:
				if (Unfs == false) {
					XPcheck();
				}
            break;
            case 10:
                AntiBanCamera();
            break;
            case 13:
                superAntiMoveMouse();
                AntiBanCamera();
            break;
            case 14:
				if (Unfs == false) {
					XPcheck();
				}
                AntiBanCamera();
            break;
            default:
                break;
        }
        return 500;
    }


	public int loop() {
		try {
			if (StartedScript == true && !bank.isOpen()) {
				AntiBans();
			}
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
			if (HasItemIDs == true) {
				if (Cleaning == true) {
					while (bank.isOpen()) {
						if (inventory.contains(CleanID)) {
							bank.depositAll();
							sleep(800, 1000);
						}
						if (!inventory.contains(CleanID) && bank.getItem(GrimyID) != null && withdraw(GrimyID, 0)) {
							bank.close();
							sleep(random(100, 200));
						} else {
							if (bank.getItem(GrimyID) == null) {
								log("Out of supplies...");
								game.logout(false);
								stopScript();
							}
						}
						if (inventory.contains(GrimyID) && !inventory.contains(CleanID)) {
							bank.close();
							sleep(random(100, 200));
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
						if (inventory.contains(UnfID) && bank.depositAll()) {
							sleep(800, 1000);
						}
						if (inventory.getCount() == 0 && bank.getItem(VialID) != null) {
							bank.withdraw(VialID, 14);
						} else {
							if (bank.getItem(VialID) == null) {
								log("Out of supplies...");
								game.logout(false);
								stopScript();
							}
						}
						if (inventory.contains(VialID) && inventory.getCount(VialID) < 15 && !inventory.contains(CleanID) && bank.getItem(CleanID) != null) {
							bank.withdraw(CleanID, 0);
						} else {
							if (inventory.getCount(VialID) > 14) {
								bank.depositAll();
								sleep(800, 1000);
							}
							if (bank.getItem(CleanID) == null) {
								log("Out of supplies...");
								game.logout(false);
								stopScript();
							}
						}
						if (inventoryContainsBoth(VialID, CleanID)) {
							bank.close();
							sleep(random(100, 200));
						}
						break;
					}
					while (!bank.isOpen()) {
						if (getMyPlayer().getAnimation() != -1) {
							this.curTime = System.currentTimeMillis();
						}
						if (!interfaces.get(905).getComponent(14).isValid() && inventoryContainsBoth(VialID, CleanID) && MakeUnfs()) {
							sleep(1000, 1250);
						}
						if (interfaces.get(905).getComponent(14).isValid() && clickInterface(905, 14)) {
							sleep(1500, 1750);
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
							sleep(800, 1000);
						}
						if (inventory.getCount() == 0 && bank.getItem(UnfID) != null) {
							bank.withdraw(UnfID, 14);
						} else {
							if (bank.getItem(UnfID) == null) {
								log("Out of supplies...");
								game.logout(false);
								stopScript();
							}
						}
						if (inventory.contains(UnfID) && inventory.getCount(UnfID) < 15 && !inventory.contains(IngredientID) && bank.getItem(IngredientID) != null) {
							bank.withdraw(IngredientID, 0);
						} else {
							if (inventory.getCount(UnfID) > 14) {
								bank.depositAll();
								sleep(800, 1000);
							}
							if (bank.getItem(IngredientID) == null) {
								log("Out of supplies...");
								game.logout(false);
								stopScript();
							}
						}
						if (inventoryContainsBoth(UnfID, IngredientID)) {
							bank.close();
							sleep(random(100, 200));
						}
						break;
					}
					while (!bank.isOpen()) {
						if (getMyPlayer().getAnimation() != -1) {
							this.curTime = System.currentTimeMillis();
						}
						if (!interfaces.get(905).getComponent(14).isValid() && inventoryContainsBoth(UnfID, IngredientID) && MakeCompletes()) {
							sleep(1000, 1250);
						}
						if (interfaces.get(905).getComponent(14).isValid() && clickInterface(905, 14)) {
							sleep(1500, 1750);
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
							bank.depositAll();
							sleep(800, 1000);
						}
						if (inventory.getCount() == 0 && bank.getItem(VialID) != null) {
							bank.withdraw(VialID, 14);
						} else {
							if (bank.getItem(VialID) == null) {
								log("Out of supplies...");
								game.logout(false);
								stopScript();
							}
						}
						if (inventory.contains(VialID) && inventory.getCount(VialID) < 15 && !inventory.contains(CleanID) && bank.getItem(CleanID) != null) {
							bank.withdraw(CleanID, 0);
						} else {
							if (inventory.getCount(VialID) > 14) {
								bank.depositAll();
								sleep(800, 1000);
							}
							if (bank.getItem(CleanID) == null) {
								log("Out of supplies...");
								game.logout(false);
								stopScript();
							}
						}
						if (inventoryContainsBoth(VialID, CleanID) || inventoryContainsBoth (UnfID, IngredientID)) {
							bank.close();
						}
						if (inventory.contains(UnfID) && !inventory.contains(IngredientID) && bank.getItem(IngredientID) != null) {
							bank.withdraw(IngredientID, 0);
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
						if (!interfaces.get(905).getComponent(14).isValid() && inventoryContainsBoth(VialID, CleanID) && MakeUnfs()) {
							sleep(1000, 1250);
						}
						if (!interfaces.get(905).getComponent(14).isValid() && inventoryContainsBoth(UnfID, IngredientID) && MakeCompletes()) {
							sleep(1000, 1250);
						}
						if (interfaces.get(905).getComponent(14).isValid() && clickInterface(905, 14)) {
							sleep(1500, 1750);
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
							if (inventory.contains(OutcomeID) && bank.depositAll()) {
								sleep(800, 1000);
							}
							if (inventory.getCount() == 0 && bank.getItem(CompleteID) != null) {
								bank.withdraw(CompleteID, 14);
							} else {
								if (bank.getItem(CompleteID) == null) {
									log("Out of supplies...");
									game.logout(false);
									stopScript();
								}
							}
							if (inventory.contains(CompleteID) && inventory.getCount(CompleteID) < 15 && !inventory.contains(IngredientID) && bank.getItem(IngredientID) != null) {
								bank.withdraw(IngredientID, 0);
							} else {
								if (inventory.getCount(CompleteID) > 14) {
									bank.depositAll();
									sleep(800, 1000);
								}
								if (bank.getItem(IngredientID) == null) {
									log("Out of supplies...");
									game.logout(false);
									stopScript();
								}
							}
							if (inventoryContainsBoth(CompleteID, IngredientID)) {
								bank.close();
								sleep(random(100, 200));
							}
							break;
						}
						while (!bank.isOpen()) {
							if (getMyPlayer().getAnimation() != -1) {
								this.curTime = System.currentTimeMillis();
							}
							if (!interfaces.get(905).getComponent(14).isValid() && inventoryContainsBoth(CompleteID, IngredientID) && MakeExtremes()) {
								sleep(1000, 1250);
							}
							if (interfaces.get(905).getComponent(14).isValid() && clickInterface(905, 14)) {
								sleep(1500, 1750);
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
								bank.depositAllExcept(IngredientID);
								sleep(random(350, 425));
							}
							if (!inventory.contains(OutcomeID) && inventory.contains(IngredientID) && !inventory.contains(CompleteID) && bank.getItem(CompleteID) != null) {
								bank.withdraw(CompleteID, 0);
							} else {
								if (bank.getItem(CompleteID) == null) {
									log("Out of supplies...");
									game.logout(false);
									stopScript();
								}
							}
							if (inventoryContainsBoth(CompleteID, IngredientID)) {
								bank.close();
							}
							break;
						}
						while (!bank.isOpen()) {
							if (getMyPlayer().getAnimation() != -1) {
								this.curTime = System.currentTimeMillis();
							}
							if (!interfaces.get(905).getComponent(14).isValid() && inventoryContainsBoth(CompleteID, IngredientID) && MakeExtremes()) {
								sleep(1000, 1250);
							}
							if (interfaces.get(905).getComponent(14).isValid() && clickInterface(905, 14)) {
								sleep(1500, 1750);
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
							bank.depositAllExcept(Mortar, SwampTarID, CleanID);
							sleep(random(350, 425));
						}
						if (inventoryContainsBoth(Mortar, SwampTarID) && !inventory.contains(CleanID) && bank.getItem(CleanID) != null) {
							bank.withdraw(CleanID, 0);
						} else {
							if (bank.getItem(CleanID) == null) {
								log("Out of supplies...");
								game.logout(false);
								stopScript();
							}
						}
						if (inventoryContainsBoth(Mortar, SwampTarID) && inventory.contains(CleanID)) {
							bank.close();
						}
						break;
					}
					while (!bank.isOpen()) {
						if (getMyPlayer().getAnimation() != -1) {
							this.curTime = System.currentTimeMillis();
						}
						if (!interfaces.get(905).getComponent(14).isValid() && inventoryContainsBoth(SwampTarID, CleanID) && MakeTars()) {
							sleep(1000, 1250);
						}
						if (interfaces.get(905).getComponent(14).isValid() && clickInterface(905, 14)) {
							sleep(1500, 1750);
						}
						if (!inventoryContainsBoth(SwampTarID, CleanID) && inventory.contains(OutcomeID) && bank.open()) {
							bank.depositAllExcept(Mortar, SwampTarID, CleanID);
							sleep(random(350, 425));
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
	
    Image getImage(String url) {
        try {
            return ImageIO.read(new URL(url));
        } catch(IOException e) {
            return null;
        }
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
	
	final Color color1 = new Color(0, 0, 0, 175);

    final BasicStroke stroke1 = new BasicStroke(2);

    final Font font1 = new Font("Arial", 1, 9);

    public void onRepaint(Graphics g1) {
        if (StartedScript == true) {
			Graphics2D g = (Graphics2D) g1;
			if (StartEXP == 0) {
				StartEXP = skills.getCurrentExp(Skills.getIndex("Herblore"));
			}
			EXPGained = skills.getCurrentExp(Skills.getIndex("Herblore")) - StartEXP;
			g.setColor(color1);
			g.fillRect(7, 228, 111, 107);
			g.setColor(Color.WHITE);
			g.setStroke(stroke1);
			g.drawRect(7, 228, 111, 107);
			g.setFont(font1);
			g.drawString("Interactions: " + Made, 9, 241);
			g.drawString("Interactions/H:  " + (int) ((Made) * 3600000D / (System.currentTimeMillis() - Duration)), 9, 254);
			g.drawString("EXP Gained: " + EXPGained, 9, 273);
			g.drawString("EXP/H: " + (int) ((EXPGained) * 3600000D / (System.currentTimeMillis() - Duration)), 9, 285);
			// Progress Bar Start
			g.setColor(Color.GRAY);
			g.fillRect(12, 318, 100, 12);
			g.setColor(Color.WHITE);
			g.drawRect(12, 318, 100, 12);
			g.setColor(Color.GREEN);
			g.fillRect(12, 319, skills.getPercentToNextLevel(Skills.getIndex("Herblore")), 10);
			g.setColor(Color.BLACK);
			g.drawString(skills.getPercentToNextLevel(Skills.getIndex("Herblore")) + "%", 54, 328);
			// Progress Bar End
			g.setColor(color1);
			g.fillRect(7, 204, 111, 23);
			g.setColor(Color.WHITE);
			g.drawRect(7, 204, 111, 23);
			g.drawString("Role: " + Role, 9, 302);
			g.drawString("Duration: " + convertDurationToString(System.currentTimeMillis() - Duration), 9, 313);
			g.drawString("BW Herblore", 10, 213);
			g.drawString("By: BlackWood", 42, 224);
		}
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
	
	public class BWHerbloreGUI extends JFrame {
		
		private static final long serialVersionUID = 1L;
		
		public BWHerbloreGUI() {
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