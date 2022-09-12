package com.game.pacman.general;

import com.game.pacman.map.MapData;
import com.game.pacman.map.instances.food.Food;
import com.game.pacman.map.instances.food.PowerUpFood;
import com.game.pacman.map.instances.ghost.Ghost;
import com.game.pacman.map.instances.ghost.GhostData;
import com.game.pacman.map.instances.ghost.types.CyanGhost;
import com.game.pacman.map.instances.ghost.types.PinkGhost;
import com.game.pacman.map.instances.ghost.types.RedGhost;
import com.game.pacman.utils.ImageHelper;
import com.game.pacman.utils.LoopPlayer;
import com.game.pacman.utils.AWTEventStatusMessage;
import com.game.pacman.utils.SoundPlayer;

import javax.imageio.ImageIO;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.AWTEvent;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Objects;

/**
 * @author kamilla
 * @description Monitors, sets and updates the game board.
 * Draws map instances.
 * Draws game score.
 * Monitors and draws game's state (won | lost | in-progress).
 */
public class PacBoard extends JPanel {

    protected Timer redrawTimer;
    protected ActionListener redrawAL;

    public int[][] map;
    protected Image[] mapSegments;

    private Image foodImage;
    protected Image[] pfoodImage;

    private Image goImage;
    private Image vicImage;

    public Pacman pacman;
    private ArrayList<Food> foods;
    private ArrayList<PowerUpFood> pufoods;
    private ArrayList<Ghost> ghosts;

    protected boolean isCustom = false;
    private boolean isGameOver = false;
    private boolean isWin = false;
    private boolean drawScore = false;
    private boolean clearScore = false;
    private int scoreToAdd = 0;

    private int score;
    protected JLabel scoreboard;

    protected LoopPlayer siren;
    private boolean mustReactivateSiren = false;
    protected LoopPlayer pac6;

    public Point ghostBase;

    public int m_x;
    public int m_y;

    protected MapData md_backup;
    protected PacWindow windowParent;

    /**
     * @constructor
     * @param scoreboard game's score
     * @param md map info {@link MapData}
     * @param pw game's window {@link PacWindow}
     */
    public PacBoard(JLabel scoreboard, MapData md, PacWindow pw){
        this.scoreboard = scoreboard;
        this.setDoubleBuffered(true);
        md_backup = md;
        windowParent = pw;
        
        m_x = md.getX();
        m_y = md.getY();
        this.map = md.getMap();

        this.isCustom = md.isCustom();
        this.ghostBase = md.getGhostBasePosition();

        pacman = new Pacman(md.getPacmanPosition().x,md.getPacmanPosition().y,this);
        addKeyListener(pacman);

        foods = new ArrayList<>();
        pufoods = new ArrayList<>();
        ghosts = new ArrayList<>();

        if (!isCustom) {
            for (int i = 0; i < m_x; i++) {
                for (int j = 0; j < m_y; j++) {
                    if (map[i][j] == 0)
                        foods.add(new Food(i, j));
                }
            }
        } else {
            foods = md.getFoodPositions();
        }

        pufoods = md.getPufoodPositions();

        ghosts = new ArrayList<>();
        for (GhostData gd : md.getGhostsData()) {
            switch (gd.getType()) {
                case RED:
                    ghosts.add(new RedGhost(gd.getX(), gd.getY(), this));
                    break;
                case PINK:
                    ghosts.add(new PinkGhost(gd.getX(), gd.getY(), this));
                    break;
                case CYAN:
                    ghosts.add(new CyanGhost(gd.getX(), gd.getY(), this));
                    break;
            }
        }

        setLayout(null);
        setSize(20*m_x,20*m_y);
        setBackground(Color.black);

        mapSegments = new Image[28];
        mapSegments[0] = null;
        for (int ms = 1; ms < 28; ms++) {
            try {
                mapSegments[ms] = ImageIO.read(Objects.requireNonNull(this.getClass().getResource("/images/map_segments/" + ms + ".png")));
            } catch (Exception ignored) {
            }
        }

        pfoodImage = new Image[5];
        for (int ms = 0; ms < 5; ms++) {
            try {
                pfoodImage[ms] = ImageIO.read(Objects.requireNonNull(this.getClass().getResource("/images/food/" + ms + ".png")));
            } catch (Exception ignored) {
            }
        }
        try {
            foodImage = ImageIO.read(Objects.requireNonNull(this.getClass().getResource("/images/food.png")));
            goImage = ImageIO.read(Objects.requireNonNull(this.getClass().getResource("/images/gameover.png")));
            vicImage = ImageIO.read(Objects.requireNonNull(this.getClass().getResource("/images/victory.png")));
        } catch (Exception ignored) {
        }

        redrawAL = new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                // draw board
                repaint();
            }
        };
        redrawTimer = new Timer(16,redrawAL);
        redrawTimer .start();

        siren = new LoopPlayer("siren.wav");
        pac6 = new LoopPlayer("pac6.wav");
        siren.start();
    }

    private void collisionTest() {
        Rectangle pr = new Rectangle(pacman.pixelPosition.x+13,pacman.pixelPosition.y+13,2,2);
        Ghost ghostToRemove = null;
        for (Ghost g : ghosts) {
            Rectangle gr = new Rectangle(g.pixelPosition.x,g.pixelPosition.y,28,28);

            if (pr.intersects(gr)) {
                if (!g.isDead()) {
                    if (!g.isWeak()) {
                        // game lost
                        siren.stop();
                        SoundPlayer.play("pacman_lose.wav");
                        pacman.moveTimer.stop();
                        pacman.animTimer.stop();
                        g.moveTimer.stop();
                        isGameOver = true;
                        scoreboard.setText("    Press R to play again!");
                        break;
                    } else {
                        // eat ghost
                        SoundPlayer.play("pacman_eatghost.wav");
                        drawScore = true;
                        scoreToAdd++;
                        if (ghostBase != null)
                            g.die();
                        else
                            ghostToRemove = g;
                    }
                }
            }
        }

        if (ghostToRemove != null) {
            ghosts.remove(ghostToRemove);
        }
    }

    /**
     * update game's (PacWindow's state) condition
     */
    private void update() {

        Food foodToEat = null;
        // check food to eat
        for (Food f : foods) {
            if(pacman.logicalPosition.x == f.position.x && pacman.logicalPosition.y == f.position.y)
                foodToEat = f;
        }
        if (foodToEat != null) {
            SoundPlayer.play("pacman_eat.wav");
            foods.remove(foodToEat);
            score++;
            scoreboard.setText("    Score : " + score);

            if (foods.size() == 0) {
                siren.stop();
                pac6.stop();
                SoundPlayer.play("pacman_intermission.wav");
                isWin = true;
                pacman.moveTimer.stop();
                for (Ghost g : ghosts) {
                    g.moveTimer.stop();
                }
            }
        }

        PowerUpFood puFoodToEat = null;
        // check pu food to eat
        for (PowerUpFood puf : pufoods) {
            if (pacman.logicalPosition.x == puf.position.x && pacman.logicalPosition.y == puf.position.y)
                puFoodToEat = puf;
        }
        if (puFoodToEat != null) {
            if (puFoodToEat.type == 0) {
                pufoods.remove(puFoodToEat);
                siren.stop();
                mustReactivateSiren = true;
                pac6.start();
                for (Ghost g : ghosts) {
                    g.weaken();
                }
                scoreToAdd = 0;
            } else {
                SoundPlayer.play("pacman_eatfruit.wav");
                pufoods.remove(puFoodToEat);
                scoreToAdd = 1;
                drawScore = true;
            }
        }

        // check ghost undie
        for (Ghost g:ghosts) {
            if (g.isDead() && g.logicalPosition.x == ghostBase.x && g.logicalPosition.y == ghostBase.y) {
                g.undie();
            }
        }

        // check isSiren
        boolean isSiren = true;
        for (Ghost g:ghosts) {
            if (g.isWeak()) {
                isSiren = false;
            }
        }
        if (isSiren) {
            pac6.stop();
            if (mustReactivateSiren) {
                mustReactivateSiren = false;
                siren.start();
            }
        }
    }

    /**
     * Draw map instances, score, game's status
     * @param g the <code>Graphics</code> object to protect
     * @see Graphics
     * @see javax.swing.JComponent#paintComponent(Graphics g)
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        //DEBUG ONLY !!!
        /*for(int ii=0;ii<=m_x;ii++){
            g.drawLine(ii*28+10,10,ii*28+10,m_y*28+10);
        }
        for(int ii=0;ii<=m_y;ii++){
            g.drawLine(10,ii*28+10,m_x*28+10,ii*28+10);
        }*/

        // draw walls
        g.setColor(Color.blue);
        for (int i = 0; i < m_x; i++) {
            for (int j = 0; j < m_y; j++) {
                if (map[i][j] > 0) {
                    g.drawImage(mapSegments[map[i][j]],10+i*28,10+j*28,null);
                }
            }
        }

        // draw food
        g.setColor(new Color(204, 122, 122));
        for(Food f : foods){
            g.drawImage(foodImage,10+f.position.x*28,10+f.position.y*28,null);
        }

        // draw PowerUp food
        g.setColor(new Color(204, 174, 168));
        for (PowerUpFood f : pufoods) {
            g.drawImage(pfoodImage[f.type],10+f.position.x*28,10+f.position.y*28,null);
        }

        // draw pacman
        switch (pacman.activeMove) {
            case NONE:
            case RIGHT:
                g.drawImage(pacman.getPacmanImage(),10+pacman.pixelPosition.x,10+pacman.pixelPosition.y,null);
                break;
            case LEFT:
                g.drawImage(ImageHelper.flipHor(pacman.getPacmanImage()),10+pacman.pixelPosition.x,10+pacman.pixelPosition.y,null);
                break;
            case DOWN:
                g.drawImage(ImageHelper.rotate90(pacman.getPacmanImage()),10+pacman.pixelPosition.x,10+pacman.pixelPosition.y,null);
                break;
            case UP:
                g.drawImage(ImageHelper.flipVer(ImageHelper.rotate90(pacman.getPacmanImage())),10+pacman.pixelPosition.x,10+pacman.pixelPosition.y,null);
                break;
        }

        // draw ghosts
        for (Ghost gh : ghosts) {
            g.drawImage(gh.getGhostImage(),10 + gh.pixelPosition.x,10 + gh.pixelPosition.y,null);
        }

        // reset score
        if (clearScore) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            drawScore = false;
            clearScore = false;
        }
        // draw score
        if (drawScore) {
            g.setFont(new Font("Arial",Font.BOLD,15));
            g.setColor(Color.yellow);
            int s = scoreToAdd*100;
            g.drawString(Integer.toString(s), pacman.pixelPosition.x + 13, pacman.pixelPosition.y + 50);
            score += s;
            scoreboard.setText("    Score : " + score);
            clearScore = true;
        }
        // game lost
        if (isGameOver) {
            g.drawImage(goImage,this.getSize().width/2-315,this.getSize().height/2-75,null);
        }
        // game won
        if (isWin) {
            g.drawImage(vicImage,this.getSize().width/2-315,this.getSize().height/2-75,null);
        }

    }


    @Override
    public void processEvent(AWTEvent ae) {

        if (ae.getID() == AWTEventStatusMessage.UPDATE) {
            update();
        } else if (ae.getID() == AWTEventStatusMessage.COLTEST) {
            if (!isGameOver) {
                collisionTest();
            }
        } else if (ae.getID() == AWTEventStatusMessage.RESET) {
            if (isGameOver)
                restart();
        } else {
            super.processEvent(ae);
        }
    }
    
    public void restart() {
        siren.stop();

        new PacWindow();
        windowParent.dispose();
    }
}
