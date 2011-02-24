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
    private LogicMain logicMain;
    private float planeMaxSize;

    public Cursor(LogicMain lm, GLRenderer renderer)
    {
        logicMain = lm;

        glCursor = renderer.getGLCursor();
        glCursor.setLogicalCursor(this);

        planeMaxSize = renderer.getPlaneMaxSize();
    }

    public Grid getGrid()
    {
        return logicMain.getGrid();
    }

    // message sent by GLCursor whenever the screen is setup
    public void dimensionChanged(float width, float height)
    {
    }
}

