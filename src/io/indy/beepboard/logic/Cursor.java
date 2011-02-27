package io.indy.beepboard.logic;

import android.util.Log;
import android.view.MotionEvent;
import io.indy.beepboard.gfx.GLCursor;
import io.indy.beepboard.gfx.GLRenderer;


public class Cursor
{
    /*
      controls the position of the cursor and fires off 'activeColumn'
      messages to LogicMain
     */

    private static final String TAG = "Cursor";

    private GLCursor glCursor;
    private GLRenderer glRenderer;

    private LogicMain logicMain;

    private float planeMaxSize;
    private float cursorOffset;



    public Cursor(LogicMain lm, GLRenderer renderer)
    {
        logicMain = lm;

        glRenderer = renderer;
        glCursor = renderer.getGLCursor();
        glCursor.setLogicalCursor(this);

        cursorOffset = 0f;
    }

    public float getCursorOffset()
    {
        return cursorOffset;
    }

    public Grid getGrid()
    {
        return logicMain.getGrid();
    }

    // message sent by GLCursor whenever the screen is setup
    public void dimensionChanged(float width, float height)
    {
        planeMaxSize = glRenderer.getPlaneMaxSize();
    }

    public void tick(long startTime, long currentTime)
    {
        long cycleDuration = 2000; // 2 seconds to move a complete cycle

        // a value between 0 and 1999
        long timeDelta = (currentTime - startTime) % cycleDuration;
        cursorOffset = (planeMaxSize / (float)cycleDuration) * (float)timeDelta;
        collisionCheckCursor(cursorOffset);
    }

    // see if logicMain should be sent the activateColumn message
    private void collisionCheckCursor(float cursorOffset)
    {
        float cursorWidth = glCursor.getCursorWidth();
        float leadingEdge = cursorOffset + (cursorWidth * 0.7f);

        while (leadingEdge > planeMaxSize) {
            leadingEdge -= planeMaxSize;
        }

        Grid grid = logicMain.getGrid();
        int numColumns = grid.getGridWidth();
        int lastColumnActivated = grid.getActiveColumn();

        float colWidth = planeMaxSize / (float)numColumns;

        int nextCol = (lastColumnActivated + 1) % numColumns;
        float colStart = (float)nextCol * colWidth;

        if(leadingEdge > colStart && leadingEdge < colStart + colWidth) {
            grid.activateColumn(nextCol);
        }
    }
}
