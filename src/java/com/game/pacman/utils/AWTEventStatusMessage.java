package com.game.pacman.utils;

import java.awt.AWTEvent;

/**
 * @author kamilla
 * @description AWTEvent events' status codes
 */
public class AWTEventStatusMessage {
    /**
     * update
     */
    public static final int UPDATE = AWTEvent.RESERVED_ID_MAX + 1;

    /**
     * collision test
     */
    public static final int COLTEST = AWTEvent.RESERVED_ID_MAX + 2;

    /**
     * reset
     */
    public static final int RESET = AWTEvent.RESERVED_ID_MAX + 3;
}
