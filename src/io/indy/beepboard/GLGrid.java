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

class GLGrid
{
    private static final String TAG = "GLGrid";

    private static int gridWidth = 8;
    private static int gridHeight = 8;
    private static int numTiles = gridWidth * gridHeight;

    private FloatBuffer mVertexBuffer;

    public GLGrid()
    {
    }

    public void setup(int width, int height)
    {

        float maxGridSize;
        if(width < height) {
            maxGridSize = (float)width;
        } else {
            maxGridSize = (float)height;
        }

        // draw a grid of numTiles elements covering a space maxGridSize^2
        float tileMaxDim = maxGridSize / gridWidth;
        float tileHalfSpace = tileMaxDim / 5f; // == 20%, so 40% blank space

        float xOrigin, yOrigin, zOrigin;
        int i, j, tBase;

        int tileNumCorners = 4;
        int tileDimensions = 3;
        float[] vertices;
        vertices = new float[numTiles * tileNumCorners * tileDimensions];

        for(j=0;j<gridHeight;j++) {
            for(i=0;i<gridWidth;i++) {
                xOrigin = i * tileMaxDim;
                yOrigin = j * tileMaxDim;
                zOrigin = 0f;
                tBase = ((gridWidth * j) + i) * 12;

                vertices[tBase +  0] = xOrigin + tileHalfSpace;
                vertices[tBase +  1] = yOrigin + tileHalfSpace;
                vertices[tBase +  2] = zOrigin;

                vertices[tBase +  3] = xOrigin + tileMaxDim - tileHalfSpace;
                vertices[tBase +  4] = yOrigin + tileHalfSpace;
                vertices[tBase +  5] = zOrigin;

                vertices[tBase +  6] = xOrigin + tileHalfSpace;
                vertices[tBase +  7] = yOrigin + tileMaxDim - tileHalfSpace;
                vertices[tBase +  8] = zOrigin;

                vertices[tBase +  9] = xOrigin + tileMaxDim - tileHalfSpace;
                vertices[tBase + 10] = yOrigin + tileMaxDim - tileHalfSpace;
                vertices[tBase + 11] = zOrigin;
            }
        }

        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
        vbb.order(ByteOrder.nativeOrder());
        mVertexBuffer = vbb.asFloatBuffer();
        mVertexBuffer.put(vertices);
        mVertexBuffer.position(0);
    }

    public void draw(GL10 gl)
    {
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, mVertexBuffer);

        gl.glColor4f(1, 1, 1, 1);
        gl.glNormal3f(0, 0, 1);

        int i;
        for(i=0;i<numTiles;i++) {
            gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, i * 4, 4);
        }

    }
}


