package com.example.virtualbasketballshooter;

import android.util.Log;

public class BasketBall {

	private float RADIUS = 0.5f;
	private float BACKBOARD_DISTANCE = 20f;
	private float backboard_top = 4f;
	private float backboard_bottom = -4f;
	private float backboard_side = 2.25f;
	private float rim_height = 1.8f;
	private float rim_middle = 25f;
	private float rim_radius = 1.5f;
	private float FLOOR = -0.5f;
	private final float GRAVITY = -3.5f;
	private float XMIN = -12f;
	private float XMAX = 12f;
	private float YMIN = -1.5f;
	private float YMAX = 25f;
	private float ZMIN = -24f;
	private float ZMAX = 24f;
	public float Vx = 0f;
	public float Vy = 0f;
	public float Vz = 0f;
	public float x = 0f;
	public float y = -0.5f;
	public float z = 1.75f;

    public float getVx() {
        return Vx;
    }

    public void setVx(float vx) {
        Vx = vx;
    }

    public float getVy() {
        return Vy;
    }

    public void setVy(float vy) {
        Vy = vy;
    }

    public float getVz() {
        return Vz;
    }

    public void setVz(float vz) {
        Vz = vz;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getZ() {
        return z;
    }

    public void setZ(float z) {
        this.z = z;
    }

    public void updateBall()
	{
		if (Vx > .1) {
            x += Vx;
        }
        if (Vy > .1) {
            y += Vy + GRAVITY;
        }
        if (Vz > .1) {
            z += Vz;
        }

        if (Vy > 0)   {

            Vy += GRAVITY;
        }

        Log.i("Basketball VX", String.valueOf(Vx));
        Log.i("Basketball VY", String.valueOf(Vy));
        Log.i("Basketball VZ", String.valueOf(Vz));
        
        if(Math.abs(z) > 0.01)
        {
        	if(check_rim_collision())
        	{
        		//handle_rim_collision();
        	}
        	if(check_backboard_collision())
        	{
        		handle_backboard_collision();
        	}
//        	if(check_floor_collision())
//        	{
//        		handle_floor_collision();
//        	}
		
        	check_bounds();
        }
		
//		if(Math.abs(Vx-0f) > 0.5f)
//		{
//			Vx = Vx * 0.45f;
//		}
//		else
//		{
//			if(Math.abs(y) < 0.8f)
//			{
//				Vx = 0.0f;
//			}
//		}
		if(Math.abs(Vy-0f) > 0.05f || y > 0)
        {
			Vy = Vy >0 ? Vy + GRAVITY : (-Vy * .8f);
		}
		else
		{

				Vy = 0.0f;
				y = RADIUS;

		}
		if(Math.abs(Vz-0f) > 0.05f)
		{
			Vz = Vz * 0.45f;
		}
		else
		{
		    Vz = 0;
		}
	}
	
	
	//rim collision detection
	public boolean check_rim_collision()
	{
		return false;
	}
	
	public boolean check_backboard_collision()
	{
		//backboard collision detection
		if ((z + RADIUS) > BACKBOARD_DISTANCE) 
		{
			if((y + RADIUS) < backboard_top && (y - RADIUS) > backboard_bottom )
			{
				if((x + RADIUS) < backboard_side && (x - RADIUS) > -backboard_side )
				{
					return true;
				}
			}
		}
		return false;
	}
	
	
	public boolean check_floor_collision()
	{
		if((y-RADIUS) < FLOOR + 0.01)
		{
			return true;
		}
		return false;
	}
	
	private void handle_rim_collision()
	{
		
		return;
	}
	
	private void handle_backboard_collision()
	{
		Vx = -1*(Vx*0.85f);	//reverse x velocity and subtract for elastic collision
		Vy = -1*(Vy*0.85f);	//reverse y velocity and subtract for elastic collision
		Vz = -1*(Vz*0.85f);	//reverse z velocity and subtract for elastic collision
	}
	
	private void handle_floor_collision()
	{
		Vx = Vx * 0.9f;				//slow x by 10% for collision
		Vy = -1*(Vy*0.8f);  //reverse y direction, but only at 80% of velocity for elastic collision with floor.
		Vz = Vz * 0.9f;				//slow z by 10% for collision
//		if(Vx < 0.000001)
//		{
//			Vx = 0;
//		}
	}
	
	public void check_bounds()
	{
		//bounds test
		if(x + RADIUS > XMAX) {
			Vx = -1*(Vx*0.85f);
			x = XMAX - RADIUS;
		}
		else if (x - RADIUS < XMIN) {
			Vx = -1*(Vx*0.85f);
			x = XMIN + RADIUS;
		}
	    if (y + RADIUS > YMAX) {
	         Vy = -1*(Vy*0.85f);
	         y = YMAX - RADIUS;
	    } 
	    else if (y - RADIUS < YMIN) {
	         Vy = -1*(Vy*0.85f);
	         y = YMIN + RADIUS;
	    }
	    if (z + RADIUS > ZMAX) {
	         Vz = -1*(Vz*0.85f);
	         z = ZMAX - RADIUS;
	    } 
	    else if (z - RADIUS < ZMIN) {
	         Vz = -1*(Vz*0.85f);
	         z = ZMIN + RADIUS;
	    }
	}
	
}
