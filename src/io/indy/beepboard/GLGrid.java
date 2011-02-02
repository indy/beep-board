package io.indy.beepboard;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLUtils;

class GLGrid
{
    private static int gridWidth = 8;
    private static int gridHeight = 8;
    private static int numTiles = gridWidth * gridHeight;

    private final IntBuffer mVertexBuffer;

    public GLGrid()
    {
        int one = 65536;
        int tileHalf = one / (gridWidth * 2);
        int numVertices = numTiles * 12;
        int xOrigin, yOrigin, zOrigin;
        int t;
        int tBase;
        int spaceHalf = tileHalf + (tileHalf / 2);
        int[] vertices;
        vertices = new int[numVertices];

        // used to centre grid at the origin
        int gridOffset = - (gridWidth / 2) * (spaceHalf * 2);

        for(t=0;t<numTiles;t++)
        {
            xOrigin = (int)((t % gridWidth) * (spaceHalf * 2)) + gridOffset;
            yOrigin = (int)((t / gridWidth) * (spaceHalf * 2)) + gridOffset;
            zOrigin = 0;
            tBase = t * 12;

            vertices[tBase +  0] = xOrigin - tileHalf;
            vertices[tBase +  1] = yOrigin - tileHalf;
            vertices[tBase +  2] = zOrigin;

            vertices[tBase +  3] = xOrigin + tileHalf;
            vertices[tBase +  4] = yOrigin - tileHalf;
            vertices[tBase +  5] = zOrigin;

            vertices[tBase +  6] = xOrigin - tileHalf;
            vertices[tBase +  7] = yOrigin + tileHalf;
            vertices[tBase +  8] = zOrigin;

            vertices[tBase +  9] = xOrigin + tileHalf;
            vertices[tBase + 10] = yOrigin + tileHalf;
            vertices[tBase + 11] = zOrigin;
        };

        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
        vbb.order(ByteOrder.nativeOrder());
        mVertexBuffer = vbb.asIntBuffer();
        mVertexBuffer.put(vertices);
        mVertexBuffer.position(0);
    }

    public void draw(GL10 gl)
    {
        gl.glVertexPointer(3, GL10.GL_FIXED, 0, mVertexBuffer);

        gl.glColor4f(1, 1, 1, 1);
        gl.glNormal3f(0, 0, 1);

        int i;
        for(i=0;i<numTiles;i++) {
            gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, i * 4, 4);
        }

    }
}


