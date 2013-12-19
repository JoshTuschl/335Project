package com.example.virtualbasketballshooter;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.PointF;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
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

	public final float FACTOR = 2f;
	public MyGL20Renderer bball = new MyGL20Renderer();
    private float mLastX=0;
    private float mLastY=0;
    private float mLastZ=0;

    public static Context context;
    public static int speed = 0;
    public static float eyeX = 0f;
    public static float eyeY = 0f;
    public static float eyeZ = -1f;
    public static float lookX = 0f;
    public static float lookY = 0f;
    public static float lookZ = 0f;
    public static MediaPlayer mediaPlayer1;
    public static MediaPlayer mediaPlayer2;


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
        context = this.getApplicationContext();
        mediaPlayer1 = MediaPlayer.create(context, R.raw.applause);
        mediaPlayer2 = MediaPlayer.create(context, R.raw.crowdboo);
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
		final ImageButton speedUp = (ImageButton)findViewById(R.id.speed_up);
		final ImageButton slowDown = (ImageButton)findViewById(R.id.speed_down);
		
		//set onclick listeners for each button
		aimUp.setOnClickListener(new View.OnClickListener() {
	        @Override
	        public void onClick(View v) {
                lookY += .4f;


	        }
	    });
		
		aimLeft.setOnClickListener(new View.OnClickListener() {
	        @Override
	        public void onClick(View v) {
	        	lookX += 1f;
	        }
	    });
		
		aimRight.setOnClickListener(new View.OnClickListener() {
	        @Override
	        public void onClick(View v) {
                lookX -= 1f;
	        }
	    });
		
		aimDown.setOnClickListener(new View.OnClickListener() {
	        @Override
	        public void onClick(View v) {
                lookY -= .4f;
	        }
	    });
		
		moveLeft.setOnClickListener(new View.OnClickListener() {
	        @Override
	        public void onClick(View v) {
                eyeX += .1f;
                bball.basketball.setX(bball.basketball.getX() + .1f);
	        }
	    });
		
		moveRight.setOnClickListener(new View.OnClickListener() {
	        @Override
	        public void onClick(View v) {
                eyeX -= .1f;
                bball.basketball.setX(bball.basketball.getX() - .1f);
	        }
	    });
		
		speedUp.setOnClickListener(new View.OnClickListener() {
	        @Override
	        public void onClick(View v) {
                if(speed == 900)
                {
                	speed = 250;
                }
                else if(speed == 250)
                {
                	speed = 0;
                }
	        }
	    });
		
		slowDown.setOnClickListener(new View.OnClickListener() {
	        @Override
	        public void onClick(View v) {
                if(speed == 0)
                {
                	speed = 250;
                }
                else if (speed == 250)
                {
                	speed = 900;
                }
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
            if ((actualTime - lastUpdate) > 3000 )
            {
                lastUpdate = actualTime;
                bball.basketball.basket = false;
                bball.basketball.replay = false;
                bball.basketball.shotTaken = true;
                //integrate to get Newtonian velocity
                dt = (event.timestamp - last_timestamp) * ns_to_s;
                bball.basketball.setVx(0); //velocityX/2); // + mLastX) / (2*dt));
                float velocity = velocityY + velocityZ;
                bball.basketball.setVy(velocity/6);
                bball.basketball.setVz(velocity/8);


                bball.basketball.originalVy = velocity/6;
                bball.basketball.originalVz = velocity/8;


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
