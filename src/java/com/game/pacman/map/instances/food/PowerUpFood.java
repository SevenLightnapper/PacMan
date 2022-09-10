package com.game.pacman.map.instances.food;

import java.awt.Point;

/**
 * @author SevenLightnapper
 * @description Position of evolved (depending on its type) food.
 */
public class PowerUpFood {

    public Point position;

    public PowerUpFood(int x, int y, int type){
        position = new Point(x,y);
        this.type = type;
    }

    public int type; //0-4

}
