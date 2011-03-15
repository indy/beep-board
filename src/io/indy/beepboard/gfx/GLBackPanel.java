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
import io.indy.beepboard.logic.LogicMain;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLUtils;
import android.util.Log;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import javax.microedition.khronos.opengles.GL10;

public class GLBackPanel
{
    private static final String TAG = "GLBackPanel";

    private GLRenderer glRenderer;
    private FloatBuffer vertexBuffer;

    public GLBackPanel(GLRenderer g)
    {
        glRenderer = g;
    }

    public void setup(GL10 gl, float width, float height, float fov)
    {
        vertexBuffer = asVertexBuffer(generateVertices());
    }

    public void draw(GL10 gl)
    {
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
        gl.glColor4f(0.3f, 0.3f, 0.3f, 0.2f);
        gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
    }

    private float[] generateVertices()
    {
        float planeHeight = glRenderer.getPlaneHeight();
        float planeWidth = glRenderer.getPlaneWidth();
        float planeMaxSize = glRenderer.getPlaneMaxSize();
        float planeDistance = glRenderer.getPlaneDistance();

        LogicMain lm = glRenderer.getLogicMain();
        Grid logicalGrid = lm.getGrid();
        int gridWidth = logicalGrid.getGridWidth();

        float xOffset = -(planeMaxSize / 2f);
        float yOffset = -(planeMaxSize / 2f);;

        float xOrigin, yOrigin, zOrigin;

        int tileNumCorners = 4;
        int tileDimensions = 3;
        float[] vertices;
        vertices = new float[tileNumCorners * tileDimensions];

        xOrigin = xOffset;
        yOrigin = yOffset;
        zOrigin = -planeDistance;

        vertices[ 0] = xOrigin;
        vertices[ 1] = yOrigin;
        vertices[ 2] = zOrigin;

        vertices[ 3] = xOrigin + planeMaxSize;
        vertices[ 4] = yOrigin;
        vertices[ 5] = zOrigin;

        vertices[ 6] = xOrigin;
        vertices[ 7] = yOrigin + planeMaxSize;
        vertices[ 8] = zOrigin;

        vertices[ 9] = xOrigin + planeMaxSize;
        vertices[10] = yOrigin + planeMaxSize;
        vertices[11] = zOrigin;

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
