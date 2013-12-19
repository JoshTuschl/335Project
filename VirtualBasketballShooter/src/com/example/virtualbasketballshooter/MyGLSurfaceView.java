package com.example.virtualbasketballshooter;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.SurfaceHolder;

// a view that can draw and manipulate objects using OpenGL ES 2.0
public class MyGLSurfaceView extends GLSurfaceView implements GestureDetector.OnGestureListener {

	private float mPreviousX;
	private float mPreviousY;
	private ScaleGestureDetector mScaleDetector;
	private MyGL20Renderer mRenderer;
	private float mScaleFactor = 1.0f;

    private SensorEventListener mSensorListener;
	
	private final float TOUCH_SCALE_FACTOR = 180.0F / 360;
	
	public MyGLSurfaceView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		//mScaleDetector = new ScaleGestureDetector(context, new ScaleListener());

		// Create and OpenGL ES 2.0 context
		setEGLContextClientVersion(2);

		// Set the Renderer for drawing on the GLSurfaceView
		mRenderer = new MyGL20Renderer();
		setRenderer(mRenderer);


		// Render the view only when there is a change in the drawing data
		setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY); // comment out for auto-rotation
	}

    public boolean onTouchEvent(MotionEvent event) {return false;}

    @Override
    public void onShowPress(MotionEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        // TODO Auto-generated method stub

        return true;
    }

    public boolean onDown(MotionEvent e) {return false;}

    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                           float velocityY) {return false;}

    public void onLongPress(MotionEvent e) {
        MyOpenGLES20.eyeX = 0f;
        MyOpenGLES20.eyeY = 0f;
        MyOpenGLES20.eyeZ = -1f;
        MyOpenGLES20.lookX = 0f;
        MyOpenGLES20.lookY = 0f;
        MyOpenGLES20.lookZ = 0f;
        mRenderer.basketball.reset();
        Log.i("long press", "lp!");
    }

    public boolean onScroll(MotionEvent e1, MotionEvent e2, float a, float b) { return false;}

//	private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
//        @Override
//        public boolean onScale(ScaleGestureDetector detector) {
//            mScaleFactor *= detector.getScaleFactor();
//
//            // Don't let the object get too small or too large.
//            mScaleFactor = Math.max(1.0f, Math.min(mScaleFactor, 50.0f));
//            mRenderer.scale = mScaleFactor;
//            requestRender();
//            return true;
//        }
//    }
//
//
//
//	@Override
//	public boolean onTouchEvent(MotionEvent e) {
//		mScaleDetector.onTouchEvent(e);
//
//		// MotionEvent reports input details from the touch screen
//		// and other input controls. In this case, you are only
//		// interested in events where the touch position changed.
//
//		float x = e.getX();
//		float y = e.getY();
//
//		if(!mScaleDetector.isInProgress())
//		{
//			switch (e.getAction()) {
//			case MotionEvent.ACTION_MOVE:
//
//				float dx = x - mPreviousX;
//				float dy = y - mPreviousY;
//
//				// reverse direction of rotation above the mid-line
//	//			if (y > getHeight() / 2) {
//	//				dx = dx * -1;
//	//			}
//	//
//	//			// reverse direction of rotation to left of the mid-line
//	//			if (x < getWidth() / 2) {
//	//				dy = dy * -1;
//	//			}
//
//				mRenderer.mAngle += dx * TOUCH_SCALE_FACTOR;	// = 90.0f / 320
//				mRenderer.mAngleY += dy * TOUCH_SCALE_FACTOR;
//				requestRender();
//			}
//
//		}
//		mPreviousX = x;
//		mPreviousY = y;
//
//		return true;
//	}



}
