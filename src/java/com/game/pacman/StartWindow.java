package com.game.pacman;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

/**
 * @author SevenLightnapper
 *
 * This class generates the starting window of the application.
 *
 * Provides two options:
 * 1. Start the game
 * 2. Customize the game
 *
 * First option starts the game on an already fixed gaming field.
 * Second option lets you create a gaming field.
 */
public class StartWindow extends JFrame {

    /**
     * @constructor
     * This one is super long.
     */
    public StartWindow() {
        // make the startup window 600x300, black background, middle of the screen, exit by clicking button [x]
        setSize(600,300);
        getContentPane().setBackground(Color.black);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        // setting the logo image of the application
        ImageIcon logo = new ImageIcon();
        try {
            logo = new ImageIcon(ImageIO.read(this.getClass().getResource("/images/pacman_logo.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // register application's font
        try {
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, this.getClass().getResourceAsStream("/fonts/crackman.ttf")));
        } catch (IOException | FontFormatException e) {
            e.printStackTrace();
        }

        // on the starting window we'll see the application's logo near its upper (north) part
        setLayout(new BorderLayout());
        getContentPane().add(new JLabel(logo),BorderLayout.NORTH);

        // create a container for buttons on the starting window
        JPanel buttonsC = new JPanel();
        buttonsC.setBackground(Color.black);
        //buttonsC.setLayout(new FlowLayout(FlowLayout.LEADING,20,10));
        buttonsC.setLayout(new BoxLayout(buttonsC,BoxLayout.Y_AXIS));

        // create two buttons: one to start the game, another to customize it
        StartWindowButton startButton = new StartWindowButton("Start Game");
        StartWindowButton customButton = new StartWindowButton("Customize Game");

        // centralize buttons layout (width center, not height)
        startButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        customButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        // what shall the application do if user clicks the 'start' button
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //TODO: add game creation
                dispose();
            }
        });

        // what shall the application do if user clicks the 'customize' button
        customButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //TODO: add game customization
                dispose();
            }
        });

        // adding the buttons to the container
        buttonsC.add(startButton);
        buttonsC.add(customButton);

        // setting container to the window
        getContentPane().add(buttonsC);

        // console output
        System.out.print('\n');
        System.out.println("PacMan v1.0.0   Developed By : SevenLightnapper");
        System.out.println("-----------------------------------------------");

        // make the starting window visible
        setVisible(true);
    }
}
