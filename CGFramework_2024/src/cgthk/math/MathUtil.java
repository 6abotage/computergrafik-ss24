/* 
 * Cologne University of Applied Sciences
 * Institute for Media and Imaging Technologies - Computer Graphics Group
 *
 * Copyright (c) 2013 Cologne University of Applied Sciences. All rights reserved.
 *
 * This source code is property of the Cologne University of Applied Sciences. Any redistribution
 * and use in source and binary forms, with or without modification, requires explicit permission. 
 */


package cgthk.math;

public class MathUtil
{
	public static final float PI        = (float)Math.PI;
	public static final float PI_HALF   = (float)(Math.PI * 0.5);
	public static final float PI_DOUBLE = (float)(Math.PI * 2);
	
    
	public static float clamp( float value, float min, float max )
	{
		float clamped = min > value ? min : value;
		return max < clamped ? max : clamped;
	}
	
	
	public static int clamp( int value, int min, int max )
	{
		int clamped = min > value ? min : value;
		return max < clamped ? max : clamped;
	}

	
	public static float toRad( float degrees )
	{
		return PI * degrees / 180.0f;
	}
	
	
	public static float toDeg( float radians )
	{
		return 180.0f * radians / PI;
	}
}
