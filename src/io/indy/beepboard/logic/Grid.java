package io.indy.beepboard.logic;

import android.util.Log;
import android.view.MotionEvent;
import io.indy.beepboard.gfx.GLGrid;


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

    // direct connection between Grid and GLGrid since this is a very simple app. Later on experiment with the 2 thread approach used by Replica Island
    private GLGrid glGrid;
    private LogicMain logicMain;

    public Grid(LogicMain lm, GLGrid g)
    {
        logicMain = lm;

        glGrid = g;
        glGrid.setLogicalGrid(this);

        int i;
        for(i=0;i<numTiles;i++){
            tileState[i] = 0;
        }
    }

    public int   getGridWidth()  { return gridWidth;  }
    public int   getGridHeight() { return gridHeight; }
    public int   getNumTiles()   { return numTiles;   }
    public int[] getTileState()  { return tileState;  }

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

    public void touched(float x, float y)
    {
        if(x > touchMinX && x < touchMaxX && y > touchMinY && y < touchMaxY) {
            int tileX = (int)Math.floor((x - touchMinX) * xFac);
            int tileY = (int)Math.floor((y - touchMinY) * yFac);
            tileY = (gridHeight - tileY) - 1;
            Log.d(TAG, "tilex: " + tileX + " tiley: " + tileY);

            int tileIndex = (tileY * gridWidth) + tileX;
            tileState[tileIndex] = 1 - tileState[tileIndex];
            Log.d(TAG, "toggle tile index: " + tileIndex);
        }
    }

}

