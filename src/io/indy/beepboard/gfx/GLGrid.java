/*

   Copyright 2011 Inderjit Gill (email@indy.io)

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.

*/

package io.indy.beepboard.gfx;

import io.indy.beepboard.logic.Grid;

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


public class GLGrid
{
    private static final String TAG = "GLGrid";

    private GLRenderer glRenderer;
    private Grid logicalGrid;

    private FloatBuffer vertexBufferOff;
    private FloatBuffer vertexBufferOn;
    private FloatBuffer vertexBufferActivated;

    public GLGrid(GLRenderer r)
    {
        glRenderer = r;
    }

    public void setLogicalGrid(Grid g)
    {
        logicalGrid = g;
    }

    public void setup(GL10 gl, float width, float height, float fov)
    {
        logicalGrid.dimensionChanged(width, height);

        vertexBufferOff = asVertexBuffer(verticesForOffState());
        vertexBufferOn = asVertexBuffer(verticesForOnState());
        vertexBufferActivated = asVertexBuffer(verticesForActivatedState());
    }

    public void draw(GL10 gl)
    {

        int i;
        int numTiles = logicalGrid.getNumTiles();
        int[] tileState = logicalGrid.getTileState();
        int activeColumn = logicalGrid.getActiveColumn();
        int gridHeight = logicalGrid.getGridHeight();
        int gridWidth = logicalGrid.getGridWidth();

        // tiles that are off
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBufferOff);
        gl.glColor4f(0f, 0f, 1f, 0.2f);

        for(i=0;i<numTiles;i++) {
            if(tileState[i] == 0) {
                gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, i * 4, 4);
            }
        }

        // tiles that are on
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBufferOn);
        gl.glColor4f(0.3f, 0.3f, 1f, 0.5f);

        for(i=0;i<numTiles;i++) {
            if(tileState[i] == 1 && i%gridWidth != activeColumn) {
                gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, i * 4, 4);
            }
        }

        // tiles that are active (on and are in the active column)
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBufferActivated);
        gl.glColor4f(1f, 0.3f, 1f, 1f);

        for(i=0;i<gridHeight;i++) {
            int tileIndex = (i*gridWidth)+activeColumn;
            if(tileState[tileIndex] == 1) {
                gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, tileIndex * 4, 4);
            }
        }
    }


    private float[] verticesForOffState()
    {
        return generateGridVertices(5f);
    }

    private float[] verticesForOnState()
    {
        return generateGridVertices(5f);
    }

    private float[] verticesForActivatedState()
    {
        return generateGridVertices(6f);
    }

    private float[] generateGridVertices(float halfSpaceDenominator)
    {
        float planeMaxSize = glRenderer.getPlaneMaxSize();
        float planeDistance = glRenderer.getPlaneDistance();

        int gridWidth = logicalGrid.getGridWidth();
        int gridHeight = logicalGrid.getGridHeight();
        int numTiles = logicalGrid.getNumTiles();

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


