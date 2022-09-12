package com.game.pacman.general;

import com.game.pacman.map.MapData;
import com.game.pacman.map.MapEditor;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.WindowConstants;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.BorderLayout;
import java.awt.Color;
import java.util.Objects;
import java.util.Scanner;

/**
 * @author kamilla
 * @description Sets the application's game window. Adjusts the map.
 */
public class PacWindow extends JFrame {

    public PacWindow(){
        setTitle("PacMan v2.0.0"); // window title
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); // exit by [x]
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.black); // bg color black

        setSize(794,884); // window margins
        setLocationRelativeTo(null); // middle of screen

        JLabel scoreboard = new JLabel("    Score : 0"); // scoreboard (low left corner)
        scoreboard.setForeground(new Color(255, 243, 36)); // sb color

        MapData map1 = getMapFromResource("/maps/map1_c.txt"); // read map from file
        adjustMap(map1); // adjust map

        PacBoard pb = new PacBoard(scoreboard, map1,this); // make pac board

        pb.setBorder(new CompoundBorder(new EmptyBorder(10,10,10,10),new LineBorder(Color.BLUE)));
        addKeyListener(pb.pacman);

        this.getContentPane().add(scoreboard,BorderLayout.SOUTH);
        this.getContentPane().add(pb);
        setVisible(true);
    }

    /**
     * @constructor
     * @param md map info {@link MapData}
     */
    public PacWindow(MapData md) {
        setTitle("PacMan v2.0.0"); // window title
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); // exit by [x]
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.black); // bg color black

        setSize(794,884); // window margins
        setLocationRelativeTo(null); // middle of screen

        JLabel scoreboard = new JLabel("    Score : 0"); // scoreboard
        scoreboard.setForeground(new Color(255, 243, 36)); // sb color

        adjustMap(md); // adjust map
        PacBoard pb = new PacBoard(scoreboard,md,this); // create pac board
        pb.setBorder(new CompoundBorder(new EmptyBorder(10,10,10,10),new LineBorder(Color.BLUE)));
        addKeyListener(pb.pacman);

        this.getContentPane().add(scoreboard,BorderLayout.SOUTH);
        this.getContentPane().add(pb);
        setVisible(true);
    }

    /**
     * convert map from .txt file to {@link MapData}
     * @param relPath path to map file
     * @see maps
     * @return converted map {@link MapData}
     * @see MapEditor#compileMap(String mapStr)
     */
    public MapData getMapFromResource(String relPath) {
        String mapStr = "";
        try {
            Scanner scn = new Scanner(Objects.requireNonNull(this.getClass().getResourceAsStream(relPath)));
            StringBuilder sb = new StringBuilder();
            String line;
            while (scn.hasNextLine()) {
                line = scn.nextLine();
                sb.append(line).append('\n');
            }
            mapStr = sb.toString();
        } catch (Exception e) {
            System.err.println("Error reading map file!!!");
        }
        if ("".equals(mapStr)) {
            System.err.println("Map is empty!!!");
        }
        return MapEditor.compileMap(mapStr);
    }

    /**
     * Dynamically generate map segments
     * @see images.map_segments
     * @param mapData map info {@link MapData}
     * @see MapData#setMap(int[][] map)
     */
    public void adjustMap(MapData mapData) {
        int[][] map = mapData.getMap();
        int mx = mapData.getX();
        int my = mapData.getY();
        for (int y = 0; y < my; y++) {
            for (int x = 0; x < mx; x++) {
                boolean l = false;
                boolean r = false;
                boolean t = false;
                boolean b = false;
                boolean tl = false;
                boolean tr = false;
                boolean bl = false;
                boolean br = false;

                if (map[x][y] > 0 && map[x][y] < 26) {
                    int mustSet = 0;
                    // LEFT
                    if (x > 0 && map[x - 1][y] > 0 && map[x - 1][y] < 26) {
                        l = true;
                    }
                    // RIGHT
                    if (x < mx - 1 && map[x + 1][y] > 0 && map[x + 1][y] < 26) {
                        r = true;
                    }
                    // TOP
                    if (y > 0 && map[x][y - 1] > 0 && map[x][y - 1] < 26) {
                        t = true;
                    }
                    // Bottom
                    if (y < my - 1 && map[x][y + 1] > 0 && map[x][y + 1] < 26) {
                        b = true;
                    }
                    // TOP LEFT
                    if (x > 0 && y > 0 && map[x - 1][y - 1] > 0 && map[x - 1][y - 1] < 26) {
                        tl = true;
                    }
                    // TOP RIGHT
                    if (x < mx - 1 && y > 0 && map[x + 1][y - 1] > 0 && map[x + 1][y - 1] < 26) {
                        tr = true;
                    }
                    // Bottom LEFT
                    if (x > 0 && y < my - 1 && map[x - 1][y + 1] > 0 && map[x - 1][y + 1] < 26) {
                        bl = true;
                    }
                    // Bottom RIGHT
                    if (x < mx - 1 && y < my - 1 && map[x + 1][y + 1] > 0 && map[x + 1][y + 1] < 26) {
                        br = true;
                    }

                    // decide image to set
                    if (!r && !l && !t && !b) {
                        mustSet = 23;
                    }
                    if (r && !l && !t && !b) {
                        mustSet = 22;
                    }
                    if (!r && l && !t && !b) {
                        mustSet = 25;
                    }
                    if (!r && !l && t && !b) {
                        mustSet = 21;
                    }
                    if (!r && !l && !t && b) {
                        mustSet = 19;
                    }
                    if (r && l && !t && !b) {
                        mustSet = 24;
                    }
                    if (!r && !l && t && b) {
                        mustSet = 20;
                    }
                    if (r && !l && t && !b && !tr) {
                        mustSet = 11;
                    }
                    if (r && !l && t && !b && tr) {
                        mustSet = 2;
                    }
                    if (!r && l && t && !b && !tl) {
                        mustSet = 12;
                    }
                    if (!r && l && t && !b && tl) {
                        mustSet = 3;
                    }
                    if (r && !l && !t && b && br) {
                        mustSet = 1;
                    }
                    if (r && !l && !t && b && !br) {
                        mustSet = 10;
                    }
                    if (!r && l && !t && b && bl) {
                        mustSet = 4;
                    }
                    if (r && !l && t && b && !tr) {
                        mustSet = 15;
                    }
                    if (r && !l && t && b && tr) {
                        mustSet = 6;
                    }
                    if (!r && l && t && b && !tl) {
                        mustSet = 17;
                    }
                    if (!r && l && t && b && tl) {
                        mustSet = 8;
                    }
                    if (r && l && !t && b && !br) {
                        mustSet = 14;
                    }
                    if (r && l && !t && b && br) {
                        mustSet = 5;
                    }
                    if (r && l && t && !b && !tr) {
                        mustSet = 16;
                    }
                    if (r && l && t && !b && tr) {
                        mustSet = 7;
                    }
                    if (!r && l && !t && b && !bl) {
                        mustSet = 13;
                    }
                    if (r && l && t && b && br && tl) {
                        mustSet = 9;
                    }
                    if (r && l && t && b && !br && !tl) {
                        mustSet = 18;
                    }

                    //System.out.println("MAP SEGMENT : " + mustSet);
                    map[x][y] = mustSet;
                }
                mapData.setMap(map);
            }
        }
        System.out.println("Map adjusted! OK!!!");
    }

}