package io.indy.beepboard.logic;

import android.util.Log;
import android.view.MotionEvent;
import io.indy.beepboard.gfx.GLCursor;
import io.indy.beepboard.gfx.GLRenderer;


public class Cursor
{
    private static final String TAG = "Cursor";

    // direct connection between Grid and GLGrid since this is a very simple app. Later on experiment with the 2 thread approach used by Replica Island
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

    public void tick(float timeDelta, long numFrames)
    {
        float cycleDuration = 2f; // 2 seconds to move a complete cycle

        /*
          this method will accumulate rounding errors so base offset on
          time since the activity was started
        */
        cursorOffset += (planeMaxSize / cycleDuration) * timeDelta;
        if(cursorOffset > planeMaxSize) {
            cursorOffset = 0f;
            Log.d(TAG, "cursor reset");
        }


    }
}

