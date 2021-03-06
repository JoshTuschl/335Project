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
    private TextureCube mBackdrop;
    private TextureCube mRightSide;
    private TextureCube mLeftSide;
    private TextureCube mTop;
    private TextureCube mBottom;
    private TextureCube mSide_one;
    private TextureCube mSide_two;
    private TextureCube mSide_three;
    private TextureCube mSide_four;
    private Context appContext;

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
    private final float[] mBackdropMVPMatrix = new float[16];
	private final float[] mTemp = new float [16];
    private final float[] mFloorMVPMatrix = new float[16];
    private final float[] mRightSideMVPMatrix = new float[16];
    private final float[] mLeftSideMVPMatrix = new float[16];
    
    //skybox
//    private final float[] mTopMVPMatrix = new float[16];
//    private final float[] mBottomMVPMatrix = new float[16];
//    private final float[] mSide_oneMVPMatrix = new float[16];
//    private final float[] mSide_twoMVPMatrix = new float[16];
//    private final float[] mSide_threeMVPMatrix = new float[16];
//    private final float[] mSide_fourMVPMatrix = new float[16];
    
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

    public MyGL20Renderer() {
        appContext = MyOpenGLES20.context;
    }

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
        mBackboard.loadGLTexture(appContext, R.drawable.uk1);
		mPole = new Cube();
        mFloor = new Cube();
		basketball = new BasketBall();
        mRightSide = new TextureCube();
        mRightSide.loadGLTexture(appContext, R.drawable.uk1);
        mLeftSide = new TextureCube();
        mLeftSide.loadGLTexture(appContext, R.drawable.uk1);
        mBackdrop = new TextureCube();
        mBackdrop.loadGLTexture(appContext, R.drawable.uk2);
        //skybox
//        mTop = new TextureCube();
//        mTop.loadGLTexture(appContext, R.drawable.ceiling);
//        mBottom = new TextureCube();
//        mBottom.loadGLTexture(appContext, R.drawable.antelope_canyon);
//        mSide_one = new TextureCube();
//        mSide_one.loadGLTexture(appContext, R.drawable.rupp_arena);
//        mSide_two = new TextureCube();
//        mSide_two.loadGLTexture(appContext, R.drawable.rupp_stands);
//        mSide_three = new TextureCube();
//        mSide_three.loadGLTexture(appContext, R.drawable.wideshot_rupp_arena);
//        mSide_four = new TextureCube();
//        mSide_four.loadGLTexture(appContext, R.drawable.nbbj_rupp_arena_bowl);


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
        Matrix.multiplyMM(mBackdropMVPMatrix,  0,  mTemp, 0, mRotationMatrix,  0);
        Matrix.multiplyMM(mRightSideMVPMatrix,  0,  mTemp, 0, mRotationMatrix,  0);
        Matrix.multiplyMM(mLeftSideMVPMatrix,  0,  mTemp, 0, mRotationMatrix,  0);
        
        //skybox
//        Matrix.multiplyMM(mTopMVPMatrix,  0,  mTemp, 0, mRotationMatrix,  0);
//        Matrix.multiplyMM(mBottomMVPMatrix,  0,  mTemp, 0, mRotationMatrix,  0);
//        Matrix.multiplyMM(mSide_oneMVPMatrix,  0,  mTemp, 0, mRotationMatrix,  0);
//        Matrix.multiplyMM(mSide_twoMVPMatrix,  0,  mTemp, 0, mRotationMatrix,  0);
//        Matrix.multiplyMM(mSide_threeMVPMatrix,  0,  mTemp, 0, mRotationMatrix,  0);
//        Matrix.multiplyMM(mSide_fourMVPMatrix,  0,  mTemp, 0, mRotationMatrix,  0);
        
		
		// normal mat = transpose(inv(modelview)); 
		Matrix.multiplyMM(mTemp, 0, mVMatrix, 0, mRotationMatrix, 0);
		
		float [] tt = new float[16]; 
		Matrix.invertM(tt, 0, mTemp, 0);
		Matrix.transposeM(mNormalMat, 0, tt, 0);

        Matrix.scaleM(mBackdropMVPMatrix, 0, -15.0f, 13.8f, 15.0f);	//set dimensions of center backdrop
        Matrix.translateM(mBackdropMVPMatrix, 0, 0.0f, 0.8f, 3.0f); //center background
        mBackdrop.draw(mBackdropMVPMatrix, mNormalMat);

        Matrix.scaleM(mRightSideMVPMatrix, 0, -5.0f, -18.0f, 15.0f);	//set dimensions of right backdrop
        Matrix.translateM(mRightSideMVPMatrix, 0, 5.0f, -0.8f, 3.0f); //right background
        mRightSide.draw(mRightSideMVPMatrix, mNormalMat);

        Matrix.scaleM(mLeftSideMVPMatrix, 0, 5.0f, -18.0f, 15.0f);	//set dimensions of left backdrop
        Matrix.translateM(mLeftSideMVPMatrix, 0, 5.0f, -0.8f, 3.0f); //left background
        mLeftSide.draw(mLeftSideMVPMatrix, mNormalMat);
		
		Matrix.scaleM(mBackboardMVPMatrix, 0, 4.5f, 1.6f, 0.1f);	//set dimensions of backboard
		Matrix.translateM(mBackboardMVPMatrix, 0, 0.0f, 5.0f, 250f); //backboard
		mBackboard.draw(mBackboardMVPMatrix, mNormalMat, Opaque);

		if (basketball.replay == true)  {
            SystemClock.sleep(MyOpenGLES20.speed);
        }
		basketball.updateBall();
        if (Math.abs(basketball.getVz()) < .1 && basketball.replay == false && basketball.shotTaken == true )  {
            basketball.replay = true;
            SystemClock.sleep(2000);
            basketball.reset();
            basketball.setVy(basketball.originalVy);
            basketball.setVz(basketball.originalVz);
            basketball.shotTaken = false;
        }

		Matrix.scaleM(mSphereMVPMatrix, 0, 0.5f, 0.5f, 0.5f);	//set dimentions of basketball
        Matrix.translateM(mSphereMVPMatrix, 0, basketball.getX(), basketball.getY(), basketball.getZ()); //basketball
		//Matrix.translateM(mSphereMVPMatrix, 0, 0.0f, -3.0f, 20.0f);
		mSphere.draw(mSphereMVPMatrix, mNormalMat, mTemp, BasketballOrange);

        Matrix.scaleM(mRimMVPMatrix, 0, 1.5f, .05f, 0.75f);	//set dimentions of rim
        Matrix.translateM(mRimMVPMatrix, 0, 0.0f, 110.0f, 32.00f); //rim
        mRim.draw(mRimMVPMatrix, mNormalMat ,mTemp, BasketballOrange);
		
		//scale = 1.0f; 
		Matrix.scaleM(mPoleMVPMatrix, 0, 0.2f, 2.4f, 0.2f);	//set dimensions of pole
		Matrix.translateM(mPoleMVPMatrix, 0, 0.0f, 0.0f, 125f);
		mPole.draw(mPoleMVPMatrix, mNormalMat, METAL);

        Matrix.scaleM(mFloorMVPMatrix, 0, 12.0f, 0.01f, 25.0f);	//set dimensions of floor
        Matrix.translateM(mFloorMVPMatrix, 0, 0f, -200.0f, 0.0f);
        mFloor.draw(mFloorMVPMatrix, mNormalMat, BROWN);
        
        //skybox
//        Matrix.scaleM(mTopMVPMatrix, 0, 25f, 5f, 40f);	//top of sky box
//		Matrix.translateM(mTopMVPMatrix, 0, 0.0f, 30.0f, 0.0f); //top
//		mTop.draw(mTopMVPMatrix, mNormalMat);
//		
//		Matrix.scaleM(mBottomMVPMatrix, 0, 25.0f, 5.0f, 40.0f);	//set dimensions of bottom of sky box
//		Matrix.translateM(mBottomMVPMatrix, 0, 0.0f, -10.0f, 0.0f); //bottom
//		mBottom.draw(mBottomMVPMatrix, mNormalMat);
//		
//		Matrix.scaleM(mBackboardMVPMatrix, 0, 5.0f, 40f, 40.0f);	//set dimensions of side_one
//		Matrix.translateM(mBackboardMVPMatrix, 0, -12.5f, -10.0f, 0.0f); //1st side
//		mSide_one.draw(mSide_oneMVPMatrix, mNormalMat);
//		
//		Matrix.scaleM(mBackboardMVPMatrix, 0, 25.0f, 40f, 5.0f);	//set dimensions of second_side
//		Matrix.translateM(mBackboardMVPMatrix, 0, 0.0f, -10.0f, 20.0f); //2nd side
//		mSide_two.draw(mSide_twoMVPMatrix, mNormalMat);
//		
//		Matrix.scaleM(mBackboardMVPMatrix, 0, -5.0f, 40f, 40f);	//set dimensions of third_side
//		Matrix.translateM(mBackboardMVPMatrix, 0, 12.5f, -10.0f, 0.0f); //3rd side
//		mSide_three.draw(mSide_threeMVPMatrix, mNormalMat);
//		
//		Matrix.scaleM(mBackboardMVPMatrix, 0, 25.0f, 40f, -5.0f);	//set dimensions of fourth side
//		Matrix.translateM(mBackboardMVPMatrix, 0, 0.0f, -10.0f, -20.0f); //4th side
//		mSide_four.draw(mSide_fourMVPMatrix, mNormalMat);
		
	}

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