package com.example.virtualbasketballshooter;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.Menu;

// Note: Be careful not to mix OpenGL ES 1.x API calls with OpenGL ES 2.0 methods! 
// The two APIs are not interchangeable and trying to use them together only results 
// in frustration and sadness.
public class MyOpenGLES20 extends Activity {
	
	// a view that can draw and manipulate objects using OpenGL API
	private GLSurfaceView mGLView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Create a GLSurfaceView instance and set it
		// as the ContentView for this Activity
		//mGLView = new MyGLSurfaceView(this);
		mGLView = (MyGLSurfaceView)findViewById(R.id.glView);
		
		setContentView(mGLView);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
