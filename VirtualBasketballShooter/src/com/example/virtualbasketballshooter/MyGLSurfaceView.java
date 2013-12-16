package com.example.virtualbasketballshooter;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;

// a view that can draw and manipulate objects using OpenGL ES 2.0
public class MyGLSurfaceView extends GLSurfaceView {
	
	private float mPreviousX;
	private float mPreviousY;
	private float mLastX=0;
	private float mLastY=0;
	private float mLastZ=0;
	private ScaleGestureDetector mScaleDetector;
	private MyGL20Renderer mRenderer;
	private float mScaleFactor = 1.f;
	
	private final float TOUCH_SCALE_FACTOR = 180.0F / 360;	
	
	public MyGLSurfaceView(Context context) {
		super(context);
		
		mScaleDetector = new ScaleGestureDetector(context, new ScaleListener());
		
		// Create and OpenGL ES 2.0 context
		setEGLContextClientVersion(2);

		// Set the Renderer for drawing on the GLSurfaceView
		mRenderer = new MyGL20Renderer();
		setRenderer(mRenderer);
		
		// Render the view only when there is a change in the drawing data
		setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY); // comment out for auto-rotation
	}
	
	private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            mScaleFactor *= detector.getScaleFactor();

            // Don't let the object get too small or too large.
            mScaleFactor = Math.max(0.1f, Math.min(mScaleFactor, 10.0f));
            mRenderer.scale = mScaleFactor;
            requestRender();
            return true;
        }
    }
	
	public class SensorActivity extends Activity implements SensorEventListener {
	     private final SensorManager mSensorManager;
	     private final Sensor mAccelerometer;
	     private long lastUpdate;
	     static final float ns_to_s = 1.0f / 1000000000.0f;
	     long last_timestamp = 0;

	     public SensorActivity() {
	         mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
	         mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
	         lastUpdate = System.currentTimeMillis();
	     }

	     protected void onResume() {
	         super.onResume();
	         mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
	     }

	     protected void onStop() {
	    	 super.onStop();
	         mSensorManager.unregisterListener(this);
	     }
	     
	     protected void onPause() {
	         super.onPause();
	         mSensorManager.unregisterListener(this);
	     }

	     public void onAccuracyChanged(Sensor sensor, int accuracy) {
	     }

	     public void onSensorChanged(SensorEvent event) {
	    	 //Movement
	    	 mRenderer.ax = event.values[0];
	    	 mRenderer.ay = event.values[1];
	    	 mRenderer.az = event.values[2];	 
	    	 
	    	 
	    	 float accelationSquareRoot = (mRenderer.ax*mRenderer.ax + mRenderer.ay*mRenderer.ay + mRenderer.az*mRenderer.az) / (SensorManager.GRAVITY_EARTH * SensorManager.GRAVITY_EARTH);
	    	 long actualTime = System.currentTimeMillis();
	    	 if(accelationSquareRoot >= 2)
	    	 {
	    		 if(actualTime - lastUpdate < 200)
	    		 {
	    			 return;
	    		 }
	    		 lastUpdate = actualTime;
	    		 if(mLastX != 0)
	    		 {		//integrate to get Newtonian velocity
	    			 mRenderer.dt = (event.timestamp - last_timestamp) * ns_to_s;
	    			 mRenderer.Vx = (mRenderer.ax + mLastX) / (2*mRenderer.dt);
	    			 mRenderer.Vy = (mRenderer.ay + mLastY) / (2*mRenderer.dt);
	    			 mRenderer.Vx = (mRenderer.az + mLastZ) / (2*mRenderer.dt);
	    		 }
	    		 else
	    		 {
	    			 mRenderer.dt = 1;
	    			 mRenderer.Vx = 0f;
	    			 mRenderer.Vy = 0f;
	    			 mRenderer.Vz = 0f;
	    		 }
	    		 mLastX = mRenderer.ax;
	    		 mLastY = mRenderer.ay;
	    		 mLastZ = mRenderer.az;
	    		 last_timestamp = event.timestamp;
	    	 }
	    	 
	     }

//		@Override
//		public void onSensorChanged(SensorEvent arg0) {
//			// TODO Auto-generated method stub
//			
//		}
	 }
	
	@Override
	public boolean onTouchEvent(MotionEvent e) {
		mScaleDetector.onTouchEvent(e);
		
		// MotionEvent reports input details from the touch screen
		// and other input controls. In this case, you are only
		// interested in events where the touch position changed.
		
		float x = e.getX();
		float y = e.getY();

		if(!mScaleDetector.isInProgress())
		{
			switch (e.getAction()) {
			case MotionEvent.ACTION_MOVE:
				
				float dx = x - mPreviousX;
				float dy = y - mPreviousY;
				
				// reverse direction of rotation above the mid-line
	//			if (y > getHeight() / 2) {
	//				dx = dx * -1;
	//			}
	//			
	//			// reverse direction of rotation to left of the mid-line
	//			if (x < getWidth() / 2) {
	//				dy = dy * -1;
	//			}
				
				mRenderer.mAngle += dx * TOUCH_SCALE_FACTOR;	// = 90.0f / 320
				mRenderer.mAngleY += -dy * TOUCH_SCALE_FACTOR;
				requestRender();
			}
			
		}
		mPreviousX = x;
		mPreviousY = y;
		
		return true;
	}

}
