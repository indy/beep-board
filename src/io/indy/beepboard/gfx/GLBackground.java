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

    private GLRenderer glRenderer;
    private FloatBuffer mVertexBuffer;

    public GLBackground(GLRenderer r)
    {
        glRenderer = r;
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


