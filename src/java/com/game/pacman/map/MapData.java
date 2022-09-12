package com.game.pacman.map;

import com.game.pacman.map.instances.food.Food;
import com.game.pacman.map.instances.food.PowerUpFood;
import com.game.pacman.map.instances.ghost.GhostData;

import java.awt.Point;
import java.util.ArrayList;

/**
 * @author kamilla
 *
 * @description This class describes the gaming field's (application's map's) instances' positions.
 * <p>
 *     basically a bunch of getters and setters
 * </p>
 * <p>
 *     and a few constructors
 * </p>
 */
public class MapData {

    private int x;
    private int y;
    private int[][] map;
    private Point pacmanPosition;
    private Point ghostBasePosition;
    private boolean isCustom;
    private final ArrayList<Food> foodPositions;
    private final ArrayList<PowerUpFood> pufoodPositions;
    private final ArrayList<GhostData> ghostsData;

    /**
     * @constructor no args
     */
    public MapData() {
        foodPositions = new ArrayList<>();
        pufoodPositions = new ArrayList<>();
        ghostsData = new ArrayList<>();
    }

    /**
     * @constructor
     * @param x
     * @param y
     */
    public MapData(int x, int y) {
        this.x = x;
        this.y = y;

        foodPositions = new ArrayList<>();
        pufoodPositions = new ArrayList<>();
        ghostsData = new ArrayList<>();
    }

    /**
     * @constructor
     * @param x
     * @param y
     * @param map
     * @param pacPosition
     */
    public MapData(int x, int y, int[][] map, Point pacPosition) {
        this.x = x;
        this.y = y;
        this.map = map;
        pacmanPosition = pacPosition;

        foodPositions = new ArrayList<>();
        pufoodPositions = new ArrayList<>();
        ghostsData = new ArrayList<>();
    }

    /**
     * x getter
     * @return x
     */
    public int getX() {
        return x;
    }

    /**
     * x setter
     * @param x
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * y getter
     * @return y
     */
    public int getY() {
        return y;
    }

    /**
     * y setter
     * @param y
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * map getter
     * @return map
     */
    public int[][] getMap() {
        return map;
    }

    /**
     * map setter
     * @param map gaming field as two-dimensional matrix int[][]
     */
    public void setMap(int[][] map) {
        this.map = map;
    }

    /**
     * position getter
     * @return pacman's position
     */
    public Point getPacmanPosition() {
        return pacmanPosition;
    }

    /**
     * position setter
     * @param pacmanPosition pacman's position
     */
    public void setPacmanPosition(Point pacmanPosition) {
        this.pacmanPosition = pacmanPosition;
    }

    /**
     * position getter
     * @return ghost's base (starting) position
     */
    public Point getGhostBasePosition() {
        return ghostBasePosition;
    }

    /**
     * position setter
     * @param ghostBasePosition ghost's base (starting) position
     */
    public void setGhostBasePosition(Point ghostBasePosition) {
        this.ghostBasePosition = ghostBasePosition;
    }

    /**
     * position getter
     * @return list of food positions
     */
    public ArrayList<Food> getFoodPositions() {
        return foodPositions;
    }

    /**
     * position getter
     * @return PowerUp food's position
     */
    public ArrayList<PowerUpFood> getPufoodPositions() {
        return pufoodPositions;
    }

    /**
     * ghost data getter
     * @return ghost data
     */
    public ArrayList<GhostData> getGhostsData() {
        return ghostsData;
    }

    /**
     * is game custom
     * @return true | false
     */
    public boolean isCustom() {
        return isCustom;
    }

    /**
     * set game custom
     * @param custom true | false
     */
    public void setCustom(boolean custom) {
        isCustom = custom;
    }
}
