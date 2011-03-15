/*

   Copyright 2011 Inderjit Gill (email@indy.io)

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.

*/

package io.indy.beepboard.logic;

import android.util.Log;
import android.view.MotionEvent;

import io.indy.beepboard.sfx.Beeper;
import io.indy.beepboard.gfx.GLRenderer;
import io.indy.beepboard.GLView;

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
    private Beeper beeper;

    public LogicMain(Beeper b, GLRenderer r)
    {
        renderer = r;
        renderer.setLogicMain(this);
        grid = new Grid(this, renderer);
        cursor = new Cursor(this, renderer);
        beeper = b;

    }

    public void testSound(int i)
    {
        beeper.beep(i);
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

        cursor.tick(startTime, currentTime);
    }

    public void onTouch(MotionEvent event)
    {
        /*
        logTouchEvent(event);
        if(event.getAction() != MotionEvent.ACTION_DOWN) {
            return;
        }
        */

        float x = event.getX();
        float y = event.getY();
        switch(event.getAction()) {
        case MotionEvent.ACTION_UP : grid.touchUp(x, y); break;
        case MotionEvent.ACTION_DOWN : grid.touchDown(x, y); break;
        case MotionEvent.ACTION_MOVE : grid.touchMove(x, y); break;
        }
        //grid.touched(event.getX(), event.getY());
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

