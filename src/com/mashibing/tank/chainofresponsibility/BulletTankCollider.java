package com.mashibing.tank.chainofresponsibility;

import com.mashibing.tank.AbstractGameObject;
import com.mashibing.tank.Bullet;
import com.mashibing.tank.ResourceMgr;
import com.mashibing.tank.Tank;
import com.mashibing.tank.net.Client;
import com.mashibing.tank.net.TankDieMsg;

import java.awt.*;

public class BulletTankCollider implements Collider {
    @Override
    public boolean collide(AbstractGameObject go1, AbstractGameObject go2) {
        if (go1 instanceof Bullet && go2 instanceof Tank){
            Bullet b = (Bullet)go1;
            Tank t = (Tank)go2;
            //如果坦克死了就不撞了
            if (!t.isLive() || !b.isLive()) return false;
            //如果是自己的子弹，不撞
            if (b.getGroup() == t.getGroup()) return true;

            Rectangle rectTank = new Rectangle(t.getX(), t.getY(), ResourceMgr.goodTankU.getWidth(), ResourceMgr.goodTankU.getHeight());
            if (b.getRect().intersects(rectTank)){
                t.die();
                b.die();

                Client.INSTANCE.send(new TankDieMsg(t.getId(), b.getId()));
                return false;
            }
        }else if (go1 instanceof Tank && go2 instanceof Bullet){
            collide(go2,go1);
        }
        return true;
    }
}
