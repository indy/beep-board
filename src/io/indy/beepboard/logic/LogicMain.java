package io.indy.beepboard.logic;

import android.util.Log;
import android.view.MotionEvent;

import io.indy.beepboard.gfx.GLRenderer;

public class LogicMain
{
    private static final String TAG = "LogicMain";

    private long startTime;
    private long currentTime;
    private long previousTime;
    private long numFrames;

    private final Grid grid;
    private final Cursor cursor;

    private GLRenderer renderer;

    public LogicMain(GLRenderer r)
    {
        renderer = r;
        renderer.setLogicMain(this);
        grid = new Grid(this, renderer);
        cursor = new Cursor(this, renderer);
    }

    public Grid getGrid()
    {
        return grid;
    }

    public void surfaceChanged()
    {
        startTime = System.currentTimeMillis();
        previousTime = startTime;
        currentTime = startTime;
        numFrames = 0;
    }

    public void tick()
    {
        previousTime = currentTime;
        currentTime = System.currentTimeMillis();
        float timeDelta = (currentTime - previousTime) / 1000f;
        numFrames += 1;

        if((numFrames % 100) == 0) {
            float fps = 1f / timeDelta;
            Log.d(TAG, "framerate = " + fps);
        }

        cursor.tick(timeDelta, numFrames);
    }

    public void onTouch(MotionEvent event)
    {
        //logTouchEvent(event);
        if(event.getAction() != MotionEvent.ACTION_DOWN) {
            return;
        }
        grid.touched(event.getX(), event.getY());
    }

    private void logTouchEvent(MotionEvent event)
    {
        String eName = "";
        switch (event.getAction()) {
        case MotionEvent.ACTION_UP : eName = "ACTION_UP"; break;
        case MotionEvent.ACTION_DOWN : eName = "ACTION_DOWN"; break;
        case MotionEvent.ACTION_MOVE : eName = "ACTION_MOVE"; break;
        case MotionEvent.ACTION_CANCEL : eName = "ACTION_CANCEL"; break;
        }

        float x = event.getX();
        float y = event.getY();
        String message = "[" + eName + "] x: " + x + " y: " + y;

        Log.d(TAG, message);
    }
}

