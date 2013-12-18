package com.example.virtualbasketballshooter;

import android.opengl.GLES20;

/**
 * Created with IntelliJ IDEA.
 * User: Josh
 * Date: 12/16/13
 * Time: 3:53 PM
 * To change this template use File | Settings | File Templates.
 *
 * Borrowed from http://stackoverflow.com/questions/9355004/drawing-a-circle-in-android-with-opengl
 *
 */
public class Circle {

    private int mProgram;
    private int vertexCount = 0;


    public Circle()
    {
        int vertexCount = 30;
        float radius = 3.0f;
        float center_x = 5.0f;
        float center_y = 5.0f;

//outer vertices of the circle
        int outerVertexCount = vertexCount-1;

        for (int i = 0; i < outerVertexCount; ++i){
            float percent = (i / (float) (outerVertexCount-1));
            float rad = (float)(percent * 2*Math.PI);

            //vertex position
            float outer_x = (float)(center_x + radius * Math.cos(rad));
            float outer_y = (float)(center_y + radius * Math.sin(rad));

        }
    }

    public void draw() {
        mProgram = GLES20.glCreateProgram();

        vertexCount = 30;

        // Add program to OpenGL ES environment
        GLES20.glUseProgram(mProgram);


        // Draw the Circle
        GLES20.glDrawArrays(GLES20.GL_LINE_LOOP, 0, vertexCount);

    }




}
