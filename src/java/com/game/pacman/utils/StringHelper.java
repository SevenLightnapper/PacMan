package com.game.pacman.utils;

/**
 * @author SevenLightnapper
 */
public class StringHelper {
    public static int countLines(String str){
        String[] lines = str.split("\r\n|\r|\n");
        return  lines.length;
    }
}
