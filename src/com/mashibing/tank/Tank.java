package com.mashibing.tank;

import net.TankJoinMsg;

import java.awt.*;
import java.util.Random;
import java.util.UUID;

public class Tank extends AbstractGameObject {
    private int x, y;
    private Dir dir;
    private boolean moving = true;
    private boolean live = true;
    private Group group;
    private int height, width;
    private UUID id;

    private int oldX, oldY;
    private Rectangle rect;

    public static final int SPEED = 1;

    //记录键盘是否被按下

    public Tank(int x, int y, Dir dir, Group group) {
        this.x = x;
        this.y = y;
        this.dir = dir;
        this.group = group;
        this.height = ResourceMgr.bulletU.getHeight();
        this.width = ResourceMgr.bulletU.getWidth();

        this.rect = new Rectangle(x, y, width, height);
    }

    public Tank(TankJoinMsg msg) {
        this.x = msg.getX();
        this.y = msg.getY();
        this.dir = msg.getDir();
        this.group = msg.getGroup();
        this.moving = msg.isMoving();
        this.id = msg.getId();

        this.rect = new Rectangle(x, y, width, height);
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
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
                g.drawImage(this.group.equals(Group.BAD)?ResourceMgr.badTankL:ResourceMgr.goodTankL, x, y,null);
                break;
            case U:
                g.drawImage(this.group.equals(Group.BAD)?ResourceMgr.badTankU:ResourceMgr.goodTankU, x, y,null);
                break;
            case R:
                g.drawImage(this.group.equals(Group.BAD)?ResourceMgr.badTankR:ResourceMgr.goodTankR, x, y,null);
                break;
            case D:
                g.drawImage(this.group.equals(Group.BAD)?ResourceMgr.badTankD:ResourceMgr.goodTankD, x, y,null);
                break;
        }

        move();
        rect.x = x;
        rect.y = y;
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

    public void back(){
        this.x = oldX;
        this.y = oldY;
    }

    private void fire() {
        int bX = x + ResourceMgr.goodTankU.getWidth()/2 - ResourceMgr.bulletU.getWidth()/2;
        int bY = y + ResourceMgr.goodTankU.getHeight()/2 - ResourceMgr.bulletU.getHeight()/2;

        TankFrame.INSTANCE.getGm().add(new Bullet(bX,bY,dir,group));
    }

    private void boundsCheck() {
        if (x < 0 || y < 30 || x + width > TankFrame.GAME_WIDTH || y + height > TankFrame.GAME_HEIGHT) {
            this.back();
        }
    }

    public void die() {
        this.setLive(false);
        TankFrame.INSTANCE.getGm().add(new Explode(x,y));
    }

    public Rectangle getRect() {
        return rect;
    }

    public void setRect(Rectangle rect) {
        this.rect = rect;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }
}
