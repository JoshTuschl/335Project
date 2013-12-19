package com.example.virtualbasketballshooter;

import android.app.Activity;
import android.graphics.PointF;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.GestureDetector.OnGestureListener;
import android.widget.ImageButton;

import javax.microedition.khronos.opengles.GL10;

// Note: Be careful not to mix OpenGL ES 1.x API calls with OpenGL ES 2.0 methods! 
// The two APIs are not interchangeable and trying to use them together only results 
// in frustration and sadness.
public class MyOpenGLES20 extends Activity implements SensorEventListener {
	
	public final float FACTOR = 5f;
	public MyGL20Renderer bball = new MyGL20Renderer();
    private float mLastX=0;
    private float mLastY=0;
    private float mLastZ=0;


    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private long lastUpdate;
    static final float ns_to_s = 1.0f / 1000000000.0f;
    long last_timestamp = 0;
    
	
	// a view that can draw and manipulate objects using OpenGL API
	private GLSurfaceView mGLView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Create a GLSurfaceView instance and set it
		// as the ContentView for this Activity
		//mGLView = new MyGLSurfaceView(this);
		mGLView = (MyGLSurfaceView)findViewById(R.id.glView);
		//setContentView(mGLView);
		setContentView(R.layout.activity_main);
		
		//create buttons
		final ImageButton aimUp = (ImageButton)findViewById(R.id.up_arrow);
		final ImageButton aimLeft = (ImageButton)findViewById(R.id.left_arrow);
		final ImageButton aimRight = (ImageButton)findViewById(R.id.right_arrow);
		final ImageButton aimDown = (ImageButton)findViewById(R.id.down_arrow);
		final ImageButton moveLeft = (ImageButton)findViewById(R.id.left_button);
		final ImageButton moveRight = (ImageButton)findViewById(R.id.right_button);
		
		//set onclick listeners for each button
		aimUp.setOnClickListener(new View.OnClickListener() {
	        @Override
	        public void onClick(View v) {
	        	bball.aim_up(FACTOR);
	        }
	    });
		
		aimLeft.setOnClickListener(new View.OnClickListener() {
	        @Override
	        public void onClick(View v) {
	        	bball.lookX -= FACTOR;
	        }
	    });
		
		aimRight.setOnClickListener(new View.OnClickListener() {
	        @Override
	        public void onClick(View v) {
	        	bball.aim_right(FACTOR);
	        }
	    });
		
		aimDown.setOnClickListener(new View.OnClickListener() {
	        @Override
	        public void onClick(View v) {
	        	bball.aim_down(FACTOR);
	        }
	    });
		
		moveLeft.setOnClickListener(new View.OnClickListener() {
	        @Override
	        public void onClick(View v) {
	        	bball.move_left(FACTOR);
	        }
	    });
		
		moveRight.setOnClickListener(new View.OnClickListener() {
	        @Override
	        public void onClick(View v) {
	        	bball.move_right(FACTOR);
	        }
	    });

        mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        lastUpdate = System.currentTimeMillis();
        
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

    


    protected void onCreate() {
      

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
        float velocityX = event.values[0];
        float velocityY = event.values[1];
        float velocityZ = event.values[2];
        float dt;



        float accelerationSquareRoot = (velocityX*velocityX + velocityY*velocityY + velocityZ*velocityZ) / (SensorManager.GRAVITY_EARTH * SensorManager.GRAVITY_EARTH);
        bball.ballspeed = accelerationSquareRoot;
        long actualTime = System.currentTimeMillis();
        if(accelerationSquareRoot >= 1.5)
        {
            if ((actualTime - lastUpdate) > 5000 )
            {
                lastUpdate = actualTime;
                if (true)
                {		//integrate to get Newtonian velocity
                    dt = (event.timestamp - last_timestamp) * ns_to_s;
                    bball.basketball.setVx(velocityX/2); // + mLastX) / (2*dt));
                    bball.basketball.setVy(velocityY/2);
                    bball.basketball.setVz(velocityZ/2);
                }
                else
                {
                    bball.basketball.setVx(0f);
                    bball.basketball.setVy(0f);
                    bball.basketball.setVz(0f);
                }
                mLastX = velocityX;
                mLastY = velocityY;
                mLastZ = velocityZ;
                Log.i("mLastX", String.valueOf(mLastX));
                Log.i("mLastY", String.valueOf(mLastY));
                Log.i("mLastZ", String.valueOf(mLastZ));


                last_timestamp = event.timestamp;
            }
        }

    }

//		@Override
//		public void onSensorChanged(SensorEvent arg0) {
//			// TODO Auto-generated method stub
//
//		}

}
