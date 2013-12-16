package com.example.virtualbasketballshooter;

/**
 * Created with IntelliJ IDEA.
 * User: Josh
 * Date: 12/16/13
 * Time: 3:53 PM
 * To change this template use File | Settings | File Templates.
 *
 * Borrowed from http://stackoverflow.com/questions/9355004/drawing-a-circle-in-android-with-opengl
 *
 */
public class Circle {
    public static float[] MakeCircle2d(float rad,int points,float x,float y)//x,y  ofsets
    {
        float[] verts=new float[points*2+2];
        boolean first=true;
        float fx=0;
        float fy=0;
        int c=0;
        for (int i = 0; i < points; i++)
        {
            float fi = (float) (2*3.14*i/points);
            float xa = (float) (rad*Math.sin(fi + 3.14)+x);
            float ya = (float) (rad*Math.cos(fi + 3.14)+y);
            if(first)
            {
                first=false;
                fx=xa;
                fy=ya;
            }
            verts[c]=xa;
            verts[c+1]=ya;
            c+=2;
        }
        verts[c]=fx;
        verts[c+1]=fy;
        return verts;
    }


}
