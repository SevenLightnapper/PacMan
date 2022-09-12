package com.game.pacman.utils;

/**
 * @author kamilla
 * @description count lines by regex of 'new line' (\r\n|\r|\n)
 */
public class StringHelper {
    public static int countLines(String str){
        String[] lines = str.split("\r\n|\r|\n");
        return  lines.length;
    }
}
