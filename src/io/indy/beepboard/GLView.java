package io.indy.beepboard;

import io.indy.beepboard.logic.LogicMain;
import io.indy.beepboard.gfx.GLRenderer;

import android.util.Log;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

import io.indy.beepboard.sfx.Beeper;
import io.indy.beepboard.sfx.SoundManager;

/*
  this is the first place that receives touch events making it
  the best place to keep the application specific stuff
*/

public class GLView extends GLSurfaceView
{
    private static final String TAG = "GLView";

    private final GLRenderer renderer;
    private final LogicMain logicMain;

    private final Beeper beeper;
    private final SoundManager soundManager;

    GLView(Context context)
    {
        super(context);

        soundManager = new SoundManager(context);
        soundManager.setSoundEnabled(true);

        int[] samples = {R.raw.gem0,
                         R.raw.gem1,
                         R.raw.gem2,
                         R.raw.gem3,
                         R.raw.gem4,
                         R.raw.gem5};
        beeper = new Beeper(soundManager, samples);

        // setDebugFlags(DEBUG_CHECK_GL_ERROR | DEBUG_LOG_GL_CALLS);
        renderer = new GLRenderer(context);
        setRenderer(renderer);

        logicMain = new LogicMain(beeper, renderer);

    }

    public boolean onTouchEvent(final MotionEvent event)
    {
        queueEvent(new Runnable(){
                public void run() {
                    logicMain.onTouch(event);
                }});
        return true;
    }
}