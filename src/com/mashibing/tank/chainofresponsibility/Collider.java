package com.mashibing.tank.chainofresponsibility;

import com.mashibing.tank.AbstractGameObject;

import java.io.Serializable;

public interface Collider extends Serializable {
    //retuen true:chain go on return false:chain break
    public boolean collide(AbstractGameObject go1, AbstractGameObject go2);
}
