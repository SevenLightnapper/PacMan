package com.game.pacman.map;

import com.game.pacman.general.PacWindow;
import com.game.pacman.map.instances.food.Food;
import com.game.pacman.map.instances.food.PowerUpFood;
import com.game.pacman.map.instances.ghost.GhostData;
import com.game.pacman.map.instances.ghost.GhostType;
import com.game.pacman.utils.StartWindowButton;
import com.game.pacman.utils.StringHelper;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author kamilla
 * @description This class makes custom gaming field
 */
public class MapEditor extends JFrame {

    /**
     * @constructor
     */
    public MapEditor(){
        setSize(650,400); // window size
        setLocationRelativeTo(null); // middle of screen
        setDefaultCloseOperation(EXIT_ON_CLOSE); // exit by clicking [X]

        getContentPane().setBackground(Color.black); // background black

        JPanel sideBar = new JPanel(); // make sidebar
        sideBar.setLayout(new BorderLayout());
        sideBar.setBackground(Color.black);
        JPanel ghostSelection = new JPanel(); // make list of options
        ghostSelection.setLayout(new BoxLayout(ghostSelection, BoxLayout.Y_AXIS));
        ghostSelection.setBackground(Color.black);

        // options
        JLabel l0 = new JLabel("= : Blank Space (without Food)");
        JLabel l1 = new JLabel("_ : Blank Space (with Food)");
        JLabel l2 = new JLabel("X : Wall");
        JLabel l3 = new JLabel("Y : Semi-Wall (Passable by Ghosts)");
        JLabel l4 = new JLabel("P : Pacman Start Position");
        JLabel l5 = new JLabel("1 : Red Ghost (Chaser)");
        JLabel l6 = new JLabel("2 : Pink Ghost (Traveler)");
        JLabel l7 = new JLabel("3 : Cyan Ghost (Patrol)");
        JLabel l8 = new JLabel("F : Fruit");
        JLabel l9 = new JLabel("B : Ghost Base");

        // options' color
        l0.setForeground(Color.yellow);
        l1.setForeground(Color.yellow);
        l2.setForeground(Color.yellow);
        l3.setForeground(Color.yellow);
        l4.setForeground(Color.yellow);
        l5.setForeground(Color.yellow);
        l6.setForeground(Color.yellow);
        l7.setForeground(Color.yellow);
        l8.setForeground(Color.yellow);
        l9.setForeground(Color.yellow);

        // add options to list
        ghostSelection.add(l0);
        ghostSelection.add(l1);
        ghostSelection.add(l2);
        ghostSelection.add(l3);
        ghostSelection.add(l4);
        ghostSelection.add(l5);
        ghostSelection.add(l6);
        ghostSelection.add(l7);
        ghostSelection.add(l8);
        ghostSelection.add(l9);

        setLayout(new BorderLayout());
        sideBar.add(ghostSelection, BorderLayout.NORTH); // north part of sidebar
        getContentPane().add(sideBar, BorderLayout.EAST); // sidebar east

        JTextArea ta = new JTextArea(); // text area (editor)
        ta.setBackground(Color.black);
        ta.setForeground(Color.yellow);
        ta.setText("XXXXXXXXXX\n"
                +  "XP_______X\n"
                +  "X________X\n"
                +  "X________X\n"
                +  "XXXXXXXXXX");
        ta.setBorder(new CompoundBorder(new CompoundBorder(new EmptyBorder(20,10,20,10),new LineBorder(Color.yellow)),new EmptyBorder(10,10,10,10)));
        getContentPane().add(ta);

        // make start button
        StartWindowButton startButton = new StartWindowButton("Start Game");
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new PacWindow(compileMap(ta.getText()));
            }
        });
        sideBar.add(startButton,BorderLayout.SOUTH);

        // make visible
        setVisible(true);
    }

    /**
     * resolve (compile) map
     * @param input map in text (String) format
     * @return compiled custom map (MapData)
     */
    public static MapData compileMap(String input) {
        int mx = input.indexOf('\n');
        int my = StringHelper.countLines(input);
        System.out.println("Making Map " + mx + "x" + my);

        MapData customMap = new MapData(mx,my);
        customMap.setCustom(true);
        int[][] map = new int[mx][my];

        // fill map instances accordingly
        int i = 0;
        int j = 0;
        for (char c : input.toCharArray()) {
            if (c == '1') {
                map[i][j] = 0;
                customMap.getGhostsData().add(new GhostData(i, j, GhostType.RED));
            }
            if (c == '2') {
                map[i][j] = 0;
                customMap.getGhostsData().add(new GhostData(i, j, GhostType.PINK));
            }
            if (c == '3') {
                map[i][j] = 0;
                customMap.getGhostsData().add(new GhostData(i, j, GhostType.CYAN));
            }
            if (c == 'P') {
                map[i][j] = 0;
                customMap.setPacmanPosition(new Point(i,j));
            }
            if (c == 'X') {
                map[i][j] = 23;
            }
            if (c == 'Y') {
                map[i][j] = 26;
            }
            if (c == '_') {
                map[i][j] = 0;
                customMap.getFoodPositions().add(new Food(i,j));
            }
            if (c == '=') {
                map[i][j] = 0;
            }
            if (c == 'O') {
                map[i][j] = 0;
                customMap.getPufoodPositions().add(new PowerUpFood(i,j,0));
            }
            if (c == 'F') {
                map[i][j] = 0;
                customMap.getPufoodPositions().add(new PowerUpFood(i,j, ThreadLocalRandom.current().nextInt(4)+1));
            }
            if (c == 'B') {
                map[i][j] = 0;
                customMap.setGhostBasePosition(new Point(i,j));
            }
            i++;
            if (c == '\n') {
                j++;
                i=0;
            }
        }

        //Print map array
        /*for (int ii = 0; ii < my; ii++) {
            for (int jj = 0; jj < mx; jj++) {
                System.out.print(map[jj][ii] + " ");
            }
            System.out.print('\n');
        }*/

        customMap.setMap(map);
        customMap.setCustom(true);
        System.out.println("Map read OK!!!");
        return customMap;
    }
}
