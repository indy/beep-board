package io.indy.beepboard;

import io.indy.beepboard.logic.LogicMain;
import io.indy.beepboard.gfx.GLRenderer;

import android.util.Log;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

import io.indy.beepboard.sfx.SoundManager;
import io.indy.beepboard.sfx.SoundManager.Sound;

/*
  this is the first place that receives touch events making it
  the best place to keep the application specific stuff
*/

public class GLView extends GLSurfaceView
{
    private static final String TAG = "GLView";

    private final GLRenderer renderer;
    private final LogicMain logicMain;

    private final SoundManager soundManager;
    private final Sound sound;

    GLView(Context context)
    {
        super(context);

        soundManager = new SoundManager(context);
        sound = soundManager.load(R.raw.gem2);
        soundManager.setSoundEnabled(true);

        // setDebugFlags(DEBUG_CHECK_GL_ERROR | DEBUG_LOG_GL_CALLS);
        renderer = new GLRenderer(context);
        setRenderer(renderer);

        logicMain = new LogicMain(this, context, renderer);

    }

    public void testSound()
    {
        soundManager.play(sound, false, 1);
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