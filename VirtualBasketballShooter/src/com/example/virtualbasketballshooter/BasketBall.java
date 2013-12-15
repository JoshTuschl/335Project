package com.example.virtualbasketballshooter;

public class BasketBall {

	private float RADIUS = (float)0.5;
	private float BACKBOARD_DISTANCE = 20;
	private float backboard_top = (float)5.3;
	private float backboard_bottom = (float)4.7;
	private float backboard_side = (float)0.75;
	
	//rim collision detection
	public boolean check_rim_collision(float x, float y, float z)
	{
		
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
			
			
		
	
	
	
			
	
	
	
}
