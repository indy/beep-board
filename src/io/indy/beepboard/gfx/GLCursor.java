package io.indy.beepboard.gfx;

import io.indy.beepboard.logic.Cursor;
import io.indy.beepboard.logic.Grid;

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

public class GLCursor
{
    private static final String TAG = "GLCursor";

    private GLRenderer glRenderer;
    private Cursor logicalCursor;

    private float cursorWidth;

    private FloatBuffer vertexBuffer;

    public GLCursor(GLRenderer g)
    {
        glRenderer = g;
    }

    public void setLogicalCursor(Cursor g)
    {
        logicalCursor = g;
    }

    public float getCursorWidth()
    {
        return cursorWidth;
    }

    public void setup(GL10 gl, float width, float height, float fov)
    {
        logicalCursor.dimensionChanged(width, height);
        vertexBuffer = asVertexBuffer(generateVertices());
    }

    public void draw(GL10 gl)
    {
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
        gl.glColor4f(.6f, 0.2f, 0f, 0.3f);

        gl.glPushMatrix();
        {
            gl.glTranslatef(logicalCursor.getCursorOffset(), 0f, 0f);
            gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
            gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 4, 4);
        }
        gl.glPopMatrix();
    }

    private float[] generateVertices()
    {
        float planeHeight = glRenderer.getPlaneHeight();
        float planeWidth = glRenderer.getPlaneWidth();
        float planeMaxSize = glRenderer.getPlaneMaxSize();
        float planeDistance = glRenderer.getPlaneDistance();

        Grid logicalGrid = logicalCursor.getGrid();
        int gridWidth = logicalGrid.getGridWidth();

        cursorWidth = planeWidth / (float)gridWidth;
        cursorWidth *= 0.5f;

        float xOffset = -(planeMaxSize / 2f);
        float yOffset = -(planeMaxSize / 2f);;

        float xOrigin, yOrigin, zOrigin;

        int numBars = 2;
        int tileNumCorners = 4;
        int tileDimensions = 3;
        float[] vertices;
        vertices = new float[numBars * tileNumCorners * tileDimensions];

        xOrigin = xOffset;
        yOrigin = yOffset;
        zOrigin = -planeDistance;

        int i;
        int vOffset = 0;
        for(i=0;i<numBars;i++){

            vertices[vOffset+ 0] = xOrigin;
            vertices[vOffset+ 1] = yOrigin;
            vertices[vOffset+ 2] = zOrigin;

            vertices[vOffset+ 3] = xOrigin + cursorWidth;
            vertices[vOffset+ 4] = yOrigin;
            vertices[vOffset+ 5] = zOrigin;

            vertices[vOffset+ 6] = xOrigin;
            vertices[vOffset+ 7] = yOrigin + planeMaxSize;
            vertices[vOffset+ 8] = zOrigin;

            vertices[vOffset+ 9] = xOrigin + cursorWidth;
            vertices[vOffset+10] = yOrigin + planeMaxSize;
            vertices[vOffset+11] = zOrigin;

            xOrigin += -planeMaxSize;
            vOffset += 12;
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
