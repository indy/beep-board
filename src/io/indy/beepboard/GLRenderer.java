package io.indy.beepboard;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.util.Log;
import android.view.MotionEvent;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

class GLRenderer implements GLSurfaceView.Renderer
{
    private static final String TAG = "GLRenderer";
    private final Context context;
    private final GLGrid grid = new GLGrid();

    private long startTime;
    private long fpsStartTime;
    private long numFrames;

    GLRenderer(Context context)
    {
        this.context = context;
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

    public void onTouch(MotionEvent event)
    {
        logTouchEvent(event);
        if(event.getAction() != MotionEvent.ACTION_DOWN) {
            return;
        }
        grid.touched(event.getX(), event.getY());
    }

    public void onSurfaceCreated(GL10 gl, EGLConfig config)
    {
        gl.glEnable(GL10.GL_DEPTH_TEST);
        gl.glDepthFunc(GL10.GL_LEQUAL);
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);

        startTime = System.currentTimeMillis();
        fpsStartTime = startTime;
        numFrames = 0;
    }

    public void onSurfaceChanged(GL10 gl, int width, int height)
    {
        gl.glViewport(0, 0, width, height);
        gl.glMatrixMode(GL10.GL_PROJECTION);
        gl.glLoadIdentity();

        grid.setup(width, height);
        gl.glOrthof(0f, (float)width, (float)height, 0, 5f, 25f);

        float lightAmbient[]  = new float[] {1.0f, 0.2f, 0.2f, 1};
        float lightDiffuse[]  = new float[] {1, 1, 1, 1};
        float lightPos[]  = new float[] {1, 1, 1, 1};

        gl.glEnable(GL10.GL_LIGHTING);
        gl.glEnable(GL10.GL_LIGHT0);
        gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_AMBIENT, lightAmbient, 0);
        gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_DIFFUSE, lightDiffuse, 0);
        gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_POSITION, lightPos, 0);


        float matAmbient[] = new float[] {1, 1, 1, 1};
        float matDiffuse[] = new float[] {1, 1, 1, 1};
        gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_AMBIENT, matAmbient, 0);
        gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_DIFFUSE, matDiffuse, 0);
    }

    public void onDrawFrame(GL10 gl)
    {
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

        gl.glMatrixMode(GL10.GL_MODELVIEW);
        gl.glLoadIdentity();
        //gl.glTranslatef(0, 0, -90.0f);
        /*
        long elapsed = System.currentTimeMillis() - startTime;
        gl.glRotatef(elapsed * (30f / 1000f), 0, 1, 0);
        gl.glRotatef(elapsed * (15f / 1000f), 1, 0, 0);
        */
        grid.draw(gl);
    }
}