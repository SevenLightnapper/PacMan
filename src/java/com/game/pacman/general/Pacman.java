package com.game.pacman.general;

import com.game.pacman.map.MoveType;
import com.game.pacman.utils.AWTEventStatusMessage;

import javax.imageio.ImageIO;
import javax.swing.Timer;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.Objects;

/**
 * @author kamilla
 * @description this class describes the pacman
 */
public class Pacman implements KeyListener {

    // move vars
    Timer moveTimer;
    ActionListener moveAL;
    public MoveType activeMove;
    MoveType todoMove;
    boolean isStuck = true;

    // animation vars
    Timer animTimer;
    ActionListener animAL;
    Image[] pac;
    int activeImage = 0;
    int addFactor = 1;

    public Point pixelPosition;
    public Point logicalPosition;

    private PacBoard parentBoard;


    /**
     * @constructor
     * @param x position
     * @param y position
     * @param pb game board {@link PacBoard}
     */
    public Pacman(int x, int y, PacBoard pb) {

        logicalPosition = new Point(x,y);
        pixelPosition = new Point(28*x,28*y);

        parentBoard = pb;

        pac = new Image[5];

        activeMove = MoveType.NONE;
        todoMove = MoveType.NONE;

        try {
            pac[0] = ImageIO.read(Objects.requireNonNull(this.getClass().getResource("/images/pac/pac0.png")));
            pac[1] = ImageIO.read(Objects.requireNonNull(this.getClass().getResource("/images/pac/pac1.png")));
            pac[2] = ImageIO.read(Objects.requireNonNull(this.getClass().getResource("/images/pac/pac2.png")));
            pac[3] = ImageIO.read(Objects.requireNonNull(this.getClass().getResource("/images/pac/pac3.png")));
            pac[4] = ImageIO.read(Objects.requireNonNull(this.getClass().getResource("/images/pac/pac4.png")));
        } catch (IOException e) {
            System.err.println("Cannot read images!!!");
        }

        // animation timer
        animAL = new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                activeImage = activeImage + addFactor;
                if (activeImage == 4 || activeImage == 0) {
                    addFactor *= -1;
                }
            }
        };
        animTimer = new Timer(40,animAL);
        animTimer.start();

        moveAL = new ActionListener() {
            public void actionPerformed(ActionEvent evt) {

                //update logical position
                if ((pixelPosition.x % 28 == 0) && (pixelPosition.y % 28 == 0)) {
                    if (!isStuck) {
                        switch (activeMove) {
                            case RIGHT:
                                logicalPosition.x++;
                                break;
                            case LEFT:
                                logicalPosition.x--;
                                break;
                            case UP:
                                logicalPosition.y--;
                                break;
                            case DOWN:
                                logicalPosition.y++;
                                break;
                        }
                        // send update message
                        parentBoard.dispatchEvent(new ActionEvent(this, AWTEventStatusMessage.UPDATE,null));
                    }
                    isStuck = true;
                    animTimer.stop();

                    if (todoMove != MoveType.NONE && isPossibleMove(todoMove)) {
                        activeMove = todoMove;
                        todoMove = MoveType.NONE;
                    }
                } else {
                    isStuck = false;
                    animTimer.start();
                }

                switch (activeMove) {
                    case RIGHT:
                        if ((pixelPosition.x >= (parentBoard.m_x-1) * 28) && parentBoard.isCustom) {
                            return;
                        }
                        if (logicalPosition.x >= 0 && logicalPosition.x < parentBoard.m_x - 1
                                && logicalPosition.y >= 0 && logicalPosition.y < parentBoard.m_y - 1) {
                            if (parentBoard.map[logicalPosition.x + 1][logicalPosition.y] > 0) {
                                return;
                            }
                        }
                        pixelPosition.x++;
                        break;
                    case LEFT:
                        if ((pixelPosition.x <= 0) && parentBoard.isCustom) {
                            return;
                        }
                        if (logicalPosition.x > 0 && logicalPosition.x < parentBoard.m_x - 1
                                && logicalPosition.y >= 0 && logicalPosition.y < parentBoard.m_y - 1) {
                            if (parentBoard.map[logicalPosition.x - 1][logicalPosition.y] > 0) {
                                return;
                            }
                        }
                        pixelPosition.x--;
                        break;
                    case UP:
                        if ((pixelPosition.y <= 0) && parentBoard.isCustom) {
                            return;
                        }
                        if (logicalPosition.x >= 0 && logicalPosition.x < parentBoard.m_x - 1
                                && logicalPosition.y >= 0 && logicalPosition.y < parentBoard.m_y - 1) {
                            if (parentBoard.map[logicalPosition.x][logicalPosition.y - 1] > 0) {
                                return;
                            }
                        }
                        pixelPosition.y--;
                        break;
                    case DOWN:
                        if ((pixelPosition.y >= (parentBoard.m_y - 1) * 28) && parentBoard.isCustom) {
                            return;
                        }
                        if (logicalPosition.x >= 0 && logicalPosition.x < parentBoard.m_x - 1
                                && logicalPosition.y >= 0 && logicalPosition.y < parentBoard.m_y - 1) {
                            if (parentBoard.map[logicalPosition.x][logicalPosition.y + 1] > 0) {
                                return;
                            }
                        }
                        pixelPosition.y++;
                        break;
                }

                // send message to PacBoard to check collision
                parentBoard.dispatchEvent(new ActionEvent(this, AWTEventStatusMessage.COLTEST,null));
            }
        };
        moveTimer = new Timer(9,moveAL);
        moveTimer.start();
    }

    /**
     * check for possible movement
     * @param move {@link MoveType}
     * @return true | false
     */
    public boolean isPossibleMove(MoveType move) {
        if (logicalPosition.x >= 0 && logicalPosition.x < parentBoard.m_x - 1
                && logicalPosition.y >= 0 && logicalPosition.y < parentBoard.m_y - 1) {
            switch (move) {
                case RIGHT:
                    return !(parentBoard.map[logicalPosition.x + 1][logicalPosition.y] > 0);
                case LEFT:
                    return !(parentBoard.map[logicalPosition.x - 1][logicalPosition.y] > 0);
                case UP:
                    return !(parentBoard.map[logicalPosition.x][logicalPosition.y - 1] > 0);
                case DOWN:
                    return !(parentBoard.map[logicalPosition.x][logicalPosition.y+1] > 0);
            }
        }
        return false;
    }

    /**
     * pacman image getter
     * @return current pacman image {@link Pacman#activeImage}
     * @see Image
     */
    public Image getPacmanImage(){
        return pac[activeImage];
    }

    @Override
    public void keyReleased(KeyEvent ke){
        // not used
    }

    @Override
    public void keyTyped(KeyEvent ke){
        // not used
    }

    /**
     * handle arrow keys
     * @param ke {@link KeyEvent}
     * @see <a href="https://spec-zone.ru/RU/Java/Docs/8/api/constant-values.html#java.awt.event.KeyEvent.VK_N">KeyEvent key codes</a>
     */
    @Override
    public void keyPressed(KeyEvent ke) {
        switch (ke.getKeyCode()) {
            case 37: // VK_LEFT
                todoMove = MoveType.LEFT;
                break;
            case 38: // VK_UP
                todoMove = MoveType.UP;
                break;
            case 39: // VK_RIGHT
                todoMove = MoveType.RIGHT;
                break;
            case 40: // VK_DOWN
                todoMove = MoveType.DOWN;
                break;
            case 82: // VK_R
                parentBoard.dispatchEvent(new ActionEvent(this, AWTEventStatusMessage.RESET,null));
                break;
        }
    }

}
