import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.rsbot.script.wrappers.RSObject;
import org.rsbot.script.methods.Game;
import org.rsbot.script.methods.Skills;
import org.rsbot.script.util.Filter;
import org.rsbot.script.Script;
import java.util.Random;
import org.rsbot.script.wrappers.RSPlayer;

import java.awt.*;
import javax.imageio.ImageIO;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;

import java.io.IOException;
import java.net.URL;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import org.rsbot.event.events.MessageEvent;
import org.rsbot.event.listeners.MessageListener;
import org.rsbot.event.listeners.PaintListener;
import org.rsbot.script.ScriptManifest;
import org.rsbot.script.wrappers.RSArea;
import org.rsbot.script.wrappers.RSPath;
import org.rsbot.script.wrappers.RSTile;

@ScriptManifest(authors = { "Wei Su" }, name = "AIO Flaxer", version = 0.09, description = "Picks and spins flax.. or both! by Wei Su", keywords = {
        "Lumbridge", "flax", "admin" })
/* Save as script.java - who could get that wrong! :) */
public class WeiFlax extends Script implements PaintListener, MessageListener,
        ActionListener, MouseMotionListener, MouseListener {

    public String option;
    public int flaxPicked;
    public int nullcheck = 0;
    public int startExp;
    public int currExp;
    public int flaxSpan;
    public int bsPrice;
    public int flaxPrice;
    public int sngProfit;
    public int totalProfit;
    public int startTimeDbl;
    public long startTime, millis, hours, minutes, seconds, last;
    public String Place;
    public int pckProf;
    public int xpGaineded;
    public RSArea camelot = new RSArea(2722, 3490, 2730, 3494);
    public RSArea lumbridges = new RSArea(3206, 3214, 3210, 3223);
    public boolean mayBegin = false;
    public int totalGain;

    @Override
    public boolean onStart() {

        new Thread(new Runnable() {
            public void run() {
                img1 = getImage("http://fc09.deviantart.net/fs71/f/2011/122/f/1/wei_su__s_paint_by_skyhigh1958-d3feox5.png");
                img2 = getImage("http://services.runescape.com/m=itemdb_rs/3338_obj_big.gif?id=1779");

            }
        }).start();

        FlaxerGui GUI = new FlaxerGui();
        GUI.setVisible(true);
        while (GUI.isVisible()) {
            sleep(50);
        }

        startTime = System.currentTimeMillis();
        sleep(random(1000, 1100));

        startExp = skills.getCurrentExp(skills.getIndex("Crafting"));
        bsPrice = grandExchange.lookup(1777).getGuidePrice();
        flaxPrice = grandExchange.lookup(1779).getGuidePrice();
        sngProfit = (bsPrice - flaxPrice);

        return true;
    }

    public void profitmade() {
        currExp = skills.getCurrentExp(skills.getIndex("Crafting"));
        flaxSpan = (currExp - startExp) / 15;
        totalProfit = (flaxSpan * sngProfit);
        pckProf = (flaxPrice * flaxPicked);
        xpGaineded = (currExp - startExp);
        totalGain = (pckProf+totalProfit);
    }

    public int loop() {
        antibanlist();
        profitmade();

        RSArea camelot = new RSArea(2722, 3490, 2730, 3494);
        RSArea lumbridges = new RSArea(3206, 3214, 3210, 3223);

        if (getMyPlayer().getAnimation() == -1) {
            nullcheck++;
        } else {
            nullcheck = 0;
        }

        // camelot hybrid
        if (option == "Camelot Hybrid") {
            CamelotHybrid();
            MiddleFloor2C();
            door();
            hybridtop();
            hybridground();
            hybridstairs();

        }

        // camelot picking
        if (option == "Camelot Picking") {
            camelotwalking();
            door();

        }

        if (option == "Lumbridge Spinning") {
            // Lumbridge
            RSObject Banker = objects.getNearest(36786);

            RSObject Topstairs = objects.getNearest(36775);
            RSObject Downstairs = objects.getNearest(36774);
            RSObject Spinner = objects.getNearest(36970);

            try {

                if (getMyPlayer().getAnimation() == -1) {
                    nullcheck++;
                } else {
                    nullcheck = 0;
                }
                if (Topstairs != null || Banker != null) {
                    TopFloor();
                    TopFloor2();
                }
                if (Downstairs != null || Spinner != null) {
                    MiddleFloor();
                    MiddleFloor2();
                }
            } catch (Exception ignore) {
            }
        }

        if (option == "Camelot Spinning") {
            // Camelot spinning
            door();
            RSObject Banker = objects.getNearest(25808);

            RSObject Topstairs = objects.getNearest(25939);
            RSObject Downstairs = objects.getNearest(25938);
            RSObject Spinner = objects.getNearest(25824);

            try {

                if (Topstairs != null || Banker != null) {
                    TopFloorC();
                    TopFloor2C();
                }
                if (Downstairs != null || Spinner != null) {
                    MiddleFloorC();
                    MiddleFloor2C();
                }
            } catch (Exception ignore) {
            }
        }
        return (random(300, 400));
    }

    @Override
    public void onFinish() {

    }

    // START: Code generated using Enfilade's Easel
    private Image getImage(String url) {
        try {
            return ImageIO.read(new URL(url));
        } catch (IOException e) {
            return null;
        }
    }

    private final Color color1 = new Color(0, 0, 0);

    private final Font font1 = new Font("Arial Rounded MT Bold", 0, 14);

    private Image img1;
    private Image img2;

    public void onRepaint(Graphics g1) {
        millis = System.currentTimeMillis() - startTime;
        hours = millis / (1000 * 60 * 60);
        millis -= hours * (1000 * 60 * 60);
        minutes = millis / (1000 * 60);
        millis -= minutes * (1000 * 60);
        seconds = millis / 1000;

        Graphics2D g = (Graphics2D) g1;

        g.drawImage(img1, 2, 304, null);
        g.setFont(font1);
        g.setColor(color1);
        g.drawString("Version: 0.09", 217, 330);
        g.drawString("Time elapsed: " + hours + ":" + minutes + ":" + seconds
                + "        EXP Gained: " + xpGaineded, 24, 376);
        g.drawString("Spinning Profit: " + totalProfit + "   " + flaxSpan + " Span,   " + flaxPicked + " Picked" , 24, 409);
        g.drawString("Picking Profit: " + pckProf + "        Total Profit " + totalGain, 23, 440);
        g.drawImage(img2, 304, 344, null);
        g.drawImage(img2, 342, 335, null);
        g.drawImage(img2, 357, 356, null);
        g.drawImage(img2, 306, 369, null);
        g.drawImage(img2, 345, 372, null);
    }

    // END: Code generated using Enfilade's Easel

    @Override
    public void mouseClicked(MouseEvent arg0) {
        // TODO Auto-generated method stub

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

    @Override
    public void mouseDragged(MouseEvent arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseMoved(MouseEvent arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void actionPerformed(ActionEvent arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void messageReceived(MessageEvent arg0) {

        if (arg0.getMessage().equals("You pick some flax.")) {

            flaxPicked++;
        }

    }

    // hybrid voids

    public void hybridground() {
        try {
            RSTile b2s = new RSTile(2716, 3472);
            RSTile s2b = new RSTile(2724, 3492);
            RSObject Spinner = objects.getNearest(1278, 1276);
            int flax = 1779;
            int bowstring = 1777;

            if (inventory.isFull() && inventory.containsAll(flax)) {
                if (getMyPlayer().getLocation().getX() > 2717) {
                    RSPath path = null;
                    if (path == null) {
                        path = walking.getPath(b2s);
                    }
                    path.traverse();

                }
            }

            if (inventory.isFull() && !inventory.contains(flax)) {
                if (getMyPlayer().getLocation().getX() > 2713) {
                    if(getMyPlayer().getLocation().getY()<3489){

                    RSPath path = null;
                    if (path == null) {
                        path = walking.getPath(s2b);
                    }
                    path.traverse();
                    }
                }
            }

        } catch (Exception ignore) {
        }

    }

    public void hybridtop() {
        try {
            int flax = 1779;
            RSTile s2b = new RSTile(2715, 3471);
            RSTile b2s = new RSTile(2711, 3471);

            RSObject Spinner = objects.getNearest(25824);
            if (Spinner != null) {
                if(getMyPlayer().getLocation().getX()<2716){
                    if (inventory.isFull() && !inventory.contains(flax)) {
                        RSPath path = null;
                        if (path == null) {
                            path = walking.getPath(s2b);
                        }
                    path.traverse();
                    }
                }
                if (inventory.contains(flax) && inventory.isFull()) {
                    RSPath path = null;
                    if (path == null) {
                        path = walking.getPath(b2s);
                    }
                    path.traverse();
                }
            }
        } catch (Exception ignore) {
        }

    }

    public void hybridstairs() {
        int flax = 1779;
        RSObject stairs = objects.getNearest(25938);
        RSObject Banker = objects.getNearest(25808);
        RSObject door = objects.getNearest(25819);
        RSObject opendoor = objects.getNearest(25820);
        try {
            if (inventory.isFull() && inventory.contains(flax)) {

                if (stairs.isOnScreen()) {

                    if (getMyPlayer().getLocation().getY() < 3484) {
                        if (opendoor.isOnScreen()) {
                            stairs.doAction("Climb");
                            sleep(random(1000, 1200));
                        }
                    }
                }
            }
        } catch (Exception ignore) {
        }
    }

    // ///Camelot hybrid
    public void CamelotHybrid() {
        try {
            RSObject flaxs = objects.getNearest(2646);
            RSTile flaxpath = new RSTile(2741, 3444);
            RSTile cambank = new RSTile(2725, 3464);
            RSObject CBank = objects.getNearest(25808);
            int flax = 1779;
            /*
             * if(nullcheck>=20){ if(inventory.isFull()){ RSPath path = null; if
             * (path == null) { path = walking.getPath(cambank); }
             * 
             * path.traverse(); }else { RSPath path1 = null; if (path1 == null)
             * { path1 = walking.getPath(flaxpath); } path1.traverse(); } }
             */
            if (inventory.isFull()) {
                if (!bank.isOpen()) {
                    if (CBank.isOnScreen()) {
                        CBank.doAction("Use-q");
                        sleep(2000, 3000);
                    } else {
                        RSObject door = objects.getNearest(25819);
                        try {
                            if (inventory.isFull() && !inventory.contains(flax)) {

                                if (door != null) {
                                    door.doAction("Open");
                                    sleep(random(400, 450));
                                }
                            }
                        } catch (Exception ignore) {
                        }
                    }
                } else {
                    bank.depositAll();
                }
            }

            if (!inventory.isFull()) {
                if (getMyPlayer().getLocation().getX() < 2736) {
                    RSPath path1 = null;
                    if (path1 == null) {
                        path1 = walking.getPath(flaxpath);
                    }
                    path1.traverse();
                } else {
                    if (getMyPlayer().getAnimation() == -1) {
                        flaxs.doAction("Pick");
                        sleep(random(400, 600));
                    }
                }
            }
        } catch (Exception ignore) {
        }
    }

    // ///Camelot picking
    public void camelotwalking() {
        try {
            RSObject flaxs = objects.getNearest(2646);
            RSTile flax = new RSTile(2741, 3444);
            RSTile cambank = new RSTile(2724, 3493);
            RSObject CBank = objects.getNearest(25808);

            if (nullcheck >= 20) {
                if (inventory.isFull()) {
                    RSPath path = null;
                    if (path == null) {
                        path = walking.getPath(cambank);
                    }
                } else {
                    RSPath path1 = null;
                    if (path1 == null) {
                        path1 = walking.getPath(flax);
                    }
                }
            }

            if (inventory.isFull()) {
                if (!bank.isOpen()) {
                    if (CBank.isOnScreen()) {
                        CBank.doAction("Use-q");
                        sleep(1000, 1200);
                    } else {
                        RSPath path1 = null;
                        if (path1 == null) {
                            path1 = walking.getPath(cambank);
                        }
                        path1.traverse();
                    }
                } else {
                    bank.depositAll();
                }
            } else {
                if (getMyPlayer().getLocation().getX() < 2736) {
                    RSPath path1 = null;
                    if (path1 == null) {
                        path1 = walking.getPath(flax);
                    }
                    path1.traverse();
                } else {
                    if (getMyPlayer().getAnimation() == -1) {
                        flaxs.doAction("Pick");
                        sleep(random(400, 600));
                    }
                }
            }
        } catch (Exception ignore) {
        }
    }

    // ////////////////////////////////////////////
    // ////////////////////////////////////////////
    // ////////////////////////////////////////////
    // ////////////////////////////////////////////
    // ////////////////////////////////////////////
    // //Camelot spinning BELOWWWWWWWWW!//////////////////
    // ////////////////////////////////////////////
    // ////////////////////////////////////////////
    // ////////////////////////////////////////////
    // ////////////////////////////////////////////
    // ////////////////////////////////////////////
    // ////////////////////////////////////////////
    /* WALKING ON FLOORS */

    public void door() {
        RSObject door = objects.getNearest(25819);
        try {
            if (getMyPlayer().getLocation().getY() > 3468) {
                if (getMyPlayer().getLocation().getX() > 2714) {
                    if (door.isOnScreen()) {
                        door.doAction("Open");
                        sleep(random(900, 950));
                    }
                }
            }
        } catch (Exception ignore) {
        }
    }

    public void TopFloorC() {
        try {
            RSTile b2s = new RSTile(2715, 3471);
            RSTile s2b = new RSTile(2724, 3493);
            RSObject Spinner = objects.getNearest(25824);
            int flax = 1779;

            if (Spinner == null) {

                if (inventory.contains(flax)) {
                    RSPath path = null;
                    if (path == null) {
                        path = walking.getPath(b2s);
                    }
                    path.traverse();
                }

                if (!inventory.contains(flax)) {
                    RSPath path = null;
                    if (path == null) {
                        path = walking.getPath(s2b);
                    }
                    path.traverse();

                }
            }
        } catch (Exception ignore) {
        }
    }

    public void MiddleFloorC() {
        try {
            int flax = 1779;
            RSTile b2s = new RSTile(2715, 3471);
            RSTile s2b = new RSTile(2711, 3471);
            RSObject Spinner = objects.getNearest(25824);
            if (Spinner != null) {
                if (inventory.contains(flax)) {
                    RSPath path = null;
                    if (path == null) {
                        path = walking.getPath(s2b);
                    }
                    path.traverse();
                }

                if (!inventory.contains(flax)) {
                    RSPath path = null;
                    if (path == null) {
                        path = walking.getPath(b2s);
                    }
                    path.traverse();
                }
            }
        } catch (Exception ignore) {
        }

    }

    /* Functioning */
    /* Top Floor */

    public void TopFloor2C() {
        new RSTile(2715, 3471);
        int flax = 1779;
        RSObject stairs = objects.getNearest(25938);
        RSObject Banker = objects.getNearest(25808);
        RSObject door = objects.getNearest(25819);
        try {
            if (inventory.contains(flax)) {

                if (stairs.isOnScreen()) {
                    if (getMyPlayer().getLocation().getY() < 3484) {
                        stairs.doAction("Climb");
                        sleep(random(1000, 1200));
                    }
                }
            } else {
                if (Banker.isOnScreen()) {
                    if (!bank.isOpen()) {
                        Banker.doAction("Use-quickly");
                    } else {
                        if (inventory.isFull()) {
                            bank.depositAll();
                        } else {
                            bank.withdraw(flax, 28);
                        }
                    }
                }

            }

        } catch (Exception ignore) {
        }
    }

    public void MiddleFloor2C() {
        try {

            int flax = 1779;
            RSObject Stairs = objects.getNearest(25939);
            RSObject Spinner = objects.getNearest(25824);
            if (inventory.contains(flax)) {
                if (nullcheck >= 5) {
                    Spinner.doAction("Spin");
                    nullcheck = 0;
                } else {
                    if (interfaces.getComponent(905, 16).getComponent(57)
                            .isValid()) {
                        interfaces.getComponent(905, 16).getComponent(57)
                                .doClick();
                    }
                }
            } else {
                if (!inventory.containsOneOf(flax)) {
                    if (Stairs.isOnScreen()) {
                        Stairs.doAction("Climb-down");
                        sleep(random(1000,2000));
                    }
                }
            }
        } catch (Exception ignore) {
        }
    }

    // ////////////////////////////////////////////
    // ////////////////////////////////////////////
    // ////////////////////////////////////////////
    // ////////////////////////////////////////////
    // ////////////////////////////////////////////
    // //LUMBRIDGE BELOWWWWWWWWW!//////////////////
    // ////////////////////////////////////////////
    // ////////////////////////////////////////////
    // ////////////////////////////////////////////
    // ////////////////////////////////////////////
    // ////////////////////////////////////////////
    // ////////////////////////////////////////////
    /* WALKING ON FLOORS */
    public void TopFloor() {
        try {
            RSTile b2s = new RSTile(3205, 3209);
            RSTile s2b = new RSTile(3208, 3220);
            int flax = 1779;

            if (inventory.containsAll(flax)) {
                RSPath path = null;
                if (path == null) {
                    path = walking.getPath(b2s);
                }
                path.traverse();
            } else {
                RSPath path = null;
                if (path == null) {
                    path = walking.getPath(s2b);
                }
                path.traverse();

            }
        } catch (Exception ignore) {
        }
    }

    public void MiddleFloor() {
        try {
            int flax = 1779;
            RSTile b2s = new RSTile(3205, 3209);
            RSTile s2b = new RSTile(3209, 3213);
            if (inventory.containsAll(flax)) {
                RSPath path = null;
                if (path == null) {
                    path = walking.getPath(s2b);
                }
                path.traverse();
            } else {
                RSPath path = null;
                if (path == null) {
                    path = walking.getPath(b2s);
                }
                path.traverse();

            }
        } catch (Exception ignore) {
        }
    }

    /* Functioning */
    /* Top Floor */
    public void TopFloor2() {
        new RSTile(3205, 3209);
        int flax = 1779;
        RSObject stairs = objects.getNearest(36775);
        RSObject Banker = objects.getNearest(36786);

        try {
            if (inventory.containsAll(flax)) {
                if (stairs.isOnScreen()) {
                    stairs.doAction("Climb");
                    sleep(random(500, 800));
                }
            } else {
                if (Banker.isOnScreen()) {
                    if (!bank.isOpen()) {
                        Banker.doAction("Use-quickly");
                    } else {
                        if (inventory.isFull()) {
                            bank.depositAll();
                        } else {
                            bank.withdraw(flax, 28);
                        }
                    }
                }

            }
        } catch (Exception ignore) {
        }
    }

    public void MiddleFloor2() {
        try {

            int flax = 1779;
            RSObject Stairs = objects.getNearest(36774);
            RSObject Spinner = objects.getNearest(36970);
            if (inventory.contains(flax)) {
                if (nullcheck >= 15) {
                    Spinner.doAction("Spin");
                    nullcheck = 0;
                } else {
                    if (interfaces.getComponent(905, 16).getComponent(57)
                            .isValid()) {
                        interfaces.getComponent(905, 16).getComponent(57)
                                .doClick();
                    }
                }
            } else {
                if (!inventory.containsOneOf(flax)) {
                    if (Stairs.isOnScreen()) {
                        Stairs.doAction("Climb-up");
                    }
                }
            }
        } catch (Exception ignore) {
        }
    }

    /*
     * ANTIBAN MADE BY CHKENMUFFIN THANKS FOR USING
     */

    public void chooserandomAFK() {
        switch (random(0, 4)) {
        case 0:
            sleep(random(500, 900));
            break;
        case 1:
            sleep(random(400, 1000));
            break;
        case 2:
            sleep(random(1000, 2000));
            break;
        case 3:
            sleep(random(1000, 3000));
            break;
        case 4:
            log("Not doing AFK");
            break;
        }
    }

    public void mouseHandler() {
        switch (random(0, 5)) {
        case 0:
            mouse.moveOffScreen();
            sleep(random(5000, 10000));
            break;
        case 1:
            mouse.moveOffScreen();
            sleep(random(8000, 15000));
            break;
        case 2:
            mouse.moveSlightly();
            break;
        case 3:
            mouse.moveSlightly();
            break;
        case 4:
            log("Not moving mouse");
            break;
        case 5:
            log("Taking offscreen break 30-60 secs");
            mouse.moveOffScreen();
            sleep(random(30000, 60000));
            break;
        }
    }

    public void superAntiMoveMouse() {
        switch (random(0, 10)) {
        case 0:
            log("Doing superAnti! Wiggling mouse a lot");
            mouse.setSpeed(random(6, 9));
            mouse.moveSlightly();
            mouse.moveSlightly();
            mouse.moveSlightly();
            break;
        case 1:
            log("Doing superAnti! Wiggling mouse ");
            mouse.setSpeed(random(6, 9));
            mouse.moveSlightly();
            mouse.moveSlightly();
            break;
        case 2:
            log("Doing superAnti! Wiggling mouse a lot");
            mouse.setSpeed(random(6, 9));
            mouse.moveSlightly();
            mouse.moveSlightly();
            mouse.moveSlightly();
            mouse.moveSlightly();
            mouse.moveSlightly();
            mouse.moveSlightly();
            break;
        }
    }

    public void randomtab() {
        switch (random(0, 12)) {
        case 0:
            game.openTab(Game.TAB_STATS);
            game.openTab(Game.TAB_INVENTORY);
            break;
        case 1:
            game.openTab(Game.TAB_INVENTORY);
            break;
        case 2:
            game.openTab(Game.TAB_CLAN);
            game.openTab(Game.TAB_INVENTORY);
            break;
        case 3:
            game.openTab(Game.TAB_FRIENDS);
            game.openTab(Game.TAB_INVENTORY);
            break;
        case 4:

        case 5:
            game.openTab(Game.TAB_EQUIPMENT);
            game.openTab(Game.TAB_INVENTORY);
            break;
        case 6:
            game.openTab(Game.TAB_MAGIC);
            game.openTab(Game.TAB_INVENTORY);
            break;
        case 7:
            game.openTab(Game.TAB_QUESTS);
            game.openTab(Game.TAB_INVENTORY);
            break;
        case 8:

        case 9:
            game.openTab(Game.TAB_NOTES);
            game.openTab(Game.TAB_INVENTORY);
            break;
        case 10:
            game.openTab(Game.TAB_PRAYER);
            game.openTab(Game.TAB_INVENTORY);
            break;
        case 11:
            game.openTab(Game.TAB_MUSIC);
            game.openTab(Game.TAB_INVENTORY);
            break;
        }
    }

    public void randomXPcheck() {
        game.openTab(Game.TAB_STATS);
        switch (random(0, 20)) {
        case 0:
            skills.doHover(Skills.INTERFACE_FISHING);
            sleep(random(2000, 3000));
            game.openTab(Game.TAB_INVENTORY);
            break;
        case 1:
            skills.doHover(Skills.INTERFACE_WOODCUTTING);
            sleep(random(2000, 3000));
            game.openTab(Game.TAB_INVENTORY);
            break;
        case 2:
            skills.doHover(Skills.INTERFACE_ATTACK);
            sleep(random(2000, 3000));
            game.openTab(Game.TAB_INVENTORY);
            break;
        case 3:
            skills.doHover(Skills.INTERFACE_STRENGTH);
            sleep(random(2000, 3000));
            game.openTab(Game.TAB_INVENTORY);
            break;
        case 4:
            skills.doHover(Skills.INTERFACE_COOKING);
            sleep(random(2000, 3000));
            game.openTab(Game.TAB_INVENTORY);
            break;
        case 5:
            skills.doHover(Skills.INTERFACE_RANGE);
            sleep(random(2000, 3000));
            game.openTab(Game.TAB_INVENTORY);
            break;
        case 6:
            skills.doHover(Skills.INTERFACE_FIREMAKING);
            sleep(random(2000, 3000));
            game.openTab(Game.TAB_INVENTORY);
            break;
        case 7:
            skills.doHover(Skills.INTERFACE_CONSTRUCTION);
            sleep(random(2000, 3000));
            game.openTab(Game.TAB_INVENTORY);
            break;
        case 8:
            skills.doHover(Skills.INTERFACE_RUNECRAFTING);
            sleep(random(2000, 3000));
            game.openTab(Game.TAB_INVENTORY);
            break;
        case 9:
            skills.doHover(Skills.INTERFACE_SUMMONING);
            sleep(random(2000, 3000));
            game.openTab(Game.TAB_INVENTORY);
            break;
        case 10:
            skills.doHover(Skills.INTERFACE_SLAYER);
            sleep(random(2000, 3000));
            game.openTab(Game.TAB_INVENTORY);
            break;
        case 11:
            skills.doHover(Skills.INTERFACE_CRAFTING);
            sleep(random(2000, 3000));
            game.openTab(Game.TAB_INVENTORY);
            break;
        case 12:
            skills.doHover(Skills.INTERFACE_FARMING);
            sleep(random(2000, 3000));
            game.openTab(Game.TAB_INVENTORY);
            break;
        case 13:
            skills.doHover(Skills.INTERFACE_AGILITY);
            sleep(random(2000, 3000));
            game.openTab(Game.TAB_INVENTORY);
            break;
        case 14:
            skills.doHover(Skills.INTERFACE_THIEVING);
            sleep(random(2000, 3000));
            game.openTab(Game.TAB_INVENTORY);
            break;
        case 15:
            skills.doHover(Skills.INTERFACE_HUNTER);
            sleep(random(2000, 3000));
            game.openTab(Game.TAB_INVENTORY);
            break;
        case 16:
            skills.doHover(Skills.INTERFACE_MINING);
            sleep(random(2000, 3000));
            game.openTab(Game.TAB_INVENTORY);
            break;
        case 17:
            skills.doHover(Skills.INTERFACE_SMITHING);
            sleep(random(2000, 3000));
            game.openTab(Game.TAB_INVENTORY);
            break;
        case 18:
            skills.doHover(Skills.INTERFACE_MAGIC);
            sleep(random(2000, 3000));
            game.openTab(Game.TAB_INVENTORY);
            break;
        case 19:
            skills.doHover(Skills.INTERFACE_FLETCHING);
            sleep(random(2000, 3000));
            game.openTab(Game.TAB_INVENTORY);
            break;
        case 20:
            skills.doHover(Skills.INTERFACE_PRAYER);
            sleep(random(2000, 3000));
            game.openTab(Game.TAB_INVENTORY);
            break;
        }
    }

    public int antibanlist() {
        switch (random(0, 300)) {
        case 0:
            chooserandomAFK();
            break;
        case 1:
        case 2:
        case 3:
            chooserandomAFK();
            break;
        case 4:
            mouse.moveSlightly();
            break;
        case 5:
            chooserandomAFK();
            break;
        case 6:
        case 7:
            mouseHandler();
            break;
        case 8:
            superAntiMoveMouse();
            break;
        case 9:
            randomXPcheck();
            break;
        case 10:
            randomtab();
            break;
        case 11:
        case 12:
            randomtab();
            break;
        case 13:
            superAntiMoveMouse();
            break;
        case 14:
            randomXPcheck();
            break;
        case 15:
        case 16:
        case 17:
            break;
        default:
            break;
        }
        return 500;
    }

    public class FlaxerGui extends JFrame {
        private static final long serialVersionUID = 1L;

        public FlaxerGui() {
            initComponents();
        }

        private void button1ActionPerformed(ActionEvent e) {
            option = comboBox1.getSelectedItem().toString();

            dispose();
        }

        private void initComponents() {
            label1 = new JLabel();
            label2 = new JLabel();
            comboBox1 = new JComboBox();
            checkBox1 = new JCheckBox();
            checkBox2 = new JCheckBox();
            label3 = new JLabel();
            label4 = new JLabel();
            checkBox3 = new JCheckBox();
            checkBox4 = new JCheckBox();
            checkBox5 = new JCheckBox();
            label5 = new JLabel();
            slider1 = new JSlider();
            label6 = new JLabel();
            label7 = new JLabel();
            panel1 = new JPanel();
            button1 = new JButton();
            // ======== this ========
            setResizable(false);
            setTitle("Wei Su's Flaxer! - settings");
            Container contentPane = getContentPane();
            contentPane.setLayout(null);
            setVisible(true);
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            // ---- label1 ----
            label1.setText("Wei Su's Flaxer!");
            label1.setFont(new Font("Snap ITC", Font.PLAIN, 30));
            contentPane.add(label1);
            label1.setBounds(new Rectangle(new Point(45, 10), label1
                    .getPreferredSize()));

            // ---- label2 ----
            label2.setText("Operation: ");
            contentPane.add(label2);
            label2.setBounds(new Rectangle(new Point(15, 60), label2
                    .getPreferredSize()));

            // ---- comboBox1 ----
            comboBox1.setModel(new DefaultComboBoxModel(new String[] {
                    "Lumbridge Spinning", "Camelot Spinning",
                    "Camelot Picking", "Camelot Hybrid", }));
            contentPane.add(comboBox1);
            comboBox1.setBounds(80, 55, 140, 25);

            //

            // /

            {
                panel1.setLayout(null);
                // ---- button1 ----
                button1.setText("Start script!");
                button1.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        button1ActionPerformed(e);
                    }
                });
                panel1.add(button1);
                button1.setBounds(80, 80, 140, 25);
                { // compute preferred size
                    Dimension preferredSize = new Dimension();
                    for (int i = 0; i < panel1.getComponentCount(); i++) {
                        Rectangle bounds = panel1.getComponent(i).getBounds();
                        preferredSize.width = 250;
                        preferredSize.height = 250;
                    }
                    Insets insets = panel1.getInsets();
                    preferredSize.width += insets.right;
                    preferredSize.height += insets.bottom;
                    panel1.setMinimumSize(preferredSize);
                    panel1.setPreferredSize(preferredSize);
                }
            }
            contentPane.add(panel1);
            panel1.setBounds(0, 0, 310, 400);
            { // compute preferred size
                Dimension preferredSize = new Dimension();
                for (int i = 0; i < contentPane.getComponentCount(); i++) {
                    Rectangle bounds = contentPane.getComponent(i).getBounds();
                    preferredSize.width = 300;
                    preferredSize.height = 150;
                }
                Insets insets = contentPane.getInsets();
                preferredSize.width += insets.right;
                preferredSize.height += insets.bottom;
                contentPane.setMinimumSize(preferredSize);
                contentPane.setPreferredSize(preferredSize);
            }
            pack();
            setLocationRelativeTo(getOwner());
        }

        private JLabel label1;
        private JLabel label2;
        private JComboBox comboBox1;
        private JCheckBox checkBox1;
        private JCheckBox checkBox2;
        private JLabel label3;
        private JLabel label4;
        private JCheckBox checkBox3;
        private JCheckBox checkBox4;
        private JCheckBox checkBox5;
        private JLabel label5;
        private JSlider slider1;
        private JLabel label6;
        private JLabel label7;
        private JPanel panel1;
        private JButton button1;
    }
}