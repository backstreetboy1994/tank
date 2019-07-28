package com.mashibing.tank;

import java.awt.*;
import java.io.Serializable;

public abstract class AbstractGameObject implements Serializable {
    public abstract void paint(Graphics graphics);

    public abstract boolean isLive();
}
