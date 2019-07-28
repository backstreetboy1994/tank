package com.mashibing.tank;

import com.mashibing.tank.chainofresponsibility.BulletTankCollider;
import com.mashibing.tank.chainofresponsibility.BulletWallCollider;
import com.mashibing.tank.chainofresponsibility.Collider;
import com.mashibing.tank.chainofresponsibility.ColliderChain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Properties;

public class TankFrame extends Frame {
    public static final TankFrame INSTANCE = new TankFrame();

    private List<AbstractGameObject> objects;
    ColliderChain chain = new ColliderChain();

    //    public static final int GAME_WIDTH = Integer.parseInt(PropertyMgr.get("gameWidth"));
//    public static final int GAME_HEIGHT = Integer.parseInt(PropertyMgr.get("gameHeight"));

    public static final int GAME_WIDTH = 800;
    public static final int GAME_HEIGHT = 600;

    private GameModel gm = new GameModel();

    private TankFrame() {
        this.setTitle("tank war");
        this.setLocation(400, 100);
        this.setSize(GAME_WIDTH, GAME_HEIGHT);

        this.addKeyListener(new TankKeyListener());
    }

    @Override
    public void paint(Graphics g) {
        gm.paint(g);
    }

    Image offScreenImage = null;

    @Override
    public void update(Graphics g) {
        if (offScreenImage == null) {
            offScreenImage = this.createImage(GAME_WIDTH, GAME_HEIGHT);
        }
        Graphics gOffScreen = offScreenImage.getGraphics();
        Color c = gOffScreen.getColor();
        gOffScreen.setColor(Color.BLACK);
        gOffScreen.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT);
        gOffScreen.setColor(c);
        paint(gOffScreen);
        g.drawImage(offScreenImage, 0, 0, null);
    }

    private class TankKeyListener extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            gm.getMyTank().keyPressed(e);
        }

        @Override
        public void keyReleased(KeyEvent e) { gm.getMyTank().keyReleased(e); }
    }

    public GameModel getGm(){
        return this.gm;
    }
}
