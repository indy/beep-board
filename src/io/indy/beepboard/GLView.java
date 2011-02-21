package io.indy.beepboard;

import io.indy.beepboard.logic.Grid;
import io.indy.beepboard.gfx.GLGrid;
import io.indy.beepboard.gfx.GLRenderer;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

/*
  this is the first place that receives touch events making it
  the best place to keep the application specific stuff
*/

class GLView extends GLSurfaceView
{
    private final GLRenderer renderer;
    private final Grid grid;

    GLView(Context context)
    {
        super(context);



        // setDebugFlags(DEBUG_CHECK_GL_ERROR | DEBUG_LOG_GL_CALLS);

        renderer = new GLRenderer(context);
        setRenderer(renderer);

        grid = new Grid(renderer.getGLGrid());
    }

    public boolean onTouchEvent(final MotionEvent event)
    {
        queueEvent(new Runnable(){
                public void run() {
                    grid.onTouch(event);
                }});
        return true;
    }
}