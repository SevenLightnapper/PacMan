package com.game.pacman.utils;

import javax.swing.JLabel;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;

/**
 * @author kamilla
 *
 * @description This class makes custom buttons to showcase on the starting window
 * and monitors if any custom button has been pressed.<p>
 * To make a button, you have to assign a value (name) to it,
 * when creating an object of this class.<p>
 * In the constructor, the button will receive its name in the application's font,
 * it will also receive a background color (yellow).<p>
 * In the constructor, a mouse will be registered (a listener will be set).<p>
 * When a mouse hovers over a custom button,
 * the said button will change color (orange-ish) to indicate that it's been captured.
 */
public class StartWindowButton extends JLabel implements MouseListener {

    private ActionListener myAL;

    /**
     * @constructor
     * @param value contains the name of the button
     */
    public StartWindowButton(String value) {
        super(value); // assign button's value
        Font customFont;
        try {
            // sets application's custom font
            customFont = Font.createFont(Font.TRUETYPE_FONT, this.getClass().getResourceAsStream("/fonts/crackman.ttf")).deriveFont(30f);
            this.setFont(customFont);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }
        this.setForeground(Color.yellow); // buttons background color
        this.setOpaque(false); // is transparent
        this.addMouseListener(this); // mouse registered
    }

    /**
     * ActionListener setter
     * @param al
     */
    public void addActionListener(ActionListener al) {
        myAL = al;
    }

    /**
     * when mouse clicks on button
     * @param e
     */
    @Override
    public void mouseClicked(MouseEvent e) {
        myAL.actionPerformed(new ActionEvent(this,501,""));
    }

    /**
     * sets color of button (foreground), when a mouse hovers over it
     * @param e
     */
    @Override
    public void mouseEntered(MouseEvent e) {
        this.setForeground(new Color(243, 105, 66));
    }

    /**
     * sets color of button (foreground), when a mouse does not hover over it
     * @param e
     */
    @Override
    public void mouseExited(MouseEvent e) {
        this.setForeground(Color.yellow);
    }

    @Override
    public void mousePressed(MouseEvent e) {}
    @Override
    public void mouseReleased(MouseEvent e) {}
}
