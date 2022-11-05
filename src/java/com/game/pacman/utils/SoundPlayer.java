package com.game.pacman.utils;

import com.game.pacman.Main;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

/**
 * @author kamilla
 * @description this class is responsible for the sounds
 */
public class SoundPlayer {

    /**
     * plays the indicated sound
     * @param name which sound to play
     */
    public static void play(final String name) {
        try {
            Clip clip = AudioSystem.getClip();
            AudioInputStream inputStream = AudioSystem.getAudioInputStream(
                    Main.class.getResourceAsStream("/sounds/" + name));
            clip.open(inputStream);
            clip.start();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

}