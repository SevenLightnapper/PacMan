package com.game.pacman.utils;

import java.awt.AWTEvent;

/**
 * @author kamilla
 * @description AWTEvent events' status codes
 */
public class AWTEventStatusMessage {
    public static final int UPDATE = AWTEvent.RESERVED_ID_MAX + 1;
    public static final int COLTEST = AWTEvent.RESERVED_ID_MAX + 2;
    public static final int RESET = AWTEvent.RESERVED_ID_MAX + 3;
}
