package com.game.pacman.utils;

import com.game.pacman.general.PacBoard;
import com.game.pacman.map.MoveType;

import java.awt.Point;

/**
 * @author kamilla
 *
 * @description This class finds path between two Maze Points using Breadth-First Search (BFS)
 *
 * @link <a href="https://en.wikipedia.org/wiki/Breadth-first_search">Breadth-First Search (BFS) - Wikipedia</a>
 */
public class BFSFinder {

    private final int[][] map;
    private final int mx;
    private final int my;

    public BFSFinder(PacBoard pb){
        this.mx = pb.m_x;
        this.my = pb.m_y;
        //init BFS map
        map = new int[pb.m_x][pb.m_y];
        for (int ii = 0; ii < pb.m_y; ii++) {
            for (int jj=0; jj<pb.m_x; jj++) {
                if (pb.map[jj][ii] > 0 && pb.map[jj][ii] < 26) {
                    map[jj][ii] = 1;
                } else {
                    map[jj][ii] = 0;
                }
            }
        }
    }

    /**
     * @author kamilla
     * @description describes a maze's cell
     */
    private static class MazeCell {
        int x;
        int y;
        boolean isVisited;

        public MazeCell(int x, int y) {
            this.x = x;
            this.y = y;
            isVisited = false;
        }

        public String toString() {
            return "x = " + x + " y = " + y;
        }
    }

    private boolean isValid(int i, int j, boolean[][] markMat) {
        return (i >= 0 && i < mx && j >= 0 && j < my && map[i][j] == 0 && !markMat[i][j]);
    }

    //Construct Parentship LinkedList
    public MoveType getMove(int x, int y, int tx, int ty) {

        //already reached
        if (x == tx && y == ty) {
            return MoveType.NONE;
        }

        //System.out.println("FINDING PATH FROM : " + x + "," + y + " TO " + tx + "," + ty);

        MazeCell[][] mazeCellTable = new MazeCell[mx][my];
        Point[][] parentTable = new Point[mx][my];
        boolean[][] markMat = new boolean[mx][my];

        for (int ii = 0; ii < mx; ii++) {
            for (int jj = 0; jj < my; jj++) {
                markMat[ii][jj] = false;
            }
        }

        MazeCell[] Q = new MazeCell[2000];
        int size = 1;

        MazeCell start = new MazeCell(x, y);
        mazeCellTable[x][y] = start;
        Q[0] = start;
        markMat[x][y] = true;

        for (int k = 0; k < size; k++) {
            int i = Q[k].x;
            int j = Q[k].y;
            //RIGHT
            if (isValid(i + 1, j, markMat)) {
                MazeCell m = new MazeCell(i + 1, j);
                mazeCellTable[i + 1][j] = m;
                Q[size] = m;
                size++;
                markMat[i + 1][j] = true;
                parentTable[i + 1][j] = new Point(i, j);
            }
            //LEFT
            if (isValid(i - 1, j, markMat)) {
                MazeCell m = new MazeCell(i - 1, j);
                mazeCellTable[i - 1][j] = m;
                Q[size] = m;
                size++;
                markMat[i - 1][j] = true;
                parentTable[i - 1][j] = new Point(i, j);
            }
            //UP
            if (isValid(i, j - 1, markMat)) {
                MazeCell m = new MazeCell(i, j - 1);
                mazeCellTable[i][j - 1] = m;
                Q[size] = m;
                size++;
                markMat[i][j - 1] = true;
                parentTable[i][j - 1] = new Point(i, j);
            }
            //DOWN
            if (isValid(i, j + 1, markMat)) {
                MazeCell m = new MazeCell(i, j + 1);
                mazeCellTable[i][j + 1] = m;
                Q[size] = m;
                size++;
                markMat[i][j + 1] = true;
                parentTable[i][j + 1] = new Point(i, j);
            }
        }

        int ttx = tx;
        int tty = ty;
        MazeCell t = mazeCellTable[ttx][tty];
        MazeCell tl = null;
        while (t != start) {
            Point tp = parentTable[ttx][tty];
            ttx = tp.x;
            tty = tp.y;
            tl = t;
            t = mazeCellTable[ttx][tty];
        }

        assert tl != null;
        if (x == tl.x - 1 && y == tl.y) {
            return MoveType.RIGHT;
        }
        if (x == tl.x + 1 && y == tl.y) {
            return MoveType.LEFT;
        }
        if (x == tl.x && y == tl.y - 1) {
            return MoveType.DOWN;
        }
        if (x == tl.x && y == tl.y + 1) {
            return MoveType.UP;
        }
        return MoveType.NONE;
    }

}