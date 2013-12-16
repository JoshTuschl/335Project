package com.example.virtualbasketballshooter;

public class BasketBall {

	private float RADIUS = 0.5f;
	private float BACKBOARD_DISTANCE = 20f;
	private float backboard_top = 5.3f;
	private float backboard_bottom = 4.7f;
	private float backboard_side = 0.75f;
	private float FLOOR = -0.5f;
	public float Vx;
	public float Vy;
	public float Vz;
	public float x;
	public float y;
	public float z;
	
	public void updateBall(float velocity)
	{
		x = x + x*Vx;
		y = y + y*Vy;
		z = z + z*Vz;
		
	//	if(basketball.check_rim_collision(x, y, z))
	//	{
	//		//basketball.handle_rim_collision();
	//	}
	//	if(basketball.check_backboard_collision(x, y, z))
	//	{
	//		//basketball.handle_backboard_collision();
	//	}
	//	if(basketball.check_floor_collision(y))
	//	{
	//		//basketball.handle_floor_collision();
	//	}
	}
	
	
	//rim collision detection
	public boolean check_rim_collision(float x, float y, float z)
	{
		return false;
	}
	
	public boolean check_backboard_collision(float x, float y, float z)
	{
		//backboard collision detectoin
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
	
	//         velocityX = -1*(velocityX - (float)ka);
	//         x = Xmax - RADIUS;
	//    } else if (x - RADIUS < Xmin) {
	//         velocityX = -1* (velocityX - (float)ka);
	//         x = Xmin + RADIUS;
	//    }
	//    if (y + RADIUS > Ymax) {
	//         velocityY = -1*(velocityY - (float)ka);
	//         y = Ymax - RADIUS;
	//    } else if (y - RADIUS < Ymin) {
	//         velocityY = -1*(velocityY - (float)ka);
	//         y = Ymin + RADIUS;
	//    }
		}
	
	
		public boolean check_floor_collision(float y)
		{
			if(y < FLOOR + 0.01)
			{
				return true;
			}
			return false;
		}
	
	
	
			
	
	
	
}
