package com.mashibing.tank;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public class Wall extends AbstractGameObject {
    private int x, y, w, h;
    private Rectangle rect;

    public Wall(int x, int y, int w, int h) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;

        rect = new Rectangle(x,y,w,h);
    }

    public void paint(Graphics g){
        Color c = g.getColor();
        g.setColor(Color.GRAY);
        g.fillRect(x,y,w,h);
        g.setColor(c);
    }

    public Rectangle getRect() {
        return rect;
    }

    @Override
    public boolean isLive() {
        return true;
    }
}
