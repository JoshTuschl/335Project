package com.example.virtualbasketballshooter;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import android.opengl.GLES20;

// square shape to be drawn in the context of an OpenGL ES view
public class Square {

	private final String vertexShaderCode =
			"uniform mat4 uMVPMatrix;" +
			"attribute vec4 vPosition;" +
			"void main() {" +
			"    gl_Position = vPosition * uMVPMatrix;" +
			"}";
	
	private final String fragmentShaderCode =
			"precision mediump float;" +
			"uniform vec4 vColor;" +
			"void main() {" +
			"    gl_FragColor = vColor;" +
			"}";
	
	private FloatBuffer vertexBuffer;
	private ShortBuffer drawListBuffer;
	
	private final int mProgram;
	private int mPositionHandle;
	private int mColorHandle;
	private int mMVPMatrixHandle;
	
	// number of coordinates per vertex in this array
	static final int COORDS_PER_VERTEX = 3;
	static float squareCoords[] = { -0.5f,  0.5f, 0.0f,		// top left
									-0.5f, -0.5f, 0.0f,		// bottom left
									 0.5f, -0.5f, 0.0f,		// bottom right
									 0.5f,  0.5f, 0.0f };	// top right
	
	private short drawOrder[] = { 0, 1, 2, 0, 2, 3 };	// order to draw vertices
	
	private final int vertexCount = squareCoords.length / COORDS_PER_VERTEX;
	private final int vertexStride = COORDS_PER_VERTEX * 4;	// bytes per vertex
	
	// Set color with red, green, blue and alpha (opacity) values
	float color[] = { 0.36328125f, 0.23046875f, 0.77734375f, 0.5f };
	
	public Square() {
		// initialize vertex byte buffer for shape coordinates
		ByteBuffer bb = ByteBuffer.allocateDirect(
				// (# of coordinate values * 4 bytes per float
				squareCoords.length * 4);
		// use the device hardware's native byte order
		bb.order(ByteOrder.nativeOrder());
		
		// create a floating point buffer from the ByteBuffer
		vertexBuffer = bb.asFloatBuffer();
		// add the coordinates to the FloatBuffer
		vertexBuffer.put(squareCoords);
		// set the buffer to read the first coordinate
		vertexBuffer.position(0);
		
		// initialize byte buffer for the draw list
		ByteBuffer dlb = ByteBuffer.allocateDirect(
				// (# of coordinate values * 2 bytes per short
				drawOrder.length * 2);
		dlb.order(ByteOrder.nativeOrder());
		drawListBuffer = dlb.asShortBuffer();
		drawListBuffer.put(drawOrder);
		drawListBuffer.position(0);
		
		int vertexShader = MyGL20Renderer.loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode);
		int fragmentShader = MyGL20Renderer.loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode);
		
		mProgram = GLES20.glCreateProgram();				// create empty OpenGL ES program
		GLES20.glAttachShader(mProgram,  vertexShader);		// add the vertex shader to program
		GLES20.glAttachShader(mProgram, fragmentShader);	// add the fragment shader to program
		GLES20.glLinkProgram(mProgram);						// creates OpenGL ES program executables
	}
	
	public void draw(float[] mvpMatrix) {	// pass in the calculated transformation matrix
		// Add program to OpenGL ES environment
		GLES20.glUseProgram(mProgram);
		
		// get handle to vertex shader's vPosition member
		mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");
		
		// Enable a handle to the triangle vertices
		GLES20.glEnableVertexAttribArray(mPositionHandle);
		
		// Prepare the triangle coordinate data
		GLES20.glVertexAttribPointer(mPositionHandle, COORDS_PER_VERTEX,
									GLES20.GL_FLOAT, false,
									vertexStride, vertexBuffer);
		
		// get handle to fragment shader's vColor member
		mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");
		
		// Set color for drawing the triangle
		GLES20.glUniform4fv(mColorHandle, 1, color, 0);
		
		// get handle to shape's transformation matrix
		mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
		
		// Apply the projection and view transformation
		GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mvpMatrix, 0);
		
		// Draw the triangle
		GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, vertexCount);
		
		// Disable vertex array
		GLES20.glDisableVertexAttribArray(mPositionHandle);
	}
}
