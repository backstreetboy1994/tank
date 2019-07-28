package com.mashibing.tank.strategy;

import com.mashibing.tank.Player;

import java.io.Serializable;

public interface FireStrategy extends Serializable {
    public void fire(Player p);
}
