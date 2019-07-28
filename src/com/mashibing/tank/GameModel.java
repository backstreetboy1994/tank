package com.mashibing.tank;

import com.mashibing.tank.chainofresponsibility.ColliderChain;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class GameModel {
    private Player myTank;

    ColliderChain chain = new ColliderChain();

    List<AbstractGameObject> objects;

    public GameModel() {
        initGameObjects();
    }

    private void initGameObjects() {
        myTank = new Player(100, 100, Dir.R, Group.GOOD);

        objects = new ArrayList<>();

        int tankCount = Integer.parseInt(PropertyMgr.get("initTankCount"));

        for (int i = 0; i < tankCount; i++) {
            this.add(new Tank(200 + 80 * i, 300, Dir.D, Group.BAD));
        }
//        this.add(new Wall(300, 200, 400, 50));
    }

    public void add(AbstractGameObject go) {
        objects.add(go);
    }

    public void paint(Graphics g){
        myTank.paint(g);
        for (int i = 0; i < objects.size(); i++) {
            if (!objects.get(i).isLive()) {
                objects.remove(i);
                break;
            }
            AbstractGameObject go1 = objects.get(i);
            for (int j=0;j<objects.size();j++){
                AbstractGameObject go2 = objects.get(j);
                chain.collide(go1,go2);
            }
            if (objects.get(i).isLive()) {
                objects.get(i).paint(g);
            }
        }
    }

    public Player getMyTank(){
        return myTank;
    }

}
