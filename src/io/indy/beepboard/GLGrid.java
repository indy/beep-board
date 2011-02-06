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
    private static int[] tileState = new int[numTiles];

    // in screen space
    private float touchMinX;
    private float touchMaxX;
    private float touchMinY;
    private float touchMaxY;
    private float touchDimension;

    private float xFac;
    private float yFac;

    // in world space
    private float planeDistance; // distance of plane from camera
    private float planeWidth;
    private float planeHeight;
    private float planeMaxSize;

    private FloatBuffer vertexBufferOff;
    private FloatBuffer vertexBufferOn;

    public GLGrid()
    {
        int i;
        for(i=0;i<numTiles;i++){
            tileState[i] = 0;
        }
    }

    public void touched(float x, float y)
    {
        if(x > touchMinX && x < touchMaxX && y > touchMinY && y < touchMaxY) {
            int tileX = (int)Math.floor((x - touchMinX) * xFac);
            int tileY = (int)Math.floor((y - touchMinY) * yFac);
            tileY = (gridHeight - tileY) - 1;
            Log.d(TAG, "tilex: " + tileX + " tiley: " + tileY);

            int tileIndex = (tileY * gridWidth) + tileX;
            tileState[tileIndex] = 1 - tileState[tileIndex];
            Log.d(TAG, "toggle tile index: " + tileIndex);
        }
    }

    public void setup(GL10 gl, float width, float height, float fov)
    {
        if(width < height) {
            touchMinX = 0f;
            touchMaxX = width;
            touchMinY = (height / 2f) - (width / 2f);
            touchMaxY = (height / 2f) + (width / 2f);
            touchDimension = width;
        } else {
            touchMinX = (width / 2f) - (height / 2f);
            touchMaxX = (width / 2f) + (height / 2f);
            touchMinY = 0f;
            touchMaxY = height;
            touchDimension = height;
        }
        xFac = (float)gridWidth / touchDimension;
        yFac = (float)gridHeight / touchDimension;

        // todo: use clipping info to determine planeDistance value
        float rfov = (float)Math.toRadians(fov);
        planeDistance = 100f;
        planeHeight = 2f * planeDistance * (float)Math.sin(rfov/2f);
        planeWidth = (width/height) * planeHeight;
        planeMaxSize = Math.min(planeWidth, planeHeight);

        // fudge factor:
        // even though the above calculation should return the dimensions
        // of a plane that perfectly covers the screen area at a distance
        // of 100f it actually seems a little too small. Therefore just
        // fudge it for the moment by bringing the plane slightly closer
        // to the camera
        planeDistance = 90f;

        vertexBufferOff = asVertexBuffer(verticesForOffState());
        vertexBufferOn = asVertexBuffer(verticesForOnState());
    }

    public void draw(GL10 gl)
    {
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBufferOff);

        gl.glColor4f(0f, 0f, 1f, 0.9f);
        gl.glNormal3f(0f, 0f, 1f);

        int i;
        for(i=0;i<numTiles;i++) {
            gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, i * 4, 4);
        }


        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBufferOn);

        gl.glColor4f(0.5f, 0.5f, 1f, 0.5f);
        gl.glNormal3f(0f, 0f, 1f);

        for(i=0;i<numTiles;i++) {
            if(tileState[i] == 1) {
                gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, i * 4, 4);
            }
        }

    }


    private float[] verticesForOffState()
    {
        return generateGridVertices(5f);
    }

    private float[] verticesForOnState()
    {
        return generateGridVertices(9f);
    }

    private float[] generateGridVertices(float halfSpaceDenominator)
    {
        float maxGridSize = planeMaxSize;

        // draw a grid of numTiles elements covering a space maxGridSize^2
        float tileMaxDim = maxGridSize / gridWidth;
        float tileHalfSpace = tileMaxDim / halfSpaceDenominator;

        float xOffset = -(planeMaxSize / 2f);
        float yOffset = -(planeMaxSize / 2f);;

        float xOrigin, yOrigin, zOrigin;
        int i, j, tBase;

        int tileNumCorners = 4;
        int tileDimensions = 3;
        float[] vertices;
        vertices = new float[numTiles * tileNumCorners * tileDimensions];

        for(j=0;j<gridHeight;j++) {
            for(i=0;i<gridWidth;i++) {
                xOrigin = xOffset + (i * tileMaxDim);
                yOrigin = yOffset + (j * tileMaxDim);
                zOrigin = -planeDistance;
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
        return vertices;
    }

    private FloatBuffer asVertexBuffer(float[] vertices)
    {
        FloatBuffer vb;
        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
        vbb.order(ByteOrder.nativeOrder());
        vb = vbb.asFloatBuffer();
        vb.put(vertices);
        vb.position(0);
        return vb;
    }


}


