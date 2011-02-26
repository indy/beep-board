package io.indy.beepboard.logic;

import android.util.Log;
import android.view.MotionEvent;
import io.indy.beepboard.gfx.GLGrid;
import io.indy.beepboard.gfx.GLRenderer;

public class Grid
{
    private static final String TAG = "Grid";

    private static int gridWidth = 8;
    private static int gridHeight = 8;
    private static int numTiles = gridWidth * gridHeight;
    private static int[] tileState = new int[numTiles];

    // in screen space
    private float touchMinX;
    private float touchMaxX;
    private float touchMinY;
    private float touchMaxY;
    private float touchDimension;
    private float xFac;
    private float yFac;


    // when touching a tile set it's state to touchState
    private int touchState;

    // direct connection between Grid and GLGrid since this is a very simple app. Later on experiment with the 2 thread approach used by Replica Island
    private GLGrid glGrid;
    private LogicMain logicMain;

    public Grid(LogicMain lm, GLRenderer renderer)
    {
        logicMain = lm;

        glGrid = renderer.getGLGrid();
        glGrid.setLogicalGrid(this);

        touchState = 1;

        int i;
        for(i=0;i<numTiles;i++){
            tileState[i] = 0;
        }
    }

    public int   getGridWidth()  { return gridWidth;  }
    public int   getGridHeight() { return gridHeight; }
    public int   getNumTiles()   { return numTiles;   }
    public int[] getTileState()  { return tileState;  }


    public void activateColumn(int nextColumn)
    {
        Log.d(TAG, "activateColumn " + nextColumn);
        // figure out which notes to ask the sound manager to play
        int i;
        boolean fired = false;
        for(i=0;i<gridHeight;i++) {
            int tileIndex = (i*gridWidth)+nextColumn;
            if(tileState[tileIndex] == 1) {
                if(fired == false) {
                    logicMain.testSound(i);
                    //fired = true;
                }
                //Log.d(TAG, "activate tile " + tileIndex);
            }
        }
    }


    // message sent by GLGrid whenever the screen is setup
    public void dimensionChanged(float width, float height)
    {

        if(width < height) {
            touchMinX = 0f;
            touchMaxX = width;
            touchMinY = (height / 2f) - (width / 2f);
            touchMaxY = (height / 2f) + (width / 2f);
            touchDimension = width;
        } else {
            touchMinX = (width / 2f) - (height / 2f);
            touchMaxX = (width / 2f) + (height / 2f);
            touchMinY = 0f;
            touchMaxY = height;
            touchDimension = height;
        }

        xFac = (float)gridWidth / touchDimension;
        yFac = (float)gridHeight / touchDimension;
    }

    public void touchDown(float x, float y)
    {
        int tileIndex = getTileIndexAtPos(x, y);
        if(tileIndex != -1) {
            touchState = 1- tileState[tileIndex];
            tileState[tileIndex] = touchState;
        }
        // if tileIndex is -1 touchState remains at it's previous value
        // maybe change this so that touchState is set to something sensible?
    }

    public void touchMove(float x, float y)
    {
        int tileIndex = getTileIndexAtPos(x, y);
        if(tileIndex != -1) {
            tileState[tileIndex] = touchState;
        }
    }

    public void touchUp(float x, float y)
    {
    }

    private int getTileIndexAtPos(float x, float y)
    {
        if(x > touchMinX && x < touchMaxX && y > touchMinY && y < touchMaxY) {
            int tileX = (int)Math.floor((x - touchMinX) * xFac);
            int tileY = (int)Math.floor((y - touchMinY) * yFac);
            tileY = (gridHeight - tileY) - 1;

            return (tileY * gridWidth) + tileX;
        }
        return -1;
    }

}

