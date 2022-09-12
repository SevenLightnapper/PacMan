package com.game.pacman.map.instances.food;

import java.awt.Point;

/**
 * @author kamilla
 * @description Class that describes food.
 * Food's location, to be precise.
 */
public class Food {

    // food's location
    public Point position;

    // constructor
    public Food(int x, int y){
        position = new Point(x,y);
    }

}
