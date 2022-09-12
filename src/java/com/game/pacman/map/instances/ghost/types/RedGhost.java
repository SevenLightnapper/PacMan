package com.game.pacman.map.instances.ghost.types;

import com.game.pacman.map.instances.ghost.GhostType;
import com.game.pacman.utils.BFSFinder;
import com.game.pacman.general.PacBoard;
import com.game.pacman.map.MoveType;
import com.game.pacman.map.instances.ghost.Ghost;
import com.game.pacman.utils.ImageHelper;

import javax.imageio.ImageIO;
import java.awt.Image;
import java.io.IOException;
import java.util.Objects;

/**
 * @author kamilla
 * @description This class describes the red ghost {@link GhostType#RED}
 * @see Ghost
 */
public class RedGhost extends Ghost {

    BFSFinder bfs;

    public RedGhost(int x, int y, PacBoard pb){
        super(x, y, pb, 12);
    }

    /**
     * load images for pink ghost {@link GhostType#RED}
     * @see images.ghost.red
     */
    @Override
    public void loadImages() {
        ghostR = new Image[2];
        ghostL = new Image[2];
        ghostU = new Image[2];
        ghostD = new Image[2];
        try {
            ghostR[0] = ImageIO.read(Objects.requireNonNull(this.getClass().getResource("/images/ghost/red/1.png")));
            ghostR[1] = ImageIO.read(Objects.requireNonNull(this.getClass().getResource("/images/ghost/red/3.png")));
            ghostL[0] = ImageHelper.flipHor(ImageIO.read(Objects.requireNonNull(this.getClass().getResource("/images/ghost/red/1.png"))));
            ghostL[1] = ImageHelper.flipHor(ImageIO.read(Objects.requireNonNull(this.getClass().getResource("/images/ghost/red/3.png"))));
            ghostU[0] = ImageIO.read(Objects.requireNonNull(this.getClass().getResource("/images/ghost/red/4.png")));
            ghostU[1] = ImageIO.read(Objects.requireNonNull(this.getClass().getResource("/images/ghost/red/5.png")));
            ghostD[0] = ImageIO.read(Objects.requireNonNull(this.getClass().getResource("/images/ghost/red/6.png")));
            ghostD[1] = ImageIO.read(Objects.requireNonNull(this.getClass().getResource("/images/ghost/red/7.png")));
        } catch (IOException e) {
            System.err.println("Cannot read images!!!");
        }
    }

    MoveType pendMove = MoveType.UP;

    /**
     * find the closest path using BFS
     * @return AI move
     * @see MoveType
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
        if (bfs == null)
            bfs = new BFSFinder(parentBoard);
        if (isDead) {
            return baseReturner.getMove(
                    logicalPosition.x,
                    logicalPosition.y,
                    parentBoard.ghostBase.x,
                    parentBoard.ghostBase.y
            );
        } else {
            return bfs.getMove(
                    logicalPosition.x,
                    logicalPosition.y,
                    parentBoard.pacman.logicalPosition.x,
                    parentBoard.pacman.logicalPosition.y
            );
        }
    }

}
