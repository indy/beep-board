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

    public void setup(GL10 gl, float width, float height, float fov)
    {
        float surfaceWidth = width;
        float surfaceHeight = height;

        float rfov = (float)Math.toRadians(fov);
        float planeDistance = 100f;
        float planeHeight = 2f * planeDistance * (float)Math.sin(rfov/2f);
        float planeWidth = (width/height) * planeHeight;
        float planeMaxSize = Math.min(planeWidth, planeHeight);


        int tileNumCorners = 4;
        int tileDimensions = 3;
        float[] vertices;
        vertices = new float[tileNumCorners * tileDimensions];

        vertices[ 0] = -(planeWidth / 2f);
        vertices[ 1] = -(planeHeight / 2f);
        vertices[ 2] = -planeDistance;

        vertices[ 3] = (planeWidth / 2f);
        vertices[ 4] = -(planeHeight / 2f);
        vertices[ 5] = -planeDistance;

        vertices[ 6] = -(planeWidth / 2f);
        vertices[ 7] = (planeHeight / 2f);
        vertices[ 8] = -planeDistance;

        vertices[ 9] = (planeWidth / 2f);
        vertices[10] = (planeHeight / 2f);
        vertices[11] = -planeDistance;

        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
        vbb.order(ByteOrder.nativeOrder());
        mVertexBuffer = vbb.asFloatBuffer();
        mVertexBuffer.put(vertices);
        mVertexBuffer.position(0);
    }

    public void draw(GL10 gl)
    {
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, mVertexBuffer);

        gl.glColor4f(0f, 1f, 0f, 0.5f);
        gl.glNormal3f(0, 0, 1);

        gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);

    }
}


