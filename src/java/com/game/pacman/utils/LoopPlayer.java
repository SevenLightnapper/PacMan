package com.game.pacman.utils;

import com.game.pacman.Main;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

/**
 * @author SevenLightnapper
 */
public class LoopPlayer {

    Clip clip;
    AudioInputStream inputStream;

    public LoopPlayer(String soundName){
        try {
            clip = AudioSystem.getClip();
            inputStream = AudioSystem.getAudioInputStream(
                    Main.class.getResourceAsStream("/sounds/" + soundName));
            clip.open(inputStream);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    public void start(){
        try {
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    public void stop(){
        try {
            clip.stop();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}
