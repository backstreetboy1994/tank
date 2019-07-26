package com.mashibing.tank;

import java.awt.*;
import java.util.Random;

public class Tank {
    private int x, y;
    private Dir dir;
    private boolean moving = true;
    private boolean live = true;
    private Group group;
    private int height, width;

    private int oldX, oldY;

    public static final int SPEED = 1;

    //记录键盘是否被按下

    public Tank(int x, int y, Dir dir, Group group) {
        this.x = x;
        this.y = y;
        this.dir = dir;
        this.group = group;
        this.height = ResourceMgr.bulletU.getHeight();
        this.width = ResourceMgr.bulletU.getWidth();
    }

    public boolean isLive() { return live; }

    public void setLive(boolean live) { this.live = live; }

    public int getX() { return x; }

    public int getY() { return y; }

    public void paint(Graphics g) {
        //如果坦克死了就不画了
        if (!this.isLive()) return;

        switch (dir){
            case L:
                g.drawImage(ResourceMgr.badTankL, x, y,null);
                break;
            case U:
                g.drawImage(ResourceMgr.badTankU, x, y,null);
                break;
            case R:
                g.drawImage(ResourceMgr.badTankR, x, y,null);
                break;
            case D:
                g.drawImage(ResourceMgr.badTankD, x, y,null);
                break;
        }

        move();
    }

    private void move() {
        if (!moving) return;
        oldX = x;
        oldY = y;
        switch (dir){
            case L:
                x -= SPEED;
                break;
            case U:
                y -= SPEED;
                break;
            case R:
                x += SPEED;
                break;
            case D:
                y += SPEED;
                break;
        }

        boundsCheck();
        randomDir();
        if (r.nextInt(500) > 490)
            fire();
    }

    private Random r = new Random();
    private void randomDir() {
        if (r.nextInt(500) > 490)
            this.dir = Dir.randomDir();
    }

    private void fire() {
        int bX = x + ResourceMgr.goodTankU.getWidth()/2 - ResourceMgr.bulletU.getWidth()/2;
        int bY = y + ResourceMgr.goodTankU.getHeight()/2 - ResourceMgr.bulletU.getHeight()/2;

        TankFrame.INSTANCE.add(new Bullet(bX,bY,dir,group));
    }

    private void boundsCheck() {
        if (x < 0 || y < 30 || x + width > TankFrame.GAME_WIDTH || y + height > TankFrame.GAME_HEIGHT) {
            this.x = oldX;
            this.y = oldY;
        }
    }

    public void die() {
        this.setLive(false);
        TankFrame.INSTANCE.add(new Explode(x,y));
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }
}