package io.indy.beepboard;

import android.content.Context;
import android.opengl.GLSurfaceView;


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

}