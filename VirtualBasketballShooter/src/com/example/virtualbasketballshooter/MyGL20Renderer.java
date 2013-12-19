package com.example.virtualbasketballshooter;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import android.annotation.SuppressLint;
import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.SystemClock;
import android.util.Log;


//a renderer that can draw and manipulate objects
@SuppressLint("NewApi")
public class MyGL20Renderer implements GLSurfaceView.Renderer {
	
	private Triangle mTriangle;
	private Square mSquare;
	private Cube mCube; 
	private Cube mLeg;
	private Cube mPole;
	private Cube mBackboard;
	private Cube mEndBumper;
	private Cube mSideBumper;
	private Sphere mSphere; 
	public static BasketBall basketball;
    private Sphere mRim;
    private Cube mFloor;

	public volatile float mAngle; // use volatile because we modify it in other classes
	public volatile float mAngleY;
	public volatile float ballspeed;
	public volatile float scale=1;
	public float x=0f;
	public float y=0f;
	public float z=0f;
	
	//matrices
	private final float[] mVMatrix = new float[16];
	private final float[] mProjMatrix = new float[16];
	private final float[] mNormalMat = new float[16]; 
	private final float[] mMVPMatrix = new float[16];
	private final float[] mLegMVPMatrix = new float[16];
	private final float[] mSquareMVPMatrix = new float[16];
	private final float[] mSideMVPMatrix = new float[16];
	private final float[] mSphereMVPMatrix = new float[16];
    private final float[] mRimMVPMatrix = new float[16];
	private final float[] mRotationMatrix = new float[16];
	private final float[] mRotationXMatrix = new float[16];
	private final float[] mRotationYMatrix = new float[16];
	private final float[] mBackboardMVPMatrix = new float[16];
	private final float[] mPoleMVPMatrix = new float[16];
	private final float[] mTemp = new float [16];
    private final float[] mFloorMVPMatrix = new float[16];
	
	//colors
	private final float[] GREEN = new float[]{(float)(49.0/255.0), (float)(153.0/255.0), (float)(94.0/255.0), 1.0f};
	private final float[] BROWN = new float[]{(float)(69.0/255.0), (float)(42.0/255.0), (float)(14.0/255.0), 1.0f};
	private final float[] RED = new float[]{(float)(253.0/255.0), (float)(3.0/255.0), (float)(3.0/255.0), 1.0f};
	private final float[] BLUE = new float[]{(float)(3.0/255.0), (float)(11.0/255.0), (float)(253.0/255.0), 1.0f};
	private final float[] PURPLE = new float[]{(float)(115.0/255.0), (float)(4.0/255.0), (float)(198.0/255.0), 1.0f};
	private final float[] BasketballOrange = new float[]{(float)(255.0/255.0), (float)(102.0/255.0), (float)(0.0/255.0), 1.0f};
	private final float[] UKBlue = new float[]{(float)(0.0/255.0), (float)(93.0/255.0), (float)(170.0/255.0), 1.0f};
	private final float[] METAL = new float[]{(float)(219.0/255.0), (float)(228.0/255.0), (float)(235.0/255.0), 1.0f};
	private final float[] WHITE = new float[]{(float)(255.0/255.0), (float)(255.0/255.0), (float)(255.0/255.0), 1.0f};
	private final float[] Opaque = new float[]{(float)(255.0/255.0), (float)(255.0/255.0), (float)(255.0/255.0), 0.01f};
	
	// Set up the view's OpenGL ES environment
	@Override
	public void onSurfaceCreated(GL10 unused, EGLConfig config) {
		// Set the background frame color
		GLES20.glClearColor(0.5f, 0.5f, 0.5f, 1.0f);
		
		
		GLES20.glEnable(GL10.GL_DEPTH_TEST);   // Enables depth-buffer for hidden surface removal
	    GLES20.glDepthFunc(GL10.GL_LEQUAL); 
		// initialize a triangle
		mTriangle = new Triangle();
		// initialize a square
		mSquare = new Square();
		
		mCube = new Cube();
		mEndBumper = new Cube();
		mSideBumper = new Cube();
		mLeg = new Cube();
		mBackboard = new Cube();
		mPole = new Cube();
        mFloor = new Cube();
		basketball = new BasketBall();


		mSphere = new Sphere(1.0f, 35, 35);
        mRim = new Sphere(0.5f, 40,40);
    }

	// Called for each redraw of the view
	@Override
	public void onDrawFrame(GL10 gl) {
		// Redraw background color
		GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT|GLES20.GL_DEPTH_BUFFER_BIT);
		// Set the camera position (View matrix)  //(CameraViewMatrix, offset, eyeX, eyeY, eyeZ, lookX, lookY, lookZ, upX, upY, upZ)
		Matrix.setLookAtM(mVMatrix, 0, MyOpenGLES20.eyeX, MyOpenGLES20.eyeY, MyOpenGLES20.eyeZ,
                MyOpenGLES20.lookX, MyOpenGLES20.lookY, MyOpenGLES20.lookZ,
                0f, 1.0f, 0.0f);
		
		// Calculate the projection and view transformation
		// temp = Proj*View; 
		Matrix.multiplyMM(mTemp, 0, mProjMatrix, 0, mVMatrix, 0);
		
		Matrix.setRotateM(mRotationXMatrix,  0,  mAngle,  0,  1.0f,  0.0f);
		Matrix.setRotateM(mRotationYMatrix,  0,  mAngleY,  1.0f,  0.0f,  0.0f);
		Matrix.multiplyMM(mRotationMatrix, 0,  mRotationXMatrix, 0, mRotationYMatrix, 0);
		float[] mScaleMatrix = new float[16];
		Matrix.setIdentityM(mScaleMatrix, 0);
		Matrix.scaleM(mScaleMatrix,  0,  1.0f, 1.0f,  1.0f);
		Matrix.multiplyMM(mRotationMatrix, 0, mScaleMatrix, 0, mRotationMatrix, 0);
		
		// MVP = Proj*View*Rot
		Matrix.multiplyMM(mMVPMatrix,  0,  mTemp, 0, mRotationMatrix,  0);
		Matrix.multiplyMM(mLegMVPMatrix,  0,  mTemp, 0, mRotationMatrix,  0);
		Matrix.multiplyMM(mSphereMVPMatrix,  0,  mTemp, 0, mRotationMatrix,  0);
        Matrix.multiplyMM(mRimMVPMatrix,  0,  mTemp, 0, mRotationMatrix,  0);
		Matrix.multiplyMM(mSquareMVPMatrix,  0,  mTemp, 0, mRotationMatrix,  0);
		Matrix.multiplyMM(mSideMVPMatrix,  0,  mTemp, 0, mRotationMatrix,  0);
		Matrix.multiplyMM(mBackboardMVPMatrix,  0,  mTemp, 0, mRotationMatrix,  0);
		Matrix.multiplyMM(mPoleMVPMatrix,  0,  mTemp, 0, mRotationMatrix,  0);
        Matrix.multiplyMM(mFloorMVPMatrix,  0,  mTemp, 0, mRotationMatrix,  0);
		
		
		// normal mat = transpose(inv(modelview)); 
		Matrix.multiplyMM(mTemp, 0, mVMatrix, 0, mRotationMatrix, 0);
		
		float [] tt = new float[16]; 
		Matrix.invertM(tt, 0, mTemp, 0);
		Matrix.transposeM(mNormalMat, 0, tt, 0);
		
		Matrix.scaleM(mBackboardMVPMatrix, 0, 4.5f, 1.6f, 0.1f);	//set dimensions of backboard
		Matrix.translateM(mBackboardMVPMatrix, 0, 0.0f, 5.0f, 250f); //backboard
		mBackboard.draw(mBackboardMVPMatrix, mNormalMat, Opaque);
		
		basketball.updateBall();

		Matrix.scaleM(mSphereMVPMatrix, 0, 0.5f, 0.5f, 0.5f);	//set dimentions of basketball
        Matrix.translateM(mSphereMVPMatrix, 0, basketball.getX(), basketball.getY(), basketball.getZ()); //basketball
		//Matrix.translateM(mSphereMVPMatrix, 0, 0.0f, -3.0f, 20.0f);
		mSphere.draw(mSphereMVPMatrix, mNormalMat, mTemp, BasketballOrange);

        Matrix.scaleM(mRimMVPMatrix, 0, 1.5f, .05f, 0.75f);	//set dimentions of rim
        Matrix.translateM(mRimMVPMatrix, 0, 0.0f, 110.0f, 32.00f); //rim
        mRim.draw(mRimMVPMatrix, mNormalMat ,mTemp, METAL);
		
		//scale = 1.0f; 
		Matrix.scaleM(mPoleMVPMatrix, 0, 0.2f, 2.4f, 0.2f);	//set dimensions of pole
		Matrix.translateM(mPoleMVPMatrix, 0, 0.0f, 0.0f, 125f);
		mPole.draw(mPoleMVPMatrix, mNormalMat, METAL);

        Matrix.scaleM(mFloorMVPMatrix, 0, 12.0f, 0.01f, 25.0f);	//set dimensions of floor
        Matrix.translateM(mFloorMVPMatrix, 0, 0f, -200.0f, 0.0f);
        mFloor.draw(mFloorMVPMatrix, mNormalMat, BROWN);

//		Matrix.scaleM(mSquareMVPMatrix, 0, 1.0f, 0.1f, 0.0f);//set dimensions of backboard square
//		Matrix.translateM(mSquareMVPMatrix, 0, 0.0f, 50.0f, 0.0f); //1st End Bumper
//		mSquare.draw(mSquareMVPMatrix);
//		mEndBumper.draw(mSquareMVPMatrix, mNormalMat, BROWN);
//		Matrix.translateM(mSquareMVPMatrix, 0, 0.0f, 58.0f, 0.0f); //2nd End Bumper
//		mSquare.draw(mSquareMVPMatrix);
//		mEndBumper.draw(mSquareMVPMatrix, mNormalMat, BROWN);
//		
//		
//		Matrix.scaleM(mSideMVPMatrix, 0, 0.1f, 1.5f, 0.2f); //set dimensions of Side Bumper
//		Matrix.translateM(mSideMVPMatrix, 0, 19.0f, 0.0f, 0.0f); //1st Side Bumper
//		mSideBumper.draw(mSideMVPMatrix, mNormalMat, BROWN);
//		Matrix.translateM(mSideMVPMatrix, 0, -38.0f, 0.0f, 0.0f); //2nd Side Bumper
//		mSideBumper.draw(mSideMVPMatrix, mNormalMat, BROWN);
//		
		
		
		
		
//		Matrix.translateM(mLegMVPMatrix, 0, 0.0f, -56.0f, 0.0f); //2nd leg
//		mLeg.draw(mLegMVPMatrix, mNormalMat, BROWN);
//		Matrix.translateM(mLegMVPMatrix, 0, -18.f, 0.0f, 0.0f); //3rd leg
//		mLeg.draw(mLegMVPMatrix, mNormalMat, BROWN);
//		Matrix.translateM(mLegMVPMatrix, 0, 0.0f, 56.0f, 0.0f); //4th leg
//		mLeg.draw(mLegMVPMatrix, mNormalMat, BROWN);
		
		
		 
		
		/*
		float [] t2 = new float[16]; 
		
		for (int kk = 0; kk< 16; kk++) t2[kk] = mMVPMatrix[kk]; 
		Matrix.scaleM(t2, 0, 2.0f, 0.5f, 0.5f);
		Matrix.translateM(t2, 0, 1.0f, 0, 0);
		
		mSphere.draw(t2, mNormalMat); 
		*/
		
		
		// Create a rotation transformation for the triangle
//		long time = SystemClock.uptimeMillis() % 4000L;
//		float angle = 0.090f * ((int) time);
		// instead of rotate automatically, we use mAngle
		

		// Combine the rotation matrix with the projection and camera view
		//Matrix.multiplyMM(mMVPMatrix,  0,  mRotationMatrix,  0,  mMVPMatrix,  0);
		
		// Draw shape
		//mTriangle.draw(mMVPMatrix);
		
	}
	
//	public void reset()
//	{
//		eyeX = 0f;
//		eyeY = 0f;
//		eyeZ = -5f;
//		lookX = 0f;
//		lookY = 0f;
//		lookZ = 0f;
//		x = 0f;
//		y = -3f;
//		z = -20f;
//	}

    // Called if the geometry of the view changes
	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		// set view port to the windows's width and height
		GLES20.glViewport(0,  0,  width,  height);
		
		float ratio = (float) width / height;
		
		// this projection matrix is applied to object coordinates
		// in the onDrawFrame() method
		
		// if you have android API level 14+ you can use this
		//Matrix.perspectiveM(mProjMatrix, 0, 60.0f, ratio, 1.0f, 25.0f);
		
		Matrix.frustumM(mProjMatrix, 0, -ratio, ratio, -1, 1, 1.0f, 50f);
	}
	
	// compile GLSL code prior to using it in OpenGL ES environment
	public static int loadShader(int type, String shaderCode) {
		
		// create a vertex shader type (GLES20.GL_VERTEX_SHADER)
		// or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
		int shader = GLES20.glCreateShader(type);
		
		// add the source code to the shader and compile it
		GLES20.glShaderSource(shader, shaderCode);
		GLES20.glCompileShader(shader);
		
		return shader;
	}

}