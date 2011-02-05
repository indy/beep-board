package io.indy.beepboard;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLUtils;

import android.util.Log;

class GLBackground
{
    private static final String TAG = "GLBackground";


    private FloatBuffer mVertexBuffer;

    public GLBackground()
    {
    }

    public void setup(GL10 gl, int width, int height)
    {
        float surfaceWidth = (float)width;
        float surfaceHeight = (float)height;

        int tileNumCorners = 4;
        int tileDimensions = 3;
        float[] vertices;
        vertices = new float[tileNumCorners * tileDimensions];

        vertices[ 0] = 0f;
        vertices[ 1] = 0f;
        vertices[ 2] = -0.3f;

        vertices[ 3] = surfaceWidth;
        vertices[ 4] = 0f;
        vertices[ 5] = -0.3f;

        vertices[ 6] = 0f;
        vertices[ 7] = surfaceHeight;
        vertices[ 8] = -0.3f;

        vertices[ 9] = surfaceWidth;
        vertices[10] = surfaceHeight;
        vertices[11] = -0.3f;

        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
        vbb.order(ByteOrder.nativeOrder());
        mVertexBuffer = vbb.asFloatBuffer();
        mVertexBuffer.put(vertices);
        mVertexBuffer.position(0);
    }

    public void draw(GL10 gl)
    {
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, mVertexBuffer);

        gl.glColor4f(0f, 0f, 0f, 0.5f);
        gl.glNormal3f(0, 0, 1);

        gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);

    }
}


