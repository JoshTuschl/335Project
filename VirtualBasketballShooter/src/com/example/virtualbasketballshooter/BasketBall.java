package com.example.virtualbasketballshooter;

public class BasketBall {

	private float RADIUS = 0.5f;
	private float BACKBOARD_DISTANCE = 20f;
	private float backboard_top = 5.3f;
	private float backboard_bottom = 4.7f;
	private float backboard_side = 0.75f;
	private float FLOOR = -0.5f;
	private final float GRAVITY = -9.81f;
	private float XMIN;
	private float XMAX;
	private float YMIN;
	private float YMAX;
	private float ZMIN;
	private float ZMAX;
	public float Vx = 0f;
	public float Vy = 0f;
	public float Vz = 0f;
	public float x = 0f;
	public float y = -3.0f;
	public float z = -20.0f;
	
	public void updateBall(float velocity)
	{
		x = x + Vx;
		y = y + Vy;
		z = z + Vz;
		
		if(check_rim_collision(x, y, z))
		{
			//handle_rim_collision();
		}
		if(check_backboard_collision(x, y, z))
		{
			handle_backboard_collision();
		}
		if(check_floor_collision(y))
		{
			handle_floor_collision();
		}
		
		check_bounds();
		
		Vx = Vx * 0.99f;
		Vy = Vy - GRAVITY;
		Vz = Vz * 0.99f;
	}
	
	
	//rim collision detection
	public boolean check_rim_collision(float x, float y, float z)
	{
		return false;
	}
	
	public boolean check_backboard_collision(float x, float y, float z)
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
	
	
	public boolean check_floor_collision(float y)
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
		Vx = -1*(Vx - (Vx*0.15f));	//reverse x velocity and subtract for elastic collision
		Vy = -1*(Vy - (Vy*0.15f));	//reverse y velocity and subtract for elastic collision
		Vz = -1*(Vz - (Vz*0.15f));	//reverse z velocity and subtract for elastic collision
	}
	
	
	private void handle_floor_collision()
	{
		Vx = Vx * 0.9f;				//slow x by 10% for collision
		Vy = -1*(Vy - (Vy*0.8f));  //reverse y direction, but only at 80% of velocity for elastic collision with floor.
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
			Vx = -1*(Vx - (Vx*0.15f));
			x = XMAX - RADIUS;
		}
		else if (x - RADIUS < XMIN) {
			Vx = -1*(Vx - (Vx*0.15f));
			x = XMIN + RADIUS;
		}
	    if (y + RADIUS > YMAX) {
	         Vy = -1*(Vy - (Vy*0.15f));
	         y = YMAX - RADIUS;
	    } 
	    else if (y - RADIUS < YMIN) {
	         Vy = -1*(Vy - (Vy*0.15f));
	         y = YMIN + RADIUS;
	    }
	    if (z + RADIUS > ZMAX) {
	         Vz = -1*(Vz - (Vy*0.15f));
	         z = ZMAX - RADIUS;
	    } 
	    else if (z - RADIUS < ZMIN) {
	         Vz = -1*(Vz - (Vz*0.15f));
	         z = ZMIN + RADIUS;
	    }
	}
	
}
