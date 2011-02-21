package io.indy.beepboard.gfx;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.util.Log;
import android.view.MotionEvent;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class GLRenderer implements GLSurfaceView.Renderer
{
    private static final String TAG = "GLRenderer";
    private final Context context;

    private final GLGrid glGrid = new GLGrid();
    private final GLBackground background = new GLBackground();

    private long startTime;
    private long currentTime;
    private long previousTime;
    private long numFrames;

    public GLRenderer(Context context)
    {
        this.context = context;
    }

    public GLGrid getGLGrid()
    {
        return glGrid;
    }

    public void onSurfaceCreated(GL10 gl, EGLConfig config)
    {
        gl.glDisable(GL10.GL_DEPTH_TEST);
        gl.glDisable(GL10.GL_LIGHTING);

        gl.glEnable(GL10.GL_BLEND);
        gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE);

        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);

        startTime = System.currentTimeMillis();
        previousTime = startTime;
        currentTime = startTime;
        numFrames = 0;
    }

    public void onSurfaceChanged(GL10 gl, int width, int height)
    {
        gl.glViewport(0, 0, width, height);
        gl.glMatrixMode(GL10.GL_PROJECTION);
        gl.glLoadIdentity();

        float ratio = (float)width/height;
        float fov = 45f;
        float nearPlane = 1f;
        float farPlane = 512f;

        GLU.gluPerspective(gl, fov, ratio, nearPlane, farPlane);

        background.setup(gl, (float)width, (float)height, fov);
        glGrid.setup(gl, (float)width, (float)height, fov);
    }

    public void onDrawFrame(GL10 gl)
    {
        previousTime = currentTime;
        currentTime = System.currentTimeMillis();
        numFrames += 1;
        if((numFrames % 100) == 0) {
            float fps = 1f / ((currentTime - previousTime) / 1000f);
            Log.d(TAG, "framerate = " + fps);
        }

        gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

        gl.glMatrixMode(GL10.GL_MODELVIEW);
        gl.glLoadIdentity();

        //background.draw(gl);
        glGrid.draw(gl);
    }
}