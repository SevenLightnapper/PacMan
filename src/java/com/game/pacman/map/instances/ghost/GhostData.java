package com.game.pacman.map.instances.ghost;

/**
 * @author kamilla
 * @description This class describes ghosts:
 * <p>
 *     getters and setters plus a constructor.
 * </p>
 */
public class GhostData {
    private int x;
    private int y;
    private GhostType type;

    public GhostData(int x, int y, GhostType type) {
        this.x = x;
        this.y = y;
        this.type = type;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public GhostType getType() {
        return type;
    }

    public void setType(GhostType type) {
        this.type = type;
    }
}


