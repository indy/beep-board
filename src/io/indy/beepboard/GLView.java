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
    private final Sound[] sound = new Sound[6];

    GLView(Context context)
    {
        super(context);

        soundManager = new SoundManager(context);

        sound[0] = soundManager.load(R.raw.gem0);
        sound[1] = soundManager.load(R.raw.gem1);
        sound[2] = soundManager.load(R.raw.gem2);
        sound[3] = soundManager.load(R.raw.gem3);
        sound[4] = soundManager.load(R.raw.gem4);
        sound[5] = soundManager.load(R.raw.gem5);

        soundManager.setSoundEnabled(true);

        // setDebugFlags(DEBUG_CHECK_GL_ERROR | DEBUG_LOG_GL_CALLS);
        renderer = new GLRenderer(context);
        setRenderer(renderer);

        logicMain = new LogicMain(this, context, renderer);

    }

    public void testSound(int i)
    {
        if(i>5) {i=5;}
        soundManager.play(sound[i], false, 1, 0.1f, 1.0f);
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