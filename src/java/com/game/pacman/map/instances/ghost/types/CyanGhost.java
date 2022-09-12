package com.game.pacman.map.instances.ghost.types;

import com.game.pacman.general.PacBoard;
import com.game.pacman.map.MoveType;
import com.game.pacman.map.instances.ghost.Ghost;
import com.game.pacman.map.instances.ghost.GhostType;
import com.game.pacman.utils.ImageHelper;

import javax.imageio.ImageIO;
import java.awt.Image;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author kamilla
 * @description This class describes the cyan ghost {@link GhostType#CYAN}
 * @see Ghost
 */
public class CyanGhost extends Ghost {

    public CyanGhost(int x, int y, PacBoard pb){
        super(x,y,pb,9);
    }

    /**
     * load images for pink ghost {@link GhostType#CYAN}
     * @see images.ghost.cyan
     */
    @Override
    public void loadImages(){
        ghostR = new Image[2];
        ghostL = new Image[2];
        ghostU = new Image[2];
        ghostD = new Image[2];
        try {
            ghostR[0] = ImageIO.read(Objects.requireNonNull(this.getClass().getResource("/images/ghost/cyan/1.png")));
            ghostR[1] = ImageIO.read(Objects.requireNonNull(this.getClass().getResource("/images/ghost/cyan/3.png")));
            ghostL[0] = ImageHelper.flipHor(ImageIO.read(Objects.requireNonNull(this.getClass().getResource("/images/ghost/cyan/1.png"))));
            ghostL[1] = ImageHelper.flipHor(ImageIO.read(Objects.requireNonNull(this.getClass().getResource("/images/ghost/cyan/3.png"))));
            ghostU[0] = ImageIO.read(Objects.requireNonNull(this.getClass().getResource("/images/ghost/cyan/4.png")));
            ghostU[1] = ImageIO.read(Objects.requireNonNull(this.getClass().getResource("/images/ghost/cyan/5.png")));
            ghostD[0] = ImageIO.read(Objects.requireNonNull(this.getClass().getResource("/images/ghost/cyan/6.png")));
            ghostD[1] = ImageIO.read(Objects.requireNonNull(this.getClass().getResource("/images/ghost/cyan/7.png")));
        } catch (IOException e) {
            System.err.println("Cannot read images!!!");
        }
    }

    MoveType lastCMove;
    MoveType pendMove = MoveType.UP;

    /**
     * ghost movement getter
     * @return AI move {@link MoveType}
     * @see Ghost#getMoveAI()
     */
    @Override
    public MoveType getMoveAI(){
        if (isPending) {
            if (isStuck) {
                if (pendMove == MoveType.UP) {
                    pendMove = MoveType.DOWN;
                } else if (pendMove == MoveType.DOWN) {
                    pendMove = MoveType.UP;
                }
                return pendMove;
            } else {
                return pendMove;
            }
        }
        if (isDead) {
            return baseReturner.getMove(logicalPosition.x,
                                        logicalPosition.y,
                                        parentBoard.ghostBase.x,
                                        parentBoard.ghostBase.y
            );
        } else {
            ArrayList<MoveType> pm = getPossibleMoves();
            int i = ThreadLocalRandom.current().nextInt(pm.size());
            lastCMove = pm.get(i);
            return lastCMove;
        }
    }
}
