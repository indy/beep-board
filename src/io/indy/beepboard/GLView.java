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

        int[] samples = {R.raw.note_c4,
                         R.raw.note_d4,
                         R.raw.note_e4,
                         R.raw.note_g4,
                         R.raw.note_a4,
                         R.raw.note_c5,
                         R.raw.note_d5,
                         R.raw.note_e5};
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