package com.example.virtualbasketballshooter;

import android.app.Activity;
import android.graphics.PointF;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.GestureDetector.OnGestureListener;
import android.widget.ImageButton;

// Note: Be careful not to mix OpenGL ES 1.x API calls with OpenGL ES 2.0 methods! 
// The two APIs are not interchangeable and trying to use them together only results 
// in frustration and sadness.
public class MyOpenGLES20 extends Activity {
	
	public final float FACTOR = 5f;
	public MyGL20Renderer bball = new MyGL20Renderer();
	
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
	        	bball.aim_left(FACTOR);
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
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
