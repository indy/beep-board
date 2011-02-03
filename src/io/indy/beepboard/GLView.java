package io.indy.beepboard;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

class GLView extends GLSurfaceView
{
    private final GLRenderer renderer;

    GLView(Context context)
    {
        super(context);

        // setDebugFlags(DEBUG_CHECK_GL_ERROR | DEBUG_LOG_GL_CALLS);

        renderer = new GLRenderer(context);
        setRenderer(renderer);
    }

    public boolean onTouchEvent(final MotionEvent event)
    {
        queueEvent(new Runnable(){
                public void run() {
                    renderer.onTouch(event);
                }});
        return true;
    }
}