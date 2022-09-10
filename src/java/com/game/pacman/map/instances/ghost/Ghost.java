package com.game.pacman.map.instances.ghost;

import com.game.pacman.utils.BFSFinder;
import com.game.pacman.general.PacBoard;
import com.game.pacman.map.MoveType;
import com.game.pacman.utils.AWTEventStatusMessage;

import javax.imageio.ImageIO;
import javax.swing.Timer;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author SevenLightnapper
 * This class is somewhat of a deserializer for Ghosts' types.
 */
public abstract class Ghost {
    //Anim Vars
    Timer animTimer;
    ActionListener animAL;

    //Pending Vars
    Timer pendingTimer;
    ActionListener pendingAL;

    //Move Vars
    public Timer moveTimer;
    ActionListener moveAL;
    public MoveType activeMove;
    protected boolean isStuck = true;
    protected boolean isPending = false;

    Timer unWeakenTimer1;
    Timer unWeakenTimer2;
    ActionListener unweak1;
    ActionListener unweak2;
    int unweakBlinks;
    boolean isWhite = false;

    protected boolean isWeak = false;
    protected boolean isDead = false;

    public boolean isWeak() {
        return isWeak;
    }

    public boolean isDead() {
        return isDead;
    }

    //Image[] pac;
    Image ghostImg;
    int activeImage = 0;
    int addFactor = 1;

    public Point pixelPosition;
    public Point logicalPosition;

    protected Image[] ghostR;
    protected Image[] ghostL;
    protected Image[] ghostU;
    protected Image[] ghostD;

    Image[] ghostW;
    Image[] ghostWW;
    Image ghostEye;

    int ghostNormalDelay;
    int ghostWeakDelay = 30;
    int ghostDeadDelay = 5;

    protected BFSFinder baseReturner;

    protected PacBoard parentBoard;

    public Ghost(int x, int y, PacBoard pb, int ghostDelay) {

        logicalPosition = new Point(x,y);
        pixelPosition = new Point(28*x,28*y);

        parentBoard = pb;

        activeMove = MoveType.RIGHT;

        ghostNormalDelay = ghostDelay;

        loadImages();

        //load weak Image
        ghostW = new Image[2];
        try {
            ghostW[0] = ImageIO.read(this.getClass().getResource("/images/ghost/blue/1.png"));
            ghostW[1] = ImageIO.read(this.getClass().getResource("/images/ghost/blue/3.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        ghostWW = new Image[2];
        try {
            ghostWW[0] = ImageIO.read(this.getClass().getResource("/images/ghost/white/1.png"));
            ghostWW[1] = ImageIO.read(this.getClass().getResource("/images/ghost/white/3.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            ghostEye = ImageIO.read(this.getClass().getResource("/images/eye.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        //animation timer
        animAL = new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                activeImage = (activeImage + 1) % 2;
            }
        };
        animTimer = new Timer(100,animAL);
        animTimer.start();

        moveAL = new ActionListener() {
            public void actionPerformed(ActionEvent evt) {

                if((pixelPosition.x % 28 == 0) && (pixelPosition.y % 28 == 0)){
                    if(!isStuck) {
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
                        parentBoard.dispatchEvent(new ActionEvent(this, AWTEventStatusMessage.UPDATE,null));
                    }


                    activeMove = getMoveAI();
                    isStuck = true;

                    //animTimer.stop();
                    //System.out.println("LOGICAL POS :" + logicalPosition.x + " , " + logicalPosition.y);
                    //if(todoMove != MoveType.NONE) {
                    //    activeMove = todoMove;
                    //    todoMove = MoveType.NONE;
                    //}
                }else{
                    isStuck = false;
                    //animTimer.start();
                }
                // }
                //TODO : fix ghost movements
                switch(activeMove){
                    case RIGHT:
                        if(pixelPosition.x >= (parentBoard.m_x-1) * 28){
                            return;
                        }
                        if((logicalPosition.x+1 < parentBoard.m_x) && (parentBoard.map[logicalPosition.x+1][logicalPosition.y]>0) && ((parentBoard.map[logicalPosition.x+1][logicalPosition.y]<26)||isPending)){
                            return;
                        }
                        pixelPosition.x ++;
                        break;
                    case LEFT:
                        if(pixelPosition.x <= 0){
                            return;
                        }
                        if((logicalPosition.x-1 >= 0) && (parentBoard.map[logicalPosition.x-1][logicalPosition.y]>0) && ((parentBoard.map[logicalPosition.x-1][logicalPosition.y]<26)||isPending)){
                            return;
                        }
                        pixelPosition.x --;
                        break;
                    case UP:
                        if(pixelPosition.y <= 0){
                            return;
                        }
                        if((logicalPosition.y-1 >= 0) && (parentBoard.map[logicalPosition.x][logicalPosition.y-1]>0) && ((parentBoard.map[logicalPosition.x][logicalPosition.y-1]<26)||isPending)){
                            return;
                        }
                        pixelPosition.y--;
                        break;
                    case DOWN:
                        if(pixelPosition.y >= (parentBoard.m_y-1) * 28){
                            return;
                        }
                        if((logicalPosition.y+1 < parentBoard.m_y) && (parentBoard.map[logicalPosition.x][logicalPosition.y+1]>0) && ((parentBoard.map[logicalPosition.x][logicalPosition.y+1]<26)||isPending)){
                            return;
                        }
                        pixelPosition.y ++;
                        break;
                }

                parentBoard.dispatchEvent(new ActionEvent(this, AWTEventStatusMessage.COLTEST,null));
            }
        };
        moveTimer = new Timer(ghostDelay,moveAL);
        moveTimer.start();

        unweak1 = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                unWeakenTimer2.start();
                unWeakenTimer1.stop();
            }
        };
        unWeakenTimer1 = new Timer(7000,unweak1);

        unweak2 = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(unweakBlinks == 10){
                    unweaken();
                    unWeakenTimer2.stop();
                }
                if(unweakBlinks % 2 == 0){
                    isWhite = true;
                }else{
                    isWhite = false;
                }
                unweakBlinks++;
            }
        };
        unWeakenTimer2 = new Timer(250,unweak2);


        pendingAL = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                isPending = false;
                pendingTimer.stop();
            }
        };
        pendingTimer = new Timer(7000,pendingAL);

        baseReturner = new BFSFinder(pb);
        //start AI
        activeMove = getMoveAI();

    }

    //load Images from Resource
    public abstract void loadImages();

    //get Move Based on AI
    public abstract MoveType getMoveAI();

    //get possible Moves
    public ArrayList<MoveType> getPossibleMoves(){
        ArrayList<MoveType> possibleMoves = new ArrayList<>();

        if(logicalPosition.x >= 0 && logicalPosition.x < parentBoard.m_x-1 && logicalPosition.y >= 0 && logicalPosition.y < parentBoard.m_y-1 ) {
            //System.out.println(this.toString());
            if (!(parentBoard.map[logicalPosition.x + 1][logicalPosition.y] > 0)) {
                possibleMoves.add(MoveType.RIGHT);
            }

            if (!(parentBoard.map[logicalPosition.x - 1][logicalPosition.y] > 0)) {
                possibleMoves.add(MoveType.LEFT);
            }

            if(!(parentBoard.map[logicalPosition.x][logicalPosition.y-1]>0)){
                possibleMoves.add(MoveType.UP);
            }

            if(!(parentBoard.map[logicalPosition.x][logicalPosition.y+1]>0)){
                possibleMoves.add(MoveType.DOWN);
            }
        }

        return possibleMoves;
    }

    public Image getGhostImage(){
        if(!isDead) {
            if (!isWeak) {
                switch (activeMove) {
                    case RIGHT:
                        return ghostR[activeImage];
                    case LEFT:
                        return ghostL[activeImage];
                    case UP:
                        return ghostU[activeImage];
                    case DOWN:
                        return ghostD[activeImage];
                }
                return ghostR[activeImage];
            } else {
                if (isWhite) {
                    return ghostWW[activeImage];
                } else {
                    return ghostW[activeImage];
                }
            }
        }else{
            return ghostEye;
        }
    }


    public void weaken(){
        isWeak = true;
        moveTimer.setDelay(ghostWeakDelay);
        unweakBlinks = 0;
        isWhite = false;
        unWeakenTimer1.start();
    }

    public void unweaken(){
        isWeak = false;
        moveTimer.setDelay(ghostNormalDelay);
    }

    public void die(){
        isDead = true;
        moveTimer.setDelay(ghostDeadDelay);
    }

    public void undie(){
        //Shift Left Or Right
        int r = ThreadLocalRandom.current().nextInt(3);
        if (r == 0) {
            //Do nothing
        }
        if(r==1){
            logicalPosition.x += 1;
            pixelPosition.x += 28;
        }
        if(r==2){
            logicalPosition.x -= 1;
            pixelPosition.x -= 28;
        }
        isPending = true;
        pendingTimer.start();

        isDead = false;
        isWeak = false;
        moveTimer.setDelay(ghostNormalDelay);
    }

}
